package com.mojing.vrplayer.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;

import com.baofeng.mojing.input.base.MojingKeyCode;
import com.bfmj.viewcore.interfaces.GLOnKeyListener;
import com.bfmj.viewcore.interfaces.GLViewFocusListener;
import com.bfmj.viewcore.render.GLColor;
import com.bfmj.viewcore.view.GLLinearView;
import com.bfmj.viewcore.view.GLRectView;
import com.bfmj.viewcore.view.GLRelativeView;
import com.bfmj.viewcore.view.GLTextView;
import com.mojing.vrplayer.R;
import com.mojing.vrplayer.interfaces.IPlayerSettingCallBack;
import com.mojing.vrplayer.utils.BitmapUtil;
import com.mojing.vrplayer.utils.GLConst;
import com.mojing.vrplayer.utils.HeadControlUtil;
import com.mojing.vrplayer.utils.ViewUtil;

/**
 * Created by yushaochen on 2017/4/11.
 */

public class HDTypeView extends GLRelativeView{

    private Context mContext;

    private int mWidth = 1000;
    private int mHeight = 120;

    private int mItemWidth = 150;
    private int mItemHeight = 60;
    private GLLinearView linearView;

    public HDTypeView(Context context) {
        super(context);

        mContext = context;
        setLayoutParams(mWidth,mHeight);
        Bitmap bitmap = BitmapUtil.getBitmap(mWidth, mHeight, 20f, "#272729");
        setBackground(bitmap);

        linearView = new GLLinearView(mContext);
        addView(linearView);

        setFocusListener(focusListener);
//        setDepth(GLConst.Player_Settings_Depth,GLConst.Player_Settings_Scale);

    }

    public void initView(String[] strs) {
        resetView();
        if(null != strs && strs.length > 0) {
            linearView.setLayoutParams(ViewUtil.getDip(mItemWidth*strs.length+10*(strs.length-1), GLConst.Player_Settings_Scale),ViewUtil.getDip(mItemHeight,GLConst.Player_Settings_Scale));
            linearView.setMargin(ViewUtil.getDip((mWidth-(mItemWidth*strs.length+10*(strs.length-1)))/2,GLConst.Player_Settings_Scale),ViewUtil.getDip(30f,GLConst.Player_Settings_Scale),0f,0f);
//            linearView.scale(GLConst.Player_Settings_Scale);
            for(int x = 0; x < strs.length; x++) {
                GLTextView glTextView = new GLTextView(mContext);
                glTextView.setId(strs[x]);
                glTextView.setSelected(false);
                glTextView.setLayoutParams(mItemWidth,mItemHeight);
                glTextView.setBackground("play_bg_control_normal_150_60");
                glTextView.setAlignment(GLTextView.ALIGN_CENTER);
                glTextView.setTextSize(28);
                glTextView.setTextColor(new GLColor(0x888888));
                if(strs[x].endsWith("k")){
                    glTextView.setText(strs[x].substring(0,strs[x].length()-1)+"K");
                }else if(strs[x].endsWith("p")){
                    glTextView.setText(strs[x].substring(0,strs[x].length()-1)+"P");
                }else if(!(strs[x].endsWith("K")||strs[x].endsWith("P"))){
                    glTextView.setText(strs[x]+"P");
                }
                glTextView.setPadding(0f,14f,0f,0f);
                if(x != 0) {
                    glTextView.setMargin(10f,0f,0f,0f);
                }
                glTextView.setFocusListener(new GLViewFocusListener() {
                    @Override
                    public void onFocusChange(GLRectView view, boolean focused) {
                        if(null != view) {
                            if(view instanceof GLTextView) {
                                if(!((GLTextView)view).isSelected()) {
                                    if(focused) {
                                        ((GLTextView)view).setTextColor(new GLColor(0xbbbbbb));
                                        ((GLTextView)view).setBackground("play_bg_control_hover_150_60");
                                    } else {
                                        ((GLTextView)view).setTextColor(new GLColor(0x888888));
                                        ((GLTextView)view).setBackground("play_bg_control_normal_150_60");
                                    }
                                }
                            }
                        }
                    }
                });
                glTextView.setOnKeyListener(new GLOnKeyListener() {
                    @Override
                    public boolean onKeyDown(GLRectView view, int keycode) {
                        switch (keycode) {
                            case MojingKeyCode.KEYCODE_ENTER:
                                setSelectedHD(view.getId());
                                if(null != mCallBack) {
                                    mCallBack.onHDChange(view.getId());
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
                glTextView.setDepth(GLConst.Player_Settings_Depth);
                glTextView.scale(GLConst.Player_Settings_Scale);
                linearView.addView(glTextView);
                HeadControlUtil.bindView(glTextView);
            }

        }

    }

    public void setSelectedHD(String hd) {
        refreshItemView(hd);
    }

    private void refreshItemView(String hd) {
        if(!TextUtils.isEmpty(hd)) {
            clearSelected();
            GLRectView view = linearView.getView(hd);
            if(view instanceof GLTextView) {
                ((GLTextView) view).setBackground("play_bg_control_normal_150_60");
                ((GLTextView) view).setTextColor(new GLColor(0x008cb3));
            }
            ((GLTextView) view).setSelected(true);
        }

    }

    private void clearSelected() {
        for(GLRectView childView : linearView.getChildView()) {
            if(childView instanceof GLTextView) {
                ((GLTextView) childView).setSelected(false);
                ((GLTextView) childView).setTextColor(new GLColor(0x888888));
                ((GLTextView) childView).setBackground("play_bg_control_normal_150_60");
            }
        }
    }

    private void resetView() {
        linearView.removeAllView();
    }

    private IPlayerSettingCallBack mCallBack;

    public void setIPlayerSettingCallBack(IPlayerSettingCallBack callBack) {

        this.mCallBack = callBack;
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
