package com.hfy.demo01.dagger2.module;

import com.google.gson.Gson;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * 依赖第三方的实例时，使用@Module、@Provides 提供 实例
 */
@Module
public class GsonModule {

    @Singleton //单例
    @Provides
    public Gson provideGson(){
        return new Gson();
    }
}
