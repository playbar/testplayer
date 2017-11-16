package com.mojing.vrplayer.publicc;

import android.content.Context;
import android.text.TextUtils;

import com.baofeng.mj.sdk.gvr.vrcore.entity.GlassesNetBean;
import com.baofeng.mj.sdk.gvr.vrcore.entity.GlassesSdkBean;
import com.baofeng.mj.sdk.gvr.vrcore.utils.GlassesManager;
import com.mojing.vrplayer.bean.VideoModeType;
import com.mojing.vrplayer.utils.SoundUtils;

import org.json.JSONObject;


/**
 * @author liuchuanchi
 * @description: 设置保存业务
 */
public class SettingSpBusiness {
    public static final String PUBLIC_GLASSES_IDS_PREFENCE = "public_glasses_ids_prefence";
    //声音
    public static final String  PLAYER_SOUND_VALUE_PREFENCE = "player_sound_value_prefence";
    //是否静音
    public static final String PLAYER_SOUND_ISMUTE="player_sound_ismute";
    public static final String PLAYER_SCREEN_TYPE = "player_screen_type"; //播放屏幕模式
    public static final String LOCAL_VIDEO_TYPE_TIP_COUNT = "local_video_type_tip_count";//本地视频类型识别提示
    public static final String SERVER_LOCAL_DIFF_TIME = "server_local_diff_time";//本地视频类型识别提示
    public static final String PLAYER_LOCK_SCREEN = "player_lock_screen";//播放锁屏
    public static final String REPORT_PLAYER_DETAIL_FIRST = "report_play_detail_first";//详情第一次播放报数
    public static final String PLAYER_IS_SINGLE = "player_is_single";
    public static final String PLAYER_IS_SINGLE_UNITY = "PLAYER_IS_SINGLE_UNITY";
    private static SettingSpBusiness instance;
    private SecurePreferences securePreferences;
    private SecurePreferences.Editor editor;
    private Context mContext;
    private SettingSpBusiness(Context context) {
        mContext = context.getApplicationContext();
    }

    public static SettingSpBusiness getInstance(Context context) {
        if (instance == null) {
            instance = new SettingSpBusiness(context);
        }
        return instance;
    }

    /**
     * 初始化SecurePreferences
     */
    private void initSecurePreferences() {
        if (securePreferences == null) {
            securePreferences = new SecurePreferences(mContext.getPackageName() + ".setting", Context.MODE_PRIVATE,mContext);
            editor = securePreferences.edit();
        }
    }


    /**
     * 保存上次设置的声音
     *
     * @param userCurVolume 0-100
     */
    public void setCurrentVolume(int userCurVolume) {
        initSecurePreferences();
        editor.putInt("currentVolumeMode", userCurVolume);
        editor.commit();
    }

    /**
     * 获取上次保存的声音大小
     *
     * @return 0-100
     */
    public int getCurrentVolume() {
        initSecurePreferences();
        return securePreferences.getInt("currentVolumeMode", 0);
    }


    /**
     * 设置亮度模式
     *
     * @param brightnessMode 0：自动 1：手动
     */
    public void setBrightnessMode(int brightnessMode) {
        initSecurePreferences();
        editor.putInt("brightnessMode", brightnessMode);
        editor.commit();
    }

    /**
     * 获取亮度模式
     *
     * @return 0：自动 1：手动
     */
    public int getBrightnessMode() {
        initSecurePreferences();
        return securePreferences.getInt("brightnessMode", 0);
    }

    /**
     * 设置亮度值
     *
     * @param brightnessValue 亮度值 0 - 255
     */
    public void setBrightnessValue(int brightnessValue) {
        initSecurePreferences();
        editor.putInt("brightnessValue", brightnessValue);
        editor.commit();
    }

    /**
     * 获取亮度值
     *
     * @return 亮度值 0 - 255
     */
    public int getBrightnessValue(Context context) {
        initSecurePreferences();
        int value = securePreferences.getInt("brightnessValue", -1);
        if (value < 0){
            value = BrightnessUtil.isAutoBrightnessMode(context) ? (int)(255 * 0.3) : BrightnessUtil.getSysBrightnessValue(context);
        }
        return value;
    }

    /**
     * 设置系统亮度值
     *
     * @param brightnessValue 亮度值 0 - 255
     */
    public void setSystemBrightnessValue(int brightnessValue) {
        initSecurePreferences();
        editor.putInt("systemBrightnessValue", brightnessValue);
        editor.commit();
    }

    /**
     * 获取系统亮度值
     *
     * @return 亮度值 0 - 255
     */
    public int getSystemBrightnessValue(Context context) {
        initSecurePreferences();
        return securePreferences.getInt("systemBrightnessValue", BrightnessUtil.getSysBrightnessValue(context.getApplicationContext()));
    }

    /***
     * 飞屏设置跳过引导页
     *
     * @param skipGuide
     */
    public void setFlyScreenSkipGuide(boolean skipGuide) {
        initSecurePreferences();
        editor.putBoolean("skipGuide", skipGuide);
        editor.putBoolean("beginGuide", !skipGuide);
        editor.commit();
    }

    /**
     * 飞屏跳过引导页
     */
    public boolean getFlyScreenSkipGuide() {
        initSecurePreferences();
        return securePreferences.getBoolean("skipGuide", false);
    }

    public boolean getFlyScreenBeginStepGuide() {
        initSecurePreferences();
        return securePreferences.getBoolean("beginGuide", false);
    }

    public void setFlyScreenBeginStepGuide(boolean b) {
        initSecurePreferences();
        editor.putBoolean("beginGuide", b);
        editor.commit();
    }


    /***
     * 飞屏Session Id
     *
     * @param sessionId
     */
    public void setFlyScreenSessionId(String sessionId) {
        initSecurePreferences();
        editor.putString("sessionId", sessionId);
        editor.commit();
    }

    /***
     * 获取飞屏session ID
     *
     * @return
     */
    public String getFlyScreenSessionId() {
        initSecurePreferences();
        return securePreferences.getString("sessionId", "");
    }

    /**
     * 设置是否可以GPRS下载
     *
     * @param gprsDownload true可以，false不可以
     */
    public void setCanGPRSDownload(boolean gprsDownload) {
        initSecurePreferences();
        editor.putBoolean("canGprsDownload", gprsDownload);
        editor.commit();
    }

    /**
     * 获取是否可以GPRS下载
     *
     * @return true可以，false不可以
     */
    public boolean getCanGPRSDownload() {
        initSecurePreferences();
        return securePreferences.getBoolean("canGprsDownload", false);
    }


    public synchronized String getGlassesIds() {
        GlassesNetBean bean = GlassesManager.getGlassesNetBean();
        String ids="";
        if(null != bean){
        try {
            JSONObject json = new JSONObject();
            json.put("manufactureid", bean.getCompany_id());
            json.put("productid", bean.getProduct_id());
            json.put("glassesid", bean.getLens_id());
            ids = json.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        }

        return ids;
    }


    public synchronized void setGlassesIds(String ids) {
        initSecurePreferences();
        editor.putString(PUBLIC_GLASSES_IDS_PREFENCE, ids);
        editor.commit();
    }

    public void setString(String key, String value) {
        initSecurePreferences();
        editor.putString(key, value);
        editor.commit();
    }

    public String getString(String key) {
        initSecurePreferences();
        return securePreferences.getString(key, "");
    }

    public String getGlassesModeKey() {
        GlassesSdkBean bean = GlassesManager.getGlassesSdkBean();
        String key = "";
        if(bean != null){
            key = bean.getGlassesKey();
        }
        return key;
    }

    /**
     * 保存服务器上返回眼镜列表上的glassesId，不同于sdk中的三个Id唯一确认一款眼镜
     * @param glassesId
     */
    public void setCMSGlassesId(String glassesId){
        initSecurePreferences();
        editor.putString("cms_glassesId", glassesId);
        editor.commit();
    }

    public String getCMSGlassesId(){
        initSecurePreferences();
        return securePreferences.getString("cms_glassesId","");
    }




    /**
     * 设置是否完成引导页面
     * @param finish
     */
    public void setFinishGuide(boolean finish){
        initSecurePreferences();
        editor.putBoolean("isFirstGuide", finish);
        editor.commit();
    }

    /**
     * 设置是否完成引导页面
     */
    public boolean getFinishGuide(){
        initSecurePreferences();
        return securePreferences.getBoolean("isFirstGuide", false);
    }

    /**
     * 设置本地视频排序规则
     *
     * @param sortRule 0
     */
    public void setLocalVideoSort(int sortRule) {
        initSecurePreferences();
        editor.putInt("localVideoSort", sortRule);
        editor.commit();
    }

    public int getLocalVideoSort() {
        initSecurePreferences();
        return securePreferences.getInt("localVideoSort", 0);
    }

    /**
     * 增强模式二、三代
     *
     * @param id（1为720p，2为二代，3为三代）
     */
    public void setStrongMode(int id) {
        initSecurePreferences();
        editor.putInt("strongMode", id);
        editor.commit();
    }

    /**
     * 获取增强模式
     *
     * @return
     */
    public int getStrongMode() {
        initSecurePreferences();
        return securePreferences.getInt("strongMode", 2);
    }

    //体验报告url
    public void setReportUrl(String url) {
        initSecurePreferences();
        editor.putString("reportUrl", url);
        editor.commit();
    }

    public String getReprotUrl() {
        initSecurePreferences();
        return securePreferences.getString("reportUrl", "");
    }



    /**
     * 反锯齿开关
     *
     * @param anti_aliasing
     */
    public void setAnti_aliasing(int anti_aliasing) {
        initSecurePreferences();
        editor.putInt("anti_aliasing", anti_aliasing);
        editor.commit();
    }

    /**
     * 获取是否设置反锯齿，0：未设置，1：设置
     *
     * @return
     */
    public int getAnti_aliasing() {
        initSecurePreferences();
        return securePreferences.getInt("anti_aliasing", 0);
    }

    /**
     * 曲面开关
     *
     * @param surSwitch
     */
    public void setSur_Switch(int surSwitch) {
        initSecurePreferences();
        editor.putInt("surSwitch", surSwitch);
        editor.commit();
    }

    /**
     * 获取是否设置曲面，0：未设置，1：设置
     *
     * @return
     */
    public int getSur_Switch() {
        initSecurePreferences();
        return securePreferences.getInt("surSwitch", 1);
    }

    /**
     * 球模背景开关
     *
     * @param bgSwitch
     */
    public void setBgSwitch(int bgSwitch) {
        initSecurePreferences();
        editor.putInt("bgSwitch", bgSwitch);
        editor.commit();
    }

    /**
     * 获取是否设置球模背景，0：未设置，1：设置
     *
     * @return
     */
    public int getBgSwitch() {
        initSecurePreferences();
        return securePreferences.getInt("bgSwitch", 1);
    }

    /**
     * 过渡动画特效开关
     *
     * @param transAniSwitch
     */
    public void setTrans_Ani_Switch(int transAniSwitch) {
        initSecurePreferences();
        editor.putInt("transAniSwitch", transAniSwitch);
        editor.commit();
    }

    /**
     * 获取是否设置过渡动画特效，0：未设置，1：设置
     *
     * @return
     */
    public int getTrans_Ani_Switch() {
        initSecurePreferences();
        return securePreferences.getInt("transAniSwitch", 0);
    }

    /**
     * 设置透明效果
     *
     * @param transSwitch
     */
    public void setTrans_Switch(int transSwitch) {
        initSecurePreferences();
        editor.putInt("transSwitch", transSwitch);
        editor.commit();
    }

    /**
     * 获取是否设置透明效果，0：未设置，1：设置
     *
     * @return
     */
    public int getTrans_Switch() {
        initSecurePreferences();
        return securePreferences.getInt("transSwitch", 0);
    }

    /**
     * 设置Mask特效
     *
     * @param mask
     */
    public void setMask(int mask) {
        initSecurePreferences();
        editor.putInt("mask", mask);
        editor.commit();
    }

    /**
     * 获取是否设置Mask特效效果，0：未设置，1：设置
     *
     * @return
     */
    public int getMask() {
        initSecurePreferences();
        return securePreferences.getInt("mask", 0);
    }

    /**
     * 保存文本通知id集合
     *
     * @param txtPushIds 文本通知id集合
     */
    public void setTxtPushIds(String txtPushIds) {
        initSecurePreferences();
        editor.putString("txtPushIds", txtPushIds);
        editor.commit();
    }

    /**
     * 获取文本通知id集合
     *
     * @return
     */
    public String getTxtPushIds() {
        initSecurePreferences();
        return securePreferences.getString("txtPushIds", "");
    }

    /**
     * 保存图片通知id集合
     *
     * @param imgPushIds 图片通知id集合
     */
    public void setImgPushIds(String imgPushIds) {
        initSecurePreferences();
        editor.putString("imgPushIds", imgPushIds);
        editor.commit();
    }

    /**
     * 获取图片通知id集合
     *
     * @return
     */
    public String getImgPushIds() {
        initSecurePreferences();
        return securePreferences.getString("imgPushIds", "");
    }

    /***
     * 设置更新app时间
     *
     * @param updateTime
     */
    public void setLastUpdateTime(long updateTime) {
        initSecurePreferences();
        editor.putLong("lastUpdateTime", updateTime);
        editor.commit();
    }

    /***
     * 获取app更新时间
     *
     * @return
     */
    public long getLastUpdateTime() {
        initSecurePreferences();
        return securePreferences.getLong("lastUpdateTime", -1l);
    }

    /**
     * 存储是否需要升级应用状态
     * @param needUpdate
     */
    public void setNeedUpdate(boolean needUpdate){
        initSecurePreferences();
        editor.putBoolean("need_update", needUpdate);
        editor.commit();
    }

    /**
     * 返回是否需要升级应用状态
     * @return
     */
    public boolean getNeedUpdate(){
        initSecurePreferences();
        return securePreferences.getBoolean("need_update", false);
    }

    /**
     * 版本更新逻辑：当前版本存在（非强制）更新时，
     * 弹窗提示新版本特性提示更新。
     * 弹窗只出现一次：用户关闭后不再出现。
     * 当存在下个新版本时，再弹窗提示更新。
     * 标注对应版本是否点击取消按钮
     * @param version
     * @param dismiss
     */
    public void setDismiss(String version,boolean dismiss){
        initSecurePreferences();
        editor.putString("click_dismiss", version + "-" + dismiss);
        editor.commit();
    }

    /**
     * 获取指定版本是否点击过取消过升级，如点击过，下次主页面不再显示升级Dialog
     * @param version
     * @return
     */
    public boolean getDismiss(String version){
        initSecurePreferences();
        String info = securePreferences.getString("click_dismiss", "");
        if(!"".equals(info)){
            String[] infos = info.split("-");
            if(infos[0].equals(version)){
                if("true".equals(infos[1])){
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 设置客服信息小红点逻辑，true有新回复，false,无新回复
     *
     * @param has
     */
    public synchronized void setHasContent(boolean has) {
        initSecurePreferences();
        editor.putBoolean("hasReplay", has);
        editor.commit();
    }

    /**
     * 获取客服信息小红点逻辑，true有新回复，false,无新回复
     *
     * @return
     */
    public synchronized boolean getHasContent() {
        initSecurePreferences();
        return securePreferences.getBoolean("hasReplay", false);
    }

    /**
     * 设置高清测试结果
     */
    public void setHigh(int high) {
        initSecurePreferences();
        editor.putInt("high", high);
        editor.commit();
    }

    /**
     * 获取高清测试结果
     */
    public int getHigh() {
        initSecurePreferences();
        return securePreferences.getInt("high", -1);
    }

    /**
     * 设置曲面
     */
    public void setHook(int hook) {
        initSecurePreferences();
        editor.putInt("hook", hook);
        editor.commit();
    }

    /**
     * 获取曲面
     */
    public int getHook() {
        initSecurePreferences();
        return securePreferences.getInt("hook", -1);
    }

    /**
     * 设置otg
     */
    public void setOtg(int otg) {
        initSecurePreferences();
        editor.putInt("otg", otg);
        editor.commit();
    }

    /**
     * 获取otg
     */
    public int getOtg() {
        initSecurePreferences();
        return securePreferences.getInt("otg", -1);
    }

    /**
     * 设置vrtip
     */
    public void setVrtip(int vrtip) {
        initSecurePreferences();
        editor.putInt("vrtip", vrtip);
        editor.commit();
    }

    /**
     * 获取vrtip
     */
    public int getVrtip() {
        initSecurePreferences();
        return securePreferences.getInt("vrtip", -1);
    }

    /**
     * 设置VR提示框，不再提醒
     * @param vrtipRemember true不再提醒，false提醒
     */
    public void setVrtipRemember(boolean vrtipRemember) {
        initSecurePreferences();
        editor.putBoolean("vrtipRemember", vrtipRemember);
        editor.commit();
    }

    /**
     * 获取VR提示框，是否提醒
     */
    public boolean getVrtipRemember() {
        initSecurePreferences();
        return securePreferences.getBoolean("vrtipRemember", false);
    }

    /**
     * 记录当前tab
     */
    public void setMCurrentTab(int mCurrentTab){ //按照实际tab记录 从0开始
        initSecurePreferences();
        editor.putInt("mCurrentTab", mCurrentTab);
        editor.commit();
    }

    /***
     * 获取当前tab
     */
    public int getMCurrentTab(){
        initSecurePreferences();
        return securePreferences.getInt("mCurrentTab", 0);
    }

    //记录每个Tab最后一次点击时的具体位置
    public void setSubTabPosition(int mainTabPosition, int subTabPosition) {
        initSecurePreferences();
        editor.putInt("main_sub_resid" + mainTabPosition, subTabPosition);
        editor.commit();
    }

    //获取每个Tab最后一次点击时的具体位置
    public int getSubTabPosition(int mainTabPosition) {
        initSecurePreferences();
        return securePreferences.getInt("main_sub_resid" + mainTabPosition, 0);
    }

    //判读Tab是否点击过，如点击过，加载缓存数据
    public void setTabClickStatus(int tabPosition, int subTabPosition, int categoryTabPosition, boolean status) {
        initSecurePreferences();
        editor.putBoolean("main_tab_status" + tabPosition + subTabPosition + categoryTabPosition , status);
        editor.commit();
    }

    public boolean getTabClickStatus(int tabPosition, int subTabPosition, int categoryTab) {
        initSecurePreferences();
        return securePreferences.getBoolean("main_tab_status" + tabPosition + subTabPosition + categoryTab, false);
    }

    //是否缓存过推荐数据，重新进入应用时置空，重新获取
    public void setRecommendData(boolean isLoaded){
        initSecurePreferences();
        editor.putBoolean("recommend_status", isLoaded);
        editor.commit();
    }

    public boolean getRecommendData(){
        initSecurePreferences();
        return securePreferences.getBoolean("recommend_status", false);
    }

    //是否缓存过Home VR中的categoryTab数据，重新进入应用时置空，重新获取
    public void setHomeVRCate(boolean isLoaded){
        initSecurePreferences();
        editor.putBoolean("home_vr_status", isLoaded);
        editor.commit();
    }

    public boolean getHomeVRCate(){
        initSecurePreferences();
        return securePreferences.getBoolean("home_vr_status", false);
    }

    //是否缓存过Home 2D中的categoryTab数据，重新进入应用时置空，重新获取
    public void setHome2DCate(boolean isLoaded){
        initSecurePreferences();
        editor.putBoolean("home_2d_status", isLoaded);
        editor.commit();
    }

    public boolean getHome2DCate(){
        initSecurePreferences();
        return securePreferences.getBoolean("home_2d_status", false);
    }

    public static final int VR_MAX_TAB = 16; //VR下Category数
    public static final int TD_MAX_TAB = 5;  //2D下Category数
    public void clearTabInfo() {
        setHomeVRCate(false);
        setHome2DCate(false);
        setRecommendData(false);
        //清除Tab指定一级，二级,CategoryTab
        for (int i = 0; i < VR_MAX_TAB; i++) {
            setTabClickStatus(0, 0, i, false);
        }
        for (int i = 0; i < TD_MAX_TAB; i++) {
            setTabClickStatus(0, 2, i, false);
        }
        for (int i = 0; i < TD_MAX_TAB; i++) {
            setTabClickStatus(1, i, 0, false);
        }
    }


    /**
     * 设置控制方式
     * @param mode 默认头控加手柄：0 纯手柄：1
     */
    public void setControlMode(int mode){
        initSecurePreferences();
        editor.putInt("control_mode", mode);
        editor.commit();
    }

    /**
     * 获取控制方式
     * @return
     */
    public int getControlMode(){
        initSecurePreferences();
        return securePreferences.getInt("control_mode", 0);
    }

    /**
     * 设置在线播放操控模式
     * @param mode 0: 极简模式  1：沉浸模式
     */
    public void setPlayerMode(int mode){
        initSecurePreferences();
        editor.putInt("player_mode", mode);
        editor.commit();
    }

    /**
     * 获取在线播放操控模式
     * @return 0: 极简模式  1：沉浸模式  -1：无选择
     */
    public int getPlayerMode(){
        initSecurePreferences();
        return  securePreferences.getInt("player_mode",-1);
    }



    /**
     * 首次进入应用市场，请求接口，提示
     * @param finishGuide
     */
    public void setGameTips(boolean finishGuide){
        initSecurePreferences();
        editor.putBoolean("game_tips", finishGuide);
        editor.commit();
    }

    public boolean getGameTips(){
        initSecurePreferences();
        return  securePreferences.getBoolean("game_tips", false);
    }

    /**
     * 体感游戏下载提示，不再显示弹框
     * @param checked
     */
    public void setGameNoMoreTips(boolean checked){
        initSecurePreferences();
        editor.putBoolean("game_no_more_tips", checked);
        editor.commit();
    }

    public boolean getGamenoMoreTips(){
        initSecurePreferences();
        return  securePreferences.getBoolean("game_no_more_tips", false);
    }

    /**
     * 点击体感游戏下载，弹框后继续下载次数
     * @param count
     */
    public void setGameDownloadCount(int count){
        initSecurePreferences();
        editor.putInt("game_download_count", count);
        editor.commit();
    }

    public int getGameDownloadCount(){
        initSecurePreferences();
        return  securePreferences.getInt("game_download_count", 0);
    }

    /**
     * 设置是否完成VR引导页面
     * @param finish
     */
    public void setVrGuide(boolean finish){
        initSecurePreferences();
        editor.putBoolean("isVrGuide", finish);
        editor.commit();
    }

    /**
     * 设置是否完成引导页面
     */
    public boolean getVrGuide(){
        initSecurePreferences();
        return securePreferences.getBoolean("isVrGuide", false);
    }

    /**
     * 设置选择的场景
     * @param
     */
    public void setSkyboxIndex(int index){
        initSecurePreferences();
        editor.putInt("skyboxIndex", index);
        editor.commit();
    }

    /**
     * 获取选择的场景
     * @return
     */
    public int getSkyboxIndex(){
        initSecurePreferences();
        return  securePreferences.getInt("skyboxIndex",0);
    }

    public void setLeftMode(boolean isLeft) {
        initSecurePreferences();
        editor.putBoolean("lefMode", isLeft);
        editor.commit();
    }

    public boolean getLeftMode() {
        initSecurePreferences();
        return securePreferences.getBoolean("lefMode", false);
    }

    /**
     * 首次锁屏提示
     * @return
     */
   public boolean getGLPlayerFirstLockTip(){
       initSecurePreferences();
      return securePreferences.getBoolean("gl_player_first_locked",true);
   }
    public void setGLPlayerFirstLockTip(boolean isFirstlocked){
        initSecurePreferences();
        editor.putBoolean("gl_player_first_locked", isFirstlocked);
        editor.commit();
    }

    /**
     * 首次解锁提示
     * @return
     */
    public boolean getGLPlayerFirstUnLockTip(){
        initSecurePreferences();
        return securePreferences.getBoolean("gl_player_first_unlocked",true);
    }
    public void setGLPlayerFirstUnLockTip(boolean isFirstunlocked){
        initSecurePreferences();
        editor.putBoolean("gl_player_first_unlocked", isFirstunlocked);
        editor.commit();
    }

    public void setPlayerSoundValue(int value){
        initSecurePreferences();
        editor.putInt(PLAYER_SOUND_VALUE_PREFENCE,value);
        editor.commit();
    }
    public int getPlayerSoundValue(Context context){
        initSecurePreferences();
        int defvalue = SoundUtils.GetCurrentVolumePercent(context);
        return securePreferences.getInt(PLAYER_SOUND_VALUE_PREFENCE,defvalue);
    }

    public void setPlayerSoundMute(boolean isMute){
        initSecurePreferences();
        editor.putBoolean(PLAYER_SOUND_ISMUTE,isMute);
        editor.commit();
    }

    public boolean getPlayerSoundMute(){
        initSecurePreferences();
        return securePreferences.getBoolean(PLAYER_SOUND_ISMUTE,false);
    }

    /**
     * 播放屏幕大小模式
     * @return
     */
    public int getPlayerScreenType(){
        initSecurePreferences();
        return securePreferences.getInt(PLAYER_SCREEN_TYPE, VideoModeType.Normal_Screen);
    }

    /**
     * 存储播放屏幕大小模式
     * @param type
     */
    public void setPlayerScreenType(int type){
        initSecurePreferences();
        editor.putInt(PLAYER_SCREEN_TYPE,type);
        editor.commit();
    }

    /**
     * 本地播放视频格式提示
     * @return
     */
    public int getLocalVideoTypeCount(){
        initSecurePreferences();
        return securePreferences.getInt(LOCAL_VIDEO_TYPE_TIP_COUNT,0);
    }

    public void setLocalVideoTypeTipCount(int count){
        initSecurePreferences();
        editor.putInt(LOCAL_VIDEO_TYPE_TIP_COUNT,count);
        editor.commit();
    }

    /**
     * 服务器与本地时间戳的差值
     * @return 毫秒
     */
    public int getDiffTime(){
        initSecurePreferences();
        return securePreferences.getInt(SERVER_LOCAL_DIFF_TIME, 0);
    }

    public void setDiffTime(int time){
        initSecurePreferences();
        editor.putInt(SERVER_LOCAL_DIFF_TIME, time);
        editor.commit();
    }

    public void setPlayerLockScreen(boolean islocked){
        initSecurePreferences();
        editor.putBoolean(PLAYER_LOCK_SCREEN, islocked);
        editor.commit();
    }

    public boolean getPlayerLockScreen(){
        initSecurePreferences();
        return securePreferences.getBoolean(PLAYER_LOCK_SCREEN, false);
    }

    public void setPlayDetailIsFirst(int isFirst){
        initSecurePreferences();
        editor.putInt(REPORT_PLAYER_DETAIL_FIRST, isFirst);
        editor.commit();
    }
    public int getPlayDetailIsFirst(){
        initSecurePreferences();
        return securePreferences.getInt(REPORT_PLAYER_DETAIL_FIRST, 1);
    }

    /**
     * 设置默认播放是单屏还是双屏
     */
    public void setPlaySingleScreen(boolean isSingle) {
        initSecurePreferences();
        editor.putBoolean(PLAYER_IS_SINGLE, isSingle);
        editor.commit();
    }

    /**
     * 默认播放是单屏还是双屏
     */
    public boolean getPlaySingleScreen() {
        initSecurePreferences();
        return securePreferences.getBoolean(PLAYER_IS_SINGLE, true);
    }

    /**
     * 设置从unity进入播放时默认播放是单屏还是双屏
     */
    public void setUnityPlaySingleScreen(boolean isSingle) {
        initSecurePreferences();
        editor.putBoolean(PLAYER_IS_SINGLE_UNITY, isSingle);
        editor.commit();
    }

    /**
     * 默认播放是单屏还是双屏
     */
    public boolean getUnityPlaySingleScreen() {
        initSecurePreferences();
        return securePreferences.getBoolean(PLAYER_IS_SINGLE_UNITY, false);
    }
}
