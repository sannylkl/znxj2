package com.jiarui.znxj.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.jiarui.znxj.R;
import com.jiarui.znxj.bean.FlowViewBeanIndex;
import com.jiarui.znxj.utils.DefaultDisplayImageOptions;
import com.jiarui.znxj.widget.AdapterOnclick;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

public class ImageAdapter extends BaseAdapter {
    private Context mContext;

    private List<FlowViewBeanIndex> mdata;

    private AdapterOnclick mclick;

    public AdapterOnclick getMclick() {
        return mclick;
    }

    public void setMclick(AdapterOnclick mclick) {
        this.mclick = mclick;
    }

    public ImageAdapter(Context context, List<FlowViewBeanIndex> data) {
        this.mContext = context;
        this.mdata = data;
    }

    @Override
    public int getCount() {
        return this.mdata == null ? 0 : Integer.MAX_VALUE;
    }

    @Override
    public FlowViewBeanIndex getItem(int arg0) {
        return mdata.get(arg0 % mdata.size());
    }

    @Override
    public long getItemId(int arg0) {
        return arg0 % mdata.size();
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewgroup) {
        ViewHolder mHolder = null;
        if (view == null) {
            mHolder = new ViewHolder();
            view = LayoutInflater.from(mContext).inflate(
                    R.layout.act_iamge_item, null);
            mHolder.mImgv = (ImageView) view.findViewById(R.id.imgv);
            mHolder.mImgv.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (mclick != null) {
                        mclick.adapterClickx(1, position);
                    }
                }
            });
            view.setTag(mHolder);
        } else {
            mHolder = (ViewHolder) view.getTag();
        }
        if (this.mdata != null) {

            ImageLoader.getInstance().displayImage(
                    getItem(position % mdata.size()).getImg(),
                    mHolder.mImgv,
                    DefaultDisplayImageOptions
                            .getDefaultDisplayImageOptions(mContext));

        }

        return view;
    }

    class ViewHolder {
        ImageView mImgv = null;
    }
}
