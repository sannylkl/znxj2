package com.jiarui.znxj.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.jiarui.znxj.R;
import com.jiarui.znxj.activity.InspectActivity;
import com.jiarui.znxj.activity.SeeActivity;
import com.jiarui.znxj.adapter.CommonAdapter;
import com.jiarui.znxj.adapter.TaskRouteAdapter;
import com.jiarui.znxj.application.AppContext;
import com.jiarui.znxj.base.BaseFragment;
import com.jiarui.znxj.bean.DataSynEvent;
import com.jiarui.znxj.bean.TaslListBeanTable;
import com.jiarui.znxj.bean.TaslListDataBean;
import com.jiarui.znxj.bean.xj_route_detailBean;
import com.jiarui.znxj.constants.InterfaceDefinition;
import com.jiarui.znxj.grobal.CommonListener;
import com.jiarui.znxj.holder.ViewHolder;
import com.jiarui.znxj.utils.DateUtil;
import com.jiarui.znxj.utils.DefaultCommonCallBack;
import com.jiarui.znxj.utils.LogUtil;
import com.jiarui.znxj.utils.PreferencesUtil;
import com.jiarui.znxj.widget.AutoListView;
import com.jiarui.znxj.widget.ListViewForScrollView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.xutils.DbManager;
import org.xutils.db.sqlite.WhereBuilder;
import org.xutils.ex.DbException;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 进行中  2017/9/9 0009.
 */

public class TaskCenterFragment2 extends BaseFragment {
    // 每页显示多少条数据
    private int mPageSize = 10;
    // 第几页
    private int mPage = 1;
    /*任务列表数适配器*/
    private List<TaslListDataBean> mtalist;
    CommonAdapter<TaslListDataBean> commonAdapter;
    /*数据库*/
    DbManager dbManager = null;//AppContext.getInstance().getDbManager()

    @ViewInject(R.id.autoListView)
    private AutoListView mAutoListView;

    @ViewInject(R.id.common_layout_no_data)
    private LinearLayout EmptyView;


    @Override
    protected View initView(LayoutInflater inflater) {
        mRootView = inflater.inflate(R.layout.taskcenterfragment2, null);
        // 订阅
        EventBus.getDefault().register(TaskCenterFragment2.this);
        return mRootView;
    }

    String user_id = null;

    @Override
    public void initData() {
        user_id = (String) PreferencesUtil.get(mContext, InterfaceDefinition.PreferencesUser.USER_ID, "");
        dbManager = x.getDb(AppContext.getConfig());
        initViewAndLocalData();
    }

    /**
     * 初始视图和加载数据（因为未开始的一旦点击开始任务，
     * 随时都有可能会变成进行中的状态，
     * 所以每次都要重新拿本地数据）
     */
    public void initViewAndLocalData() {
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
                if (item.getStatus().equals("已逾期")) {
                    mHolder.setText(R.id.task_item_text8, "补救任务");//操作按钮文字
                    mHolder.setBackgroundResource(R.id.task_item_text8, R.drawable.text_blue2);
                    mHolder.setVisibility(R.id.task_item_text2, View.VISIBLE);
                    mHolder.setText(R.id.task_item_text2, item.getStatus());//任务状态
                } else {
                    mHolder.setText(R.id.task_item_text8, "继续任务");//操作按钮文字
                    mHolder.setBackgroundResource(R.id.task_item_text8, R.drawable.text_blue);
                    mHolder.setVisibility(R.id.task_item_text2, View.GONE);//任务状态
                }
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
                        Bundle bundle = new Bundle();
                        bundle.putInt("taskid", mtalist.get(position).getId());//任务id
                        bundle.putInt("reseid", mtalist.get(position).getReseid());//水库id
                        bundle.putInt("routeid", Integer.parseInt(mtalist.get(position).getRoute_id()));//路线id
                        bundle.putString("tasktype", mtalist.get(position).getTask_type());//任务类型
                        bundle.putString("creator", mtalist.get(position).getCreator());//发布任务的人
                        gotoAct(bundle, InspectActivity.class);//继续任务
                    }
                });
            }
        };
        mAutoListView.setAdapter(commonAdapter);
        setData();
    }

    /**
     * 初始化列表
     */
    public void setData() {
        //清除原有数据
        mtalist.clear();
        //重新取出数据放到数据源头
        try {
            //已发布里的有两种状态根据开始时间判断是预发布里的还是未开始的（这里只取表里预发布的数据）
            SimpleDateFormat sdfdate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            //取出当前时间搓
            int date = DateUtil.dateInt(sdfdate.format(new Date()), "yyyy-MM-dd HH:mm:ss");
            WhereBuilder b = WhereBuilder.b();
            b.and("status", "==", "1");//条件
            b.and("uid", "=", user_id);
            List<TaslListBeanTable> mtalistdb = dbManager.selector(TaslListBeanTable.class).where(b).findAll();//.where("status", "==", "0").and("start_time",">",date).
            if (mtalistdb != null) {
                //存放数据到适配器的数据源里
                String taskStatus = "";//状态
                String tacktype = "未知";//类型
//                    是开始了，还是开始了但没有完成造成逾期
                for (int db = 0; db < mtalistdb.size(); db++) {
                    if (mtalistdb.get(db).getEnd_time() < date) {
                        taskStatus = "已逾期";
                    } else {
                        taskStatus = "进行中";
                    }

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
                Log.e("mtalistStatus", "" + mtalistdb.size());
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
        commonAdapter.notifyDataSetChanged();
        Log.e("进行中commonAdapter", "" + commonAdapter.getCount());
        //判断有没有数据
        if (commonAdapter.getCount() == 0) {
            EmptyView.setVisibility(View.VISIBLE);
        } else {
            EmptyView.setVisibility(View.GONE);
        }
    }

    @Subscribe
    public void onDataSynEvent(DataSynEvent event) {
        setData();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // 取消订阅
        EventBus.getDefault().unregister(TaskCenterFragment2.this);
    }
}
