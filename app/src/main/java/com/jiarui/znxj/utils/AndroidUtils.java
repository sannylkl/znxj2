package com.jiarui.znxj.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

/**
 * 单位转换类
 *
 * @author Only You
 * @version 1.0
 * @date 2016年1月6日
 */
public class AndroidUtils {

    public static Map<String, Object> getCurrentVersion(Context paramContext) {
        HashMap<String, Object> localHashMap = new HashMap<String, Object>();
        try {
            PackageInfo localPackageInfo = paramContext.getPackageManager()
                    .getPackageInfo(paramContext.getPackageName(), 0);
            String str = localPackageInfo.versionName;
            localHashMap.put("versionCode",
                    Integer.valueOf(localPackageInfo.versionCode));
            localHashMap.put("versionName", str);
            return localHashMap;
        } catch (PackageManager.NameNotFoundException localNameNotFoundException) {
            localNameNotFoundException.printStackTrace(System.err);
        }
        return localHashMap;
    }

    public static String getDeviceId(Context paramContext) {
        return ((TelephonyManager) paramContext.getSystemService("phone"))
                .getDeviceId();
    }

    public static final int getHeightInDp(Context paramContext) {
        return px2dip(paramContext, paramContext.getResources()
                .getDisplayMetrics().heightPixels);
    }

    public static final float getHeightInPx(Context paramContext) {
        return paramContext.getResources().getDisplayMetrics().heightPixels;
    }

    public static final int getWidthInDp(Context paramContext) {
        return px2dip(paramContext, paramContext.getResources()
                .getDisplayMetrics().heightPixels);
    }

    public static final float getWidthInPx(Context paramContext) {
        return paramContext.getResources().getDisplayMetrics().widthPixels;
    }

    public static boolean isMultiPane(Context paramContext) {
        return paramContext.getResources().getConfiguration().orientation == 2;
    }

    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 将dip或dp值转换为px值，保证尺寸大小不变
     *
     * @param dipValue
     * @param
     * @return
     */
    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    /**
     * 将px值转换为sp值，保证文字大小不变
     *
     * @param context
     * @param pxValue （DisplayMetrics类中属性scaledDensity）
     * @return
     */
    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     *
     * @param spValue
     * @param
     * @return
     */
    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * 返回当前程序版本名
     */
    public static String getAppVersionName(Context context) {
        String versionName = "";

        try {
            // ---get the package info---
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            versionName = pi.versionName;

            if (versionName == null || versionName.length() <= 0) {
                return "";
            }
        } catch (Exception e) {
            Log.e("VersionInfo", "Exception", e);
        }
        return versionName;
    }

}