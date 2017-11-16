package com.mojing.vrplayer.page;

import android.content.Context;
import android.text.TextUtils;

import com.bfmj.viewcore.interfaces.IGLPlayer;
import com.bfmj.viewcore.util.GLExtraData;
import com.bfmj.viewcore.view.GLRectView;
import com.bfmj.viewcore.view.GLRelativeView;
import com.mojing.vrplayer.activity.GLBaseActivity;
import com.mojing.vrplayer.bean.VideoModeType;
import com.mojing.vrplayer.interfaces.ISkyBoxChangedCallBack;
import com.mojing.vrplayer.publicc.SettingSpBusiness;
import com.mojing.vrplayer.utils.GLConst;
import com.mojing.vrplayer.utils.MJGLUtils;
import com.mojing.vrplayer.utils.SkyboxManager;
import com.mojing.vrplayer.utils.ViewUtil;
import com.mojing.vrplayer.view.MoviePlayerView;

/**
 * Created by wanghongfang on 2017/5/15.
 * 本地非全景视频播放页面
 */
public class LocalMoviePlayPage extends BaseLocalPlayPage  implements ISkyBoxChangedCallBack {
    private static String VIDEO_SCREEN = "video_screen";
    GLRelativeView mPlayLayer;
    private GLRelativeView mMovieView;
    public LocalMoviePlayPage(Context context) {
        super(context, GLConst.LOCAL_MOVIE);
    }
    @Override
    public GLRectView createView(GLExtraData glExtraData) {
        super.createView(glExtraData);
        	/*切换播放场景*/
        int skyBoxtype = SettingSpBusiness.getInstance(mActivity).getSkyboxIndex();
        mActivity.showSkyBox(skyBoxtype);
        reSetDepthParams(skyBoxtype);
        SkyboxManager.getInstance().onBind(this);
        createPlayerView();
        initData(glExtraData);
        setLockScreen();
        return mRootView;
    }
    private void createPlayerView(){
        	/*中间的播放view*/
//        playerWidth = (int)MJGLUtils.GLUnitToPx(10);
//        playerWidth = 2400;
//        playerHeight = playerWidth*9/16;
        mPlayLayer = new GLRelativeView(mActivity);
        mPlayLayer.setLayoutParams(2400,2400);
        mPlayLayer.setHandleFocus(false);
        mMovieView = new GLRelativeView(mActivity);
        mMovieView.setId(VIDEO_SCREEN);
        mMovieView.setLayoutParams(playerWidth,
                playerHeight);
        mMovieView.setHandleFocus(false);
//        mMovieView.setX(playerLeft);
//        mMovieView.setY(1200-playerHeight/2);
        mMovieView.setMargin(1200-playerWidth/2,1200-playerHeight/2, 0, 0);
        playerView = new MoviePlayerView(mMovieView,mActivity,this);

        mPlayLayer.addView(mMovieView);
        mPlayLayer.setDepth(GLConst.Movie_Player_Depth,GLConst.Movie_Player_Scale);
      //  mPlayLayer.setEyeDeviation(0);
//        playerView.setDecodeType(decodeType);
        mRootView.addView(mPlayLayer);
        setCurPlayMode(mCurPlayMode);
    }

    @Override
    public void onFinish() {
        super.onFinish();
        SkyboxManager.getInstance().unBind(this);
    }

    @Override
    public void setCurPlayMode(int playMode) {
        super.setCurPlayMode(playMode);
        if(playMode==VideoModeType.PLAY_MODE_SIMPLE_FULL){
            mActivity.hideSkyBox();
        }else {
            mActivity.showSkyBox();
        }
        if(playerView!=null){
            ((MoviePlayerView)playerView).setSimpeVRMode(playMode);
        }
    }

    @Override
    public void onSkyBoxChanged(int type) {
        int curtype =  SettingSpBusiness.getInstance(mActivity).getSkyboxIndex();
        if(type==curtype)
            return;
        reSetDepthParams(type);
        switch (type){
            case GLBaseActivity.SCENE_TYPE_CINEMA:  //18
                switch (curtype){
                    case GLBaseActivity.SCENE_TYPE_HOME:
                        handleSkyBoxChange(3.6f);

                        break;
                    case GLBaseActivity.SCENE_TYPE_OUTCINEMA:

                        handleSkyBoxChange(2.5f);
                        break;
                }
                break;
            case GLBaseActivity.SCENE_TYPE_HOME:   //5
                switch (curtype){
                    case GLBaseActivity.SCENE_TYPE_CINEMA:
                        handleSkyBoxChange(0.28f);
                        break;
                    case GLBaseActivity.SCENE_TYPE_OUTCINEMA:
                        handleSkyBoxChange(0.7f);
                        break;
                }
                break;
            case GLBaseActivity.SCENE_TYPE_OUTCINEMA:  //7
                switch (curtype){
                    case GLBaseActivity.SCENE_TYPE_CINEMA:
                        handleSkyBoxChange(0.39f);
                        break;
                    case GLBaseActivity.SCENE_TYPE_HOME:
                        handleSkyBoxChange(1.4f);
                        break;

                }
                break;
        }

    }

    public void reSetDepthParams(int type){
        switch (type) {
            case GLBaseActivity.SCENE_TYPE_CINEMA: //18
                GLConst.Movie_Player_Depth = 18;
                GLConst.Movie_Player_Scale = 4.5f;
                break;
            case GLBaseActivity.SCENE_TYPE_HOME: //5
                GLConst.Movie_Player_Depth = 5;
                GLConst.Movie_Player_Scale = 1.25f;
                break;
            case GLBaseActivity.SCENE_TYPE_OUTCINEMA: //7
                GLConst.Movie_Player_Depth = 7;
                GLConst.Movie_Player_Scale = 1.75f;
                break;
        }
    }

    private void handleSkyBoxChange(float scale){
        if(mPlayLayer!=null) {
            mPlayLayer.setDepth(GLConst.Movie_Player_Depth,scale);
        }
    }

    /**
     * 设置画面比例
     * @param ratioName
     */
    @Override
    public void onRatioChange(String ratioName) {
        super.onRatioChange(ratioName);
        if(TextUtils.isEmpty(ratioName))
            return;
        if(playerView==null)
            return;
        mRatioType = ratioName;
        if(ratioName.contains(":")){ //按设置比例重置画面比例
            String[] sizes = ratioName.split(":");
            if(sizes==null||sizes.length<=0)
                return;
            float w  = Float.parseFloat(sizes[0]);
            float h = Float.parseFloat(sizes[1]);
            float aa = ((float) w)/h;
            int scren_w = (int)  ((MoviePlayerView) playerView).getPlayerWidth();
            int scren_h = (int) (scren_w /aa);
            ((MoviePlayerView) playerView).setVideoSize(ViewUtil.getDip(scren_w,GLConst.Movie_Player_Scale),ViewUtil.getDip(scren_h,GLConst.Movie_Player_Scale));
//            float sc_h = (scren_h-((MoviePlayerView) playerView).getPlayerTop()-((MoviePlayerView) playerView).getPlayerBottom());
            if(glvrPlayControlUI!=null) {
                glvrPlayControlUI.setSubtitleTextPosition(1200 + scren_h / 2);
            }
        }else {//原始画面
            if(playerView!=null){
                int width = playerView.getVideoWidth();
                int height = playerView.getVideoHeight();
                float aa = ((float) width)/height;
                int scren_w = (int)  ((MoviePlayerView) playerView).getPlayerWidth();
                int scren_h = (int) (scren_w /aa);
                ((MoviePlayerView) playerView).setVideoSize(ViewUtil.getDip(scren_w,GLConst.Movie_Player_Scale),ViewUtil.getDip(scren_h,GLConst.Movie_Player_Scale));
//                float h = (scren_h-((MoviePlayerView) playerView).getPlayerTop()-((MoviePlayerView) playerView).getPlayerBottom());
                if(glvrPlayControlUI!=null) {
                    glvrPlayControlUI.setSubtitleTextPosition(1200 + scren_h / 2);
                }
            }
        }

    }

    /**
     * 旋转画面
     * @param roteType 1:顺时针旋转  0：逆时针选择
     */
    @Override
    public void onRoteChange(int roteType) {
        super.onRoteChange(roteType);
        if(roteType== VideoModeType.Mode_CW_Rotate){//顺时针旋转
            mRotateAngle = (mRotateAngle+90);
        }else { //逆时针旋转
            mRotateAngle = (mRotateAngle-90);
        }
        mRotateAngle = mRotateAngle%360;
        if(playerView!=null){
            //代码效果与实际相反的 所以旋转角度取反
            ((MoviePlayerView) playerView).setRotate(-mRotateAngle);
        }
    }

    @Override
    public void onScreenSizeChange(int type) {
        super.onScreenSizeChange(type);
        if(playerView!=null){
            ((MoviePlayerView) playerView).setScreenSize(type,glvrPlayControlUI);

        }
    }

    @Override
    public void onPrepared(IGLPlayer player) {
        super.onPrepared(player);
        if(mCurPlayMode!=VideoModeType.PLAY_MODE_SIMPLE_FULL){
            int type = SettingSpBusiness.getInstance(mActivity).getPlayerScreenType();
            ((MoviePlayerView) playerView).setScreenSize(type,glvrPlayControlUI);
        }
    }

    @Override
    public void onPlayPrepared() {
        super.onPlayPrepared();
        MJGLUtils.exeGLQueueEvent(mActivity, new Runnable() {
            @Override
            public void run() {
                if(playerView!=null){
                    onRatioChange(mRatioType);
                    if(mRotateAngle!=((MoviePlayerView)playerView).getRoteAngle()) {
                        ((MoviePlayerView) playerView).setRotate(-mRotateAngle);
                    }
                }
            }
        });
    }


}
