package com.hfy.demo01.module.mvp.view;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.hfy.demo01.R;
import com.hfy.demo01.module.mvp.dagger2.DaggerIpInfoActivityComponent;
import com.hfy.demo01.module.mvp.dagger2.IIpInfoViewModule;
import com.hfy.demo01.module.mvp.model.IpInfo;
import com.hfy.demo01.module.mvp.presenter.IpInfoPresenter;

import javax.inject.Inject;

/**
 * MvpActivity,用于练习使用： MVP 结合 RxJava、Dagger2
 */
public class MvpActivity extends AppCompatActivity implements IIpInfoView{

    private static final String TAG = "hfy_IpInfoActivity";

    @Inject
    IpInfoPresenter mIpInfoPresenter;

    /**
     * launch MvpActivity
     */
    public static void launch(Activity activity) {
        Intent intent = new Intent(activity, MvpActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        initData();
    }

    private void initData() {
//        if (mIpInfoPresenter == null) {
//            mIpInfoPresenter = new IpInfoPresenter(this);
//        }

        DaggerIpInfoActivityComponent.builder().iIpInfoViewModule(new IIpInfoViewModule(this)).build().inject(this);
        mIpInfoPresenter.getIpInfo("59.108.54.37");

//        取消请求
//        mIpInfoPresenter.unsubscribe();
    }

    @Override
    public void onGetIpInfoSuccess(IpInfo ipInfo) {
        Log.i(TAG, "onGetIpInfoSuccess: " +ipInfo.toString());
    }

    @Override
    public void onGetIpInfoFailed(String message) {
        Toast.makeText(this, "onGetIpInfoFailed: " + message, Toast.LENGTH_SHORT).show();
        Log.i(TAG, "onGetIpInfoFailed: " + message);
    }

    @Override
    public void onHttpRequestBegin() {
        Toast.makeText(this, "onHttpRequestBegin", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onHttpRequestEnd() {
        Toast.makeText(this, "onHttpRequestEnd", Toast.LENGTH_SHORT).show();

    }
}
