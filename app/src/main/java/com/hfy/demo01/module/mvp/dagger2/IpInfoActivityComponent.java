package com.hfy.demo01.module.mvp.dagger2;

import com.hfy.demo01.module.mvp.view.MvpActivity;

import dagger.Component;

/**
 * IpInfoActivityComponent, 用于向IpInfoActivity 注入 presenter
 */
@Component(modules = IIpInfoViewModule.class)//因为IpInfoActivity需要的presenter 内部 需要IIpInfoView，所以这里 要加 IIpInfoViewModule
public interface IpInfoActivityComponent {
    void inject(MvpActivity ipInfoActivity);
}
