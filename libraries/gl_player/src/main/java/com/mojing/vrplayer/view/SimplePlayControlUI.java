package com.mojing.vrplayer.view;

import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mojing.vrplayer.R;
import com.mojing.vrplayer.activity.GLBaseActivity;
import com.mojing.vrplayer.activity.MjVrPlayerActivity;
import com.mojing.vrplayer.bean.AudioSteamInfo;
import com.mojing.vrplayer.bean.VideoModeType;
import com.mojing.vrplayer.interfaces.IPlayerControlCallBack;
import com.mojing.vrplayer.interfaces.IPlayerSettingCallBack;
import com.mojing.vrplayer.page.BaseLocalPlayPage;
import com.mojing.vrplayer.page.BasePlayerPage;
import com.mojing.vrplayer.publicc.BrightnessUtil;
import com.mojing.vrplayer.publicc.MojingAppCallbackUtil;
import com.mojing.vrplayer.publicc.NetworkUtil;
import com.mojing.vrplayer.publicc.SettingSpBusiness;
import com.mojing.vrplayer.publicc.bean.ContentInfo;
import com.mojing.vrplayer.publicc.bean.LocalVideoBean;
import com.mojing.vrplayer.publicc.bean.VideoDetailBean;
import com.mojing.vrplayer.simpleview.PlayControlView;
import com.mojing.vrplayer.simpleview.PlayHDSelectView;
import com.mojing.vrplayer.simpleview.PlayModeView;
import com.mojing.vrplayer.simpleview.PlaySelectSourceView;
import com.mojing.vrplayer.simpleview.PlaySettingsView;
import com.mojing.vrplayer.simpleview.PlayToastView;
import com.mojing.vrplayer.simpleview.PlayerDialogView;
import com.mojing.vrplayer.simpleview.PlayerSeekToastView;
import com.mojing.vrplayer.simpleview.ScreenControlView;
import com.mojing.vrplayer.simpleview.SingleLoadingView;
import com.mojing.vrplayer.utils.MJGLUtils;
import com.mojing.vrplayer.utils.SoundUtils;
import com.storm.smart.common.utils.LogHelper;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by wanghongfang on 2017/5/9.
 * 触屏模式下播放控制UI 逻辑调用管理类
 */
public class SimplePlayControlUI extends BasePlayControlUI {
    public final static String TAG="SimplePlayControlUI";
    public final static int TIME_TOUCH_ONCLICK=25;
    private SingleLoadingView loadingView;
    private PlayToastView playToastView;
    private PlayerSeekToastView playSeekToastView;
    private RelativeLayout tip_dialog_layout;
    private TextView subtitleText;
    PlayerDialogView exceptionDialog;// 异常提示框
    PlayerDialogView rePlayDialog;//重播提示
    private PlaySelectSourceView mPlaySelectSourceView; //选集view
    private PlayHDSelectView playHDSelectView;//清晰度选择
    private PlayModeView playModeView;//模式选择
    PlayControlView mPlayControlView;
    PlaySettingsView mPlaySettingsView;
    ImageView mTopBlackImage;
    private RelativeLayout view;
    private int leftRightType = 0;
    private int roundModeType = 0;

    public SimplePlayControlUI(GLBaseActivity activity, BasePlayerPage basePlayerPage, int type) {
        super(activity,basePlayerPage);
        this.mType = type;
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                initView();
            }
        });
    }

    private void initView(){
        view = (RelativeLayout) LayoutInflater.from(mActivity).inflate(R.layout.activity_simple_player_ui, null);
        mActivity.getRootLayout().addView(view);

        mActivity.getRootView().setOnTouchListener(touchListener);
//        view.findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //basePlayerPage.setCurPlayMode(VideoModeType.PLAY_MODE_SIMPLE_FULL);
//                if(mPlayControlView.getVisibility()!=View.VISIBLE){
//                    mPlaySettingsView.hideView();
//                    mPlayControlView.showView();
//                }else{
//                    mPlaySettingsView.showView();
//                    mPlayControlView.hideView();
//                }
//
//            }
//        });

        mPlaySelectSourceView = (PlaySelectSourceView)view.findViewById(R.id.playseletview);
        mPlaySelectSourceView.setIPlayerSettingCallBack(settingCallBack);

        VideoDetailBean mVieoDetailBean = new VideoDetailBean();
        mVieoDetailBean.setCategory_type(2);
        mVieoDetailBean.setTotal("99");
        mPlaySelectSourceView.setMovieVideoDatas(mVieoDetailBean,0);
      //  mPlaySelectSourceView.setVisibility(View.VISIBLE);


//        List<VideoDetailBean.AlbumsBean.VideosBean>  list = new ArrayList<>();
//        for(int x = 0; x < 10; x++) {
//            VideoDetailBean.AlbumsBean.VideosBean bean = new VideoDetailBean.AlbumsBean.VideosBean();
//            bean.setTitle("测试测试测试测试测试测试"+x);
//            bean.setSeq(x+1);
//            list.add(bean);
//        }
//        VideoDetailBean.AlbumsBean albumsBean = new VideoDetailBean.AlbumsBean();
//        albumsBean.setVideos(list);
//        ArrayList<VideoDetailBean.AlbumsBean> al = new ArrayList<>();
//        al.add(albumsBean);
//        mVieoDetailBean.setAlbums(al);
//        mVieoDetailBean.setCategory_type(1);

//        mPlaySelectSourceView.setMovieVideoDatas(mVieoDetailBean,0);
//        mPlaySelectSourceView.setVisibility(View.VISIBLE);

        playHDSelectView = (PlayHDSelectView) view.findViewById(R.id.playhdseletdview);
        playHDSelectView.setIPlayerSettingCallBack(settingCallBack);

//        String[] strs = new String[]{"自动", "蓝光", "1080", "720", "480"};
//        setHDdata(strs,"自动");
//        playHDSelectView.setVisibility(View.VISIBLE);

        playModeView = (PlayModeView) view.findViewById(R.id.playmodeview);
        playModeView.setIPlayerSettingCallBack(settingCallBack);
//        playModeView.setVisibility(View.VISIBLE);

        mPlayControlView = (PlayControlView)view.findViewById(R.id.playcontrolview);
        mPlayControlView.setIPlayerControlCallBack(controlCallBack);
        mPlayControlView.setIPlayerSettingCallBack(settingCallBack);
        mPlayControlView.setIPlayerRateListener(mPlayerRateListener);
        mPlayControlView.setType(mType);
        tip_dialog_layout = (RelativeLayout)view.findViewById(R.id.tip_dialog_layout);
        exceptionDialog = (PlayerDialogView)view.findViewById(R.id.player_exception_dialog);
        rePlayDialog = (PlayerDialogView)view.findViewById(R.id.player_replay_dialog);
        rePlayDialog.setTipText("播放结束，点击重新播放");
        rePlayDialog.setTipBtnText("重播");
        loadingView = (SingleLoadingView)view.findViewById(R.id.loadingview);
        playToastView = (PlayToastView)view.findViewById(R.id.play_toast_view);
        mPlaySettingsView=(PlaySettingsView)view.findViewById(R.id.playsettingsview);
        mPlaySettingsView.setIPlayerSettingCallBack(settingCallBack);

        playSeekToastView = (PlayerSeekToastView)view.findViewById(R.id.player_seek_toast);
        subtitleText = (TextView)view.findViewById(R.id.subtitlefontsize_text);

        mTopBlackImage=(ImageView)view.findViewById(R.id.topBlackImage);
        setListener();
        setDelayVisiable();
        LogHelper.d(PlayControlView.TAG,"contrl ui onCreat changeUIByPlayMode");
        changeUIByPlayMode(mMode);
    }
    int mMode=0;
    public void setMode(int mode){
        LogHelper.d(PlayControlView.TAG,"contrl ui mode:tag:"+mode);
        mMode=mode;
    }

    public void changeUIByPlayMode(int mode){
        LogHelper.d(TAG,"changeUIByPlayMode mode:"+mode);
        if(mode==VideoModeType.PLAY_MODE_SIMPLE){
            showPlayControlView();
        }else if(mode==VideoModeType.PLAY_MODE_SIMPLE_FULL){
            showPlayControlView();
            tip_dialog_layout.setVisibility(View.VISIBLE);
        }else if(mode==VideoModeType.PLAY_MODE_VR){
            hidePlayControlView();
            tip_dialog_layout.setVisibility(View.GONE);
        }
        mPlayControlView.changeUIByPlayMode(mode);
    }

    public void showBlackImge(){
        if(basePlayerPage!=null&&basePlayerPage.mCurPlayMode==VideoModeType.PLAY_MODE_VR){
            mTopBlackImage.setVisibility(View.VISIBLE);
            tip_dialog_layout.setVisibility(View.GONE);
        }else if(basePlayerPage!=null&&basePlayerPage.mCurPlayMode==VideoModeType.PLAY_MODE_SIMPLE_FULL){
            mTopBlackImage.setVisibility(View.GONE);
            tip_dialog_layout.setVisibility(View.VISIBLE);
        }else if(basePlayerPage!=null&&basePlayerPage.mCurPlayMode==VideoModeType.PLAY_MODE_SIMPLE){
            mTopBlackImage.setVisibility(View.VISIBLE);
            tip_dialog_layout.setVisibility(View.GONE);
        }

    }

    public void showPlayControlView(){
        setDelayVisiable();
        mPlayControlView.showView();
        showBlackImge();
    }
    public void hideHDAndSourceView(boolean fullTOGone){
        LogHelper.d(TAG,"hideHDAndSourceView");
        if(basePlayerPage!=null&&basePlayerPage.mCurPlayMode==VideoModeType.PLAY_MODE_VR){
            basePlayerPage.setCurPlayMode(VideoModeType.PLAY_MODE_SIMPLE);
            showPlayControlView();
            tip_dialog_layout.setVisibility(View.GONE);
            //view.setVisibility(View.VISIBLE);
        }else if(basePlayerPage!=null&&basePlayerPage.mCurPlayMode==VideoModeType.PLAY_MODE_SIMPLE_FULL){
            if(fullTOGone){
                hidePlayControlView();
            }else{
                if(mPlayControlView.getVisibility()!=View.VISIBLE){
                    showPlayControlView();
                }else{
                    hidePlayControlView();
                }
            }
            tip_dialog_layout.setVisibility(View.VISIBLE);
        }else if(basePlayerPage!=null&&basePlayerPage.mCurPlayMode==VideoModeType.PLAY_MODE_SIMPLE){
            basePlayerPage.setCurPlayMode(VideoModeType.PLAY_MODE_VR);
            hidePlayControlView();
            tip_dialog_layout.setVisibility(View.GONE);
        }
    }


    public void hidePlayControlView(){
        mPlayControlView.hideView();
        mTopBlackImage.setVisibility(View.GONE);
        cancelDelay();
    }

    public void cancelDelay(){
        if(timer!=null){
            timer.cancel();
            timer = null;
        }
    }

    Timer timer ;
    public void setDelayVisiable(){
        if(timer!=null){
            timer.cancel();
            timer = null;
        }
        timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(basePlayerPage!=null&&basePlayerPage.mCurPlayMode==VideoModeType.PLAY_MODE_SIMPLE){
                            basePlayerPage.setCurPlayMode(VideoModeType.PLAY_MODE_VR);
                        }
                        playModeView.hideView();
                        mPlayControlView.refreshPlayModeImage(false);
                        hidePlayControlView();
                    }
                });
            }
        };
        timer.schedule(task,5*1000);
    }

    public void hideAllVisible(){
        mPlaySettingsView.hideView();
        playHDSelectView.hideView();
        playModeView.hideView();
        mPlaySelectSourceView.hideView();
        mPlayControlView.refreshPlayModeImage(false);
    }

    boolean isMove=false;
    float FirxtX=0;
    float FirxtY=0;
    private View.OnTouchListener touchListener = new View.OnTouchListener(){

        @Override
        public boolean onTouch(View v, MotionEvent event) {
//            if(basePlayerPage!=null&&basePlayerPage.mCurPlayMode==VideoModeType.PLAY_MODE_SIMPLE) {
//                if (event.getAction() == MotionEvent.ACTION_UP) {
//                    se1tDelayVisiable();
//                } else {
//                    cancelDelay();
//                }
//            }
            LogHelper.d(TAG,"onTouch ");
            if(event.getAction()==MotionEvent.ACTION_DOWN){
                isMove=false;
                FirxtX=event.getX();
                FirxtY=event.getY();
                LogHelper.d(TAG,"onTouch ACTION_DOWN");
            }else if(event.getAction()==MotionEvent.ACTION_MOVE){
                LogHelper.d(TAG,"onTouch ACTION_MOVE");
                if(Math.abs(FirxtX-event.getX())>TIME_TOUCH_ONCLICK ||Math.abs(FirxtY-event.getY())>TIME_TOUCH_ONCLICK ){
                    isMove=true;
                }

            }else if(event.getAction()==MotionEvent.ACTION_UP&&!isMove){
                if(playModeView.getVisibility()==View.VISIBLE){
                    playModeView.hideView();
                    mPlayControlView.refreshPlayModeImage(false);
                    return false;
                }
                if(mPlaySettingsView.getVisibility()==View.VISIBLE||
                    playHDSelectView.getVisibility()==View.VISIBLE||
                    mPlaySelectSourceView.getVisibility()==View.VISIBLE) {
                    hideAllVisible();
                    showPlayControlView();
                    return false;
                }
                hideHDAndSourceView(false);

            }
            return false;
        }
    };

    private boolean isSettingVisiable(){
        if(playModeView!=null&&playModeView.getVisibility()==View.VISIBLE)
            return true;
        if(mPlaySettingsView!=null&&mPlaySettingsView.getVisibility()==View.VISIBLE)
            return true;
        if(mPlaySelectSourceView!=null&&mPlaySelectSourceView.getVisibility()==View.VISIBLE)
            return true;
        if(playHDSelectView!=null&&playHDSelectView.getVisibility()==View.VISIBLE)
            return true;
        return false;
    }


    private ScreenControlView.IPlayerRateListener mPlayerRateListener = new ScreenControlView.IPlayerRateListener(){

        @Override
        public void onViewTouchEvent(int type) {
            if(basePlayerPage!=null){
                basePlayerPage.onViewTouchEvent(type);
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
            LogHelper.d("onControlChanged",id);
            if(PlayControlView.VIEW_ID_CLOSE.equals(id)) {
                mActivity.finish();
            }else if(PlayControlView.VIEW_ID_SETTINGS.equals(id)) {
                if(playModeView!=null){
                    playModeView.hideView();
                }
                hidePlayControlView();
                mPlaySettingsView.showView();
                showBlackImge();
            }else if(PlayControlView.VIEW_ID_HDSelect.equals(id)){
                hidePlayControlView();
                playHDSelectView.showView();
                showBlackImge();
            }else if(PlayControlView.VIEW_ID_playMode.equals(id)){
//                hidePlayControlView();
                if(timer == null){
                    return;
                }
                if(selectedStatus){
                    playModeView.showView();
                }else{
                    playModeView.hideView();
                }
                mPlayControlView.refreshPlayModeImage(selectedStatus);
                setDelayVisiable();
                showBlackImge();
            }else if(PlayControlView.VIEW_ID_SelectSource.equals(id)) {
                hidePlayControlView();
                mPlaySelectSourceView.showView();
                showBlackImge();
            }else if(PlayControlView.VIEW_ID_VIEW_TOUCH.equals(id)){
                if(playModeView.getVisibility()==View.VISIBLE){
                    playModeView.hideView();
                    mPlayControlView.refreshPlayModeImage(false);
                    return;
                }
                if(mPlaySettingsView.getVisibility()==View.VISIBLE||
                        playHDSelectView.getVisibility()==View.VISIBLE||
                        mPlaySelectSourceView.getVisibility()==View.VISIBLE) {

                    hideAllVisible();
                    showPlayControlView();
                }else{
                    hideHDAndSourceView(false);
                }
//                if(mPlayControlView.getVisibility()!=View.VISIBLE){
//                    showPlayControlView();
//                }else{
//                    hidePlayControlView();
//                }
            }else if(PlayControlView.VIEW_ID_VIEW_PREVIOUS.equals(id)){
                if(MjVrPlayerActivity.videoList == null||MjVrPlayerActivity.videoList.size() == 0){
                    return;
                }
                BaseLocalPlayPage page = ((BaseLocalPlayPage)(mActivity.getPageManager().getIndexView()));
                int index = page.index - 1;
                if(index<0||index>=MjVrPlayerActivity.videoList.size()){
                    return;
                }
                page.onSelectedLocalMovie(MjVrPlayerActivity.videoList.get(index),index);
            }else if(PlayControlView.VIEW_ID_VIEW_NEXT.equals(id)){
                if(MjVrPlayerActivity.videoList == null||MjVrPlayerActivity.videoList.size() == 0){
                    return;
                }
                BaseLocalPlayPage page = ((BaseLocalPlayPage)(mActivity.getPageManager().getIndexView()));
                int index = page.index + 1;
                if(index<0||index>=MjVrPlayerActivity.videoList.size()){
                    return;
                }
                page.onSelectedLocalMovie(MjVrPlayerActivity.videoList.get(index),index);
            }else if(PlayControlView.VIEW_ID_VIEW_BUY.equals(id)){
                BaseLocalPlayPage page = ((BaseLocalPlayPage)(mActivity.getPageManager().getIndexView()));
                page.reportClickBuy();

                Intent intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                Uri content_url = Uri.parse("http://m.mojing.cn/");
                intent.setData(content_url);
                mActivity.startActivity(intent);
            }

        }

        @Override
        public void onHideControlAndSettingView(boolean isHide) {
//            if(isHide) {
//                hideViewTimer();
//            } else {
//                cancelHideViewTimer();
//            }
        }

        @Override
        public void onChangFullScreen(final boolean fullScreen) {
//            MJGLUtils.exeGLQueueEvent(mActivity, new Runnable() {
//                @Override
//                public void run() {
            if(((MjVrPlayerActivity)mActivity).isComeFromUnity){
                SettingSpBusiness.getInstance(mActivity).setUnityPlaySingleScreen(fullScreen);
            }else{
                SettingSpBusiness.getInstance(mActivity).setPlaySingleScreen(fullScreen);
            }
            mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    basePlayerPage.setCurPlayMode(fullScreen? VideoModeType.PLAY_MODE_SIMPLE_FULL:VideoModeType.PLAY_MODE_SIMPLE);
//                    if(basePlayerPage!=null&&basePlayerPage.mCurPlayMode==VideoModeType.PLAY_MODE_SIMPLE) {
//                        mTopBlackImage.setVisibility(View.VISIBLE);
//                    }else {
//                        mTopBlackImage.setVisibility(View.GONE);
//                    }
                    showBlackImge();
                }
            });

//                }
//            });

        }

    };

    public void hideAllView(){
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(mPlaySettingsView!=null&&mPlaySettingsView.getVisibility()==View.VISIBLE){
                    mPlaySettingsView.hideView();
                }
                if(mPlayControlView!=null&&mPlayControlView.getVisibility()==View.VISIBLE){
                    mPlayControlView.hideView();
                }
                if(mPlaySelectSourceView!=null&&mPlaySelectSourceView.getVisibility()==View.VISIBLE){
                    mPlaySelectSourceView.hideView();
                }
                if(playHDSelectView!=null&&playHDSelectView.getVisibility()==View.VISIBLE){
                    playHDSelectView.hideView();
                }
                if(playModeView!=null&&playModeView.getVisibility()==View.VISIBLE){
                    playModeView.hideView();
                    mPlayControlView.refreshPlayModeImage(false);
                }
            }
        });

    }


    @Override
    public  void showLoading(){
        super.showLoading();
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
        hideLoadToast();
        mLoadingTime = -1;
        if ( basePlayerPage!=null&&basePlayerPage.getPlayerCurrentState()== BasePlayerView.PlayerState.PREPARED) {
            mStartTipsTime = -1;
        }
    }

    @Override
    public void showToast(final String text, int type) {
        super.showToast(text, type);
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(playToastView!=null){
                    playToastView.showToast(text);
                }
            }
        });

    }

    @Override
    public void showImgToast(final String text, int imgRes) {
        super.showImgToast(text, imgRes);
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(playToastView!=null){
                  //  playToastView.showToast("已为您识别到 "+text+" 模式，如有错误请到 模式 中手动切换");
                }
            }
        });

    }

    /**
     * 网络状态改变后的处理
     * @param isPlayError
     */
    @Override
    public void doNetChanged(final boolean isPlayError){
        super.doNetChanged(isPlayError);

        if(!NetworkUtil.isNetworkConnected(mActivity)){//无网络
            if(exceptionDialog!=null){
                exceptionDialog.showExceptionDialog(mActivity.getResources().getString(R.string.player_network_exception),mActivity.getString(R.string.player_reload),false);
                showDialogView(1);
            }
            hideLoading();
            if(basePlayerPage!=null) {
                basePlayerPage.pausePlay();
            }

            return;
        }
        if(isPlayError){
            if(exceptionDialog!=null){
                exceptionDialog.showExceptionDialog(mActivity.getResources().getString(R.string.player_load_failed),mActivity.getString(R.string.player_reload),false);
                showDialogView(1);
            }
            hideLoading();
            if(basePlayerPage!=null) {
                basePlayerPage.pausePlay();
            }

            return;
        }

        if(!NetworkUtil.canPlayAndDownload(mActivity) ){ //有网络 但为非WIFI网络
            if(exceptionDialog!=null){
                exceptionDialog.showExceptionDialog(mActivity.getResources().getString(R.string.player_no_wifi),mActivity.getString(R.string.player_play_continue),true);
                showDialogView(1);
            }
            hideLoading();
            if(basePlayerPage!=null){
                basePlayerPage.pausePlay();
                basePlayerPage.onMobileNet();
            }

            return;
        }else { //连上网络
            if(exceptionDialog!=null&&basePlayerPage!=null&&basePlayerPage.getPlayerCurrentState()== BasePlayerView.PlayerState.PREPARED) {
                if(!basePlayerPage.isPauseView) {
                    basePlayerPage.startPlay();
                }
            }else {
                if(!basePlayerPage.isPauseView) {
                    basePlayerPage.rePlay();
                }
            }
            if(basePlayerPage.isPauseView){
                mPlayControlView.setPlayOrPauseBtn(false);
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
            mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    doNetChanged(true);
                    hideLoading();
                }
            });

        }
    }

    @Override
    public void setHDdata(final String[] strs, final String defaultHD) {
        super.setHDdata(strs, defaultHD);
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(playHDSelectView!=null) {
                    playHDSelectView.setHDdata(strs, defaultHD);
                }
                if(mPlayControlView!=null) {
                    mPlayControlView.setDefinitionSelectShow();
                }
                LogHelper.d(TAG,"setHDdata:"+defaultHD);
                if(mPlayControlView!=null){
                    mPlayControlView.setHDname(defaultHD);
                }
            }
        });

    }

    @Override
    public void setSelectedHD(final String hdtype) {
        super.setSelectedHD(hdtype);
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(playHDSelectView!=null) {
                    playHDSelectView.setSelectedHD(hdtype);
                }
                LogHelper.d(TAG,"setSelectedHD:"+hdtype);
                if(mPlayControlView!=null){
                    mPlayControlView.setHDname(hdtype);
                }
            }
        });

    }

    @Override
    public void setCurrentNum(final int index) {
        super.setCurrentNum(index);
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(mPlaySelectSourceView!=null){
                    mPlaySelectSourceView.setCurrentNum(index);
                }
            }
        });

    }

    @Override
    public void setName(final String name) {
        super.setName(name);
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(mPlayControlView!=null){
                    mPlayControlView.setVideoTitle(name);
                }
            }
        });

    }

    @Override
    public void setDisplayDuration(final long duration) {
        super.setDisplayDuration(duration);
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(mPlayControlView!=null){
                    mPlayControlView.setDisplayDuration(duration);
                }
            }
        });

    }

    @Override
    public void setMovieVideoDatas(final VideoDetailBean videosBean, final int index) {
        super.setMovieVideoDatas(videosBean, index);
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(mPlaySelectSourceView!=null){
                    mPlaySelectSourceView.setMovieVideoDatas(videosBean,index);
                }
                if(mPlayControlView!=null){
                    mPlayControlView.setPlaysIndexSelectShow();
                }
            }
        });

    }

    @Override
    public void setPlayOrPauseBtn(final boolean playOrPause) {
        super.setPlayOrPauseBtn(playOrPause);
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(mPlayControlView!=null){
                    mPlayControlView.setPlayOrPauseBtn(playOrPause);
                }
            }
        });

    }

    @Override
    public boolean isPlayFlag() {
        if(mPlayControlView!=null) {
            return mPlayControlView.isPlayFlag();
        }
        return super.isPlayFlag();
    }


    @Override
    public void setVolume(int vm) {
        super.setVolume(vm);
//        if(moviePlayerSettingView!=null) {
//            moviePlayerSettingView.setVolume(vm);
//        }
    }

    @Override
    public void setSoundMute(boolean flag) {
        super.setSoundMute(flag);
//        if(moviePlayerSettingView!=null) {
//            moviePlayerSettingView.setSoundIcon(!flag);
//        }
    }

    @Override
    public void setType(int type) {
        super.setType(type);
    }

    @Override
    public void updateProgress(int current, int duration) {
        super.updateProgress(current, duration);
        if(mPlayControlView!=null){
            mPlayControlView.updateProgress(current,duration);
        }
    }

    @Override
    public void setProcess(final int process) {
        super.setProcess(process);
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(mPlayControlView!=null){
                    mPlayControlView.setSeekBarProcess(process);
                }
            }
        });

    }

    @Override
    public boolean getLoadToastVisiable(){
        return loadingView!=null&&loadingView.getVisibility()==View.VISIBLE;
    }

    public void showLoadToast(final boolean loading){
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(loadingView!=null) {
                    loadingView.setVisibility(View.VISIBLE);
                    loadingView.setLoadingTvVisable(!loading);
                }
            }
        });

    }

    public void hideLoadToast(){
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(loadingView!=null){
                    loadingView.setVisibility(View.GONE);
                }
            }
        });

    }

    @Override
    public void hideDialogView(){
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(exceptionDialog!=null){
                    exceptionDialog.setVisibility(View.GONE);
                }
            }
        });

    }
    public void showDialogView(int type){
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(exceptionDialog!=null){
                    exceptionDialog.setVisibility(View.VISIBLE);
                }
            }
        });

    }

    @Override
    public boolean isDialogShowing(){
        return (exceptionDialog!=null&&exceptionDialog.getVisibility()==View.VISIBLE)||(rePlayDialog!=null&&rePlayDialog.getVisibility()==View.VISIBLE);
    }

    private void doExceptionRePlay(){
        if(!NetworkUtil.isNetworkConnected(mActivity)){ //无网络时 不处理
            return;
        }
        if(basePlayerPage!=null){
            if(exceptionDialog.getIsContinueBtn()){//继续播放
                basePlayerPage.doPlayContinue();
            }else{//从新播放
                basePlayerPage.rePlay();
            }
            basePlayerPage.notifyHideDialog();
            hideDialogView();
        }
    }

    private void setListener(){
        exceptionDialog.setBtnOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doExceptionRePlay();
            }
        });
        rePlayDialog.setBtnOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rePlayDialog.setVisibility(View.GONE);
                if(basePlayerPage!=null){
                    basePlayerPage.onReplayBtnClick(true);
                }
            }
        });
    }

    /**
     * 更新loading显示的文字提示
     * @param text
     * @param name
     */
    @Override
    public void setLoadText(final String text, final String name) {
        super.setLoadText(text, name);
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(loadingView!=null) {
                    loadingView.setText(text, name);
                }
            }
        });

    }

    /**
     * 本地设置影院比例
     * @param ratioName
     */
    public void setSelectedRatio(String ratioName) {
//        if(moviePlayerSettingView!=null){
//            moviePlayerSettingView.setSelectedRatio(ratioName);
//        }
    }
    /**
     * 本地设置影院旋转
     * @param roteName
     */
    public void setSelectedRote(String roteName) {
//        if(moviePlayerSettingView!=null){
//            moviePlayerSettingView.setSelectedRote(roteName);
//        }
    }
    /**
     * 本地设置场景2D，3D左右，3D上下
     * @param mode
     */
    public void setSelectedLeftRightMode(final int mode) {
        leftRightType = mode;
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(playModeView!=null){
                    playModeView.setSelectedLeftRightMode(mode);
                }
            }
        });

    }
    /**
     * 本地设置场景平面，半球，球面
     * @param mode
     */
    public void setSelectedRoundMode(final int mode) {
        roundModeType = mode;
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(playModeView!=null){
                    playModeView.setSelectedRoundMode(mode);
                }
            }
        });

    }

    /**
     * 本地字幕列表（player会自动调用）
     * @param subtitleList
     */
    public void setSubtitleList(List<String> subtitleList){
        // TODO: 2017/5/22
        super.setSubtitleList(subtitleList);
        if(mPlaySettingsView!=null) {
            mPlaySettingsView.setSubtitleList(subtitleList);
        }
    }

    /**
     * 显示字幕内容（player自动调用）
     * @param content
     */
    @Override
    public void showSubTitle(final String content) {
        super.showSubTitle(content);
        if(basePlayerPage!=null&&basePlayerPage.mCurPlayMode!=VideoModeType.PLAY_MODE_SIMPLE_FULL){
            return;
        }
        //TODO 字幕显示
        mActivity.runOnUiThread( new Runnable() {
            @Override
            public void run() {
                if(subtitleText!=null){
                    if(TextUtils.isEmpty(content)){
                        subtitleText.setVisibility(View.GONE);
                    }else if(subtitleText.getVisibility()!=View.VISIBLE){
                        subtitleText.setVisibility(View.VISIBLE);
                    }
                    subtitleText.setText(content.equals("NULL")?"":content);
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

        if(mPlaySettingsView!=null) {
            mPlaySettingsView.setAudioStreamList(audioStreamList);
        }
    }

    /**
     * 设置当前选择的音轨
     * @param index
     */
    @Override
    public void setSelectedAudioStream(final int index) {
        super.setSelectedAudioStream(index);
        LogHelper.d(MovieSettingSubView.settingCallback,"setSelectedAudioStream index: "+index);
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(mPlaySettingsView!=null) {
                    mPlaySettingsView.setSelectedAudioStream(index);
                }
            }
        });

    }

    /**
     * 设置当前选择的解码方式
     * @param type
     */
    @Override
    public void setSelectedDecodeType(final int type) {
        super.setSelectedDecodeType(type);
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(mPlaySettingsView!=null) {
                    mPlaySettingsView.setSelectedDecodeType(type);
                }
            }
        });

    }

    /**
     * 设置当前选择的屏幕大小
     * @param type
     */
    @Override
    public void setSelectedScreenSize(int type) {
        super.setSelectedScreenSize(type);
        // TODO: 2017/5/22
//        if(moviePlayerSettingView!=null) {
//            moviePlayerSettingView.setSelectedScreenSize(type);
//        }
    }

    /**
     * 设置当前选择的屏幕亮度值
     * @param value
     */
    public void setSelectedScreenLight(int value){
        int perValue=0;
        if(value==0){
            perValue=0;
        }else{
            perValue=value*100/250;
        }
//        if(moviePlayerSettingView!=null) {
//            moviePlayerSettingView.setSelectedScreenLight(perValue);
//        }
    }

    /**
     * 设置当前选择的字幕
     * @param name
     */
    @Override
    public void setSelectedSubtitle(final String name) {
        super.setSelectedSubtitle(name);
        LogHelper.d(MovieSettingSubView.settingCallback,"setSelectedSubtitle name: "+name);
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(mPlaySettingsView!=null) {
                    mPlaySettingsView.setSelectedSubtitle(name);
                }
            }
        });

    }

    /**
     * 在线选片数据
     * @param contentInfos
     */
    @Override
    public void setMovieSelectVideoDatas(List<ContentInfo> contentInfos) {
        super.setMovieSelectVideoDatas(contentInfos);
//        if(moviePlayerSettingView!=null){
//            moviePlayerSettingView.setMovieVideoDatas(contentInfos,0);
//        }
    }

    /**
     * 本地选片数据
     * @param videoList
     * @param curIndex
     */
    @Override
    public void setLocalMovieSelectVideoDatas(final List<LocalVideoBean> videoList,final int curIndex) {
        super.setLocalMovieSelectVideoDatas(videoList,curIndex);
        //TODO
    }

    @Override
    public void updateSeekingProcess(final int curpos, final int duration,final boolean rewind) {
        super.updateSeekingProcess(curpos, duration,rewind);
//        if(basePlayerPage!=null&&basePlayerPage.mCurPlayMode!=VideoModeType.PLAY_MODE_SIMPLE_FULL) {
//            return;
//        }
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(playSeekToastView!=null){
                    playSeekToastView.updateSeekingProcess(curpos,duration,rewind);
                }

            }
        });


    }

    @Override
    public void setSeekingProcessVisable(final boolean visable) {
        super.setSeekingProcessVisable(visable);
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(playSeekToastView!=null){
                    playSeekToastView.setVisibility(visable?View.VISIBLE:View.GONE);
                }

            }
        });
    }


    @Override
    public void setHDable(boolean isable) {
      //TODO 设置清晰度按钮是否可用状态
    }

    public void setSubtitleFontSize(final int size){
       //TODO 刷新字体大小选择
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                LogHelper.d(MovieSettingSubView.settingCallback,"play lay view setSubtitleFontSize: "+size);
                int fontSize=mPlaySettingsView.changeGL2Layout(size);
                LogHelper.d(MovieSettingSubView.settingCallback,"play lay view setSubtitleFontSize changeGL2Layout: "+size);
                if(subtitleText!=null){
                    LogHelper.d(MovieSettingSubView.settingCallback,"play lay subtitleText!=null: "+size);
                    subtitleText.setTextSize(fontSize);
                }
                if(mPlaySettingsView!=null) {
                    LogHelper.d(MovieSettingSubView.settingCallback,"play lay mPlaySettingsView!=null "+size);
                    mPlaySettingsView.setSelectedSubtiteFondSize(fontSize);
                }
            }
        });

    }

    private IPlayerSettingCallBack settingCallBack = new IPlayerSettingCallBack(){

        @Override
        public void onSettingShowChange(String id,boolean isShow) {
            if(PlayControlView.VIEW_ID_SETTINGS.equals(id)) {
                hideAllView();
                showPlayControlView();
            }else if(PlayControlView.VIEW_ID_HDSelect.equals(id)){
                hideAllView();
                showPlayControlView();
            }else if(PlayControlView.VIEW_ID_playMode.equals(id)){
                hideAllView();
                showPlayControlView();
            }else if(PlayControlView.VIEW_ID_SelectSource.equals(id)) {
                hideAllView();
                showPlayControlView();
            }
        }

        @Override
        public void onHideControlAndSettingView(boolean isHide) {

        }

        @Override
        public void onHDChange(final String hdtype) {
//            if (2 != result && "4k".equals(hdtype)) {
//                if (unLockDialog == null) {
//                    unLockDialog = new UnLockDialog(activity, new UnLockDialog.UnLockCallBack() {
//                        @Override
//                        public void onConfirm() {
//                            changeHdType(hdtype);
//                        }
//
//                        @Override
//                        public void onCancel() {
//                            if(bootomView!=null) {
//                                bootomView.setDefinitionText(mCurDefinition);
//                            }
//                            if(mVideoSelectView!=null) {
//                                mVideoSelectView.setCurDefinition(mCurDefinition);
//                            }
//                        }
//                    }, PixelsUtil.getheightPixels()- PixelsUtil.dip2px(40));
//                }
//                unLockDialog.setContentText(getResources().getString(R.string.player_lock_tips));
//                unLockDialog.setConfirmText(getResources().getString(R.string.play));
//                unLockDialog.show();
//                return;
//            }
            if(mPlayControlView!=null){
                mPlayControlView.setHDname(hdtype);
            }
            hideHDAndSourceView(true);

            MJGLUtils.exeGLQueueEvent(mActivity, new Runnable() {
                @Override
                public void run() {
                    if(basePlayerPage!=null){
                        basePlayerPage.onChangeHd(hdtype);
                    }
                }
            });

        }

        @Override
        public void onHDable(boolean isAble) {

        }

        @Override
        public void onSoundChange(int vm) {
            if(vm>0){
                SoundUtils.SetVolumeMute(false,mActivity);
                SettingSpBusiness.getInstance(mActivity).setPlayerSoundMute(false);
            }
            SoundUtils.SetSoundVolume(vm,mActivity);
            if(basePlayerPage!=null) {
                basePlayerPage.onSoundChange(vm);
            }
        }

        @Override
        public void isOpenSound(boolean isOpen) {

        }

        @Override
        public void onSelected(final int num) {
            hideHDAndSourceView(true);
            MJGLUtils.exeGLQueueEvent(mActivity, new Runnable() {
                @Override
                public void run() {
                    if (basePlayerPage != null) {
                        basePlayerPage.onChangeSelectIndex(num);
                    }
                }
            });
        }

        @Override
        public void onRatioChange(String ratioName) {

        }

        @Override
        public void onRoteChange(int roteType) {

        }

        @Override
        public void onLeftRightModeChange(int modetype) {
            leftRightType = modetype;
            if(basePlayerPage!=null) {
                basePlayerPage.onLeftRightModeChange(modetype);
            }
            if(MojingAppCallbackUtil.mIMojingAppCallback!=null){
                MojingAppCallbackUtil.mIMojingAppCallback.changeVideoType(basePlayerPage.playerView.getPath(),leftRightType,roundModeType);
            }
        }

        @Override
        public void onRoundModeChange(final int modetype) {
            roundModeType = modetype;
            MJGLUtils.exeGLQueueEvent(mActivity, new Runnable() {
                @Override
                public void run() {
                    if(basePlayerPage!=null) {
                        basePlayerPage.onRoundModeChange(modetype);
                    }
                }
            });
            if(MojingAppCallbackUtil.mIMojingAppCallback!=null){
                MojingAppCallbackUtil.mIMojingAppCallback.changeVideoType(basePlayerPage.playerView.getPath(),leftRightType,roundModeType);
            }

        }

        @Override
        public void onResetMode() {
           if(basePlayerPage!=null){
               basePlayerPage.reSetMode();
           }
        }

        @Override
        public void onSelectedMovie(ContentInfo contentInfo, int currentNum) {

        }

        @Override
        public void onSelectedLocalMovie(LocalVideoBean bean, int currentNum) {

        }

        @Override
        public void onSelectedLocalMovieOri(int Ori) {

        }

        @Override
        public void onScreenSizeChange(int modetype) {

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
            if(subtitleText!=null){
                subtitleText.setTextSize(size);
            }
            if(basePlayerPage!=null) {
                basePlayerPage.onScreenSubtitleFontSize( mPlaySettingsView.sendLayout2GL(size) );
            }
        }

        @Override
        public void onMovieStatus(int status) {

        }

        @Override
        public void onSetSelectSourceType(int type) {

        }
    };

    @Override
    public void onFinish() {
        super.onFinish();
        if(mActivity!=null&&!mActivity.isFinishing()) {
            mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    hideAllView();
                    if (view != null) {
                        mActivity.getRootLayout().removeView(view);
                    }
                }
            });
        }

    }
    @Override
    public void setResID(String resID) {
        super.setResID(resID);

    }

    @Override
    public void showReplayDialog(List<ContentInfo> contentInfos, int currentIndex) {
        super.showReplayDialog(contentInfos, currentIndex);
        if(mActivity!=null&&!mActivity.isFinishing()){
            mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(rePlayDialog!=null) {
                        rePlayDialog.setVisibility(View.VISIBLE);
                    }
                }
            });
        }
    }

    @Override
    public void hideReplayDialog() {
        super.hideReplayDialog();
        if(mActivity!=null&&!mActivity.isFinishing()){
            mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(rePlayDialog!=null) {
                        rePlayDialog.setVisibility(View.GONE);
                    }
                }
            });
        }
    }

    @Override
    public boolean isSeekingProcessVisable() {
        if(playSeekToastView!=null){
            return playSeekToastView.getVisibility()==View.VISIBLE;
        }
        return super.isSeekingProcessVisable();
    }
}
