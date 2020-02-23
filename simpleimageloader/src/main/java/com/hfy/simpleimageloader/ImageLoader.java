package com.hfy.simpleimageloader;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.util.LruCache;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.hfy.simpleimageloader.util.BitmapSampleDecodeUtil;
import com.hfy.simpleimageloader.util.IoUtil;
import com.hfy.simpleimageloader.util.UrlKeyTransformer;
import com.jakewharton.disklrucache.DiskLruCache;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 图片加载器，功能点如下：
 *  - 图片压缩，就是采样加载
 *  - 内存缓存，LruCache
 *  - 磁盘缓存，DiskLruCache
 *  - 网络获取，请求网络url
 *  - 同步加载，外部子线程同步执行
 *  - 异步加载，ImageLoader内部线程异步执行
 * @author hufeiyang
 */
public class ImageLoader {

    private static final String TAG = "ImageLoader";

    private static final long KEEP_ALIVE_TIME = 10L;

    private static final int CPU_COUNT =  Runtime.getRuntime().availableProcessors();

    private static final int CORE_THREAD_SIZE = CPU_COUNT + 1;

    private static final int THREAD_SIZE = CPU_COUNT * 2 + 1;

    private static final int VIEW_TAG_URL = R.id.view_tag_url;

    private static final Object object = new Object();


    private ThreadPoolExecutor mExecutor;

    private Handler mMainHandler;


    private Context mApplicationContext;

    private static volatile ImageLoader mImageLoader;

    private LruCache<String, Bitmap> mLruCache;

    private DiskLruCache mDiskLruCache;

    /**
     * 磁盘缓存最大容量,50M
     */
    private static final long DISK_LRU_CACHE_MAX_SIZE = 50 * 1024 *1024;

    /**
     * 当前进程的最大内存，取进程内存的1/8
     */
    private static final long MEMORY_CACHE_MAX_SIZE = Runtime.getRuntime().maxMemory() / 8;


    public ImageLoader(Context context) {
        if (context == null) {
            throw new RuntimeException("context can not be null !");
        }
        mApplicationContext = context.getApplicationContext();

        initLruCache();
        initDiskLruCache();
        initAsyncLoad();
    }

    public static ImageLoader with(Context context){
        if (mImageLoader == null) {
            synchronized (object) {
                if (mImageLoader == null) {
                    mImageLoader = new ImageLoader(context);
                }
            }
        }
        return mImageLoader;
    }

    private void initAsyncLoad() {
        mExecutor = new ThreadPoolExecutor(CORE_THREAD_SIZE, THREAD_SIZE,
                KEEP_ALIVE_TIME, TimeUnit.SECONDS,
                new LinkedBlockingQueue<Runnable>(), new ThreadFactory() {
            private final AtomicInteger count = new AtomicInteger(1);
            @Override
            public Thread newThread(Runnable runnable) {
                return new Thread(runnable, "load bitmap thread "+ count.getAndIncrement());
            }
        });

        mMainHandler = new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(@NonNull Message msg) {
                LoadResult result = (LoadResult) msg.obj;
                ImageView imageView = result.imageView;
                Bitmap bitmap = result.bitmap;
                String url = result.url;
                if (imageView == null || bitmap == null) {
                    return;
                }

                //此判断是 避免 ImageView在列表中复用导致图片错位的问题
                if (url.equals(imageView.getTag(VIEW_TAG_URL))) {
                    imageView.setImageBitmap(bitmap);
                }else {
                    Log.w(TAG, "handleMessage: set image bitmap，but url has changed,ignore!");
                }
            }
        };
    }

    private void initLruCache() {

        mLruCache = new LruCache<String, Bitmap>((int) MEMORY_CACHE_MAX_SIZE){
            @Override
            protected int sizeOf(String key, Bitmap value) {
                //缓存对象bitmap的大小，单位要和MEMORY_CACHE_MAX_SIZE一致，M
                return value.getByteCount();
            }

            @Override
            protected void entryRemoved(boolean evicted, String key, Bitmap oldValue, Bitmap newValue) {
                //移除旧缓存时会调用，可以在这里进行像资源回收的工作。
            }
        };

    }

    private void initDiskLruCache() {

        File externalCacheDir = mApplicationContext.getExternalCacheDir();
        if (externalCacheDir != null) {
            long usableSpace = externalCacheDir.getUsableSpace();
            if (usableSpace < DISK_LRU_CACHE_MAX_SIZE){
                //剩余空间不够了
                Log.e(TAG, "initDiskLruCache: "+"UsableSpace="+usableSpace+" , not enough(target 50M)，cannot creat diskLruCache！");
                return;
            }
        }

        //一、创建DiskLruCache
            //第一个参数是要存放的目录，这里选择外部缓存目录（若app卸载此目录也会删除）；
            //第二个是版本一般设1；第三个是缓存节点的value数量一般也是1；
            //第四个是最大缓存容量这里取50M
        try {
            this.mDiskLruCache = DiskLruCache.open(mApplicationContext.getExternalCacheDir(), 1, 1, DISK_LRU_CACHE_MAX_SIZE);
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "initDiskLruCache: "+e.getMessage());
        }
    }

    /**
     * 缓存bitmap到内存
     * @param url url
     * @param bitmap bitmap
     */
    private void addBitmapMemoryCache(String url, Bitmap bitmap) {
        String key = UrlKeyTransformer.transform(url);
        if (mLruCache.get(key) == null && bitmap != null) {
            mLruCache.put(key,bitmap);
        }
    }

    /**
     * 从内存缓存加载bitmap
     * @param url url
     * @return
     */
    private Bitmap loadFromMemoryCache(String url) {
        return mLruCache.get(UrlKeyTransformer.transform(url));
    }


    /**
     * 从磁盘缓存加载bitmap（并添加到内存缓存）
     * @param url url
     * @param requestWidth 要求的宽
     * @param requestHeight 要求的高
     * @return bitmap
     */
    private Bitmap loadFromDiskCache(String url, int requestWidth, int requestHeight) throws IOException {
        if (Looper.myLooper()==Looper.getMainLooper()) {
            Log.w(TAG, "loadFromDiskCache from Main Thread may cause block ！");
        }

        if (mDiskLruCache == null) {
            return null;
        }
        DiskLruCache.Snapshot snapshot = null;
        String key = UrlKeyTransformer.transform(url);
        snapshot = mDiskLruCache.get(key);
        if (snapshot != null) {
            FileInputStream inputStream = (FileInputStream)snapshot.getInputStream(0);

            //Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

            //一般需要采样加载，但文件输入流是有序的文件流，采样时两次decodeStream影响文件流的位置属性，
            //导致第二次decode是获取是null，为解决此问题，可用文件描述符。
            FileDescriptor fd = inputStream.getFD();
            Bitmap bitmap = BitmapSampleDecodeUtil.decodeFileDescriptor(fd, requestWidth, requestHeight);
            addBitmapMemoryCache(url,bitmap);
            return bitmap;
        }

        return null;
    }


    /**
     * 从网路加载图片 到磁盘缓存（然后再从磁盘中采样加载）
     * @param urlString urlString
     * @param requestWidth 要求的宽
     * @param requestHeight 要求的高
     * @return Bitmap
     */
    private Bitmap loadFromHttp(String urlString, int requestWidth, int requestHeight) throws IOException {
        //线程检查，不能是主线程
        if (Looper.myLooper()==Looper.getMainLooper()) {
            throw new RuntimeException("Do not loadFromHttp from Main Thread!");
        }

        if (mDiskLruCache == null) {
            return null;
        }

        DiskLruCache.Editor editor = null;
        editor = mDiskLruCache.edit(UrlKeyTransformer.transform(urlString));
        if (editor != null) {
            OutputStream outputStream = editor.newOutputStream(0);
            if (downloadBitmapToStreamFromHttp(urlString, outputStream)) {
                editor.commit();
            }else {
                editor.abort();
            }
            mDiskLruCache.flush();
        }

        return loadFromDiskCache(urlString, requestWidth, requestHeight);
    }

    /**
     * 从网络下载图片到文件输入流
     * @param urlString
     * @param outputStream
     * @return
     */
    private boolean downloadBitmapToStreamFromHttp(String urlString, OutputStream outputStream) {
        URL url = null;
        HttpURLConnection urlConnection = null;
        BufferedInputStream in = null;
        BufferedOutputStream out = null;
        try {
            url = new URL(urlString);
            urlConnection = (HttpURLConnection) url.openConnection();
            in = new BufferedInputStream(urlConnection.getInputStream());
            out = new BufferedOutputStream(outputStream);

            int b;
            while ((b=in.read()) != -1) {
                //写入文件输入流
                out.write(b);
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "downloadBitmapToStreamFromHttp,failed : "+e.getMessage());
        }finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            IoUtil.close(in);
            IoUtil.close(out);
        }
        return false;
    }

    /**
     * 从网络直接下载bitmap（无缓存、无采样）
     * @param urlString
     * @return
     */
    private Bitmap downloadBitmapFromUrlDirectly(String urlString) {
        URL url;
        HttpURLConnection urlConnection = null;
        BufferedInputStream bufferedInputStream = null;
        try {
            url = new URL(urlString);
            urlConnection = (HttpURLConnection) url.openConnection();
            bufferedInputStream = new BufferedInputStream(urlConnection.getInputStream());
            return BitmapFactory.decodeStream(bufferedInputStream);
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "downloadBitmapFromUrlDirectly,failed : "+e.getMessage());
        }finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            IoUtil.close(bufferedInputStream);
        }
        return null;
    }

    public Bitmap loadBitmap(String url){
        return loadBitmap(url,0,0);
    }

    /**
     * 同步 加载bitmap
     *
     * 不能在主线程执行。加载时 先从内存缓存获取，有就返回bitmap，若没有就从磁盘缓存获取；
     * 磁盘缓存有就返回bitmap并缓存到内存缓存，没有就请求网络；
     * 网络请求回来，就缓存到磁盘缓存，然后从磁盘缓存获取返回。
     *
     * @return Bitmap
     */
    public Bitmap loadBitmap(String url, int requestWidth, int requestHeight){

        Bitmap bitmap = loadFromMemoryCache(url);
        if (bitmap != null) {
            Log.d(TAG, "loadBitmap: loadFromMemoryCache, url:"+url);
            return bitmap;
        }

        try {
            bitmap = loadFromDiskCache(url, requestWidth, requestHeight);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (bitmap != null) {
            Log.d(TAG, "loadBitmap: loadFromDiskCache, url:"+url);
            return bitmap;
        }

        try {
            bitmap = loadFromHttp(url, requestWidth, requestHeight);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (bitmap != null){
            Log.d(TAG, "loadBitmap: loadFromHttp, url:"+url);
            return bitmap;
        }

        if (mDiskLruCache == null) {
            Log.d(TAG, "loadBitmap: diskLruCache is null，load bitmap from url directly！");
            bitmap = downloadBitmapFromUrlDirectly(url);
        }

        return bitmap;
    }

    public void loadBitmapAsync(final String url, final ImageView imageView){
        loadBitmapAsync(url,imageView,0,0);
    }

    /**
     * 异步 加载bitmap
     * 外部可在任意线程执行，因为内部实现是在子线程(线程池)加载，
     * 并且内部会通过Handler切到主线程，只需要传入view，内部就可直接绘制Bitmap到view。
     * @param url
     * @param imageView
     * @param requestWidth
     * @param requestHeight
     */
    public void loadBitmapAsync(final String url, final ImageView imageView, final int requestWidth, final int requestHeight){
        if (url == null || url.isEmpty() || imageView == null) {
            return;
        }

        // 标记当前imageView要绘制图片的url
        imageView.setTag(VIEW_TAG_URL, url);

        mExecutor.execute(new Runnable() {
            @Override
            public void run() {

                Bitmap loadBitmap = loadBitmap(url, requestWidth, requestHeight);

                Message message = Message.obtain();
                message.obj = new LoadResult(loadBitmap, url, imageView);
                mMainHandler.sendMessage(message);
            }
        });
    }

}
