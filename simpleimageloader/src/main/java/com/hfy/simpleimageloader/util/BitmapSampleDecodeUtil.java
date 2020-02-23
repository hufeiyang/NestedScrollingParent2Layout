package com.hfy.simpleimageloader.util;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.FileDescriptor;

/**
 * Bitmap采样压缩加载工具
 * @author hufeiyang
 */
public class BitmapSampleDecodeUtil {

    private static final String TAG = "BitmapSampleDecodeUtil";

    /**
     * 对资源图片的采样
     * @param resources resources
     * @param resourcesId 资源id
     * @param requestWidth view的宽
     * @param requestHeight view的高
     * @return 采样后的bitmap
     */
    public static Bitmap decodeSampleResources(Resources resources, int resourcesId, int requestWidth, int requestHeight){
        if (resources == null || resourcesId<=0) {
            return null;
        }

        //1、inJustDecodeBounds设为true，并加载图片
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(resources, resourcesId, options);

        //2、获取原始宽高信息
        int outWidth = options.outWidth;
        int outHeight = options.outHeight;

        //3、原始宽高信息 和 view的大小 计算并设置采样率
        options.inSampleSize = getInSampleSize(requestWidth, requestHeight, outWidth, outHeight);

        //4、inJustDecodeBounds设为false，并加载图片
        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeResource(resources, resourcesId, options);
    }

    /**
     * 对文件描述符的采样加载
     * @param fileDescriptor fileDescriptor
     * @param requestWidth view的宽
     * @param requestHeight view的高
     * 注意，文件输入流是有序的文件流，采样时两次decodeStream影响文件流的文职属性，导致第二次decode是获取是null。
     * 为解决此问题，可用本方法对文件流的文件描述符 加载。
     */
    public static Bitmap decodeFileDescriptor(FileDescriptor fileDescriptor, int requestWidth, int requestHeight){

        if (fileDescriptor == null) {
            return null;
        }

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds=true;
        BitmapFactory.decodeFileDescriptor(fileDescriptor,null,options);
        options.inSampleSize = getInSampleSize(requestWidth, requestHeight, options.outWidth, options.outHeight);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFileDescriptor(fileDescriptor, null, options);
    }

    /**
     * 计算采样率
     * @param width view的宽
     * @param height view的高
     * @param outWidth 图片原始的宽
     * @param outHeight 图片原始的高
     * @return
     */
    private static int getInSampleSize(int width, int height, int outWidth, int outHeight) {
        int inSampleSize = 1;

        if (width==0 || height ==0){
            return inSampleSize;
        }

        if (outWidth>width || outHeight>height){
            int halfWidth = outWidth / 2;
            int halfHeight = outHeight / 2;
            //保证采样后的宽高都不小于目标快高，否则会拉伸而模糊
            while (halfWidth/inSampleSize >=width
                    && halfHeight/inSampleSize>=height){
                inSampleSize *=2;
            }
        }

        Log.d(TAG, "getInSampleSize: inSampleSize="+inSampleSize);
        return inSampleSize;
    }
}
