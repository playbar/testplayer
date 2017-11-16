package com.mojing.vrplayer.interfaces;


/**
 * Created by yushaochen on 2016/4/18.
 */
public interface IResetLayerCallBack {
    void isOpen(boolean isOpen);//true 打开；false 关闭
    void onFocusChange(boolean focused);//true 获取焦点; false 失去焦点
    void onResetView();//复位
}
