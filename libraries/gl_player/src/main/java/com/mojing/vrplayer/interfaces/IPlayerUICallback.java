package com.mojing.vrplayer.interfaces;

import com.bfmj.viewcore.interfaces.IGLPlayer;

/**
 * Created by wanghongfang on 2017/5/8.
 */
public interface IPlayerUICallback {
    void onPrepared(IGLPlayer player);
    boolean onInfo(IGLPlayer player, int what, Object extra);
    void onCompletion(IGLPlayer player);
    void onSeekComplete(IGLPlayer player);
    boolean onError(IGLPlayer player, int what, int extra);
    void showLoading();
    void hideLoading();
    void onChangeToSoft();
}
