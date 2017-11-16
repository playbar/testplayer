package com.mojing.vrplayer.publicc.bean;

import java.io.Serializable;
import java.util.List;

/**
 * ResTypeUtil.res_type_video == 4情况，视频
 * Created by muyu on 2016/5/21.
 */
public class PanoramaVideoBean extends BaseBean implements Serializable{
    private static final long serialVersionUID = 859373839742937246L;
    private LandscapeUrlBean landscape_url;
    private String title;
    private String subtitle;
    private List<String> thumb_pic_url;
    private String desc;
    private int payment_type;
    private float payment_count;
    private int is_pay;
    private int headwear;
    private int download_count;
    private float score;
    private int score_count;
    private String source;
    private  int video_dimension;
    private int operation_type;//1只在线，2只下载，3两者都（默认播放）
    private int is_panorama;
    private int video_is_live;
    //TODO 暂不知List中类型
    private List extra;
    private int duration;
    private String download_url; //图片和漫游，直接用于下载
    private List<PanoramaVideoAttrs> video_attrs; //视频用于选择清晰度的下载列表
    private String subtitle_file;
    private List<String> screenshot;

    private int pov_heading; //初始视角
    private String size;

    private List<ContentInfo> rec_list; //201701XX版新添推荐

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public List<String> getThumb_pic_url() {
        return thumb_pic_url;
    }

    public void setThumb_pic_url(List<String> thumb_pic_url) {
        this.thumb_pic_url = thumb_pic_url;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getPayment_type() {
        return payment_type;
    }

    public void setPayment_type(int payment_type) {
        this.payment_type = payment_type;
    }

    public float getPayment_count() {
        return payment_count;
    }

    public void setPayment_count(float payment_count) {
        this.payment_count = payment_count;
    }

    public int getIs_pay() {
        return is_pay;
    }

    public void setIs_pay(int is_pay) {
        this.is_pay = is_pay;
    }

    public int getHeadwear() {
        return headwear;
    }

    public void setHeadwear(int headwear) {
        this.headwear = headwear;
    }

    public int getDownload_count() {
        return download_count;
    }

    public void setDownload_count(int download_count) {
        this.download_count = download_count;
    }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }

    public int getScore_count() {
        return score_count;
    }

    public void setScore_count(int score_count) {
        this.score_count = score_count;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public int getVideo_dimension() {
        return video_dimension;
    }

    public void setVideo_dimension(int video_dimension) {
        this.video_dimension = video_dimension;
    }

    public int getOperation_type() {
        return operation_type;
    }

    public void setOperation_type(int operation_type) {
        this.operation_type = operation_type;
    }

    public int getIs_panorama() {
        return is_panorama;
    }

    public void setIs_panorama(int is_panorama) {
        this.is_panorama = is_panorama;
    }

    public int getVideo_is_live() {
        return video_is_live;
    }

    public void setVideo_is_live(int video_is_live) {
        this.video_is_live = video_is_live;
    }

    public List getExtra() {
        return extra;
    }

    public void setExtra(List extra) {
        this.extra = extra;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public List<PanoramaVideoAttrs> getVideo_attrs() {
        return video_attrs;
    }

    public void setVideo_attrs(List<PanoramaVideoAttrs> video_attrs) {
        this.video_attrs = video_attrs;
    }

    public String getSubtitle_file() {
        return subtitle_file;
    }

    public void setSubtitle_file(String subtitle_file) {
        this.subtitle_file = subtitle_file;
    }

    public List<String> getScreenshot() {
        return screenshot;
    }

    public void setScreenshot(List<String> screenshot) {
        this.screenshot = screenshot;
    }

    public int getPov_heading() {
        return pov_heading;
    }

    public void setPov_heading(int pov_heading) {
        this.pov_heading = pov_heading;
    }

    public LandscapeUrlBean getLandscape_url() {
        return landscape_url;
    }

    public void setLandscape_url(LandscapeUrlBean landscape_url) {
        this.landscape_url = landscape_url;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getDownload_url() {
        return download_url;
    }

    public void setDownload_url(String download_url) {
        this.download_url = download_url;
    }

    public List<ContentInfo> getRec_list() {
        return rec_list;
    }

    public void setRec_list(List<ContentInfo> rec_list) {
        this.rec_list = rec_list;
    }

    @Override
    public String toString() {
        return "PanoramaVideoBean{" +
                "landscape_url=" + landscape_url +
                ", title='" + title + '\'' +
                ", subtitle='" + subtitle + '\'' +
                ", thumb_pic_url=" + thumb_pic_url +
                ", desc='" + desc + '\'' +
                ", payment_type=" + payment_type +
                ", payment_count=" + payment_count +
                ", is_pay=" + is_pay +
                ", headwear=" + headwear +
                ", download_count=" + download_count +
                ", score=" + score +
                ", score_count=" + score_count +
                ", source='" + source + '\'' +
                ", video_dimension=" + video_dimension +
                ", operation_type=" + operation_type +
                ", is_panorama=" + is_panorama +
                ", video_is_live=" + video_is_live +
                ", extra=" + extra +
                ", duration=" + duration +
                ", download_url='" + download_url + '\'' +
                ", video_attrs=" + video_attrs +
                ", subtitle_file='" + subtitle_file + '\'' +
                ", screenshot=" + screenshot +
                ", pov_heading=" + pov_heading +
                ", size='" + size + '\'' +
                ", rec_list=" + rec_list +
                '}';
    }
}
