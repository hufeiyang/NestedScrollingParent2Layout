package com.hfy.demo01.common.customview;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.hfy.demo01.R;

/**
 * @author hufy
 * @apiNote 自定义toast
 * @date 2019/5/27 21:33
 */
public class MyToast {

    private static Toast toast;

    public static void showMsg(Activity context, String msg) {

        LayoutInflater inflater = context.getLayoutInflater();
        View layout = inflater.inflate(R.layout.custom_toast, null);

        TextView text = (TextView) layout.findViewById(R.id.text);
        text.setText(msg);

//        if (toast == null){
//        }
        toast = new Toast(context);
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();

    }
}
