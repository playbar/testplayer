package com.mojing.vrplayer.publicc;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.content.PermissionChecker;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhaominglei on 2016/7/13.
 */
public class PermissionUtil {

    public static final String[] ALL_PERMISSIONS = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.CAMERA};


    private PermissionUtil() {
    }

    public static boolean isOverMarshmallow() {
        return Build.VERSION.SDK_INT >= 23;
    }

    /**
     * Returns true 所有权限允许
     *
     * @param context     context
     * @param permissions permissions
     * @return
     */
    @TargetApi(value = 23)
    public static boolean hasSelfPermissions(Context context, String... permissions) {
        for (String permission : permissions) {
            if (isOverMarshmallow() && permission.equals(Manifest.permission.SYSTEM_ALERT_WINDOW)) {
                if (!Settings.canDrawOverlays(context)) {
                    return false;
                }
            } else if (isOverMarshmallow() && permission.equals(Manifest.permission.WRITE_SETTINGS)) {
                if (!Settings.System.canWrite(context)) {
                    return false;
                }
            } else if (PermissionChecker.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }


    @TargetApi(value = 23)
    public static List<String> findDeniedPermissions(Activity activity, String... permission) {
        List<String> denyPermissions = new ArrayList<String>();
        for (String value : permission) {
            if (isOverMarshmallow() && value.equals(Manifest.permission.SYSTEM_ALERT_WINDOW)) {
                if (!Settings.canDrawOverlays(activity)) {
                    denyPermissions.add(value);
                }
            } else if (isOverMarshmallow() && value.equals(Manifest.permission.WRITE_SETTINGS)) {
                if (!Settings.System.canWrite(activity)) {
                    denyPermissions.add(value);
                }
            } else if (PermissionChecker.checkSelfPermission(activity, value) != PackageManager.PERMISSION_GRANTED) {
                denyPermissions.add(value);
            }
        }
        return denyPermissions;
    }

    /**
     * 获取targetSdkVersion
     */
    public static int getTargetSdkVersion(Context context){
        int targetSdkVersion = 0;
        try {
            PackageManager packageManager = context.getPackageManager();
            String packageName = context.getPackageName();
            PackageInfo packageInfo = packageManager.getPackageInfo(packageName, 0);
            targetSdkVersion = packageInfo.applicationInfo.targetSdkVersion;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return targetSdkVersion;
    }

    /**
     * 检查权限
     */
    public static boolean checkSelfPermission(Context context, String permission){
        // For Android < Android M, self permissions are always granted.
        boolean result = true;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int targetSdkVersion = getTargetSdkVersion(context);
            if (targetSdkVersion >= Build.VERSION_CODES.M) {
                // targetSdkVersion >= Android M, we can use Context#checkSelfPermission
                result = context.checkSelfPermission(permission)
                        == PackageManager.PERMISSION_GRANTED;
            } else {
                // targetSdkVersion < Android M, we have to use PermissionChecker
                result = PermissionChecker.checkSelfPermission(context, permission)
                        == PermissionChecker.PERMISSION_GRANTED;
            }
        }
        return result;
    }
}