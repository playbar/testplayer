package com.mojing.vrplayer.view;

import android.content.Context;
import android.graphics.Bitmap;

import com.bfmj.viewcore.interfaces.GLViewFocusListener;
import com.bfmj.viewcore.render.GLColor;
import com.bfmj.viewcore.view.GLImageView;
import com.bfmj.viewcore.view.GLRectView;
import com.bfmj.viewcore.view.GLRelativeView;
import com.bfmj.viewcore.view.GLTextView;
import com.mojing.vrplayer.interfaces.IPlayerSettingCallBack;
import com.mojing.vrplayer.utils.BitmapUtil;
import com.mojing.vrplayer.utils.GLConst;

/**
 * Created by yushaochen on 2017/5/16.
 */

public class LocalModeView extends GLRelativeView{

    private Context mContext;

    private int mWidth = 1000;
    private int mHeigth = 400;

    private RatioView ratioView;

    private RoteView roteView;

    private ModeView leftRightModeView;

    private ModeView roundModeView;

    public LocalModeView(Context context) {
        super(context);
        mContext = context;
        setLayoutParams(mWidth,mHeigth);
        Bitmap bitmap = BitmapUtil.getBitmap(mWidth, mHeigth, 20f, "#272729");
        setBackground(bitmap);
        setFocusListener(focusListener);

        //创建比例选择view
        createRatioView();

        //创建旋转选择view
        createRoteView();
        
        //创建分隔线view
        createLineView();
        
        //创建模式选择view
        createModeView();
    }

    public void setType(int type) {
        if(type == GLConst.LOCAL_MOVIE) {
            roteTextView.setTextColor(new GLColor(0x888888));
            ratioTextView.setTextColor(new GLColor(0x888888));
        } else if(type == GLConst.LOCAL_PANO) {
            roteTextView.setTextColor(new GLColor(0x333333));
            ratioTextView.setTextColor(new GLColor(0x333333));
        }
        ratioView.setType(type);
        roteView.setType(type);
    }

    private void createModeView() {
        GLTextView modeTextView = new GLTextView(mContext);
        modeTextView.setLayoutParams(65,35);
        modeTextView.setText("模式:");
        modeTextView.setTextSize(28);
        modeTextView.setTextColor(new GLColor(0x888888));
        modeTextView.setMargin(45+41*2+130*3+10*2+8,82-20-35,0,0);
        modeTextView.setAlignment(GLTextView.ALIGN_LEFT);

        leftRightModeView = new ModeView(mContext);
        leftRightModeView.initView(ModeView.LEFT_RIGHT_MODE);
        leftRightModeView.setMargin(45+41*2+130*3+10*2+8,82,0,0);

        roundModeView = new ModeView(mContext);
        roundModeView.initView(ModeView.ROUND_MODE);
        roundModeView.setMargin(45+41*2+130*3+10*2+8,82+80+15+35+43,0,0);

        addView(modeTextView);
        addView(leftRightModeView);
        addView(roundModeView);
    }

    private void createLineView() {
        GLImageView lineView = new GLImageView(mContext);
        lineView.setLayoutParams(8,328);
        Bitmap bitmap = BitmapUtil.getBitmap(8, 328, 4f, "#333333");
        lineView.setBackground(bitmap);
        lineView.setMargin(45+41+130*3+10*2,35,0,0);
        addView(lineView);
    }
    private GLTextView roteTextView;
    private void createRoteView() {
        roteTextView = new GLTextView(mContext);
        roteTextView.setLayoutParams(65,35);
        roteTextView.setText("旋转:");
        roteTextView.setTextSize(28);
        roteTextView.setTextColor(new GLColor(0x888888));
        roteTextView.setMargin(45,82+60*2+20+27,0,0);
        roteTextView.setAlignment(GLTextView.ALIGN_LEFT);

        roteView = new RoteView(mContext);
        roteView.setMargin(45,82+120+20+82,0,0);

        addView(roteTextView);
        addView(roteView);
    }
    private GLTextView ratioTextView;
    private void createRatioView() {
        ratioTextView = new GLTextView(mContext);
        ratioTextView.setLayoutParams(65,35);
        ratioTextView.setText("比例:");
        ratioTextView.setTextSize(28);
        ratioTextView.setTextColor(new GLColor(0x888888));
        ratioTextView.setMargin(45,82-20-35,0,0);
        ratioTextView.setAlignment(GLTextView.ALIGN_LEFT);
//        ratioTextView.setBackground(new GLColor(0xff0000));

        ratioView = new RatioView(mContext);
        ratioView.setMargin(45,82,0,0);

        addView(ratioTextView);
        addView(ratioView);
    }

    private IPlayerSettingCallBack mCallBack;

    public void setIPlayerSettingCallBack(IPlayerSettingCallBack callBack) {

        this.mCallBack = callBack;
        ratioView.setIPlayerSettingCallBack(callBack);
        roteView.setIPlayerSettingCallBack(callBack);
        leftRightModeView.setIPlayerSettingCallBack(callBack);
        roundModeView.setIPlayerSettingCallBack(callBack);
    }

    public void setSelectedRatio(String ratioName) {
        ratioView.setSelectedRatio(ratioName);
    }

    public void setSelectedRote(String roteName) {
        roteView.setSelectedRote(roteName);
    }

    public void setSelectedLeftRightMode(int mode) {
        leftRightModeView.setSelectedMode(mode);
    }

    public void setSelectedRoundMode(int mode) {
        roundModeView.setSelectedMode(mode);
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
