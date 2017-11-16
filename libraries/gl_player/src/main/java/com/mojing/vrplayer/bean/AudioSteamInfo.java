package com.mojing.vrplayer.bean;

import java.util.ArrayList;

/**
 * Created by wanghongfang on 2017/5/17.
 * 音轨数据
 */
public class AudioSteamInfo {
    public ArrayList<AudioStremItem> streams;

    public ArrayList<AudioStremItem> getStreams() {
        return streams;
    }

    public void setStreams(ArrayList<AudioStremItem> streams) {
        this.streams = streams;
    }

    public class AudioStremItem{
        /**
         * 如果 handler_name 为空 或者 default 就改为 「未知音轨」
         */
        public String handler_name; //名称
        public int index;   //索引

        public String getHandler_name() {
            return handler_name;
        }

        public void setHandler_name(String handler_name) {
            this.handler_name = handler_name;
        }

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }
    }
}
