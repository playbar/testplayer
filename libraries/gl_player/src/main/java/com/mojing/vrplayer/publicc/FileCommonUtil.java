package com.mojing.vrplayer.publicc;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;

import com.storm.smart.common.utils.LogHelper;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.net.URI;

/**
 * Created by liuchuanchi on 2016/4/29.
 * 文件通用工具类
 */
@SuppressLint("NewApi")
public class FileCommonUtil {
    public static final String filePrefix = "file://";//文件前缀
    public static final int ruleFileLastModify = 0;//文件上次修改时间排序
    public static final int ruleFileName = 1;//文件名排序
    public static final int ruleFileSize = 2;//文件大小排序

    /**
     * 写文件
     * @param serializable
     * @param path
     */
    public static void writeFileSerializable(Serializable serializable, String path) {
        if(serializable == null || TextUtils.isEmpty(path)) {
            return;
        }
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;
        try {
            fos = new FileOutputStream(new File(path));
            oos = new ObjectOutputStream(fos);
            oos.writeObject(serializable);
            oos.close();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if(null != fos){
                try {
                    fos.close();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            if(null != oos){
                try {
                    oos.close();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 写文件
     * @param serializable
     * @param file
     */
    public static void writeFileSerializable(Serializable serializable, File file) {
        if(serializable == null || file == null) {
            return;
        }
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;
        try {
            fos = new FileOutputStream(file);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(serializable);
            oos.close();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if(null != fos){
                try {
                  fos.close();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            if(null != oos){
                try {
                    oos.close();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 读文件
     * @param path
     * @return
     */
    public static Serializable readFileSerializable(String path) {
        if(TextUtils.isEmpty(path)) {
            return null;
        }
        File file = new File(path);
        if(file == null || !file.exists()){
            return null;
        }
        FileInputStream fis = null;
        ObjectInputStream ois = null;
        try {
            fis = new FileInputStream(file);
            ois = new ObjectInputStream(fis);
            Serializable serializable = (Serializable) ois.readObject();
            ois.close();
            fis.close();
            return serializable;
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if(null != fis){
                try{
                    fis.close();
                }catch (Exception e){
                    e.printStackTrace();
                }

            }

            if(null != ois){
                try{
                    ois.close();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * 读文件
     * @param file
     * @return
     */
    public static Serializable readFileSerializable(File file) {
        if(file == null || !file.exists()){
            return null;
        }
        FileInputStream fis = null;
        ObjectInputStream ois = null;
        try {
            fis = new FileInputStream(file);
            ois = new ObjectInputStream(fis);
            Serializable serializable = (Serializable) ois.readObject();
            ois.close();
            fis.close();
            return serializable;
        } catch (Exception e) {
        }finally {
            if(null != fis){
                try {
                    fis.close();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            if(null != ois){
                try {
                    ois.close();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * 写文件
     * @param jsonStr
     * @param path
     */
    public static void writeFileString(String jsonStr, String path) {
        if(TextUtils.isEmpty(jsonStr) || TextUtils.isEmpty(path)) {
            return;
        }
        FileOutputStream fos = null;
        OutputStreamWriter osw = null;
        try {
            fos = new FileOutputStream(new File(path));
            osw = new OutputStreamWriter(fos, "UTF-8");
            osw.write(jsonStr);
            osw.flush();
            osw.close();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if(null != fos){
                try{
                    fos.close();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            if(null != osw){
                try{
                    osw.close();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 写文件
     * @param jsonStr
     * @param file
     */
    public static void writeFileString(String jsonStr, File file) {
        if(TextUtils.isEmpty(jsonStr)) {
            return;
        }
        FileOutputStream fos = null;
        OutputStreamWriter osw = null;
        try {
            fos = new FileOutputStream(file);
            osw = new OutputStreamWriter(fos, "UTF-8");
            osw.write(jsonStr);
            osw.flush();
            osw.close();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if(null != fos){
                try {
                    fos.close();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            if(null != osw){
                try {
                    osw.close();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 读文件
     * @param path
     * @return
     */
    public static String readFileString(String path) {
        if(TextUtils.isEmpty(path)) {
            return null;
        }
        return readFileString(new File(path));
    }

    /**
     * 读文件
     */
    public static String readFileString(File file) {
        if(file == null || !file.exists()){
            return null;
        }
        FileInputStream inputStream = null;
        InputStreamReader isr = null;
        BufferedReader br = null;
        try {
            inputStream = new FileInputStream(file);
            isr = new InputStreamReader(inputStream, "UTF-8");
            br = new BufferedReader(isr);
            StringBuilder sb=new StringBuilder();
            String line = null;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            br.close();
            isr.close();
            inputStream.close();
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if(null != br){
                try {
                    br.close();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            if(null != isr){
                try {
                   isr.close();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            if(null != inputStream){
                try {
                    inputStream.close();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * 删除文件
     */
    public static boolean deleteFile(String filePath) {
        LogHelper.e("infos","filePath ===="+filePath);
        if(TextUtils.isEmpty(filePath)){
            return false;
        }
        return deleteFile(new File(filePath));
    }

    /**
     * 删除文件
     */
    public static boolean deleteFile(File curFile) {
        if(curFile == null || !curFile.exists()){
            return false;
        }
        if (curFile.isDirectory()) {//删除子目录
            File[] childrenFile = curFile.listFiles();
            if (childrenFile != null && childrenFile.length > 0) {
                for (int i = 0; i < childrenFile.length; i++) {
                    if(!deleteFile(childrenFile[i])){
                        return false;
                    }
                }
            }
        }
        return curFile.delete();//删除当前file
    }


    /**
     * 获取文件后缀名
     */
    public static String getFileSuffix(String downloadUrl) {
        if (TextUtils.isEmpty(downloadUrl)) {
            return "";
        }
        if (downloadUrl.contains(".apk")) {
            return ".apk";
        } else if (downloadUrl.contains(".zip")) {
            return ".zip";
        } else if (downloadUrl.contains(".mp4")) {
            return ".mp4";
        } else {
            try {
                String urlPath = URI.create(downloadUrl.trim()).getPath();
                if (!TextUtils.isEmpty(urlPath)){
                    String[] strs = urlPath.split("\\.");
                    if (strs.length == 2) {
                        return "." + strs[1];
                    }
                }
            } catch (IllegalArgumentException e) {
            }
            return "";
        }
    }

    /**
     * 获取文件后缀名
     */
    public static String getFileNameSuffix(String fileName) {
        if(!TextUtils.isEmpty(fileName)){
            int lastIndex = fileName.lastIndexOf(".");
            if(lastIndex >= 0){
                return fileName.substring(lastIndex);
            }
        }
        return null;
    }

    /**
     * 获取文件后缀名
     */
    public static String getFileNameSuffixNoDot(String fileName) {
        if(!TextUtils.isEmpty(fileName)){
            int lastIndex = fileName.lastIndexOf(".");
            if(lastIndex >= 0){
                return fileName.substring(lastIndex + 1);
            }
        }
        return null;
    }



}
