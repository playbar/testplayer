package com.baofeng.mj.vrplayer.util;

/**
 * Created by liuchuanchi on 2016/7/19.
 * 单线程代理（数据库用）
 */
public class SqliteProxy extends SingleThreadProxy {
    private static SqliteProxy instance;//单例

    private SqliteProxy(){
    }

    public static SqliteProxy getInstance(){
        if(instance == null){
            instance = new SqliteProxy();
        }
        return instance;
    }
}
