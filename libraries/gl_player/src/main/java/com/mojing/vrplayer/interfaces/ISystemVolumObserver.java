package com.mojing.vrplayer.interfaces;

/**
 * Created by wanghongfang on 2016/8/12.
 * 通知系统音量变化的观察者
 */
public interface ISystemVolumObserver {
    void NotifyVolumChanged(int keyCode);
}
