package com.mojing.vrplayer.view;

import android.text.TextUtils;
import android.util.Log;

import com.baofeng.mojing.input.base.MojingKeyCode;
import com.bfmj.sdk.util.TimeFormat;
import com.bfmj.viewcore.interfaces.GLOnKeyListener;
import com.bfmj.viewcore.interfaces.GLViewFocusListener;
import com.bfmj.viewcore.render.GLColor;
import com.bfmj.viewcore.util.GLFocusUtils;
import com.bfmj.viewcore.view.GLGroupView;
import com.bfmj.viewcore.view.GLImageView;
import com.bfmj.viewcore.view.GLRectView;
import com.bfmj.viewcore.view.GLRelativeView;
import com.bfmj.viewcore.view.GLTextView;
import com.mojing.vrplayer.R;
import com.mojing.vrplayer.activity.GLBaseActivity;
import com.mojing.vrplayer.activity.MjVrPlayerActivity;
import com.mojing.vrplayer.bean.AudioSteamInfo;
import com.mojing.vrplayer.bean.VideoModeType;
import com.mojing.vrplayer.interfaces.IPlayerControlCallBack;
import com.mojing.vrplayer.interfaces.IPlayerSettingCallBack;
import com.mojing.vrplayer.interfaces.IRePlayDialogCallBack;
import com.mojing.vrplayer.interfaces.IResetLayerCallBack;
import com.mojing.vrplayer.interfaces.IViewVisiableListener;
import com.mojing.vrplayer.page.BaseLocalPlayPage;
import com.mojing.vrplayer.page.BasePlayerPage;
import com.mojing.vrplayer.publicc.BrightnessUtil;
import com.mojing.vrplayer.publicc.NetworkUtil;
import com.mojing.vrplayer.publicc.SettingSpBusiness;
import com.mojing.vrplayer.publicc.StickUtil;
import com.mojing.vrplayer.publicc.bean.ContentInfo;
import com.mojing.vrplayer.publicc.bean.LocalVideoBean;
import com.mojing.vrplayer.publicc.bean.VideoDetailBean;
import com.mojing.vrplayer.utils.GLConst;
import com.mojing.vrplayer.utils.MJGLUtils;
import com.mojing.vrplayer.utils.SoundUtils;
import com.mojing.vrplayer.utils.ViewUtil;
import com.storm.smart.common.utils.LogHelper;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by wanghongfang on 2017/5/9.
 * 沉浸模式的播放控制UI调用管理类
 */
public class GLVRPlayControlUI extends BasePlayControlUI {
    protected GLRelativeView mControlLayer;
    protected GLRelativeView mSettingLayer;
    protected GLRelativeView mDialogLayer;
    protected GLRelativeView mLockLayer;
    protected GLRelativeView mSubtitleLayer; //字幕显示层
    protected MoviePlayerControlView moviePlayerControlView;
    protected MoviePlayerSettingView moviePlayerSettingView;
    protected GLTextToast glTextToast; //toast提示
    protected GLImageToast glImgToast; //带图片的Toast
    protected GLLoadingToast glLoadingToast; //loading
    protected GLLoadToast glLoadToast;
    protected GLDialogView2 glDialogView; //提示框
    protected GLDialogViewSingleBtn mSingleBtnDialog;
    protected GLUnLockDialog unLockDialog;
    protected GLLockScreenView lockView;
    protected RePlayDialogView rePlayView; //续播提示框
    protected RePlayDialogButtonView rePlayDialogButtonView;
    protected GLSeekToast glSeekToast;//快进快退显示
    protected GLTextView glSubtitleText; //字幕展示
    protected GLGroupView mRootView;
    protected boolean delay_showloadin_flag = false; //房子显示重叠 delay显示loading
    private final float anim_depth = 0.15f;
    private boolean resetLayerfocused = false;
    private boolean isFinish = false;
    private GLRelativeView subtitleLayoutView; //字幕
    private  int subtitleHeight = 150;


    public GLVRPlayControlUI(GLGroupView mRootView, GLBaseActivity activity, BasePlayerPage basePlayerPage, int type){
        super(activity,basePlayerPage);
        isFinish = false;
        this.mRootView = mRootView;
        this.mType = type;
        ((GLBaseActivity)mActivity).setIResetLayerCallBack(new IResetLayerCallBack() {
            @Override
            public void isOpen(boolean isOpen) {
//                if(isOpen) {
//                    moviePlayerControlView.showAllView();
//                    setLookAngle();
//                } else {
//                    //全部高级菜单子view隐藏
//                    moviePlayerSettingView.hideAllView();
//                    //隐藏控制菜单
//                    moviePlayerControlView.hideAllView();
//                }
//                doOpenMenu(isOpen);
            }

            @Override
            public void onFocusChange(boolean focused) {
                resetLayerfocused = focused;
////                LogHelper.d("cursor","--------onFocusChange---"+focused);
//                if(!focused) {
//                    if(null == moviePlayerControlView || !moviePlayerControlView.isShow()) {
////                        LogHelper.d("cursor","--------hideCursorView---setIResetLayerCallBack");
//                        ((GLBaseActivity)mActivity).hideCursorView();
//                    } else {
//                        hideViewTimer();
//                    }
//                } else {
////                    LogHelper.d("cursor","--------showCursorView---setIResetLayerCallBack");
//
////                    ((GLBaseActivity)mActivity).showCursorView();
////                    cancelHideViewTimer();
//
//                    if(null == moviePlayerControlView || !moviePlayerControlView.isShow()){
//                        moviePlayerControlView.showAllView();
//                        mResetView.setVisible(true);
//                        setLookAngle();
//                    }else{
//                        mResetView.setVisible(true);
//                        //全部高级菜单子view隐藏
//                        moviePlayerSettingView.hideAllView();
//                        //隐藏控制菜单
//                        moviePlayerControlView.hideAllView();
//                    }
//
//                }


//                LogHelper.d("cursor","--------onFocusChange---"+focused);
                if(!focused) {
                    if(null == moviePlayerControlView || !moviePlayerControlView.isShow()) {
//                        LogHelper.d("cursor","--------hideCursorView---setIResetLayerCallBack");
                     //   ((GLBaseActivity)mActivity).hideCursorView();
                    } else {
                        //hideViewTimer();
                    }
                } else {

                    if(!moviePlayerControlView.isShow()){
                        mResetView.setVisible(false);
                        moviePlayerControlView.showAllView();
                        moviePlayerSettingView.hideAllView();
//                    LogHelper.d("cursor","--------showCursorView---setIResetLayerCallBack");
                        ((GLBaseActivity)mActivity).showCursorView();
                        cancelHideViewTimer();
                        hideViewTimer();
                    }else{
                        moviePlayerControlView.hideAllView();
                        moviePlayerSettingView.hideAllView();
                        ((GLBaseActivity)mActivity).hideCursorView();
                        mResetView.setVisible(false);
                        cancelHideViewTimer();
                    }


                }

            }

            @Override
            public void onResetView() {
                //解决全景视频复位不准确的问题
                if(mControlLayer!=null) {
                    mControlLayer.setLookAngle(0);
                }
                if(mSettingLayer!=null) {
                    mSettingLayer.setLookAngle(0);
                }
                if(mDialogLayer!=null) {
                    mDialogLayer.setLookAngle(0);
                }
                if(mSubtitleLayer!=null) {
                    mSubtitleLayer.setLookAngle(0);
                }
                if(mLockLayer!=null) {
                    mLockLayer.setLookAngle(0);
                }
            }
        });
        createSubtitleLayer();
        createPlayerSettingView();
        createPlayerControlView();
        if(type== GLConst.MOVIE||type==GLConst.LOCAL_MOVIE) {
            createLockView();
        }
        createToastView();

        moviePlayerSettingView.setIPlayerSettingCallBack(settingCallBack);
        moviePlayerSettingView.setContrlViewInterface(new MoviePlayerSettingView.ContrlViewInterface(){
            @Override
            public void showContrlView() {
                moviePlayerSettingView.hideAllView();
                moviePlayerControlView.showAllView();
            }
        });
    }
    GLResetView mResetView;
    /**
     * 底部控制栏
     */
    protected void createPlayerControlView() {
        mControlLayer = new GLRelativeView(mActivity);
        mControlLayer.setLayoutParams(2400,2400);
        mControlLayer.setHandleFocus(false);
        moviePlayerControlView = new MoviePlayerControlView(mActivity,mType);
        moviePlayerControlView.setType(mType);
//        moviePlayerControlView.setX( 1200f-500f);
//        moviePlayerControlView.setY(1320f);
      //  moviePlayerControlView.setMargin(1200-500,1320f+60f,0,0);
        moviePlayerControlView.setMargin(1200-500,1000f,0,0);
        moviePlayerControlView.setIPlayerControlCallBack(controlCallBack);
        moviePlayerControlView.setIPlayerSettingCallBack(settingCallBack);
        moviePlayerControlView.setSubViewCallback(moviePlayerSettingView.getSubViewCallback());
        moviePlayerControlView.setMoviePlayerSettingView(moviePlayerSettingView);
        mControlLayer.addView(moviePlayerControlView);

        mResetView=new GLResetView(mActivity);
        mResetView.setVisible(true);
        mResetView.setMargin(1200-280,1000f+500,0,0);
        mControlLayer.addView(mResetView);
        mResetView.setFocusListener(new GLViewFocusListener() {
            @Override
            public void onFocusChange(GLRectView view, boolean focused) {
                if(focused){
                    cancelHideResetViewTimer();
                }else{
                    setResetDelayVisiable();
                }
            }
        });

        mResetView.setAddBtnFocusInterface(new GLResetView.AddBtnFocusInterface() {
            @Override
            public int focusListener(boolean focus) {
           //       Log.v("22222bb","KEYCODE_ENTER setAddbtnListener 2");
                if(!focus){
                   //   Log.v("22222bb","KEYCODE_ENTER setAddbtnListener 22 false");
                    if(null == moviePlayerControlView || !moviePlayerControlView.isShow()) {
                     //   Log.v("22222bb","KEYCODE_ENTER setAddbtnListener 22 false");
                    }
                }else{
                   // Log.v("22222bb","KEYCODE_ENTER setAddbtnListener 21 ");
                    if(!moviePlayerControlView.isShow()) {
                      //  Log.v("22222bb","KEYCODE_ENTER setAddbtnListener 21 false");
                        moviePlayerControlView.showAllView();
                        return 1;
                        //mResetView.showOther(true);
                    }else{
                      //  Log.v("22222bb","KEYCODE_ENTER setAddbtnListener 21 true");
                        moviePlayerControlView.hideAllView();
                        return 2;
                        //mResetView.showOther(false);
                    }
                }
                return 0;
            }
        });


        mControlLayer.setDepth(GLConst.Player_Controler_Depth,GLConst.Player_Controler_Scale);
        mRootView.addView(mControlLayer);
        moviePlayerControlView.setVisible(true);



    }
    protected void createPlayerSettingView() {
        mSettingLayer = new GLRelativeView(mActivity);
        mSettingLayer.setLayoutParams(2400,2400);
        mSettingLayer.setHandleFocus(false);
        moviePlayerSettingView = new MoviePlayerSettingView(mActivity);
        moviePlayerSettingView.setType(mType);
        moviePlayerSettingView.setMargin(1200-500,0,0,1090-50);
        mSettingLayer.addViewBottom(moviePlayerSettingView);
        mSettingLayer.setDepth(GLConst.Player_Settings_Depth,GLConst.Player_Settings_Scale);
        mRootView.addView(mSettingLayer);
        //在addview后设置下进度初始位置，这样可以避免第一次显示0进度，icon位置偏下
        boolean ismute = SettingSpBusiness.getInstance(mActivity.getApplicationContext()).getPlayerSoundMute();
        moviePlayerSettingView.setSoundIcon(!ismute);
        int value = BrightnessUtil.getUserBrightnessValue(mActivity.getApplicationContext());
        BrightnessUtil.setUserBrightnessValue(mActivity,value);
        //TODO  刷新界面
        int perValue=0;
        if(value==0){
            perValue=0;
        }else{
            perValue=value*100/255;
            if(perValue>100){
                perValue=100;
            }
        }
        setSelectedScreenLight(perValue);
    }

    protected void createToastView() {
        mDialogLayer = new GLRelativeView(mActivity);
        mDialogLayer.setLayoutParams(2400,2400);
        mDialogLayer.setHandleFocus(false);
        glDialogView = new GLDialogView2(mActivity);
        glDialogView.setMargin(1200f-400,1200f-100,0f,0f);
//        glDialogView.setDepth(GLConst.Dialog_Depth,GLConst.Dialog_Scale);
        mDialogLayer.addView(glDialogView);
        glDialogView.setVisible(false);

        mSingleBtnDialog = new GLDialogViewSingleBtn(mActivity);
        mSingleBtnDialog.setMargin(1200f-400,1200f-100,0f,0f);
//        mSingleBtnDialog.setDepth(GLConst.Dialog_Depth,GLConst.Dialog_Scale);
        mDialogLayer.addView(mSingleBtnDialog);
        mSingleBtnDialog.setVisible(false);

        unLockDialog = new GLUnLockDialog(mActivity);
        unLockDialog.setMargin(1200f-400,1200f-100,0f,0f);
//        unLockDialog.setDepth(GLConst.Dialog_Depth,GLConst.Dialog_Scale);
        mDialogLayer.addView(unLockDialog);
        unLockDialog.setVisible(false);


        glImgToast = new GLImageToast(mActivity);
//        glImgToast.setMargin(1200f-200,1200-100f,0f,0f);
        glImgToast.setMargin(1200-glImgToast.getWidth()/2,1200-100,0,0);
        mDialogLayer.addView(glImgToast);
        glImgToast.setVisible(false);

        glTextToast = new GLTextToast(mActivity);
        glTextToast.setMargin(1200f-200,1200-100f,0f,0f);
//        glTextToast.showToast("切换中",GLTextToast.SHORT);
//        glTextToast.showToast("已为您切换到1080P清晰度",GLTextToast.MEDIUM);
//        glTextToast.showToast("网络原因，已为您切换到1080P清晰度",GLTextToast.LONG);
        mDialogLayer.addView(glTextToast);
        glTextToast.setVisible(false);

        //显示加载中的loading
        glLoadingToast = new GLLoadingToast(mActivity);
        glLoadingToast.setMargin(1200f-270/2,1200f-80,0f,0f);
        mDialogLayer.addView(glLoadingToast);
        glLoadingToast.setVisible(false);

        //首次播放显示即将播放和续播toast
        glLoadToast = new GLLoadToast(mActivity);
        glLoadToast.setMargin(1200f-525/2,1200f-80,0f,0f);
        mDialogLayer.addView(glLoadToast);
        glLoadToast.setVisible(false);

        rePlayView = new RePlayDialogView(mActivity);
        rePlayView.setMargin(1200f-(268*3+12*2)/2,1200f-(60+70+(156+10+35)*2+23+15+70)/2,0f,0f);
        rePlayView.setIRePlayDialogCallBack(rePlayDialogCallBack);
        mDialogLayer.addView(rePlayView);
        rePlayView.setVisible(false);

        rePlayDialogButtonView = new RePlayDialogButtonView(mActivity);
        rePlayDialogButtonView.setMargin(1200-(240*2+60)/2,1200-70/2,0,0);
        rePlayDialogButtonView.setIRePlayDialogCallBack(rePlayDialogCallBack);
        mDialogLayer.addView(rePlayDialogButtonView);
        rePlayDialogButtonView.setVisible(false);

        glSeekToast = new GLSeekToast(mActivity);
        glSeekToast.setMargin(1200-(305/2),1200-145/2,0,0);
        mDialogLayer.addView(glSeekToast);
        glSeekToast.setVisible(false);

        mDialogLayer.setDepth(GLConst.Dialog_Depth,GLConst.Dialog_Scale);
        mRootView.addView(mDialogLayer);
        setListener();
    }

    private void createSubtitleLayer(){
        mSubtitleLayer = new GLRelativeView(mActivity);
        mSubtitleLayer.setLayoutParams(2400,2400);
        mSubtitleLayer.setHandleFocus(false);
        //展示字幕的View
        subtitleLayoutView = new GLRelativeView(mActivity);
        subtitleLayoutView.setLayoutParams(1200,subtitleHeight);
        glSubtitleText = new GLTextView(mActivity);
        glSubtitleText.setLayoutParams(1200,subtitleHeight);
        glSubtitleText.setTextSize(40);
        glSubtitleText.setTextColor(new GLColor(0xffbbbbbb));
        glSubtitleText.setAlignment(GLTextView.ALIGN_CENTER);
        glSubtitleText.setText("-----字幕测试---");
        subtitleLayoutView.addView(glSubtitleText);
        subtitleLayoutView.setMargin(1200-(1200/2),1570,0,0);
        mSubtitleLayer.addView(subtitleLayoutView);
        glSubtitleText.setVisible(false);
        mSubtitleLayer.setDepth(GLConst.Subtitle_Depth,GLConst.Subtitle_Scale);
        mRootView.addView(mSubtitleLayer);
    }

    private void createLockView(){
        mLockLayer = new GLRelativeView(mActivity);
        mLockLayer.setLayoutParams(2400,2400);
        mLockLayer.setHandleFocus(false);
        lockView= new GLLockScreenView(mActivity);
//        lockView.setX(1200-GLLockScreenView.lock_view_widht/2);
//        lockView.setY(1200-playerHeight/2-80);
        lockView.setMargin(1200-GLLockScreenView.lock_view_widht/2,(1200-basePlayerPage.playerHeight/2)-15, 0,0);
        mLockLayer.addView(lockView);
        mLockLayer.setDepth(GLConst.LockScreen_Depth,GLConst.LockScreen_Scale);
        mRootView.addView(mLockLayer);
        lockView.setViewVisable(false);
        setLockListener();
        moviePlayerControlView.setLockScreenBtnKeyListener(  lockView.getLockKeyListener());
    }

    public void setLockListener(){
        lockView.setLockCallback(new GLLockScreenView.ILockScreenListener() {
            @Override
            public void onLockChanged(boolean isLocked) {
                mRootView.setFixed(isLocked);
                ((MjVrPlayerActivity)mActivity).isLockScreen = isLocked;
                ((MjVrPlayerActivity)mActivity).onLockChanged(isLocked);
                SettingSpBusiness.getInstance(mActivity.getApplicationContext()).setPlayerLockScreen(isLocked);

            }
        });
    }

    @Override
    public void onResum() {
        super.onResum();

    }

    @Override
    public void onFinish() {
        if(mType==GLConst.MOVIE||mType==GLConst.LOCAL_MOVIE) {
            mRootView.setFixed(false);
            ((MjVrPlayerActivity) mActivity).onLockChanged(false);
            ((MjVrPlayerActivity) mActivity).hideSkyBox();
        }
        isFinish = true;
        hideAllView();

        super.onFinish();
    }

    protected void doOpenMenu(boolean isOpen) {
        if(lockView!=null) {
            if(!isOpen&&!lockView.isFocused()) {
                lockView.setViewVisable(isOpen);
            }
        }
    }

    /**
     * 设置控制UI是否显示
     * @param visiable
     */
    public void setUIVisiable(final boolean visiable){
        MJGLUtils.exeGLQueueEvent(mActivity, new Runnable() {
            @Override
            public void run() {
                if(mControlLayer!=null){
                    mControlLayer.setVisible(visiable);
                }
                if(mSettingLayer!=null){
                    mSettingLayer.setVisible(visiable);
                }
                if(mDialogLayer!=null){
                    mDialogLayer.setVisible(visiable);
                }
                if(mLockLayer!=null){
                    mLockLayer.setVisible(visiable);
                }
                if(mSubtitleLayer!=null){
                    mSubtitleLayer.setVisible(visiable);
                }
            }
        });

    }

    @Override
    public  void showLoading(){
        super.showLoading();
        if(isDialogShowing()){
            return;
        }
        if(show_load_toast == true &&basePlayerPage!=null&&basePlayerPage.getPlayerCurrentState()== BasePlayerView.PlayerState.PREPARED){
            show_load_toast = false;
        }
        showLoadToast(!show_load_toast);
        mLoadingTime = System.currentTimeMillis();
        if (basePlayerPage!=null&&basePlayerPage.getPlayerCurrentState()== BasePlayerView.PlayerState.PREPARED) {
            mStartTipsTime = System.currentTimeMillis();
            if (mLoadingCount > 0) {
                mLoadingCount++;
            }
        }
    }

    @Override
    public void hideLoading(){
        super.hideLoading();
        delay_showloadin_flag = false;
        hideLoadToast();
        mLoadingTime = -1;
        if ( basePlayerPage!=null&&basePlayerPage.getPlayerCurrentState()== BasePlayerView.PlayerState.PREPARED) {
            mStartTipsTime = -1;
        }
    }

    @Override
    public void showToast(String text, int type) {
        super.showToast(text, type);
        if(isLoadingShowing()){
            delay_showloadin_flag = true;
            hideLoadToast();
        }
        if(glTextToast!=null){
            glTextToast.showToast(text,type);
        }
    }

    @Override
    public void showImgToast(String text, int imgRes) {
        super.showImgToast(text, imgRes);
        if(glImgToast!=null){
            glImgToast.showToast(text,R.drawable.play_toast_icon_model);
        }
    }

    /**
     * 网络状态改变后的处理
     * @param isPlayError
     */
    @Override
    public void doNetChanged(final boolean isPlayError){
        super.doNetChanged(isPlayError);

        if(!NetworkUtil.isNetworkConnected(mActivity)){//无网络
            if(glDialogView!=null){
                glDialogView.showExceptionDialog(mActivity.getResources().getString(R.string.player_network_exception),mActivity.getString(R.string.player_reload),false);
                showDialogView(1);
            }
            hideLoading();
            if(basePlayerPage!=null) {
                basePlayerPage.pausePlay();
            }

            return;
        }
        if(isPlayError){
            if(glDialogView!=null){
                glDialogView.showExceptionDialog(mActivity.getResources().getString(R.string.player_load_failed),mActivity.getString(R.string.player_reload),false);
//                        glDialogView.setVisible(true);
                showDialogView(1);
            }
            hideLoading();
            if(basePlayerPage!=null) {
                basePlayerPage.pausePlay();
            }

            return;
        }


        if(!NetworkUtil.canPlayAndDownload(mActivity) ){ //有网络 但为非WIFI网络
            if(glDialogView!=null){
                glDialogView.showExceptionDialog(mActivity.getResources().getString(R.string.player_no_wifi),mActivity.getString(R.string.player_play_continue),true);
                showDialogView(1);
            }
            hideLoading();
            if(basePlayerPage!=null){
                basePlayerPage.pausePlay();
                basePlayerPage.onMobileNet();
            }

            return;
        }else { //连上网络
            if(glDialogView!=null&&basePlayerPage!=null&&basePlayerPage.getPlayerCurrentState()== BasePlayerView.PlayerState.PREPARED) {
                if(!basePlayerPage.isPauseView) {
                    basePlayerPage.startPlay();
                }
            }else {
                if(!basePlayerPage.isPauseView) {
                    basePlayerPage.rePlay();
                }
            }
            if(basePlayerPage.isPauseView){
                moviePlayerControlView.setPlayOrPauseBtn(false);
            }
            hideDialogView();
        }


    }

    @Override
    public void handleNetWorkException(){
         super.handleNetWorkException();
        if (!getLoadToastVisiable()) {
            return;
        }
//        LogHelper.d("login"," mLoadingtime = "+mLoadingTime+" mLoadingCount = "+mLoadingCount+"  current = "+System.currentTimeMillis()+",alltime = "+mLoadingAllTime);
//        /**当次播放时，首次出现卡顿超过10s，提示*/
//        if(mStartTipsTime!=-1 && System.currentTimeMillis() - mStartTipsTime>3*1000){
//            if(mLoadingCount==0){
//                hideLoadToast();
//                mSingleBtnDialog.showNetworkTips(4 * 1000,false, new  GLDialogViewSingleBtn.IGLDismissListener() {
//                    @Override
//                    public void dismiss() {
//                        if(mStartTipsTime>0){
//                            showLoadToast(true);
//                        }
//                    }
//                });
//                showDialogView(2);
//                mLoadingAllTime = System.currentTimeMillis();
//                mLoadingCount++;
//            }
//        }
//        /**当首次卡顿到第三次卡顿间隔<=30s*/
//        if(mLoadingCount==2&&mLoadingAllTime>0&&System.currentTimeMillis()-mLoadingAllTime<=30*1000){
//            hideLoadToast();
//            mSingleBtnDialog.showNetworkTips(4 * 1000,false, new GLDialogViewSingleBtn.IGLDismissListener() {
//                @Override
//                public void dismiss() {
//                    if(mStartTipsTime>0){
//                        showLoadToast(true);
//                    }
//                }
//            });
//            showDialogView(2);
//        }
        /**loading超过1分钟显示网络加载失败*/
        if (mLoadingTime != -1 && System.currentTimeMillis() - mLoadingTime >60000 ) {
            doNetChanged(true);
            hideLoading();
        }
    }

    @Override
    public void setHDdata(String[] strs, String defaultHD) {
        super.setHDdata(strs, defaultHD);
        moviePlayerSettingView.setHDdata(strs, defaultHD);
    }

    @Override
    public void setSelectedHD(String hdtype) {
        super.setSelectedHD(hdtype);
        if(moviePlayerSettingView!=null) {
            moviePlayerSettingView.setSelectedHD(hdtype);
        }
    }

    @Override
    public void setCurrentNum(int index) {
        super.setCurrentNum(index);
        if(moviePlayerSettingView!=null){
            moviePlayerSettingView.setCurrentNum(index);
        }
    }

    @Override
    public void setName(String name) {
        super.setName(name);
        if(moviePlayerControlView!=null){
            moviePlayerControlView.setName(name);
        }
    }

    @Override
    public void setDisplayDuration(long duration) {
        super.setDisplayDuration(duration);
        if(moviePlayerControlView!=null){
            moviePlayerControlView.updateDisplayDuration(duration);
        }

    }

    @Override
    public void setMovieVideoDatas(VideoDetailBean videosBean, int index) {
        super.setMovieVideoDatas(videosBean, index);
        if(moviePlayerSettingView!=null){
            moviePlayerSettingView.setMovieVideoDatas(videosBean,index);
        }
    }

    @Override
    public void setPlayOrPauseBtn(final boolean playOrPause) {
        super.setPlayOrPauseBtn(playOrPause);
        MJGLUtils.exeGLQueueEvent(mActivity, new Runnable() {
            @Override
            public void run() {
                if(moviePlayerControlView!=null){
                    moviePlayerControlView.setPlayOrPauseBtn(playOrPause);
                }
            }
        });

    }

    @Override
    public boolean isPlayFlag() {
        if(moviePlayerControlView!=null) {
            return moviePlayerControlView.isPlayFlag();
        }
        return super.isPlayFlag();
    }


    @Override
    public void setVolume(int vm) {
        super.setVolume(vm);
        if(moviePlayerSettingView!=null) {
            moviePlayerSettingView.setVolume(vm);
        }
    }

    @Override
    public void setSoundMute(boolean flag) {
        super.setSoundMute(flag);
        if(moviePlayerSettingView!=null) {
            moviePlayerSettingView.setSoundIcon(!flag);
        }
    }

    @Override
    public void setType(int type) {
        super.setType(type);
    }

    @Override
    public void updateProgress(int current, int duration) {
        super.updateProgress(current, duration);
        if(moviePlayerControlView!=null){
            moviePlayerControlView.updateProgress(current,duration);
        }
    }

    @Override
    public void setProcess(int process) {
        super.setProcess(process);
        if(moviePlayerControlView!=null&&process==0){
            moviePlayerControlView.setProcess(process);
        }
    }

    @Override
    public boolean getLoadToastVisiable(){
        return glLoadToast.isVisible()||glLoadingToast.isVisible();
    }

    private boolean isToastShowing(){
        return (glTextToast!=null&&glTextToast.isVisible())||(glImgToast!=null&&glImgToast.isVisible())||(glSeekToast!=null&&glSeekToast.isVisible());
    }
    private boolean isLoadingShowing(){
        return (glLoadToast!=null&&glLoadToast.isVisible())||(glLoadingToast!=null&&glLoadingToast.isVisible());
    }

    public void showLoadToast(final boolean loading){
        if(isToastShowing()){
            delay_showloadin_flag = true;
            return;
        }

        MJGLUtils.exeGLQueueEvent(mActivity, new Runnable() {
            @Override
            public void run() {
                if(null != moviePlayerControlView && !moviePlayerControlView.isShow()) {
                    setLookAngle();
                }
                if(loading){
                    glLoadingToast.setVisible(true);
                    glLoadToast.setVisible(false);
                }else {
                    glLoadingToast.setVisible(false);
                    glLoadToast.setVisible(true);
                }
            }
        });


    }

    public void hideLoadToast(){

        MJGLUtils.exeGLQueueEvent(mActivity, new Runnable() {
            @Override
            public void run() {
                if(glLoadingToast!=null){
                    glLoadingToast.setVisible(false);
                }
                if(glLoadToast!=null){
                    glLoadToast.setVisible(false);
                }
            }
        });

    }

    @Override
    public void hideDialogView(){
        if(glDialogView!=null){
            glDialogView.setVisible(false);
        }
        if(mSingleBtnDialog!=null){
            mSingleBtnDialog.setVisible(false);
        }
        ((GLBaseActivity)mActivity).showResetLayerView();
    }
    public void showDialogView(int type){
        hideAllDialogView();
        if(type==1){//双按钮
            if(glDialogView!=null){
                glDialogView.setVisible(true);
            }
            if(mSingleBtnDialog!=null){
                mSingleBtnDialog.setVisible(false);
            }
        }else if(type==2){//单按钮
            if(glDialogView!=null){
                glDialogView.setVisible(false);
            }
            if(mSingleBtnDialog!=null){
                mSingleBtnDialog.setVisible(true);
            }
        }
    }

    @Override
    public boolean isDialogShowing(){
        return mSingleBtnDialog.isVisible()||glDialogView.isVisible()||rePlayView.isVisible()||rePlayDialogButtonView.isVisible()||unLockDialog.isVisible();
    }

    @Override
    public boolean isRePlayDialogShowing() {
        return rePlayView.isVisible()||rePlayDialogButtonView.isVisible();
    }

    private void doExceptionRePlay(){
       if(!NetworkUtil.isNetworkConnected(mActivity)){ //无网络时 不处理
           return;
       }
       if(basePlayerPage!=null){
           if(glDialogView.getIsContinueBtn()){//继续播放
               basePlayerPage.doPlayContinue();
           }else{//从新播放
               basePlayerPage.rePlay();
           }
           basePlayerPage.notifyHideDialog();
           hideDialogView();
       }
   }

    public void setListener(){
        glDialogView.setLeftKeyListener(new GLOnKeyListener() {  //退出
            @Override
            public boolean onKeyDown(GLRectView view, int keycode) {
                (mActivity).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        basePlayerPage.finish();
                        mActivity.finish();
                    }
                });

                return false;
            }

            @Override
            public boolean onKeyUp(GLRectView view, int keycode) {
                return false;
            }

            @Override
            public boolean onKeyLongPress(GLRectView view, int keycode) {
                return false;
            }
        });
        glDialogView.setRightKeyListener(new GLOnKeyListener() { //继续播放 或 重新播放
            @Override
            public boolean onKeyDown(GLRectView view, int keycode) {
                doExceptionRePlay();
                return false;
            }

            @Override
            public boolean onKeyUp(GLRectView view, int keycode) {
                return false;
            }

            @Override
            public boolean onKeyLongPress(GLRectView view, int keycode) {
                return false;
            }
        });

        glDialogView.setFocusListener(new GLViewFocusListener() {
            @Override
            public void onFocusChange(GLRectView view, boolean focused) {
//                Log.d("cursor","--------showCursorView---glDialogView");
                if(focused) {
                    ((GLBaseActivity)mActivity).showCursorView();
                } else {
                    ((GLBaseActivity)mActivity).hideCursorView2();
                }
            }
        });
        unLockDialog.setFocusListener(new GLViewFocusListener() {
            @Override
            public void onFocusChange(GLRectView view, boolean focused) {
//                Log.d("cursor","--------showCursorView---unLockDialog");
                if(focused) {
                    ((GLBaseActivity)mActivity).showCursorView();
                } else {
                    ((GLBaseActivity)mActivity).hideCursorView2();
                }
            }
        });
        rePlayView.setFocusListener(new GLViewFocusListener() {
            @Override
            public void onFocusChange(GLRectView view, boolean focused) {
//                Log.d("cursor","--------showCursorView---rePlayView");
                if(focused) {
                    ((GLBaseActivity)mActivity).showCursorView();
                } else {
                    ((GLBaseActivity)mActivity).hideCursorView2();
                }
            }
        });
        rePlayDialogButtonView.setFocusListener(new GLViewFocusListener() {
            @Override
            public void onFocusChange(GLRectView view, boolean focused) {
//                Log.d("cursor","--------showCursorView---rePlayDialogButtonView");
                if(focused) {
                    ((GLBaseActivity)mActivity).showCursorView();
                } else {
                    ((GLBaseActivity)mActivity).hideCursorView2();
                }
            }
        });

        glTextToast.setOnToastDismisListener(new GLTextToast.IToastDismisListener() {
            @Override
            public void onDismiss() {
                if(delay_showloadin_flag){
                    showLoading();
                }
            }
        });
        glImgToast.setOnToastDismisListener(new GLTextToast.IToastDismisListener() {
            @Override
            public void onDismiss() {
                if(delay_showloadin_flag){
                    showLoading();
                }
            }
        });

        glSeekToast.setOnToastDismisListener(new GLTextToast.IToastDismisListener() {
            @Override
            public void onDismiss() {
                if(delay_showloadin_flag){
                    showLoading();
                }
            }
        });

        moviePlayerSettingView.setOnViewVisiableListener(new IViewVisiableListener() {
            @Override
            public void onVisibility(boolean isvisible) {
                if(!isvisible){
                    if(mSettingLayer.getDepth()!=GLConst.Player_Settings_Depth){
                        startAnim(true);
                    }
                }else {
                    startAnim(false);
                }
            }
        });
        moviePlayerControlView.setOnViewVisiableListener(new IViewVisiableListener() {
            @Override
            public void onVisibility(boolean isvisible) {
                if(!isvisible){
                    if(mControlLayer.getDepth()!=GLConst.Player_Controler_Depth){
                        startAnim(true);
                    }
                }
            }
        });


    }

    private void startAnim(boolean isReset){
        if(isReset){
//            mControlLayer.setDepth(GLConst.Player_Controler_Depth);
//            mSettingLayer.setDepth(GLConst.Player_Settings_Depth);
            mControlLayer.setDepth(mControlLayer.getDepth()-anim_depth);
            mSettingLayer.setDepth(mSettingLayer.getDepth()+anim_depth);
            return;
        }
        //基础面板后退放大  高级面板迁移缩小
        mControlLayer.setDepth(mControlLayer.getDepth()+anim_depth);
        mSettingLayer.setDepth(mSettingLayer.getDepth()-anim_depth);
    }

    private Timer timer;

    public void setDelayVisiable(int duration){
        if(timer!=null){
            timer.cancel();
            timer = null;
        }
        timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                //全部高级菜单子view隐藏
                if(null != moviePlayerSettingView) {
                    moviePlayerSettingView.hideAllView();
                }
                //隐藏控制菜单
                if(null != moviePlayerControlView) {
                    moviePlayerControlView.hideAllView();
                }

                //菜单失去焦点隐藏，要设置底部按钮关闭
             //   ((GLBaseActivity)mActivity).showClose(false);
//                mResetView.setVisible(false);
//                mResetView.showClose(true);
                if(null == rePlayView || !rePlayView.isVisible()) {
//                    Log.d("cursor","--------hideCursorView---setDelayVisiable");
                    //隐藏头控焦点
                }
                doOpenMenu(false);
            }
        };
        timer.schedule(task,duration);
    }


    public void hideViewTimer(){
        if(isFinish) return;
        setDelayVisiable(2*1000);
    }

    public void cancelHideViewTimer(){
        if(timer!=null){
            timer.cancel();
            timer = null;
        }
    }

    private Timer resetViewCloseTimer;

    public void setResetDelayVisiable(int duration){
        if(resetViewCloseTimer!=null){
            resetViewCloseTimer.cancel();
            resetViewCloseTimer = null;
        }
        resetViewCloseTimer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                if(moviePlayerControlView.isShow()) {
                    setResetDelayVisiable();
                }else{
                    mResetView.setVisible(false);
                    mResetView.showClose(true);
                   // ((GLBaseActivity)mActivity).hideCursorView();
                }
            }
        };
        resetViewCloseTimer.schedule(task,duration);
    }

    public void setResetDelayVisiable(){
        if(isFinish) return;
        setResetDelayVisiable(2*1000);
    }

    public void cancelHideResetViewTimer(){
        if(resetViewCloseTimer!=null){
            resetViewCloseTimer.cancel();
            resetViewCloseTimer = null;
        }
    }


    private IRePlayDialogCallBack rePlayDialogCallBack = new IRePlayDialogCallBack() {
        @Override
        public void onSelected(ContentInfo contentInfo, int currentNum) {
              if(basePlayerPage!=null){
                  basePlayerPage.onSelected(contentInfo,currentNum);
              }
        }

        @Override
        public void onBtnClick(boolean isRePlay) {
            if(basePlayerPage!=null){
                basePlayerPage.onReplayBtnClick(isRePlay);
            }
            if(rePlayView!=null&&rePlayView.isVisible()){
                rePlayView.setVisible(false);
            }
            if(rePlayDialogButtonView!=null&&rePlayDialogButtonView.isVisible()){
                rePlayDialogButtonView.setVisible(false);
            }
        }

        @Override
        public void onStopTimer() {
            if(basePlayerPage!=null){
                basePlayerPage.stopChangeTimer();
            }
        }
    };

    private IPlayerControlCallBack controlCallBack = new IPlayerControlCallBack() {

        @Override
        public void onPlayChanged(boolean status) {
             if(basePlayerPage!=null){
                 basePlayerPage.onPlayChanged(status);
             }
        }

        @Override
        public void onSeekToChanged(int curPosition) {
            if(curPosition>=0) {
                basePlayerPage.onSeekToChanged(curPosition);
            }
        }

        @Override
        public void onControlChanged(String id, boolean selectedStatus) {
            if(MoviePlayerControlView.SETTING.equals(id)) {
                moviePlayerSettingView.hideAllView();//全部高级菜单子view隐藏
                moviePlayerSettingView.setMovieSettingViewShow(selectedStatus);
            } else if(MoviePlayerControlView.SELECTED_SOURCE.equals(id)) {
                moviePlayerSettingView.hideAllView();//全部高级菜单子view隐藏
                moviePlayerSettingView.setSelectSourceViewShow(selectedStatus);
            } else if(MoviePlayerControlView.SOUND.equals(id)) {
                moviePlayerSettingView.hideAllView();//全部高级菜单子view隐藏
                moviePlayerSettingView.setSoundBarViewShow(selectedStatus);
            } else if(MoviePlayerControlView.HD_MODE.equals(id)) {
                moviePlayerSettingView.hideAllView();//全部高级菜单子view隐藏
                moviePlayerSettingView.setHDTypeViewShow(selectedStatus);
            }else if(MoviePlayerControlView.SELECTED_RESET.equals(id)) {
                hideAllView();
                //moviePlayerSettingView.hideAllView();//全部高级菜单子view隐藏

                basePlayerPage.setResetViewVisible(true);

                setDelayVisiable1(3000);

            }


        }

        @Override
        public void onHideControlAndSettingView(boolean isHide) {
            if(isHide) {
                hideViewTimer();
            } else {
                cancelHideViewTimer();
            }
        }

        @Override
        public void onChangFullScreen(boolean fullScreen) {

        }

    };

    public void cancelDelay1(){
        if(timerq!=null){
            timerq.cancel();
            timerq = null;
        }
    }
    /**
     * 无操作时延迟3s隐藏
     */
    Timer timerq ;
    public void setDelayVisiable1(int duraion){
        if(timerq!=null){
            timerq.cancel();
            timerq = null;
        }
        timerq = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                showCursorView();
            }
        };
        timerq.schedule(task, duraion);
    }


    public void showCursorView() {
            MJGLUtils.exeGLQueueEvent(mActivity, new Runnable() {
                @Override
                public void run() {
                    basePlayerPage.setResetViewVisible(false);
                    ((GLBaseActivity)mActivity).initHeadView();

                    if(mControlLayer!=null) {
                        mControlLayer.setLookAngle(0);
                    }
                    if(mSettingLayer!=null) {
                        mSettingLayer.setLookAngle(0);
                    }
                    if(mDialogLayer!=null) {
                        mDialogLayer.setLookAngle(0);
                    }
                    if(mSubtitleLayer!=null) {
                        mSubtitleLayer.setLookAngle(0);
                    }
                    if(mLockLayer!=null) {
                        mLockLayer.setLookAngle(0);
                    }
                }
            });

    }



    private IPlayerSettingCallBack settingCallBack = new IPlayerSettingCallBack(){

        @Override
        public void onSettingShowChange(String id,boolean isShow) {

        }

        @Override
        public void onHideControlAndSettingView(boolean isHide) {
            if(isHide) {
                hideViewTimer();
            } else {
                cancelHideViewTimer();
            }
        }

        @Override
        public void onHDChange(final String hdtype) {
            int result = SettingSpBusiness.getInstance(mActivity.getApplicationContext()).getHigh();
            if (2 != result && "4k".equals(hdtype)) {
                unLockDialog.setmUnLockCallBack(new GLUnLockDialog.UnLockCallBack() {
                    @Override
                    public void onConfirm() {
                        if(basePlayerPage!=null) {
                            basePlayerPage.onChangeHd(hdtype);
                        }
                    }

                    @Override
                    public void onCancel() {
                        moviePlayerSettingView.setSelectedHD(basePlayerPage.mCurDefinition);
                    }
                });
                unLockDialog.setVisible(true);
                return;
            }
            if(basePlayerPage!=null) {
                basePlayerPage.onChangeHd(hdtype);
            }
        }

        @Override
        public void onHDable(boolean isAble) {
            setHDable(isAble);
            if(basePlayerPage!=null) {
                basePlayerPage.onHDable(isAble);
            }
        }

        @Override
        public void onSoundChange(int vm) {
            if(vm>0){
                SoundUtils.SetVolumeMute(false,mActivity.getApplicationContext());
                SettingSpBusiness.getInstance(mActivity.getApplicationContext()).setPlayerSoundMute(false);
            }
            SoundUtils.SetSoundVolume(vm,mActivity.getApplicationContext());
            if(moviePlayerSettingView!=null){
                moviePlayerSettingView.setVolume(vm);
            }
            if(basePlayerPage!=null) {
                basePlayerPage.onSoundChange(vm);
            }
        }

        @Override
        public void isOpenSound(boolean isOpen) {
            SoundUtils.SetVolumeMute(!isOpen,mActivity.getApplicationContext());
            SettingSpBusiness.getInstance(mActivity.getApplicationContext()).setPlayerSoundMute(!isOpen);
            if(moviePlayerSettingView!=null){
                moviePlayerSettingView.setSoundIcon(isOpen);
            }
            if(basePlayerPage!=null) {
                basePlayerPage.isOpenSound(isOpen);
            }
        }

        @Override
        public void onSelected(int num) {
            if(basePlayerPage!=null) {
                basePlayerPage.onChangeSelectIndex(num);
            }
            //选集过后关闭所有操作菜单
            moviePlayerControlView.hideAllView();
            moviePlayerSettingView.hideAllView();
            ((GLBaseActivity)mActivity).showClose(false);
//            Log.d("cursor","--------hideCursorView---onSelected");
            ((GLBaseActivity)mActivity).hideCursorView();
        }

        @Override
        public void onRatioChange(String ratioName) {
            if(basePlayerPage!=null) {
                basePlayerPage.onRatioChange(ratioName);
            }
        }

        @Override
        public void onRoteChange(int roteType) {
            if(basePlayerPage!=null) {
                basePlayerPage.onRoteChange(roteType);
            }
        }

        @Override
        public void onLeftRightModeChange(int modetype) {
            if(basePlayerPage!=null) {
                basePlayerPage.onLeftRightModeChange(modetype);
            }
        }

        @Override
        public void onRoundModeChange(int modetype) {
            if(basePlayerPage!=null) {
                basePlayerPage.onRoundModeChange(modetype);
            }
        }

        @Override
        public void onResetMode() {

        }

        @Override
        public void onSelectedMovie(ContentInfo contentInfo, int currentNum) {
            if(basePlayerPage!=null) {
                basePlayerPage.onSelectedMovie(contentInfo,currentNum);
            }
        }

        @Override
        public void onSelectedLocalMovie(LocalVideoBean bean, int currentNum) {
            if(basePlayerPage!=null) {
                basePlayerPage.onSelectedLocalMovie(bean, currentNum);
                //同类型资源切换时需要关闭底部控制
                mActivity.showClose(false);
            }
        }

        @Override
        public void onSelectedLocalMovieOri(int Ori) {

             if(basePlayerPage instanceof BaseLocalPlayPage&&MjVrPlayerActivity.videoList!=null){
                 int currentNum=((BaseLocalPlayPage) basePlayerPage).index;
                 if(Ori==0){//下一个
                     currentNum++;
                 }else{
                     currentNum--;
                 }
                 if(currentNum>=0&&currentNum<MjVrPlayerActivity.videoList.size()){
                     onSelectedLocalMovie(MjVrPlayerActivity.videoList.get(currentNum),currentNum);
                 }
             }
        }

        @Override
        public void onScreenSizeChange(int modetype) {
            if(basePlayerPage!=null) {
                getSubtitleSizeInscreen(modetype,mCurrentFontSize);
               // glSubtitleText.setTextSize(23);
                basePlayerPage.onScreenSizeChange(modetype);
            }

        }

        @Override
        public void onChangeDecodeType(int type) {
            if(basePlayerPage!=null) {
                basePlayerPage.onChangeDecodeType(type);
            }
        }

        @Override
        public void onAudioTrackChange(int index) {
            if(basePlayerPage!=null) {
                basePlayerPage.onAudioTrackChange(index);
            }
        }

        @Override
        public void onSelectedSubtitle(int index, String subtitleName) {
            if(basePlayerPage!=null) {
                basePlayerPage.onSelectedSubtitle(index,subtitleName);
            }
        }

        @Override
        public void onScreenLightChange(int size) {
            BrightnessUtil.setUserBrightnessValue(mActivity,(int)(255*(size/100f)));
             if(basePlayerPage!=null){
                 basePlayerPage.onScreenLightChange(size);
             }
        }

        @Override
        public void onScreenSubtitleFontSize(int size) {
            int screenType=SettingSpBusiness.getInstance(mActivity.getApplicationContext()).getPlayerScreenType();
            int showSize=getSubtitleSizeInscreen(screenType,size);
            LogHelper.d(MovieSettingSubView.settingCallback,"onScreenSubtitleFontSize size:"+showSize);
            if(glSubtitleText!=null){
                LogHelper.d(MovieSettingSubView.settingCallback,"onScreenSubtitleFontSize MovieSettingSubView size:"+showSize);
              //  glSubtitleText.setTextSize(showSize);
            }
            if(basePlayerPage!=null){
                LogHelper.d(MovieSettingSubView.settingCallback,"onScreenSubtitleFontSize onScreenSubtitleFontSize size:"+size);
                basePlayerPage.onScreenSubtitleFontSize(size);
            }
        }

        @Override
        public void onMovieStatus(int status) {
            if(basePlayerPage!=null) {
                if (status == 1) {
                    basePlayerPage.showLoading();
                } else {
                    basePlayerPage.hideLoading();
                }
            }
        }

        @Override
        public void onSetSelectSourceType(int type) {
            if(null != moviePlayerControlView) {
                moviePlayerControlView.setSelectSourceType(type);
            }
            if(basePlayerPage!=null){
                basePlayerPage.onSetSelectSourceType(type);
            }
        }
    };


    public int getSubtitleSizeInscreen(int screenSize,int fontSize){
        int specificSize=0;
        int paddingTop=80;
        if(VideoModeType.Small_Screen==screenSize){
            if(fontSize==MovieSettingSubView.subTitleSize_s){
                paddingTop=0;
            }else if(fontSize==MovieSettingSubView.subTitleSize_m){
                paddingTop=0;
            }else if(fontSize==MovieSettingSubView.subTitleSize_l){
                paddingTop=0;
            }
            specificSize=(int)(fontSize*0.8);
        }else if(VideoModeType.Large_Screen==screenSize){
            if(fontSize==MovieSettingSubView.subTitleSize_s){
                paddingTop=0;
            }else if(fontSize==MovieSettingSubView.subTitleSize_m){
                paddingTop=0;
            }else if(fontSize==MovieSettingSubView.subTitleSize_l){
                paddingTop=0;
            }
            specificSize=(int)(fontSize*1.2);
        }else if(VideoModeType.Normal_Screen==screenSize){
            if(fontSize==MovieSettingSubView.subTitleSize_s){
                paddingTop=0;
            }else if(fontSize==MovieSettingSubView.subTitleSize_m){
                paddingTop=0;
            }else if(fontSize==MovieSettingSubView.subTitleSize_l){
                paddingTop=0;
            }
            specificSize=(int)(fontSize*1);
        }else if(VideoModeType.Huge_Screen==screenSize){
            if(fontSize==MovieSettingSubView.subTitleSize_s){
                paddingTop=0;
            }else if(fontSize==MovieSettingSubView.subTitleSize_m){
                paddingTop=0;
            }else if(fontSize==MovieSettingSubView.subTitleSize_l){
                paddingTop=0;
            }
            specificSize=(int)(fontSize*1.4);
        }else if(VideoModeType.IMAX_Screen==screenSize){
            if(fontSize==MovieSettingSubView.subTitleSize_s){
                paddingTop=0;
            }else if(fontSize==MovieSettingSubView.subTitleSize_m){
                paddingTop=0;
            }else if(fontSize==MovieSettingSubView.subTitleSize_l){
                paddingTop=0;
            }
            specificSize=(int)(fontSize*1.6);
        }
        if(glSubtitleText!=null){
            LogHelper.d(MovieSettingSubView.settingCallback,"onScreenSubtitleFontSize MovieSettingSubView size:"+specificSize);
            setSubtitleViewFont(specificSize,paddingTop);
        }
        return specificSize;
    }


    /**
     * 显示Toast提示
     * @param text
     * @param name
     */
    @Override
    public void setLoadText(String text, String name) {
        super.setLoadText(text, name);
        if(glLoadToast!=null) {
            glLoadToast.setText(text, name);
        }
    }

    /**
     * 本地设置影院比例
     * @param ratioName
     */
    public void setSelectedRatio(String ratioName) {
        if(moviePlayerSettingView!=null){
            moviePlayerSettingView.setSelectedRatio(ratioName);
        }
    }
    /**
     * 本地设置影院旋转
     * @param roteName
     */
    public void setSelectedRote(String roteName) {
        if(moviePlayerSettingView!=null){
            moviePlayerSettingView.setSelectedRote(roteName);
        }
    }
    /**
     * 本地设置场景2D，3D左右，3D上下
     * @param mode
     */
    public void setSelectedLeftRightMode(int mode) {
        if(moviePlayerSettingView!=null){
            moviePlayerSettingView.setSelectedLeftRightMode(mode);
        }
    }
    /**
     * 本地设置场景平面，半球，球面
     * @param mode
     */
    public void setSelectedRoundMode(int mode) {
        if(moviePlayerSettingView!=null){
            moviePlayerSettingView.setSelectedRoundMode(mode);
        }
    }

    /**
     * 本地字幕列表（player会自动调用）
     * @param subtitleList
     */
    public void setSubtitleList(List<String> subtitleList){
        // TODO: 2017/5/22
        super.setSubtitleList(subtitleList);
        if(subtitleList==null||subtitleList.size()==0){
            if(glSubtitleText!=null){
                glSubtitleText.setVisible(false);
            }
        }else{
            if(glSubtitleText!=null){
                glSubtitleText.setVisible(true);
            }
        }
        moviePlayerSettingView.setSubtitleList(subtitleList);
    }

    /**
     * 显示字幕内容（player自动调用）
     * @param content
     */
    @Override
    public void showSubTitle(final String content) {
        super.showSubTitle(content);
        MJGLUtils.exeGLQueueEvent(mActivity, new Runnable() {
            @Override
            public void run() {
                if(glSubtitleText!=null){
                    if(TextUtils.isEmpty(content)){
                        glSubtitleText.setVisible(false);
                    }else if(!glSubtitleText.isVisible()){
                        glSubtitleText.setVisible(true);
                    }
                    glSubtitleText.setText(content.equals("NULL")?"":content);
                }
            }
        });
    }

    /**
     * 本地音轨列表（player自动调用）
     * @param audioStreamList
     */
    @Override
    public void setAudioStreamList(List<AudioSteamInfo.AudioStremItem> audioStreamList) {
        //// TODO: 2017/5/22
        super.setAudioStreamList(audioStreamList);
        moviePlayerSettingView.setAudioStreamList(audioStreamList);
    }

    /**
     * 设置当前选择的音轨
     * @param index
     */
    @Override
    public void setSelectedAudioStream(int index) {
        super.setSelectedAudioStream(index);
        moviePlayerSettingView.setSelectedAudioStream(index);
        // TODO: 2017/5/22
    }

    /**
     * 设置当前选择的解码方式
     * @param type
     */
    @Override
    public void setSelectedDecodeType(int type) {
        super.setSelectedDecodeType(type);
        moviePlayerSettingView.setSelectedDecodeType(type);

    }

    /**
     * 设置当前选择的屏幕大小
     * @param type
     */
    @Override
    public void setSelectedScreenSize(int type) {
        super.setSelectedScreenSize(type);
        // TODO: 2017/5/22
        moviePlayerSettingView.setSelectedScreenSize(type);
    }
    public void setSelectedScreenLight(int value){
        if(moviePlayerSettingView!=null) {
            moviePlayerSettingView.setSelectedScreenLight(value);
        }else{
        }
    }

    /**
     * 设置当前选择的字幕
     * @param name
     */
    @Override
    public void setSelectedSubtitle(String name) {
        super.setSelectedSubtitle(name);
        // TODO: 2017/5/22
        moviePlayerSettingView.setSelectedSubtitle(name);
    }

    /**
     * 在线选片数据
     * @param contentInfos
     */
    @Override
    public void setMovieSelectVideoDatas(List<ContentInfo> contentInfos) {
        super.setMovieSelectVideoDatas(contentInfos);
        if(moviePlayerSettingView!=null){
            moviePlayerSettingView.setMovieVideoDatas(contentInfos,0);
        }
    }

    /**
     * 本地选片数据
     * @param videoList
     * @param curIndex
     */
    @Override
    public void setLocalMovieSelectVideoDatas(final List<LocalVideoBean> videoList,final int curIndex) {
        super.setLocalMovieSelectVideoDatas(videoList,curIndex);
        if(moviePlayerSettingView!=null){
            moviePlayerSettingView.setLocalMovieVideoDatas(videoList,curIndex);
        }
    }

    private void hideAllDialogView(){
        if(glDialogView!=null&&glDialogView.isVisible()){
            glDialogView.setVisible(false);
        }
        if(mSingleBtnDialog!=null&&mSingleBtnDialog.isVisible()){
            mSingleBtnDialog.setVisible(false);
        }
        if(unLockDialog!=null&&unLockDialog.isVisible()){
            unLockDialog.setVisible(false);
        }
        moviePlayerControlView.hideAllView();
        moviePlayerSettingView.hideAllView();
        ((GLBaseActivity)mActivity).showClose(false);
        ((GLBaseActivity)mActivity).hideResetLayerView();
    }
    @Override
    public void showReplayDialog(List<ContentInfo> contentInfos,int index) {
        super.showReplayDialog(contentInfos,index);
        hideAllDialogView();
        if(contentInfos==null||contentInfos.size()<=0){
            if(rePlayView!=null&&rePlayView.isVisible()){
                rePlayView.setVisible(false);
            }
            if(rePlayDialogButtonView!=null) {
                rePlayDialogButtonView.setVisible(true);
            }
        }else {
            if(rePlayDialogButtonView!=null&&rePlayDialogButtonView.isVisible()){
                rePlayDialogButtonView.setVisible(false);
            }
            if(rePlayView!=null){
                rePlayView.setData(contentInfos);
                //防止显示偏移
                if (mType == PANO || mType == LOCAL_PANO) {
                    rePlayView.setLookAngle(rePlayView.getParent().getLookAngle());
                }
                rePlayView.setVisible(true);
                rePlayView.setCurrentNum(index);
            }
        }
    }

    private long pre_time = -1;

    @Override
    public boolean onZKeyDown(int keyCode) {
        switch (keyCode){
            case MojingKeyCode.KEYCODE_DPAD_UP://向上翻页,普通手柄
                if(null != moviePlayerSettingView) {
                    moviePlayerSettingView.prePage();
                }
                return false;
            case MojingKeyCode.KEYCODE_DPAD_DOWN://向下翻页,普通手柄
                if(null != moviePlayerSettingView) {
                    moviePlayerSettingView.nextPage();
                }
                return false;
            case MojingKeyCode.KEYCODE_MENU://普通手柄
                if(((MjVrPlayerActivity)mActivity).mCurPlayMode != VideoModeType.PLAY_MODE_VR) {
                    return false;
                }
                if(null != moviePlayerControlView && !moviePlayerControlView.isShow()) {
                    ((GLBaseActivity)mActivity).showOpen(false);
                    if(null != moviePlayerControlView) {
                        moviePlayerControlView.showAllView();
                        setLookAngle();
                    }
                    if(!resetLayerfocused) {//底部已经获取焦点，就不需要自动隐藏
                        hideViewTimer();
                    }
                }
                return false;
            case MojingKeyCode.KEYCODE_BACK://普通手柄,体感手柄
                if(null != moviePlayerControlView && moviePlayerControlView.isShow()) {
                    if(null != moviePlayerSettingView) {
                        //全部高级菜单子view隐藏
                        moviePlayerSettingView.hideAllView();
                    }
                    //隐藏控制菜单
                    moviePlayerControlView.hideAllView();
                    //隐藏底部控制
                    ((GLBaseActivity)mActivity).showClose(false);
                    //隐藏头控焦点
                    ((GLBaseActivity)mActivity).hideCursorView();
                    return true;
                } else {
                    return false;
                }
            case MojingKeyCode.KEYCODE_ENTER://体感手柄
                if(((MjVrPlayerActivity)mActivity).mCurPlayMode != VideoModeType.PLAY_MODE_VR) {
                    return false;
                }
                if(pre_time>0&&System.currentTimeMillis()-pre_time<400){ //连击
                    if(basePlayerPage!=null) {
                        if( basePlayerPage.isPlaying()){
                            setPlayOrPauseBtn(true);
                            basePlayerPage.pausePlay();
                        }else {
                            setPlayOrPauseBtn(false);
                            basePlayerPage.startPlay();
                        }
                    }
                    pre_time = -1;
                }else{//单击
                    if(resetLayerfocused) return false;
                    if(null != moviePlayerControlView) {
                        if(!moviePlayerControlView.isShow()) {
                            //必选弹框出现时，再弹出基础菜单
                           if(lockView!=null&&lockView.isViewVisable()){
                               return false;
                           }
                            if(glDialogView!=null&&glDialogView.isVisible()){
                                return false;
                            }
                            if(mSingleBtnDialog!=null&&mSingleBtnDialog.isVisible()){
                                return false;
                            }
                            if(unLockDialog!=null&&unLockDialog.isVisible()){
                                return false;
                            }
                            if(rePlayView!=null&&rePlayView.isVisible()){
                                return false;
                            }
                            if(rePlayDialogButtonView!=null&&rePlayDialogButtonView.isVisible()){
                                return false;
                            }

                            moviePlayerControlView.showAllView();
                            setLookAngle();

                            if(StickUtil.getConnectStatus() == 2) {
//                                if(!resetLayerfocused) {//底部已经获取焦点，就不需要自动隐藏
                                    ((GLBaseActivity)mActivity).showOpen(true);
                                    hideViewTimer();
//                                }
                            } else if(StickUtil.getConnectStatus() == 1) {
                                hideViewTimer();
                            }
                        } else {
                            //基础菜单获取焦点后，单击不能消失，否则菜单上的按钮无法点击就已经消失了
                            if(null!=moviePlayerSettingView&&moviePlayerSettingView.isFocused() || moviePlayerControlView.isFocused()) return false;
                            if(null != moviePlayerSettingView) {
                                //全部高级菜单子view隐藏
                                moviePlayerSettingView.hideAllView();
                            }
                            //隐藏控制菜单
                            moviePlayerControlView.hideAllView();
    //                        if(!resetLayerfocused) {
                                if(StickUtil.getConnectStatus() == 2) {
                                    //隐藏底部控制
                                        ((GLBaseActivity)mActivity).showClose(false);
                                }
                                //隐藏头控焦点
                                ((GLBaseActivity)mActivity).hideCursorView();
    //                        }
                        }
                    }

                }
                pre_time = System.currentTimeMillis();

                return false;
            default:
                return false;
        }
    }


    @Override
    public void onZKeyLongPress(int keyCode) {
        switch (keyCode) {
            case MojingKeyCode.KEYCODE_HOME:
                ((GLBaseActivity)mActivity).initHeadView();
                break;
        }
    }

    /**
     * 隐藏续播对话框
     */
    @Override
    public void hideReplayDialog() {
        super.hideReplayDialog();
        if(rePlayView!=null){
            rePlayView.setVisible(false);
        }
        if(rePlayDialogButtonView!=null){
            rePlayDialogButtonView.setVisible(false);
        }
        ((GLBaseActivity)mActivity).showResetLayerView();
    }

    @Override
    public void updateSeekingProcess(final int curpos, final int duration,final boolean rewind) {
        super.updateSeekingProcess(curpos, duration,rewind);
        MJGLUtils.exeGLQueueEvent(mActivity, new Runnable() {
            @Override
            public void run() {
              if(glSeekToast!=null){
                  String cur = TimeFormat.format(curpos/1000);
                  String dur = TimeFormat.format(duration/1000);
                  glSeekToast.showToast(cur,dur,rewind?"play_toast_icon_rewind":"play_toast_icon_fastforward");
              }
            }
        });
    }

    @Override
    public void setSeekingProcessVisable(final boolean visable) {
        super.setSeekingProcessVisable(visable);
        MJGLUtils.exeGLQueueEvent(mActivity, new Runnable() {
            @Override
            public void run() {
                if(glSeekToast!=null){
                    glSeekToast.setVisible(visable);
                }
            }
        });
    }

    @Override
    public void prePage() {
        super.prePage();
        if(null != moviePlayerSettingView) {
            moviePlayerSettingView.prePage();
        }
        if(null != rePlayView) {
            rePlayView.prePage();
        }
    }

    @Override
    public void nextPage() {
        super.nextPage();
        if(null != moviePlayerSettingView) {
            moviePlayerSettingView.nextPage();
        }
        if(null != rePlayView) {
            rePlayView.nextPage();
        }
    }

    @Override
    public void setHDable(boolean isable) {
        if(null != moviePlayerControlView) {
            moviePlayerControlView.setHDable(isable);
        }
    }

    public void setSubtitleFontSize(int size){
        LogHelper.d(MovieSettingSubView.settingCallback,"lay setSubtitleFontSize: "+size);
        if(glSubtitleText!=null){
            LogHelper.d(MovieSettingSubView.settingCallback,"lay glSubtitleText !=null : "+size);
            int screenType=SettingSpBusiness.getInstance(mActivity.getApplicationContext()).getPlayerScreenType();
            getSubtitleSizeInscreen(screenType,size);
           // glSubtitleText.setTextSize(size);
            setSubtitleParams(size);
        }
        if(moviePlayerSettingView!=null){
            LogHelper.d(MovieSettingSubView.settingCallback,"lay moviePlayerSettingView !=null : "+size);
            moviePlayerSettingView.setSelectedSubtitleFondSize(size);
        }
    }

    private int mCurrentFontSize=0;

    public void setSubtitleViewFont(int size,int top){
        if(glSubtitleText!=null){
            mCurrentFontSize=size;
            glSubtitleText.setTextSize(size);
         //   glSubtitleText.setPadding(0,top,0,0);
        }
    }


    private void setSubtitleParams(int size){
        //TODO 根据size计算 height
        
//        if(glSubtitleText!=null) {
//            subtitleLayoutView.setLayoutParams(1200,subtitleHeight);
//            glSubtitleText.setLayoutParams(ViewUtil.getDip(1200,GLConst.Subtitle_Scale),ViewUtil.getDip(subtitleHeight,GLConst.Subtitle_Scale));
//        }
//        if(subtitleLayoutView!=null) {
//            glSubtitleText.setLayoutParams(ViewUtil.getDip(1200,GLConst.Subtitle_Scale),ViewUtil.getDip(subtitleHeight,GLConst.Subtitle_Scale));
//        }
    }


    @Override
    public void setSelectSourceType(int type) {
        super.setSelectSourceType(type);
        if(moviePlayerControlView!=null){
            moviePlayerControlView.setSelectSourceType(type);
        }
    }

    private void setLookAngle(){
        if(mControlLayer==null)
            return;
        if (mType == PANO || mType == LOCAL_PANO){
            float[] out = new float[3];
            GLFocusUtils.getEulerAngles(mControlLayer.getMatrixState().getVMatrix(), out, 0);
            float angle = (float) Math.toDegrees(out[0]) + mControlLayer.getLookAngle();

            if(mControlLayer!=null) {
                mControlLayer.setLookAngle(angle);
            }
            if(mSettingLayer!=null) {
                mSettingLayer.setLookAngle(angle);
            }
            if(mDialogLayer!=null) {
                mDialogLayer.setLookAngle(angle);
            }
            if(mSubtitleLayer!=null) {
                mSubtitleLayer.setLookAngle(angle);
            }
            if(mLockLayer!=null) {
                mLockLayer.setLookAngle(angle);
            }
        }
    }

    @Override
    public void setResID(String resID) {
        super.setResID(resID);
        if(moviePlayerSettingView!=null){
            moviePlayerSettingView.setResId(resID);
        }
    }

    @Override
    public void hideAllView() {
        super.hideAllView();
        if(moviePlayerControlView!=null){
            moviePlayerControlView.hideAllView();
        }
        if(moviePlayerSettingView!=null){
            moviePlayerSettingView.hideAllView();
        }
        if(lockView!=null){
            lockView.setViewVisable(false);
        }
        mActivity.showClose(false);
    }
    public void setLockLayerVisiable(boolean visiable){
        if(lockView!=null){
            lockView.setVisible(visiable);
        }
    }
    /**
     * 屏幕大小变化时动态调整字幕位置
     */
    public void setSubtitleTextPosition(float topmargin){
        if(subtitleLayoutView!=null){
            subtitleLayoutView.setMargin(subtitleLayoutView.getMarginLeft(), ViewUtil.getDip(topmargin-subtitleHeight,GLConst.Subtitle_Scale), subtitleLayoutView.getMarginRight(),0);
        }
    }

    public void reSetReplayView(){
        if(mActivity!=null&&!mActivity.isFinishing()) {
            mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (rePlayView != null) {
                        rePlayView.setCurrentNum(-1);
                    }
                }
            });
        }
    }

    @Override
    public boolean isSeekingProcessVisable() {
        if(glSeekToast!=null){
            return glSeekToast.isVisible();
        }
        return super.isSeekingProcessVisable();
    }
}
