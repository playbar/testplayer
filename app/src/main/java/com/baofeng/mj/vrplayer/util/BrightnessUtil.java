package com.baofeng.mj.vrplayer.util;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.provider.Settings;
import android.view.WindowManager;

import com.baofeng.mj.vrplayer.business.SpSettingBusiness;

/**
 * Created by liuchuanchi on 2016/5/12.
 * 亮度工具类
 */
public class BrightnessUtil {
    /**
     * 设置用户亮度值
     * @param brightnessValue 亮度值 0 - 255
     */
    public static void setUserBrightnessValue(final Activity activity, int brightnessValue) {
        if (brightnessValue < 0){
            brightnessValue = 0;
        } else if (brightnessValue > 255){
            brightnessValue = 255;
        }
        SpSettingBusiness.getInstance().setBrightnessValue(activity, brightnessValue);
        final int tempBrightnessValue = brightnessValue;
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
                lp.screenBrightness = tempBrightnessValue / 255f;
                activity.getWindow().setAttributes(lp);
            }
        });
    }
    public static int getScreenBrightness(Activity activity) {
        int nowBrightnessValue = 0;
        ContentResolver resolver = activity.getContentResolver();
        try {
            nowBrightnessValue = android.provider.Settings.System.getInt(
                    resolver, Settings.System.SCREEN_BRIGHTNESS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return nowBrightnessValue;
    }



    /**
     * 获取用户亮度值 0 - 255
     */
    public static int getUserBrightnessValue(Context mContext) {
        return SpSettingBusiness.getInstance().getBrightnessValue(mContext);
    }

    /**
     * 获取系统亮度值 0 - 255
     */
    public static int getSysBrightnessValue(Context mContext) {
        ContentResolver contentResolver = mContext.getContentResolver();
        return Settings.System.getInt(contentResolver, Settings.System.SCREEN_BRIGHTNESS, 255);
    }




    /**
     * 设置自适应亮度模式
     * @param isAutoBrightnessMode true打开，false关闭
     */
    public static void setAutoBrightnessMode(Context mContext, boolean isAutoBrightnessMode) {
        ContentResolver aContentResolver = mContext.getContentResolver();
        if(isAutoBrightnessMode){//打开
            Settings.System.putInt(aContentResolver,
                    Settings.System.SCREEN_BRIGHTNESS_MODE,
                    Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC);
        }else{//关闭
            Settings.System.putInt(aContentResolver,
                    Settings.System.SCREEN_BRIGHTNESS_MODE,
                    Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
        }
    }

    /**
     * true是自适应亮度模式，false不是
     */
    public static boolean isAutoBrightnessMode(Context mContext) {
        try {
            ContentResolver aContentResolver = mContext.getContentResolver();
            int brightnessMode = Settings.System.getInt(aContentResolver, Settings.System.SCREEN_BRIGHTNESS_MODE);
            return brightnessMode == Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC;
        } catch (Exception e) {
        }
        return false;
    }
}
