package com.hfy.demo01.module.home.touchevent;

import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import com.hfy.demo01.R;
import com.hfy.demo01.module.home.adapter.HomePagerAdapter;
import com.hfy.demo01.module.home.touchevent.fragment.NestedScrollTestFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 传统事件分发机制下的冲突处理--内外都是 竖向滑动的解决 及 缺陷点。
 *
 * 可滑动的外层 + header + tab +viewPager +recyclerView
 *
 * 可滑动的外层滑动冲突解决：1、传统方法，不连贯，2、使用NestedScrollingParent2Layout，OK
 *
 * https://juejin.im/post/5d3e639e51882508dc163606#heading-13
 * https://www.jianshu.com/p/bc6d703e7ca9
 */
public class NestedScrollTestActivity extends AppCompatActivity {

    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.view_pager)
    ViewPager viewPager;

//    private String[] titles = {"头条", "新闻", "娱乐"};
    private String[] titles = {"头条"};

    /**
     * 首页fragments
     */
    private List<Fragment> fragments = new ArrayList<Fragment>(titles.length);

    public static void launch(FragmentActivity activity, boolean isNested) {
        Intent intent = new Intent(activity, NestedScrollTestActivity.class);
        intent.putExtra("isNested",isNested);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        boolean isNested = intent.getBooleanExtra("isNested", false);
        if (isNested) {
            setContentView(R.layout.activity_nested_scroll);
        }else {
            setContentView(R.layout.activity_traditional_scroll);
        }
        ButterKnife.bind(this);

        initView();
    }

    private void initView() {
        fragments.add(new NestedScrollTestFragment());
//        fragments.add(new NestedScrollTestFragment());
//        fragments.add(new NestedScrollTestFragment());
        HomePagerAdapter adapter = new HomePagerAdapter(getSupportFragmentManager(), titles, fragments);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

    }
}
