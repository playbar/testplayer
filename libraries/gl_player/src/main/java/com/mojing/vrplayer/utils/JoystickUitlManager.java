package com.mojing.vrplayer.utils;

import com.mojing.vrplayer.interfaces.IJoystickCallBack;
import com.mojing.vrplayer.publicc.MotionInputCallback;

import java.util.ArrayList;

/**
 * Created by wanghongfang on 2017/5/18.
 * 通知UI层手柄连接状态
 */
public class JoystickUitlManager {
    public static JoystickUitlManager mInstance;
    private ArrayList<IJoystickCallBack> mCallList = new ArrayList<>();
    private JoystickUitlManager(){

    }
    public static JoystickUitlManager getInstance(){
        if(mInstance==null){
            mInstance = new JoystickUitlManager();
        }
        return mInstance;
    }

    public synchronized void onBind(IJoystickCallBack callBack){
        if(mCallList!=null){
            mCallList.add(callBack);
        }
    }

    public synchronized void unBind(IJoystickCallBack callBack){
        if(mCallList!=null&&mCallList.contains(callBack)){
            mCallList.remove(callBack);
        }
    }


    public synchronized boolean onZKeyDown( int keycode){
        if(mCallList==null || mCallList.size()<=0)
            return false;
        boolean flag = false;
        for(IJoystickCallBack callBack:mCallList){
            boolean f = callBack.onZKeyDown(keycode);
            if(!flag){
                flag = f;
            }
        }
        return flag;
    }

    public synchronized void onZKeyUp(int keycode){
        if(mCallList==null || mCallList.size()<=0)
            return;
        for(IJoystickCallBack callBack:mCallList){
            callBack.onZKeyUp(keycode);
        }
    }

    public synchronized void onZKeyLongPress(int keycode){
        if(mCallList==null || mCallList.size()<=0)
            return;
        for(IJoystickCallBack callBack:mCallList){
            callBack.onZKeyLongPress(keycode);
        }
    }

    public synchronized void onConnStartCheck(){
        if(mCallList==null || mCallList.size()<=0)
            return;
        for(IJoystickCallBack callBack:mCallList){
            callBack.onConnStartCheck();
        }
    }

    public synchronized void onMotionTouch(MotionInputCallback.Event event) {
        if(mCallList==null || mCallList.size()<=0)
            return;
        for(IJoystickCallBack callBack:mCallList){
            callBack.onMotionTouch(event);
        }
    }
}
