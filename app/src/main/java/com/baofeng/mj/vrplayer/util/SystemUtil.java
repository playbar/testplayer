package com.baofeng.mj.vrplayer.util;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by liuchuanchi on 2016/5/21.
 * 系统工具类
 */
public class SystemUtil {
    /**
     * 获取cpu型号
     */
    public static String getCpuType(){
        String strInfo = getCpuInfo();
        String strType = null;
        if (strInfo.contains("ARMv5")) {
            strType = "armv5";
        } else if (strInfo.contains("ARMv6")) {
            strType = "armv6";
        } else if (strInfo.contains("ARMv7")) {
            strType = "armv7";
        } else if (strInfo.contains("Intel")){
            strType = "x86";
        }else{
            strType = "unknown";
            return strType;
        }
        if (strInfo.contains("neon")) {
            strType += "_neon";
        }else if (strInfo.contains("vfpv3")) {
            strType += "_vfpv3";
        }else if (strInfo.contains(" vfp")) {
            strType += "_vfp";
        }else{
            strType += "_none";
        }
        return strType;
    }

    /**
     * 获取cpu信息
     */
    public static String getCpuInfo(){
        if(Build.CPU_ABI.equalsIgnoreCase("x86")){
            return "Intel";
        }
        try {
            byte[] bs = new byte[1024];
            RandomAccessFile reader = new RandomAccessFile("/proc/cpuinfo", "r");
            reader.read(bs);
            String ret = new String(bs);
            int index = ret.indexOf(0);
            if(index != -1) {
                return ret.substring(0, index);
            } else {
                return ret;
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 获取手机cpu信息
     */
    public static List<String> getCpuInfos() {
        ArrayList<String> cpuInfoList = new ArrayList<String>();
        try {
            FileReader fileReader = new FileReader("/proc/cpuinfo");
            BufferedReader bufferedReader = new BufferedReader(fileReader, 1024);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                cpuInfoList.add(line);
            }
            bufferedReader.close();
            fileReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cpuInfoList;
    }

    /**
     * 获取手机cpu硬件型号
     */
    public static String getCpuHardCode() {
        List<String> cupInfo = getCpuInfos();
        if (cupInfo != null) {
            String hardCode = "";
            int size = cupInfo.size();
            for (int i = 0; i < size; i++) {
                if (i >= size) {
                    break;
                }
                if (TextUtils.isEmpty(cupInfo.get(i))) {
                    continue;
                } else {
                    int j = cupInfo.get(i).indexOf("Hardware");
                    if (j == -1) {
                        continue;
                    } else {
                        int n = cupInfo.get(i).substring(j).trim().indexOf(":");
                        if (n == -1) {
                            continue;
                        } else {
                            return cupInfo.get(i).substring(j).substring(n + 1).trim();
                        }
                    }
                }
            }
            return hardCode;
        } else {
            return "";
        }
    }

    /**
     * 获取手机cpu指令型号
     */
    public static String getCpuInsCode() {
        List<String> cupInfo = getCpuInfos();
        if (cupInfo != null) {
            if (TextUtils.isEmpty(cupInfo.get(0))) {
                return "";
            } else {
                String str = cupInfo.get(0);
                int i = str.indexOf(":") + 1;
                return str.substring(i).trim();
            }
        } else {
            return "";
        }
    }

    /**
     * 获取运行平台
     */
    public static String getRunningPlatform(Context mContext) {
        try {
            PackageManager pm = mContext.getPackageManager();
            String packageName = mContext.getPackageName();
            ApplicationInfo ai = pm.getApplicationInfo(packageName, PackageManager.GET_META_DATA);
            Object value = ai.metaData.get("running_platform");
            if (value != null) {
                return value.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "1";
    }

    /**
     * 获取手机运营商信息
     */
    public static String getServiceOperator(Context mContext) {
        TelephonyManager telephonyManager = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
        String operatorString = telephonyManager.getSimOperator();
        if (operatorString == null) {
            return "未知";
        }
        if (operatorString.equals("46000") || operatorString.equals("46002")) {
            //中国移动
            return "移动";
        } else if (operatorString.equals("46001")) {
            //中国联通
            return "联通";
        } else if (operatorString.equals("46003")) {
            //中国电信
            return "电信";
        }
        //error
        return "未知";
    }

    /**
     * 获取手机品牌
     */
    public static String getMobileBrand() {
        return Build.BRAND;
    }

    /**
     * 获取手机型号
     */
    public static String getMobileModel() {
        return Build.MODEL;
    }


}
