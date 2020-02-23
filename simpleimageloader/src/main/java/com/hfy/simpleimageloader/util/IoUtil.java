package com.hfy.simpleimageloader.util;

import android.util.Log;

import java.io.Closeable;
import java.io.IOException;

/**
 * @author hufeiyang
 */
public class IoUtil {

    private static final String TAG = "IoUtil";

    public static void close(Closeable closeable) {
        try {
            if (closeable != null) {
                closeable.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.w(TAG, "close: "+e.getMessage());
        }
    }
}
