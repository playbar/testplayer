package com.baofeng.mj.vrplayer.business;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;

import com.baofeng.mj.vrplayer.util.FileCommonUtil;
import com.baofeng.mj.vrplayer.util.StorageUtil;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by liuchuanchi on 2016/5/5.
 * 本地视频路径业务
 */
public class LocalVideoPathBusiness {
    /**
     * 所有默认根路径
     */
    public static final String Pictures = "Pictures";
    public static final String Download = "Download";
    public static final String DCIM = "DCIM";
    public static final String Movies = "Movies";
    public static final String XFPLAY = "xfplay/video";

    private static String localVideoFolder;//本地视频文件夹

    /**
     * 获取本地视频文件夹
     */
    public static String getLocalVideoFolder(Context mContext){
        if(TextUtils.isEmpty(localVideoFolder)){
            localVideoFolder = StorageUtil.getStorageDir(mContext) + "localVideoFolder";
        }
        StorageUtil.mkdirs(localVideoFolder);
        return localVideoFolder;
    }

    /**
     * 获取本地视频缩略图
     */
    public static String getLocalVideoImg(Context mContext, String filePath){
        if(!TextUtils.isEmpty(filePath)){
            int lastIndex = filePath.lastIndexOf(".");
            if(lastIndex > 0){
                filePath = filePath.substring(0, lastIndex);
            }
        }
        return getLocalVideoFolder(mContext) + "/" + filePath.hashCode() + ".png";
    }

    /**
     * 转换名称
     */
    public static String convertName(String name){
        if(Pictures.equals(name)){
            return "相册";
        }else if(Download.equals(name)){
            return "系统下载";
        }else if(DCIM.equals(name)){
            return "系统视频";
        }else if(Movies.equals(name)){
            return "系统影视";
        }else if(XFPLAY.equals(name)){
            return "影音先锋";
        }
        return name;
    }

    /**
     * 获取所有根路径
     * @param containAll true包含所有，false只包含用户选中的
     */
    public static List<String> getAllDir(Context mContext, boolean containAll){
        List<String> dirList = new ArrayList<String>();
        dirList.addAll(getAllDefaultDir(mContext, containAll));
        dirList.addAll(getAllCustomDir(mContext, containAll));
        return dirList;
    }

    /**
     * 获取所有默认根路径
     * @param containAll true包含所有，false只包含用户选中的
     */
    public static List<String> getAllDefaultDir(Context mContext, boolean containAll){
        List<String> dirList = new ArrayList<String>();
        if(containAll){//包含所有
            addAllDefaultDir(dirList);
        }else{//只包含用户选中的
            if(SpPublicBusiness.getInstance().getDefaultStorageSearch(mContext)){
                addAllDefaultDir(dirList);
            }
        }
        for(String dirPath : dirList){
            StorageUtil.mkdirs(dirPath);
        }
        return dirList;
    }

    /**
     * 添加所有默认根路径
     */
    private static void addAllDefaultDir(List<String> dirList){
        String externalDir = Environment.getExternalStorageDirectory().getAbsolutePath();
        dirList.add(externalDir + "/" + Pictures);
        dirList.add(externalDir + "/" + Download);
        dirList.add(externalDir + "/" + DCIM);
        dirList.add(externalDir + "/" + Movies);
        dirList.add(externalDir + "/" + XFPLAY);
    }

    /**
     * 获取所有自定义根路径
     * @param containAll true包含所有，false只包含用户选中的
     */
    public static List<String> getAllCustomDir(Context mContext, boolean containAll){
        List<String> dirList = new ArrayList<String>();
        String externalDir = Environment.getExternalStorageDirectory().getAbsolutePath();
        File customFile = new File(externalDir, ".customDirs");
        String dirJson = FileCommonUtil.readFileString(customFile);
        try {
            if(!TextUtils.isEmpty(dirJson)){
                JSONArray dirJa = new JSONArray(dirJson);
                int size = dirJa.length();
                for(int i = size - 1; i >= 0; i--){
                    String customDir = dirJa.getString(i);
                    if(containAll){//包含所有
                        dirList.add(customDir);
                    }else{//只包含用户选中的
                        if(SpPublicBusiness.getInstance().getCustomStorageSearch(mContext, customDir)){
                            dirList.add(customDir);
                        }
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        for(String dirPath : dirList){
            StorageUtil.mkdirs(dirPath);
        }
        return dirList;
    }

    /**
     * 添加自定义根路径
     */
    public static void addCustomDir(List<String> pathList){
        String externalDir = Environment.getExternalStorageDirectory().getAbsolutePath();
        File customFile = new File(externalDir, ".customDirs");
        String dirJson = FileCommonUtil.readFileString(customFile);
        try {
            if(!TextUtils.isEmpty(dirJson)){
                JSONArray dirJa = new JSONArray(dirJson);
                for(String path : pathList){
                    dirJa.put(path);
                }
                FileCommonUtil.writeFileString(dirJa.toString(), customFile);
            }else{
                JSONArray dirJa = new JSONArray();
                for(String path : pathList){
                    dirJa.put(path);
                }
                FileCommonUtil.writeFileString(dirJa.toString(), customFile);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 添加自定义根路径
     */
    public static void addCustomDir(String dirPath){
        String externalDir = Environment.getExternalStorageDirectory().getAbsolutePath();
        File customFile = new File(externalDir, ".customDirs");
        String dirJson = FileCommonUtil.readFileString(customFile);
        try {
            if(!TextUtils.isEmpty(dirJson)){
                JSONArray dirJa = new JSONArray(dirJson);
                dirJa.put(dirPath);
                FileCommonUtil.writeFileString(dirJa.toString(), customFile);
            }else{
                JSONArray dirJa = new JSONArray();
                dirJa.put(dirPath);
                FileCommonUtil.writeFileString(dirJa.toString(), customFile);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 移除自定义根路径
     */
    public static void removeCustomDir(String dirPath){
        String externalDir = Environment.getExternalStorageDirectory().getAbsolutePath();
        File customFile = new File(externalDir, ".customDirs");
        String dirJson = FileCommonUtil.readFileString(customFile);
        try {
            if(!TextUtils.isEmpty(dirJson)){
                JSONArray dirJa = new JSONArray(dirJson);
                int size = dirJa.length();
                for(int i = size - 1; i >= 0; i--){
                    if(dirJa.getString(i).equals(dirPath)){
                        dirJa.remove(i);
                    }
                }
                FileCommonUtil.writeFileString(dirJa.toString(), customFile);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
