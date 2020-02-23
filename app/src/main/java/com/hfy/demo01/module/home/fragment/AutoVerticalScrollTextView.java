package com.hfy.demo01.module.home.fragment;

import android.content.Context;
import android.graphics.Camera;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import androidx.appcompat.content.res.AppCompatResources;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.hfy.demo01.R;

import java.util.ArrayList;

/**
 * 垂直滚动的Textview 类似小喇叭通知
 */
public class AutoVerticalScrollTextView extends TextSwitcher implements ViewSwitcher.ViewFactory {

    private Context mContext;

    //mInUp,mOutUp分别构成向下翻页的进出动画
    private Rotate3dAnimation mInUp;
    private Rotate3dAnimation mOutUp;

    private int number = 0;

    private boolean isRunning = true;

    private NoticeHandler noticeHandler = new NoticeHandler();

    /**
     * 这个数组 用来存放你需要显示的text
     */
    private ArrayList<String> data;
    private AutoVerticalScrollThread mAutoVerticalScrollThread;


    public AutoVerticalScrollTextView(Context context) {
        this(context, null);
    }

    public AutoVerticalScrollTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    private void init() {

        setFactory(this);

        mInUp = createInAnim(true, true);
        mOutUp = createOutAnim(false, true);

        setInAnimation(mInUp);//当View显示时动画资源ID
        setOutAnimation(mOutUp);//当View隐藏是动画资源ID。
    }

    private Rotate3dAnimation createInAnim(boolean turnIn, boolean turnUp) {

        Rotate3dAnimation rotation = new Rotate3dAnimation(turnIn, turnUp);
        rotation.setDuration(1500);//执行动画的时间
        rotation.setFillAfter(false);//是否保持动画完毕之后的状态
        rotation.setInterpolator(new AccelerateInterpolator());//设置加速模式

        return rotation;
    }

    private Rotate3dAnimation createOutAnim(boolean turnIn, boolean turnUp) {

        Rotate3dAnimation rotation = new Rotate3dAnimation(turnIn, turnUp);
        rotation.setDuration(1500);//执行动画的时间
        rotation.setFillAfter(false);//是否保持动画完毕之后的状态
        rotation.setInterpolator(new AccelerateInterpolator());//设置加速模式

        return rotation;
    }


    /**
     * 这里返回的TextView，就是我们看到的View,可以设置自己想要的效果
     *
     * @return
     */
    public View makeView() {

        TextView textView = new TextView(mContext);
        textView.setGravity(Gravity.LEFT);
        textView.setTextSize(11);//这是sp的单位
        textView.setGravity(Gravity.CENTER_VERTICAL);
        textView.setSingleLine(true);
        textView.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        textView.setMarqueeRepeatLimit(1);
        textView.setTextColor(getResources().getColor(R.color.colorPrimary));//TODO
        textView.setBackgroundResource(R.drawable.text_switch_bg);

        Drawable leftDrawable = AppCompatResources.getDrawable(mContext, R.mipmap.icon_item_detail_back);
        int w = leftDrawable.getIntrinsicWidth();
        int h = leftDrawable.getIntrinsicHeight();
        leftDrawable.setBounds(0, 0, w, h);
        textView.setCompoundDrawables(leftDrawable, null, null, null);
        textView.setCompoundDrawablePadding(10);
        textView.setPadding(10,10,10,10);
        return textView;

    }

    /**
     * 定义动作，向上滚动翻页
     */
    private void showAnimation() {
        //显示动画
        if (getInAnimation() != mInUp) {
            setInAnimation(mInUp);
        }
        //隐藏动画
        if (getOutAnimation() != mOutUp) {
            setOutAnimation(mOutUp);
        }
    }

    /**
     * 传入 要展示的 内容
     * @param data
     */
    public void setData(ArrayList<String> data) {
        this.data = data;
    }

    /**
     * 开始滚动
     */
    public void start() {
        if (mAutoVerticalScrollThread == null) {
            mAutoVerticalScrollThread = new AutoVerticalScrollThread();
        }

        isRunning = true;
        number = 0;
        if (!mAutoVerticalScrollThread.isAlive()) {
            mAutoVerticalScrollThread.start();
        }
    }

    class AutoVerticalScrollThread extends Thread {
        @Override
        public void run() {
            while (isRunning) {
                noticeHandler.sendEmptyMessage(199);
                SystemClock.sleep(4000); // TODO 这个播放间隔时间 可以作成 自定义属性，在xml中设置
//                    noticeHandler.sendEmptyMessage(200);
//                    SystemClock.sleep(2500);
            }
        }
    }

    /**
     * 停止滚动
     */
    public void stop() {
//        getNextView().setFocusable(false);
//        getNextView().setFocusableInTouchMode(false);

        isRunning = false;

        noticeHandler.removeMessages(199);

        if (mAutoVerticalScrollThread.isAlive()) {
            mAutoVerticalScrollThread.stop();
        }
    }

    private class NoticeHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 199) {
                setText(data.get(number % data.size()));
                showAnimation();
                number++;
//
//                getCurrentView().setFocusable(false);
//                getCurrentView().setFocusableInTouchMode(false);
            }
            else if (msg.what == 200) {
                //开始 跑马灯效果
//                getCurrentView().setFocusable(true);
//                getCurrentView().setFocusableInTouchMode(true);
            }

        }
    }

    class Rotate3dAnimation extends Animation {
        private float mCenterX;
        private float mCenterY;
        private final boolean mTurnIn;
        private final boolean mTurnUp;
        private Camera mCamera;

        public Rotate3dAnimation(boolean turnIn, boolean turnUp) {
            mTurnIn = turnIn;
            mTurnUp = turnUp;
        }

        @Override
        public void initialize(int width, int height, int parentWidth, int parentHeight) {
            super.initialize(width, height, parentWidth, parentHeight);
            mCamera = new Camera();
            mCenterY = getHeight();
            mCenterX = getWidth();
        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {

            final float centerX = mCenterX;
            final float centerY = mCenterY;
            final Camera camera = mCamera;
            final int derection = mTurnUp ? 1 : -1;

            final Matrix matrix = t.getMatrix();

            camera.save();
            if (mTurnIn) {
                camera.translate(0.0f, derection * mCenterY * (interpolatedTime - 1.0f), 0.0f);
            } else {
                camera.translate(0.0f, derection * mCenterY * (interpolatedTime), 0.0f);
            }
            camera.getMatrix(matrix);
            camera.restore();

            matrix.preTranslate(-centerX, -centerY);
            matrix.postTranslate(centerX, centerY);
        }
    }

}