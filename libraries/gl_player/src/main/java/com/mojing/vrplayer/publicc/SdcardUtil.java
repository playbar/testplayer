package com.mojing.vrplayer.publicc;

import android.os.Environment;

import java.io.File;

public class SdcardUtil {


    private static final String APP_DIR_NAME = "baofeng";

    /**
     * 获取下载数据库文件路径 数据库文件存在SD卡上
     *
     * @return Environment.getExternalStorageDirectory().getAbsolutePath()+/mojingYtj
     * /database
     */
    public static String getMojingDBPath() {
        String path = Environment.getExternalStorageDirectory().getAbsolutePath();
        path += File.separator + APP_DIR_NAME + File.separator + ".download/" + ".database";
        File file = new File(path);
        file.mkdirs();
        return path;
    }

    /**
     * 获取文件默认下载路径，为暴风应用文件夹下download目录，旧版下载在该文件夹下
     * Environment.getExternalStorageDirectory().getAbsolutePath
     * ()+/baofeng/download
     *
     * @return
     */
    public static String getDefaultDownLoadPath() {
        String path = Environment.getExternalStorageDirectory().getAbsolutePath();
        path = path + File.separator + APP_DIR_NAME;
        path += File.separator + ".download";
        File file = new File(path);
        file.mkdirs();
        return path;
    }
}
