package com.mojing.vrplayer.view;

import android.content.Context;
import android.graphics.Bitmap;

import com.bfmj.viewcore.render.GLColor;
import com.bfmj.viewcore.view.GLRelativeView;
import com.bfmj.viewcore.view.GLTextView;
import com.mojing.vrplayer.utils.BitmapUtil;
import com.mojing.vrplayer.utils.GLConst;
import com.mojing.vrplayer.utils.MJGLUtils;
import com.mojing.vrplayer.utils.ViewUtil;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by yushaochen on 2017/4/9.
 */
public class GLTextToast extends GLRelativeView {

    public static final int SHORT = 0;
    public static final int MEDIUM = 1;
    public static final int LONG = 2;
    public static final int LARGE = 3;

    private GLTextView textView;

    int shortWidth = 100;
    int mediumWidth = 350;
    int longWidth = 490;
    int largeWidth = 580;

    int height = 75;

    private Timer timer ;

    private Bitmap shortBitmap;
    private Bitmap mediumBitmap;
    private Bitmap longBitmap;
    private Bitmap largeBitmap;

    public GLTextToast(Context context){
        super(context);
        initView();
    }

    private void initView(){
        textView = new GLTextView(getContext());
        textView.setTextSize(28);
        textView.setTextColor(new GLColor(0x888888));
        textView.setPadding(0,10,0,10);
        textView.setAlignment(GLTextView.ALIGN_CENTER);
//        textView.setBackground(new GLColor(0xff0000));
        setPadding(50f,10f,50f,0f);
        addView(textView);

        shortBitmap = BitmapUtil.getBitmap(shortWidth+100, height, 20f, "#19191a");
        mediumBitmap = BitmapUtil.getBitmap(mediumWidth+100, height, 20f, "#19191a");
        longBitmap = BitmapUtil.getBitmap(longWidth+100, height, 20f, "#19191a");
        largeBitmap = BitmapUtil.getBitmap(largeWidth+100,height,20f,"#19191a");
        setVisible(false);
    }

    public void showToast(String text,int type,int duration){
        setTextType(text,type);
        setDelayVisiable(duration);
    }

    public void setTextType(String text,int type){
        setTextType(text,type,true);
    }
    public void setTextType(String text,int type,boolean setCenter) {
        if(type == SHORT) {
            GLTextToast.this.setLayoutParams(ViewUtil.getDip(shortWidth+100f, GLConst.Dialog_Scale),ViewUtil.getDip(height,GLConst.Dialog_Scale));
            textView.setLayoutParams(ViewUtil.getDip(shortWidth, GLConst.Dialog_Scale),ViewUtil.getDip(height,GLConst.Dialog_Scale));
            GLTextToast.this.setBackground(shortBitmap);
            if(setCenter) {
                this.setMargin(ViewUtil.getDip(1200f - (shortWidth + 100f) / 2, GLConst.Dialog_Scale), getMarginTop(), 0f, 0f);
            }
        } else if(type == MEDIUM) {
            GLTextToast.this.setLayoutParams(ViewUtil.getDip(mediumWidth+100f, GLConst.Dialog_Scale),ViewUtil.getDip(height,GLConst.Dialog_Scale));
            textView.setLayoutParams(ViewUtil.getDip(mediumWidth,GLConst.Dialog_Scale),ViewUtil.getDip(height,GLConst.Dialog_Scale));
            GLTextToast.this.setBackground(mediumBitmap);
            if(setCenter) {
                this.setMargin(ViewUtil.getDip(1200f - (mediumWidth + 100f) / 2, GLConst.Dialog_Scale), getMarginTop(), 0f, 0f);
            }
        } else if(type == LONG) {
            GLTextToast.this.setLayoutParams(ViewUtil.getDip(longWidth+100f, GLConst.Dialog_Scale),ViewUtil.getDip(height,GLConst.Dialog_Scale));
            textView.setLayoutParams(ViewUtil.getDip(longWidth,GLConst.Dialog_Scale),ViewUtil.getDip(height,GLConst.Dialog_Scale));
            GLTextToast.this.setBackground(longBitmap);
            if(setCenter) {
                this.setMargin(ViewUtil.getDip(1200f - (longWidth + 100f) / 2, GLConst.Dialog_Scale), getMarginTop(), 0f, 0f);
            }
        }else if(type == LARGE){
            GLTextToast.this.setLayoutParams(ViewUtil.getDip(largeWidth+100f, GLConst.Dialog_Scale),ViewUtil.getDip(height,GLConst.Dialog_Scale));
            textView.setLayoutParams(ViewUtil.getDip(largeWidth,GLConst.Dialog_Scale),ViewUtil.getDip(height,GLConst.Dialog_Scale));
            GLTextToast.this.setBackground(largeBitmap);
            if(setCenter) {
                this.setMargin(ViewUtil.getDip(1200f - (largeWidth + 100f) / 2, GLConst.Dialog_Scale), getMarginTop(), 0f, 0f);
            }
        }
        textView.setText(text);
    }

    public void setText(String text){
        textView.setText(text);
    }

    public void cancelTimer(){
        if(timer!=null){
            timer.cancel();
            timer = null;
        }
        GLTextToast.this.setVisible(false);
    }

    public void showToast(final String text ,final int type){
        showToast(text,type,true);
    }
    public void showToast(final String text , final int type, final boolean setCenter){

        MJGLUtils.exeGLQueueEvent(getContext(), new Runnable() {
            @Override
            public void run() {
                setTextType(text,type,setCenter);
                GLTextToast.this.setVisible(true);
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
                        GLTextToast.this.setVisible(false);
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

    private IToastDismisListener mListener;
    public void setOnToastDismisListener(IToastDismisListener listener){
         mListener = listener;
    }

    public interface IToastDismisListener{
        void onDismiss();
    }
}
