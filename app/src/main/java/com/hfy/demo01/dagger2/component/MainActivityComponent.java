package com.hfy.demo01.dagger2.component;

import com.hfy.demo01.MainActivity;
import com.hfy.demo01.dagger2.module.CarModule;
import com.hfy.demo01.dagger2.module.EngineModule;
import com.hfy.demo01.dagger2.module.GsonModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {GsonModule.class, EngineModule.class, CarModule.class})
public interface MainActivityComponent {
    void inject(MainActivity mainActivity);
}
