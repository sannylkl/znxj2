package com.jiarui.znxj.fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.google.gson.Gson;
import com.jiarui.znxj.R;
import com.jiarui.znxj.adapter.CommonAdapter;
import com.jiarui.znxj.bean.NoticeEntity;
import com.jiarui.znxj.constants.InterfaceDefinition;
import com.jiarui.znxj.holder.ViewHolder;
import com.jiarui.znxj.utils.DateUtil;
import com.jiarui.znxj.utils.PacketUtil;
import com.jiarui.znxj.utils.StringUtil;
import com.jiarui.znxj.utils.ToastUtil;
import com.jiarui.znxj.widget.AutoListView;

import org.json.JSONArray;
import org.json.JSONObject;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CanZhaoFragment extends BaseFragmentRefreshLoad implements
        OnItemClickListener {

    // 每页显示多少条数据
    private int mPageSize = 10;

    // 第几页
    private int mPage = 1;

    private String mCacheUrl = "";

    private List<NoticeEntity> mData = null;

    private CommonAdapter<NoticeEntity> mAdapter = null;

    static CanZhaoFragment mFragment;

    public static CanZhaoFragment NewFragment() {
        if (mFragment == null) {
            mFragment = new CanZhaoFragment();
        }
        return mFragment;

    }

    @Override
    protected View initView(LayoutInflater inflater) {
        mRootView = inflater.inflate(R.layout.canzhao_fragment, null);
        return mRootView;
    }

    @Override
    public void initData() {

        setRefreshLoadListView();
        mData = new ArrayList<NoticeEntity>();
        mAdapter = new CommonAdapter<NoticeEntity>(getActivity(), mData,
                R.layout.canzhao_fragment_item) {
            @Override
            public void convert(ViewHolder mHolder, NoticeEntity item,
                                int position) {
                mHolder.setText(R.id.layout_message_item_title, item.getTitle());
                mHolder.setText(R.id.layout_message_item_desc, item.getIntro());
                mHolder.setText(R.id.layout_message_item_date,
                        DateUtil.timeStamp2Date(item.getAdd_time(), "MM-dd"));
            }
        };
        mAutoListView.setAdapter(mAdapter);
        // 设置item的点击事件
        mAutoListView.setOnItemClickListener(this);
        // 初次加载属于刷新状态
        LoadResultData(AutoListView.REFRESH);
    }

    @Override
    void clearResultData() {
        mData.clear();
    }

    @Override
    void disposeResultData(JSONObject arg0) {
        Gson gson = new Gson();
        JSONArray list = arg0.optJSONArray(InterfaceDefinition.IBulletin.LIST);
        if (StringUtil.isJSONArrayNotEmpty(list)) {
            for (int i = 0; i < list.length(); i++) {
                String json = list.optJSONObject(i).toString();
                NoticeEntity entity = gson.fromJson(json, NoticeEntity.class);
                mData.add(entity);
            }
            EmptyView.setVisibility(View.GONE);
            mAutoListView.setResultSize(list.length());
        } else
            mAutoListView.setResultSize(0);
        if (mAdapter.getCount() == 0)
            EmptyView.setVisibility(View.VISIBLE);
        else
            EmptyView.setVisibility(View.GONE);
    }

    @Override
    void notifyDataSetChanged() {
        mAdapter.notifyDataSetChanged();
        setFirstLoad(false);
    }

    @Override
    void LoadResultData(int what) {
        RequestParams params = new RequestParams(
                InterfaceDefinition.IBulletin.BASE_URL);
        Map<String, String> data = new HashMap<String, String>();
        data.put(InterfaceDefinition.IBulletin.DATE_FLAG,
                InterfaceDefinition.IBulletin.OLD);
        data.put(InterfaceDefinition.IBulletin.PAGESIZE, mPageSize + "");
        if (what == AutoListView.LOAD) {
            mPage++;
            data.put(InterfaceDefinition.IBulletin.PAGE, String.valueOf(mPage));
            mCacheUrl = "";
        } else {
            mPage = 1;
            data.put(InterfaceDefinition.IBulletin.PAGE, String.valueOf(mPage));
            // 拼凑缓存路径
            mCacheUrl = InterfaceDefinition.IBulletin.BASE_URL
                    + InterfaceDefinition.IBulletin.PACKET_NO_DATA
                    + data.toString();
        }
        // 设置缓存数据的名字及，请求标识（是下拉刷新还是上拉加载）
        setBaseRequestParams(mCacheUrl, what);
        params.addBodyParameter(InterfaceDefinition.ICommonKey.REQUEST_DATA,
                PacketUtil.getRequestPacket(mContext,
                        InterfaceDefinition.IBulletin.PACKET_NO_DATA, "",
                        data.toString()));
        x.http().post(params, this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        ToastUtil.TextToast(position + "");
    }

}
