package com.baofeng.mj.vrplayer.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.BatteryManager;

import com.baofeng.mj.unity.IAndroidCallback;
import com.baofeng.mj.unity.UnityActivity;
import com.baofeng.mj.unity.UnityPublicBusiness;

/**
 * @author liuchuanchi
 * @description: 监听电池状态（温度,电量）
 */
public class BatteryStateReceiver extends BroadcastReceiver {
    private int batteryLevel;
    private int batterySum;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (null != intent) {
            UnityActivity.INSTANCE.setBatteryLevel(intent.getIntExtra(UnityPublicBusiness.BATTERY_LEVEL, 0));//电池电量
            UnityActivity.INSTANCE.setBatteryScale(intent.getIntExtra(UnityPublicBusiness.BATTERY_SCALE, 100));//电池最大电量
            UnityActivity.INSTANCE.setBatteryTemperature(intent.getIntExtra(UnityPublicBusiness.BATTERY_TEMPERATURE, 0));//电池温度
            UnityActivity.INSTANCE.setBatteryStatus(intent.getIntExtra(UnityPublicBusiness.BATTERY_STATUS, BatteryManager.BATTERY_STATUS_UNKNOWN));

            int status = intent.getIntExtra(UnityPublicBusiness.BATTERY_STATUS, android.os.BatteryManager.BATTERY_STATUS_UNKNOWN);
            batteryLevel = intent.getIntExtra(UnityPublicBusiness.BATTERY_LEVEL, 0);
            batterySum = intent.getIntExtra(UnityPublicBusiness.BATTERY_SCALE, 100);
            switch (status) {
                case android.os.BatteryManager.BATTERY_STATUS_CHARGING://充电
                case android.os.BatteryManager.BATTERY_STATUS_NOT_CHARGING://没充电
                case android.os.BatteryManager.BATTERY_STATUS_DISCHARGING://放电
                    if (UnityActivity.INSTANCE != null) {
                        IAndroidCallback iAndroidCallback = UnityActivity.INSTANCE.getIAndroidCallback();
                        if (iAndroidCallback != null) {//通知Unity
                            iAndroidCallback.sendBatteryChanged(status, batteryLevel, batterySum);//发送电池电量
                        }
                    }
                    break;
                case android.os.BatteryManager.BATTERY_STATUS_FULL://充满电
                    batteryLevel = 100;
                    if (UnityActivity.INSTANCE != null) {
                        IAndroidCallback iAndroidCallback = UnityActivity.INSTANCE.getIAndroidCallback();
                        if (iAndroidCallback != null) {//通知Unity
                            iAndroidCallback.sendBatteryChanged(status, batteryLevel, batterySum);//发送电池电量
                        }
                    }
                    break;
                case android.os.BatteryManager.BATTERY_STATUS_UNKNOWN://未知
                    /**
                     * unknow   一般表示状态切换  无法获取电量 使用之前值
                     */
                    if (UnityActivity.INSTANCE != null) {
                        IAndroidCallback iAndroidCallback = UnityActivity.INSTANCE.getIAndroidCallback();
                        if (iAndroidCallback != null) {//通知Unity
                            iAndroidCallback.sendBatteryChanged(status, batteryLevel, batterySum);
                        }
                    }
                    break;
            }
            batteryLevel = intent.getIntExtra(UnityPublicBusiness.BATTERY_LEVEL, 0);
            batterySum = intent.getIntExtra(UnityPublicBusiness.BATTERY_SCALE, 100);
            /**
             * 测试代码
             */
            /*if(intent.getAction().equals(Intent.ACTION_POWER_CONNECTED)){//充电2
                if (UnityActivity.INSTANCE != null) {
                    IAndroidCallback iAndroidCallback = UnityActivity.INSTANCE.getIAndroidCallback();
                    if (iAndroidCallback != null) {//通知Unity
                        iAndroidCallback.sendBatteryChanged(2, batteryLevel, batterySum);
                    }
                }
            }else if(intent.getAction().equals(Intent.ACTION_POWER_DISCONNECTED)){//没充电4
                if (UnityActivity.INSTANCE != null) {
                    IAndroidCallback iAndroidCallback = UnityActivity.INSTANCE.getIAndroidCallback();
                    if (iAndroidCallback != null) {//通知Unity
                        iAndroidCallback.sendBatteryChanged(4, batteryLevel, batterySum);
                    }
                }
            }*/

        }
    }
}
