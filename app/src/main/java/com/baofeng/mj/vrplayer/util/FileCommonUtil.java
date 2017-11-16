package com.baofeng.mj.vrplayer.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;

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
     * 拷贝文件
     * @param sourceFilePath
     * @param targetFilePath
     */
    public static void copyFile(String sourceFilePath, String targetFilePath) {
        if(!TextUtils.isEmpty(sourceFilePath) && !TextUtils.isEmpty(targetFilePath)){
            copyFile(new File(sourceFilePath), new File(targetFilePath), 1024 * 5);
        }
    }

    /**
     * 拷贝文件
     * @param sourceFile
     * @param targetFile
     */
    public static void copyFile(File sourceFile, File targetFile, int buffSize) {
        if(sourceFile == null || targetFile == null){
            return;
        }
        if(!sourceFile.exists()){
            return;
        }
        FileInputStream input = null;
        BufferedInputStream inBuff = null;
        FileOutputStream output = null;
        BufferedOutputStream outBuff = null;
        try {
            input = new FileInputStream(sourceFile);
            inBuff = new BufferedInputStream(input);
            output = new FileOutputStream(targetFile);
            outBuff = new BufferedOutputStream(output);

            byte[] b = new byte[buffSize];// 缓冲数组
            int len;
            while ((len = inBuff.read(b)) != -1) {
                outBuff.write(b, 0, len);
            }

            outBuff.flush();// 刷新此缓冲的输出流
            outBuff.close();
            output.close();
            inBuff.close();
            input.close();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if(null != outBuff){
                try {
                    outBuff.close();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            if(null != output){
                try {
                    output.close();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            if(null != inBuff){
                try {
                    inBuff.close();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            if(null != input){
                try {
                    input.close();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 拷贝文件（使用shell命令）
     * @param oldPath
     * @param newPath
     * @return
     */
    public static boolean copyFileByShell(String oldPath, String newPath) {
        boolean copyResult = false;
        Process proc = null;
        try {
            String cmd = "cp -r " + oldPath + "/. " + newPath;
            proc = Runtime.getRuntime().exec(cmd);
            if (proc.waitFor() == 0) {
                copyResult = true;
            }else{
                printShellError(proc);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(proc != null){
                proc.destroy();
                proc = null;
            }
        }
        return copyResult;
    }

    /**
     * 打印失败原因
     * @param proc
     */
    public static void printShellError(Process proc) {
        try {
            InputStream in = proc.getErrorStream();
            StringBuilder result = new StringBuilder();
            byte[] re = new byte[1024];
            while (in.read(re) != -1) {
                 result.append(new String(re));
            }
        } catch (IOException e) {
        }
    }

    /**
     * 重命名文件
     */
    public static boolean renameFile(String path, String newName) {
        if (TextUtils.isEmpty(path) || TextUtils.isEmpty(newName)) {
            return false;
        }
        File file = new File(path);
        if (file.exists()) {
            String dir = file.getParentFile().getAbsolutePath();
            if (!dir.endsWith(File.separator)) {
                dir += File.separator;
            }
            File newFile = new File(dir + newName);
            return file.renameTo(newFile);
        }
        return false;
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

    /**
     * 获取文件名
     */
    public static String getFileNameNoSuffix(String fileName) {
        if(!TextUtils.isEmpty(fileName)){
            int lastIndex = fileName.lastIndexOf(".");
            if(lastIndex >= 0){
                return fileName.substring(0, lastIndex);
            }
        }
        return null;
    }

    /**
     * 替换文件后缀名
     */
    public static String replaceFileNameSuffix(String fileName, String newSuffix){
        if(!TextUtils.isEmpty(fileName) && !TextUtils.isEmpty(newSuffix)){
            int lastIndex = fileName.lastIndexOf(".");
            if(lastIndex >= 0){
                return fileName.substring(0, lastIndex) + newSuffix.trim();
            }
        }
        return fileName;
    }

    /**
     * 根据Uri获取路径
     */
    public static String getPathByUri(Context mContext, final Uri uri) {
        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
        if (isKitKat && DocumentsContract.isDocumentUri(mContext, uri)) {// DocumentProvider
            if (isExternalStorageDocument(uri)) {// ExternalStorageProvider
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
                // TODO handle non-primary volumes
            } else if (isDownloadsDocument(uri)) {// DownloadsProvider
                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
                return getDataColumn(mContext, contentUri, null, null);
            } else if (isMediaDocument(uri)) {// MediaProvider
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[] {split[1]
                };
                return getDataColumn(mContext, contentUri, selection, selectionArgs);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {// MediaStore (and general)
            if (isGooglePhotosUri(uri)){// Return the remote address
                return uri.getLastPathSegment();
            }
            return getDataColumn(mContext, uri, null, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {// File
            return uri.getPath();
        }
        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     * @param uri The Uri to query.
     * @param selection (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context mContext, Uri uri, String selection, String[] selectionArgs) {
        if(uri == null){
            return null;
        }
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Video.Media.DATA };
            ContentResolver contentResolver = mContext.getContentResolver();
            cursor = contentResolver.query(uri, proj, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                int index = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
                return cursor.getString(index);
            }
        } catch (Exception e){
            e.printStackTrace();
        }finally {
            if (cursor != null){
                cursor.close();
            }
        }
        return null;
    }


    /**
     * 获取真正的视频地址
     * @param activity
     * @param path content://media/external/video/media/1711
     * @return 返回 /storage/emulated/0/阿波罗11号3D.mp4
     */
    public static String getRealVideoPath(Activity activity, String path){
        if(activity == null || TextUtils.isEmpty(path)){
            return path;
        }else if(path.startsWith("content://")){
            String[] proj = { MediaStore.Video.Media.DATA };
            Cursor cursor = activity.managedQuery(Uri.parse(path), proj, null, null, null);
            if(cursor.moveToFirst()){
                int column_index = cursor.getColumnIndexOrThrow(proj[0]);
                String realPath = cursor.getString(column_index);

                if(Build.VERSION.SDK_INT < 14) { //4.0及其以上的版本中，Cursor会自动关闭
                    cursor.close();
                }
                return realPath;
            }else{
                return path;
            }
        }else{
            return path;
        }
    }


    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }
}
