package com.baofeng.mj.unity.UnityHistoryBusiness;

import android.text.TextUtils;

import com.baofeng.mj.unity.IAndroidCallback;
import com.baofeng.mj.unity.UnityActivity;
import com.baofeng.mj.vrplayer.util.FileCommonUtil;
import com.mojing.sdk.pay.activity.UnityPlayerActivity;
import com.mojing.vrplayer.publicc.HistoryBusiness;
import com.storm.smart.common.utils.LogHelper;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.mojing.vrplayer.publicc.HistoryBusiness.getLocalHistoryFolder;
import static com.mojing.vrplayer.publicc.HistoryBusiness.getLocalHistoryPath;

/**
 * Created by liuchuanchi on 2016/5/18.
 * unity历史业务
 */
public class UnityHistoryBusiness {
    /**
     * 历史信息写入文件
     * @param historyJson 历史信息json
     */
    public static void writeToHistory(final String historyJson){
        LogHelper.e("infos","historyJson==="+historyJson);
        HistoryProxy.getInstance().addProxyRunnable(new SingleThreadProxy.ProxyRunnable() {
            @Override
            public void run() {
                if(!TextUtils.isEmpty(historyJson)){
                    try {
                        JSONObject historyJo = new JSONObject(historyJson);
                        int type = historyJo.getInt("type");
                        LogHelper.e("infos","historyType==="+type);
                        if(HistoryBusiness.historyTypeLocal == type) {//本地历史
                            String fileName = historyJo.getString("videoPlayUrl");
                            HistoryBusiness.writeToHistory(historyJson, fileName, type);
                            LogHelper.e("infos","historyFileName==="+fileName);
                        }else{//在线历史
                            String fileName = historyJo.getString("videoId");
                            HistoryBusiness.writeToHistory(historyJson, fileName, type);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    /**
     * 从文件读历史信息
     * @param fileName 文件名称 在线历史传视频id，本地历史传视频路径
     * @param type 0本地历史，1在线历史
     * @return json
     */
    public static String readFromHistory(final String fileName, final int type){
        String historyJson = HistoryBusiness.readFromHistory(fileName, type);
        if(TextUtils.isEmpty(historyJson)){
            historyJson = "";
        }
        return historyJson;
    }

    /**
     * 从文件读历史信息（本地用）
     * @param filePath 文件路径
     * @return json
     */
    public static String readFromHistoryByPath(final String filePath){
        String historyJson = HistoryBusiness.readFromHistory(filePath, 0);
        if(TextUtils.isEmpty(historyJson)){
            historyJson = "";
        }
        return historyJson;
    }

    /**
     * 查询本地历史记录
     * @param page 第几页
     * @param page_cnt 每页多少条
     */
    public static void queryLocalHistory(final int page, final int page_cnt){
            HistoryProxy.getInstance().addProxyRunnable(new SingleThreadProxy.ProxyRunnable() {
                @Override
                public void run() {
                    String result = HistoryBusiness.readAllFromHistory(page, page_cnt, 0, 0);
                    LogHelper.e("infos","=====未登录===="+result);
                    if(UnityActivity.INSTANCE != null){
                        IAndroidCallback iAndroidCallback = UnityActivity.INSTANCE.getIAndroidCallback();
                        if (iAndroidCallback != null) {//通知Unity
                            if(result==null){
                                result="";
                            }
                            iAndroidCallback.sendHistoryJSONObject(result);
                        }
                    }
                }
            });
    }

    /**
     * 删除单个本地历史
     */
    public static void deleteSingleLocalHistory(final String videoPath){
        HistoryProxy.getInstance().addProxyRunnable(new SingleThreadProxy.ProxyRunnable() {
            @Override
            public void run() {
                boolean status = FileCommonUtil.deleteFile(getLocalHistoryPath(videoPath));//删除本地历史
            }
        });
    }

    /**
     * 删除所有本地历史
     */
    public static void deleteAllLocalHistory(){
        HistoryProxy.getInstance().addProxyRunnable(new SingleThreadProxy.ProxyRunnable() {
            @Override
            public void run() {
                boolean status = FileCommonUtil.deleteFile(getLocalHistoryFolder());//删除本地历史文件夹
            }
        });
    }

}
