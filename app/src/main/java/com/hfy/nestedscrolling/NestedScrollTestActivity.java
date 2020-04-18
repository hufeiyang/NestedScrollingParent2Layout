package com.hfy.nestedscrolling;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.hfy.nested_scrolling_layout2.NestedScrollingParent2LayoutImpl1;
import com.hfy.nestedscrolling.adapter.HomePagerAdapter;
import com.hfy.nestedscrolling.fragment.NestedScrollTestFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 传统事件分发机制下的冲突处理--内外都是 竖向滑动的解决 及 缺陷点。
 * <p>
 * 可滑动的外层 + header + tab +viewPager +recyclerView
 * <p>
 * 可滑动的外层滑动冲突解决：1、传统方法，不连贯，2、使用NestedScrollingParent2Layout，OK
 * <p>
 * https://juejin.im/post/5d3e639e51882508dc163606#heading-13
 * https://www.jianshu.com/p/bc6d703e7ca9
 */
public class NestedScrollTestActivity extends AppCompatActivity {

    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.view_pager)
    ViewPager viewPager;
    @BindView(R.id.tv_head)
    TextView tvHead;

    //    private String[] titles = {"头条", "新闻", "娱乐"};
    private String[] titles = {"头条"};

    /**
     * 首页fragments
     */
    private List<Fragment> fragments = new ArrayList<Fragment>(titles.length);
    private boolean mIsNested;

    public static void launch(FragmentActivity activity, boolean isNested) {
        Intent intent = new Intent(activity, NestedScrollTestActivity.class);
        intent.putExtra("isNested", isNested);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        mIsNested = intent.getBooleanExtra("isNested", false);
        if (mIsNested) {
            setContentView(R.layout.activity_nested_scroll);
        } else {
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


        if (mIsNested) {
            NestedScrollingParent2LayoutImpl1 layoutImpl1 = findViewById(R.id.nested_scrolling_parent2_layout_impl1);
            layoutImpl1.setTopView(tvHead);
            layoutImpl1.setTabLayout(tabLayout);
            layoutImpl1.setViewPager(viewPager);
        }
    }
}
