package com.hfy.demo01.common.push;


import androidx.annotation.NonNull;
import android.util.Log;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.hfy.demo01.common.bean.PushInfoEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.Map;
import java.util.Set;

/**
 * This service is necessary to receive notifications in foregrounded apps, to receive data payload, to send upstream messages, and so on.
 *
 * @author hufy
 */
public class MYFirebaseMessagingService extends FirebaseMessagingService {

    private String TAG = "MYFirebaseMessagingService";


    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     * <p>
     *
     * 获取刷新的Token， 每当生成新令牌时，都会触发 onNewToken 回调函数。
     * 获取该令牌后，您可以将其发送到应用服务器，并使用您偏好的方法进行存储。
     *
     * @param token token
     */
    @Override
    public void onNewToken(String token) {
        Log.d(TAG, "Refreshed token: " + token);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.

//        sendRegistrationToServer(token);

    }

    /**
     * //how to get token in your activity
     * //TODO 在Activity中获取Token
     */
    private void getdeviceTokenInActivity() {
//        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(MyActivity.this, new OnSuccessListener<InstanceIdResult>() {
//            @Override
//            public void onSuccess(InstanceIdResult instanceIdResult) {
//                String newToken = instanceIdResult.getToken();
//                Log.e("newToken", newToken);
//
//            }
//        });

        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        String token = task.getResult().getToken();

                        // Log and toast
//                        String msg = getString(R.string.msg_token_fmt, token);
//                        Log.d(TAG, msg);
//                        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });

    }


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // ...

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());

            if (/* Check if data needs to be processed by long running job */ false) {
                // For long-running tasks (10 seconds or more) use Firebase Job Dispatcher.
//                scheduleJob();
            } else {
                // Handle message within 10 seconds
                handleNow(remoteMessage);
            }

        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
//            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }

    private void handleNow(RemoteMessage remoteMessage) {
        Map<String, String> data = remoteMessage.getData();
        Set<String> keySet = data.keySet();
        Set<Map.Entry<String, String>> entrySet = data.entrySet();
        for (String key : keySet) {
            Log.d(TAG, "Message Notification data: key = " + key + ", value = " + data.get(key));
        }

        RemoteMessage.Notification notification = remoteMessage.getNotification();
        Log.d(TAG, "Message Notification : " + notification.getTitle() + ", Body = " + notification.getBody());

        EventBus.getDefault().post(new PushInfoEvent(remoteMessage));

    }


}
