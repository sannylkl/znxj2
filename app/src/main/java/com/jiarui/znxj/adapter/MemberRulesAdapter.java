package com.jiarui.znxj.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jiarui.znxj.R;
import com.jiarui.znxj.activity.BaseofDamActivity;
import com.jiarui.znxj.activity.TaskPreviewCheckItemActivity;
import com.jiarui.znxj.bean.AAA;
import com.jiarui.znxj.bean.TaskDetailsTablePartBean;
import com.jiarui.znxj.bean.TaskPreviewData;
import com.jiarui.znxj.holder.ViewHolder;
import com.jiarui.znxj.utils.ToastUtil;
import com.jiarui.znxj.widget.CustomGridView;
import com.jiarui.znxj.widget.ListViewForScrollView;

import java.util.ArrayList;
import java.util.List;

/**
 * 任务预览部位展示 2017/9/22 0022.
 */

public class MemberRulesAdapter extends BaseAdapter {
    private List<TaskPreviewData.ItemsBean> lstRulesContent;
    private Context context;

    public MemberRulesAdapter(List<TaskPreviewData.ItemsBean> lstRulesContent, Context context) {
        this.lstRulesContent = lstRulesContent;
        this.context = context;
    }

    @Override
    public int getCount() {
        return lstRulesContent == null ? 0 : lstRulesContent.size();
    }

    @Override
    public Object getItem(int position) {
        if (lstRulesContent == null) {
            return 0;
        }
        return lstRulesContent.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        PersonViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new PersonViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_member_type_bw, parent, false);
            viewHolder.tv_group_name_rigth = (TextView) convertView.findViewById(R.id.tv_group_name_rigth);
            viewHolder.lvTitleContent = (ListViewForScrollView) convertView.findViewById(R.id.lv_title_content);
            viewHolder.type_yc= (TextView) convertView.findViewById(R.id.type_yc);
            viewHolder.image = (ImageView) convertView.findViewById(R.id.image);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (PersonViewHolder) convertView.getTag();
        }

        //设置检查项目的数据
        TaskPreviewData.ItemsBean rulesContent = lstRulesContent.get(position);
        if (rulesContent.isCheck()) {
            viewHolder.lvTitleContent.setVisibility(View.VISIBLE);
            viewHolder.image.setImageResource(R.mipmap.more_icon);
        } else {
            viewHolder.lvTitleContent.setVisibility(View.GONE);
            viewHolder.image.setImageResource(R.mipmap.less_icon);
        }
        viewHolder.tv_group_name_rigth.setText(rulesContent.getName());
        if (rulesContent.getStatus()==2) {//正常
            viewHolder.type_yc.setText( "正常");
            viewHolder.type_yc.setTextColor(Color.parseColor("#92C35C"));
        } else if(rulesContent.getStatus()==3) {//异常
            viewHolder.type_yc.setText( "异常");
            viewHolder.type_yc.setTextColor(Color.parseColor("#F46666"));
        }else {//0,1都是后台给的数据，所以都是未检查的.本地设置未检查的就是值为1
            viewHolder.type_yc.setText("未检查");
            viewHolder.type_yc.setTextColor(Color.parseColor("#B4B4B4"));
        }
        //设置检查项目的数据
        final List<TaskPreviewData.ItemsBean.ChildBean> licitme =rulesContent.getChild();
        CommonAdapter   commonAdapter = new CommonAdapter<TaskPreviewData.ItemsBean.ChildBean>(context, licitme, R.layout.task_preview_citem_) {
            @Override
            public void convert(ViewHolder mHolder, TaskPreviewData.ItemsBean.ChildBean item, int position) {
                mHolder.setText(R.id.task_preview_citem_name, item.getName());
                if (item.getStatus()==2) {//正常
                    mHolder.setTextColor(R.id.task_preview_citem_status, "正常",Color.parseColor("#92C35C"));
                } else if(item.getStatus()==3) {//异常
                    mHolder.setTextColor(R.id.task_preview_citem_status, "异常",Color.parseColor("#F46666"));
                }else {//0,1都是后台给的数据，所以都是未检查的
                    mHolder.setTextColor(R.id.task_preview_citem_status, "未检查",Color.parseColor("#B4B4B4"));
                }
            }
        };
        viewHolder.lvTitleContent.setAdapter(commonAdapter);
        viewHolder.lvTitleContent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Bundle bundle = new Bundle();
                bundle.putInt("taskid", licitme.get(i).getTaskid());//区分任务
                bundle.putInt("did", licitme.get(i).getDid());//区分点位
                bundle.putInt("pid", licitme.get(i).getPid());//区分当前部位
                bundle.putInt("cid", licitme.get(i).getCid());//区分当前项
                bundle.putString("name", licitme.get(i).getName());//当前项目名称
                Intent intent = new Intent(context, TaskPreviewCheckItemActivity.class);
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });
        return convertView;
    }

    public class PersonViewHolder {
        private TextView tv_group_name_rigth;
        private ListViewForScrollView lvTitleContent;
        private TextView type_yc;
        private ImageView image;
    }

}
