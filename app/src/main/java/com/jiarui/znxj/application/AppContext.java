package com.jiarui.znxj.application;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.StrictMode;
import android.support.multidex.MultiDex;
import android.util.DisplayMetrics;
import android.util.Log;

import com.jiarui.znxj.constants.InterfaceDefinition;
import com.jiarui.znxj.grobal.AppConfig;
import com.jiarui.znxj.utils.PreferencesUtil;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.utils.StorageUtils;

import org.xutils.DbManager;
import org.xutils.x;

import java.io.File;
import java.util.List;

/**
 * 全局
 *
 * @author PS
 * @version 1.0
 * @date 2016年1月10日
 */
public class AppContext extends Application {
    public static final int NETTYPE_WIFI = 0x01;

    public static final int NETTYPE_CMWAP = 0x02;

    public static final int NETTYPE_CMNET = 0x03;

    protected static Context context;

    private static DbManager.DaoConfig mConfig = null;

    /**
     * 屏幕宽度
     */
    public static int screenWidth;
    /**
     * 屏幕高度
     */
    public static int screenHeight;
    /**
     * 缓存路径
     */
    public static String cachePath;

    public static boolean chose;

    public static SharedPreferences sp;
//
    public static DbManager.DaoConfig getConfig() {
        return mConfig;
    }

    public void setConfig(DbManager.DaoConfig Config) {
        mConfig = Config;
    }
    /**
     * 实现单例，任何一个页面都能拿到这个类的数据和对象
     */
    private static AppContext instance;

    public static AppContext getInstance() {
        return instance;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        MultiDex.install(this);
        context = this;
        initXutils();
        initImageLoader(this);
        initDb();
        getScreenSize();
        // 初始化图片加载
        // 数据库操作对象
        // 异常处理
        instance = this;
        //对接凭据token
//        PreferencesUtil.put(context, InterfaceDefinition.PreferencesUser.Docking_CREDENTIALS, "VJyk1WLZle4Pnht14oZFvVOsz8pzqSTB_1488880131");
        sp = getSharedPreferences("meiya", 0);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
        }
    }

    /**
     * 获取屏幕宽高 void
     */
    private void getScreenSize() {

        DisplayMetrics dm = new DisplayMetrics();
        dm = getApplicationContext().getResources().getDisplayMetrics();
        screenWidth = dm.widthPixels; // 1024
        screenHeight = dm.heightPixels; // 720
        float density = dm.density; // 1.0
        float densityDpi = dm.densityDpi; // 160.0
    }
    /**
     * 获取数据库的管理器
     * 通过管理器进行增删改查
     */
    public DbManager getDbManager() {
        DbManager.DaoConfig daoconfig = new DbManager.DaoConfig();
        //默认在data/data/包名/database/数据库名称
//        daoconfig.setDbDir()
        daoconfig.setDbName("user.db");
//        daoconfig.setDbVersion(1);//默认1
        //通过manager进行增删改查
        return x.getDb(daoconfig);
    }

    /**
     * 初始化数据
     */
    private void initDb() {
        if (mConfig == null) {
            // 创建DaoConfig对象
            mConfig = new DbManager.DaoConfig();
            // 设置数据库的名字
            mConfig.setDbName("user.db");
            // 设置数据库存放的位置/默认在data/data/包名/database/数据库名称
            mConfig.setDbDir(new File(AppConfig.DEFAULT_SAVE_DB_PATH));
            // 设置数据库的版本//默认1
            mConfig.setDbVersion(1);
            setConfig(mConfig);
        }
    }

    public static Context getContext() {
        return context;
    }

    private void initXutils() {
        // 初始化Xutils
        x.Ext.init(this);
        // 设置是否输出debug日志
        // x.Ext.setDebug(true);
    }

    /**
     * 初始化ImageLoader
     *
     * @param context
     */
    public static void initImageLoader(Context context) {
        // 获取缓存图片目录
        File cacheDir = StorageUtils.getOwnCacheDirectory(context,
                "imageloader/Cache");
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                context)
                .threadPriority(Thread.MAX_PRIORITY - 1)
                .diskCache(new UnlimitedDiscCache(cacheDir))
                .tasksProcessingOrder(QueueProcessingType.FIFO)
                // 设置图片下载和显示的工作队列排序
                .denyCacheImageMultipleSizesInMemory()
                // .memoryCacheExtraOptions(400, 800)
                .threadPoolSize(3)
                .diskCacheSize(50 * 1024 * 1024)
                // .memoryCacheSize(1 * 1024 * 1024)
                // .memoryCache(new FIFOLimitedMemoryCache())
                // .memoryCacheSize(3 * 1024 * 1024)
                // .memoryCacheExtraOptions(480, 800) // default = device screen
                // dimensions
                // .diskCacheExtraOptions(320, 480, null)
                .imageDownloader(
                        new BaseImageDownloader(context, 10 * 1000, 30 * 1000))
                .build();
        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config);
    }

    /**
     * 获取App安装包信息
     *
     * @param context
     * @return
     * @throws NameNotFoundException
     */
    public static String getVersionName(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(
                    context.getPackageName(), 0).versionName;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 检测网络是否可用
     *
     * @return
     */
    public static boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        return ni != null && ni.isConnectedOrConnecting();
    }

    /**
     * 获取当前网络类型
     *
     * @return 0：没有网络 1：WIFI网络 2：WAP网络 3：NET网络
     */
    public int getNetworkType() {
        int netType = 0;
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo == null) {
            return netType;
        }
        int nType = networkInfo.getType();
        if (nType == ConnectivityManager.TYPE_MOBILE) {
            String extraInfo = networkInfo.getExtraInfo();
            if (extraInfo != null) {
                if (extraInfo.toLowerCase().equals("cmnet")) {
                    netType = NETTYPE_CMNET;
                } else {
                    netType = NETTYPE_CMWAP;
                }
            }
        } else if (nType == ConnectivityManager.TYPE_WIFI) {
            netType = NETTYPE_WIFI;
        }
        return netType;
    }

    public String getRunningActivityName() {
        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        String runningActivity = activityManager.getRunningTasks(1).get(0).topActivity
                .getClassName();
        return runningActivity;
    }

    /**
     * 检测app是否在后台运行
     *
     * @param context
     * @return
     */
    public static boolean isBackground(Context context) {
        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningAppProcessInfo> appProcesses = activityManager
                .getRunningAppProcesses();
        for (RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.processName.equals(context.getPackageName())) {
                Log.i(context.getPackageName(), "此appimportace ="
                        + appProcess.importance
                        + ",context.getClass().getName()="
                        + context.getClass().getName());
                if (appProcess.importance != RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    Log.i(context.getPackageName(), "处于后台"
                            + appProcess.processName);
                    return true;
                } else {
                    Log.i(context.getPackageName(), "处于前台"
                            + appProcess.processName);
                    return false;
                }
            }
        }
        return false;
    }
}
