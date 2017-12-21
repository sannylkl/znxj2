package com.jiarui.znxj.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.jiarui.znxj.R;
import com.jiarui.znxj.bean.Group_item_gridBean;

import java.util.List;

/**
 * 项目检查照片视频gridview
 *
 * @author lzh
 * @version 1.0
 * @date 2017/1/10 14:10
 */
public class Group_item_grid extends BaseAdapter {

    private Context mcontext;

    private List<Group_item_gridBean> mdata;

    public Group_item_grid(Context mcontext, List<Group_item_gridBean> mdata) {
        this.mcontext = mcontext;
        this.mdata = mdata;
    }

    @Override
    public int getCount() {
        return mdata.size();
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
            convertView = LayoutInflater.from(mcontext).inflate(R.layout.group_item_grid, null);
            myHolder.group_item_grid= (ImageView) convertView.findViewById(R.id.group_item_grid);
            convertView.setTag(myHolder);
        } else {
            myHolder = (MyHolder) convertView.getTag();
        }
        myHolder.group_item_grid.setImageResource(mdata.get(position).getImage());
        return convertView;
    }

    class MyHolder {
        ImageView group_item_grid;
    }
}
