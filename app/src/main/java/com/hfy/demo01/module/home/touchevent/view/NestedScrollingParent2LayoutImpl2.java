package com.hfy.demo01.module.home.touchevent.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.NestedScrollingParent2;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.hfy.demo01.R;

/**
 * 处理 header + recyclerView 的 嵌套滑动，且保证 recyclerView复用机制不失效
 * Description:NestedScrolling2机制下的嵌套滑动，实现NestedScrollingParent2接口下，处理fling效果的区别
 *
 */
public class NestedScrollingParent2LayoutImpl2 extends NestedScrollingParent2Layout implements NestedScrollingParent2 {


    private View mTopView;

    private View mRecyclerView;

    private int mTopViewHeight;


    public NestedScrollingParent2LayoutImpl2(Context context) {
        this(context, null);
    }

    public NestedScrollingParent2LayoutImpl2(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NestedScrollingParent2LayoutImpl2(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOrientation(VERTICAL);
    }


    @Override
    public boolean onStartNestedScroll(@NonNull View child, @NonNull View target, int axes, int type) {
        return (axes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0;
    }


    /**
     * 在嵌套滑动的子View未滑动之前，判断父view是否优先与子view处理(也就是父view可以先消耗，然后给子view消耗）
     *
     * @param target   具体嵌套滑动的那个子类
     * @param dx       水平方向嵌套滑动的子View想要变化的距离
     * @param dy       垂直方向嵌套滑动的子View想要变化的距离 dy<0向下滑动 dy>0 向上滑动
     * @param consumed 这个参数要我们在实现这个函数的时候指定，回头告诉子View当前父View消耗的距离
     *                 consumed[0] 水平消耗的距离，consumed[1] 垂直消耗的距离 好让子view做出相应的调整
     * @param type     滑动类型，ViewCompat.TYPE_NON_TOUCH fling效果,ViewCompat.TYPE_TOUCH 手势滑动
     */
    @Override
    public void onNestedPreScroll(@NonNull View target, int dx, int dy, @NonNull int[] consumed, int type) {
        //这里不管手势滚动还是fling都处理
        boolean hideTop = dy > 0 && getScrollY() < mTopViewHeight;
        boolean showTop = dy < 0 && getScrollY() >= 0 && !target.canScrollVertically(-1);
        if (hideTop || showTop) {
            scrollBy(0, dy);
            consumed[1] = dy;
        }
    }


    @Override
    public void onNestedScroll(@NonNull View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int type) {
        //当子控件处理完后，交给父控件进行处理。
        if (dyUnconsumed < 0) {
            //表示已经向下滑动到头
            scrollBy(0, dyUnconsumed);
        }
    }

    @Override
    public boolean onNestedPreFling(@NonNull View target, float velocityX, float velocityY) {
        return false;
    }


    @Override
    public boolean onNestedFling(@NonNull View target, float velocityX, float velocityY, boolean consumed) {
        return false;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        //这里修改mRecylerVIew的高度为屏幕高度，否则底部会出现空白。（因为scrollTo方法是滑动子view，就把mRecylerVIew滑上去了）
        ViewGroup.LayoutParams layoutParams = mRecyclerView.getLayoutParams();
        layoutParams.height = getMeasuredHeight();
        mRecyclerView.setLayoutParams(layoutParams);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        mRecyclerView = getRecyclerView(this);
    }

    /**
     * 设置 嵌套滑动的header
     * @return
     */
    public View setHeader(View header) {
        mTopView = header;
        return header;
    }

    /**
     * 自动 获取 嵌套滑动的 RecyclerView
     * @param viewGroup
     * @return
     */
    private RecyclerView getRecyclerView(ViewGroup viewGroup) {
        int childCount = viewGroup.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childAt = getChildAt(i);
            if (childAt instanceof RecyclerView) {
                if (mRecyclerView == null) {
                    return (RecyclerView) childAt;
                }
            }
        }
        return null;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mTopViewHeight = mTopView.getMeasuredHeight();
    }

    @Override
    public void scrollTo(int x, int y) {
        if (y < 0) {
            y = 0;
        }
        if (y > mTopViewHeight) {
            y = mTopViewHeight;
        }
        super.scrollTo(x, y);
    }


}
