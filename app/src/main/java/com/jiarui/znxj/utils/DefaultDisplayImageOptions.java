package com.jiarui.znxj.utils;

import android.content.Context;
import android.graphics.Bitmap;

import com.jiarui.znxj.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;


/**
 * ImageLoader中加载图片的设置
 *
 * @author Only You
 * @version 1.0
 * @date 2016年1月13日
 */
public class DefaultDisplayImageOptions {
    /**
     * 带圆角的图片参数选项设置
     *
     * @param context 上下文对象
     * @return 图片参数设置
     */
    public static DisplayImageOptions getDefaultDisplayImageOptionsRounded(Context context) {
        // 返回图片加载前的默认值
        return new DisplayImageOptions.Builder().showImageOnLoading(R.mipmap.default_icon)
                .showImageForEmptyUri(R.mipmap.default_icon)
                .showImageOnFail(R.mipmap.default_icon)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(false)
                        // 设置图片圆角度
                .displayer(new RoundedBitmapDisplayer(context.getResources().getDimensionPixelSize(R.dimen.icon_rounded)))
                .build();
    }

    public static DisplayImageOptions getDefaultDisplayImageOptionsRounded(Context context, int round) {
        // 返回图片加载前的默认值
        return new DisplayImageOptions.Builder().showImageOnLoading(R.mipmap.defult_header)
                .showImageForEmptyUri(R.mipmap.defult_header)
                .showImageOnFail(R.mipmap.defult_header)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .considerExifParams(false)
                .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
                        // 返回图片加载前的默认值
                .displayer(new RoundedBitmapDisplayer(round))
                .build();
    }

    /**
     * 不带圆角的图片参数选项设置
     *
     * @param context 上下文对象
     * @return 图片参数设置
     */
    public static DisplayImageOptions getDefaultDisplayImageOptions(Context context) {
        // 返回图片加载前的默认值
        return new DisplayImageOptions.Builder().showImageOnLoading(R.mipmap.default_icon)
                .showImageForEmptyUri(R.mipmap.default_icon)
                .showImageOnFail(R.mipmap.default_icon)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .considerExifParams(false)
                .build();
    }
}
