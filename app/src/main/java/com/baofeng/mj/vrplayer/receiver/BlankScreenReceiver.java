package com.baofeng.mj.vrplayer.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.baofeng.mj.unity.IAndroidCallback;
import com.baofeng.mj.unity.UnityActivity;

/**
 * Created by dupengwei on 2017/2/27.
 */

public class BlankScreenReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if(null != intent && intent.getAction().equals("android.intent.action.PRE_SCREEN_OFF")){
            if (UnityActivity.INSTANCE != null) {
                IAndroidCallback iAndroidCallback = UnityActivity.INSTANCE.getIAndroidCallback();
                if (iAndroidCallback != null) {//通知Unity
                    iAndroidCallback.sendBlankScreen();
                }
            }
        }
    }
}
