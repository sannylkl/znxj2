package com.jiarui.znxj.utils;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.jiarui.znxj.constants.InterfaceDefinition;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;


public class PacketUtil {
    public static final String TAG = PacketUtil.class.getSimpleName();

    /**
     * @param context  下下文对象
     * @param packetNo 报文编号
     * @param data     报文体
     * @return 请求报文
     */
    public static String getRequestPacket(Context context, String packetNo, String UserId, String data) {
        String result = "";
        JSONObject rows = new JSONObject();
        try {
            // 请求代码＋用户ID+当前时间，然后用md5加密
            if (com.jiarui.znxj.utils.StringUtil.isEmpty(UserId)) {
                UserId = com.jiarui.znxj.utils.PreferencesUtil.get(context, InterfaceDefinition.PreferencesUser.USER_ID, "").toString();
            }
            String date = System.currentTimeMillis() + "";
            String token = packetNo + UserId + date + InterfaceDefinition.ICommonKey.KEY;
            Log.i("main", "==========加密前==========：" + token);
            token = MD5keyBean.newInstance().getkeyBeanofStr(token);
            Log.i("main", "==========加密后==========：" + token);
            rows.put(InterfaceDefinition.ICommonKey.TOKEN, token);
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            String deviceId = tm.getDeviceId();
            rows.put(InterfaceDefinition.ICommonKey.DEVICE_ID, deviceId);
            rows.put(InterfaceDefinition.ICommonKey.ROLES, "");
            // 添加报文编号
            rows.put(InterfaceDefinition.ICommonKey.PACK_NO, packetNo);
            // 发起方时间
            rows.put(InterfaceDefinition.ICommonKey.REQ_DATE, date);
            // 用户ID
            rows.put(InterfaceDefinition.ICommonKey.USER_ID, UserId);
            // data为空时，添加空的JOSNObject
            rows.put(InterfaceDefinition.ICommonKey.DATA, data == null ? new JSONObject() : new JSONObject(data));
            LogUtil.log(TAG, "getRequestPacket", "请求报文:" + rows.toString());
            result = ChangeCharset.toUTF_8(rows.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }
}
