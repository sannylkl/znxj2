package com.jiarui.znxj.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.jiarui.znxj.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/9/9 0009.
 */

public class OrderPageAdapter extends FragmentPagerAdapter {
    Context context;
    private ArrayList<Fragment> list;
    private List<String> mTieles;

    public OrderPageAdapter(FragmentManager fm, Context context, ArrayList<Fragment> list, List<String> mTieles) {
        super(fm);
        this.context = context;
        this.list = list;
        this.mTieles = mTieles;
    }
    public OrderPageAdapter(FragmentManager fm, ArrayList<Fragment> list, List<String> mTieles) {
        super(fm);
        this.list = list;
        this.mTieles = mTieles;
    }
    @Override
    public Fragment getItem(int position) {
        return list.get(position);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTieles.get(position);
    }

    public View getTabView(int position) {
        View tabView = LayoutInflater.from(context).inflate(R.layout.item_tab_layout, null);
        TextView tabTitle = (TextView) tabView.findViewById(R.id.tv_tab_title);
        tabTitle.setText(mTieles.get(position));
        return tabView;
    }

}
