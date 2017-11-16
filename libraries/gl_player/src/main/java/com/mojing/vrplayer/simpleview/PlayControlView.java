package com.mojing.vrplayer.simpleview;

/**
 * Created by wanghongfang on 2016/12/30.
 * 在线播放选集view
 */

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bfmj.sdk.util.TimeFormat;
import com.mojing.vrplayer.R;
import com.mojing.vrplayer.bean.VideoModeType;
import com.mojing.vrplayer.interfaces.IPlayerControlCallBack;
import com.mojing.vrplayer.interfaces.IPlayerSettingCallBack;
import com.mojing.vrplayer.publicc.SettingSpBusiness;
import com.mojing.vrplayer.utils.GLConst;
import com.storm.smart.common.utils.LogHelper;

public class PlayControlView extends LinearLayout implements View.OnClickListener{
    public final static String TAG="PlayControlView";
    public final static String VIEW_ID_SETTINGS="view_id_setting";
    public final static String VIEW_ID_CLOSE="view_id_close";
    public final static String VIEW_ID_SelectSource="VIEW_ID_SelectSource";
    public final static String VIEW_ID_HDSelect="VIEW_ID_HDSelect";
    public final static String VIEW_ID_playMode="VIEW_ID_playMode";
    public final static String VIEW_ID_VIEW_TOUCH="VIEW_ID_VIEW_TOUCH";
    public final static String VIEW_ID_VIEW_PREVIOUS = "VIEW_ID_VIEW_PREVIOUS";
    public final static String VIEW_ID_VIEW_NEXT = "VIEW_ID_VIEW_NEXT";
    public final static String VIEW_ID_VIEW_BUY = "VIEW_ID_VIEW_BUY";

    private Animation top_show_amin;
    private Animation top_hide_amin;

    private Animation bottom_show_amin;
    private Animation bottom_hide_amin;

    View mClose=null;
    TextView mVideoTitle=null;
    ImageView mPlayer_btn=null;
    TextView tv_current_time=null;
    TextView tv_total_time=null;
    TextView player_definition_select=null;
    SeekBar mSeekbar;

    ImageView settingview;
    private ScreenControlView view_ScreenControlView;
    LinearLayout certer_show_view;
    ImageView certer_show_view_image;
    TextView player_playsindex_select;
    ImageView player_playmode_select;
    ProgressBar lightprogress;
    private ImageView imageview_doublemode;
    private TextView textview_doublemode;
    private ImageView imageview_buyglasses;
    private TextView textview_buyglasses;
    private ImageView imageview_player_previous;
    private ImageView imageview_player_next;

    public PlayControlView(Context context) {
        super(context);
    }
    Context mContext;
    public PlayControlView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext=context;
        top_show_amin = AnimationUtils.loadAnimation(mContext, R.anim.player_contrl_up_show);
        top_hide_amin = AnimationUtils.loadAnimation(mContext, R.anim.player_contrl_up_hide);
        bottom_show_amin = AnimationUtils.loadAnimation(mContext, R.anim.player_contrl_down_hide);
        bottom_hide_amin = AnimationUtils.loadAnimation(mContext, R.anim.player_contrl_down_show);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.activity_simple_player_control, null);
        this.addView(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        initView(view);
    }

    public void initView(View view){
        mClose=view.findViewById(R.id.closeBtn);
        mClose.setOnClickListener(this);
        mVideoTitle=(TextView)view.findViewById(R.id.videoTitle);
        mPlayer_btn=(ImageView)view.findViewById(R.id.player_btn);
        mPlayer_btn.setOnClickListener(this);
        tv_current_time=(TextView)view.findViewById(R.id.tv_current_time);
        tv_total_time=(TextView)view.findViewById(R.id.tv_total_time);

        player_definition_select=(TextView)view.findViewById(R.id.player_definition_select);
        player_definition_select.setOnClickListener(this);
        player_definition_select.setVisibility(GONE);

        player_playsindex_select=(TextView)view.findViewById(R.id.player_playsindex_select);
        player_playsindex_select.setOnClickListener(this);
        player_playsindex_select.setVisibility(GONE);

        player_playmode_select=(ImageView)view.findViewById(R.id.player_playmode_select);
        player_playmode_select.setOnClickListener(this);
        player_playmode_select.setVisibility(GONE);

        imageview_player_previous = (ImageView)view.findViewById(R.id.imageview_player_previous);
        imageview_player_next = (ImageView)view.findViewById(R.id.imageview_player_next);
        imageview_player_previous.setOnClickListener(this);
        imageview_player_next.setOnClickListener(this);

        mSeekbar=(SeekBar)view.findViewById(R.id.progress);
        mSeekbar.setOnSeekBarChangeListener(new SeekBarChangeEvent());
        imageview_doublemode=(ImageView)view.findViewById(R.id.imageview_doublemode);
        imageview_doublemode.setOnClickListener(this);
        textview_doublemode = (TextView)view.findViewById(R.id.textview_doublemode);
        textview_doublemode.setOnClickListener(this);
        imageview_buyglasses = (ImageView)view.findViewById(R.id.imageview_buyglasses);
        textview_buyglasses = (TextView)view.findViewById(R.id.textview_buyglasses);

        settingview=(ImageView)view.findViewById(R.id.settingview);
        settingview.setOnClickListener(this);
       // settingview.setVisibility(GONE);

        view_ScreenControlView = (ScreenControlView) findViewById(R.id.rl_player_layout);

        certer_show_view = (LinearLayout) this.findViewById(R.id.certer_show_view);
        certer_show_view_image = (ImageView) this.findViewById(R.id.certer_show_view_image);
        lightprogress=(ProgressBar)this.findViewById(R.id.lightprogress);

        view_ScreenControlView.setIPlayerBrightListener(new ScreenControlView.IPlayerBrightListener() {
            @Override
            public void changeBright(boolean isUp, float percent) {
                refreshCenterViewByBright(percent);
                if(mSettingCallBack!=null) {
                    mSettingCallBack.onScreenLightChange((int)(percent * 100));
                }
            }

            @Override
            public void changeFinish() {
                hideCenterView();
            }
        });

        view_ScreenControlView.setIPlayerVoiceListener(new ScreenControlView.IPlayerVoiceListener() {
            @Override
            public void changeVoice(boolean isUp, float voice) {
                refreshCenterViewByVoice(voice);
                if(mSettingCallBack!=null) {
                    mSettingCallBack.onSoundChange((int)(voice * 100));
                }
            }

            @Override
            public void changeFinish() {
                hideCenterView();
            }
        });
      //  setSelectedDobleMode(mSingleFull);

        imageview_buyglasses.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallBack.onControlChanged(VIEW_ID_VIEW_BUY,true);
            }
        });
        textview_buyglasses.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                imageview_buyglasses.performClick();
            }
        });
    }

    public void setIPlayerRateListener(ScreenControlView.IPlayerRateListener listener){
        if(view_ScreenControlView!=null){
            view_ScreenControlView.setIPlayerRateListener(listener);
        }
    }

    private void refreshCenterViewByVoice(float voice) {
        showCenterView();
        lightprogress.setProgress((int)(voice * 100));
        if(voice==0){
            certer_show_view_image.setImageResource(R.drawable.play_touch_toast_icon_mute);
        }else{
            certer_show_view_image.setImageResource(R.drawable.play_touch_toast_icon_voice);
        }

    }

    private void refreshCenterViewByBright(float bright) {
        showCenterView();
       // certer_show_view_image.setImageResource(R.mipmap.player_bright_downdown);
        lightprogress.setProgress((int)(bright * 100));
        certer_show_view_image.setImageResource(R.drawable.play_touch_toast_icon_light);
    }

    private void showCenterView() {
        certer_show_view.setVisibility(View.VISIBLE);
        lightprogress.setVisibility(View.VISIBLE);
        certer_show_view_image.setVisibility(View.VISIBLE);
    }

    private void hideCenterView() {
        certer_show_view.setVisibility(View.GONE);
    }

    private boolean playFlag = true;//当前按钮显示暂停
    private boolean mSingleFull = false;
    @Override
    public void onClick(View view) {
        if(view==mClose){
            mCallBack.onControlChanged(VIEW_ID_CLOSE,false);
        }else if(view ==mPlayer_btn){
            playFlag=!playFlag;
            mCallBack.onPlayChanged(playFlag);
            if(playFlag){
                mPlayer_btn.setImageResource(R.drawable.play_touch_icon_play_normal);
            }else{
                mPlayer_btn.setImageResource(R.drawable.play_touch_icon_pause_normal);
            }
            //播放切换
        }else if(view ==player_definition_select){
            mCallBack.onControlChanged(VIEW_ID_HDSelect,true);
        }else if(view ==player_playsindex_select){
            mCallBack.onControlChanged(VIEW_ID_SelectSource,true);
        }else if(view ==imageview_doublemode||view == textview_doublemode){
            mSingleFull = !mSingleFull;
            if(mCallBack!=null) {
                mCallBack.onChangFullScreen(mSingleFull);
            }
            setGestureEnable();
            setDoubleModeImage(mSingleFull);
        }else if(view ==settingview){
            if(mCallBack!=null) {
                mCallBack.onControlChanged(VIEW_ID_SETTINGS,true);
            }
        }else if(view==player_playmode_select){
            player_playmode_select_isShow=!player_playmode_select_isShow;
            if(player_playmode_select_isShow){
                mCallBack.onControlChanged(VIEW_ID_playMode,true);
            }else{
                mCallBack.onControlChanged(VIEW_ID_playMode,false);
            }

        }else if(view==view_ScreenControlView){
           // mCallBack.onControlChanged(VIEW_ID_VIEW_TOUCH,true);
        }else if(view==imageview_player_previous){
            mCallBack.onControlChanged(VIEW_ID_VIEW_PREVIOUS,true);
        }else if(view==imageview_player_next){
            mCallBack.onControlChanged(VIEW_ID_VIEW_NEXT,true);
        }
    }
    boolean player_playmode_select_isShow=false;
    public void refreshPlayModeImage(boolean isShow){
        player_playmode_select_isShow=isShow;
        if(isShow){
            player_playmode_select.setImageResource(R.drawable.mj_player_play_touch_icon_model_click);
        }else{
            player_playmode_select.setImageResource(R.drawable.mj_player_play_touch_icon_model_normal);
        }
    }

    public void setDoubleModeImage(boolean moubleMode){
        if(moubleMode){
            imageview_doublemode.setImageResource(R.drawable.play_touch_icon_vr_normal);
            textview_doublemode.setText(mContext.getResources().getString(R.string.player_double_screen));
        }else{
            imageview_doublemode.setImageResource(R.drawable.play_touch_icon_vr_click);
            textview_doublemode.setText(mContext.getResources().getString(R.string.player_single_screen));
        }
    }

    public void setSelectedDobleMode(boolean moubleMode){
        mSingleFull=moubleMode;
        setGestureEnable();
        setDoubleModeImage(moubleMode);
    }

    IPlayerControlCallBack mCallBack;
    IPlayerSettingCallBack mSettingCallBack;
    public void setIPlayerControlCallBack(IPlayerControlCallBack callBack){
        mCallBack = callBack;
    }
    public void setIPlayerSettingCallBack(IPlayerSettingCallBack settingCallBack){
        mSettingCallBack = settingCallBack;
    }

    public void setDefinitionSelectShow(){
        player_definition_select.setVisibility(VISIBLE);
    }
    public void setPlaysIndexSelectShow(){
        player_playsindex_select.setVisibility(VISIBLE);
    }
    int mType=0;
    public void setType(int type){
        mType=type;
    }
    public void changeUIByPlayMode(int mode){
        LogHelper.d(PlayControlView.TAG,"contrl view changeUIByPlayMode");
        if(mType== GLConst.LOCAL_MOVIE||mType==GLConst.LOCAL_PANO){
            LogHelper.d(PlayControlView.TAG,"contrl view local:");
            player_playmode_select.setVisibility(VISIBLE);
            settingview.setVisibility(VISIBLE);
        }
        if(mode== VideoModeType.PLAY_MODE_SIMPLE_FULL){
            LogHelper.d(PlayControlView.TAG,"contrl view mSingleFull=true:");
            mSingleFull=true;
        }else{
            LogHelper.d(PlayControlView.TAG,"contrl view mSingleFull=false;:");
            mSingleFull=false;
        }
        setSelectedDobleMode(mSingleFull);
    }

    ScreenControlView.OnClickInOnTouchEvent onClickInOnTouchEvent=new ScreenControlView.OnClickInOnTouchEvent() {
        @Override
        public void onClick() {
            if(mCallBack!=null){
                mCallBack.onControlChanged(VIEW_ID_VIEW_TOUCH,true);
            }
        }
    };

    public void setGestureEnable(){
        LogHelper.d(TAG,"setGestureEnable mSingleFull："+mSingleFull);
        if(mType==GLConst.MOVIE||mType==GLConst.LOCAL_MOVIE){
            LogHelper.d(TAG,"setGestureEnable mSingleFull mType==GLConst.MOVIE");
            if(mSingleFull){
                view_ScreenControlView.setClickable(true);
                view_ScreenControlView.setLockScreen(false);
                view_ScreenControlView.setOnClickInOnTouchEventListener(onClickInOnTouchEvent);
                return;
            }else{
                view_ScreenControlView.setClickable(false);
                view_ScreenControlView.setLockScreen(true);
                view_ScreenControlView.setOnClickInOnTouchEventListener(null);
            }
        }
        //view_ScreenControlView.setVisibility(View.GONE);
        //view_ScreenControlView.setLockScreen(true);
    }

    /**
     * 更新进度
     * @param current
     * @param duration
     */
    public void updateProgress(final int current,final int duration){
        tv_total_time.post(new Runnable() {
            @Override
            public void run() {
                if(mSeekbar!=null) {
                    final int progress = (int) ((float) current / duration * 100);
                    mSeekbar.setProgress(progress);
                }
                boolean ishowhour=false;
                if(tv_total_time!=null) {
                    String dur="";
                    if(duration>60*60*1000){
                        ishowhour=true;
                        dur= TimeFormat.format(duration/1000);
                    }else{
                        dur= TimeFormat.formatHideHour(duration/1000);
                    }
                    tv_total_time.setText(dur);
                }

                if(tv_current_time!=null){
                    String cur="";
                    if(ishowhour){
                        cur= TimeFormat.format(current/1000);
                    }else{
                        cur= TimeFormat.formatHideHour(current/1000);
                    }
                    tv_current_time.setText(cur);
                }
            }
        });
    }

    public void setPlayOrPauseBtn(boolean isplay){
        if(isplay){
            mPlayer_btn.setImageResource(R.drawable.play_touch_icon_play_normal);
        }else{
            mPlayer_btn.setImageResource(R.drawable.play_touch_icon_pause_normal);
        }
        playFlag = isplay;
    }
    /**
     * true ：暂停   false：播放
     * @return
     */
    public boolean isPlayFlag(){
        return playFlag;
    }

    public void showView() {
        if(findViewById(R.id.downContainer)==null){
            return;
        }
        if (findViewById(R.id.downContainer).getVisibility() != VISIBLE) {
            findViewById(R.id.upContainer).startAnimation(top_show_amin);
            findViewById(R.id.downContainer).startAnimation(bottom_show_amin);
            findViewById(R.id.upContainer).setVisibility(View.VISIBLE);
            findViewById(R.id.downContainer).setVisibility(View.VISIBLE);
        }
    }
    @Override
    public int getVisibility(){
        return findViewById(R.id.downContainer).getVisibility();
    }

    public void hideView() {
        if(findViewById(R.id.downContainer)==null){
            return;
        }
        if(findViewById(R.id.downContainer).getVisibility() == VISIBLE) {
            findViewById(R.id.downContainer).startAnimation(bottom_hide_amin);
            top_hide_amin.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {}
                @Override
                public void onAnimationEnd(Animation animation) {
                    findViewById(R.id.upContainer).setVisibility(View.GONE);
                    findViewById(R.id.downContainer).setVisibility(View.GONE);
                }
                @Override
                public void onAnimationRepeat(Animation animation) {}
            });
            findViewById(R.id.upContainer).startAnimation(top_hide_amin);
        }
    }

    class SeekBarChangeEvent implements SeekBar.OnSeekBarChangeListener {

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress,
                                      boolean fromUser) {
            /* 拖动中，进度发生了变化，取progress值换算进度，最小值0，最大值100 */
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            /* 开始拖动触发该方法 */
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
             /* 拖动结束触发该方法 */
            LogHelper.d(TAG,"seekbar seek:"+seekBar.getProgress());

            if (mPlayerDuration >0) {
                LogHelper.d(TAG,"seekbar seek in:"+seekBar.getProgress());
                long all = mPlayerDuration * seekBar.getProgress() / 100;
                mCallBack.onSeekToChanged((int)all);
            }else {
                seekBar.setProgress(0);
            }
        }
    }
    long mPlayerDuration=0;
    public void setDisplayDuration(long duration){
        mPlayerDuration=duration;
    }
    public void setVideoTitle(String name){
        if(mVideoTitle!=null){
            mVideoTitle.setText(name);
        }
    }

    public void setHDname(String name){
        if(player_definition_select!=null){
            player_definition_select.setText(getStrName(name));
        }
    }

    public String getStrName(String type){
        if(type.endsWith("k")){
            return type.substring(0,type.length()-1)+"K";
        }else if(type.endsWith("p")){
            return type.substring(0,type.length()-1)+"P";
        }else if(!(type.endsWith("K")||type.endsWith("P"))){
            return type+"P";
        }
        return type;
    }

    public void setSeekBarProcess(int process){
        if(mSeekbar!=null){
            mSeekbar.setProgress(process);
        }
    }
}

