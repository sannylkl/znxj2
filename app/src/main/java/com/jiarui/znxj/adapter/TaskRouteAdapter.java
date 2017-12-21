package com.jiarui.znxj.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jiarui.znxj.R;
import com.jiarui.znxj.bean.TaskCenter_listBean;
import com.jiarui.znxj.bean.xj_route_detailBean;

import java.util.List;

import static com.jiarui.znxj.R.id.task_item_serial;

/**
 * 任务列表路线规则
 *
 * @author lzh
 * @version 1.0
 * @date 2017/1/10 14:10
 */
public class TaskRouteAdapter extends BaseAdapter {

    private Context mcontext;

    private List<xj_route_detailBean> mdata;

    public TaskRouteAdapter(Context mcontext, List<xj_route_detailBean> mdata) {
        this.mcontext = mcontext;
        this.mdata = mdata;
    }

    @Override
    public int getCount() {
        if (mdata.size() < 2) {
            return mdata.size();
        } else {
            return 2;
        }
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MyHolder myHolder;
        if (convertView == null) {
            myHolder = new MyHolder();
            convertView = LayoutInflater.from(mcontext).inflate(R.layout.taskcenter_list_item, null);
            myHolder.text_shu1 = (TextView) convertView.findViewById(R.id.text_shu1);
            myHolder.task_item_serial = (TextView) convertView.findViewById(task_item_serial);
            myHolder.text_shu2 = (TextView) convertView.findViewById(R.id.text_shu2);
            myHolder.task_item_commis = (TextView) convertView.findViewById(R.id.task_item_commis);
            myHolder.task_item_time = (TextView) convertView.findViewById(R.id.task_item_time);
            convertView.setTag(myHolder);
        } else {
            myHolder = (MyHolder) convertView.getTag();
        }

        xj_route_detailBean listBean=mdata.get(position);
        if (position == 0) {
            myHolder.text_shu1.setVisibility(View.INVISIBLE);
            myHolder.text_shu2.setVisibility(View.VISIBLE);
            myHolder.task_item_time.setVisibility(View.VISIBLE);
        } else if (position == mdata.size() - 1) {
            myHolder.text_shu1.setVisibility(View.VISIBLE);
            myHolder.text_shu2.setVisibility(View.INVISIBLE);
            myHolder.task_item_time.setVisibility(View.GONE);
        }else {
            myHolder.text_shu1.setVisibility(View.VISIBLE);
            myHolder.text_shu2.setVisibility(View.VISIBLE);
            myHolder.task_item_time.setVisibility(View.VISIBLE);
        }
        myHolder.task_item_serial.setText(""+listBean.getLocation_id());
        myHolder.task_item_commis.setText("完成点位任务时间"+listBean.getFinished_time());
        myHolder.task_item_time.setText(listBean.getNext_time()+"到达下一个点位");
        return convertView;
    }
    class MyHolder {
        TextView text_shu1;
        TextView task_item_serial;
        TextView text_shu2;
        TextView task_item_commis;
        TextView task_item_time;
    }
}
