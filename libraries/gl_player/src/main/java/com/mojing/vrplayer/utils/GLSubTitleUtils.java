
package com.mojing.vrplayer.utils;

import android.content.Context;
import android.text.TextUtils;

import com.bfmj.sdk.util.StringUtils;
import com.storm.smart.common.utils.LogHelper;
import com.storm.smart.domain.FileListItem;
import com.storm.smart.play.call.IBaofengPlayer;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

/**
 * 
 * ClassName: SubTitleUtils <br/>
 * @author lixianke    
 * @date: 2015-1-20 下午2:44:23 <br/>  
 * description: 字幕处理类
 */
public class GLSubTitleUtils {

    private static int subTitleListViewPositon = 0;

    private static String EXTENSION_SRT = "srt";

    private static String EXTENSION_SSA = "ssa";

    private static String EXTENSION_ASS = "ass";

    /**
     * 初始化时设置字幕
     * 
     * @author zony
     * @time 2014-8-21
     */
    private static void setSubTitle(Context context, FileListItem fileListItem,
            ArrayList<String> subtilesList, IBaofengPlayer stormPlayer) {
        if (subtilesList.size() <= 1) {
            return;
        }
        boolean isSubTitleIndex = false;
        // 获取历史字幕索引
        int subTitleIndex = fileListItem.getSubTitleIndex();

        // 获取历史字幕路径
        String subTitlePath = fileListItem.getSubTitlePath();

        for (int i = 0; i < subtilesList.size(); i++) {
            String subTitlePathTemp = subtilesList.get(i);

            if (subTitlePathTemp.contains("【内嵌】")
                    && subTitleIndex != -1) {
                isSubTitleIndex = true;
                break;
            }

            // 如果字幕路径不为空，获得路径在listview中位置
            if (subTitlePath != null && subTitlePath.equals(subTitlePathTemp)) {
                subTitleListViewPositon = i;
            }
        }

        if (isSubTitleIndex) {
            setSubTitleIndex(stormPlayer, fileListItem, subTitleIndex);
        } else {
            setSubTitlePath(subtilesList, stormPlayer, fileListItem, subTitlePath);
        }
    }

    /**
     * 初始化时设置字幕索引
     * 
     * @param subTitleIndex
     * @author zony
     * @time 2014-8-21
     */
    private static void setSubTitleIndex(IBaofengPlayer stormPlayer, FileListItem fileListItem,
                                         int subTitleIndex) {
        LogHelper.d("zzz", "SubTitleUtils setSubTitleIndex fileListItem.getPlayTime:"
                + fileListItem.getPlayTime());
        if (fileListItem.getPlayTime() == 0) {
            subTitleIndex = 1;
        }
        subTitleListViewPositon = subTitleIndex;
        stormPlayer.setSubTitleIndex(subTitleIndex);
        fileListItem.setSubTitleIndex(subTitleIndex);
    }

    /**
     * 初始化时设置字幕路径
     * 
     * @author zony
     * @time 2014-8-21
     */
    private static void setSubTitlePath(ArrayList<String> subtilesList, IBaofengPlayer stormPlayer,
                                        FileListItem fileListItem, String subTitlePath) {
        String subTitleName = StringUtils.getFileNameNoEx(fileListItem.getName());
        boolean isFind = false;
        if (subTitlePath == null) {
            for (int i = 0; i < subtilesList.size(); i++) {
                String fileName = subtilesList.get(i);
                String subTitleNameTemp = StringUtils.getFileNameNoEx(StringUtils
                        .getFileName(fileName));
                String subTitleNameExtension = StringUtils.getExtensionName(StringUtils
                        .getFileName(fileName));
                // 如果字幕路径不为空，获得路径在listview中位置
                if (fileListItem != null && subTitleName.equals(subTitleNameTemp)) {
                    isFind = true;
                    if (EXTENSION_SRT.equals(subTitleNameExtension)) {
                        LogHelper.d("zzz", "SubTitleUtils srt");
                        subTitleListViewPositon = i;
                        break;
                    } else if (EXTENSION_SSA.equals(subTitleNameExtension)) {
                        LogHelper.d("zzz", "SubTitleUtils ssa");
                        subTitleListViewPositon = i;
                        break;
                    } else if (EXTENSION_ASS.equals(subTitleNameExtension)) {
                        LogHelper.d("zzz", "SubTitleUtils ass");
                        subTitleListViewPositon = i;
                        continue;
                    }

                }
            }

            if (isFind) {
                subTitlePath = subtilesList.get(subTitleListViewPositon);
            } else {
                LogHelper.d("zzz", "subTitleListViewPositon:0");
                subTitleListViewPositon = 0;
                subTitlePath = null;
            }
        }

        LogHelper
                .d("zzz", "SubTitleUtils setSubTitlePath stormPlayer SubTitlePath:" + subTitlePath);
        stormPlayer.setSubTitleFilePath(subTitlePath);
        fileListItem.setSubTitlePath(subTitlePath);
    }

    /**
     * 添加外挂字幕
     * 
     * @param context
     * @param subTitleList
     * @author zony
     * @time 2014-8-21
     */
    public static void addSubtitlePlug(Context context, ArrayList<String> subTitleList, String path) {
        File root = new File(path);
        String rootName = root.getName();
        String fileName = StringUtils.getFileNameNoEx(rootName);
        String extensionName = StringUtils.getExtensionName(rootName);
        
        if (!root.exists()) {
            return;
        }
        File[] files = root.getParentFile().listFiles();
        for (File file : files) {
            String listFileExtension = StringUtils.getExtensionName(file.getName());
            /**只保留srt字幕文件  by whf 20160316*/
            if (listFileExtension.equals(EXTENSION_SRT)
                    && !extensionName.equals(listFileExtension)) {
                if (file != null) {
                    String abSolutePath = file.getAbsolutePath();
                    if (!subTitleList.contains(abSolutePath)
                            && file.getName().contains(fileName)) {
                        subTitleList.add(file.getAbsolutePath());

                    }
                }
            }
        }

        //SubTitleUtils.setSubTitle(context, fileListItem, subTitleList, stormPlayer);
    }

    /**
     * 解析底层传来的内嵌字幕标题
     * 
     * @param extra
     * @author zony
     * @time 2014-8-19
     */
    public static void parseSubtitle(Context context, Object extra, ArrayList<String> subtilesList) {
//        addSubTitleNo(context, subtilesList);
        String jsonString = extra.toString();
        if (TextUtils.isEmpty(jsonString) || "NULL".equals(jsonString)) {
            return;
        }
        try {
            JSONObject jsonObj = new JSONObject(jsonString);
            if (jsonObj.length() == 0) {
                return;
            }
            JSONArray jsonArray = jsonObj.getJSONArray("Subtitle");
            // 添加底层返回字幕选项
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jObj = jsonArray.getJSONObject(i);
                String name = "";
                String language = "";
                if(jObj.has("language")){
                    language = jObj.getString("language").toString();
                }
                if(jObj.has("names")){
                    name = jObj.getString("names").toString();
                }
                subtilesList.add("【内嵌】"+""+name+" "+language);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 添加无字幕选项
     * 
     * @param context
     * @param subtilesList
     * @author zony
     * @time 2014-8-25
     */
    public static void addSubTitleNo(Context context, ArrayList<String> subtilesList) {
        String subTitleNo = "无"; // listView中第一项：无
        // 添加无字幕选项
        subtilesList.clear();
        subtilesList.add(subTitleNo);
    }

    /**
     * @return the subTitleListViewPositon
     */
    private static int getSubTitleListViewPositon() {
        return subTitleListViewPositon;
    }

    /**
     * @param subTitleListViewPositon the subTitleListViewPositon to set
     */
    public static void setSubTitleListViewPositon(int subTitleListViewPositon) {
        GLSubTitleUtils.subTitleListViewPositon = subTitleListViewPositon;
    }

    /**
     * 字幕扫描结果
     * 
     * @author zony
     * @time 2014-8-22
     */
    public interface OnSubTitleScanListener {
        public void onSubTitlePlugScanResult(boolean result); // 外挂字幕扫描结果
    }

}
