package com.jiarui.znxj.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jiarui.znxj.R;
import com.jiarui.znxj.adapter.CommonAdapter;
import com.jiarui.znxj.base.BaseFragment;
import com.jiarui.znxj.bean.HistoricalRainfallBean;
import com.jiarui.znxj.holder.ViewHolder;
import com.jiarui.znxj.widget.ListViewForScrollView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.addapp.pickers.picker.DatePicker;

/**
 * Created by Administrator on 2017/9/12 0012.
 * 历史雨量查询
 */

public class HistoricalRainfall extends BaseFragment {

    @Bind(R.id.rainfall_list)
    ListViewForScrollView rainfallList;
    @Bind(R.id.time1)
    TextView time1;
    @Bind(R.id.time2)
    TextView time2;
    private List<HistoricalRainfallBean> hrlist = new ArrayList<>();//数据源
    private CommonAdapter<HistoricalRainfallBean> commonAdapter;//适配器

    @Override
    protected View initView(LayoutInflater inflater) {
        mRootView = inflater.inflate(R.layout.historical_rainfall, null);
        ButterKnife.bind(this, mRootView);
        initView();
        return mRootView;
    }

    private void initView() {
        commonAdapter = new CommonAdapter<HistoricalRainfallBean>(getActivity(), hrlist, R.layout.historical_rainfall_item) {
            @Override
            public void convert(ViewHolder mHolder, HistoricalRainfallBean item, int position) {
                mHolder.setText(R.id.rainfall_item_time, item.getTime());
                mHolder.setText(R.id.rainfall_item_jianyu, item.getJianyu());
                mHolder.setText(R.id.hiwater_item_sjd, item.getTimeduan());
            }
        };
        rainfallList.setAdapter(commonAdapter);

    }

    @Override
    public void initData() {
        String time[] = new String[]{"5分钟", "15分钟"};
        String jianyu[] = new String[]{"50m", "60m"};
        String sjd[] = new String[]{"12:00~13:00", "12:00~13:00"};
        hrlist.clear();
        for (int i = 0; i < time.length; i++) {
            hrlist.add(new HistoricalRainfallBean(time[i], jianyu[i], sjd[i]));
        }
        commonAdapter.notifyDataSetChanged();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @OnClick({R.id.time1, R.id.time2})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.time1:
                onYearMonthDayPicker();
                break;
            case R.id.time2:
                onYearMonthDayPicker();
                break;
        }
    }

    public void onYearMonthDayPicker() {
        final DatePicker picker = new DatePicker(getActivity());
        picker.setCanLoop(false);
        picker.setWheelModeEnable(true);
        picker.setTopPadding(15);
        picker.setRangeStart(2016, 8, 29);
        picker.setRangeEnd(2111, 1, 11);
        picker.setSelectedItem(2050, 10, 14);
        picker.setWeightEnable(true);
        picker.setOnDatePickListener(new DatePicker.OnYearMonthDayPickListener() {
            @Override
            public void onDatePicked(String year, String month, String day) {
            }
        });
        picker.setOnWheelListener(new DatePicker.OnWheelListener() {
            @Override
            public void onYearWheeled(int index, String year) {
                picker.setTitleText(year + "-" + picker.getSelectedMonth() + "-" + picker.getSelectedDay());
            }

            @Override
            public void onMonthWheeled(int index, String month) {
                picker.setTitleText(picker.getSelectedYear() + "-" + month + "-" + picker.getSelectedDay());
            }

            @Override
            public void onDayWheeled(int index, String day) {
                picker.setTitleText(picker.getSelectedYear() + "-" + picker.getSelectedMonth() + "-" + day);
            }
        });
        picker.show();
    }
}
