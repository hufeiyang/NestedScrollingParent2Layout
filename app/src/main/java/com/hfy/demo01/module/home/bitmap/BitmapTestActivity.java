package com.hfy.demo01.module.home.bitmap;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.HandlerThread;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.LruCache;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.hfy.demo01.R;
import com.jakewharton.disklrucache.DiskLruCache;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * bitmap 的加载与Cache
 * @author hufeiyang
 */
public class BitmapTestActivity extends AppCompatActivity {

    private final String TAG = "BitmapTestActivity";

    @BindView(R.id.iv_bitmap_test)
    ImageView mIvBitamp;
    private LruCache<String, Bitmap> mBitmapLruCache;
    private Bitmap mBitmap;
    private DiskLruCache mDiskLruCache;
    private Handler mWorkHandler;

    public static void launch(Activity activity) {
        Intent intent = new Intent(activity, BitmapTestActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bitmap_test);
        ButterKnife.bind(this);

        initView();

        testLruCache();

        String urlString = "http://a1.att.hudong.com/05/00/01300000194285122188000535877.jpg";
        HandlerThread thread = new HandlerThread("testDiskLruCache");
        thread.start();
        mWorkHandler = new Handler(thread.getLooper());
        mWorkHandler.post(new Runnable() {
            @Override
            public void run() {
                testDiskLruCache(urlString);
            }
        });
    }

    /**
     * DiskLruCache使用
     * @param urlString
     */
    private void testDiskLruCache(String urlString) {
        long maxSize = 50*1024*1024;

        try {
            //一、创建DiskLruCache
            //第一个参数是要存放的目录，这里选择外部缓存目录（若app卸载此目录也会删除）；
            //第二个是版本一般设1；第三个是缓存节点的value数量一般也是1；
            //第四个是最大缓存容量这里取50M
            mDiskLruCache = DiskLruCache.open(getExternalCacheDir(), 1, 1, maxSize);

            //二、缓存的添加：1、通过Editor，把图片的url转成key，通过edit方法得到editor,然后获取输出流，就可以写到文件系统了。
            DiskLruCache.Editor editor = mDiskLruCache.edit(hashKeyFormUrl(urlString));
            if (editor != null) {
                //参数index取0（因为上面的valueCount取的1）
                OutputStream outputStream = editor.newOutputStream(0);
                boolean downSuccess = downloadPictureToStream(urlString, outputStream);
                if (downSuccess) {
                    //2、编辑提交，释放编辑器
                    editor.commit();
                }else {
                    editor.abort();
                }
                //3、写到文件系统，会检查当前缓存大小，然后写到文件
                mDiskLruCache.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        //三、缓存查找
        try {
            String key = hashKeyFormUrl(urlString);
            DiskLruCache.Snapshot snapshot = mDiskLruCache.get(key);
            if (snapshot == null) {
                return;
            }
            FileInputStream inputStream = (FileInputStream)snapshot.getInputStream(0);

//            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
//            mIvBitamp.setImageBitmap(bitmap);

            //注意，一般需要采样加载，但文件输入流是有序的文件流，采样时两次decodeStream影响文件流的文职属性，导致第二次decode是获取是null
            //为解决此问题，可用文件描述符
            FileDescriptor fd = inputStream.getFD();

            //采样加载（就是前面讲的bitmap的高效加载）
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds=true;
            BitmapFactory.decodeFileDescriptor(fd,null,options);
            ViewGroup.LayoutParams layoutParams = mIvBitamp.getLayoutParams();
            options.inSampleSize = getInSampleSize(layoutParams.width, layoutParams.height, options.outWidth, options.outHeight);
            options.inJustDecodeBounds = false;
            Bitmap bitmap = BitmapFactory.decodeFileDescriptor(fd, null, options);

            //存入内容缓存，绘制到view。（下次先从内存缓存获取，没有就从磁盘缓存获取，在没有就请求网络--"三级缓存"）
            mBitmapLruCache.put(key,bitmap);

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mIvBitamp.setImageBitmap(mBitmapLruCache.get(key));
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 下载图片到文件输入流
     * @param urlString
     * @param outputStream
     * @return
     */
    private boolean downloadPictureToStream(String urlString, OutputStream outputStream) {
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
        }finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            try {
                if (in != null) {
                    in.close();
                }
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 图片的url转成key
     * @param url
     * @return MD5转换后的key
     */
    private String hashKeyFormUrl(String url) {
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            return byteToHexString(digest.digest(url.getBytes()));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String byteToHexString(byte[] bytes) {
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(0XFF & bytes[i]);
            if (hex.length()==1) {
                stringBuffer.append(0);
            }
            stringBuffer.append(hex);
        }
        return stringBuffer.toString();
    }

    private void testLruCache() {
        //当前进程的最大内存，单位M
        long maxMemory = Runtime.getRuntime().maxMemory() / 1024 / 1024;
        //取进程内存的1/8
        int cacheMaxSize = (int) (maxMemory/8);
        mBitmapLruCache = new LruCache<String, Bitmap>(cacheMaxSize){
            @Override
            protected int sizeOf(String key, Bitmap value) {
                //缓存对象bitmap的大小，单位M
                return value.getByteCount()/1024/1024;
            }

            @Override
            protected void entryRemoved(boolean evicted, String key, Bitmap oldValue, Bitmap newValue) {
                //移除旧缓存时会调用，可以在这里进行像资源回收的工作。
            }
        };

        //添加缓存
        mBitmapLruCache.put("1",mBitmap);

        //获取缓存
        Bitmap bitmap = mBitmapLruCache.get("1");
        mIvBitamp.setImageBitmap(bitmap);

        //删除缓存，一般不会用，因为快满时会自动删近期最少使用的缓存，就是它的核心功能
        mBitmapLruCache.remove("1");
    }

    private void initView() {
        //R.mipmap.blue放在Res的xxh（480dpi）中，测试手机dpi也是480

        //1、inJustDecodeBounds设为true，并加载图片
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(getResources(), R.mipmap.blue, options);

        //2、获取原始宽高信息
        int outWidth = options.outWidth;
        int outHeight = options.outHeight;

        Log.i(TAG, "initView: outWidth="+outWidth+"，outHeight="+outHeight);

        //3、原始宽高信息 和 view的大小 计算并设置采样率
        ViewGroup.LayoutParams layoutParams = mIvBitamp.getLayoutParams();
        int inSampleSize = getInSampleSize(layoutParams.width, layoutParams.height, outWidth, outHeight);
        options.inSampleSize = inSampleSize;

        Log.i(TAG, "initView: inSampleSize="+options.inSampleSize);

        //4、inJustDecodeBounds设为false，并加载图片
        options.inJustDecodeBounds = false;
        mBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.blue, options);

        Log.i(TAG, "initView: size="+ mBitmap.getByteCount());

        int density = mBitmap.getDensity();
        Log.i(TAG, "initView: density="+density);
        Log.i(TAG, "initView: original size="+337*222*4);
        Log.i(TAG, "initView: calculated size="+ (337/inSampleSize) *(222/inSampleSize)* density/480 *4);


        //绘制到view
        mIvBitamp.setImageBitmap(mBitmap);
    }

    /**
     * 计算采样率
     * @param width view的宽
     * @param height view的高
     * @param outWidth 图片原始的宽
     * @param outHeight 图片原始的高
     * @return
     */
    private int getInSampleSize(int width, int height, int outWidth, int outHeight) {
        int inSampleSize = 1;
        if (outWidth>width || outHeight>height){
            int halfWidth = outWidth / 2;
            int halfHeight = outHeight / 2;
            //保证采样后的宽高都不小于目标快高，否则会拉伸而模糊
            while (halfWidth/inSampleSize >=width
                    && halfHeight/inSampleSize>=height){
                inSampleSize *=2;
            }
        }

        return inSampleSize;
    }
}
