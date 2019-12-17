package com.hfy.demo01.module.home.leaktest;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.hfy.demo01.R;

public class LeakTestActivity extends AppCompatActivity {

    private static final String TAG = "LeakTestActivity";

    public static void launch(Activity activity) {
        Intent intent = new Intent(activity, LeakTestActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leak_test);

        String name = FYManager.getInstance(this).getName();
        Log.d(TAG, "onCreate: " + name);
    }
}
