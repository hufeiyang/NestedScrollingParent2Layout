package com.hfy.demo01.dagger2.bean;

import android.util.Log;

import javax.inject.Inject;

public class Watch {

    private static final String TAG = "hfy";

    @Inject
    public Watch(Battery battery) {
    }

    public void work(){
        Log.i(TAG, "work: 手表工作");
    }
}
