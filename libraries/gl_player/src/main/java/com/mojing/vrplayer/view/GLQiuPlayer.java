package com.mojing.vrplayer.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.Surface;
import com.bfmj.viewcore.player.GLPanoPlayerView;
import com.bfmj.viewcore.view.GLRootView;
import com.mojing.vrplayer.interfaces.IPlayerChangeToSoftCallBack;
import com.mojing.vrplayer.utils.MediaHelp;
import com.storm.smart.play.baseplayer.BaseSurfacePlayer;
import com.storm.smart.play.call.BaofengPlayerListener;
import com.storm.smart.play.call.IBaofengPlayer;
import com.storm.smart.play.call.IBfPlayerConstant;


/**
 * Created by wangfuzheng on 2015/12/9.
 */
public class GLQiuPlayer extends GLPanoPlayerView {
    private Context mContext;
    private IPlayerChangeToSoftCallBack changeToSoftCallBack;
    public GLQiuPlayer(Context context, GLRootView mRootView) {
        super(context, mRootView);
        this.mContext = context;
//        if (MediaHelp.mPlayer == null) {
//            MediaHelp.createPlayer(mContext);
//        }
    }
    public void setChangeToSoftCallBack(IPlayerChangeToSoftCallBack callBack){
        this.changeToSoftCallBack = callBack;
    }

    @SuppressLint("NewApi")
    @Override
    protected boolean openVideo() {
        if (getPath() == null || getSurfaceTexture() == null) {
            return false;
        }

        try {
//            if(getPath().startsWith("https://")){
//                MediaHelp.decodeType = IBfPlayerConstant.IBasePlayerType.TYPE_SYS;
//            }
//            getRootView().initHeadView();
            if (MediaHelp.mPlayer == null) {
                MediaHelp.createPlayer(mContext,false);
            }
            getRootView().queueEvent(new Runnable() {
                @Override
                public void run() {
                    if(MediaHelp.mPlayer!=null) {
                        MediaHelp.mPlayer.setSurfaceTexture(getSurfaceTexture());
                        MediaHelp.mPlayer.setSurface(new Surface(getSurfaceTexture()));
                    }
                }
            });

            if(MediaHelp.mPlayer==null){
                return false;
            }
            MediaHelp.doSDKMedia(mContext,getPath());
            setListener();
            return true;
        } catch (IllegalArgumentException e) {
            handleError(e, 9001);
        } catch (SecurityException e) {
            handleError(e, 9002);
        } catch (IllegalStateException e) {
            handleError(e, 9003);
        } catch (Exception e) {
            handleError(e, 9004);
        }

        return false;
    }


    private void handleError(Exception e, final int errorCode) {
        reset();
        if (getContext() instanceof Activity) {
            ((Activity) getContext()).runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    playError(errorCode, 0);
                }
            });
        }
        e.printStackTrace();
    }


    @SuppressLint("NewApi")
    private void setListener() {
        MediaHelp.mPlayer.setListener(new BaofengPlayerListener() {
            @Override
            public void onPrepared(IBaofengPlayer iBaofengPlayer) {
                MediaHelp.mState = MediaHelp.STATE_PREPARED;
                int width = iBaofengPlayer.getVideoWidth();
                if(width == 0&&MediaHelp.decodeType != IBfPlayerConstant.IBasePlayerType.TYPE_SOFT){
                    MediaHelp.decodeType = IBfPlayerConstant.IBasePlayerType.TYPE_SOFT;
                    MediaHelp.errorToChangeSoft(mContext);
                    MediaHelp.mPlayer.setSurfaceTexture(getSurfaceTexture());
                    MediaHelp.mPlayer.setSurface(new Surface(getSurfaceTexture()));
                    setListener();
                    if(changeToSoftCallBack!=null){
                        changeToSoftCallBack.onChangeToSoft();
                    }
                }else{
                    playPrepared();
                }
            }

            @Override
            public void onCompletion(IBaofengPlayer iBaofengPlayer) {
                playCompletion();
            }

            @Override
            public void onError(IBaofengPlayer iBaofengPlayer, int what) {
                if (MediaHelp.decodeType != IBfPlayerConstant.IBasePlayerType.TYPE_SOFT) {
                    if(what == 10000){
                        MediaHelp.errorToChangeSys(mContext);
                        MediaHelp.mPlayer.setSurfaceTexture(getSurfaceTexture());
                        MediaHelp.mPlayer.setSurface(new Surface(getSurfaceTexture()));
                        setListener();
                        if(changeToSoftCallBack!=null){
                            changeToSoftCallBack.onChangeToSoft();
                        }
                    }else{
                        MediaHelp.errorToChangeSoft(mContext);
                        MediaHelp.mPlayer.setSurfaceTexture(getSurfaceTexture());
                        MediaHelp.mPlayer.setSurface(new Surface(getSurfaceTexture()));
                        setListener();
                        if(changeToSoftCallBack!=null){
                            changeToSoftCallBack.onChangeToSoft();
                        }
                    }
                } else {
                    playError(what, 0);
                }
            }

            @Override
            public void onInfo(IBaofengPlayer iBaofengPlayer, int what, Object extra) {
                if(what == 860){
                    if(MediaHelp.decodeType!=IBfPlayerConstant.IBasePlayerType.TYPE_SOFT){
                        pause();
                        MediaHelp.decodeType = IBfPlayerConstant.IBasePlayerType.TYPE_SOFT;
                        MediaHelp.errorToChangeSoft(mContext);
                        MediaHelp.mPlayer.setSurfaceTexture(getSurfaceTexture());
                        MediaHelp.mPlayer.setSurface(new Surface(getSurfaceTexture()));
                        setListener();
                        if(changeToSoftCallBack!=null){
                            changeToSoftCallBack.onChangeToSoft();
                        }
                    }else{
                        playInfo(what,extra);
                    }
                } else {
                    playInfo(what,extra);
                }
            }

            @Override
            public void onSeekToComplete(IBaofengPlayer iBaofengPlayer) {
                playSeekComplete();
            }

            @Override
            public boolean onSwitchPlayer(IBaofengPlayer iBaofengPlayer, Object o, int i) {
                return false;
            }

            @Override
            public String getCompleteUrl(String s) {
                return null;
            }

            @Override
            public boolean isCodecLibraryInstalled() {
                return false;
            }

            @Override
            public boolean canStart() {
                return false;
            }

            @Override
            public void onRawVideoDataUpdate() {

            }

            @Override
            public String getSite() {
                return null;
            }

            @Override
            public void onPlayerStop() {
            }

            @Override
            public void onP2pLocalToOnline() {

            }

            @Override
            public void onVideoInfo(int i, int i1, int i2, int i3, int i4, int i5, int i6) {

            }
        });

    }

    @Override
    public void reset() {
        if (MediaHelp.mPlayer == null || MediaHelp.mState == MediaHelp.STATE_IDLE) {
            return;
        }

        ((Activity)getContext()).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (MediaHelp.mPlayer != null) {
                    MediaHelp.mPlayer.release();
                }

                MediaHelp.createPlayer(mContext,false);
            }
        });
        MediaHelp.mState = MediaHelp.STATE_IDLE;
    }

    @Override
    public void start() {
        if (MediaHelp.mPlayer != null && MediaHelp.mState == MediaHelp.STATE_PREPARED) {
            try {
                MediaHelp.mPlayer.start();
            } catch (Exception e) {
            }

        }

        super.start();
    }

    @Override
    public void pause() {
        if (MediaHelp.mPlayer != null ) {
            MediaHelp.mPlayer.pause();
        }

        super.pause();
    }

    @Override
    public void stop() {
        if (MediaHelp.mPlayer != null) {
            MediaHelp.mPlayer.stop();
        }
    }

    @Override
    public void releasePlay() {

    }


    @Override
    public void seekTo(int pos) {
        if (MediaHelp.mPlayer != null) {
            try {
                if (!isPlaying()) {
                    MediaHelp.mPlayer.start();
                }
                MediaHelp.mPlayer.seekTo(pos);
            } catch (Exception e) {
            }
        }
    }

    @Override
    public int getCurrentPosition() {
        if (MediaHelp.mPlayer != null && MediaHelp.mState == MediaHelp.STATE_PREPARED) {
            try {
                return MediaHelp.mPlayer.getCurrentPosition();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return -1;
    }

    @Override
    public int getDuration() {
        if (MediaHelp.mPlayer != null && MediaHelp.mState == MediaHelp.STATE_PREPARED) {
            try {
                return MediaHelp.mPlayer.getDuration();
            } catch (Exception e) {
            }
        }

        return -1;
    }

    @Override
    public boolean isPlaying() {
        if (MediaHelp.mPlayer != null && MediaHelp.mState == MediaHelp.STATE_PREPARED) {
            try {
                return MediaHelp.mPlayer.isPlaying();
            } catch (IllegalStateException e) {
                return false;
            }

        }
        return false;
    }

    @Override
    public void onSurfaceCreated() {
        super.onSurfaceCreated();
        if (MediaHelp.mState == MediaHelp.STATE_IDLE) {
            openVideo();

        } else {
            try {
                MediaHelp.mPlayer.setSurfaceTexture(getSurfaceTexture());
                MediaHelp.mPlayer.setSurface(new Surface(getSurfaceTexture()));
            } catch (Exception e) {
                handleError(e, 9005);
            }

        }
    }

    public int getCurrentPlayStatus() {
        return MediaHelp.mState;
    }
    public int getAvgSpeed(){
        if(MediaHelp.mPlayer!=null){
            return MediaHelp.mPlayer.getAverageSpeed();
        }
        return 0;
    }


    public BaseSurfacePlayer getMediaPlayer() {
        if (MediaHelp.mPlayer != null)
            return MediaHelp.mPlayer;
        return null;
    }
}
