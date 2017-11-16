package com.mojing.vrplayer.view;

import android.content.Context;

import com.baofeng.mojing.input.base.MojingKeyCode;
import com.bfmj.sdk.util.TimeFormat;
import com.bfmj.viewcore.interfaces.GLOnKeyListener;
import com.bfmj.viewcore.interfaces.GLViewFocusListener;
import com.bfmj.viewcore.render.GLColor;
import com.bfmj.viewcore.util.GLFocusUtils;
import com.bfmj.viewcore.view.GLImageView;
import com.bfmj.viewcore.view.GLRectView;
import com.bfmj.viewcore.view.GLTextView;
import com.mojing.vrplayer.R;
import com.mojing.vrplayer.interfaces.IPlayerControlCallBack;
import com.mojing.vrplayer.utils.GLConst;
import com.mojing.vrplayer.utils.MJGLUtils;
import com.mojing.vrplayer.utils.ViewUtil;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by yushaochen on 2016/2/17.
 */
public class GLSeekBarView extends GLProcessView {

    private int mWidth = 1000-340;
    private int mHeight = 12;

    private String mResImg;
    private GLImageView mBarView;
    private GLTextView mDisplayView;
    private IPlayerControlCallBack mCallBack;
    private int bar_width = 38,bar_height=38;
    private int display_width = 130,display_height=40;
    private long mDuration=0; //毫秒
    private GLImageView display_image;
    private int display_image_width = 10,display_image_height=38;

    private int offsetHeight = (bar_height-mHeight)/2;

    public void setBarImage(String barImage){
        this.mResImg = barImage;
        initView();
        this.setFocusListener(viewFocusListener);
        this.setOnKeyListener(mKeyListener);
    }
    public GLSeekBarView(Context context){
        super(context);
        setProcessColor("play_control_slider_progress_1");
        setLayoutParams(mWidth,mHeight);
        setBackground("play_control_slider_bg");
//        setPrestrainColor(R.drawable.play_control_slider_progress);
//        setPrestrainProcess(0);
        setBarImage("play_control_cursor_normal");
//        setProcess(100);
//        setProcessColor(R.drawable.play_control_slider_progress_1);
        setProcess(0);

    }

    public void updateDisplayDuration(long duration){
        mDuration = duration;
    }

    public void setIPlayerControlCallBack(IPlayerControlCallBack callBack){
        this.mCallBack = callBack;
    }

    private void initView(){
        mBarView = new GLImageView(getContext());
        mBarView.setImage(mResImg);
        mBarView.setLayoutParams(bar_width,bar_height);

        this.addView(mBarView);
        mDisplayView = new GLTextView(getContext());
        mDisplayView.setTextColor(new GLColor(0x888888));
        mDisplayView.setTextSize(28);
        mDisplayView.setText("00:00:00");
        mDisplayView.setLayoutParams(display_width,display_height);
        mDisplayView.setAlignment(GLTextView.ALIGN_CENTER);
        mDisplayView.setPadding(0,0,0,5f);
//        mDisplayView.setBackground(new GLColor(0xff0000));
        mDisplayView.setVisible(false);
        this.addView(mDisplayView);

        display_image = new GLImageView(getContext());
        display_image.setLayoutParams(display_image_width,display_image_height);
        display_image.setBackground("play_control_cursor_hover");
        display_image.setVisible(false);
        this.addView(display_image);
    }

    @Override
    public void addView(GLRectView view) {
        view.setX(this.getX() + this.getPaddingLeft() + view.getMarginLeft());
        view.setY(this.getY() + this.getPaddingTop() + view.getMarginTop());
        super.addView(view);
    }
    /**
     * 更新当前加载进度
     * @param process
     */
    @Override
    public void setProcess(int process) {
        super.setProcess(process);
         float width = (this.getWidth() - this.getPaddingLeft() - this.getPaddingRight()) / 100.0F * process;
        this.mBarView.setLayoutParams(ViewUtil.getDip(bar_width, GLConst.Player_Controler_Scale) ,ViewUtil.getDip(bar_height,GLConst.Player_Controler_Scale));
        this.mBarView.setX(this.getX() + this.getPaddingLeft()+width-ViewUtil.getDip(bar_width,GLConst.Player_Controler_Scale)/2);
        this.mBarView.setY(this.getY() + this.getPaddingTop()-ViewUtil.getDip(offsetHeight,GLConst.Player_Controler_Scale));
    }
    private void setDisplayLayout(){
        if(mDisplayView!=null) {
            int xy[] = GLFocusUtils.getCursorPosition(this);
            int x=xy[0],y=xy[1];
            float viewX = GLSeekBarView.this.getX();
            float viewWidth = GLSeekBarView.this.getWidth();
            float viewY = GLSeekBarView.this.getY();
            if(x>0&&x< viewWidth){
                float process =(x)/viewWidth;
                long current = (long) (mDuration*process);
                String currentStr = TimeFormat.format((int)(current/1000));
                mDisplayView.setText(currentStr);
                mDisplayView.setLayoutParams(ViewUtil.getDip(display_width,GLConst.Player_Controler_Scale) ,ViewUtil.getDip(display_height,GLConst.Player_Controler_Scale));
                mDisplayView.setX(viewX+x-ViewUtil.getDip(display_width,GLConst.Player_Controler_Scale) / 2);
                mDisplayView.setY(viewY -ViewUtil.getDip(display_height,GLConst.Player_Controler_Scale)-ViewUtil.getDip(5,GLConst.Player_Controler_Scale));
                display_image.setX(viewX+x- ViewUtil.getDip(display_image_width,GLConst.Player_Controler_Scale) / 2);
                display_image.setY(viewY- ViewUtil.getDip(offsetHeight, GLConst.Player_Controler_Scale));
            }
        }
    }

    private GLViewFocusListener viewFocusListener = new GLViewFocusListener() {
        @Override
        public void onFocusChange(GLRectView glRectView, boolean b) {
            if(b){
                mDisplayView.setVisible(true);
                display_image.setVisible(true);
                startTimer();
            }else {
                stopTimer();
                mDisplayView.setVisible(false);
                display_image.setVisible(false);
            }
        }
    };

    Timer timer;
    public void startTimer(){
        if(timer!=null){
            timer.cancel();
            timer = null;
        }
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                MJGLUtils.exeGLQueueEvent(getContext(), new Runnable() {
                    @Override
                    public void run() {
                        setDisplayLayout();
                    }
                });
            }
        },0,300);

    }

    public void stopTimer(){
        if(timer!=null){
            timer.cancel();
            timer = null;
        }
    }

    /**
     * 更新预加载进度
     * @param process
     */
//    @Override
//    public void setPrestrainProcess(int process) {
//        super.setPrestrainProcess(process);
//    }

    private GLOnKeyListener mKeyListener = new GLOnKeyListener() {
        @Override
        public boolean onKeyDown(GLRectView glRectView, int keyCode) {
            if(keyCode== MojingKeyCode.KEYCODE_ENTER){

//                    int xy[] = GLFocusUtils.getCursorPosition();
//                    int x=xy[0],y=xy[1];
//
//                    float viewX = GLSeekBarView.this.getX();
//                    float viewWidth = GLSeekBarView.this.getWidth();
//                    if(x>=viewX&&x< viewX+viewWidth){
////                    int process = (int)((x-viewX)/viewWidth*100);
////                    setProcess(process);
//                        float process =(x-viewX)/viewWidth;
//                        int current = (int) (mDuration*process);
//                        mCallBack.onSeekToChanged(current);
//                    }
//                }

                int[] pos = GLFocusUtils.getPosition(GLSeekBarView.this);
                int x = pos[0];//;,y=xy[1];
                float viewX = GLSeekBarView.this.getX();
                float viewWidth = GLSeekBarView.this.getWidth();
                if(x>=viewX&&x< viewX+viewWidth){
                    float process =(x-viewX)/viewWidth;
                    int current = (int) (mDuration*process);
                    if(null != mCallBack) {
                        mCallBack.onSeekToChanged(current);
                    }
                }
            }
            return false;
        }
        @Override
        public boolean onKeyUp(GLRectView glRectView, int i) {
            return false;
        }

        @Override
        public boolean onKeyLongPress(GLRectView glRectView, int i) {
            return false;
        }
    };
}
