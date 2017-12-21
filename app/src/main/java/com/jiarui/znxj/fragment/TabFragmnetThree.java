package com.jiarui.znxj.fragment;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;

import com.jiarui.znxj.R;
import com.jiarui.znxj.adapter.MyFragmentPagerAdapter;
import com.jiarui.znxj.base.BaseFragment;
import com.jiarui.znxj.widget.PagerSlidingTabStrip;

import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

public class TabFragmnetThree extends BaseFragment {

    @ViewInject(R.id.tabs)
    private PagerSlidingTabStrip tabs;
    @ViewInject(R.id.pager)
    private ViewPager viewpager;

    private List<Fragment> mydata;

    private MessageGonggaoFragment myfragment1;// 公告

    private MessageGerenFragment myfragment2;// 个人消息

    private MessageTongxunFragment myfragment3;// 通讯录

    @Override
    protected View initView(LayoutInflater inflater) {
        mRootView = inflater.inflate(R.layout.frg_tabhost_three, null);
        return mRootView;
    }

    @Override
    public void initData() {
        setTitle();
        mTvForTitle.setText("消息");
        mImgvForLeft.setVisibility(View.INVISIBLE);
        myfragment1 = new MessageGonggaoFragment();
        myfragment2 = new MessageGerenFragment();
        myfragment3 = new MessageTongxunFragment();
        String[] mTitles = {"公告", "个人消息", "通讯录"};
        mydata = new ArrayList<Fragment>();
        mydata.add(myfragment1);
        mydata.add(myfragment2);
        mydata.add(myfragment3);
        viewpager.setAdapter(new MyFragmentPagerAdapter(
                getChildFragmentManager(), mTitles, mydata));
        viewpager.setOffscreenPageLimit(3);// 设置可以缓存多少个界面
        viewpager.setCurrentItem(1);
        tabs.setActivity(getActivity());
        tabs.setViewPager(viewpager);
    }

}
