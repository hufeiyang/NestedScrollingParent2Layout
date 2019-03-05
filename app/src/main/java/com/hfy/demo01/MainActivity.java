package com.hfy.demo01;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hfy.demo01.dagger2.bean.Car;
import com.hfy.demo01.dagger2.bean.Man;
import com.hfy.demo01.dagger2.bean.Watch;
import com.hfy.demo01.dagger2.component.DaggerMainActivityComponent;
import com.hfy.demo01.module.home.adapter.HomePagerAdapter;
import com.hfy.demo01.module.home.fragment.FirstFragment;
import com.hfy.demo01.module.home.fragment.SecondFragment;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.Lazy;

/**
 * @author hufy
 * @date
 */
public class MainActivity extends AppCompatActivity {

    private static final String TAG = "hfy";

    @BindView(R.id.tl_home_page)
    TabLayout mTlHomeTab;

    @BindView(R.id.vp_home_page)
    ViewPager mVpHomePage;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;

    @BindView(R.id.nv_navigation)
    NavigationView mNavigationView;

    /****** Dagger2 练习*****/
    @Inject
    Watch mWatch;

    @Inject
    Gson mGson;

    @Inject
    Gson mGson1;

    /**
     * 懒加载
     */
    @Inject
    Lazy<Car> mCarLazy;

    He he = new He();

    private String[] titles = {"头条", "新闻", "娱乐"};

    /**
     * 首页fragments
     */
    private List<Fragment> fragments = new ArrayList<Fragment>(titles.length);

    private ActionBarDrawerToggle mActionBarDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initConfig();

        initView();

        initData();

        //jenkins 在push到github后 自动构建，test
    }

    /**
     *
     */
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
        Log.i(TAG, "initData: " + man.toString());
        Log.i(TAG, "initData: mGson:" + mGson.hashCode() + ",mGson1:" + mGson1.hashCode());

        Car car = mCarLazy.get();
        car.run();
    }

    private void initConfig() {
        ButterKnife.bind(this);
    }

    private void initView() {
        //PagerAdapter
        fragments.add(new FirstFragment());
        fragments.add(new SecondFragment());
        fragments.add(new FirstFragment());
        HomePagerAdapter adapter = new HomePagerAdapter(getSupportFragmentManager(), titles, fragments);
        mVpHomePage.setAdapter(adapter);

        //TabLayout关联ViewPage
        mTlHomeTab.setupWithViewPager(mVpHomePage);

        initToolbar();

        initDrawerLayout();
    }

    private void initToolbar() {
        //设置Toolbar：意思是把Toolbar当做ActionBar来用。实际上可以不用这句。
//        setSupportActionBar(mToolbar);

        //设置menu（直接用mToolbar设置menu。因为上面没有设置setSupportActionBar(mToolbar)，即不用重写onCreateOptionsMenu(Menu menu)）
        mToolbar.inflateMenu(R.menu.main);

        //设置左侧箭头 监听
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "返回", Toast.LENGTH_SHORT).show();
            }
        });

        //设置menu item 点击监听
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                int itemId = menuItem.getItemId();
                switch (itemId) {
                    case R.id.item_share:
                        Toast.makeText(MainActivity.this, "分享", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.item_settings:
                        Toast.makeText(MainActivity.this, "设置", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }
                return false;
            }
        });


//        View close = findViewById(R.id.tv_close);
//        close.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mDrawerLayout.closeDrawer(Gravity.LEFT);
//            }
//        });

        //Toolbar的背景设置为 palette从图片提取到的活力色 (目的是动态 适应当前界面的色调)
//        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.dog);
//        Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
//            @Override
//            public void onGenerated(@Nullable Palette palette) {
//                Palette.Swatch vibrantSwatch = palette.getVibrantSwatch();
//                mToolbar.setBackgroundDrawable(new ColorDrawable(vibrantSwatch.getRgb()));
//            }
//        });

    }

    private void initDrawerLayout() {
        //用DrawerLayout实现侧滑
        mActionBarDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.drawer_open, R.string.close);
        mActionBarDrawerToggle.syncState();

        mDrawerLayout.addDrawerListener(mActionBarDrawerToggle);

        //侧滑页面的导航菜单 选中监听
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int itemId = menuItem.getItemId();
                CharSequence title = menuItem.getTitle();
                Toast.makeText(MainActivity.this, title, Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }


//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        //加载menu；如果actionbar存在就把items添加到actionbar
//        //注意，因为上面没有调用setSupportActionBar(mToolbar)，且主题中NoActionbar，即没有Actionbar。所以本方法也不用重写。
//        getMenuInflater().inflate(R.menu.main, menu);
//        return true;
//    }


}
