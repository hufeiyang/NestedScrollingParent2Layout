package com.hfy.demo01.module.mvp.model;

import com.hfy.demo01.common.BaseModel;
import com.hfy.demo01.common.HttpRequestCallBack;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * IpInfoModel
 * 使用 RxJava 结合 Retrofit 实现 网络请求。
 */
public class IpInfoModel implements BaseModel<String> {

    @Inject
    public IpInfoModel() {
    }

    /**
     * HOST
     */
    private static final String HOST = "http://ip.tabao.com/service/";

    private Disposable mDisposable;

    @Override
    public Disposable execute(String ip, HttpRequestCallBack callBack) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(HOST)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        IpService ipService = retrofit.create(IpService.class);
        Observable<IpInfo> ipInfoObservable = ipService.getIpInfo(ip);
        ipInfoObservable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<IpInfo>() {
                    @Override
                    public void onSubscribe(Disposable disposable) {
                        mDisposable = disposable;
                        callBack.onStart();
                    }

                    @Override
                    public void onNext(IpInfo ipInfo) {
                        callBack.onSuccess(ipInfo);
                    }

                    @Override
                    public void onError(Throwable e) {
                        callBack.onFailed(e);
                    }

                    @Override
                    public void onComplete() {
                        callBack.onFinish();
                    }
                });


        return mDisposable;
    }
}
