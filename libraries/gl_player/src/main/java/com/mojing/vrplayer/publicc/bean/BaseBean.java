package com.mojing.vrplayer.publicc.bean;

import java.io.Serializable;

/**
 * Created by liuchuanchi on 2016/5/13.
 * 资源信息基类
 */
public class BaseBean implements Serializable{
    private static final long serialVersionUID = 2650360814897585724L;
    private int type;//资源类型
    private String res_id;//资源id

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getRes_id() {
        return res_id;
    }

    public void setRes_id(String res_id) {
        this.res_id = res_id;
    }
}
