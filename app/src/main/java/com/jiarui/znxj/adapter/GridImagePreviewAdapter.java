package com.jiarui.znxj.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.jiarui.znxj.R;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.tools.DateUtils;
import com.luck.picture.lib.tools.DebugUtil;
import com.luck.picture.lib.tools.StringUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * author：luck
 * project：PictureSelector
 * package：com.luck.pictureselector.adapter
 * email：893855882@qq.com
 * data：16/7/27
 * 图片和视频预览
 */
public class GridImagePreviewAdapter extends
        RecyclerView.Adapter<GridImagePreviewAdapter.ViewHolder> {
    private LayoutInflater mInflater;
    private List<LocalMedia> list = new ArrayList<>();
    private Context context;
    /**
     * 点击添加图片跳转
     */
    private onAddPicClickListener mOnAddPicClickListener;

    public interface onAddPicClickListener {
        void onAddPicClick(boolean isPV);
    }

    public GridImagePreviewAdapter(Context context) {
        this.context = context;
        mInflater = LayoutInflater.from(context);
    }

    public GridImagePreviewAdapter(Context context, onAddPicClickListener mOnAddPicClickListener) {
        this.context = context;
        mInflater = LayoutInflater.from(context);
        this.mOnAddPicClickListener = mOnAddPicClickListener;
    }

    public void setList(List<LocalMedia> list) {
        this.list = list;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView mImg;
        LinearLayout ll_del;
        TextView tv_duration;

        public ViewHolder(View view) {
            super(view);
            mImg = (ImageView) view.findViewById(R.id.fiv);
            ll_del = (LinearLayout) view.findViewById(R.id.ll_del);
            tv_duration = (TextView) view.findViewById(R.id.tv_duration);
        }
    }

    @Override
    public int getItemCount() {
            return list.size();
    }

    @Override
    public int getItemViewType(int position) {
//        if (isShowAddItem(position)) {
//            return TYPE_CAMERA;
//        } else {
//            return TYPE_PICTURE;
//        }
        //随便返回的值，对于只是预览没什么用的
        return 2;

    }

    /**
     * 创建ViewHolder
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = mInflater.inflate(R.layout.gv_filter_image, viewGroup, false);
        final ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    private boolean isShowAddItem(int position) {
        int size = list.size() == 0 ? 0 : list.size();
        return position == size;
    }

    /**
     * 设置值
     */
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        viewHolder.ll_del.setVisibility(View.INVISIBLE);
        LocalMedia media = list.get(position);
        int mimeType = media.getMimeType();
        String path = "";
        if (media.isCut() && !media.isCompressed()) {
            // 裁剪过
            path = media.getCutPath();
        } else if (media.isCompressed() || (media.isCut() && media.isCompressed())) {
            // 压缩过,或者裁剪同时压缩过,以最终压缩过图片为准
            path = media.getCompressPath();
        } else {
            // 原图
            path = media.getPath();
        }
        // 图片
        if (media.isCompressed()) {
            Log.i("compress image result:", new File(media.getCompressPath()).length() / 1024 + "k");
            Log.i("压缩地址::", media.getCompressPath());
        }

        Log.i("原图地址::", media.getPath());
        int pictureType = PictureMimeType.isPictureType(media.getPictureType());
        if (media.isCut()) {
            Log.i("裁剪地址::", media.getCutPath());
        }
        long duration = media.getDuration();
        viewHolder.tv_duration.setVisibility(pictureType == PictureConfig.TYPE_VIDEO
                ? View.VISIBLE : View.GONE);
        if (mimeType == PictureMimeType.ofAudio()) {
            viewHolder.tv_duration.setVisibility(View.VISIBLE);
            Drawable drawable = ContextCompat.getDrawable(context, R.drawable.picture_audio);
            StringUtils.modifyTextViewDrawable(viewHolder.tv_duration, drawable, 0);
        } else {
            Drawable drawable = ContextCompat.getDrawable(context, R.drawable.video_icon);
            StringUtils.modifyTextViewDrawable(viewHolder.tv_duration, drawable, 0);
        }
        viewHolder.tv_duration.setText(DateUtils.timeParse(duration));
        if (mimeType == PictureMimeType.ofAudio()) {
            viewHolder.mImg.setImageResource(R.drawable.audio_placeholder);
        } else {
            RequestOptions options = new RequestOptions()
                    .centerCrop()
                    .placeholder(R.color.color_55)
                    .diskCacheStrategy(DiskCacheStrategy.ALL);
            Glide.with(viewHolder.itemView.getContext())
                    .load(path)
                    .apply(options)
                    .into(viewHolder.mImg);
        }
        //itemView 的点击事件
        if (mItemClickListener != null) {
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int adapterPosition = viewHolder.getAdapterPosition();
                    mItemClickListener.onItemClick(adapterPosition, v);
                }
            });
        }
    }

    protected OnItemClickListener mItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(int position, View v);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mItemClickListener = listener;
    }
}
