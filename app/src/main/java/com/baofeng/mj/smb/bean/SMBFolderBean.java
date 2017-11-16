package com.baofeng.mj.smb.bean;

/**
 * Created by panxin on 2017/4/18.
 */

public class SMBFolderBean {

    public String filename;
    public String filepath;
    public int type;//0文件夹，1视频，2字幕，3其他文件
    public long size;
    public long modified;//文件时间(这里取创建时间)
}
