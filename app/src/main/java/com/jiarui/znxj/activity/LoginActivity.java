package com.jiarui.znxj.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.google.gson.Gson;
import com.jiarui.znxj.Interface.IToken;
import com.jiarui.znxj.R;
import com.jiarui.znxj.adapter.CommonAdapter;
import com.jiarui.znxj.base.BaseActivity;
import com.jiarui.znxj.bean.BaseBean;
import com.jiarui.znxj.bean.CityBean;
import com.jiarui.znxj.bean.LoginBean;
import com.jiarui.znxj.constants.GetToken;
import com.jiarui.znxj.constants.InterfaceDefinition;
import com.jiarui.znxj.holder.ViewHolder;
import com.jiarui.znxj.utils.DefaultCommonCallBack;
import com.jiarui.znxj.utils.LogUtil;
import com.jiarui.znxj.utils.PreferencesUtil;
import com.jiarui.znxj.utils.ServiceCallback;
import com.jiarui.znxj.utils.ServiceUtil;
import com.jiarui.znxj.utils.StringUtil;
import com.jiarui.znxj.utils.ToastUtil;

import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

import static com.jiarui.znxj.R.id.login_password;
import static com.jiarui.znxj.R.id.login_username;

/**
 * 登录
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener, IToken {

    @Bind(login_username)
    EditText loginUsername;//用户名
    @Bind(login_password)
    EditText loginPassword;//密码
    @Bind(R.id.login_xz)
    LinearLayout loginXz;//选择水库
    @Bind(R.id.login_sxk)
    TextView loginSxk;//地市
    @Bind(R.id.login_jzmm)
    CheckBox loginJzmm;//记住密码
    @Bind(R.id.login_szym)
    TextView login_szym;//设置域名
    @Bind(R.id.login_dl)
    Button loginDl;//登录
    @Bind(R.id.login_line)
    LinearLayout line;//点击
    Dialog dialog;
    List<LoginBean> loginBeanlist = new ArrayList<>();
    CommonAdapter<LoginBean> commonAdapter;
    int index = 0;
    //    数据列表里被选中的城市位置，被选中的区县位置，被选中的水库位置
    int cposition, qposition, sposition = -1;
    @Bind(R.id.login_xian)
    TextView loginXian;//县
    @Bind(R.id.login_sk)
    TextView loginSk;//水库
    String city2;
    String county;
    String reservoir;
    /*后台返回数据*/
    CityBean cityBean;
    String ReservoId = null;
    public LocationClient mLocationClient = null;
    public BDAbstractLocationListener myListener = new MyLocationListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        login_szym.setText("网络设置");
        line.setOnClickListener(this);
        loginDl.setOnClickListener(this);
        login_szym.setOnClickListener(this);
        if (!ServiceUtil.isServiceRunning(LoginActivity.this, ServiceCallback.class.getName())) {
            Log.e("isServiceRunning", "" + ServiceUtil.isServiceRunning(LoginActivity.this, ServiceCallback.class.getName()));
            //定位
            mLocationClient = new LocationClient(LoginActivity.this);
            //声明LocationClient类
            mLocationClient.registerLocationListener(myListener);
            //注册监听函数
            initLocation();
            mLocationClient.start();
            Intent intent = new Intent();
            // 设置Class属性
            intent.setClass(LoginActivity.this, ServiceCallback.class);
            // 启动该Service
            startService(intent);
//            Context.startService() ;
//      • Context.bindService()
        } else {
            Log.e("isServiceRunning", "" + ServiceUtil.isServiceRunning(LoginActivity.this, ServiceCallback.class.getName()));
        }
        initData();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login_line:
                if (StringUtil.isEmpty(loginUsername.getText().toString().trim())) {
                    Toast.makeText(this, "用户名不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (StringUtil.isEmpty(loginPassword.getText().toString().trim())) {
                    Toast.makeText(this, "密码不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                LoginCity();
                break;
            case R.id.login_dl:
                Log.e("loginUsername", loginUsername.getText().toString().trim() + "loginpass" + loginPassword.getText().toString().trim());
                Log.e("name", name + "pass" + pass);
                if (CheckData() == false) {
                    return;
                } else {
                    //是否记住密码
                    if (loginJzmm.isChecked()) {
                        PreferencesUtil.put(LoginActivity.this, InterfaceDefinition.PreferencesUser.REMEMBER, true);
                    } else {
                        PreferencesUtil.put(LoginActivity.this, InterfaceDefinition.PreferencesUser.REMEMBER, false);
                    }
                    PreferencesUtil.put(LoginActivity.this, InterfaceDefinition.PreferencesUser.LOGIN_STATE, true);
                    PreferencesUtil.put(LoginActivity.this, InterfaceDefinition.PreferencesUser.City, loginSxk.getText().toString());
                    PreferencesUtil.put(LoginActivity.this, InterfaceDefinition.PreferencesUser.County, loginXian.getText().toString());
                    PreferencesUtil.put(LoginActivity.this, InterfaceDefinition.PreferencesUser.Reservoir, loginSk.getText().toString());
                    //                    //跳转
                    gotoActivity(MainActivity.class);
                }
                break;
            case R.id.login_szym:
                //设置域名
                gotoActivity(NetSetActivity.class);
                break;
        }
    }

    String name;
    String pass;

    private void initData() {
        name = (String) PreferencesUtil.get(LoginActivity.this, InterfaceDefinition.PreferencesUser.USER_NAME, "");
        pass = (String) PreferencesUtil.get(LoginActivity.this, InterfaceDefinition.PreferencesUser.PASSWORD, "");
        city2 = (String) PreferencesUtil.get(LoginActivity.this, InterfaceDefinition.PreferencesUser.City, "");
        county = (String) PreferencesUtil.get(LoginActivity.this, InterfaceDefinition.PreferencesUser.County, "");
        reservoir = (String) PreferencesUtil.get(LoginActivity.this, InterfaceDefinition.PreferencesUser.Reservoir, "");
        ReservoId = (String) PreferencesUtil.get(LoginActivity.this, InterfaceDefinition.PreferencesUser.ReservoId, "-1");
        loginSxk.setText(city2);
        loginXian.setText(county);
        loginSk.setText(reservoir);
        boolean choseRemember = (boolean) PreferencesUtil.get(LoginActivity.this, InterfaceDefinition.PreferencesUser.REMEMBER, false);
        //如果上次选了记住密码，那进入登录页面也自动勾选记住密码，并填上用户名和密码
        if (choseRemember) {
            loginUsername.setText(name);
            loginPassword.setText(pass);
            loginJzmm.setChecked(true);
        } else {
            loginUsername.setText(name);
        }
        //第一次打开app里面是没有数据的。空的
        loginUsername.setSelection(name.length());

    }

    private boolean CheckData() {
        if (StringUtil.isEmpty(loginUsername.getText().toString().trim())) {
            ToastUtil.TextToast("用户名不能为空");
            return false;
        } else if (StringUtil.isEmpty(loginPassword.getText().toString().trim())) {
            ToastUtil.TextToast("密码不能为空");
            return false;
        } else if (StringUtil.isEmpty(loginSxk.getText().toString().trim())) {
            ToastUtil.TextToast("城市不能为空");
            return false;
        } else if (StringUtil.isEmpty(loginXian.getText().toString().trim())) {
            ToastUtil.TextToast("区县不能为空");
            return false;
        } else if (StringUtil.isEmpty(loginSk.getText().toString().trim())) {
            ToastUtil.TextToast("水库不能为空");
            return false;
        } else if (!loginUsername.getText().toString().trim().equals(name) || !loginPassword.getText().toString().trim().equals(pass)) {
            ToastUtil.TextToast("已重新输入账号或密码，请重新选择水库");
            return false;
        } else {
            return true;
        }
    }

    private void popuMethod() {
        cposition = -1;
        qposition = -1;
        sposition = -1;
        index = 0;
        dialog = new Dialog(LoginActivity.this, R.style.Theme_Light_Dialog);
        View dialogView = LayoutInflater.from(LoginActivity.this).inflate(R.layout.login_dialog, null);
        TextView dialog_qd = (TextView) dialogView.findViewById(R.id.dialog_qd);
        final TextView dialog_cs = (TextView) dialogView.findViewById(R.id.dialog_cs);
        final TextView dialog_qx = (TextView) dialogView.findViewById(R.id.dialog_qx);
        final TextView dialog_sk = (TextView) dialogView.findViewById(R.id.dialog_sk);
        final ListView dialog_list1 = (ListView) dialogView.findViewById(R.id.dialog_list1);
        final TextView dialog_view1 = (TextView) dialogView.findViewById(R.id.dialog_view1);
        final TextView dialog_view2 = (TextView) dialogView.findViewById(R.id.dialog_view2);
        final TextView dialog_view3 = (TextView) dialogView.findViewById(R.id.dialog_view3);
        if (StringUtil.isEmpty(loginSxk.getText().toString()) && StringUtil.isEmpty(loginXian.getText().toString()) && StringUtil.isEmpty(loginSk.getText().toString())) {
            dialog_cs.setText("城市");
            dialog_qx.setText("区县");
            dialog_sk.setText("水库");
        } else {
            dialog_cs.setText(loginSxk.getText().toString());
            dialog_qx.setText(loginXian.getText().toString());
            dialog_sk.setText(loginSk.getText().toString());
        }

        commonAdapter = new CommonAdapter<LoginBean>(LoginActivity.this, loginBeanlist, R.layout.logdialog_item) {
            @Override
            public void convert(ViewHolder mHolder, LoginBean item, int position) {
                TextView logdialog_name = mHolder.getView(R.id.logdialog_name);
                logdialog_name.setText(item.getName());
                if (index == 0) {
                    //为了防止已经选择好了城市有城市数据的情况下
                    int spec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
                    dialog_cs.measure(spec, spec);
                    int width = dialog_cs.getMeasuredWidth();
                    ViewGroup.LayoutParams lp = dialog_view1.getLayoutParams();
                    lp.width = width;
                    dialog_view1.setLayoutParams(lp);
                    //全国大陆里是没有相同的城市名称，所以不用加个id去重判断
                    if (item.getName().equals(dialog_cs.getText().toString().trim())) {
                        logdialog_name.setTextColor(ContextCompat.getColor(LoginActivity.this, R.color.them_color));
                    } else {
                        logdialog_name.setTextColor(ContextCompat.getColor(LoginActivity.this, R.color.bar_grey));
                    }
                } else if (index == 1) {
                    int spec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
                    dialog_qx.measure(spec, spec);
                    int width = dialog_qx.getMeasuredWidth();
                    ViewGroup.LayoutParams lp = dialog_view2.getLayoutParams();
                    lp.width = width;
                    dialog_view2.setLayoutParams(lp);
                    //一个城市里不肯能出现同名区，所以不用判断id
                    if (item.getName().equals(dialog_qx.getText().toString().trim())) {
                        logdialog_name.setTextColor(ContextCompat.getColor(LoginActivity.this, R.color.them_color));
                    } else {
                        logdialog_name.setTextColor(ContextCompat.getColor(LoginActivity.this, R.color.bar_grey));
                    }
//                    if (index == 2)
                } else {
                    int spec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
                    dialog_sk.measure(spec, spec);
                    int width = dialog_sk.getMeasuredWidth();
                    ViewGroup.LayoutParams lp = dialog_view3.getLayoutParams();
                    lp.width = width;
                    dialog_view3.setLayoutParams(lp);
                    //一个区里可能出现相同的水库名称所以要加个水库i，来判断是不是选中的那个水库
                    if (item.getName().equals(dialog_sk.getText().toString().trim()) && item.getId().equals(ReservoId)) {
                        logdialog_name.setTextColor(ContextCompat.getColor(LoginActivity.this, R.color.them_color));
                    } else {
                        logdialog_name.setTextColor(ContextCompat.getColor(LoginActivity.this, R.color.bar_grey));
                    }
                }
            }
        };

        loginBeanlist.clear();
        for (int i = 0; i < cityBean.getResult().getTask().size(); i++) {
            //进来时不用触发itme点击，获取到选中项的城市的位置，下面好获取对应的区域
            if (cityBean.getResult().getTask().get(i).getCity_name().equals(dialog_cs.getText().toString().trim())) {
                cposition = i;
            }
            LoginBean loginBean = new LoginBean(cityBean.getResult().getTask().get(i).getCity_name(), cityBean.getResult().getTask().get(i).getCity_id());
            loginBeanlist.add(loginBean);
        }
        dialog_list1.setAdapter(commonAdapter);

        dialog_list1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (index == 0) {
                    cposition = i;
                    dialog_cs.setText(loginBeanlist.get(i).getName());
                    int spec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
                    dialog_cs.measure(spec, spec);
                    int width = dialog_cs.getMeasuredWidth();
                    ViewGroup.LayoutParams lp = dialog_view1.getLayoutParams();
                    lp.width = width;
                    dialog_view1.setLayoutParams(lp);
                    loginBeanlist.clear();
                    for (int j = 0; j < cityBean.getResult().getTask().get(i).getAreas().size(); j++) {
                        LoginBean loginBeanQ = new LoginBean(cityBean.getResult().getTask().get(i).getAreas().get(j).getArea_name(), cityBean.getResult().getTask().get(i).getAreas().get(j).getArea_id());
                        loginBeanlist.add(loginBeanQ);
                    }
                    index = 1;
                    dialog_view2.setVisibility(View.VISIBLE);
                    dialog_view1.setVisibility(View.GONE);
                    dialog_view3.setVisibility(View.GONE);
                    commonAdapter.notifyDataSetChanged();
                } else if (index == 1) {
                    qposition = i;
                    dialog_qx.setText(loginBeanlist.get(i).getName());
                    int spec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
                    dialog_qx.measure(spec, spec);
                    int width = dialog_qx.getMeasuredWidth();
                    ViewGroup.LayoutParams lp = dialog_view2.getLayoutParams();
                    lp.width = width;
                    dialog_view2.setLayoutParams(lp);
                    loginBeanlist.clear();
                    for (int j = 0; j < cityBean.getResult().getTask().get(cposition).getAreas().get(i).getReservoirs().size(); j++) {
                        LoginBean loginBeanS = new LoginBean(cityBean.getResult().getTask().get(cposition).getAreas().get(i).getReservoirs().get(j).getName(), cityBean.getResult().getTask().get(cposition).getAreas().get(i).getReservoirs().get(j).getId());
                        loginBeanlist.add(loginBeanS);
                    }
                    index = 2;
                    dialog_view2.setVisibility(View.GONE);
                    dialog_view1.setVisibility(View.GONE);
                    dialog_view3.setVisibility(View.VISIBLE);
                    commonAdapter.notifyDataSetChanged();
                } else if (index == 2) {
                    sposition = i;
                    dialog_sk.setText(loginBeanlist.get(i).getName());
                    PreferencesUtil.put(LoginActivity.this, InterfaceDefinition.PreferencesUser.ReservoId, loginBeanlist.get(i).getId());
                    ReservoId = (String) PreferencesUtil.get(LoginActivity.this, InterfaceDefinition.PreferencesUser.ReservoId, "-1");
                    int spec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
                    dialog_sk.measure(spec, spec);
                    int width = dialog_sk.getMeasuredWidth();
                    ViewGroup.LayoutParams lp = dialog_view3.getLayoutParams();
                    lp.width = width;
                    dialog_view3.setLayoutParams(lp);
                    commonAdapter.notifyDataSetChanged();
                }
            }
        });
        dialog_cs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                index = 0;
                loginBeanlist.clear();
                for (int i = 0; i < cityBean.getResult().getTask().size(); i++) {
                    LoginBean loginBean = new LoginBean(cityBean.getResult().getTask().get(i).getCity_name(), cityBean.getResult().getTask().get(i).getCity_id());
                    loginBeanlist.add(loginBean);
                }
                dialog_view2.setVisibility(View.GONE);
                dialog_view1.setVisibility(View.VISIBLE);
                dialog_view3.setVisibility(View.GONE);
                commonAdapter.notifyDataSetChanged();
            }
        });
        dialog_qx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //获取到的数据里没有符合选中了的城市数据的位置
                if (cposition == -1) {
                    ToastUtil.TextToast("请先选择对应的城市");
                    return;
                }
                //上面获取的数据里有符合选中了城市的的位置
                index = 1;
                loginBeanlist.clear();
                for (int j = 0; j < cityBean.getResult().getTask().get(cposition).getAreas().size(); j++) {
                    //点击区县按钮，不用重新触发itme点击，获取到选中项的区县位置，下面好获取对应的水库
                    if (cityBean.getResult().getTask().get(cposition).getAreas().get(j).getArea_name().equals(loginXian.getText().toString())) {
                        qposition = j;
                    }
                    LoginBean loginBeanQ = new LoginBean(cityBean.getResult().getTask().get(cposition).getAreas().get(j).getArea_name(), cityBean.getResult().getTask().get(cposition).getAreas().get(j).getArea_id());
                    loginBeanlist.add(loginBeanQ);
                }
                dialog_view2.setVisibility(View.VISIBLE);
                dialog_view1.setVisibility(View.GONE);
                dialog_view3.setVisibility(View.GONE);
                commonAdapter.notifyDataSetChanged();
            }
        });
        dialog_sk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //直接点击水库，没有获取过区县的数据，这里就要重新获取一下之前选中的区县，城市已经在一进入已经获取了。
                if (cposition != -1 && qposition == -1) {
                    for (int j = 0; j < cityBean.getResult().getTask().get(cposition).getAreas().size(); j++) {
                        //点击区县按钮，不用重新触发itme点击，获取到选中项的区县位置，下面好获取对应的水库
                        if (cityBean.getResult().getTask().get(cposition).getAreas().get(j).getArea_name().equals(loginXian.getText().toString())) {
                            qposition = j;
                            break;
                        }
                    }
                }
                //获取到的数据里没有符合选中了的城市数据的位置+选中了的区县数据的位置
                if (cposition == -1 || qposition == -1) {
                    ToastUtil.TextToast("请先选择对应的城市和区县");
                    return;
                }
                //上面获取的数据里有符合选中了城市的的位置+区县的位置
                index = 2;
                loginBeanlist.clear();
                for (int j = 0; j < cityBean.getResult().getTask().get(cposition).getAreas().get(qposition).getReservoirs().size(); j++) {
                    LoginBean loginBeanS = new LoginBean(cityBean.getResult().getTask().get(cposition).
                            getAreas().
                            get(qposition).getReservoirs().get(j).getName(), cityBean.getResult().getTask().get(cposition).getAreas().get(qposition).getReservoirs().get(j).getId());
                    loginBeanlist.add(loginBeanS);
                }
                dialog_view2.setVisibility(View.GONE);
                dialog_view1.setVisibility(View.GONE);
                dialog_view3.setVisibility(View.VISIBLE);
                commonAdapter.notifyDataSetChanged();
            }
        });
        dialog_qd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //判断是不是选择了水库
                if (dialog_cs.getText().toString().equals("城市") || dialog_qx.equals("区县") || dialog_sk.getText().toString().equals("水库")) {
                    ToastUtil.TextToast("请选择对应的城市或者区县和水库");
                    return;
                }
                //判断水库有没有重新选择
                if (cposition == -1 || qposition == -1 || sposition == -1) {
                    ToastUtil.TextToast("请重新选择对应的城市区域和水库");
                    return;
                }
                //存数据
                PreferencesUtil.put(LoginActivity.this, InterfaceDefinition.PreferencesUser.USER_NAME, loginUsername.getText().toString().trim());
                PreferencesUtil.put(LoginActivity.this, InterfaceDefinition.PreferencesUser.REAL_NAME, cityBean.getResult().getUser().getRealname());
                PreferencesUtil.put(LoginActivity.this, InterfaceDefinition.PreferencesUser.PASSWORD, loginPassword.getText().toString().trim());
                PreferencesUtil.put(LoginActivity.this, InterfaceDefinition.PreferencesUser.USER_ID, cityBean.getResult().getUser().getUid());
                PreferencesUtil.put(LoginActivity.this, InterfaceDefinition.PreferencesUser.GROUP_ID, cityBean.getResult().getUser().getGroup_id());
                //重新赋值账号和密码数据数据
                name = (String) PreferencesUtil.get(LoginActivity.this, InterfaceDefinition.PreferencesUser.USER_NAME, "");
                pass = (String) PreferencesUtil.get(LoginActivity.this, InterfaceDefinition.PreferencesUser.PASSWORD, "");
                loginSxk.setText(dialog_cs.getText().toString());
                loginXian.setText(dialog_qx.getText().toString());
                loginSk.setText(dialog_sk.getText().toString());
                dialog.dismiss();
            }
        });
        //获得dialog的window窗口
        Window window = dialog.getWindow();
        //设置dialog在屏幕底部
        window.setGravity(Gravity.BOTTOM);
        //设置dialog弹出时的动画效果，从屏幕底部向上弹出
        window.setWindowAnimations(R.style.dialogStyle);
        window.getDecorView().setPadding(0, 0, 0, 0);
        //获得window窗口的属性
        WindowManager.LayoutParams lp = window.getAttributes();
        //设置窗口宽度为充满全屏
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        //设置窗口高度为包裹内容
        lp.height = 700;
        //将设置好的属性set回去
        window.setAttributes(lp);
        //设置此参数获得焦点,否则无法点击
        dialog.setCanceledOnTouchOutside(true);
        //将自定义布局加载到dialog上
        dialog.setContentView(dialogView);
        dialog.show();

    }

    /**
     * 登录接口
     */
    private void LoginCity() {
        String url = InterfaceDefinition.ITaskLogin.IURL + PreferencesUtil.get(this, InterfaceDefinition.PreferencesUser.Docking_CREDENTIALS, "Docking_CREDENTIALS");
        LogUtil.e("登录接口拼接路径", url);
        // 接口对接
        RequestParams mParams = new RequestParams(url);
        mParams.addBodyParameter(InterfaceDefinition.ITaskLogin.USERNAME, loginUsername.getText().toString().trim());
        mParams.addBodyParameter(InterfaceDefinition.ITaskLogin.PASSWORD, loginPassword.getText().toString().trim());
        x.http().post(mParams, new DefaultCommonCallBack(LoginActivity.this, true) {
            @Override
            public void onSuccess(String json) {
                LogUtil.e("登录接口", json);
                cityBean = new Gson().fromJson(json, CityBean.class);
                if (InterfaceDefinition.IStatusCode.SUCCESS.equals(cityBean.getStatus())) {
                    popuMethod();
                } else if ("401".equals(cityBean.getStatus())) {
                    GetToken mGetToken = new GetToken(LoginActivity.this);
                    mGetToken.getToken();
                } else {
                    ToastUtil.TextToast("" + cityBean.getMsg());
                }
            }
        });
    }


    @Override
    public void success(int errtype) {
        if (errtype == 0) {
            LoginCity();
        }
    }

    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Device_Sensors);
        //可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
//        int span = 1000;
        option.setScanSpan(5000);//5*60*1000可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
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
            LogUtil.e("getLatitude", "" + location.getLatitude() + "\ngetLongitude:" + location.getLongitude());
//            //获取定位结果
//            setting_wd.setText(String.valueOf(location.getLatitude()));
//            setting_jd.setText(String.valueOf(location.getLongitude()));
        }
    }
}
