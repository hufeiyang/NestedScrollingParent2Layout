package com.hfy.demo01.module.home.designsupportlibrarytest;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;

import com.hfy.demo01.common.customview.MovingView;

/**
 * 文件描述：自定义CoordinatorLayout.Behavior，为了理解CoordinatorLayout的作用。
 * 作者：hufy
 * 创建时间：2019/2/22
 * 更改时间：2019/2/22 17:26
 * 版本号：1
 *
 * @author hufy
 */
public class TestBehavior extends CoordinatorLayout.Behavior<Button> {

    /**
     * 屏幕宽度
     */
    private int mWidth;

    public TestBehavior() {
    }

    public TestBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        mWidth = displayMetrics.widthPixels;
    }

    /**
     * 判断child的布局是否依赖dependency
     * @param parent CoordinatorLayout
     * @param child 执行动作的CoordinatorLayout的子View，即设置这个behavior的view
     * @param dependency Child依赖的View，需要关心的view
     * @return 返回false表示child不依赖dependency，true表示依赖
     */
    @Override
    public boolean layoutDependsOn(@NonNull CoordinatorLayout parent, @NonNull Button child, @NonNull View dependency) {
        //如果dependency是MovingView的实例，说明它就是我们所需要的Dependency
        return dependency instanceof MovingView;
    }

    /**
     * 当dependency发生改变时（位置、宽高等），执行这个函数
     * @param parent
     * @param child
     * @param dependency
     * @return 返回true表示 child的位置或者是宽高 要发生改变，否则就返回false
     */
    @Override
    public boolean onDependentViewChanged(@NonNull CoordinatorLayout parent, @NonNull Button child, @NonNull View dependency) {

        int left = dependency.getLeft();
        int top = dependency.getTop();

        //dependency移动时，child随着dependency移动：竖直方向一致，水平方向反向移动
        child.layout(mWidth - left - child.getWidth(), top, mWidth - left, top + child.getHeight());

        return true;
    }
}
