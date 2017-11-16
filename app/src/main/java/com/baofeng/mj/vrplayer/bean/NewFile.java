package com.baofeng.mj.vrplayer.bean;

import java.io.Serializable;

/**
 * Created by liuchuanchi on 2016/5/10.
 * 新的文件实体类
 */
public class NewFile implements Serializable {
    private static final long serialVersionUID = -2906663245222306316L;
    private String name;//文件名
    private String path;//文件路径

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}