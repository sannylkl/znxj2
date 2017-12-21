package com.jiarui.znxj.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jiarui.znxj.R;
import com.jiarui.znxj.bean.AAA;
import com.jiarui.znxj.bean.MemberRules;
import com.jiarui.znxj.bean.TaskPreviewData;
import com.jiarui.znxj.widget.ListViewForScrollView;

import java.util.ArrayList;
import java.util.List;

/**
 * 任务预览项目点位数据展示 2017/9/22 0022.
 */

public class MemberTypeAdapter extends BaseAdapter {
    private List<TaskPreviewData> liConte;
    private Context context;

    public MemberTypeAdapter(List<TaskPreviewData> liConte, Context context) {
        this.liConte = liConte;
        this.context = context;
    }

    @Override
    public int getCount() {
        return liConte == null ? 0 : liConte.size();
    }

    @Override
    public Object getItem(int position) {
        if (liConte == null) {
            return 0;
        }
        return liConte.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        PersonViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new PersonViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_member_type, parent, false);
            viewHolder.tv_group_name_rigth = (TextView) convertView.findViewById(R.id.tv_group_name_rigth);
            viewHolder.lvTitleContent = (ListViewForScrollView) convertView.findViewById(R.id.lv_title_content);
            viewHolder.type_yc= (TextView) convertView.findViewById(R.id.type_yc);
            viewHolder.image = (ImageView) convertView.findViewById(R.id.image);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (PersonViewHolder) convertView.getTag();
        }


        //设置点位的数据
        TaskPreviewData memberType = liConte.get(position);
        if (memberType.isCheck()) {
            viewHolder.lvTitleContent.setVisibility(View.VISIBLE);
            viewHolder.image.setImageResource(R.mipmap.more_icon);
        } else {
            viewHolder.lvTitleContent.setVisibility(View.GONE);
            viewHolder.image.setImageResource(R.mipmap.less_icon);
        }
        viewHolder.tv_group_name_rigth.setText(memberType.getName());
        if (memberType.getSatus()==2) {//正常
            viewHolder.type_yc.setText( "正常");
            viewHolder.type_yc.setTextColor(Color.parseColor("#92C35C"));
        } else if(memberType.getSatus()==3) {//异常
            viewHolder.type_yc.setText( "异常");
            viewHolder.type_yc.setTextColor(Color.parseColor("#F46666"));
        }else {//0,1都是后台给的数据，所以都是未检查的.本地设置未检查的就是值为1
            viewHolder.type_yc.setText("未检查");
            viewHolder.type_yc.setTextColor(Color.parseColor("#B4B4B4"));
        }
        //设置检查部位的数据
        final List<TaskPreviewData.ItemsBean> liCon =memberType.getItems();
        final MemberRulesAdapter adapter = new MemberRulesAdapter(memberType.getItems(), context);
        viewHolder.lvTitleContent.setAdapter(adapter);
        viewHolder.lvTitleContent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (liCon.get(i).isCheck() == true) {
                    liCon.get(i).setCheck(false);
                } else {
                    liCon.get(i).setCheck(true);
                }
                adapter.notifyDataSetChanged();
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
