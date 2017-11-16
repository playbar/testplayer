package com.mojing.vrplayer.publicc.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by hanyang on 2016/5/24.
 * 二级tab内容扩展字段
 */
public class AppExtraBean implements Serializable {

    private String package_name;
    private String filesize;
    private String filename;
    private String version_code;
    private String recommend_desc;
    private String download_count;
    private int product_type;
    private String h5_url;
    private String icon_url;

    //----------------------------201704XX新增可播放类型游戏
    private String video_url;
    private List<String> play_mode; //游戏类型 1.单手 2.双手 3.头控 4.触控 5.手势 6.体感
    private String cover_image;//视频封面
    //----------------------------以下是直播相关字段
    private String live_id;//主播id
    private String nickName;//主播昵称
    private String headImg;//主播头像
    private String videoUrl;//直播流地址
    private int status;//开播状态，0：休息，1：直播，2：回放
    private String reviewId;//回放id
    private String duration;//回放时长
    private String signature;//房间公告
    private String showId;//直播/房间id
    private String workTime;//开播时间
    private int onLineCount;//在线人数
    private int followerNumber;//关注人数
    private int level;//主播等级
    private int source_id;//来源id，1：小花秀，2：小熊VR
    private String source;//来源名称
    private String category;//

    public String getCover_image() {
        return cover_image;
    }

    public void setCover_image(String cover_image) {
        this.cover_image = cover_image;
    }

    public int getProduct_type() {
        return product_type;
    }

    public void setProduct_type(int product_type) {
        this.product_type = product_type;
    }

    public String getH5_url() {
        return h5_url;
    }

    public void setH5_url(String h5_url) {
        this.h5_url = h5_url;
    }

    public String getIcon_url() {
        return icon_url;
    }

    public void setIcon_url(String icon_url) {
        this.icon_url = icon_url;
    }

    public String getPackage_name() {
        return package_name;
    }

    public void setPackage_name(String package_name) {
        this.package_name = package_name;
    }

    public String getFilesize() {
        return filesize;
    }

    public void setFilesize(String filesize) {
        this.filesize = filesize;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getVersion_code() {
        return version_code;
    }

    public void setVersion_code(String version_code) {
        this.version_code = version_code;
    }

    public String getRecommend_desc() {
        return recommend_desc;
    }

    public void setRecommend_desc(String recommend_desc) {
        this.recommend_desc = recommend_desc;
    }

    public String getDownload_count() {
        return download_count;
    }

    public void setDownload_count(String download_count) {
        this.download_count = download_count;
    }
    //----------------------------------------------------------------------------------------------

    public String getVideo_url() {
        return video_url;
    }

    public void setVideo_url(String video_url) {
        this.video_url = video_url;
    }

    public List<String> getPlay_mode() {
        return play_mode;
    }

    public void setPlay_mode(List<String> play_mode) {
        this.play_mode = play_mode;
    }


    //----------------------------------------------------------------------------------------------


    public String getLive_id() {
        return live_id;
    }

    public void setLive_id(String live_id) {
        this.live_id = live_id;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getReviewId() {
        return reviewId;
    }

    public void setReviewId(String reviewId) {
        this.reviewId = reviewId;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getShowId() {
        return showId;
    }

    public void setShowId(String showId) {
        this.showId = showId;
    }

    public String getWorkTime() {
        return workTime;
    }

    public void setWorkTime(String workTime) {
        this.workTime = workTime;
    }

    public int getOnLineCount() {
        return onLineCount;
    }

    public void setOnLineCount(int onLineCount) {
        this.onLineCount = onLineCount;
    }

    public int getFollowerNumber() {
        return followerNumber;
    }

    public void setFollowerNumber(int followerNumber) {
        this.followerNumber = followerNumber;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getSource_id() {
        return source_id;
    }

    public void setSource_id(int source_id) {
        this.source_id = source_id;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
