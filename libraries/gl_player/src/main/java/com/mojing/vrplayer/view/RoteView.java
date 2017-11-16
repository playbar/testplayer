package com.mojing.vrplayer.view;

import android.content.Context;
import android.text.TextUtils;

import com.baofeng.mojing.input.base.MojingKeyCode;
import com.bfmj.viewcore.interfaces.GLOnKeyListener;
import com.bfmj.viewcore.interfaces.GLViewFocusListener;
import com.bfmj.viewcore.render.GLColor;
import com.bfmj.viewcore.view.GLLinearView;
import com.bfmj.viewcore.view.GLRectView;
import com.bfmj.viewcore.view.GLTextView;
import com.mojing.vrplayer.R;
import com.mojing.vrplayer.bean.VideoModeType;
import com.mojing.vrplayer.interfaces.IPlayerSettingCallBack;
import com.mojing.vrplayer.utils.GLConst;
import com.mojing.vrplayer.utils.HeadControlUtil;

/**
 * Created by yushaochen on 2017/5/16.
 */

public class RoteView extends GLLinearView{

    private Context mContext;

    private int mItemWidth = 130;
    private int mItemHeight = 60;

    private String[] datas = new String[]{"顺时针","逆时针"};

    private int mType = 1;

    public RoteView(Context context) {
        super(context);

        mContext = context;
        setLayoutParams(mItemWidth*datas.length+(datas.length-1)*10,mItemHeight);
        //创建所有选项
        createItemView();
    }

    public void setType(int type) {
        mType = type;
        if(type == GLConst.LOCAL_MOVIE) {
            setItemClickable(true);
        } else if(type == GLConst.LOCAL_PANO) {
            setItemClickable(false);
        }
    }

    public void setItemClickable(boolean clickable) {
        for(GLRectView childView : getChildView()) {
            if(childView instanceof GLTextView) {
                if(clickable) {
                    ((GLTextView) childView).setTextColor(new GLColor(0x888888));
                    HeadControlUtil.bindView((GLTextView) childView);
                } else {
                    ((GLTextView) childView).setTextColor(new GLColor(0x444444));
                    HeadControlUtil.unbindView((GLTextView) childView);
                }
            }
        }
    }

    public void createItemView() {
            for(int x = 0; x < datas.length; x++) {
                GLTextView glTextView = new GLTextView(mContext);
                glTextView.setId(datas[x]);
                glTextView.setSelected(false);
                glTextView.setLayoutParams(mItemWidth,mItemHeight);
                glTextView.setBackground("play_bg_control_normal_130_60");
                glTextView.setAlignment(GLTextView.ALIGN_CENTER);
                glTextView.setTextSize(28);
                glTextView.setTextColor(new GLColor(0x888888));
                glTextView.setText(datas[x]);
                glTextView.setPadding(0f,12f,0f,0f);
                if(x != 0) {
                    glTextView.setMargin(10f,0f,0f,0f);
                }
                glTextView.setFocusListener(new GLViewFocusListener() {
                    @Override
                    public void onFocusChange(GLRectView view, boolean focused) {
                        if(mType == GLConst.LOCAL_PANO) return;
                        if(null != view) {
                            if(view instanceof GLTextView) {
                                if(!((GLTextView)view).isSelected()) {
                                    if(focused) {
                                        ((GLTextView)view).setTextColor(new GLColor(0xbbbbbb));
                                        ((GLTextView)view).setBackground("play_bg_control_hover_130_60");
                                    } else {
                                        ((GLTextView)view).setTextColor(new GLColor(0x888888));
                                        ((GLTextView)view).setBackground("play_bg_control_normal_130_60");
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
                                if(mType == GLConst.LOCAL_PANO) return false;
                                setSelectedRote(view.getId());
                                if(null != mCallBack) {
                                    mCallBack.onRoteChange(view.getId().equals(datas[0])? VideoModeType.Mode_CW_Rotate:VideoModeType.Mode_CCW_Rotate);
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
                addView(glTextView);
                HeadControlUtil.bindView(glTextView);
            }
    }

    public void setSelectedRote(String rote) {
        if(mType == GLConst.LOCAL_PANO) return;
        refreshItemView(rote);
    }

    private void refreshItemView(String rote) {
        if(!TextUtils.isEmpty(rote)) {
            clearSelected();
            GLRectView view = getView(rote);
//            if(view instanceof GLTextView) {
//                ((GLTextView) view).setBackground(R.drawable.play_bg_control_normal_130_60);
//                ((GLTextView) view).setTextColor(new GLColor(0x008cb3));
//            }
            ((GLTextView) view).setSelected(true);
        }

    }

    private void clearSelected() {
        for(GLRectView childView : getChildView()) {
            if(childView instanceof GLTextView) {
                ((GLTextView) childView).setSelected(false);
                ((GLTextView) childView).setTextColor(new GLColor(0x888888));
                ((GLTextView) childView).setBackground("play_bg_control_normal_130_60");
            }
        }
    }

    private IPlayerSettingCallBack mCallBack;

    public void setIPlayerSettingCallBack(IPlayerSettingCallBack callBack) {

        this.mCallBack = callBack;
    }
}
