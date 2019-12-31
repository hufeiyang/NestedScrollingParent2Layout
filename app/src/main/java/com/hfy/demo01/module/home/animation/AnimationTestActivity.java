package com.hfy.demo01.module.home.animation;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hfy.demo01.R;
import com.hfy.demo01.common.customview.MyToast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class AnimationTestActivity extends AppCompatActivity {

    private Unbinder unbinder;

    @BindView(R.id.textView1)
    TextView textView1;

    @BindView(R.id.textView2)
    TextView textView2;

    @BindView(R.id.textView3)
    TextView textView3;

    @BindView(R.id.linearLayout)
    LinearLayout linearLayout;



    public static void launch(Activity activity) {
        Intent intent = new Intent(activity, AnimationTestActivity.class);
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.enter_from_right, 0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animation_test);
        unbinder = ButterKnife.bind(this);


        //view动画使用，方式一：xml
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.animation_test);
        textView1.startAnimation(animation);

        //view动画使用，方式二：new 动画对象
        AnimationSet animationSet = new AnimationSet(false);
        animationSet.setDuration(3000);
        animationSet.addAnimation(new TranslateAnimation(0, 100, 0, 0));
        animationSet.addAnimation(new ScaleAnimation(0.1f, 1f, 0.1f, 1f));
        animationSet.setFillAfter(true);
        textView2.startAnimation(animationSet);

        //view动画使用，方式二：new 动画对象,使用setAnimation
        AnimationSet animationSet2 = new AnimationSet(false);
        animationSet2.setDuration(3000);
        animationSet2.addAnimation(new TranslateAnimation(0, 100, 0, 0));
        animationSet2.addAnimation(new ScaleAnimation(0.1f, 1f, 0.1f, 1f));
        animationSet2.setFillAfter(true);
        animationSet2.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                MyToast.showMsg(AnimationTestActivity.this, "动画结束~");
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        textView3.setAnimation(animationSet2);

        LayoutAnimationController controller = new LayoutAnimationController(animationSet2);
        controller.setDelay(0.5f);
        controller.setOrder(LayoutAnimationController.ORDER_NORMAL);
        linearLayout.setLayoutAnimation(controller);
    }

    @OnClick({R.id.button})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button:
                linearLayout.addView(new Button(this));
            break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, R.anim.exit_to_right);
    }
}
