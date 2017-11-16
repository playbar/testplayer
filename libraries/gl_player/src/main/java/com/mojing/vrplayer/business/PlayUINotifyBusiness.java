package com.mojing.vrplayer.business;


import com.mojing.vrplayer.bean.AudioSteamInfo;
import com.mojing.vrplayer.interfaces.IBaseControlView;
import com.mojing.vrplayer.publicc.bean.ContentInfo;
import com.mojing.vrplayer.publicc.bean.LocalVideoBean;
import com.mojing.vrplayer.publicc.bean.VideoDetailBean;
import com.mojing.vrplayer.view.BasePlayControlUI;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wanghongfang on 2017/6/2.
 * UI信息同步处理类
 */
public class PlayUINotifyBusiness implements IBaseControlView {
    private List<BasePlayControlUI> mListUI = new ArrayList<>();
    public PlayUINotifyBusiness(){

    }
    public void addUIView(BasePlayControlUI controlUI){
        if(mListUI!=null){
            mListUI.add(controlUI);
        }
    }

    @Override
    public void setPlayOrPauseBtn(boolean playOrPause) {
        if(mListUI==null||mListUI.size()<=0)
            return;
        for (BasePlayControlUI basePlayControlUI:mListUI){
            basePlayControlUI.setPlayOrPauseBtn(playOrPause);
        }
    }
    public void setPlayOrPauseBtn(boolean playOrPause,BasePlayControlUI controlUI) {
        if(mListUI==null||mListUI.size()<=0)
            return;
        for (BasePlayControlUI basePlayControlUI:mListUI){
            if(basePlayControlUI.equals(controlUI)){
                continue;
            }
            basePlayControlUI.setPlayOrPauseBtn(playOrPause);
        }
    }
    @Override
    public void setProcess(int process) {
        if(mListUI==null||mListUI.size()<=0)
            return;
        for (BasePlayControlUI basePlayControlUI:mListUI){
            basePlayControlUI.setProcess(process);
        }
    }

    @Override
    public void updateProgress(int current, int duration) {
        if(mListUI==null||mListUI.size()<=0)
            return;
        for (BasePlayControlUI basePlayControlUI:mListUI){
            basePlayControlUI.updateProgress(current,duration);
        }
    }

    @Override
    public boolean isPlayFlag() {
        return false;
    }

    @Override
    public void setName(String name) {
        if(mListUI==null||mListUI.size()<=0)
            return;
        for (BasePlayControlUI basePlayControlUI:mListUI){
            basePlayControlUI.setName(name);
        }
    }

    @Override
    public void setDisplayDuration(long duration) {
        if(mListUI==null||mListUI.size()<=0)
            return;
        for (BasePlayControlUI basePlayControlUI:mListUI){
            basePlayControlUI.setDisplayDuration(duration);
        }
    }

    @Override
    public void setMovieVideoDatas(VideoDetailBean videosBean, int index) {
        if(mListUI==null||mListUI.size()<=0)
            return;
        for (BasePlayControlUI basePlayControlUI:mListUI){
            basePlayControlUI.setMovieVideoDatas(videosBean,index);
        }
    }

    @Override
    public void setCurrentNum(int index) {
        if(mListUI==null||mListUI.size()<=0)
            return;
        for (BasePlayControlUI basePlayControlUI:mListUI){
            basePlayControlUI.setCurrentNum(index);
        }
    }

    public void setCurrentNum(int index,BasePlayControlUI controlUI) {
        if(mListUI==null||mListUI.size()<=0)
            return;
        for (BasePlayControlUI basePlayControlUI:mListUI){
//            if(basePlayControlUI.equals(controlUI)) {
//                continue;
//            }
            basePlayControlUI.setCurrentNum(index);
        }
    }

    @Override
    public void setVolume(int vm) {
        if(mListUI==null||mListUI.size()<=0)
            return;
        for (BasePlayControlUI basePlayControlUI:mListUI){
            basePlayControlUI.setVolume(vm);
        }
    }

    public void setVolume(int vm,BasePlayControlUI controlUI) {
        if(mListUI==null||mListUI.size()<=0)
            return;
        for (BasePlayControlUI basePlayControlUI:mListUI){
            if(basePlayControlUI.equals(controlUI)){
                continue;
            }
            basePlayControlUI.setVolume(vm);
        }
    }

    @Override
    public void setSoundMute(boolean flag) {
        if(mListUI==null||mListUI.size()<=0)
            return;
        for (BasePlayControlUI basePlayControlUI:mListUI){
            basePlayControlUI.setSoundMute(flag);
        }
    }

    public void setSoundMute(boolean flag,BasePlayControlUI controlUI) {
        if(mListUI==null||mListUI.size()<=0)
            return;
        for (BasePlayControlUI basePlayControlUI:mListUI){
            if(basePlayControlUI.equals(controlUI)){
                continue;
            }
            basePlayControlUI.setSoundMute(flag);
        }
    }

    @Override
    public void setHDdata(String[] strs, String defaultHD) {
        if(mListUI==null||mListUI.size()<=0)
            return;
        for (BasePlayControlUI basePlayControlUI:mListUI){
            basePlayControlUI.setHDdata(strs,defaultHD);
        }
    }

    @Override
    public void setSelectedHD(String hdtype) {
        if(mListUI==null||mListUI.size()<=0)
            return;
        for (BasePlayControlUI basePlayControlUI:mListUI){
            basePlayControlUI.setSelectedHD(hdtype);
        }
    }

    public void setSelectedHD(String hdtype,BasePlayControlUI controlUI) {
        if(mListUI==null||mListUI.size()<=0)
            return;
        for (BasePlayControlUI basePlayControlUI:mListUI){
//            if(basePlayControlUI.equals(controlUI)){
//                continue;
//            }
            basePlayControlUI.setSelectedHD(hdtype);
        }
    }

    @Override
    public void setType(int type) {
        if(mListUI==null||mListUI.size()<=0)
            return;
        for (BasePlayControlUI basePlayControlUI:mListUI){
            basePlayControlUI.setType(type);
        }
    }

    @Override
    public void showLoading() {
        if(mListUI==null||mListUI.size()<=0)
            return;
        for (BasePlayControlUI basePlayControlUI:mListUI){
            basePlayControlUI.showLoading();
        }
    }

    @Override
    public void hideLoading() {
        if(mListUI==null||mListUI.size()<=0)
            return;
        for (BasePlayControlUI basePlayControlUI:mListUI){
            basePlayControlUI.hideLoading();
        }
    }

    @Override
    public void doNetChanged(boolean isPlayError) {
        if(mListUI==null||mListUI.size()<=0)
            return;
        for (BasePlayControlUI basePlayControlUI:mListUI){
            basePlayControlUI.doNetChanged(isPlayError);
        }
    }

    @Override
    public void handleNetWorkException() {
        if(mListUI==null||mListUI.size()<=0)
            return;
        for (BasePlayControlUI basePlayControlUI:mListUI){
            basePlayControlUI.handleNetWorkException();
        }
    }

    @Override
    public void setLoadText(String text, String name) {
        if(mListUI==null||mListUI.size()<=0)
            return;
        for (BasePlayControlUI basePlayControlUI:mListUI){
            basePlayControlUI.setLoadText(text,name);
        }
    }

    @Override
    public void showToast(String text, int type) {
        if(mListUI==null||mListUI.size()<=0)
            return;
        for (BasePlayControlUI basePlayControlUI:mListUI){
            basePlayControlUI.showToast(text,type);
        }
    }

    @Override
    public void showImgToast(String text, int imgRes) {
        if(mListUI==null||mListUI.size()<=0)
            return;
        for (BasePlayControlUI basePlayControlUI:mListUI){
            basePlayControlUI.showImgToast(text,imgRes);
        }
    }

    @Override
    public void updateSeekingProcess(int curpos, int duration, boolean rewind) {
        if(mListUI==null||mListUI.size()<=0)
            return;
        for (BasePlayControlUI basePlayControlUI:mListUI){
            basePlayControlUI.updateSeekingProcess(curpos,duration,rewind);
        }
    }

    @Override
    public void setSeekingProcessVisable(boolean visable) {
        if(mListUI==null||mListUI.size()<=0)
            return;
        for (BasePlayControlUI basePlayControlUI:mListUI){
            basePlayControlUI.setSeekingProcessVisable(visable);
        }
    }

    @Override
    public void setSelectedRatio(String ratioName) {
        if(mListUI==null||mListUI.size()<=0)
            return;
        for (BasePlayControlUI basePlayControlUI:mListUI){
            basePlayControlUI.setSelectedRatio(ratioName);
        }
    }

    public void setSelectedRatio(String ratioName,BasePlayControlUI controlUI) {
        if(mListUI==null||mListUI.size()<=0)
            return;
        for (BasePlayControlUI basePlayControlUI:mListUI){
            if(basePlayControlUI.equals(controlUI)){
                continue;
            }
            basePlayControlUI.setSelectedRatio(ratioName);
        }
    }

    @Override
    public void setSelectedLeftRightMode(int mode) {
        if(mListUI==null||mListUI.size()<=0)
            return;
        for (BasePlayControlUI basePlayControlUI:mListUI){
            basePlayControlUI.setSelectedLeftRightMode(mode);
        }
    }

    public void setSelectedLeftRightMode(int mode,BasePlayControlUI controlUI) {
        if(mListUI==null||mListUI.size()<=0)
            return;
        for (BasePlayControlUI basePlayControlUI:mListUI){
            if(basePlayControlUI.equals(controlUI)){
                continue;
            }
            basePlayControlUI.setSelectedLeftRightMode(mode);
        }
    }

    @Override
    public void setSelectedRoundMode(int mode) {
        if(mListUI==null||mListUI.size()<=0)
            return;
        for (BasePlayControlUI basePlayControlUI:mListUI){
            basePlayControlUI.setSelectedRoundMode(mode);
        }
    }

    public void setSelectedRoundMode(int mode,BasePlayControlUI controlUI) {
        if(mListUI==null||mListUI.size()<=0)
            return;
        for (BasePlayControlUI basePlayControlUI:mListUI){
            if(basePlayControlUI.equals(controlUI)){
                continue;
            }
            basePlayControlUI.setSelectedRoundMode(mode);
        }
    }

    @Override
    public void setAudioStreamList(List<AudioSteamInfo.AudioStremItem> audioStreamList) {
        if(mListUI==null||mListUI.size()<=0)
            return;
        for (BasePlayControlUI basePlayControlUI:mListUI){
            basePlayControlUI.setAudioStreamList(audioStreamList);
        }
    }

    @Override
    public void setSubtitleList(List<String> subtitleList) {
        if(mListUI==null||mListUI.size()<=0)
            return;
        for (BasePlayControlUI basePlayControlUI:mListUI){
            basePlayControlUI.setSubtitleList(subtitleList);
        }
    }

    @Override
    public void showSubTitle(String content) {
        if(mListUI==null||mListUI.size()<=0)
            return;
        for (BasePlayControlUI basePlayControlUI:mListUI){
            basePlayControlUI.showSubTitle(content);
        }
    }

    @Override
    public void setSelectedAudioStream(int index) {
        if(mListUI==null||mListUI.size()<=0)
            return;
        for (BasePlayControlUI basePlayControlUI:mListUI){
            basePlayControlUI.setSelectedAudioStream(index);
        }
    }

    public void setSelectedAudioStream(int index,BasePlayControlUI controlUI) {
        if(mListUI==null||mListUI.size()<=0)
            return;
        for (BasePlayControlUI basePlayControlUI:mListUI){
            if(basePlayControlUI.equals(controlUI)){
                continue;
            }
            basePlayControlUI.setSelectedAudioStream(index);
        }
    }

    @Override
    public void setSelectedSubtitle(String name) {
        if(mListUI==null||mListUI.size()<=0)
            return;
        for (BasePlayControlUI basePlayControlUI:mListUI){
            basePlayControlUI.setSelectedSubtitle(name);
        }
    }

    public void setSelectedSubtitle(String name,BasePlayControlUI controlUI) {
        if(mListUI==null||mListUI.size()<=0)
            return;
        for (BasePlayControlUI basePlayControlUI:mListUI){
            if(basePlayControlUI.equals(controlUI)){
                continue;
            }
            basePlayControlUI.setSelectedSubtitle(name);
        }
    }

    @Override
    public void setSelectedDecodeType(int type) {
        if(mListUI==null||mListUI.size()<=0)
            return;
        for (BasePlayControlUI basePlayControlUI:mListUI){
            basePlayControlUI.setSelectedDecodeType(type);
        }
    }

    public void setSelectedDecodeType(int type,BasePlayControlUI controlUI) {
        if(mListUI==null||mListUI.size()<=0)
            return;
        for (BasePlayControlUI basePlayControlUI:mListUI){
            if(basePlayControlUI.equals(controlUI)){
                continue;
            }
            basePlayControlUI.setSelectedDecodeType(type);
        }
    }

    @Override
    public void setSelectedScreenSize(int type) {
        if(mListUI==null||mListUI.size()<=0)
            return;
        for (BasePlayControlUI basePlayControlUI:mListUI){
            basePlayControlUI.setSelectedScreenSize(type);
        }
    }
    public void setSelectedScreenSize(int type,BasePlayControlUI controlUI) {
        if(mListUI==null||mListUI.size()<=0)
            return;
        for (BasePlayControlUI basePlayControlUI:mListUI){
            if(basePlayControlUI.equals(controlUI)){
                continue;
            }
            basePlayControlUI.setSelectedScreenSize(type);
        }
    }

    @Override
    public void setMovieSelectVideoDatas(List<ContentInfo> contentInfos) {
        if(mListUI==null||mListUI.size()<=0)
            return;
        for (BasePlayControlUI basePlayControlUI:mListUI){
            basePlayControlUI.setMovieSelectVideoDatas(contentInfos);
        }
    }

    @Override
    public void setLocalMovieSelectVideoDatas(List<LocalVideoBean> videoList, int curIndex) {
        if(mListUI==null||mListUI.size()<=0)
            return;
        for (BasePlayControlUI basePlayControlUI:mListUI){
            basePlayControlUI.setLocalMovieSelectVideoDatas(videoList,curIndex);
        }
    }

    @Override
    public void showReplayDialog(List<ContentInfo> contentInfos, int currentIndex) {
        if(mListUI==null||mListUI.size()<=0)
            return;
        for (BasePlayControlUI basePlayControlUI:mListUI){
            basePlayControlUI.showReplayDialog(contentInfos,currentIndex);
        }
    }

    @Override
    public void hideReplayDialog() {
        if(mListUI==null||mListUI.size()<=0)
            return;
        for (BasePlayControlUI basePlayControlUI:mListUI){
            basePlayControlUI.hideReplayDialog();
        }
    }

    @Override
    public void prePage() {
        if(mListUI==null||mListUI.size()<=0)
            return;
        for (BasePlayControlUI basePlayControlUI:mListUI){
            basePlayControlUI.prePage();
        }
    }

    @Override
    public void nextPage() {
        if(mListUI==null||mListUI.size()<=0)
            return;
        for (BasePlayControlUI basePlayControlUI:mListUI){
            basePlayControlUI.nextPage();
        }
    }

    @Override
    public void onResum() {
        if(mListUI==null||mListUI.size()<=0)
            return;
        for (BasePlayControlUI basePlayControlUI:mListUI){
            basePlayControlUI.onResum();
        }
    }

    @Override
    public void onPause() {
        if(mListUI==null||mListUI.size()<=0)
            return;
        for (BasePlayControlUI basePlayControlUI:mListUI){
            basePlayControlUI.onPause();
        }
    }

    @Override
    public void onFinish() {
        if(mListUI==null||mListUI.size()<=0)
            return;
        for (BasePlayControlUI basePlayControlUI:mListUI){
            basePlayControlUI.onFinish();
        }
    }

    @Override
    public void setHDable(boolean isable) {
        if(mListUI==null||mListUI.size()<=0)
            return;
        for (BasePlayControlUI basePlayControlUI:mListUI){
            basePlayControlUI.setHDable(isable);
        }
    }

    @Override
    public void setSelectedScreenLight(int size){
        if(mListUI==null||mListUI.size()<=0)
            return;
        for (BasePlayControlUI basePlayControlUI:mListUI){
            basePlayControlUI.setSelectedScreenLight(size);
        }
    }

    @Override
    public void setSubtitleFontSize(int size) {
        if(mListUI==null||mListUI.size()<=0)
            return;
        for (BasePlayControlUI basePlayControlUI:mListUI){
            basePlayControlUI.setSubtitleFontSize(size);
        }
    }

    public void setSubtitleFontSize(int size,BasePlayControlUI controlUI) {
        if(mListUI==null||mListUI.size()<=0)
            return;
        for (BasePlayControlUI basePlayControlUI:mListUI){
            if(basePlayControlUI.equals(controlUI)){
                continue;
            }
            basePlayControlUI.setSubtitleFontSize(size);
        }
    }

    @Override
    public void hideDialogView() {
        if(mListUI==null||mListUI.size()<=0)
            return;
        for (BasePlayControlUI basePlayControlUI:mListUI){
            basePlayControlUI.hideDialogView();
        }
    }

    public void setSelectedScreenLight(int size,BasePlayControlUI controlUI){
        if(mListUI==null||mListUI.size()<=0)
            return;
        for (BasePlayControlUI basePlayControlUI:mListUI){
            if(basePlayControlUI.equals(controlUI)){
                continue;
            }
            basePlayControlUI.setSelectedScreenLight(size);
        }
    }

    public void setShowLoadToast(boolean ishow){
        if(mListUI==null||mListUI.size()<=0)
            return;
        for (BasePlayControlUI basePlayControlUI:mListUI){
            basePlayControlUI.show_load_toast = ishow;
        }
    }

    public void setSelectSourceType(int type){
        if(mListUI==null||mListUI.size()<=0)
            return;
        for (BasePlayControlUI basePlayControlUI:mListUI){
            basePlayControlUI.setSelectSourceType(type);
        }
    }

    public void setResID(String resID){
        if(mListUI==null||mListUI.size()<=0)
            return;
        for (BasePlayControlUI basePlayControlUI:mListUI){
            basePlayControlUI.setResID(resID);
        }
    }

    public void hideAllView(){
        if(mListUI==null||mListUI.size()<=0)
            return;
        for (BasePlayControlUI basePlayControlUI:mListUI){
            basePlayControlUI.hideAllView();
        }
    }

    public void reSetReplayView(){
        if(mListUI==null||mListUI.size()<=0)
            return;
        for (BasePlayControlUI basePlayControlUI:mListUI){
            basePlayControlUI.reSetReplayView();
        }
    }
}
