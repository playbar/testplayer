package com.mojing.vrplayer.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.Log;

import com.bfmj.viewcore.interfaces.GLOnKeyListener;
import com.bfmj.viewcore.view.GLRectView;
import com.bfmj.viewcore.view.GLRelativeView;
import com.mojing.vrplayer.activity.MjVrPlayerActivity;
import com.mojing.vrplayer.bean.AudioSteamInfo;
import com.mojing.vrplayer.interfaces.IPlayRecommendCallBack;
import com.mojing.vrplayer.interfaces.IPlayerSettingCallBack;
import com.mojing.vrplayer.interfaces.IViewVisiableListener;
import com.mojing.vrplayer.publicc.SettingSpBusiness;
import com.mojing.vrplayer.publicc.bean.ContentInfo;
import com.mojing.vrplayer.publicc.bean.LocalVideoBean;
import com.mojing.vrplayer.publicc.bean.VideoDetailBean;
import com.mojing.vrplayer.utils.BitmapUtil;
import com.mojing.vrplayer.utils.GLConst;
import com.mojing.vrplayer.utils.HeadControlUtil;

import java.util.List;

/**
 * Created by wanghongfang on 2016/7/15.
 * 播放功能设置
 */
public class MoviePlayerSettingView extends GLRelativeView {

    private int mType = 0;

    private String mResId = "";

    private Context mContext;

    private MovieSettingView movieSettingView;

    private HDTypeView hdTypeView;

    private SoundBarView soundBarView;

    private SelectSourceView selectSourceView;

    private SelectMovieView selectMovieView;

    private LocalModeView localModeView;

    private LocalSelectSourceView localSelectSourceView;

    public MoviePlayerSettingView(Context context) {
        super(context);
        mContext = context;
        setLayoutParams(1000,1000);
        setHandleFocus(false);
//        setBackground(new GLColor(0xff0000));
        initView();
    }

    public void initView() {
        //创建在线影院选集菜单
        createSelectSourceView();
        //创建在线选片菜单
        createSelectMovieView();
        //创建在线影院音量调节菜单
        createSoundBarView();
        //创建在线影院清晰度菜单
        createHDTypeView();
        //创建本地模式选择菜单
        createLocalModeView();
        //创建在线影院设置菜单
        createMovieSettingView();
        //创建本地选片菜单
        createLocalSelectSourceView();
      // setDepth(GLConst.Player_Settings_Depth,GLConst.Player_Settings_Scale);
    }

    public void nextPage() {
        if(mType == GLConst.MOVIE || mType == GLConst.PANO) {
            if(mSelectSourceType == GLConst.EPISODE_SOURCE) {
                if(selectSourceView.isVisible()) {
                    selectSourceView.nextPage();
                }
            } else if(mSelectSourceType == GLConst.MOVIE_SOURCE) {
                if(selectMovieView.isVisible()) {
                    selectMovieView.nextPage();
                }
            } else {
                return;
            }
        } else if(mType == GLConst.LOCAL_MOVIE || mType == GLConst.LOCAL_PANO) {
            if(mSelectSourceType == GLConst.MOVIE_SOURCE) {
                if(localSelectSourceView.isVisible()) {
                    localSelectSourceView.nextPage();
                }
            }
        }
    }

    public void prePage() {
        if(mType == GLConst.MOVIE || mType == GLConst.PANO) {
            if(mSelectSourceType == GLConst.EPISODE_SOURCE) {
                if(selectSourceView.isVisible()) {
                    selectSourceView.prePage();
                }
            } else if(mSelectSourceType == GLConst.MOVIE_SOURCE) {
                if(selectMovieView.isVisible()) {
                    selectMovieView.prePage();
                }
            } else {
                return;
            }
        } else if(mType == GLConst.LOCAL_MOVIE || mType == GLConst.LOCAL_PANO) {
            if(mSelectSourceType == GLConst.MOVIE_SOURCE) {
                if(localSelectSourceView.isVisible()) {
                    localSelectSourceView.prePage();
                }
            }
        }
    }

    private void createLocalSelectSourceView() {
        localSelectSourceView = new LocalSelectSourceView(mContext);
        localSelectSourceView.setVisible(false);
        localSelectSourceView.setMargin(-100,200,0,0);
        addViewBottom(localSelectSourceView);
    }

    public void setType(int type) {
        mType = type;
        localModeView.setType(mType);
        movieSettingView.setType(mType);
    }

    public void setResId(String res_id) {
        mResId = res_id;
    }

    private int mSelectSourceType = GLConst.MOVIE_SOURCE;

    private void createSelectMovieView() {
        selectMovieView = new SelectMovieView(mContext);
        selectMovieView.setVisible(false);
        addViewBottom(selectMovieView);
    }

    private void createLocalModeView() {
        localModeView = new LocalModeView(mContext);
        localModeView.setVisible(false);
        localModeView.setMargin(0,0,0,200);
        addViewBottom(localModeView);
    }

    /**
     * 本地设置影院比例
     * @param ratioName
     */
    public void setSelectedRatio(String ratioName) {
        localModeView.setSelectedRatio(ratioName);
    }
    /**
     * 本地设置影院旋转
     * @param roteName
     */
    public void setSelectedRote(String roteName) {
        localModeView.setSelectedRote(roteName);
    }
    /**
     * 本地设置场景2D，3D左右，3D上下
     * @param mode
     */
    public void setSelectedLeftRightMode(int mode) {
        localModeView.setSelectedLeftRightMode(mode);
    }
    /**
     * 本地设置场景平面，半球，球面
     * @param mode
     */
    public void setSelectedRoundMode(int mode) {
        localModeView.setSelectedRoundMode(mode);
    }

    private void createSelectSourceView() {
        selectSourceView = new SelectSourceView(mContext);
        selectSourceView.setVisible(false);
//        selectSourceView.setDepth(GLConst.Player_Settings_Depth,GLConst.Player_Settings_Scale);
        addViewBottom(selectSourceView);
    }
    private VideoDetailBean videosBean;
    public void setMovieVideoDatas(VideoDetailBean videosBean,int index) {
        this.videosBean = videosBean;
        selectSourceView.setMovieVideoDatas(videosBean,index);
        checkSelectedType();
    }
    private List<ContentInfo> contentInfos;
    public void setMovieVideoDatas(List<ContentInfo> contentInfos, int index) {
        this.contentInfos = contentInfos;
        selectMovieView.setCurrentNum(index);
        selectMovieView.setData(contentInfos);
        //在线选片不用回调
//        checkSelectedType();
    }
    private List<LocalVideoBean> videoList;
    public void setLocalMovieVideoDatas(List<LocalVideoBean> videoList, int index) {
        this.videoList = videoList;
        localSelectSourceView.setCurrentNum(index);
        localSelectSourceView.setData(videoList);
        checkSelectedType();
    }

    private void checkSelectedType() {
        if(mType == GLConst.MOVIE || mType == GLConst.PANO) {
            if(null != videosBean
                    && null != videosBean.getAlbums()
                    && null != videosBean.getAlbums().get(0)
                    && null != videosBean.getAlbums().get(0).getVideos()
                    && videosBean.getAlbums().get(0).getVideos().size() > 0) {
                mSelectSourceType = GLConst.EPISODE_SOURCE;
            } else {
                if(null != contentInfos && contentInfos.size() > 0) {
                    mSelectSourceType = GLConst.MOVIE_SOURCE;
                } else {
//                    mSelectSourceType = GLConst.NO_SOURCE;
                    //在线情况下，没有选集，肯定有选片
                    mSelectSourceType = GLConst.MOVIE_SOURCE;
                }
            }
        } else if(mType == GLConst.LOCAL_MOVIE || mType == GLConst.LOCAL_PANO) {
            if(null != videoList && videoList.size() > 0) {
                mSelectSourceType = GLConst.MOVIE_SOURCE;
            } else {
                mSelectSourceType = GLConst.NO_SOURCE;
            }
        }
        if(null != mCallBack) {
            mCallBack.onSetSelectSourceType(mSelectSourceType);
        }
    }

    public void setCurrentNum(int index) {
        if(mType == GLConst.MOVIE || mType == GLConst.PANO) {
            if(mSelectSourceType == GLConst.EPISODE_SOURCE) {
                selectSourceView.setCurrentNum(index);
            } else if(mSelectSourceType == GLConst.MOVIE_SOURCE) {
                selectMovieView.setCurrentNum(index);
            }
        } else if(mType == GLConst.LOCAL_MOVIE || mType == GLConst.LOCAL_PANO) {
            if(mSelectSourceType == GLConst.MOVIE_SOURCE) {
                localSelectSourceView.setCurrentNum(index);
            }
        }
    }

    public void setSelectSourceViewShow(boolean isShow) {
        if(mType == GLConst.MOVIE || mType == GLConst.PANO) {
            if(mSelectSourceType == GLConst.EPISODE_SOURCE) {
                if(isShow) {
                    selectSourceView.setVisible(true);
                } else {
                    selectSourceView.setVisible(false);
                }
            } else if(mSelectSourceType == GLConst.MOVIE_SOURCE) {
                if(isShow) {
                    selectMovieViewFlag = true;
                    getRecommendSource();
//                    selectMovieView.setVisible(true);
                } else {
                    selectMovieView.setVisible(false);
                }
            } else {
                return;
            }
        } else if(mType == GLConst.LOCAL_MOVIE || mType == GLConst.LOCAL_PANO) {
            if(mSelectSourceType == GLConst.MOVIE_SOURCE) {
                if(isShow) {
                    localSelectSourceView.notifyDataSetChanged();
                    localSelectSourceView.setVisible(true);
                } else {
                    localSelectSourceView.setVisible(false);
                }
            }
        }
        if(mVisiableCallBack!=null){
            mVisiableCallBack.onVisibility(isShow);
        }
    }

    private void createSoundBarView() {
        soundBarView = new SoundBarView(mContext);
        soundBarView.setVisible(false);
        soundBarView.setMargin(0,0,0,200);
        addViewBottom(soundBarView);
    }

    public void setSoundBarViewShow(boolean isShow) {
        if(isShow) {
            soundBarView.setVisible(true);
        } else {
            soundBarView.setVisible(false);
        }
        if(mVisiableCallBack!=null){
            mVisiableCallBack.onVisibility(isShow);
        }
    }

    public void setSoundIcon(boolean flag) {
        soundBarView.setSoundIcon(flag);
        if(flag){
            soundBarView.setVolume(SettingSpBusiness.getInstance(mContext).getPlayerSoundValue(mContext));
        }else {
            soundBarView.setVolume(0);
        }
    }

    public void setVolume(int vm) {
        soundBarView.setVolume(vm);
        soundBarView.setSoundIcon(vm>0);
        SettingSpBusiness.getInstance(mContext).setPlayerSoundValue(vm);
        SettingSpBusiness.getInstance(mContext).setPlayerSoundMute(!(vm>0));
    }

    private void createHDTypeView() {
        hdTypeView = new HDTypeView(mContext);
        hdTypeView.setVisible(false);
        addViewBottom(hdTypeView);
    }
    private String[] hdData;
    public void setHDdata(String[] strs,String defaultHD) {
        this.hdData = strs;
        hdTypeView.initView(strs);
        hdTypeView.setSelectedHD(defaultHD);
        checkHDdata();
    }

    private void checkHDdata() {
        if(null != mCallBack) {
            if(null != hdData && hdData.length != 0) {
                mCallBack.onHDable(true);
            } else {
                mCallBack.onHDable(false);
            }
        }
    }

    public void setSelectedHD(String hdtype){
        hdTypeView.setSelectedHD(hdtype);
    }

    public void setHDTypeViewShow(boolean isShow) {
        if(mType == GLConst.MOVIE || mType == GLConst.PANO) {
            if(isShow) {
                hdTypeView.setVisible(true);
            } else {
                hdTypeView.setVisible(false);
            }
        } else if(mType == GLConst.LOCAL_MOVIE || mType == GLConst.LOCAL_PANO) {
            if(isShow) {
                localModeView.setVisible(true);
            } else {
                localModeView.setVisible(false);
            }
        }
        if(mVisiableCallBack!=null){
            mVisiableCallBack.onVisibility(isShow);
        }
    }

    private void createMovieSettingView() {
        movieSettingView = new MovieSettingView(mContext);
        movieSettingView.setVisible(false);
       // movieSettingView.setPadding(0,0,0,200);
        movieSettingView.setMargin(0,0,0,200);
//        movieSettingView.setSettingViewInterface(new MovieSettingView.SettingViewInterface() {
//            @Override
//            public void closeAllView() {
//                hideAllView();
//            }
//        });
        addViewBottom(movieSettingView);
    }

//    public void setSkyboxViewHide(){
//        movieSettingView.setSkyboxViewHide();
//    }
//
//    public boolean isSkyboxViewShow(){
//        return movieSettingView.isSkyboxViewShow();
//    }

    public boolean isMovieSettingViewShow(){
        return movieSettingView.isVisible();
    }

    public void setMovieSettingViewHide(){
         movieSettingView.setVisible(false);
    }


    public void setMovieSettingViewShow(boolean isShow) {
        if(isShow) {
            movieSettingView.refreshView();
            movieSettingView.setVisible(true);
        } else {
            movieSettingView.setVisible(false);
        }
        if(mVisiableCallBack!=null){
            mVisiableCallBack.onVisibility(isShow);
        }
    }

    /**
     * 隐藏此view的所有子view
     */
    public void hideAllView() {
        //隐藏设置相关全部菜单
        movieSettingView.setVisible(false);
        hdTypeView.setVisible(false);
        localModeView.setVisible(false);
        soundBarView.setVisible(false);
        selectSourceView.setVisible(false);
        selectMovieView.setVisible(false);
        localSelectSourceView.setVisible(false);
        if(mVisiableCallBack!=null){
            mVisiableCallBack.onVisibility(false);
        }
        selectMovieViewFlag = false;
        if(null != mCallBack) {
            mCallBack.onMovieStatus(0);
        }
    }

    private IPlayerSettingCallBack mCallBack;
    private IViewVisiableListener mVisiableCallBack;

    public void setIPlayerSettingCallBack(IPlayerSettingCallBack callBack) {

        this.mCallBack = callBack;
        movieSettingView.setIPlayerSettingCallBack(callBack);
        hdTypeView.setIPlayerSettingCallBack(callBack);
        localModeView.setIPlayerSettingCallBack(callBack);
        soundBarView.setIPlayerSettingCallBack(callBack);
        selectSourceView.setIPlayerSettingCallBack(callBack);
        selectMovieView.setIPlayerSettingCallBack(callBack);
        localSelectSourceView.setIPlayerSettingCallBack(callBack);
    }
    public void setOnViewVisiableListener(IViewVisiableListener listener){
        mVisiableCallBack = listener;
    }
    public void setAudioStreamList(List<AudioSteamInfo.AudioStremItem> audioStreamList) {
        movieSettingView.setAudioStreamList(audioStreamList);
    }
    public void setSubtitleList(List<String> subtitleList){
        movieSettingView.setSubtitleList(subtitleList);
    }
    public void setSelectedAudioStream(int index) {
        movieSettingView.setSelectedAudioStream(index);
    }
    public void setSelectedDecodeType(int type) {
        movieSettingView.setSelectedDecodeType(type);
    }
    public void setSelectedScreenSize(int type) {
        movieSettingView.setSelectedScreenSize(type);
    }
    public void setSelectedScreenLight(int light) {
        movieSettingView.setSelectedScreenLight(light);
    }
    public void setSelectedSubtitle(String name) {
        movieSettingView.setSelectedSubtitle(name);
    }

    public void setSelectedSubtitleFondSize(int size){movieSettingView.setSelectedSubtitleFontSize(size);}

    private boolean selectMovieViewFlag = true;

    private void getRecommendSource() {
        if(TextUtils.isEmpty(mResId)) return;
//        ((MjVrPlayerActivity)mContext).runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                PlayRecommendApiUtil.getInstance(mContext).getRecommendSource(PlayRecommendApiUtil.WAVECONTRL, mResId, new IPlayRecommendCallBack() {
//                    @Override
//                    public void onResultData(int status, List<ContentInfo> data) {
//                        if(!selectMovieViewFlag) return;
//                        if(status == PlayRecommendApiUtil.SUCCESS) {
//                            if(null != data && data.size() > 0) {
//                                //添加数据
//                                setMovieVideoDatas(data,-1);
//                                //防止显示偏移
//                                if (mType == GLConst.PANO || mType == GLConst.LOCAL_PANO){
//                                    selectMovieView.setLookAngle(MoviePlayerSettingView.this.getParent().getLookAngle());
//                                }
//                                //显示数据
//                                selectMovieView.setVisible(true);
//                            }
//                        } else if(status == PlayRecommendApiUtil.FAILURE){
//                            selectMovieView.setVisible(false);
//                        }
//
//                        if(null != mCallBack) {
//                            mCallBack.onMovieStatus(status);
//                        }
//                    }
//                });
//            }
//        });
    }

    public  MovieSettingSubView.SubViewCallback getSubViewCallback(){
        return movieSettingView.getSubViewCallback();
    }


    interface ContrlViewInterface{
        public void showContrlView();
    }

    public void setContrlViewInterface(ContrlViewInterface contrlViewInterface){
        if(localSelectSourceView!=null){
            localSelectSourceView.setContrlViewInterface(contrlViewInterface);
        }
        if(movieSettingView!=null){
            movieSettingView.setContrlViewInterface(contrlViewInterface);
        }
    }
}
