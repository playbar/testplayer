package com.mojing.vrplayer.page;

import android.content.Context;
import android.media.MediaPlayer;

import com.baofeng.mojing.MojingSDK;
import com.bfmj.viewcore.interfaces.IGLPlayer;
import com.bfmj.viewcore.render.GLColor;
import com.bfmj.viewcore.util.GLExtraData;
import com.bfmj.viewcore.view.GLGroupView;
import com.bfmj.viewcore.view.GLImageView;
import com.bfmj.viewcore.view.GLLinearView;
import com.bfmj.viewcore.view.GLRectView;
import com.bfmj.viewcore.view.GLViewPage;
import com.mojing.vrplayer.R;
import com.mojing.vrplayer.activity.GLBaseActivity;
import com.mojing.vrplayer.activity.MjVrPlayerActivity;
import com.mojing.vrplayer.bean.VideoModeType;
import com.mojing.vrplayer.business.PlayUINotifyBusiness;
import com.mojing.vrplayer.interfaces.IJoystickCallBack;
import com.mojing.vrplayer.interfaces.IPlayerUICallback;
import com.mojing.vrplayer.publicc.MotionInputCallback;
import com.mojing.vrplayer.publicc.PlayerBusiness;
import com.mojing.vrplayer.publicc.StickUtil;
import com.mojing.vrplayer.publicc.bean.ContentInfo;
import com.mojing.vrplayer.publicc.bean.LocalVideoBean;
import com.mojing.vrplayer.simpleview.ScreenControlView;
import com.mojing.vrplayer.utils.GLConst;
import com.mojing.vrplayer.utils.JoystickUitlManager;
import com.mojing.vrplayer.utils.MJGLUtils;
import com.mojing.vrplayer.view.BasePlayControlUI;
import com.mojing.vrplayer.view.BasePlayerView;
import com.mojing.vrplayer.view.DrawResetEyeView;
import com.mojing.vrplayer.view.GLVRPlayControlUI;
import com.mojing.vrplayer.view.SimplePlayControlUI;
import com.storm.smart.play.call.IBfPlayerConstant;
import com.storm.smart.play.utils.PlayCheckUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by wanghongfang on 2016/10/14.
 * 播放页面基类 （控制播放逻辑和控制UI交互）
 * UI层通过该类接口设置播放view需要的数据，调用播放View的方法，通知player执行用户操作
 */
public abstract class BasePlayerPage extends GLViewPage implements IPlayerUICallback,IJoystickCallBack {
    private Timer mUpdateTimer;
    protected int lastPlaytime;
    protected GLBaseActivity mActivity;
    protected GLGroupView mRootView;
    protected PlayUINotifyBusiness playUINotifyBusiness;
    protected BasePlayControlUI moviePlayerControlView;
    protected GLVRPlayControlUI glvrPlayControlUI;
    protected SimplePlayControlUI simplePlayControlUI;
    protected final int layout_width=2400;
    protected final int layout_height=2400;
    public    int playerWidth = 1460;
    public    int playerHeight = 821;
    protected final int layout_x=0;
    protected final int layout_y=720;
    /*播放成功时时间，退出时报数计算utime3 使用*/
    protected long playSuccessTime= 0;
//    public boolean isPlayCompletion = false;
    /*播放报数随机数*/
    protected String ReportRoundID="0";
    /*计算尝试时长的报数*/
    protected long startReport = 0;
    protected boolean hd_change_flag = false;  //正在切换清晰度
    protected List<String> mHdTextList = new ArrayList<>(); //清晰度
    public boolean isPauseView = false;//是否切出播放页面
//    protected Handler mHandler;
    public BasePlayerView playerView;
    protected int mPageType;
    private final float anim_depth = 0.15f;
    private final float anim_scale = 0.0375f;
    public String mCurDefinition; //当前选择的清晰度text
    private long mKeyLongPressTime = -1;//记录遥控器长按时间
    public boolean canThreadHandler = true;//控制进度条刷新
    private boolean seeking_flag =  false; //标志当前是否正在seek
    private boolean isTouchSeeking = true;//标志当前seeking是否触屏操作的
    private int prepareSeek = 0;
    public int mCurPlayMode = VideoModeType.PLAY_MODE_VR;//播放模式： 极简模式或沉浸模式
    protected String pageType; //报数pageType
    protected int mPlayDuration;
    protected int report_isloading = 1; // 播放报数字段（退出播放type=7时上报 1：加载中退出，0：播放成功退出）
    protected long report_conntime = 0; //播放报数字段（退出播放type=7时上报，记录开始加载到加载成功的时间）
    protected long report_360time = 0; //播放报数字段（type = 7时上报，记录双屏播放时长）
    protected  int report_vvplaysorce = 2; // 播放报数字段 点击预览=1，播放=2，还是全屏=3 (type = 1,2,3,7都上报)
    protected int report_isFirst = 1; //播放报数字段  1：首次进入 0：非首次进入 （type = 1,2,3,7）
    protected long report_360time_tmp = 0;
    boolean iscountTime = false; //报数 是否记录loading时间
    boolean isplayLong = false; //播放时间是否大于三分之二或5分钟
    DrawResetEyeView resetImage;
    GLLinearView resetImageContainer;

    public BasePlayerPage(Context context,int type){
        super(context);
        mActivity = ((GLBaseActivity) getContext());
        this.mPageType = type;
        if (!MojingSDK.GetInitSDK()) {
            MojingSDK.Init(getContext());
            MojingSDK.onDisableVrService(true);
        }
        PlayCheckUtil.setSupportLeftEye(true);
        JoystickUitlManager.getInstance().onBind(this);
        createResetView();
    }

    public void createResetView(){
        resetImageContainer=new GLLinearView(mActivity);
        resetImageContainer.setLayoutParams(2000,2000);

//        resetImage = new GLImageView(mActivity);
//        resetImage.setLayoutParams(100,100);
//        resetImage.setMargin(1500f,1000f,35f,10f);
//        resetImage.setBackground(R.drawable.play_menu_button_view_hover_1);
//        resetImage.setFixed(true);
//        resetImage.setVisible(false);
      //  resetImageContainer.addView(resetImage);

        resetImage=new DrawResetEyeView(mActivity);
        resetImage.setMargin(1150f,1100f,0f,0f);
        resetImage.setFixed(true);
        //drawResetEyeView.setBackground(R.drawable.play_menu_button_view_hover_1);
        resetImageContainer.setFixed(true);
        resetImageContainer.addView(resetImage);

//        GLImageView drawResetEyeView=new GLImageView(mActivity);
//        drawResetEyeView.setLayoutParams(500,500);
//        drawResetEyeView.setMargin(1300f,700f,35f,10f);
//        drawResetEyeView.setFixed(true);
//        drawResetEyeView.setVisible(true);
//        drawResetEyeView.setBackground(new GLColor(0x00ff00));
//        resetImageContainer.setFixed(true);
//        resetImageContainer.setBackground(R.drawable.play_menu_button_view_hover_1);
//        resetImageContainer.addView(drawResetEyeView);

        resetImageContainer.setDepth(GLConst.Subtitle_Depth);
        mActivity.getRootView().addView(resetImageContainer);



      //  setResetViewVisible(false);
    }

    public void setResetViewVisible(boolean visible){
        if(visible){
            resetImage.setVisible(true);
            resetImage.startDrawme();
        }else{
            resetImage.setVisible(false);
        }
        resetImageContainer.setVisible(visible);
    }


    public boolean isResetViewVisable(){
        return resetImage.isVisible();
    }



    @Override
    public GLRectView createView(GLExtraData glExtraData) {
        mRootView = new GLGroupView(mActivity);
        mRootView.setDepth(GLConst.Movie_Player_Depth);
        mCurPlayMode = glExtraData.getExtraInt("play_mode");
        pageType = glExtraData.getExtraString("pageType");
        report_vvplaysorce = glExtraData.getExtraInt("vvplaysource",2);
        report_isFirst = ((MjVrPlayerActivity)mActivity).isFirst;
        startUpdateTimer();
        glvrPlayControlUI = new GLVRPlayControlUI(mRootView,mActivity,this,mPageType);
        simplePlayControlUI = new SimplePlayControlUI(mActivity,this,mPageType);
        playUINotifyBusiness = new PlayUINotifyBusiness();
        playUINotifyBusiness.addUIView(glvrPlayControlUI);
        playUINotifyBusiness.addUIView(simplePlayControlUI);
        if(mCurPlayMode==VideoModeType.PLAY_MODE_VR){
            moviePlayerControlView = glvrPlayControlUI;
            simplePlayControlUI.setUIVisiable(false);
        }else {
            moviePlayerControlView=simplePlayControlUI;
            glvrPlayControlUI.setUIVisiable(false);
        }
        isPauseView = mActivity.isActivityPause;
        return mRootView;
    }


    @Override
    public void onResume() {
        super.onResume();
        if(!mActivity.isActivityPause){
            isPauseView = false;
        }
         startUpdateTimer();
        if(playUINotifyBusiness!=null){
            playUINotifyBusiness.onResum();
        }


    }

    @Override
    public void onPause() {
        super.onPause();
        isPauseView = true;
        stopUpdateTimer();
        if(playUINotifyBusiness!=null){
            playUINotifyBusiness.onPause();
        }
    }

    @Override
    public void onFinish() {
        super.onFinish();
        if(playerView!=null){
            playerView.release();
        }
        if(playUINotifyBusiness!=null){
            playUINotifyBusiness.onFinish();
        }
        JoystickUitlManager.getInstance().unBind(this);
    }
    private void startUpdateTimer() {
        if (mUpdateTimer != null) {
            mUpdateTimer.cancel();
            mUpdateTimer = null;
        }

        mUpdateTimer = new Timer();
        mUpdateTimer.schedule(new TimerTask() {

            @Override
            public void run() {
                if(canThreadHandler) {
                    updateProgress();
                }
            }
        }, 1000, 1000);
    }

    private void stopUpdateTimer() {
        if (mUpdateTimer != null) {
            mUpdateTimer.cancel();
            mUpdateTimer = null;
        }
    }


    public BasePlayerView.PlayerState getPlayerCurrentState(){
        if(playerView!=null){
           return playerView.getmCurrentState();
        }
        return BasePlayerView.PlayerState.IDEL;
    }
    /**
     * 取消seek时的实时进度刷新
     */
    private synchronized void cancelSeekProgress(){
        if(timer!=null){
            timer.cancel();
            timer = null;
        }
    }
    Timer timer;

    /**
     * 长按摇杆快进时 实时刷新进度
     * @param rewind
     */
    private synchronized void changeSeekProgress(final boolean rewind){
        canThreadHandler = false;
        seeking_flag = true;
        cancelSeekProgress();
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if(seeking_flag) {
//                    ((Activity)getContext()).runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
                    if (playerView == null) {
                        return;
                    }
                    int curpos = prepareSeek>0?prepareSeek:lastPlaytime;
                    int rateProgress = getRateProgress();
                    int duration = playerView.getDuration();
                    if (rewind) {
                        if (curpos > rateProgress) {
                            curpos -= rateProgress;
                        } else {
                            curpos = 0;
                        }
                    } else {
                        if (curpos < duration - rateProgress) {
                            curpos += rateProgress;//
                        } else {
                            curpos = duration - 5000;
                        }
                    }
                    if(curpos>0) {
                        prepareSeek = curpos;
                        updateSeekingProcess(curpos, duration,rewind);
                    }

//                        }
//                    });


                }else {
                    timer.cancel();
                    timer = null;
                }
            }
        },0,600);

    }
    @Override
    public boolean onZKeyDown(int keyCode) {
        if (keyCode == PlayerBusiness.getInstance().KEY_BLUETOOTH_OK
                || keyCode == PlayerBusiness.getInstance().KEY_JOYSTICK_OK) {
            return false;
        }  else if (keyCode == PlayerBusiness.getInstance().KEY_BLUETOOTH_LEFT) {
            mKeyLongPressTime = System.currentTimeMillis();
            if(playerView!=null&&playerView.getDuration()>0 && (glvrPlayControlUI!=null && !glvrPlayControlUI.isRePlayDialogShowing())){
                canThreadHandler = false;
                setSeekingProcessVisable(true,false);
                changeSeekProgress(true);
            }

            return false;
        } else if (keyCode == PlayerBusiness.getInstance().KEY_BLUETOOTH_RIGHT) {
            mKeyLongPressTime = System.currentTimeMillis();
            if(playerView!=null&&playerView.getDuration()>0 && (glvrPlayControlUI!=null && !glvrPlayControlUI.isRePlayDialogShowing())) {
                canThreadHandler = false;
                setSeekingProcessVisable(true,false);
                changeSeekProgress(false);
            }
            return false;
        }
        return false;
    }

    @Override
    public void onZKeyUp(int keyCode) {
        if (keyCode == PlayerBusiness.getInstance().KEY_BLUETOOTH_OK
                || keyCode == PlayerBusiness.getInstance().KEY_JOYSTICK_OK) {

        }else if (keyCode ==  PlayerBusiness.getInstance().KEY_BLUETOOTH_LEFT) {
            if(playerView!=null&&playerView.getDuration()>0 && (glvrPlayControlUI!=null && !glvrPlayControlUI.isRePlayDialogShowing())) {
                rewind();
            }
        } else if (keyCode ==  PlayerBusiness.getInstance().KEY_BLUETOOTH_RIGHT) {
            if (playerView != null && playerView.getDuration() > 0 && (glvrPlayControlUI!=null && !glvrPlayControlUI.isRePlayDialogShowing())) {
                fastForward();
            }
        }

        mKeyLongPressTime = -1;

    }
    @Override
    public void onZKeyLongPress(int keyCode){

    }
    @Override
    public void onConnStartCheck(){
       if(StickUtil.getConnectStatus()==0){ //断开连接
           if(moviePlayerControlView!=null&&moviePlayerControlView.isSeekingProcessVisable()&&!isTouchSeeking){
               rewind();
           }
       }
    }

    private float mStartX = -1;
    private float mStartY = -1;
    private float mEndX = -1;
    private float mEndY = -1;
    float minMove = 0.4f;
    boolean isRight = false;
    boolean isMove = false;
    @Override
    public void onMotionTouch(MotionInputCallback.Event e) {
        switch (e.getAction()) {
            case MotionInputCallback.Event.ACTION_DOWN:
                mStartX = e.getX();
                mStartY = e.getY();
                mEndX = e.getX();
                mEndY = e.getY();
                break;
            case MotionInputCallback.Event.ACTION_MOVE:
                mEndX = e.getX();
                mEndY = e.getY();
                if(mStartX-mEndX>minMove){   //左滑
                    isRight = false;
                    isMove = true;
                    mKeyLongPressTime = System.currentTimeMillis();
                    if(playerView!=null&&playerView.getDuration()>0 && (glvrPlayControlUI!=null && !glvrPlayControlUI.isRePlayDialogShowing())) {
                        canThreadHandler = false;
                        setSeekingProcessVisable(true,false);
                        changeSeekProgress(true);
                    }
                    mStartX = mEndX;
                }else if(mEndX-mStartX>minMove){   //右滑
                    isRight = true;
                    isMove = true;
                    mKeyLongPressTime = System.currentTimeMillis();
                    if(playerView!=null&&playerView.getDuration()>0 && (glvrPlayControlUI!=null && !glvrPlayControlUI.isRePlayDialogShowing())) {
                        canThreadHandler = false;
                        setSeekingProcessVisable(true,false);
                        changeSeekProgress(false);
                    }
                    mStartX = mEndX;
                }else if(mStartY-mEndY>minMove){   //上滑
                    if(null != playUINotifyBusiness) {
                        playUINotifyBusiness.prePage();
                    }
                    mStartY = mEndY;
                }else if(mEndY-mStartY>minMove){   //下滑
                    if(null != playUINotifyBusiness) {
                        playUINotifyBusiness.nextPage();
                    }
                    mStartY = mEndY;
                }
                break;
            case MotionInputCallback.Event.ACTION_UP:
                if(playerView!=null&&playerView.getDuration()>0 && (glvrPlayControlUI!=null && !glvrPlayControlUI.isRePlayDialogShowing())) {

                    if (isMove) {
                        if (isRight) {
                            fastForward();
                        } else {
                            rewind();
                        }
                        isMove = false;
                    }
                }
                mKeyLongPressTime = -1;
                break;
        }
    }

    /**
     * 触屏全屏模式下滑动快进快退处理
     */
    public void onViewTouchEvent(int event){
        if(event == ScreenControlView.IPlayerRateListener.TOUCH_DOWN){
            mKeyLongPressTime = System.currentTimeMillis();
        }else if(event == ScreenControlView.IPlayerRateListener.TOUCH_MOVE_LEFT){   //左滑
            isRight = false;
            if(playerView!=null&&playerView.getDuration()>0) {
                canThreadHandler = false;
                setSeekingProcessVisable(true,true);
                changeSeekProgress(true);
            }
        }else if(event == ScreenControlView.IPlayerRateListener.TOUCH_MOVE_RIGHT){   //右滑
            isRight = true;
            if(playerView!=null&&playerView.getDuration()>0) {
                canThreadHandler = false;
                setSeekingProcessVisable(true,true);
                changeSeekProgress(false);
            }
        }else if(event == ScreenControlView.IPlayerRateListener.TOUCH_UP){
            if(playerView!=null&&playerView.getDuration()>0) {

                if (isRight) {
                    fastForward();
                } else {
                    rewind();
                }
            }
            mKeyLongPressTime = -1;
        }
    }

    /**
     * 快进
     */
    int curpos = 0;
    public synchronized void fastForward() {
        rewind();
    }

    /***
     * 快退
     *
     */
    Timer seekTimer;
    public synchronized void rewind() {
        if(seekTimer!=null){
            seekTimer.cancel();
            seekTimer = null;
        }
        seekTimer = new Timer();
        seekTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                seeking_flag = false;
                cancelSeekProgress();
                setSeekingProcessVisable(false,true);
                canThreadHandler = false;
                curpos = lastPlaytime;
                if (prepareSeek < 0||prepareSeek>mPlayDuration) {
                    canThreadHandler = true;
                    return;
                }
                curpos = prepareSeek;
                seekTo(curpos);
                prepareSeek =0;
            }
        },300);

    }

    /**
     * 快进或快退的进度
     * @return
     */
    private synchronized int getRateProgress() {
        double rate = 5.0f;
        if (mKeyLongPressTime > 0) {  //小于等于3s时 每0.5s跳5s的进度
            long time = (System.currentTimeMillis() - mKeyLongPressTime)/1000;

            if (time>0&&time <= 3) {
                rate = 5*2.0f*time;
            } else if (time > 3 ) { //大于3s 每秒跳进度，按倍率调 为前一秒的1.5倍
                rate = 5f*Math.pow(1.5,time-3)*time;
            }
        }
        return (int)(rate*1000);
    }

    /**
     * 刷新快进快退显示
     * @param curpos
     * @param duration
     */
    public void updateSeekingProcess(int curpos,int duration,boolean rewind){
        if(playUINotifyBusiness!=null){
            playUINotifyBusiness.updateSeekingProcess(curpos,duration,rewind);
        }
    }
    public void setSeekingProcessVisable(boolean visable,boolean isTouch){
        isTouchSeeking = isTouch;
        if(playUINotifyBusiness!=null){
            playUINotifyBusiness.setSeekingProcessVisable(visable);
        }
    }
    /**
     * 更新播放进度
     * @param
     * @return
     */
    public abstract void updateProgress();

    /**
     * 暂停播放
     */
    public abstract void pausePlay();

    /**
     * 开始播放
     */
    public abstract void startPlay();

    /**
     * 重播
     */
    public abstract void rePlay();

    /**
     * 报数
     * @param type
     * @param utime
     * @param ltime
     */
    public void reportVV(String type, String utime, String ltime){
        if("1".equals(type)){
            report_isloading = 1;
            report_conntime = System.currentTimeMillis();
            report_360time = 0;
            report_360time_tmp = System.currentTimeMillis();
            iscountTime = false;
            isplayLong = false;
            mPlayDuration = 0;
        }else{
            if("2".equals(type)||"3".equals(type)) {
                report_isloading = 0;
            }
            if(!iscountTime){
                report_conntime = (report_conntime>0)?System.currentTimeMillis()-report_conntime:0;
                iscountTime = true;
            }

        }
        if("7".equals(type)){
            if(report_360time_tmp>0) {
                report_360time += System.currentTimeMillis() - report_360time_tmp;
            }
            if(mPlayDuration>0){
                float process = ((float) lastPlaytime)/mPlayDuration;
                if(lastPlaytime>5*60*1000||process>0.66){
                    isplayLong = true;
                }
            }
        }
    }

    /**
     * 切换清晰度
     * @param hdtype
     */
    public void onChangeHd(String hdtype){
        if(playUINotifyBusiness!=null){
            playUINotifyBusiness.setSelectedHD(hdtype,moviePlayerControlView);
        }
    }

    /**
     * 切换选集
     * @param SeqNo
     */
    public void onChangeSelectIndex(int SeqNo){
        if(playUINotifyBusiness!=null){
            playUINotifyBusiness.setCurrentNum(SeqNo,moviePlayerControlView);
        }
    }

    /**
     * 检测是否是移动网络播放
     */
    public void checkPlay(){}

    /**
     * 网络异常后重试操作
     * @param isPlayError
     */
    public void doNetChanged(boolean isPlayError ){}
    public void handleNetWorkException(){}
    public void showLoading(){}
    public void hideLoading(){}
    /*屏幕大小*/
    public void onScreenSizeChange(int type){
        if(playUINotifyBusiness!=null){
            playUINotifyBusiness.setSelectedScreenSize(type,moviePlayerControlView);
        }
    }
    /*本地字幕选择回调*/
    public void onSelectedSubtitle(int index, String subtitleName){
        if(playUINotifyBusiness!=null){
            playUINotifyBusiness.setSelectedSubtitle(subtitleName,moviePlayerControlView);
        }
    }
    /*本地字幕打开或隐藏回调*/
    public void onHideSubtitleView(boolean isHide){}
    /*本地编解码切换回调*/
    public void onChangeDecodeType(int type){
        if(playUINotifyBusiness!=null){
            playUINotifyBusiness.setSelectedDecodeType(type,moviePlayerControlView);
        }
    }
    /*本地模式选择左右3d模式*/
    public void onLeftRightModeChange(int modeltype){
        if(playUINotifyBusiness!=null){
            playUINotifyBusiness.setSelectedLeftRightMode(modeltype,moviePlayerControlView);
        }
    }
    /*本地模式选择全景,180,立方体等*/
    public void onRoundModeChange(int modeltype){
        if(playUINotifyBusiness!=null){
            playUINotifyBusiness.setSelectedRoundMode(modeltype,moviePlayerControlView);
        }
    }
    /*本地画面比例*/
    public void onRatioChange(String ratioName){
        if(playUINotifyBusiness!=null){
            playUINotifyBusiness.setSelectedRatio(ratioName,moviePlayerControlView);
        }
    }
    /*本地模式旋转*/
    public void onRoteChange(int roteType){}
    /*设置音轨选项  index为AudioStreamInfo中的index */
    public void onAudioTrackChange(int index){
        if(playUINotifyBusiness!=null){
            playUINotifyBusiness.setSelectedAudioStream(index,moviePlayerControlView);
        }
    }
    /*在线选片回调*/
    public void onSelectedMovie(ContentInfo contentInfo, int currentNum) {
        if(playUINotifyBusiness!=null){
            playUINotifyBusiness.setCurrentNum(currentNum,moviePlayerControlView);
        }
    }
    /*本地选片回调*/
    public void onSelectedLocalMovie(LocalVideoBean bean, int currentNum) {
        if(playUINotifyBusiness!=null){
            playUINotifyBusiness.setCurrentNum(currentNum,moviePlayerControlView);
        }
    }
    /*续播dialog中切换回调*/
    public void onSelected(ContentInfo contentInfo, int currentNum) {
        if(playUINotifyBusiness!=null){
            playUINotifyBusiness.setCurrentNum(currentNum,moviePlayerControlView);
        }
    }
    /*续播对话框是否重播回调*/
    public void onReplayBtnClick(boolean isRePlay) {
         notifyHideDialog();
        if(playUINotifyBusiness!=null){
            playUINotifyBusiness.hideReplayDialog();
        }
    }
    /**
     * 设置播放进度
     * @param position
     */
    public  void seekTo(int position){
        if(playerView==null||position<0||playerView.getDuration()<=0) {
            return;
        }
//        if(position>=playerView.getDuration()){
//            position = playerView.getDuration()-1000;
//        }
        canThreadHandler = false;
        try {
            playerView.seekTo(position);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onMobileNet(){
        if(playerView!=null) {
            playerView.stopP2P();
        }
    }

    /**
     * 移动网络时继续播放操作
     */
    public void doPlayContinue(){}


        @Override
        public void onPrepared(IGLPlayer player) {
            if (playUINotifyBusiness != null) {
                playUINotifyBusiness.setPlayOrPauseBtn(false);
                playUINotifyBusiness.hideReplayDialog();
            }
            if(isPauseView){
              playerView.pausePlay();
            }
            if(player!=null) {
                mPlayDuration = player.getDuration();
                if(playUINotifyBusiness!=null){
                    playUINotifyBusiness.setDisplayDuration(mPlayDuration);
                }
            }
            onPlayPrepared();
            checkPlay();
            hideLoading();

        }

        @Override
        public boolean onInfo(IGLPlayer player, int what, Object extra) {
            if (what == MediaPlayer.MEDIA_INFO_BUFFERING_START
                    || what == IBfPlayerConstant.IOnInfoType.INFO_BUFFERING_START) {
                showLoading();

            } else if (what == MediaPlayer.MEDIA_INFO_BUFFERING_END
                    || what == IBfPlayerConstant.IOnInfoType.INFO_BUFFERING_END) {
                hideLoading();
            }
            return false;
        }



        @Override
        public void onCompletion(IGLPlayer player) {
            onPlayComplete();
        }

        @Override
        public void onSeekComplete(IGLPlayer player) {
            canThreadHandler= true;
            hideLoading();
            if (isPauseView) {
                pausePlay();
            } else {
                startPlay();
                if(playUINotifyBusiness!=null) {
                    playUINotifyBusiness.setPlayOrPauseBtn(false);
                }
            }

        }

        @Override
        public boolean onError(IGLPlayer player, int what, int extra) {
            doNetChanged(true);
            onPlayError();
            return false;
        }


    /**
     * 成功播放后回调改方法（在playerView中），处理了报数问题
     */
    public void onPlayPrepared(){

      /*记录播放成功时的时间*/
        if(playSuccessTime<=0){
            playSuccessTime = System.currentTimeMillis();
        }
        if(startReport>0) {
            reportVV("2", "0", (System.currentTimeMillis() - startReport) + "" );
            startReport = 0;
        }
    }

    public void onPlayComplete() {

        reportPlaySuccess();
        startReport = System.currentTimeMillis();
        reSetRoundId();
    }
    /**
     * 播放失败回调
     */
    public void onPlayError() {

        long utime = 0,ltime=0;
        if(playSuccessTime>0){
            utime = System.currentTimeMillis()-playSuccessTime;
        }
        if(startReport>0){
            ltime = System.currentTimeMillis()-startReport;
        }
        playSuccessTime = 0;
        startReport = 0;
        reportVV("3",utime<0?"0":utime+"",ltime<0?"0":ltime+"" );
    }

    @Override
    public void onChangeToSoft() {
    }

    /**
     * 重置报数用的随机数
     */
    public void reSetRoundId(){
        Random random = new Random();
        ReportRoundID = random.nextInt()+"";
    }

    public void reportPlaySuccess() {
        long utime = 0,ltime=0;
        if(playSuccessTime>0){
            utime = System.currentTimeMillis()-playSuccessTime;
        }
        if(startReport>0){
            ltime = System.currentTimeMillis()-startReport;
        }
        playSuccessTime = 0;
        reportVV("7",utime<0?"0":utime+"",ltime<0?"0":ltime+"");
    }

    public void setHDData(List<String> hdtext,String cur){
        String[] arry = (String[])hdtext.toArray(new String[hdtext.size()]);
        if(playUINotifyBusiness!=null) {
            playUINotifyBusiness.setHDdata(arry, cur);
        }
    }

    public void updateVolumChange(final int keyCode){
        if(moviePlayerControlView!=null){
            moviePlayerControlView.updateVolumChange(keyCode);
        }
    }

    /**
     * 设置当前模式 极简模式或沉浸模式
     * @param playMode
     */
    public void setCurPlayMode(int playMode){
        this.mCurPlayMode = playMode;
        ((MjVrPlayerActivity)mActivity).mCurPlayMode = playMode;
        if(playMode==VideoModeType.PLAY_MODE_SIMPLE_FULL){ //触屏全屏模式
            glvrPlayControlUI.setUIVisiable(false);
            moviePlayerControlView = simplePlayControlUI;
            mActivity.getRootView().setDoubleScreen(false);
            mActivity.hideResetLayerView();
//            Log.d("cursor","--------hideCursorView---setCurPlayMode");
            mActivity.hideCursorView();
            mActivity.hideMotionCursorView();
        }else if(playMode==VideoModeType.PLAY_MODE_SIMPLE){ //触屏模式
            glvrPlayControlUI.setUIVisiable(true);
            glvrPlayControlUI.hideAllView();
            glvrPlayControlUI.setLockLayerVisiable(false);
            moviePlayerControlView = simplePlayControlUI;
            mActivity.getRootView().setDoubleScreen(true);
//            Log.d("cursor","--------hideCursorView---setCurPlayMode");
            mActivity.hideCursorView();
            mActivity.hideResetLayerView();
            mActivity.hideMotionCursorView();
        }else { //沉浸模式
           // simplePlayControlUI.setUIVisiable(false);
            moviePlayerControlView = glvrPlayControlUI;
            mActivity.getRootView().setDoubleScreen(true);
            glvrPlayControlUI.setLockLayerVisiable(true);
//            mActivity.setCursorLayerVisiable(true);
            if (StickUtil.getConnectStatus() == 1) { //体感手柄连接中
                mActivity.showMotionCursorView();
                mActivity.hideResetLayerView();
            } else {
                if(!glvrPlayControlUI.isDialogShowing()) {
                    mActivity.showResetLayerView();
                } else {
                    mActivity.showCursorView();
                }
            }
            moviePlayerControlView.setUIVisiable(true);
        }
        simplePlayControlUI.setMode(mCurPlayMode);

        //报数需要的计算双屏模式下播放时长
        if(report_360time_tmp>0){
            report_360time+=System.currentTimeMillis()-report_360time_tmp;
            report_360time_tmp = 0;
        }
        if(mCurPlayMode!=VideoModeType.PLAY_MODE_SIMPLE_FULL){
            report_360time_tmp = System.currentTimeMillis();
        }
    }

    /**
     * 设置可触屏
     * @param touch
     */
    public void setScreenTouch(boolean touch){
        if(mActivity!=null&&mActivity.getRootView()!=null){
            mActivity.getRootView().setScreenTouch(touch);
        }
    }

    /**
     * 设置陀螺仪是否可用
     * @param enable
     */
    public void setGyroscopeEnable(final boolean enable){
        if(mActivity!=null&&mActivity.getRootView()!=null){
            MJGLUtils.exeGLQueueEvent(mActivity, new Runnable() {
                @Override
                public void run() {
                    mActivity.setGroyEnable(enable);
                }
            });

        }
    }

    public int getmPageType(){
        return  mPageType;
    }

    public boolean isPlaying(){
        if(playerView!=null){
            return playerView.isPlaying();
        }
        return false;
    }
    public void stopChangeTimer(){}


    public void onPlayChanged(boolean status){
        if(!status){ //播放
            startPlay();
        }else {//暂停
            pausePlay();
        }
        if(playUINotifyBusiness!=null){
            playUINotifyBusiness.setPlayOrPauseBtn(status,moviePlayerControlView);
        }
    }
    public void onSeekToChanged(int curPosition){
        if(playerView!=null&&playerView.isPlayCompletion){
            playerView.mPrepareSeekTo = curPosition;
            rePlay();
            if(playUINotifyBusiness!=null) {
                playUINotifyBusiness.hideReplayDialog();
            }
            return;
        }
        pausePlay();
        seekTo(curPosition);
    }


    //--------updateUI--------
    public void onHDable(boolean isable){
       if(playUINotifyBusiness!=null){
           playUINotifyBusiness.setHDable(isable);
       }
    }

    public void onSoundChange(int vm){
        if(playUINotifyBusiness!=null){
            playUINotifyBusiness.setVolume(vm,moviePlayerControlView);
        }
    }

    public void isOpenSound(boolean isOpen){
        if(playUINotifyBusiness!=null){
            playUINotifyBusiness.setSoundMute(!isOpen,moviePlayerControlView);
        }
    }

    public void onScreenLightChange(int size){
        if(playUINotifyBusiness!=null){
            playUINotifyBusiness.setSelectedScreenLight(size,moviePlayerControlView);
        }
    }

    public void onScreenSubtitleFontSize(int size){
        if(playUINotifyBusiness!=null){
            playUINotifyBusiness.setSubtitleFontSize(size,moviePlayerControlView);
        }
    }

    public void onSetSelectSourceType(int type){

    }

    public void notifyHideDialog(){
        if(playUINotifyBusiness!=null){
            playUINotifyBusiness.hideDialogView();
        }
    }

    /**
     * 本地播放模式重置
     */
    public void reSetMode(){}

    public void setLockScreen(){
        if(mPageType==GLConst.MOVIE||mPageType==GLConst.LOCAL_MOVIE) {
            if (((MjVrPlayerActivity) mActivity).isLockScreen) {
                MJGLUtils.exeGLQueueEvent(mActivity, new Runnable() {
                    @Override
                    public void run() {
                        mRootView.setFixed(true);
                        ((MjVrPlayerActivity) mActivity).onLockChanged(true);
                    }
                });

            }
        }
    }

}
