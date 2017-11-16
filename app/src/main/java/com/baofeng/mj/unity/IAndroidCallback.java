package com.baofeng.mj.unity;

/**
 * Created by zhanglei1 on 2016/5/12.
 */
public interface IAndroidCallback {

    void sendLocalVideoJSONArray(String localVideoJSONArray);//发送本地视频json

    void sendLocalVideoThumbnailCompleted(String videoPath);//发送本地视频缩略图创建完成

    void sendGetVideoTypeCompleted(String videoPath, int videoType);//发送获取视频类型成功

    void sendClearOtherApp(int count,long size);//清理数量，空间(M)

    void sendBatteryChanged(int status,int batteryLevel,int batterySum);//status==1未知(一般表示状态切换  无法获取电量 使用之前值);2充电;3放电;4没充电;5充满电;

    void sendFlyScreenDeviceList(String deviceList);// 发送飞屏设备列表

    void sendFlyScreenDeviceResourceList(String VideoList);// 发送设备上的视频列表

    void sendFlyScreenServerPort(int port); // 发送服务端口号

    void sendFlyScreenException(int code);  // 发送飞屏异常

    void sendPlayLocalMovie(String json); //播放本地视频

    void sendBlankScreen();//表示接收到这个黑屏广播

    void sendCurrentVolume(int value);//发送当前音量

    void sendHomeIsPress(int value);//短按1，长按2

    void sendHistoryJSONObject(String historyJSONObject);//发送历史json
}