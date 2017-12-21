package com.jiarui.znxj.activity;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import com.jiarui.znxj.Interface.IToken;
import com.jiarui.znxj.R;
import com.jiarui.znxj.adapter.MemberTypeAdapter;
import com.jiarui.znxj.application.AppContext;
import com.jiarui.znxj.base.BaseActivity;
import com.jiarui.znxj.bean.AAA;
import com.jiarui.znxj.bean.CheckTheContentAbnormalsTable;
import com.jiarui.znxj.bean.CheckTheContentPhotoTable;
import com.jiarui.znxj.bean.CheckTheContentTable;
import com.jiarui.znxj.bean.CheckTheContentVideoTable;
import com.jiarui.znxj.bean.CityBean;
import com.jiarui.znxj.bean.DataSynEvent;
import com.jiarui.znxj.bean.TaskDetailsTableLoctionBean;
import com.jiarui.znxj.bean.TaskDetailsTableLoctionDataBean;
import com.jiarui.znxj.bean.TaskDetailsTablePartBean;
import com.jiarui.znxj.bean.TaskPreviewData;
import com.jiarui.znxj.bean.TaslListBeanTable;
import com.jiarui.znxj.bean.UploadFileBean;
import com.jiarui.znxj.bean.UploadPdfServiseBean;
import com.jiarui.znxj.constants.GetToken;
import com.jiarui.znxj.constants.InterfaceDefinition;
import com.jiarui.znxj.grobal.AppManager;
import com.jiarui.znxj.pdfhtml.AssetsCopyTOSDcard;
import com.jiarui.znxj.pdfhtml.MyFont;
import com.jiarui.znxj.utils.CommonDialog;
import com.jiarui.znxj.utils.CretePDFUtils;
import com.jiarui.znxj.utils.DefaultCommonCallBack;
import com.jiarui.znxj.utils.HttpUtil;
import com.jiarui.znxj.utils.PreferencesUtil;
import com.jiarui.znxj.utils.StringUtil;
import com.jiarui.znxj.utils.ToastUtil;
import com.luck.picture.lib.entity.LocalMedia;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.DbManager;
import org.xutils.db.sqlite.WhereBuilder;
import org.xutils.ex.DbException;
import org.xutils.http.RequestParams;
import org.xutils.http.body.MultipartBody;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 任务预览
 */
public class TaskPreviewActivity extends BaseActivity {
    /*点位+检查部位+检查项*/
    @ViewInject(R.id.task_listview)
    public ListView task_listview;
    /*巡检水库*/
    @ViewInject(R.id.tv_group_name_rigth)
    public TextView tv_group_name_rigth;
    /*水位*/
    @ViewInject(R.id.previe_water_line)
    public TextView previe_water_line;
    /*天气*/
    @ViewInject(R.id.previe_weather)
    public TextView previe_weather;
    /*下游水位*/
    @ViewInject(R.id.previe_down_water)
    public TextView previe_down_water;
    private MemberTypeAdapter adapter;
    /*reseid（水库id）,taskid(任务id)routeid(路线id)*/
    int reseid,taskid,routeid;
    /*任务类型+发布任务的人*/
    String tasktype,creator;
    /*数据库*/
    DbManager dbManager = null;//AppContext.getInstance().getdbManager()
     /*任务数据*/
      List<TaskPreviewData> previewDatalist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_preview);
        x.view().inject(this);
        setTitle();
        mTvForTitle.setText("任务预览");
        dbManager = x.getDb(AppContext.getConfig());
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            taskid = bundle.getInt("taskid");//任务id
            reseid = bundle.getInt("reseid");//水库id
            routeid= bundle.getInt("routeid");//路线id
            tasktype= bundle.getString("tasktype");//任务类型
            creator= bundle.getString("creator");//发布任务的人
            Log.e("previewtaskid",""+taskid+"\treseid"+""+reseid+"\trouteid"+""+routeid);
        }
        previewDatalist=new ArrayList<>();
        // 取出数据
        selectData();
        preserva();
    }
    /**
     * 重新取出预览数据“点位+部位+检查项
     */
    private void selectData() {
        previewDatalist.clear();
        try {
            WhereBuilder didw = WhereBuilder.b();
            didw.and("reseid", "=", reseid);
            didw.and("taskid", "=", taskid);
            List<TaskDetailsTableLoctionBean> dwsqlist  = dbManager.selector(TaskDetailsTableLoctionBean.class).where(didw).findAll();
            if (dwsqlist!=null){
                Log.e("dwsqlistsize",""+dwsqlist.size());
                //存放数据到适配器的数据源里
                for (int db = 0; db < dwsqlist.size(); db++) {
                    int drowpan=0;
                    List<TaskPreviewData.ItemsBean> items=new ArrayList<>();
                    WhereBuilder bww = WhereBuilder.b();
                    bww.and("taskid", "=", taskid);
                    bww.and("did", "=", dwsqlist.get(db).getDid());
                    bww.and("pid", "=", 0);
                    List<TaskDetailsTablePartBean> bwsqlist  = dbManager.selector(TaskDetailsTablePartBean.class).where(bww).findAll();
                    for (int bwd = 0; bwd < bwsqlist.size(); bwd++){
                        List<TaskPreviewData.ItemsBean.ChildBean> childitems=new ArrayList<>();
                        WhereBuilder ccw = WhereBuilder.b();
                        ccw.and("taskid", "=", taskid);
                        ccw.and("did", "=",bwsqlist.get(bwd).getDid() );
                        ccw.and("pid", "=", bwsqlist.get(bwd).getCid());
                        List<TaskDetailsTablePartBean> ccwsqlist  = dbManager.selector(TaskDetailsTablePartBean.class).where(ccw).findAll();
                        drowpan=drowpan+ccwsqlist.size();
                        //检查项增加数据
                        for (int cd = 0; cd < ccwsqlist.size(); cd++){
                          String content=  selectContent( ccwsqlist.get(cd).getDid(), ccwsqlist.get(cd).getPid(),ccwsqlist.get(cd).getCid());
                          String photo=  selectPhoto(ccwsqlist.get(cd).getDid(), ccwsqlist.get(cd).getPid(),ccwsqlist.get(cd).getCid());
                          String video=   selectVideo(ccwsqlist.get(cd).getDid(), ccwsqlist.get(cd).getPid(),ccwsqlist.get(cd).getCid());
                            Log.e("ccwsqname"+cd,""+ccwsqlist.get(cd).getName());
                            TaskPreviewData.ItemsBean.ChildBean  previewCwItme=new TaskPreviewData.ItemsBean.ChildBean(
                                    ccwsqlist.get(cd).getTaskid(),  ccwsqlist.get(cd).getDid(),
                                    ccwsqlist.get(cd).getPid(),ccwsqlist.get(cd).getCid(),
                                    ccwsqlist.get(cd).getName(),ccwsqlist.get(cd).getStatus(),cContent,content+","+photo+","+video);
                            childitems.add(previewCwItme);
                        }
                        //部位增加数据
                        TaskPreviewData.ItemsBean previewBwItme=new TaskPreviewData.ItemsBean(
                                bwsqlist.get(bwd).getTaskid(),  bwsqlist.get(bwd).getDid(),
                                bwsqlist.get(bwd).getPid(), bwsqlist.get(bwd).getName(),
                                bwsqlist.get(bwd).getStatus(),childitems,false,ccwsqlist.size());
                        items.add(previewBwItme);
                    }

                    //点位增加数据,false
                    TaskPreviewData previewItme=new TaskPreviewData(dwsqlist.get(db).getTaskid(),  dwsqlist.get(db).getDid(),
                            dwsqlist.get(db).getTitle(),dwsqlist.get(db).getStatus(),items,false,drowpan);
                    previewDatalist.add(previewItme);
                }
            }
        } catch (DbException e) {
            e.printStackTrace();
        }

        adapter = new MemberTypeAdapter(previewDatalist, this);
        task_listview.setAdapter(adapter);
        task_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (previewDatalist.get(i).isCheck() == true) {
                    previewDatalist.get(i).setCheck(false);
                } else {
                    previewDatalist.get(i).setCheck(true);
                }
                adapter.notifyDataSetChanged();
            }
        });
        //取出水库数据
       String city2 = (String) PreferencesUtil.get(TaskPreviewActivity.this, InterfaceDefinition.PreferencesUser.City, "");
        String  county =(String) PreferencesUtil.get(TaskPreviewActivity.this,InterfaceDefinition.PreferencesUser.County, "");
        String   reservoir =(String) PreferencesUtil.get(TaskPreviewActivity.this,InterfaceDefinition.PreferencesUser.Reservoir, "");
        tv_group_name_rigth.setText(city2+county+reservoir);

        String   user_id = (String) PreferencesUtil.get(TaskPreviewActivity.this, InterfaceDefinition.PreferencesUser.USER_ID, "1");
        try {
            WhereBuilder tb = WhereBuilder.b();
            tb.and("id", "=", taskid);//条件
            tb.and("uid", "=", user_id);
            TaslListBeanTable taskbean = dbManager.selector(TaslListBeanTable.class).where(tb).findFirst();
            if (taskbean!=null){
                previe_water_line.setText(taskbean.getWater_line());
                previe_weather.setText(taskbean.getWeather());
                previe_down_water.setText(taskbean.getDown_water());
            }
        } catch (DbException e) {
            e.printStackTrace();
        }

    }

    @Event({R.id.common_title_left, R.id.common_text})
    private void onClick(View v) {
        switch (v.getId()) {
            case R.id.common_title_left:
                finish();
                break;
            case R.id.common_text:
                InItDialog();
//                if (selectIsSave()==true){
//                    InItDialog();
//                }else{
//                    ToastUtil.TextToast("还存在未保存的检查项");
//                }
                break;
            default:
                break;
        }
    }
    /**
     * 保存字体包
     */
    public void preserva() {
        String path = "znxj/DroidSansChinese.ttf";
        AssetsCopyTOSDcard assetsCopyTOSDcard = new AssetsCopyTOSDcard(getApplicationContext());
        assetsCopyTOSDcard.AssetToSD(path, Environment.getExternalStorageDirectory() + "/" + path);
    }
    /**
     * 获取单个文件的MD5值！
     *
     * @param file
     * @return
     */
    public static String getFileMD5(File file) {
        if (!file.isFile()) {
            return null;
        }
        MessageDigest digest = null;
        FileInputStream in = null;
        byte buffer[] = new byte[1024];
        int len;
        try {
            digest = MessageDigest.getInstance("MD5");
            in = new FileInputStream(file);
            while ((len = in.read(buffer, 0, 1024)) != -1) {
                digest.update(buffer, 0, len);
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return bytesToHexString(digest.digest());
    }

    public static String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }

    CheckTheContentTable ccdataselect;
    private String cContent,baseof_zc,baseof="";
    /**
     * 初始化本身数据
     */
    private String selectContent(int did,int pid,int cid) {
        try {
            WhereBuilder d = WhereBuilder.b();
            d.and("taskid", "=", taskid);
            d.and("did", "=", did);
            d.and("pid", "=", pid);
            d.and("cid", "=", cid);
            ccdataselect=dbManager.selector(CheckTheContentTable.class).where(d).findFirst();
            if (ccdataselect!=null){
                cContent=ccdataselect.getContent();
                if (ccdataselect.issave()==true){ //表里修改并保存过这条数据
                /*设备问题adioGroup*/
                    if (ccdataselect.isEquipmentstatus()==true){
                        baseof_zc="有设备问题";
                    }else {
                        baseof_zc="无设备问题";
                    }
                /*重复问题RadioGroup(是就是有重复问题=false，否就是无重复问题=true)*/
                    if (ccdataselect.isRepertstatus()==true){
                        baseof="有重复问题";
                    }else {
                        baseof="无重复问题";
                    }
                   return selectAbnormal(did,pid)+baseof_zc+","+baseof+","+ccdataselect.getOthercontent();
                }else{
                    return "未保存对应的检查结果";
                }
            }else{
                Log.e("ccdataselect" , "nullnullnull");
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
        return "数据查询失败";
    }
    private String ccAlist="";
    /**
     * 重新取出异常数据
     */
    private String selectAbnormal(int did,int pid) {
        try {
            WhereBuilder d = WhereBuilder.b();
            d.and("taskid", "=", taskid);
            d.and("did", "=", did);
            d.and("pid", "=", pid);
            d.and("cid", "=", ccdataselect.getCid());
            d.and("content_id", "=", ccdataselect.getContent_id());
            d.and("status", "=", true);
            List<CheckTheContentAbnormalsTable> bwsqlist  = dbManager.selector(CheckTheContentAbnormalsTable.class).where(d).findAll();
            if (bwsqlist!=null){
                //存放数据到适配器的数据源里
                for (int db = 0; db < bwsqlist.size(); db++) {
                    ccAlist=ccAlist+bwsqlist.get(db).getName()+",";
                }
                return ccAlist;
            }else{
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
        return "无异常数据";
    }

    /**
     * 重新取出图片
     */
    private String selectPhoto(int did,int pid,int cid) {
        try {
            WhereBuilder d = WhereBuilder.b();
            d.and("taskid", "=", taskid);
            d.and("did", "=", did);
            d.and("pid", "=", pid);
            d.and("cid", "=", cid);
            List<CheckTheContentPhotoTable> ccpsqlist  = dbManager.selector(CheckTheContentPhotoTable.class).where(d).findAll();
            if (ccpsqlist!=null){
             return "有图片";
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
        return "无图片";
    }
    /**
     * 重新取出视频
     */
    private String selectVideo(int did,int pid,int cid) {
        try {
            WhereBuilder d = WhereBuilder.b();
            d.and("taskid", "=", taskid);
            d.and("did", "=", did);
            d.and("pid", "=", pid);
            d.and("cid", "=", cid);
            List<CheckTheContentVideoTable> ccpsqlist  = dbManager.selector(CheckTheContentVideoTable.class).where(d).findAll();
            if (ccpsqlist!=null){
                return "有视频";
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
        return "无视频";
    }

    /**
     * 看这个任务的检查项目有没有都检查过
     *  false 有没有检查的 ；true 全部检查过
     */
    private boolean selectIsSave() {
        try {
            WhereBuilder d = WhereBuilder.b();
            d.and("taskid", "=", taskid);
            d.and("pid", "!=", 0);
            List<TaskDetailsTablePartBean> ctsqlist  = dbManager.selector(TaskDetailsTablePartBean.class).where(d).findAll();
            if (ctsqlist!=null){
                for (int db = 0; db < ctsqlist.size(); db++) {
                   if (ctsqlist.get(db).getStatus()==0||ctsqlist.get(db).getStatus()==1){
                    return false;
                   }
                }
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
        return true;
    }

    /**
     * 是否上传
     */
    private void InItDialog( ) {
        CommonDialog mDialog = new CommonDialog(TaskPreviewActivity.this, R.style.MyDialog);
        mDialog.setIcon(R.mipmap.reservoir_icon_logo);
        mDialog.setContent(",请确定检查完毕，是否上传报告？");
        mDialog.setLeftBtnText("取消");
        mDialog.setRightBtnText("确定");
        mDialog.setListener(new CommonDialog.DialogClickListener() {
            @Override
            public void onRightBtnClick(Dialog dialog) {
                String inspection = (String) PreferencesUtil.get(TaskPreviewActivity.this,InterfaceDefinition.PreferencesUser.REAL_NAME, "");
                CretePDFUtils cretePDFUtils=new CretePDFUtils(previewDatalist,tv_group_name_rigth.getText().toString().trim(),
                        ""+reseid,tasktype,previe_water_line.getText().toString().trim(),previe_weather.getText().toString().trim(),
                        previe_down_water.getText().toString().trim(),inspection,creator);
                String pdfFileName=cretePDFUtils.intbdData();
                if (pdfFileName.equals("failure")){
                    ToastUtil.TextToast("创建巡检报告文件失败");
                }else {
                    ToastUtil.TextToast("创建巡检报告文件成功,需要执行校验MD5,进行上传");
                    File file = new File( Environment.getExternalStorageDirectory() + "/"+pdfFileName);
                    Log.e("MD5", getFileMD5(file));
                    InsetFile(file);
                }
            }
            @Override
            public void onLeftBtnClick(Dialog dialog) {
                dialog.dismiss();
            }
        });
        mDialog.show();
    }

    /**
     * 上传文件pdf到文件服务器
     */
    private void InsetFile(File file) {
        Log.e("filegetPath",file.getPath());
        RequestParams params = new RequestParams(HttpUtil.UPLOAD_FILE(TaskPreviewActivity.this));
        Map<String, Object> stringObjectMap = new HashMap<>();
        Log.e("上传文件pdf", HttpUtil.UPLOAD_FILE(TaskPreviewActivity.this));
        stringObjectMap.put("file", file);
        MultipartBody body = new MultipartBody(stringObjectMap, "UTF-8");
        params.setRequestBody(body);
        x.http().post(params, new DefaultCommonCallBack(this, true) {
            @Override
            public void onSuccess(String json) {
                Log.e("上传文件pdf", json);
                Gson gson = new Gson();
                UploadFileBean uploadImgBean = gson.fromJson(json, UploadFileBean.class);
                if (uploadImgBean.getError() == 0) {
                    uploadPdfServise(uploadImgBean.getResult().get(0).getSave_path()+uploadImgBean.getResult().get(0).getSave_name());
                }else {
                    ToastUtil.TextToast("" + uploadImgBean.getMessage());
                }
            }
        });
    }

    /**
     * 上传pdf路径到数据服务器
     */
    private void uploadPdfServise(final String filepath) {
        Log.e("filepath",filepath);
        // 拼接路径
        String url = InterfaceDefinition.ISavePdf.IURL + PreferencesUtil.get(TaskPreviewActivity.this, InterfaceDefinition.PreferencesUser.Docking_CREDENTIALS, "");
        Log.e("拼接路径", url);
        JSONArray loctionList=new JSONArray();
        // 接口对接
        RequestParams mParams = new RequestParams(url);
        mParams.addBodyParameter(InterfaceDefinition.ISavePdf.TASK_ID, ""+taskid);
        mParams.addBodyParameter(InterfaceDefinition.ISavePdf.PDF, filepath);
        if (StringUtil.isJSONArrayNotEmpty(loctionList)){
            mParams.addBodyParameter(InterfaceDefinition.ISavePdf.TRAILS, loctionList.toString());
        }
        x.http().post(mParams, new DefaultCommonCallBack(TaskPreviewActivity.this, true) {
            @Override
            public void onSuccess(String json) {
                Log.e("上传pdf路径到数据服务器", json);
                UploadPdfServiseBean uploadPdf= new Gson().fromJson(json, UploadPdfServiseBean.class);
                if (InterfaceDefinition.IStatusCode.SUCCESS.equals(uploadPdf.getStatus())) {
                    String   user_id = (String) PreferencesUtil.get(TaskPreviewActivity.this,InterfaceDefinition.PreferencesUser.USER_ID, "");
                    WhereBuilder tb = WhereBuilder.b();
                    tb.and("id", "=", taskid);//条件
                    tb.and("uid", "=", user_id);
                    TaslListBeanTable taskbean  = null;
                    try {
                        taskbean = dbManager.selector(TaslListBeanTable.class).where(tb).findFirst();
                    } catch (DbException e) {
                        e.printStackTrace();
                    }
                    taskbean.setStatus("2");
                    try {
                        dbManager.update(taskbean);
                    } catch (DbException e) {
                        e.printStackTrace();
                        Log.e("DbException未开始状态修改", ""+e.toString());
                    }
                    EventBus.getDefault().post(new DataSynEvent());
                    finish();
                    AppManager.getAppManager().finishActivity(InspectActivity.class);
                } else if ("401".equals(uploadPdf.getStatus())) {
                    GetToken mGetToken = new GetToken(new IToken() {
                        @Override
                        public void success(int errtype) {
                            if (errtype == 0) {
                                uploadPdfServise(filepath);
                            }
                        }
                    });
                    mGetToken.getToken();
                } else {
                    ToastUtil.TextToast("" + uploadPdf.getMsg());
                }
            }
        });
    }
}
