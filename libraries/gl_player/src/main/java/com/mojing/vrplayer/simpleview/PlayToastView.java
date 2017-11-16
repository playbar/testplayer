package com.mojing.vrplayer.simpleview;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.mojing.vrplayer.R;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by wanghongfang on 2017/6/5.
 * 播放ToastView
 */
public class PlayToastView extends RelativeLayout{
    TextView textView;
    public PlayToastView(Context context) {
        super(context);
        initView();
    }

    public PlayToastView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView(){
        View view = LayoutInflater.from(getContext()).inflate(R.layout.player_toast_layout,null);
        textView = (TextView) view.findViewById(R.id.toast_layout);

        LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
        this.addView(view,params);
    }

    public void showToast(String text,int duration){
        cancelDelay();
        if(textView!=null){
            textView.setText(text);
        }
        this.setVisibility(VISIBLE);
        setDelayVisiable(duration);
    }
    public void showToast(String text){
        showToast(text,3*1000);
    }

    public void cancelDelay(){
        if(timer!=null){
            timer.cancel();
            timer = null;
        }
    }
    /**
     * 无操作时延迟3s隐藏
     */
    Timer timer ;
    public void setDelayVisiable(int duraion){
        if(timer!=null){
            timer.cancel();
            timer = null;
        }
        timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                ((Activity)getContext()).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(PlayToastView.this.getVisibility()== View.VISIBLE) {
                            PlayToastView.this.setVisibility(GONE);
                        }
                    }
                });
            }
        };
        timer.schedule(task, duraion);
    }


}
