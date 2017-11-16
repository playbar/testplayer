package com.mojing.vrplayer.view;

import android.content.Context;
import android.graphics.Bitmap;

import com.baofeng.mojing.input.base.MojingKeyCode;
import com.bfmj.viewcore.interfaces.GLOnKeyListener;
import com.bfmj.viewcore.interfaces.GLViewFocusListener;
import com.bfmj.viewcore.view.GLRectView;
import com.bfmj.viewcore.view.GLRelativeView;
import com.mojing.vrplayer.R;
import com.mojing.vrplayer.interfaces.IPlayerSettingCallBack;
import com.mojing.vrplayer.utils.BitmapUtil;
import com.mojing.vrplayer.utils.HeadControlUtil;

/**
 * Created by yushaochen on 2017/4/11.
 */

public class SoundBarView extends GLRelativeView{

    private Context mContext;

    private int mWidth = 1000;
    private int mHeight = 120;

    private SoundControlButton mSoundBtn;

    private SoundSeekBar soundSeekBar;

    private boolean soundFlag = true;//默认关闭声音

    public SoundBarView(Context context) {
        super(context);

        mContext = context;
        setLayoutParams(mWidth,mHeight);
        Bitmap bitmap = BitmapUtil.getBitmap(mWidth, mHeight, 20f, "#272729");
        setBackground(bitmap);
        
        //创建左侧icon
        createIconView();
        //创建声音进度条
        createBarView();

        setFocusListener(focusListener);
    }

    private void createBarView() {
        GLRelativeView glRelativeView = new GLRelativeView(mContext);
        glRelativeView.setLayoutParams(740,60);
        glRelativeView.setBackground("play_bg_control_normal_740_60");
        glRelativeView.setMargin((88f-22f)+88f+(40f-22f),30f,0f,0f);

        soundSeekBar = new SoundSeekBar(mContext);
        soundSeekBar.setBackground("play_volume_slider_bg");
        soundSeekBar.setBarImage("play_volume_cursor_normal");
        soundSeekBar.setDuration(100);
        soundSeekBar.setMargin(30f,20f,30f,0);
        soundSeekBar.setProcess(0);
        HeadControlUtil.bindView(soundSeekBar);

        glRelativeView.addView(soundSeekBar);
        addView(glRelativeView);
    }

    private void createIconView() {
        mSoundBtn = new SoundControlButton(mContext);
        mSoundBtn.setImageBg("play_icon_function_voice_normal");
        mSoundBtn.setMargin(88f-22f,38f,0f,0f);
        mSoundBtn.setText("关闭静音");
        mSoundBtn.setSelected(false);
        mSoundBtn.setImageFocusListener(new GLViewFocusListener() {
            @Override
            public void onFocusChange(GLRectView view, boolean focused) {
                if(focused) {
                    mSoundBtn.setTextVisible(true);
                    if(soundFlag) {
                        mSoundBtn.setImageBg("play_icon_function_voice_hover");

                    } else {
                        mSoundBtn.setImageBg("play_icon_mute_hover1");
                    }
                } else {
                    mSoundBtn.setTextVisible(false);
                    if(soundFlag) {
                        mSoundBtn.setImageBg("play_icon_function_voice_normal");
                    } else {
                        mSoundBtn.setImageBg("play_icon_mute_normal");
                    }
                }
            }
        });
        mSoundBtn.setImageKeyListener(new GLOnKeyListener() {
            @Override
            public boolean onKeyDown(GLRectView view, int keycode) {
                switch (keycode) {
                    case MojingKeyCode.KEYCODE_ENTER:
                        if(!soundFlag) {
                            setSoundIcon(true);
                            mSoundBtn.setText("关闭静音");
                        } else {
                            setSoundIcon(false);
                            mSoundBtn.setText("打开静音");
                        }
                        if(null != mCallBack) {
                            mCallBack.isOpenSound(soundFlag);
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
        addView(mSoundBtn);
    }

    /**
     * true 静音关闭（有声音），false 静音开启 （无声音）
     * @param flag
     */
    public void setSoundIcon(boolean flag) {
        if(flag) {
            if(mSoundBtn.getTopImage().isFocused()) {
                mSoundBtn.setImageBg("play_icon_function_voice_hover");
            } else {
                mSoundBtn.setImageBg("play_icon_function_voice_normal");
            }
        } else {
            if(mSoundBtn.getTopImage().isFocused()) {
                mSoundBtn.setImageBg("play_icon_mute_hover1");
            } else {
                mSoundBtn.setImageBg("play_icon_mute_normal");
            }
        }
        soundFlag = flag;
    }

    public void setVolume(int vm) {
        soundSeekBar.setProcess(vm);
    }

    private IPlayerSettingCallBack mCallBack;

    public void setIPlayerSettingCallBack(IPlayerSettingCallBack callBack) {

        this.mCallBack = callBack;
        soundSeekBar.setIPlayerSettingCallBack(mCallBack);
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
}
