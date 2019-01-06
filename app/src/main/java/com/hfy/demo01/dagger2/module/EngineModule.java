package com.hfy.demo01.dagger2.module;

import com.hfy.demo01.dagger2.bean.DieselEngine;
import com.hfy.demo01.dagger2.bean.Engine;
import com.hfy.demo01.dagger2.bean.GasolineEngine;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

@Module
public class EngineModule {
    @Provides
    @Named("GasolineEngine")
    public Engine provideGasolineEngine(){
        return new GasolineEngine();
    }

    @Provides
    @Named("DieselEngine")
    public Engine provideDieselEngine(){
        return new DieselEngine();
    }
}
