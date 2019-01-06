package com.hfy.demo01.module.mvp.dagger2;

import com.hfy.demo01.module.mvp.view.IIpInfoView;

import dagger.Module;
import dagger.Provides;

/**
 * 用于提供 view。（因为view，即activity不能直接用构造方法）
 */
@Module
public class IIpInfoViewModule {
    private IIpInfoView mView;

    public IIpInfoViewModule(IIpInfoView view) {
        this.mView = view;
    }

    @Provides
    public IIpInfoView provideIIpInfoView(){
        return mView;
    }
}
