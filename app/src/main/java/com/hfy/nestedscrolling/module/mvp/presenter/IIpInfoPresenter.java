package com.hfy.nestedscrolling.module.mvp.presenter;

import com.hfy.nestedscrolling.common.BasePresenter;

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
