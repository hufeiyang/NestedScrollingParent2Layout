package com.hfy.demo01.module.home.designsupportlibrarytest;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RemoteViews;

import com.hfy.demo01.R;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author hufy
 * 通知test
 */
public class NotificationActivity extends AppCompatActivity {

    private static final String CHANNEL_ID_01 = "Demo01_notification_channel_id_01";
    private static final String CHANNEL_ID_02 = "Demo01_notification_channel_id_02";
    private static final String CHANNEL_ID_03 = "Demo01_notification_channel_id_03";

    @BindView(R.id.btn_ordinary)
    public Button mBtnOrdinary;

    @BindView(R.id.btn_fold_picture)
    public Button mBtnFoldPic;

    @BindView(R.id.btn_fold_text)
    public Button mBtnFoldText;

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

    @OnClick({R.id.btn_ordinary, R.id.btn_fold_picture, R.id.btn_fold_text, R.id.btn_fold_custom, R.id.btn_hang})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_ordinary:
                sendOrdinaryNotification();
                break;
            case R.id.btn_fold_picture:
//                sendFoldNotification();
                sendExpandableNotificationForPic();
                break;
            case R.id.btn_fold_text:
                sendExpandableNotificationForText();
                break;
            case R.id.btn_fold_custom:
                sendCustomNotification(this, R.mipmap.ic_notify, "已定义title", "msg", null, null, "");
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

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID_01);

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

        //兼容8.0
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID_01,
                    "NAME_CHANNEL_ID_01", NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("CHANNEL_ID_01_Description");
            notificationManager.createNotificationChannel(channel);
        }

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
     * 可展开的通知
     * 默认情况下，通知的文字内容会被截断以放在一行。如果您需要长一些的通知，可以通过应用其他模板启用更大的展开式文本区域
     */
    private void sendExpandableNotificationForPic() {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID_02);

        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://blog.csdn.net/hfy8971613/article/details/85481124"));
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);


//        Bitmap bitmapExpanable = BitmapFactory.decodeResource(getResources(), R.mipmap.expanablepic);
        Bitmap bitmapExpanable = BitmapFactory.decodeResource(getResources(), R.mipmap.qqq);
//        Bitmap bitmapExpanable = BitmapFactory.decodeResource(getResources(), R.mipmap.dog);

//        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmapExpanable, 612, 290, true);

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID_02)
                .setSmallIcon(R.mipmap.ic_notify)
                .setLargeIcon(bitmapExpanable)
                .setContentTitle("大图_可展开通知Title:ContentTitle")
                .setContentText("可展开通知Text:ContentTex_ContentTex_ContentTex_ContentTex_ContentTex_ContentTex_ContentTex_ContentTex_ContentTex_ContentTex_ContentTex_")
                .setContentIntent(pendingIntent)
                .setStyle(new NotificationCompat.BigPictureStyle()
                                .bigPicture(bitmapExpanable)
                                .bigLargeIcon(null)
//                        .setBigContentTitle("BigPictureStyle:BigContentTitle")//展开后用这个title
                        .setSummaryText("BigPictureStyle:SummaryTextSummary_TextSummaryText")//展开后用这个text
                )
                .build();

        //兼容8.0
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID_02,
                    "NAME_CHANNEL_ID_02", NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("CHANNEL_ID_01_Description");
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(3, notification);
    }

    private void sendExpandableNotificationForText() {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://blog.csdn.net/hfy8971613/article/details/85481124"));
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        Bitmap bitmapExpanable = BitmapFactory.decodeResource(getResources(), R.mipmap.expanablepic);

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID_02)
                .setSmallIcon(R.mipmap.ic_notify)
//                .setLargeIcon(bitmapExpanable)
                .setContentTitle("打文字_可展开通知Title:ContentTitle")
                .setContentText("可展开通知Text:ContentText_ContentText_ContentText_ContentText_ContentText_ContentText_ContentText_ContentText_ContentText_ContentText_")
                .setContentIntent(pendingIntent)
                .setStyle(new NotificationCompat.BigTextStyle().bigText("可展开通知Text:ContentText_ContentText_ContentText_ContentText_ContentText_ContentText_ContentText_ContentText_ContentText_ContentText_")
                )
                .build();

        //兼容8.0
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID_02,
                    "NAME_CHANNEL_ID_02", NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("CHANNEL_ID_01_Description");
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(3, notification);
    }

    /**
     * 悬挂通知
     * 直接显示在 屏幕上方，且焦点不变，会自动消失。
     */
    private void sendHangNotification() {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID_03);

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

    public static void sendCustomNotification(Context context, int iconId, String title, String msg, Bitmap bigPictureBitmap, Intent appIntent, String tag) {
        NotificationCompat.Builder notificationBuilder = null;
        if (notificationBuilder == null) {
            notificationBuilder = new NotificationCompat.Builder(context)
                    .setVibrate(new long[]{0, 100, 200, 300})
                    .setDefaults(Notification.DEFAULT_SOUND);
        }

        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.custom_notification);
        remoteViews.setImageViewBitmap(
                R.id.notification_large_icon,
                BitmapFactory.decodeResource(context.getResources(), R.drawable.common_full_open_on_phone));
        remoteViews.setTextViewText(R.id.notification_content, "" +
                "contentcontentcontentcontentcontentcontentcontentcontentcontentcontentcontentcontentcontent" +
                "contentcontentcontentcontentcontentcontentcontentcontentcontentcontentcontentcontentcontent" +
                "contentcontentcontentcontentcontentcontentcontentcontentcontentcontentcontentcontentcontent" +
                "contentcontentcontentcontentcontentcontentcontentcontentcontentcontentcontentcontentcontent" +
                "contentconten");
        remoteViews.setTextViewText(R.id.notification_title, "title");
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        remoteViews.setTextViewText(R.id.notification_time, format.format(new Date()));

        notificationBuilder.setContent(remoteViews);
        //we can do not use setContent in N version
        //use setCustomContentView intead is also ok


        notificationBuilder.setSmallIcon(R.mipmap.ic_notify);


        RemoteViews remoteViewBig = new RemoteViews(context.getPackageName(), R.layout.custom_notification_big);
        Notification notification;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                notification = notificationBuilder.build();
                notification.bigContentView = remoteViewBig;
            } else {
                notification = notificationBuilder.build();
            }
        } else {
            notificationBuilder.setCustomBigContentView(remoteViewBig);
            notification = notificationBuilder.build();
        }
        notificationBuilder.setContentText("context2");
        notificationBuilder.setContentTitle("title2");

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify((int) System.currentTimeMillis(), notification);
    }
}
