package com.baofeng.mj.vrplayer.util;

import android.text.TextUtils;

import com.baofeng.mj.vrplayer.publicc.AppContant;
import com.mojing.vrplayer.publicc.ReportBusiness;

import java.util.HashMap;

/**
 * Created by panxin on 2017/7/10.
 */

public class ReportUtil {

    public static void reportTimer(String tops){
        if(AppContant.comeInTime == 0){
            return;
        }
        long useTime = System.currentTimeMillis()-AppContant.comeInTime;
        HashMap<String,String> params = new HashMap<>();
        params.put("etype","timer");
        params.put("tpos",tops);
        params.put("tpostime",useTime+"");
        ReportBusiness.getInstance().reportVV(params);
        AppContant.comeInTime = System.currentTimeMillis();
    }

    public static void reportPV(String pagetype){
        HashMap<String,String> params = new HashMap<>();
        params.put("etype","pv");
        params.put("pagetype",pagetype);
        ReportBusiness.getInstance().reportVV(params);
    }

    /**
     * 上报视频模式切换
     * @param videoType 视频类型
     */
    public static void reportVideoModeChange(String videoType){
        HashMap<String,String> params = new HashMap<>();
        params.put("tpos", "1");
        params.put("pagetype", PageTypeUtil.PageTypeLocal);
        params.put("etype", "click");
        params.put("clicktype", "switch");
        params.put("clickitem", "videomode");
        if(!TextUtils.isEmpty(videoType)){
            params.put("clickitemvalue", videoType);
        }
        ReportBusiness.getInstance().reportClick(params);
    }

    /**
     * 上报文件夹添加
     * @param folderPath 文件夹路径
     */
    public static void reportFolderAdd(String folderPath){
        HashMap<String,String> params = new HashMap<>();
        params.put("tpos", "1");
        params.put("pagetype", PageTypeUtil.PageTypeLocal);
        params.put("etype", "click");
        params.put("clicktype", "switch");
        params.put("clickitem", "addfolder");
        if(!TextUtils.isEmpty(folderPath)){
            params.put("clickitemvalue", folderPath);
        }
        ReportBusiness.getInstance().reportClick(params);
    }
}
