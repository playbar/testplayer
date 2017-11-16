package com.mojing.vrplayer.simpleview;


import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.media.AudioManager;
import android.provider.Settings;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import com.mojing.vrplayer.view.SimplePlayControlUI;
import com.storm.smart.common.utils.LogHelper;

/**
 * Created by panxin on 2017/5/8.
 */

public class ScreenControlView extends View {
    String TAG="ScreenControlView";
    private int width;
    private int height;
    private float MIN_DISTANCE = 20;
    private int MODE_DEFAULT = -1;
    private int MODE_VOICE = 0;
    private int MODE_BRIGHTNESS = 1;
    private int MODE_RATE = 2;
    private int mode = MODE_DEFAULT;

    private float currentBright = -1;//当前设置的亮度
    private float tempBright = 0;//临时亮度值 计算用
    private WindowManager.LayoutParams lp;

    private float currentVoice = -1;
    private float tempVoice = 0;
    /** 最大声音 */
    private int mMaxVolume;
    private AudioManager mAudioManager;

    private int tempRate = 0;
    private int mMaxRate;
    private Activity mActivity;

    public ScreenControlView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mActivity = (Activity) context;
        mAudioManager = (AudioManager) mActivity.getSystemService(Context.AUDIO_SERVICE);
        mMaxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        LogHelper.d("px","mMaxVolume  "+mMaxVolume);
    }

    public void setMaxRate(int max){
        mMaxRate = max;
    }

    float startX;
    float startY;
    float disX;
    float disY;
    private boolean isLockScreen=false;
    public void setLockScreen(boolean lockScreen){
        isLockScreen=lockScreen;
    }
    public boolean isLockScreen(){
        return isLockScreen;
    }

    public interface OnClickInOnTouchEvent{
        public void onClick();
    }

    OnClickInOnTouchEvent mOnClickInOnTouchEvent;
    public void setOnClickInOnTouchEventListener(OnClickInOnTouchEvent onClickInOnTouchEvent){
        mOnClickInOnTouchEvent=onClickInOnTouchEvent;
    }

    boolean isMove=false;
    float FirxtX=0;
    float FirxtY=0;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        LogHelper.d(TAG,"onTouchEvent1");

        if(event.getAction()==MotionEvent.ACTION_DOWN){
            LogHelper.d(TAG,"onTouchEvent ACTION_DOWN");
            isMove=false;
            FirxtX=event.getX();
            FirxtY=event.getY();
        }else if(event.getAction()==MotionEvent.ACTION_MOVE){
            LogHelper.d(TAG,"onTouchEvent ACTION_MOVE");
            if(Math.abs(FirxtX-event.getX())> SimplePlayControlUI.TIME_TOUCH_ONCLICK ||Math.abs(FirxtY-event.getY())>SimplePlayControlUI.TIME_TOUCH_ONCLICK ){
                LogHelper.d(TAG,"onTouchEvent ACTION_MOVE1");
                isMove=true;
            }

        }else if(event.getAction()==MotionEvent.ACTION_UP&&!isMove) {
            LogHelper.d(TAG,"onTouchEvent isMove:"+isMove);
            if(mOnClickInOnTouchEvent!=null){
                mOnClickInOnTouchEvent.onClick();
            }
        }

        if(isLockScreen){
            LogHelper.d(TAG,"onTouchEvent isLockScreen:"+isLockScreen);
            return super.onTouchEvent(event);
        }else{
            LogHelper.d(TAG,"onTouchEvent isLockScreen:"+isLockScreen);
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mode = MODE_DEFAULT;
                startX = event.getX();
                startY = event.getY();
                if(mIPlayerRateListener!=null){
                    mIPlayerRateListener.onViewTouchEvent(IPlayerRateListener.TOUCH_DOWN);
                }
                break;

            case MotionEvent.ACTION_MOVE:
                disX = event.getX()-startX;
                disY = event.getY()-startY;
                initMode(disX,disY);
                if(mode == MODE_BRIGHTNESS){//亮度
                    if(disY<0){//向上滑动，亮度增大
                        tempBright = currentBright + Math.abs(disY/height) * 255;
                        if(tempBright >= 255){
                            tempBright = 255;
                        }
                    }else{
                        tempBright = currentBright - Math.abs(disY/height) * 255;
                        if(tempBright <= 0){
                            tempBright = 0;
                        }
                    }
                    setLight(tempBright);
                    if(mIPlayerBrightListener != null){
                        mIPlayerBrightListener.changeBright(disY<0,tempBright/255f);
                    }
                }else if(mode == MODE_VOICE){//声音
                    if(disY<0){//向上滑动，声音增大
                        tempVoice = currentVoice + Math.abs(disY/height) * mMaxVolume;
                        if(tempVoice >= mMaxVolume){
                            tempVoice = mMaxVolume;
                        }
                    }else{
                        tempVoice = currentVoice - Math.abs(disY/height) * mMaxVolume;
                        if(tempVoice <= 0){
                            tempVoice = 0;
                        }
                    }
                    setVoice(tempVoice);
                    if(mIPlayerVoiceListener != null){
                        mIPlayerVoiceListener.changeVoice(disY<0,tempVoice/mMaxVolume);
                    }
                }else if(mode == MODE_RATE){//进度
                    if (disX > 0) { //向右滑动
                        if(mIPlayerRateListener!=null){
                            mIPlayerRateListener.onViewTouchEvent(IPlayerRateListener.TOUCH_MOVE_RIGHT);
                        }
                    }else{
                        if(mIPlayerRateListener!=null){
                            mIPlayerRateListener.onViewTouchEvent(IPlayerRateListener.TOUCH_MOVE_LEFT);
                        }
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                if(mode == MODE_BRIGHTNESS){
                    currentBright = tempBright;
                    tempBright = 0;
                    if(mIPlayerBrightListener != null){
                        mIPlayerBrightListener.changeFinish();
                    }
                }else if(mode == MODE_VOICE){
                    currentVoice = tempVoice;
                    tempVoice = 0;
                    if(mIPlayerVoiceListener != null){
                        mIPlayerVoiceListener.changeFinish();
                    }
                }else if(mode == MODE_RATE){
                    if(mIPlayerRateListener != null){
                        mIPlayerRateListener.onViewTouchEvent(IPlayerRateListener.TOUCH_UP);
                    }
                }
                break;
        }
        return super.onTouchEvent(event);
    }



    private void initMode(float disX,float disY){
//        if(mode == MODE_DEFAULT){
//            if(Math.abs(disX)>=MIN_DISTANCE*2){
//                mode = MODE_RATE;
//            }else if(Math.abs(disX)<MIN_DISTANCE*2 && Math.abs(disY)>=MIN_DISTANCE){
//                if(startX<width/2){
//                    mode = MODE_BRIGHTNESS;
//                }else{
//                    mode = MODE_VOICE;
//                }
//            }else{
//                mode = MODE_DEFAULT;
//            }
//        }

//        if(mode == MODE_DEFAULT&&(Math.abs(disX)>=MIN_DISTANCE||Math.abs(disY)>=MIN_DISTANCE)){
//            if(Math.abs(disX/4)>Math.abs(disY)){
//                mode = MODE_RATE;
//            }else if(Math.abs(disX)<MIN_DISTANCE && Math.abs(disY)>=MIN_DISTANCE){
//                if(startX<width/2){
//                    mode = MODE_BRIGHTNESS;
//                }else{
//                    mode = MODE_VOICE;
//                }
//            }else{
//                mode = MODE_DEFAULT;
//            }
//        }


        if(mode == MODE_DEFAULT&&(Math.abs(disX)>=MIN_DISTANCE||Math.abs(disY)>=MIN_DISTANCE)){
            if(Math.abs(disX)*2/3>Math.abs(disY)){
                mode = MODE_RATE;
            }else{
                if(startX<width/2){
                    mode = MODE_BRIGHTNESS;
                }else{
                    mode = MODE_VOICE;
                }
            }
        }


    }


    /**
     * 设置亮度
     * @param value
     */
    private void setLight(float value) {
        if(currentBright == -1){
            currentBright = getScreenBrightness(mActivity);
        }
        if(lp == null){
            lp = mActivity.getWindow().getAttributes();
        }
        lp.screenBrightness = value * (1f / 255f);
        mActivity.getWindow().setAttributes(lp);
    }

    /**
     * 设置音量
     * @param value
     */
    private void setVoice(float value){
        if(currentVoice == -1){
            currentVoice = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        }
        if(currentVoice < 0){
            currentVoice = 0;
        }

        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, (int)value, 0);
    }

    /**
     * 获取屏幕的亮度
     *
     * @param activity
     * @return
     */
    private int getScreenBrightness(Activity activity) {
        int nowBrightnessValue = 0;
        ContentResolver resolver = activity.getContentResolver();
        try {
            nowBrightnessValue = Settings.System.getInt(
                    resolver, Settings.System.SCREEN_BRIGHTNESS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return nowBrightnessValue;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        width = getWidth();
        height = getHeight();

    }

    private IPlayerBrightListener mIPlayerBrightListener;
    public void setIPlayerBrightListener(IPlayerBrightListener listener){
        this.mIPlayerBrightListener = listener;
    }

    private IPlayerVoiceListener mIPlayerVoiceListener;
    public void setIPlayerVoiceListener(IPlayerVoiceListener listener){
        this.mIPlayerVoiceListener = listener;
    }

    private IPlayerRateListener mIPlayerRateListener;
    public void setIPlayerRateListener(IPlayerRateListener listener){
        this.mIPlayerRateListener = listener;
    }

    public interface IPlayerBrightListener {
        void changeBright(boolean isUp, float bright);
        void changeFinish();
    }

    public interface IPlayerVoiceListener {
        void changeVoice(boolean isUp, float voice);
        void changeFinish();
    }

    public interface IPlayerRateListener {
        public int TOUCH_MOVE_LEFT = 0;
        public int TOUCH_MOVE_RIGHT = 1;
        public int TOUCH_UP = 2;
        public int TOUCH_DOWN = 3;
        void onViewTouchEvent(int type);
    }

}
