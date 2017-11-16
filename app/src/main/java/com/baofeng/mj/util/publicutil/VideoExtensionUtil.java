package com.baofeng.mj.util.publicutil;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by liuchuanchi on 2016/5/22.
 * 视频扩展名工具类
 */
public class VideoExtensionUtil {
    private static HashMap<String,Boolean> videoExtensionMap;//视频扩展名map

    /**
     * 文件是不是字幕
     */
    public static boolean fileIsSRT(String fileName){

        if(fileName == null||"".equals(fileName)){
            return false;
        }
        int lastIndex = fileName.lastIndexOf(".");
        if(lastIndex >= 0){
            String fileSuffix = fileName.substring(lastIndex);
            if(fileSuffix.toLowerCase().equals(".srt")){
                return true;
            }
        }
        return false;
    }

    /**
     * 文件是不是视频
     */
    public static boolean fileIsVideo(String fileName){

        if(fileName == null||"".equals(fileName)){
            return false;
        }
        int lastIndex = fileName.lastIndexOf(".");
        if(lastIndex >= 0){
            String fileSuffix = fileName.substring(lastIndex);
            if(videoExtensionMap == null){
                initVideoExtensionMap();
            }
            return videoExtensionMap.containsKey(fileSuffix.toLowerCase());
        }
        return false;
    }

    /**
     * 文件是不是视频
     */
    public static boolean fileIsVideo(File file){

        String fileName = file.getName();
        int lastIndex = fileName.lastIndexOf(".");
        if(lastIndex >= 0){
            String fileSuffix = fileName.substring(lastIndex);
            if(videoExtensionMap == null){
                initVideoExtensionMap();
            }
            return videoExtensionMap.containsKey(fileSuffix);
        }
        return false;
    }

    /**
     * 获取视频扩展名
     * @return 字符串集合
     */
    public static String getVideoExtensionStrs(){
        StringBuilder sb = new StringBuilder();
        if(videoExtensionMap == null){
            initVideoExtensionMap();
        }
        Iterator iter = videoExtensionMap.keySet().iterator();
        while (iter.hasNext()) {
            String videoExtension = (String)iter.next();
            sb.append(videoExtension).append(";");
        }
        return sb.toString();
    }

    /**
     * 初始化视频扩展名map
     */
    private static void initVideoExtensionMap(){
        videoExtensionMap = new HashMap<String,Boolean>();
        videoExtensionMap.put(".asf",true);
        videoExtensionMap.put(".wm", true);
        videoExtensionMap.put(".wmp",true);
        videoExtensionMap.put(".wmv",true);
        videoExtensionMap.put(".ram",true);
        videoExtensionMap.put(".rm",true);
        videoExtensionMap.put(".rmvb",true);
        videoExtensionMap.put(".rpm",true);
        videoExtensionMap.put(".scm",true);
        videoExtensionMap.put(".rp",true);
        videoExtensionMap.put(".evo",true);
        videoExtensionMap.put(".vob",true);
        videoExtensionMap.put(".mov",true);
        videoExtensionMap.put(".qt",true);
        videoExtensionMap.put(".3g2",true);
        videoExtensionMap.put(".3gp",true);
        videoExtensionMap.put(".3gp2",true);
        videoExtensionMap.put(".3gpp",true);
        videoExtensionMap.put(".BHD",true);
        videoExtensionMap.put(".GHD",true);
        videoExtensionMap.put(".amv",true);
        videoExtensionMap.put(".avi",true);
        videoExtensionMap.put(".bik",true);
        videoExtensionMap.put(".csf",true);
        videoExtensionMap.put(".d2v",true);
        videoExtensionMap.put(".dsm",true);
        videoExtensionMap.put(".ivf",true);
        videoExtensionMap.put(".m1v",true);
        videoExtensionMap.put(".m2p",true);
        videoExtensionMap.put(".m2ts",true);
        videoExtensionMap.put(".m2v",true);
        videoExtensionMap.put(".m4b",true);
        videoExtensionMap.put(".m4p",true);
        videoExtensionMap.put(".m4v",true);
        videoExtensionMap.put(".mkv",true);
        videoExtensionMap.put(".mp4",true);
        videoExtensionMap.put(".mpe",true);
        videoExtensionMap.put(".mpeg",true);
        videoExtensionMap.put(".mpg",true);
        videoExtensionMap.put(".mts",true);
        videoExtensionMap.put(".ogm",true);
        videoExtensionMap.put(".pmp",true);
        videoExtensionMap.put(".pmp2",true);
        videoExtensionMap.put(".pss",true);
        videoExtensionMap.put(".pva",true);
        videoExtensionMap.put(".ratDVD",true);
        videoExtensionMap.put(".smk",true);
        videoExtensionMap.put(".tp",true);
        videoExtensionMap.put(".tpr",true);
        videoExtensionMap.put(".ts",true);
        videoExtensionMap.put(".vg2",true);
        videoExtensionMap.put(".vid",true);
        videoExtensionMap.put(".vp6",true);
        videoExtensionMap.put(".vp7",true);
        videoExtensionMap.put(".wv",true);
        videoExtensionMap.put(".asm",true);
        videoExtensionMap.put(".avsts",true);
        videoExtensionMap.put(".divx",true);
        videoExtensionMap.put(".webm",true);
        videoExtensionMap.put(".swf",true);
        videoExtensionMap.put(".flv",true);
        videoExtensionMap.put(".flic",true);
        videoExtensionMap.put(".fli",true);
        videoExtensionMap.put(".flc",true);
        videoExtensionMap.put(".mod",true);
        videoExtensionMap.put(".vp5",true);
        videoExtensionMap.put(".asx",true);
        videoExtensionMap.put(".sub",true);
        videoExtensionMap.put(".bhd",true);
    }
}
