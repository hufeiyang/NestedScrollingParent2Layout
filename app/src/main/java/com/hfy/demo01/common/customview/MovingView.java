package com.hfy.demo01.common.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;


/**
 * 文件描述：随手指滑动的view
 * 作者：hufy
 * 创建时间：2019/2/22
 * 更改时间：2019/2/22 10:29
 * 版本号：1
 *
 * @author hufy
 */
public class MovingView extends View {

    /**
     * 手指按下的初始X坐标
     */
    private int mLastX;

    /**
     * 手指按下的初始Y坐标
     */
    private int mLastY;

    public MovingView(Context context) {
        super(context);
    }

    public MovingView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MovingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        int x = (int) event.getX();
        int y = (int) event.getY();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                //触摸点相对view的坐标。（活动过程中，这个值不变）
                mLastX = x;
                mLastY = y;
                break;
            case MotionEvent.ACTION_MOVE:
                //滑动的距离 = 触摸点滑动到的坐标 - 开始触摸的坐标 （都是相对于view本身）
                int offsetX = x - mLastX;
                int offsetY = y - mLastY;

                //所以View也要跟上这个滑动距离
                layout(getLeft() + offsetX, getTop() + offsetY, getRight() + offsetX, getBottom() + offsetY);
                break;
            case MotionEvent.ACTION_UP:
//                Toast.makeText(getContext(), "点了", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }

        return true;
    }

}
