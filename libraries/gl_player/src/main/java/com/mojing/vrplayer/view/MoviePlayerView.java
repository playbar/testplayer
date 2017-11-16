package com.mojing.vrplayer.view;

import android.app.Activity;

import com.bfmj.viewcore.interfaces.IGLPlayer;
import com.bfmj.viewcore.render.GLColor;
import com.bfmj.viewcore.view.GLRelativeView;
import com.mojing.vrplayer.bean.VideoModeType;
import com.mojing.vrplayer.interfaces.IPlayerUICallback;
import com.mojing.vrplayer.publicc.SettingSpBusiness;
import com.mojing.vrplayer.utils.GLConst;
import com.mojing.vrplayer.utils.MJGLUtils;
import com.mojing.vrplayer.utils.MediaHelp;
import com.mojing.vrplayer.utils.ViewUtil;

/**
 * Created by wanghongfang on 2017/5/5.
 * 非全景播放器
 */
public class MoviePlayerView extends BasePlayerView {
    private GLRelativeView playerLayer;
    private GLMovieBasePlayer playerView;
    private int playerWidth = 1460;
    private int playerHeight = 821;
    private GLRelativeView playerTopLayer;
    private int curPlayMode=-1;
    public MoviePlayerView(GLRelativeView playerLayer, Activity context,IPlayerUICallback mUICallback){
        super(context,mUICallback,1);
        this.playerLayer = playerLayer;
        createPlayerView();
    }


    private void createPlayerView(){
        playerTopLayer = new GLRelativeView(context);
        playerTopLayer.setBackground(new GLColor(0x000000,0.85f));
        playerView = new GLMovieBasePlayer(context,null);
            float[] screen = new float[2];
            int type = SettingSpBusiness.getInstance(context).getPlayerScreenType();
            getScreenWH(screen, type);
            if (screen[0] > 0) {
                playerLayer.setMargin((1200-screen[0]/2),(1200-screen[1]/2),0,0);
                playerView.setLayoutParams(screen[0], screen[1]);
                playerView.setX(playerLayer.getX());
                playerView.setY(playerLayer.getY());
                playerTopLayer.setLayoutParams(screen[0], screen[1]);
                playerTopLayer.setX(playerLayer.getX()-10);
                playerTopLayer.setY(playerLayer.getY()-10);
            } else {
                playerTopLayer.setLayoutParams(playerWidth, playerHeight);
                playerView.setLayoutParams(playerWidth, playerHeight);
            }
        playerView.setListener(this);
       // playerView.setEyeDeviation(0);
        playerView.setVideoSize(playerWidth,playerHeight);
        playerView.setChangeToSoftCallBack(this);
        playerLayer.addView(playerView);
        playerLayer.addView(playerTopLayer);
        playerView.setFixed(playerLayer.isFixed());
        playerTopLayer.setVisible(false);
    }

    public void setSimpeVRMode(final int mode){
        MJGLUtils.exeGLQueueEvent(context, new Runnable() {
            @Override
            public void run() {
                if (mode == VideoModeType.PLAY_MODE_SIMPLE_FULL) {
                    if (playerView != null) {
                        playerView.setLayoutParams(2400, 2400);
                        playerView.setX(0);
                        playerView.setY(0);
                        playerView.setDepth(4);
                        int width = getVideoWidth();
                        int height = getVideoHeight();
                        float aa = ((float) width) / height;
                        int scren_w = (int) getPlayerWidth();
                        int scren_h = (int) (scren_w / aa);
                        if(scren_h<=0){
                            scren_h = (int)getPlayerHeight();
                        }
                        setVideoSize(scren_w, scren_h);
                        playerView.setFixed(true);

                    }
                } else {
                    if(curPlayMode!=-1&&curPlayMode!=VideoModeType.PLAY_MODE_SIMPLE_FULL){
                        return;
                    }
                    float[] screen = new float[2];
                    int type = SettingSpBusiness.getInstance(context).getPlayerScreenType();
                    getScreenWH(screen,type);
                    if(screen[0]>0){
                        playerLayer.setMargin(ViewUtil.getDip(1200-screen[0]/2, GLConst.Movie_Player_Scale),ViewUtil.getDip(1200-screen[1]/2, GLConst.Movie_Player_Scale),0,0);
                        playerView.setLayoutParams(ViewUtil.getDip(screen[0],GLConst.Movie_Player_Scale),ViewUtil.getDip(screen[1],GLConst.Movie_Player_Scale));
                        playerView.setX(playerLayer.getX());
                        playerView.setY(playerLayer.getY());

                    }else {
                        playerView.setLayoutParams(ViewUtil.getDip(playerWidth,GLConst.Movie_Player_Scale), ViewUtil.getDip(playerHeight,GLConst.Movie_Player_Scale));
                    }
                    playerView.setX(playerLayer.getX());
                    playerView.setY(playerLayer.getY());
                    playerView.setDepth(GLConst.Movie_Player_Depth);
                    setVideoSize(ViewUtil.getDip(playerView.getWidth(), GLConst.Movie_Player_Scale),ViewUtil.getDip( playerView.getHeight(), GLConst.Movie_Player_Scale));
                    playerView.setFixed(playerLayer.isFixed());
                }
                curPlayMode = mode;
            }
        });

    }


    @Override
    public void startPlay() {
        if (playerView == null) {
            return;
        }
        if (isPlayCompletion) {
            playerView.setVideoPath(path);
        }else {
            playerView.start();
        }

    }

    @Override
    public void pausePlay(){
        if (playerView == null) {
            return;
        }
        if(playerView!=null) {
            playerView.pause();
        }

    }

    @Override
    public void seekTo(final int current_pos) {
        super.seekTo(current_pos);

        ((Activity)context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(playerView!=null) {
                    try {
                        playerView.seekTo(current_pos);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });


    }

    @Override
    public void setPath(final String path) {
        super.setPath(path);
        if(playerView!=null) {
            playerView.setVideoPath(path);
        }

    }

    @Override
    public int getDuration() {
        if(playerView!=null){
            return playerView.getDuration();
        }
        return 0;
    }

    @Override
    public int getCurrentPosition() {
        if(playerView!=null){
            return playerView.getCurrentPosition();
        }
        return 0;
    }

    @Override
    public int getAvgSpeed(){
        if(playerView!=null){
            return playerView.getAvgSpeed();
        }
        return 0;
    }

    @Override
    public void setSceneType(int type) {
    }


    @Override
    public void setPlayMode(int playMode){
        if(playerView!=null){
            playerView.setPlayMode(playMode);
        }
    }

    /**
     * 设置屏幕大小
     * @param type
     */
    public void setScreenSize(final int type,final GLVRPlayControlUI glvrPlayControlUI){
        if(playerView==null)
            return;
        MJGLUtils.exeGLQueueEvent(context, new Runnable() {
            @Override
            public void run() {
                if(MediaHelp.mPlayer==null){
                    return;
                }
//                int width = MediaHelp.mPlayer.getVideoWidth();
//                int height = MediaHelp.mPlayer.getVideoHeight();
//                float aa = ((float) width)/height;
                float scren_w =playerWidth;
                float scren_h = playerHeight;
                float[] screen = new float[2];
                getScreenWH(screen,type);
                if(screen[0]>0) {
                    scren_w = screen[0];
                    scren_h = screen[1];
                }
                setVideoSize(ViewUtil.getDip(scren_w, GLConst.Movie_Player_Scale),ViewUtil.getDip( scren_h, GLConst.Movie_Player_Scale));
                SettingSpBusiness.getInstance(context).setPlayerScreenType(type);
                playerLayer.setMargin(ViewUtil.getDip(1200-scren_w/2, GLConst.Movie_Player_Scale),ViewUtil.getDip(1200-scren_h/2, GLConst.Movie_Player_Scale),0,0);
                playerView.setLayoutParams(ViewUtil.getDip(scren_w, GLConst.Movie_Player_Scale),ViewUtil.getDip(scren_h, GLConst.Movie_Player_Scale));
                playerView.setX(playerLayer.getX());
                playerView.setY(playerLayer.getY());
                if(glvrPlayControlUI!=null) {
                    glvrPlayControlUI.setSubtitleTextPosition(1200 + scren_h / 2);
                }
            }
        });


    }

    private void getScreenWH(float[] screen,int type){
        float rat = 1;
        if(playerView.getVideoHeight()>0) {
            rat = playerView.getVideoWidth() / playerView.getVideoHeight();
        }
        if(type==VideoModeType.Small_Screen){ //80%
            screen[0] = playerWidth*0.8f;
        }else if(type == VideoModeType.Large_Screen){  //120%
            screen[0] = playerWidth*1.2f;
        }else if(type == VideoModeType.Normal_Screen){ //100%
            screen[0] = playerWidth*1f;
        }else if(type == VideoModeType.Huge_Screen){  //140%
            screen[0] = playerWidth*1.4f;
        }else if(type == VideoModeType.IMAX_Screen){ //160%
            screen[0] = playerWidth*1.6f;
        }
        screen[1] = (int)( screen[0] /rat);
    }

    /**
     * 设置画面大小
     * @param width
     * @param height
     */
    public void setVideoSize(final int width,final int height){
        MJGLUtils.exeGLQueueEvent(context, new Runnable() {
            @Override
            public void run() {
                if(playerView!=null){
                    playerView.setVideoSize(width,height);
                }
            }
        });

    }

    /**
     * 设置画面旋转角度
     * @param angle
     */
    public void setRotate(int angle){
        if(playerView!=null){
            playerView.rotate(angle);
        }
    }

    /**
     * 获取旋转角度
     * @return
     */
    public int getRoteAngle(){
        if(playerView!=null){
            return playerView.getAngle();
        }
        return 0;
    }

    /**
     * 获取播放View 宽度
     * @return
     */
    public float getPlayerWidth(){
        if(playerView!=null){
            return playerWidth;
        }
        return 0;
    }

    public  float getPlayerHeight(){
        if(playerView!=null){
            return playerHeight;
        }
        return 0;
    }

    @Override
    public void release() {
        super.release();

        if(playerView!=null){
            playerView.release();
        }
    }

    @Override
    public void onPrepared(IGLPlayer player) {
        super.onPrepared(player);
        if(playerTopLayer!=null){
            playerTopLayer.setVisible(false);
        }

    }

    @Override
    public void onCompletion(IGLPlayer player) {
        super.onCompletion(player);
        if(playerTopLayer!=null){
            playerTopLayer.setLayoutParams(playerView.getVideoWidth()+20, playerView.getVideoHeight()+20);
            playerTopLayer.setX(playerView.getX()-10);
            playerTopLayer.setY(playerView.getY()-10);
            playerTopLayer.setVisible(true);
        }
    }

    public float getPlayerTop(){
        if(playerView!=null){
            return playerView.getPaddingTop();
        }
        return 0;
    }
    public float getPlayerBottom(){
        if(playerView!=null){
            return playerView.getPaddingBottom();
        }
        return 0;
    }

}
