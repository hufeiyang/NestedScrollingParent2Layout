package com.hfy.demo01.common.bean;

import com.google.firebase.messaging.RemoteMessage;

/**
 * @author hufy
 * @apiNote
 * @date 2019/4/18 9:35
 */
public class PushInfoEvent {

    /**
     * 接受到的push信息
     */
    RemoteMessage remoteMessage;

    public PushInfoEvent(RemoteMessage remoteMessage) {
        this.remoteMessage = remoteMessage;
    }

    public RemoteMessage getRemoteMessage() {
        return remoteMessage;
    }

    public void setRemoteMessage(RemoteMessage remoteMessage) {
        this.remoteMessage = remoteMessage;
    }
}
