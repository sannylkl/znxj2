package com.jiarui.znxj.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.google.gson.Gson;
import com.jiarui.znxj.R;
import com.jiarui.znxj.adapter.CommonAdapter;
import com.jiarui.znxj.bean.InitBean;
import com.jiarui.znxj.constants.InterfaceDefinition;
import com.jiarui.znxj.holder.ViewHolder;
import com.jiarui.znxj.utils.DateUtil;
import com.jiarui.znxj.utils.DefaultDisplayImageOptions;
import com.jiarui.znxj.utils.HttpUtil;
import com.jiarui.znxj.utils.PacketUtil;
import com.jiarui.znxj.utils.StringUtil;
import com.jiarui.znxj.widget.AutoListView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

import org.json.JSONArray;
import org.json.JSONObject;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.Event;
import org.xutils.x;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CanzhaoActivity extends BaseActivityRefreshLoad {

    // 每页显示多少条数据
    private int mPageSize = 10;

    // 第几页
    private int mPage = 1;

    // 数据源
    private ArrayList<InitBean> mData;

    // 公共适配器
    private CommonAdapter<InitBean> mAdapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_canzhao);
        x.view().inject(this);
        setTitle();
        mTvForTitle.setText("参照activity");
        setRefreshLoadListView();
        mData = new ArrayList<InitBean>();
        initAutoListView();
        LoadResultData(AutoListView.REFRESH);
    }

    private void initAutoListView() {
        final DisplayImageOptions mOptions = DefaultDisplayImageOptions
                .getDefaultDisplayImageOptionsRounded(CanzhaoActivity.this, 360);
        mAdapter = new CommonAdapter<InitBean>(this, mData,
                R.layout.act_canzhao_item) {
            @Override
            public void convert(ViewHolder mHolder, InitBean item, int position) {
                mHolder.setText(R.id.common_task_item_title, item.getTitle());
                mHolder.setText(R.id.common_task_item_commission,
                        item.getCommission());
                mHolder.setTextHtml(R.id.common_task_item_desc,
                        item.getContent());
                mHolder.setText(R.id.common_task_item_desc_title,
                        item.getType_name());
                mHolder.setText(R.id.common_task_item_location, item.getArea());
                mHolder.setText(R.id.common_task_item_time,
                        DateUtil.timeStamp2Date(item.getE_time(), "yyyy-MM-dd"));
                mHolder.setImageByUrl(R.id.common_task_item_head,
                        HttpUtil.LOAD_URL(CanzhaoActivity.this) + item.getHead(),
                        CanzhaoActivity.this, mOptions);
            }
        };
        mAutoListView.setAdapter(mAdapter);
        mAutoListView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                String Task_id = mData.get(position).getTask_id();
                Bundle bundle = new Bundle();
                bundle.putString("id", Task_id);
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
        Gson gson = new Gson();
        JSONArray list = arg0
                .optJSONArray(InterfaceDefinition.IGetTaskType.LIST);
        if (StringUtil.isJSONArrayNotEmpty(list)) {
            for (int i = 0; i < list.length(); i++) {
                String json = list.optJSONObject(i).toString();
                InitBean entity = gson.fromJson(json, InitBean.class);
                mData.add(entity);
            }
            EmptyView.setVisibility(View.GONE);
            mAutoListView.setVisibility(View.VISIBLE);
            mAutoListView.setResultSize(list.length());
        } else {
            mAutoListView.setVisibility(View.GONE);
            EmptyView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    void notifyDataSetChanged() {
        mAdapter.notifyDataSetChanged();
    }

    @Override
    void LoadResultData(int what) {
//        RequestParams params = new RequestParams(
//                InterfaceDefinition.IGetTaskType.URL);
//        Map<String, String> data = new HashMap<String, String>();
//        data.put(InterfaceDefinition.IGetTaskType.PAGESIZE, mPageSize + "");
//        data.put(InterfaceDefinition.IGetTask.URGENT, String.valueOf(0));
//        data.put(InterfaceDefinition.IGetTask.LONGTERM, String.valueOf(0));
//        data.put(InterfaceDefinition.IGetTask.ORDINARY, String.valueOf(0));
//        mTvForTitle.setText("任务大厅");
//        if (what == AutoListView.LOAD) {
//            mPage++;
//            data.put(InterfaceDefinition.IGetTask.PAGE, String.valueOf(mPage));
//            mCacheUrl = "";
//        } else {
//            mPage = 1;
//            data.put(InterfaceDefinition.IGetTask.PAGE, String.valueOf(mPage));
//            // 拼凑缓存路径
//            mCacheUrl = InterfaceDefinition.IGetTask.URL
//                    + InterfaceDefinition.IGetTask.PACKET_NO_DATA
//                    + data.toString();
//        }
//        // 设置缓存数据的名字及，请求标识（是下拉刷新还是上拉加载）
//        setBaseRequestParams(mCacheUrl, what);
//        params.addBodyParameter(InterfaceDefinition.ICommonKey.REQUEST_DATA,
//                PacketUtil.getRequestPacket(this,
//                        InterfaceDefinition.IGetTask.PACKET_NO_DATA,
//                        "", data.toString()));
//        x.http().post(params, this);
    }

    @Event({R.id.common_title_left})
    private void onClick(View v) {
        switch (v.getId()) {
            case R.id.common_title_left:
                finish();
                break;
            default:
                break;
        }
    }

}
