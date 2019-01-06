package com.hfy.demo01.module.mvp.presenter;

import com.hfy.demo01.common.BasePresenter;

/**
 * IIpInfoPresenter
 */
public interface IIpInfoPresenter extends BasePresenter {

    /**
     * 获取IpInfo
     * @param ip ip
     */
    void getIpInfo(String ip);
}
