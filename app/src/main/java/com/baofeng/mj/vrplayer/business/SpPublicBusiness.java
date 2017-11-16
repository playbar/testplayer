package com.baofeng.mj.vrplayer.business;

import android.content.Context;

import com.baofeng.mj.vrplayer.util.FileCommonUtil;
import com.baofeng.mj.vrplayer.util.SecurePreferences;


/**
 * @author liuchuanchi
 * @description: 公共相关的保存业务
 */
public class SpPublicBusiness {
    private static SpPublicBusiness instance;
    private SecurePreferences securePreferences;
    private SecurePreferences.Editor editor;

    private SpPublicBusiness() {
    }

    public static SpPublicBusiness getInstance() {
        if (instance == null) {
            instance = new SpPublicBusiness();
        }
        return instance;
    }

    /**
     * 初始化SecurePreferences
     */
    private void initSecurePreferences(Context mContext) {
        if (securePreferences == null) {
            String spName = mContext.getPackageName() + ".public";
            securePreferences = new SecurePreferences(mContext,  spName, Context.MODE_PRIVATE);
            editor = securePreferences.edit();
        }
    }

    /***
     * 飞屏设置跳过引导页
     * @param skipGuide
     */
    public void setFlyScreenSkipGuide(Context mContext, boolean skipGuide) {
        initSecurePreferences(mContext);
        editor.putBoolean("skipGuide", skipGuide);
        editor.putBoolean("beginGuide", !skipGuide);
        editor.commit();
    }

    /**
     * 飞屏跳过引导页
     */
    public boolean getFlyScreenSkipGuide(Context mContext) {
        initSecurePreferences(mContext);
        return securePreferences.getBoolean("skipGuide", false);
    }

    public boolean getFlyScreenBeginStepGuide(Context mContext) {
        initSecurePreferences(mContext);
        return securePreferences.getBoolean("beginGuide", false);
    }

    public void setFlyScreenBeginStepGuide(Context mContext, boolean b) {
        initSecurePreferences(mContext);
        editor.putBoolean("beginGuide", b);
        editor.commit();
    }

    /***
     * 飞屏Session Id
     * @param sessionId
     */
    public void setFlyScreenSessionId(Context mContext, String sessionId) {
        initSecurePreferences(mContext);
        editor.putString("sessionId", sessionId);
        editor.commit();
    }

    /***
     * 获取飞屏session ID
     * @return
     */
    public String getFlyScreenSessionId(Context mContext) {
        initSecurePreferences(mContext);
        return securePreferences.getString("sessionId", "");
    }

    /**
     * 设置本地视频排序规则
     */
    public void setLocalVideoSort(Context mContext, int sortRule) {
        initSecurePreferences(mContext);
        editor.putInt("localVideoSort", sortRule);
        editor.commit();
    }

    /**
     * 获取本地视频排序规则
     */
    public int getLocalVideoSort(Context mContext) {
        initSecurePreferences(mContext);
        return securePreferences.getInt("localVideoSort", FileCommonUtil.ruleFileLastModify);
    }

    /**
     * 设置默认存储是否扫描
     * @param isSearch true扫描，false不扫描
     */
    public void setDefaultStorageSearch(Context mContext, boolean isSearch) {
        initSecurePreferences(mContext);
        editor.putBoolean("defaultStorageSearch", isSearch);
        editor.commit();
    }

    /**
     * 获取默认存储是否扫描
     * @return true扫描，false不扫描
     */
    public boolean getDefaultStorageSearch(Context mContext) {
        initSecurePreferences(mContext);
        return securePreferences.getBoolean("defaultStorageSearch", true);
    }

    /**
     * 设置自定义存储是否扫描
     * @param storageDir 存储目录
     * @param isSearch true扫描，false不扫描
     */
    public void setCustomStorageSearch(Context mContext, String storageDir, boolean isSearch) {
        initSecurePreferences(mContext);
        editor.putBoolean(storageDir, isSearch);
        editor.commit();
    }

    /**
     * 获取自定义存储是否扫描
     * @param storageDir 存储目录
     * @return true扫描，false不扫描
     */
    public boolean getCustomStorageSearch(Context mContext, String storageDir) {
        initSecurePreferences(mContext);
        return securePreferences.getBoolean(storageDir, true);
    }

    /**
     * 移除某个自定义存储的扫描标记
     * @param storageDir 存储目录
     */
    public void removeCustomStorageSearch(Context mContext, String storageDir) {
        initSecurePreferences(mContext);
        editor.remove(storageDir);
        editor.commit();
    }
}
