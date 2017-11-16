package com.mojing.vrplayer.view;


import android.view.KeyEvent;
import com.baofeng.mojing.input.base.MojingKeyCode;
import com.mojing.vrplayer.activity.GLBaseActivity;
import com.mojing.vrplayer.bean.AudioSteamInfo;
import com.mojing.vrplayer.interfaces.IBaseControlView;
import com.mojing.vrplayer.interfaces.IJoystickCallBack;
import com.mojing.vrplayer.page.BasePlayerPage;
import com.mojing.vrplayer.publicc.MotionInputCallback;
import com.mojing.vrplayer.publicc.SettingSpBusiness;
import com.mojing.vrplayer.publicc.StickUtil;
import com.mojing.vrplayer.publicc.bean.ContentInfo;
import com.mojing.vrplayer.publicc.bean.LocalVideoBean;
import com.mojing.vrplayer.publicc.bean.VideoDetailBean;
import com.mojing.vrplayer.utils.JoystickUitlManager;
import com.mojing.vrplayer.utils.MJGLUtils;
import com.mojing.vrplayer.utils.SoundUtils;
import com.storm.smart.common.utils.LogHelper;

import java.util.List;

/**
 * Created by wanghongfang on 2017/5/9.
 * 播放控制UI层基类（子类有极简模式UI和沉浸模式UI）
 * 播放的view通过该类接口操作通知更新UI层或者从UI层获取状态
 */
public class BasePlayControlUI implements IBaseControlView,IJoystickCallBack {
    public static final int MOVIE = 0;
    public static final int LOCAL_MOVIE = 1;
    public static final int PANO = 2;
    public static final int LOCAL_PANO = 3;
    public boolean show_load_toast = true;  //标志是否显示"即将播放..."的toast
    protected long mLoadingTime = -1;
    protected long mStartTipsTime = -1;  //播放中加载超过10s提示的计时
    protected int mLoadingCount = 0;
    protected long mLoadingAllTime = 0;
    protected int mType = 0;  //页面类型
    protected GLBaseActivity mActivity;
    protected BasePlayerPage basePlayerPage;
    public BasePlayControlUI(GLBaseActivity activity, BasePlayerPage basePlayerPage){
        this.mActivity = activity;
        this.basePlayerPage = basePlayerPage;
        JoystickUitlManager.getInstance().onBind(this);
    }

    /**
     * 更新播放或暂停
     * @param playOrPause
     */
    @Override
    public void setPlayOrPauseBtn(boolean playOrPause) {

    }

    /**
     * 设置进度条进度
     * @param process
     */
    @Override
    public void setProcess(int process) {
    }

  /**更新播放进度  */
    @Override
    public void updateProgress(int current, int duration) {

    }

    /**
     * 是否播放状态
     * */
    @Override
    public boolean isPlayFlag() {
        return false;
    }

    /**
     * 设置名称
     * @param name
     */
    @Override
    public void setName(String name) {

    }

    /**
     * 设置显示的总时长
     * @param duration
     */
    @Override
    public void setDisplayDuration(long duration) {
    }

    /**
     * 在线详情数据（选集会用）
     * @param videosBean
     * @param index
     */
    @Override
    public void setMovieVideoDatas(VideoDetailBean videosBean, int index) {
    }

    /**
     * 当前选的集数
     * @param index
     */
    @Override
    public void setCurrentNum(int index) {
    }

    /**
     * 当前音量
     * @param vm
     */
    @Override
    public void setVolume(int vm) {
    }

    /**
     * 设置是否静音
     * @param flag true：静音  false 关闭静音
     */
    @Override
    public void setSoundMute(boolean flag) {
    }

    /**
     * 设置清晰度数据
     * @param strs 清晰度列表
     * @param defaultHD  默认清晰度选项
     */
    @Override
    public void setHDdata(String[] strs, String defaultHD) {
    }

    /**
     * 设置当前选择的清晰度
     * @param hdtype
     */
    @Override
    public void setSelectedHD(String hdtype) {
    }

    /**
     * 页面类型
     * @param type
     */
    @Override
    public void setType(int type) {
        this.mType = type;
    }

    @Override
    public void showLoading() {
    }

    @Override
    public void hideLoading() {
    }

    /**
     * 通知UI层处理网络问题
     * @param isPlayError
     */
    public void doNetChanged(boolean isPlayError){
    }

    /**
     * 网络异常通知UI处理显示对应提示
     */
    public void handleNetWorkException(){
    }

    public void setLoadText(String text,String name){
    }

    public void showToast(String text,int type){
    }
    /*显示带图片的toast*/
    public void showImgToast(String text,int imgRes){
    }

    /**
     * 是否有Toast显示
     * @return
     */
    public boolean getLoadToastVisiable(){
        return false;
    }

    /**
     * 是否有对话框显示
     * @return
     */
    public boolean isDialogShowing(){
        return false;
    }

    public boolean isRePlayDialogShowing() {return false;}

    /**
     * 刷新开进快退的进度
     * @param curpos
     * @param duration
     * @param rewind   true：快进  false 快退
     */
    public void updateSeekingProcess(int curpos,int duration,boolean rewind){
    }

    /**
     * 显示或隐藏快进快退框
     * @param visable
     */
    public void setSeekingProcessVisable(boolean visable){
    }

    public boolean isSeekingProcessVisable(){
        return false;
    }
    public void updateVolumChange(final int keyCode){
        MJGLUtils.exeGLQueueEvent(mActivity, new Runnable() {
            @Override
            public void run() {
                if(KeyEvent.KEYCODE_VOLUME_MUTE == keyCode){
                    setSoundMute(SoundUtils.isVolumeMute(mActivity));
                    SettingSpBusiness.getInstance(mActivity).setPlayerSoundMute(SoundUtils.isVolumeMute(mActivity));
                } else if(keyCode== MojingKeyCode.KEYCODE_VOLUME_DOWN||keyCode==MojingKeyCode.KEYCODE_VOLUME_UP){
                    SoundUtils.addSoundVolume(keyCode == MojingKeyCode.KEYCODE_VOLUME_DOWN ? -1 : 1,mActivity);
                    setVolume(SoundUtils.GetCurrentVolumePercent(mActivity));
                }
                else {
                    setSoundMute(false);
                    int sound = SoundUtils.GetCurrentVolumePercent(mActivity);
                    sound=sound<0?0:sound;
                    if(sound==0&&KeyEvent.KEYCODE_VOLUME_DOWN==keyCode){
                        SoundUtils.SetSoundVolume(0,mActivity);
                    }
                    setVolume(sound);
                }
            }
   });
    }

    /**
     * 本地设置影院比例
     * @param ratioName
     */
    public void setSelectedRatio(String ratioName){
    }
    /**
     * 本地设置影院旋转
     * @param roteName
     */
    public void setSelectedRote(String roteName) {}
    /**
     * 本地设置场景2D，3D左右，3D上下
     */
    public void setSelectedLeftRightMode(int mode) {
    }
    /**
     * 本地设置场景平面，半球，球面
     */
    public void setSelectedRoundMode(int mode) {
    }

    /**
     *设置音轨列表
     * @param audioStreamList
     */
    public void setAudioStreamList(List<AudioSteamInfo.AudioStremItem> audioStreamList){
    }

    /**
     * 设置本地字幕列表
     * @param subtitleList
     */
    public void setSubtitleList(List<String> subtitleList){
    }

    /**
     * 显示字幕内容
     * @param content
     */
    public void showSubTitle(String content){

    }

    /**
     * 设置选择的音轨
     * @param index
     */
    public void setSelectedAudioStream(int index){}

    /**
     * 设置选择的字幕
     * @param name
     */
    public void setSelectedSubtitle(String name){}

    /**
     * 设置选择的解码方式
     * @param type
     */
    public void setSelectedDecodeType(int type){}

    /**
     * 设置选择的屏幕大小
     * @param type
     */
    public void setSelectedScreenSize(int type){}

    /**
     * 设置在线选片数据
     * @param contentInfos
     */
    public void setMovieSelectVideoDatas(List<ContentInfo> contentInfos){}

    /**
     * 设置本地选片数据
     * @param videoList
     */
    public void setLocalMovieSelectVideoDatas(List<LocalVideoBean> videoList, int curIndex){}

    /**
     * 显示续播对话框
     * @param contentInfos
     */
    public void showReplayDialog(List<ContentInfo> contentInfos,int currentIndex){}

    /**
     * 隐藏续播对话框
     */
    public void hideReplayDialog(){}

    /*  ---- start----- 遥控器按键事件回调（沉浸模式可在page中处理）*/

    @Override
    public boolean onZKeyDown(int keyCode) {
        return false;
    }

    @Override
    public void onZKeyUp(int keyCode) { }

    @Override
    public void onZKeyLongPress(int keyCode) {}

    @Override
    public void onConnStartCheck() {

        if (StickUtil.isBFMJ5Connection() && StickUtil.getJoystickConnect()) { //魔镜5代usb连接，并且遥控器连接上
//            titleBackView.connectManager(true);
        } else if (!StickUtil.blutoothEnble()) {// 蓝牙关闭
//            titleBackView.connectManager(false);
        } else if (!StickUtil.isBondBluetooth()) {// 蓝牙与魔镜设备未配对
//            titleBackView.connectManager(false);
        } else if (!StickUtil.isConnected()) {// 设备未开启或者设备休眠
//            titleBackView.connectManager(false);
        } else {// 已连接
//            titleBackView.connectManager(true);
        }
        LogHelper.d("login","----onConnStartCheck  isConnected = "+StickUtil.isConnected());
    }

    @Override
    public void onMotionTouch(MotionInputCallback.Event e) {

    }
    /*  ---- end----- 遥控器按键事件回调（沉浸模式可在page中处理）*/

    /**
     * 设置UI是否可见
     * @param visiable
     */
    public void setUIVisiable(boolean visiable){}
    public void onResum(){}

    public void onPause(){}

    public void onFinish(){
        JoystickUitlManager.getInstance().unBind(this);
    }

    public void prePage() {}

    public void nextPage() {}

    @Override
    public void setHDable(boolean isable) {
    }

    @Override
    public void setSelectedScreenLight(int size) {

    }

    @Override
    public void setSubtitleFontSize(int size) {

    }

    @Override
    public void hideDialogView() {

    }
    //是否有选片或选集
    public void setSelectSourceType(int type){}

    public void setResID(String resID){  }
    public void hideAllView(){}
    public void reSetReplayView(){}
}
