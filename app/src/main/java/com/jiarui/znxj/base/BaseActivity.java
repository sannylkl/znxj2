package com.jiarui.znxj.base;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.jiarui.znxj.R;
import com.jiarui.znxj.constants.InterfaceDefinition;
import com.jiarui.znxj.grobal.AppManager;
import com.jiarui.znxj.utils.PreferencesUtil;


/**
 * Activity基类
 *
 * @author Only You
 * @version 1.0
 * @date 2016年1月10日
 */
public class BaseActivity extends FragmentActivity {
    View mTitleView = null;

    protected String UserId = "";

    protected TextView mImgvForLeft = null;

    protected TextView mTvForTitle = null;

    protected TextView mImgvForRight = null;
    private HomeWatcherReceiver mHomeWatcherReceiver = null;
    private boolean isNeedFinish = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉标题栏
        // 添加Activity到堆栈
        AppManager.getAppManager().addActivity(this);
        UserId = PreferencesUtil.get(this, InterfaceDefinition.PreferencesUser.USER_ID, "")
                .toString();
        registerReceiver();

    }

    public void setBackFinish(boolean flag) {
        isNeedFinish = flag;
    }

    private void registerReceiver() {
        mHomeWatcherReceiver = new HomeWatcherReceiver();
        IntentFilter filter = new IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        registerReceiver(mHomeWatcherReceiver, filter);
    }

    public class HomeWatcherReceiver extends BroadcastReceiver {

        private static final String SYSTEM_DIALOG_REASON_KEY = "reason";
        private static final String SYSTEM_DIALOG_REASON_HOME_KEY = "homekey";

        @Override
        public void onReceive(Context context, Intent intent) {
            String intentAction = intent.getAction();
            if (TextUtils.equals(intentAction, Intent.ACTION_CLOSE_SYSTEM_DIALOGS)) {
                String reason = intent.getStringExtra(SYSTEM_DIALOG_REASON_KEY);
                if (TextUtils.equals(SYSTEM_DIALOG_REASON_HOME_KEY, reason)) {
                    BaseActivity.this.finish();
                }
            }
        }
    }


    /**
     * 设置Title栏蓝色
     */
    protected void setTitle() {
        mTitleView = findViewById(R.id.common_title_layout);
        mImgvForLeft = (TextView) findViewById(R.id.common_title_left);
        mImgvForRight = (TextView) findViewById(R.id.common_title_right);
        mTvForTitle = (TextView) findViewById(R.id.common_title_tv);
    }

    /**
     * 跳转到下一个Activity
     *
     * @param class1
     */
    public void gotoActivity(Class class1) {
        Intent i = new Intent(this, class1);
        startActivity(i);
    }

    /**
     * 传递数据并跳转到下一Activity
     *
     * @param bundle
     * @param class1
     */
    public void gotoActivity(Bundle bundle, Class class1) {
        Intent i = new Intent(this, class1);
        i.putExtras(bundle);
        startActivity(i);
    }

    /**
     * 传递数据并跳转到下一Activity,并且有返回数据
     *
     * @param bundle
     * @param class1
     * @param requestCode
     */
    public void gotoActivityForResult(Bundle bundle, Class class1,
                                      int requestCode) {
        Intent i = new Intent(this, class1);
        i.putExtras(bundle);
        startActivityForResult(i, requestCode);
    }

    public void gotoActivityForResult(Class class1, int requestCode) {
        Intent i = new Intent(this, class1);
        startActivityForResult(i, requestCode);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppManager.getAppManager().finishActivity(this);
        if (mHomeWatcherReceiver != null) {
            try {
                unregisterReceiver(mHomeWatcherReceiver);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void onDestory(Class context) {
        AppManager.getAppManager().finishActivity(context);
    }
}

