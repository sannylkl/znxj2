package com.jiarui.znxj.fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;

import com.jiarui.znxj.R;
import com.jiarui.znxj.activity.DataDetailsActivity;
import com.jiarui.znxj.adapter.CommonAdapter;
import com.jiarui.znxj.bean.PatrolReportBean;
import com.jiarui.znxj.constants.InterfaceDefinition;
import com.jiarui.znxj.holder.ViewHolder;
import com.jiarui.znxj.utils.PacketUtil;
import com.jiarui.znxj.widget.AutoListView;

import org.json.JSONObject;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/9/11 0011.
 * 巡检报告
 */

public class PatrolReportFragment extends BaseFragmentRefreshLoad {
    // 每页显示多少条数据
    private int mPageSize = 10;
    // 第几页
    private int mPage = 1;
    private CommonAdapter<PatrolReportBean> commonAdapter;//适配器
    private List<PatrolReportBean> pblist;
    @ViewInject(R.id.report_wlbg)
    private RadioButton report_wlbg;//网络报告
    @ViewInject(R.id.report_bdbg)
    private RadioButton report_bdbg;//本地报告
    private String type = "1";

    @Override
    protected View initView(LayoutInflater inflater) {
        mRootView = inflater.inflate(R.layout.patrol_report_fragment, null);
        ButterKnife.bind(this, mRootView);
        return mRootView;
    }

    @Override
    public void initData() {
        setRefreshLoadListView();
        LoadResultData(AutoListView.REFRESH);
        report_wlbg.setChecked(true);
        type = "1";
        report_wlbg.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    type = "1";
                    setRefreshLoadListView();
                    LoadResultData(AutoListView.REFRESH);
                    initView();
                }
            }
        });
        report_bdbg.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    type = "2";
                    Data();
                    initView();
                }
            }
        });
    }

    private void initView() {
        pblist = new ArrayList<>();//数据源
        commonAdapter = new CommonAdapter<PatrolReportBean>(getActivity(), pblist, R.layout.patrol_report) {
            @Override
            public void convert(ViewHolder mHolder, PatrolReportBean item, int position) {
                mHolder.setText(R.id.report_item_data, item.getData());
                mHolder.setText(R.id.report_item_name, item.getName());
                mHolder.setText(R.id.report_item_dianwei, item.getDianwei());
                mHolder.setText(R.id.report_item_jiancha, item.getJiancha());
                mHolder.setText(R.id.report_item_time, item.getTime());
                TextView report_ckxq = (TextView) mHolder.getConvertView().findViewById(R.id.report_ckxq);
                report_ckxq.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        gotoAct(DataDetailsActivity.class);
                    }
                });
            }
        };
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onResume() {
        super.onResume();
            initView();

    }

    @Override
    void clearResultData() {
            pblist.clear();

    }

    @Override
    void disposeResultData(JSONObject arg0) {
        if (type.equals("1")) {
            String data[] = new String[]{"09-02-09:37", "09-02-09:37", "09-02-09:37"};
            String name[] = new String[]{"湾里幸福水库", "湾里幸福水库", "湾里幸福水库"};
            String dianwei[] = new String[]{"点位1", "点位2", "点位3"};
            String jiancha[] = new String[]{"坝体检查", "坝体检查", "坝体检查"};
            String tiame[] = new String[]{"8", "8", "8"};
            for (int i = 0; i < data.length; i++) {
                pblist.add(new PatrolReportBean(data[i], name[i], dianwei[i], jiancha[i], tiame[i]));
            }
            mAutoListView.setAdapter(commonAdapter);
        }
    }

    private void Data() {
       if (type.equals("2")) {
            String data[] = new String[]{"09-02-09:37", "09-02-09:37", "09-02-09:37"};
            String name[] = new String[]{"湾里幸福水库", "湾里幸福水库", "湾里幸福水库"};
            String dianwei[] = new String[]{"点位1", "点位2", "点位3"};
            String jiancha[] = new String[]{"坝体检查", "坝体检查", "坝体检查"};
            String tiame[] = new String[]{"7", "7", "7"};
           pblist.clear();
           for (int i = 0; i < data.length; i++) {
                pblist.add(new PatrolReportBean(data[i], name[i], dianwei[i], jiancha[i], tiame[i]));
            }
           mAutoListView.setAdapter(commonAdapter);
        }
    }

    @Override
    void notifyDataSetChanged() {
            commonAdapter.notifyDataSetChanged();
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
