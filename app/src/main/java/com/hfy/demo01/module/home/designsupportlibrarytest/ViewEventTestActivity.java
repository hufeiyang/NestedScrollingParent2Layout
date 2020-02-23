package com.hfy.demo01.module.home.designsupportlibrarytest;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.FragmentActivity;
import androidx.core.view.ViewConfigurationCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.ViewConfiguration;

import com.hfy.demo01.R;

/**
 * view事件体系测试
 * 《艺术探索》-第三章
 * @author hufy
 */
public class ViewEventTestActivity extends AppCompatActivity {

    private String TAG = getClass().getSimpleName();

    public static void launch(FragmentActivity activity) {
        Intent intent = new Intent(activity, ViewEventTestActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coordinator_layout);

        initView();
    }

    private void initView() {
        //scaledTouchSlop，被认为是滑动的最小距离，单位是像素。和设备有关。
        int scaledTouchSlop = ViewConfiguration.get(this).getScaledTouchSlop();
        Log.i(TAG, "initView, scaledTouchSlop = "+ scaledTouchSlop + " pixels");
    }


}
