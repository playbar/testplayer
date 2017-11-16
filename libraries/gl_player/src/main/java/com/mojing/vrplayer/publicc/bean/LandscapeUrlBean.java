package com.mojing.vrplayer.publicc.bean;

import java.io.Serializable;

/**
 * 对应横竖屏url
 * Created by muyu on 2016/5/12.
 */
public class LandscapeUrlBean implements Serializable{

    private String contents;
    private String nav;

    public String getContents() {
        return contents == null ? "" : contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public String getNav() {
        return nav == null ? "" : nav;
    }

    public void setNav(String nav) {
        this.nav = nav;
    }

    @Override
    public String toString() {
        return "{" +
                "contents='" + contents + '\'' +
                ", nav='" + nav + '\'' +
                '}';
    }
}
