package com.jiarui.znxj.holder;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.jiarui.znxj.R;
import com.jiarui.znxj.grobal.CommonListener;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;


public class ViewHolder {
    private final SparseArray<View> mViews;

    private int mPosition;

    private View mConvertView;

    private ViewHolder(Context context, ViewGroup parent, int layoutId, int position) {
        mPosition = position;
        mViews = new SparseArray<View>();
        mConvertView = LayoutInflater.from(context).inflate(layoutId, parent, false);
        mConvertView.setTag(this);
    }

    public static ViewHolder get(Context context, View convertView, ViewGroup parent, int layoutId, int position) {
        if (convertView == null) {
            return new ViewHolder(context, parent, layoutId, position);
        }
        return (ViewHolder) convertView.getTag();
    }

    public View getConvertView() {
        return mConvertView;
    }

    @SuppressWarnings("unchecked")
    public <T extends View> T getView(int viewId) {
        View view = mViews.get(viewId);
        if (view == null) {
            view = mConvertView.findViewById(viewId);
            mViews.put(viewId, view);
        }
        return (T) view;
    }

    /**
     * 设置文本信息
     *
     * @param viewId 控件的id
     * @param text   文本值
     * @return
     */
    public ViewHolder setText(int viewId, String text) {
        TextView view = getView(viewId);
        view.setText(text + "");
        return this;
    }

    public ViewHolder setSelected(int viewId, boolean b) {
        View view = getView(viewId);
        view.setSelected(b);
        return this;
    }

    public ViewHolder setChecked(int viewId, boolean b) {
        CheckBox view = getView(viewId);
        view.setChecked(b);
        return this;
    }

    public ViewHolder setTextHtml(int viewId, String text) {
        TextView view = getView(viewId);
        String source = com.jiarui.znxj.utils.StringUtil.htmlEscapeCharsToString(text);
        view.setText(Html.fromHtml(source));
        return this;
    }

    /**
     * 设置部分文字颜色
     *
     * @param viewId 控件id
     * @param text   文本值
     * @param start  开始位置
     * @param end    结束位置
     * @return
     */
    public ViewHolder setTextandColor(int viewId, String text, int start, int end) {
        TextView view = getView(viewId);
        SpannableStringBuilder style = new SpannableStringBuilder(text);
        style.setSpan(new ForegroundColorSpan(Color.RED), start, end, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        view.setText(style);
        return this;
    }

    /**
     * 设置控件的文本颜色
     *
     * @param viewId 控件id
     * @param color  颜色id
     * @return
     */
    public ViewHolder setTextColor(int viewId, int color) {
        TextView view = getView(viewId);
        view.setTextColor(color);
        return this;
    }

    /**
     * 设置控件的文本颜色
     *
     * @param viewId 控件id
     * @param text   文本值
     * @param color  颜色id
     * @return
     */
    public ViewHolder setTextColor(int viewId, String text, int color) {
        TextView view = getView(viewId);
        view.setText(text);
        view.setTextColor(color);
        return this;
    }

    /**
     * 设置某个控件的状态，显示或者隐藏
     *
     * @param viewId     控件id
     * @param Visibility 显示状态
     * @return
     */
    public ViewHolder setVisibility(int viewId, int Visibility) {
        View view = getView(viewId);
        view.setVisibility(Visibility);
        return this;
    }

    /**
     * 设置某个控件的背景
     *
     * @param viewId     控件id
     * @param Visibility 显示状态
     * @return
     */
    public ViewHolder setBackgroundResource(int viewId, int drawable) {
        View view = getView(viewId);
        view.setBackgroundResource(drawable);
        return this;
    }



    /**
     * 根据图片id给ImageView控件设置图片
     *
     * @param viewId     控件id
     * @param drawableId 图片id
     * @return
     */
    public ViewHolder setImageResource(int viewId, int drawableId) {
        ImageView view = getView(viewId);
        view.setImageResource(drawableId);
        return this;
    }

    public ViewHolder setOnClickListener(int viewId, final int position, final CommonListener commonListener) {
        View view = getView(viewId);
        view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                commonListener.commonListener(v, position);
            }
        });
        return this;
    }

    /**
     * 根据图片id给ImageView控件设置图片
     *
     * @param viewId 控件id
     * @param bm     bitmap
     * @return
     */
    public ViewHolder setImageBitmap(int viewId, Bitmap bm) {
        ImageView view = getView(viewId);
        view.setImageBitmap(bm);
        return this;
    }

    /**
     * @param viewId  图片控件id
     * @param url     图片地址
     * @param context 上下文对象
     * @param options 图片参数的选项
     * @return
     */
    public ViewHolder setImageByUrl(final int viewId, String url, Context context, DisplayImageOptions options) {
        ImageLoader.getInstance().displayImage(url, (ImageView) getView(viewId), options);
        return this;
    }

    public ViewHolder setImageAddPhoto(final int viewId, Context context) {
        ImageLoader.getInstance().displayImage("drawable://" + R.mipmap.tj, (ImageView) getView(viewId));
        return this;
    }

    public ViewHolder setImageByUrl(final int viewId, String url, Context context) {
        ImageLoader.getInstance().displayImage(url, (ImageView) getView(viewId));
        return this;
    }

    /**
     * @param viewId    控件id
     * @param url       图片地址
     * @param context   上下文对象
     * @param imageSize 图片的大小
     * @param options   图片参数的选项
     * @return
     */
    public ViewHolder setLoadImageUrl(final int viewId, String url, Context context, ImageSize imageSize, DisplayImageOptions options) {
        ImageLoader.getInstance().loadImage(url, imageSize, options, new SimpleImageLoadingListener() {
            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                super.onLoadingComplete(imageUri, view, loadedImage);
                ((ImageView) getView(viewId)).setImageBitmap(loadedImage);
            }
        });
        return this;
    }

    public int getPosition() {
        return mPosition;
    }
}