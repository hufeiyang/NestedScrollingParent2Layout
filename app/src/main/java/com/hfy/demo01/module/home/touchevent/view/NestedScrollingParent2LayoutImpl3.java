package com.hfy.demo01.module.home.touchevent.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;

/**
 * 处理RecyclerView 套viewPager， viewPager内的fragment中 也有RecyclerView，处理外层、内层 RecyclerView的嵌套滑动问题
 * 类似淘宝、京东首页
 *
 */
public class NestedScrollingParent2LayoutImpl3 extends NestedScrollingParent2Layout {

    private final String TAG = this.getClass().getSimpleName();

    private RecyclerView mParentRecyclerView;


    private RecyclerView mChildRecyclerView;

    private View mLastItemView;


    public NestedScrollingParent2LayoutImpl3(Context context) {
        super(context);
    }

    public NestedScrollingParent2LayoutImpl3(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public NestedScrollingParent2LayoutImpl3(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOrientation(VERTICAL);
    }


    /**
     * 有嵌套滑动到来了，判断父view是否接受嵌套滑动
     *
     * @param child            嵌套滑动对应的父类的子类(因为嵌套滑动对于的父View不一定是一级就能找到的，可能挑了两级父View的父View，child的辈分>=target)
     * @param target           具体嵌套滑动的那个子类
     * @param nestedScrollAxes 支持嵌套滚动轴。水平方向，垂直方向，或者不指定
     * @param type             滑动类型，ViewCompat.TYPE_NON_TOUCH fling 效果ViewCompat.TYPE_TOUCH 手势滑动
     */
    @Override
    public boolean onStartNestedScroll(@NonNull View child, @NonNull View target, int nestedScrollAxes, int type) {
        //自己处理逻辑
        //这里处理是接受 竖向的 嵌套滑动
        return nestedScrollAxes == ViewCompat.SCROLL_AXIS_VERTICAL;
    }

    /**
     * 在嵌套滑动的子View未滑动之前，判断父view是否优先与子view处理(也就是父view可以先消耗，然后给子view消耗）
     *
     * @param target   具体嵌套滑动的那个子类，就是手指滑的那个 产生嵌套滑动的view
     * @param dx       水平方向嵌套滑动的子View想要变化的距离
     * @param dy       垂直方向嵌套滑动的子View想要变化的距离 dy<0向下滑动 dy>0 向上滑动
     * @param consumed 这个参数要我们在实现这个函数的时候指定，回头告诉子View当前父View消耗的距离
     *                 consumed[0] 水平消耗的距离，consumed[1] 垂直消耗的距离 好让子view做出相应的调整
     * @param type     滑动类型，ViewCompat.TYPE_NON_TOUCH fling 效果ViewCompat.TYPE_TOUCH 手势滑动
     */
    @Override
    public void onNestedPreScroll(@NonNull View target, int dx, int dy, @NonNull int[] consumed, int type) {
        //自己处理逻辑

        if (mLastItemView == null) {
            return;
        }

        int lastItemTop = mLastItemView.getTop();

        if (target == mParentRecyclerView) {
            handleParentRecyclerViewScroll(lastItemTop, dy, consumed);
        } else if (target == mChildRecyclerView) {
            handleChildRecyclerViewScroll(lastItemTop, dy, consumed);
        }
    }

    /**
     * 滑动外层RecyclerView时，的处理
     *
     * @param lastItemTop tab到屏幕顶部的距离，是0就代表到顶了
     * @param dy          目标滑动距离， dy>0 代表向上滑
     * @param consumed
     */
    private void handleParentRecyclerViewScroll(int lastItemTop, int dy, int[] consumed) {
        //tab上边没到顶
        if (lastItemTop != 0) {
            if (dy > 0) {
                //向上滑
                if (lastItemTop > dy) {
                    //tab的top>想要滑动的dy,就让外部RecyclerView自行处理
                } else {
                    //tab的top<=想要滑动的dy,先滑外部RecyclerView，滑距离为lastItemTop，刚好到顶；剩下的就滑内层了。
                    consumed[1] = dy;
                    mParentRecyclerView.scrollBy(0, lastItemTop);
                    mChildRecyclerView.scrollBy(0, dy - lastItemTop);
                }
            } else {
                //向下滑，就让外部RecyclerView自行处理
            }
        } else {
            //tab上边到顶了
            if (dy > 0){
                //向上，内层直接消费掉
                mChildRecyclerView.scrollBy(0, dy);
                consumed[1] = dy;
            }else {
                int childScrolledY = mChildRecyclerView.computeVerticalScrollOffset();
                if (childScrolledY > Math.abs(dy)) {
                    //内层已滚动的距离，大于想要滚动的距离，内层直接消费掉
                    mChildRecyclerView.scrollBy(0, dy);
                    consumed[1] = dy;
                }else {
                    //内层已滚动的距离，小于想要滚动的距离，那么内层消费一部分，到顶后，剩的还给外层自行滑动
                    mChildRecyclerView.scrollBy(0, -(Math.abs(dy)-childScrolledY));
                    consumed[1] = -(Math.abs(dy)-childScrolledY);
                }
            }
        }

    }

    /**
     * 滑动内层RecyclerView时，的处理
     *
     * @param lastItemTop tab到屏幕顶部的距离，是0就代表到顶了
     * @param dy
     * @param consumed
     */
    private void handleChildRecyclerViewScroll(int lastItemTop, int dy, int[] consumed) {
        //tab上边没到顶
        if (lastItemTop != 0) {
            if (dy > 0) {
                //向上滑
                if (lastItemTop > dy) {
                    //tab的top>想要滑动的dy,外层直接消耗掉
                    mParentRecyclerView.scrollBy(0, dy);
                    consumed[1] = dy;
                } else {
                    //tab的top<=想要滑动的dy,先滑外层，消耗距离为lastItemTop，刚好到顶；剩下的就滑内层了。
                    mParentRecyclerView.scrollBy(0, lastItemTop);
                    consumed[1] = dy - lastItemTop;
                }
            } else {
                //向下滑，外层直接消耗
                mParentRecyclerView.scrollBy(0, dy);
                consumed[1] = dy;
            }
        }else {
            //tab上边到顶了
            if (dy > 0){
                //向上，内层自行处理
            }else {
                int childScrolledY = mChildRecyclerView.computeVerticalScrollOffset();
                if (childScrolledY > Math.abs(dy)) {
                    //内层已滚动的距离，大于想要滚动的距离，内层自行处理
                }else {
                    //内层已滚动的距离，小于想要滚动的距离，那么内层消费一部分，到顶后，剩的外层滑动
                    mChildRecyclerView.scrollBy(0, -childScrolledY);
                    mParentRecyclerView.scrollBy(0, -(Math.abs(dy)-childScrolledY));
                    consumed[1] = dy;
                }
            }
        }
    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        //直接获取外层RecyclerView
        mParentRecyclerView = getRecyclerView(this);
        Log.i(TAG, "onFinishInflate: mParentRecyclerView=" + mParentRecyclerView);

        //关于内层RecyclerView：此时还获取不到ViewPager内fragment的RecyclerView，需要在加载ViewPager后 fragment可见时 传入
    }

    private RecyclerView getRecyclerView(ViewGroup viewGroup) {
        int childCount = viewGroup.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childAt = getChildAt(i);
            if (childAt instanceof RecyclerView) {
                if (mParentRecyclerView == null) {
                    return (RecyclerView) childAt;
                }
            }
        }
        return null;
    }

    /**
     * 传入内部RecyclerView
     *
     * @param childRecyclerView
     */
    public void setChildRecyclerView(RecyclerView childRecyclerView) {
        mChildRecyclerView = childRecyclerView;
    }


    /**
     * 外层RecyclerView的最后一个item，即：tab + viewPager
     * 用于判断 滑动 临界位置
     *
     * @param lastItemView
     */
    public void setLastItem(View lastItemView) {
        mLastItemView = lastItemView;
    }
}
