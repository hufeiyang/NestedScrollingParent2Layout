package com.hfy.demo01.module.mvp.dagger2;

import com.hfy.demo01.module.mvp.presenter.IpInfoPresenter;

import dagger.Component;

/**
 * IpInfoPresenter注入器，用于向注入IpInfoPresenter：model、view
 */
@Component(modules = IIpInfoViewModule.class)
public interface IpInfoPresenterComponent {
    void inject(IpInfoPresenter ipInfoPresenter);
}
