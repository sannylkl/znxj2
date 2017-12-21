package com.jiarui.znxj.fragment;

import android.view.LayoutInflater;
import android.view.View;

import com.jiarui.znxj.R;
import com.jiarui.znxj.activity.UrgentReportActivity;
import com.jiarui.znxj.adapter.CommonAdapter;
import com.jiarui.znxj.bean.UrgentBean1;
import com.jiarui.znxj.bean.UrgentBean2;
import com.jiarui.znxj.constants.InterfaceDefinition;
import com.jiarui.znxj.holder.ViewHolder;
import com.jiarui.znxj.utils.PacketUtil;
import com.jiarui.znxj.widget.AutoListView;

import org.json.JSONObject;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/9/11 0011.
 */

public class UrgentFragment1 extends BaseFragmentRefreshLoad {
    CommonAdapter<UrgentBean1> commonAdapter;//适配器
    List<UrgentBean1> list;//数据源
    CommonAdapter<UrgentBean2> commonAdapter2;//适配器
    List<UrgentBean2> list2;//数据源
    // 每页显示多少条数据
    private int mPageSize = 10;
    // 第几页
    private int mPage = 1;

    @Override
    protected View initView(LayoutInflater inflater) {
        mRootView = inflater.inflate(R.layout.urgent_frgment1, null);
        x.view().inject(getActivity());
        return mRootView;
    }

    @Override
    public void initData() {
        setRefreshLoadListView();
        comm();
        LoadResultData(AutoListView.REFRESH);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (UrgentReportActivity.type.equals("1")) {
            comm();
        } else if (UrgentReportActivity.type.equals("2")) {
            comm2();
        }
    }

    public void comm() {
        list = new ArrayList<>();
        commonAdapter = new CommonAdapter<UrgentBean1>(getActivity(), list, R.layout.urgent_frgment_item) {
            @Override
            public void convert(ViewHolder mHolder, UrgentBean1 item, int position) {
                mHolder.setText(R.id.time, item.getTime());
                mHolder.setText(R.id.name, item.getName());
                mHolder.setText(R.id.position, item.getPosition());
                mHolder.setText(R.id.situation, item.getSituation());
                mHolder.setText(R.id.waterlevel, item.getWaterlevel());
                mHolder.setText(R.id.weather, item.getWeather());
                mHolder.setText(R.id.xwaterlevel, item.getXwaterlevel());
                mHolder.setText(R.id.remarks, item.getRemarks());
                mHolder.setText(R.id.soundrecording, item.getSoundrecording());
            }
        };
    }

    public void comm2() {
        list2 = new ArrayList<>();
        commonAdapter2 = new CommonAdapter<UrgentBean2>(getActivity(), list2, R.layout.urgent_frgment_item2) {
            @Override
            public void convert(ViewHolder mHolder, UrgentBean2 item, int position) {
                mHolder.setText(R.id.time, item.getTime());
                mHolder.setText(R.id.site, item.getSite());
                mHolder.setText(R.id.waterdepth, item.getWaterdepth());
                mHolder.setText(R.id.rflow, item.getRflow());
                mHolder.setText(R.id.cflow, item.getCflow());
                mHolder.setText(R.id.interval, item.getInterval());
            }
        };
    }

    @Override
    void clearResultData() {
        if (UrgentReportActivity.type.equals("1")) {
            list.clear();
        } else if (UrgentReportActivity.type.equals("2")) {
            list2.clear();
        }
    }

    @Override
    void disposeResultData(JSONObject arg0) {
        if (UrgentReportActivity.type.equals("1")) {
            UrgentBean1 urg1 = new UrgentBean1("2017-09-08 15:18", "湾里幸福水库", "坝基", "正常", "22.97", "多云", "32.96", "备注", "03:25:00");
            UrgentBean1 urg2 = new UrgentBean1("2017-09-08 15:18", "湾里幸福水库", "坝基", "正常", "22.97", "多云", "32.96", "备注", "03:25:00");
            UrgentBean1 urg3 = new UrgentBean1("2017-09-08 15:18", "湾里幸福水库", "坝基", "正常", "22.97", "多云", "32.96", "备注", "03:25:00");
            list.add(urg1);
            list.add(urg2);
            list.add(urg3);
            mAutoListView.setAdapter(commonAdapter);
        } else if (UrgentReportActivity.type.equals("2")) {
            UrgentBean2 urg1 = new UrgentBean2("2017-09-08 15:18", "A站点", "50.55", "25.30", "22.97", "45.00");
            UrgentBean2 urg2 = new UrgentBean2("2017-09-08 15:18", "A站点", "48.36", "49.36", "32.33", "多云");
            UrgentBean2 urg3 = new UrgentBean2("2017-09-08 15:18", "A站点", "32.33", "32.33", "22.97", "多云");
            list2.add(urg1);
            list2.add(urg2);
            list2.add(urg3);
            mAutoListView.setAdapter(commonAdapter2);
        }
    }

    @Override
    void notifyDataSetChanged() {
        if (UrgentReportActivity.type.equals("1")) {
            commonAdapter.notifyDataSetChanged();
        } else if (UrgentReportActivity.type.equals("2")) {
            commonAdapter2.notifyDataSetChanged();
        }
    }

    @Override
    void LoadResultData(int what) {
        RequestParams params = new RequestParams(InterfaceDefinition.IGetTaskType.BASE_URL);
        Map<String, String> data = new HashMap<>();
        data.put(InterfaceDefinition.IGetTaskType.PAGESIZE, mPageSize + "");
        data.put(InterfaceDefinition.IGetTask.URGENT, String.valueOf(0));
        data.put(InterfaceDefinition.IGetTask.LONGTERM, String.valueOf(0));
        data.put(InterfaceDefinition.IGetTask.ORDINARY, String.valueOf(0));
        if (what == AutoListView.LOAD) {
            mPage++;
            data.put(InterfaceDefinition.IGetTask.PAGE, String.valueOf(mPage));
            mCacheUrl = "";
        } else {
            mPage = 1;
            data.put(InterfaceDefinition.IGetTask.PAGE, String.valueOf(mPage));
            // 拼凑缓存路径
            mCacheUrl = InterfaceDefinition.IGetTask.BASE_URL + InterfaceDefinition.IGetTask.PACKET_NO_DATA + data.toString();
        }
        // 设置缓存数据的名字及，请求标识（是下拉刷新还是上拉加载）
        setBaseRequestParams(mCacheUrl, what);
        params.addBodyParameter(InterfaceDefinition.ICommonKey.REQUEST_DATA,
                PacketUtil.getRequestPacket(getActivity(), InterfaceDefinition.IGetTask.PACKET_NO_DATA, "pengsong2", data.toString()));
        x.http().post(params, this);
    }
}
