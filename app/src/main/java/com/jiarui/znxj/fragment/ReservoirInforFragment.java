package com.jiarui.znxj.fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.jiarui.znxj.R;
import com.jiarui.znxj.base.BaseFragment;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/9/11 0011.
 * 水库信息
 */

public class ReservoirInforFragment extends BaseFragment {
    @Bind(R.id.reinfo_skmc)
    TextView reinfoSkmc;//水库名称
    @Bind(R.id.reinfo_skfzr)
    TextView reinfoSkfzr;//水库负责人
    @Bind(R.id.reinfo_skclsj)
    TextView reinfoSkclsj;//水库名称
    @Bind(R.id.reinfo_ckjj)
    TextView reinfoCkjj;//水库成立时间

    @Override
    protected View initView(LayoutInflater inflater) {
        mRootView = inflater.inflate(R.layout.reservoir_infor_fragment, null);
        ButterKnife.bind(this, mRootView);
        return mRootView;
    }

    @Override
    public void initData() {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
