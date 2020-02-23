package com.hfy.demo01.module.home.animation;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.TextView;

import com.hfy.demo01.R;
import com.hfy.demo01.common.customview.MyToast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * 动画 测试
 * @author hufeiyang
 */
public class AnimationTestActivity extends AppCompatActivity {

    private Unbinder unbinder;

    @BindView(R.id.textView1)
    TextView textView1;

    @BindView(R.id.textView2)
    TextView textView2;

    @BindView(R.id.textView3)
    TextView textView3;

    @BindView(R.id.textView4)
    TextView textView4;

    @BindView(R.id.button_animator_test)
    Button button;
    private AnimatorSet mAnimatorSet;


    public static void launch(Activity activity) {
        Intent intent = new Intent(activity, AnimationTestActivity.class);
        activity.startActivity(intent);
        //overridePendingTransition必须位于startActivity会finish的后面，才会有动画效果
        activity.overridePendingTransition(R.anim.enter_from_right, 0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animation_test);
        unbinder = ButterKnife.bind(this);

        viewAnimationTest();

        objectAnimatorTest();

        testAnimatorAboutButtonWidth();
    }

    /**
     * 属性动画test
     * 属性动画几乎无所不能，只要对象有这个属性，就可以对这个属性做动画。
     * 原理：内部通过反射 调用对应属性的set、get方法。
     *
     *
     * 差值器：Interpolator，根据 时间流逝的百分比，计算，当前属性值改变的百分比。
     * 例如duration是1000，start后过了200，那么时间百分比是0.2，那么如果差值器是LinearInterpolator线性差值器，那么属性值改变的百分比也是0.2
     *
     * 估值器：Evaluator，就是根据 差值器获取的 属性值百分比，计算改变后的属性值。
     * ofInt、onFloat内部会自动设置IntEvaluator、FloatEvaluator。如果使用ofInt且是颜色相关的属性，就要设置ArgbEvaluator。见下面 文字颜色变化 的例子。
     *
     */
    private void objectAnimatorTest() {
        //属性动画使用，方式一：代码，建议使用。
        //setTranslationX，像view动画一样，view实际的layout位置没变，改变了视图位置，但是 给触摸点生效区域增加了位移。（而view动画仅改变了视图位置）
        ObjectAnimator translationX = ObjectAnimator
                .ofFloat(textView4, "translationX", 0, 200)
                .setDuration(1000);
        translationX.setInterpolator(new LinearInterpolator());
        setAnimatorListener(translationX);

        //属性动画使用，方式二：xml。
        Animator animatorUpAndDown = AnimatorInflater.loadAnimator(this, R.animator.animator_test);
        animatorUpAndDown.setTarget(textView4);

        //文字颜色变化
        ObjectAnimator textColor = ObjectAnimator
                .ofInt(textView4, "textColor", 0xffff0000, 0xff00ffff)
                .setDuration(1000);
        textColor.setRepeatCount(ValueAnimator.INFINITE);
        textColor.setRepeatMode(ValueAnimator.REVERSE);
        //注意，这里如果不设置 那么颜色就是跳跃的，设置ArgbEvaluator 就是连续过度的颜色变化
        textColor.setEvaluator(new ArgbEvaluator());

        //animatorSet
        mAnimatorSet = new AnimatorSet();
        mAnimatorSet
                .play(animatorUpAndDown)
                .with(textColor)
                .after(translationX);

        mAnimatorSet.start();

    }

    /**
     * 对任意属性做动画 要求两个条件：
     *      1、object有对应属性的set方法，没设置初始值还要有get方法。
     *      2、set方法要对object有所改变，如UI的变化。（如TextView调用setWidth方法却不会改变UI，那就不产生动画）
     *
     *  当不满足条件时，有如下处理方法：
     *      1、给object添加set、get方法，如果有权限。（一般不行，如TextView是SDK里面的不能直接改）
     *      2、给Object包装一层，在包装类中提供set、get方法。{@link #}
     *      3、使用ValueAnimator，监听Value变化过程，自己实现属性的改变。
     */
    private void testAnimatorAboutButtonWidth() {
        //Button width 属性动画：如果xml中宽度是wrap_content，那么动画有效。
        // 如果设置button确切的dp值，那么无效，因为对应属性"width"的setWidth()方法就是 在wrap_content是才有效。
        ObjectAnimator width1 = ObjectAnimator.ofInt(button, "width", 600);
        width1.setDuration(2000);
//        width1.start();

        //那么，想要在button原本有确切dp值时，要能对width动画，怎么做呢？
        //方法一，包一层，然后用layoutParams
        ViewWrapper wrapper = new ViewWrapper(button);
        ObjectAnimator width2 = ObjectAnimator.ofInt(wrapper, "width", 600);
        width2.setDuration(2000);
//        width2.start();

        //方法二，使用ValueAnimator，每一帧自己显示宽度的变化
        ValueAnimator valueAnimator = ValueAnimator.ofInt(button.getLayoutParams().width, 600);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int animatedValue = (Integer) animation.getAnimatedValue();
                Log.i("hfy", "onAnimationUpdate: animatedValue="+animatedValue);

//                IntEvaluator intEvaluator = new IntEvaluator();
////                获取属性值改变比例、计算属性值
//                float animatedFraction = animation.getAnimatedFraction();
//                Integer evaluate = intEvaluator.evaluate(animatedFraction, 300, 600);
//                Log.i("hfy", "onAnimationUpdate: evaluate="+evaluate);


                button.getLayoutParams().width = animatedValue;
                button.requestLayout();
            }
        });

        valueAnimator.setDuration(4000).start();

    }

    private void setAnimatorListener(ObjectAnimator translationX) {
        translationX.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                //每播放一帧，都会调用
            }
        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            translationX.addPauseListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationResume(Animator animation) {
                    super.onAnimationResume(animation);
                }
            });
        }

        translationX.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
            }
        });
    }




    /**
     * view动画test
     */
    private void viewAnimationTest() {
        //view动画使用，方式一：xml，建议使用。
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
                MyToast.showMsg(AnimationTestActivity.this, "View动画：代码 set：View动画结束~");
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        textView3.setAnimation(animationSet2);
    }

    @OnClick({R.id.button_animator_test,
            R.id.textView4})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button:
//                linearLayout.addView(new Button(this));
            break;
            case R.id.textView4:
                MyToast.showMsg(this, "点了textView4");
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();

        if (mAnimatorSet != null && mAnimatorSet.isRunning()) {
            mAnimatorSet.cancel();
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, R.anim.exit_to_right);
    }


    /**
     * 包一层，提供对应属性的set、get方法
     */
    private class ViewWrapper {

        private final View mView;

        public ViewWrapper(View view) {
            mView = view;
        }

        public int getWidth(){
            return mView.getLayoutParams().width;
        }

        public void setWidth(int width){
            ViewGroup.LayoutParams layoutParams = mView.getLayoutParams();
            layoutParams.width = width;
            mView.setLayoutParams(layoutParams);
            mView.requestLayout();
        }
    }


}
