package com.hfy.demo01.module.home.fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.FrameLayout;
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
        mTextView.setBackgroundResource(R.drawable.text_switch_bg);
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mTextView.setPadding(8, 6 ,8 ,6);//todo
        addView(mTextView, layoutParams);
    }

    /**mTextView
     * 设置要展示的数据，会自动开始翻滚。
     * @param stringList
     */
    public void setData(List<String> stringList){
        this.stringList = stringList;
        start();
    }

    /**
     * 快速向上滑动text、停留展示、向上滑出、等待x 秒后 继续下一个text 。
     */
    private void start(){

        int size = stringList.size();
        index = 0;
        setTextInfo(index);

        AnimatorSet animatorSet = new AnimatorSet();

        //从底部出现
        ObjectAnimator showAnimator = ObjectAnimator.ofFloat(mTextView, "translationY", 100, 0);
        showAnimator.setDuration(500);

        //向上滑出
        ObjectAnimator outAnimator = ObjectAnimator.ofFloat(mTextView, "translationY", 0, -100);
        outAnimator.setDuration(1000);
        outAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                //动画结束后，继续下一个
                index++;
                if (index <= size - 1){
                    setTextInfo(index);
                    long nextPopupTime = getNextPopupTime(index);
                    animatorSet.play(outAnimator).after(1500).after(showAnimator).after(nextPopupTime);
                    animatorSet.start();
                }
            }
        });

        animatorSet.play(outAnimator).after(1500).after(showAnimator);
        animatorSet.start();
    }

    /**
     * 获取 等待多久后 继续下一条弹窗
     * @param index
     * @return
     */
    private long getNextPopupTime(int index) {
        // TODO: 2019/7/4 获取 等待多久后 继续下一条弹窗

        return 0;
    }


    /**
     *  设置文字、图片
     * @param index
     */
    private void setTextInfo(int index) {
        String text = stringList.get(index);
        mTextView.setText(text);
    }

}
