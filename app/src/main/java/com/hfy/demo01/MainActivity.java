package com.hfy.demo01;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.gson.Gson;
import com.hfy.demo01.module.home.adapter.HomePagerAdapter;
import com.hfy.demo01.dagger2.bean.Car;
import com.hfy.demo01.dagger2.bean.Man;
import com.hfy.demo01.dagger2.bean.Watch;
import com.hfy.demo01.dagger2.component.DaggerMainActivityComponent;
import com.hfy.demo01.module.home.fragment.FirstFragment;
import com.hfy.demo01.module.home.fragment.SecondFragment;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.Lazy;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "hfy";

    @BindView(R.id.tl_home_page)
    TabLayout mTlHomeTab;

    @BindView(R.id.vp_home_page)
    ViewPager mVpHomePage;

    /****** Dagger2 练习*****/
    @Inject
    Watch mWatch;

    @Inject
    Gson mGson;

    @Inject
    Gson mGson1;

    @Inject
    Lazy<Car> mCarLazy;//懒加载



    private String[] titles = {"头条", "新闻", "娱乐"};

    /**
     * 首页fragments
     */
    private List<Fragment> fragments = new ArrayList<Fragment>(titles.length);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initConfig();

        initView();

        initData();
    }

    private void initData() {
        testDagger2();
    }

    private void testDagger2() {
        //调用此句，mWatch、mGson
        DaggerMainActivityComponent.create().inject(this);
        boolean equals = mWatch.equals("");
        mWatch.work();

        String json = "{'name':'hfy'}";
        Man man = mGson.fromJson(json, Man.class);
        Log.i(TAG, "initData: "+ man.toString());
        Log.i(TAG, "initData: mGson:" + mGson.hashCode() +",mGson1:" + mGson1.hashCode());

        Car car = mCarLazy.get();
        car.run();
    }

    private void initConfig() {
        ButterKnife.bind(this);
    }

    private void initView() {
        fragments.add(new FirstFragment());
        fragments.add(new SecondFragment());
        fragments.add(new FirstFragment());
        HomePagerAdapter adapter =  new HomePagerAdapter(getSupportFragmentManager(), titles, fragments);
        mVpHomePage.setAdapter(adapter);

        //TabLayout关联ViewPage
        mTlHomeTab.setupWithViewPager(mVpHomePage);
}
    }
