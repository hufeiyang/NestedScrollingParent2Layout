package com.hfy.demo01.module.mvp.view;

import android.app.Activity;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.hfy.demo01.R;
import com.hfy.demo01.module.mvp.model.IpInfo;
import com.hfy.demo01.module.mvp.presenter.IpInfoPresenter;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Predicate;

/**
 * MvpActivity,用于练习使用： MVP 结合 RxJava、retrofit、Dagger2
 */
public class MvpActivity extends AppCompatActivity implements IIpInfoView {

    private static final String TAG = "hfy_IpInfoActivity";

    @Inject
    IpInfoPresenter mIpInfoPresenter;

    @BindView(R.id.btn_get_Ip_Info)
    Button mBtnGetIpInfo;

    @BindView(R.id.btn_test02)
    Button mBtnTest02;

    /**
     * launch MvpActivity
     */
    public static void launch(Activity activity) {
        Intent intent = new Intent();
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        ButterKnife.bind(this);
    }

    @OnClick({R.id.btn_get_Ip_Info,
            R.id.btn_test02})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_get_Ip_Info:
                getIpInfo();
                break;
            case R.id.btn_test02:
                testRx();
                break;
            default:
                break;
        }
    }

    private void testRx() {
        Observable.concat(Observable.just(1, 2, 3),
                Observable.just(4, 5, 6),
                Observable.just(7, 8, 9),
                Observable.just(10, 11, 12))
                .filter(new Predicate<Integer>() {
                    @Override
                    public boolean test(Integer integer) throws Exception {
                        return integer > 9;
                    }
                }).take(1)
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Integer value) {
                        Log.d(TAG, "接收到了事件" + value);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "对Error事件作出响应");
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "对Complete事件作出响应");
                    }
                });
    }

    private void getIpInfo() {
//        if (mIpInfoPresenter == null) {
//            mIpInfoPresenter = new IpInfoPresenter(this);
//        }

//        DaggerIpInfoActivityComponent.builder().iIpInfoViewModule(new IIpInfoViewModule(this)).build().inject(this);
//        mIpInfoPresenter.getIpInfo("59.108.54.37");

//        取消请求
//        mIpInfoPresenter.unsubscribe();
    }

    @Override
    public void onGetIpInfoSuccess(IpInfo ipInfo) {
        Log.i(TAG, "onGetIpInfoSuccess: " + ipInfo.toString());
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
