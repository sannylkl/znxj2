package com.jiarui.znxj.widget;


import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.jiarui.znxj.R;
import com.jiarui.znxj.application.AppContext;


/**
 * 上拉加载更多
 *
 * @author Only You
 * @version 1.0
 * @date 2016年1月24日
 */
public class AutoListView extends ListView implements OnScrollListener, OnItemClickListener {
    // 下拉刷新的标识
    public static final int REFRESH = 0;

    // 上拉加载更多的标识
    public static final int LOAD = 1;

    private LayoutInflater inflater;

    // 底部加载更多布局
    public View footer;

    // 提示已加载全部的TextView
    private TextView loadFull;

    // 提示加载中的TextView
    private TextView more;

    private TextView noNetwork;

    // 显示底部加载进度的ProgressBar
    private ProgressBar loading;

    // 判断是否正在加载
    private boolean isLoading;

    // 开启或者关闭加载更多功能，默认为开启状态
    private boolean loadEnable = true;

    // 是否已经加载false 表示未加满，true 表示已加满
    private boolean isLoadFull = true;

    // 每页数据条目
    private int pageSize = 10;

    private OnLoadListener onLoadListener;

    private SwipeRefreshLayout mSwipeRefreshLayout;

    public void setSwipeRefreshLayout(SwipeRefreshLayout mSwipeRefreshLayout) {
        this.mSwipeRefreshLayout = mSwipeRefreshLayout;
    }

    public AutoListView(Context context) {
        super(context);
        initView(context);
    }

    public AutoListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public AutoListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView(context);
    }

    // 加载更多监听
    public void setOnLoadListener(OnLoadListener onLoadListener) {
        this.loadEnable = true;
        this.onLoadListener = onLoadListener;
    }

    public boolean isLoadEnable() {
        return loadEnable;
    }

    // 这里的开启或者关闭加载更多，并不支持动态调整
    public void setLoadEnable(boolean loadEnable) {
        this.loadEnable = loadEnable;
        this.removeFooterView(footer);
    }

    public int getPageSize() {
        return pageSize;
    }

    /**
     * 设置分页数量默认为10条
     *
     * @param pageSize 每页数量
     */
    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    // 初始化组件
    private void initView(Context context) {

        inflater = LayoutInflater.from(context);
        footer = inflater.inflate(R.layout.listview_footer, null);
        loadFull = (TextView) footer.findViewById(R.id.loadFull);
        more = (TextView) footer.findViewById(R.id.more);
        noNetwork = (TextView) footer.findViewById(R.id.no_network);
        loading = (ProgressBar) footer.findViewById(R.id.loading);
        footer.setVisibility(View.GONE);
        this.addFooterView(footer, null, false);
        this.setOnScrollListener(this);
    }

    public void onLoad() {
        if (onLoadListener != null) {
            onLoadListener.onLoad();
        }
    }

    // 用于加载更多结束后的回调
    public void onLoadComplete() {
        isLoading = false;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        ifNeedLoad(view, scrollState);
    }

    // 根据listview滑动的状态判断是否需要加载更多
    private void ifNeedLoad(AbsListView view, int scrollState) {
        if (!loadEnable) {
            return;
        }
        try {
            if (scrollState == OnScrollListener.SCROLL_STATE_IDLE && !isLoading && view.getLastVisiblePosition() == view.getPositionForView(footer) && !isLoadFull) {
                onLoad();
                footer.setVisibility(View.VISIBLE);
                isLoading = true;
            }
        } catch (Exception e) {
        }
    }

    /**
     * 这个方法是根据结果的大小来决定footer显示的。
     * <p>
     * 这里假定每次请求的条数为10。如果请求到了10条。则认为还有数据。如过结果不足10条，则认为数据已经全部加载，这时footer显示已经全部加载
     * </p>
     *
     * @param resultSize
     */
    public void setResultSize(int resultSize) {
        footer.setVisibility(View.VISIBLE);
        //有网络
        if (AppContext.isNetworkConnected()) {
            //根据返回数据做判断
            if (resultSize == 0) {//返回数据为0
                // 已经加满
                if (isLoadFull == false) {
                    loadFull.setVisibility(View.VISIBLE);
                    loading.setVisibility(View.GONE);
                    more.setVisibility(View.GONE);
                    noNetwork.setVisibility(View.GONE);
                } else {
                    loadFull.setVisibility(View.GONE);
                    loading.setVisibility(View.GONE);
                    more.setVisibility(View.GONE);
                    noNetwork.setVisibility(View.GONE);
                }
                isLoadFull = true;
            } else if (resultSize > 0 && resultSize < pageSize) {//返回数据大于0单是小于每页返回数据
                isLoadFull = true;
                loadFull.setVisibility(View.VISIBLE);
                loading.setVisibility(View.GONE);
                more.setVisibility(View.GONE);
                noNetwork.setVisibility(View.GONE);
            } else if (resultSize == pageSize) {//返回数据等于每页返回数据
                isLoadFull = false;
                loadFull.setVisibility(View.GONE);
                loading.setVisibility(View.VISIBLE);
                more.setVisibility(View.VISIBLE);
                noNetwork.setVisibility(View.GONE);
            }
        } else {//没有网络
            isLoadFull = true;
            loadFull.setVisibility(View.GONE);
            loading.setVisibility(View.GONE);
            more.setVisibility(View.GONE);
            noNetwork.setVisibility(View.VISIBLE);
        }
    }

    /*
     * 定义加载更多接口
     */
    public interface OnLoadListener {
        public void onLoad();
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        boolean enable = false;
        if (this != null && getChildCount() > 0) {
            boolean firstItemVisible = getFirstVisiblePosition() == 0;
            boolean topOfFirstItemVisible = getChildAt(0).getTop() == 0;
            enable = firstItemVisible && topOfFirstItemVisible;
        }
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setEnabled(enable);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (view == footer) {
            return;
        }
    }
}
