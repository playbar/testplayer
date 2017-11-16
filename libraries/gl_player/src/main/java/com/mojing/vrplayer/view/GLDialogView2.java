package com.mojing.vrplayer.view;

import android.content.Context;
import android.text.TextUtils;
import com.bfmj.viewcore.interfaces.GLOnKeyListener;
import com.bfmj.viewcore.interfaces.GLViewFocusListener;
import com.bfmj.viewcore.render.GLColor;
import com.bfmj.viewcore.view.GLRectView;
import com.bfmj.viewcore.view.GLRelativeView;
import com.bfmj.viewcore.view.GLTextView;
import com.mojing.vrplayer.R;
import com.mojing.vrplayer.utils.HeadControlUtil;

/**
 * Created by yushaochen on 2017/4/10.
 */

public class GLDialogView2 extends GLRelativeView{

    private Context mContext;

    private int width = 800;
    private int height = 200+140;
    private int btnWidth = 240;
    private int btnHeight = 70;

    private GLTextView textView;
    private GLTextView leftBtn;
    private GLTextView rightBtn;

    private boolean isContinuBtn;
    public GLDialogView2(Context context) {
        super(context);
        mContext = context;
        setLayoutParams(width,height);
        setBackground(new GLColor(0,0,0,0.85f));
        //创建需要显示的view
        createView();
        setVisible(false);
    }

    private void createView() {
        //创建文案显示
        textView = new GLTextView(mContext);
        textView.setLayoutParams(width,100);
        textView.setMargin(0,70,0,0);
        textView.setTextSize(30);
        textView.setTextColor(new GLColor(0x888888));
        textView.setAlignment(GLTextView.ALIGN_CENTER);
        textView.setPadding(0,20,0,0);

        textView.setText("您正在使用非WiFi网络，播放将产生流量费用");
        addView(textView);
        //创建退出按钮
        leftBtn = new GLTextView(mContext);
        leftBtn.setLayoutParams(btnWidth,btnHeight);
        leftBtn.setBackground("play_button_bg_ok_normal");
        leftBtn.setMargin((800f-540f)/2,60f+70f+70f,0f,70f);
        leftBtn.setTextSize(32);
        leftBtn.setTextColor(new GLColor(0x888888));
        leftBtn.setAlignment(GLTextView.ALIGN_CENTER);
        leftBtn.setText("退出");
        leftBtn.setPadding(0f,10f,0f,20f);
        leftBtn.setFocusListener(new GLViewFocusListener() {
            @Override
            public void onFocusChange(GLRectView view, boolean focused) {
                if(focused) {
                    leftBtn.setBackground("play_button_bg_ok_hover");
                    leftBtn.setTextColor(new GLColor(0x19191a));
                } else {
                    leftBtn.setBackground("play_button_bg_ok_normal");
                    leftBtn.setTextColor(new GLColor(0x888888));
                }
            }
        });
        HeadControlUtil.bindView(leftBtn);
        addView(leftBtn);
        //创建继续播放按钮
        rightBtn = new GLTextView(mContext);
        rightBtn.setLayoutParams(btnWidth,btnHeight);
        rightBtn.setBackground("play_button_bg_ok_normal");
        rightBtn.setMargin((800f-540f)/2+300f,60f+70f+70f,0f,70f);
        rightBtn.setTextSize(32);
        rightBtn.setTextColor(new GLColor(0x888888));
        rightBtn.setAlignment(GLTextView.ALIGN_CENTER);
        rightBtn.setText("继续播放");
        rightBtn.setPadding(0f,10f,0f,20f);
        rightBtn.setFocusListener(new GLViewFocusListener() {
            @Override
            public void onFocusChange(GLRectView view, boolean focused) {
                if(focused) {
                    rightBtn.setBackground("play_button_bg_ok_hover");
                    rightBtn.setTextColor(new GLColor(0x19191a));
                } else {
                    rightBtn.setBackground("play_button_bg_ok_normal");
                    rightBtn.setTextColor(new GLColor(0x888888));
                }
            }
        });
        HeadControlUtil.bindView(rightBtn);
        addView(rightBtn);
    }

    public void setTitleText(String text) {
        if(!TextUtils.isEmpty(text)) {
            textView.setText(text);
        }
    }

    public void setLeftText(String text) {
        if(!TextUtils.isEmpty(text)) {
            leftBtn.setText(text);
        }
    }

    public void setRightText(String text) {
        if(!TextUtils.isEmpty(text)) {
            rightBtn.setText(text);
        }
    }
    public boolean getIsContinueBtn(){
        return isContinuBtn;
    }


    public void setLeftKeyListener(GLOnKeyListener keyListener) {
        if(null != keyListener) {
            leftBtn.setOnKeyListener(keyListener);
        }
    }

    public void setRightKeyListener(GLOnKeyListener keyListener) {
        if(null != keyListener) {
            rightBtn.setOnKeyListener(keyListener);
        }
    }

    public void showExceptionDialog(String msg,String BtnName,boolean isContinuBtn){
        this.isContinuBtn = isContinuBtn;
        setRightText(BtnName);
        setTitleText(msg);
    }

}
