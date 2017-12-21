package com.jiarui.znxj.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;

import com.jiarui.znxj.R;
import com.jiarui.znxj.adapter.OrderPageAdapter;
import com.jiarui.znxj.base.BaseActivity;
import com.jiarui.znxj.fragment.UrgentFragment1;
import com.jiarui.znxj.fragment.UrgentFragment2;
import com.jiarui.znxj.topmenu.MenuItem;
import com.jiarui.znxj.topmenu.TopRightMenu;

import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * 紧急汇报
 */
public class UrgentReportActivity extends BaseActivity {
    @ViewInject(R.id.urgent_mTabLayout)
    private TabLayout mTabLayout;//滑动的标题
    @ViewInject(R.id.urgent_viewpager)
    private ViewPager viewpager;
    @ViewInject(R.id.common_title_1)
    private RadioButton common_title_1;//未上报
    @ViewInject(R.id.common_title_2)
    private RadioButton common_title_2;//已上报
    @ViewInject(R.id.common_title_right)
    private ImageView common_title_right;
    private ArrayList<Fragment> fragmentList;
    private TopRightMenu mTopRightMenu;
    public static String type = "1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_urgent_report);
        x.view().inject(this);
        fragmentList = new ArrayList<>();
        InitViewPager1();
        common_title_1.setChecked(true);
        common_title_1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    type = "1";
                    InitViewPager1();
                }
            }
        });
        common_title_2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    type = "2";
                    InitViewPager2();
                }
            }
        });
    }

    public void InitViewPager1() {
        type="1";
        fragmentList.clear();
        List<String> mTieles = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            mTieles.add("未上报");
            mTieles.add("已上报");
        }
        fragmentList.add(new UrgentFragment1());
        fragmentList.add(new UrgentFragment2());
        viewpager.setAdapter(new OrderPageAdapter(getSupportFragmentManager(), fragmentList, mTieles));
        mTabLayout.setupWithViewPager(viewpager);
    }

    public void InitViewPager2() {
        fragmentList.clear();
        type="2";
        List<String> mTieles = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            mTieles.add("未上报");
            mTieles.add("已上报");
        }
        fragmentList = new ArrayList<>();
        fragmentList.add(new UrgentFragment1());
        fragmentList.add(new UrgentFragment2());
        viewpager.setAdapter(new OrderPageAdapter(getSupportFragmentManager(), fragmentList, mTieles));
        mTabLayout.setupWithViewPager(viewpager);
    }


    @Event({R.id.common_title_left, R.id.common_title_right})
    private void onClick(View v) {
        switch (v.getId()) {
            case R.id.common_title_left:
                finish();
                break;
            case R.id.common_title_right:
                mTopRightMenu = new TopRightMenu(UrgentReportActivity.this);
                List<MenuItem> menuItems = new ArrayList<>();
                menuItems.add(new MenuItem(R.mipmap.work_icon, "工情"));
                menuItems.add(new MenuItem(R.mipmap.under_icon, "水情"));
                mTopRightMenu
                        .setHeight(300)     //默认高度480
                        .setWidth(300)      //默认宽度wrap_content
                        .showIcon(true)     //显示菜单图标，默认为true
//                        .dimBackground(false)           //背景变暗，默认为true
                        .needAnimationStyle(true)   //显示动画，默认为true
                        .setAnimationStyle(R.style.TRM_ANIM_STYLE)  //默认为R.style.TRM_ANIM_STYLE
                        .addMenuList(menuItems)
                        .setOnMenuItemClickListener(new TopRightMenu.OnMenuItemClickListener() {
                            @Override
                            public void onMenuItemClick(int position) {
                                if (position == 0) {
                                    gotoActivity(ProjectActivity.class);
                                } else if (position == 1) {
                                    gotoActivity(WaterregimeActivity.class);
                                }
                            }
                        })
                        .showAsDropDown(common_title_right, -225, 0);
                break;
            default:
                break;
        }
    }
}
