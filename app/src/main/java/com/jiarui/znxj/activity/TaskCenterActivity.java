package com.jiarui.znxj.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.jiarui.znxj.R;
import com.jiarui.znxj.adapter.OrderPageAdapter;
import com.jiarui.znxj.base.BaseActivity;
import com.jiarui.znxj.fragment.TaskCenterFragment1;
import com.jiarui.znxj.fragment.TaskCenterFragment2;
import com.jiarui.znxj.fragment.TaskCenterFragment3;
import com.jiarui.znxj.fragment.TaskCenterFragment4;

import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * 任务中心
 */
public class TaskCenterActivity extends BaseActivity {
    @ViewInject(R.id.mTabLayout)
    private TabLayout mTabLayout;//滑动的标题
    @ViewInject(R.id.viewpager)
    private ViewPager viewpager;
    private ArrayList<Fragment> fragmentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_center);
        x.view().inject(this);
        setTitle();
        mTvForTitle.setText("任务中心");
        InitViewPager();
    }

    public void InitViewPager() {
        List<String> mTieles = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            mTieles.add("预发布");
            mTieles.add("未开始");
            mTieles.add("进行中");
            mTieles.add("已完成");
        }
        fragmentList = new ArrayList<>();
        Fragment fragment[] = new Fragment[]{
                new TaskCenterFragment1(), new TaskCenterFragment3(), new TaskCenterFragment2(), new TaskCenterFragment4()};
        for (int i = 0; i < fragment.length; i++) {
            fragmentList.add(fragment[i]);
        }
        OrderPageAdapter orderPage = new OrderPageAdapter(getSupportFragmentManager(), TaskCenterActivity.this, fragmentList, mTieles);
        viewpager.setAdapter(orderPage);
        mTabLayout.setupWithViewPager(viewpager);
        //设置小红点
        View tabView = orderPage.getTabView(1);
        final TextView textnum = (TextView) tabView.findViewById(R.id.iv_tab_red);
        /**在这里判断每个TabLayout的内容是否有更新，来设置小红点是否显示**/
        mTabLayout.getTabAt(1).setCustomView(tabView);
        if (textnum.getText().toString().equals("0")) {
            textnum.setVisibility(View.GONE);
        } else {
            textnum.setVisibility(View.GONE);
        }
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                /**在这里记录TabLayout选中后内容更新已读标记**/
                View customView = tab.getCustomView();
                if (position == 1) {
                    textnum.setVisibility(View.INVISIBLE);
                    TextView textView = (TextView) customView.findViewById(R.id.tv_tab_title);
                    textView.setTextColor(getResources().getColor(R.color.them_color));
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                /**在这里记录TabLayout选中后内容更新已读标记**/
                View customView = tab.getCustomView();
                if (position == 1) {
                    TextView textView = (TextView) customView.findViewById(R.id.tv_tab_title);
                    textView.setTextColor(getResources().getColor(R.color.black));
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Event({R.id.common_title_left})
    private void onClick(View v) {
        switch (v.getId()) {
            case R.id.common_title_left:
                finish();
                break;
            default:
                break;
        }
    }
}
