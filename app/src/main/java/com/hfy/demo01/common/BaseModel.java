package com.hfy.demo01.common;

import io.reactivex.disposables.Disposable;

/**
 * BaseModel
 * @param <T> 请求参数类型
 */
public interface BaseModel<T> {


    /**
     * 执行请求
     * @param param 请求参数
     * @param callBack 请求回调
     * @return Disposable，用户取消请求
     */
    Disposable execute(T param, HttpRequestCallBack callBack);
}
