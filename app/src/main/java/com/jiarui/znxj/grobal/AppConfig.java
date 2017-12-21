package com.jiarui.znxj.grobal;

import android.os.Environment;

import java.io.File;

/**
 *
 * @author Only You
 * @version 1.0
 * @date 2016年1月10日
 */
public class AppConfig
{
    public final static String APP_SDCAR_FOLDER = Environment.getExternalStorageDirectory() + File.separator + "YongBing" + File.separator;

    public final static String DEFAULT_SAVE_DB_PATH = APP_SDCAR_FOLDER + "db" + File.separator;

    public final static String DEFAULT_SAVE_LOG_PATH = APP_SDCAR_FOLDER + "log" + File.separator;

    public final static String DEFAULT_SAVE_APK_PATH = APP_SDCAR_FOLDER + "apk" + File.separator;
}
