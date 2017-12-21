package com.jiarui.znxj.activity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import com.jiarui.znxj.R;
import com.jiarui.znxj.adapter.FullyGridLayoutManager;
import com.jiarui.znxj.adapter.GridImageAdapter;
import com.jiarui.znxj.adapter.GridImagePreviewAdapter;
import com.jiarui.znxj.adapter.MemberTypeAdapter;
import com.jiarui.znxj.application.AppContext;
import com.jiarui.znxj.base.BaseActivity;
import com.jiarui.znxj.bean.AAA;
import com.jiarui.znxj.bean.CheckTheContentAbnormalsTable;
import com.jiarui.znxj.bean.CheckTheContentPhotoTable;
import com.jiarui.znxj.bean.CheckTheContentTable;
import com.jiarui.znxj.bean.CheckTheContentVideoTable;
import com.jiarui.znxj.bean.TaskDetailsTableLoctionBean;
import com.jiarui.znxj.bean.TaskDetailsTablePartBean;
import com.jiarui.znxj.bean.TaskPreviewData;
import com.jiarui.znxj.flow.TagAdapter;
import com.jiarui.znxj.flow.TagFlowLayoutALL;
import com.jiarui.znxj.pdfhtml.AssetsCopyTOSDcard;
import com.jiarui.znxj.pdfhtml.MyFont;
import com.jiarui.znxj.utils.PreferencesUtil;
import com.jiarui.znxj.utils.StringUtil;
import com.jiarui.znxj.utils.ToastUtil;
import com.jiarui.znxj.utils.VoicePlayingBgUtil;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;

import org.xutils.DbManager;
import org.xutils.db.sqlite.WhereBuilder;
import org.xutils.ex.DbException;
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
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 检查项任务预览
 */
public class TaskPreviewCheckItemActivity extends BaseActivity {
    /*异常问题*/
    @ViewInject(R.id.rules_ycwt)
    TextView rules_ycwt;
    private String ccAlist="";
    /*设备运行状况*/
    @ViewInject(R.id.rules_sbzk)
    private TextView rules_sbzk;
    /*重复问题*/
    @ViewInject(R.id.rules_cfwt)
    private TextView rules_cfwt;
    /*其它描述*/
    @ViewInject(R.id.rules_qtms)
    private TextView rules_qtms;
    /*图片选择*/
    @ViewInject(R.id.previe_recycler_photo)
    private RecyclerView recyclerView;
    private GridImagePreviewAdapter adapter;
    private List<LocalMedia> selectList = new ArrayList<>();
    /*视频选择*/
    @ViewInject(R.id.previe_recycler_video)
    private RecyclerView recyclerView_video;
    private GridImagePreviewAdapter adapter_video;
    private List<LocalMedia> selectvideoList = new ArrayList<>();
    /*语音播放*/
    @ViewInject(R.id.img_play)
    private ImageView img_plays;
    VoicePlayingBgUtil  playBgUtil;
    boolean ispaly=false;
    String audioFilePath;
    Handler handler ;
    /*检查像名称*/
    String name;
    /*taskid(任务id),did（点位id）,pid(检查部位id),cid(检查项目id)*/
    int taskid,did,pid,cid;
    /*数据库*/
    DbManager dbManager = null;//AppContext.getInstance().getdbManager()

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_task_preview_citem_);
        x.view().inject(this);
        setTitle();
        dbManager = x.getDb(AppContext.getConfig());
        phon();
        videoSelect();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            taskid = bundle.getInt("taskid");//任务id
            did = bundle.getInt("did");//水库id
            pid= bundle.getInt("pid");//路线id
            cid= bundle.getInt("cid");//路
            name= bundle.getString("name");//检查项目名称
            mTvForTitle.setText(name);
            Log.e("previewChecktaskid",""+taskid+"\tdid"+""+did+"\tpid"+""+pid+"\tcid"+""+cid);
        }
        //重新取出检查内容数据
        selectContent();
        //重新取出检查内容异常数据
        if (ccdataselect!=null){
            selectAbnormal();
        }else{
            rules_ycwt.setText("检查项数据丢失无法知道有无异常");
        }
        //获取图片表
        selectPhoto();
        //获取视频表
        selectVideo();
    }

    /**
     * 图片选择控件初始化
     */
    public void phon() {
        FullyGridLayoutManager manager = new FullyGridLayoutManager(TaskPreviewCheckItemActivity.this, 4, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        adapter = new GridImagePreviewAdapter(TaskPreviewCheckItemActivity.this);
        adapter.setList(selectList);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new GridImagePreviewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                if (selectList.size() > 0) {

                    LocalMedia media = selectList.get(position);
                    String pictureType = media.getPictureType();
                    int mediaType = PictureMimeType.pictureToVideo(pictureType);
                    switch (mediaType) {
                        case 1:
                            PictureSelector.create(TaskPreviewCheckItemActivity.this).externalPicturePreview(position, selectList);
                            break;
                        case 2:
                            // 预览视频
                            PictureSelector.create(TaskPreviewCheckItemActivity.this).externalPictureVideo(media.getPath());
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
        FullyGridLayoutManager manager = new FullyGridLayoutManager(TaskPreviewCheckItemActivity.this, 4, GridLayoutManager.VERTICAL, false);
        recyclerView_video.setLayoutManager(manager);
        adapter_video = new GridImagePreviewAdapter(TaskPreviewCheckItemActivity.this);
        adapter_video.setList(selectvideoList);
        recyclerView_video.setAdapter(adapter_video);
        adapter_video.setOnItemClickListener(new GridImagePreviewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                if (selectvideoList.size() > 0) {
                    LocalMedia media = selectvideoList.get(position);
                    String pictureType = media.getPictureType();
                    int mediaType = PictureMimeType.pictureToVideo(pictureType);
                    switch (mediaType) {
                        case 1:
                            PictureSelector.create(TaskPreviewCheckItemActivity.this).externalPicturePreview(position, selectList);
                            break;
                        case 2:
                            // 预览视频
                            PictureSelector.create(TaskPreviewCheckItemActivity.this).externalPictureVideo(media.getPath());
                            break;
                    }
                }
            }
        });
    }



    @Event({R.id.common_title_left, R.id.img_play})
    private void onClick(View v) {
        switch (v.getId()) {
            case R.id.common_title_left:
                finish();
                break;
            case R.id.img_play:
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
            default:
                break;
        }
    }



    CheckTheContentTable ccdataselect;
    /**
     * 初始化本身数据
     */
    private void selectContent() {
        try {
            WhereBuilder d = WhereBuilder.b();
            d.and("taskid", "=", taskid);
            d.and("did", "=", did);
            d.and("pid", "=", pid);
            d.and("cid", "=", cid);
            ccdataselect=dbManager.selector(CheckTheContentTable.class).where(d).findFirst();
            if (ccdataselect!=null){
                Log.e("issave" , ""+ccdataselect.issave());
                if (ccdataselect.issave()==true){ //表里修改并保存过这条数据
                /*正常异常RadioGroup*/
                    if (ccdataselect.isStatus()==true){
                        rules_sbzk.setText("正常");
                    }else {
                        rules_sbzk.setText("异常");
                    }
                /*设备问题adioGroup*/
                    if (ccdataselect.isEquipmentstatus()==true){
                        rules_sbzk.setText("正常");
                    }else {
                        rules_sbzk.setText("异常");
                    }
                /*重复问题RadioGroup(是就是有重复问题=false，否就是无重复问题=true)*/
                    if (ccdataselect.isRepertstatus()==true){
                        rules_cfwt.setText("有");
                    }else {
                        rules_cfwt.setText("无");
                    }
                /*其它描述*/
                if (StringUtil.isNotEmpty(ccdataselect.getOthercontent())){
                    rules_qtms.setText(ccdataselect.getOthercontent());
                }else{
                    rules_qtms.setText("未填写");
                }
                /*语音播放按钮*/
                    if (StringUtil.isNotEmpty(ccdataselect.getYpath())){
                        img_plays.setVisibility(View.VISIBLE);
                        //这有个bug。这个路径是固定的，一开始录音就是重新创建一份文件名称一致的录音文件
                        //这个路径要是保存一个就是一个，不能固定
                        audioFilePath=ccdataselect.getYpath();
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
                rules_ycwt.setText(ccAlist);
            }else{
                rules_ycwt.setText("无异常数据");
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

}
