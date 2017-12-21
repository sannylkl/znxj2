package com.jiarui.znxj.activity;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.jiarui.znxj.Interface.IToken;
import com.jiarui.znxj.R;
import com.jiarui.znxj.adapter.CommonAdapter;
import com.jiarui.znxj.application.AppContext;
import com.jiarui.znxj.base.BaseActivity;
import com.jiarui.znxj.bean.CityBean;
import com.jiarui.znxj.bean.MineDataBean;
import com.jiarui.znxj.bean.MineWorkTaskBean;
import com.jiarui.znxj.bean.PersonalCenterBean;
import com.jiarui.znxj.constants.GetToken;
import com.jiarui.znxj.constants.InterfaceDefinition;
import com.jiarui.znxj.grobal.AppManager;
import com.jiarui.znxj.holder.ViewHolder;
import com.jiarui.znxj.utils.CommonDialog;
import com.jiarui.znxj.utils.DefaultCommonCallBack;
import com.jiarui.znxj.utils.DefaultDisplayImageOptions;
import com.jiarui.znxj.utils.HttpUtil;
import com.jiarui.znxj.utils.LogUtil;
import com.jiarui.znxj.utils.PreferencesUtil;
import com.jiarui.znxj.utils.StringUtil;
import com.jiarui.znxj.utils.ToastUtil;
import com.jiarui.znxj.widget.CircleImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.xutils.DbManager;
import org.xutils.db.sqlite.WhereBuilder;
import org.xutils.ex.DbException;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

import static com.jiarui.znxj.R.id.centen_txl;

/**
 * 个人中心
 */
public class PersonalCenterActivity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.common_title_left)
    TextView commonTitleLeft;//返回
    @Bind(R.id.common_title_right)
    ImageButton commonTitleRight;//标题栏右边按钮
    @Bind(R.id.centen_tx)
    CircleImageView centenTx;//头像
    @Bind(R.id.centen_name)
    TextView centenName;//名字
    @Bind(R.id.centen_gzxl)
    TextView centenGzxl;//工作效率
    @Bind(R.id.centen_grid)
    GridView centenGrid;
    @Bind(centen_txl)
    RelativeLayout centenTxl;//通讯录
    @Bind(R.id.centen_xgmm)
    RelativeLayout centenXgmm;//修改密码
    @Bind(R.id.centen_bbgx)
    TextView centenBbgx;//版本更新
    @Bind(R.id.my_jfsc)
    RelativeLayout myJfsc;
    @Bind(R.id.centen_gywm)
    RelativeLayout centenGywm;//关于我们
    @Bind(R.id.centen_tcdl)
    Button centenTcdl;//退出登录
    private List<MineWorkTaskBean> pelist = new ArrayList<>();
    CommonAdapter<MineWorkTaskBean> commonAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_center);
        ButterKnife.bind(this);
        initAdapter();
        initData();
        centenTxl.setOnClickListener(this);
        centenXgmm.setOnClickListener(this);
        centenTx.setOnClickListener(this);
        commonTitleLeft.setOnClickListener(this);
        centenTcdl.setOnClickListener(this);
        commonTitleRight.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.centen_txl:
                //通讯录
                gotoActivity(AddressListActivity.class);
                break;
            case R.id.centen_xgmm:
                //修改密码
                gotoActivity(UpdatePasswordActivity.class);
                break;
            case R.id.centen_tx:
                //资料修改
                gotoActivity(DataModificationActivity.class);
                break;
            case R.id.common_title_left:
                //返回
                finish();
                break;
            case R.id.centen_tcdl:
                //退出登录
                initDialog();
                break;
            case R.id.common_title_right:
                //资料修改
                gotoActivity(DataModificationActivity.class);
                break;
        }
    }

    private void initAdapter() {
        commonAdapter = new CommonAdapter<MineWorkTaskBean>(PersonalCenterActivity.this, pelist, R.layout.personal_center_item) {
            @Override
            public void convert(ViewHolder mHolder, MineWorkTaskBean item, int position) {
                mHolder.setText(R.id.centeniten_name, item.getName());
                mHolder.setText(R.id.centeniten_number, item.getNumber());
            }
        };
        centenGrid.setAdapter(commonAdapter);
    }

    MineDataBean mineDataBean=null;
    String name[] = new String[]{"任务总数", "完成任务", "未完成任务"};
    String number[] = new String[name.length];
    /**
     * 初始化数据
     */
    private void initData() {
        final DisplayImageOptions mOptions = DefaultDisplayImageOptions
                .getDefaultDisplayImageOptionsRounded(PersonalCenterActivity.this, 360);
        //获取user_id
        String user_id= (String) PreferencesUtil.get(this, InterfaceDefinition.PreferencesUser.USER_ID, "-1");
        String url = InterfaceDefinition.IGetUser.IURL + PreferencesUtil.get(this, InterfaceDefinition.PreferencesUser.Docking_CREDENTIALS, "Docking_CREDENTIALS");
        LogUtil.e("获取个人资料拼接路径", url);
        // 接口对接
        RequestParams mParams = new RequestParams(url);
        mParams.addBodyParameter(InterfaceDefinition.IGetUser.UID,user_id);
        x.http().get(mParams, new DefaultCommonCallBack(PersonalCenterActivity.this, true) {
            @Override
            public void onSuccess(String json) {
                LogUtil.e("获取个人资料", json);
                mineDataBean = new Gson().fromJson(json, MineDataBean.class);
                if (InterfaceDefinition.IStatusCode.SUCCESS.equals(mineDataBean.getStatus())) {
                    //头像
                    if (StringUtil.isNotEmpty(mineDataBean.getResult().getAvatar())) {
                        PreferencesUtil.put(PersonalCenterActivity.this, InterfaceDefinition.PreferencesUser.USER_HEAD, HttpUtil.LOAD_URL(PersonalCenterActivity.this)+mineDataBean.getResult().getAvatar());
                        ImageLoader.getInstance().displayImage(
                                HttpUtil.LOAD_URL(PersonalCenterActivity.this)+mineDataBean.getResult().getAvatar(),
                                centenTx, mOptions);
                    }
                    //昵称
                    if (StringUtil.isNotEmpty(mineDataBean.getResult().getRealname())) {
                        PreferencesUtil.put(PersonalCenterActivity.this, InterfaceDefinition.PreferencesUser.REAL_NAME, mineDataBean.getResult().getRealname());
                        centenName.setText(mineDataBean.getResult().getRealname());
                    }
                    //工作效率
                    int proportion=(Integer.parseInt(mineDataBean.getResult().getDone())
                            /Integer.parseInt(mineDataBean.getResult().getTotal()))*100;
                    Log.e("proportion",""+proportion);
                    if (proportion<60){//不及格
                        centenGzxl.setText("不及格");
                    }else if (proportion==60&&proportion<75){//及格
                        centenGzxl.setText("及格");
                    }else if (proportion==75&&proportion<85){//良好
                        centenGzxl.setText("良好");
                    }else if (proportion==85&&proportion<100){//优秀
                        centenGzxl.setText("优秀");
                    }
                    // 任务情况
                    number[0]=mineDataBean.getResult().getTotal();
                    number[1]=mineDataBean.getResult().getDone();
                    number[2]=mineDataBean.getResult().getUndo();
                    pelist.clear();
                    for (int i = 0; i <name.length ; i++) {
                        MineWorkTaskBean minedata=new MineWorkTaskBean(name[i], number[i]);
                        pelist.add(minedata);
                    }
                    commonAdapter.notifyDataSetChanged();

                } else if ("401".equals(mineDataBean.getStatus())) {
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
                    ToastUtil.TextToast("" + mineDataBean.getMsg());
                }
            }
        });
    }


    /**
     * 退出登录
     */
    private void initDialog() {
        CommonDialog mDialog = new CommonDialog(PersonalCenterActivity.this, R.style.dialog);
        mDialog.setIcon(R.mipmap.ic_launcher);
        mDialog.setContent("您是否要退出当前账号？");
        mDialog.setLeftBtnText("确定");
        mDialog.setRightBtnText("取消");
        mDialog.setListener(new CommonDialog.DialogClickListener() {
            @Override
            public void onRightBtnClick(Dialog dialog) {
                dialog.dismiss();
            }

            @Override
            public void onLeftBtnClick(Dialog dialog) {
                PreferencesUtil.put(PersonalCenterActivity.this, InterfaceDefinition.PreferencesUser.LOGIN_STATE, false);
                PreferencesUtil.remove(PersonalCenterActivity.this, InterfaceDefinition.PreferencesUser.USER_ID);//用户账号
                AppManager.getAppManager().AppExit(PersonalCenterActivity.this);
                gotoActivity(LoginActivity.class);
            }
        });
        mDialog.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
       String header_photo= (String) PreferencesUtil.get(PersonalCenterActivity.this, InterfaceDefinition.PreferencesUser.USER_HEAD, "");
        String real_name= (String) PreferencesUtil.get(PersonalCenterActivity.this, InterfaceDefinition.PreferencesUser.REAL_NAME, "");
        if (StringUtil.isNotEmpty(header_photo)){
            DisplayImageOptions mOptions = DefaultDisplayImageOptions
                    .getDefaultDisplayImageOptionsRounded(PersonalCenterActivity.this, 360);
            ImageLoader.getInstance().displayImage(header_photo, centenTx, mOptions);
        }
        if (StringUtil.isNotEmpty(real_name)){
            centenName.setText(real_name);
        }
    }
}
