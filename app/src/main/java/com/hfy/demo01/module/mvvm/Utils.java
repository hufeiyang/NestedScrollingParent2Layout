package com.hfy.demo01.module.mvvm;

import androidx.databinding.BindingConversion;

import com.hfy.demo01.module.mvvm.bean.Man;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {
    public static String getName(Man man) {
        return man.getName();
    }

    /**
     * convertDate（）这个方法在哪个类不重要，重要的是 @BindingConversion
     * @param date
     * @return
     */
    @BindingConversion
    public static String convertDate(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String format = simpleDateFormat.format(date);
        return format;
    }
}
