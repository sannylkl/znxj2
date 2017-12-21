package com.jiarui.znxj.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.jiarui.znxj.R;
import com.jiarui.znxj.activity.SeeActivity;
import com.jiarui.znxj.adapter.CommonAdapter;
import com.jiarui.znxj.adapter.MineQBListAdapter;
import com.jiarui.znxj.adapter.TaskRouteAdapter;
import com.jiarui.znxj.application.AppContext;
import com.jiarui.znxj.bean.TaskCenterBean;
import com.jiarui.znxj.bean.TaskCenter_listBean;
import com.jiarui.znxj.bean.TaslListBean;
import com.jiarui.znxj.bean.TaslListBeanTable;
import com.jiarui.znxj.bean.TaslListDataBean;
import com.jiarui.znxj.bean.Xj_ReservoirsBean;
import com.jiarui.znxj.bean.Xj_TaskBean;
import com.jiarui.znxj.bean.xj_route_detailBean;
import com.jiarui.znxj.constants.GetToken;
import com.jiarui.znxj.constants.InterfaceDefinition;
import com.jiarui.znxj.grobal.CommonListener;
import com.jiarui.znxj.holder.ViewHolder;
import com.jiarui.znxj.utils.DateUtil;
import com.jiarui.znxj.utils.PacketUtil;
import com.jiarui.znxj.utils.PreferencesUtil;
import com.jiarui.znxj.utils.ToastUtil;
import com.jiarui.znxj.widget.AutoListView;
import com.jiarui.znxj.widget.ListViewForScrollView;

import org.json.JSONObject;
import org.xutils.DbManager;
import org.xutils.db.sqlite.WhereBuilder;
import org.xutils.ex.DbException;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *已完成 on 2017/9/9 0009.
 */

public class TaskCenterFragment4 extends  LocalBaseFragmentRefreshLoad {
    // 每页显示多少条数据
    private int mPageSize = 10;
    // 第几页
    private int mPage = 1;
    /*任务列表数适配器*/
    private List<TaslListDataBean> mtalist;
    CommonAdapter<TaslListDataBean> commonAdapter;
    /*数据库*/
    DbManager dbManager = null;//AppContext.getInstance().getDbManager()
    boolean isGS=true;
    @Override
    protected View initView(LayoutInflater inflater) {
        mRootView = inflater.inflate(R.layout.taskcenterfragment3, null);
        return mRootView;
    }
    String user_id,ReservoId=null;
    @Override
    public void initData() {
        user_id = (String) PreferencesUtil.get(mContext,InterfaceDefinition.PreferencesUser.USER_ID, "");
        ReservoId=(String) PreferencesUtil.get(mContext,InterfaceDefinition.PreferencesUser.ReservoId, "-1");
        dbManager = x.getDb(AppContext.getConfig());
        setRefreshLoadListView();
        init();
        LoadResultData(AutoListView.REFRESH);
    }

    public void init() {
        mtalist = new ArrayList<TaslListDataBean>();
        commonAdapter = new CommonAdapter<TaslListDataBean>(mContext, mtalist, R.layout.taskcenter_time) {
            @Override
            public void convert(ViewHolder mHolder, TaslListDataBean item, int position) {
                mHolder.setText(R.id.task_item_text1, item.getTask_type());//任务类型
                mHolder.setText(R.id.task_item_text3, item.getReservoir());//水库名称
                mHolder.setText(R.id.task_item_text4, item.getCreator());//发布者
                mHolder.setText(R.id.task_item_text5, item.getItems());//检查项
                String date_star = DateUtil.timeStamp2Date(String.valueOf(item.getStart_time()), "MM/dd");
                String date_end = DateUtil.timeStamp2Date(String.valueOf(item.getEnd_time()), "MM/dd");
                mHolder.setText(R.id.task_item_text6, "" + date_star + "-" + date_end);//任务时间

                mHolder.setText(R.id.task_item_text8, "关闭任务");//操作按钮文字
                mHolder.setBackgroundResource(R.id.task_item_text8, R.drawable.text_hei);
                //获取巡检路线,
                ListViewForScrollView item_list = (ListViewForScrollView) mHolder.getConvertView().findViewById(R.id.task_list2);
                try {
                    List<xj_route_detailBean> xj_routelist = dbManager.selector(xj_route_detailBean.class).where("route_id", "=", item.getRoute_id()).limit(2).findAll();
                    if (xj_routelist != null) {
                        TaskRouteAdapter taskRouteAdapter = new TaskRouteAdapter(mContext, xj_routelist);
                        item_list.setAdapter(taskRouteAdapter);
                    }
                } catch (DbException e) {
                    e.printStackTrace();
                }
                mHolder.setOnClickListener(R.id.task_item_text7, position, new CommonListener() {
                    @Override
                    public void commonListener(View view, int position) {
                        Bundle bundle = new Bundle();
                        bundle.putInt("id", mtalist.get(position).getId());
                        gotoAct(bundle, SeeActivity.class);//任务详情
                    }
                });

                mHolder.setOnClickListener(R.id.task_item_text8, position, new CommonListener() {
                    @Override
                    public void commonListener(View view, int position) {
                        ToastUtil.TextToast("任务完成");
                    }
                });
            }
        };

    }

    @Override
    void clearResultData() {
        mtalist.clear();
    }

    @Override
    void disposeResultData() {
        try {
            WhereBuilder b = WhereBuilder.b();
            b.and("status", "==", "2");//条件
            b.and("uid", "=", user_id);
            List<TaslListBeanTable> mtalistdb = dbManager.selector(TaslListBeanTable.class).where(b).findAll();//.where("status", "==", "0").and("start_time",">",date).
            if (mtalistdb != null) {
                //存放数据到适配器的数据源里
                String taskStatus = "";//状态
                String tacktype = "未知";//类型
                for (int db = 0; db < mtalistdb.size(); db++) {
                    if (mtalistdb.get(db).getTask_type().equals("daily")) {
                        tacktype = "日常任务";
                    } else if (mtalistdb.get(db).getTask_type().equals("regular")) {
                        tacktype = "定期任务";
                    } else if (mtalistdb.get(db).getTask_type().equals("special")) {
                        tacktype = "特别检查";
                    }

                    TaslListDataBean taslListDataBean = new TaslListDataBean(
                            mtalistdb.get(db).getId(), mtalistdb.get(db).getStart_time(),
                            mtalistdb.get(db).getEnd_time(), mtalistdb.get(db).getReservoir(),
                            mtalistdb.get(db).getCreator(), mtalistdb.get(db).getRoute_id(),
                            mtalistdb.get(db).getItems(), taskStatus,
                            tacktype, mtalistdb.get(db).getReseid());
                    mtalist.add(taslListDataBean);
                }

            }
        } catch (DbException e) {
            e.printStackTrace();
        }
        Log.e("进行中commonAdapter", "" + commonAdapter.getCount());
        //判断有没有数据
        if (commonAdapter.getCount() == 0) {
            EmptyView.setVisibility(View.VISIBLE);
        } else {
            EmptyView.setVisibility(View.GONE);
        }
        onFinish();
    }
    @Override
    void notifyDataSetChanged() {
        commonAdapter.notifyDataSetChanged();
        setFirstLoad(false);
    }

    @Override
    void LoadResultData(int what) {
        if (what == AutoListView.LOAD) {
            disposeResultData();
        }else{
            clearResultData();
            disposeResultData();
        }
    }
}