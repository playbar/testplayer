package com.baofeng.mj.unity.UnityHistoryBusiness;

/**
 * Created by liuchuanchi on 2016/7/19.
 * 单线程代理（播放历史用）
 */
public class HistoryProxy extends SingleThreadProxy {
    private static HistoryProxy instance;//单例

    private HistoryProxy(){
    }

    public static HistoryProxy getInstance(){
        if(instance == null){
            instance = new HistoryProxy();
        }
        return instance;
    }
}
