package com.hfy.demo01.module.home.designsupportlibrarytest;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RemoteViews;

import com.hfy.demo01.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author hufy
 * 通知test
 */
public class NotificationActivity extends AppCompatActivity {

    @BindView(R.id.btn_ordinary)
    public Button mBtnOrdinary;

    @BindView(R.id.btn_fold)
    public Button mBtnFold;

    @BindView(R.id.btn_hang)
    public Button mBtnHang;

    public static void launch(Context context) {
        Intent intent = new Intent(context, NotificationActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        ButterKnife.bind(this);
    }

    @OnClick({R.id.btn_ordinary, R.id.btn_fold, R.id.btn_hang})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_ordinary:
                sendOrdinaryNotification();
                break;
            case R.id.btn_fold:
                sendFoldNotification();
                break;
            case R.id.btn_hang:
                sendHangNotification();
                break;

            default:
                break;
        }
    }

    /**
     * 普通通知
     */
    private void sendOrdinaryNotification() {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);

        Notification notification = null;

        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://blog.csdn.net/hfy8971613/article/details/85481124"));
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        notification = builder.setContentTitle("普通通知Title")
                .setContentText("普通通知Text")
                .setSmallIcon(R.mipmap.dog)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                .setAutoCancel(true)
                //点击跳转
                .setContentIntent(pendingIntent)
                .build();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //显示等级，任何情况 都会显示通知
            builder.setVisibility(Notification.VISIBILITY_PUBLIC);
        }

        if (notificationManager != null) {
            notificationManager.notify(1, notification);
        }

    }

    /**
     * 折叠通知
     * 就是自定义视图的通知； 这个视图显示的进程 和 创建视图的进程不是一个进程，所以我们需要使用 {@link RemoteViews}
     */
    private void sendFoldNotification() {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);

        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://blog.csdn.net/hfy8971613/article/details/85481124"));
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        RemoteViews remoteView = new RemoteViews(getPackageName(), R.layout.fold_notification);

        Notification notification = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            notification = builder.setContentTitle("折叠通知Title")
                    .setContentText("折叠通知Text")
                    .setSmallIcon(R.mipmap.dog)
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                    .setAutoCancel(true)
                    //点击跳转
                    .setContentIntent(pendingIntent)
                    //下拉前
//                        .setCustomContentView(collapsed)
                    //设置展开时的视图(下拉后)
                    .setCustomBigContentView(remoteView)
                    //设置浮动通知视图
//                        .setCustomHeadsUpContentView(remoteView)
                    .build();

            if (notificationManager != null) {
                notificationManager.notify(2, notification);
            }
        }
    }

    /**
     * 悬挂通知
     * 直接显示在 屏幕上方，且焦点不变，会自动消失。
     */
    private void sendHangNotification() {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);

        Notification notification = null;

        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://blog.csdn.net/hfy8971613/article/details/85481124"));
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Intent hangIntent = new Intent();
            hangIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            hangIntent.setClass(this, NotificationActivity.class);
            PendingIntent hangPendingIntent = PendingIntent.getActivity(this, 0, hangIntent, PendingIntent.FLAG_CANCEL_CURRENT);

            notification = builder.setContentTitle("悬挂通知Title")
                    .setContentText("悬挂通知Text")
                    .setSmallIcon(R.mipmap.dog)
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent)
                    //悬挂式通知  悬挂在手机屏上方
                    .setFullScreenIntent(hangPendingIntent, true)
                    .build();

            if (notificationManager != null) {
                notificationManager.notify(3, notification);
            }
        }
    }
}
