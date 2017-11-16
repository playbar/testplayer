package com.mojing.vrplayer.publicc.bean;

import java.io.Serializable;

/**
 * Created by zhaominglei on 2016/8/17.
 */
public class ReportFromBean extends  ReportFromBaseBean implements Serializable, Cloneable {

    private static final long serialVersionUID = 7123961733436478384L;
    private String etype;
    private String tpos;
    private String pagetype;
    private String frompage;
    private String video_menu_id;
    private String appgame_menu_id;
    private String title;
    private String videoid;
    private String typeid;
    private String movieid;
    private String movietypeid;
    private String gameid;
    private String colid;
    private String coltitle;
    private String subcateid;
    private String subcatetitle;
    private String topicid;
    private String topictitle;
    private String curpage;


    public String getEtype() {
        return etype;
    }

    public void setEtype(String etype) {
        this.etype = etype;
    }

    public String getTpos() {
        return tpos;
    }

    public void setTpos(String tpos) {
        this.tpos = tpos;
    }

    public String getPagetype() {
        return pagetype;
    }

    public void setPagetype(String pagetype) {
        this.pagetype = pagetype;
    }

    public String getFrompage() {
        return frompage;
    }

    public void setFrompage(String frompage) {
        this.frompage = frompage;
    }

    public String getVideo_menu_id() {
        return video_menu_id;
    }

    public void setVideo_menu_id(String video_menu_id) {
        this.video_menu_id = video_menu_id;
    }

    public String getAppgame_menu_id() {
        return appgame_menu_id;
    }

    public void setAppgame_menu_id(String appgame_menu_id) {
        this.appgame_menu_id = appgame_menu_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getVideoid() {
        return videoid;
    }

    public void setVideoid(String videoid) {
        this.videoid = videoid;
    }

    public String getTypeid() {
        return typeid;
    }

    public void setTypeid(String typeid) {
        this.typeid = typeid;
    }

    public String getMovieid() {
        return movieid;
    }

    public void setMovieid(String movieid) {
        this.movieid = movieid;
    }

    public String getMovietypeid() {
        return movietypeid;
    }

    public void setMovietypeid(String movietypeid) {
        this.movietypeid = movietypeid;
    }

    public String getGameid() {
        return gameid;
    }

    public void setGameid(String gameid) {
        this.gameid = gameid;
    }

    public String getColid() {
        return colid;
    }

    public void setColid(String colid) {
        this.colid = colid;
    }

    public String getColtitle() {
        return coltitle;
    }

    public void setColtitle(String coltitle) {
        this.coltitle = coltitle;
    }

    public String getSubcateid() {
        return subcateid;
    }

    public void setSubcateid(String subcateid) {
        this.subcateid = subcateid;
    }

    public String getSubcatetitle() {
        return subcatetitle;
    }

    public void setSubcatetitle(String subcatetitle) {
        this.subcatetitle = subcatetitle;
    }

    public String getTopicid() {
        return topicid;
    }

    public void setTopicid(String topicid) {
        this.topicid = topicid;
    }

    public String getTopictitle() {
        return topictitle;
    }

    public void setTopictitle(String topictitle) {
        this.topictitle = topictitle;
    }

    public String getCurpage() {
        return curpage;
    }

    public void setCurpage(String curpage) {
        this.curpage = curpage;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
