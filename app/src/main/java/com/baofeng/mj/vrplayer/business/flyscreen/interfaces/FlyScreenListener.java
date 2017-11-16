package com.baofeng.mj.vrplayer.business.flyscreen.interfaces;

/**
 * Created by zhaominglei on 2016/5/11.
 */
public interface FlyScreenListener {
    public void onMessageReceived(int type);
    public void onDataReceived(int type, Object obj);
}
