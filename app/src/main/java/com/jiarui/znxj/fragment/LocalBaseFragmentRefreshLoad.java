package com.jiarui.znxj.fragment;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.util.Log;
import android.view.View;

import com.jiarui.znxj.Interface.TackListLocalInterface;
import com.jiarui.znxj.R;
import com.jiarui.znxj.base.BaseFragment;
import com.jiarui.znxj.constants.Constants;
import com.jiarui.znxj.constants.InterfaceDefinition;
import com.jiarui.znxj.utils.LogUtil;
import com.jiarui.znxj.utils.StringUtil;
import com.jiarui.znxj.widget.AutoListView;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;

import java.io.UnsupportedEncodingException;


/**
 * 用于下拉刷新，上拉底部加载的Fragment
 *
 * @author Only You
 * @version 1.0
 * @date 2016年1月27日
 */
public abstract class LocalBaseFragmentRefreshLoad extends BaseFragment implements
        OnRefreshListener, AutoListView.OnLoadListener
        {
            private static String TAG = BaseFragmentRefreshLoad.class.getSimpleName();

            protected int mWhat;

            protected String mCacheUrl;

            protected SwipeRefreshLayout mSwipeRefreshLayout = null;

            protected View EmptyView = null;

            protected AutoListView mAutoListView = null;

            public void setBaseRequestParams(String CacheUrl, int what) {
                mCacheUrl = CacheUrl;
                mWhat = what;
            }

            @Override
            public void onLoad() {
                LoadResultData(AutoListView.LOAD);
            }

            public void onRefresh() {
                LoadResultData(AutoListView.REFRESH);
            }

            /**
             * 设置下拉刷新，上拉底部加载的listview
             */
            protected void setRefreshLoadListView() {
                mSwipeRefreshLayout = (SwipeRefreshLayout) mRootView
                        .findViewById(R.id.swipeRefreshLayout);
                EmptyView = mRootView.findViewById(R.id.common_layout_no_data);
                mAutoListView = (AutoListView) mRootView
                        .findViewById(R.id.autoListView);
                mSwipeRefreshLayout.setColorSchemeResources(R.color.red, R.color.pink,
                        R.color.blue, R.color.gray);
                // 设置下拉刷新数据和上拉加载数据的监听事件
                mSwipeRefreshLayout.setOnRefreshListener(this);
                mAutoListView.setOnLoadListener(this);
                setRefreshLayout();
            }

            protected void setRefreshLayout() {
                // 设置一开始进入界面时进行刷新
                mSwipeRefreshLayout.post(new Runnable() {
                    @Override
                    public void run() {
                        mSwipeRefreshLayout.setRefreshing(true);
                    }
                });
                // 初次加载时隐藏底部FooterView
                mAutoListView.footer.setVisibility(View.GONE);
                mAutoListView.setSwipeRefreshLayout(mSwipeRefreshLayout);
            }

            public void onFinish(){
                // 停止刷新
                mSwipeRefreshLayout.setRefreshing(false);
                // 停止底部加载
                mAutoListView.onLoadComplete();
                // 更新数据
                notifyDataSetChanged();
            }


//            @Override
//            public void onFinished() {

//            }

            /**
             * 清除数据集合的方法
             */
            abstract void clearResultData();

            /**
             * 处理数据的方法;该方法放置解析数据的操作
             */
            abstract void disposeResultData();

            /**
             * 刷新数据的
             */
            abstract void notifyDataSetChanged();

            /**
             * 请求数据的方法
             *
             * @param what 标识
             */
            abstract void LoadResultData(int what);
        }
