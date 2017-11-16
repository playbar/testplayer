package com.mojing.vrplayer.utils;

import com.mojing.vrplayer.interfaces.ISkyBoxChangedCallBack;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wanghongfang on 2017/4/20.
 */
public class SkyboxManager {
    private static SkyboxManager mInstance;
    private SkyboxManager(){}
    private List<ISkyBoxChangedCallBack> list = new ArrayList<>();
    public static SkyboxManager getInstance(){
        if(mInstance==null){
            mInstance = new SkyboxManager();
        }
        return mInstance;
    }

    public void onBind(ISkyBoxChangedCallBack callBack){
        if(list!=null){
            list.add(callBack);
        }
    }

    public void unBind(ISkyBoxChangedCallBack callBack){
        if(list!=null&&list.contains(callBack)){
            list.remove(callBack);
        }
    }

    public void notifyChanged(int type){
        if(list==null||list.size()<=0)
            return;

        for (ISkyBoxChangedCallBack callBack:list){
            callBack.onSkyBoxChanged(type);
        }
    }
}
