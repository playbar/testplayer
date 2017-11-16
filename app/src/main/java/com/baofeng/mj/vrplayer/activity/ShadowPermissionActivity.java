package com.baofeng.mj.vrplayer.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.WindowManager;

import com.baofeng.mj.vrplayer.util.PermissionListener;
import com.baofeng.mj.vrplayer.util.PermissionUtil;

import java.util.ArrayList;
import java.util.List;

public class ShadowPermissionActivity extends Activity {

    public static final int REQ_CODE_PERMISSION_REQUEST = 1000;

    public static final String EXTRA_PERMISSIONS = "permissions";

    private String[] permissions;

    public static PermissionListener mPermissionListener;

    public static PermissionListener getPermissionListener() {
        return mPermissionListener;
    }

    public static void setPermissionListener(PermissionListener permissionListener) {
        ShadowPermissionActivity.mPermissionListener = permissionListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        setupFromSavedInstanceState(savedInstanceState);
        requestPermissions();
    }

    private void setupFromSavedInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            permissions = savedInstanceState.getStringArray(EXTRA_PERMISSIONS);
        } else {
            Bundle bundle = getIntent().getExtras();
            permissions = bundle.getStringArray(EXTRA_PERMISSIONS);
        }
    }

    @TargetApi(value = 23)
    public void requestPermissions() {
        List<String> needPermissions = PermissionUtil.findDeniedPermissions(this, permissions);
        if (needPermissions.isEmpty()) {
            permissionGranted();
        }else{
            ActivityCompat.requestPermissions(this, needPermissions.toArray(new String[needPermissions.size()]), REQ_CODE_PERMISSION_REQUEST);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putStringArray(EXTRA_PERMISSIONS, permissions);
        super.onSaveInstanceState(outState);
    }

    private void permissionGranted() {
        if (mPermissionListener != null) {
            mPermissionListener.permissionGranted();
            mPermissionListener = null;
        }
        finish();
        overridePendingTransition(0, 0);
    }

    private void permissionDenied(List<String> deniedpermissions) {
        if (mPermissionListener != null) {
            mPermissionListener.permissionDenied();
            mPermissionListener = null;
        }
        finish();
        overridePendingTransition(0, 0);
    }

    @TargetApi(value = 23)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        ArrayList<String> deniedPermissions = new ArrayList<String>();
        for (int i = 0; i < permissions.length; i++) {
            String permission = permissions[i];
            if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                deniedPermissions.add(permission);
            }
        }
        if (deniedPermissions.isEmpty()) {
            permissionGranted();
        } else {
            permissionDenied(deniedPermissions);
        }
    }
}
