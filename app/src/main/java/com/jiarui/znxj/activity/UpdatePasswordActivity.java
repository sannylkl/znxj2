package com.jiarui.znxj.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.jiarui.znxj.Interface.IToken;
import com.jiarui.znxj.R;
import com.jiarui.znxj.base.BaseActivity;
import com.jiarui.znxj.bean.BaseBean;
import com.jiarui.znxj.bean.MineDataBean;
import com.jiarui.znxj.bean.MineWorkTaskBean;
import com.jiarui.znxj.constants.GetToken;
import com.jiarui.znxj.constants.InterfaceDefinition;
import com.jiarui.znxj.utils.DefaultCommonCallBack;
import com.jiarui.znxj.utils.DefaultDisplayImageOptions;
import com.jiarui.znxj.utils.HttpUtil;
import com.jiarui.znxj.utils.LogUtil;
import com.jiarui.znxj.utils.PreferencesUtil;
import com.jiarui.znxj.utils.StringUtil;
import com.jiarui.znxj.utils.ToastUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.xutils.http.RequestParams;
import org.xutils.x;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 修改密码
 */
public class UpdatePasswordActivity extends BaseActivity implements View.OnClickListener{

    @Bind(R.id.common_title_left)
    TextView commonTitleLeft;//返回
    @Bind(R.id.common_title_tv)
    TextView commonTitleTv;
    @Bind(R.id.common_title_right)
    TextView commonTitleRight;
    @Bind(R.id.common_title_layout)
    RelativeLayout commonTitleLayout;
    @Bind(R.id.changepassword_ymm)
    EditText changepasswordYmm;//原密码
    @Bind(R.id.changepassword_xmm)
    EditText changepasswordXmm;//新密码
    @Bind(R.id.changepassword_qr)
    Button changepasswordQr;//确认

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_password);
        ButterKnife.bind(this);
        commonTitleTv.setText("修改密码");
        commonTitleLeft.setOnClickListener(this);
        changepasswordQr.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.common_title_left:
                //返回
                finish();
                break;
            case R.id.changepassword_qr:
               if (StringUtil.isEmpty(changepasswordYmm.getText().toString().trim())){
                   ToastUtil.TextToast("原密码不能为空");
                   return;
               }
                if (StringUtil.isEmpty(changepasswordXmm.getText().toString().trim())){
                    ToastUtil.TextToast("新密码不能为空");
                    return;
                }
//                if (StringUtil.isEmpty(changepasswordYmm.getText().toString().trim())){
//                    ToastUtil.TextToast("原密码格式错误");
//                    return;
//                }
//                if (StringUtil.isEmpty(changepasswordXmm.getText().toString().trim())){
//                    ToastUtil.TextToast("新密码格式错误");
//                    return;
//                }
                if (changepasswordYmm.getText().toString().trim().equals(changepasswordXmm.getText().toString().trim())){
                    ToastUtil.TextToast("新密码和原密码相同,未做修改");
                    return;
                }
                //修改密码
                UpaloadPW();
                break;
        }
    }
    BaseBean baseBean=null;
    /**
     * 修改密码
     */
    private void UpaloadPW() {
        //获取user_id
        String user_id= (String) PreferencesUtil.get(this, InterfaceDefinition.PreferencesUser.USER_ID, "-1");
        String url = InterfaceDefinition.IUpadatePW.IURL + PreferencesUtil.get(this, InterfaceDefinition.PreferencesUser.Docking_CREDENTIALS, "Docking_CREDENTIALS");
        LogUtil.e("修改密码拼接路径", url);
        // 接口对接
        RequestParams mParams = new RequestParams(url);
        mParams.addBodyParameter(InterfaceDefinition.IUpadatePW.UID,user_id);
        mParams.addBodyParameter(InterfaceDefinition.IUpadatePW.OLDPASSWORD,changepasswordYmm.getText().toString().trim());
        mParams.addBodyParameter(InterfaceDefinition.IUpadatePW.NEWPASSWORD,changepasswordXmm.getText().toString().trim());
        x.http().post(mParams, new DefaultCommonCallBack(UpdatePasswordActivity.this, true) {
            @Override
            public void onSuccess(String json) {
                LogUtil.e("修改密码", json);
                baseBean = new Gson().fromJson(json, BaseBean.class);
                if (InterfaceDefinition.IStatusCode.SUCCESS.equals(baseBean.getStatus())) {
                    PreferencesUtil.put(UpdatePasswordActivity.this, InterfaceDefinition.PreferencesUser.PASSWORD, changepasswordXmm.getText().toString().trim());
                    finish();
                } else if ("401".equals(baseBean.getStatus())) {
                    GetToken mGetToken = new GetToken(new IToken() {
                        @Override
                        public void success(int errtype) {
                            if (errtype == 0) {
                                UpaloadPW();
                            }
                        }
                    });
                    mGetToken.getToken();
                } else {
                    ToastUtil.TextToast("" + baseBean.getMsg());
                }
            }
        });
    }
}
