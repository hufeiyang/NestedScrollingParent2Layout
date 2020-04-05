package com.hfy.demo01.module.home.touchevent.fragment;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.hfy.demo01.module.home.touchevent.adapter.RecyclerNestAdapter;

public class ViewPagerBean implements MultiItemEntity {
    @Override
    public int getItemType() {
        return RecyclerNestAdapter.VIEW_PAGE_TYPE;
    }
}
