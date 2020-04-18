package com.hfy.nestedscrolling.dagger2.bean;

import android.util.Log;

public class DieselEngine extends Engine {
    private static final String TAG = "hfy";

    @Override
    public void work() {
        Log.i(TAG, "work: DieselEngine");
    }
}
