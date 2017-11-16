package com.baofeng.mj.vrplayer.util;

import android.app.Activity;
import android.content.Context;
import android.os.Environment;
import android.os.storage.StorageManager;
import android.text.TextUtils;

import com.baofeng.mj.vrplayer.business.Constants;
import com.baofeng.mj.vrplayer.business.LocalVideoPathBusiness;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by liuchuanchi on 2016/5/5.
 * 存储工具类
 */
public class StorageUtil {
    private static String storageDir;//存储根路径

    /**
     * 获取存储根路径
     */
    public static String getStorageDir(Context mContext) {
        if (!TextUtils.isEmpty(storageDir)) {
            mkdirs(storageDir);
            return storageDir;
        }
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            storageDir = getExternalDir();//外部存储根路径
        } else {
            storageDir = getInternalCacheDir(mContext);//内部缓存根路径
        }
        mkdirs(storageDir);
        return storageDir;
    }

    /**
     * 获取外部存储根路径
     */
    public static String getExternalDir(){
        String externalDir = Environment.getExternalStorageDirectory().getAbsolutePath();
        StringBuilder sb = new StringBuilder();
        sb.append(externalDir).append("/").append(Constants.STORAGE_DIR).append("/");
        String filePath = sb.toString();
        mkdirs(filePath);
        return filePath;
    }

    /**
     * 获取外部缓存根路径
     */
    public static String getExternalCacheDir(Context mContext) {
        String cacheDir = Environment.getExternalStorageDirectory().getAbsolutePath();
        String packageName = mContext.getPackageName();
        StringBuilder sb = new StringBuilder();
        sb.append(cacheDir).append("/Android/data/").append(packageName).append("/")
          .append(Constants.STORAGE_DIR).append("/");
        String filePath = sb.toString();
        mkdirs(filePath);
        return filePath;
    }

    /**
     * 获取内部缓存根路径
     */
    public static String getInternalCacheDir(Context mContext){
        String cacheDir = Environment.getDataDirectory().getAbsolutePath();
        String packageName = mContext.getPackageName();
        StringBuilder sb = new StringBuilder();
        sb.append(cacheDir).append("/data/").append(packageName).append("/")
          .append(Constants.STORAGE_DIR).append("/");
        String filePath = sb.toString();
        mkdirs(filePath);
        return filePath;
    }

    /**
     * 获取手机内置，外置的存储根路径
     */
    public static String[] getAllStorageDir(Context mContext) {
        try {
            StorageManager mStorageManager = (StorageManager) mContext.getSystemService(Activity.STORAGE_SERVICE);
            Method mMethodGetPaths = mStorageManager.getClass().getMethod("getVolumePaths");
            return (String[]) mMethodGetPaths.invoke(mStorageManager);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 创建根目录
     */
    public static void mkdirs(String dirPath){
        File dirFile = new File(dirPath);
        if(!dirFile.exists()){
            dirFile.mkdirs();//mkdir()不能创建多个目录，所以要用mkdirs()
        }
    }

    public static String getDownloadDir(){
        String downloadPath = Environment.getExternalStorageDirectory().getAbsolutePath()+"/Download";
//        String downloadPath = getStorageDir(mContext)+"/download";
        mkdirs(downloadPath);
        return downloadPath;
    }

    /**
     * 获取飞屏字幕缓存路径
     * @return
     */
    public static String getMJFlyScreenSubFile(){
        return getExternalDir()+"FlyScreenSubtitle/";
    }
}
