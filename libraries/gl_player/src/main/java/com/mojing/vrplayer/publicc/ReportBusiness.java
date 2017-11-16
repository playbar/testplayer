package com.mojing.vrplayer.publicc;

import android.text.TextUtils;

import com.alibaba.fastjson.JSONObject;
import com.baofeng.mojing.MojingSDK;
import com.baofeng.mojing.MojingSDKReport;
import com.google.gson.Gson;
import com.mojing.vrplayer.publicc.bean.ContentInfo;
import com.mojing.vrplayer.publicc.bean.ReportFromBean;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhaominglei on 2016/7/16.
 */
public class ReportBusiness {
    /**
     * 点击类型
     */
    public static final String CLICK_TYPE_DOWNLOAD = "download";//游戏下载
    public static final String CLICK_TYPE_INSTALL = "install";//游戏安装
    public static final String CLICK_TYPE_OPEN = "open";//游戏打开
    public static final String CLICK_TYPE_UPDATE = "update";//游戏更新
    /**
     * 页面类型
     */
    public static final String PAGE_TYPE_RECOMMEND = "waterfall";//推荐页
    public static final String PAGE_TYPE_VIDEO = "video";//视频页
    public static final String PAGE_TYPE_APPGAME = "appgame";//应用市场页
    public static final String PAGE_TYPE_LOCAL = "local";//本地页
    public static final String PAGE_TYPE_ACCOUNT = "account"; //我的页面
    public static final String PAGE_TYPE_DETAIL = "detail";//详情页
    public static final String PAGE_TYPE_TOPIC_LIST = "topic_list";//专题列表页
    public static final String PAGE_TYPE_COLUMN_LIST = "column_list";//
    public static final String PAGE_TYPE_SUBCATE_LIST = "subcate_list";//
    public static final String PAGE_TYPE_PLAY_RECOMMEND_COM = "playrecom";//
    public static final String PAGE_TYPE_PLAY_RECOMMEND_COUNTDOWN = "countdownplay";//
    public static final String PAGE_TYPE_PLAY_RECOMMEND_FINISH = "contdownfinish";//
    public static final String UNKNOW = "UNKNOWN";

    private static ReportBusiness instance;
    private Map<String, ReportFromBean> mReportBean;
    private Map<String, Integer> mPVContants;
    private boolean isDebug = false;

    private ReportBusiness() {
        mReportBean = new HashMap<String, ReportFromBean>();
        mPVContants = new HashMap<String, Integer>();
        mPVContants.put("VR", 1);
        mPVContants.put("2D", 2);
        mPVContants.put("3D", 3);
        mPVContants.put("直播", 4);
        mPVContants.put("游戏", 1);
        mPVContants.put("软件", 2);
        mPVContants.put("福利", 3);
        mPVContants.put("分类", 4);
        mPVContants.put("榜单", 5);
    }

    public static ReportBusiness getInstance() {
        if (instance == null) {
            instance = new ReportBusiness();
        }
        return instance;
    }

    public void put(String key, ReportFromBean reportFromBean) {
        try{
            if (TextUtils.isEmpty(key) || reportFromBean == null) {
                return;
            }
            ReportFromBean clone=(ReportFromBean)reportFromBean.clone();
            mReportBean.put(key, clone);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /***
     * 获取详细信息时，包含在header 中agent信息
     */
    public void putHeader(ContentInfo contentInfo, ReportFromBean reportFromBean) {
        if (contentInfo == null || reportFromBean == null) {
            return;
        }
        if (TextUtils.isEmpty(contentInfo.getUrl())) {
            return;
        }
        reportFromBean.setCompid(contentInfo.getParentResId());
        reportFromBean.setComponenttype(contentInfo.getLayout_type());
        reportFromBean.setCompsubtitle(contentInfo.getTitle());
        reportFromBean.setCompsubid(contentInfo.getRes_id());
        reportFromBean.setCurpage(contentInfo.getUrl());
        mReportBean.put(contentInfo.getUrl(), reportFromBean);
    }

    public ReportFromBean get(String key) {
        if (TextUtils.isEmpty(key) || !mReportBean.containsKey(key)) {
            return new ReportFromBean();
        }
        return mReportBean.get(key);
    }

    public void remove(String key) {
        if (!TextUtils.isEmpty(key) && mReportBean.containsKey(key)) {
            mReportBean.remove(key);
        }
    }

    public String getReportStr(String key) {
        if (TextUtils.isEmpty(key) || !mReportBean.containsKey(key)) {
            return null;
        }
        return JSONObject.toJSONString(mReportBean.get(key));
    }






    /***
     * 从url中截取resid
     * @param url
     * @return
     */
    public String getResIdFromUrl(String url) {
        if (TextUtils.isEmpty(url)) {
            return null;
        }
        int firstIndex = url.lastIndexOf("/");
        int lastIndex = url.lastIndexOf(".");
        if (firstIndex == -1 || lastIndex == -1) {
            return null;
        }
        return url.substring(firstIndex + 1, lastIndex);
    }




    /**
     * vv报数
     * @param params
     */
    public void reportVV(HashMap<String ,String> params){
        String vvParams = new Gson().toJson(params);
//        Log.d("login","---reportVV vvParams = "+params);
        MojingSDKReport.onEvent(vvParams,UNKNOW,UNKNOW,0f,UNKNOW,0f);
    }

    /**
     * 点击报数
     * @param params
     */
    public void reportClick(HashMap<String,String> params){
        String clickParams = new Gson().toJson(params);
//        Log.d("login","----reportClick params = "+clickParams);
        MojingSDKReport.onEvent(clickParams,UNKNOW,UNKNOW,0f,UNKNOW,0f);
    }

    /**
     * 针对达观报数
     * @param
     */
    public void reportUserAction(String res_id,String action_type, String jsonValue){
        MojingSDKReport.onReportUserAction(action_type,res_id, jsonValue);
    }



    /**
     * 页面帧率报数
     * @param
     */
    public void reportFramerate(String aveframe){
        if(!TextUtils.isEmpty(aveframe)) {
            HashMap<String,String> map = new HashMap<>();
            map.put("aveframe",aveframe);
            map.put("tpos","1");
            map.put("framemode","player");
            String clickParams = new Gson().toJson(map);
            MojingSDKReport.onEvent(clickParams,UNKNOW,UNKNOW,0f,UNKNOW,0f);
        }
    }
}
