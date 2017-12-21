package com.jiarui.znxj.fragment;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.jiarui.znxj.R;
import com.jiarui.znxj.adapter.OrderPageAdapter;
import com.jiarui.znxj.application.AppContext;
import com.jiarui.znxj.base.BaseFragment;
import com.jiarui.znxj.widget.NoScrollViewPager;
import com.jiarui.znxj.widget.TabLayoutUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/9/11 0011.
 * 水雨晴
 */

public class WaterFragment extends BaseFragment {

    @Bind(R.id.water_yl)
    TextView waterYl;
    @Bind(R.id.water_sw)
    TextView waterSw;
    @Bind(R.id.water_dqkr)
    TextView waterDqkr;
    @Bind(R.id.water_xsw)
    TextView waterXsw;
    @Bind(R.id.water_wz)
    TextView waterWz;
    @Bind(R.id.mTabLayout)
    TabLayout mTabLayout;
    @Bind(R.id.viewpager)
    NoScrollViewPager viewpager;
    private ArrayList<Fragment> fragmentList;

    @Override
    protected View initView(LayoutInflater inflater) {
        mRootView = inflater.inflate(R.layout.water_fragment, null);
        ButterKnife.bind(this, mRootView);
        viewpager.setNoScroll(true);
        InitViewPager();
        return mRootView;
    }

    @Override
    public void initData() {

    }

    public void InitViewPager() {
        List<String> mTieles = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            mTieles.add("历史水位查询");
            mTieles.add("历史雨量查询");
        }
        fragmentList = new ArrayList<>();
        Fragment fragment[] = new Fragment[]{
                new HistoricalWater(), new HistoricalRainfall()};
        for (int i = 0; i < fragment.length; i++) {
            fragmentList.add(fragment[i]);
        }
        viewpager.setAdapter(new OrderPageAdapter(getChildFragmentManager(), fragmentList, mTieles));
        mTabLayout.setupWithViewPager(viewpager);
        mTabLayout.post(new Runnable() {
            @Override
            public void run() {
                TabLayoutUtil.setIndicatorPx(mTabLayout, (int) (AppContext.screenWidth * 0.07), (int) (AppContext.screenWidth * 0.07));
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
