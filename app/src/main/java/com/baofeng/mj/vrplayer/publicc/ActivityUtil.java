package com.baofeng.mj.vrplayer.publicc;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.util.Log;

import com.baofeng.mj.vrplayer.activity.MainActivity;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by panxin on 2017/7/7.
 */

public class ActivityUtil {


    private static ActivityUtil mActivityUtil;

    public static ActivityUtil getInstance(){
        if(mActivityUtil == null){
            mActivityUtil = new ActivityUtil();
        }
        return mActivityUtil;
    }

    private List<Activity> activityList = new LinkedList<Activity>();

    /**
     * ------------下载业务逻辑    结束---------------
     */

    public void addActivity(Activity activity) {
        if (activity != null && activity != getCurrentActivity()) {
            activityList.add(activity);
        }
    }

    public void removeActivty(Activity activity) {
        if (activity != null) {
            activityList.remove(activity);
        }
    }

    public void exitAllActivity() {
        for (Activity activity : activityList) {
            if (activity != null) {
                activity.finish();
            }
        }
        activityList.clear();
    }


    /**
     * 获取当前的activity
     *
     * @return
     */
    public Activity getCurrentActivity() {
        if (activityList != null && activityList.size() > 0) {
            return activityList.get(activityList.size() - 1);
        }
        return null;
    }

    /**
     * 获取主activity
     *
     * @return
     */
    public Activity getMainActivity() {
        for (Activity activity : activityList) {
            if (activity instanceof MainActivity) {
                return activity;
            }
        }
        return null;
    }

    /**
     * 判断app是否处于后台运行
     * @param context
     * @return
     */
    public boolean isBackground(Context context) {
        try {
            ActivityManager activityManager = (ActivityManager) context
                    .getSystemService(Context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager
                    .getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
                if (appProcess.processName.equals(context.getPackageName())) {
                    if (appProcess.importance != ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                        return true;
                    } else {
                        return false;
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }
}
