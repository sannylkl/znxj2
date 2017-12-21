package com.jiarui.znxj.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.jiarui.znxj.R;
import com.jiarui.znxj.bean.AAA;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.List;

/**
 * Created by Administrator on 2017/9/22 0022.
 */

public class RulesContentAdapter extends BaseAdapter {
    private List<AAA.ListBean.ContentBean.ImageBean> lstContent;
    private Context context;

    public RulesContentAdapter(List<AAA.ListBean.ContentBean.ImageBean> lstContent, Context context) {
        this.lstContent = lstContent;
        this.context = context;
    }

    @Override
    public int getCount() {
        return lstContent == null ? 0 : lstContent.size();
    }

    @Override
    public Object getItem(int position) {
        if (lstContent == null) {
            return 0;
        }
        return lstContent.get(position);
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_preview_item, parent, false);
            viewHolder.image_lift= (ImageView) convertView.findViewById(R.id.image_lift);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (PersonViewHolder) convertView.getTag();
        }

        AAA.ListBean.ContentBean.ImageBean content = lstContent.get(position);

        ImageLoader.getInstance().init(
                ImageLoaderConfiguration.createDefault(context));
        ImageLoader.getInstance().displayImage(content.getUrl(),  viewHolder.image_lift, null,
                new SimpleImageLoadingListener());
        return convertView;
    }

    public class PersonViewHolder {

        private ImageView image_lift;
    }
}
