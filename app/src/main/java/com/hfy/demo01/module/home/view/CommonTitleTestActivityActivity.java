package com.hfy.demo01.module.home.view;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.hfy.demo01.R;

public class CommonTitleTestActivityActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common_title_test_activity);
    }

    public static void launch(Activity activity) {
        Intent intent = new Intent(activity, CommonTitleTestActivityActivity.class);
        activity.startActivity(intent);
    }
}
