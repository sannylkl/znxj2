package com.jiarui.znxj.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.jiarui.znxj.R;
import com.jiarui.znxj.activity.ViewFlowGotoActivity;
import com.jiarui.znxj.adapter.ImageAdapter;
import com.jiarui.znxj.base.BaseFragment;
import com.jiarui.znxj.bean.FlowViewBeanIndex;
import com.jiarui.znxj.widget.AdapterOnclick;
import com.jiarui.znxj.widget.CircleFlowIndicator;
import com.jiarui.znxj.widget.ViewFlow;

import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

public class TabFragmentOne extends BaseFragment {

    @ViewInject(R.id.vf)
    private ViewFlow mFv;

    @ViewInject(R.id.cfi)
    private CircleFlowIndicator mCfi;

    private ImageAdapter imageIndex;

    String img[] = {
            "http://img4.imgtn.bdimg.com/it/u=3767928281,3855971241&fm=21&gp=0.jpg",
            "http://img5.imgtn.bdimg.com/it/u=1472696957,3964242202&fm=21&gp=0.jpg",
            "http://img1.imgtn.bdimg.com/it/u=2526067219,1580563890&fm=21&gp=0.jpg"};

    String title[] = {"图片内容1111111111111", "图片内容222222222222",
            "图片内容3333333333"};

    private List<FlowViewBeanIndex> mdata;


    @Override
    protected View initView(LayoutInflater inflater) {
        mRootView = inflater.inflate(R.layout.frg_tabhost_one, null);
        return mRootView;
    }

    @Override
    public void initData() {
        mdata = new ArrayList<FlowViewBeanIndex>();
        for (int i = 0; i < img.length; i++) {
            FlowViewBeanIndex beanIndex = new FlowViewBeanIndex("", img[i],
                    title[i]);
            mdata.add(beanIndex);
        }
        viewflow();

    }

    private void viewflow() {
        imageIndex = new ImageAdapter(mContext, mdata);
        mFv.setAdapter(imageIndex);
        // 给ViewFlow设置缓冲数量，实际图片的张数
        mFv.setmSideBuffer(mdata.size());
        // 将圆点指示器添加到ViewFlow中
        mFv.setFlowIndicator(mCfi);
        // 设置时间的跨度（轮播图片的间隔）
        mFv.setTimeSpan(5500);
        // 设置初始位置
        mFv.setSelection(0);
        // 启动自动播放
        mFv.startAutoFlowTimer();
        imageIndex.setMclick(new AdapterOnclick() {

            @Override
            public void adapterClickx(int whic, int position) {
                Bundle bundle = new Bundle();
                bundle.putString("title", mdata.get(position % mdata.size())
                        .getTitle());
                bundle.putString("img", mdata.get(position % mdata.size())
                        .getImg());
                Intent to = new Intent();
                to.setClass(mContext, ViewFlowGotoActivity.class);
                to.putExtras(bundle);
                startActivity(to);
            }
        });
    }
}
