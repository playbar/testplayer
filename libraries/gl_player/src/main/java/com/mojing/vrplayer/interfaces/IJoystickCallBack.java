package com.mojing.vrplayer.interfaces;


import com.mojing.vrplayer.publicc.MotionInputCallback;

/**
 * Created by wanghongfang on 2017/5/18.
 */
public interface IJoystickCallBack {
    public boolean onZKeyDown(int keyCode);
    public void onZKeyUp(int keyCode);
    public void onZKeyLongPress(int keyCode);
    public void onConnStartCheck();
    public void onMotionTouch(MotionInputCallback.Event event);
}
