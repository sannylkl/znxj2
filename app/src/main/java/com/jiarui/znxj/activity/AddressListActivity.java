package com.jiarui.znxj.activity;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jiarui.znxj.Interface.IToken;
import com.jiarui.znxj.R;
import com.jiarui.znxj.adapter.CommonAdapter;
import com.jiarui.znxj.base.BaseActivity;
import com.jiarui.znxj.bean.AddressListBean;
import com.jiarui.znxj.bean.InitBean;
import com.jiarui.znxj.bean.PhoneList;
import com.jiarui.znxj.bean.TaslListBean;
import com.jiarui.znxj.constants.GetToken;
import com.jiarui.znxj.constants.InterfaceDefinition;
import com.jiarui.znxj.holder.ViewHolder;
import com.jiarui.znxj.utils.CommonDialog;
import com.jiarui.znxj.utils.DateUtil;
import com.jiarui.znxj.utils.DefaultDisplayImageOptions;
import com.jiarui.znxj.utils.HttpUtil;
import com.jiarui.znxj.utils.LogUtil;
import com.jiarui.znxj.utils.PacketUtil;
import com.jiarui.znxj.utils.PreferencesUtil;
import com.jiarui.znxj.utils.StringUtil;
import com.jiarui.znxj.widget.AutoListView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

import org.json.JSONArray;
import org.json.JSONObject;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 通讯录
 */
public class AddressListActivity extends BaseActivityRefreshLoad implements View.OnClickListener  ,IToken {

    @Bind(R.id.common_title_left)
    TextView commonTitleLeft;    //返回
    @Bind(R.id.address_edit)
    EditText addressEdit;//输入框

    // 每页显示多少条数据
    private int mPageSize = 10;

    // 第几页
    private int mPage = 1;

    // 数据源
    private ArrayList< PhoneList.ResultBean.DataBean> mData;
    PhoneList phoneListBean;
    // 公共适配器
    private CommonAdapter< PhoneList.ResultBean.DataBean> mAdapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_list);
        ButterKnife.bind(this);
        setTitle();
        mTvForTitle.setText("通讯录");
        commonTitleLeft.setOnClickListener(this);
        setRefreshLoadListView();
        mData = new ArrayList< PhoneList.ResultBean.DataBean>();
        initAutoListView();
        LoadResultData(AutoListView.REFRESH);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.common_title_left:
                //返回
                finish();
                break;
        }
    }

    private void initAutoListView() {
        final DisplayImageOptions mOptions = DefaultDisplayImageOptions
                .getDefaultDisplayImageOptionsRounded(AddressListActivity.this, 360);
        mAdapter = new CommonAdapter< PhoneList.ResultBean.DataBean>(AddressListActivity.this, mData, R.layout.address_list_item) {
            @Override
            public void convert(ViewHolder mHolder,  PhoneList.ResultBean.DataBean item, int position) {
                mHolder.setText(R.id.address_item_name, item.getName());
                mHolder.setText(R.id.address_item_phone, item.getPhone());
                mHolder.setImageByUrl(R.id.address_item_icon,
                        HttpUtil.LOAD_URL(AddressListActivity.this) + item.getAvatar(),
                        AddressListActivity.this, mOptions);
            }
        };
        mAutoListView.setAdapter(mAdapter);
        mAutoListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                InItDialog(mData.get(position).getPhone());
//                String Task_id = mData.get(position).getTask_id();
//                Bundle bundle = new Bundle();
//                bundle.putString("id", Task_id);
                // gotoActivity(bundle, TaskXiangQingActivity.class);
            }
        });
    }

    @Override
    void clearResultData() {
        mData.clear();
    }

    @Override
    void disposeResultData(JSONObject arg0) {
        LogUtil.e("通讯录数据",arg0.toString());
        phoneListBean = new Gson().fromJson(arg0.toString(), PhoneList.class);
        if (InterfaceDefinition.IStatusCode.SUCCESS.equals(phoneListBean.getStatus())) {
             //当前页数大于总页数时（后台数据没有数据）
            if (mPage > phoneListBean.getResult().getPageCount()) {
                EmptyView.setVisibility(View.VISIBLE);
                mAutoListView.setResultSize(0);
                //当前页数于小于总页数时（后台还有数据）
            } else if (mPage <  phoneListBean.getResult().getPageCount()) {
                mData.addAll(phoneListBean.getResult().getData());
                mAutoListView.setResultSize(phoneListBean.getResult().getData().size());
                EmptyView.setVisibility(View.GONE);
               //当前页数于等于总页数时（最后一页数据）
           } else if (mPage ==  phoneListBean.getResult().getPageCount()) {
                mData.addAll(phoneListBean.getResult().getData());
                //禁止到了最后一页加载，根据封装好的判断特别设立值小于每页显示数据
                mAutoListView.setResultSize(mAutoListView.getPageSize() - 1);
                EmptyView.setVisibility(View.GONE);
          }
        } else if ("401".equals(phoneListBean.getStatus())) {
            GetToken mGetToken = new GetToken(AddressListActivity.this);
            mGetToken.getToken();
        }
    }

    @Override
    void notifyDataSetChanged() {
        mAdapter.notifyDataSetChanged();
    }
    @Override
    void LoadResultData(int what) {
        String url = InterfaceDefinition.IPhoneList.IURL + PreferencesUtil.get(this, InterfaceDefinition.PreferencesUser.Docking_CREDENTIALS, "");
        LogUtil.e("通讯录接口", url);
        RequestParams params = new RequestParams(url);
        params.addBodyParameter(InterfaceDefinition.IPhoneList.PAGENUM, mPageSize + "");
        if (what == AutoListView.LOAD) {
            mPage++;
            params.addBodyParameter(InterfaceDefinition.IPhoneList.PAGE, String.valueOf(mPage));
            mCacheUrl = "";
        } else {
            mPage = 1;
            params.addBodyParameter(InterfaceDefinition.IPhoneList.PAGE, String.valueOf(mPage));
            // 拼凑缓存路径
            mCacheUrl = InterfaceDefinition.IPhoneList.IURL;
        }
        // 设置缓存数据的名字及，请求标识（是下拉刷新还是上拉加载）
        setBaseRequestParams(mCacheUrl, what);
        x.http().get(params, this);
    }

    @Override
    public void success(int errtype) {
        if (errtype==0){
            LoadResultData(AutoListView.REFRESH);
        }
    }

    private static final int REQUEST_CALL_PHONE = 1;
    private void InItDialog(final String phone) {
        CommonDialog mDialog = new CommonDialog(AddressListActivity.this, R.style.MyDialog);
        mDialog.setIcon(R.mipmap.reservoir_icon_logo);
        mDialog.setContent("您确定要拨打吗？");
        mDialog.setLeftBtnText("取消");
        mDialog.setRightBtnText("确定");
        mDialog.setListener(new CommonDialog.DialogClickListener() {
            @Override
            public void onRightBtnClick(Dialog dialog) {
                // 检查是否获得了权限（Android6.0运行时权限）
                if (ContextCompat.checkSelfPermission(AddressListActivity.this,
                        Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // 没有获得授权，申请授权
                    if (ActivityCompat.shouldShowRequestPermissionRationale(AddressListActivity.this,
                            Manifest.permission.CALL_PHONE)) {
                        // 返回值：
//                          如果app之前请求过该权限,被用户拒绝, 这个方法就会返回true.
//                          如果用户之前拒绝权限的时候勾选了对话框中”Don’t ask again”的选项,那么这个方法会返回false.
//                          如果设备策略禁止应用拥有这条权限, 这个方法也返回false.
                        // 弹窗需要解释为何需要该权限，再次请求授权
                        Toast.makeText(AddressListActivity.this, "请授权！", Toast.LENGTH_LONG).show();
                        // 帮跳转到该应用的设置界面，让用户手动授权
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                        intent.setData(uri);
                        startActivity(intent);
                    } else {
                        // 不需要解释为何需要该权限，直接请求授权
                        ActivityCompat.requestPermissions(AddressListActivity.this,
                                new String[]{Manifest.permission.CALL_PHONE},
                                REQUEST_CALL_PHONE);
                    }
                } else {
                    // 已经获得授权，可以打电话
                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"
                            + phone));
                    startActivity(intent);
                }

            }

            @Override
            public void onLeftBtnClick(Dialog dialog) {
                dialog.dismiss();

            }
        });
        mDialog.show();
    }
}
