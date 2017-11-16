package com.mojing.vrplayer.utils;

import android.app.Activity;
import android.content.Context;

import com.bfmj.sdk.util.MD5;
import com.storm.smart.play.baseplayer.BaseSurfacePlayer;
import com.storm.smart.play.call.IBfPlayerConstant;
import com.storm.smart.play.call.PlayerWithoutSurfaceFactory;

public class MediaHelp {
    public static BaseSurfacePlayer mPlayer;

    public static final int STATE_IDLE = 0;
    public static final int STATE_PREPARING = 1;
    public static final int STATE_PREPARED = 2;
    public static int mState = STATE_IDLE;

    public static int decodeType = IBfPlayerConstant.IBasePlayerType.TYPE_SYSPLUS;
    private static String videoPath;
    private static boolean isOpen3D = false; //只有影院下才开启

    public static void doSDKMedia(Context mContext, String videoPath) {
        try {
            MediaHelp.videoPath = videoPath;
            mPlayer.setVideoID(MD5.getMD5(MediaHelp.videoPath));
            if (!mPlayer.setVideoPath(MediaHelp.videoPath)) {
                errorToChangeSoft(mContext);
            } 
            mState = STATE_PREPARING;
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static BaseSurfacePlayer createPlayer(Context mContext,boolean isopen3d) {
        release();
        isOpen3D = isopen3d;
        if (mPlayer == null) {
            mPlayer = PlayerWithoutSurfaceFactory.createPlayer((Activity) mContext, decodeType,isopen3d);
        }
        return mPlayer;
    }

    /**
     * 播放失败切换到软解
     */
    public static void errorToChangeSoft(Context mContext) {
        decodeType = IBfPlayerConstant.IBasePlayerType.TYPE_SOFT;
        release();
        createPlayer(mContext,isOpen3D);
        doSDKMedia(mContext, MediaHelp.videoPath);
    }

    public static void errorToChangeSys(Context mContext) {
        decodeType = IBfPlayerConstant.IBasePlayerType.TYPE_SYS;
        release();
        createPlayer(mContext,isOpen3D);
        doSDKMedia(mContext, MediaHelp.videoPath);
    }


    /**
     * MediaPlayer release
     */
    public static void release() {
        mState = STATE_IDLE;
        if (mPlayer != null) {
            mPlayer.release();
            mPlayer = null;
        }
    }

}
