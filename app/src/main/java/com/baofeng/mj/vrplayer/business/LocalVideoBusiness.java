package com.baofeng.mj.vrplayer.business;

import android.content.Context;
import android.media.MediaMetadataRetriever;
import android.text.TextUtils;
import android.util.Log;

import com.baofeng.mj.unity.IAndroidCallback;
import com.baofeng.mj.unity.UnityActivity;
import com.baofeng.mj.vrplayer.bean.LocalVideoBean;
import com.baofeng.mj.vrplayer.util.AssetUtil;
import com.baofeng.mj.vrplayer.util.FileCommonUtil;
import com.baofeng.mj.vrplayer.util.FileSizeUtil;
import com.baofeng.mj.vrplayer.util.ImageUtil;
import com.baofeng.mj.vrplayer.util.SingleThreadProxy;
import com.baofeng.mj.vrplayer.util.SqliteProxy;
import com.baofeng.mj.vrplayer.util.ThreadPoolUtil;
import com.baofeng.mj.util.publicutil.VideoTypeUtil;
import com.bn.mojingscaner.MJSCANLib;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

/**
 * @author liuchuanchi
 * @description: 本地视频业务
 */
public class LocalVideoBusiness {
    private static LocalVideoBusiness instance;//单例
    private String fileFilter;//规则json
    private String fileTypes;//后缀名json
    private boolean isCreateVideoType;//true正在创建视频类型，false不是
    private boolean isCreateVideoThumbnail;//true正在创建视频缩略图，false不是
    private boolean isCreateVideoDuration;//true正在创建视频总时间，false不是

    static {
        System.loadLibrary("mojingscan");
    }

    private LocalVideoBusiness() {
    }

    public static LocalVideoBusiness getInstance(){
        if(instance == null){
            instance = new LocalVideoBusiness();
        }
        return instance;
    }


    /**
     * 排序视频
     * @param localVideoBeanList 视频集合
     * @param sortRule 排序规则，0文件上次修改时间排序，1文件名排序，2文件大小排序
     * @param isAscending true升序，false降序
     */
    public HashMap<String, TreeMap<String, LocalVideoBean>> sortVideo(final Context mContext, List<LocalVideoBean> localVideoBeanList, int sortRule, boolean isAscending) {
        HashMap<String, TreeMap<String, LocalVideoBean>> hashMap = new HashMap<String, TreeMap<String, LocalVideoBean>>();
        List<String> localVideoDirList = LocalVideoPathBusiness.getAllDir(mContext, false);//获取所有文件夹(所有分组)
        if (FileCommonUtil.ruleFileLastModify == sortRule) {//文件上次修改时间排序，并分组
            for (String localVideoDir : localVideoDirList) {//遍历所有文件夹
                TreeMap treeMap = null;
                if(isAscending){//升序
                    treeMap = new TreeMap<String, LocalVideoBean>(new ComparatorKeyAsc());
                }else{//降序
                    treeMap = new TreeMap<String, LocalVideoBean>(new ComparatorKeyDes());
                }
                for (LocalVideoBean localVideoBean : localVideoBeanList) {
                    if(localVideoBean.path.startsWith(localVideoDir)){
                        localVideoBean.group = localVideoDir;
                        treeMap.put(String.valueOf(localVideoBean.lastModify), localVideoBean);
                    }
                }
                hashMap.put(localVideoDir, treeMap);
            }
        } else if (FileCommonUtil.ruleFileName == sortRule) {//文件名排序，并分组
            for (String localVideoDir : localVideoDirList) {//遍历所有文件夹
                TreeMap treeMap = null;
                if(isAscending) {//升序
                    treeMap = new TreeMap<String, LocalVideoBean>(new ComparatorStringAscending());
                }else{//降序
                    treeMap = new TreeMap<String, LocalVideoBean>(new ComparatorStringDescending());
                }
                for (LocalVideoBean localVideoBean : localVideoBeanList) {
                    if(localVideoBean.path.startsWith(localVideoDir)){
                        localVideoBean.group = localVideoDir;
                        treeMap.put(localVideoBean.name, localVideoBean);
                    }
                }
                hashMap.put(localVideoDir, treeMap);
            }
        } else {//文件大小排序，并分组
            for (String localVideoDir : localVideoDirList) {//遍历所有文件夹
                TreeMap treeMap = null;
                if(isAscending){//升序
                    treeMap = new TreeMap<String, LocalVideoBean>(new ComparatorKeyAsc());
                }else{//降序
                    treeMap = new TreeMap<String, LocalVideoBean>(new ComparatorKeyDes());
                }
                for (LocalVideoBean localVideoBean : localVideoBeanList) {
                    if(localVideoBean.path.startsWith(localVideoDir)){
                        localVideoBean.group = localVideoDir;
                        treeMap.put(String.valueOf(localVideoBean.length), localVideoBean);
                    }
                }
                hashMap.put(localVideoDir, treeMap);
            }
        }
        return hashMap;
    }

    /**
     * 通过C层检索视频
     * @param mContext 上下文
     */
    public void searchVideoByNative(final Context mContext, final LocalVideoDataCallback localVideoDataCallback){
        //检索视频
        final List<LocalVideoBean> localVideoBeanList = new ArrayList<LocalVideoBean>();
        String[] allMediaFiles = MJSCANLib.getAllMediaFiles(getAllRootPaths(mContext), getFileTypes(mContext), getFileFilter(mContext));
        //从数据库获取视频类型集合
        HashMap<String, Integer> videoTypeMap = SqliteProxy.getInstance().addProxyExecute(new SqliteProxy.ProxyExecute<HashMap<String, Integer>>() {
            @Override
            public HashMap<String, Integer> execute() {
                return SqliteManager.getInstance(mContext).getAllFromLocalVideoType();
            }
        });
        if(videoTypeMap == null){
            videoTypeMap = new HashMap<String, Integer>();
        }
        for (final String mediaFile : allMediaFiles) {
            File file = new File(mediaFile);
            LocalVideoBean localVideoBean = new LocalVideoBean();
            localVideoBean.path = file.getAbsolutePath();//文件路径
            localVideoBean.name = file.getName();//文件名称
            localVideoBean.lastModify = file.lastModified();//文件上次修改时间
            localVideoBean.length = file.length();//文件大小
            localVideoBean.size = FileSizeUtil.formatFileSize(localVideoBean.length);//文件大小
            //缩略图地址
            String thumbPath = LocalVideoPathBusiness.getLocalVideoImg(mContext, localVideoBean.path);
            localVideoBean.thumbPath = FileCommonUtil.filePrefix + thumbPath;
            //视频类型
            int videoType = VideoTypeUtil.MJVideoPictureTypeUnCreate;
            String key = String.valueOf(mediaFile.hashCode());
            if(videoTypeMap.containsKey(key)){
                videoType = videoTypeMap.get(key);
            }
            localVideoBean.videoType = videoType;
            localVideoBeanList.add(localVideoBean);
        }
        SqliteProxy.getInstance().addProxyRunnable(new SqliteProxy.ProxyRunnable() {
            @Override
            public void run() {
                SqliteManager.getInstance(mContext).closeSQLiteDatabase();
            }
        });

        //创建视频类型
        createVideoType(mContext, localVideoBeanList);
        //创建视频缩略图
        createVideoThumbnail(mContext, localVideoBeanList);
        createVideoDuration(mContext, localVideoBeanList);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //返回数据
                if (localVideoDataCallback != null) {
                   // Log.v("timeall",localVideoBeanList.get(0).videoDuration+"");
                    localVideoDataCallback.callback(localVideoBeanList);
                }
            }
        }).start();

    }

    /**
     * 获取所有根路径
     */
    private String getAllRootPaths(Context mContext){
        JSONObject joDir = new JSONObject();
        try {
            JSONArray jaDir = new JSONArray();
            List<String> localVideoDirList = LocalVideoPathBusiness.getAllDir(mContext, false);
            for (String localVideoDir : localVideoDirList) {
                jaDir.put(localVideoDir);
            }
            joDir.put("rootDir",jaDir);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return joDir.toString();
    }

    /**
     * 获取后缀名json
     */
    private String getFileTypes(Context mContext) {
        if(TextUtils.isEmpty(fileTypes)){
            fileTypes = AssetUtil.loadAssetFileAsString(mContext, "fileTypes.json");
        }
        return fileTypes;
    }

    /**
     * 获取规则json
     */
    private String getFileFilter(Context mContext){
        if(TextUtils.isEmpty(fileFilter)){
            fileFilter = AssetUtil.loadAssetFileAsString(mContext, "file_filter.json");
        }
        return fileFilter;
    }

    public void createVideoDuration(final Context mContext, final List<LocalVideoBean> localVideoBeanList){
        if(isCreateVideoDuration){
            return;//直接返回
        }
        isCreateVideoDuration = true;//正在创建视频类型
        ThreadPoolUtil.runThread(new Runnable() {
            @Override
            public void run() {
                //从数据库获取视频类型集合
                HashMap<String, Integer> videoTypeMap = SqliteProxy.getInstance().addProxyExecute(new SqliteProxy.ProxyExecute<HashMap<String, Integer>>() {
                    @Override
                    public HashMap<String, Integer> execute() {
                        return SqliteManager.getInstance(mContext).getAllFromLocalVideoDuration();
                    }
                });
                if(videoTypeMap == null){
                    videoTypeMap = new HashMap<String, Integer>();
                }
                final MediaMetadataRetriever retriever = new MediaMetadataRetriever();
                for(final LocalVideoBean localVideoBean : localVideoBeanList){
                    String key = String.valueOf(localVideoBean.path.hashCode());
                    if (!videoTypeMap.containsKey(key)) {//没有创建过视频类型
                        try{
                            retriever.setDataSource(localVideoBean.path);
                            String durationString = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
                            if(TextUtils.isEmpty(durationString)){
                                continue;
                            }
                            localVideoBean.videoDuration =(int)(Long.valueOf(durationString) / 1000) ;//视频总时长为秒
                            SqliteProxy.getInstance().addProxyRunnable(new SingleThreadProxy.ProxyRunnable() {
                                @Override
                                public void run() {//存入数据库
                                    SqliteManager.getInstance(mContext).addToLocalVideoDuration(localVideoBean.path, localVideoBean.videoDuration);
                                }
                            });
    //                        if(UnityActivity.INSTANCE != null){
    //                            IAndroidCallback iAndroidCallback = UnityActivity.INSTANCE.getIAndroidCallback();
    //                            if(iAndroidCallback != null) {//通知Unity
    //                                iAndroidCallback.sendGetVideoTypeCompleted(localVideoBean.path, videoType);//发送获取视频类型成功
    //                            }
    //                        }
                        } catch (IllegalArgumentException ex) {
                        } catch (RuntimeException ex) {
                        } catch (Exception ex) {
                        }
                    }else{
                        localVideoBean.videoDuration=videoTypeMap.get(key);
                    }
                }
                SqliteProxy.getInstance().addProxyRunnable(new SqliteProxy.ProxyRunnable() {
                    @Override
                    public void run() {
                        SqliteManager.getInstance(mContext).closeSQLiteDatabase();
                    }
                });
                try {
                    retriever.release();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                isCreateVideoDuration = false;//创建视频类型结束
            }
        });
    }

    /**
     * 创建视频类型
     */
    private void createVideoType(final Context mContext, final List<LocalVideoBean> localVideoBeanList){
        if(isCreateVideoType){
            return;//直接返回
        }
        isCreateVideoType = true;//正在创建视频类型
        ThreadPoolUtil.runThread(new Runnable() {
            @Override
            public void run() {
                //从数据库获取视频类型集合
                HashMap<String, Integer> videoTypeMap = SqliteProxy.getInstance().addProxyExecute(new SqliteProxy.ProxyExecute<HashMap<String, Integer>>() {
                    @Override
                    public HashMap<String, Integer> execute() {
                        return SqliteManager.getInstance(mContext).getAllFromLocalVideoType();
                    }
                });
                if(videoTypeMap == null){
                    videoTypeMap = new HashMap<String, Integer>();
                }
                final MediaMetadataRetriever retriever = new MediaMetadataRetriever();
                for(final LocalVideoBean localVideoBean : localVideoBeanList){
                    String key = String.valueOf(localVideoBean.path.hashCode());
                    if (!videoTypeMap.containsKey(key)) {//没有创建过视频类型
                        final int videoType = ImageUtil.createVideoType(retriever, localVideoBean.path);//创建视频类型
                        SqliteProxy.getInstance().addProxyRunnable(new SingleThreadProxy.ProxyRunnable() {
                            @Override
                            public void run() {//存入数据库
                                SqliteManager.getInstance(mContext).addToLocalVideoType(localVideoBean.path, videoType);
                            }
                        });
                        if(UnityActivity.INSTANCE != null){
                            IAndroidCallback iAndroidCallback = UnityActivity.INSTANCE.getIAndroidCallback();
                            if(iAndroidCallback != null) {//通知Unity
                                iAndroidCallback.sendGetVideoTypeCompleted(localVideoBean.path, videoType);//发送获取视频类型成功
                            }
                        }
                    }
                }
                SqliteProxy.getInstance().addProxyRunnable(new SqliteProxy.ProxyRunnable() {
                    @Override
                    public void run() {
                        SqliteManager.getInstance(mContext).closeSQLiteDatabase();
                    }
                });
                try {
                    retriever.release();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                isCreateVideoType = false;//创建视频类型结束
            }
        });
    }


    /**
     * 创建视频缩略图
     */
    private void createVideoThumbnail(final Context mContext, final List<LocalVideoBean> localVideoBeanList){
        if(isCreateVideoThumbnail){
            return;//直接返回
        }
        isCreateVideoThumbnail = true;//正在创建视频缩略图
        ThreadPoolUtil.runThread(new Runnable() {
            @Override
            public void run() {
                MediaMetadataRetriever retriever = new MediaMetadataRetriever();
                for(final LocalVideoBean localVideoBean : localVideoBeanList){
                    boolean result = ImageUtil.createVideoThumbnail(mContext, retriever, localVideoBean.path, 320, 240);//创建视频缩略图
                    if(result){//创建成功
                        if(UnityActivity.INSTANCE != null){
                            IAndroidCallback iAndroidCallback = UnityActivity.INSTANCE.getIAndroidCallback();
                            if(iAndroidCallback != null) {//通知Unity
                                iAndroidCallback.sendLocalVideoThumbnailCompleted(localVideoBean.path);//发送本地视频缩略图创建完成
                            }
                        }
                    }
                }
                try {
                    retriever.release();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                isCreateVideoThumbnail = false;//创建视频缩略图结束
            }
        });
    }

    public interface LocalVideoDataCallback{
        void callback(List<LocalVideoBean> resultList);
    }
}
