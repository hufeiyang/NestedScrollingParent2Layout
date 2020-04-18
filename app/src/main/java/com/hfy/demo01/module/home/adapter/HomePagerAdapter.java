package com.hfy.demo01.module.home.adapter;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.List;

public class HomePagerAdapter extends FragmentPagerAdapter {

    private final String[] mTitles;
    private List<Fragment> mFragments;

    public HomePagerAdapter(FragmentManager fm, String[] titles, List<Fragment> fragments) {
        super(fm);
        mTitles = titles;
        mFragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {

        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mTitles.length;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles[position];
    }


}
