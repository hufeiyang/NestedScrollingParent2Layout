package com.hfy.demo01.module.home.touchevent.adapter;

import android.view.ViewTreeObserver;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.ViewPager;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.google.android.material.tabs.TabLayout;
import com.hfy.demo01.R;
import com.hfy.demo01.module.home.adapter.HomePagerAdapter;
import com.hfy.demo01.module.home.touchevent.fragment.DataBean;
import com.hfy.demo01.module.home.touchevent.fragment.NestedScrollTestFragment;
import com.hfy.demo01.module.home.touchevent.fragment.ViewPagerBean;
import com.hfy.demo01.module.home.touchevent.view.NestedScrollingParent2LayoutImpl3;
import com.hfy.simpleimageloader.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * @author hufeiyang
 */
public class RecyclerNestAdapter extends BaseMultiItemQuickAdapter<MultiItemEntity, BaseViewHolder> {

    public static final int NORMAL_TYPE = 1;
    public static final int VIEW_PAGE_TYPE = 2;

    private final FragmentActivity mActivity;

    private String[] titles = {"头条", "新闻", "娱乐"};

    private NestedScrollingParent2LayoutImpl3 mNestedScrollingParent2Layout;


    public RecyclerNestAdapter(FragmentActivity activity) {
        mActivity = activity;
        addItemType(NORMAL_TYPE, R.layout.item_nested_scroll_test);
        addItemType(VIEW_PAGE_TYPE, R.layout.item_view_page);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, MultiItemEntity multiItemEntity) {
        int itemViewType = baseViewHolder.getItemViewType();
        switch (itemViewType) {
            case NORMAL_TYPE:
                convertNormal(baseViewHolder, (DataBean) multiItemEntity);
                break;
            case VIEW_PAGE_TYPE:
                convertViewPager(baseViewHolder, (ViewPagerBean) multiItemEntity);
                break;
            default:
                break;
        }

    }

    private void convertViewPager(BaseViewHolder baseViewHolder, ViewPagerBean viewPagerBean) {
        List<Fragment> fragments = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            NestedScrollTestFragment fragment = new NestedScrollTestFragment();
            fragment.setIndex(i);
            fragment.setNestedParentLayout(mNestedScrollingParent2Layout);
            fragments.add(fragment);
        }


        HomePagerAdapter adapter = new HomePagerAdapter(mActivity.getSupportFragmentManager(), titles, fragments);
        ViewPager viewPager = (ViewPager) baseViewHolder.getView(R.id.view_pager);
        viewPager.setAdapter(adapter);

        TabLayout tab = (TabLayout) baseViewHolder.getView(R.id.tab_layout);
        tab.setupWithViewPager(viewPager);

        if (mNestedScrollingParent2Layout != null) {
            mNestedScrollingParent2Layout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    //设置最后一个item：tab+viewPager
                    mNestedScrollingParent2Layout.setLastItem(baseViewHolder.itemView);
                }
            });
        }
    }

    private void convertNormal(BaseViewHolder baseViewHolder, DataBean dataBean) {
        baseViewHolder.setText(R.id.textView, dataBean.text);
        ImageLoader.with(mActivity).loadBitmapAsync(dataBean.url, baseViewHolder.getView(R.id.imageView));
    }

    public void setNestedParentLayout(NestedScrollingParent2LayoutImpl3 nestedScrollingParent2Layout) {
        mNestedScrollingParent2Layout = nestedScrollingParent2Layout;
    }
}
