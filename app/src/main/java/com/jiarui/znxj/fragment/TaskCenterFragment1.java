package com.jiarui.znxj.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.jiarui.znxj.Interface.IToken;
import com.jiarui.znxj.R;
import com.jiarui.znxj.activity.LoginActivity;
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
import com.jiarui.znxj.utils.StringUtil;
import com.jiarui.znxj.utils.ToastUtil;
import com.jiarui.znxj.widget.AutoListView;
import com.jiarui.znxj.widget.ListViewForScrollView;

import org.json.JSONObject;
import org.xutils.DbManager;
import org.xutils.db.sqlite.WhereBuilder;
import org.xutils.ex.DbException;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 已发布—预发布
 * Created by Administrator on 2017/9/9 0009.
 */

public class TaskCenterFragment1 extends BaseFragmentRefreshLoad implements IToken {
    // 每页显示多少条数据
    private int mPageSize = 10;
    // 第几页
    private int mPage = 1;
    /*任务列表数适配器*/
    private List<TaslListDataBean> mtalist;
    CommonAdapter<TaslListDataBean> commonAdapter;
    //获取token的
    GetToken appContext;
    /*后台数据源*/
    private List<TaslListBean.DataBean.ResultBean> mLists;
    TaslListBean tasklistbean;
    List<TaskCenterBean> list;//路线规划数据源
    /*数据库*/
    DbManager dbManager = null;//AppContext.getInstance().getDbManager()
    List<TaskCenter_listBean> mdata2;


    @Override
    protected View initView(LayoutInflater inflater) {
        mRootView = inflater.inflate(R.layout.taskcenterfragment1, null);
        x.view().inject(getActivity());
        return mRootView;
    }

    String user_id, ReservoId = null;

    @Override
    public void initData() {
        user_id = (String) PreferencesUtil.get(mContext, InterfaceDefinition.PreferencesUser.USER_ID, "");
        ReservoId = (String) PreferencesUtil.get(mContext, InterfaceDefinition.PreferencesUser.ReservoId, "-1");
        list = new ArrayList<>();
        mdata2 = new ArrayList<>();
        Log.e("initData", "" + user_id + "\tReservoId:" + ReservoId);
        dbManager = x.getDb(AppContext.getConfig());
        //正式上线之前要清除一波数据
//        try {//清除任务数据表
//            dbManager.delete(TaslListBeanTable.class);
//        } catch (DbException e) {
//            e.printStackTrace();
//        }
//        try {//清除路线数据表
//            dbManager.delete(xj_route_detailBean.class);
//        } catch (DbException e) {
//            e.printStackTrace();
//        }
        appContext = new GetToken(this);
        setRefreshLoadListView();
        init();
        LoadResultData(AutoListView.REFRESH);
    }

    public void init() {
        mLists = new ArrayList<TaslListBean.DataBean.ResultBean>();
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
                mHolder.setVisibility(R.id.task_item_text8, View.GONE);//补救任务按钮隐藏
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
                //查看详情
                mHolder.setOnClickListener(R.id.task_item_text7, position, new CommonListener() {
                    @Override
                    public void commonListener(View view, int position) {
                        Bundle bundle = new Bundle();
                        bundle.putInt("id", mtalist.get(position).getId());
                        gotoAct(bundle, SeeActivity.class);//任务详情
                    }
                });

            }
        };
        mAutoListView.setAdapter(commonAdapter);
    }

    @Override
    void clearResultData() {
        Log.e("mtalist", "clearclear");
        mLists.clear();
        mtalist.clear();
    }

    @Override
    void disposeResultData(JSONObject arg0) {
        String num[] = new String[]{"1", "2"};
        String name[] = new String[]{"完成定点检查", "完成定点检查"};
        String time[] = new String[]{"5分钟", "6分钟"};
        String rc[] = new String[]{"日常检查", "日常检查"};
        String sk[] = new String[]{"水库", "水水"};
        String xl[] = new String[]{"小李", "小李"};
        String jc[] = new String[]{"检查", "检查"};
        String time2[] = new String[]{"08/16-08/19", "08/16-08/19"};
        for (int i = 0; i < num.length; i++) {
            mdata2.add(new TaskCenter_listBean(num[i], name[i], time[i]));
            list.add(new TaskCenterBean(rc[i], sk[i], xl[i], jc[i], time2[i], mdata2));
        }
//        TaskCenter_listBean ta1 = new TaskCenter_listBean("1", "完成定点检查", "5分钟");
//        TaskCenter_listBean ta2 = new TaskCenter_listBean("2", "完成定点检查", "6分钟");
//        mdata2.add(ta1);
//        mdata2.add(ta2);
//        TaskCenterBean taskCenterBean1 = new TaskCenterBean("日常检查", "水库", "小李", "检查", "08/16-08/19", mdata2);
//        TaskCenterBean taskCenterBean2 = new TaskCenterBean("日常检查", "火库", "小李", "检查", "08/16-08/19", mdata2);
//        list.add(taskCenterBean1);
//        list.add(taskCenterBean2);
        mAutoListView.setAdapter(commonAdapter);
        TaskCenter_listBean ta1 = new TaskCenter_listBean("1", "完成定点检查", "5分钟");
        TaskCenter_listBean ta2 = new TaskCenter_listBean("2", "完成定点检查", "6分钟");
        mdata2.add(ta1);
        mdata2.add(ta2);
        TaskCenterBean taskCenterBean1 = new TaskCenterBean("日常检查", "水库", "小李", "检查", "08/16-08/19", mdata2);
        TaskCenterBean taskCenterBean2 = new TaskCenterBean("日常检查", "火库", "小李", "检查", "08/16-08/19", mdata2);
        list.add(taskCenterBean1);
        list.add(taskCenterBean2);
        mAutoListView.setAdapter(commonAdapter);
        Log.e("预发布任务arg0", "" + arg0);

        tasklistbean = new Gson().fromJson(arg0.toString(), TaslListBean.class);
        if (InterfaceDefinition.IStatusCode.SUCCESS.equals(tasklistbean.getStatus())) {
            //先取出所有的已发布的数据网络数据
            mLists.addAll(tasklistbean.getData().getResult());
            //取出网络数据增加到表里，或者进行修改
            for (int i = 0; i < mLists.size(); i++) {
//                //获取任务列表
                TaslListBean.DataBean.ResultBean resultBean = mLists.get(i);
                Log.e("任务id" + i, "" + resultBean.getId());
                TaslListBeanTable taskbean = null;
                try {
                    Log.e("重新查询数据" + i, "" + resultBean.getCreator());
                    WhereBuilder tb = WhereBuilder.b();
                    tb.and("id", "=", resultBean.getId());//条件
                    tb.and("uid", "=", user_id);
                    taskbean = dbManager.selector(TaslListBeanTable.class).where(tb).findFirst();
                    Log.e("taskbean" + i, "" + taskbean);
                    /*数据库不存在*/
                    if (taskbean == null) {
                        //获取检查项目
                        String itemName = "";
                        for (int j = 0; j < mLists.get(i).getItems().size(); j++) {
                            itemName = itemName + mLists.get(i).getItems().get(j).getItem_name() + ",";
                        }
                        //已发布任务列表数据
                        TaslListBeanTable taslListBeanTable = new TaslListBeanTable(
                                resultBean.getId(), resultBean.getStart_time(),
                                resultBean.getEnd_time(), resultBean.getReservoir()
                                , resultBean.getCreator(), resultBean.getRoute_id(), itemName,
                                "0", resultBean.getTask_type(), "未知", "未知", "未知", user_id, resultBean.getReseid());

                        //任务表增加数据库
                        try {
                            if (taslListBeanTable != null) {
                                dbManager.save(taslListBeanTable);
                            }
                            Log.e("taslListBeanTable" + i, "" + taslListBeanTable);
                        } catch (DbException e) {
                            e.printStackTrace();
                            Log.e("任务表增加数据库异常" + i, e.toString());
                        }
                        //判断是不是空的路线
                        if (StringUtil.isListNotEmpty(mLists.get(i).getRoutes())) {
                            //路线表增加数据
                            for (int r = 0; r < mLists.get(i).getRoutes().size(); r++) {
                                TaslListBean.DataBean.ResultBean.RoutesBean rotesBean = mLists.get(i).getRoutes().get(r);
                                Log.e("rotesBean", "" + rotesBean.getId());
                                xj_route_detailBean xj_route_detailBean = new xj_route_detailBean(
                                        rotesBean.getId(), rotesBean.getRoute_id(),
                                        rotesBean.getLocation_id(), rotesBean.getLocation_name()
                                        , rotesBean.getFinished_time(), rotesBean.getNext_time()
                                        , rotesBean.getIf_end(), rotesBean.getSortby());
                                try {
                                    dbManager.save(xj_route_detailBean);
                                } catch (DbException e) {
                                    e.printStackTrace();
                                    Log.e("路线表增加数据异常" + i, e.toString());
                                }
                            }
                        } else {
                            Log.e("路线数据" + i, "nullnullnull");
                        }
                        /*数据库存在数据*/
                    } else {
                        Log.e("有值", "" + taskbean.getCreator());
                        //resultBean网络的，taskbean本地表里对应的
                        // 判断是不是一样的状态，不是一样就修改状态，改变表里的数据。要加个状态
                        if (resultBean.getStatus().equals("3")) {
                            Log.e("resultBeanStatus=" + resultBean.getStatus(), "taskbeanStatus=" + taskbean.getStatus());
                            taskbean.setStatus(resultBean.getStatus());
                            try {
                                dbManager.update(taskbean);
                            } catch (DbException e) {
                                e.printStackTrace();
                                Log.e("DbException状态修改", "" + e.toString());
                            }
                        }
                    }
                } catch (DbException e) {
                    e.printStackTrace();
                    Log.e("DbException", "" + e.toString());
                }
            }

//            重新取出数据放到数据源头
            try {
                //已发布里的有两种状态根据开始时间判断是预发布里的还是未开始的（这里只取表里预发布的数据）
                SimpleDateFormat sdfdate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                //取出当前时间搓
                int date = DateUtil.dateInt(sdfdate.format(new Date()), "yyyy-MM-dd HH:mm:ss");
//                //条件查询方法一  25>age>22
                WhereBuilder b = WhereBuilder.b();
                b.and("status", "==", "0");//条件
//                b.or("status", "==", "3");//条件
                b.and("start_time", ">", date);
                b.and("uid", "=", user_id);
                List<TaslListBeanTable> mtalistdb = dbManager.selector(TaslListBeanTable.class).where(b).findAll();//.where("status", "==", "0").and("start_time",">",date).
                if (mtalistdb != null) {
                    String taskStatus = "预发布";//状态
                    String tacktype = "未知";//类型
                    //存放数据到适配器的数据源里
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
                                tacktype, 1);
                        mtalist.add(taslListDataBean);
                    }
                    Log.e("mtalistStatus", "" + mtalistdb.size());
                }
            } catch (DbException e) {
                e.printStackTrace();
            }
            //判断有没有数据
            if (commonAdapter.getCount() == 0) {
                EmptyView.setVisibility(View.VISIBLE);
            } else {
//                    mAutoListView.setResultSize(mtalist.size());
                EmptyView.setVisibility(View.GONE);
            }
        } else if ("401".equals(tasklistbean.getStatus())) {//token获取失败
            appContext.getToken();
        } else {//走接口失败
            ToastUtil.TextToast(tasklistbean.getMsg());
            //清除数据
            //查询数据库 , 最后取数据库的数据
            try {
                //已发布里的有两种状态根据开始时间判断是预发布里的还是未开始的（这里只取表里预发布的数据）
                SimpleDateFormat sdfdate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                //取出当前时间搓
                int date = DateUtil.dateInt(sdfdate.format(new Date()), "yyyy-MM-dd HH:mm:ss");
                //（状态为0，然后还有就是要大于当前时间。因为这是预发布的任务是时间还没有到的任务）
                WhereBuilder b = WhereBuilder.b();
                b.and("status", "==", "0");//条件
                b.and("start_time", ">", date);
                b.and("uid", "=", user_id);
                List<TaslListBeanTable> mtalistdb = dbManager.selector(TaslListBeanTable.class).where(b).findAll();//.where("status", "==", "0").and("start_time",">",date).
                //存放数据到适配器的数据源里
                if (mtalistdb != null) {
                    String taskStatus = "预发布";//状态
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
                                tacktype, 1);
                        mtalist.add(taslListDataBean);
                    }
                }
            } catch (DbException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    void notifyDataSetChanged() {
        commonAdapter.notifyDataSetChanged();
        setFirstLoad(false);
    }

    @Override
    void LoadResultData(int what) {
        String url = InterfaceDefinition.ITaskList.IURL + PreferencesUtil.get(mContext, InterfaceDefinition.PreferencesUser.Docking_CREDENTIALS, "");
        RequestParams params = new RequestParams(url);
        params.addBodyParameter(InterfaceDefinition.ITaskList.PARAM, "prepare");
        params.addBodyParameter(InterfaceDefinition.ITaskList.UID, user_id);
        params.addBodyParameter(InterfaceDefinition.ITaskList.RESEID, ReservoId);
        params.addBodyParameter(InterfaceDefinition.ITaskList.PAGENUM, mAutoListView.getPageSize() + "");
        if (what == AutoListView.LOAD) {
            mPage++;
            params.addBodyParameter(InterfaceDefinition.ITaskList.PAGE, mPage + "");
            mCacheUrl = "";
        } else {
            mPage = 1;
            params.addBodyParameter(InterfaceDefinition.ITaskList.PAGE, mPage + "");
            // 拼凑缓存路径
            mCacheUrl = InterfaceDefinition.ITaskList.IURL + "prepare";
        }
        // 设置缓存数据的名字及，请求标识（是下拉刷新还是上拉加载）
        setBaseRequestParams(mCacheUrl, what);
        x.http().get(params, this);
    }

    @Override
    public void success(int errtype) {
        if (errtype == 0) {
            LoadResultData(0);
        }
    }
}
