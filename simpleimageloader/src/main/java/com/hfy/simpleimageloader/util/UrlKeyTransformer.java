package com.hfy.simpleimageloader.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 图片的url转成key
 * @author hufeiyang
 */
public class UrlKeyTransformer {

    /**
     * 图片的url转成key
     * @param url
     * @return MD5转换后的key
     */
    public static String transform(String url) {
        if (url == null || url.isEmpty()) {
            return null;
        }
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            return byteToHexString(digest.digest(url.getBytes()));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String byteToHexString(byte[] bytes) {
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
}
