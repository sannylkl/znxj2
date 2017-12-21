package com.jiarui.znxj.activity;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.jiarui.znxj.R;
import com.jiarui.znxj.adapter.FullyGridLayoutManager;
import com.jiarui.znxj.adapter.GridImageAdapter;
import com.jiarui.znxj.adapter.MyBaseExpandableListAdapter;
import com.jiarui.znxj.base.BaseActivity;
import com.jiarui.znxj.bean.Group;
import com.jiarui.znxj.bean.Item;
import com.jiarui.znxj.flow.FlowLayout;
import com.jiarui.znxj.flow.TagAdapter;
import com.jiarui.znxj.flow.TagFlowLayoutALL;
import com.jiarui.znxj.utils.AudioRecoderUtils;
import com.jiarui.znxj.utils.PopupWindowFactory;
import com.jiarui.znxj.utils.TimeUtils;
import com.jiarui.znxj.widget.MyExpandableListView;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.compress.Luban;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.tools.DebugUtil;

import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.jiarui.znxj.activity.BaseofDamActivity.VOICE_REQUEST_CODE;

/**
 * 汇报工情
 */
public class ProjectActivity extends BaseActivity implements PopupWindowFactory.onPopupDismiss {
    private ArrayList<Group> gData = null;
    private ArrayList<ArrayList<Item>> iData = null;
    private ArrayList<Item> lData = null;
    private MyBaseExpandableListAdapter myAdapter = null;
    @ViewInject(R.id.exlist_lol)
    private MyExpandableListView exlist_lol;//折叠listview
    @ViewInject(R.id.tv_group_name_rigth)
    private TextView tv_group_name_rigth;//设备运转情况
    @ViewInject(R.id.project_skmc)
    private TextView project_skmc;//水库名称
    @ViewInject(R.id.project_sw)
    private TextView project_sw;//水位
    @ViewInject(R.id.project_tq)
    private TextView project_tq;//天气
    @ViewInject(R.id.project_xysw)
    private TextView project_xysw;//下游水位
    @ViewInject(R.id.leave_message)
    private TextView leave_message;//描述
    @ViewInject(R.id.project_ly)
    private TextView project_ly;//录音
    private final static String TAG = ProjectActivity.class.getSimpleName();
    private List<LocalMedia> selectList = new ArrayList<>();
    @ViewInject(R.id.recycler)
    private RecyclerView recyclerView;
    private GridImageAdapter adapter;
    private int maxSelectNum = 8;
    private int chooseMode = PictureMimeType.ofAll();
    private int compressMode = PictureConfig.SYSTEM_COMPRESS_MODE;
    String pro = "1";
    @ViewInject(R.id.prohect_line)
    private TextView prohect_line;
    private PopupWindowFactory mPop;
    private String filePath = Environment.getExternalStorageDirectory() + "/YuYin/";
    private AudioRecoderUtils mAudioRecoderUtils;
    File file;
    int index = 0;
    private ImageView mImageView;
    private TextView mTextView;
    private Context context;
    int num;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project);
        x.view().inject(this);
        context = this;
        setTitle();
        mTvForTitle.setText("汇报工情");
        mImgvForRight.setText("保存");
        init();
        phon();
    }

    public void init() {
        //PopupWindow的布局文件
        final View view = View.inflate(this, R.layout.layout_microphone, null);
        mPop = new PopupWindowFactory(this, view);
        //PopupWindow布局文件里面的控件
        mImageView = (ImageView) view.findViewById(R.id.iv_recording_icon);
        mTextView = (TextView) view.findViewById(R.id.tv_recording_time);
        mAudioRecoderUtils = new AudioRecoderUtils();
        //录音回调
        mAudioRecoderUtils.setOnAudioStatusUpdateListener(new AudioRecoderUtils.OnAudioStatusUpdateListener() {

            //录音中....db为声音分贝，time为录音时长
            @Override
            public void onUpdate(double db, long time) {
                mImageView.getDrawable().setLevel((int) (3000 + 6000 * db / 100));
                mTextView.setText(TimeUtils.long2String(time));
            }

            //录音结束，filePath为保存路径
            @Override
            public void onStop(String filePath) {
                Toast.makeText(ProjectActivity.this, "录音保存在：" + filePath, Toast.LENGTH_SHORT).show();
                mTextView.setText(TimeUtils.long2String(0));
                MediaPlayer mediaPlayer = new MediaPlayer();
                try {
                    mediaPlayer.setDataSource(file.getPath());
                    mediaPlayer.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                mediaPlayer.start();
            }
        });
        getData();
        //6.0以上需要权限申请
        requestPermissions();
        mPop.setOnPopupDismiss(this);
        //数据准备
        gData = new ArrayList<Group>();
        iData = new ArrayList<ArrayList<Item>>();
        lData = new ArrayList<Item>();
        //AD组
        String gname[] = new String[]{"异常部位"};
        String rname[] = new String[]{"坝顶"};
        for (int i = 0; i < gname.length; i++) {
            gData.add(new Group(gname[i], rname[i]));
        }
        String content[] = new String[]{"坝体", "坝基和坝区", "溢洪道", "输、泄水洞(管)", "闸门及启闭器", "观测及通讯设备"};
        String mess[] = new String[]{"坝顶", "", "", "", "", ""};
        for (int j = 0; j < content.length; j++) {
            lData.add(new Item(content[j], mess[j]));
            iData.add(lData);
        }
        myAdapter = new MyBaseExpandableListAdapter(gData, iData, this);
        exlist_lol.setAdapter(myAdapter);
        exlist_lol.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i1, long l) {
                if (i1 == 0) {
                    change();
                } else if (i1 == 1) {
                    change();
                } else if (i1 == 2) {
                    change();
                } else if (i1 == 3) {
                    change();
                } else if (i1 == 4) {
                    change();
                } else if (i1 == 5) {
                    change();
                }
                return true;
            }
        });
    }

    /**
     * 机器运转情况
     */

    public void changepro() {
        pro = "1";
        View view = getLayoutInflater().inflate(R.layout.equipment_layout, null);
        final Dialog dialog = new Dialog(this, R.style.transparentFrameWindowStyle);
        dialog.setContentView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        RadioGroup personal_sex_rg = (RadioGroup) view.findViewById(R.id.personal_sex_rg);
        final RadioButton personal_sex_nan = (RadioButton) view.findViewById(R.id.personal_sex_nan);
        final RadioButton personal_sex_nv =  (RadioButton) view.findViewById(R.id.personal_sex_nv);
        TextView personal_wc = (TextView)  view.findViewById(R.id.personal_wc);
        personal_sex_nan.setChecked(true);
        if (tv_group_name_rigth.getText().toString().equals("正常")) {
            personal_sex_nan.setChecked(true);
        } else if (tv_group_name_rigth.getText().toString().equals("异常")) {
            personal_sex_nv.setChecked(true);
        }
        TextView personal_sex_commit = (TextView) view.findViewById(R.id.personal_sex_commit);
        personal_sex_rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.personal_sex_nan:
                        pro = "1";
                        break;
                    case R.id.personal_sex_nv:
                        pro = "2";
                        break;
                }
            }
        });
        personal_wc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (pro.equals("1")) {
                    tv_group_name_rigth.setText(personal_sex_nan.getText().toString());
                    pro = "1";
                    dialog.dismiss();
                } else if (pro.equals("2")) {
                    tv_group_name_rigth.setText(personal_sex_nv.getText().toString());
                    pro = "2";
                    dialog.dismiss();
                }
            }
        });
        Window window = dialog.getWindow();
        WindowManager.LayoutParams wl = window.getAttributes();
        wl.x = 0;
        // 以下这两句是为了保证按钮可以水平满屏
        wl.width = ViewGroup.LayoutParams.MATCH_PARENT;
        // 设置显示位置
        dialog.onWindowAttributesChanged(wl);
        // 设置点击外围解散
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }

    public void phon() {
        FullyGridLayoutManager manager = new FullyGridLayoutManager(ProjectActivity.this, 4, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        adapter = new GridImageAdapter(ProjectActivity.this, onAddPicClickListener);
        adapter.setList(selectList);
        adapter.setSelectMax(maxSelectNum);
        recyclerView.setAdapter(adapter);
    }

    private GridImageAdapter.onAddPicClickListener onAddPicClickListener = new GridImageAdapter.onAddPicClickListener() {
        @Override
        public void onAddPicClick(boolean isPv) {
            if (true) {
                // 进入相册 以下是例子：不需要的api可以不写
                PictureSelector.create(ProjectActivity.this)
                        .openGallery(chooseMode)// 全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                        .theme(R.style.picture_default_style)// 主题样式设置 具体参考 values/styles   用法：R.style.picture.white.style
                        .maxSelectNum(maxSelectNum)// 最大图片选择数量
                        .minSelectNum(1)// 最小选择数量
                        .imageSpanCount(4)// 每行显示个数
                        .selectionMode(true ?
                                PictureConfig.MULTIPLE : PictureConfig.SINGLE)// 多选 or 单选
                        .previewImage(true)// 是否可预览图片
                        .previewVideo(true)// 是否可预览视频
                        .enablePreviewAudio(true) // 是否可播放音频
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
                        .isGif(false)// 是否显示gif图片
                        .freeStyleCropEnabled(true)// 裁剪框是否可拖拽
                        .circleDimmedLayer(false)// 是否圆形裁剪
                        .showCropFrame(false)// 是否显示裁剪矩形边框 圆形裁剪时建议设为false
                        .showCropGrid(true)// 是否显示裁剪矩形网格 圆形裁剪时建议设为false
                        .openClickSound(false)// 是否开启点击声音
                        .selectionMedia(selectList)// 是否传入已选图片
                        .forResult(PictureConfig.CHOOSE_REQUEST);//结果回调onActivityResult code
            } else {
                // 单独拍照
                PictureSelector.create(ProjectActivity.this)
                        .openCamera(chooseMode)// 单独拍照，也可录像或也可音频 看你传入的类型是图片or视频
                        .theme(R.style.picture_default_style)// 主题样式设置 具体参考 values/styles
                        .maxSelectNum(maxSelectNum)// 最大图片选择数量
                        .minSelectNum(1)// 最小选择数量
                        .selectionMode(true ?
                                PictureConfig.MULTIPLE : PictureConfig.SINGLE)// 多选 or 单选
                        .previewImage(true)// 是否可预览图片
                        .previewVideo(true)// 是否可预览视频
                        .enablePreviewAudio(true) // 是否可播放音频
                        .compressGrade(Luban.THIRD_GEAR)// luban压缩档次，默认3档 Luban.FIRST_GEAR、Luban.CUSTOM_GEAR
                        .isCamera(true)// 是否显示拍照按钮
                        .enableCrop(false)// 是否裁剪
                        .compress(false)// 是否压缩
                        .compressMode(compressMode)//系统自带 or 鲁班压缩 PictureConfig.SYSTEM_COMPRESS_MODE or LUBAN_COMPRESS_MODE
                        .glideOverride(160, 160)// glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
                        .withAspectRatio(1, 1)// 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
                        .hideBottomControls(false ? false : true)// 是否显示uCrop工具栏，默认不显示
                        .isGif(false)// 是否显示gif图片
                        .freeStyleCropEnabled(true)// 裁剪框是否可拖拽
                        .circleDimmedLayer(false)// 是否圆形裁剪
                        .showCropFrame(true)// 是否显示裁剪矩形边框 圆形裁剪时建议设为false
                        .showCropGrid(true)// 是否显示裁剪矩形网格 圆形裁剪时建议设为false
                        .openClickSound(false)// 是否开启点击声音
                        .selectionMedia(selectList)// 是否传入已选图片
                        .previewEggs(false)//预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中)
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
                    // 图片选择结果回调
                    selectList = PictureSelector.obtainMultipleResult(data);
                    adapter.setList(selectList);
                    adapter.notifyDataSetChanged();
                    DebugUtil.i(TAG, "onActivityResult:" + selectList.size());
                    break;
            }
        }
    }

    @Event({R.id.common_title_left, R.id.progress_lin_1})
    private void onClick(View v) {
        switch (v.getId()) {
            case R.id.common_title_left:
                finish();
                break;
            case R.id.progress_lin_1:
                changepro();
                break;
            default:
                break;
        }
    }

    /**
     * 坝体弹框
     */
    public void change() {
        View view = getLayoutInflater().inflate(R.layout.flow_popug, null);
        Dialog dialog = new Dialog(this, R.style.transparentFrameWindowStyle);
        dialog.setContentView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        final TextView popug_qd = (TextView) view.findViewById(R.id.popug_qd);
        final TagFlowLayoutALL flow = (TagFlowLayoutALL) view.findViewById(R.id.flow);
        final String name[] = new String[]{"坝体", "坝基和坝区", "溢洪道"};
        flow.setAdapter(new TagAdapter(name) {
            @Override
            public View getView(FlowLayout parent, final int position, Object o) {
                TextView te = (TextView) LayoutInflater.from(ProjectActivity.this).inflate(R.layout.flow_popug_item, flow, false);
                te.setText(String.valueOf(o));
                return te;
            }
        });
        flow.setOnTagClickListener(new TagFlowLayoutALL.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                num=position;
                Toast.makeText(ProjectActivity.this, "" + name[position], Toast.LENGTH_SHORT).show();
                return true;
            }
        });
        popug_qd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ProjectActivity.this, "" + name[num], Toast.LENGTH_SHORT).show();
            }
        });
        Window window = dialog.getWindow();
        WindowManager.LayoutParams wl = window.getAttributes();
        wl.x = 0;
        // 以下这两句是为了保证按钮可以水平满屏
        wl.width = ViewGroup.LayoutParams.MATCH_PARENT;
        // 设置显示位置
        dialog.onWindowAttributesChanged(wl);
        // 设置点击外围解散
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
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

    private void getData() {
        File f = new File(filePath);
        // 列出所有文件
        File[] files = f.listFiles();
        // 将所有文件存入list中
        if (files != null) {
            int count = files.length;// 文件个数
            for (int i = 0; i < count; i++) {
                file = files[i];
            }
        }
    }

    public void StartListener() {
        project_ly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (index == 0) {
                    project_ly.setText("完成录制");
                    mPop.showAtLocation(prohect_line, Gravity.CENTER, 0, -140);
                    mAudioRecoderUtils.startRecord();
                    index = 1;
                } else if (index == 1) {
                    mPop.dismiss();
                    index = 0;
                }
            }
        });

    }

    @Override
    public void popupDismiss() {
        index = 0;
        project_ly.setText("开始录音");
        mAudioRecoderUtils.stopRecord();        //结束录音（保存录音文件）
    }
}
