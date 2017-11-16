package com.baofeng.mj.vrplayer.business;

import android.content.Context;

import com.baofeng.mj.vrplayer.util.BrightnessUtil;
import com.baofeng.mj.vrplayer.util.SecurePreferences;
import com.baofeng.mj.vrplayer.util.SoundUtils;


/**
 * @author liuchuanchi
 * @description: 设置相关的保存业务
 */
public class SpSettingBusiness {
    private static SpSettingBusiness instance;
    private SecurePreferences securePreferences;
    private SecurePreferences.Editor editor;

    private SpSettingBusiness() {
    }

    public static SpSettingBusiness getInstance() {
        if (instance == null) {
            instance = new SpSettingBusiness();
        }
        return instance;
    }

    /**
     * 初始化SecurePreferences
     */
    private void initSecurePreferences(Context mContext) {
        if (securePreferences == null) {
            String spName = mContext.getPackageName() + ".setting";
            securePreferences = new SecurePreferences(mContext,  spName, Context.MODE_PRIVATE);
            editor = securePreferences.edit();
        }
    }


    /**
     * 保存上次设置的声音
     * @param userCurVolume 0-100
     */
    public void setCurrentVolume(Context mContext, int userCurVolume) {
        initSecurePreferences(mContext);
        editor.putInt("currentVolumeMode", userCurVolume);
        editor.commit();
    }

    /**
     * 获取上次保存的声音大小
     * @return 0-100
     */
    public int getCurrentVolume(Context mContext) {
        initSecurePreferences(mContext);
        return securePreferences.getInt("currentVolumeMode", 0);
    }

    /**
     * 设置亮度模式
     * @param brightnessMode 0：自动 1：手动
     */
    public void setBrightnessMode(Context mContext, int brightnessMode) {
        initSecurePreferences(mContext);
        editor.putInt("brightnessMode", brightnessMode);
        editor.commit();
    }

    /**
     * 获取亮度模式
     * @return 0：自动 1：手动
     */
    public int getBrightnessMode(Context mContext) {
        initSecurePreferences(mContext);
        return securePreferences.getInt("brightnessMode", 0);
    }

    /**
     * 设置亮度值
     * @param brightnessValue 亮度值 0 - 255
     */
    public void setBrightnessValue(Context mContext, int brightnessValue) {
        initSecurePreferences(mContext);
        editor.putInt("brightnessValue", brightnessValue);
        editor.commit();
    }

    /**
     * 获取亮度值
     * @return 亮度值 0 - 255
     */
    public int getBrightnessValue(Context mContext) {
        initSecurePreferences(mContext);
        int value = securePreferences.getInt("brightnessValue", -1);
        if (value < 0){
            value = BrightnessUtil.isAutoBrightnessMode(mContext) ? (int)(255 * 0.3) : BrightnessUtil.getSysBrightnessValue(mContext);
        }
        return value;
    }

    /**
     * 设置系统亮度值
     * @param brightnessValue 亮度值 0 - 255
     */
    public void setSystemBrightnessValue(Context mContext, int brightnessValue) {
        initSecurePreferences(mContext);
        editor.putInt("systemBrightnessValue", brightnessValue);
        editor.commit();
    }

    /**
     * 获取系统亮度值
     * @return 亮度值 0 - 255
     */
    public int getSystemBrightnessValue(Context mContext) {
        initSecurePreferences(mContext);
        return securePreferences.getInt("systemBrightnessValue", BrightnessUtil.getSysBrightnessValue(mContext));
    }

    /**
     * 增强模式二、三代
     * @param id（1为720p，2为二代，3为三代）
     */
    public void setStrongMode(Context mContext, int id) {
        initSecurePreferences(mContext);
        editor.putInt("strongMode", id);
        editor.commit();
    }

    /**
     * 获取增强模式
     * @return
     */
    public int getStrongMode(Context mContext) {
        initSecurePreferences(mContext);
        return securePreferences.getInt("strongMode", 2);
    }

    /**
     * 反锯齿开关
     * @param anti_aliasing
     */
    public void setAnti_aliasing(Context mContext, int anti_aliasing) {
        initSecurePreferences(mContext);
        editor.putInt("anti_aliasing", anti_aliasing);
        editor.commit();
    }

    /**
     * 获取是否设置反锯齿，0：未设置，1：设置
     * @return
     */
    public int getAnti_aliasing(Context mContext) {
        initSecurePreferences(mContext);
        return securePreferences.getInt("anti_aliasing", 0);
    }

    /**
     * 曲面开关
     * @param surSwitch
     */
    public void setSur_Switch(Context mContext, int surSwitch) {
        initSecurePreferences(mContext);
        editor.putInt("surSwitch", surSwitch);
        editor.commit();
    }

    /**
     * 获取是否设置曲面，0：未设置，1：设置
     * @return
     */
    public int getSur_Switch(Context mContext) {
        initSecurePreferences(mContext);
        return securePreferences.getInt("surSwitch", 1);
    }

    /**
     * 球模背景开关
     * @param bgSwitch
     */
    public void setBgSwitch(Context mContext, int bgSwitch) {
        initSecurePreferences(mContext);
        editor.putInt("bgSwitch", bgSwitch);
        editor.commit();
    }

    /**
     * 获取是否设置球模背景，0：未设置，1：设置
     * @return
     */
    public int getBgSwitch(Context mContext) {
        initSecurePreferences(mContext);
        return securePreferences.getInt("bgSwitch", 1);
    }

    /**
     * 过渡动画特效开关
     * @param transAniSwitch
     */
    public void setTrans_Ani_Switch(Context mContext, int transAniSwitch) {
        initSecurePreferences(mContext);
        editor.putInt("transAniSwitch", transAniSwitch);
        editor.commit();
    }

    /**
     * 获取是否设置过渡动画特效，0：未设置，1：设置
     * @return
     */
    public int getTrans_Ani_Switch(Context mContext) {
        initSecurePreferences(mContext);
        return securePreferences.getInt("transAniSwitch", 0);
    }

    /**
     * 设置透明效果
     * @param transSwitch
     */
    public void setTrans_Switch(Context mContext, int transSwitch) {
        initSecurePreferences(mContext);
        editor.putInt("transSwitch", transSwitch);
        editor.commit();
    }

    /**
     * 获取是否设置透明效果，0：未设置，1：设置
     * @return
     */
    public int getTrans_Switch(Context mContext) {
        initSecurePreferences(mContext);
        return securePreferences.getInt("transSwitch", 0);
    }

    /**
     * 设置Mask特效
     * @param mask
     */
    public void setMask(Context mContext, int mask) {
        initSecurePreferences(mContext);
        editor.putInt("mask", mask);
        editor.commit();
    }

    /**
     * 获取是否设置Mask特效效果，0：未设置，1：设置
     * @return
     */
    public int getMask(Context mContext) {
        initSecurePreferences(mContext);
        return securePreferences.getInt("mask", 0);
    }

    /**
     * 设置高清测试结果
     */
    public void setHigh(Context mContext, int high) {
        initSecurePreferences(mContext);
        editor.putInt("high", high);
        editor.commit();
    }

    /**
     * 获取高清测试结果
     */
    public int getHigh(Context mContext) {
        initSecurePreferences(mContext);
        return securePreferences.getInt("high", -1);
    }

    /**
     * 设置曲面
     */
    public void setHook(Context mContext, int hook) {
        initSecurePreferences(mContext);
        editor.putInt("hook", hook);
        editor.commit();
    }

    /**
     * 获取曲面
     */
    public int getHook(Context mContext) {
        initSecurePreferences(mContext);
        return securePreferences.getInt("hook", -1);
    }

    /**
     * 设置otg
     */
    public void setOtg(Context mContext, int otg) {
        initSecurePreferences(mContext);
        editor.putInt("otg", otg);
        editor.commit();
    }

    /**
     * 获取otg
     */
    public int getOtg(Context mContext) {
        initSecurePreferences(mContext);
        return securePreferences.getInt("otg", -1);
    }

    /**
     * 设置控制方式
     * @param mode 默认头控加手柄：0 纯手柄：1
     */
    public void setControlMode(Context mContext, int mode){
        initSecurePreferences(mContext);
        editor.putInt("control_mode", mode);
        editor.commit();
    }

    /**
     * 获取控制方式
     * @return
     */
    public int getControlMode(Context mContext){
        initSecurePreferences(mContext);
        return securePreferences.getInt("control_mode", 0);
    }

    /**
     * 设置在线播放操控模式
     * @param mode 0: 极简模式  1：沉浸模式
     */
    public void setPlayerMode(Context mContext, int mode){
        initSecurePreferences(mContext);
        editor.putInt("player_mode", mode);
        editor.commit();
    }

    /**
     * 获取在线播放操控模式
     * @return 0: 极简模式  1：沉浸模式  -1：无选择
     */
    public int getPlayerMode(Context mContext){
        initSecurePreferences(mContext);
        return  securePreferences.getInt("player_mode",-1);
    }

    /**
     * 设置选择的场景
     * @param
     */
    public void setSkyboxIndex(Context mContext, int index){
        initSecurePreferences(mContext);
        editor.putInt("skyboxIndex", index);
        editor.commit();
    }

    /**
     * 获取选择的场景
     * @return
     */
    public int getSkyboxIndex(Context mContext){
        initSecurePreferences(mContext);
        return  securePreferences.getInt("skyboxIndex",0);
    }

    public void setLeftMode(Context mContext, boolean isLeft) {
        initSecurePreferences(mContext);
        editor.putBoolean("lefMode", isLeft);
        editor.commit();
    }

    public boolean getLeftMode(Context mContext) {
        initSecurePreferences(mContext);
        return securePreferences.getBoolean("lefMode", false);
    }

    /**
     * 首次锁屏提示
     * @return
     */
   public boolean getGLPlayerFirstLockTip(Context mContext){
       initSecurePreferences(mContext);
      return securePreferences.getBoolean("gl_player_first_locked",true);
   }
    public void setGLPlayerFirstLockTip(Context mContext, boolean isFirstlocked){
        initSecurePreferences(mContext);
        editor.putBoolean("gl_player_first_locked", isFirstlocked);
        editor.commit();
    }

    /**
     * 首次解锁提示
     * @return
     */
    public boolean getGLPlayerFirstUnLockTip(Context mContext){
        initSecurePreferences(mContext);
        return securePreferences.getBoolean("gl_player_first_unlocked",true);
    }
    public void setGLPlayerFirstUnLockTip(Context mContext, boolean isFirstunlocked){
        initSecurePreferences(mContext);
        editor.putBoolean("gl_player_first_unlocked", isFirstunlocked);
        editor.commit();
    }

    public void setPlayerSoundValue(Context mContext, int value){
        initSecurePreferences(mContext);
        editor.putInt("player_sound_value_prefence",value);
        editor.commit();
    }
    public int getPlayerSoundValue(Context mContext){
        initSecurePreferences(mContext);
        int defvalue = SoundUtils.GetCurrentVolumePercent(mContext);
        return securePreferences.getInt("player_sound_value_prefence",defvalue);
    }

    public void setPlayerSoundMute(Context mContext, boolean isMute){
        initSecurePreferences(mContext);
        editor.putBoolean("player_sound_ismute",isMute);
        editor.commit();
    }

    public boolean getPlayerSoundMute(Context mContext){
        initSecurePreferences(mContext);
        return securePreferences.getBoolean("player_sound_ismute",false);
    }

    /**
     * 播放屏幕大小模式
     * @return
     */
//    public int getPlayerScreenType(Context mContext){
//        initSecurePreferences(mContext);
//        return securePreferences.getInt(PLAYER_SCREEN_TYPE, VideoModeType.Normal_Screen);
//    }

    /**
     * 存储播放屏幕大小模式
     * @param type
     */
    public void setPlayerScreenType(Context mContext, int type){
        initSecurePreferences(mContext);
        editor.putInt("player_screen_type",type);
        editor.commit();
    }

    /**
     * 本地播放视频格式提示
     * @return
     */
    public int getLocalVideoTypeCount(Context mContext){
        initSecurePreferences(mContext);
        return securePreferences.getInt("local_video_type_tip_count",0);
    }

    public void setLocalVideoTypeTipCount(Context mContext, int count){
        initSecurePreferences(mContext);
        editor.putInt("local_video_type_tip_count",count);
        editor.commit();
    }

    /**
     * 服务器与本地时间戳的差值
     * @return 毫秒
     */
    public int getDiffTime(Context mContext){
        initSecurePreferences(mContext);
        return securePreferences.getInt("server_local_diff_time", 0);
    }

    public void setDiffTime(Context mContext, int time){
        initSecurePreferences(mContext);
        editor.putInt("server_local_diff_time", time);
        editor.commit();
    }

    public void setPlayerLockScreen(Context mContext, boolean islocked){
        initSecurePreferences(mContext);
        editor.putBoolean("player_lock_screen", islocked);
        editor.commit();
    }

    public boolean getPlayerLockScreen(Context mContext){
        initSecurePreferences(mContext);
        return securePreferences.getBoolean("player_lock_screen", false);
    }

    public void setPlayDetailIsFirst(Context mContext, int isFirst){
        initSecurePreferences(mContext);
        editor.putInt("report_play_detail_first", isFirst);
        editor.commit();
    }
    public int getPlayDetailIsFirst(Context mContext){
        initSecurePreferences(mContext);
        return securePreferences.getInt("report_play_detail_first", 1);
    }
}
