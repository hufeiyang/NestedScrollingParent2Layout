package com.hfy.demo01.module.home.fragment;

import android.content.Context;
import android.graphics.Color;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hfy.demo01.R;

import java.util.ArrayList;
import java.util.List;


/**
 * @author hufy
 * @apiNote 自动向上翻滚的view，用于秒杀会场页面的用户购买信息展示~
 * @date 2019/7/4 17:34
 */
public class AutoSwitchTextView extends FrameLayout {

    /**
     * 展示的数据
     */
    private List<String> stringList = new ArrayList<>();

    /**
     * 用于翻滚的textView
     */
    private TextView mTextView;

    /**
     * index
     */
    private int index = 0;

    public AutoSwitchTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    private void initView(Context context) {
        mTextView = new TextView(context);
        mTextView.setTextColor(Color.WHITE);
        mTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 10);
        mTextView.setBackgroundResource(R.drawable.text_switch_bg);
        mTextView.setPadding(16, 0 ,16 ,0);
        mTextView.setGravity(Gravity.CENTER);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        addView(mTextView, layoutParams);
    }

    /**mTextView
     * 设置要展示的数据，会自动开始翻滚。
     * @param stringList
     */
    public void setData(List<String> stringList){
        this.stringList = stringList;
        setVisibility(View.VISIBLE);
        start();
    }

    /**
     * 快速向上滑动text、停留展示、向上滑出、等待x 秒后 继续下一个text 。
     */
    private void start(){

        int size = stringList.size();
        index = 0;
        setTextInfo(index);

        //1.从底部出现
        TranslateAnimation showAnimator = new TranslateAnimation(Animation.ABSOLUTE, 0, Animation.ABSOLUTE, 0,
                Animation.RELATIVE_TO_PARENT, 1, Animation.ABSOLUTE, 0);
        showAnimator.setDuration(500);
        showAnimator.setFillAfter(true);

        //2、停留1.5s 向上滑出
        TranslateAnimation outAnimator = new TranslateAnimation(Animation.ABSOLUTE, 0, Animation.RELATIVE_TO_SELF,
                0, Animation.ABSOLUTE, 0, Animation.RELATIVE_TO_PARENT, -1);
        outAnimator.setDuration(1000);
        outAnimator.setFillAfter(true);
        outAnimator.setStartOffset(1500);

        showAnimator.setAnimationListener(new AnimationEndListener() {
            @Override
            void onAnimationFinish(Animation animation) {
                mTextView.startAnimation(outAnimator);
            }
        });

        outAnimator.setAnimationListener(new AnimationEndListener() {
            @Override
            void onAnimationFinish(Animation animation) {
                index++;
                if (index <= size - 1){
                    setTextInfo(index);
                    long nextPopupTime = getNextPopupTime(index);
                    showAnimator.setStartOffset(nextPopupTime);
                    mTextView.startAnimation(showAnimator);
                }
            }
        });

        mTextView.startAnimation(showAnimator);
    }

    /**
     * 结束动画
     */
    public void finish(){
        if (mTextView != null) {
            mTextView.clearAnimation();
        }
    }

    /**
     * 获取 等待多久后 继续下一条弹窗
     * @param index
     * @return
     */
    private long getNextPopupTime(int index) {
        // TODO: 2019/7/4 获取 等待多久后 继续下一条弹窗

        return 1000;
    }


    /**
     *  设置文字、图片
     * @param index
     */
    private void setTextInfo(int index) {
        String text = stringList.get(index);
        mTextView.setText(text);
    }

    abstract class AnimationEndListener implements Animation.AnimationListener{


        @Override
        public void onAnimationStart(Animation animation) {
            //nothing
        }

        @Override
        public void onAnimationEnd(Animation animation) {
            onAnimationFinish(animation);
        }

        @Override
        public void onAnimationRepeat(Animation animation) {
            //nothing
        }

        abstract void onAnimationFinish(Animation animation);
    }

}
