package com.mojing.vrplayer.publicc.bean;

/**
 * Created by muyu on 2016/4/1.
 */
public class ResponseBaseBean<T> {
    public static final int SUCCESS = 0;
    public static final int FAIL = 1;
    public static final int TOKEN_INVALID = 6;

    private int status = -1; //状态码
    private String status_msg; //状态码的表述信息
    private String version; //当前返回结果的cms版本
    private int channel; ////当前返回结果的cms渠道
    private int date;  //服务器返回结果时的时间戳
    private String language; //当前返回结果的语言
    private int data_type; //0为横版，1为竖版
    private T data; //消息体

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getStatus_msg() {
        return status_msg;
    }

    public void setStatus_msg(String status_msg) {
        this.status_msg = status_msg;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public int getChannel() {
        return channel;
    }

    public void setChannel(int channel) {
        this.channel = channel;
    }

    public int getDate() {
        return date;
    }

    public void setDate(int date) {
        this.date = date;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public int getData_type() {
        return data_type;
    }

    public void setData_type(int data_type) {
        this.data_type = data_type;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ResponseBaseBean{" +
                "status=" + status +
                ", status_msg='" + status_msg + '\'' +
                ", version='" + version + '\'' +
                ", channel=" + channel +
                ", date=" + date +
                ", language='" + language + '\'' +
                ", data_type=" + data_type +
                ", data=" + data +
                '}';
    }
}
