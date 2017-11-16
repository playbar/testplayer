package com.mojing.vrplayer.interfaces;


import com.mojing.vrplayer.publicc.bean.ContentInfo;

import java.util.List;

/**
 * Created by yushaochen on 2017/6/9.
 */

public interface IPlayRecommendCallBack {
    public void onResultData(int status, List<ContentInfo> data);//-1 失败,0 成功,1 开始获取
}
