package com.jiarui.znxj.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.view.View;

import com.jiarui.znxj.R;
import com.jiarui.znxj.adapter.OrderPageAdapter;
import com.jiarui.znxj.base.BaseActivity;
import com.jiarui.znxj.fragment.PatrolReportFragment;
import com.jiarui.znxj.fragment.ReservoirInforFragment;
import com.jiarui.znxj.fragment.WaterFragment;
import com.jiarui.znxj.widget.NoScrollViewPager;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * 数据查询
 */
public class QueryActivity extends BaseActivity implements View.OnClickListener{
    @ViewInject(R.id.mTabLayout)
    private TabLayout mTabLayout;//滑动的标题
    @ViewInject(R.id.viewpager)
    private NoScrollViewPager viewpager;
    private ArrayList<Fragment> fragmentList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query);
        x.view().inject(this);
        setTitle();
        mTvForTitle.setText("数据查询");
        mImgvForLeft.setOnClickListener(this);
        InitViewPager();
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.common_title_left:
                //返回
                finish();
                break;
        }
    }
    public void InitViewPager() {
        List<String> mTieles = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            mTieles.add("水库信息");
            mTieles.add("巡检报告");
            mTieles.add("水雨情");
        }
        fragmentList = new ArrayList<>();
        Fragment fragment[] = new Fragment[]{
                new ReservoirInforFragment(), new PatrolReportFragment(), new WaterFragment()};
        for (int i = 0; i < fragment.length; i++) {
            fragmentList.add(fragment[i]);
        }
        viewpager.setAdapter(new OrderPageAdapter(getSupportFragmentManager(), fragmentList, mTieles));
        mTabLayout.setupWithViewPager(viewpager);
    }


}
