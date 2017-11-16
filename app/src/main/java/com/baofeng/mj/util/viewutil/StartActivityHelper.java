package com.baofeng.mj.util.viewutil;

import android.content.Context;
import android.content.Intent;

import com.mojing.vrplayer.activity.MjVrPlayerActivity;
import com.mojing.vrplayer.bean.VideoModeType;
import com.mojing.vrplayer.publicc.ReportBusiness;
import com.mojing.vrplayer.publicc.bean.LocalVideoBean;

import java.util.List;

/**
 * Created by huangliang on 2017/9/27.
 */

public class StartActivityHelper {

    /**
     * 播放视频（smb）    gl播放器
     * @param mContext 上下文
     * @param name 名称
     * @param resourcePath 本地路径
     */
    public static void playVideoWithSMB(final Context mContext, final String name, int videoType, final String resourcePath, List<LocalVideoBean> videoList , int index){
        Intent intent = new Intent(mContext, MjVrPlayerActivity.class);
        intent.putExtra("type", VideoModeType.LOCAL_TYPE);
        intent.putExtra("videoPath",resourcePath);
        intent.putExtra("videoName",name);
        intent.putExtra("videoType",String.valueOf(videoType));
        intent.putExtra("pageType", ReportBusiness.PAGE_TYPE_LOCAL);
        intent.putExtra("play_mode", VideoModeType.PLAY_MODE_SIMPLE);
        intent.putExtra("index",index);
        intent.putExtra("local_menu_id","5");
        MjVrPlayerActivity.videoList = videoList;
        mContext.startActivity(intent);
    }
}
