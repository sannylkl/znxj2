package com.jiarui.znxj.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.jiarui.znxj.Interface.IToken;
import com.jiarui.znxj.R;
import com.jiarui.znxj.constants.Constants;
import com.jiarui.znxj.constants.InterfaceDefinition;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback.CommonCallback;

import java.io.UnsupportedEncodingException;


public abstract class DefaultCommonCallBack implements CommonCallback<String> {
    public static final String TAG = DefaultCommonCallBack.class.getSimpleName();

    private Context mContext;

    /**
     * 是否响应成功
     */
    private boolean mResponseSuccess;

    private ProgressDialog mDialog;

    /**
     * 响应Data节点
     */
    private String mDataObject;

    /**
     * 响应码
     */
    private String mCode;

    /**
     * 响应消息
     */
    private String mMessage;

    // Dialog是否显示
    private boolean mIsDialogShow = true;

    public boolean isResponseSuccess() {
        return mResponseSuccess;
    }

    public void setmResponseSuccess(boolean mResponseSuccess) {
        this.mResponseSuccess = mResponseSuccess;
    }

    public String getDataObject() {
        return mDataObject;
    }

    public void setDataObject(String mDataObject) {
        this.mDataObject = mDataObject;
    }

    public String getCode() {
        return mCode;
    }

    public void setCode(String mCode) {
        this.mCode = mCode;
    }

    public String getMessage() {
        return mMessage;
    }

    public void setMessage(String mMessage) {
        this.mMessage = mMessage;
    }

    public DefaultCommonCallBack(Context context) {
        mContext = context;
        initDialogShow();
    }

    private IToken mItoken;
    /**
     * @param context
     * @param isDialogShow true 表示显示dialog ,false 表示不显示
     */
    public DefaultCommonCallBack(Context context, boolean isDialogShow) {
        mContext = context;
        mIsDialogShow = isDialogShow;
        if (isDialogShow)
            initDialogShow();
    }


    /**
     * @param context
     * @param isDialogShow true 表示显示dialog ,false 表示不显示
     * @param mItoken      看需不需要获取token
     */
    public DefaultCommonCallBack(Context context, boolean isDialogShow, IToken mItoken) {
        this.mItoken = mItoken;
        mContext = context;
        mIsDialogShow = isDialogShow;
        if (isDialogShow)
            initDialogShow();
    }

    boolean tvChange;

    public DefaultCommonCallBack(Context context, boolean isDialogShow, IToken mItoken, boolean tvChange) {
        this.mItoken = mItoken;
        this.tvChange = tvChange;
        mContext = context;
        mIsDialogShow = isDialogShow;
        if (isDialogShow)
            initDialogShow();
    }

    private void initDialogShow() {
        initViews();
        mDialog.show();
        mDialog.setContentView(R.layout.progress_bar_dialog);
    }

    @Override
    public void onCancelled(CancelledException arg0) {

    }

    @Override
    public void onError(Throwable arg0, boolean arg1) {
        Log.e("onEror",arg0.toString());
        com.jiarui.znxj.utils.ToastUtil.TextToast("请检查网络环境是否正常");
    }

    @Override
    public void onFinished() {
        if (mIsDialogShow)
            dismissProgressDialog();
    }

    @Override
    public void onSuccess(String json) {
        com.jiarui.znxj.utils.LogUtil.log(TAG, "responseData:", new String(json));
        try {
            JSONObject rows = new JSONObject(new String(json.getBytes(), Constants.CHARSET));
            Log.e("json数据:", rows.toString());
            if (com.jiarui.znxj.utils.StringUtil.isJSONObjectNotEmpty(rows)) {
                mCode = rows.optJSONObject(InterfaceDefinition.IStatus.STATUS).getString(InterfaceDefinition.IStatus.CODE);
                mMessage = rows.optJSONObject(InterfaceDefinition.IStatus.STATUS).getString(InterfaceDefinition.IStatus.MESSAGE);
                if (InterfaceDefinition.IStatusCode.SUCCESS.equals(mCode)) {
                    // 后台响应成功
                    mDataObject = rows.optJSONObject(InterfaceDefinition.ICommonKey.DATA).toString();
                    // 响应成功
                    mResponseSuccess = true;
                    return;
                } else {
                    // 其他错误
                    Toast.makeText(mContext, mMessage, Toast.LENGTH_SHORT).show();
                }
                mResponseSuccess = false;
            }
        } catch (UnsupportedEncodingException e) {
            mResponseSuccess = false;
            e.printStackTrace();
        } catch (JSONException e) {
            mResponseSuccess = false;
            e.printStackTrace();
        }
    }

    private void initViews() {
        mDialog = new ProgressDialog(mContext, R.style.Dialog);
        mDialog.setCancelable(false);
        mDialog.setCanceledOnTouchOutside(false);
    }

    protected void dismissProgressDialog() {
        if (mDialog.isShowing()) {
            // 对话框显示中
            mDialog.dismiss();
        }
    }

}
