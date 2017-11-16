package com.baofeng.mj.vrplayer.util;

/**
 * Created by liuchuanchi on 2016/7/19.
 * 单线程代理（本地视频用）
 */
public class LocalVideoProxy extends SingleThreadProxy {
    private static LocalVideoProxy instance;//单例

    private LocalVideoProxy(){
    }
    
    public static LocalVideoProxy getInstance(){
        if(instance == null){
            instance = new LocalVideoProxy();
        }
        return instance;
    }
}
