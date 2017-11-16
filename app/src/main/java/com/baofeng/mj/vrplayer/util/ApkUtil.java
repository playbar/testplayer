package com.baofeng.mj.vrplayer.util;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;

/**
 * Created by liuchuanchi
 * apk工具类
 */
public class ApkUtil {
    /**
     * 获取PackageInfo
     *
     * @param filePath
     * @return
     */
    public static PackageInfo getPackageArchiveInfo(Context mContext, String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            return null;
        }
        return mContext.getPackageManager().getPackageArchiveInfo(filePath,
                PackageManager.GET_ACTIVITIES);
    }

    /**
     * 获取PackageInfo
     *
     * @param packageName
     * @return
     */
    public static PackageInfo getPackageInfo(Context mContext, String packageName) {
        if (TextUtils.isEmpty(packageName)) {
            return null;
        }
        try {
            return mContext.getPackageManager().getPackageInfo(packageName,
                    PackageManager.GET_ACTIVITIES);
        } catch (Exception e) {
//            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取PackageInfo、
     *
     * @param packageManager
     * @param packageName
     * @return
     */
    public static PackageInfo getPackageInfo(PackageManager packageManager, String packageName) {
        if (packageManager == null || TextUtils.isEmpty(packageName)) {
            return null;
        }
        try {
            return packageManager.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取apk版本号
     *
     * @param filePath
     * @return
     */
    public static int getVersionCodeByFilePath(Context mContext, String filePath) {
        PackageInfo packageInfo = getPackageArchiveInfo(mContext, filePath);
        if (packageInfo != null) {
            return packageInfo.versionCode;
        }
        return 0;
    }

    /**
     * 获取apk版本号
     *
     * @param packageName
     * @return
     */
    public static int getVersionCodeByPackageName(Context mContext, String packageName) {
        PackageInfo packageInfo = getPackageInfo(mContext, packageName);
        if (packageInfo != null) {
            return packageInfo.versionCode;
        }
        return 0;
    }

    public static int getVersionCode(Context mContext) {
        PackageInfo packageInfo = getPackageInfo(mContext, mContext.getPackageName());
        if (packageInfo != null) {
            return packageInfo.versionCode;
        }
        return 0;
    }

    public static String getVersionName(Context mContext) {
        PackageInfo packageInfo = getPackageInfo(mContext, mContext.getPackageName());
        if (packageInfo != null) {
            return packageInfo.versionName;
        }
        return "";
    }

    public static String getApkPackage(Context mContext, String path){
        PackageManager pm = mContext.getPackageManager();
        PackageInfo info = pm.getPackageArchiveInfo(path, PackageManager.GET_ACTIVITIES);
        ApplicationInfo appInfo = null;
        if (info != null) {
            appInfo = info.applicationInfo;
            return appInfo.packageName;
        }
       return null;

    }

    /**
     * 获取versionName的前缀  "4.0.0-4.00.0823.1112"
     */
    public static String getVersionNamePrefix(Context mContext) {
        String versionNamePrefix = null;
        PackageInfo packageInfo = getPackageInfo(mContext, mContext.getPackageName());
        if (packageInfo != null) {
            int index = packageInfo.versionName.indexOf("-");
            if (index >= 0) {
                versionNamePrefix = packageInfo.versionName.substring(0, index);
            }else{
                versionNamePrefix = packageInfo.versionName;
            }
        }
        return versionNamePrefix;
    }

    /**
     * 获取versionName的后缀  "4.0.0-4.00.0823.1112"
     */
    public static String getVersionNameSuffix(Context mContext) {
        String versionNameSuffix = null;
        PackageInfo packageInfo = getPackageInfo(mContext, mContext.getPackageName());
        if (packageInfo != null) {
            int index = packageInfo.versionName.indexOf("-");
            if (index >= 0) {
                versionNameSuffix = packageInfo.versionName.substring(index + 1);
            }else{
                versionNameSuffix = packageInfo.versionName;
            }
        }
        return versionNameSuffix;
    }
    /**
     * 获取versionName的中间部分（4.00.0823）  "4.0.0-4.00.0823.1112"
     */
    public static String getVersionNameMiddle(Context mContext){
        String versionNameMiddle = null;
        String versionName = getVersionNameSuffix(mContext);
        if (versionName != null) {
            int lastIndex = versionName.lastIndexOf(".");
            if (lastIndex > 0) {
                versionNameMiddle = versionName.substring(0, lastIndex);
            }else{
                versionNameMiddle = versionName;
            }
        }
        return versionNameMiddle;
    }
}
