package com.hfy.demo01.common.push;


import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * @author hufy
 * @apiNote
 * @date 2019/4/17 19:55
 *
 * This class is deprecated.
 * In favour of overriding onNewToken in FirebaseMessagingService. Once that has been implemented, this service can be safely removed.
 * TODO 所以 MYFirebaseMessagingService 中重写onNewToken方法，然后 此类就可删除了~
 */
@Deprecated
public class MyFirebaseInstanceIdService extends FirebaseInstanceIdService {

    private String TAG = "MyFirebaseInstanceIdService";

    /**
     * This method is deprecated.（过时）
     * In favour of overriding onNewToken in FirebaseMessagingService. This method will be invoked on token changes even if onNewToken is also used.
     */
    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.

//        sendRegistrationToServer(refreshedToken);
    }
}
