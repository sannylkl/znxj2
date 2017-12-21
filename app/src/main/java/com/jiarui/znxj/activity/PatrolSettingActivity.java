package com.jiarui.znxj.activity;

import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jiarui.znxj.Interface.IToken;
import com.jiarui.znxj.R;
import com.jiarui.znxj.application.AppContext;
import com.jiarui.znxj.base.BaseActivity;
import com.jiarui.znxj.bean.BaseBean;
import com.jiarui.znxj.bean.CheckingPointContentBean;
import com.jiarui.znxj.bean.ObservingThingsBean;
import com.jiarui.znxj.bean.ObservingThingsCheckPointBean;
import com.jiarui.znxj.bean.ObservingThingsPointContentTable;
import com.jiarui.znxj.bean.ObservingThingsPointSaveTable;
import com.jiarui.znxj.bean.ObservingThingsTable;
import com.jiarui.znxj.constants.GetToken;
import com.jiarui.znxj.constants.InterfaceDefinition;
import com.jiarui.znxj.flow.FlowLayout;
import com.jiarui.znxj.flow.TagAdapter;
import com.jiarui.znxj.flow.TagFlowLayoutALL;
import com.jiarui.znxj.utils.DefaultCommonCallBack;
import com.jiarui.znxj.utils.GPSLocation.GPSLocationListener;
import com.jiarui.znxj.utils.GPSLocation.GPSLocationManager;
import com.jiarui.znxj.utils.GPSLocation.GPSProviderStatus;
import com.jiarui.znxj.utils.LogUtil;
import com.jiarui.znxj.utils.PreferencesUtil;
import com.jiarui.znxj.utils.StringUtil;
import com.jiarui.znxj.utils.ToastUtil;
import com.luck.picture.lib.tools.StringUtils;

import org.xutils.DbManager;
import org.xutils.db.sqlite.WhereBuilder;
import org.xutils.ex.DbException;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import butterknife.Bind;
import butterknife.ButterKnife;

import static com.jiarui.znxj.R.id.setting_btgcw;
import static com.jiarui.znxj.R.id.setting_upordown;

/**
 * 巡检点设置
 */
public class PatrolSettingActivity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.common_title_left)
    TextView settingLeft;//返回
    @Bind(R.id.setting_bc)
    TextView settingBc;//完成
    @Bind(R.id.setting_akmc)
    TextView settingAkmc;//水库名称
    @Bind(R.id.setting_btsk)
    LinearLayout settingBtsk;//水库名称点击
    @Bind(R.id.setting_dwmc)
    EditText settingDwmc;//点位名称
    @Bind(setting_btgcw)
    LinearLayout settingBtgcw;//观测物点击
    @Bind(setting_upordown)
    ImageView settingUpordown;//上还是下
    @Bind(R.id.id_flowlayout)
    TagFlowLayoutALL idFlowlayout;
    @Bind(R.id.setting_wd)
    TextView setting_wd;//纬度
    @Bind(R.id.setting_jd)
    TextView setting_jd;//精度
    @Bind(R.id.patrol_bsk)
    EditText patrol_bsk;//标示卡
    private TagFlowLayoutALL mFlowLayout;
    private String[] mVals = new String[]
            {"坝体", "坝基", "坝区", "溢洪道", "夹门及通讯设备"};
    int type,pass_did,auto_id;//是添加修改/再次添加；did好做修改用：查询自身数据用
    String Observation;
    private GPSLocationManager gpsLocationManager;
    private TagAdapter tagAdater;
    /*数据库*/
    DbManager dbManager = null;//AppContext.getInstance().getdbManager()
    private String reseid,reservoir,city2,county= null;
    private boolean isupadta;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patrol_setting);
        ButterKnife.bind(this);
        dbManager = x.getDb(AppContext.getConfig());
        settingLeft.setOnClickListener(this);
        settingBtsk.setOnClickListener(this);
        settingBc.setOnClickListener(this);
        reseid= (String) PreferencesUtil.get(this, InterfaceDefinition.PreferencesUser.ReservoId, "");
        //水库名称
        city2 = (String) PreferencesUtil.get(PatrolSettingActivity.this, InterfaceDefinition.PreferencesUser.City, "");
        county =(String) PreferencesUtil.get(PatrolSettingActivity.this,InterfaceDefinition.PreferencesUser.County, "");
        reservoir =(String) PreferencesUtil.get(PatrolSettingActivity.this,InterfaceDefinition.PreferencesUser.Reservoir, "");
        settingAkmc.setText(city2+county+reservoir);
        Bundle bundle = getIntent().getExtras();
        type = bundle.getInt("type");
        isupadta = bundle.getBoolean("isupadta");
        pass_did= bundle.getInt("did");
        auto_id= bundle.getInt("auto_id");
        initFlowyout();//初始化控件
        if (type == 1) {//点击添加进入
            gpsLocationManager = GPSLocationManager.getInstances(PatrolSettingActivity.this);
            //开启定位
            gpsLocationManager.start(new MyListener(),false);
            settingDwmc.setHint("请输入点位");
            //获取观测物
            ObservingThings();
        } else if (type == 2) {//列表进入
            String dwname = bundle.getString("dwname");
            Observation = bundle.getString("Observation");
            String weidu = bundle.getString("weidu");
            String jingdu = bundle.getString("jingdu");
            String card = bundle.getString("card");
            settingDwmc.setText(dwname);
            settingDwmc.setSelection(dwname.length());
            setting_wd.setText(String.valueOf(weidu));
            setting_jd.setText(String.valueOf(jingdu));
            patrol_bsk.setText(card);
            patrol_bsk.setSelection(card.length());
            if (isupadta==true){
                GetCheckingPointContent();//只有成功过的才可以获取巡检点内容。
                // 然后跟换后台返回的数据获取对应的数据
            }else{ //没添加成功的，拿本地数据用的是这个点的自动增长的id
                LocalObservingThings(auto_id);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (gpsLocationManager!=null){
            gpsLocationManager.stop();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.common_title_left:
                //返回
                finish();
                break;
            case R.id.setting_btsk:
                //选择水库
                break;
            case R.id.setting_bc:
                if (StringUtil.isEmpty(settingDwmc.getText().toString().trim())){
                    ToastUtil.TextToast("点位名称不能为空");
                    return;
                }
                if (StringUtil.isListNotEmpty(otlist)){
                    /*修改内容异常数据的状态*/
                    Set<Integer> selectccalist=mFlowLayout.getSelectedList();
                    if ( selectccalist == null || selectccalist.size() == 0){
                        ToastUtil.TextToast("请选择观测物");
                        return;
                    }
                }
                if (StringUtil.isEmpty(setting_jd.getText().toString().trim())){
                    ToastUtil.TextToast("定位失败，请重新进入定位");
                    return;
                }
                if (StringUtil.isEmpty(setting_wd.getText().toString().trim())){
                    ToastUtil.TextToast("定位失败，请重新进入定位");
                    return;
                }
                if (StringUtil.isEmpty(patrol_bsk.getText().toString().trim())){
                    ToastUtil.TextToast("标示卡不能为空");
                    return;
                }
                idFlowlayout.setEnabled(false);
                PatrolListActivity.isupadate=true;
                if (isupadta==false){//添加巡检点
                    AddCheckingPoint();
                }else{//修改巡检点
                    UpadataCheckingPoint();
                }
                break;
        }
    }

    class MyListener implements GPSLocationListener {

        @Override
        public void UpdateLocation(Location location) {
            if (location != null) {
                setting_jd.setText(""+location.getLongitude());
                setting_wd.setText(""+location.getLatitude());
            }else{
                setting_jd.setText("");
                setting_wd.setText("");
            }
        }

        @Override
        public void UpdateStatus(String provider, int status, Bundle extras) {
            if ("gps" == provider) {
                Toast.makeText(PatrolSettingActivity.this, "定位类型：" + provider, Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void UpdateGPSProviderStatus(int gpsStatus) {
            switch (gpsStatus) {
                case GPSProviderStatus.GPS_ENABLED:
                    Toast.makeText(PatrolSettingActivity.this, "GPS开启", Toast.LENGTH_SHORT).show();
                    break;
                case GPSProviderStatus.GPS_DISABLED:
                    Toast.makeText(PatrolSettingActivity.this, "GPS关闭", Toast.LENGTH_SHORT).show();
                    break;
                case GPSProviderStatus.GPS_OUT_OF_SERVICE:
                    Toast.makeText(PatrolSettingActivity.this, "GPS不可用", Toast.LENGTH_SHORT).show();
                    break;
                case GPSProviderStatus.GPS_TEMPORARILY_UNAVAILABLE:
                    Toast.makeText(PatrolSettingActivity.this, "GPS暂时不可用", Toast.LENGTH_SHORT).show();
                    break;
                case GPSProviderStatus.GPS_AVAILABLE:
                    Toast.makeText(PatrolSettingActivity.this, "GPS可用啦", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }
    List< ObservingThingsTable> otlist=null;
    ObservingThingsBean observingThingsBean=null;
    private void initFlowyout() {
        mFlowLayout = (TagFlowLayoutALL) findViewById(R.id.id_flowlayout);
        //观测物数据
        otlist= new ArrayList< ObservingThingsTable>();
            final LayoutInflater mInflater = LayoutInflater.from(PatrolSettingActivity.this);
            tagAdater=new TagAdapter< ObservingThingsTable>(otlist) {
                @Override
                public View getView(FlowLayout parent, int position, ObservingThingsTable s) {
                    ObservingThingsTable it= otlist.get(position);
                    TextView tv = (TextView) mInflater.inflate(R.layout.tv, mFlowLayout, false);
                    tv.setText(it.getName());
                    return tv;
                }
            };
            mFlowLayout.setAdapter(tagAdater);
    }
    ObservingThingsCheckPointBean checkPoint=null;
    /**
     * 获取观测物
     */
    private void ObservingThings() {
        String url = InterfaceDefinition.IObservingThings.IURL + PreferencesUtil.get(this, InterfaceDefinition.PreferencesUser.Docking_CREDENTIALS, "");
        LogUtil.e("获取观测物拼接路径", url);
        // 接口对接
        RequestParams mParams = new RequestParams(url);
        mParams.addBodyParameter(InterfaceDefinition.IObservingThings.RESEID,reseid);
        x.http().get(mParams, new DefaultCommonCallBack(PatrolSettingActivity.this, true) {
            @Override
            public void onSuccess(String json) {
                LogUtil.e("获取观测物", json);
                observingThingsBean = new Gson().fromJson(json, ObservingThingsBean.class);
                if (InterfaceDefinition.IStatusCode.SUCCESS.equals(observingThingsBean.getStatus())) {
                    Log.e("SUCCESS", "SUCCESS");
                    Log.e("observingThingsSize", ""+observingThingsBean.getResult().size());
                    for (int i = 0; i < observingThingsBean.getResult().size(); i++) {
                        Log.e("observingThings", ""+i);
                        ObservingThingsBean.ResultBean  otBean = observingThingsBean.getResult().get(i);
                        //增加内容表
                        try {
                            WhereBuilder d = WhereBuilder.b();
                            d.and("reseid", "=",Integer.parseInt(reseid));
                            d.and("otid", "=", otBean.getId());
                            ObservingThingsTable ccdata = dbManager.selector(ObservingThingsTable.class).where(d).findFirst();
                            if (ccdata == null) {
                                Log.e("ccdata", "无值");
                                //整个水库观测物增加
                                ObservingThingsTable  observingThingsTable = new ObservingThingsTable(
                                        Integer.parseInt(reseid), otBean.getId(), otBean.getPid(), otBean.getCategory(),
                                        otBean.getName(), otBean.getDescription(),  false,otBean.getLevel());
                                try {
                                    dbManager.save(observingThingsTable);
                                } catch (DbException e) {
                                    e.printStackTrace();
                                    Log.e("整个水库观测物增加异常", e.toString());
                                }
                            } else {
                                Log.e("整个水库观测物内容表", "有数据");
                            }
                        } catch (DbException e) {
                            e.printStackTrace();
                            Log.e("整个水库观测物内容表",e.toString());
                        }
                    }
                    getReseObservingThings(reseid);
                } else if ("401".equals(observingThingsBean.getStatus())) {
                    GetToken mGetToken = new GetToken(new IToken() {
                        @Override
                        public void success(int errtype) {
                            if (errtype == 0) {
                                ObservingThings();
                            }
                        }
                    });
                    mGetToken.getToken();
                } else {
                    ToastUtil.TextToast("" + observingThingsBean.getMsg());
                    getReseObservingThings(reseid);
                }
            }

            @Override
            public void onError(Throwable arg0, boolean arg1) {
                Log.e("onError获取观测物",arg0.toString());
                getReseObservingThings(reseid);
            }
        });
    }


    /**
     * 获取水库的观测物
     */
    private void getReseObservingThings(String reseid) {
        Log.e("reseid",""+reseid);
        otlist.clear();
        try {
            WhereBuilder d = WhereBuilder.b();
            d.and("reseid", "=",Integer.parseInt(reseid));
            List<ObservingThingsTable> bwsqlist  = dbManager.selector(ObservingThingsTable.class).where(d).findAll();
            if (bwsqlist!=null){
                Log.e("bwsqlist",""+bwsqlist.size());
                //存放数据到适配器的数据源里
                for (int db = 0; db < bwsqlist.size(); db++) {
                    ObservingThingsTable ccadata = new ObservingThingsTable(
                            bwsqlist.get(db).getReseid(), bwsqlist.get(db).getOtid() , bwsqlist.get(db).getPid(),
                            bwsqlist.get(db).getCategory(),  bwsqlist.get(db).getName(),  bwsqlist.get(db).getDescription(),
                            bwsqlist.get(db).getStatus(), bwsqlist.get(db).getLevel());
                    otlist.add(ccadata);
                }
                tagAdater.notifyDataChanged();
            }
        } catch (DbException e) {
            e.printStackTrace();
            Log.e("水库观测物数据异常",e.toString());
        }
    }

    /**
     * 获取本地的观测物
     * 为了区分不同的点，为了防止后台did和本地存的did相同取不到对应的。就加了个状态
     * 成功保存就是这个后台给的id状态为true，添加失败就是这个点自动增长的id状态为false
     *成功下次进来就是修改就取之前这个点成功保存的观测物，失败下次就来就取保存失败的观测物
     */
    private void LocalObservingThings(int did) {
        final String user_id = (String) PreferencesUtil.get(this, InterfaceDefinition.PreferencesUser.USER_ID, "");
        Log.e("Localdid",""+did);
        otlist.clear();
        Set<Integer> set=new HashSet();
        if (isupadta==true){//添加成功的，拿后台给的数据用的是这个点的did
            try {
                WhereBuilder d = WhereBuilder.b();
                d.and("user_id", "=", user_id);
                d.and("reseid", "=", Integer.parseInt(reseid));
                d.and("did", "=", did);
                d.and("status", "=", true);
                List<ObservingThingsPointSaveTable> bwsqlist  = dbManager.selector(ObservingThingsPointSaveTable.class).where(d).findAll();
                if (bwsqlist!=null){
                    Log.e("Localbwsqlist",""+bwsqlist.size());
                    //存放数据到适配器的数据源里，默认都是选择好的
                    for (int db = 0; db < bwsqlist.size(); db++) {
                        ObservingThingsTable ccadata = new ObservingThingsTable(
                                bwsqlist.get(db).getReseid(), bwsqlist.get(db).getOtid() , bwsqlist.get(db).getPid(),
                                bwsqlist.get(db).getCategory(),  bwsqlist.get(db).getName(),  bwsqlist.get(db).getDescription(),
                                true, bwsqlist.get(db).getLevel());
                        otlist.add(ccadata);
                        set.add(db);
                    }
                    tagAdater.setSelectedList(set);
                }
            } catch (DbException e) {
                e.printStackTrace();
            }
        }else{//没添加成功的，拿本地数据用的是这个点的自动增长的id
            try {
                WhereBuilder d = WhereBuilder.b();
                d.and("user_id", "=", user_id);
                d.and("reseid", "=", Integer.parseInt(reseid));
                d.and("did", "=", did);
                d.and("status", "=", false);
                List<ObservingThingsPointSaveTable> bwsqlist  = dbManager.selector(ObservingThingsPointSaveTable.class).where(d).findAll();
                if (bwsqlist!=null){
                    Log.e("bwsqlist",""+bwsqlist.size());
                    //存放数据到适配器的数据源里，默认都是选择好的
                    for (int db = 0; db < bwsqlist.size(); db++) {
                        ObservingThingsTable ccadata = new ObservingThingsTable(
                                bwsqlist.get(db).getReseid(), bwsqlist.get(db).getOtid() , bwsqlist.get(db).getPid(),
                                bwsqlist.get(db).getCategory(),  bwsqlist.get(db).getName(),  bwsqlist.get(db).getDescription(),
                                true, bwsqlist.get(db).getLevel());
                        otlist.add(ccadata);
                        set.add(db);
                    }
                    tagAdater.setSelectedList(set);
                }
            } catch (DbException e) {
                e.printStackTrace();
            }
        }
    }


    //检查项id
    ArrayList<String> urStringlist=new ArrayList<String>();
    /**
     * 添加巡检点
     */
    Set<Integer> selectccalist;
    private void AddCheckingPoint() {
        urStringlist.clear();
        String itemids;
         /*获取选中的项,点击之后虽然已经禁止点击，但是为了防止就取触发完成是的选中项*/
        selectccalist=mFlowLayout.getSelectedList();
        if (selectccalist != null && selectccalist.size() > 0){
            Iterator<Integer> it = selectccalist.iterator();
            while(it.hasNext()){//判断是否有下一个
                int preIndex = it.next();
                urStringlist.add(""+otlist.get(preIndex).getOtid());
            }
        }
        final String user_id = (String) PreferencesUtil.get(this, InterfaceDefinition.PreferencesUser.USER_ID, "");
        //保存这个数据
        String url = InterfaceDefinition.IAddCheckingPoint.IURL + PreferencesUtil.get(this, InterfaceDefinition.PreferencesUser.Docking_CREDENTIALS, "");
        LogUtil.e("添加巡检点拼接路径", url);
        // 接口对接
        RequestParams mParams = new RequestParams(url);
        if (urStringlist != null && urStringlist.size() > 0) {
            //截取字符串
            itemids=urStringlist.toString().substring(1,urStringlist.toString().length()-1);
            Log.e("urStringlist",itemids);
            mParams.addBodyParameter(InterfaceDefinition.IAddCheckingPoint.ITEMIDS,itemids);
        }
        mParams.addBodyParameter(InterfaceDefinition.IAddCheckingPoint.TITLE,settingDwmc.getText().toString().trim());
        mParams.addBodyParameter(InterfaceDefinition.IAddCheckingPoint.IDCARD,patrol_bsk.getText().toString().trim());
        mParams.addBodyParameter(InterfaceDefinition.IAddCheckingPoint.XPOINT,setting_jd.getText().toString().trim());
        mParams.addBodyParameter(InterfaceDefinition.IAddCheckingPoint.YPOINT,setting_wd.getText().toString().trim());
//        String ADDRESS = "address";不知道是什么没有传入
        mParams.addBodyParameter(InterfaceDefinition.IAddCheckingPoint.RESEID,reseid);
        x.http().post(mParams, new DefaultCommonCallBack(PatrolSettingActivity.this, true) {
            @Override
            public void onSuccess(String json) {
                LogUtil.e("添加巡检点", json);
                checkPoint = new Gson().fromJson(json, ObservingThingsCheckPointBean.class);
                if (InterfaceDefinition.IStatusCode.SUCCESS.equals(checkPoint.getStatus())) {
                    //保存这个点的观测物
                    sucAddCheckingPoint(Integer.parseInt(user_id),checkPoint.getId());
                } else if ("401".equals(checkPoint.getStatus())) {
                    GetToken mGetToken = new GetToken(new IToken() {
                        @Override
                        public void success(int errtype) {
                            if (errtype == 0) {
                                AddCheckingPoint();
                            }
                        }
                    });
                    mGetToken.getToken();
                } else {
                    ToastUtil.TextToast("" + checkPoint.getMsg());
                    errorAddCheckingPoint(Integer.parseInt(user_id),pass_did);

                }
            }
            @Override
            public void onError(Throwable arg0, boolean arg1) {
                super.onError(arg0, arg1);
                Log.e("onError",arg0.toString());
                errorAddCheckingPoint(Integer.parseInt(user_id),pass_did);
            }
        });
    }


    /**
     * 成功添加巡检点
     */
    private void sucAddCheckingPoint(int user_id,int did) {
        //成功添加点位不存在就是保存，存在就是点位的状态进行修改
        ObservingThingsPointContentTable observingOTdata= null;
        try {
            WhereBuilder d = WhereBuilder.b();
            d.and("user_id", "=", user_id);
            d.and("reseid", "=", Integer.parseInt(reseid));
            d.and("did", "=", did);
            observingOTdata = dbManager.selector(ObservingThingsPointContentTable.class).where(d).findFirst();
            if (observingOTdata==null){//保存
                observingOTdata=new ObservingThingsPointContentTable(did, settingDwmc.getText().toString().trim(),
                        user_id, Integer.parseInt(reseid),settingAkmc.getText().toString().trim() , patrol_bsk.getText().toString().trim(),
                        setting_jd.getText().toString().trim(), setting_wd.getText().toString().trim(), true,true);
                dbManager.save(observingOTdata);
            }else{//存在之前存在本地状态是false就是进行修改状态。其他值不能改变。这个是不是在那个点都不知道。
                // 之前状态为true的是保存成功的再进来就是走修改接口
                // 要加个按钮自自行触发最好。就是操作人员自行触发什么时候该重新定位
                observingOTdata.setDid(did);
                observingOTdata.setTitle( settingDwmc.getText().toString().trim());
                observingOTdata.setRese_name( settingAkmc.getText().toString().trim());
                observingOTdata.setIdcard( patrol_bsk.getText().toString().trim());
                observingOTdata.setXpoint( setting_jd.getText().toString().trim());
                observingOTdata.setYpoint( setting_wd.getText().toString().trim());
                observingOTdata.setStatus(true);
                observingOTdata.setIsupadta(true);
                dbManager.update(observingOTdata);
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
        //成功保存了的。下面观测的上级did,就是就是这个后台返回的id
        sucAddObservingThings(user_id,did);
    }

    /**
     * 总结：走添加接口的时候要添加观测物不管是调用添加成功的函数还是添加失败的函数要先清除掉之前保存的观测物，他们的状态就都是false；
     * 不管成功还是失败都要区别：不存在过就执行save语句。存在过执行update修改语句。
     * 不想成功和失败在一个函数里传入参数在一个函数里判断，就分开调用函数
     *简介：成功添加巡检点的观测物
     * 走添加接口成功之后就要删除掉之前存好的数据就是那些本地状态为false的
     * 添加成功之后就要变为true，（下次进来的时候就是走修改的方法。状态就是true的那些）
     * 不想重复查数据，所以直接就是写4个成功的。4个失败的函数，2个成功+2个失败是添加的，2个成功+2个失败是修改的
     */
    private void sucAddObservingThings(int user_id,int did) {
        /*点位删除之前选中观测物数据，确保保存的是最新的*/
        try {
            WhereBuilder d = WhereBuilder.b();
            d.and("user_id", "=", user_id);
            d.and("reseid", "=", Integer.parseInt(reseid));
            d.and("did", "=", auto_id);//点击添加是第一次进入的auto_id是-1是查不到的。所以是假删除。
            // 点击列表是再次进入之前保存失败了，did都是这个点自动增长的id。所以查的到就是真的删除
            d.and("status", "=", false);
            dbManager.delete(ObservingThingsPointSaveTable.class, d);
        } catch (DbException e) {
            e.printStackTrace();
            Log.e("点位删除之前选中观测物数据异常",e.toString());
        }
    /*重新查询选中的观测物,并保存*/
        if (selectccalist != null && selectccalist.size() > 0) {
            ObservingThingsPointSaveTable observingTSdata= null;
            Iterator<Integer> it = selectccalist.iterator();
            while (it.hasNext()) {//判断是否有下一个
                int preIndex = it.next();
                Log.e("it.next()", "" + preIndex);
                //取出已经保存好了的点位里的观测物
                try {
                    WhereBuilder d = WhereBuilder.b();
                    d.and("user_id", "=", user_id);
                    d.and("reseid", "=", Integer.parseInt(reseid));
                    d.and("did", "=", did);
                    d.and("otid", "=",otlist.get(preIndex).getOtid());
                    observingTSdata = dbManager.selector(ObservingThingsPointSaveTable.class).where(d).findFirst();
                    if (observingTSdata==null){
                        observingTSdata=new ObservingThingsPointSaveTable(
                                user_id, Integer.parseInt(reseid),  did, otlist.get(preIndex).getOtid(),
                                otlist.get(preIndex).getPid(), otlist.get(preIndex).getCategory(), otlist.get(preIndex).getName(),
                                otlist.get(preIndex).getDescription(),true, otlist.get(preIndex).getLevel());
                        dbManager.save(observingTSdata);
                    }
                } catch (DbException e) {
                    e.printStackTrace();
                }
            }
        }
        finish();
    }

    /**
     * 简介：失败添加巡检点（did（就是这个点自己的id）。这个是以后台为准，本地的是假的。对于添加来说没用。
     * 失败里添加的巡检点的观测物的did是这个点自动增长的id。所以走添加接口来说是没有用的，修改才是要传入did。
     * 本地传入假的只是为了不出现空指针而已）
     * 点击添加进入是第一次然后添加失败did传入的是-1。
     * 点击列表进入之后添加失败did传入是之前保存了的还是0;
     * 这里查的是;
     */
    private void errorAddCheckingPoint(int user_id,int did) {
        ObservingThingsPointContentTable observingOTdata= null;
        try {
            WhereBuilder d = WhereBuilder.b();
            d.and("user_id", "=", user_id);
            d.and("reseid", "=", reseid);
            d.and("id", "=", auto_id);
            observingOTdata = dbManager.selector(ObservingThingsPointContentTable.class).where(d).findFirst();
            if (observingOTdata==null){//不存在就是保存
                observingOTdata=new ObservingThingsPointContentTable(did, settingDwmc.getText().toString().trim(),
                        user_id, Integer.parseInt(reseid) ,settingAkmc.getText().toString().trim(), patrol_bsk.getText().toString().trim(),
                        setting_jd.getText().toString().trim(),
                        setting_wd.getText().toString().trim(), false,false);
                dbManager.save(observingOTdata);
            }else{//存在之前存在本地状态是false，再次保存还是失败就什么都不用干也用修改
                // 之前状态为true的是保存成功的再进来就是走修改接口
                // 要加个按钮自自行触发最好。就是操作人员自行触发什么时候该重新定位
                observingOTdata.setTitle( settingDwmc.getText().toString().trim());
                observingOTdata.setRese_name( settingAkmc.getText().toString().trim());
                observingOTdata.setIdcard( patrol_bsk.getText().toString().trim());
                observingOTdata.setXpoint( setting_jd.getText().toString().trim());
                observingOTdata.setYpoint( setting_wd.getText().toString().trim());
                observingOTdata.setStatus(false);
                observingOTdata.setIsupadta(false);
                dbManager.update(observingOTdata);
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
        //下面观测物品的上级did,就是就是这个点自动增长的id
        errorAddObservingThings(user_id,observingOTdata.getId());
    }

    /**
     * 失败添加巡检点的观测物
     */
    private void errorAddObservingThings(int user_id,int did) {
         /*点位删除之前保存失败时选中观测物数据，确保保存的是最新的*/
        try {
            WhereBuilder d = WhereBuilder.b();
            d.and("user_id", "=", user_id);
            d.and("reseid", "=", Integer.parseInt(reseid));
            d.and("did", "=", did);
            d.and("status", "=", false);
            dbManager.delete(ObservingThingsPointSaveTable.class, d);
        } catch (DbException e) {
            e.printStackTrace();
            Log.e("点位删除之前保存失败时选中观测物数据异常",e.toString());
        }
    /*查询选中的观测物,并保存*/
        if (selectccalist != null && selectccalist.size() > 0) {
            ObservingThingsPointSaveTable observingTSdata= null;
            Iterator<Integer> it = selectccalist.iterator();
            while (it.hasNext()) {//判断是否有下一个
                int preIndex = it.next();
                Log.e("it.next()", "" + preIndex);
                //取出已经保存好了的点位里的观测物
                try {
                    WhereBuilder d = WhereBuilder.b();
                    d.and("user_id", "=", user_id);
                    d.and("reseid", "=", Integer.parseInt(reseid));
                    d.and("did", "=", did);
                    d.and("otid", "=",otlist.get(preIndex).getOtid());
                    observingTSdata = dbManager.selector(ObservingThingsPointSaveTable.class).where(d).findFirst();
                    if (observingTSdata==null){
                        observingTSdata=new ObservingThingsPointSaveTable(
                                user_id, Integer.parseInt(reseid),  did, otlist.get(preIndex).getOtid(),
                                otlist.get(preIndex).getPid(), otlist.get(preIndex).getCategory(), otlist.get(preIndex).getName(),
                                otlist.get(preIndex).getDescription(),false, otlist.get(preIndex).getLevel());
                        dbManager.save(observingTSdata);
                    }
                } catch (DbException e) {
                    e.printStackTrace();
                }
            }
        }
        finish();
    }


    BaseBean baseBean=null;

    /**
     * 修改巡检点接口
     */
    private void UpadataCheckingPoint() {
        urStringlist.clear();
        String itemids;
         /*获取选中的项,点击之后虽然已经禁止点击，但是为了防止就取触发完成了的选中项*/
        selectccalist=mFlowLayout.getSelectedList();
        if (selectccalist != null && selectccalist.size() > 0){
            Iterator<Integer> it = selectccalist.iterator();
            while(it.hasNext()){//判断是否有下一个
                int preIndex = it.next();
                urStringlist.add(""+otlist.get(preIndex).getOtid());
            }
        }
        final String user_id = (String) PreferencesUtil.get(this, InterfaceDefinition.PreferencesUser.USER_ID, "");
        //保存这个数据
        String url = InterfaceDefinition.IUpdataCheckingPoint.IURL + PreferencesUtil.get(this, InterfaceDefinition.PreferencesUser.Docking_CREDENTIALS, "");
        LogUtil.e("修改巡检点拼接路径", url);
        // 接口对接
        RequestParams mParams = new RequestParams(url);
        if (urStringlist != null && urStringlist.size() > 0) {
            //截取字符串
            itemids=urStringlist.toString().substring(1,urStringlist.toString().length()-1);
            mParams.addBodyParameter(InterfaceDefinition.IUpdataCheckingPoint.ITEMIDS,itemids);
        }
        mParams.addBodyParameter(InterfaceDefinition.IUpdataCheckingPoint.TITLE,settingDwmc.getText().toString().trim());
        mParams.addBodyParameter(InterfaceDefinition.IUpdataCheckingPoint.IDCARD,patrol_bsk.getText().toString().trim());
        mParams.addBodyParameter(InterfaceDefinition.IUpdataCheckingPoint.XPOINT,setting_jd.getText().toString().trim());
        mParams.addBodyParameter(InterfaceDefinition.IUpdataCheckingPoint.YPOINT,setting_wd.getText().toString().trim());
//        String ADDRESS = "address";不知道是什么没有传入
        mParams.addBodyParameter(InterfaceDefinition.IUpdataCheckingPoint.RESEID,reseid);
        mParams.addBodyParameter(InterfaceDefinition.IUpdataCheckingPoint.ID,""+pass_did);
        x.http().post(mParams, new DefaultCommonCallBack(PatrolSettingActivity.this, true) {
            @Override
            public void onSuccess(String json) {
                LogUtil.e("修改巡检点内容", json);
                baseBean = new Gson().fromJson(json, BaseBean.class);
                if (InterfaceDefinition.IStatusCode.SUCCESS.equals(baseBean.getStatus())) {
                    //修改这个点里的
                    sucUpadtaCheckingPoint(Integer.parseInt(user_id),pass_did);
                } else if ("401".equals(baseBean.getStatus())) {
                    GetToken mGetToken = new GetToken(new IToken() {
                        @Override
                        public void success(int errtype) {
                            if (errtype == 0) {
                                UpadataCheckingPoint();
                            }
                        }
                    });
                    mGetToken.getToken();
                } else {
                    ToastUtil.TextToast("" + baseBean.getMsg());
                    errorUpadtaCheckingPoint(Integer.parseInt(user_id),pass_did);
                }
            }
            @Override
            public void onError(Throwable arg0, boolean arg1) {
                Log.e("onError修改巡检点",arg0.toString());
                errorUpadtaCheckingPoint(Integer.parseInt(user_id),pass_did);
            }
        });
    }

    /**
     * 成功修改巡检点
     */
    private void sucUpadtaCheckingPoint(int user_id,int did) {
        ObservingThingsPointContentTable observingOTdata= null;
        try {
            WhereBuilder d = WhereBuilder.b();
            d.and("user_id", "=", user_id);
            d.and("reseid", "=", reseid);
            d.and("did", "=", did);
            observingOTdata = dbManager.selector(ObservingThingsPointContentTable.class).where(d).findFirst();
            observingOTdata.setTitle(settingDwmc.getText().toString().trim());
            observingOTdata.setIdcard(patrol_bsk.getText().toString().trim());
            observingOTdata.setXpoint(setting_jd.getText().toString().trim());
            observingOTdata.setYpoint(setting_wd.getText().toString().trim());
            observingOTdata.setStatus(true);
            dbManager.update(observingOTdata);
        } catch (DbException e) {
            e.printStackTrace();
        }
        //成功修改巡检点里的观测物
        sucUpadataObservingThings(user_id,did);
    }
    /**
     * 成功修改巡检点里的观测物。传入的did要是用上面给的不能取全局的，这个是对应了后台的
     */
    private void sucUpadataObservingThings(int user_id,int did) {
        /*点位删除之前选中观测物数据，确保保存的是最新的*/
        try {
            WhereBuilder d = WhereBuilder.b();
            d.and("user_id", "=", user_id);
            d.and("reseid", "=", Integer.parseInt(reseid));
            d.and("did", "=", did);//点击添加是第一次进入的auto_id是-1是查不到的。所以是假删除。
            // 点击列表是再次进入之前保存失败了，did都是这个点自动增长的id。所以查的到就是真的删除
            d.and("status", "=", true);
            dbManager.delete(ObservingThingsPointSaveTable.class, d);
        } catch (DbException e) {
            e.printStackTrace();
            Log.e("点位删除之前选中观测物数据异常",e.toString());
        }
    /*重新查询选中的观测物,并保存*/
        if (selectccalist != null && selectccalist.size() > 0) {
            ObservingThingsPointSaveTable observingTSdata= null;
            Iterator<Integer> it = selectccalist.iterator();
            while (it.hasNext()) {//判断是否有下一个
                int preIndex = it.next();
                Log.e("it.next()", "" + preIndex);
                //取出已经保存好了的点位里的观测物
                try {
                    WhereBuilder d = WhereBuilder.b();
                    d.and("user_id", "=", user_id);
                    d.and("reseid", "=", Integer.parseInt(reseid));
                    d.and("did", "=", did);
                    d.and("otid", "=",otlist.get(preIndex).getOtid());
                    observingTSdata = dbManager.selector(ObservingThingsPointSaveTable.class).where(d).findFirst();
                    if (observingTSdata==null){
                        observingTSdata=new ObservingThingsPointSaveTable(
                                user_id, Integer.parseInt(reseid),  did, otlist.get(preIndex).getOtid(),
                                otlist.get(preIndex).getPid(), otlist.get(preIndex).getCategory(), otlist.get(preIndex).getName(),
                                otlist.get(preIndex).getDescription(),true, otlist.get(preIndex).getLevel());
                        dbManager.save(observingTSdata);
                    }
                } catch (DbException e) {
                    e.printStackTrace();
                }
            }
        }
        finish();
    }

    /**
     * 失败修改巡检点
     */
    private void errorUpadtaCheckingPoint(int user_id,int did) {
        ObservingThingsPointContentTable observingOTdata= null;
        try {
            WhereBuilder d = WhereBuilder.b();
            d.and("user_id", "=", user_id);
            d.and("reseid", "=", reseid);
            d.and("did", "=", did);
            observingOTdata = dbManager.selector(ObservingThingsPointContentTable.class).where(d).findFirst();
            observingOTdata.setTitle(settingDwmc.getText().toString().trim());
            observingOTdata.setIdcard(patrol_bsk.getText().toString().trim());
            observingOTdata.setXpoint(setting_jd.getText().toString().trim());
            observingOTdata.setYpoint(setting_wd.getText().toString().trim());
            observingOTdata.setStatus(false);
            dbManager.update(observingOTdata);
        } catch (DbException e) {
            e.printStackTrace();
        }
        //修改失败，重新修改这个点位的观测物
        errorSucUpadtaObservingThings(user_id,did);
    }
    /**
     * 失败修改巡检点里的观测物品
     */
    private void errorSucUpadtaObservingThings(int user_id,int did) {
         /*点位删除之前保存失败时选中观测物数据，确保保存的是最新的*/
        try {
            WhereBuilder d = WhereBuilder.b();
            d.and("user_id", "=", user_id);
            d.and("reseid", "=", Integer.parseInt(reseid));
            d.and("did", "=", did);
            d.and("status", "=", true);
            dbManager.delete(ObservingThingsPointSaveTable.class, d);
        } catch (DbException e) {
            e.printStackTrace();
            Log.e("点位删除之前保存失败时选中观测物数据异常",e.toString());
        }
    /*查询选中的观测物,并保存*/
        if (selectccalist != null && selectccalist.size() > 0) {
            ObservingThingsPointSaveTable observingTSdata= null;
            Iterator<Integer> it = selectccalist.iterator();
            while (it.hasNext()) {//判断是否有下一个
                int preIndex = it.next();
                Log.e("it.next()", "" + preIndex);
                //取出已经保存好了的点位里的观测物
                try {
                    WhereBuilder d = WhereBuilder.b();
                    d.and("user_id", "=", user_id);
                    d.and("reseid", "=", Integer.parseInt(reseid));
                    d.and("did", "=", did);
                    d.and("otid", "=",otlist.get(preIndex).getOtid());
                    observingTSdata = dbManager.selector(ObservingThingsPointSaveTable.class).where(d).findFirst();
                    if (observingTSdata==null){
                        observingTSdata=new ObservingThingsPointSaveTable(
                                user_id, Integer.parseInt(reseid),  did, otlist.get(preIndex).getOtid(),
                                otlist.get(preIndex).getPid(), otlist.get(preIndex).getCategory(), otlist.get(preIndex).getName(),
                                otlist.get(preIndex).getDescription(),true, otlist.get(preIndex).getLevel());
                        dbManager.save(observingTSdata);
                    }
                } catch (DbException e) {
                    e.printStackTrace();
                }
            }
        }
        finish();
    }

    CheckingPointContentBean getCheckingPoint;
    /**
     * 获取巡检点内容，只有保存成功过的才可以调用
     */
    private void GetCheckingPointContent() {
        final String user_id = (String) PreferencesUtil.get(this, InterfaceDefinition.PreferencesUser.USER_ID, "");
        String url = InterfaceDefinition.ICheckingPointContent.IURL + PreferencesUtil.get(this, InterfaceDefinition.PreferencesUser.Docking_CREDENTIALS, "");
        LogUtil.e("获取巡检点内容拼接路径", url);
        // 接口对接
        RequestParams mParams = new RequestParams(url);
        mParams.addBodyParameter(InterfaceDefinition.ICheckingPointContent.RESEID,reseid);
        mParams.addBodyParameter(InterfaceDefinition.ICheckingPointContent.ID,""+pass_did);
        x.http().get(mParams, new DefaultCommonCallBack(PatrolSettingActivity.this, true) {
            @Override
            public void onSuccess(String json) {
                LogUtil.e("获取巡检点内容", json);
                getCheckingPoint = new Gson().fromJson(json, CheckingPointContentBean.class);
                if (InterfaceDefinition.IStatusCode.SUCCESS.equals(getCheckingPoint.getStatus())) {
                    //1.判断后台返回的数据为不为空。并且要和之前的数据不同再换数据
                    //点位名称
                    if (StringUtil.isNotEmpty(getCheckingPoint.getResult().getTitle())
                            &&!getCheckingPoint.getResult().getTitle().equals(settingDwmc.getText().toString().trim())){
                        settingDwmc.setText(getCheckingPoint.getResult().getTitle());
                    }
                    //标示卡
                    if (StringUtil.isNotEmpty(getCheckingPoint.getResult().getIdcard())
                            &&!getCheckingPoint.getResult().getIdcard().equals(patrol_bsk.getText().toString().trim())){
                        patrol_bsk.setText(getCheckingPoint.getResult().getIdcard());
                    }
                    //经度
                    if (StringUtil.isNotEmpty(getCheckingPoint.getResult().getXpoint())
                            &&!getCheckingPoint.getResult().getXpoint().equals(setting_jd.getText().toString().trim())){
                        setting_jd.setText(getCheckingPoint.getResult().getXpoint());
                    }
                    //维度
                    if (StringUtil.isNotEmpty(getCheckingPoint.getResult().getYpoint())
                            &&!getCheckingPoint.getResult().getYpoint().equals(setting_wd.getText().toString().trim())){
                        setting_wd.setText(getCheckingPoint.getResult().getYpoint());
                    }
                    //2.判断现在后台给的选中了的观测物本地是不是存在，如果存在就不保存
                    for (int i = 0; i <getCheckingPoint.getResult().getSelected().size() ; i++) {
                        for (int j = 0; j < getCheckingPoint.getResult().getItems().size(); j++) {
                            CheckingPointContentBean.ResultBean.ItemsBean resultbean=getCheckingPoint.getResult().getItems().get(j);
                            if (resultbean.getId()==Integer.parseInt(getCheckingPoint.getResult().getSelected().get(i))){
                                ObservingThingsPointSaveTable observingTSdata=null;
                                try {
                                    WhereBuilder d = WhereBuilder.b();
                                    d.and("user_id", "=", user_id);
                                    d.and("reseid", "=", Integer.parseInt(reseid));
                                    d.and("did", "=", pass_did);//走添加接口保存成功，did就是后台返回的getCheckingPoint.getResult().getId()(String) id。
                                    // 所以用pass_did也是一样的。
                                    d.and("otid", "=",resultbean.getId());
                                    observingTSdata = dbManager.selector(ObservingThingsPointSaveTable.class).where(d).findFirst();
                                    if (observingTSdata==null){
                                        observingTSdata=new ObservingThingsPointSaveTable(
                                                Integer.parseInt(user_id), Integer.parseInt(reseid), pass_did, resultbean.getId(),
                                                resultbean.getPid(), resultbean.getCategory(),resultbean.getName(),
                                                resultbean.getDescription(),true,resultbean.getLevel());
                                        dbManager.save(observingTSdata);
                                    }else{
                                        //防止后台改了名字，然后进行修改
                                        if (!resultbean.getName().equals(observingTSdata.getName())){
                                            observingTSdata.setName(resultbean.getName());
                                            dbManager.update(observingTSdata);
                                        }
                                    }
                                } catch (DbException e) {
                                    e.printStackTrace();
                                }
                                break;
                            }
                        }
                    }
                    //3.再进行数据重新获取之后再取出对应的观测物
                    LocalObservingThings(pass_did); //添加成功的，拿后台给的数据用的是这个点的did
                } else if ("401".equals(getCheckingPoint.getStatus())) {
                    GetToken mGetToken = new GetToken(new IToken() {
                        @Override
                        public void success(int errtype) {
                            if (errtype == 0) {
                                GetCheckingPointContent();
                            }
                        }
                    });
                    mGetToken.getToken();
                } else {
                    ToastUtil.TextToast("" + getCheckingPoint.getMsg());
                    LocalObservingThings(pass_did);
                }
            }
            @Override
            public void onError(Throwable arg0, boolean arg1) {
                Log.e("onError获取巡检点内容",arg0.toString());
                LocalObservingThings(pass_did);
            }
        });
    }
}
