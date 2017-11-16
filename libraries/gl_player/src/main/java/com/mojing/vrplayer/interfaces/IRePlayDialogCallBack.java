package com.mojing.vrplayer.interfaces;


import com.mojing.vrplayer.publicc.bean.ContentInfo;

/**
 * Created by wanghongfang on 2016/7/22.
 */
public interface IRePlayDialogCallBack {
    void onSelected(ContentInfo contentInfo, int currentNum);//选择第几个播放
    void onBtnClick(boolean isRePlay);//true 重播,false 退出
    void onStopTimer();//停止10s自动播放计时
}
