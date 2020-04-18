package com.hfy.nestedscrolling.dagger2.component;

import com.hfy.nestedscrolling.MainActivity;
import com.hfy.nestedscrolling.dagger2.module.CarModule;
import com.hfy.nestedscrolling.dagger2.module.EngineModule;
import com.hfy.nestedscrolling.dagger2.module.GsonModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {GsonModule.class, EngineModule.class, CarModule.class})
public interface MainActivityComponent {
    void inject(MainActivity mainActivity);
}
