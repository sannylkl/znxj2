package com.jiarui.znxj.utils;

import android.util.Log;

public class LogUtil {
    /**
     * 输出日志
     *
     * @param TAG      用于标识不同的类
     * @param function 方法名
     * @param message  输出内容
     */
    public static void log(String TAG, String function, String message) {
        Log.i("info", TAG + "-" + function + "-" + message);
    }

    /**
     * 输出日志，默认会输出调用者的方法名
     *
     * @param TAG     用于标识不同的类
     * @param message 输出内容
     */
    public static void log(String TAG, String message) {
        Log.i("info", TAG + "-" + new Exception().getStackTrace()[1].getMethodName() + "-" + message);
    }

    /**
     * 输出错误日志（可以显示超过4K的数据）
     */
    public static void e(String tag, String msg) {

        int length = msg.length();

        if (length >= 4000) {

            int chunkCount = (length % 4000) > 0 ? (length / 4000 + 1) : (length / 4000);

            for (int i = 0; i < chunkCount; i++) {

                int min = 4000 * i;
                int max = 4000 * (i + 1);

                if (max >= length) {
                    Log.e(tag, msg.substring(min, length));
                } else {
                    Log.e(tag, msg.substring(min, max));
                }
            }
        } else {
            Log.e(tag, msg);
        }
    }

}
