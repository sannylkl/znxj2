package com.jiarui.znxj.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.jiarui.znxj.Interface.IToken;
import com.jiarui.znxj.R;
import com.jiarui.znxj.adapter.CommonAdapter;
import com.jiarui.znxj.application.AppContext;
import com.jiarui.znxj.base.BaseActivity;
import com.jiarui.znxj.bean.CheckingPointContentBean;
import com.jiarui.znxj.bean.CheckingPointList;
import com.jiarui.znxj.bean.ObservingThingsPointContentTable;
import com.jiarui.znxj.bean.ObservingThingsPointSaveTable;
import com.jiarui.znxj.bean.ObservingThingsTable;
import com.jiarui.znxj.bean.PatrolListBean;
import com.jiarui.znxj.bean.PatrolSettingBean;
import com.jiarui.znxj.constants.GetToken;
import com.jiarui.znxj.constants.InterfaceDefinition;
import com.jiarui.znxj.holder.ViewHolder;
import com.jiarui.znxj.utils.DefaultCommonCallBack;
import com.jiarui.znxj.utils.LogUtil;
import com.jiarui.znxj.utils.PreferencesUtil;
import com.jiarui.znxj.utils.StringUtil;
import com.jiarui.znxj.utils.ToastUtil;

import org.xutils.DbManager;
import org.xutils.db.sqlite.WhereBuilder;
import org.xutils.ex.DbException;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 巡检点列表
 */
public class PatrolListActivity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.common_title_left)
    TextView commonTitleLeft;
    @Bind(R.id.common_title_tv)
    TextView commonTitleTv;
    @Bind(R.id.common_title_right)
    TextView commonTitleRight;
    @Bind(R.id.common_title_layout)
    RelativeLayout commonTitleLayout;
    @Bind(R.id.patrol_list)
    ListView patrolList;
    @Bind(R.id.patrol_tjxjd)
    TextView patrolTjxjd;//添加巡检点
    public  static boolean isupadate=false;
    /*数据库*/
    DbManager dbManager = null;//AppContext.getInstance().getdbManager()

    private List<ObservingThingsPointContentTable> palist = new ArrayList<>();//数据源
    private CommonAdapter<ObservingThingsPointContentTable> commonAdapter;//适配器
    private String reseid,city2,county,reservoir,user_id= null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patrol_list);
        ButterKnife.bind(this);
        //水库id
        reseid= (String) PreferencesUtil.get(this, InterfaceDefinition.PreferencesUser.ReservoId, "");
        //水库名称
        city2 = (String) PreferencesUtil.get(this, InterfaceDefinition.PreferencesUser.City, "");
        county =(String) PreferencesUtil.get(this,InterfaceDefinition.PreferencesUser.County, "");
        reservoir =(String) PreferencesUtil.get(this,InterfaceDefinition.PreferencesUser.Reservoir, "");
        //用户id
        user_id = (String) PreferencesUtil.get(this, InterfaceDefinition.PreferencesUser.USER_ID, "");
        dbManager = x.getDb(AppContext.getConfig());
        commonTitleLeft.setOnClickListener(this);
        initView();
        initData();
        commonTitleTv.setText("巡检点列表");
        commonTitleLeft.setOnClickListener(this);
        patrolTjxjd.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.common_title_left:
                //返回
                finish();
                break;
            case R.id.patrol_tjxjd:
                //添加巡检点
                Bundle bundle = new Bundle();
                bundle.putInt("type", 1);
                bundle.putInt("auto_id",-1);
                bundle.putInt("did",-1);
                bundle.putBoolean("isupadta",false);
                gotoActivity(bundle, PatrolSettingActivity.class);
                break;
        }
    }

    private void initView() {
        commonAdapter = new CommonAdapter<ObservingThingsPointContentTable>(PatrolListActivity.this, palist, R.layout.patrol_list_item) {
            @Override
            public void convert(ViewHolder mHolder, ObservingThingsPointContentTable item, int position) {
                mHolder.setText(R.id.patrol_item_name, item.getRese_name());
                mHolder.setText(R.id.patrol_item_dianwei, item.getTitle());
                if (item.isStatus()==true){
                    mHolder.setTextColor(R.id.patrol_item_dianwei, Color.parseColor("#92C35C"));
                }else{
                    mHolder.setTextColor(R.id.patrol_item_dianwei, Color.parseColor("#F46666"));
                }
            }
        };
        patrolList.setAdapter(commonAdapter);
        patrolList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Bundle bundle = new Bundle();
                bundle.putInt("type", 2);
                bundle.putInt("auto_id",palist.get(i).getId());
                bundle.putBoolean("isupadta",palist.get(i).isupadta());
                bundle.putInt("did",palist.get(i).getDid());
                bundle.putString("dwname",palist.get(i).getTitle());
                bundle.putString("weidu",palist.get(i).getYpoint());
                bundle.putString("jingdu",palist.get(i).getXpoint());
                bundle.putString("card",palist.get(i).getIdcard());
                gotoActivity(bundle, PatrolSettingActivity.class);
            }
        });
    }
    CheckingPointList checkingPointList=null;

    /**
     * 获取巡检点列表
     */
    private void initData() {

        String url = InterfaceDefinition.ICheckingPointList.IURL + PreferencesUtil.get(this, InterfaceDefinition.PreferencesUser.Docking_CREDENTIALS, "");
        LogUtil.e("获取巡检点列表拼接路径", url);
        // 接口对接
        RequestParams mParams = new RequestParams(url);
        mParams.addBodyParameter(InterfaceDefinition.ICheckingPointList.RESEID,reseid);
        x.http().get(mParams, new DefaultCommonCallBack(PatrolListActivity.this, true) {
            @Override
            public void onSuccess(String json) {
                LogUtil.e("获取巡检点列表", json);
                checkingPointList = new Gson().fromJson(json, CheckingPointList.class);
                if (InterfaceDefinition.IStatusCode.SUCCESS.equals(checkingPointList.getStatus())) {
                    //2.判断现在后台给的选中了的观测物本地是不是存在，如果存在就不保存
                    for (int i = 0; i <checkingPointList.getResult().size() ; i++) {
                            CheckingPointList.ResultBean resultbean=checkingPointList.getResult().get(i);
                        //成功添加点位不存在就是保存，存在就是点位的状态进行修改
                        ObservingThingsPointContentTable observingOTdata= null;
                        try {
                            WhereBuilder d = WhereBuilder.b();
                            d.and("user_id", "=", user_id);
                            d.and("reseid", "=", Integer.parseInt(reseid));
                            d.and("did", "=", checkingPointList.getResult().get(i).getId());
                            observingOTdata = dbManager.selector(ObservingThingsPointContentTable.class).where(d).findFirst();
                            if (observingOTdata==null){//保存
                                observingOTdata=new ObservingThingsPointContentTable(checkingPointList.getResult().get(i).getId(),
                                        checkingPointList.getResult().get(i).getTitle(),Integer.parseInt(user_id) , Integer.parseInt(reseid),
                                        city2+county+reservoir ,  checkingPointList.getResult().get(i).getIdcard(),
                                        checkingPointList.getResult().get(i).getXpoint(), checkingPointList.getResult().get(i).getYpoint(), true,true);
                                dbManager.save(observingOTdata);
                        }else{
                                //判断要不要执行修改语句
                            if (!observingOTdata.getTitle().equals(checkingPointList.getResult().get(i).getTitle())||
                                    !observingOTdata.getIdcard().equals(checkingPointList.getResult().get(i).getIdcard())){
                                //修改了主题
                                if (!observingOTdata.getTitle().equals(checkingPointList.getResult().get(i).getTitle())){
                                    observingOTdata.setTitle(checkingPointList.getResult().get(i).getTitle());
                                }
                                //修改了卡号
                                if (!observingOTdata.getIdcard().equals(checkingPointList.getResult().get(i).getIdcard())){
                                     observingOTdata.setIdcard(checkingPointList.getResult().get(i).getIdcard());
                                }
                                dbManager.update(observingOTdata);
                              }
                                 }
                                     } catch (DbException e) {
                                        e.printStackTrace();
                        }
                    }
                    //重新取出点位
                    getAll();
                } else if ("401".equals(checkingPointList.getStatus())) {
                    GetToken mGetToken = new GetToken(new IToken() {
                        @Override
                        public void success(int errtype) {
                            if (errtype == 0) {
                                initData();
                            }
                        }
                    });
                    mGetToken.getToken();
                } else {
                    ToastUtil.TextToast("" + checkingPointList.getMsg());
                    getAll();
                }
            }
            @Override
            public void onError(Throwable arg0, boolean arg1) {
                Log.e("onError获取巡检点列表",arg0.toString());
                getAll();
            }
        });
    }

    /**
     * 获取本地数据
     */
    private void getAll() {
        //查询所有
        try {
            WhereBuilder d = WhereBuilder.b();
            d.and("reseid", "=",Integer.parseInt(reseid));
            List<ObservingThingsPointContentTable> bwsqlist  = dbManager.selector(ObservingThingsPointContentTable.class).where(d).findAll();
            if (bwsqlist!=null){
                palist.clear();
                Log.e("bwsqlist",""+bwsqlist.size());
                palist.addAll(bwsqlist);
                commonAdapter.notifyDataSetChanged();
            }
        } catch (DbException e) {
            e.printStackTrace();
            Log.e("水库点位数据异常",e.toString());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isupadate==true){
            isupadate=false;
            initData();
        }
    }
}
