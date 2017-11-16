package com.mojing.vrplayer.view;

import android.content.Context;
import android.graphics.Bitmap;

import com.bfmj.viewcore.render.GLColor;
import com.bfmj.viewcore.view.GLImageView;
import com.bfmj.viewcore.view.GLLinearView;
import com.bfmj.viewcore.view.GLTextView;
import com.mojing.vrplayer.R;
import com.mojing.vrplayer.utils.BitmapUtil;
import com.mojing.vrplayer.utils.MJGLUtils;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by yushaochen on 2017/4/9.
 */
public class GLImageToast extends GLLinearView {

    private GLImageView imageView;

    int width1 = 500;
    int width2 = 100;
    int width3 = 150;
    int height = 70;

    private Timer timer ;
    GLTextView textView1;
    public GLImageToast(Context context){
        super(context);
        setLayoutParams(width1+100+44+10+width2+7+width3+10, height);
        Bitmap bitmap = BitmapUtil.getBitmap(width1+100+44+10+width2+7+width3+10, height, 20f, "#19191a");
        setPadding(50f,0f,50f,0f);
        setBackground(bitmap);

        initView();
        setVisible(false);
    }

    private void initView(){
        textView1 = new GLTextView(getContext());
        textView1.setTextSize(25);
        textView1.setTextColor(new GLColor(0x888888));
        textView1.setPadding(0,20,0,0);
        textView1.setAlignment(GLTextView.ALIGN_CENTER);
        textView1.setText("已为您识别到非全景模式");
        textView1.setLayoutParams(width1+width2, height);
//        textView.setBackground(new GLColor(0xff0000));
        addView(textView1);
//        GLTextView textView2 = new GLTextView(getContext());
//        textView2.setLayoutParams(width2,height);
//        textView2.setPadding(0,20,0,0);
//        textView2.setMargin(7f,0f,0f,0f);
//        textView2.setTextSize(28);
//        textView2.setTextColor(new GLColor(0x008cb3));
//        textView2.setAlignment(GLTextView.ALIGN_CENTER);
//        textView2.setText("模式，如有错误请到");
//        addView(textView2);

        imageView = new GLImageView(getContext());
        imageView.setLayoutParams(44f,44f);
        imageView.setMargin(10f,13f,0f,0f);
        imageView.setBackground("play_toast_icon_model");
        addView(imageView);
        GLTextView textView3 = new GLTextView(getContext());
        textView3.setLayoutParams(width3,height);
        textView3.setPadding(0,20,0,0);
        textView3.setMargin(0f,0f,0f,0f);
        textView3.setTextSize(25);
        textView3.setTextColor(new GLColor(0x888888));
        textView3.setAlignment(GLTextView.ALIGN_CENTER);
        textView3.setText("手动切换");
//        textView3.setBackground(new GLColor(0xff0000));
        addView(textView3);
    }

//    public void showToast(int imageId,int duration){
//        imageView.setBackground(imageId);
//        setDelayVisiable(duration);
//    }

    public void cancelTimer(){
        if(timer!=null){
            timer.cancel();
            timer = null;
        }
        GLImageToast.this.setVisible(false);
    }

    public void showToast(final String text, final int imageId){

        MJGLUtils.exeGLQueueEvent(getContext(), new Runnable() {
            @Override
            public void run() {
                //textView1.setText("已为您识别到 "+text+" 模式，如有错误请到模式中");
               // imageView.setBackground(imageId);
               // GLImageToast.this.setVisible(true);
            }
        });
        setDelayVisiable(3*1000);
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
                        GLImageToast.this.setVisible(false);
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
