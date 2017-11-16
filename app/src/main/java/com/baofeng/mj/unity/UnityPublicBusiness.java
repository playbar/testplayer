package com.baofeng.mj.unity;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.BatteryManager;
import android.os.Debug;
import android.os.Environment;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

import com.baofeng.mj.vrplayer.activity.MainActivity;
import com.baofeng.mj.vrplayer.business.SpSettingBusiness;
import com.baofeng.mj.vrplayer.util.AudioManagerUtil;
import com.baofeng.mj.vrplayer.util.BrightnessUtil;
import  com.baofeng.mj.util.publicutil.NetworkUtil;
import com.baofeng.mj.vrplayer.util.ReportUtil;
import com.baofeng.mj.vrplayer.util.StickUtil;

import java.io.File;
import java.util.List;

/**
 * @author liuchuanchi
 * @description: Unity公共业务类
 */
public class UnityPublicBusiness {

    public static final String BATTERY_LEVEL = "level";
    public static final String BATTERY_SCALE = "scale";
    public static final String BATTERY_STATUS = "status";
    public static final String BATTERY_TEMPERATURE = "temperature";

    private final static int HANDCONTRL_LEFT = 0;//左手
    private final static int HANDCONTRL_RIGHT = 1;//右手
    private final static String HANDCONTRL_KEY = "remotecontroller_mode";// 存储key

    private static boolean isDefaultMusicActive = false;
    private static boolean isPortraitOrLandscape = true; // ture:竖屏，false横屏

    /**
     * 设置当前音量（音量范围是 0 - 100）
     */
    public static void setStreamCurrentVolume(int currentVolume) {
        AudioManagerUtil.getInstance().setStreamCurrentVolume(UnityActivity.INSTANCE, currentVolume);
    }

    /**
     * 获取当前音量（音量范围是 0 - 100，不是系统的音量范围 0 - 15）
     */
    public static int getStreamCurrentVolume() {
        return AudioManagerUtil.getInstance().getStreamCurrentVolume(UnityActivity.INSTANCE);
    }

    /**
     * 获取最大音量（不返回系统最大音量：15，直接返回100）
     */
    public static int getStreamMaxVolume() {
        return AudioManagerUtil.getInstance().getStreamMaxVolume();
    }

    /**
     * 设置用户亮度值
     *
     * @param brightnessValue 亮度值 0 - 255
     */
    public static void setUserBrightnessValue(int brightnessValue) {
        BrightnessUtil.setUserBrightnessValue(UnityActivity.INSTANCE, brightnessValue);
    }

    /**
     * 获取用户亮度值 0 - 255
     */
    public static int getUserBrightnessValue() {
        return BrightnessUtil.getUserBrightnessValue(UnityActivity.INSTANCE);
    }

    /**
     * 设置系统亮度值
     *
     * @param brightnessValue 亮度值 0 - 255
     */
    public static void setSystemBrightnessValue(Context context, int brightnessValue) {
        Settings.System.putInt(context.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, brightnessValue);
    }


    /**
     * 获取系统亮度值 0 - 255
     */
    public static int getSysBrightnessValue() {
        return BrightnessUtil.getSysBrightnessValue(UnityActivity.INSTANCE);
    }



    public static void backSetSysBrightnessValue(){
        UnityPublicBusiness.setSystemBrightnessValue(UnityActivity.INSTANCE, SpSettingBusiness.getInstance().getSystemBrightnessValue(UnityActivity.INSTANCE));
    }
    /**
     * 设置自适应亮度模式
     *
     * @param isAutoBrightnessMode true打开，false关闭
     */
    public static void setAutoBrightnessMode(boolean isAutoBrightnessMode) {
        BrightnessUtil.setAutoBrightnessMode(UnityActivity.INSTANCE, isAutoBrightnessMode);
    }

    /**
     * true是自适应亮度模式，false不是
     */
    public static boolean isAutoBrightnessMode() {
        return BrightnessUtil.isAutoBrightnessMode(UnityActivity.INSTANCE);
    }

    /**
     * 返回电池是否在充电中
     * @return
     */
    public static boolean isBatteryCharging(){
        int status = UnityActivity.INSTANCE.getBatteryStatus();
        if(status == BatteryManager.BATTERY_STATUS_CHARGING ||status == BatteryManager.BATTERY_STATUS_FULL){
            return true;
        }
        return false;
    }

    /**
     * 获取电池电量 0 - 100
     */
    public static int getBatteryLevel() {
        return UnityActivity.INSTANCE.getBatteryLevel();
    }

    /**
     * 获取电池温度 例如值：29.0
     */
    public static String getBatteryTemperature() {
        return UnityActivity.INSTANCE.getBatteryTemperature();
    }

    /**
     * Android与Unity交互，添加Android回调
     */
    public void addIAndroidCallback(IAndroidCallback iAndroidCallback) {
        if(UnityActivity.INSTANCE != null){
            UnityActivity.INSTANCE.addIAndroidCallback(iAndroidCallback);
        }
    }

    /**
     * 获取是否设置反锯齿
     *
     * @return false：未设置，true：设置
     */
    public static boolean getAntiSwitch() {
        if (SpSettingBusiness.getInstance().getAnti_aliasing(UnityActivity.INSTANCE) == 0) {
            return false;
        }
        return true;
    }

    /**
     * 获取是否设置曲面
     *
     * @return false：未设置，true：设置
     */
    public static boolean getSurSwitch() {
        if (SpSettingBusiness.getInstance().getSur_Switch(UnityActivity.INSTANCE) == 0) {
            return false;
        }
        return true;
    }

    /**
     * 获取是否设置球模背景
     *
     * @return false：未设置，true：设置
     */
    public static boolean getBgSwitch() {
        if (SpSettingBusiness.getInstance().getBgSwitch(UnityActivity.INSTANCE) == 0) {
            return false;
        }
        return true;
    }

    /**
     * 获取是否设置过渡动画
     *
     * @return false：未设置，true：设置
     */
    public static boolean getAniSwitch() {
        if (SpSettingBusiness.getInstance().getTrans_Ani_Switch(UnityActivity.INSTANCE) == 0) {
            return false;
        }
        return true;
    }

    /**
     * 获取是否设置透明
     *
     * @return false：未设置，true：设置
     */
    public static boolean getTransSwitch() {
        if (SpSettingBusiness.getInstance().getTrans_Switch(UnityActivity.INSTANCE) == 0) {
            return false;
        }
        return true;
    }

    /**
     * 获取是否设置Mask特效
     *
     * @return false：未设置，true：设置
     */
    public static boolean getMaskSwitch() {
        if (SpSettingBusiness.getInstance().getMask(UnityActivity.INSTANCE) == 0) {
            return false;
        }
        return true;
    }

    /*
    * 获取手机时间格式
    * @returen true:24小时制，false:12小时制
     */
    public static boolean getTimeFormat() {
        return android.text.format.DateFormat.is24HourFormat(UnityActivity.INSTANCE);
    }

    /*
    *获取操纵杆状态
    * @return ture:连接，false：断开
     */
    public static boolean getJoystickStatus() {
        boolean isJoystickConnect = StickUtil.getInstance(UnityActivity.INSTANCE).isConnected();
        return isJoystickConnect;
    }

    /*
    * 获取设备ID，
    * @return 设备ID
     */
    public static String getDeviceID() {
        String deviceID = ((TelephonyManager) UnityActivity.INSTANCE.getSystemService(UnityActivity.INSTANCE.TELEPHONY_SERVICE)).getDeviceId();
        if (deviceID == null)
            return "";
        return deviceID;
    }

    /*
    * 获取系统版本号
    * @return 系统版本号
     */
    public static String getOSVersion() {
        String osVersion = android.os.Build.VERSION.RELEASE;
        if (osVersion == null)
            return "";
        return osVersion;
    }

    /**
     * 获取控制模式
     * @return
     */
    public static int getControlMode(){
        return SpSettingBusiness.getInstance().getControlMode(UnityActivity.INSTANCE);
    }

    /**
     * 设置控制模式
     * @param mode 默认0：头控+手柄，1：纯手柄
     */
    public static void setControlMode(int mode){
        SpSettingBusiness.getInstance().setControlMode(UnityActivity.INSTANCE, mode);
    }

    /**
     * 手柄左右手 0：左手  1： 右手
     * @return  默认为 右手
     */
    public static  int getHandContrl(){
     return Settings.Secure.getInt(UnityActivity.INSTANCE.getContentResolver(),HANDCONTRL_KEY,HANDCONTRL_RIGHT);
    }



    public static void clearOtherApp(){
        //To change body of implemented methods use File | Settings | File Templates.
        ActivityManager am = (ActivityManager) UnityActivity.INSTANCE.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> infoList = am.getRunningAppProcesses();
        List<ActivityManager.RunningServiceInfo> serviceInfos = am.getRunningServices(100);

        long beforeMem = getAvailMemory();
        int count = 0;
        if (infoList != null) {
            for (int i = 0; i < infoList.size(); ++i) {
                ActivityManager.RunningAppProcessInfo appProcessInfo = infoList.get(i);
//                Log.d(TAG, "process name : " + appProcessInfo.processName);
                //importance 该进程的重要程度  分为几个级别，数值越低就越重要。
//                Log.d(TAG, "importance : " + appProcessInfo.importance);

                // 一般数值大于RunningAppProcessInfo.IMPORTANCE_SERVICE的进程都长时间没用或者空进程了
                // 一般数值大于RunningAppProcessInfo.IMPORTANCE_VISIBLE的进程都是非可见进程，也就是在后台运行着
                if (appProcessInfo.importance > ActivityManager.RunningAppProcessInfo.IMPORTANCE_VISIBLE) {
                    String[] pkgList = appProcessInfo.pkgList;
                    for (int j = 0; j < pkgList.length; ++j) {//pkgList 得到该进程下运行的包名
//                        Log.d(TAG, "It will be killed, package name : " + pkgList[j]);
                        if(pkgList[j].equals("com.baofeng.mj")
                                || pkgList[j].equals("com.baofeng.fota")
                                || pkgList[j].equals("com.baofeng.mj.vrplayer")){
                            continue;
                        }
                        am.killBackgroundProcesses(pkgList[j]);
                        count++;
                    }
                }
                String[] pkgList = appProcessInfo.pkgList;
                for (int j = 0; j < pkgList.length; ++j) {//pkgList 得到该进程下运行的包名
//                        Log.d(TAG, "It will be killed, package name : " + pkgList[j]);
                }

            }
        }

        long afterMem = getAvailMemory();
//        Toast.makeText(ClearMemoryActivity.this, "clear " + count + " process, "
//                + (afterMem - beforeMem) + "M", Toast.LENGTH_LONG).show();
        if(null != UnityActivity.INSTANCE && null != UnityActivity.INSTANCE.getIAndroidCallback()){
            UnityActivity.INSTANCE.getIAndroidCallback().sendClearOtherApp(count,(afterMem - beforeMem));
        }
    }

    //获取可用内存大小
    private static long getAvailMemory() {
        // 获取android当前可用内存大小
        ActivityManager am = (ActivityManager) UnityActivity.INSTANCE.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        am.getMemoryInfo(mi);
        //mi.availMem; 当前系统的可用内存
        //return Formatter.formatFileSize(context, mi.availMem);// 将获取的内存大小规格化
        return mi.availMem / (1024 * 1024);
    }

    /**
     * 获取背景图片
     * @param packageName
     * @return
     */
    public static String getAppBgIcon(String packageName){
        String iconBg = "";
        File file = new File(getRootPath() + "/APPIcon/");
        if(file.exists() && file.isDirectory()){
            File[] files = file.listFiles();
            String path;
            for(File f : files){
                path = subPath(f.getName());
                if((packageName+"bj").equals(path)){
                    iconBg = f.getAbsolutePath();
                }
            }
        }
        return iconBg;
    }

    /**
     * 获取应用icon
     * @param packageName
     * @return
     */
    public static String getAppIcon(String packageName){
        String icon = "";
        File file = new File(getRootPath() + "/APPIcon/");
        if(file.exists() && file.isDirectory()){
            File[] files = file.listFiles();
            String path;
            for(File f : files){
                path = subPath(f.getName());
                if(packageName.equals(path)){
                    icon = f.getAbsolutePath();
                }
            }
        }
        return icon;
    }

    private static String getRootPath() {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            return Environment.getExternalStorageDirectory().getAbsolutePath();
        } else {
            return "";
        }
    }

    private static String subPath(String path){
        if(!TextUtils.isEmpty(path)){
            String[] strings = path.split(".png");
            return strings[0];
        }
        return "";
    }

    /**
     * 发送QVR_INIT_COMPLETED广播
     */
    public static void sendBroadcastQVR(){
        Intent intent=new Intent("android.intent.action.QVR_INIT_COMPLETED");
        UnityActivity.INSTANCE.sendBroadcast(intent);
    }


    private void getRunningAppProcessInfo() {
        ActivityManager am = (ActivityManager) UnityActivity.INSTANCE.getSystemService(Context.ACTIVITY_SERVICE);

        //获得系统里正在运行的所有进程
        List<ActivityManager.RunningAppProcessInfo> runningAppProcessesList = am.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo runningAppProcessInfo : runningAppProcessesList) {
            String[] pkgList = runningAppProcessInfo.pkgList;
//            String packageName =
            // 进程ID号
            int pid = runningAppProcessInfo.pid;
            // 用户ID
            int uid = runningAppProcessInfo.uid;
            // 进程名
            String processName = runningAppProcessInfo.processName;
            // 占用的内存
            int[] pids = new int[] {pid};
            Debug.MemoryInfo[] memoryInfo = am.getProcessMemoryInfo(pids);
            int memorySize = memoryInfo[0].dalvikPrivateDirty;

            System.out.println("processName="+processName+",pid="+pid+",uid="+uid+",memorySize="+memorySize+"kb");
        }
    }

    public static boolean isPlayAudioByBackground(){
        return isDefaultMusicActive;
    }

    public static void setDefaultMusicActive(boolean active){
        isDefaultMusicActive = active;
    }

    /**
     * 横屏接口URL
     *
     * @return
     */
    public static String horizonServiceUrl() {
        return "";
    }



    /*
    * 获取屏幕模式
    */
    public static boolean getOrientationMode() {
        return isPortraitOrLandscape;
    }

    /*
     * 设置屏幕模式
     */
    public static void setOrientationMode(boolean flag) {
        isPortraitOrLandscape = flag;
    }

    /*
    *获取魔镜5代连接状态
    * @return ture:连接，false：断开
     */
    public static boolean getBFMJ5Connect() {
        return false;
    }

    /**
     * @return  deviceType
     */
    public static int getInputDeviceType(){

        return 1;
    }

    /**
     * 获取网络，0：无网络，1：WiFi，2：2G，3：3G，4：4G
     */
    public static int getNetwork() {
        int currentNetwork = NetworkUtil.getNetwork(UnityActivity.INSTANCE);
        return NetworkUtil.convertNetwork(currentNetwork);
    }

    /**
     * 获取测试的文件夹
     *
     * @return
     */
    public static String unityHierarchy() {
        return "";
    }

    public static void clickToNative() {
        ReportUtil.reportTimer("0");
        Intent nullIntent = new Intent(UnityActivity.INSTANCE, MainActivity.class);
        UnityActivity.INSTANCE.startActivity(nullIntent);
    }
}
