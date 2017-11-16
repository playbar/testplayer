package com.baofeng.mj.util.publicutil;

/**
 * Created by huangliang on 2017/9/26.
 */

public class ConfigUrl {
    public static boolean IsOnline = true; //ChannelUtil.getChannelCode("ONLINE_OFFLINE").equals("1") ? true : false;
    public static String HELP_FEEDBACK_URL = "http://fuwu.mojing.cn/help";
    static{
            if(IsOnline){
                HELP_FEEDBACK_URL = "http://fuwu.mojing.cn/help";
            }else{
                HELP_FEEDBACK_URL = "http://192.168.12.62:8084/help";
            }
        }
}
