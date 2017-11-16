package com.mojing.vrplayer.view;

import android.app.Activity;
import android.text.TextUtils;

import com.bfmj.viewcore.interfaces.IGLPlayer;
import com.bfmj.viewcore.interfaces.IGLPlayerListener;
import com.mojing.vrplayer.bean.AudioSteamInfo;
import com.mojing.vrplayer.interfaces.IPlayerChangeToSoftCallBack;
import com.mojing.vrplayer.interfaces.IPlayerUICallback;
import com.mojing.vrplayer.utils.DataUtil;
import com.mojing.vrplayer.utils.MediaHelp;
import com.storm.smart.core.IProxy;
import com.storm.smart.core.URlHandleProxyFactory;
import com.storm.smart.domain.P2pInfo;
import com.storm.smart.play.call.IBfPlayerConstant;

import java.util.ArrayList;

/**
 * Created by wanghongfang on 2017/5/5.
 * 播放逻辑基类（子类有全景播放和非全景播放）
 */
public abstract class BasePlayerView implements IGLPlayerListener,IPlayerChangeToSoftCallBack {
    protected boolean isError = false;
    protected IProxy proxy;
    protected String path;
    protected String mPlayUrl;
    public enum PlayerState{  //记录播放状态  目前主要用在 播放还未加载成功前断开网络，再次连上网后需要根据State是否为PrePared的来判断重新创建player播放
        IDEL, PREPARED,COMPLETE,ERROR
    }
    public PlayerState mCurrentState = PlayerState.IDEL;
    public int mPrepareSeekTo=-1;
    protected IPlayerUICallback mUICallback;
    protected int decodeType = IBfPlayerConstant.IBasePlayerType.TYPE_SYSPLUS;
    protected Activity context;
    public boolean isPlayCompletion = false;
    public boolean isloacl = false;
    private int playerType; //1:影院 0：全景
    public BasePlayerView(Activity context,IPlayerUICallback mUICallback,int type){
        this.mUICallback = mUICallback;
        this.context = context;
        this.playerType = type;
    }

    public void createPlayer(){
        MediaHelp.decodeType = decodeType;
        context.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                MediaHelp.release();
                MediaHelp.mPlayer = null;
                MediaHelp.createPlayer(context,playerType==1?true:false);
            }
        });
    }

    public void setPlayUrl(String url){
        this.mPlayUrl = url;
        this.path = url;
        /**特殊格式用软解*/
        if (path.startsWith("qstp:")||path.startsWith("yun:") || path.startsWith("yunlive:")){
            decodeType = IBfPlayerConstant.IBasePlayerType.TYPE_SOFT;
        }
    }

    public String getPath(){
        return path;
    }

    //首次初始化完调用播放
    public void startPlayer() {
        if(TextUtils.isEmpty(path))
            return;
        if (path.startsWith("qstp:")) {  //qstp 格式的用p2p播放
            URlHandleProxyFactory.getIProxy(context, path);
            proxy = URlHandleProxyFactory.getInstance();
            path = P2pInfo.P2P_PLAY_SERVER_PATH;
        } else if (path.startsWith("yun:") || path.startsWith("yunlive:")) { //云视频
            URlHandleProxyFactory.getIProxy(context, path);
            proxy = URlHandleProxyFactory.getInstance();
            proxy.setcallback(new IProxy.UrlCallBack() {
                @Override
                public void mcallBack(String state) {
                    if (state != null && !"4".equals(state)) {
                        path = state;
                    }
                    setPath(path);

                }
            });
            if(proxy!=null){
                proxy.p2pStartPlay(path);
            }
        }
        setPath(path);
    }

    /**
     * 重播
     */
    public void rePlay(){
        this.path = mPlayUrl;
        if(TextUtils.isEmpty(mPlayUrl))
            return;
        if (mPlayUrl.startsWith("qstp:")||mPlayUrl.startsWith("yun:") || mPlayUrl.startsWith("yunlive:")){
            decodeType = IBfPlayerConstant.IBasePlayerType.TYPE_SOFT;//如果默认软解播可以设置这里
        }else {
            decodeType = IBfPlayerConstant.IBasePlayerType.TYPE_SYSPLUS;
        }
        MediaHelp.decodeType = decodeType;
        reSetPlay();

    }

    public void reSetPlay(){
        context.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                createPlayer();
                startPlayer();
            }
        });

    }

    public void destroyView() {
        context.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                MediaHelp.release();
                MediaHelp.mPlayer = null;
                mCurrentState = PlayerState.IDEL;
                MediaHelp.decodeType = IBfPlayerConstant.IBasePlayerType.TYPE_SYSPLUS;
                MediaHelp.mState = MediaHelp.STATE_IDLE;
                if(proxy!=null) {
                    proxy.p2pUninit();
                }
            }
        });

    }

    public void stopP2P(){
        if(proxy!=null)
            proxy.p2pStopPlay();
    }
    public void startP2P(String path){
        if(proxy!=null){
            proxy.p2pStartPlay(path);
        }
    }

    public  void seekTo(int current_pos){
        mPrepareSeekTo = current_pos;
        if (mUICallback != null) {
            mUICallback.showLoading();
        }
    }

    public void setPath(String path){
        if(mUICallback!=null) {
            mUICallback.showLoading();
        }
        isPlayCompletion = false;
        mCurrentState = PlayerState.IDEL;
    }
    //关闭
    public void disableSub(){
        if(MediaHelp.mPlayer!=null){
            MediaHelp.mPlayer.disableSub();
        }
    }
    //设置字幕选项
    public void setSubTitleIndex(int index){
        if(MediaHelp.mPlayer!=null){
            MediaHelp.mPlayer.setSubTitleIndex(index);
        }
    }
    //获取字幕text
    public String readSubInfo(){
        if(MediaHelp.mPlayer!=null){
            return MediaHelp.mPlayer.readSubInfo();
        }
        return "";
    }

    /**
     * 获取视频宽度
     * */
    public int getVideoWidth(){
        if(MediaHelp.mPlayer!=null){
          return   MediaHelp.mPlayer.getVideoWidth();
        }
        return 0;
    }

    /**
     * 获取视频高度
     * */
    public int getVideoHeight(){
        if(MediaHelp.mPlayer!=null){
            return   MediaHelp.mPlayer.getVideoHeight();
        }
        return 0;
    }

    /**
     * 获取本地音轨数据
     * @return
     */
    public  ArrayList<AudioSteamInfo.AudioStremItem> getAudioStreamInfos(){
        if(MediaHelp.mPlayer!=null){
            String info_json = MediaHelp.mPlayer.getAudioInfo();
           AudioSteamInfo infos = DataUtil.getgson(info_json,AudioSteamInfo.class);
            if(infos!=null){
                return infos.getStreams();
            }
            return null;
        }
        return null;
    }

    /**
     * 设置选择的音轨
     * @param index 索引值 (AudioSteamInfo中取到的index)
     */
    public void setAudioStream(final int index){
        context.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(MediaHelp.mPlayer!=null){
                    MediaHelp.mPlayer.setAudioStream(index);
                }
            }
        });


    }


    public  abstract void startPlay();
    public abstract void pausePlay();
    public abstract void setPlayMode(int mode);
    public abstract int getDuration();
    public abstract int getCurrentPosition();
    public abstract int getAvgSpeed();
    public abstract void setSceneType(int type);
    public void setInit_Pov_head(int pov_head){}

    /**
     * 播放监听
     */

        @Override
        public void onPrepared(IGLPlayer player) {
            isError = false;
            mCurrentState = PlayerState.PREPARED;
            MediaHelp.mPlayer.start();
            if(!isloacl) { //本地视频需要在onInfo字幕回调后再seek
                if (mPrepareSeekTo > 0) {
                    seekTo(mPrepareSeekTo);
                } else {
                    mPrepareSeekTo = -1;
                }
            }
            if(mUICallback!=null) {
                mUICallback.onPrepared(player);
            }
        }

        @Override
        public boolean onInfo(IGLPlayer player, int what, Object extra) {
            if(mUICallback!=null){
                mUICallback.onInfo(player,what,extra);
            }
            return false;
        }

        @Override
        public void onBufferingUpdate(IGLPlayer player, int percent) {

        }

        @Override
        public void onCompletion(IGLPlayer player) {
            if (isError) {
                return;
            }
            if(mUICallback!=null){
                mUICallback.onCompletion(player);
            }
        }

        @Override
        public void onSeekComplete(IGLPlayer player) {
            mPrepareSeekTo = -1;
            if(mUICallback!=null) {
                mUICallback.onSeekComplete(player);
            }

        }

        @Override
        public boolean onError(IGLPlayer player, int what, int extra) {
            mCurrentState = PlayerState.ERROR;
            if(mUICallback!=null){
                mUICallback.onError(player,what,extra);
            }
            return false;
        }

        @Override
        public void onVideoSizeChanged(IGLPlayer player, int width, int height) {

        }

        @Override
        public void onTimedText(IGLPlayer player, String text) {

        }

    public int getDecodeType() {
        return decodeType;
    }

    public void setDecodeType(int decodeType) {
        this.decodeType = decodeType;
    }

    public PlayerState getmCurrentState() {
        return mCurrentState;
    }

    public void setmCurrentState(PlayerState mCurrentState) {
        this.mCurrentState = mCurrentState;
    }

    public boolean isPlayCompletion() {
        return isPlayCompletion;
    }

    public void setPlayCompletion(boolean playCompletion) {
        isPlayCompletion = playCompletion;
    }

    public boolean isError() {
        return isError;
    }

    public void setError(boolean error) {
        isError = error;
    }

    public void release(){}
    public boolean isPlaying(){
        if(MediaHelp.mPlayer!=null){
           return MediaHelp.mPlayer.isPlaying();
        }
        return false;
    }
    @Override
    public void onChangeToSoft() {
       if(mUICallback!=null){
           mUICallback.onChangeToSoft();
       }
    }
}
