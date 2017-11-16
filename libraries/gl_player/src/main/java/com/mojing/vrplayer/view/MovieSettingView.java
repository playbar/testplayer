package com.mojing.vrplayer.view;

import android.content.Context;
import android.graphics.Bitmap;

import com.bfmj.viewcore.interfaces.GLOnKeyListener;
import com.bfmj.viewcore.interfaces.GLViewFocusListener;
import com.bfmj.viewcore.render.GLColor;
import com.bfmj.viewcore.view.GLImageView;
import com.bfmj.viewcore.view.GLRectView;
import com.bfmj.viewcore.view.GLRelativeView;
import com.bfmj.viewcore.view.GLTextView;
import com.mojing.vrplayer.R;
import com.mojing.vrplayer.bean.AudioSteamInfo;
import com.mojing.vrplayer.interfaces.IPlayerSettingCallBack;
import com.mojing.vrplayer.publicc.SettingSpBusiness;
import com.mojing.vrplayer.utils.BitmapUtil;
import com.mojing.vrplayer.utils.GLConst;
import com.mojing.vrplayer.utils.HeadControlUtil;
import com.storm.smart.common.utils.LogHelper;

import java.util.List;

/**
 * Created by yushaochen on 2017/4/7.
 */

public class MovieSettingView extends GLRelativeView{

    public final static String TAG="MovieSettingView";
    private Context mContext;

    private GLRelativeView glLinearView2;
    private LargerSkyboxView skyboxView;

    private IPlayerSettingCallBack mCallBack;

    MovieSettingSubView localMovieSettingSubView=null;
    MovieSettingSubView localPanoSettingSubView=null;
    MovieSettingSubView movieSettingSubView=null;
    MovieSettingSubView panoSettingSubView=null;
    private int mType=0;
    private SettingBottomRightView settingBottomRightView;

    public MovieSettingView(Context context) {
        super(context);
        mContext = context;
       // setOrientation(GLConstant.GLOrientation.VERTICAL);
        setLayoutParams(1000,600);
        initView();
    }

    public void initView() {
        //创建场景选择button
        localMovieSettingSubView=new MovieSettingSubView(mContext, GLConst.LOCAL_MOVIE);
        addViewBottom(localMovieSettingSubView);
        localMovieSettingSubView.setSubViewCallback(mSubViewCallback);
        localPanoSettingSubView=new MovieSettingSubView(mContext, GLConst.LOCAL_PANO);
        addViewBottom(localPanoSettingSubView);
        localPanoSettingSubView.setSubViewCallback(mSubViewCallback);
        movieSettingSubView=new MovieSettingSubView(mContext, GLConst.MOVIE);
        movieSettingSubView.setSubViewCallback(mSubViewCallback);
        addViewBottom(movieSettingSubView);
        panoSettingSubView=new MovieSettingSubView(mContext, GLConst.PANO);
        addViewBottom(panoSettingSubView);
        panoSettingSubView.setSubViewCallback(mSubViewCallback);
        //创建场景选择view
        glLinearView2 = new GLRelativeView(mContext);
        glLinearView2.setLayoutParams(1000,560);
        Bitmap bitmap2 = BitmapUtil.getBitmap(1250, 560, 20f,"#272729");
        glLinearView2.setPadding(55f,30f,105f,0f);
        glLinearView2.setMargin(-120,0,0,0);
        skyboxView = new LargerSkyboxView(mContext);
        skyboxView.setPadding(60,0,0,0);
        skyboxView.setMargin(0,200,0,0);
        skyboxView.setSelected(SettingSpBusiness.getInstance(mContext).getSkyboxIndex()+"");

        GLRelativeView boxContainer = new GLRelativeView(mContext);
        boxContainer.setLayoutParams(1250,560);
        boxContainer.setMargin(0,0,0,150);
        boxContainer.setBackground(bitmap2);
        glLinearView2.addView(boxContainer);

        boxContainer.addView(skyboxView);
        glLinearView2.setVisible(false);
        glLinearView2.setFocusListener(showSettingfocusListener);
        //进入和返回的button
        GLTextView sceneTextView = new GLTextView(mContext);
        sceneTextView.setLayoutParams(240f,56f);
        sceneTextView.setTextSize(54);
        sceneTextView.setTextColor(new GLColor(0x888888));
        sceneTextView.setAlignment(GLTextView.ALIGN_CENTER);
        sceneTextView.setText("场景选择");
        sceneTextView.setMargin(540f,70f,0f,70f);
        boxContainer.addView(sceneTextView);

        settingBottomRightView = new SettingBottomRightView(mContext);
        settingBottomRightView.setBackName("返回");
        settingBottomRightView.setOnKeyListener(new GLOnKeyListener() {
            @Override
            public boolean onKeyDown(GLRectView view, int keycode) {
                //setSubVisible(true);
                glLinearView2.setVisible(false);
//                if(mSettingViewInterface!=null){
//                    mSettingViewInterface.closeAllView();
//                }
                return false;
            }
            @Override
            public boolean onKeyUp(GLRectView view, int keycode) {
                return false;
            }
            @Override
            public boolean onKeyLongPress(GLRectView view, int keycode) {
                return false;
            }
        });
        settingBottomRightView.setType(SettingBottomRightView.TYPE_SKYBOX);
       // HeadControlUtil.bindView(settingBottomRightView);
      //  glLinearView2.addViewBottom(settingBottomRightView);

        final GLImageView mCloseImage = new GLImageView(mContext);
        mCloseImage.setLayoutParams(80,80);
        mCloseImage.setImage("play_menu_button_close_normal");
        mCloseImage.setMargin(610,630,0,0);
        HeadControlUtil.bindView(mCloseImage);
        mCloseImage.setOnKeyListener(new GLOnKeyListener() {
            @Override
            public boolean onKeyDown(GLRectView view, int keycode) {
                if(mContrlViewInterface!=null){
                    mContrlViewInterface.showContrlView();
                }
                return false;
            }

            @Override
            public boolean onKeyUp(GLRectView view, int keycode) {
                return false;
            }

            @Override
            public boolean onKeyLongPress(GLRectView view, int keycode) {
                return false;
            }
        });
        mCloseImage.setFocusListener(new GLViewFocusListener() {
            @Override
            public void onFocusChange(GLRectView view, boolean focused) {
                if(focused) {
                    mCloseImage.setImage("play_menu_button_close_hover");
                } else {
                    mCloseImage.setImage("play_menu_button_close_normal");
                }
            }
        });

        glLinearView2.addView(mCloseImage);
      //  glLinearView2.setFocusListener(focusListener);
        //addViewBottom(glLinearView1);
        addViewBottom(glLinearView2);
    }

    MoviePlayerSettingView.ContrlViewInterface mContrlViewInterface;
    public void setContrlViewInterface(MoviePlayerSettingView.ContrlViewInterface contrlViewInterface){
        mContrlViewInterface=contrlViewInterface;
    }


    private GLViewFocusListener showSettingfocusListener = new GLViewFocusListener() {
        @Override
        public void onFocusChange(GLRectView view, boolean focused) {
            if(focused) {
//                ((GLBaseActivity)getContext()).showCursorView();
                if(null != mCallBack) {
                    mCallBack.onHideControlAndSettingView(false);
                }
            } else {
//                ((GLBaseActivity)getContext()).hideCursorView2();
                if(null != mCallBack) {
                    mCallBack.onHideControlAndSettingView(true);
                }
            }
        }
    };

    public void refreshView() {
        setSubVisible(true);
        glLinearView2.setVisible(false);
    }

    public boolean isSkyboxViewShow(){
        return glLinearView2.isVisible();
    }
    public void setSkyboxViewHide(){
        setSubVisible(true);
        glLinearView2.setVisible(false);
    }

    public MovieSettingSubView.SubViewCallback getSubViewCallback(){
        return mSubViewCallback;
    }

    MovieSettingSubView.SubViewCallback mSubViewCallback=new MovieSettingSubView.SubViewCallback(){
        @Override
        public void showScene() {
            MovieSettingView.this.setVisible(true);
            localMovieSettingSubView.setVisible(false);
            localPanoSettingSubView.setVisible(false);
            movieSettingSubView.setVisible(false);
            panoSettingSubView.setVisible(false);
            glLinearView2.setVisible(true);
        }
    };

    public void setIPlayerSettingCallBack(IPlayerSettingCallBack callBack) {
        this.mCallBack = callBack;
        localMovieSettingSubView.setIPlayerSettingCallBack(mCallBack);
        localPanoSettingSubView.setIPlayerSettingCallBack(mCallBack);
        movieSettingSubView.setIPlayerSettingCallBack(mCallBack);
        panoSettingSubView.setIPlayerSettingCallBack(mCallBack);
    }

    public void setAudioStreamList(List<AudioSteamInfo.AudioStremItem> audioStreamList) {
        if(mType==GLConst.LOCAL_PANO){
            LogHelper.d(TAG,"setAudioStreamList LOCAL_PANO");
            localPanoSettingSubView.setAudioStreamList(audioStreamList);
        }else if(mType==GLConst.LOCAL_MOVIE){
            LogHelper.d(TAG,"setAudioStreamList LOCAL_MOVIE");
            localMovieSettingSubView.setAudioStreamList(audioStreamList);
        }
    }

    public void setSubtitleList(List<String> subtitleList){
        LogHelper.d(TAG,"setSubtitleList");
        if(mType==GLConst.LOCAL_PANO){
            LogHelper.d(TAG,"setSubtitleList LOCAL_PANO");
            localPanoSettingSubView.setSubtitleList(subtitleList);
        }else if(mType==GLConst.LOCAL_MOVIE){
            LogHelper.d(TAG,"setSubtitleList LOCAL_MOVIE");
            localMovieSettingSubView.setSubtitleList(subtitleList);
            LogHelper.d(TAG,"setSubtitleList LOCAL_MOVIE end");
        }
    }
    public void setSelectedAudioStream(int index) {
        if(mType==GLConst.LOCAL_PANO){
            localPanoSettingSubView.setSelectedAudioStream(index);
        }else if(mType==GLConst.LOCAL_MOVIE){
            localMovieSettingSubView.setSelectedAudioStream(index);
        }
    }
    public void setSelectedDecodeType(int type) {
        if(mType==GLConst.LOCAL_PANO){
            localPanoSettingSubView.setSelectedDecodeType(type);
        }else if(mType==GLConst.LOCAL_MOVIE){
            localMovieSettingSubView.setSelectedDecodeType(type);
        }
    }
    public void setSelectedScreenSize(int type) {
        if(mType==GLConst.MOVIE){
            movieSettingSubView.setSelectedScreenSize(type);
        }else if(mType==GLConst.LOCAL_MOVIE){
            localMovieSettingSubView.setSelectedScreenSize(type);
        }
    }

    public void setSelectedScreenLight(int light) {
        if(mType==GLConst.LOCAL_PANO){
            localPanoSettingSubView.setSelectedScreenLight(light);
        }else if(mType==GLConst.LOCAL_MOVIE){
            localMovieSettingSubView.setSelectedScreenLight(light);
        }else if(mType==GLConst.MOVIE) {
            movieSettingSubView.setSelectedScreenLight(light);
        }else if(mType==GLConst.PANO) {
            panoSettingSubView.setSelectedScreenLight(light);
        }
    }

    public void setSelectedSubtitle(String name) {
        if(mType==GLConst.LOCAL_PANO){
            localPanoSettingSubView.setSelectedSubtitle(name);
        }else if(mType==GLConst.LOCAL_MOVIE){
            localMovieSettingSubView.setSelectedSubtitle(name);
        }
    }

    public void setSelectedSubtitleFontSize(int size){
        if(mType==GLConst.LOCAL_PANO){
            localPanoSettingSubView.setSelectedSubtitleFontSize(size);
        }else if(mType==GLConst.LOCAL_MOVIE){
            localMovieSettingSubView.setSelectedSubtitleFontSize(size);
        }
    }

    public void setType(int type){
        mType=type;
        if(isVisible()){
            setSubVisible(true);
        }
    }

    public void setSubVisible(boolean visble){
        localMovieSettingSubView.setVisible(false);
        localPanoSettingSubView.setVisible(false);
        movieSettingSubView.setVisible(false);
        panoSettingSubView.setVisible(false);
        if(visble){
            if(mType==GLConst.MOVIE){
                LogHelper.d(TAG,"current type is MOVIE");
                movieSettingSubView.setVisible(true);
            }else if(mType==GLConst.LOCAL_MOVIE){
                LogHelper.d(TAG,"current type is LOCAL_MOVIE");
                localMovieSettingSubView.setVisible(true);
            }else if(mType==GLConst.PANO){
                LogHelper.d(TAG,"current type is PANO");
                panoSettingSubView.setVisible(true);
            }else if(mType==GLConst.LOCAL_PANO){
                LogHelper.d(TAG,"current type is LOCAL_PANO");
                localPanoSettingSubView.setVisible(true);
            }
        }
    }
}
