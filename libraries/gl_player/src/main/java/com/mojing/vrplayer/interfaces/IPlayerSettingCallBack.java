package com.mojing.vrplayer.interfaces;

import com.mojing.vrplayer.publicc.bean.ContentInfo;
import com.mojing.vrplayer.publicc.bean.LocalVideoBean;

/**
 * Created by wanghongfang on 2016/7/20.
 */
public interface IPlayerSettingCallBack {
    void onSettingShowChange(String id, boolean isShow);
    void onHideControlAndSettingView(boolean isHide);//true 隐藏，false 停止隐藏
    void onHDChange(String hd);//清晰度改变
    void onHDable(boolean isAble);//清晰度是否可以用
    void onSoundChange(int vm);//0-100
    void isOpenSound(boolean isOpen);//true 关闭静音，false 打开静音
    void onSelected(int index);//当前选中第几集(集合角标)
    void onRatioChange(String ratioName);//比例--改变
    void onRoteChange(int rotetype);//画面旋转--改变     1:順時針 0:逆時針
    void onLeftRightModeChange(int modetype);//2d,3d左右，3d上下--改变
    void onRoundModeChange(int modetype);//平面，半球，球面--改变
    void onResetMode();//恢复默认模式
    void onSelectedMovie(ContentInfo contentInfo, int currentNum);//当前选中第几个影片
    void onSelectedLocalMovie(LocalVideoBean bean, int currentNum);//当前选中本地第几个资源
    void onSelectedLocalMovieOri(int Ori);//0 播放下一个 ，1 播放上一个，
    void onSetSelectSourceType(int type);//当前选择资源类型
    void onScreenSizeChange(int modetype);//屏幕大小
    void onChangeDecodeType(int type);//解码方式
    void onAudioTrackChange(int index);//音轨切换
    void onSelectedSubtitle(int index, String subtitleName);//字幕切换
    void onScreenLightChange(int size);//屏幕亮度
    void onScreenSubtitleFontSize(int size);//字幕大小
    void onMovieStatus(int status);//-1 失败,0 成功,1 开始获取
}
