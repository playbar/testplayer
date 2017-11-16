package com.mojing.vrplayer.publicc.bean;

import java.io.Serializable;

/**
 *
 * Created by muyu on 2016/5/21.
 */
public class PanoramaVideoAttrs implements Serializable{

    private int definition_id;
    private String definition_name;
    private String size;
    private String play_url;
    private String download_url;
    private String ball_url; //不为空 则使用球模型播放

    public String getBall_url() {
        return ball_url;
    }

    public void setBall_url(String ball_url) {
        this.ball_url = ball_url;
    }

    public int getDefinition_id() {
        return definition_id;
    }

    public void setDefinition_id(int definition_id) {
        this.definition_id = definition_id;
    }

    public String getDefinition_name() {
        return definition_name;
    }

    public void setDefinition_name(String definition_name) {
        this.definition_name = definition_name;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getPlay_url() {
        return play_url;
    }

    public void setPlay_url(String play_url) {
        this.play_url = play_url;
    }

    public String getDownload_url() {
        return download_url;
    }

    public void setDownload_url(String download_url) {
        this.download_url = download_url;
    }

    @Override
    public String toString() {
        return "PanoramaVideoAttrs{" +
                "definition_id=" + definition_id +
                ", definition_name='" + definition_name + '\'' +
                ", size='" + size + '\'' +
                ", play_url='" + play_url + '\'' +
                ", download_url='" + download_url + '\'' +
                '}';
    }
}
