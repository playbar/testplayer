package com.mojing.vrplayer.view;

import android.content.Context;

import com.baofeng.mojing.input.base.MojingKeyCode;
import com.bfmj.viewcore.interfaces.GLOnKeyListener;
import com.bfmj.viewcore.interfaces.GLViewFocusListener;
import com.bfmj.viewcore.render.GLColor;
import com.bfmj.viewcore.render.GLConstant;
import com.bfmj.viewcore.view.GLLinearView;
import com.bfmj.viewcore.view.GLRectView;
import com.bfmj.viewcore.view.GLTextView;
import com.mojing.vrplayer.R;
import com.mojing.vrplayer.interfaces.IRePlayDialogCallBack;
import com.mojing.vrplayer.utils.HeadControlUtil;

/**
 * Created by yushaochen on 2017/5/18.
 */

public class RePlayDialogButtonView extends GLLinearView{

    private Context mContext;

    private int mWidth = 240*2+60;
    private int mHeigth = 70;

    private GLTextView rePlayBtn;

    private GLTextView exitPlayBtn;

    public RePlayDialogButtonView(Context context) {
        super(context);
        mContext = context;
        setOrientation(GLConstant.GLOrientation.HORIZONTAL);
        setLayoutParams(mWidth,mHeigth);

        //创建重播按钮
        rePlayBtn = new GLTextView(mContext);
        rePlayBtn.setLayoutParams(240f,70f);
        rePlayBtn.setBackground("play_button_bg_ok_normal");
        rePlayBtn.setTextSize(32);
        rePlayBtn.setTextColor(new GLColor(0x888888));
        rePlayBtn.setAlignment(GLTextView.ALIGN_CENTER);
        rePlayBtn.setText("重播");
        rePlayBtn.setPadding(0f,12f,0f,0f);
        rePlayBtn.setFocusListener(new GLViewFocusListener() {
            @Override
            public void onFocusChange(GLRectView view, boolean focused) {
                if(focused) {
                    rePlayBtn.setBackground("play_button_bg_ok_hover");
                    rePlayBtn.setTextColor(new GLColor(0x19191a));
                } else {
                    rePlayBtn.setBackground("play_button_bg_ok_normal");
                    rePlayBtn.setTextColor(new GLColor(0x888888));
                }
            }
        });
        rePlayBtn.setOnKeyListener(new GLOnKeyListener() {
            @Override
            public boolean onKeyDown(GLRectView view, int keycode) {
                switch (keycode) {
                    case MojingKeyCode.KEYCODE_ENTER:
                        if(null != mCallBack) {
                            mCallBack.onBtnClick(true);
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
        HeadControlUtil.bindView(rePlayBtn);
        addView(rePlayBtn);

        //创建退出按钮
        exitPlayBtn = new GLTextView(mContext);
        exitPlayBtn.setLayoutParams(240f,70f);
        exitPlayBtn.setBackground("play_button_bg_ok_normal");
        exitPlayBtn.setMargin(60,0,0,0);
        exitPlayBtn.setTextSize(32);
        exitPlayBtn.setTextColor(new GLColor(0x888888));
        exitPlayBtn.setAlignment(GLTextView.ALIGN_CENTER);
        exitPlayBtn.setText("退出");
        exitPlayBtn.setPadding(0f,12f,0f,0f);
        exitPlayBtn.setFocusListener(new GLViewFocusListener() {
            @Override
            public void onFocusChange(GLRectView view, boolean focused) {
                if(focused) {
                    exitPlayBtn.setBackground("play_button_bg_ok_hover");
                    exitPlayBtn.setTextColor(new GLColor(0x19191a));
                } else {
                    exitPlayBtn.setBackground("play_button_bg_ok_normal");
                    exitPlayBtn.setTextColor(new GLColor(0x888888));
                }
            }
        });
        exitPlayBtn.setOnKeyListener(new GLOnKeyListener() {
            @Override
            public boolean onKeyDown(GLRectView view, int keycode) {
                switch (keycode) {
                    case MojingKeyCode.KEYCODE_ENTER:
                        if(null != mCallBack) {
                            mCallBack.onBtnClick(false);
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
        HeadControlUtil.bindView(exitPlayBtn);
        addView(exitPlayBtn);
    }

    private IRePlayDialogCallBack mCallBack;

    public void setIRePlayDialogCallBack(IRePlayDialogCallBack callBack) {
        mCallBack = callBack;
    }
}
