package com.hfy.nestedscrolling.common;

/**
 * BaseView
 * 定义公共方法
 */
public interface BaseView {

    /**
     * 请求开始
     * 实现者可以showLoading等
     */
    void onHttpRequestBegin();

    /**
     * 请求结束
     * 实现者可以dismissLoading
     */
    void onHttpRequestEnd();
}
