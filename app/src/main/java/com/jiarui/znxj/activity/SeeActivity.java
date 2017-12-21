package com.jiarui.znxj.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.jiarui.znxj.R;
import com.jiarui.znxj.adapter.CommonAdapter;
import com.jiarui.znxj.application.AppContext;
import com.jiarui.znxj.base.BaseActivity;
import com.jiarui.znxj.bean.BaseBean;
import com.jiarui.znxj.bean.TaskCenter_listBean;
import com.jiarui.znxj.bean.TaslListBeanTable;
import com.jiarui.znxj.bean.Xj_TaskBean;
import com.jiarui.znxj.bean.xj_route_detailBean;
import com.jiarui.znxj.constants.GetToken;
import com.jiarui.znxj.constants.InterfaceDefinition;
import com.jiarui.znxj.holder.ViewHolder;
import com.jiarui.znxj.utils.DateUtil;
import com.jiarui.znxj.utils.DefaultCommonCallBack;
import com.jiarui.znxj.utils.LogUtil;
import com.jiarui.znxj.utils.PreferencesUtil;
import com.jiarui.znxj.utils.ToastUtil;
import com.jiarui.znxj.widget.ListViewForScrollView;

import org.xutils.DbManager;
import org.xutils.db.sqlite.WhereBuilder;
import org.xutils.ex.DbException;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/***
 * 任务详情
 */
public class SeeActivity extends BaseActivity {
    @ViewInject(R.id.see_xjlx)
    private TextView see_xjlx;//巡检类型
    @ViewInject(R.id.see_jcxm)
    private TextView see_jcxm;//检查项目
    @ViewInject(R.id.see_fbr)
    private TextView see_fbr;//发布人
    @ViewInject(R.id.see_starttime)
    private TextView see_starttime;//任务开始日期
    @ViewInject(R.id.see_endtime)
    private TextView see_endtime;//要求完成日期
    @ViewInject(R.id.see_wz)
    private TextView see_wz;//位置
    @ViewInject(R.id.see_sw)
    private TextView see_sw;//水位
    @ViewInject(R.id.see_tq)
    private TextView see_tq;//天气
    @ViewInject(R.id.see_xysw)
    private TextView see_xysw;//下游水位
    @ViewInject(R.id.task_list)
    private ListViewForScrollView task_list;//路线规划
    CommonAdapter<xj_route_detailBean> commonAdapter;//路线规划适配器，数据源在下面从表取出来
    int id;
    /*数据库*/
    DbManager dbManager = null;//AppContext.getInstance().getDbManager()

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see);
        x.view().inject(this);
        setTitle();
        mTvForTitle.setText("任务详情");
        dbManager = x.getDb(AppContext.getConfig());
        init();
    }


    /**
     * 初始化控件
     */
    public void init() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            id = bundle.getInt("id");//int类型
            Log.e("bundleid", ""+id);
            try {
                TaslListBeanTable taskbean = dbManager.selector(TaslListBeanTable.class).where("id", "=", id).findFirst();
                Log.e("taskbeanid", ""+taskbean.getId());
                see_xjlx.setText(taskbean.getTask_type());
                see_jcxm.setText(taskbean.getItems());
                see_fbr.setText(taskbean.getCreator());
                String date_star= DateUtil.timeStamp2Date(String.valueOf(taskbean.getStart_time()),"MM-dd");
                String date_end=DateUtil.timeStamp2Date(String.valueOf(taskbean.getEnd_time()),"MM-dd");
                see_starttime.setText(date_star);
                see_endtime.setText(date_end);
                see_wz.setText(taskbean.getReservoir());
                see_sw.setText(taskbean.getWater_line());
                see_tq.setText(taskbean.getWeather());
                see_xysw.setText(taskbean.getDown_water());
                Log.e("xj_routeid", ""+taskbean.getRoute_id());
                final List<xj_route_detailBean> xj_routelist = dbManager.selector(xj_route_detailBean.class).where("route_id", "=", taskbean.getRoute_id()).findAll();
                Log.e("xj_routelistSize", ""+xj_routelist.size());
                final List<xj_route_detailBean> xj_routeall = dbManager.selector(xj_route_detailBean.class).findAll();
                Log.e("xj_routeallSize", ""+xj_routeall.size());
                commonAdapter = new CommonAdapter<xj_route_detailBean>(SeeActivity.this, xj_routelist, R.layout.taskcenter_list_item) {
                    @Override
                    public void convert(ViewHolder mHolder, xj_route_detailBean item, int position) {
                        mHolder.setText(R.id.task_item_serial, ""+item.getLocation_id());
                        mHolder.setText(R.id.task_item_commis, "完成点位任务时间"+item.getFinished_time());
                        mHolder.setText(R.id.task_item_time, item.getNext_time()+"到达下一个点位");
                        if (position == 0) {
                            mHolder.setVisibility(R.id.text_shu1,View.INVISIBLE);
                            mHolder.setVisibility(R.id.text_shu2,View.VISIBLE);
                            mHolder.setVisibility(R.id.task_item_time,View.VISIBLE);
                        } else if (position == xj_routelist.size() - 1) {
                            mHolder.setVisibility(R.id.text_shu1,View.VISIBLE);
                            mHolder.setVisibility(R.id.text_shu2,View.INVISIBLE);
                            mHolder.setVisibility(R.id.task_item_time,View.GONE);
                        }else {
                            mHolder.setVisibility(R.id.text_shu1,View.VISIBLE);
                            mHolder.setVisibility(R.id.text_shu2,View.VISIBLE);
                            mHolder.setVisibility(R.id.task_item_time,View.VISIBLE);
                        }
                    }
                };
                task_list.setAdapter(commonAdapter);
            } catch (DbException e) {
                e.printStackTrace();
            }
        }else{
            Log.e("bundle", "bundle=null");
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
