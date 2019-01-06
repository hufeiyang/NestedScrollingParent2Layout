package com.hfy.demo01.common;

import com.hfy.demo01.module.mvp.model.IpInfo;

/**
 * HttpRequestCallBack
 */
public interface HttpRequestCallBack {
    void onStart();
    void onFinish();
    void onSuccess(IpInfo ipInfo);
    void onFailed(Throwable e);
}
