package com.jiarui.znxj.activity;


import android.Manifest;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jiarui.znxj.Interface.IToken;
import com.jiarui.znxj.R;
import com.jiarui.znxj.adapter.CommonAdapter;
import com.jiarui.znxj.base.BaseActivity;
import com.jiarui.znxj.bean.CityBean;
import com.jiarui.znxj.bean.MainGridBean;
import com.jiarui.znxj.bean.UpataVersionBean;
import com.jiarui.znxj.constants.GetToken;
import com.jiarui.znxj.constants.InterfaceDefinition;
import com.jiarui.znxj.grobal.AppManager;
import com.jiarui.znxj.holder.ViewHolder;
import com.jiarui.znxj.utils.DefaultCommonCallBack;
import com.jiarui.znxj.utils.LogUtil;
import com.jiarui.znxj.utils.PreferencesUtil;
import com.jiarui.znxj.utils.StringUtil;
import com.jiarui.znxj.utils.ToastUtil;
import com.jiarui.znxj.utils.VersionUtils;
import com.luck.picture.lib.permissions.RxPermissions;
import com.luck.picture.lib.tools.PictureFileUtils;

import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class MainActivity extends BaseActivity implements IToken{

    @ViewInject(R.id.main_grid)
    private GridView main_grid;//主界面gridview
    CommonAdapter<MainGridBean> commonAdapter;//适配器
    List<MainGridBean> list;//数据源
    boolean isExit;// 标记是否退出
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        x.view().inject(this);
        setTitle();
        mImgvForLeft.setVisibility(View.GONE);
       String city2 = (String) PreferencesUtil.get(MainActivity.this, InterfaceDefinition.PreferencesUser.City, "");
       String county =(String) PreferencesUtil.get(MainActivity.this,InterfaceDefinition.PreferencesUser.County, "");
       String  reservoir =(String) PreferencesUtil.get(MainActivity.this,InterfaceDefinition.PreferencesUser.Reservoir, "");
        mTvForTitle.setText(city2+county+reservoir);
        mImgvForRight.setVisibility(View.GONE);
        init();
        Perm();
        //更新版本
        //        生成广播处理
        Receiver   receiver = new Receiver();
        //实例化过滤器并设置要过滤的广播
        IntentFilter intentFilter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        //注册广播
        MainActivity.this.registerReceiver(receiver, intentFilter);
        UpataVersion();
    }
   String itmeText[]=new String[]{"任务中心","紧急汇报","数据查询","个人中心","巡检预置"};
    int itmeIcon[]=new int[]{R.mipmap.task_center,R.mipmap.report_icon,R.mipmap.query_icon,R.mipmap.mine_icon,R.mipmap.set_icon};
    public void init() {
        list = new ArrayList<>();
        String group_id= (String) PreferencesUtil.get(MainActivity.this, InterfaceDefinition.PreferencesUser.GROUP_ID, "-1");
        if (group_id.equals("1")){//巡检员（itmeText.length-1）
            for (int i = 0; i <itmeText.length-1 ; i++) {
                MainGridBean mainGrid=new MainGridBean(itmeIcon[i],itmeText[i]);
                list.add(mainGrid);
            }
        }else{//设点员（itmeText.length）
            for (int i = 0; i <itmeText.length ; i++) {
                MainGridBean mainGrid=new MainGridBean(itmeIcon[i],itmeText[i]);
                list.add(mainGrid);
            }
        }
        commonAdapter = new CommonAdapter<MainGridBean>(MainActivity.this, list, R.layout.main_grid_item) {
            @Override
            public void convert(ViewHolder mHolder, MainGridBean item, int position) {
                mHolder.setText(R.id.main_grid_text, item.getTitle());
                mHolder.setImageResource(R.id.main_grid_image, item.getImage());
            }
        };
        main_grid.setAdapter(commonAdapter);

        main_grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    gotoActivity(TaskCenterActivity.class);//任务中心
                } else if (i == 1) {
                    ToastUtil.TextToast("此板块加急完善中。。。");
//                    gotoActivity(UrgentReportActivity.class);//紧急汇报
                } else if (i == 2) {
                    ToastUtil.TextToast("此板块加急完善中。。。");
//                    gotoActivity(QueryActivity.class);//数据查询
                } else if (i == 3) {
                    gotoActivity(PersonalCenterActivity.class);//个人中心
                } else if (i == 4) {
                    gotoActivity(PatrolListActivity.class);//巡检预置
                }
            }
        });
    }

    //进入首页必须要获取权限
    public void Perm() {
        RxPermissions permissions = new RxPermissions(this);
        permissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION).subscribe(new Observer<Boolean>() {
            @Override
            public void onSubscribe(Disposable d) {
            }

            @Override
            public void onNext(Boolean aBoolean) {
                if (aBoolean) {
                    PictureFileUtils.deleteCacheDirFile(MainActivity.this);
                } else {
                    Toast.makeText(MainActivity.this, "未获得相应的权限，程序不能正常运行", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onComplete() {
            }
        });
    }

    // 重写onkeyDown方法
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return false;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    // 退出方法
    private void exit() {
        if (!isExit) {
            isExit = true;
            Toast.makeText(getApplicationContext(), "再按一次退出程序",
                    Toast.LENGTH_SHORT).show();
            mHandler.sendEmptyMessageDelayed(0, 2000);
        } else {
            AppManager.getAppManager().AppExit(MainActivity.this);
        }

    }

    Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            isExit = false;
        }
    };
    // 这个更新的APK的版本部分，我们是这样命名的:xxx_v1.0.0_xxxxxxxxx.apk
    // 这里我们用的是git提交版本的前九位作为表示
    private static final String FILE_NAME = "znxj-";
    /*后台返回数据*/
    UpataVersionBean upataVersion;
    /**
     * 版本更新接口
     */
    private void UpataVersion() {
        String url = InterfaceDefinition.IUpdataVersion.IURL + PreferencesUtil.get(this, InterfaceDefinition.PreferencesUser.Docking_CREDENTIALS, "");
        LogUtil.e("版本更新接口拼接路径", url);
        // 接口对接
        RequestParams mParams = new RequestParams(url);
        mParams.addBodyParameter(InterfaceDefinition.IUpdataVersion.NAME,VersionUtils.getVersionName(MainActivity.this));
        mParams.addBodyParameter(InterfaceDefinition.IUpdataVersion.NUMBER, ""+VersionUtils.getVersionCode(MainActivity.this));
        x.http().get(mParams, new DefaultCommonCallBack(MainActivity.this, true) {
            @Override
            public void onSuccess(String json) {
                LogUtil.e("版本更新接口", json);
                upataVersion= new Gson().fromJson(json, UpataVersionBean.class);
                if (InterfaceDefinition.IStatusCode.SUCCESS.equals(upataVersion.getStatus())) {
                    String   endpoint = upataVersion.getResult().getDownload_url();//.replace("https", "http")
                    // 下面的都是拼接apk更新下载url的，path是保存的文件夹路径//可以设置在_Path。也可以不用，目前没有用到这个文件夹
                    final String _Path = setFile();
                    if (StringUtil.isNotEmpty(_Path)){
                        final String _Url = endpoint;
                        final DownloadManager _DownloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                        DownloadManager.Request _Request = new DownloadManager.Request(Uri.parse(_Url));
                        _Request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, FILE_NAME +VersionUtils.getVersionName(MainActivity.this)+ ".apk");
                        _Request.setTitle(MainActivity.this.getString(R.string.app_name));
                        // 是否显示下载对话框
                        _Request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
                        _Request.setMimeType("application/com.trinea.download.file");
                        // 将下载请求放入队列
                        _DownloadManager.enqueue(_Request);
                    }else
                    {
                        Log.e("NULL","NULL");
                    }
                } else if ("401".equals(upataVersion.getStatus())) {
                    GetToken mGetToken = new GetToken(MainActivity.this);
                    mGetToken.getToken();
                } else {
                    ToastUtil.TextToast("" + upataVersion.getMsg());
                }
            }
        });
    }
    /**
     * 创建文件夹
     * @return
     */
    public String setFile() {
        String path = "";
        path = Environment.getExternalStorageDirectory() + "/XinJiangHuiMB/";
        File dirFile = new File(path);
        if (!dirFile.exists()) {
            dirFile.mkdir();
        }
        return path;
    }
    // 注册一个广播接收器，当下载完毕后会收到一个android.intent.action.DOWNLOAD_COMPLETE
    // 的广播,在这里取出队列里下载任务，进行安装
    public static class Receiver extends BroadcastReceiver {
        public void onReceive(Context context, Intent intent) {
            final DownloadManager _DownloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
            final long _DownloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0);
            final DownloadManager.Query _Query = new DownloadManager.Query();
            _Query.setFilterById(_DownloadId);
            final Cursor _Cursor = _DownloadManager.query(_Query);
            if (_Cursor.moveToFirst()) {
                final int _Status = _Cursor.getInt(_Cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_STATUS));
                final String _Name = _Cursor.getString(_Cursor.getColumnIndexOrThrow("local_filename"));
                if (_Status == DownloadManager.STATUS_SUCCESSFUL && _Name.indexOf(FILE_NAME) != 0) {

                    Intent _Intent = new Intent(Intent.ACTION_VIEW);
                    _Intent.setDataAndType(Uri.parse(_Cursor.getString(_Cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_LOCAL_URI))), "application/vnd.android.package-archive");
                    _Intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(_Intent);
                }
            }
            _Cursor.close();
        }
    }
    @Override
    public void success(int errtype) {
    if (errtype==0){
        UpataVersion();
    }
    }
}
