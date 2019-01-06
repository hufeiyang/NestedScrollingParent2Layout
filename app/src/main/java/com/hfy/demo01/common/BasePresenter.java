package com.hfy.demo01.common;

/**
 * BasePresenter
 * 定义取消请求的方法
 */
public interface BasePresenter {

    /**
     * 把Subscription添加到CompositeSubscription，用于统一取消多个请求
     */
    void addSubscribe();

    /**
     * 取消请求
     */
    void unsubscribe();
}
