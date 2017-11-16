package com.baofeng.mj.vrplayer.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.baofeng.mj.smb.view.CustomProgressDialog;
import com.baofeng.mj.unity.UnityPublicBusiness;
import com.baofeng.mj.vrplayer.MyAppliaction;
import com.baofeng.mj.vrplayer.publicc.ActivityUtil;
import com.baofeng.mj.vrplayer.publicc.AppContant;
import com.baofeng.mj.vrplayer.util.ReportUtil;

/**
 * activity基类
 */
public abstract class BaseActivity extends FragmentActivity {
    // loading对话框启动次数
    private int mProgressDialogCount = 0;
    // 自定义ProgressDialog
    private CustomProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyAppliaction.getInstance().setOrientationMode(true);
        ActivityUtil.getInstance().addActivity(this);
        initView();
        initData();
        initListener();
    }

    public abstract void initView();

    public abstract void initData();

    public abstract void initListener();

    @Override
    protected void onResume() {
        super.onResume();
        UnityPublicBusiness.setOrientationMode(true);
        if (AppContant.isRunningInBackground) {
            AppContant.isRunningInBackground = false;
            AppContant.comeInTime = System.currentTimeMillis();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (ActivityUtil.getInstance().isBackground(this)) {
            AppContant.isRunningInBackground = true;
            ReportUtil.reportTimer("1");
        } else {
            AppContant.isRunningInBackground = false;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityUtil.getInstance().removeActivty(this);
    }

    public CustomProgressDialog getProgressDialog() {
        checkProgressDialog();
        return mProgressDialog;
    }

    /**
     * 隐藏加载Dialog
     */
    public void dismissProgressDialog() {
        mProgressDialogCount--;
        if (mProgressDialogCount < 0) {
            mProgressDialogCount = 0;
        }
        if (mProgressDialog != null && mProgressDialog.isShowing()
                && mProgressDialogCount == 0) {

            try {//某些情况下，对话框未加载就被取消有可能抛出异常
                mProgressDialog.dismiss();
            } catch (Exception e) {}
        }
    }

    public void showProgressDialog(int resId) {
        showProgressDialog(getString(resId));
    }


    /**
     * 显示Loading对话框
     */
    public void showProgressDialog(String message) {
        mProgressDialogCount++;
        checkProgressDialog();
        mProgressDialog.setMessage(message);
        if (!mProgressDialog.isShowing()&&!isFinishing()) {
            mProgressDialog.show();
        }
    }

    private synchronized void checkProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new CustomProgressDialog(this);
            mProgressDialog.setCanceledOnTouchOutside(false);
        }
    }

    /**
     * 显示一个默认的Loading对话框
     */
    public void showProgressDialog() {
        showProgressDialog("正在加载……");
    }
}

