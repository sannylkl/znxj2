package com.jiarui.znxj.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.google.gson.Gson;
import com.jiarui.znxj.Interface.IToken;
import com.jiarui.znxj.R;
import com.jiarui.znxj.activity.InspectActivity;
import com.jiarui.znxj.activity.SeeActivity;
import com.jiarui.znxj.adapter.CommonAdapter;
import com.jiarui.znxj.adapter.TaskRouteAdapter;
import com.jiarui.znxj.application.AppContext;
import com.jiarui.znxj.bean.BaseBean;
import com.jiarui.znxj.bean.TaslListBean;
import com.jiarui.znxj.bean.TaslListBeanTable;
import com.jiarui.znxj.bean.TaslListDataBean;
import com.jiarui.znxj.bean.xj_route_detailBean;
import com.jiarui.znxj.constants.GetToken;
import com.jiarui.znxj.constants.InterfaceDefinition;
import com.jiarui.znxj.grobal.CommonListener;
import com.jiarui.znxj.holder.ViewHolder;
import com.jiarui.znxj.utils.DateUtil;
import com.jiarui.znxj.utils.DefaultCommonCallBack;
import com.jiarui.znxj.utils.LogUtil;
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
import java.util.List;

/**
 *已发布—未开始 2017/9/9 0009.
 */

public class TaskCenterFragment3 extends BaseFragmentRefreshLoad  implements IToken {
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

    /*数据库*/
    DbManager dbManager = null;//AppContext.getInstance().getDbManager()
    boolean isGS=true;
    @Override
    protected View initView(LayoutInflater inflater) {
        mRootView = inflater.inflate(R.layout.taskcenterfragment3, null);
        x.view().inject(getActivity());
        return mRootView;
    }

    String user_id =null;
    int tid,tposition;
    @Override
    public void initData() {
        user_id = (String) PreferencesUtil.get(mContext,InterfaceDefinition.PreferencesUser.USER_ID, "");
        dbManager = x.getDb(AppContext.getConfig());
        appContext = new GetToken(this);
        setRefreshLoadListView();
        init();
        LoadResultData(AutoListView.REFRESH);
    }

    public void init() {
        mLists = new ArrayList<TaslListBean.DataBean.ResultBean>();
        mtalist= new ArrayList<TaslListDataBean>();
        commonAdapter = new CommonAdapter<TaslListDataBean>(mContext, mtalist, R.layout.taskcenter_time) {
            @Override
            public void convert(ViewHolder mHolder, TaslListDataBean item, int position) {
                mHolder.setText(R.id.task_item_text1, item.getTask_type());//任务类型
                mHolder.setText(R.id.task_item_text3, item.getReservoir());//水库名称
                mHolder.setText(R.id.task_item_text4, item.getCreator());//发布者
                mHolder.setText(R.id.task_item_text5, item.getItems());//检查项
                String date_star= DateUtil.timeStamp2Date(String.valueOf(item.getStart_time()),"MM/dd");
                String date_end=DateUtil.timeStamp2Date(String.valueOf(item.getEnd_time()),"MM/dd");
                mHolder.setText(R.id.task_item_text6, ""+date_star+"-"+date_end);//任务时间
                if (item.getStatus().equals("已逾期")) {
                    mHolder.setText(R.id.task_item_text8, "补救任务");//操作按钮文字
                    mHolder.setBackgroundResource(R.id.task_item_text8, R.drawable.text_blue2);
                    mHolder.setVisibility(R.id.task_item_text2, View.VISIBLE);
                    mHolder.setText(R.id.task_item_text2, item.getStatus());//任务状态
                } else{
                    mHolder.setText(R.id.task_item_text8, "开始任务");//操作按钮文字
                    mHolder.setBackgroundResource(R.id.task_item_text8, R.drawable.text_blue);
                    mHolder.setVisibility(R.id.task_item_text2, View.GONE);//任务状态
                }
                //获取巡检路线,
                ListViewForScrollView item_list = (ListViewForScrollView) mHolder.getConvertView().findViewById(R.id.task_list2);
                Log.e("item.getRoute_id()"+position,""+item.getRoute_id());
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
                        tid=mtalist.get(position).getId();
                        tposition=position;
                        //正常是走接口的，目前因为后台没有出来走本地2017-11-2下午5:00,下午7:00出
                        startTask();
                    }
                });
            }
        };
        mAutoListView.setAdapter(commonAdapter);
    }

    @Override
    void clearResultData() {
        mLists.clear();
        mtalist.clear();
    }

    @Override
    void disposeResultData(JSONObject arg0) {
        Log.e("未开始发布任务arg0",""+arg0);
        tasklistbean = new Gson().fromJson(arg0.toString(), TaslListBean.class);
        if (InterfaceDefinition.IStatusCode.SUCCESS.equals(tasklistbean.getStatus())) {
            //先取出所有的已发布的数据网络数据
            mLists.addAll(tasklistbean.getData().getResult());
            //取出网络数据增加到表里，或者进行修改
            for (int i = 0; i < mLists.size(); i++) {
//                //获取任务列表
                TaslListBean.DataBean.ResultBean resultBean = mLists.get(i);
                TaslListBeanTable taskbean = null;
                try {
                    WhereBuilder tb = WhereBuilder.b();
                    tb.and("id", "=", resultBean.getId());//条件
                    tb.and("uid", "=", user_id);
                    taskbean = dbManager.selector(TaslListBeanTable.class).where(tb).findFirst();
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
                                "0",resultBean.getTask_type(),"未知","未知","未知",user_id,resultBean.getReseid()
                        );
                        //任务表未开始增加数据库
                        try {
                            if (taslListBeanTable != null) {
                                dbManager.save(taslListBeanTable);
                            }
                        } catch (DbException e) {
                            e.printStackTrace();
                            Log.e("任务表未开始增加数据库异常" + i, e.toString());
                        }
                        //判断是不是空的路线
                        if (StringUtil.isListNotEmpty( mLists.get(i).getRoutes())){
                            //路线表增加数据
                            for (int r = 0; r < mLists.get(i).getRoutes().size(); r++) {
                                TaslListBean.DataBean.ResultBean.RoutesBean rotesBean = mLists.get(i).getRoutes().get(r);
                                Log.e("rotesBean",""+rotesBean.getId());
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
                        }else{
                            Log.e("路线数据" + i, "nullnullnull");
                        }
                        /*数据库存在数据*/
                    } else {
                        Log.e("有值", "" + taskbean.getCreator());
                        //resultBean网络的，taskbean本地表里对应的
                        // 判断是不是一样的状态，不是一样就修改状态，改变表里的数据。要加个状态
                        if (resultBean.getStatus() .equals("3") ) {
                            Log.e("resultBeanStatus="+resultBean.getStatus(), "taskbeanStatus="+taskbean.getStatus() );
                            taskbean.setStatus(resultBean.getStatus());
                            try {
                                dbManager.update(taskbean);
                            } catch (DbException e) {
                                e.printStackTrace();
                                Log.e("DbException状态修改", ""+e.toString());
                            }
                        }
                    }
                } catch (DbException e) {
                    e.printStackTrace();
                    Log.e("DbException", ""+e.toString());
                }
            }

//            重新取出数据放到数据源头
            try {
                //已发布里的有两种状态根据开始时间判断是预发布里的还是未开始的（这里只取表里预发布的数据）
                SimpleDateFormat sdfdate=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                //取出当前时间搓
                int date= DateUtil.dateInt(sdfdate.format(new Date()),"yyyy-MM-dd HH:mm:ss");
//                //条件查询方法一  25>age>22
                WhereBuilder b = WhereBuilder.b();
                b.and("status", "==", "0");//条件
                b.and("start_time", "<", date);
                b.and("uid", "=", user_id);
                List<TaslListBeanTable> mtalistdb= dbManager.selector(TaslListBeanTable.class).where(b).findAll();//.where("status", "==", "0").and("start_time",">",date).

                if (mtalistdb!=null){
                    String taskStatus="";//状态
                    String tacktype = "未知";//类型
                    //存放数据到适配器的数据源里，要加时间判断是未开始还是做的没做就时间过去了变成了已逾期，
                    for (int db = 0; db < mtalistdb.size(); db++) {

                        if (mtalistdb.get(db).getEnd_time()<date){
                            taskStatus="已逾期";
                        }else{
                            taskStatus="未开始";
                        }

                        if (mtalistdb.get(db).getTask_type().equals("daily")){
                            tacktype="日常任务";
                        } else if (mtalistdb.get(db).getTask_type().equals("regular")){
                            tacktype="定期任务";
                        }else if (mtalistdb.get(db).getTask_type().equals("special")){
                            tacktype="特别检查";
                        }
                        TaslListDataBean taslListDataBean = new TaslListDataBean(
                                mtalistdb.get(db).getId(), mtalistdb.get(db).getStart_time(),
                                mtalistdb.get(db).getEnd_time(), mtalistdb.get(db).getReservoir(),
                                mtalistdb.get(db).getCreator(), mtalistdb.get(db).getRoute_id(),
                                mtalistdb.get(db).getItems(), taskStatus,
                                tacktype,1);
                        mtalist.add(taslListDataBean);
                    }
                    Log.e("mtalistStatus", ""+mtalistdb.size() );
                }
            } catch (DbException e) {
                e.printStackTrace();
            }

            Log.e("预发布commonAdapter", ""+commonAdapter.getCount() );
            //判断有没有数据
            if (commonAdapter.getCount() == 0) {
                EmptyView.setVisibility(View.VISIBLE);
            } else {
//                    mAutoListView.setResultSize(mtalist.size());
                EmptyView.setVisibility(View.GONE);
            }
        }
        else if ("401".equals(tasklistbean.getStatus())) {//token获取失败
            isGS=true;
            appContext.getToken();
        } else {//走接口失败
            ToastUtil.TextToast(tasklistbean.getMsg());
            //清除数据
            //查询数据库 , 最后取数据库的数据
            try {
                //已发布里的有两种状态根据开始时间判断是预发布里的还是未开始的（这里只取表里预发布的数据）
                SimpleDateFormat sdfdate=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                //取出当前时间搓
                int date= DateUtil.dateInt(sdfdate.format(new Date()),"yyyy-MM-dd HH:mm:ss");
                //（状态为0，然后还有就是要大于当前时间。因为这是预发布的任务是时间还没有到的任务）
                WhereBuilder b = WhereBuilder.b();
                b.and("status", "==", "0");//条件
                b.and("start_time", "<", date);
                b.and("uid", "=", user_id);
                List<TaslListBeanTable> mtalistdb= dbManager.selector(TaslListBeanTable.class).where(b).findAll();//.where("status", "==", "0").and("start_time",">",date).
                //存放数据到适配器的数据源里
                if (mtalistdb!=null){
                    String taskStatus="";//状态
                    String tacktype = "未知";//类型
                    //存放数据到适配器的数据源里，要加时间判断是未开始还是做的没做就时间过去了变成了已逾期，
                    for (int db = 0; db < mtalistdb.size(); db++) {

                        if (mtalistdb.get(db).getEnd_time()<date){
                            taskStatus="已逾期";
                        }else{
                            taskStatus="未开始";
                        }

                        if (mtalistdb.get(db).getTask_type().equals("daily")){
                            tacktype="日常任务";
                        } else if (mtalistdb.get(db).getTask_type().equals("regular")){
                            tacktype="定期任务";
                        }else if (mtalistdb.get(db).getTask_type().equals("special")){
                            tacktype="特别检查";
                        }
                        TaslListDataBean taslListDataBean = new TaslListDataBean(
                                mtalistdb.get(db).getId(), mtalistdb.get(db).getStart_time(),
                                mtalistdb.get(db).getEnd_time(), mtalistdb.get(db).getReservoir(),
                                mtalistdb.get(db).getCreator(), mtalistdb.get(db).getRoute_id(),
                                mtalistdb.get(db).getItems(), taskStatus,
                                tacktype,1);
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
        params.addBodyParameter(InterfaceDefinition.ITaskList.PAGENUM, mAutoListView.getPageSize() + "");
        if (what == AutoListView.LOAD) {
            mPage++;
            params.addBodyParameter(InterfaceDefinition.ITaskList.PAGE, mPage + "");
            mCacheUrl = "";
        } else {
            mPage = 1;
            params.addBodyParameter(InterfaceDefinition.ITaskList.PAGE, mPage + "");
            // 拼凑缓存路径
            mCacheUrl = InterfaceDefinition.ITaskList.IURL+"prepare";
        }
        // 设置缓存数据的名字及，请求标识（是下拉刷新还是上拉加载）
        setBaseRequestParams(mCacheUrl, what);
        x.http().get(params, this);
    }

    /*数据对接基类,用来判断是成功还是是失败了*/
    BaseBean baseBean=null;
    /**
     * 开始任务接口
     */
    private void startTask() {
        String url = InterfaceDefinition.IStartTask.IURL + PreferencesUtil.get(mContext, InterfaceDefinition.PreferencesUser.Docking_CREDENTIALS, "");
        // 接口对接
        RequestParams mParams = new RequestParams(url);
        mParams.addBodyParameter(InterfaceDefinition.IStartTask.TASK_ID, ""+tid);
        mParams.addBodyParameter(InterfaceDefinition.IStartTask.UID, user_id);
        mParams.addBodyParameter(InterfaceDefinition.IStartTask.PARAM, "start");
        x.http().post(mParams, new DefaultCommonCallBack(mContext, true) {
            @Override
            public void onSuccess(String json) {
                LogUtil.e("开始任务接口", json);
                baseBean = new Gson().fromJson(json, BaseBean.class);
                if ("0".equals(baseBean.getStatus())) {
                    onData(tposition);
                } else if ("401".equals(baseBean.getStatus())) {
                    isGS=false;
                    appContext.getToken();
                } else {
                    ToastUtil.TextToast("" + baseBean.getMsg());
                    onData(tposition);
                }
            }
            @Override
            public void onError(Throwable arg0, boolean arg1) {
                onData(tposition);
            }
        });
    }

    /**
     * 改变状态，刷新数据，进行跳转
     * @param position
     */
    public void onData(int position) {
        WhereBuilder tb = WhereBuilder.b();
        tb.and("id", "=", mtalist.get(position).getId());//条件
        tb.and("uid", "=", user_id);
        TaslListBeanTable taskbean  = null;
        try {
            taskbean = dbManager.selector(TaslListBeanTable.class).where(tb).findFirst();
        } catch (DbException e) {
            e.printStackTrace();
        }
        taskbean.setStatus("1");
        try {
            dbManager.update(taskbean);
        } catch (DbException e) {
            e.printStackTrace();
            Log.e("DbException未开始状态修改", ""+e.toString());
        }
        //刷新适配器
        mtalist.remove(position);
        commonAdapter.notifyDataSetChanged();
        Bundle bundle = new Bundle();
        bundle.putInt("taskid", mtalist.get(position).getId());//任务id
        bundle.putInt("reseid", mtalist.get(position).getReseid());//水库id
        bundle.putInt("routeid", Integer.parseInt(mtalist.get(position).getRoute_id()));//路线id
        gotoAct(bundle, InspectActivity.class);//开始/补救任务
    }

    @Override
    public void success(int errtype) {
        if (errtype == 0) {
            //判断是重新走那个接口
            if (isGS){
                LoadResultData(0);
            }else{
                startTask();
            }
        }
    }
}