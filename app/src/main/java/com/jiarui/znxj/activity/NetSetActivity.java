package com.jiarui.znxj.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.jiarui.znxj.R;
import com.jiarui.znxj.base.BaseActivity;
import com.jiarui.znxj.utils.PreferencesUtil;
import com.jiarui.znxj.utils.StringUtil;
import com.jiarui.znxj.utils.ToastUtil;

import org.xutils.view.annotation.ViewInject;

/**
 * 网络设置
 */
public class NetSetActivity extends BaseActivity implements View.OnClickListener{
    @ViewInject(R.id.net_dlip)
    private EditText net_dlip;//输入域ip

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_net_set);
        setTitle();
        mTvForTitle.setText("网络设置");
        mImgvForRight.setText("保存");
        mImgvForLeft.setOnClickListener(this);
        mImgvForRight.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.common_title_left:
                //返回
                finish();
                break;
            case R.id.common_title_right://保存
                if (StringUtil.isEmpty(net_dlip.getText().toString().trim())){
                    ToastUtil.TextToast("登录IP不能为空");
                    return;
                }
                PreferencesUtil.put(NetSetActivity.this,"BASE_IP_URL",net_dlip.getText().toString().trim());
                finish();
                break;
        }
    }
}
