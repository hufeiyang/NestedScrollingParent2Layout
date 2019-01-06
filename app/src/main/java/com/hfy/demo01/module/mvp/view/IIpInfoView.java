package com.hfy.demo01.module.mvp.view;

import com.hfy.demo01.module.mvp.model.IpInfo;
import com.hfy.demo01.common.BaseView;

/**
 * IIpInfoView
 */
public interface IIpInfoView extends BaseView {
    /**
     * ipInfo 获取成功
     * @param ipInfo
     */
    void onGetIpInfoSuccess(IpInfo ipInfo);

    /**
     * IpInfo获取失败
     * @param e
     */
    void onGetIpInfoFailed(String e);
}
