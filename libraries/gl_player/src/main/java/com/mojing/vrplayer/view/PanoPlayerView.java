package com.mojing.vrplayer.view;

import android.app.Activity;
import android.opengl.Matrix;

import com.bfmj.viewcore.interfaces.IGLPlayer;
import com.bfmj.viewcore.render.GLScreenParams;
import com.bfmj.viewcore.view.GLPanoView;
import com.bfmj.viewcore.view.GLRootView;
import com.mojing.vrplayer.interfaces.IPlayerUICallback;

/**
 * Created by wanghongfang on 2017/5/5.
 * 全景播放器
 */
public class PanoPlayerView extends BasePlayerView {
    private GLRootView playerLayer;
    private Activity context;
    private GLQiuPlayer playerView;
    private float mInitLookAngle = 0;
    public PanoPlayerView(GLRootView playerLayer, Activity context, IPlayerUICallback mUICallback) {
        super(context,mUICallback,0);
        this.context = context;
        this.playerLayer = playerLayer;
        createPlayerView();
    }

    private void createPlayerView(){
        playerView = new GLQiuPlayer(context, null){
            @Override
            public void onBeforeDraw(boolean isLeft) {
                // 针对在线视频球模型增大可视范围 lixianke
                String path = mPlayUrl == null || mPlayUrl.isEmpty() ? "" : mPlayUrl.toLowerCase();
                if (getSceneType() == SCENE_TYPE_SPHERE && (path.startsWith("http") || path.startsWith("qstp"))){
                    float[] fM = getMatrixState().getVMatrix();
                    System.arraycopy(fM, 0, getMatrixState().getCurrentMatrix(), 0, 16);
                    rotate(mInitLookAngle, 0, 1, 0);

                    float[] vMatrix = new float[16];
                    Matrix.setLookAtM(vMatrix, 0, 0, 0, 45, 0f, 0.0f, -0.1f, 0f, 1.0f, 0.0f);
                    getMatrixState().setVMatrix(vMatrix);

                    float near = (float) (1 / Math.tan(Math.toRadians(88 / 2)));
                    GLScreenParams.setNear(near);
                }
                super.onBeforeDraw(isLeft);
                GLScreenParams.setNear(0.1f);
            }

            @Override
            public void onAfterDraw(boolean isLeft) {
                super.onAfterDraw(isLeft);
            }

            @Override
            public void onSurfaceCreated() {
                super.onSurfaceCreated();


            }

            @Override
            public void initDraw() {
                super.initDraw();
                if(getSurfaceTexture()==null){
//                    onSurfaceCreated();
                    playerLayer.queueEvent(new Runnable() {
                        @Override
                        public void run() {
                            setRenderType(GLPanoView.RENDER_TYPE_VIDEO);
                            setVisible(true);
                        }
                    });
                }

            }
        };
//        playerView.translate(6, 0, 0);
        playerView.setVisible(true);
        playerView.setListener(this);
        playerView.setChangeToSoftCallBack(this);
        playerLayer.addView(playerView);
    }

    @Override
    public void release(){
        super.release();
        if(context!=null) {
            if (playerView != null) {
                playerLayer.removeView(playerView);
                playerView.release();
                playerView = null;
            }

        }

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
    public void pausePlay() {
        if (playerView == null ) {
            return;
        }
        playerView.pause();

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
        if(context!=null) {
            if (playerView != null) {
                playerView.setVideoPath(path);
            }
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
    public void setPlayMode(int playMode){
        if(playerView!=null){
            playerView.setPlayType(playMode);
        }
    }

    @Override
    public void setSceneType(int sceneType){
        if(playerView!=null){
            playerView.setSceneType(sceneType);
        }
    }

    @Override
    public void setInit_Pov_head(int pov_head){
        super.setInit_Pov_head(pov_head);
        if(playerView!=null){
            //uv贴图偏移值
            playerView.rotate(pov_head, 0, 1, 0);
            mInitLookAngle = pov_head;
        }
    }

    @Override
    public void onPrepared(IGLPlayer player) {
        super.onPrepared(player);
        if(playerView!=null) {
            playerView.setVisible(true);
        }
    }

    @Override
    public void onCompletion(IGLPlayer player) {
        super.onCompletion(player);
        if(playerView!=null) {
            playerView.setVisible(false);
        }
    }
}
