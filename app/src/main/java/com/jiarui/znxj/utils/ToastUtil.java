package com.jiarui.znxj.utils;

import android.view.Gravity;
import android.widget.Toast;

import com.jiarui.znxj.application.AppContext;


public class ToastUtil {
    private static Toast toast = null;
    private static int LENGTH_SHORT = Toast.LENGTH_SHORT;

    public static void TextToast(String msg) {
        if (toast != null) {
            toast.cancel();
        }
        // 创建一个Toast提示消息
        toast = Toast.makeText(AppContext.getContext(), msg, LENGTH_SHORT);
        // 设置Toast提示消息在屏幕上的位置
        toast.setGravity(Gravity.BOTTOM, 0, 240);
        // 显示消息
        toast.show();
    }

}