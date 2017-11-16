package com.mojing.vrplayer.publicc;

import com.mojing.vrplayer.interfaces.IMojingAppCallback;

/**
 * Created by panxin on 2017/7/7.
 */

public class MojingAppCallbackUtil {

    public static IMojingAppCallback mIMojingAppCallback;

    public static void setIMojingAppCallback(IMojingAppCallback listener){
        MojingAppCallbackUtil.mIMojingAppCallback = listener;
    }
}
