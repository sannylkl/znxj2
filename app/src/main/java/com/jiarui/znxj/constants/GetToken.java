package com.jiarui.znxj.constants;


import com.jiarui.znxj.Interface.IToken;
import com.jiarui.znxj.application.AppContext;
import com.jiarui.znxj.utils.DefaultCommonCallBack;
import com.jiarui.znxj.utils.PreferencesUtil;

import org.json.JSONException;
import org.json.JSONObject;
import com.jiarui.znxj.utils.LogUtil;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.HashMap;
import java.util.Map;

/**
 * 获取Token的实现
 * date 2017/3/8.
 */
public class GetToken {

    private IToken mIToken;

    public GetToken(IToken mIToken) {
        this.mIToken = mIToken;
    }

    /**
     * 获取Token
     * http://api.taobaowed.com/?r=auth/get-token/login
     *
     * @return 成功和失败
     */
    public void getToken() {
        // 拼接路径
        Map<String, String> mMap = new HashMap<String, String>();
        mMap.put(InterfaceDefinition.IToken.R, InterfaceDefinition.IToken.RValue);
        LogUtil.e("获取Token的url", InterfaceDefinition.addUrlValue(mMap));
        // 接口对接
        RequestParams mParams = new RequestParams(InterfaceDefinition.addUrlValue(mMap));
        // 参数
        mParams.addBodyParameter(InterfaceDefinition.IToken.USERNAME, InterfaceDefinition.IToken.TOKEN_USERNAME);
        mParams.addBodyParameter(InterfaceDefinition.IToken.PASSWORD, InterfaceDefinition.IToken.TOKEN_PASSWORE);
        x.http().post(mParams, new DefaultCommonCallBack(AppContext.getContext(), false, mIToken) {
            @Override
            public void onSuccess(String json) {
                LogUtil.e("获取Token", json);// {"errcode":0,"token":"6kUfj1ClEYUKB-ns9_ATRYpc09KwFKdK_1489979077"}
                try {
                    JSONObject object = new JSONObject(json);
                    String code = object.optString("status");
                    String token = object.optString("token");
                    if ("0".equals(code)) {
                        PreferencesUtil.put(AppContext.getContext(), InterfaceDefinition.PreferencesUser.Docking_CREDENTIALS, token);
                        mIToken.success(0);// mIToken.failure();
                        return;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

}
