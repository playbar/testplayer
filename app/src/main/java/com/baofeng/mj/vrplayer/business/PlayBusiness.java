package com.baofeng.mj.vrplayer.business;

import android.content.Context;
import android.content.Intent;

import com.baofeng.mj.vrplayer.bean.LocalVideoBean;
import com.google.gson.Gson;
import com.mojing.vrplayer.activity.MjVrPlayerActivity;
import com.mojing.vrplayer.bean.VideoModeType;
import com.mojing.vrplayer.publicc.CreateHistoryUtil;
import com.mojing.vrplayer.publicc.HistoryBusiness;
import com.mojing.vrplayer.publicc.ReportBusiness;
import com.mojing.vrplayer.publicc.SettingSpBusiness;
import com.mojing.vrplayer.publicc.bean.HistoryInfo;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liuchuanchi on 2017/7/6.
 * 播放业务
 */

public class PlayBusiness {
    /**
     * 播放本地视频
     */
    public static void playLocalVideo(Context context, List<LocalVideoBean> localVideoBeanList, int position, int videoType,boolean isFromUnity){
        if(MjVrPlayerActivity.videoList == null){
            MjVrPlayerActivity.videoList = new ArrayList();
        }
        MjVrPlayerActivity.videoList.clear();
        for(int i = 0;i<localVideoBeanList.size();i++){
            com.mojing.vrplayer.publicc.bean.LocalVideoBean bean = new com.mojing.vrplayer.publicc.bean.LocalVideoBean();
            bean.name = localVideoBeanList.get(i).name;
            bean.path = localVideoBeanList.get(i).path;
            bean.lastModify = localVideoBeanList.get(i).lastModify;
            bean.length = localVideoBeanList.get(i).length;
            bean.size = localVideoBeanList.get(i).size;
            bean.thumbPath = localVideoBeanList.get(i).thumbPath;
            MjVrPlayerActivity.videoList.add(bean);
        }
        LocalVideoBean localVideoBean = localVideoBeanList.get(position);
        refreshHistoryJson(localVideoBean.path,videoType);
        Intent intent = new Intent(context, MjVrPlayerActivity.class);
        intent.putExtra("type", VideoModeType.LOCAL_TYPE);
        intent.putExtra("videoPath", localVideoBean.path);
        intent.putExtra("videoName", localVideoBean.name);
        intent.putExtra("videoType", String.valueOf(videoType));//扫描后可得类型
        if(isFromUnity){
            if(SettingSpBusiness.getInstance(context).getUnityPlaySingleScreen()){
                intent.putExtra("play_mode", VideoModeType.PLAY_MODE_SIMPLE_FULL);
            }else{
                intent.putExtra("play_mode", VideoModeType.PLAY_MODE_SIMPLE);
            }
        }else{
            if(SettingSpBusiness.getInstance(context).getPlaySingleScreen()){
                intent.putExtra("play_mode", VideoModeType.PLAY_MODE_SIMPLE_FULL);
            }else{
                intent.putExtra("play_mode", VideoModeType.PLAY_MODE_SIMPLE);
            }
        }

        intent.putExtra("pageType", ReportBusiness.PAGE_TYPE_LOCAL);
        intent.putExtra("index", position);//该影片是列表中的第几个，需要计算
        intent.putExtra("local_menu_id", "1");
        intent.putExtra("isComeFromUnity",isFromUnity);
        context.startActivity(intent);
    }

    /**
     * 播放飞屏视频
     * @param videoPath 视频路径
     * @param videoName 视频名称
     * @param videoType 视频类型
     */
    public static void playFlyScreenVideo(Context context, String videoPath, String videoName, int videoType){
        if(MjVrPlayerActivity.videoList == null){
            MjVrPlayerActivity.videoList = new ArrayList();
        }
        MjVrPlayerActivity.videoList.clear();

        refreshHistoryJson(videoPath, videoType);
        Intent intent = new Intent(context, MjVrPlayerActivity.class);
        intent.putExtra("type", VideoModeType.LOCAL_TYPE);
        intent.putExtra("videoPath", videoPath);
        intent.putExtra("videoName", videoName);
        intent.putExtra("videoType", String.valueOf(videoType));//扫描后可得类型
        if(SettingSpBusiness.getInstance(context).getPlaySingleScreen()){
            intent.putExtra("play_mode", VideoModeType.PLAY_MODE_SIMPLE_FULL);
        }else{
            intent.putExtra("play_mode", VideoModeType.PLAY_MODE_SIMPLE);
        }
        intent.putExtra("pageType", ReportBusiness.PAGE_TYPE_LOCAL);
        intent.putExtra("index", 0);//该影片是列表中的第几个，需要计算
        intent.putExtra("local_menu_id", "3");
        context.startActivity(intent);
    }

    public static void refreshHistoryJson(String path,int type){
        final HistoryBusiness.VideoViewparam videoViewparam =  HistoryBusiness.JudgeVideoType(type);
        String history = HistoryBusiness.readFromHistory(path,0);
        HistoryInfo historyInfo = null;
        try {
            if(history!=null) {
                JSONObject myJsonObject = new JSONObject(history);
                historyInfo = CreateHistoryUtil.localJsonToHistoryInfo(myJsonObject);
                historyInfo.setVideo3dType(videoViewparam.mVideo3DType);
                historyInfo.setVideoType(videoViewparam._videoModelType);
                Gson gson = new Gson();
                String json = gson.toJson(historyInfo);
                HistoryBusiness.writeToHistory(json,path,0);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
