package com.jiarui.znxj.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.jiarui.znxj.R;
import com.jiarui.znxj.adapter.MyBaseExpandableListAdapter3;
import com.jiarui.znxj.base.BaseActivity;
import com.jiarui.znxj.bean.Group;
import com.jiarui.znxj.bean.Group_itemBean;
import com.jiarui.znxj.bean.Group_item_gridBean;
import com.jiarui.znxj.widget.MyExpandableListView;

import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

public class PreviewActivity extends BaseActivity {
    private ArrayList<Group> gData = null;//外面数据员
    private ArrayList<ArrayList<Group_itemBean>> iData = null;//item数据
    private ArrayList<Group_itemBean> lData = null;
    private MyBaseExpandableListAdapter3 myAdapter = null;
    @ViewInject(R.id.task_list)
    private MyExpandableListView task_list;
    List<Group_item_gridBean> mdata2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);
        x.view().inject(this);
        setTitle();
        mTvForTitle.setText("任务预览");
        mImgvForRight.setText("保存");
        init();
    }

    public void init() {
        //数据准备
        gData = new ArrayList<Group>();
        mdata2 = new ArrayList<>();
        iData = new ArrayList<ArrayList<Group_itemBean>>();
        gData.add(new Group("坝基", "正常"));
        gData.add(new Group("坝端", "异常"));
        gData.add(new Group("坝址近区", "异常"));
        gData.add(new Group("上游铺盖", "异常"));

        mdata2.add(new Group_item_gridBean(R.mipmap.logo));
        mdata2.add(new Group_item_gridBean(R.mipmap.logo));

        //AD组
        lData = new ArrayList<Group_itemBean>();
        lData.add(new Group_itemBean(R.mipmap.abnormal_nomar, "异常问题", "有裂逢，有渗水"));
        lData.add(new Group_itemBean(R.mipmap.device_nomar, "设备运转情况", "正常"));
        lData.add(new Group_itemBean(R.mipmap.repeat_icon, "重复问题", "否"));
        lData.add(new Group_itemBean(R.mipmap.describe_icon, "其他描述", "基岩有裂缝和断层"));
        lData.add(new Group_itemBean(R.mipmap.picture_icon, "照片/视频", mdata2));
        lData.add(new Group_itemBean(R.mipmap.record_nomar, "录音", ""));
        iData.add(lData);
        //AD组
        lData = new ArrayList<Group_itemBean>();
        lData.add(new Group_itemBean(R.mipmap.abnormal_nomar, "异常问题", "有裂逢，有渗水"));
        lData.add(new Group_itemBean(R.mipmap.device_nomar, "设备运转情况", "正常"));
        lData.add(new Group_itemBean(R.mipmap.repeat_icon, "重复问题", "否"));
        lData.add(new Group_itemBean(R.mipmap.describe_icon, "其他描述", "基岩有裂缝和断层"));
        lData.add(new Group_itemBean(R.mipmap.picture_icon, "照片/视频", mdata2));
        lData.add(new Group_itemBean(R.mipmap.record_nomar, "录音", ""));
        iData.add(lData);
        //AD组
        lData = new ArrayList<Group_itemBean>();
        lData.add(new Group_itemBean(R.mipmap.abnormal_nomar, "异常问题", "有裂逢，有渗水"));
        lData.add(new Group_itemBean(R.mipmap.device_nomar, "设备运转情况", "正常"));
        lData.add(new Group_itemBean(R.mipmap.repeat_icon, "重复问题", "否"));
        lData.add(new Group_itemBean(R.mipmap.describe_icon, "其他描述", "基岩有裂缝和断层"));
        lData.add(new Group_itemBean(R.mipmap.picture_icon, "照片/视频", mdata2));
        lData.add(new Group_itemBean(R.mipmap.record_nomar, "录音", ""));
        iData.add(lData);
        //AD组
        lData = new ArrayList<Group_itemBean>();
        lData.add(new Group_itemBean(R.mipmap.abnormal_nomar, "异常问题", "有裂逢，有渗水"));
        lData.add(new Group_itemBean(R.mipmap.device_nomar, "设备运转情况", "正常"));
        lData.add(new Group_itemBean(R.mipmap.repeat_icon, "重复问题", "否"));
        lData.add(new Group_itemBean(R.mipmap.describe_icon, "其他描述", "基岩有裂缝和断层"));
        lData.add(new Group_itemBean(R.mipmap.picture_icon, "照片/视频", mdata2));
        lData.add(new Group_itemBean(R.mipmap.record_nomar, "录音", ""));
        iData.add(lData);
        myAdapter = new MyBaseExpandableListAdapter3(gData, iData, this);
        task_list.setAdapter(myAdapter);
        Log.e("=============", "" + mdata2);
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
