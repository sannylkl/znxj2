package com.jiarui.znxj.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jiarui.znxj.R;
import com.jiarui.znxj.bean.Group;
import com.jiarui.znxj.bean.Item;

import java.util.ArrayList;

/**
 * Created by Jay on 2015/9/25 0025.
 */
public class MyBaseExpandableListAdapter extends BaseExpandableListAdapter {

    private ArrayList<Group> gData;
    private ArrayList<ArrayList<Item>> iData;
    private Context mContext;

    public MyBaseExpandableListAdapter(ArrayList<Group> gData, ArrayList<ArrayList<Item>> iData, Context mContext) {
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
    public Item getChild(int groupPosition, int childPosition) {
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_exlist_group, parent, false);
            groupHolder = new ViewHolderGroup();
            groupHolder.tv_group_name = (TextView) convertView.findViewById(R.id.tv_group_name);
            groupHolder.tv_group_name_rigth = (TextView) convertView.findViewById(R.id.tv_group_name_rigth);
            groupHolder.image = (ImageView) convertView.findViewById(R.id.image);
            convertView.setTag(groupHolder);
        } else {
            groupHolder = (ViewHolderGroup) convertView.getTag();
        }
        groupHolder.tv_group_name.setText(gData.get(groupPosition).getgName());
        groupHolder.tv_group_name_rigth.setText(gData.get(groupPosition).getrName());
        groupHolder.image.setImageResource(R.mipmap.more_icon);
        // 更换展开分组图片
        if (!isExpanded) {
            groupHolder.image.setImageResource(R.mipmap.less_icon);
        }
        return convertView;
    }

    //取得显示给定分组给定子位置的数据用的视图
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ViewHolderItem itemHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_exlist_item, parent, false);
            itemHolder = new ViewHolderItem();
            itemHolder.tv_group_name_itme = (TextView) convertView.findViewById(R.id.tv_group_name_itme);
            itemHolder.tv_group_name_itme_rigth = (TextView) convertView.findViewById(R.id.tv_group_name_itme_rigth);
            convertView.setTag(itemHolder);
        } else {
            itemHolder = (ViewHolderItem) convertView.getTag();
        }
        itemHolder.tv_group_name_itme.setText(iData.get(groupPosition).get(childPosition).getgNameitme());
        itemHolder.tv_group_name_itme_rigth.setText(iData.get(groupPosition).get(childPosition).getrNameitme());
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
        private TextView tv_group_name_itme;
        private TextView tv_group_name_itme_rigth;
    }

}
