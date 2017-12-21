package com.jiarui.znxj.utils;

import android.os.Environment;

import com.jiarui.znxj.application.AppContext;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.OutputStreamWriter;

/**
 * 缓存工具类
 *
 * @author Only You
 * @version 1.0
 * @date 2016年1月27日
 */
public class CacheUtils {

    static File realFile;

    static {
        File dir = AppContext.getContext().getExternalFilesDir(null);
        if (!dir.exists() && Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            dir.mkdirs();
        }
        realFile = dir;
    }

    /**
     * 根据url的MD5作为文件名,进行缓存
     *
     * @param url  文件名
     * @param json
     */
    public static void setCacheToLocalJson(String url, String json) {
        String urlMD5 = com.jiarui.znxj.utils.MD5keyBean.newInstance().getkeyBeanofStr(url);
        String path = realFile.getAbsolutePath() + "/" + urlMD5;
        try {
            File file = new File(path);
            if (file.exists()) {
                file.delete();
            }
            FileOutputStream fis = new FileOutputStream(file);
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fis));
            long currentTime = System.currentTimeMillis();
            bw.write(currentTime + "");
            bw.newLine();
            bw.write(json);
            bw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 根据缓存地址，从缓存中取出数据
     *
     * @param url
     * @return
     */
    public static String getCacheToLocalJson(String url) {
        StringBuffer sb = new StringBuffer();
        String urlMD5 = com.jiarui.znxj.utils.MD5keyBean.newInstance().getkeyBeanofStr(url);
        // 创建缓存文件夹
        File file = new File(realFile, urlMD5);
        if (file.exists()) {
            try {
                FileReader fr = new FileReader(file);
                BufferedReader br = new BufferedReader(fr);
                br.readLine();
                String temp;
                while ((temp = br.readLine()) != null) {
                    sb.append(temp);
                }
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        return sb.toString();
    }
}