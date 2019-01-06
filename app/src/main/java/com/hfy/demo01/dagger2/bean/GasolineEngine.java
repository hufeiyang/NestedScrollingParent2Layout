package com.hfy.demo01.dagger2.bean;

import android.util.Log;

public class GasolineEngine extends Engine {
    private static final String TAG = "hfy";

    @Override
    public void work() {
        Log.i(TAG, "work: GasolineEngine");
    }
}
