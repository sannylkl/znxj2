package com.jiarui.znxj.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;


/**
 * 重写ListView
 *
 * @author QangGe
 * @version 1.0
 * @date 2016年1月10日
 */


public class SameCityMylistview extends ListView {

    public SameCityMylistview(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // TODO Auto-generated method stub
        int height = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, height);
    }

}
