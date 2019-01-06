package com.hfy.demo01.module.mvp.presenter;

import com.hfy.demo01.common.HttpRequestCallBack;
import com.hfy.demo01.module.mvp.model.IpInfo;
import com.hfy.demo01.module.mvp.model.IpInfoModel;
import com.hfy.demo01.module.mvp.view.IIpInfoView;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;

public class IpInfoPresenter implements IIpInfoPresenter {

    /**
     * 使用dagger2 注入mView
     */
    @Inject
    IIpInfoView mView;

    /**
     * 使用dagger2 注入model
     */
    @Inject
    IpInfoModel mIpInfoModel;


//    List<Disposable> disposableList;

    private Disposable mDisposable;


    @Inject
    public IpInfoPresenter() {
//        disposableList = new ArrayList<>();
    }

    @Override
    public void getIpInfo(String ip) {

        //此时 mView、mIpInfoModel 都已经完成注入。所以 此处 不需要使用 IpInfoPresenterComponent 来注入了。
//        DaggerIpInfoPresenterComponent.builder().iIpInfoViewModule(new IIpInfoViewModule(mView)).build().inject(this);

        mDisposable = mIpInfoModel.execute(ip, new HttpRequestCallBack() {
            @Override
            public void onStart() {
                mView.onHttpRequestBegin();
            }

            @Override
            public void onFinish() {
                mView.onHttpRequestEnd();
            }

            @Override
            public void onSuccess(IpInfo ipInfo) {

                // TODO 此处可以 对 ipInfo 进行逻辑处理，然后 把最终结果给到 view 。 即，presenter 起到 逻辑层的作用。

                mView.onGetIpInfoSuccess(ipInfo);
            }

            @Override
            public void onFailed(Throwable e) {
                mView.onGetIpInfoFailed(e.getMessage());
            }
        });

        addSubscribe();
    }

    @Override
    public void addSubscribe() {
//        disposableList.add(mDisposable);
    }

    @Override
    public void unsubscribe() {
        mDisposable.dispose();
    }
}
