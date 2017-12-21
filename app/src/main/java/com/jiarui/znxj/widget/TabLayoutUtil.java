package com.jiarui.znxj.widget;

import android.content.res.Resources;
import android.support.design.widget.TabLayout;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;

import java.lang.reflect.Field;

/**
 * 设置TabLayout下划线长度
 *
 * @author yangfei
 */
public class TabLayoutUtil {

    public static void setIndicatorDip(TabLayout mTabLayout, int leftDip, int rightDip) {
        Class<?> tabLayout = mTabLayout.getClass();
        Field mField = null;
        try {
            mField = tabLayout.getDeclaredField("mTabStrip");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        mField.setAccessible(true);
        LinearLayout mLinearLayout = null;
        try {
            mLinearLayout = (LinearLayout) mField.get(mTabLayout);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        int left = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, leftDip, Resources.getSystem().getDisplayMetrics());
        int right = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, rightDip, Resources.getSystem().getDisplayMetrics());

        for (int i = 0; i < mLinearLayout.getChildCount(); i++) {
            View child = mLinearLayout.getChildAt(i);
            child.setPadding(0, 0, 0, 0);
            LinearLayout.LayoutParams mParams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1);
            mParams.leftMargin = left;
            mParams.rightMargin = right;
            child.setLayoutParams(mParams);
            child.invalidate();
        }
    }

    public static void setIndicatorPx(TabLayout mTabLayout, int leftPx, int rightPx) {
        Class<?> tabLayout = mTabLayout.getClass();
        Field mField = null;
        try {
            mField = tabLayout.getDeclaredField("mTabStrip");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        mField.setAccessible(true);

        LinearLayout mLinearLayout = null;
        try {
            mLinearLayout = (LinearLayout) mField.get(mTabLayout);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        int left = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, leftPx, Resources.getSystem().getDisplayMetrics());
        int right = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, rightPx, Resources.getSystem().getDisplayMetrics());

        for (int i = 0; i < mLinearLayout.getChildCount(); i++) {
            View child = mLinearLayout.getChildAt(i);
            child.setPadding(0, 0, 0, 0);
            LinearLayout.LayoutParams mParams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1);
            mParams.leftMargin = left;
            mParams.rightMargin = right;
            child.setLayoutParams(mParams);
            child.invalidate();
        }
    }
}