package com.jiarui.znxj.activity;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.jiarui.znxj.R;
import com.jiarui.znxj.base.BaseActivity;
import com.jiarui.znxj.constants.InterfaceDefinition;
import com.jiarui.znxj.utils.PreferencesUtil;

import org.xutils.x;

/**
 * 欢迎页面
 *
 * @author Hui
 * @version 1.0
 * @date 2016年1月20日
 */

public class WelcomeActivity extends BaseActivity {

    Boolean WelcomeState = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_welcome);
        x.view().inject(this);
        WelcomeState = (Boolean) PreferencesUtil.get(this,
                InterfaceDefinition.PreferencesUser.WELCOME_STATE, false);
        handler.sendEmptyMessageDelayed(0, 1000);
        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            finish();
            return;
        }
    }

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        public void dispatchMessage(Message msg) {
            Intent intent = new Intent(WelcomeActivity.this,
                    LoginActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.my_scale_action, R.anim.my_alpha_action);
            finish();
        }
    };
}
