package com.jiarui.znxj.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.TextView;

import com.jiarui.znxj.R;

import org.xutils.x;


/**
 * Fragment基类
 *
 * @author Only You
 * @version 1.0
 * @date 2016年1月6日
 */
public abstract class BaseFragment extends Fragment {
    protected View mTitleView = null;

    protected InputMethodManager inputMethodManager;

    protected TextView mImgvForLeft = null;

    protected TextView mTvForTitle = null;

    protected ImageButton mImgvForRight = null;

    // 根部view
    protected View mRootView = null;

    protected Context mContext = null;

    /**
     * 是否是第一次加载数据
     */
    private boolean isFirstLoad = true;

    public boolean isFirstLoad() {
        return isFirstLoad;
    }

    public void setFirstLoad(boolean isFirstLoad) {
        this.isFirstLoad = isFirstLoad;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mRootView == null) {
            mRootView = initView(inflater);
        }
        x.view().inject(this, mRootView);
        return mRootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getUserVisibleHint()) {
            initData();
        }
    }

    /**
     * 设置Title栏
     */
    protected void setTitle() {
        mTitleView = mRootView.findViewById(R.id.common_title_layout);
        mImgvForLeft = (TextView) mRootView.findViewById(R.id.common_title_left);
        mImgvForRight = (ImageButton) mRootView.findViewById(R.id.common_title_right);
        mTvForTitle = (TextView) mRootView.findViewById(R.id.common_title_tv);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint() & mRootView != null & isFirstLoad()) {
            initData();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mRootView != null) {
            ((ViewGroup) mRootView.getParent()).removeView(mRootView);
        }
    }

    // 子类实现初始化View操作
    protected abstract View initView(LayoutInflater inflater);

    // 子类实现初始化数据操作(子类自己调用)
    public abstract void initData();

    /**
     * 传递数据并跳转到下一Activity
     *
     * @param bundle
     * @param cls
     */
    protected void gotoAct(Bundle bundle, Class<?> cls) {
        Intent intent = new Intent(getActivity(), cls);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    /**
     * 跳转到下一Activity
     *
     * @param cls
     */
    protected void gotoAct(Class<?> cls) {
        Intent intent = new Intent(getActivity(), cls);
        startActivity(intent);
    }

    /**
     * 传递数据并跳转到下一Activity,并且有返回数据
     *
     * @param bundle
     * @param cls
     * @param requestCode
     */
    protected void gotoActForResult(Bundle bundle, Class<?> cls, int requestCode) {
        Intent intent = new Intent(getActivity(), cls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivityForResult(intent, requestCode);
    }

    /**
     * 隐藏键盘
     *
     * @param context
     */
    public static void hiddenKeyboard(Activity context) {
        if (context.getCurrentFocus() != null) {
            // 焦点不为空
            InputMethodManager inputMethodManager = ((InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE));
            if (inputMethodManager.isActive(context.getCurrentFocus())) {
                // 隐藏键盘
                context.getCurrentFocus().clearFocus();
                inputMethodManager.hideSoftInputFromWindow(context.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

    /**
     * 显示键盘
     *
     * @param context
     */
    public static void showKeyboard(Activity context) {
        if (context.getCurrentFocus() != null) {
            // 焦点不为空
            InputMethodManager inputMethodManager = ((InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE));
            if (inputMethodManager.isActive(context.getCurrentFocus())) {
                // 键盘未显示
                inputMethodManager.showSoftInput(context.getCurrentFocus(), InputMethodManager.SHOW_IMPLICIT);
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    /**
     * 显示标题栏
     */
    public void ShowmTitleView() {
        if (mTitleView != null) {
            mTitleView.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 隐藏标题栏
     */
    public void hidemTitleView() {
        if (mTitleView != null) {
            mTitleView.setVisibility(View.GONE);
        }
    }

    protected void hideSoftKeyboard() {
        if (getActivity().getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (getActivity().getCurrentFocus() != null)
                inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

}
