package com.jiarui.znxj.activity;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jiarui.znxj.R;
import com.jiarui.znxj.adapter.CommonAdapter;
import com.jiarui.znxj.base.BaseActivity;
import com.jiarui.znxj.bean.LoginBean;

import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import cn.addapp.pickers.picker.DateTimePicker;


/**
 * 汇报水情
 */
public class WaterregimeActivity extends BaseActivity {
    @ViewInject(R.id.linear2)
    private LinearLayout linear2;
    @ViewInject(R.id.water_time)
    private TextView water_time;

    int index = 0;
    Dialog dialog;
    List<LoginBean> list = new ArrayList<>();
    CommonAdapter<LoginBean> commonAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waterregime);
        x.view().inject(this);
        setTitle();
        mTvForTitle.setText("汇报水情");
        mImgvForRight.setText("保存");
    }

    @Event({R.id.common_title_left, R.id.linear2})
    private void onClick(View v) {
        switch (v.getId()) {
            case R.id.common_title_left:
                finish();
                break;
            case R.id.linear2:
                onYearMonthDayTimePicker();
                break;
            default:
                break;
        }
    }

//    *
//     * 时间选择
//
//    private void popuMethod() {
//        index = 0;
//        dialog = new Dialog(WaterregimeActivity.this, R.style.Theme_Light_Dialog);
//        View dialogView = LayoutInflater.from(WaterregimeActivity.this).inflate(R.layout.time_dialog, null);
//        TextView dialog_qd = (TextView) dialogView.findViewById(R.id.dialog_qd);
//        final TextView year = (TextView) dialogView.findViewById(R.id.year);
//        final TextView month = (TextView) dialogView.findViewById(R.id.month);
//        final TextView day = (TextView) dialogView.findViewById(R.id.day);
//        final TextView time = (TextView) dialogView.findViewById(R.id.time);
//        final TextView branch = (TextView) dialogView.findViewById(R.id.branch);
//        final ListView dialog_list1 = (ListView) dialogView.findViewById(R.id.dialog_list1);
//        final TextView te1 = (TextView) dialogView.findViewById(R.id.te1);
//        final TextView te2 = (TextView) dialogView.findViewById(R.id.te2);
//        final TextView te3 = (TextView) dialogView.findViewById(R.id.te3);
//        final TextView te4 = (TextView) dialogView.findViewById(R.id.te4);
//        final TextView te5 = (TextView) dialogView.findViewById(R.id.te5);
//        commonAdapter = new CommonAdapter<LoginBean>(WaterregimeActivity.this, list, R.layout.logdialog_item) {
//            @Override
//            public void convert(ViewHolder mHolder, LoginBean item, int position) {
//                mHolder.setText(R.id.logdialog_name, item.getName());
//            }
//        };
//        final String name[] = new String[]{"2017", "2016", "2015", "2014", "2013", "2012", "2011", "2010", "2009", "2008", "2007", "2003"};
//        list.clear();
//        for (int i = 0; i < name.length; i++) {
//            list.add(new LoginBean(name[i]));
//        }
//        dialog_list1.setAdapter(commonAdapter);
//        dialog_list1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                if (index == 0) {
//                    year.setText(list.get(i).getName());
//                    list.clear();
//                    for (int j = 1; j < 13; j++) {
//                        list.add(new LoginBean(String.valueOf(j)));
//                    }
//                    commonAdapter.notifyDataSetChanged();
//                    index = 1;
//                    te1.setVisibility(View.INVISIBLE);
//                    te2.setVisibility(View.VISIBLE);
//                    te3.setVisibility(View.INVISIBLE);
//                    te4.setVisibility(View.INVISIBLE);
//                    te5.setVisibility(View.INVISIBLE);
//                } else if (index == 1) {
//                    month.setText(list.get(i).getName());
//                    list.clear();
//                    for (int j = 1; j < 13; j++) {
//                        list.add(new LoginBean(String.valueOf(j)));
//                    }
//                    commonAdapter.notifyDataSetChanged();
//                    index = 2;
//                    te2.setVisibility(View.INVISIBLE);
//                    te3.setVisibility(View.VISIBLE);
//                    te1.setVisibility(View.INVISIBLE);
//                    te4.setVisibility(View.INVISIBLE);
//                    te5.setVisibility(View.INVISIBLE);
//                } else if (index == 2) {
//                    day.setText(list.get(i).getName());
//                    list.clear();
//                    for (int j = 1; j < 31; j++) {
//                        list.add(new LoginBean(String.valueOf(j)));
//                    }
//                    commonAdapter.notifyDataSetChanged();
//                    index = 3;
//                    te3.setVisibility(View.INVISIBLE);
//                    te2.setVisibility(View.INVISIBLE);
//                    te1.setVisibility(View.INVISIBLE);
//                    te4.setVisibility(View.VISIBLE);
//                    te5.setVisibility(View.INVISIBLE);
//                } else if (index == 3) {
//                    time.setText(list.get(i).getName());
//                    list.clear();
//                    for (int j = 1; j < 24; j++) {
//                        list.add(new LoginBean(String.valueOf(j)));
//                    }
//                    commonAdapter.notifyDataSetChanged();
//                    index = 4;
//                    te4.setVisibility(View.INVISIBLE);
//                    te2.setVisibility(View.INVISIBLE);
//                    te1.setVisibility(View.INVISIBLE);
//                    te3.setVisibility(View.INVISIBLE);
//                    te5.setVisibility(View.VISIBLE);
//                } else if (index == 4) {
//                    branch.setText(list.get(i).getName());
//                    for (int j = 1; j < 61; j++) {
//                        list.add(new LoginBean(String.valueOf(j)));
//                    }
//                    commonAdapter.notifyDataSetChanged();
//                    list.clear();
//                    te5.setVisibility(View.VISIBLE);
//                    te2.setVisibility(View.INVISIBLE);
//                    te1.setVisibility(View.INVISIBLE);
//                    te3.setVisibility(View.INVISIBLE);
//                    te4.setVisibility(View.INVISIBLE);
//                }
//            }
//        });
//        year.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                year.setText("年");
//                index = 0;
//                list.clear();
//                for (int i = 0; i < name.length; i++) {
//                    list.add(new LoginBean(name[i]));
//                }
//                commonAdapter.notifyDataSetChanged();
//                te1.setVisibility(View.VISIBLE);
//                te2.setVisibility(View.INVISIBLE);
//                te3.setVisibility(View.INVISIBLE);
//                te4.setVisibility(View.INVISIBLE);
//                te5.setVisibility(View.INVISIBLE);
//            }
//        });
//        month.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                month.setText("月");
//                index = 1;
//                list.clear();
//                for (int j = 1; j < 13; j++) {
//                    list.add(new LoginBean(String.valueOf(j)));
//                }
//                commonAdapter.notifyDataSetChanged();
//                te2.setVisibility(View.VISIBLE);
//                te3.setVisibility(View.INVISIBLE);
//                te1.setVisibility(View.INVISIBLE);
//                te4.setVisibility(View.INVISIBLE);
//                te5.setVisibility(View.INVISIBLE);
//            }
//        });
//        day.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                day.setText("日");
//                index = 2;
//                list.clear();
//                for (int j = 1; j < 31; j++) {
//                    list.add(new LoginBean(String.valueOf(j)));
//                }
//                commonAdapter.notifyDataSetChanged();
//                te3.setVisibility(View.VISIBLE);
//                te2.setVisibility(View.INVISIBLE);
//                te1.setVisibility(View.INVISIBLE);
//                te4.setVisibility(View.INVISIBLE);
//                te5.setVisibility(View.INVISIBLE);
//            }
//        });
//        time.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                time.setText("时");
//                index = 2;
//                list.clear();
//                for (int j = 1; j < 31; j++) {
//                    list.add(new LoginBean(String.valueOf(j)));
//                }
//                commonAdapter.notifyDataSetChanged();
//                te4.setVisibility(View.VISIBLE);
//                te2.setVisibility(View.INVISIBLE);
//                te1.setVisibility(View.INVISIBLE);
//                te3.setVisibility(View.INVISIBLE);
//                te5.setVisibility(View.INVISIBLE);
//            }
//        });
//        branch.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                branch.setText("分");
//                index = 2;
//                list.clear();
//                for (int j = 1; j < 31; j++) {
//                    list.add(new LoginBean(String.valueOf(j)));
//                }
//                commonAdapter.notifyDataSetChanged();
//                te5.setVisibility(View.VISIBLE);
//                te2.setVisibility(View.INVISIBLE);
//                te1.setVisibility(View.INVISIBLE);
//                te4.setVisibility(View.INVISIBLE);
//                te3.setVisibility(View.INVISIBLE);
//            }
//        });
//        dialog_qd.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                water_time.setText(year.getText().toString() + ""
//                        + month.getText().toString() + ""
//                        + day.getText().toString() + ""
//                        + time.getText().toString() + ""
//                        + branch.getText().toString());
//                dialog.dismiss();
//            }
//        });
//        //获得dialog的window窗口
//        Window window = dialog.getWindow();
//        //设置dialog在屏幕底部
//        window.setGravity(Gravity.BOTTOM);
//        //设置dialog弹出时的动画效果，从屏幕底部向上弹出
//        window.setWindowAnimations(R.style.dialogStyle);
//        window.getDecorView().setPadding(0, 0, 0, 0);
//        //获得window窗口的属性
//        android.view.WindowManager.LayoutParams lp = window.getAttributes();
//        //设置窗口宽度为充满全屏
//        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
//        //设置窗口高度为包裹内容
//        lp.height = 700;
//        //将设置好的属性set回去
//        window.setAttributes(lp);
//        //设置此参数获得焦点,否则无法点击
//        dialog.setCanceledOnTouchOutside(true);
//        //将自定义布局加载到dialog上
//        dialog.setContentView(dialogView);
//        dialog.show();
//
//    }

    /**
     * 时间选择
     */
    public void onYearMonthDayTimePicker() {
        DateTimePicker picker = new DateTimePicker(this, DateTimePicker.HOUR_24);
        picker.setDateRangeStart(2017, 1, 1);
        picker.setDateRangeEnd(2050, 12, 31);
        picker.setTimeRangeStart(0, 0);
        picker.setTimeRangeEnd(23, 59);
        picker.setWeightEnable(true);
        picker.setWheelModeEnable(true);
        picker.setOnDateTimePickListener(new DateTimePicker.OnYearMonthDayTimePickListener() {
            @Override
            public void onDateTimePicked(String year, String month, String day, String hour, String minute) {
                water_time.setText(year + "-" + month + "-" + day + " " + hour + ":" + minute);
            }
        });
        picker.show();
    }

}
