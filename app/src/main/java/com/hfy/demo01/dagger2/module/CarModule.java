package com.hfy.demo01.dagger2.module;

import com.hfy.demo01.dagger2.annotation.Color;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

@Module
public class CarModule {
    @Provides
    @Named("名字")
    public String providerCardName(){
        return "法拉利";
    }

    @Provides
    @Color
    public String providerCardColor(){
        return "黑色";
    }
}
