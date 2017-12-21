package com.jiarui.znxj.activity;

import android.graphics.Canvas;
import android.os.Bundle;
import android.view.View;

import com.jiarui.znxj.R;
import com.jiarui.znxj.base.BaseActivity;
import com.jiarui.znxj.utils.PreferencesUtil;
import com.joanzapata.pdfview.PDFView;
import com.joanzapata.pdfview.listener.OnDrawListener;
import com.joanzapata.pdfview.listener.OnLoadCompleteListener;
import com.joanzapata.pdfview.listener.OnPageChangeListener;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.File;

/**
 * 数据查询详情
 */
public class DataDetailsActivity extends BaseActivity implements View.OnClickListener, OnPageChangeListener, OnLoadCompleteListener, OnDrawListener {
    @ViewInject(R.id.pdfview)
    private PDFView pdfview;//pdf查看器
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_details);
        x.view().inject(this);
        setTitle();
        mImgvForLeft.setOnClickListener(this);
        mTvForTitle.setText("巡检报告");
        String pdf = (String) PreferencesUtil.get(DataDetailsActivity.this, "pdf", "");
        File f = new File(pdf);
        displayFromFile(f);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.common_title_left:
                finish();
                break;
        }
    }

    private void displayFromFile(File file) {
        pdfview.fromFile(file)   //设置pdf文件地址
                .defaultPage(6)         //设置默认显示第1页
                .onPageChange(this)     //设置翻页监听
                .onLoad(this)           //设置加载监听
                .onDraw(this)            //绘图监听
                .showMinimap(false)     //pdf放大的时候，是否在屏幕的右上角生成小地图
                .swipeVertical(false)  //pdf文档翻页是否是垂直翻页，默认是左右滑动翻页
                .enableSwipe(true)   //是否允许翻页，默认是允许翻
                // .pages( 2 ，5  )  //把2  5 过滤掉
                .load();
    }

    @Override
    public void onLayerDrawn(Canvas canvas, float pageWidth, float pageHeight, int displayedPage) {

    }

    @Override
    public void loadComplete(int nbPages) {

    }

    @Override
    public void onPageChanged(int page, int pageCount) {

    }
}
