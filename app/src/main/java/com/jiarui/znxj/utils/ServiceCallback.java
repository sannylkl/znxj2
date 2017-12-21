package com.jiarui.znxj.utils;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * 定位回调
 */

public class ServiceCallback extends Service {

    public static final String TAG = "MyService";

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
