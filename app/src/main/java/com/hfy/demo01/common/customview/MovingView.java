package com.hfy.demo01.common.customview;

import android.content.Context;
import android.graphics.Canvas;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.widget.Scroller;
import android.widget.Toast;


/**
 * 文件描述：随手指滑动的view
 * 作者：hufy
 * 创建时间：2019/2/22
 * 更改时间：2019/2/22 10:29
 * 版本号：1
 *
 * @author hufy
 */
public class MovingView extends View implements GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener {

    private final String TAG = getClass().getSimpleName();
    /**
     * 手指按下的初始X坐标
     */
    private int mLastX;

    /**
     * 手指按下的初始Y坐标
     */
    private int mLastY;

    /**
     * 手指滑动速度计算器
     */
    private VelocityTracker mVelocityTracker;

    /**
     * 手势检测
     * 建议在监听“双击”时使用GestureDetector。 其他监听如“滑动”直接在onTouchEvent()中实现即可。
     */
    private GestureDetector mGestureDetector;

    /**
     * 滚动帮助类
     */
    private Scroller mScroller;

    public MovingView(Context context) {
        super(context);
        init();
    }

    public MovingView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MovingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {

        mVelocityTracker = VelocityTracker.obtain();

        mGestureDetector = new GestureDetector(getContext(), this);
        mGestureDetector.setOnDoubleTapListener(this);

        mScroller = new Scroller(getContext());
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
        //获取触摸点坐标
        int x = (int) event.getX();
        int y = (int) event.getY();

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                //DOWN时，即刚开始的触摸点相对view的坐标。
                mLastX = x;
                mLastY = y;
                break;
            case MotionEvent.ACTION_MOVE:
                //滑动的距离 = 触摸点滑动到的坐标 - 开始触摸的坐标 （都是相对于view本身）
                int offsetX = x - mLastX;
                int offsetY = y - mLastY;

                //所以View也要跟上这个滑动距离——有多重方式：

                //方法一，layout（）
//                layout(getLeft() + offsetX, getTop() + offsetY, getRight() + offsetX, getBottom() + offsetY);

                //方法二，offsetLeftAndRight、offsetTopAndBottom
//                offsetLeftAndRight(offsetX);
//                offsetTopAndBottom(offsetY);

                //方法三，LayoutParams
//                ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) getLayoutParams();
//                layoutParams.leftMargin = getLeft() + offsetX;
//                layoutParams.topMargin = getTop() + offsetY;
//                setLayoutParams(layoutParams);

                //方法四，动画（一般在外面调用）
                //1、view动画(最终效果是滑动第一下可以滑动，后面再滑不行，因为view不能改变view的位置参数，不能真正的交互。)
//                float toXDelta = offsetX / getWidth();
//                float toYDelta = offsetY / getHeight();
//                TranslateAnimation animation = new TranslateAnimation(
//                        TranslateAnimation.RELATIVE_TO_SELF, 0,
//                        TranslateAnimation.RELATIVE_TO_SELF, toXDelta,
//                        TranslateAnimation.RELATIVE_TO_SELF, 0,
//                        TranslateAnimation.RELATIVE_TO_SELF, toYDelta);
//                animation.setDuration(0);
//                animation.setFillAfter(true);
//                startAnimation(animation);

                //2、属性动画(横移，貌似不适合放这里使用，效果会闪)
//                ObjectAnimator.ofFloat(this,"translationX",0, offsetX).setDuration(0).start();

                //方法五，scrollTo、scrollBy。
                // 注意：scrollTo、scrollBy 移动的是view的内容，即子view，本身不动。所以这里用的是(View)getParent()。
                //或者可以理解为，view本身移动（且手机屏幕一起）了，view的内容没动，视觉上就是view的内容反向移动。
                //getScrollX()：mScrollX，是view左边缘 和 view内容左边缘的距离。view左边缘在 view内容左边缘 的右侧，则mScrollX为正值，左侧为负。
                // 也就是说使用view.scrollTo（100，0），那么view和屏幕一起右移100，即视觉上view的内容左移100。
                // 所以要让view视觉上右移100，需要view的父view左移100，((View)getParent()).scrollTo（-100,0）；
//                ((View) getParent()).scrollTo(getScrollX() - offsetX, getScrollY() - offsetY);
                //scrollBy同理：
                ((View)getParent()).scrollBy(-offsetX, -offsetY);

                //速度器添加事件
                mVelocityTracker.addMovement(event);

                break;
            case MotionEvent.ACTION_UP:
                //计算手指滑动速度：1000ms内滑过的像素，（终点-起点）/时间段，所以从右向左滑 为负值。
                mVelocityTracker.computeCurrentVelocity(1000);
                float xVelocity = mVelocityTracker.getXVelocity();
                Log.i(TAG, "onTouchEvent: xVelocity = " + xVelocity);
                if (Math.abs(xVelocity) > 100) {
                    Toast.makeText(getContext(), "滑的有点快！", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }

        //接管TouchEvent来分析是什么手势。consumed表示消费掉事件。
        boolean consumed = mGestureDetector.onTouchEvent(event);
        return true;
    }

    /**
     * 使用Scroller弹性滑动
     *
     * @param desX     目标X
     * @param desY     目标Y
     * @param duration 时间
     */
    private void smoothScrollTo(int desX, int desY, int duration) {
        int deltaX = desX - getScrollX();
        int deltaY = desY - getScrollY();

        //前两个是起点左边，中间两个是滑动距离，duration是时间。此时仅仅是存入数据，并没有滑动。
        mScroller.startScroll(getScrollX(), getScrollY(), deltaX, deltaY, duration);

            //invalidate会导致重新绘制，即走draw()，然后走computeScroll()
        invalidate();
    }

    @Override
    public void computeScroll() {
        //计算本次滚动的位置，数据保存在Scroller中。返回true表示滚动未结束。
        if (mScroller.computeScrollOffset()) {
            //从Scroller中取出计算好的位置，并使用父view调scrollerTo来滑动 本身。
            ((View) getParent()).scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            //再次调绘制，又会走computeScroll()，继续这个过程，直到mScroller.computeScrollOffset()返回false结束滑动。
            invalidate();
        }
    }

    @Override
    public boolean onDown(MotionEvent e) {
        //手指触摸的一瞬间，由1个DOWN触发
        Log.i(TAG, "onDown: ");
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {
        //手指触摸的状态，由1个DOWN触发，强调的是没有拖动的状态，就是按着没动。
        Log.i(TAG, "onShowPress: ");
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        //单击，UP触发
        Log.i(TAG, "onSingleTapUp: ");
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        //滚动，1个DOWN，多个MOVE触发
        Log.i(TAG, "onScroll: ");
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {
        //长按
        Log.i(TAG, "onLongPress: ");
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        //快速move后up
        Log.i(TAG, "onFling: ");
        return false;
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        //确认的单击，不是双击中的某一击
        Log.i(TAG, "onSingleTapConfirmed: ");
        return false;
    }

    @Override
    public boolean onDoubleTap(MotionEvent e) {
        //双击，两个单击组成。 和onSingleTapConfirmed不能共存
        Log.i(TAG, "onDoubleTap: ");
        return false;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent e) {
        //发生了双击行为，双击期间DOWN、MOVE、UP都会触发此回调
        Log.i(TAG, "onDoubleTapEvent: ");
        return false;
    }
}
