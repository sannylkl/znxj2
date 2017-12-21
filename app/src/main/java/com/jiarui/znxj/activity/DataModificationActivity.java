package com.jiarui.znxj.activity;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.jiarui.znxj.Interface.IToken;
import com.jiarui.znxj.R;
import com.jiarui.znxj.base.BaseActivity;
import com.jiarui.znxj.bean.BaseBean;
import com.jiarui.znxj.bean.MineDataBean;
import com.jiarui.znxj.bean.MineHeaderPhoto;
import com.jiarui.znxj.constants.GetToken;
import com.jiarui.znxj.constants.InterfaceDefinition;
import com.jiarui.znxj.utils.DefaultCommonCallBack;
import com.jiarui.znxj.utils.DefaultDisplayImageOptions;
import com.jiarui.znxj.utils.HttpUtil;
import com.jiarui.znxj.utils.PreferencesUtil;
import com.jiarui.znxj.utils.StringUtil;
import com.jiarui.znxj.utils.ToastUtil;
import com.jiarui.znxj.widget.CircleImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.http.RequestParams;
import org.xutils.http.body.MultipartBody;
import org.xutils.x;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 资料修改
 */
public class DataModificationActivity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.data_left)
    TextView dataLeft;
    @Bind(R.id.data_ucon)
    CircleImageView dataUcon;//头像
    @Bind(R.id.data_name)
    TextView dataName;//名字
    Dialog dialog;
    @Bind(R.id.rela)
    RelativeLayout rela;
    @Bind(R.id.rela2)
    RelativeLayout rela2;//名字点击
    // 创建一个以当前时间为名称的文件
    private File tempFile = new File(Environment.getExternalStorageDirectory(),
            getPhotoFileName());
    public static final int PHOTO_REQUEST_TAKEPHOTO = 1;// 拍照
    public static final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择
    public static final int PHOTO_REQUEST_CUT = 3;// 结果
    private Uri mUri = null;
    private String real_name,header_photo = null;
    DisplayImageOptions mOptions;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_modification);
        ButterKnife.bind(this);
        //设置头像，昵称在onResume里
        mOptions = DefaultDisplayImageOptions
                .getDefaultDisplayImageOptionsRounded(DataModificationActivity.this, 360);
        header_photo= (String) PreferencesUtil.get(DataModificationActivity.this, InterfaceDefinition.PreferencesUser.USER_HEAD, "");
        ImageLoader.getInstance().displayImage(
                header_photo,
                dataUcon, mOptions);
        dataLeft.setOnClickListener(this);
        rela.setOnClickListener(this);
        rela2.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.data_left:
                //返回
                finish();
                break;
            case R.id.rela:
                //更换头像
                changephoto();
                break;
            case R.id.rela2:
                //修改名字
                gotoActivity(UpdateNameActivity.class);
                break;
        }
    }

    /**
     * 头像dialog
     */
    public void changephoto() {
        View view = getLayoutInflater().inflate(R.layout.photo_layout, null);
        Button paizhao = (Button) view.findViewById(R.id.mTvPaizhao);
        Button xuanqu = (Button) view.findViewById(R.id.mTvXuan);
        Button quxiao = (Button) view.findViewById(R.id.mTvmQuxiao);
        dialog = new Dialog(this, R.style.transparentFrameWindowStyle);
        dialog.setContentView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        Window window = dialog.getWindow();
        window.setWindowAnimations(R.style.main_menu_animstyle);
        WindowManager.LayoutParams wl = window.getAttributes();
        wl.x = 0;
        wl.y = getWindowManager().getDefaultDisplay().getHeight();
        // 以下这两句是为了保证按钮可以水平满屏
        wl.width = ViewGroup.LayoutParams.MATCH_PARENT;
        wl.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        // 设置显示位置
        dialog.onWindowAttributesChanged(wl);
        // 设置点击外围解散
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
        paizhao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                // 调用系统的拍照功能
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                  /*获取当前系统的android版本号*/
                int currentapiVersion = Build.VERSION.SDK_INT;
                if (currentapiVersion < 24) {
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tempFile));
                    startActivityForResult(intent, DataModificationActivity.PHOTO_REQUEST_TAKEPHOTO);
                } else {
                    ContentValues contentValues = new ContentValues(1);
                    contentValues.put(MediaStore.Images.Media.DATA, tempFile.getAbsolutePath());
                    Uri uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                    startActivityForResult(intent, DataModificationActivity.PHOTO_REQUEST_TAKEPHOTO);
                }
            }
        });
        xuanqu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getImageFromAlbum();
                dialog.dismiss();
            }
        });
        quxiao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {
            if (data != null && data.getData() != null) {
                mUri = data.getData();
            } else {
                mUri = Uri.fromFile(tempFile);
            }
            switch (requestCode) {
                // 拍照
                case DataModificationActivity.PHOTO_REQUEST_TAKEPHOTO:
                    startPhotoZoom(Uri.fromFile(tempFile), 360);
                    break;
                // 从手机相册中获取图片
                case DataModificationActivity.PHOTO_REQUEST_GALLERY:
                    startPhotoZoom(mUri, 360);
                    break;
                // 裁剪的图片
                case DataModificationActivity.PHOTO_REQUEST_CUT:
                    if (mUri != null) {
                        String path = "";
                        Bundle bundle = data.getExtras();
                        if (bundle != null) {
                            // 截图后获取的图片
                            Bitmap photo = bundle.getParcelable("data");
                            // 显示图片
                            dataUcon.setImageBitmap(photo);
                            // 保存截图，并返回截图的文件地址
                            path = saveFile(photo, getPhotoFileName());
                        }
                        setPicToView(path);//上传图片
                    }
                    break;
            }
            super.onActivityResult(requestCode, resultCode, data);
        }
    }


    private void startPhotoZoom(Uri uri, int size) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // crop为true是设置在开启的intent中设置显示的view可以剪裁
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX,outputY 是剪裁图片的宽高
        intent.putExtra("outputX", size);
        intent.putExtra("outputY", size);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, DataModificationActivity.PHOTO_REQUEST_CUT);
    }
    /**
     * 从手机相册中获取图片
     */
    protected void getImageFromAlbum() {
        dialog.dismiss();
        Intent intent = new Intent(Intent.ACTION_PICK, null);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                "image/*");
        startActivityForResult(intent, DataModificationActivity.PHOTO_REQUEST_GALLERY);
    }

    /**
     * 将截图文件 保存到本地，为了上传时使用
     *
     * @param bm
     * @param fileName
     * @return
     */
    public String saveFile(Bitmap bm, String fileName) {
        String path = "";
        try {
            path = Environment.getExternalStorageDirectory() + "/revoeye/";
            File dirFile = new File(path);
            if (!dirFile.exists()) {
                dirFile.mkdir();
            }
            File myCaptureFile = new File(path + fileName);
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));
            bm.compress(Bitmap.CompressFormat.JPEG, 80, bos);
            bos.flush();
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return path + fileName;
    }

    /**
     * 使用系统当前日期加以调整作为照片的名称
     */
    private String getPhotoFileName() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "'IMG'_yyyyMMdd_HHmmss");
        return dateFormat.format(date) +
                ".jpg";
    }

    //头像路径
   String imgpath =null;//
    MineHeaderPhoto mineHeaderPhoto=null;
    /**
     *  将进行剪裁后的图片显示到UI界面上，以及图片的上传
     */
    private void setPicToView(final String savePath) {
        RequestParams params = new RequestParams(HttpUtil.UPLOAD_IMG(this));
        Map<String, Object> stringObjectMap = new HashMap<>();
        Log.e("对接路径", HttpUtil.UPLOAD_IMG(this));
        //得到图片路径
        stringObjectMap.put("" + 0, new File(savePath));
        MultipartBody body = new MultipartBody(stringObjectMap, "UTF-8");
        params.setRequestBody(body);
        x.http().post(params, new DefaultCommonCallBack(this) {
            @Override
            public void onSuccess(String json) {
                Log.e("上传头像", json);
                mineHeaderPhoto = new Gson().fromJson(json, MineHeaderPhoto.class);
                if (InterfaceDefinition.IStatusCode.SUCCESS.equals(baseBean.getStatus())) {
                    for (int i = 0; i < mineHeaderPhoto.getResult().size(); i++) {
                        imgpath=  mineHeaderPhoto.getResult().get(i).getSave_path() + mineHeaderPhoto.getResult().get(i).getSave_name();
                    }
                    IUpadateInfo(imgpath);
                } else if ("401".equals(mineHeaderPhoto.getStatus())) {
                GetToken mGetToken = new GetToken(new IToken() {
                    @Override
                    public void success(int errtype) {
                        if (errtype == 0) {
                            setPicToView(savePath);
                        }
                    }
                });
                mGetToken.getToken();
            } else {
                ToastUtil.TextToast("" + mineHeaderPhoto.getMsg());
            }
            }
        });
    }

    BaseBean baseBean=null;
    /**
     * 更新个人资料
     */
    private void IUpadateInfo(final String avatar) {
        String url =  InterfaceDefinition.IUpadateInfo.IURL + PreferencesUtil.get(this, InterfaceDefinition.PreferencesUser.Docking_CREDENTIALS, "Docking_CREDENTIALS");
        Log.e("更新个人资料url", url + "");
        //接口对接
        RequestParams params = new RequestParams(url);
        params.addBodyParameter(InterfaceDefinition.IUpadateInfo.ID, PreferencesUtil.get(this, InterfaceDefinition.PreferencesUser.USER_ID, "") + "");
        params.addBodyParameter(InterfaceDefinition.IUpadateInfo.AVATAR, avatar);
        x.http().post(params, new DefaultCommonCallBack(this,true) {
            public void onSuccess(String arg0) {
                Log.e("更新个人头像", arg0);
                baseBean = new Gson().fromJson(arg0, BaseBean.class);
                if (InterfaceDefinition.IStatusCode.SUCCESS.equals(baseBean.getStatus())) {
                    PreferencesUtil.put(DataModificationActivity.this,InterfaceDefinition.PreferencesUser.USER_HEAD,HttpUtil.LOAD_URL(DataModificationActivity.this)+avatar);
                    finish();
                } else if ("401".equals(baseBean.getStatus())) {
                    GetToken mGetToken = new GetToken(new IToken() {
                        @Override
                        public void success(int errtype) {
                            if (errtype == 0) {
                                IUpadateInfo(avatar);
                            }
                        }
                    });
                    mGetToken.getToken();
                } else {
                    ToastUtil.TextToast("" + baseBean.getMsg());
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        real_name= (String) PreferencesUtil.get(DataModificationActivity.this, InterfaceDefinition.PreferencesUser.REAL_NAME, "未设置");
        dataName.setText(real_name);
    }
}
