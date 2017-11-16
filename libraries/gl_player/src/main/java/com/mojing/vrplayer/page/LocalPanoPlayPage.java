package com.mojing.vrplayer.page;

import android.content.Context;

import com.bfmj.viewcore.util.GLExtraData;
import com.bfmj.viewcore.view.GLRectView;
import com.mojing.vrplayer.bean.VideoModeType;
import com.mojing.vrplayer.utils.GLConst;
import com.mojing.vrplayer.view.PanoPlayerView;

/**
 * Created by wanghongfang on 2017/5/12.
 */
public class LocalPanoPlayPage extends BaseLocalPlayPage{
    public LocalPanoPlayPage(Context context) {
        super(context, GLConst.LOCAL_PANO);
    }

    @Override
    public GLRectView createView(GLExtraData glExtraData) {
        super.createView(glExtraData);
        	/*切换播放场景*/
        mActivity.hideSkyBox();
        playerView = new PanoPlayerView(mActivity.getRootView(),mActivity,this);
        initData(glExtraData);
        setCurPlayMode(mCurPlayMode);
        return mRootView;
    }

    @Override
    public void setCurPlayMode(int playMode) {
        super.setCurPlayMode(playMode);
        if(playMode== VideoModeType.PLAY_MODE_SIMPLE_FULL){
            setScreenTouch(true);
        }else {
            mActivity.getRootView().queueEvent(new Runnable() {
                @Override
                public void run() {
                    mActivity.getRootView().ResetRoteDegree();
                }
            });
            setScreenTouch(false);
        }
    }
    @Override
    public void onResume() {
        super.onResume();
    }
}
