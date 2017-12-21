package com.jiarui.znxj.utils;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.jiarui.znxj.R;
import com.nostra13.universalimageloader.core.ImageLoader;

public class CommonDialog extends Dialog implements View.OnClickListener {

    Context context;

    private ImageView mIconLeft = null;

    private TextView mContent = null;

    private TextView mCommonCancle;

    private TextView mCommonOk;

    private int icon;// 图标

    private String Content;// 内容

    private String leftBtnText;// 坐标按钮文字

    private String rightBtnText;// 右边按钮文字

    private float montentSize = 0;

    public float getContentSize() {
        return montentSize;
    }

    public void setContentSize(float contentSize) {
        this.montentSize = contentSize;
    }

    private DialogClickListener listener;

    public DialogClickListener getListener() {
        return listener;
    }

    /**
     * 设置点击按钮的监听事件
     *
     * @param listener
     */
    public void setListener(DialogClickListener listener) {
        this.listener = listener;
    }

    public String getContent() {
        return Content;
    }

    /**
     * 设置提示内容
     *
     * @param content
     */
    public void setContent(String content) {
        Content = content;
    }

    public int getIcon() {
        return icon;
    }

    /**
     * 设置小图标
     *
     * @param icon
     */
    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getLeftBtnText() {
        return leftBtnText;
    }

    /**
     * 设置左边按钮的文本
     *
     * @param leftBtnText
     */
    public void setLeftBtnText(String leftBtnText) {
        this.leftBtnText = leftBtnText;
    }

    public String getRightBtnText() {
        return rightBtnText;
    }

    /**
     * 设置右边按钮的文本
     *
     * @param rightBtnText
     */
    public void setRightBtnText(String rightBtnText) {
        this.rightBtnText = rightBtnText;
    }

    public CommonDialog(Context context) {
        super(context);
        this.context = context;
    }

    /**
     * 主题
     *
     * @param context
     * @param theme
     */
    public CommonDialog(Context context, int theme) {
        super(context, theme);
        this.context = context;
    }

    /**
     * 设置按钮的大小
     *
     * @param size
     */
    public void setBtnTextSize(int size) {
        mCommonCancle.setTextSize(size);
        mCommonOk.setTextSize(size);
    }

    ;

    /**
     * 设置按钮字体的颜色
     *
     * @param colors
     */
    public void setBtnTextColor(int colors) {
        mCommonCancle.setTextColor(colors);
        mCommonOk.setTextColor(colors);
    }

    ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.common_layout_dilaog);
        mIconLeft = (ImageView) findViewById(R.id.common_dialog_icon_left);
        mContent = (TextView) findViewById(R.id.common_dialog_content);
        mCommonCancle = (TextView) findViewById(R.id.common_dialog_cancle);
        mCommonOk = (TextView) findViewById(R.id.common_dialog_ok);
        mCommonCancle.setOnClickListener(this);
        mCommonOk.setOnClickListener(this);
        initView();
        initDialog(context);
    }

    /**
     * 设置dialog的宽为屏幕的3分之1
     *
     * @param context
     */
    private void initDialog(Context context) {
        setCanceledOnTouchOutside(false);
        setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
                    return true;
                } else {
                    return false;
                }
            }
        });
        WindowManager windowManager = this.getWindow().getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = this.getWindow().getAttributes();
        lp.width = (int) (display.getWidth() / 6 * 5); // // 设置宽度
        this.getWindow().setAttributes(lp);
    }

    private void initView() {
        if (getIcon() == 0) {
            mIconLeft.setVisibility(View.GONE);
        } else
            ImageLoader.getInstance().displayImage("drawable://" + getIcon(), mIconLeft, com.jiarui.znxj.utils.DefaultDisplayImageOptions.getDefaultDisplayImageOptionsRounded(context, 360));
        if (getContent() == null) {
            mContent.setVisibility(View.GONE);
        } else {
            mContent.setText(getContent());
            if (getContentSize() != 0)
                mContent.setTextSize(getContentSize());
        }
        mCommonCancle.setText(getLeftBtnText());
        mCommonOk.setText(getRightBtnText());
    }

    public interface DialogClickListener {
        void onLeftBtnClick(Dialog dialog);

        void onRightBtnClick(Dialog dialog);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.common_dialog_cancle:
                listener.onLeftBtnClick(this);
                dismiss();
                break;
            case R.id.common_dialog_ok:
                listener.onRightBtnClick(this);
                dismiss();
                break;
            default:
                break;
        }
    }
}
