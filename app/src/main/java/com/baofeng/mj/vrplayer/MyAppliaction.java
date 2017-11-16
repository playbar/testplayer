package com.baofeng.mj.vrplayer;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.baofeng.mj.vrplayer.bean.LocalVideoBean;
import com.baofeng.mj.vrplayer.business.FileUploadBusiness;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Created by huangliang on 2017/8/15.
 */

public class MyAppliaction extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        instance=this;
    }
    private FileUploadBusiness mFileUploadBusiness;
    static MyAppliaction instance=null;
    public static  MyAppliaction getInstance(){
        return instance;
    }

    public void initFileUploadBusiness(Context context){
        mFileUploadBusiness=new FileUploadBusiness(context);
    }
    public FileUploadBusiness getmFileUploadBusiness(){
        if(mFileUploadBusiness==null){
            initFileUploadBusiness(MyAppliaction.getInstance());
        }
        return mFileUploadBusiness;
    }

    private boolean isPortraitOrLandscape = true; // ture:竖屏，false横屏

    public boolean getOrientationMode() {
        return isPortraitOrLandscape;
    }

    public void setOrientationMode(boolean flag) {
        isPortraitOrLandscape = flag;
    }

}
