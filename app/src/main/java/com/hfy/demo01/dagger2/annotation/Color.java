package com.hfy.demo01.dagger2.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Qualifier;

@Qualifier//用于自定义注解，用来 指定 需要什么样的 实例
@Retention(RetentionPolicy.RUNTIME)
public @interface Color {
}
