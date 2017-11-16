package com.baofeng.mj.vrplayer.business;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.baofeng.mj.vrplayer.MyAppliaction;
import com.baofeng.mj.vrplayer.activity.MainActivity;
import com.baofeng.mj.vrplayer.bean.LocalVideoBean;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by huangliang on 2017/8/24.
 */

public class FileUploadBusiness {
    SharedPreferences sharedPreferences ;
    Set<String> newUploadVideos=new HashSet<String>();
    final String sp_name_new_uploadfile="sp_name_new_uploadfile";
    final String sp_item_new_uploadfile="sp_item_new_uploadfile";

    public FileUploadBusiness(Context context){
        initSet(context);
    }

    public void saveUploadVideo(String newVideoName){
        newVideoName=getStringHashString(newVideoName);
        newUploadVideos.add(newVideoName);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.clear();
        editor.putStringSet(sp_item_new_uploadfile,newUploadVideos).commit();
    }

    public String getStringHashString(String name){
        if(TextUtils.isEmpty(name)){
            return "";
        }
        return String.valueOf(name.hashCode());
    }

    public void initSet(Context context){
        newUploadVideos.clear();
        sharedPreferences = context.getSharedPreferences(sp_name_new_uploadfile, Context.MODE_PRIVATE);
        newUploadVideos=sharedPreferences.getStringSet(sp_item_new_uploadfile,newUploadVideos);
    }
    public void removeVideoTag(String name){
        name=getStringHashString(name);
        newUploadVideos.remove(name);
        SharedPreferences.Editor editor = sharedPreferences.edit() ;
        editor.clear();
        editor.putStringSet(sp_item_new_uploadfile,newUploadVideos).commit();
    }

    public boolean removeNotExist(List<LocalVideoBean> list){

//        for(String str:newUploadVideos){
//            Log.v("testName","str: "+str);
//        }
//
//        for(LocalVideoBean ocalVideoBean:list){
//            Log.v("testName","ocalVideoBean.name: "+ocalVideoBean.name);
//        }
//        return  true;

        if(list==null||list.size()==0||newUploadVideos.size()==0){
            newUploadVideos.clear();
            SharedPreferences.Editor editor=sharedPreferences.edit();
            editor.clear();
            editor.putStringSet(sp_item_new_uploadfile,newUploadVideos).commit();
            return false;
        }
        ArrayList<String> hasItems=new ArrayList<String>();
        for(String str:newUploadVideos){
            boolean ishasNmae=false;
            for(LocalVideoBean ocalVideoBean:list){
                if(TextUtils.isEmpty(ocalVideoBean.name)){
                    continue;
                }
                if(getStringHashString(ocalVideoBean.name).equals(str)){
                    ishasNmae=true;
                }
            }
            if(ishasNmae){
                hasItems.add(str);
            }
        }

        newUploadVideos.clear();
        newUploadVideos.addAll(hasItems);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.clear();
        editor.putStringSet(sp_item_new_uploadfile,newUploadVideos).commit();
        MainActivity.tabShowNewVideoTAG();
        return !newUploadVideos.isEmpty();
    }

    public boolean isShowNewOnTab( ){
        if(newUploadVideos!=null&&newUploadVideos.size()>0){
            return true;
        }
        return false;
    }

    public boolean isShowNewOnItem(String name){
        name=getStringHashString(name);
        if(newUploadVideos.contains(name)){
            return true;
        }
        return false;
    }
}
