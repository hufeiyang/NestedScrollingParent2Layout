package com.hfy.nestedscrolling.common;

import com.hfy.nestedscrolling.module.mvp.model.IpInfo;

/**
 * HttpRequestCallBack
 */
public interface HttpRequestCallBack {
    void onStart();
    void onFinish();
    void onSuccess(IpInfo ipInfo);
    void onFailed(Throwable e);
}
