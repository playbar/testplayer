package com.mojing.vrplayer.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.baofeng.mojing.input.base.MojingKeyCode;
import com.bfmj.viewcore.interfaces.GLOnKeyListener;
import com.bfmj.viewcore.interfaces.GLViewFocusListener;
import com.bfmj.viewcore.render.GLColor;
import com.bfmj.viewcore.view.GLRectView;
import com.bfmj.viewcore.view.GLRelativeView;
import com.bfmj.viewcore.view.GLTextView;
import com.mojing.vrplayer.R;
import com.mojing.vrplayer.activity.GLBaseActivity;
import com.mojing.vrplayer.publicc.ReportBusiness;
import com.mojing.vrplayer.publicc.SettingSpBusiness;
import com.mojing.vrplayer.utils.BitmapUtil;
import com.mojing.vrplayer.utils.GLConst;
import com.mojing.vrplayer.utils.HeadControlUtil;

import java.util.HashMap;

/**
 * Created by wanghongfang on 2017/4/14.
 * 在线播放锁屏按钮
 */
public class GLLockScreenView extends GLRelativeView{
    private float mDepth = GLConst.LockScreen_Depth;
    private Context mContext;
    public static final int lock_view_widht = 2400;
    public static final int lock_view_height= 165+35;
    private int img_width = 100;
    private int img_height = 100;
    private int mTextWidth = 150;
    private int mTextHeight = 60;
    private DefGLImageView imageView;
    private ILockScreenListener callBack;
    private GLTextView mText;
    private boolean isLocked = false; //是否锁屏
    private Bitmap bgBitmap;
    private GLTextToast glTextToast;
    GLRelativeView viewLayer;
    public GLLockScreenView(Context context) {
        super(context);
        this.mContext = context;
        this.setLayoutParams(lock_view_widht,lock_view_height);
        viewLayer = new GLRelativeView(mContext);
        viewLayer.setLayoutParams(lock_view_widht,lock_view_height);
        createBtn();
        createToast();
        this.addView(viewLayer);
        setListener();
        isLocked = SettingSpBusiness.getInstance(mContext).getPlayerLockScreen();
        if(isLocked){
            imageView.setImage("play_lock_button_unlock_normal", "play_lock_button_unlock_hover");
            mText.setText("解锁屏幕");
        }else {
            imageView.setImage("play_lock_button_lock_normal","play_lock_button_lock_hover");
            mText.setText("锁定屏幕");
        }
    }

    private void setListener(){
        this.setFocusListener(new GLViewFocusListener() {
            @Override
            public void onFocusChange(GLRectView view, boolean focused) {
                if(focused) {
//                    Log.d("cursor","--------showCursorView---GLLockScreenView");
                    ((GLBaseActivity)getContext()).showCursorView();
                    reportShow(-1);
                } else {
//                    Log.d("cursor","--------hideCursorView---GLLockScreenView");
                    ((GLBaseActivity)getContext()).hideCursorView();
                }
                GLLockScreenView.this.viewLayer.setVisible(focused);
            }
        });
    }
    private void createBtn() {
        imageView = new DefGLImageView(mContext);
        imageView.setImage("play_lock_button_lock_normal","play_lock_button_lock_hover");
        imageView.setId("lock_btn");
        imageView.setLayoutParams(img_width,img_height);
        imageView.setMargin(0f,10f,0f,0f);
        HeadControlUtil.bindView(imageView);
        imageView.setFocusListener(mViewFocusLisener);
        imageView.setOnKeyListener(mViewKeyListener);
        mText = new GLTextView(mContext);
        mText.setLayoutParams(mTextWidth,mTextHeight);
        mText.setMargin(0f,img_height+20f,0f,0f);
        mText.setTextSize(28);
        mText.setPadding(15,10,15,10);
        mText.setTextColor(new GLColor(0x888888));
        mText.setAlignment(GLTextView.ALIGN_CENTER);
        mText.setText("锁定屏幕");
        mText.setVisible(false);
        bgBitmap = BitmapUtil.getBitmap(mTextWidth, mTextHeight, 10f, "#19191a");
        mText.setBackground(bgBitmap);
        //viewLayer.addViewCenterHorizontal(imageView);
      //  viewLayer.addViewCenterHorizontal(mText);
//        this.setBackground(new GLColor(1,0,0));
    }

    private void createToast(){
        glTextToast = new GLTextToast(getContext());
        glTextToast.setTextType(getContext().getString(R.string.gl_player_locked),GLTextToast.LARGE,false);
        glTextToast.setMargin(lock_view_widht/2-(490)/2,img_height+20f+10f,0f,0f);
        glTextToast.setBackground("play_lock_tips_bg");
        viewLayer.addView(glTextToast);
        glTextToast.setVisible(false);
    }

    public void showTips(String string){
        glTextToast.showToast(string,GLTextToast.LARGE,false);
    }

    public void onFinish(){
        if(bgBitmap!=null&&!bgBitmap.isRecycled()){
            bgBitmap.recycle();
        }
    }
    private void updateLockView(){
           isLocked =!isLocked;
                if(isLocked){
                    imageView.setImage("play_lock_button_unlock_normal","play_lock_button_unlock_hover");
                    mText.setText("解锁屏幕");
                }else {
                    imageView.setImage("play_lock_button_lock_normal","play_lock_button_lock_hover");
                    mText.setText("锁定屏幕");
                }
    }

    public void setViewVisable(boolean viewVisable){
        viewLayer.setVisible(viewVisable);
    }

    public boolean isViewVisable(){
        return viewLayer.isVisible();
    }
    public void setLockCallback(ILockScreenListener callback){
       this.callBack = callback;
    }

    private GLViewFocusListener mViewFocusLisener = new GLViewFocusListener() {
        @Override
        public void onFocusChange(GLRectView view, boolean focused) {
            ((DefGLImageView) view).updateFocuse(focused);
                mText.setVisible(focused);
        }
    };

    public GLOnKeyListener getLockKeyListener(){
        return mViewKeyListener;
    }

    public GLOnKeyListener mViewKeyListener = new GLOnKeyListener() {
        @Override
        public boolean onKeyDown(GLRectView view, int keycode) {
            Log.v("lockme","lock onKeyDown");
            if(keycode== MojingKeyCode.KEYCODE_ENTER){
                Log.v("lockme","lock onKeyDown KEYCODE_ENTER");
                updateLockView();
                if(callBack!=null) {
                    callBack.onLockChanged(isLocked);
                }
                if( isLocked&&SettingSpBusiness.getInstance(getContext()).getGLPlayerFirstLockTip()){ //锁屏
                    showTips(getContext().getString(R.string.gl_player_locked));
                    mText.setVisible(false);
                    SettingSpBusiness.getInstance(getContext()).setGLPlayerFirstLockTip(false);
                    reportShow(0);
                }else if(!isLocked&&SettingSpBusiness.getInstance(getContext()).getGLPlayerFirstUnLockTip()){
                    showTips(getContext().getString(R.string.gl_player_unlocked));
                    mText.setVisible(false);
                    SettingSpBusiness.getInstance(getContext()).setGLPlayerFirstUnLockTip(false);
                }
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
    };


    public interface ILockScreenListener{
        void onLockChanged(boolean islocked);
    }

    /**
     * 锁屏报数
     */
    private void reportShow(int popuptype ){
        HashMap<String,String> clickParam = new HashMap<>();
        clickParam.put("etype","show");
        clickParam.put("sensemode","commmovie");
        clickParam.put("showtype","0");
        if(popuptype>=0) {
            clickParam.put("popuptype", popuptype + "");
        }
        ReportBusiness.getInstance().reportClick(clickParam);
    }
}

