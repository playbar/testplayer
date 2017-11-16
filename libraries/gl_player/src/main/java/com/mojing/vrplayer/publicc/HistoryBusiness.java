package com.mojing.vrplayer.publicc;

import android.app.Activity;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.mojing.vrplayer.bean.VideoModeType;
import com.mojing.vrplayer.utils.publicutil.ComparatorLong;
import com.mojing.vrplayer.utils.publicutil.ComparatorString;
import com.storm.smart.common.utils.LogHelper;
import com.storm.smart.play.call.IBfPlayerConstant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.Iterator;
import java.util.TreeMap;

/**
 * Created by liuchuanchi on 2016/5/18.
 * 历史业务
 */
public class HistoryBusiness {
    public static final int historyTypeLocal = 0;//本地历史
    public static final int historyTypeCinema = 1;//在线历史
    public static final int historyTypeBoth = 2;//两者都
    private static String localHistoryFolder;//本地历史文件夹
    private static String cinemaHistoryFolder;//在线历史文件夹
    private static String cinemaTempHistoryFolder;//在线临时历史文件夹

    private HistoryBusiness(){
    }

    /**
     * 获取本地历史文件夹
     */
    public static String getLocalHistoryFolder() {
        if(TextUtils.isEmpty(localHistoryFolder)){
            localHistoryFolder = FileStorageUtil.getMojingDir() + "history/localHistory";
        }
        FileStorageUtil.mkdir(localHistoryFolder);
        return localHistoryFolder;
    }


    /**
     * 获取在线临时历史文件夹
     */
    public static String getCinemaTempHistoryFolder() {
        if(TextUtils.isEmpty(cinemaTempHistoryFolder)){
            cinemaTempHistoryFolder = FileStorageUtil.getMojingDir() + "history/cinemaTempHistory";
        }
        FileStorageUtil.mkdir(cinemaTempHistoryFolder);
        return cinemaTempHistoryFolder;
    }

    /**
     * 获取本地历史路径
     * @param videoPath 视频路径
     */
    public static String getLocalHistoryPath(String videoPath) {
        LogHelper.e("infos","==getLocalHistoryPath==="+getLocalHistoryFolder() + "/" + videoPath.hashCode() + ".history");
        return getLocalHistoryFolder() + "/" + videoPath.hashCode() + ".history";
    }


    /**
     * 获取在线临时历史路径
     * @param videoId 视频id
     */
    public static String getCinemaTempHistoryPath(String videoId) {
        return getCinemaTempHistoryFolder() + "/" + videoId + ".history";
    }

    /**
     * 历史信息写入文件
     * @param historyJson 历史信息json
     * @param fileName 文件名称 在线历史传视频id，本地历史传视频路径
     * @param type 0本地历史，1在线历史
     */
    public static void writeToHistory(final String historyJson, String fileName, int type){
        if(historyTypeLocal == type){//本地历史
            String historyPath = getLocalHistoryPath(fileName);
            FileCommonUtil.writeFileString(historyJson, historyPath);
        }
//       else{ //在线历史
//            if(UserSpBusiness.getInstance().isUserLogin()){//已登录，存入在线历史文件
//                String historyPath = getCinemaHistoryPath(fileName);
//                FileCommonUtil.writeFileString(historyJson, historyPath);
//                //上报在线历史记录
//                if(BaseApplication.INSTANCE!=null) {
//                    Activity curActivity = BaseApplication.INSTANCE.getCurrentActivity();
//                    if (curActivity != null && !curActivity.isFinishing()) {
//                        curActivity.runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                JSONArray historyJa = new JSONArray();
//                                historyJa.put(CreateHistoryUtil.createReportJson(historyJson));
//                                new VideoApi().reportCinemaHistory(historyJa.toString(), new ApiCallBack<String>() {
//                                    @Override
//                                    public void onSuccess(String result) {
//                                        super.onSuccess(result);
//                                    }
//
//                                    @Override
//                                    public void onFailure(Throwable error, String content) {
//                                        super.onFailure(error, content);
//                                    }
//                                });
//                            }
//                        });
//                    }
//                }
//            }else{//未登录，存入在线临时历史文件
//                String historyPath = getCinemaTempHistoryPath(fileName);
//                FileCommonUtil.writeFileString(historyJson, historyPath);
//            }
//        }
    }

    /**
     * 从文件读历史信息
     * @param fileName 文件名称 在线历史传视频id，本地历史传视频路径
     * @param type 0本地历史，1在线历史
     * @return json
     */
    public static String readFromHistory(String fileName, int type){
        if(historyTypeLocal == type) {//本地历史
            String historyPath = getLocalHistoryPath(fileName);
            return FileCommonUtil.readFileString(historyPath);
        }
//        else{//在线历史
//            if(UserSpBusiness.getInstance().isUserLogin()) {//已登录，读在线历史文件
//                String historyPath = getCinemaHistoryPath(fileName);
//                return FileCommonUtil.readFileString(historyPath);
//            }else{//未登录，读在线临时历史文件
//                String historyPath = getCinemaTempHistoryPath(fileName);
//                return FileCommonUtil.readFileString(historyPath);
//            }
//        }
        return null;
    }

    /**
     * 删除单个在线历史
     */
    public static void deleteSingleCinemaHistory(final String videoId, final DeleteHistoryCallback deleteHistoryCallback){
//        if(UserSpBusiness.getInstance().isUserLogin()) {//已登录
//            new VideoApi().deleteCinemaHistory("1", videoId, new ApiCallBack<String>() {
//                @Override
//                public void onSuccess(String result) {
//                    super.onSuccess(result);
//                    boolean status = false;
//                    try {
//                        JSONObject joResult = new JSONObject(result);
//                        if ("1".equals(joResult.getString("status"))) {//删除成功
//                            status = true;
//                            HistoryProxy.getInstance().addProxyRunnable(new SingleThreadProxy.ProxyRunnable() {
//                                @Override
//                                public void run() {
//                                    FileCommonUtil.deleteFile(getCinemaHistoryPath(videoId));
//                                }
//                            });
//                        }
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                    if (deleteHistoryCallback != null) {
//                        deleteHistoryCallback.callback(status);
//                    }
//                }
//
//                @Override
//                public void onFailure(Throwable error, String content) {
//                    super.onFailure(error, content);
//                    if (deleteHistoryCallback != null) {
//                        deleteHistoryCallback.callback(false);
//                    }
//                }
//            });
//        }else{//未登录
//            HistoryProxy.getInstance().addProxyRunnable(new SingleThreadProxy.ProxyRunnable() {
//                @Override
//                public void run() {
//                    boolean status = FileCommonUtil.deleteFile(getCinemaTempHistoryPath(videoId));
//                    if(deleteHistoryCallback != null){
//                        deleteHistoryCallback.callback(status);
//                    }
//                }
//            });
//        }
    }

    /**
     * 删除所有在线历史
     */
    public static void deleteAllCinemaHistory(final DeleteHistoryCallback deleteHistoryCallback){
//        LogHelper.e("infos","======deleteAllCinemaHistory========");
//        if(UserSpBusiness.getInstance().isUserLogin()) {//已登录
//            new VideoApi().deleteCinemaHistory("2", "", new ApiCallBack<String>() {
//                @Override
//                public void onSuccess(String result) {
//                    super.onSuccess(result);
//                    boolean status = false;
//                    try {
//                        JSONObject joResult = new JSONObject(result);
//                        if("1".equals(joResult.getString("status"))){//删除成功
//                            status = true;
//                            HistoryProxy.getInstance().addProxyRunnable(new SingleThreadProxy.ProxyRunnable() {
//                                @Override
//                                public void run() {
//                                    FileCommonUtil.deleteFile(getCinemaHistoryFolder());//删除在线历史文件夹
//                                }
//                            });
//                        }
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                    if(deleteHistoryCallback != null){
//                        deleteHistoryCallback.callback(status);
//                    }
//                }
//
//                @Override
//                public void onFailure(Throwable error, String content) {
//                    super.onFailure(error, content);
//                    if(deleteHistoryCallback != null){
//                        deleteHistoryCallback.callback(false);
//                    }
//                }
//            });
//        }else{//未登录
//            HistoryProxy.getInstance().addProxyRunnable(new SingleThreadProxy.ProxyRunnable() {
//                @Override
//                public void run() {
//                    boolean status = FileCommonUtil.deleteFile(getCinemaTempHistoryFolder());//删除在线临时历史文件夹
//                    if(deleteHistoryCallback != null){
//                        deleteHistoryCallback.callback(status);
//                    }
//                }
//            });
//        }
    }

    /**
     * 删除所有在线临时历史
     */
    public static void deleteAllCinemaTempHistory(){
//        HistoryProxy.getInstance().addProxyRunnable(new SingleThreadProxy.ProxyRunnable() {
//            @Override
//            public void run() {
//                FileCommonUtil.deleteFile(getCinemaTempHistoryFolder());//删除在线临时历史文件夹
//            }
//        });
    }

    /**
     * 删除单个本地历史
     */
    public static void deleteSingleLocalHistory(final String videoPath, final DeleteHistoryCallback deleteAllHistoryCallback){
//        HistoryProxy.getInstance().addProxyRunnable(new SingleThreadProxy.ProxyRunnable() {
//            @Override
//            public void run() {
//                boolean status = FileCommonUtil.deleteFile(getLocalHistoryPath(videoPath));//删除本地历史
//                if(deleteAllHistoryCallback != null){
//                    deleteAllHistoryCallback.callback(status);
//                }
//            }
//        });
    }

    /**
     * 删除所有本地历史
     */
    public static void deleteAllLocalHistory(final DeleteHistoryCallback deleteAllHistoryCallback){
//        HistoryProxy.getInstance().addProxyRunnable(new SingleThreadProxy.ProxyRunnable() {
//            @Override
//            public void run() {
//                boolean status = FileCommonUtil.deleteFile(getLocalHistoryFolder());//删除本地历史文件夹
//                if(deleteAllHistoryCallback != null){
//                    deleteAllHistoryCallback.callback(status);
//                }
//            }
//        });
    }

    /**
     * 从文件读所有的历史信息
     * @param pageNum 第几页（1是第一页）
     * @param pageCount 每页个数
     * @param sortRule 排序规则 0文件上次修改时间排序 1：文件名排序
     * @param type 0本地历史，1在线历史，2两者都
     */
    public static String readAllFromHistory(final int pageNum, final int pageCount, final int sortRule, final int type){
        String totalNum = "0";//总个数
        JSONArray historyJSONArray = new JSONArray();
        TreeMap fileTreeMap = getFileTreeMap(sortRule, type);
        LogHelper.e("infos","fileTreeMap.size==="+fileTreeMap.size());
        if(fileTreeMap != null && fileTreeMap.size() > 0){
            totalNum = String.valueOf(fileTreeMap.size());
            int startIndex = (pageNum - 1) * pageCount;//起始位置，例如第一页(pageNum=1)的起始位置是0
            if(startIndex < fileTreeMap.size()){
                int endIndex = startIndex + pageCount;//从起始位置开始，检索一页的数据
                if(endIndex > fileTreeMap.size()){
                    endIndex = fileTreeMap.size();
                }
                Iterator iterator = fileTreeMap.values().iterator();
                int count = 0;//计数
                while(iterator.hasNext()) {
                    File file = (File) iterator.next();
                    if(count >= startIndex && count < endIndex){
                        try {
                            String historyJson = FileCommonUtil.readFileString(file);
                            historyJSONArray.put(new JSONObject(historyJson));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    count++;//计数加1
                }
            }
        }
        return createResultJson("1", "请求成功", totalNum, historyJSONArray);
    }



    /**
     * 获取排序的fileTreeMap
     */
    private static TreeMap getFileTreeMap(int sortRule, int type){
        TreeMap fileTreeMap = null;
        if(FileCommonUtil.ruleFileName == sortRule){//根据文件名排序
            fileTreeMap = new TreeMap<String,File>(new ComparatorString());
        }else if(FileCommonUtil.ruleFileLastModify == sortRule){//根据文件上次修改时间排序
            fileTreeMap = new TreeMap<Long,File>(new ComparatorLong());
        }

        if(historyTypeLocal == type) {//本地历史
            addToFileTreeMap(fileTreeMap, sortRule, getLocalHistoryFolder());
        }else if(historyTypeCinema == type) {//在线历史
//            if(UserSpBusiness.getInstance().isUserLogin()) {//已登录，读在线历史文件夹
//                addToFileTreeMap(fileTreeMap, sortRule, getCinemaHistoryFolder());
//            }else{//未登录，读在线临时历史文件夹
                addToFileTreeMap(fileTreeMap, sortRule, getCinemaTempHistoryFolder());
                LogHelper.e("infos","===未登录，读在线临时历史文件夹===");
//            }
        }else{//两者都
            addToFileTreeMap(fileTreeMap, sortRule, getLocalHistoryFolder());
//            if(UserSpBusiness.getInstance().isUserLogin()) {//已登录，读在线历史文件夹
//                addToFileTreeMap(fileTreeMap, sortRule, getCinemaHistoryFolder());
//            }else{//未登录，读在线临时历史文件夹
                addToFileTreeMap(fileTreeMap, sortRule, getCinemaTempHistoryFolder());
//            }
        }
        return fileTreeMap;
    }

    /**
     * 加入fileTreeMap
     */
    private static void addToFileTreeMap(TreeMap fileTreeMap, int sortRule, String dirFilePath){
        File dirFile = new File(dirFilePath);//根目录
        if(dirFile != null && dirFile.exists() && dirFile.isDirectory()){
            File[] fileArr = dirFile.listFiles();
            if(fileArr != null && fileArr.length > 0){
                if(FileCommonUtil.ruleFileName == sortRule) {//根据文件名排序
                    for(File file : fileArr){
                        fileTreeMap.put(file.getName(), file);
                    }
                }else{//根据文件上次修改时间排序
                    for(File file : fileArr){
                        fileTreeMap.put(file.lastModified(), file);
                        LogHelper.e("infos","file.path==="+file.getAbsolutePath());
                    }
                }
            }
        }
    }

    /**
     * 上报所有在线临时历史记录
     */
//    public static void reportAllCinemaTempHistory(){
//        final File dirFile = new File(getCinemaTempHistoryFolder());//在线临时历史文件夹
//        if(dirFile != null && dirFile.exists() && dirFile.isDirectory()){
//            final File[] fileArr = dirFile.listFiles();
//            if(fileArr != null && fileArr.length > 0){
//                final JSONArray historyJa = new JSONArray();
//                for(File file : fileArr){
//                    String historyJson = FileCommonUtil.readFileString(file);
//                    historyJa.put(CreateHistoryUtil.createReportJson(historyJson));
//                }
//                Activity curActivity = BaseApplication.INSTANCE.getCurrentActivity();
//                if(curActivity != null){
//                    curActivity.runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            new VideoApi().reportCinemaHistory(historyJa.toString(), new ApiCallBack<String>() {//开始上报
//                                @Override
//                                public void onSuccess(String result) {
//                                    super.onSuccess(result);
//                                    if (!TextUtils.isEmpty(result)) {
//                                        try {
//                                            JSONObject resultJo = new JSONObject(result);
//                                            if ("1".equals(resultJo.getString("status"))) {//上报成功
//                                                reportSuccess();
//                                            }
//                                        } catch (JSONException e) {
//                                            e.printStackTrace();
//                                        }
//                                    }
//                                }
//
//                                @Override
//                                public void onFailure(Throwable error, String content) {
//                                    super.onFailure(error, content);
//                                }
//                            });
//                        }
//                    });
//                }
//            }
//        }
//    }

    /**
     * 上报成功
     */
//    private static void reportSuccess(){
//        HistoryProxy.getInstance().addProxyRunnable(new SingleThreadProxy.ProxyRunnable() {
//            @Override
//            public void run() {
//                File dirFile = new File(getCinemaTempHistoryFolder());//在线临时历史文件夹
//                if(dirFile != null && dirFile.exists() && dirFile.isDirectory()) {
//                    File[] fileArr = dirFile.listFiles();
//                    if (fileArr != null && fileArr.length > 0) {
//                        for(File file : fileArr){//拷贝文件到，在线历史文件夹
//                            File targetFile = new File(getCinemaHistoryFolder(), file.getName());
//                            if(!targetFile.exists()){//目标文件不存在
//                                FileCommonUtil.copyFile(file, targetFile, 1024);
//                            }
//                        }
//                        FileCommonUtil.deleteFile(dirFile);//删除在线临时历史文件夹
//                    }
//                }
//            }
//        });
//    }

    /**
     * 保存在线历史到本地
     */
//    public static void saveCinemaHistoryToLocal(final JSONArray jaResult){
//        if(jaResult == null || jaResult.length() == 0){
//            return;
//        }
//        HistoryProxy.getInstance().addProxyRunnable(new SingleThreadProxy.ProxyRunnable() {
//            @Override
//            public void run() {
//                for(int i = 0; i < jaResult.length(); i++){
//                    try {
//                        JSONObject joResult = jaResult.getJSONObject(i);
//                        String videoId = joResult.getString("videoId");
//                        String cinemaHistoryPath = getCinemaHistoryPath(videoId);
//                        if(!new File(cinemaHistoryPath).exists()){//在线历史不存在
//                            FileCommonUtil.writeFileString(joResult.toString(), cinemaHistoryPath);
//                        }
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        });
//    }

    /**
     * 创建结果json
     */
    public static String createResultJson(String status, String status_msg, String total, JSONArray data){
        JSONObject resultJo = new JSONObject();
        try {
            resultJo.put("status", status);
            resultJo.put("status_msg", status_msg);
            resultJo.put("total", total);
            resultJo.put("data", data);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return resultJo.toString();
    }

    public interface DeleteHistoryCallback {
        void callback(boolean status);
    }


    public static class VideoViewparam{
        public int mVideo3DType;
        public int _videoModelType;
    }


    public static VideoViewparam JudgeVideoType(String dimension, String is_Pano)
    {

        VideoViewparam videoViewparam = new VideoViewparam();

        if (dimension.equals("0"))//2d
        {
            videoViewparam.mVideo3DType = VideoModeType.Video2D;
            videoViewparam._videoModelType = VideoModeType.Mode_Sphere360;
        }
        else if (dimension.equals("1"))//2d
        {
            if (is_Pano.equals("2"))
            {
                videoViewparam.mVideo3DType = VideoModeType.Video2D ;
                videoViewparam._videoModelType = VideoModeType.Mode_Sphere360 ;
            }
            else if (is_Pano.equals("4"))//2d 立方体
            {

                videoViewparam.mVideo3DType = VideoModeType.Video2D ;
                videoViewparam._videoModelType = VideoModeType.Mode_Box ;
            }
        }
        else if (dimension.equals("2") && is_Pano.equals("2"))//3d 上下
        {

            videoViewparam.mVideo3DType = VideoModeType.VideoUD3D ;
            videoViewparam._videoModelType = VideoModeType.Mode_Sphere360 ;

        }
        else if (dimension.equals("3") && is_Pano.equals("3"))//3d 180 左右
        {

            videoViewparam.mVideo3DType = VideoModeType.VideoLR3D ;
            videoViewparam._videoModelType = VideoModeType.Mode_Sphere180 ;
        }
        else
        {
            videoViewparam.mVideo3DType = VideoModeType.Video2D ;
            videoViewparam._videoModelType = VideoModeType.Mode_Sphere360 ;
        }

        return videoViewparam;
    }

    /**
     * 本地视频播放根据极简模式中的videotype计算u3d中需要的video3Dtype 和 videoType
     * @param localVideoType
     * @return
     */
    public static VideoViewparam JudgeVideoType(int localVideoType)
    {
        VideoViewparam videoViewparam = new VideoViewparam();
        switch (localVideoType){
            case VideoTypeUtil.MJVideoPictureTypeUnknown:
            case VideoTypeUtil.MJVideoPictureTypeSingle:  //2d\
                videoViewparam.mVideo3DType = 1;
                videoViewparam._videoModelType=1;
                break;
            case VideoTypeUtil.MJVideoPictureTypeSideBySide: //3d左右
                videoViewparam.mVideo3DType = 3;
                videoViewparam._videoModelType=1;
                break;
            case VideoTypeUtil.MJVideoPictureTypeStacked: //3d上下
                videoViewparam.mVideo3DType = 2;
                videoViewparam._videoModelType=1;
                break;
            case VideoTypeUtil.MJVideoPictureTypePanorama360: //360度全景
                videoViewparam.mVideo3DType = 1;
                videoViewparam._videoModelType=2;
                break;
            case VideoTypeUtil.MJVideoPictureTypePanorama3603DStacked: //360度全景3d上下
                videoViewparam.mVideo3DType = 2;
                videoViewparam._videoModelType=2;
                break;
            case VideoTypeUtil.MJVideoPictureTypePanorama3603DSide: //360度全景3d左右
                videoViewparam.mVideo3DType = 3;
                videoViewparam._videoModelType=2;
                break;
            case VideoTypeUtil.MJVideoPictureTypePanorama360Cube: //立方体全景
                videoViewparam.mVideo3DType = 1;
                videoViewparam._videoModelType=4;
                break;
            case VideoTypeUtil.MJVideoPictureTypePanorama1803DSide: ////180度全景3d左右
                videoViewparam.mVideo3DType = 3;
                videoViewparam._videoModelType=3;
                break;
            default:
                videoViewparam.mVideo3DType = 1;
                videoViewparam._videoModelType=1;
                break;

        }
        return videoViewparam;

    }
    /**end  ****/

    /**
     *  本地存储的播放类型  public int playType;//播放类型 1:硬解  2：硬解+  3 软解
     *  播放核心定义的playertype ：
     *  int TYPE_NONE = 0;
     *  int TYPE_SYS = 1;
     *  int TYPE_SOFT = 2;
     *  int TYPE_SYSPLUS = 3;
     * @param type
     * @return
     */
     //将读取本地playerType转换成播放核心对应type
    public static int JudgePlayerTypeHistroyToCore(int type){
       switch (type){
           case 1:
               return IBfPlayerConstant.IBasePlayerType.TYPE_SYS;
           case 2:
               return IBfPlayerConstant.IBasePlayerType.TYPE_SYSPLUS;
           case 3:
               return IBfPlayerConstant.IBasePlayerType.TYPE_SOFT;
       }
        return IBfPlayerConstant.IBasePlayerType.TYPE_NONE;

    }
    //播放核心playerType转换成对应的存储plyerType供u3d使用
    public static int JudgePlayerTypeCoreToHistroy(int type){
            switch (type){
                case IBfPlayerConstant.IBasePlayerType.TYPE_SYS:
                    return 1;
                case IBfPlayerConstant.IBasePlayerType.TYPE_SYSPLUS:
                    return 2;
                case IBfPlayerConstant.IBasePlayerType.TYPE_SOFT:
                    return 3;
            }
        return 2;
    }

//    public static void editLocalHistoryFile(){
//        ThreadProxy.getInstance().addRun(new ThreadProxy.IHandleThreadWork() {
//            @Override
//            public void doWork() {
//                String path = getLocalHistoryFolder();
//                File dirs = new File(path);
//                if(dirs==null||!dirs.exists()||!dirs.isDirectory())
//                    return;
//                File[]  files = dirs.listFiles();
//                for (File file:files) {
//                    if(!file.exists()){
//                        continue;
//                    }
//                    editFile(file);
//                }
//            }
//        });
//
//
//    }

//    private static void editFile(File file){
//        String fileJson =  FileCommonUtil.readFileString(file);
//        try {
//            if(fileJson!=null) {
//                JSONObject myJsonObject = new JSONObject(fileJson);
//                HistoryInfo historyInfo = CreateHistoryUtil.localJsonToHistoryInfo(myJsonObject);
//                historyInfo.setVideo3dType(-1);
//                historyInfo.setVideoType(-1);
//                String json=new Gson().toJson(historyInfo);
//                FileCommonUtil.writeFileString(json,file);
//            }
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//    }
}
