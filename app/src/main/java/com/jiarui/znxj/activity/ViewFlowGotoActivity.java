package com.jiarui.znxj.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jiarui.znxj.R;
import com.jiarui.znxj.base.BaseActivity;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;


public class ViewFlowGotoActivity extends BaseActivity {

    @ViewInject(R.id.content)
    private TextView contents;

    @ViewInject(R.id.img)
    private ImageView imgs;

    private String title;
    private String img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_viewflow_goto);
        x.view().inject(this);
        setTitle();
        mTvForTitle.setText("广告页");
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            title = bundle.getString("title");
            img = bundle.getString("img");
            contents.setText(title);
            ImageLoader.getInstance().displayImage(img, imgs);
        }
    }

    @Event({R.id.common_title_left})
    private void onClick(View v) {
        switch (v.getId()) {
            case R.id.common_title_left:
                finish();
                break;

            default:
                break;
        }
    }

}
