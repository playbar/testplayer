package com.mojing.vrplayer.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.Surface;

import com.bfmj.viewcore.view.GLPlayerView;
import com.bfmj.viewcore.view.GLRootView;
import com.mojing.vrplayer.interfaces.IPlayerChangeToSoftCallBack;
import com.mojing.vrplayer.utils.MediaHelp;
import com.storm.smart.play.baseplayer.BaseSurfacePlayer;
import com.storm.smart.play.call.BaofengPlayerListener;
import com.storm.smart.play.call.IBaofengPlayer;

public class GLMovieBasePlayer extends GLPlayerView {
    private Context mContext;
    private IPlayerChangeToSoftCallBack changeToSoftCallBack;
    public GLMovieBasePlayer(Context context, GLRootView mRootView) {
        super(context, mRootView);
        this.mContext = context;
//        if(MediaHelp.mPlayer == null) {
//            MediaHelp.createPlayer(this.mContext);
//        }
    }

    public void setChangeToSoftCallBack(IPlayerChangeToSoftCallBack callBack){
        this.changeToSoftCallBack = callBack;
    }

    @SuppressLint({"NewApi"})
    protected boolean openVideo() {
        if(this.getPath() != null && this.getSurfaceTexture() != null) {
            try {
                MediaHelp.mPlayer.setSurfaceTexture(this.getSurfaceTexture());
                MediaHelp.mPlayer.setSurface(new Surface(this.getSurfaceTexture()));
                MediaHelp.doSDKMedia(this.mContext, this.getPath());
                //设置3D音频相关参数
                float spread = 0.8f;
                MediaHelp.mPlayer.setStereoSpread3DAudio(spread);

                this.setListener();
                return true;
            } catch (IllegalArgumentException var2) {
                this.handleError(var2, 9001);
            } catch (SecurityException var3) {
                this.handleError(var3, 9002);
            } catch (IllegalStateException var4) {
                this.handleError(var4, 9003);
            } catch (Exception var5) {
                this.handleError(var5, 9004);
            }

            return false;
        } else {
            return false;
        }
    }

    private void handleError(Exception e, final int errorCode) {
        e.printStackTrace();
        this.reset();
        if(this.getContext() instanceof Activity) {
            ((Activity)this.getContext()).runOnUiThread(new Runnable() {
                public void run() {
                    GLMovieBasePlayer.this.playError(errorCode,0);
                }
            });
        }

        e.printStackTrace();
    }

    @SuppressLint({"NewApi"})
    private void setListener() {
        MediaHelp.mPlayer.setListener(new BaofengPlayerListener() {
            public void onPrepared(IBaofengPlayer iBaofengPlayer) {
                MediaHelp.mState = 2;
                int width = iBaofengPlayer.getVideoWidth();
                int height = iBaofengPlayer.getVideoHeight();
                float aa = ((float) width)/height;
                int scren_w = (int) GLMovieBasePlayer.this.getWidth();
                int Scren_h = (int) (scren_w /aa);
                GLMovieBasePlayer.this.setVideoSize(scren_w, Scren_h);
                if(width == 0 && MediaHelp.decodeType != 2) {
                    MediaHelp.decodeType = 2;
                    MediaHelp.errorToChangeSoft(GLMovieBasePlayer.this.mContext);
                    MediaHelp.mPlayer.setSurfaceTexture(GLMovieBasePlayer.this.getSurfaceTexture());
                    MediaHelp.mPlayer.setSurface(new Surface(GLMovieBasePlayer.this.getSurfaceTexture()));
                    GLMovieBasePlayer.this.setListener();
                    if(changeToSoftCallBack!=null){
                        changeToSoftCallBack.onChangeToSoft();
                    }
                } else {
                    GLMovieBasePlayer.this.playPrepared();
                }

            }

            public void onCompletion(IBaofengPlayer iBaofengPlayer) {
                GLMovieBasePlayer.this.playCompletion();
            }

            public void onError(IBaofengPlayer iBaofengPlayer, int what) {
                if(MediaHelp.decodeType != 2) {
                    MediaHelp.errorToChangeSoft(GLMovieBasePlayer.this.mContext);
                    MediaHelp.mPlayer.setSurfaceTexture(GLMovieBasePlayer.this.getSurfaceTexture());
                    MediaHelp.mPlayer.setSurface(new Surface(GLMovieBasePlayer.this.getSurfaceTexture()));
                    GLMovieBasePlayer.this.setListener();
                    if(changeToSoftCallBack!=null){
                        changeToSoftCallBack.onChangeToSoft();
                    }
                } else {
                    GLMovieBasePlayer.this.playError(what,0);
                }

            }

            public void onInfo(IBaofengPlayer iBaofengPlayer, int what, Object extra) {
                if(what == 860) {
                    if(MediaHelp.decodeType != 2) {
                        GLMovieBasePlayer.this.pause();
                        MediaHelp.decodeType = 2;
                        MediaHelp.errorToChangeSoft(GLMovieBasePlayer.this.mContext);
                        MediaHelp.mPlayer.setSurfaceTexture(GLMovieBasePlayer.this.getSurfaceTexture());
                        MediaHelp.mPlayer.setSurface(new Surface(GLMovieBasePlayer.this.getSurfaceTexture()));
                        GLMovieBasePlayer.this.setListener();
                        if(changeToSoftCallBack!=null){
                            changeToSoftCallBack.onChangeToSoft();
                        }
                    } else {
                        GLMovieBasePlayer.this.playInfo(what, extra);
                    }
                } else {
                    GLMovieBasePlayer.this.playInfo(what, extra);
                }

            }

            public void onSeekToComplete(IBaofengPlayer iBaofengPlayer) {
                GLMovieBasePlayer.this.playSeekComplete();
            }

            public boolean onSwitchPlayer(IBaofengPlayer iBaofengPlayer, Object o, int i) {
                return false;
            }

            public String getCompleteUrl(String s) {
                return null;
            }

            public boolean isCodecLibraryInstalled() {
                return false;
            }

            public boolean canStart() {
                return false;
            }

            public void onRawVideoDataUpdate() {
            }

            public String getSite() {
                return null;
            }

            public void onPlayerStop() {
            }

            public void onP2pLocalToOnline() {
            }

            public void onVideoInfo(int i, int i1, int i2, int i3, int i4, int i5, int i6) {
            }
        });
    }

    protected void reset() {
        if(MediaHelp.mPlayer != null && MediaHelp.mState != 0) {
            BaseSurfacePlayer var10000 = MediaHelp.mPlayer;
            BaseSurfacePlayer.reset();
            MediaHelp.mState = 0;
        }
    }

    public void start() {
        if(MediaHelp.mPlayer != null && MediaHelp.mState == 2) {
            try {
                MediaHelp.mPlayer.start();
            } catch (Exception var2) {
                var2.printStackTrace();
            }
        }

        super.start();
    }

    public void pause() {
        if(MediaHelp.mPlayer != null) {
            MediaHelp.mPlayer.pause();
        }

        super.pause();
    }

    public void stop() {
        if(MediaHelp.mPlayer != null) {
            MediaHelp.mPlayer.stop();
        }

    }

    public void releasePlay() {
    }

    public void seekTo(int pos) {
        if(MediaHelp.mPlayer != null) {
            try {
                if(!this.isPlaying()) {
                    MediaHelp.mPlayer.start();
                }
                MediaHelp.mPlayer.seekTo(pos);
            } catch (Exception var3) {
                var3.printStackTrace();
            }
        }

    }

    public int getCurrentPosition() {
        if(MediaHelp.mPlayer != null && MediaHelp.mState == 2) {
            try {
                return MediaHelp.mPlayer.getCurrentPosition();
            } catch (Exception var2) {
                var2.printStackTrace();
            }
        }

        return -1;
    }

    public int getDuration() {
        if(MediaHelp.mPlayer != null && MediaHelp.mState == 2) {
            try {
                return MediaHelp.mPlayer.getDuration();
            } catch (Exception var2) {
            }
        }

        return -1;
    }

    public boolean isPlaying() {
        if(MediaHelp.mPlayer != null && MediaHelp.mState == 2) {
            try {
                return MediaHelp.mPlayer.isPlaying();
            } catch (Exception var2) {
                return false;
            }
        } else {
            return false;
        }
    }

    public void onSurfaceCreated() {
        super.onSurfaceCreated();
        if(MediaHelp.mState == 0) {
            openVideo();

        } else {
            try {
                MediaHelp.mPlayer.setSurfaceTexture(this.getSurfaceTexture());
                MediaHelp.mPlayer.setSurface(new Surface(this.getSurfaceTexture()));
            } catch (Exception var2) {
                this.handleError(var2, 9005);
            }
        }

    }

    public int getCurrentPlayStatus() {
        return MediaHelp.mState;
    }

    public BaseSurfacePlayer getMediaPlayer() {
        return MediaHelp.mPlayer != null?MediaHelp.mPlayer:null;
    }

    public int getAvgSpeed(){
        if(MediaHelp.mPlayer!=null){
            return MediaHelp.mPlayer.getAverageSpeed();
        }
        return 0;
    }

    @Override
    public void onBeforeDraw(boolean isLeft) {
        //更新3D音频位置
        if (MediaHelp.mPlayer != null && MediaHelp.mState == MediaHelp.STATE_PREPARED) {
            MediaHelp.mPlayer.setHeadPosition3DAudio(getMatrixState().getVMatrix());
        }

        super.onBeforeDraw(isLeft);
    }
}
