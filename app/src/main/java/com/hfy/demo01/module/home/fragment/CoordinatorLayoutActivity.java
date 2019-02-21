package com.hfy.demo01.module.home.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;

import com.hfy.demo01.R;

/**
 * @author hufy
 */
public class CoordinatorLayoutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coordinator_layout);
    }

    public static void launch(FragmentActivity activity) {
        Intent intent = new Intent(activity, CoordinatorLayoutActivity.class);
        activity.startActivity(intent);
    }

}
