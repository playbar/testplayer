package com.mojing.vrplayer.view;

import android.content.Context;
import android.graphics.Bitmap;

import com.bfmj.viewcore.render.GLColor;
import com.bfmj.viewcore.view.GLImageView;
import com.bfmj.viewcore.view.GLRelativeView;
import com.bfmj.viewcore.view.GLTextView;
import com.mojing.vrplayer.R;
import com.mojing.vrplayer.utils.BitmapUtil;
import com.mojing.vrplayer.utils.MJGLUtils;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by yushaochen on 2017/4/9.
 */
public class GLSeekToast extends GLRelativeView {

    private GLImageView imageView;
    private GLTextView textView1;
    private GLTextView textView2;

    int width = 305;
    int height = 145;

    private Timer timer ;

    public GLSeekToast(Context context){
        super(context);
        setLayoutParams(width,height);
        Bitmap bitmap = BitmapUtil.getBitmap(width, height, 20f, "#19191a");
        setBackground(bitmap);
        initView();
        setVisible(false);
    }

    private void initView(){

        imageView = new GLImageView(getContext());
        imageView.setLayoutParams(50f,50f);
        imageView.setBackground("play_toast_icon_fastforward");
        imageView.setMargin((width-50)/2,15f,100f,0f);
        addView(imageView);

        textView1 = new GLTextView(getContext());
        textView1.setTextSize(28);
        textView1.setTextColor(new GLColor(0x008cb3));
        textView1.setPadding(0,20,0,0);
        textView1.setAlignment(GLTextView.ALIGN_CENTER);
        textView1.setMargin(29f,65f,0f,0f);
//        textView1.setText("360:54 / 120:45");
        textView1.setText("360:54");
        textView1.setLayoutParams(111f,80f);
//        textView1.setBackground(new GLColor(0xff0000));
        addView(textView1);

        textView2 = new GLTextView(getContext());
        textView2.setTextSize(28);
        textView2.setTextColor(new GLColor(0x888888));
        textView2.setPadding(0,20,0,0);
        textView2.setAlignment(GLTextView.ALIGN_CENTER);
        textView2.setMargin(29f +111f-1f,65f,0f,0f);
//        textView2.setText("360:54 / 120:45");
        textView2.setText(" / 120:45");
        textView2.setLayoutParams(141f,80f);
//        textView2.setBackground(new GLColor(0xff0000));
        addView(textView2);

    }

//    public void showToast(String currentTime,String totalTime,int imageId,int duration){
//        textView1.setText(currentTime);
//        textView2.setText(" / "+totalTime);
//        imageView.setBackground(imageId);
//        setDelayVisiable(duration);
//    }

    public void cancelTimer(){
        if(timer!=null){
            timer.cancel();
            timer = null;
        }
        GLSeekToast.this.setVisible(false);
    }

    public void showToast(final String currentTime ,final String totalTime,final String imageId){

        MJGLUtils.exeGLQueueEvent(getContext(), new Runnable() {
            @Override
            public void run() {
                textView1.setText(currentTime);
                textView2.setText(" / "+totalTime);
                imageView.setBackground(imageId);
            }
        });
//        setDelayVisiable(3*1000);
    }
    public void setDelayVisiable(int duration){
        if(timer!=null){
            timer.cancel();
            timer = null;
        }
        timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                MJGLUtils.exeGLQueueEvent(getContext(), new Runnable() {
                    @Override
                    public void run() {
                        GLSeekToast.this.setVisible(false);
                    }
                });
            }
        };
        timer.schedule(task,duration);
    }
    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);
        if(!visible&&mListener!=null){
            mListener.onDismiss();
        }
    }

    private GLTextToast.IToastDismisListener mListener;
    public void setOnToastDismisListener(GLTextToast.IToastDismisListener listener){
        mListener = listener;
    }
}
