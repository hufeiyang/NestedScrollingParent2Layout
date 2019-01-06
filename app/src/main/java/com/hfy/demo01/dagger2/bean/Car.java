package com.hfy.demo01.dagger2.bean;

import android.util.Log;

import com.hfy.demo01.dagger2.annotation.Color;

import javax.inject.Inject;
import javax.inject.Named;

public class Car {

    private static final String TAG = "hfy";

    private Engine engine;

    @Inject
    @Named("名字")
    public String name;

    @Inject
    @Color
    public String color;

    @Inject
    public Car(@Named("DieselEngine") Engine engine) {
        this.engine = engine;
    }

    public void run(){
        engine.work();

        Log.i(TAG, "run: "+ toString());
    }

    @Override
    public String toString() {
        return "Car{" +
                "engine=" + engine +
                ", name='" + name + '\'' +
                ", color='" + color + '\'' +
                '}';
    }
}
