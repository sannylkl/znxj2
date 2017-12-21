package com.jiarui.znxj.utils;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;

import java.util.List;

/**
 *  启动app by Administrator on 2017/11/4 0004.
 */

public class startActivityAPPutils {

    public ResolveInfo doStartApplicationWithPackageName(String packagename,PackageManager pack) {

        // 通过包名获取此APP详细信息，包括Activities、services、versioncode、name等等
        PackageInfo packageinfo = null;
        try {
            packageinfo = pack.getPackageInfo(packagename, 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        if (packageinfo != null) {
            // 创建一个类别为CATEGORY_LAUNCHER的该包名的Intent
            Intent resolveIntent = new Intent(Intent.ACTION_MAIN, null);
            resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER);
            resolveIntent.setPackage(packageinfo.packageName);

            // 通过getPackageManager()的queryIntentActivities方法遍历
            List<ResolveInfo> resolveinfoList = pack
                    .queryIntentActivities(resolveIntent, 0);
            ResolveInfo resolveinfo = resolveinfoList.iterator().next();
            return resolveinfo;
        }
        return null;
    }
}
