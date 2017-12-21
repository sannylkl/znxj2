package com.jiarui.znxj.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.jiarui.znxj.Interface.IToken;
import com.jiarui.znxj.R;
import com.jiarui.znxj.base.BaseActivity;
import com.jiarui.znxj.bean.BaseBean;
import com.jiarui.znxj.constants.GetToken;
import com.jiarui.znxj.constants.InterfaceDefinition;
import com.jiarui.znxj.utils.DefaultCommonCallBack;
import com.jiarui.znxj.utils.PreferencesUtil;
import com.jiarui.znxj.utils.StringUtil;
import com.jiarui.znxj.utils.ToastUtil;

import org.xutils.http.RequestParams;
import org.xutils.x;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 修改名字
 */
public class UpdateNameActivity extends BaseActivity {

    @Bind(R.id.common_title_left)
    TextView commonTitleLeft;//返回
    @Bind(R.id.common_title_tv)
    TextView commonTitleTv;//标题
    @Bind(R.id.common_title_right)
    TextView commonTitleRight;
    @Bind(R.id.common_title_layout)
    RelativeLayout commonTitleLayout;
    @Bind(R.id.update_edit)
    EditText updateEdit;//名字
    String real_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_name);
        ButterKnife.bind(this);
        commonTitleTv.setText("修改名字");
        commonTitleRight.setVisibility(View.VISIBLE);
        commonTitleRight.setText("保存");
        real_name = (String) PreferencesUtil.get(UpdateNameActivity.this, InterfaceDefinition.PreferencesUser.REAL_NAME, "");
        updateEdit.setText(real_name);
        if (StringUtil.isNotEmpty(real_name)) {
            updateEdit.setSelection(real_name.length());
        }
    }


    BaseBean baseBean = null;

    /**
     * 更新个人资料
     */
    private void IUpadateInfo() {
        String url = InterfaceDefinition.IUpadateInfo.IURL + PreferencesUtil.get(this, InterfaceDefinition.PreferencesUser.Docking_CREDENTIALS, "Docking_CREDENTIALS");
        Log.e("更新个人姓名url", url + "");
        //接口对接
        RequestParams params = new RequestParams(url);
        params.addBodyParameter(InterfaceDefinition.IUpadateInfo.ID, PreferencesUtil.get(this, InterfaceDefinition.PreferencesUser.USER_ID, "") + "");
        params.addBodyParameter(InterfaceDefinition.IUpadateInfo.REALNAME, commonTitleRight.getText().toString().trim());
        x.http().post(params, new DefaultCommonCallBack(this, true) {
            public void onSuccess(String arg0) {
                Log.e("更新个人姓名", arg0);
                baseBean = new Gson().fromJson(arg0, BaseBean.class);
                if (InterfaceDefinition.IStatusCode.SUCCESS.equals(baseBean.getStatus())) {
                    PreferencesUtil.put(UpdateNameActivity.this, InterfaceDefinition.PreferencesUser.REAL_NAME, commonTitleRight.getText().toString().trim());
                    finish();
                } else if ("401".equals(baseBean.getStatus())) {
                    GetToken mGetToken = new GetToken(new IToken() {
                        @Override
                        public void success(int errtype) {
                            if (errtype == 0) {
                                IUpadateInfo();
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

    @OnClick({R.id.common_title_left, R.id.common_title_right})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.common_title_left:
                finish();
                break;
            case R.id.common_title_right:
                if (StringUtil.isEmpty(commonTitleRight.getText().toString().trim())) {
                    ToastUtil.TextToast("请输入您的姓名");
                    return;
                }
                if (real_name.equals(commonTitleRight.getText().toString().trim())) {
                    ToastUtil.TextToast("请输入新的姓名");
                    return;
                }
                IUpadateInfo();
                break;
        }
    }
}
