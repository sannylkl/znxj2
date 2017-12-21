package com.jiarui.znxj.fragment;


import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;

import com.jiarui.znxj.R;
import com.jiarui.znxj.adapter.CommonAdapter;
import com.jiarui.znxj.bean.FrgMessageGonggaoBean;
import com.jiarui.znxj.constants.InterfaceDefinition;
import com.jiarui.znxj.holder.ViewHolder;
import com.jiarui.znxj.utils.DefaultDisplayImageOptions;
import com.jiarui.znxj.utils.PacketUtil;
import com.jiarui.znxj.utils.ToastUtil;
import com.jiarui.znxj.widget.AutoListView;

import org.json.JSONObject;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MessageGonggaoFragment extends BaseFragmentRefreshLoad implements AdapterView.OnItemClickListener {

    String img[] = {
            "http://img4.imgtn.bdimg.com/it/u=3660411511,513290481&fm=21&gp=0.jpg",
            "http://img0.imgtn.bdimg.com/it/u=567877910,3578077276&fm=21&gp=0.jpg",
            "http://img5.imgtn.bdimg.com/it/u=623642464,3162664837&fm=21&gp=0.jpg",
            "http://img2.imgtn.bdimg.com/it/u=2748408299,2703836845&fm=11&gp=0.jpg",
            "http://img5.imgtn.bdimg.com/it/u=1154617231,4271315306&fm=21&gp=0.jpg",
            "http://img3.imgtn.bdimg.com/it/u=1811891003,3063771788&fm=11&gp=0.jpg",
            "http://img1.imgtn.bdimg.com/it/u=884984071,1392074070&fm=21&gp=0.jpg",
            "http://img2.imgtn.bdimg.com/it/u=1664929247,1604114061&fm=21&gp=0.jpg"};

    // 每页显示多少条数据
    private int mPageSize = 10;

    // 第几页
    private int mPage = 1;

    private String mCacheUrl = "";

    private List<FrgMessageGonggaoBean> mData = null;

    private CommonAdapter<FrgMessageGonggaoBean> mAdapter = null;

    static MessageGonggaoFragment mFragment;

    public static MessageGonggaoFragment NewFragment() {
        if (mFragment == null) {
            mFragment = new MessageGonggaoFragment();
        }
        return mFragment;

    }

    @Override
    protected View initView(LayoutInflater inflater) {
        mRootView = inflater.inflate(R.layout.frg_messgae_gonggao, null);
        return mRootView;
    }

    @Override
    public void initData() {
        setRefreshLoadListView();
        mData = new ArrayList<FrgMessageGonggaoBean>();
        mAdapter = new CommonAdapter<FrgMessageGonggaoBean>(getActivity(), mData,
                R.layout.frg_message_gonggao_item) {
            @Override
            public void convert(ViewHolder mHolder, FrgMessageGonggaoBean item,
                                int position) {
                mHolder.setText(R.id.frg_message_gonggao_item_title,
                        item.getTitle());
                mHolder.setText(R.id.frg_message_gonggao_item_desc, item.getInfo());
                mHolder.setImageByUrl(R.id.frg_message_gonggao_item_img, item
                        .getImg(), getActivity(), DefaultDisplayImageOptions
                        .getDefaultDisplayImageOptions(getActivity()));
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
        if (img.equals("") || img == null) {
            mAutoListView.setResultSize(0);
            if (mAdapter.getCount() == 0)
                EmptyView.setVisibility(View.VISIBLE);
            else
                EmptyView.setVisibility(View.GONE);
        } else {
            for (int i = 0; i < img.length; i++) {
                FrgMessageGonggaoBean entity = new FrgMessageGonggaoBean("", img[i],
                        "公告" + i, "公告的内容内容内容内容内容内容内容内容内容内容内容内容" + i);
                mData.add(entity);
            }
            EmptyView.setVisibility(View.GONE);
            mAutoListView.setResultSize(img.length);
        }
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
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ToastUtil.TextToast(position + "");
    }
}
