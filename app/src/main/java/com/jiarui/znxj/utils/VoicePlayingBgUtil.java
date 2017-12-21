package com.jiarui.znxj.utils;

import android.os.Handler;
import android.widget.ImageView;

import com.jiarui.znxj.R;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 语音播放效果 Administrator on 2017/11/7 0007.
 */

public class VoicePlayingBgUtil {
    private Handler handler;

    private ImageView imageView;

    private ImageView lastImageView;

    private Timer timer = new Timer();
    private TimerTask timerTask;

    private int i;

    private int modelType = 1;//类型

    private int[] leftVoiceBg = new int[] { R.mipmap.gray1, R.mipmap.gray2, R.mipmap.gray3 };
    private int[] rightVoiceBg = new int[] { R.mipmap.green1, R.mipmap.green2, R.mipmap.green3 };

    public VoicePlayingBgUtil(Handler handler) {
        super();
        this.handler = handler;
    }

    public void voicePlay() {
        if (imageView == null) {
            return;
        }
        i = 0;
        timerTask = new TimerTask() {

            @Override
            public void run() {
                if (imageView != null) {
                    if (modelType == 1) {
                        changeBg(leftVoiceBg[i % 3], false);
                    }else if(modelType==2){
                        changeBg(rightVoiceBg[i % 3], false);
                    }
                }
                else {
                    return;
                }
                i++;
            }
        };
        timer.schedule(timerTask, 0, 500);
    }

    public void stopPlay() {
        lastImageView = imageView;
        if (lastImageView != null) {
            switch (modelType) {
                case 1:
                    changeBg(R.mipmap.gray3, true);
                    break;
                case 2:
                    changeBg(R.mipmap.green3, true);
                    break;
                default:
                    changeBg(R.mipmap.gray3, true);
                    break;
            }
            if (timerTask != null) {
                timerTask.cancel();
            }
        }
    }

    private void changeBg(final int id, final boolean isStop) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (isStop) {
                    lastImageView.setImageResource(id);
                }
                else {
                    imageView.setImageResource(id);
                }
            }
        });
    }

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }

    public void setModelType(int modelType) {
        this.modelType = modelType;
    }
}
