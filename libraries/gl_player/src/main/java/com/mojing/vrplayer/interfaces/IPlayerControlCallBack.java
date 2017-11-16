package com.mojing.vrplayer.interfaces;


/**
 * Created by wanghongfang on 2016/7/22.
 */
public interface IPlayerControlCallBack {
    void onPlayChanged(boolean status);//true:暂停播放 false:恢复播放
    void onSeekToChanged(int curPosition); //
    void onControlChanged(String id, boolean selectedStatus);
    void onHideControlAndSettingView(boolean isHide);//true 隐藏，false 停止隐藏
    void onChangFullScreen(boolean fullScreen); //播放切换全屏
}
