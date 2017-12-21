package com.jiarui.znxj.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.IdRes;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jiarui.znxj.Interface.IToken;
import com.jiarui.znxj.R;
import com.jiarui.znxj.adapter.FullyGridLayoutManager;
import com.jiarui.znxj.adapter.GridImageAdapter;
import com.jiarui.znxj.application.AppContext;
import com.jiarui.znxj.base.BaseActivity;
import com.jiarui.znxj.bean.CheckTheContent;
import com.jiarui.znxj.bean.CheckTheContentAbnormalsTable;
import com.jiarui.znxj.bean.CheckTheContentPhotoTable;
import com.jiarui.znxj.bean.CheckTheContentTable;
import com.jiarui.znxj.bean.CheckTheContentVideoTable;
import com.jiarui.znxj.bean.DataSynEvent;
import com.jiarui.znxj.bean.TaskDetailsTableLoctionBean;
import com.jiarui.znxj.bean.TaskDetailsTablePartBean;
import com.jiarui.znxj.bean.TaslListBeanTable;
import com.jiarui.znxj.bean.UploadFileBean;
import com.jiarui.znxj.bean.UploadPdfServiseBean;
import com.jiarui.znxj.constants.GetToken;
import com.jiarui.znxj.constants.InterfaceDefinition;
import com.jiarui.znxj.flow.FlowLayout;
import com.jiarui.znxj.flow.TagAdapter;
import com.jiarui.znxj.flow.TagFlowLayoutALL;
import com.jiarui.znxj.grobal.AppManager;
import com.jiarui.znxj.utils.AudioRecoderUtils;
import com.jiarui.znxj.utils.DefaultCommonCallBack;
import com.jiarui.znxj.utils.HttpUtil;
import com.jiarui.znxj.utils.LogUtil;
import com.jiarui.znxj.utils.PopupWindowFactory;
import com.jiarui.znxj.utils.PreferencesUtil;
import com.jiarui.znxj.utils.StringUtil;
import com.jiarui.znxj.utils.TimeUtils;
import com.jiarui.znxj.utils.ToastUtil;
import com.jiarui.znxj.utils.VoicePlayingBgUtil;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.compress.Luban;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.xutils.DbManager;
import org.xutils.db.sqlite.WhereBuilder;
import org.xutils.ex.DbException;
import org.xutils.http.RequestParams;
import org.xutils.http.body.MultipartBody;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class BaseofDamActivity extends BaseActivity implements PopupWindowFactory.onPopupDismiss  , RadioGroup.OnCheckedChangeListener ,IToken{
    static final int VOICE_REQUEST_CODE = 66;
    /*整体布局*/
    @ViewInject(R.id.r1)
    private LinearLayout rl;
    /*检查内容*/
    @ViewInject(R.id.tv_check_content)
    TextView tv_check_content;
    /*正常异常RadioGroup*/
    @ViewInject(R.id.baseof_grouprad)
    private RadioGroup baseof_grouprad;
    @ViewInject(R.id.baseof_rad1)
    private RadioButton baseof_rad1;//正常
    @ViewInject(R.id.baseof_rad2)
    private RadioButton baseof_rad2;//异常
    /*设备问题adioGroup*/
    @ViewInject(R.id.equipment_status_rg)
    private RadioGroup equipment_status_rg;
    @ViewInject(R.id.baseof_zc)
    private RadioButton baseof_zc;//设备运转正常
    @ViewInject(R.id.baseof_yc)
    private RadioButton baseof_yc;//设备运转异常
    /*重复问题RadioGroup*/
    @ViewInject(R.id.repeat_status_rg)
    private RadioGroup repeat_status_rg;
    @ViewInject(R.id.baseof_s)
    private RadioButton baseof_s;//重复问题是
    @ViewInject(R.id.baseof_f)
    private RadioButton baseof_f;//重复问题否
    /*隐藏输入文本框*/
    @ViewInject(R.id.baseof_linear_gone)
    private LinearLayout baseof_linear_gone;
    /*异常选择*/
    @ViewInject(R.id.flow)
    TagFlowLayoutALL flow;
    private TagAdapter tagAdater;
    private List<CheckTheContentAbnormalsTable> ccAlist;
    /*图片选择*/
    @ViewInject(R.id.recycler)
    private RecyclerView recyclerView;
    private GridImageAdapter adapter;
    /*已选择图片*/
    private List<LocalMedia> selectList = new ArrayList<>();
    /*视频选择*/
    @ViewInject(R.id.recycler_video)
    private RecyclerView recyclerView_video;
    private GridImageAdapter adapter_video;
    /*已选择图片*/
    private List<LocalMedia> selectvideoList = new ArrayList<>();
    /*开始录音*/
    @ViewInject(R.id.baseof_ksly)
    private TextView baseof_ksly;
    /*语音播放*/
    @ViewInject(R.id.img_play)
    private ImageView img_plays;
    /*其他描述*/
    @ViewInject(R.id.leave_message)
    private EditText leave_message;

    /*图片/视频可选择数量*/
    private int maxSelectNum = 8;
    /*选择器模式*/
    private int chooseMode = PictureMimeType.ofImage();
    /*压缩模式*/
    private int compressMode = PictureConfig.SYSTEM_COMPRESS_MODE;

    /*选择图片/视频的状态*/
    private boolean ispv;

    private PopupWindowFactory mPop;
    private String filePath = Environment.getExternalStorageDirectory() + "/YuYin/";
    private AudioRecoderUtils mAudioRecoderUtils;
    String audioFilePath;
    /*index=0录音未开始，1完成录制*/
    int index = 0;
    private ImageView mImageView;
    private TextView mTextView;
    private Context context;
    int taskid,did,pid,cid;
    String itme_name;
    int  work = 0, repeat = 0;
    //语音播放
    VoicePlayingBgUtil  playBgUtil;
    boolean ispaly=false;
    Handler handler ;
    /*后台数据源*/
    private List<CheckTheContent.ResultBean.AbnormalsBean> ccADatalist;
    CheckTheContent checkTheContent=null;
    /*数据库*/
    DbManager dbManager = null;//AppContext.getInstance().getdbManager()
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_baseof_dam);
        x.view().inject(this);
        setTitle();
        context = this;
        dbManager = x.getDb(AppContext.getConfig());
//                try {//清除检查内容
//            dbManager.delete(CheckTheContentTable.class);
//        } catch (DbException e) {
//            e.printStackTrace();
//        }
//        try {//清除检查内容异常数据
//            dbManager.delete(CheckTheContentAbnormalsTable.class);
//        } catch (DbException e) {
//            e.printStackTrace();
//        }
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            taskid=bundle.getInt("taskid");//区分任务
            did= bundle.getInt("did");//区分点位
            pid = bundle.getInt("pid");//区分当前部位
            cid = bundle.getInt("cid");//区分当前项
            Log.e("bundle:taskid",""+taskid+"\tdid"+""+did+"\tpid"+""+pid+"\tcid"+""+cid);
            itme_name = bundle.getString("name");
            mTvForTitle.setText(itme_name);
            mImgvForRight.setText("保存");

        //异常正常选择
        baseof_grouprad.setOnCheckedChangeListener(this);
        equipment_status_rg.setOnCheckedChangeListener(this);
        repeat_status_rg.setOnCheckedChangeListener(this);
        /*后台返回数据*/
        ccADatalist = new ArrayList<CheckTheContent.ResultBean.AbnormalsBean>();
        //异常内容选择
        ccAlist= new ArrayList<CheckTheContentAbnormalsTable>();
        tagAdater=new TagAdapter(ccAlist) {
            @Override
            public View getView(FlowLayout parent, int position, Object o) {
                TextView te = (TextView) LayoutInflater.from(BaseofDamActivity.this).inflate(R.layout.flow_popug_item, flow, false);
                CheckTheContentAbnormalsTable it= ccAlist.get(position);
                te.setText(it.getName());
                return te;
            }
//            @Override
//            public boolean setSelected(int position, Object o) {
//                return o.equals(bwlist.get(0));
//            }
        };
        flow.setAdapter(tagAdater);
         flow.setOnTagClickListener(new TagFlowLayoutALL.OnTagClickListener() {
             @Override
             public boolean onTagClick(View view, int position, FlowLayout parent) {
                 Set<Integer> dhjs = flow.getSelectedList();
                 for (Iterator it = dhjs.iterator(); it.hasNext(); ) {
                     Integer index = (Integer) it.next();
                     if (position == index) {
                         Log.e("position == index","\tposition:"+position+"\tindex:"+index);
                     }
                 }
                 return true;
             }
         });
        //照片选择初始化
        phon();
        //视频选择初始化
        videoSelect();
        //获取图片表
         selectPhoto();
        //获取视频表
        selectVideo();
        //检查项与检查内容接口
        taskItemContentData();
        //录音PopupWindow的布局文件
        final View view = View.inflate(this, R.layout.layout_microphone, null);
        mPop = new PopupWindowFactory(this, view);
        /*PopupWindow布局文件里面的控件*/
        mImageView = (ImageView) view.findViewById(R.id.iv_recording_icon);
        mTextView = (TextView) view.findViewById(R.id.tv_recording_time);
        mAudioRecoderUtils = new AudioRecoderUtils();
        /*录音回调*/
        mAudioRecoderUtils.setOnAudioStatusUpdateListener(new AudioRecoderUtils.OnAudioStatusUpdateListener() {
            /*录音中....db为声音分贝，time为录音时长*/
            @Override
            public void onUpdate(double db, long time) {
                mImageView.getDrawable().setLevel((int) (3000 + 6000 * db / 100));
                mTextView.setText(TimeUtils.long2String(time));
            }
            /*录音结束，filePath为保存路径*/
            @Override
            public void onStop(String filePath) {
                audioFilePath=filePath;
                Log.e("filePath保存路径",""+filePath);
                mTextView.setText(TimeUtils.long2String(0));
            }
        });
        //6.0以上需要权限申请
        requestPermissions();
        mPop.setOnPopupDismiss(this);
        }else{
            Log.e("bundle", "nullnullnull");
        }
    }
    /*异常数据*/
    String question="";
    @Event({R.id.common_title_left, R.id.img_play, R.id.common_title_right})
    private void onClick(View v) {
        switch (v.getId()) {
            case R.id.common_title_left:
                finish();
                break;
            case R.id.img_play://播放语音
                //判断播放工具类是不是为空
                    if (playBgUtil==null){
//                        判断handler是不是为空
        if (handler==null){
            handler =new Handler();
        }
        playBgUtil = new VoicePlayingBgUtil(handler);
    }
    //判断是否播放完毕
                if (ispaly==false){
    //判断文件路径为不为空
    if (audioFilePath!=null){
        Log.e("audioFilePath", ""+audioFilePath);
            MediaPlayer mediaPlayer = new MediaPlayer();
            try {
                mediaPlayer.setDataSource(audioFilePath);
                mediaPlayer.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
            mediaPlayer.start();
            ispaly=true;
            playBgUtil.setImageView(img_plays);
            playBgUtil.voicePlay();
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    Log.e("tag播放完毕", "播放完毕");
                    ispaly=false;
                    //根据需要添加自己的代码。。。
                    playBgUtil.stopPlay();
                }
            });
        }
    }
                break;
            case R.id.common_title_right://保存
                /*内容表*/
                //其他描述
                String other_content=leave_message.getText().toString().trim();
                if (!baseof_rad1.isChecked()&&!baseof_rad2.isChecked()){
                    ToastUtil.TextToast("请选择有无异常");
                    return;
                }
                if (!baseof_zc.isChecked()&&!baseof_yc.isChecked()){
                    ToastUtil.TextToast("请选择设备运转情况");
                    return;
                }
                if (!baseof_s.isChecked()&&!baseof_f.isChecked()){
                    ToastUtil.TextToast("请选择重复问题情况");
                    return;
                }
                /*查询内容表为了保存数据*/
                CheckTheContentTable ccdatafind = null;
                try {
                    WhereBuilder d = WhereBuilder.b();
                    d.and("taskid", "=", taskid);
                    d.and("did", "=", did);
                    d.and("pid", "=", pid);
                    //cid 是检查项id，后台的关联是用part_id表示。
                    d.and("cid", "=", cid);
                    ccdatafind=dbManager.selector(CheckTheContentTable.class).where(d).findFirst();
                } catch (DbException e) {
                    e.printStackTrace();
                }

                 /*修改内容异常数据的状态*/
                 Set<Integer> selectccalist=flow.getSelectedList();
                if (selectccalist != null && selectccalist.size() > 0){
                    CheckTheContentAbnormalsTable  ccadataupadate = null;
                    Iterator<Integer> it = selectccalist.iterator();
                    while(it.hasNext()){//判断是否有下一个
                        int preIndex = it.next();
                        Log.e("it.next()",""+preIndex);
                        //取出表里对应的数据
                        try {
                            WhereBuilder d = WhereBuilder.b();
                            d.and("taskid", "=", taskid);
                            d.and("did", "=", did);
                            d.and("pid", "=", pid);
                            d.and("cid", "=", cid);
                            d.and("content_id", "=", ccAlist.get(preIndex).getContent_id());
                            ccadataupadate=dbManager.selector(CheckTheContentAbnormalsTable.class).where(d).findFirst();
                        } catch (DbException e) {
                            e.printStackTrace();
                        }
                        //修改这个对应的数据
                        if (ccadataupadate!=null){
                            ccadataupadate.setStatus(true);
                            question=question+ccadataupadate.getName()+",";
                            Log.e("upadate.isStatus()",""+ccadataupadate.isStatus());
                            try {
                                dbManager.update(ccadataupadate);
                            } catch (DbException e) {
                                e.printStackTrace();
                            }
                        }else{
                            Log.e("ccadataupadate",""+ "无无无无无无无无无");
                        }
                    }
                }else {
                    Log.e("it.next()",""+ "无无无无无无无无无");
                }
                /*查询内容异常数据表为了判断选没有选择异常数据*/
                List<CheckTheContentAbnormalsTable> ccadatalist = null;
                if (ccdatafind!=null){
                try {
                    WhereBuilder d = WhereBuilder.b();
                    d.and("taskid", "=", taskid);
                    d.and("did", "=", did);
                    d.and("pid", "=", pid);
                    d.and("cid", "=", cid);
                    d.and("content_id", "=", ccdatafind.getContent_id());
                    ccadatalist=dbManager.selector(CheckTheContentAbnormalsTable.class).where(d).findAll();
                } catch (DbException e) {
                    e.printStackTrace();
                }
                }
                //异常数据，选择了异常就必须选择异常数据
                if (baseof_rad2.isChecked()){
                    boolean ischeck=false;
                    if (StringUtil.isListNotEmpty(ccadatalist)){
                    for (int ccA = 0; ccA < ccadatalist.size() ; ccA++) {
                        if (ccadatalist.get(ccA).isStatus()==true){
                            ischeck=true;
                            break;
                        }
                    }
                    }else{//没有异常数据就可以不选择
                        ischeck=true;
                    }
                    if (ischeck==false){
                        ToastUtil.TextToast("请选择异常数据");
                        return;
                    }
                }
                 /*保存内容表数据*/
                if (ccdatafind!=null){
                    //设置检查内容状态值
                    if (baseof_rad1.isChecked()){
                        ccdatafind.setStatus(true);
                    }else{
                        ccdatafind.setStatus(false);
                    }
                    //设置检查内容设备运转情况
                    if (baseof_zc.isChecked()){
                        ccdatafind.setEquipmentstatus(true);
                    }else{
                        ccdatafind.setEquipmentstatus(false);
                    }
                    //设置重复问题情况(是就是有重复问题=false，否就是无重复问题=true)
                    if (baseof_s.isChecked()){
                        ccdatafind.setRepertstatus(false);
                    }else{
                        ccdatafind.setRepertstatus(true);
                    }
                    //其它描述
                    if (StringUtil.isNotEmpty(other_content)){
                        ccdatafind.setOthercontent(other_content);
                    }
                    //语音路径
                    if (StringUtil.isNotEmpty(audioFilePath)){
                        ccdatafind.setYpath(audioFilePath);
                    }
                    //是否保存过
                    ccdatafind.setIssave(true);
                }
                try {
                    dbManager.update(ccdatafind);
                } catch (DbException e) {
                    e.printStackTrace();
                }
                  /*保存图片表和视频表之前要先清除*/
                 try {//清除图片表内
                 dbManager.delete(CheckTheContentPhotoTable.class);
                  } catch (DbException e) {
                     e.printStackTrace();
                 }
                   try {//清除视频表
                  dbManager.delete(CheckTheContentVideoTable.class);
                  } catch (DbException e) {
                    e.printStackTrace();
                  }
                  /*保存图片表数据*/
                  if (StringUtil.isListNotEmpty(selectList)){
                      for (int i = 0; i <selectList.size() ; i++) {
                          CheckTheContentPhotoTable ccpsqlist=new CheckTheContentPhotoTable(taskid,did,pid,cid,selectList.get(i).getPath());
                          try {
                              dbManager.save(ccpsqlist);
                          } catch (DbException e) {
                              e.printStackTrace();
                          }
                      }
                  }
                  /*保存视频表数据*/
                if (StringUtil.isListNotEmpty(selectvideoList)){
                    for (int i = 0; i <selectvideoList.size() ; i++) {
                        CheckTheContentVideoTable ccpsqlist=new CheckTheContentVideoTable(taskid,did,pid,cid,selectvideoList.get(i).getPath());
                        try {
                            dbManager.save(ccpsqlist);
                        } catch (DbException e) {
                            e.printStackTrace();
                        }
                    }
                }

                /*修改项目检查的检查项*/
                //查到这个对应的检查内容
                    try {
                        WhereBuilder cdw = WhereBuilder.b();
                        cdw.and("taskid", "=", taskid);
                        cdw.and("did", "=", did);
                        cdw.and("pid", "=", pid);
                        cdw.and("cid", "=", cid);
                        CheckTheContentTable   cctsq = dbManager.selector(CheckTheContentTable.class).where(cdw).findFirst();
                        if (cctsq!=null){
                            //存放数据到适配器的数据源里
                            //是否有选中异常
                            int ischeck=0;
//                         是否有检查过
                                //检查内容状态
                                if (cctsq.isStatus()==true){
                                    ischeck=2;
                                }else{
                                    ischeck=3;
                                }
                                //设备运转情况+重复问题
                                if (cctsq.isEquipmentstatus()==false||cctsq.isRepertstatus()==false){
                                    ischeck=3;
                                }
                            WhereBuilder Pd = WhereBuilder.b();
                            Pd.and("taskid", "=", taskid);
                            Pd.and("did", "=", did);
                            Pd.and("pid", "=", pid);
                            Pd.and("cid", "=", cid);
                            TaskDetailsTablePartBean psqlist  = dbManager.selector(TaskDetailsTablePartBean.class).where(Pd).findFirst();
                            if (psqlist!=null){
                                psqlist.setStatus(ischeck);
                            }
                            dbManager.update(psqlist);
                        }
                    } catch (DbException e) {
                        e.printStackTrace();
                    }


                 /*修改项目检查的检查部位*/
                //查到这个对应的检查部位里所有的检查项、
                // 存的时候部位的cid就是这个后台返回数据里的id。
                // 这个部位的id就是部位里所有检查项的pid。
                //这个部位的pid是0；
                try {
                WhereBuilder icd = WhereBuilder.b();
                icd.and("taskid", "=", taskid);
                icd.and("did", "=", did);
                icd.and("pid", "=", pid);
                List<TaskDetailsTablePartBean> bdsqlist  = dbManager.selector(TaskDetailsTablePartBean.class).where(icd).findAll();
                for (int i = 0; i <bdsqlist.size() ; i++) {
                    TaskDetailsTablePartBean itmebaen=bdsqlist.get(i);
                    if (itmebaen.getStatus()==3){
                        WhereBuilder bd = WhereBuilder.b();
                        bd.and("taskid", "=", taskid);
                        bd.and("did", "=", did);
                        bd.and("pid", "=", 0);
                        bd.and("cid", "=", pid);
                        //查到这个对应的检查部位
                        TaskDetailsTablePartBean bdbean  = dbManager.selector(TaskDetailsTablePartBean.class).where(bd).findFirst();
                        bdbean.setStatus(3);
                        dbManager.update(bdbean);
                        break;
                    }
                }
        } catch (DbException e) {
            e.printStackTrace();
        }

                 /*修改项目检查的点位状态*/
                //查到这个对应的点位里所有的检查部位
                //存的时候点位的id就是部位的did
                try {
                WhereBuilder didw = WhereBuilder.b();
                didw.and("taskid", "=", taskid);
                didw.and("did", "=", did);
                didw.and("pid", "=", 0);
                List<TaskDetailsTablePartBean> bdsqlist  = dbManager.selector(TaskDetailsTablePartBean.class).where(didw).findAll();
                for (int i = 0; i <bdsqlist.size() ; i++) {
                    TaskDetailsTablePartBean itmebaen=bdsqlist.get(i);
                    if (itmebaen.getStatus()==3){
                        WhereBuilder bd = WhereBuilder.b();
                        bd.and("taskid", "=", taskid);
                        bd.and("did", "=", did);
                        //查到这个对应的检查部位
                        TaskDetailsTableLoctionBean bdbean  = dbManager.selector(TaskDetailsTableLoctionBean.class).where(bd).findFirst();
                        bdbean.setStatus(3);
                        dbManager.update(bdbean);
                        break;
                    }
                }
                } catch (DbException e) {
                    e.printStackTrace();
                }

                //点击保存，根据图片+视频+音频的情况逐级上传,每一级的开端是自己，先是判断从那开始上传
                if (StringUtil.isListNotEmpty(selectList)){//图片不为空开始上传图片，为空就是上传视频
                    InsetImg();
                }else if (StringUtil.isListNotEmpty(selectvideoList)){//视频不为空开始上传视频，为空就上传音频
                    InsetVideo();
                }else if (StringUtil.isNotEmpty(urAudio)){//音频不为空开始上传音频，为空到此结束直接上传检查项数据
                    InsetAudio();
                }else{//上传检查项数据
                    uploadISaceCheckServise();
                }
                break;
            default:
                break;
        }
    }
    /**
     * 异常选择
     */
    @Override
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
        switch(checkedId){
            case R.id.baseof_rad1:
                baseof_linear_gone.setVisibility(View.GONE);
                break;
            case R.id.baseof_rad2:
                baseof_linear_gone.setVisibility(View.VISIBLE);
                break;
//            case R.id.baseof_zc://可判断保存一次后要禁止修改的话，直接设置控件不可点击即可。保存按钮不放出
//                break;
//            case R.id.baseof_yc:
//                break;
//            case R.id.baseof_s:
//                break;
//            case R.id.baseof_f:
//                break;
        }
    }
    /**
     * 图片选择控件初始化
     */
    public void phon() {
        FullyGridLayoutManager manager = new FullyGridLayoutManager(BaseofDamActivity.this, 4, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        adapter = new GridImageAdapter(BaseofDamActivity.this, onAddPicClickListener,true);
        adapter.setList(selectList);
        adapter.setSelectMax(maxSelectNum);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new GridImageAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                if (selectList.size() > 0) {
                    LocalMedia media = selectList.get(position);
                    String pictureType = media.getPictureType();
                    int mediaType = PictureMimeType.pictureToVideo(pictureType);
                    switch (mediaType) {
                        case 1:
                            // 预览图片 可自定长按保存路径
                            //PictureSelector.create(MainActivity.this).externalPicturePreview(position, "/custom_file", selectList);
//                            PictureSelector.create(MainActivity.this).externalPicturePreview(position, selectList);
                            break;
                        case 2:
                            // 预览视频
//                            PictureSelector.create(MainActivity.this).externalPictureVideo(media.getPath());
                            break;
                        case 3:
                            // 预览音频
//                            PictureSelector.create(MainActivity.this).externalPictureAudio(media.getPath());
                            break;
                    }
                }
            }
        });
    }

    /**
     * 视频选择控件初始化
     */
    public void videoSelect(){
        FullyGridLayoutManager manager = new FullyGridLayoutManager(BaseofDamActivity.this, 4, GridLayoutManager.VERTICAL, false);
        recyclerView_video.setLayoutManager(manager);
        adapter_video = new GridImageAdapter(BaseofDamActivity.this, onAddPicClickListener,false);
        adapter_video.setList(selectvideoList);
        adapter_video.setSelectMax(maxSelectNum);
        recyclerView_video.setAdapter(adapter_video);
        adapter_video.setOnItemClickListener(new GridImageAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                if (selectvideoList.size() > 0) {
                    LocalMedia media = selectvideoList.get(position);
                    String pictureType = media.getPictureType();
                    int mediaType = PictureMimeType.pictureToVideo(pictureType);
                    switch (mediaType) {
                        case 1:
                            // 预览图片 可自定长按保存路径
                            //PictureSelector.create(MainActivity.this).externalPicturePreview(position, "/custom_file", selectList);
//                            PictureSelector.create(MainActivity.this).externalPicturePreview(position, selectList);
                            break;
                        case 2:
                            // 预览视频
//                            PictureSelector.create(MainActivity.this).externalPictureVideo(media.getPath());
                            break;
                        case 3:
                            // 预览音频
//                            PictureSelector.create(MainActivity.this).externalPictureAudio(media.getPath());
                            break;
                    }
                }
            }
        });
    }

    private GridImageAdapter.onAddPicClickListener onAddPicClickListener = new GridImageAdapter.onAddPicClickListener() {
        @Override
        public void onAddPicClick(boolean isPv) {
            ispv=isPv;
            Log.e("ispv",""+ispv);
            //这个只是给个参考而已，用来自己定义区分的
            if (isPv) {
                // 进入选择图片模式
                chooseMode=PictureMimeType.ofImage();
                PictureSelector.create(BaseofDamActivity.this)
                    .openGallery(chooseMode)// 全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                    .theme(R.style.picture_default_style)// 主题样式设置 具体参考 values/styles   用法：R.style.picture.white.style
                    .maxSelectNum(maxSelectNum)// 最大图片选择数量
                    .minSelectNum(1)// 最小选择数量
                    .imageSpanCount(4)// 每行显示个数
                    .selectionMode(true ? PictureConfig.MULTIPLE : PictureConfig.SINGLE)// 多选 or 单选
                    .previewImage(true)// 是否可预览图片
                    .previewVideo(true)// 是否可预览视频
                    .compressGrade(Luban.THIRD_GEAR)// luban压缩档次，默认3档 Luban.FIRST_GEAR、Luban.CUSTOM_GEAR
                    .isCamera(true)// 是否显示拍照按钮
                    .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
                    //.setOutputCameraPath("/CustomPath")// 自定义拍照保存路径
                    .enableCrop(false)// 是否裁剪
                    .compress(false)// 是否压缩
                    .compressMode(compressMode)//系统自带 or 鲁班压缩 PictureConfig.SYSTEM_COMPRESS_MODE or LUBAN_COMPRESS_MODE
                    //.sizeMultiplier(0.5f)// glide 加载图片大小 0~1之间 如设置 .glideOverride()无效
                    .glideOverride(160, 160)// glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
                    .withAspectRatio(1, 1)// 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
                    .hideBottomControls(false ? false : true)// 是否显示uCrop工具栏，默认不显示
                    .selectionMedia(selectList)// 是否传入已选图片
                    .forResult(PictureConfig.CHOOSE_REQUEST);//结果回调onActivityResult code
            } else {
                // 进入选择视频模式
                chooseMode=PictureMimeType.ofVideo();
                PictureSelector.create(BaseofDamActivity.this)
                        .openGallery(chooseMode)// 全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                        .theme(R.style.picture_default_style)// 主题样式设置 具体参考 values/styles   用法：R.style.picture.white.style
                        .maxSelectNum(maxSelectNum)// 最大图片选择数量
                        .minSelectNum(1)// 最小选择数量
                        .imageSpanCount(4)// 每行显示个数
                        .selectionMode(true ? PictureConfig.MULTIPLE : PictureConfig.SINGLE)// 多选 or 单选
                        .previewImage(true)// 是否可预览图片
                        .previewVideo(true)// 是否可预览视频
                        .compressGrade(Luban.THIRD_GEAR)// luban压缩档次，默认3档 Luban.FIRST_GEAR、Luban.CUSTOM_GEAR
                        .isCamera(true)// 是否显示拍照按钮
                        .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
                        //.setOutputCameraPath("/CustomPath")// 自定义拍照保存路径
                        .enableCrop(false)// 是否裁剪
                        .compress(false)// 是否压缩
                        .compressMode(compressMode)//系统自带 or 鲁班压缩 PictureConfig.SYSTEM_COMPRESS_MODE or LUBAN_COMPRESS_MODE
                        //.sizeMultiplier(0.5f)// glide 加载图片大小 0~1之间 如设置 .glideOverride()无效
                        .glideOverride(160, 160)// glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
                        .withAspectRatio(1, 1)// 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
                        .hideBottomControls(false ? false : true)// 是否显示uCrop工具栏，默认不显示
                        .selectionMedia(selectvideoList)// 是否传入已选图片
                        .recordVideoSecond(15)
                        .forResult(PictureConfig.CHOOSE_REQUEST);//结果回调onActivityResult code
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    // 例如 LocalMedia 里面返回三种path
                    // 1.media.getPath(); 为原图path
                    // 2.media.getCutPath();为裁剪后path，需判断media.isCut();是否为true
                    // 3.media.getCompressPath();为压缩后path，需判断media.isCompressed();是否为true
                    // 如果裁剪并压缩了，已取压缩路径为准，因为是先裁剪后压缩的
                    if (ispv){
                        // 图片选择结果回调
                        selectList = PictureSelector.obtainMultipleResult(data);
                        adapter.setList(selectList);
                        adapter.notifyDataSetChanged();
                    }else{
                        // 图片选择结果回调
                        selectvideoList = PictureSelector.obtainMultipleResult(data);
                        adapter_video.setList(selectvideoList);
                        adapter_video.notifyDataSetChanged();
                    }
                    break;
            }
        }
    }

    /**
     * 开启扫描之前判断权限是否打开
     */
    private void requestPermissions() {
        //判断是否开启摄像头权限
        if ((ContextCompat.checkSelfPermission(context,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) &&
                (ContextCompat.checkSelfPermission(context,
                        Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED)
                ) {
            StartListener();
            //判断是否开启语音权限
        } else {
            //请求获取摄像头权限
            ActivityCompat.requestPermissions((Activity) context,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO}, VOICE_REQUEST_CODE);
        }
    }

    /**
     * 录制音频
     */
    public void StartListener() {
        baseof_ksly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (index == 0) {//开始录音
                    if (StringUtil.isNotEmpty(audioFilePath)){
                        mAudioRecoderUtils.setItmefilePath(audioFilePath);
                    }else {
                        mAudioRecoderUtils.setItmefilePath(getAudioFileName());
                    }
                    Log.e("开始录音","开始录音开始录音开始录音");
                    baseof_ksly.setText("完成录制");
                    mPop.showAtLocation(rl, Gravity.CENTER, 0, -140);
                    mAudioRecoderUtils.startRecord();
                    index = 1;
                    img_plays.setVisibility(View.GONE);
                } else if (index == 1) {//完成录制
                    mPop.dismiss();
                    index = 0;
                }
            }
        });

    }
    @Override
    public void popupDismiss() {
        index = 0;
        baseof_ksly.setText("开始录音");
        mAudioRecoderUtils.stopRecord();        //结束录音（保存录音文件）
        img_plays.setVisibility(View.VISIBLE);
    }

    /**
     * 检查内容与异常分类
     */
    private void taskItemContentData() {
        String url = InterfaceDefinition.ITaskItemContent.IURL + PreferencesUtil.get(this, InterfaceDefinition.PreferencesUser.Docking_CREDENTIALS, "");
        // 接口对接
        RequestParams mParams = new RequestParams(url);
        mParams.addBodyParameter(InterfaceDefinition.ITaskItemContent.ITEMID, ""+cid);
        x.http().get(mParams, new DefaultCommonCallBack(BaseofDamActivity.this, true) {
            @Override
            public void onSuccess(String json) {
                LogUtil.e("检查内容与异常分类", json);
                checkTheContent = new Gson().fromJson(json, CheckTheContent.class);
                if (InterfaceDefinition.IStatusCode.SUCCESS.equals(checkTheContent.getStatus())) {
//                    //先取出所有的已发布的数据网络数据
                    ccADatalist.addAll(checkTheContent.getResult().getAbnormals());
                    Log.e("taskid",""+taskid+"\tdid"+""+did+"\tpid"+""+pid+"\tcPart_id"+""+checkTheContent.getResult().getPart_id());
                    //增加内容表
                    try {
                        WhereBuilder d = WhereBuilder.b();
                        d.and("taskid", "=", taskid);
                        d.and("did", "=", did);
                        d.and("pid", "=", pid);
                        //cid 是检查项id，后台的关联是用part_id表示。
                        d.and("cid", "=", checkTheContent.getResult().getPart_id());
                        d.and("content_id", "=", checkTheContent.getResult().getId());
                        CheckTheContentTable ccdata=dbManager.selector(CheckTheContentTable.class).where(d).findFirst();
                        if (ccdata==null){
                            Log.e("ccdata", "无值");
                            //检查项内容表增加
                            CheckTheContentTable checkTheContentTable = new CheckTheContentTable(
                                    taskid,did ,pid, checkTheContent.getResult().getPart_id(),
                                    checkTheContent.getResult().getId(), "", checkTheContent.getResult().getContent(), false,
                                    false,false,"",false);
                            try {
                                    dbManager.save(checkTheContentTable);
                            } catch (DbException e) {
                                e.printStackTrace();
                                Log.e("检查项内容表增加异常", e.toString());
                            }
                        }else{
                            Log.e("内容表", "有数据");
                        }
                    } catch (DbException e) {
                        e.printStackTrace();
                    }
                    Log.e("增加内容表的异常数据", "增加内容表的异常数据");
                    //增加内容表的异常数据
                    for (int i = 0; i < ccADatalist.size(); i++) {
                        CheckTheContent.ResultBean.AbnormalsBean  abnormalsBean= ccADatalist.get(i);
                        CheckTheContentAbnormalsTable checkTheContentAbnormalsTable=null;
                        try {
                            WhereBuilder cb = WhereBuilder.b();
                            cb.and("taskid", "=", taskid);
                            cb.and("did", "=", did);
                            cb.and("pid", "=", pid);
                            cb.and("cid", "=", checkTheContent.getResult().getPart_id());
                            cb.and("content_id", "=", abnormalsBean.getContent_id());
                            cb.and("abnormals_id", "=", abnormalsBean.getId());
                            checkTheContentAbnormalsTable = dbManager.selector(CheckTheContentAbnormalsTable.class).where(cb).findFirst();
                            if (checkTheContentAbnormalsTable==null){
                                Log.e("AbnormalsTable", "无值");
                            //增加内容表的异常数据
                                CheckTheContentAbnormalsTable ccadata = new CheckTheContentAbnormalsTable(
                                        taskid,did ,pid, checkTheContent.getResult().getPart_id(),
                                        abnormalsBean.getContent_id(), abnormalsBean.getId(),false,abnormalsBean.getName());
                                try {
                                        dbManager.save(ccadata);
                                } catch (DbException e) {
                                    e.printStackTrace();
                                    Log.e("增加内容表的异常数据异常" + i, e.toString());
                                }
                            }else{
                                Log.e("增加内容表的异常数据", "有数据");
                            }
                        } catch (DbException e) {
                            e.printStackTrace();
                        }
                    }

//                  重新取出检查内容数据
                    selectContent();
//                  重新取出检查内容异常数据
                    if (ccdataselect!=null){
                        selectAbnormal();
                    }else{
                        Log.e("ccdata" , "ccdatanullccdatanull");
                    }
                } else if ("401".equals(checkTheContent.getStatus())) {//token获取失败
                    GetToken mGetToken = new GetToken(BaseofDamActivity.this);
                    mGetToken.getToken();
                } else {//走接口失败
                    ToastUtil.TextToast(checkTheContent.getMsg());
                    //                  重新取出检查内容数据
                    selectContent();
                    if (ccdataselect!=null){
                        selectAbnormal();
                    }else{
                        Log.e("ccdata" , "ccdatanullccdatanull");
                    }
                }
            }
            @Override
            public void onError(Throwable arg0, boolean arg1) {
                Log.e("onError",arg0.toString());
                //                  重新取出检查内容数据
                selectContent();
                //                  重新取出检查内容异常数据
                if (ccdataselect!=null){
                    selectAbnormal();
                }else{
                    Log.e("ccdataonError" , "nullnullnull");
                }
            }
        });
    }

    @Override
    public void success(int errtype) {
        if (errtype==0){
            taskItemContentData();
        }
    }
    CheckTheContentTable ccdataselect;
    /**
     * 重新取出内容表（不能用涉及到后台网络返回的数据查询）
     */
    private void selectContent() {
        try {
            WhereBuilder d = WhereBuilder.b();
            d.and("taskid", "=", taskid);
            d.and("did", "=", did);
            d.and("pid", "=", pid);
//            cid 是检查项id，后台的关联是用part_id表示。
            d.and("cid", "=", cid);
            ccdataselect=dbManager.selector(CheckTheContentTable.class).where(d).findFirst();
            if (ccdataselect!=null){
                 /*检查内容*/
                tv_check_content.setText("检查内容:"+ccdataselect.getContent());
                Log.e("issave" , ""+ccdataselect.issave());
                if (ccdataselect.issave()==true){ //表里修改并保存过这条数据
                /*正常异常RadioGroup*/
                if (ccdataselect.isStatus()==true){
                    baseof_linear_gone.setVisibility(View.GONE);
                    baseof_rad1.setChecked(true);
                }else {
                    baseof_linear_gone.setVisibility(View.VISIBLE);
                    baseof_rad2.setChecked(true);
                }
                /*设备问题adioGroup*/
                if (ccdataselect.isEquipmentstatus()==true){
                    baseof_zc.setChecked(true);
                }else {
                    baseof_yc.setChecked(true);
                }
                /*重复问题RadioGroup(是就是有重复问题=false，否就是无重复问题=true)*/
                if (ccdataselect.isRepertstatus()==true){
                    baseof_f.setChecked(true);
                }else {
                    baseof_s.setChecked(true);
                }
                /*其它描述*/
                leave_message.setText(ccdataselect.getOthercontent());
                /*语音播放按钮*/
                if (StringUtil.isNotEmpty(ccdataselect.getYpath())){
                    img_plays.setVisibility(View.VISIBLE);
                    //这有个bug。这个路径是固定的，一开始录音就是重新创建一份文件名称一致的录音文件
                    //这个路径要是保存一个就是一个，不能固定
                    audioFilePath=ccdataselect.getYpath();
//                    File f = new File(ccdataselect.getYpath());
//                    fileaudio=f;
                }else {
                    img_plays.setVisibility(View.GONE);
                }
                }
            }else{
                Log.e("ccdataselect" , "nullnullnull");
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    /**
     * 重新取出异常数据
     */
    private void selectAbnormal() {
        ccAlist.clear();
        try {
        WhereBuilder d = WhereBuilder.b();
            d.and("taskid", "=", taskid);
            d.and("did", "=", did);
            d.and("pid", "=", pid);
            d.and("cid", "=", ccdataselect.getCid());
            d.and("content_id", "=", ccdataselect.getContent_id());
        List<CheckTheContentAbnormalsTable> bwsqlist  = dbManager.selector(CheckTheContentAbnormalsTable.class).where(d).findAll();
        Set<Integer> set=new HashSet();
        if (bwsqlist!=null){
            //存放数据到适配器的数据源里
            for (int db = 0; db < bwsqlist.size(); db++) {
                CheckTheContentAbnormalsTable ccadata = new CheckTheContentAbnormalsTable(
                        bwsqlist.get(db).getTaskid(), bwsqlist.get(db).getDid() , bwsqlist.get(db).getPid(),
                        bwsqlist.get(db).getCid(),  bwsqlist.get(db).getContent_id(),  bwsqlist.get(db).getAbnormals_id(),
                        bwsqlist.get(db).isStatus(), bwsqlist.get(db).getName());
                ccAlist.add(ccadata);
                if (ccadata.isStatus()==true){
                    Log.e("ccadata.isStatus()","truetruetruetrue");
                    set.add(db);
                }else{
                    Log.e("ccadata.isStatus()","falsefalsefalse");
                }
            }
//            flow.setSL
            tagAdater.setSelectedList(set);
        }
    } catch (DbException e) {
        e.printStackTrace();
    }
    }

    /**
     * 重新取出图片
     */
    private void selectPhoto() {
        try {
            WhereBuilder d = WhereBuilder.b();
            d.and("taskid", "=", taskid);
            d.and("did", "=", did);
            d.and("pid", "=", pid);
            d.and("cid", "=", cid);
            List<CheckTheContentPhotoTable> ccpsqlist  = dbManager.selector(CheckTheContentPhotoTable.class).where(d).findAll();
            if (ccpsqlist!=null){
                //存放数据到适配器的数据源里
                for (int db = 0; db < ccpsqlist.size(); db++) {
                    LocalMedia localMedia=new LocalMedia();
                    localMedia.setPath(ccpsqlist.get(db).getPhoto_path());
                    //为了区分视频和图片，为了可以选择图片和视频，不给值默认值就是image/jpeg
                    localMedia.setPictureType(createImageType(ccpsqlist.get(db).getPhoto_path()));
                    selectList.add(localMedia);
                }
                adapter.notifyDataSetChanged();
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
    }
    /**
     * 重新取出视频
     */
    private void selectVideo() {
        try {
            WhereBuilder d = WhereBuilder.b();
            d.and("taskid", "=", taskid);
            d.and("did", "=", did);
            d.and("pid", "=", pid);
            d.and("cid", "=", cid);
            List<CheckTheContentVideoTable> ccpsqlist  = dbManager.selector(CheckTheContentVideoTable.class).where(d).findAll();
            if (ccpsqlist!=null){
                //存放数据到适配器的数据源里
                for (int db = 0; db < ccpsqlist.size(); db++) {
                    LocalMedia localMedia=new LocalMedia();
                    localMedia.setPath(ccpsqlist.get(db).getVideo_path());
                    localMedia.setPictureType(createVideoType(ccpsqlist.get(db).getVideo_path()));
                    selectvideoList.add(localMedia);
                }
                adapter_video.notifyDataSetChanged();
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    /**
     * 建立图片类型
     * @param path 图片路径
     * @return
     */
    public static String createImageType(String path) {
        try {
            if (!TextUtils.isEmpty(path)) {
                File file = new File(path);
                String fileName = file.getName();
                int last = fileName.lastIndexOf(".") + 1;
                String temp = fileName.substring(last, fileName.length());
                return "image/" + temp;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "image/jpeg";
        }
        return "image/jpeg";
    }
    /**
     * 建立视频类型
     * @param path 视频路径
     * @return
     */
    public static String createVideoType(String path) {
        try {
            if (!TextUtils.isEmpty(path)) {
                File file = new File(path);
                String fileName = file.getName();
                int last = fileName.lastIndexOf(".") + 1;
                String temp = fileName.substring(last, fileName.length());
                return "video/" + temp;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "video/mp4";
        }
        return "video/mp4";
    }

    /**
     * 使用系统当前日期加以调整作为音频的名称
     */
    private String getAudioFileName() {
        //文件存储路径，AudioRecoderUtils建立对象就会判断有无并创建好。
        String avdiopath=Environment.getExternalStorageDirectory() + "/YuYin/";
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "'Audio'_yyyyMMdd_HHmmss");
        return avdiopath +taskid+dateFormat.format(date)+ ".amr";
    }

    //图片数组
    ArrayList<String> urStringlist;
    //视频数组
    ArrayList<String> urVideoStringlist;
    //音频路径
    String urAudio;
    /**
     * 上传文件图片到图片服务器
     */
    private void InsetImg() {
        RequestParams params = new RequestParams(HttpUtil.UPLOAD_IMG(BaseofDamActivity.this));
        Map<String, Object> stringObjectMap = new HashMap<>();
        for (int i = 0; i <selectList.size() ; i++) {
            stringObjectMap.put(""+i,new File(selectList.get(i).getPath()));
        }
        MultipartBody body = new MultipartBody(stringObjectMap, "UTF-8");
        params.setRequestBody(body);
        x.http().post(params, new DefaultCommonCallBack(this, true) {
            @Override
            public void onSuccess(String json) {
                Log.e("上传图片", json);
                Gson gson = new Gson();
                UploadFileBean uploadImgBean = gson.fromJson(json, UploadFileBean.class);
                if (uploadImgBean.getError() == 0) {
                    urStringlist = new ArrayList<String>();
                    for (int i = 0; i < uploadImgBean.getResult().size(); i++) {
                        String beanPath =
                                uploadImgBean.getResult().get(i).getSave_path()
                                        + uploadImgBean.getResult().get(i).getSave_name();
                        urStringlist.add("\"" + beanPath + "\"");
                    }
                    if (StringUtil.isListNotEmpty(selectvideoList)){
                        InsetVideo();
                    }else if (StringUtil.isNotEmpty(urAudio)){
                        InsetAudio();
                    }else {
                        uploadISaceCheckServise();
                    }
                }else {
                    ToastUtil.TextToast("" + uploadImgBean.getMessage());
                }
            }
        });
    }

    /**
     * 上传视频到文件服务器
     */
    private void InsetVideo() {
        RequestParams params = new RequestParams(HttpUtil.UPLOAD_FILE(BaseofDamActivity.this));
        Map<String, Object> stringObjectMap = new HashMap<>();
        for (int i = 0; i <selectvideoList.size() ; i++) {
            stringObjectMap.put(""+i,new File(selectvideoList.get(i).getPath()));
        }
        stringObjectMap.put("file", new File(audioFilePath));
        MultipartBody body = new MultipartBody(stringObjectMap, "UTF-8");
        params.setRequestBody(body);
        x.http().post(params, new DefaultCommonCallBack(this, true) {
            @Override
            public void onSuccess(String json) {
                Log.e("上传视频", json);
                Gson gson = new Gson();
                UploadFileBean uploadImgBean = gson.fromJson(json, UploadFileBean.class);
                if (uploadImgBean.getError() == 0) {
                    urVideoStringlist = new ArrayList<String>();
                    for (int i = 0; i < uploadImgBean.getResult().size(); i++) {
                        String beanPath =
                                uploadImgBean.getResult().get(i).getSave_path()
                                        + uploadImgBean.getResult().get(i).getSave_name();
                        urVideoStringlist.add("\"" + beanPath + "\"");
                    }
                    if (StringUtil.isNotEmpty(urAudio)){
                        InsetAudio();
                    }else {
                        uploadISaceCheckServise();
                    }
                }else {
                    ToastUtil.TextToast("" + uploadImgBean.getMessage());
                }
            }
        });
    }

    /**
     * 上传音频到文件服务器
     */
    private void InsetAudio() {
        RequestParams params = new RequestParams(HttpUtil.UPLOAD_FILE(BaseofDamActivity.this));
        Map<String, Object> stringObjectMap = new HashMap<>();
        stringObjectMap.put("file", new File(audioFilePath));
        MultipartBody body = new MultipartBody(stringObjectMap, "UTF-8");
        params.setRequestBody(body);
        x.http().post(params, new DefaultCommonCallBack(this, true) {
            @Override
            public void onSuccess(String json) {
                Log.e("音频", json);
                Gson gson = new Gson();
                UploadFileBean uploadImgBean = gson.fromJson(json, UploadFileBean.class);
                if (uploadImgBean.getError() == 0) {
                    urAudio=  uploadImgBean.getResult().get(0).getSave_path() + uploadImgBean.getResult().get(0).getSave_name();
                    uploadISaceCheckServise();
                }else {
                    ToastUtil.TextToast("" + uploadImgBean.getMessage());
                }
            }
        });
    }

    /**
     * 上传检查项数据
     */
    private void uploadISaceCheckServise() {
        String uid = (String) PreferencesUtil.get(BaseofDamActivity.this, InterfaceDefinition.PreferencesUser.USER_ID, "");
        // 拼接路径
        String url = InterfaceDefinition.ISaceCheck.IURL + PreferencesUtil.get(BaseofDamActivity.this, InterfaceDefinition.PreferencesUser.Docking_CREDENTIALS, "");
        // 接口对接
        RequestParams mParams = new RequestParams(url);
        mParams.addBodyParameter(InterfaceDefinition.ISaceCheck.UID, ""+uid);
        if (StringUtil.isListNotEmpty(urStringlist)) {
            Log.e("IMAGES",urStringlist.toString());
            mParams.addBodyParameter(InterfaceDefinition.ISaceCheck.IMAGES, urStringlist.toString());
        }
        if (StringUtil.isListNotEmpty(urVideoStringlist)) {
            Log.e("VIDEOS",urVideoStringlist.toString());
            mParams.addBodyParameter(InterfaceDefinition.ISaceCheck.VIDEOS, urVideoStringlist.toString());
        }
        if (StringUtil.isNotEmpty(urAudio)) {
            Log.e("AUDIOS",urAudio);
            mParams.addBodyParameter(InterfaceDefinition.ISaceCheck.AUDIOS, ""+urAudio);
        }
        mParams.addBodyParameter(InterfaceDefinition.ISaceCheck.TASK_ID, ""+taskid);
        mParams.addBodyParameter(InterfaceDefinition.ISaceCheck.ITEMID,""+ cid);
        if (baseof_rad1.isChecked()){//是否异常,正常就不用上传异常数据+其他描述
            mParams.addBodyParameter(InterfaceDefinition.ISaceCheck.NORMAL,"1");
        }else{
            mParams.addBodyParameter(InterfaceDefinition.ISaceCheck.NORMAL,"0");
            if (StringUtil.isNotEmpty(question)){//异常问题
                mParams.addBodyParameter(InterfaceDefinition.ISaceCheck.QUESTION,question);
            }
            if (StringUtil.isNotEmpty(leave_message.getText().toString().trim())){//其他描述
                mParams.addBodyParameter(InterfaceDefinition.ISaceCheck.REMARK,leave_message.getText().toString().trim());
            }
        }
        if (baseof_zc.isChecked()){//设备运转
            mParams.addBodyParameter(InterfaceDefinition.ISaceCheck.WORK,"1");
        }else{
            mParams.addBodyParameter(InterfaceDefinition.ISaceCheck.WORK,"0");
        }

        if (baseof_s.isChecked()){////是否重复
            mParams.addBodyParameter(InterfaceDefinition.ISaceCheck.REPEAT,"0");
        }else{
            mParams.addBodyParameter(InterfaceDefinition.ISaceCheck.REPEAT,"1");
        }
        x.http().post(mParams, new DefaultCommonCallBack(BaseofDamActivity.this, true) {
            @Override
            public void onSuccess(String json) {
                Log.e("上传pdf路径到数据服务器", json);
                UploadPdfServiseBean uploadPdf= new Gson().fromJson(json, UploadPdfServiseBean.class);
                if (InterfaceDefinition.IStatusCode.SUCCESS.equals(uploadPdf.getStatus())) {
                    String   user_id = (String) PreferencesUtil.get(BaseofDamActivity.this,InterfaceDefinition.PreferencesUser.USER_ID, "");
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
                                uploadISaceCheckServise();
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


