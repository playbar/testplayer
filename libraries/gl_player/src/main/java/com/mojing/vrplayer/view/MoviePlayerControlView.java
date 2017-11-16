package com.mojing.vrplayer.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.widget.LinearLayout;

import com.baofeng.mojing.input.base.MojingKeyCode;
import com.bfmj.sdk.util.TimeFormat;
import com.bfmj.viewcore.interfaces.GLOnKeyListener;
import com.bfmj.viewcore.interfaces.GLViewFocusListener;
import com.bfmj.viewcore.render.GLColor;
import com.bfmj.viewcore.render.GLConstant;
import com.bfmj.viewcore.view.GLImageView;
import com.bfmj.viewcore.view.GLLinearView;
import com.bfmj.viewcore.view.GLRectView;
import com.bfmj.viewcore.view.GLRelativeView;
import com.bfmj.viewcore.view.GLTextView;
import com.mojing.vrplayer.R;
import com.mojing.vrplayer.activity.GLBaseActivity;
import com.mojing.vrplayer.activity.MjVrPlayerActivity;
import com.mojing.vrplayer.interfaces.IPlayerControlCallBack;
import com.mojing.vrplayer.interfaces.IPlayerSettingCallBack;
import com.mojing.vrplayer.interfaces.IViewVisiableListener;
import com.mojing.vrplayer.utils.BitmapUtil;
import com.mojing.vrplayer.utils.GLConst;
import com.mojing.vrplayer.utils.HeadControlUtil;
import com.mojing.vrplayer.utils.MJGLUtils;

/**
 * yushaochen 2017/4/6
 * 播放控制层
 */
public class MoviePlayerControlView extends GLRelativeView{

    private int mType = 0;
    public static final String SELECTED_RESET = "selected_reset";
    public static final String SELECTED_SOURCE = "selected_source";
    public static final String SOUND = "sound";
    public static final String HD_MODE = "hd_mode";
    public static final String SETTING = "setting";

    private Context mContext;
    private GLRelativeView mRootView;

    private float mWidth = 1000f;
    private float mHeigth = 450f;

    private GLSeekBarView seekBarView;

    private GLLinearView mBottomView;//
    private GLTextView mCurrentTime;//当前播放时间
    private GLTextView mTotalTime;//当前影片总时长
    private GLTextView mName;//影片名称

    private PlayControlButton mSelectedSourceBtn;//选集按钮
    private PlayControlButton mSoundBtn;//音量按钮
    private PlayControlButton mHdOrModeBtn;//清晰度或者模式按钮
    private PlayControlButton mSettingBtn;//设置按钮

    private GLImageView mPlayOrPauseBtn;//播放或者暂停按钮

    private boolean playFlag = false;//当前按钮显示暂停

    public MoviePlayerControlView(Context context,int type) {
        super(context);
        mType = type;
        mContext = context;
        setLayoutParams(mWidth,mHeigth);
        mRootView = new GLRelativeView(mContext);
        mRootView.setLayoutParams(mWidth,mHeigth-200);
        mRootView.setMargin(0,200,0,0);
        Bitmap bitmap = BitmapUtil.getBitmap((int) mWidth, (int) mHeigth, 20f, "#19191a");
        mRootView.setBackground(bitmap);
//        setBackground(new GLColor(0x00ff00));
        //创建进度条
        createTopButtonView();
        //创建底部显示
        createBottomView();
        //创建顶部控制按钮
        createTopView();
        createMenuButtonView();
        addView(mRootView);
        mRootView.setFocusListener(focusListener);
        mRootView.setVisible(false);
    }

    private GLViewFocusListener focusListener = new GLViewFocusListener() {
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

    GLLinearView mTopViewContainer;
    PlayControlButton mLockScreenBtn;
    PlayControlButton mResetBtn;

    public void setLockScreenBtnKeyListener(GLOnKeyListener glOnKeyListener){
        if(mLockScreenBtn!=null){
            mLockScreenBtn.setImageKeyListener(glOnKeyListener);
        }
    }
    MovieSettingSubView.SubViewCallback mSubViewCallback;
    public void setSubViewCallback(MovieSettingSubView.SubViewCallback subViewCallback){
         mSubViewCallback=subViewCallback;
    }

    private void createTopButtonView() {
        mTopViewContainer = new GLLinearView(mContext);
        mTopViewContainer.setOrientation(GLConstant.GLOrientation.HORIZONTAL);
        mTopViewContainer.setLayoutParams(1000f,200f);
        mTopViewContainer.setMargin(0,0,0,0);
        addView(mTopViewContainer);
        mTopViewContainer.setVisible(false);
        mTopViewContainer.setFocusListener(focusListener);
        //选片
        mSelectedSourceBtn = new PlayControlButton(mContext);
        mSelectedSourceBtn.setLayoutParams(100,100+50);
        mSelectedSourceBtn.setImageLayout(100,100);
        mSelectedSourceBtn.getText().setMargin(20,110,0,0);
        mSelectedSourceBtn.setId(SELECTED_SOURCE);
        mSelectedSourceBtn.setMargin(100f+80f,0f,0f,0f);

        mSelectedSourceBtn.setSelected(false);
        mSelectedSourceBtn.setText("列表");

        mSelectedSourceBtn.setImageHeadControl(true);
        mSelectedSourceBtn.setImageBg("play_menu_button_file_normal");

        mSelectedSourceBtn.setImageFocusListener(new GLViewFocusListener() {
            @Override
            public void onFocusChange(GLRectView view, boolean focused) {
                if(mSelectSourceType == GLConst.NO_SOURCE) return;
                if(!mSelectedSourceBtn.isSelected()) {
                    if(focused) {
                        mSelectedSourceBtn.setImageBg("play_menu_button_file_hover");
                      //  refreshButtonImage(SELECTED_SOURCE,BTN_FOCUS);
                        mSelectedSourceBtn.setTextVisible(true);
                    } else {
                        mSelectedSourceBtn.setImageBg("play_menu_button_file_normal");
                      //  refreshButtonImage(SELECTED_SOURCE,BTN_NO_FOCUS);
                        mSelectedSourceBtn.setTextVisible(false);
                    }
                } else {
                    if(focused) {
                        mSelectedSourceBtn.setTextVisible(true);
                    } else {
                        mSelectedSourceBtn.setTextVisible(false);
                    }
                }
            }
        });
        mSelectedSourceBtn.setImageKeyListener(new GLOnKeyListener() {
            @Override
            public boolean onKeyDown(GLRectView view, int keycode) {
                switch (keycode) {
                    case MojingKeyCode.KEYCODE_ENTER:
//                        hideAllView();
//                        mMoviePlayerSettingView.setMovieSettingViewShow(true);
//                        if(mSubViewCallback!=null){
//                            mSubViewCallback.showScene();
//                        }
                        hideAllView();
                        if(mSelectSourceType == GLConst.NO_SOURCE) return false;
                        resetOtherButton(SELECTED_SOURCE);
                        if(!mSelectedSourceBtn.isSelected()) {
                            refreshButtonImage(SELECTED_SOURCE,BTN_CLICK);
                            mSelectedSourceBtn.setSelected(true);
                        } else {
                            refreshButtonImage(SELECTED_SOURCE,BTN_FOCUS);
                            mSelectedSourceBtn.setSelected(false);
                        }
                        mSelectedSourceBtn.setSelected(false);
                        if(null != mCallBack) {
                            mCallBack.onControlChanged(mSelectedSourceBtn.getId(),true);
                        }
                        break;
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
        mTopViewContainer.addView(mSelectedSourceBtn);

        //视角
        mResetBtn = new PlayControlButton(mContext);
        mResetBtn.setId(SELECTED_RESET);
        mResetBtn.setLayoutParams(100,100+50);
        mResetBtn.setImageLayout(100,100);
        mResetBtn.getText().setMargin(20,110,0,0);
        mResetBtn.setMargin(80f,0f,0f,0f);
        mResetBtn.setSelected(false);
        mResetBtn.setText("视角");
        if(mType == GLConst.LOCAL_MOVIE ){
            mResetBtn.setImageHeadControl(true);
            mResetBtn.setImageBg("play_menu_button_view_normal_1");
        }else{
            mResetBtn.setImageBg("play_menu_button_view_disable");
        }

        mResetBtn.setImageFocusListener(new GLViewFocusListener() {
            @Override
            public void onFocusChange(GLRectView view, boolean focused) {
                    if(mType != GLConst.LOCAL_MOVIE ) return;
                    if(focused) {
                        mResetBtn.setTextVisible(true);
                        mResetBtn.setImageBg("play_menu_button_view_hover_1");
                    } else {
                        mResetBtn.setTextVisible(false);
                        mResetBtn.setImageBg("play_menu_button_view_normal_1");
                    }
            }
        });
        mResetBtn.setImageKeyListener(new GLOnKeyListener() {
            @Override
            public boolean onKeyDown(GLRectView view, int keycode) {
                switch (keycode) {
                    case MojingKeyCode.KEYCODE_ENTER:
                        if(mType != GLConst.LOCAL_MOVIE ) return false;
                        if(mType == GLConst.LOCAL_MOVIE &&((MjVrPlayerActivity)mContext).isLockScreen)  return false;
                        if(null != mCallBack) {
                            mCallBack.onControlChanged(mResetBtn.getId(),mSelectedSourceBtn.isSelected());
                        }
                        break;
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
        mTopViewContainer.addView(mResetBtn);

        //锁屏
        mLockScreenBtn = new PlayControlButton(mContext);
        mLockScreenBtn.setLayoutParams(100,100+50);
        mLockScreenBtn.setImageLayout(100,100);
        mLockScreenBtn.getText().setMargin(20,110,0,0);
        mLockScreenBtn.setId(SELECTED_SOURCE);
        mLockScreenBtn.setMargin(80f,0f,0f,0f);
        setLockScreenBtn(false);
        mLockScreenBtn.setSelected(false);
        if(mType == GLConst.LOCAL_MOVIE ){
            mLockScreenBtn.setImageHeadControl(true);
        }else{
        }

        mLockScreenBtn.setImageFocusListener(new GLViewFocusListener() {
            @Override
            public void onFocusChange(GLRectView view, boolean focused) {
                if(focused){
                    mLockScreenBtn.setTextVisible(true);
                }else{
                    mLockScreenBtn.setTextVisible(false);
                }
                    setLockScreenBtn(focused);
            }
        });
        mTopViewContainer.addView(mLockScreenBtn);

        //场景选择
        final PlayControlButton mSelectedSourceBtn2 = new PlayControlButton(mContext);
        mSelectedSourceBtn2.setLayoutParams(100,100+50);
        mSelectedSourceBtn2.setImageLayout(100,100);
        mSelectedSourceBtn2.setId(SELECTED_SOURCE);
        mSelectedSourceBtn2.getText().setMargin(20,110,0,0);
        mSelectedSourceBtn2.setMargin(80f,0f,0f,0f);
        mSelectedSourceBtn2.setSelected(false);
        mSelectedSourceBtn2.setText("场景");
        if(mType == GLConst.LOCAL_MOVIE ){
            mSelectedSourceBtn2.setImageHeadControl(true);
            mSelectedSourceBtn2.setImageBg("play_menu_button_scene_normal");
        }else{
            mSelectedSourceBtn2.setImageBg("play_menu_button_scene_disable");
        }
        mSelectedSourceBtn2.setImageFocusListener(new GLViewFocusListener() {
            @Override
            public void onFocusChange(GLRectView view, boolean focused) {
                if(mType != GLConst.LOCAL_MOVIE ) return;
                if(focused) {
                    mSelectedSourceBtn2.setTextVisible(true);
                    mSelectedSourceBtn2.setImageBg("play_menu_button_scene_hover");
                } else {
                    mSelectedSourceBtn2.setTextVisible(false);
                    mSelectedSourceBtn2.setImageBg("play_menu_button_scene_normal");
                }
            }
        });
        mSelectedSourceBtn2.setImageKeyListener(new GLOnKeyListener() {
            @Override
            public boolean onKeyDown(GLRectView view, int keycode) {
                switch (keycode) {
                    case MojingKeyCode.KEYCODE_ENTER:
                        if(mType != GLConst.LOCAL_MOVIE ) return false;
                        //MoviePlayerControlView.this.hideAllView();
                        hideAllView();
                        mMoviePlayerSettingView.setMovieSettingViewShow(true);
                        if(mSubViewCallback!=null){
                            mSubViewCallback.showScene();
                        }
                        break;
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
        mTopViewContainer.addView(mSelectedSourceBtn2);
    }
    MoviePlayerSettingView mMoviePlayerSettingView;
    public void setMoviePlayerSettingView(MoviePlayerSettingView moviePlayerSettingView){
        mMoviePlayerSettingView=moviePlayerSettingView;
    }


    public void setLockScreenBtn(boolean focus){
        if(mType != GLConst.LOCAL_MOVIE ){
            mLockScreenBtn.setText("锁屏");
            mLockScreenBtn.setImageBg("play_menu_button_lock_disable");
            if(closeControlButton!=null){
                closeControlButton.setVisible(false);
                closeControlOneButton.setVisible(true);
                hideControlButton.setVisible(false);
            }
            return ;
        }
        if(((MjVrPlayerActivity)mContext).isLockScreen){
            if(focus){
                mLockScreenBtn.setImageBg("play_menu_button_unlock_hover");
            }else{
                mLockScreenBtn.setImageBg("play_menu_button_unlock_normal");
            }
            mLockScreenBtn.setText("解锁");
            if(closeControlButton!=null){
                closeControlButton.setVisible(true);
                closeControlOneButton.setVisible(false);
                hideControlButton.setVisible(true);
            }
        }else{
            if(focus){
                mLockScreenBtn.setImageBg("play_menu_button_lock_hover");
            }else{
                mLockScreenBtn.setImageBg("play_menu_button_lock_normal");
            }
            mLockScreenBtn.setText("锁屏");
            if(closeControlButton!=null) {
                closeControlButton.setVisible(false);
                closeControlOneButton.setVisible(true);
                hideControlButton.setVisible(false);
            }
        }
    }

    GLImageView mPlayPreBtn;
    GLImageView mPlayNextBtn;
    private void createTopView() {
        float centerHeight=80f;

        mSoundBtn = new PlayControlButton(mContext);
        mSoundBtn.setId(SOUND);
        mSoundBtn.setMargin(40f,centerHeight,0f,0f);//40f+60f+230f
        refreshButtonImage(SOUND,BTN_NO_FOCUS);
        mSoundBtn.setText("音量");
        mSoundBtn.setSelected(false);
        mSoundBtn.setImageHeadControl(true);
        mSoundBtn.setImageFocusListener(new GLViewFocusListener() {
            @Override
            public void onFocusChange(GLRectView view, boolean focused) {
                if(!mSoundBtn.isSelected()) {
                    if(focused) {
                        refreshButtonImage(SOUND,BTN_FOCUS);
                        mSoundBtn.setTextVisible(true);
                    } else {
                        refreshButtonImage(SOUND,BTN_NO_FOCUS);
                        mSoundBtn.setTextVisible(false);
                    }
                } else {
                    if(focused) {
                        mSoundBtn.setTextVisible(true);
                    } else {
                        mSoundBtn.setTextVisible(false);
                    }
                }
            }
        });
        mSoundBtn.setImageKeyListener(new GLOnKeyListener() {
            @Override
            public boolean onKeyDown(GLRectView view, int keycode) {
                switch (keycode) {
                    case MojingKeyCode.KEYCODE_ENTER:
                        resetOtherButton(SOUND);
                        if(!mSoundBtn.isSelected()) {
                            refreshButtonImage(SOUND,BTN_CLICK);
                            mSoundBtn.setSelected(true);
                        } else {
                            refreshButtonImage(SOUND,BTN_FOCUS);
                            mSoundBtn.setSelected(false);
                        }
                        if(null != mCallBack) {
                            mCallBack.onControlChanged(mSoundBtn.getId(),mSoundBtn.isSelected());
                        }
                        break;
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
        mRootView.addView(mSoundBtn);

        mHdOrModeBtn = new PlayControlButton(mContext);
        mHdOrModeBtn.setId(HD_MODE);//40f+3*60f+2*230f+2*70f+80f
        mHdOrModeBtn.setMargin(40f+90f+2*230f+2*70f+80f,centerHeight,0f,0f);
        mHdOrModeBtn.setSelected(false);
        mHdOrModeBtn.setImageFocusListener(new GLViewFocusListener() {
            @Override
            public void onFocusChange(GLRectView view, boolean focused) {
                if(mType == GLConst.MOVIE || mType == GLConst.PANO) {
                    if(!hdable) return;
                }
                if(!mHdOrModeBtn.isSelected()) {
                    if(focused) {
                        refreshButtonImage(HD_MODE,BTN_FOCUS);
                        mHdOrModeBtn.setTextVisible(true);
                    } else {
                        refreshButtonImage(HD_MODE,BTN_NO_FOCUS);
                        mHdOrModeBtn.setTextVisible(false);
                    }
                } else {
                    if(focused) {
                        mHdOrModeBtn.setTextVisible(true);
                    } else {
                        mHdOrModeBtn.setTextVisible(false);
                    }
                }
            }
        });
        mHdOrModeBtn.setImageKeyListener(new GLOnKeyListener() {
            @Override
            public boolean onKeyDown(GLRectView view, int keycode) {
                switch (keycode) {
                    case MojingKeyCode.KEYCODE_ENTER:
                        if(mType == GLConst.MOVIE || mType == GLConst.PANO) {
                            if(!hdable) return false;
                        }
                        resetOtherButton(HD_MODE);
                        if(!mHdOrModeBtn.isSelected()) {
                            refreshButtonImage(HD_MODE,BTN_CLICK);
                            mHdOrModeBtn.setSelected(true);
                        } else {
                            refreshButtonImage(HD_MODE,BTN_FOCUS);
                            mHdOrModeBtn.setSelected(false);
                        }
                        if(null != mCallBack) {
                            mCallBack.onControlChanged(mHdOrModeBtn.getId(),mHdOrModeBtn.isSelected());
                        }
                        break;
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
        mRootView.addView(mHdOrModeBtn);

        mSettingBtn = new PlayControlButton(mContext);
        mSettingBtn.setId(SETTING);
        mSettingBtn.setMargin(40f+3*60f+2*230f+2*70f+80f,centerHeight,0f,0f);
        refreshButtonImage(SETTING,BTN_NO_FOCUS);
        mSettingBtn.setText("设置");
        mSettingBtn.setSelected(false);
        mSettingBtn.setImageHeadControl(true);
        mSettingBtn.setImageFocusListener(new GLViewFocusListener() {
            @Override
            public void onFocusChange(GLRectView view, boolean focused) {

                if(!mSettingBtn.isSelected()) {
                    if(focused) {
                        refreshButtonImage(SETTING,BTN_FOCUS);
                        mSettingBtn.setTextVisible(true);
                    } else {
                        refreshButtonImage(SETTING,BTN_NO_FOCUS);
                        mSettingBtn.setTextVisible(false);
                    }
                } else {
                    if(focused) {
                        mSettingBtn.setTextVisible(true);
                    } else {
                        mSettingBtn.setTextVisible(false);
                    }
                }
            }
        });
        mSettingBtn.setImageKeyListener(new GLOnKeyListener() {
            @Override
            public boolean onKeyDown(GLRectView view, int keycode) {
                switch (keycode) {
                    case MojingKeyCode.KEYCODE_ENTER:
                        resetOtherButton(SETTING);
                        if(!mSettingBtn.isSelected()) {
                            refreshButtonImage(SETTING,BTN_CLICK);
                            mSettingBtn.setSelected(true);
                        } else {
                            refreshButtonImage(SETTING,BTN_FOCUS);
                            mSettingBtn.setSelected(false);
                        }
                        if(null != mCallBack) {
                            mCallBack.onControlChanged(mSettingBtn.getId(),mSettingBtn.isSelected());
                        }
                        break;
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
        mRootView.addView(mSettingBtn);


        //上一曲
        mPlayPreBtn = new GLImageView(mContext);
        mPlayPreBtn.setLayoutParams(80f,80f);
        mPlayPreBtn.setMargin(40f+10f+230f+70f,centerHeight,0f,0f);
        mPlayPreBtn.setBackground("play_icon_function_rewind_normal");
        mPlayPreBtn.setFocusListener(new GLViewFocusListener() {
            @Override
            public void onFocusChange(GLRectView view, boolean focused) {
                if(focused) {
                        mPlayPreBtn.setBackground("play_icon_function_rewind_hover");
                } else {
                        mPlayPreBtn.setBackground("play_icon_function_rewind_normal");
                }
            }
        });
        mPlayPreBtn.setOnKeyListener(new GLOnKeyListener() {
            @Override
            public boolean onKeyDown(GLRectView view, int keycode) {
                switch (keycode) {
                    case MojingKeyCode.KEYCODE_ENTER:
                        if(mIPlayerSettingCallBack!=null){
                            mIPlayerSettingCallBack.onSelectedLocalMovieOri(1);
                        }
                        break;
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
        HeadControlUtil.bindView(mPlayPreBtn);
        mRootView.addView(mPlayPreBtn);

        //下一曲
        mPlayNextBtn = new GLImageView(mContext);
        mPlayNextBtn.setLayoutParams(80f,80f);
        mPlayNextBtn.setMargin(40f+150f+80+230f+70f,centerHeight,0f,0f);
        mPlayNextBtn.setBackground("play_icon_function_fastforward_normal");
        mPlayNextBtn.setFocusListener(new GLViewFocusListener() {
            @Override
            public void onFocusChange(GLRectView view, boolean focused) {
                if(focused) {
                    mPlayNextBtn.setBackground("play_icon_function_fastforward_hover");
                } else {
                    mPlayNextBtn.setBackground("play_icon_function_fastforward_normal");
                }
            }
        });
        mPlayNextBtn.setOnKeyListener(new GLOnKeyListener() {
            @Override
            public boolean onKeyDown(GLRectView view, int keycode) {
                switch (keycode) {
                    case MojingKeyCode.KEYCODE_ENTER:
                        if(mIPlayerSettingCallBack!=null){
                            mIPlayerSettingCallBack.onSelectedLocalMovieOri(0);
                        }
                        break;
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
        HeadControlUtil.bindView(mPlayNextBtn);
        mRootView.addView(mPlayNextBtn);


        //创建播放按钮
        mPlayOrPauseBtn = new GLImageView(mContext);
        mPlayOrPauseBtn.setLayoutParams(80f,80f);
        mPlayOrPauseBtn.setMargin(40f+2*60f+230f+70f,centerHeight,0f,0f);
        mPlayOrPauseBtn.setBackground("play_icon_function_pause_normal");
        mPlayOrPauseBtn.setFocusListener(new GLViewFocusListener() {
            @Override
            public void onFocusChange(GLRectView view, boolean focused) {
                if(focused) {
                    if(playFlag) {//当前显示播放
                        mPlayOrPauseBtn.setBackground("play_icon_function_play_hover");
                    } else {//当前显示暂停
                        mPlayOrPauseBtn.setBackground("play_icon_function_pause_hover");
                    }
                } else {
                    if(playFlag) {//当前显示播放
                        mPlayOrPauseBtn.setBackground("play_icon_function_play_normal");
                    } else {//当前显示暂停
                        mPlayOrPauseBtn.setBackground("play_icon_function_pause_normal");
                    }
                }
            }
        });
        mPlayOrPauseBtn.setOnKeyListener(new GLOnKeyListener() {
            @Override
            public boolean onKeyDown(GLRectView view, int keycode) {
                switch (keycode) {
                    case MojingKeyCode.KEYCODE_ENTER:
                        if(!playFlag) {
                            setPlayOrPauseBtn(true);
                        } else {
                            setPlayOrPauseBtn(false);
                        }
                        if(null != mCallBack) {
                            mCallBack.onPlayChanged(playFlag);
                        }
                        break;
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
        HeadControlUtil.bindView(mPlayOrPauseBtn);
        mRootView.addView(mPlayOrPauseBtn);

        mName = new GLTextView(mContext);
        mName.setLayoutParams(1000f,60f);
        mName.setTextSize(32);
        mName.setTextColor(new GLColor(0x666666));
        mName.setText("");
        mName.setAlignment(GLTextView.ALIGN_CENTER);
//        mName.setBackground(new GLColor(0x00ff00));
        mName.setMargin(0f,5f,0f,0f);
        mName.setPadding(0f,10f,0f,0f);

        mRootView.addView(mName);
    }

    GLLinearView mMenuViewContainer;
    PlayControlButton closeControlButton;
    PlayControlButton closeControlOneButton;
    PlayControlButton hideControlButton;

    private void createMenuButtonView() {
        mMenuViewContainer = new GLLinearView(mContext);
        mMenuViewContainer.setOrientation(GLConstant.GLOrientation.HORIZONTAL);
        mMenuViewContainer.setLayoutParams(1000f, 200f);
        mMenuViewContainer.setMargin(0, mHeigth+60, 0, 0);
        addView(mMenuViewContainer);
        mMenuViewContainer.setVisible(false);
        mMenuViewContainer.setFocusListener(focusListener);
        closeControlButton=getBackBtn(380);
        closeControlOneButton=getBackBtn(0);
        closeControlOneButton.setVisible(false);

        hideControlButton = new PlayControlButton(mContext);
        hideControlButton.setLayoutParams(80, 80 + 50);
        hideControlButton.setImageLayout(80, 80);
        hideControlButton.getText().setMargin(15, 100, 0, 0);
        hideControlButton.setId(SELECTED_SOURCE);
        hideControlButton.setMargin(0, 0f, 0f, 0f);
        hideControlButton.setImageBg("play_menu_button_close_normal");
        hideControlButton.setSelected(false);
        hideControlButton.getText().setMargin(15,100,0,0);
        hideControlButton.setText("隐藏");
        hideControlButton.setImageHeadControl(true);
        hideControlButton.setImageFocusListener(new GLViewFocusListener() {
            @Override
            public void onFocusChange(GLRectView view, boolean focused) {
                setLockScreenBtn(focused);
                if(focused) {
                    hideControlButton.setTextVisible(true);
                    hideControlButton.setImageBg("play_menu_button_close_hover");
                } else {
                    hideControlButton.setTextVisible(false);
                    hideControlButton.setImageBg("play_menu_button_close_normal");
                }
            }
        });
        hideControlButton.setImageKeyListener(new GLOnKeyListener() {
            @Override
            public boolean onKeyDown(GLRectView view, int keycode) {
                switch (keycode) {
                    case MojingKeyCode.KEYCODE_ENTER:
                        hideAllView();
                        break;
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
        mMenuViewContainer.addView(hideControlButton);
        setLockScreenBtn(false);
    }


    public PlayControlButton getBackBtn(int left){
        final PlayControlButton tempCloseControlButton = new PlayControlButton(mContext);
        tempCloseControlButton.setLayoutParams(80, 80 + 50);
        tempCloseControlButton.setImageLayout(80, 80);
        tempCloseControlButton.getText().setMargin(15, 100, 0, 0);
        tempCloseControlButton.setId(SELECTED_SOURCE);
        tempCloseControlButton.setMargin(left, 0f, 0f, 0f);
        tempCloseControlButton.setImageBg("play_menu_button_quit_normal");
        tempCloseControlButton.setSelected(false);
        tempCloseControlButton.setText("退出");
        tempCloseControlButton.getText().setMargin(15,100,0,0);
        tempCloseControlButton.setImageHeadControl(true);
        tempCloseControlButton.setImageFocusListener(new GLViewFocusListener() {
            @Override
            public void onFocusChange(GLRectView view, boolean focused) {
                if(focused) {
                    tempCloseControlButton.setTextVisible(true);
                    tempCloseControlButton.setImageBg("play_menu_button_quit_hover");
                } else {
                    tempCloseControlButton.setTextVisible(false);
                    tempCloseControlButton.setImageBg("play_menu_button_quit_normal");
                }
            }
        });
        tempCloseControlButton.setImageKeyListener(new GLOnKeyListener() {
            @Override
            public boolean onKeyDown(GLRectView view, int keycode) {
                switch (keycode) {
                    case MojingKeyCode.KEYCODE_ENTER:
                        ((MjVrPlayerActivity)mContext).finish();
                        break;
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
        mMenuViewContainer.addView(tempCloseControlButton);
        return tempCloseControlButton;
    }

    /**
     * 点击时其它按钮置灰
     * @param id
     */
    private void resetOtherButton(String id) {
        if(SELECTED_SOURCE.equals(id)) {
            mSoundBtn.setSelected(false);
            refreshButtonImage(SOUND,BTN_NO_FOCUS);
            mHdOrModeBtn.setSelected(false);
            refreshButtonImage(HD_MODE,BTN_NO_FOCUS);
            mSettingBtn.setSelected(false);
            refreshButtonImage(SETTING,BTN_NO_FOCUS);
        } else if(SOUND.equals(id)) {
            mSelectedSourceBtn.setSelected(false);
            refreshButtonImage(SELECTED_SOURCE,BTN_NO_FOCUS);
            mHdOrModeBtn.setSelected(false);
            refreshButtonImage(HD_MODE,BTN_NO_FOCUS);
            mSettingBtn.setSelected(false);
            refreshButtonImage(SETTING,BTN_NO_FOCUS);
        } else if(HD_MODE.equals(id)) {
            mSelectedSourceBtn.setSelected(false);
            refreshButtonImage(SELECTED_SOURCE,BTN_NO_FOCUS);
            mSoundBtn.setSelected(false);
            refreshButtonImage(SOUND,BTN_NO_FOCUS);
            mSettingBtn.setSelected(false);
            refreshButtonImage(SETTING,BTN_NO_FOCUS);
        } else if(SETTING.equals(id)) {
            mSelectedSourceBtn.setSelected(false);
            refreshButtonImage(SELECTED_SOURCE,BTN_NO_FOCUS);
            mSoundBtn.setSelected(false);
            refreshButtonImage(SOUND,BTN_NO_FOCUS);
            mHdOrModeBtn.setSelected(false);
            refreshButtonImage(HD_MODE,BTN_NO_FOCUS);
        } else {
            mSelectedSourceBtn.setSelected(false);
            refreshButtonImage(SELECTED_SOURCE,BTN_NO_FOCUS);
            mSoundBtn.setSelected(false);
            refreshButtonImage(SOUND,BTN_NO_FOCUS);
            mHdOrModeBtn.setSelected(false);
            refreshButtonImage(HD_MODE,BTN_NO_FOCUS);
            mSettingBtn.setSelected(false);
            refreshButtonImage(SETTING,BTN_NO_FOCUS);
        }
    }

    public static final int BTN_NO_FOCUS = 0;
    public static final int BTN_FOCUS = 1;
    public static final int BTN_CLICK = 2;

    public void refreshButtonImage(String id,int status) {
        if(SELECTED_SOURCE.equals(id)) {
//            switch (status) {
//                case BTN_NO_FOCUS:
//                    if(mSelectSourceType == GLConst.EPISODE_SOURCE) {
//                        mSelectedSourceBtn.setImageBg(R.drawable.play_icon_function_number_normal);
//                    } else if(mSelectSourceType == GLConst.MOVIE_SOURCE){
//                        mSelectedSourceBtn.setImageBg(R.drawable.play_menu_button_file_normal);
//                    }
//                    break;
//                case BTN_FOCUS:
//                    if(mSelectSourceType == GLConst.EPISODE_SOURCE) {
//                        mSelectedSourceBtn.setImageBg(R.drawable.play_icon_function_number_hover);
//                    } else if(mSelectSourceType == GLConst.MOVIE_SOURCE){
//                        mSelectedSourceBtn.setImageBg(R.drawable.play_menu_button_file_hover);
//                    }
//                    break;
//                case BTN_CLICK:
//                    if(mSelectSourceType == GLConst.EPISODE_SOURCE) {
//                        mSelectedSourceBtn.setImageBg(R.drawable.play_icon_function_number_click);
//                    } else if(mSelectSourceType == GLConst.MOVIE_SOURCE){
//                        mSelectedSourceBtn.setImageBg(R.drawable.play_menu_button_file_hover);
//                    }
//                    break;
//            }
        } else if(SOUND.equals(id)) {
            switch (status) {
                case BTN_NO_FOCUS:
                    mSoundBtn.setImageBg("play_icon_function_voice_normal");
                    break;
                case BTN_FOCUS:
                    mSoundBtn.setImageBg("play_icon_function_voice_hover");
                    break;
                case BTN_CLICK:
                    mSoundBtn.setImageBg("play_icon_function_voice_click");
                    break;
            }
        } else if(HD_MODE.equals(id)) {
            switch (status) {
                case BTN_NO_FOCUS:
                    if(mType == GLConst.MOVIE || mType == GLConst.PANO) {
                        if(!hdable) {
                            mHdOrModeBtn.setImageBg("play_icon_function_definition_disable");
                        } else {
                            mHdOrModeBtn.setImageBg("play_icon_function_definition_normal");
                        }
                    } else if(mType == GLConst.LOCAL_MOVIE || mType == GLConst.LOCAL_PANO) {
                        mHdOrModeBtn.setImageBg("play_icon_function_model_normal");
                    }
                    break;
                case BTN_FOCUS:
                    if(mType == GLConst.MOVIE || mType == GLConst.PANO) {
                        mHdOrModeBtn.setImageBg("play_icon_function_definition_hover");
                    } else if(mType == GLConst.LOCAL_MOVIE || mType == GLConst.LOCAL_PANO) {
                        mHdOrModeBtn.setImageBg("play_icon_function_model_hover");
                    }
                    break;
                case BTN_CLICK:
                    if(mType == GLConst.MOVIE || mType == GLConst.PANO) {
                        mHdOrModeBtn.setImageBg("play_icon_function_definition_click");
                    } else if(mType == GLConst.LOCAL_MOVIE || mType == GLConst.LOCAL_PANO) {
                        mHdOrModeBtn.setImageBg("play_icon_function_model_click");
                    }
                    break;
            }
        } else if(SETTING.equals(id)) {
            switch (status) {
                case BTN_NO_FOCUS:
                    mSettingBtn.setImageBg("play_icon_function_setting_normal");
                    break;
                case BTN_FOCUS:
                    mSettingBtn.setImageBg("play_icon_function_setting_hover");
                    break;
                case BTN_CLICK:
                    mSettingBtn.setImageBg("play_icon_function_setting_click");
                    break;
            }
        }
    }

//    /**
//     * 设置播放控制按钮状态
//     * @param id
//     * @param isSelected
//     */
//    public void setPlayControlBtn(String id,boolean isSelected) {
//
//        if(SELECTED_SOURCE.equals(id)) {
//            if(isSelected) {
//                mSelectedSourceBtn.setImageBg(R.drawable.play_icon_function_number_click);
//            } else {
//                if(mSelectedSourceBtn.isFocused()) {
//                    mSelectedSourceBtn.setImageBg(R.drawable.play_icon_function_number_hover);
//                } else {
//                    mSelectedSourceBtn.setImageBg(R.drawable.play_icon_function_number_normal);
//                }
//            }
//            mSelectedSourceBtn.setSelected(isSelected);
//        } else if(SOUND.equals(id)) {
//            if(isSelected) {
//                mSoundBtn.setImageBg(R.drawable.play_icon_function_voice_click);
//            } else {
//                if(mSoundBtn.isFocused()) {
//                    mSoundBtn.setImageBg(R.drawable.play_icon_function_voice_hover);
//                } else {
//                    mSoundBtn.setImageBg(R.drawable.play_icon_function_voice_normal);
//                }
//            }
//            mSoundBtn.setSelected(isSelected);
//        } else if(HD.equals(id)) {
//            if(isSelected) {
//                mHDBtn.setImageBg(R.drawable.play_icon_function_definition_click);
//            } else {
//                if(mHDBtn.isFocused()) {
//                    mHDBtn.setImageBg(R.drawable.play_icon_function_definition_hover);
//                } else {
//                    mHDBtn.setImageBg(R.drawable.play_icon_function_definition_normal);
//                }
//            }
//            mHDBtn.setSelected(isSelected);
//        } else if(SETTING.equals(id)) {
//            if(isSelected) {
//                mSettingBtn.setImageBg(R.drawable.play_icon_function_setting_click);
//            } else {
//                if(mSettingBtn.isFocused()) {
//                    mSettingBtn.setImageBg(R.drawable.play_icon_function_setting_hover);
//                } else {
//                    mSettingBtn.setImageBg(R.drawable.play_icon_function_setting_normal);
//                }
//            }
//            mSettingBtn.setSelected(isSelected);
//        }
//    }

    /**
     * true 显示播放按钮，false 显示暂停按钮
     * @param playOrPause
     */
    public void setPlayOrPauseBtn(boolean playOrPause) {
        if(playOrPause) {
            if(mPlayOrPauseBtn.isFocused()) {
                mPlayOrPauseBtn.setBackground("play_icon_function_play_hover");
            } else {
                mPlayOrPauseBtn.setBackground("play_icon_function_play_normal");
            }
        } else {
            if(mPlayOrPauseBtn.isFocused()) {
                mPlayOrPauseBtn.setBackground("play_icon_function_pause_hover");
            } else {
                mPlayOrPauseBtn.setBackground("play_icon_function_pause_normal");
            }
        }
        playFlag = playOrPause;
    }

    private void createBottomView() {
        mBottomView = new GLLinearView(mContext);
        mBottomView.setOrientation(GLConstant.GLOrientation.HORIZONTAL);
        mBottomView.setAlign(GLConstant.GLAlign.CENTER_VERTICAL);
        mBottomView.setLayoutParams(mWidth-80f,60f);
        mBottomView.setMargin(40f,170f,40f,0f);

        mCurrentTime = new GLTextView(mContext);
        mCurrentTime.setLayoutParams(110f,40f);
        mCurrentTime.setTextSize(28);
        mCurrentTime.setTextColor(new GLColor(0x666666));
        mCurrentTime.setText("00:00:00");
//        mCurrentTime.setBackground(new GLColor(0xff0000));
        mCurrentTime.setMargin(0f,10f,0f,0f);
        mCurrentTime.setPadding(0f,3f,0f,0f);

        seekBarView = new GLSeekBarView(mContext);
        seekBarView.setMargin(10f,25f,10f,0);
        HeadControlUtil.bindView(seekBarView);

        mTotalTime = new GLTextView(mContext);
        mTotalTime.setLayoutParams(110f,40f);
        mTotalTime.setTextSize(28);
        mTotalTime.setTextColor(new GLColor(0x666666));
        mTotalTime.setText("00:00:00");
        mTotalTime.setAlignment(GLTextView.ALIGN_RIGHT);
//        mTotalTime.setBackground(new GLColor(0x0000ff));
        mTotalTime.setMargin(20f,10f,0f,0f);
        mTotalTime.setPadding(0f,3f,0f,0f);

        mBottomView.addView(mCurrentTime);
        mBottomView.addView(seekBarView);
        mBottomView.addView(mTotalTime);

        mRootView.addView(mBottomView);
    }

    /**
     * 更新当前加载进度
     * @param process
     */
    public void setProcess(int process) {
        seekBarView.setProcess(process);
    }
    /**
     * 更新预加载进度
     * @param process
     */
    public void setPrestrainProcess(int process) {
//        seekBarView.setPrestrainProcess(process);
    }

    private IPlayerControlCallBack mCallBack;
    private IViewVisiableListener mVisiableCallBack;

    /**
     * 播放控制回调
     * @param callBack
     */
    public void setIPlayerControlCallBack(IPlayerControlCallBack callBack){
        mCallBack = callBack;
        if(seekBarView!=null) {
            seekBarView.setIPlayerControlCallBack(callBack);
        }
    }
    IPlayerSettingCallBack mIPlayerSettingCallBack;
    public void setIPlayerSettingCallBack(IPlayerSettingCallBack callBack){
        mIPlayerSettingCallBack=callBack;
    }


    public void setOnViewVisiableListener(IViewVisiableListener listener){
        mVisiableCallBack = listener;
    }

    /**
     * 更新进度
     * @param current
     * @param duration
     */
    public void updateProgress(int current,int duration){
//        MJGLUtils.exeGLQueueEvent(mContext, new Runnable() {
//            @Override
//            public void run() {
                if(seekBarView!=null) {
                    final int progress = (int) ((float) current / duration * 100);
                    MJGLUtils.exeGLQueueEvent(mContext, new Runnable() {
                        @Override
                        public void run() {
                            seekBarView.setProcess(progress);
                        }
                    });

                }
                if(mCurrentTime!=null){
                    String cur = TimeFormat.format(current/1000);
                    mCurrentTime.setText(cur);
                }
                if(mTotalTime!=null) {
                    String dur = TimeFormat.format(duration / 1000);
                    mTotalTime.setText(dur);
                }
//            }
//        });
    }

    public void updateDisplayDuration(long duration){
        if(seekBarView!=null) {
            seekBarView.updateDisplayDuration(duration);
        }
    }

    /**
     * 最大显示24个中文字符
     * @param name
     */
    public void setName(String name){
        if(!TextUtils.isEmpty(name)) {
            int length = 30;
            if (name.length() > length) {
                name = name.substring(0, 15) +"..."+name.substring(name.length()-15);
            }
            mName.setText(name);
        } else {
            mName.setText("");
        }
    }

    /**
     * 重置所有点击按钮状态并隐藏
     */
    public void hideAllView() {
        //重置所有点击按钮
        resetOtherButton("");
        mRootView.setVisible(false);
        mTopViewContainer.setVisible(false);
        mMenuViewContainer.setVisible(false);
        if(mVisiableCallBack!=null){
            mVisiableCallBack.onVisibility(false);
        }
    }

    public void showAllView() {
        mRootView.setVisible(true);
        mTopViewContainer.setVisible(true);
        mMenuViewContainer.setVisible(true);
        if(mVisiableCallBack!=null){
            mVisiableCallBack.onVisibility(true);
        }
//        Log.d("cursor","--------showCursorView---showAllView");
        ((GLBaseActivity)mContext).showCursorView();
    }

    public boolean isShow() {
        return mRootView.isVisible();
    }

    /**
     * true ：暂停   false：播放
     * @return
     */
    public boolean isPlayFlag(){
        return playFlag;
    }

    public void resetAll() {
        resetOtherButton("");
        if(null != mCallBack) {
            mCallBack.onControlChanged("",false);
        }
    }

    public void setType(int type) {
        mType = type;
//        resetAll();
        refreshButtonImage(HD_MODE,BTN_NO_FOCUS);
        if(mType == GLConst.MOVIE || mType == GLConst.PANO) {
            mHdOrModeBtn.setImageBg("play_icon_function_definition_disable");
            mHdOrModeBtn.setText("画质");
            mHdOrModeBtn.setImageHeadControl(false);
        } else if(mType == GLConst.LOCAL_MOVIE || mType == GLConst.LOCAL_PANO) {
            mHdOrModeBtn.setText("模式");
            mHdOrModeBtn.setImageHeadControl(true);
        }
    }

    private int mSelectSourceType = GLConst.MOVIE_SOURCE;
    /**
     * 0 无选集无选片 1 选集 2 选片
     * @param type
     */
    public void setSelectSourceType(int type) {
//        mSelectSourceType = type;
//        if(mSelectSourceType == GLConst.NO_SOURCE) {
//            mSelectedSourceBtn.setImageBg(R.drawable.play_icon_function_video_disable);
//            mSelectedSourceBtn.setImageHeadControl(false);
//        } else {
//            mSelectedSourceBtn.setImageHeadControl(true);
//            refreshButtonImage(SELECTED_SOURCE,BTN_NO_FOCUS);
//            if(mSelectSourceType == GLConst.EPISODE_SOURCE) {
//                mSelectedSourceBtn.setText("选集");
//            } else if(mSelectSourceType == GLConst.MOVIE_SOURCE) {
//                mSelectedSourceBtn.setText("选片");
//            }
//        }
    }
    private boolean hdable;
    public void setHDable(boolean isAble) {
        hdable = isAble;
        if(mType == GLConst.MOVIE || mType == GLConst.PANO) {
            if(!isAble) {
                mHdOrModeBtn.setImageBg("play_icon_function_definition_disable");
            } else {
                mHdOrModeBtn.setImageBg("play_icon_function_definition_normal");
            }
            mHdOrModeBtn.setImageHeadControl(isAble);
        }
    }
}
