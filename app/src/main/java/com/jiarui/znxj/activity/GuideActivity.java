package com.jiarui.znxj.activity;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.jiarui.znxj.R;
import com.jiarui.znxj.base.BaseActivity;
import com.jiarui.znxj.constants.InterfaceDefinition;
import com.jiarui.znxj.utils.PreferencesUtil;

import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * 引导页面
 *
 * @author PS
 * @version 1.0
 * @date 2016年1月20日
 */

public class GuideActivity extends BaseActivity {

    @ViewInject(R.id.mViewPager)
    private ViewPager mViewPager;
    @ViewInject(R.id.lines)
    private LinearLayout lines;
    private List<View> data = null;
    // 存放引导页图片
    private int[] pping = {R.mipmap.yd1, R.mipmap.yd2, R.mipmap.yd3,
            R.mipmap.yd4};

    // 引导页最后一页按钮
    @ViewInject(R.id.mButton)
    private View mView;

    // 点击按钮跳转主页面
    @Event({R.id.mButton})
    private void onClick(View v) {
        switch (v.getId()) {
            case R.id.mButton:
                // 第一次进入
                PreferencesUtil.put(this, InterfaceDefinition.PreferencesUser.WELCOME_STATE, true);
                gotoActivity(MainActivity.class);
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_guide);
        x.view().inject(this);
        initDate();
    }

    private void initDate() {

        data = new ArrayList<View>();
        for (int i = 0; i < pping.length; i++) {
            ImageView imageView = new ImageView(this);
            imageView.setBackgroundResource(pping[i]);
            data.add(imageView);
        }
        MyViewpager myViewpager = new MyViewpager(data);
        mViewPager.setAdapter(myViewpager);
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            public void onPageSelected(int arg0) {
                // 判断按钮显隐藏
                if (arg0 == 3) {
                    mView.setVisibility(View.VISIBLE);
                } else {
                    mView.setVisibility(View.GONE);
                }
            }

            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            public void onPageScrollStateChanged(int arg0) {
            }
        });
    }

    class MyViewpager extends PagerAdapter {

        List<View> views;

        public MyViewpager(List<View> views) {
            this.views = views;
        }

        public int getCount() {
            return views.size();
        }

        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(views.get(position));
        }

        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(views.get(position));
            return views.get(position);
        }

        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

    }

}
