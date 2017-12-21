package com.jiarui.znxj.activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.google.gson.Gson;
import com.jiarui.znxj.Interface.IToken;
import com.jiarui.znxj.R;
import com.jiarui.znxj.adapter.CommonAdapter;
import com.jiarui.znxj.application.AppContext;
import com.jiarui.znxj.base.BaseActivity;
import com.jiarui.znxj.bean.CheckTheContentTable;
import com.jiarui.znxj.bean.TaskDetailsTablePartBean;
import com.jiarui.znxj.bean.TaskDetailsTableLoctionDataBean;
import com.jiarui.znxj.bean.TaskDetailsBean;
import com.jiarui.znxj.bean.TaskDetailsTableLoctionBean;
import com.jiarui.znxj.bean.TaslListBeanTable;
import com.jiarui.znxj.constants.GetToken;
import com.jiarui.znxj.constants.InterfaceDefinition;
import com.jiarui.znxj.flow.FlowLayout;
import com.jiarui.znxj.flow.TagAdapter;
import com.jiarui.znxj.flow.TagFlowLayout;
import com.jiarui.znxj.holder.ViewHolder;
import com.jiarui.znxj.utils.DefaultCommonCallBack;
import com.jiarui.znxj.utils.FileHelper;
import com.jiarui.znxj.utils.LogUtil;
import com.jiarui.znxj.utils.PreferencesUtil;
import com.jiarui.znxj.utils.StringUtil;
import com.jiarui.znxj.utils.ToastUtil;
import com.jiarui.znxj.utils.startActivityAPPutils;
import com.jiarui.znxj.widget.CustomGridView;
import com.jiarui.znxj.widget.ListViewForScrollView;

import org.xutils.DbManager;
import org.xutils.db.sqlite.WhereBuilder;
import org.xutils.ex.DbException;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 项目检查
 */
public class InspectActivity extends BaseActivity implements IToken{
    /*检查点位*/
    @ViewInject(R.id.inspect_grid)
    private CustomGridView inspect_grid;
    CommonAdapter<TaskDetailsTableLoctionDataBean> mcommonAdapter;
    List<TaskDetailsTableLoctionDataBean> dwlist;
    /*检查部位*/
    @ViewInject(R.id.flow)
    private TagFlowLayout flow;
    private TagAdapter tagAdater;
    List<TaskDetailsTablePartBean> bwlist;
    /*检查项*/
    @ViewInject(R.id.inspect_list)
    private ListViewForScrollView inspect_list;
    CommonAdapter<TaskDetailsTablePartBean> commonAdapter;
    List<TaskDetailsTablePartBean> iblist;
    @ViewInject(R.id.end_now_loction)
    private EditText end_now_loction;
    /*位置偏离*/
    @ViewInject(R.id.tv_loction)
    private TextView tv_loction;
    /*签到情况*/
    @ViewInject(R.id.tv_sigup)
    private TextView tv_sigup;
    /*水位*/
    @ViewInject(R.id.tv_group_name_rigth)
    private EditText tv_group_name_rigth;
    /*天气*/
    @ViewInject(R.id.inspect_tq)
    private EditText inspect_tq;
    /*下游水位*/
    @ViewInject(R.id.inspect_xysw)
    private EditText inspect_xysw;
    /*任务预览*/
    @ViewInject(R.id.inspect_determine)
    private TextView inspect_determine;

    /*reseid（水库id）,taskid(任务id)routeid(路线id)*/
    int reseid,taskid,routeid;
    /*任务类型 +发布任务的人*/
    String tasktype,creator;
    /*数据库*/
    DbManager dbManager = null;//AppContext.getInstance().getdbManager()
    /*后台数据源*/
    private List<TaskDetailsBean.ResultBean> dwDatalists;
    TaskDetailsBean taskDetailsListBean;
    /*SD卡中文件读写类*/
    FileHelper helper;
    Map<Integer, String> map;
    /*当前用户id*/
    String user_id =null;
    startActivityAPPutils startActivityAPPutils=null;
    public LocationClient mLocationClient = null;
    public BDAbstractLocationListener myListener = new MyLocationListener();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inspect);
        x.view().inject(this);
        setTitle();
        mTvForTitle.setText("项目检查");
        //启动app
        startActivityAPPutils=new startActivityAPPutils();
        if ( startActivityAPPutils.doStartApplicationWithPackageName("RFIDread",getPackageManager())!=null){

        } ;
        //读取文件
        helper = new FileHelper(getApplicationContext());
        //定位
        mLocationClient = new LocationClient(getApplicationContext());
        //声明LocationClient类
        mLocationClient.registerLocationListener(myListener);
        //注册监听函数
        initLocation();
        mLocationClient.start();
        dbManager = x.getDb(AppContext.getConfig());
        //正式上线之前要清除一波数据
//        try {//清除点位数据表
//            dbManager.delete(TaskDetailsTableLoctionBean.class);
//        } catch (DbException e) {
//            e.printStackTrace();
//        }
//        try {//清除点位部位/检查项数据表
//            dbManager.delete(TaskDetailsTablePartBean.class);
//        } catch (DbException e) {
//            e.printStackTrace();
//        }
        user_id=(String) PreferencesUtil.get(this,InterfaceDefinition.PreferencesUser.USER_ID, "");
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            taskid = bundle.getInt("taskid");//任务id
            reseid = bundle.getInt("reseid");//水库id
            routeid= bundle.getInt("routeid");//路线id
            tasktype= bundle.getString("tasktype");//任务类型
            creator= bundle.getString("creator");//发布任务的人
            Log.e("taskid",""+taskid+"\treseid"+""+reseid+"\trouteid"+""+routeid);
            init();
            taskDati();
            //取出天气，水位数据
            try {
                WhereBuilder tb = WhereBuilder.b();
                tb.and("id", "=", taskid);//条件
                tb.and("uid", "=", user_id);
                TaslListBeanTable taskbean = dbManager.selector(TaslListBeanTable.class).where(tb).findFirst();
                //为空处理
                if (!taskbean.getWater_line().equals("未知")){
                    tv_group_name_rigth.setText(taskbean.getWater_line());
                }
                if (!taskbean.getWeather().equals("未知")){
                    inspect_tq.setText(taskbean.getWater_line());
                }
                if (!taskbean.getDown_water().equals("未知")){
                    inspect_xysw.setText(taskbean.getWater_line());
                }
            } catch (DbException e) {
                e.printStackTrace();
            }
        }
    }


    public void init() {
        /*后台返回数据*/
        dwDatalists = new ArrayList<TaskDetailsBean.ResultBean>();
        /*点位gridview*/
        dwlist = new ArrayList<>();
        mcommonAdapter = new CommonAdapter<TaskDetailsTableLoctionDataBean>(InspectActivity.this, dwlist, R.layout.inspect_griditem) {
            @Override
            public void convert(ViewHolder mHolder, final TaskDetailsTableLoctionDataBean item, int position) {
                CheckBox inspect_grid_text_lin = (CheckBox) mHolder.getConvertView().findViewById(R.id.inspect_grid_text);
                inspect_grid_text_lin.setText(item.getTitle());
                inspect_grid_text_lin.setChecked(item.isLin());
            }
        };
        inspect_grid.setAdapter(mcommonAdapter);
        inspect_grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                for (int j = 0; j < dwlist.size(); j++) {
                    dwlist.get(j).setLin(false);
                }
                dwlist.get(i).setLin(true);
                mcommonAdapter.notifyDataSetChanged();
                //定位
                mLocationClient.start();
                //获取部位
                selectBW(dwlist.get(i).getDid());
//              取出点位的部位的所有的检查项
                if (StringUtil.isListNotEmpty(bwlist)){
                    selectCT(bwlist.get(0).getCid(),bwlist.get(0).getDid());
                }else{
                    iblist.clear();
                    commonAdapter.notifyDataSetChanged();
                }
            }
        });
         /*部位TagFlowLayout*/
        bwlist= new ArrayList<TaskDetailsTablePartBean>();
        flow.removeAllViews();
        tagAdater = new TagAdapter(bwlist) {
        @Override
        public View getView(FlowLayout parent, int position, Object o) {
        TextView te = (TextView) LayoutInflater.from(InspectActivity.this).inflate(R.layout.flow_item, flow, false);
        TaskDetailsTablePartBean it= bwlist.get(position);
         te.setText(it.getName());
         return te;
         }
        @Override
        public boolean setSelected(int position, Object o) {
            return o.equals(bwlist.get(0));
        }
         };

        flow.setAdapter(tagAdater);
        flow.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                    selectCT(bwlist.get(position).getCid(),bwlist.get(position).getDid());
                    return true;
            }
        });
        /*检查项listview*/
        iblist = new ArrayList<>();
        commonAdapter = new CommonAdapter<TaskDetailsTablePartBean>(this, iblist, R.layout.inspiron_list) {
            @Override
            public void convert(ViewHolder mHolder, TaskDetailsTablePartBean item, int position) {
                TextView tv_group_name_rigth = (TextView) mHolder.getConvertView().findViewById(R.id.tv_group_name_rigth);
                mHolder.setText(R.id.tv_group_name, item.getName());
                if (item.getStatus()==2) {//正常
                    mHolder.setText(R.id.tv_group_name_rigth, "正常");
                    tv_group_name_rigth.setTextColor(Color.parseColor("#92C35C"));
                } else if(item.getStatus()==3) {//异常
                    mHolder.setText(R.id.tv_group_name_rigth, "异常");
                    tv_group_name_rigth.setTextColor(Color.parseColor("#F46666"));
                }else {//0,1都是后台给的数据，所以都是未检查的
                    mHolder.setText(R.id.tv_group_name_rigth, "未检查");
                    tv_group_name_rigth.setTextColor(Color.parseColor("#B4B4B4"));
                }
            }
        };
        inspect_list.setAdapter(commonAdapter);
        inspect_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Bundle bundle = new Bundle();
                bundle.putInt("taskid", iblist.get(i).getTaskid());//区分任务
                bundle.putInt("did", iblist.get(i).getDid());//区分点位
                bundle.putInt("pid", iblist.get(i).getPid());//区分当前部位
                bundle.putInt("cid", iblist.get(i).getCid());//区分当前项
                bundle.putString("name", iblist.get(i).getName());//当前项目名称
                gotoActivity(bundle, BaseofDamActivity.class);
            }
        });

    }

    String water_line,weather,down_water;
    @Event({R.id.common_title_left, R.id.inspect_determine,R.id.end_now_loction})
    private void onClick(View v) {
        switch (v.getId()) {
            case R.id.common_title_left:
                finish();
                break;
            case R.id.end_now_loction:
                //当前点结束
                break;
            case R.id.inspect_determine:
                water_line=tv_group_name_rigth.getText().toString().trim();
                weather=inspect_tq.getText().toString().trim();
                down_water=inspect_xysw.getText().toString().trim();
                if (StringUtil.isEmpty(water_line)){
                    ToastUtil.TextToast("请输入水位");
                    return;
                }
                if (StringUtil.isEmpty(weather)){
                    ToastUtil.TextToast("请输入天气");
                    return;
                }
                if (StringUtil.isEmpty(down_water)){
                ToastUtil.TextToast("下游水位");
                return;
            }
                //保存数据
                try {
                    WhereBuilder tb = WhereBuilder.b();
                    tb.and("id", "=", taskid);//条件
                    tb.and("uid", "=", user_id);
                    TaslListBeanTable taskbean = dbManager.selector(TaslListBeanTable.class).where(tb).findFirst();
                    //为空处理
                    if (StringUtil.isNotEmpty(water_line)){
                        taskbean.setWater_line(water_line);
                    }else{
                        taskbean.setWater_line("未知");
                    }
                    if (StringUtil.isNotEmpty(weather)){
                        taskbean.setWeather(weather);
                    }else{
                        taskbean.setWeather("未知");
                    }
                    if (StringUtil.isNotEmpty(down_water)){
                        taskbean.setDown_water(down_water);
                    }else{
                        taskbean.setDown_water("未知");
                    }
                    dbManager.update(taskbean);
                } catch (DbException e) {
                    e.printStackTrace();
                }
                Bundle bundle = new Bundle();
                bundle.putInt("taskid", taskid);//任务id
                bundle.putInt("reseid", reseid);//水库id
                bundle.putInt("routeid",routeid);//路线id
                bundle.putString("tasktype",tasktype);//任务类型
                bundle.putString("creator",creator);//发布任务的人
                gotoActivity(bundle,TaskPreviewActivity.class);
                break;
            default:
                break;
        }
    }

    /**
     * 任务详情接口
     */
    private void taskDati() {
        String url = InterfaceDefinition.ITaskCheck.IURL + PreferencesUtil.get(this, InterfaceDefinition.PreferencesUser.Docking_CREDENTIALS, "");
        // 接口对接
        RequestParams mParams = new RequestParams(url);
        mParams.addBodyParameter(InterfaceDefinition.ITaskCheck.ROUTE_ID, ""+routeid);
        x.http().get(mParams, new DefaultCommonCallBack(InspectActivity.this, true) {
            @Override
            public void onSuccess(String json) {
                LogUtil.e("任务详情接口", json);
                taskDetailsListBean = new Gson().fromJson(json, TaskDetailsBean.class);
                if (InterfaceDefinition.IStatusCode.SUCCESS.equals(taskDetailsListBean.getStatus())) {
                    //先取出所有的已发布的数据网络数据
                    dwDatalists.addAll(taskDetailsListBean.getResult());
                    //取出网络数据增加到表里，或者进行修改
                    for (int i = 0; i < dwDatalists.size(); i++) {
                        //项目检查详情
                        TaskDetailsBean.ResultBean resultBean = dwDatalists.get(i);
                        Log.e("点位id"+i, "" + resultBean.getId());
                        TaskDetailsTableLoctionBean taskbean = null;
                        try {
                            WhereBuilder tb = WhereBuilder.b();
                            tb.and("did", "=", resultBean.getId());//
                            tb.and("reseid", "=", resultBean.getReseid());
                            tb.and("taskid", "=", taskid);
                            taskbean = dbManager.selector(TaskDetailsTableLoctionBean.class).where(tb).findFirst();
                           /*表里不存在数据*/
                            if (taskbean == null) {
                                Log.e("taskbean" + i, "没有数据");
                                //任务点位列表数据
                                TaskDetailsTableLoctionBean taskDetailsListBeanTable = new TaskDetailsTableLoctionBean(
                                        resultBean.getId(),taskid ,resultBean.getReseid(),
                                        resultBean.getTitle(), resultBean.getIdcard()
                                        , resultBean.getAddress(), resultBean.getXpoint(), resultBean.getYpoint(),
                                        resultBean.getStatus(),resultBean.getSignup());
                                //任务点位表增加
                                try {
                                        dbManager.save(taskDetailsListBeanTable);
                                } catch (DbException e) {
                                    e.printStackTrace();
                                    Log.e("任务点位表增加异常" + i, e.toString());
                                }

                                //判断点位表增加检查部位是不是空
                                if (StringUtil.isListNotEmpty( dwDatalists.get(i).getItems())){
                                    //点位表增加检查部位
                                    for (int b = 0; b < dwDatalists.get(i).getItems().size(); b++) {
                                            TaskDetailsBean.ResultBean.ItemsBean loctionBWBean =  dwDatalists.get(i).getItems().get(b);
                                            Log.e("loctionBWBean",""+loctionBWBean.getId());
                                            TaskDetailsTablePartBean loctionBWTable = new TaskDetailsTablePartBean(
                                                    dwDatalists.get(i).getId(),
                                                    taskid,loctionBWBean.getId() ,loctionBWBean.getPid(),
                                                    loctionBWBean.getName(), loctionBWBean.getStatus());
                                            try {
                                                dbManager.save(loctionBWTable);
                                            } catch (DbException e) {
                                                e.printStackTrace();
                                                Log.e("点位表增加检查部位异常" + i, e.toString());
                                            }

                                            //任务点位表增加检查部位的检查项
                                            if(StringUtil.isListNotEmpty(dwDatalists.get(i).getItems().get(b).getChild())){
                                                for (int ct = 0; ct < dwDatalists.get(i).getItems().get(b).getChild().size(); ct++) {
                                                    TaskDetailsBean.ResultBean.ItemsBean.ChildBean childBean =  dwDatalists.get(i).getItems().get(b).getChild().get(ct);
                                                    Log.e("childBean",""+childBean.getId());
                                                    TaskDetailsTablePartBean childTable = new TaskDetailsTablePartBean( dwDatalists.get(i).getId(),
                                                            taskid, childBean.getId(), childBean.getPid(),
                                                            childBean.getName(), childBean.getStatus());
                                                    try {
                                                        dbManager.save(childTable);
                                                    } catch (DbException e) {
                                                        e.printStackTrace();
                                                        Log.e("点位表增加检查部位的检查项异常" + i, e.toString());
                                                    }
                                                }
                                        }
                                    }
                                }
                            }
                             /*表里存在数据，目前不需要操作*/
                            else{
                                Log.e("taskbean" + i, "有数据");
                            }

                        } catch (DbException e) {
                            e.printStackTrace();
                        }
//
                    }

//                  重新取出点位数据
                    selectDW();
//                  重新取出第一个点位的部位
                    if (StringUtil.isListNotEmpty(dwlist)){
                        selectBW(dwlist.get(0).getDid());
                    }else{
                        bwlist.clear();
                        tagAdater.notifyDataChanged();
                    }
//                   重新取出第一个点位的第一个部位的所有的检查项
                    if (StringUtil.isListNotEmpty(bwlist)){
                        selectCT(bwlist.get(0).getCid(),bwlist.get(0).getDid());
                    }else{
                        iblist.clear();
                        commonAdapter.notifyDataSetChanged();
                    }
                } else if ("401".equals(taskDetailsListBean.getStatus())) {//token获取失败
                    GetToken mGetToken = new GetToken(InspectActivity.this);
                    mGetToken.getToken();
                } else {//走接口失败
                    ToastUtil.TextToast(taskDetailsListBean.getMsg());
                }
            }
            @Override
            public void onError(Throwable arg0, boolean arg1) {
                Log.e("onError",arg0.toString());
//                  重新取出点位数据
                selectDW();
//                  重新取出第一个点位的部位
                if (StringUtil.isListNotEmpty(dwlist)){
                    selectBW(dwlist.get(0).getDid());
                }else{
                    bwlist.clear();
                    tagAdater.notifyDataChanged();
                }
//                   重新取出第一个点位的第一个部位的所有的检查项
                if (StringUtil.isListNotEmpty(bwlist)){
                    selectCT(bwlist.get(0).getCid(),bwlist.get(0).getDid());
                }else{
                    iblist.clear();
                    commonAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    /**
     * 重新取出点位数据
     */
    private void selectDW() {
        try {
            WhereBuilder d = WhereBuilder.b();
            d.and("reseid", "=", reseid);
            d.and("taskid", "=", taskid);
            List<TaskDetailsTableLoctionBean> dwsqlist  = dbManager.selector(TaskDetailsTableLoctionBean.class).where(d).findAll();
            if (dwsqlist!=null){
                //存放数据到适配器的数据源里
                for (int db = 0; db < dwsqlist.size(); db++) {
                    TaskDetailsTableLoctionDataBean taslListDataBean = new TaskDetailsTableLoctionDataBean(
                            dwsqlist.get(db).getDid(),  dwsqlist.get(db).getReseid(),dwsqlist.get(db).getTaskid(),
                            dwsqlist.get(db).getTitle(),  dwsqlist.get(db).getIdcard()
                            ,dwsqlist.get(db).getAddress(),  dwsqlist.get(db).getXpoint(),dwsqlist.get(db).getYpoint(),
                            dwsqlist.get(db).getStatus(), dwsqlist.get(db).getSignup());
                    dwlist.add(taslListDataBean);
                }
                if (StringUtil.isListNotEmpty(dwlist)){
                    dwlist.get(0).setLin(true);
                }
                mcommonAdapter.notifyDataSetChanged();
                Log.e("dwlistsize",""+dwlist.size());
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
    }
    /**
     * 重新取出点位部位数据
     * @param did 点位id
     */
    private void selectBW(int did) {
        //
        bwlist.clear();
        try {
            WhereBuilder d = WhereBuilder.b();
            d.and("pid", "=", 0);
            d.and("did", "=", did);
            d.and("taskid", "=", taskid);
            Log.e("selectBWdid",""+did);
            List<TaskDetailsTablePartBean> bwsqlist  = dbManager.selector(TaskDetailsTablePartBean.class).where(d).findAll();
            if (bwsqlist!=null){
                //存放数据到适配器的数据源里
                for (int db = 0; db < bwsqlist.size(); db++) {
                    TaskDetailsTablePartBean taslListDataBean = new TaskDetailsTablePartBean(bwsqlist.get(db).getDid(),
                            bwsqlist.get(db).getTaskid(), bwsqlist.get(db).getCid(),  bwsqlist.get(db).getPid(),
                            bwsqlist.get(db).getName(),  bwsqlist.get(db).getStatus());
                    bwlist.add(taslListDataBean);
                }
                tagAdater.notifyDataChanged();
                Log.e("bwlistsize",""+bwlist.size());
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
    }
    /**
     * 重新取出点位部位的检查项数据
     * @param cid 部位id
     */
    private void selectCT(int cid,int did) {
        iblist.clear();
        try {
            WhereBuilder d = WhereBuilder.b();
            d.and("did", "=", did);
            d.and("pid", "=", cid);
            d.and("taskid", "=", taskid);
            Log.e("selectCTdid",""+did+"\npid"+""+cid+"\ntaskid"+""+taskid);
            List<TaskDetailsTablePartBean> ctsqlist  = dbManager.selector(TaskDetailsTablePartBean.class).where(d).findAll();
            if (ctsqlist!=null){
                //存放数据到适配器的数据源里
                for (int db = 0; db < ctsqlist.size(); db++) {
                    TaskDetailsTablePartBean taslListDataBean = new TaskDetailsTablePartBean(ctsqlist.get(db).getDid(),
                            ctsqlist.get(db).getTaskid(), ctsqlist.get(db).getCid(),  ctsqlist.get(db).getPid(),
                            ctsqlist.get(db).getName(),  ctsqlist.get(db).getStatus());
                    iblist.add(taslListDataBean);
                }
                commonAdapter.notifyDataSetChanged();
                Log.e("iblistsize",""+iblist.size());
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void success(int errtype) {
          if (errtype==0){
              taskDati();
          }
    }

    /**
     * 读取文件txt
     */
    /*判断要不要重复读取校验数据 true要，false不要*/
    boolean isFor=true;
    int  isYesNum=0, isum=0;
    public  void ReadTxt(){
        //取得系统时间;读取文件夹还是下面的时间校验都需要读取时间
        //24小时还是12小时取决于HH是大写还是小写
        SimpleDateFormat sdfdate=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date=sdfdate.format(new Date());
        Log.e("date", "" + date);
        //yyyy-MM-dd去掉-符号就可以得到当天文件夹的名称
        SimpleDateFormat sdfFile=new SimpleDateFormat("yyyyMMdd");
        String dateFile=sdfFile.format(new Date());
        Log.e("dateFile", "" + dateFile);
        //yyyy-MM-dd去掉-符号就可以得到当天文件夹的名称
        SimpleDateFormat sdfY=new SimpleDateFormat("yyyy-MM-dd");
        String dateY=sdfY.format(new Date());
        Log.e("dateY", "" + dateY);
        //获得SD卡根目录路径   "/sdcard"
        File sdDir = Environment.getExternalStorageDirectory();
        //根目录下某个txt文件名
        File path = new File(sdDir + File.separator + "/TestTxT/20170110.txt");
        Log.e("path", "" + path.getPath().toString());
        // 判断SD卡是否存在，并且是否具有读写权限
        if (Environment.getExternalStorageState()
                .equals(Environment.MEDIA_MOUNTED)) {
            String content = "";
            map = helper.getFileContent(path);
            Log.e("maphelper", "" + map);
            for (int i = 0; i < map.size(); i++) {
                content += map.get(i) + "\t";
            }
            Log.e("maphelper内容", "" + content);
            //假设txt文件行数是3行，读取每行数据是循环了3遍，但是map的长度却变成了4，
            // map下标从0开始，0.1.2都有数据，3下标是没有数据的。
            // 所以需要的指令是在map长度-2的的下标上
            Log.e("maphelper指令", "" + map.get(map.size() - 2));
            if( map.get(map.size() - 2)!=null){
                //去除空格
                String str = map.get(map.size() - 2).replace(" ", "");
                Log.e("str去除空格",""+ str);
                //去除0x号
                str = map.get(map.size() - 2).replace("0x", "");
                Log.e("str去除0x号",""+ str);
                //建立字符串数组
                String[] temp = str.split(",");
                Log.e("temp42", temp[5]);
                //签到
                int getSVAA = Integer.parseInt(temp[5], 16);
                String tString = Integer.toBinaryString((getSVAA & 0xFF) + 0x100).substring(1);
                String tSD7D6 = tString.substring(0, 2);
                String status = "";
                // 00:正常 : 01:被拆: 10:缺电:
                if (tSD7D6.equals("00")) {
                    status = "正常";
                } else if (tSD7D6.equals("01")) {
                    status = "被拆";
                } else if (tSD7D6.equals("10")) {
                    status = "缺电";
                }
                Log.e("tv_sigup", "" + "对应签到二进制:" + tString + "\t" + "对应签到D7D6:" + tSD7D6 + "\t" + "对应签到:" + status);
                //时间校验成功了，要开始校验卡位
                String nubmer = temp[4]+tString.substring(2, 8);
                Log.e("maphelper手机现在时间", "" +date);
                Log.e("maphelper指令时间", "" +dateY+" "+temp[1]+":"+temp[2]+":"+temp[3]);
                Log.e("maphelper卡号", "" +nubmer);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                try {
                    String mS= String.valueOf(sdf.parse(date).getTime() / 1000);
                    String dS= String.valueOf(sdf.parse(dateY+" "+temp[1]+":"+temp[2]+":"+temp[3]).getTime() / 1000);
                    Log.e("mS秒数",""+mS);
                    Log.e("dS秒数",""+dS);
                    //60秒的误差(时间校验)
                    if (-60<(Integer.parseInt(mS)-Integer.parseInt(dS))&&(Integer.parseInt(mS)-Integer.parseInt(dS))<60){
                        Log.e("60秒的误差",""+(Integer.parseInt(mS)-Integer.parseInt(dS)));
                        if (nubmer.equals("00000010")){
                            tv_sigup.setText("签到:正常");
                        }else{
                            isYesNum++;
                            if (isYesNum>10){
                                tv_sigup.setText("签到:卡号校验失败");
                            }else{
                                handler.sendEmptyMessageDelayed(0, 3000);//启动handler，实现3秒定时循环执行
                            }
                        }
                    }else {
                        isum++;
                        if (isum>10){
                            tv_sigup.setText("签到:时间校验失败");
                        }else{
                            handler.sendEmptyMessageDelayed(0, 3000);//启动handler，实现3秒定时循环执行
                        }
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }else {
                Log.e("kong","kongkong");
            }
            //卡号
        } else {
            Log.e("path", "SD卡不存在");
        }
    }

    // handler类接收数据
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0://读取
                    ReadTxt();
                    break;
            }
        };
    };

    /**
     *启动app（不知到类名的情况下）
     * @param resolveinfo 收集的信息目标>标签。
     */
    private  void  startAPP(ResolveInfo resolveinfo){
        // packagename = 参数packname
        String packageName = resolveinfo.activityInfo.packageName;
        // 这个就是我们要找的该APP的LAUNCHER的Activity[组织形式：packagename.mainActivityname]
        String className = resolveinfo.activityInfo.name;
        // LAUNCHER Intent
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        // 设置ComponentName参数1:packagename参数2:MainActivity路径
        ComponentName cn = new ComponentName(packageName, className);
        intent.setComponent(cn);
        startActivity(intent);
    }
    /**
     *启动app（知到类名的情况下）
     */
    private  void  startClassAPP(){
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        ComponentName cn = new ComponentName("RFIDread", "activityInfo");
        intent.setComponent(cn);
        startActivity(intent);
    }


    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Device_Sensors);
        //可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
//        int span = 1000;
//        option.setScanSpan(span);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
        option.setLocationNotify(true);//可选，默认false，设置是否当GPS有效时按照1S/1次频率输出GPS结果
        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(false);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤GPS仿真结果，默认需要
    }

    public class MyLocationListener extends BDAbstractLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            //获取定位结果
            Log.e("x+y",""+location.getLongitude()+"+"+location.getLatitude());
        }
        }


}
