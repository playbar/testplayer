package com.mojing.vrplayer.publicc.bean;

import java.io.Serializable;

/**
 * Created by hanyang on 2016/7/7.\
 * 用于兼容数据类型,进行转化
 */
public class ContentBaseBean implements Serializable {
    private String layout_type;

    public String getLayout_type() {
        return layout_type;
    }

    public void setLayout_type(String layout_type) {
        this.layout_type = layout_type;
    }
}
