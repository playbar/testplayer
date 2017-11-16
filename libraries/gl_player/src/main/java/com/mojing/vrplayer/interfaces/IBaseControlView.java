package com.mojing.vrplayer.interfaces;


import com.mojing.vrplayer.bean.AudioSteamInfo;
import com.mojing.vrplayer.publicc.bean.ContentInfo;
import com.mojing.vrplayer.publicc.bean.LocalVideoBean;
import com.mojing.vrplayer.publicc.bean.VideoDetailBean;

import java.util.List;

/**
 * Created by wanghongfang on 2017/5/10.
 */
public interface IBaseControlView {
    /**更新播放状态  true 显示播放按钮，false 显示暂停按钮*/
    public void setPlayOrPauseBtn(boolean playOrPause);
    /**
     * 更新当前加载进度
     * @param process
     */
    public void setProcess(int process);

    /**
     * 更新进度
     * @param current
     * @param duration
     */
    public void updateProgress(int current, int duration);

    /**
     * true ：暂停   false：播放
     * @return
     */
    public boolean isPlayFlag();
    /**
     * 最大显示24个中文字符
     * @param name
     */
    public void setName(String name);

    /**
     * 设置时长
     * @param duration
     */
    public void setDisplayDuration(long duration);

    /**
     * 设置选集数据
     * @param videosBean
     * @param index
     */
    public void setMovieVideoDatas(VideoDetailBean videosBean, int index);

    /**
     * 设置当前选集
     * @param index
     */
    public void setCurrentNum(int index);

    /**
     * 设置音量
     * @param vm
     */
    public void setVolume(int vm);

    /**
     * 是否静音
     * @param flag true：静音  false 关闭静音
     */
    public void setSoundMute(boolean flag);

    /**
     * 设置清晰度
     * @param strs 清晰度列表
     * @param defaultHD  默认清晰度选项
     */
    public void setHDdata(String[] strs, String defaultHD);

    /**
     * 当前选择的清晰度
     * @param hdtype
     */
    public void setSelectedHD(String hdtype);


    /**
     * 设置页面类型
     * @param type
     */
    public void setType(int type);

    public void showLoading();

    public void hideLoading();
    /**
     * 通知UI层处理网络问题
     * @param isPlayError
     */
    public void doNetChanged(boolean isPlayError);

    /**
     * 网络异常通知UI处理显示对应提示
     */
    public void handleNetWorkException();

    public void setLoadText(String text, String name);

    public void showToast(String text, int type);
    /*显示带图片的toast*/
    public void showImgToast(String text, int imgRes);

    /**
     * 刷新开进快退的进度
     * @param curpos
     * @param duration
     * @param rewind   true：快进  false 快退
     */
    public void updateSeekingProcess(int curpos, int duration, boolean rewind);

    /**
     * 显示或隐藏快进快退框
     * @param visable
     */
    public void setSeekingProcessVisable(boolean visable);

    /**
     * 本地设置影院比例
     * @param ratioName
     */
    public void setSelectedRatio(String ratioName);

    /**
     * 本地设置场景2D，3D左右，3D上下
     */
    public void setSelectedLeftRightMode(int mode) ;
    /**
     * 本地设置场景平面，半球，球面
     */
    public void setSelectedRoundMode(int mode) ;

    /**
     *设置音轨列表
     * @param audioStreamList
     */
    public void setAudioStreamList(List<AudioSteamInfo.AudioStremItem> audioStreamList);

    /**
     * 设置本地字幕列表
     * @param subtitleList
     */
    public void setSubtitleList(List<String> subtitleList);

    /**
     * 显示字幕内容
     * @param content
     */
    public void showSubTitle(String content);

    /**
     * 设置选择的音轨
     * @param index
     */
    public void setSelectedAudioStream(int index);

    /**
     * 设置选择的字幕
     * @param name
     */
    public void setSelectedSubtitle(String name);

    /**
     * 设置选择的解码方式
     * @param type
     */
    public void setSelectedDecodeType(int type);

    /**
     * 设置选择的屏幕大小
     * @param type
     */
    public void setSelectedScreenSize(int type);

    /**
     * 设置在线选片数据
     * @param contentInfos
     */
    public void setMovieSelectVideoDatas(List<ContentInfo> contentInfos);

    /**
     * 设置本地选片数据
     * @param videoList
     */
    public void setLocalMovieSelectVideoDatas(List<LocalVideoBean> videoList, int curIndex);

    /**
     * 显示续播对话框
     * @param contentInfos
     */
    public void showReplayDialog(List<ContentInfo> contentInfos, int currentIndex);

    /**
     * 隐藏续播对话框
     */
    public void hideReplayDialog();
    public void prePage();

    public void nextPage();
    public void onResum();

    public void onPause();

    public void onFinish();

    public void setHDable(boolean isable);

    public void setSelectedScreenLight(int size);

    public void setSubtitleFontSize(int size);
    public void hideDialogView();
}
