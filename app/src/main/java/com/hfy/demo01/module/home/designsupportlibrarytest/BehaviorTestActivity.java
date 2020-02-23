package com.hfy.demo01.module.home.designsupportlibrarytest;

import android.app.Activity;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.hfy.demo01.R;
import com.hfy.demo01.common.customview.MovingView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 学习CoordinatorLayout时，练习Behavior的使用
 *
 * @author hufy
 */
public class BehaviorTestActivity extends AppCompatActivity {

    @BindView(R.id.mv_test)
    public MovingView mMvTest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_behavior_test);

        ButterKnife.bind(this);

    }

    public static void launch(Activity activity) {
        Intent intent = new Intent(activity, BehaviorTestActivity.class);
        activity.startActivity(intent);
    }

    @OnClick({R.id.mv_test})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mv_test:
                Toast.makeText(this, "点了", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }
}
