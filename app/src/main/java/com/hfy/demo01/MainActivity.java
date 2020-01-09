package com.hfy.demo01;

import android.content.Intent;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hfy.demo01.dagger2.bean.Car;
import com.hfy.demo01.dagger2.bean.Watch;
import com.hfy.demo01.module.home.adapter.HomePagerAdapter;
import com.hfy.demo01.module.home.fragment.FirstFragment;
import com.hfy.demo01.module.home.fragment.SecondFragment;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.Lazy;

//import com.hfy.demo01.dagger2.component.DaggerMainActivityComponent;
//import com.hfy.demo01.dagger2.component.DaggerMainActivityComponent;

/**
 * @author hufy
 * @date
 */
public class MainActivity extends AppCompatActivity {

    private static final String TAG = "hfy";
    private static final int OVERLAY_PERMISSION_REQ_CODE = 1000;

    @BindView(R.id.tl_home_page)
    TabLayout mTlHomeTab;

    @BindView(R.id.vp_home_page)
    ViewPager mVpHomePage;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;

//    @BindView(R.id.nv_navigation)
//    NavigationView mNavigationView;

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

    private Handler mHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        String age = intent.getStringExtra("age");
        Log.i(TAG, "onCreate: name = " + name);
        Log.i(TAG, "onCreate: age = " + age);

        initConfig();

        initView();

        initData();

        //jenkins 在push到github后 自动构建，test

        testToast();

        testThreadLocal();

        testHandler();
    }


    /**
     * Handler 使用方法 test
     */
    private void testHandler() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                //1、准备looper，即threadLocal<Looper>.set(new Looper())
                Looper.prepare();

                //2、创建handler实例
                // 这个重写了handleMessage，handler是属于Handler的子类的实例
//                Handler handler = new Handler() {
//                    @Override
//                    public void handleMessage(Message msg) {
//                        super.handleMessage(msg);
//                    }
//                };

                //这种就是Handler的实例
                mHandler = new Handler(new Handler.Callback() {
                    @Override
                    public boolean handleMessage(Message msg) {
                        Log.i(TAG, "child thread 1, handleMessage: what="+msg.what);
                        return false;
                    }
                });

                //3、looper启动,sThreadLocal.get()拿到looper，拿到queue，开始queue.next
                Looper.loop();

            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                //4.1、Handler.post发送消息，queue.enqueueMessage(msg),即消息入队列。
                Log.i(TAG, "child thread2, mHandler.post");
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        Log.i(TAG, "child thread2, mHandler.post, run()");

                    }
                });
            }
        }).start();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //4.2、handler.sendMessage发送消息，queue.enqueueMessage(msg),即消息入队列。
        Log.i(TAG, "main thread, sendMessage");
        Message message = Message.obtain();
        message.what = 100;
        mHandler.sendMessage(message);

    }

    /**
     * threadLocal原理验证
     */
    private void testThreadLocal() {

        ThreadLocal<Boolean> booleanThreadLocal = new ThreadLocal<>();
        ThreadLocal<Integer> integerThreadLocal = new ThreadLocal<>();
        booleanThreadLocal.set(true);
        integerThreadLocal.set(0);
        Log.i(TAG, "testThreadLocal: main thread, boolean= "+booleanThreadLocal.get());
        Log.i(TAG, "testThreadLocal: main thread, int = "+integerThreadLocal.get());

        new Thread(new Runnable() {
            @Override
            public void run() {
                booleanThreadLocal.set(false);
                integerThreadLocal.set(1);
                Log.i(TAG, "testThreadLocal: a thread, boolean="+booleanThreadLocal.get());
                Log.i(TAG, "testThreadLocal: a thread, int = "+integerThreadLocal.get());

            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                booleanThreadLocal.set(null);
                integerThreadLocal.set(2);
                Log.i(TAG, "testThreadLocal: b thread, boolean="+booleanThreadLocal.get());
                Log.i(TAG, "testThreadLocal: b thread, int = "+integerThreadLocal.get());

            }
        }).start();
    }

    private void testToast() {
        Toast.makeText(this, "hehe", Toast.LENGTH_SHORT).show();
    }

    /**
     *
     */
    private void initData() {
        testDagger2();
    }

    private void testDagger2() {
        //调用此句，mWatch、mGson
//        DaggerMainActivityComponent.create().inject(this);
//        boolean equals = mWatch.equals("");
//        mWatch.work();

//        String json = "{'name':'hfy'}";
//        Man man = mGson.fromJson(json, Man.class);
//        Log.i(TAG, "initData: " + man.toString());
//        Log.i(TAG, "initData: mGson:" + mGson.hashCode() + ",mGson1:" + mGson1.hashCode());
//
//        Car car = mCarLazy.get();
//        car.run();
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

        initCustomWindow();
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
        Toast.makeText(this, "hehe", Toast.LENGTH_SHORT).show();

        //侧滑页面的导航菜单 选中监听
//        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
//                int itemId = menuItem.getItemId();
//                CharSequence title = menuItem.getTitle();
//                Toast.makeText(MainActivity.this, title, Toast.LENGTH_SHORT).show();
//                return true;
//            }
//        });
    }

    private void initCustomWindow() {
        //6.0以上需要用户手动打开权限
        // (SYSTEM_ALERT_WINDOW and WRITE_SETTINGS, 这两个权限比较特殊，
        // 不能通过代码申请方式获取，必须得用户打开软件设置页手动打开，才能授权。Manifest申请该权限是无效的。)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if (!Settings.canDrawOverlays(this)) {
                //打开设置页，让用户打开设置
                Toast.makeText(this, "can not DrawOverlays", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + MainActivity.this.getPackageName()));
                startActivityForResult(intent, OVERLAY_PERMISSION_REQ_CODE);
            }else {
                //已经打开了权限
                handleAddWindow();
            }
        }else {
            //6.0以下直接 Manifest申请该权限 就行。
            handleAddWindow();
        }
    }

    private void handleAddWindow() {
        Button view = new Button(this);
        view.setText("添加到window中的button");

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                0, 0,
                PixelFormat.TRANSPARENT
        );
        // flag 设置 Window 属性
        layoutParams.flags= WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;
        // type 设置 Window 类别（层级）
        layoutParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            layoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        }

        layoutParams.gravity = Gravity.TOP | Gravity.LEFT;
        layoutParams.x = 100;
        layoutParams.y = 100;

        WindowManager windowManager = getWindowManager();
        windowManager.addView(view, layoutParams);
    }


//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        //加载menu；如果actionbar存在就把items添加到actionbar
//        //注意，因为上面没有调用setSupportActionBar(mToolbar)，且主题中NoActionbar，即没有Actionbar。所以本方法也不用重写。
//        getMenuInflater().inflate(R.menu.main, menu);
//        return true;
//    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case OVERLAY_PERMISSION_REQ_CODE:
                if (Settings.canDrawOverlays(this)) {
                    //打开了权限
                    handleAddWindow();
                }else {
                    Toast.makeText(this, "can not DrawOverlays", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }
}
