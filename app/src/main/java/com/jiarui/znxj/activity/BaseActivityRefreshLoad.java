package com.jiarui.znxj.activity;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.jiarui.znxj.R;
import com.jiarui.znxj.base.BaseActivity;
import com.jiarui.znxj.constants.Constants;
import com.jiarui.znxj.constants.InterfaceDefinition;
import com.jiarui.znxj.fragment.BaseFragmentRefreshLoad;
import com.jiarui.znxj.utils.CacheUtils;
import com.jiarui.znxj.utils.LogUtil;
import com.jiarui.znxj.utils.StringUtil;
import com.jiarui.znxj.widget.AutoListView;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;

import java.io.UnsupportedEncodingException;

/**
 * 用于下拉刷新，上拉底部加载的Activity
 *
 * @author Only You
 * @version 1.0
 * @date 2016年1月27日
 */
public abstract class BaseActivityRefreshLoad extends BaseActivity implements
        OnRefreshListener, AutoListView.OnLoadListener,
        Callback.CommonCallback<String> {
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

    @Override
    public void onCancelled(CancelledException arg0) {

    }

    /**
     * 设置下拉刷新，上拉底部加载的listview
     */
    protected void setRefreshLoadListView() {
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        EmptyView = findViewById(R.id.common_layout_no_data);
        mAutoListView = (AutoListView) findViewById(R.id.autoListView);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.red, R.color.pink, R.color.blue, R.color.gray);
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

    @Override
    public void onError(Throwable arg0, boolean arg1) {
        Log.e("onError",""+arg0.toString());
        Toast.makeText(this, "请检查网络环境是否正常", Toast.LENGTH_SHORT).show();
        disposeCache();
    }

    /**
     * 处理缓存数据的方法
     */
    private void disposeCache() {
        try {
            if (mWhat == AutoListView.REFRESH) {
                clearResultData();
            }
            JSONObject mCacheJson = new JSONObject(CacheUtils.getCacheToLocalJson(mCacheUrl));
            disposeResultData(mCacheJson);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSuccess(String arg0) {
        LogUtil.e("arg0", ""+arg0);
        try {
            JSONObject rows = new JSONObject(new String(arg0.getBytes(),
                    Constants.CHARSET));
            LogUtil.e("分页数据rows", ""+rows);
            if (StringUtil.isJSONObjectNotEmpty(rows)) {
                String status = rows.optString("status");
//                String mMessage = rows.optJSONObject(InterfaceDefinition.IStatus.STATUS).getString(InterfaceDefinition.IStatus.MESSAGE);
//                Log.i(TAG, mMessage + ":" + mCode);
                if (InterfaceDefinition.IStatusCode.SUCCESS.equals(status)) {
                    // 后台响应成功
                    JSONObject mDataObject = rows.optJSONObject(InterfaceDefinition.ICommonKey.RESULTS);
                    if (StringUtil.isJSONObjectNotEmpty(mDataObject)) {
                        // 将数据写入缓存中
                        if (StringUtil.isNotEmpty(mCacheUrl)) {
                            CacheUtils.setCacheToLocalJson(mCacheUrl, rows.toString());
                        }
                        if (mWhat == AutoListView.REFRESH) {
                            clearResultData();
                        }
                        disposeResultData(rows);
                    }
                    notifyDataSetChanged();
                    return;
                }  else if(InterfaceDefinition.IStatusCode.TOKEN_FAILURE.equals(status)){
                    disposeResultData(rows);
                    return;
                }
                else {
                    // 其他错误
//                    Toast.makeText(this, mMessage, Toast.LENGTH_SHORT).show();
                    disposeCache();
                }
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFinished() {
        // 停止刷新
        mSwipeRefreshLayout.setRefreshing(false);
        // 停止底部加载
        mAutoListView.onLoadComplete();
        // 更新数据
        notifyDataSetChanged();
    }

    /**
     * 清除数据集合的方法
     */
    abstract void clearResultData();

    /**
     * 处理数据的方法;该方法放置解析数据的操作
     */
    abstract void disposeResultData(JSONObject arg0);

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
