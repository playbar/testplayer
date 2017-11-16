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
import com.mojing.vrplayer.bean.SelectInfo;
import com.mojing.vrplayer.utils.BitmapUtil;
import com.mojing.vrplayer.utils.GLConst;
import com.mojing.vrplayer.utils.HeadControlUtil;
import com.mojing.vrplayer.utils.ViewUtil;

import java.util.List;

/**
 * Created by yushaochen on 2017/4/11.
 */

public class SelectBottomView extends GLRelativeView{

    private Context mContext;

    private int mWidth = 1000;
    private int mHeight = 75;

    private int mItemWidth = 150;
    private int mItemHeight = 45;

    private GLLinearView linearView;

    public SelectBottomView(Context context) {
        super(context);

        mContext = context;
        setLayoutParams(mWidth,mHeight);
        Bitmap bitmap = BitmapUtil.getBottomRoundBitmap(mWidth, mHeight, 20f, "#333333");
        setBackground(bitmap);

        linearView = new GLLinearView(mContext);
        addView(linearView);

    }

    public void initView(List<SelectInfo> infos) {
        resetView();
        if(null != infos && infos.size() > 0) {
            linearView.setLayoutParams(ViewUtil.getDip(mItemWidth*infos.size()+15*(infos.size()-1), GLConst.Player_Settings_Scale),
                    ViewUtil.getDip(mItemHeight,GLConst.Player_Settings_Scale));
            linearView.setMargin(ViewUtil.getDip(95f, GLConst.Player_Settings_Scale),ViewUtil.getDip(15f, GLConst.Player_Settings_Scale),0f,0f);
            for(int x = 0; x < infos.size(); x++) {
                GLTextView glTextView = new GLTextView(mContext);
                glTextView.setId(infos.get(x).getNums());
                glTextView.setSelected(false);
                glTextView.setLayoutParams(mItemWidth,mItemHeight);
                glTextView.setBackground(new GLColor(0,0,0,0));
                glTextView.setAlignment(GLTextView.ALIGN_CENTER);
                glTextView.setTextSize(24);
                glTextView.setTextColor(new GLColor(0x888888));
                glTextView.setText(infos.get(x).getNums());
                glTextView.setPadding(0f,7f,0f,0f);
                if(x != 0) {
                    glTextView.setMargin(15,0f,0f,0f);
                }
                glTextView.setFocusListener(new GLViewFocusListener() {
                    @Override
                    public void onFocusChange(GLRectView view, boolean focused) {
                        if(null != view) {
                            if(view instanceof GLTextView) {
                                if(!((GLTextView)view).isSelected()) {
                                    if(focused) {
                                        ((GLTextView)view).setTextColor(new GLColor(0xbbbbbb));
                                        ((GLTextView)view).setBackground("play_bg_control_hover_150_45");
                                    } else {
                                        ((GLTextView)view).setTextColor(new GLColor(0x888888));
                                        ((GLTextView)view).setBackground(new GLColor(0,0,0,0));
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
                                setSelectedItem(view.getId());
                                if(null != mCallBack) {
                                    mCallBack.onCallBack(view.getId());
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

    public void setSelectedItem(String nums) {
        refreshItemView(nums);
    }

    private void refreshItemView(String nums) {
        if(!TextUtils.isEmpty(nums)) {
            clearSelected();
            GLRectView view = linearView.getView(nums);
            if(view instanceof GLTextView) {
                ((GLTextView) view).setBackground("play_bg_control_normal_150_45");
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
                ((GLTextView) childView).setBackground(new GLColor(0,0,0,0));
            }
        }
    }

    private void resetView() {
        linearView.removeAllView();
    }

    private SelectCallBack mCallBack;

    public void setSelectCallBack(SelectCallBack callBack) {

        this.mCallBack = callBack;
    }

    public interface SelectCallBack {
        void onCallBack(String nums);
    }

}
