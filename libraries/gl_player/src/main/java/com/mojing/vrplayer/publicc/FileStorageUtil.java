package com.mojing.vrplayer.publicc;

import android.app.Activity;
import android.os.Environment;
import android.os.StatFs;
import android.os.storage.StorageManager;
import android.text.TextUtils;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by liuchuanchi on 2016/5/5.
 * 文件存储工具类
 */
public class FileStorageUtil {
    private static String mojingDir;//mojing存储路径
    private static String downloadDir;//下载存储路径
    private final static  String PACKAGENAME = "com.baofeng.mj";
    /**
     * 重置下载路径
     */
    public static void resetDownloadDir(){
        downloadDir = null;
    }

    /**
     * 获取mojing存储路径
     */
    public static String getMojingDir() {
        if (!TextUtils.isEmpty(mojingDir)) {
            mkdir(mojingDir);//创建mojing存储路径
            return mojingDir;
        }
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            mojingDir = getExternalMojingDir();//外部mojing存储路径
        } else {
            mojingDir = getInternalMojingCacheDir();//内部mojing缓存路径
        }
        mkdir(mojingDir);//创建mojing存储路径
        return mojingDir;
    }

    /**
     * 获取下载存储路径
     */

    /**
     * 获取内部mojing缓存路径
     */
    public static String getInternalMojingCacheDir(){
        String dataDirectory = Environment.getDataDirectory().getAbsolutePath();
        StringBuilder sb = new StringBuilder();
        sb.append(dataDirectory).append("/data/com.baofeng.mj/").append(ConfigConstant.STORAGE_DIR).append("/");
        String filePath = sb.toString();
        mkdir(filePath);
        return filePath;
    }

    /**
     * 获取外部mojing存储路径
     */
    public static String getExternalMojingDir(){
        String externalStorageDir = Environment.getExternalStorageDirectory().getAbsolutePath();
        StringBuilder sb = new StringBuilder();
        sb.append(externalStorageDir).append("/").append(ConfigConstant.STORAGE_DIR).append("/");
        String filePath = sb.toString();
        mkdir(filePath);
        return filePath;
    }

    /**
     * 创建根目录
     */
    public static void mkdir(String filePath){
        File dirFile = new File(filePath);
        if(!dirFile.exists()){
            dirFile.mkdirs();//mkdir()不能创建多个目录，所以要用mkdirs()
        }
    }



    /**
     * 获取飞屏字幕缓存路径
     * @return
     */
    public static String getMJFlyScreenSubFile(){
        return getExternalMojingDir()+"FlyScreenSubtitle/";
    }

    /**
     * 判断是否有足够的空间供下载
     *
     * @return
     */
    public static long getEnoughSDSize()
    {
        StatFs statFs = new StatFs(Environment.getExternalStorageDirectory()
                .getAbsolutePath());

        //sd卡分区数
        int blockCounts = statFs.getBlockCount();

        //sd卡可用分区数
        int avCounts = statFs.getAvailableBlocks();

        //一个分区数的大小
        long blockSize = statFs.getBlockSize();

        //sd卡可用空间
        long spaceLeft = avCounts * blockSize;

        return spaceLeft;
    }

    public static String getInternalMojingdownloadDir(){
        String dataDirectory = Environment.getDataDirectory().getAbsolutePath();
        StringBuilder sb = new StringBuilder();
        sb.append(dataDirectory).append("/data/com.baofeng.mj/").append(ConfigConstant.STORAGE_DIR);
        String filePath = sb.toString();
        mkdir(filePath);
        return filePath;
    }






}
