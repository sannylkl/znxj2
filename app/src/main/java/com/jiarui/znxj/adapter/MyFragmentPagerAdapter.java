package com.jiarui.znxj.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

public class MyFragmentPagerAdapter extends FragmentPagerAdapter {
    String[] mTitle;

    List<Fragment> mFragments = null;

    public MyFragmentPagerAdapter(FragmentManager fm, String[] titles,
                                  List<Fragment> Fragments) {
        super(fm);
        mTitle = titles;
        mFragments = Fragments;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitle[position];
    }

    @Override
    public int getCount() {
        return mTitle.length;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }
}
