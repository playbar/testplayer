package com.baofeng.mj.vrplayer.util;

import android.content.Context;
import android.util.Log;

import com.baofeng.mj.vrplayer.bean.LocalVideoBean;
import com.baofeng.mj.vrplayer.business.LocalVideoPathBusiness;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;

/**
 * Created by liuchuanchi on 2016/5/21.
 * 创建本地视频(实体类)工具类
 */
public class LocalVideoBeanUtil {
    /**
     * 创建JSONArray
     */
    public static JSONArray createJSONArray(Context context, HashMap<String, TreeMap<String, LocalVideoBean>> hashMap){
        JSONArray jsonRoot = new JSONArray();
        List<String> localVideoDirList = LocalVideoPathBusiness.getAllDir(context, false);
        for(String localVideoDir : localVideoDirList) {//遍历所有文件夹(所有分组)
            JSONObject jsonGroup = new JSONObject();
            JSONArray jsonData = new JSONArray();
            TreeMap treeMap = (TreeMap) hashMap.get(localVideoDir);
            Iterator iterator = treeMap.values().iterator();
            while (iterator.hasNext()) {
                LocalVideoBean localVideoBean = (LocalVideoBean) iterator.next();
                jsonData.put(createJSONObject(localVideoBean));
            }
            try {
                jsonGroup.put("group", localVideoDir);
                jsonGroup.put("data", jsonData);
                jsonRoot.put(jsonGroup);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return jsonRoot;
    }

    /**
     * 创建JSONObject
     */
    public static JSONObject createJSONObject(LocalVideoBean localVideoBean){
        JSONObject localVideoJson = new JSONObject();
        try {
            localVideoJson.put("name",localVideoBean.name);
            localVideoJson.put("length",localVideoBean.length);
            localVideoJson.put("size",localVideoBean.size);
            localVideoJson.put("path",localVideoBean.path);
            localVideoJson.put("lastModify",localVideoBean.lastModify);
            localVideoJson.put("thumbPath",localVideoBean.thumbPath);
            localVideoJson.put("videoType",localVideoBean.videoType);
            localVideoJson.put("videoDuration",localVideoBean.videoDuration);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return localVideoJson;
    }

    /**
     * 创建本地视频实体类集合
     */
    public static List<LocalVideoBean> createLocalVideoBeanList(String strJson){
        List<LocalVideoBean> localVideoBeanList = new ArrayList<LocalVideoBean>();
        try {
            JSONArray jaJson = new JSONArray(strJson);
            for(int i = 0; i < jaJson.length(); i++){
                JSONObject joJson = jaJson.getJSONObject(i);
                String group = joJson.getString("group");
                JSONArray jaData = joJson.getJSONArray("data");
                for (int j = 0; j < jaData.length(); j++) {
                    JSONObject joData = jaData.getJSONObject(j);
                    LocalVideoBean localVideoBean = createLocalVideoBean(joData);
                    localVideoBean.group = group;
                    localVideoBeanList.add(localVideoBean);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return localVideoBeanList;
    }

    /**
     * 创建本地视频实体类
     */
    public static LocalVideoBean createLocalVideoBean(JSONObject localVideoJson){
        LocalVideoBean localVideoBean = new LocalVideoBean();
        try {
            localVideoBean.name = localVideoJson.getString("name");
            localVideoBean.length = localVideoJson.getLong("length");
            localVideoBean.size = localVideoJson.getString("size");
            localVideoBean.path = localVideoJson.getString("path");
            localVideoBean.lastModify = localVideoJson.getLong("lastModify");
            localVideoBean.thumbPath = localVideoJson.getString("thumbPath");
            localVideoBean.videoType = localVideoJson.getInt("videoType");
            localVideoBean.videoDuration = localVideoJson.getInt("videoDuration");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return localVideoBean;
    }
}
