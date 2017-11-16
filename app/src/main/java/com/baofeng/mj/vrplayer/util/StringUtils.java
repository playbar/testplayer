package com.baofeng.mj.vrplayer.util;

import java.text.SimpleDateFormat;
import java.util.TimeZone;

public class StringUtils {

    public static String getStringTime(long position) {
        SimpleDateFormat fmPlayTime;
        if (position <= 0) {
            return "00:00";
        }

        long lCurrentPosition = position / 1000;
        long lHours = lCurrentPosition / 3600;

        if (lHours > 0)
            fmPlayTime = new SimpleDateFormat("HH:mm:ss");
        else
            fmPlayTime = new SimpleDateFormat("mm:ss");

        fmPlayTime.setTimeZone(TimeZone.getTimeZone("GMT"));
        return fmPlayTime.format(position);
    }

    /**
     * 获取文件扩展名
     * 
     * @param fileName test.mp4
     * @return mp4
     * @author zony
     * @time 2014-8-20
     */
    public static String getExtensionName(String fileName) {
        if ((fileName != null) && (fileName.length() > 0)) {
            int index = fileName.lastIndexOf('.');
            if ((index > -1) && (index < (fileName.length() - 1))) {
                return fileName.substring(index + 1);
            }
        }
        return fileName;
    }

    /**
     * 获取不带扩展名的文件名
     * 
     * @param fileName test.mp4
     * @return test
     * @author zony
     * @time 2014-8-20
     */
    public static String getFileNameNoEx(String fileName) {
        if ((fileName != null) && (fileName.length() > 0)) {
            int index = fileName.lastIndexOf('.');
            if ((index > -1) && (index < (fileName.length()))) {
                return fileName.substring(0, index);
            }
        }
        return fileName;
    }

    /**
     * 获取文件名
     * 
     * @param filePath /sdcard/test.mp4
     * @return test.mp4
     * @author zony
     * @time 2014-8-20
     */
    public static String getFileName(String filePath) {
        if ((filePath != null) && (filePath.length() > 0)) {
            int index = filePath.lastIndexOf('/');
            if ((index > -1) && (index < (filePath.length()))) {
                return filePath.substring(index).replace("/", "");
            }
        }
        return filePath;
    }

}
