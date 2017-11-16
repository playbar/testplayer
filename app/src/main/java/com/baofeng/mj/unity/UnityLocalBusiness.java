package com.baofeng.mj.unity;

import android.util.Log;

import com.baofeng.mj.vrplayer.bean.DeviceInfo;
import com.baofeng.mj.vrplayer.bean.LocalVideoBean;
import com.baofeng.mj.vrplayer.business.LocalVideoBusiness;
import com.baofeng.mj.vrplayer.business.LocalVideoPathBusiness;
import com.baofeng.mj.vrplayer.business.PlayBusiness;
import com.baofeng.mj.vrplayer.business.flyscreen.FlyScreenBusiness;
import com.baofeng.mj.vrplayer.business.flyscreen.util.FlyScreenUtil;
import com.baofeng.mj.vrplayer.util.FileCommonUtil;
import com.baofeng.mj.vrplayer.util.LocalVideoBeanUtil;
import com.baofeng.mj.vrplayer.util.LocalVideoProxy;
import com.baofeng.mj.util.publicutil.VideoTypeUtil;

import org.json.JSONArray;

import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

/**
 * @author liuchuanchi
 * @description: Unity本地业务类
 */
public class UnityLocalBusiness {


    /**
     * 获取本地视频数据
     */
    public static void getLocalVideoData() {
        getLocalVideoData(true);
    }
    /**
     * 获取本地视频数据
     * @param isAscending true升序，false降序
     */
    public static void getLocalVideoData(final boolean isAscending) {
        getLocalVideoData(FileCommonUtil.ruleFileLastModify, isAscending);
    }

    /**
     * 获取本地视频数据
     * @param sortRule 排序规则，0文件上次修改时间排序，1文件名排序，2文件大小排序
     * @param isAscending true升序，false降序
     */
    public static void getLocalVideoData(final int sortRule, final boolean isAscending) {
        LocalVideoProxy.getInstance().addProxyRunnable(new LocalVideoProxy.ProxyRunnable() {
            @Override
            public void run() {
                LocalVideoBusiness.getInstance().searchVideoByNative(UnityActivity.INSTANCE, new LocalVideoBusiness.LocalVideoDataCallback() {
                    @Override
                    public void callback(List<LocalVideoBean> resultList) {
                        HashMap<String, TreeMap<String, LocalVideoBean>> hashMap = LocalVideoBusiness.getInstance().sortVideo(UnityActivity.INSTANCE, resultList, sortRule, isAscending);
                        if(UnityActivity.INSTANCE != null){
                            IAndroidCallback iAndroidCallback = UnityActivity.INSTANCE.getIAndroidCallback();
                            if (iAndroidCallback != null) {//通知Unity
                                if (hashMap == null || hashMap.size() == 0) {
                                    iAndroidCallback.sendLocalVideoJSONArray("");
                                } else {
                                    JSONArray localVideoJSONArray = LocalVideoBeanUtil.createJSONArray(UnityActivity.INSTANCE,hashMap);
                                    iAndroidCallback.sendLocalVideoJSONArray(localVideoJSONArray.toString());
                                }
                            }
                        }
                    }
                });
            }
        });
    }

    //----------------------飞屏start---------------------
    /**
     * 初始化设备列表
     *
     * @return void
     * @author linzanxian  @Date 2015-8-18 下午5:11:12
     */
    public static void flyScreenInit() {
        //FlyScreenInterface.getInstance(BaseApplication.INSTANCE).init();
        UnityActivity.INSTANCE.flyScreenBusiness = new FlyScreenBusiness();
        UnityActivity.INSTANCE.flyScreenBusiness.init(UnityActivity.INSTANCE);
        UnityActivity.INSTANCE.flyScreenBusiness.setTcpReceiver(true);
    }

    /**
     * 刷新设备列表
     *
     * @return void
     * @author linzanxian  @Date 2015-8-18 下午5:13:33
     */
    public static void freshDevlistClick() {
        //FlyScreenInterface.getInstance(BaseApplication.INSTANCE).freshDevlistClick();
        UnityActivity.INSTANCE.flyScreenBusiness.startScan();
    }
    /*
    * 获取设备URL
    * @return 设备URL
     */
    public static String getDeviceURL(){
        String url =  "http://" +UnityActivity.INSTANCE.flyScreenBusiness.getCurrDevInfo().getIp() + ":" + UnityActivity.INSTANCE.flyScreenBusiness.getCurrentDeviceServerPort();
        return url;
    }
    /**
     * 获取设备资源列表
     *
     * @param id
     * @return void
     * @author linzanxian  @Date 2015-8-18 下午5:18:14
     */
    public static void getDeviceResourceList(String id, boolean fresh) {
        //FlyScreenInterface.getInstance(BaseApplication.INSTANCE).getDeviceResourceList(index, fresh);
        DeviceInfo deviceInfo = null;

        List<DeviceInfo> deviceInfos = UnityActivity.INSTANCE.flyScreenBusiness.getmDeviceInfos();
        for (DeviceInfo device:deviceInfos) {
            if(device.getId().equals(id))
            {
                deviceInfo = device;
                break;
            }
        }
        if(deviceInfo != null)
            UnityActivity.INSTANCE.flyScreenBusiness.requestLoginData(deviceInfo);
        else
            System.err.println("zl->No device");
    }

//    public static void getDeviceResourceList(int index) {
//        FlyScreenInterface.getInstance(BaseApplication.INSTANCE).getDeviceResourceList(index, false);
//    }
    /*
    * 重连飞屏
     */
    public static void checkSokect(String ip)
    {
        UnityActivity.INSTANCE.flyScreenBusiness.reConnect();
    }

    /**
     * 进入目录
     *
     * @param dirUri 目录地址
     * @return void
     * @author linzanxian  @Date 2015-8-18 下午5:18:49
     */
    public static void forwardDir(String dirUri) {
        //FlyScreenInterface.getInstance(BaseApplication.INSTANCE).forwardDir(dirUri);
        UnityActivity.INSTANCE.flyScreenBusiness.forwardDirectory(dirUri);
    }

    /**
     * 返回进入
     *
     * @return void
     * @author linzanxian  @Date 2015-8-18 下午5:19:18
     */
    public static void backDir() {
        //FlyScreenInterface.getInstance(BaseApplication.INSTANCE).backDir();
        UnityActivity.INSTANCE.flyScreenBusiness.backToParentDir();
    }
    public static boolean getCurrentDirectory(){
        return UnityActivity.INSTANCE.flyScreenBusiness.getCurrentDirectory();
    }

    /**
     *   获取缓存好的飞屏字幕文件路径，播放时加载字幕使用  （u3d调用）
     * @param filePath 本地字幕路径
     * @return
     */
    public static String[] getSubtitleList(String filePath){
        List<String> strings = FlyScreenUtil.getSubtitleList(filePath);
        if(strings==null||strings.size()==0){
            return new String[0];
        }
        String[] arry = (String[])strings.toArray(new String[strings.size()]);
         return arry;
    }

    //--------飞屏callback begin-------
    public static void sendFlyScreenDeviceList(String deviceList)
    {
        if(UnityActivity.INSTANCE != null){
            IAndroidCallback iAndroidCallback = UnityActivity.INSTANCE.getIAndroidCallback();
            if(iAndroidCallback != null){//通知Unity
                iAndroidCallback.sendFlyScreenDeviceList(deviceList);
            }
        }
    }

    public static void sendFlyScreenDeviceResourceList(String VideoList)
    {
        if(UnityActivity.INSTANCE != null){
            IAndroidCallback iAndroidCallback = UnityActivity.INSTANCE.getIAndroidCallback();
            if(iAndroidCallback != null){//通知Unity
                iAndroidCallback.sendFlyScreenDeviceResourceList(VideoList);
            }
        }
    }

    public static void sendFlyScreenServerPort(int port)
    {
        if(UnityActivity.INSTANCE != null){
            IAndroidCallback iAndroidCallback = UnityActivity.INSTANCE.getIAndroidCallback();
            if(iAndroidCallback != null){//通知Unity
                iAndroidCallback.sendFlyScreenServerPort(port);
            }
        }
    }
    public static void sendFlyScreenException(int code){
        if(UnityActivity.INSTANCE != null){
            IAndroidCallback iAndroidCallback = UnityActivity.INSTANCE.getIAndroidCallback();
            if(iAndroidCallback != null){//通知Unity
                iAndroidCallback.sendFlyScreenException(code);
            }
        }
    }
    //--------飞屏callback end-------
    //----------------------飞屏end-----------------------

    /**
     * 获取视频类型
     * @param videoPath 视频地址
     */
    public static void getVideoType(final String videoPath){
        VideoTypeUtil.getVideoType(UnityActivity.INSTANCE, videoPath, new VideoTypeUtil.VideoTypeCallback() {
            @Override
            public void result(int videoType) {
                if(UnityActivity.INSTANCE != null){
                    IAndroidCallback iAndroidCallback = UnityActivity.INSTANCE.getIAndroidCallback();
                    if(iAndroidCallback != null) {//通知Unity
                        iAndroidCallback.sendGetVideoTypeCompleted(videoPath, videoType);//发送获取视频类型成功
                    }
                }
            }
        });
    }

    /**
     * 保存视频类型
     * @param videoPath 视频地址
     * @param videoType 视频类型
     */
    public static void saveVideoType(final String videoPath, final int videoType){
        VideoTypeUtil.saveVideoType(UnityActivity.INSTANCE, videoPath, videoType);
    }

    /**
     * 播放本地视频
     * @param strJson 视频列表Json
     * @param videoPath 视频地址
     * @param videoType 视频类型
     */
    public static void playLocalVideo(String strJson, String videoPath, int videoType){
        List<LocalVideoBean> localVideoBeanList = LocalVideoBeanUtil.createLocalVideoBeanList(strJson);
        int position = 0;
        for(int i = 0; i < localVideoBeanList.size(); i++){
            if(videoPath.equals(localVideoBeanList.get(i).path)){
                position = i;
                break;
            }
        }
        PlayBusiness.playLocalVideo(UnityActivity.INSTANCE, localVideoBeanList, position, videoType,true);
    }

    /**
     * 播放飞屏视频
     * @param videoPath 视频地址
     * @param videoName 视频名称
     */
    public static void playFlyScreenVideo(String videoPath, String videoName){
        int videoType = VideoTypeUtil.MJVideoPictureTypeSingle;
        PlayBusiness.playFlyScreenVideo(UnityActivity.INSTANCE, videoPath, videoName, videoType);
    }

    /**
     * 转换名称
     */
    public static String convertName(String name){
        return LocalVideoPathBusiness.convertName(name);
    }
}
