package com.mojing.vrplayer.publicc;

import android.text.TextUtils;


import com.mojing.vrplayer.publicc.bean.HistoryInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by liuchuanchi on 2016/5/21.
 * 创建历史记录工具类
 */
public class CreateHistoryUtil {

    /**
     * 网络json转换成本地json
     */
    public static JSONArray netJsonToLocalJson(JSONArray netJson){
        JSONArray localJson = new JSONArray();
        if(netJson != null && netJson.length() > 0){
            for (int i = 0; i < netJson.length(); i++) {
                try {
                    localJson.put(netJsonToLocalJson(netJson.getJSONObject(i)));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return localJson;
    }

    /**
     * 网络json转换成本地json
     */
    public static JSONObject netJsonToLocalJson(JSONObject netJson){
        JSONObject localJson = new JSONObject();
        try {
            localJson.put("type", HistoryBusiness.historyTypeCinema);
            localJson.put("videoId", netJson.getString("object_id"));
            localJson.put("videoTitle", netJson.getString("title"));
            localJson.put("videoImg", netJson.getString("w_thumb"));
            localJson.put("videoPlayUrl", "");
            localJson.put("videoSet", -1);
            localJson.put("lastSetIndex", Integer.valueOf(netJson.getString("seq")));
            localJson.put("video3dType", Integer.valueOf(netJson.getString("dimension")));
            localJson.put("videoType", -1);
            localJson.put("totalDuration", Integer.valueOf(netJson.getString("length")));
            localJson.put("playDuration", Integer.valueOf(netJson.getString("play_time")));
            localJson.put("playFinished", -1);
            localJson.put("playTimestamp", Long.valueOf(netJson.getString("create_time")));
            localJson.put("playType", -1);
            localJson.put("videoClarity", netJson.getString("hd_type"));
            String object_type = netJson.getString("object_type");
            if(TextUtils.isEmpty(object_type)){
                localJson.put("resType", 0);
            }else{
                localJson.put("resType", Integer.valueOf(object_type));
            }
            localJson.put("detailUrl", netJson.getString("url"));
            //localJson.put("", netJson.getString("user_id"));
            //localJson.put("", netJson.getString("h_thumb"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return localJson;
    }

    /**
     * 网络json转换成historyInfo
     */
    public static HistoryInfo netJsonToHistoryInfo(JSONObject netJson){
        HistoryInfo historyInfo = new HistoryInfo();
        try {
            historyInfo.setType(HistoryBusiness.historyTypeCinema);
            historyInfo.setVideoId(netJson.getString("object_id"));
            historyInfo.setVideoTitle(netJson.getString("title"));
            historyInfo.setVideoImg(netJson.getString("w_thumb"));
            historyInfo.setVideoPlayUrl("");
            historyInfo.setVideoSet(-1);
            historyInfo.setLastSetIndex(Integer.valueOf(netJson.getString("seq")));
            historyInfo.setVideo3dType(Integer.valueOf(netJson.getString("dimension")));
            historyInfo.setVideoType(-1);
            historyInfo.setTotalDuration(Integer.valueOf(netJson.getString("length")));
            historyInfo.setPlayDuration(Integer.valueOf(netJson.getString("play_time")));
            historyInfo.setPlayFinished(-1);
            historyInfo.setPlayTimestamp(Long.valueOf(netJson.getString("create_time")));
            historyInfo.setPlayType(-1);
            historyInfo.setVideoClarity(netJson.getString("hd_type"));
            String object_type = netJson.getString("object_type");
            if(TextUtils.isEmpty(object_type)){
                historyInfo.setResType(0);
            }else{
                historyInfo.setResType(Integer.valueOf(object_type));
            }
            historyInfo.setDetailUrl(netJson.getString("url"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return historyInfo;
    }

    /**
     * 本地json转换成historyInfo
     */
    public static HistoryInfo localJsonToHistoryInfo(JSONObject localJson){
        HistoryInfo historyInfo = new HistoryInfo();
        try {
            historyInfo.setType(localJson.getInt("type"));
            historyInfo.setVideoId(localJson.getString("videoId"));
            historyInfo.setVideoTitle(localJson.getString("videoTitle"));
            historyInfo.setVideoImg(localJson.getString("videoImg"));
            historyInfo.setVideoPlayUrl(localJson.getString("videoPlayUrl"));
            historyInfo.setVideoSet(localJson.getInt("videoSet"));
            historyInfo.setLastSetIndex(localJson.getInt("lastSetIndex"));
            historyInfo.setVideo3dType(localJson.getInt("video3dType"));
            historyInfo.setVideoType(localJson.getInt("videoType"));
            historyInfo.setTotalDuration(localJson.getInt("totalDuration"));
            historyInfo.setPlayDuration(localJson.getInt("playDuration"));
            historyInfo.setPlayFinished(localJson.getInt("playFinished"));
            historyInfo.setPlayTimestamp(localJson.getLong("playTimestamp"));
            historyInfo.setPlayType(localJson.getInt("playType"));
            if(localJson.isNull("videoClarity")) {//视频清晰度
                historyInfo.setVideoClarity("");
            }else{
                historyInfo.setVideoClarity(localJson.getString("videoClarity"));
            }
            if(localJson.isNull("resType")) {//资源类型
                historyInfo.setResType(0);
            }else{
                historyInfo.setResType(localJson.getInt("resType"));
            }
            if(localJson.isNull("detailUrl")) {//资源详情页url
                historyInfo.setDetailUrl("");
            }else{
                historyInfo.setDetailUrl(localJson.getString("detailUrl"));
            }
            if(localJson.has("viewRatio")){
                historyInfo.setViewRatio(localJson.getString("viewRatio"));
            }
            if(localJson.has("viewRotate")){
                historyInfo.setViewRotate(localJson.getInt("viewRotate"));
            }
            if(localJson.has("subtitle")){
                historyInfo.setSubtitle(localJson.getString("subtitle"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return historyInfo;
    }

    /**
     * 创建上报json
     */
    public static JSONObject createReportJson(String historyJson){
        JSONObject reportJson = new JSONObject();
        if(!TextUtils.isEmpty(historyJson)){
            try {
                JSONObject localJson = new JSONObject(historyJson);
                reportJson.put("object_id", localJson.getString("videoId"));
                reportJson.put("seq", String.valueOf(localJson.getInt("lastSetIndex")));
                reportJson.put("length", String.valueOf(localJson.getInt("totalDuration")));
                reportJson.put("play_time", String.valueOf(localJson.getInt("playDuration")));
                if(localJson.isNull("videoClarity")){//视频清晰度
                    reportJson.put("hd_type", "");
                }else{
                    reportJson.put("hd_type", localJson.getString("videoClarity"));
                }
                reportJson.put("dimension", String.valueOf(localJson.getInt("video3dType")));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return reportJson;
    }

//    public static HistoryInfoNet createHistoryInfoNet(JSONObject joResult){
//        HistoryInfoNet historyInfoNet = new HistoryInfoNet();
//        try {
//            historyInfoNet.setTitle(joResult.getString("title"));
//            historyInfoNet.setCreate_time(joResult.getString("create_time"));
//            historyInfoNet.setDimension(joResult.getString("dimension"));
//            historyInfoNet.setH_thumb(joResult.getString("h_thumb"));
//            historyInfoNet.setHd_type(joResult.getString("hd_type"));
//            historyInfoNet.setLength(joResult.getString("length"));
//            historyInfoNet.setObject_id(joResult.getString("object_id"));
//            historyInfoNet.setObject_type(joResult.getString("object_type"));
//            historyInfoNet.setPlay_time(joResult.getString("play_time"));
//            historyInfoNet.setSeq(joResult.getString("seq"));
//            historyInfoNet.setUrl(joResult.getString("url"));
//            historyInfoNet.setUser_id(joResult.getString("user_id"));
//            historyInfoNet.setW_thumb(joResult.getString("w_thumb"));
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        return historyInfoNet;
//    }
}
