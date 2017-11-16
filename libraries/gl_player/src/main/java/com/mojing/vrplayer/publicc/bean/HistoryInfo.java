package com.mojing.vrplayer.publicc.bean;

import java.io.Serializable;

/**
 * Created by liuchuanchi on 2016/5/18.
 * 历史信息
 */
public class HistoryInfo implements Serializable{
    private static final long serialVersionUID = -1686494324560658330L;
    private int type;//类型，0本地历史，1在线历史
    private String videoId;//视频id
    private String videoTitle;//视频名称（眉飞色舞）
    private String videoImg;//视频缩略图
    private String videoPlayUrl;//视频播放url
    private int videoSet;//视频集数
    private int lastSetIndex;//上次播放第几集
    private int videoType;//视频类型(Rect = 1, Sphere360, Sphere180, Box)
    private int video3dType;//视频3d类型 视频3d类型( 1:2d 2:上下3d  3:左右3d )

    private int totalDuration;//视频总时长（精确到秒）
    private int playDuration;//视频播放进度（精确到秒）
    private int playFinished;//是否播完
    private long playTimestamp;//播放时的系统时间戳（精确到秒）
    private int playType;//播放类型
    private String videoClarity;//视频清晰度
    private int resType;//资源类型 （1：普通视频  ，4：全景视频）
    private String detailUrl;//资源详情页url
    private int audioTrack;//音轨
    private String viewRatio; //播放画面比例
    private int viewRotate; //播放画面旋转角度
    private String subtitle; //字幕

    public int getAudioTrack() {
        return audioTrack;
    }

    public void setAudioTrack(int audioTrack) {
        this.audioTrack = audioTrack;
    }

    public String getVideoClarity() {
        return videoClarity;
    }

    public void setVideoClarity(String videoClarity) {
        this.videoClarity = videoClarity;
    }

    public int getResType() {
        return resType;
    }

    public void setResType(int resType) {
        this.resType = resType;
    }

    public String getDetailUrl() {
        return detailUrl;
    }

    public void setDetailUrl(String detailUrl) {
        this.detailUrl = detailUrl;
    }

    public int getPlayType() {
        return playType;
    }

    public void setPlayType(int playType) {
        this.playType = playType;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public String getVideoTitle() {
        return videoTitle;
    }

    public void setVideoTitle(String videoTitle) {
        this.videoTitle = videoTitle;
    }

    public String getVideoImg() {
        return videoImg;
    }

    public void setVideoImg(String videoImg) {
        this.videoImg = videoImg;
    }

    public String getVideoPlayUrl() {
        return videoPlayUrl;
    }

    public void setVideoPlayUrl(String videoPlayUrl) {
        this.videoPlayUrl = videoPlayUrl;
    }

    public int getVideoSet() {
        return videoSet;
    }

    public void setVideoSet(int videoSet) {
        this.videoSet = videoSet;
    }

    public int getLastSetIndex() {
        return lastSetIndex;
    }

    public void setLastSetIndex(int lastSetIndex) {
        this.lastSetIndex = lastSetIndex;
    }

    public int getVideo3dType() {
        return video3dType;
    }

    public void setVideo3dType(int video3dType) {
        this.video3dType = video3dType;
    }

    public int getTotalDuration() {
        return totalDuration;
    }

    public void setTotalDuration(int totalDuration) {
        this.totalDuration = totalDuration;
    }

    public int getPlayDuration() {
        return playDuration;
    }

    public void setPlayDuration(int playDuration) {
        this.playDuration = playDuration;
    }

    public int getPlayFinished() {
        return playFinished;
    }

    public void setPlayFinished(int playFinished) {
        this.playFinished = playFinished;
    }

    public long getPlayTimestamp() {
        return playTimestamp;
    }

    public void setPlayTimestamp(long playTimestamp) {
        this.playTimestamp = playTimestamp;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getVideoType() {
        return videoType;
    }

    public void setVideoType(int videoType) {
        this.videoType = videoType;
    }

    public String getViewRatio() {
        return viewRatio;
    }

    public void setViewRatio(String viewRatio) {
        this.viewRatio = viewRatio;
    }

    public int getViewRotate() {
        return viewRotate;
    }

    public void setViewRotate(int viewRotate) {
        this.viewRotate = viewRotate;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    @Override
    public String toString() {
        return "HistoryInfo{" +
                "type=" + type +
                ", videoId='" + videoId + '\'' +
                ", videoTitle='" + videoTitle + '\'' +
                ", videoImg='" + videoImg + '\'' +
                ", videoPlayUrl='" + videoPlayUrl + '\'' +
                ", videoSet=" + videoSet +
                ", lastSetIndex=" + lastSetIndex +
                ", videoType=" + videoType +
                ", video3dType=" + video3dType +
                ", totalDuration=" + totalDuration +
                ", playDuration=" + playDuration +
                ", playFinished=" + playFinished +
                ", playTimestamp=" + playTimestamp +
                ", playType=" + playType +
                ", videoClarity='" + videoClarity + '\'' +
                ", resType=" + resType +
                ", detailUrl='" + detailUrl + '\'' +
                ", audioTrack=" + audioTrack +
                ", viewRatio='" + viewRatio + '\'' +
                ", viewRotate=" + viewRotate +
                ", subtitle='" + subtitle + '\'' +
                '}';
    }
}
