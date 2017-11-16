package com.baofeng.mj.smb.util;

import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by panxin on 2017/3/31.
 */

public class SmbIpUtil {

    public static String getSmbIp(String ip) {
        if (ip == null) {
            return "";
        }
        return "smb://" + ip + "/";
    }


    public static String getCurrentDirName(String path) {
        String temp = path.substring(6, path.lastIndexOf("/"));
        if (temp.contains("/")) {
            return temp.substring(temp.lastIndexOf("/") + 1);
        } else {
            return "smb://";
        }
    }


    public static boolean isValidIpAddress(String ip) {
        if (TextUtils.isEmpty(ip)) {
            return false;
        }
        Pattern pattern = Pattern.compile("\\b((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\b");
        Matcher matcher = pattern.matcher(ip);
        return matcher.matches();
    }
}
