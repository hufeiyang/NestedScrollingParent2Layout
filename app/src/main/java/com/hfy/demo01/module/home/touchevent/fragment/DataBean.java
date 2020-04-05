package com.hfy.demo01.module.home.touchevent.fragment;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.hfy.demo01.module.home.touchevent.adapter.RecyclerNestAdapter;

public class DataBean implements MultiItemEntity {

    public  String text;
    public   String url;

    public DataBean(String text, String url) {
        this.text = text;
        this.url = url;
    }

    @Override
    public int getItemType() {
        return RecyclerNestAdapter.NORMAL_TYPE;
    }
}
