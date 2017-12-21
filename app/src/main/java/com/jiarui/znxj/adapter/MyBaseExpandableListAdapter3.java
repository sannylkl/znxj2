package com.jiarui.znxj.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jiarui.znxj.R;
import com.jiarui.znxj.bean.Group;
import com.jiarui.znxj.bean.Group_itemBean;
import com.jiarui.znxj.bean.Group_item_gridBean;
import com.jiarui.znxj.widget.CustomGridView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jay on 2015/9/25 0025.
 */
public class MyBaseExpandableListAdapter3 extends BaseExpandableListAdapter {

    private ArrayList<Group> gData;
    private ArrayList<ArrayList<Group_itemBean>> iData;
    private Context mContext;

    public MyBaseExpandableListAdapter3(ArrayList<Group> gData, ArrayList<ArrayList<Group_itemBean>> iData, Context mContext) {
        this.gData = gData;
        this.iData = iData;
        this.mContext = mContext;
    }

    @Override
    public int getGroupCount() {
        return gData.size();

    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return iData.get(groupPosition).size();
    }

    @Override
    public Group getGroup(int groupPosition) {
        return gData.get(groupPosition);
    }

    @Override
    public Group_itemBean getChild(int groupPosition, int childPosition) {
        return iData.get(groupPosition).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    //取得用于显示给定分组的视图. 这个方法仅返回分组的视图对象
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        ViewHolderGroup groupHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_preview_group, parent, false);
            groupHolder = new ViewHolderGroup();
            groupHolder.tv_group_name = (TextView) convertView.findViewById(R.id.tv_group_name);
            groupHolder.tv_group_name_rigth = (TextView) convertView.findViewById(R.id.tv_group_name_rigth);
            groupHolder.image = (ImageView) convertView.findViewById(R.id.image);
            if (gData.get(groupPosition).getrName().equals("正常")) {
                groupHolder.tv_group_name_rigth.setTextColor(Color.parseColor("#A2CB77"));
            } else if (gData.get(groupPosition).getrName().equals("异常")) {
                groupHolder.tv_group_name_rigth.setTextColor(Color.parseColor("#F46666"));
            }
            convertView.setTag(groupHolder);
        } else {
            groupHolder = (ViewHolderGroup) convertView.getTag();
        }
        groupHolder.image.setImageResource(R.mipmap.more_icon);
        // 更换展开分组图片
        if (!isExpanded) {
            groupHolder.image.setImageResource(R.mipmap.less_icon);
        }
        groupHolder.tv_group_name.setText(gData.get(groupPosition).getgName());
        groupHolder.tv_group_name_rigth.setText(gData.get(groupPosition).getrName());
        return convertView;
    }

    //取得显示给定分组给定子位置的数据用的视图
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ViewHolderItem itemHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_preview_item, parent, false);
            itemHolder = new ViewHolderItem();
            itemHolder.tv_group_name = (TextView) convertView.findViewById(R.id.tv_group_name);
            //itemHolder.tv_group_context = (TextView) convertView.findViewById(R.id.tv_group_context);
            itemHolder.image_item = (ImageView) convertView.findViewById(R.id.image_lift);
         //   itemHolder.preview_grid = (CustomGridView) convertView.findViewById(R.id.preview_grid);
            convertView.setTag(itemHolder);
        } else {
            itemHolder = (ViewHolderItem) convertView.getTag();
        }
        itemHolder.tv_group_name.setText(iData.get(groupPosition).get(childPosition).getName());
        itemHolder.tv_group_context.setText(iData.get(groupPosition).get(childPosition).getCont());
        itemHolder.image_item.setImageResource(iData.get(groupPosition).get(childPosition).getImage());
        List<Group_item_gridBean> arrayList = iData.get(groupPosition).get(childPosition).getBean();
        if (iData.get(groupPosition).get(childPosition).getCont()!=null){
            itemHolder.tv_group_context.setVisibility(View.VISIBLE);
            itemHolder.preview_grid.setVisibility(View.GONE);
        }else if (iData.get(groupPosition).get(childPosition).getBean()!=null){
            itemHolder.tv_group_context.setVisibility(View.GONE);
            itemHolder.preview_grid.setVisibility(View.VISIBLE);
        }
        if (arrayList != null) {
            Group_item_grid group_item_grid = new Group_item_grid(mContext, arrayList);
            itemHolder.preview_grid.setAdapter(group_item_grid);
        }
        return convertView;
    }

    //设置子列表是否可选中
    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    private static class ViewHolderGroup {
        private TextView tv_group_name;
        private TextView tv_group_name_rigth;
        private ImageView image;
    }

    private static class ViewHolderItem {
        private TextView tv_group_name;
        private TextView tv_group_context;
        private ImageView image_item;
        private CustomGridView preview_grid;
    }
}
