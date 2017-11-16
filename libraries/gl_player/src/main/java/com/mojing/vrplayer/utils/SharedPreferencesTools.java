package com.mojing.vrplayer.utils;

import android.content.Context;
import android.text.TextUtils;

import com.bfmj.sdk.util.SharedPreferencesUtil;

/**
 * Created by huangliang on 2017/10/10.
 */

public class SharedPreferencesTools {
    static SharedPreferencesUtil mSharedPreferencesUtil;
    static SharedPreferencesTools instance;
    public SharedPreferencesTools(Context context){
        if(mSharedPreferencesUtil==null){
            mSharedPreferencesUtil=new SharedPreferencesUtil(context);
        }
    }
    public static SharedPreferencesTools getInstance(Context context){
        if(instance==null){
            instance=new SharedPreferencesTools(context);
        }
        return instance;
    }

    public  void saveLastVideoName(String name){
        if(TextUtils.isEmpty(name)){
            return;
        }
        mSharedPreferencesUtil.setString("lastPlayName",name);
    }

    public  String getLastVideoName(){
        return mSharedPreferencesUtil.getString("lastPlayName","");
    }

}
