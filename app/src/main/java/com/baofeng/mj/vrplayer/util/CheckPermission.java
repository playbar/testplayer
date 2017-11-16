package com.baofeng.mj.vrplayer.util;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;

import com.baofeng.mj.vrplayer.activity.ShadowPermissionActivity;

/**
 * Created by zhaominglei on 2016/7/13.
 * 权限管理
 */
public class CheckPermission {

    private final Context mContext;
    private String[] mPermissions;
    private PermissionListener mPermissionListener;

    public CheckPermission(Context context) {
        this.mContext = context;
    }

    public static CheckPermission from(Context context) {
        return new CheckPermission(context);
    }

    public CheckPermission setPermissions(String... permissions) {
        this.mPermissions = permissions;
        return this;
    }

    public CheckPermission setPermissionListener(PermissionListener listener) {
        this.mPermissionListener = listener;
        return this;
    }

    //android 6.0 以上
    @TargetApi(value = 23)
    public void check() {
        if(PermissionUtil.isOverMarshmallow()){
            ShadowPermissionActivity.setPermissionListener(mPermissionListener);
            Intent intent = new Intent(mContext, ShadowPermissionActivity.class);
            intent.putExtra(ShadowPermissionActivity.EXTRA_PERMISSIONS, mPermissions);
            mContext.startActivity(intent);
        }
    }
}
