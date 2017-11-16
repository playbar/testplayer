package com.baofeng.mj.unity;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.KeyEvent;

import com.baofeng.mj.vrplayer.MyAppliaction;
import com.baofeng.mj.vrplayer.business.flyscreen.FlyScreenBusiness;
import com.baofeng.mj.vrplayer.publicc.ActivityUtil;
import com.baofeng.mj.vrplayer.publicc.AppContant;
import com.baofeng.mj.vrplayer.receiver.BatteryStateReceiver;
import com.baofeng.mj.vrplayer.receiver.BlankScreenReceiver;
import com.baofeng.mj.vrplayer.util.AudioManagerUtil;
import com.baofeng.mj.vrplayer.util.CheckPermission;
import com.baofeng.mj.vrplayer.util.PermissionListener;
import com.baofeng.mj.vrplayer.util.PermissionUtil;
import com.baofeng.mj.vrplayer.util.ReportUtil;
import com.baofeng.mj.vrplayer.util.StickUtil;
import com.baofeng.mj.vrplayer.util.SystemUtil;
import com.baofeng.mojing.unity.MojingVrActivity;

/**
 * @author liuchuanchi
 * @description: UnityActivity，android与unity交互
 */
public class UnityActivity extends MojingVrActivity {
    public static UnityActivity INSTANCE;
    public static boolean isActive = false;
    private IAndroidCallback iAndroidCallback;//Android与Unity交互，Android回调
    private String hierarchyString;
    private boolean manualDisconnectJoystick  = false;
    private HomeListener mHomeListener;
    private int mJoystickCount = 0;
    private int mMotionCount = 0;
//    private TestReceiver receiver;//测试耳机拔插行为
    private int mLastValue;

    private long mResumeTimes = 0;
    private long mPauseTimes = 0;

    private BlankScreenReceiver blackScreenReceiver;//黑屏广播
    private BatteryStateReceiver batteryStateReceiver;//监听电量广播
    private int batteryStatus; //电池状态：充电，放电，充电完成
    private int batteryLevel;//电池电量
    private int batteryScale;//电池最大电量
    private int batteryTemperature;//电池温度
    public FlyScreenBusiness flyScreenBusiness;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyAppliaction.getInstance().setOrientationMode(false);
        ActivityUtil.getInstance().addActivity(this);
        //检测是否有背景声音
        UnityPublicBusiness.setDefaultMusicActive(isMusicActive());
        INSTANCE = this;
        hierarchyString = getIntent().getStringExtra("hierarchy");

//        UnityDownloadBusiness.startAllInstall();
        initHomeListen();
        mHomeListener.start();
        registerBroadcastReceiver();//注册广播接收者
    }

    @Override
    protected void onResume() {
        isActive = true;
        UnityPublicBusiness.setDefaultMusicActive(isMusicActive());
        MyAppliaction.getInstance().setOrientationMode(false);
      //  MojingSDK.StartTracker(100);
        super.onResume();
        manualDisconnectJoystick = false;
        if (AppContant.isRunningInBackground) {
            AppContant.isRunningInBackground = false;
            AppContant.comeInTime = System.currentTimeMillis();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        getIntent().putExtras(intent);
        hierarchyString = getIntent().getStringExtra("hierarchy");
        String json = getIntent().getStringExtra("json");
        if (json != null && !json.isEmpty()){
            iAndroidCallback.sendPlayLocalMovie(json);
        }
    }

    @Override
    protected void onDestroy() {
        ActivityUtil.getInstance().removeActivty(this);
        unregisterBroadcastReceiver();//取消广播接收者
        mHomeListener.stop();
        stopBluetooth();
        super.onDestroy();
    }
    @Override
    protected void onPause()
    {
        manualDisconnectJoystick = true;
       // MojingSDK.StopTracker();
        super.onPause();
        isActive = false;
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (ActivityUtil.getInstance().isBackground(this)) {
            AppContant.isRunningInBackground = true;
            ReportUtil.reportTimer("0");
        } else {
            AppContant.isRunningInBackground = false;
        }
    }

    public String getHierarchyString(){
        return hierarchyString;
    }
    public void SetHierarchyString(){
        hierarchyString = null;
    }

    @Override
    public void onMojingDeviceAttached(String deviceName)
    {
        System.out.println("====UnityActivity=onMojingDeviceAttached:" + deviceName);

        if (!StickUtil.blutoothEnble()) {// 蓝牙关闭
            System.out.println("====UnityActivity=onMojingDeviceAttached:0" );

            return;
        }
        System.out.println("====UnityActivity=onMojingDeviceAttached:1" );

        int inputDeviceType = 0;
        if(deviceName.contains("mojing-motion")){//体感手柄
            System.out.println("====UnityActivity=onMojingDeviceAttached:2" );

            inputDeviceType = 2;// 3;   // 走onMojingDeviceAttached 都被认为是普通手柄，此方法为HID协议出口，GATT走Google原生途径
        }
        else {
            System.out.println("====UnityActivity=onMojingDeviceAttached:3" );

            String model = SystemUtil.getMobileModel();
            if (!StickUtil.filterDeviceName(deviceName)) {
                System.out.println("====UnityActivity=onMojingDeviceAttached:4" );

                return;
            }
            System.out.println("====UnityActivity=onMojingDeviceAttached:5" );

            mJoystickCount++;
            StickUtil.getInstance(UnityActivity.INSTANCE).setJoystickName(deviceName);
            inputDeviceType = 2;
        }
        System.out.println("====UnityActivity=onMojingDeviceAttached:6" );

//        if (iAndroidCallback != null) {//通知Unity
//            System.out.println("====UnityActivity=onMojingDeviceAttached:7" );
//
//            if(inputDeviceType == 2) {
//                System.out.println("====UnityActivity=onMojingDeviceAttached:8" );
//
//                if(BaseApplication.INSTANCE.getInputDeviceType()==3){
//                    System.out.println("====UnityActivity=onMojingDeviceAttached:9" );
//
//                    inputDeviceType = 3;
//                }
//                System.out.println("====UnityActivity=onMojingDeviceAttached:10" );
//
//                iAndroidCallback.sendJoystickStatus(UnityPublicBusiness.getJoystickStatus());
//            }
//            System.out.println("====UnityActivity=onMojingDeviceAttached:" + inputDeviceType);
//            System.out.println("====UnityActivity=onMojingDeviceAttached:11" );
//
//            iAndroidCallback.sendInputDeviceType(inputDeviceType);
//        }else {
//            System.out.println("====UnityActivity=onMojingDeviceAttached:12" );
//
//            if (inputDeviceType == 2){
//                System.out.println("====UnityActivity=onMojingDeviceAttached:13" );
//
//                if(BaseApplication.INSTANCE.getInputDeviceType()==3)
//                {
//                    System.out.println("====UnityActivity=onMojingDeviceAttached:14" );
//
//                    inputDeviceType = 3;
//                }
//
//            }
//        }
//        System.out.println("====UnityActivity=onMojingDeviceAttached:15" );
//
//        BaseApplication.INSTANCE.setInputDeviceType(inputDeviceType);
//        System.out.println("====UnityActivity=onMojingDeviceAttached:16" );

    }

    @Override
    public void onMojingDeviceDetached(String deviceName) {
        System.out.println("====UnityActivity=onMojingDeviceDetached:" + deviceName);
        int inputDeviceType = 0;
        if (deviceName.contains("mojing-motion")) {//体感手柄
            inputDeviceType = 2;//3; // 走onMojingDeviceAttached 都被认为是普通手柄，此方法为HID协议出口，GATT走Google原生途径
        } else {
            String model = SystemUtil.getMobileModel();
            if (!StickUtil.filterDeviceName(deviceName)) {
                return;
            }
            mJoystickCount--;

            StickUtil.getInstance(UnityActivity.INSTANCE).removeJoystickName(deviceName);
            inputDeviceType = 2;
//            if (manualDisconnectJoystick) {
//                manualDisconnectJoystick = false;
//                return;
//            }
        }
//        if (iAndroidCallback != null) {//通知Unity
//            if(inputDeviceType == 2){
//                if(BaseApplication.INSTANCE.getInputDeviceType() == 3)
//                    inputDeviceType = 3;
//                else
//                    inputDeviceType = 0;
//                iAndroidCallback.sendJoystickStatus(UnityPublicBusiness.getJoystickStatus());
//            }else if(inputDeviceType == 3){
//                if(UnityPublicBusiness.getJoystickStatus())
//                    inputDeviceType = 2;
//                else
//                    inputDeviceType = 0;
//            }
//            iAndroidCallback.sendInputDeviceType(inputDeviceType);
//        }
//        else
//        {
//            if(inputDeviceType == 3)
//            {
//                if(UnityPublicBusiness.getJoystickStatus())
//                    inputDeviceType = 2;
//            }
//            else
//                inputDeviceType = 0;
//        }
//        System.out.println("====UnityActivity=onMojingDeviceDetached.inputDeviceType:" + inputDeviceType);
//
//        BaseApplication.INSTANCE.setInputDeviceType(inputDeviceType);
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            // 音量减小
            case KeyEvent.KEYCODE_VOLUME_DOWN:
            // 音量增大
            case KeyEvent.KEYCODE_VOLUME_UP:
                // 获取手机当前音量值
                int volume = AudioManagerUtil.getInstance().getStreamCurrentVolume(UnityActivity.INSTANCE);
                if(null != iAndroidCallback){
                    iAndroidCallback.sendCurrentVolume(volume);
                }
                break;
//            case KeyEvent.KEYCODE_BACK:
//                UnityPublicBusiness.clickToNative();
//                break;

        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * Android与Unity交互，添加Android回调
     */
    public void addIAndroidCallback(IAndroidCallback iAndroidCallback) {
        this.iAndroidCallback = iAndroidCallback;
    }

    /**
     * Android与Unity交互，获取Android回调
     */
    public IAndroidCallback getIAndroidCallback() {
        return iAndroidCallback;
    }

    private void initHomeListen(){
        mHomeListener = new HomeListener(this);
        mHomeListener.setOnHomeBtnPressListener( new HomeListener.OnHomeBtnPressListener() {
            @Override
            public void onHomeBtnPress() {
              if(null != iAndroidCallback){
                  iAndroidCallback.sendHomeIsPress(1);
              }
            }

            @Override
            public void onHomeBtnLongPress() {
                if(null != iAndroidCallback){
                    iAndroidCallback.sendHomeIsPress(2);
                }

            }
        });
    }

    private void checkPermission(){
        CheckPermission.from(this)
                .setPermissions(PermissionUtil.ALL_PERMISSIONS)
                .setPermissionListener(new PermissionListener(){
                    @Override
                    public void permissionGranted() {
                        if (PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(UnityActivity.this,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                        }
                    }
                    @Override
                    public void permissionDenied() {
                        if (PackageManager.PERMISSION_DENIED == ContextCompat.checkSelfPermission(UnityActivity.this,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                        }
                    }
                }).check();
    }

    @TargetApi(value = 23)
    private void requesetPermission() {
        int permissionCheck1 = this.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
        int permissionCheck2 = this.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionCheck1 != PackageManager.PERMISSION_GRANTED || permissionCheck2 != PackageManager.PERMISSION_GRANTED) {
            this.requestPermissions(
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE},
                    1000);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1000: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d("requesetPermission", "onRequestPermission grant");
                } else {
                    Log.d("requesetPermission", "onRequestPermission dened");
                }
            }
            return;
        }
    }

    public void startBluetooth(){
        try {
            AudioManager mAudioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
            mAudioManager.setBluetoothScoOn(true);
            mAudioManager.startBluetoothSco();
        } catch (Exception e){e.printStackTrace();}
    }

    public void stopBluetooth(){
        try {
            AudioManager mAudioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
            mAudioManager.setBluetoothScoOn(false);
            mAudioManager.stopBluetoothSco();
        } catch (Exception e){e.printStackTrace();}
    }

    private boolean isMusicActive(){
        AudioManager am = (AudioManager) getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
        return am.isMusicActive();
    }

    /**
     * 设置电池状态
     * @param status
     */
    public void setBatteryStatus(int status){
        this.batteryStatus = status;
    }

    public int getBatteryStatus(){
        return batteryStatus;
    }

    /**
     * 设置电池电量
     */
    public void setBatteryLevel(int batteryLevel) {
        this.batteryLevel = batteryLevel;
    }

    /**
     * 获取电池电量 0 - 100
     */
    public int getBatteryLevel() {
        if (batteryScale == 0) {
            return 0;
        }
        return batteryLevel * 100 / batteryScale;
    }

    /**
     * 设置电池最大电量
     */
    public void setBatteryScale(int batteryScale) {
        this.batteryScale = batteryScale;
    }

    /**
     * 设置电池温度
     */
    public void setBatteryTemperature(int batteryTemperature) {
        this.batteryTemperature = batteryTemperature;
    }

    /**
     * 获取电池温度
     */
    public String getBatteryTemperature() {
        int tens = batteryTemperature / 10;
        return Integer.toString(tens) + "." + (batteryTemperature - 10 * tens);
    }

    /**
     * true电池温度过高，发出警告，false不用发出警告
     */
    public boolean isBatteryTemperatureWarning() {
        if (batteryTemperature > 600) {
            return true;
        }
        return false;
    }

    /**
     * 注册广播接收者
     */
    private void registerBroadcastReceiver(){
        //电量状态广播
        IntentFilter batteryStateFilter = new IntentFilter();
        batteryStateFilter.addAction(Intent.ACTION_BATTERY_CHANGED);
        batteryStateFilter.addAction(Intent.ACTION_POWER_CONNECTED);
        batteryStateFilter.addAction(Intent.ACTION_POWER_DISCONNECTED);
        batteryStateReceiver = new BatteryStateReceiver();
        registerReceiver(batteryStateReceiver, batteryStateFilter);
        //黑屏广播
        IntentFilter blackScreenFilter = new IntentFilter();
        blackScreenFilter.addAction("android.intent.action.PRE_SCREEN_OFF");
        blackScreenReceiver = new BlankScreenReceiver();
        registerReceiver(blackScreenReceiver,blackScreenFilter);
    }

    /**
     * 取消广播接收者
     */
    private void unregisterBroadcastReceiver(){
        unregisterReceiver(batteryStateReceiver);
        unregisterReceiver(blackScreenReceiver);
    }


}
