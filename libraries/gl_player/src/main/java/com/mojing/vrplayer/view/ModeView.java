package com.mojing.vrplayer.view;

import android.content.Context;
import android.text.TextUtils;

import com.baofeng.mojing.input.base.MojingKeyCode;
import com.bfmj.viewcore.interfaces.GLOnKeyListener;
import com.bfmj.viewcore.interfaces.GLViewFocusListener;
import com.bfmj.viewcore.view.GLRectView;
import com.bfmj.viewcore.view.GLRelativeView;
import com.mojing.vrplayer.R;
import com.mojing.vrplayer.bean.ModeItemInfo;
import com.mojing.vrplayer.bean.VideoModeType;
import com.mojing.vrplayer.interfaces.IPlayerSettingCallBack;

import java.util.ArrayList;

/**
 * Created by yushaochen on 2017/5/16.
 */

public class ModeView extends GLRelativeView{

    private Context mContext;

    private int mItemWidth = 130;
    private int mItemHeigth = 80+15+35;

    public static final int LEFT_RIGHT_MODE = 0;
    public static final int ROUND_MODE = 1;

    private ArrayList<ModeItemInfo> datas = new ArrayList<>();

    private int mType = 0;

    public ModeView(Context context) {
        super(context);
        mContext = context;
    }
    public void initView(int type) {
        mType = type;
        //初始化数据
        if(type == LEFT_RIGHT_MODE) {
            initLeftRightData();
        } else if(type == ROUND_MODE) {
            initRoundData();
        }

        setLayoutParams(mItemWidth*datas.size()+(datas.size()-1)*10,mItemHeigth);
        //创建所有选项
        createItemView();
    }

    private void initRoundData() {
        ModeItemInfo modeItemInfo = new ModeItemInfo();
        modeItemInfo.setName("平面");
        modeItemInfo.setImage_no_focus(R.drawable.play_icon_model_plane_normal);
        modeItemInfo.setImage_focused(R.drawable.play_icon_model_plane_hover);
        modeItemInfo.setImage_click(R.drawable.play_icon_model_plane_click);
        modeItemInfo.setImage_no_focus_Str("play_icon_model_plane_normal");
        modeItemInfo.setImage_focused_Str("play_icon_model_plane_hover");
        modeItemInfo.setImage_click_Str("play_icon_model_plane_click");
        modeItemInfo.setValue(VideoModeType.Mode_Rect);
        datas.add(modeItemInfo);
        modeItemInfo = new ModeItemInfo();
        modeItemInfo.setName("半球");
        modeItemInfo.setImage_no_focus(R.drawable.play_icon_model_180_normal);
        modeItemInfo.setImage_focused(R.drawable.play_icon_model_180_hover);
        modeItemInfo.setImage_click(R.drawable.play_icon_model_180_click);
        modeItemInfo.setImage_no_focus_Str("play_icon_model_180_normal");
        modeItemInfo.setImage_focused_Str("play_icon_model_180_hover");
        modeItemInfo.setImage_click_Str("play_icon_model_180_click");
        modeItemInfo.setValue(VideoModeType.Mode_Sphere180);
        datas.add(modeItemInfo);
        modeItemInfo = new ModeItemInfo();
        modeItemInfo.setName("球面");
        modeItemInfo.setImage_no_focus(R.drawable.play_icon_model_360_normal);
        modeItemInfo.setImage_focused(R.drawable.play_icon_model_360_hover);
        modeItemInfo.setImage_click(R.drawable.play_icon_model_360_click);
        modeItemInfo.setImage_no_focus_Str("play_icon_model_360_normal");
        modeItemInfo.setImage_focused_Str("play_icon_model_360_hover");
        modeItemInfo.setImage_click_Str("play_icon_model_360_click");
        modeItemInfo.setValue(VideoModeType.Mode_Sphere360);
        datas.add(modeItemInfo);
    }

    private void initLeftRightData() {
        ModeItemInfo modeItemInfo = new ModeItemInfo();
        modeItemInfo.setName("2D");
        modeItemInfo.setImage_no_focus(R.drawable.play_icon_model_2d_normal);
        modeItemInfo.setImage_focused(R.drawable.play_icon_model_2d_hover);
        modeItemInfo.setImage_click(R.drawable.play_icon_model_2d_click);
        modeItemInfo.setImage_no_focus_Str("play_icon_model_2d_normal");
        modeItemInfo.setImage_focused_Str("play_icon_model_2d_hover");
        modeItemInfo.setImage_click_Str("play_icon_model_2d_click");
        modeItemInfo.setValue(VideoModeType.Video2D);
        datas.add(modeItemInfo);
        modeItemInfo = new ModeItemInfo();
        modeItemInfo.setName("3D左右");
        modeItemInfo.setImage_no_focus(R.drawable.play_icon_model_3dside_normal);
        modeItemInfo.setImage_focused(R.drawable.play_icon_model_3dside_hover);
        modeItemInfo.setImage_click(R.drawable.play_icon_model_3dside_click);
        modeItemInfo.setImage_no_focus_Str("play_icon_model_3dside_normal");
        modeItemInfo.setImage_focused_Str("play_icon_model_3dside_hover");
        modeItemInfo.setImage_click_Str("play_icon_model_3dside_click");
        modeItemInfo.setValue(VideoModeType.VideoLR3D);
        datas.add(modeItemInfo);
        modeItemInfo = new ModeItemInfo();
        modeItemInfo.setName("3D上下");
        modeItemInfo.setImage_no_focus(R.drawable.play_icon_model_3dtop_normal);
        modeItemInfo.setImage_focused(R.drawable.play_icon_model_3dtop_hover);
        modeItemInfo.setImage_click(R.drawable.play_icon_model_3dtop_click);
        modeItemInfo.setImage_no_focus_Str("play_icon_model_3dtop_normal");
        modeItemInfo.setImage_focused_Str("play_icon_model_3dtop_hover");
        modeItemInfo.setImage_click_Str("play_icon_model_3dtop_click");
        modeItemInfo.setValue(VideoModeType.VideoUD3D);
        datas.add(modeItemInfo);
    }

    private void createItemView() {
        removeAllView();
        for(int x  = 0; x < datas.size(); x++) {
                String name = datas.get(x).getName();
                ModeItemView modeItemView = new ModeItemView(mContext);
                modeItemView.setId(name);
                modeItemView.setSelected(false);
                modeItemView.setData(datas.get(x));
                modeItemView.setMargin(mItemWidth*x+x*10,0,0,0);
                modeItemView.setImageFocusListener(new GLViewFocusListener() {
                    @Override
                    public void onFocusChange(GLRectView view, boolean focused) {
                        if(null != view) {
                            if(view.getParent() instanceof ModeItemView) {
                                if(!((ModeItemView)view.getParent()).isSelected()) {
                                    if(focused) {
                                        ((ModeItemView)view.getParent()).setItemFocuse(focused);
                                    } else {
                                        ((ModeItemView)view.getParent()).setItemFocuse(focused);
                                    }
                                }
                            }
                        }
                    }
                });
                modeItemView.setImageKeyListener(new GLOnKeyListener() {
                    @Override
                    public boolean onKeyDown(GLRectView view, int keycode) {
                        switch (keycode) {
                            case MojingKeyCode.KEYCODE_ENTER:
                                if(null != view) {
                                    if(view.getParent() instanceof ModeItemView) {
                                        setSelectedMode(((ModeItemView)(view.getParent())).getInfo().getValue());
                                        if(null != mCallBack) {
                                            if(mType == LEFT_RIGHT_MODE) {
                                                mCallBack.onLeftRightModeChange(((ModeItemView)(view.getParent())).getInfo().getValue());
                                            } else if(mType == ROUND_MODE) {
                                                mCallBack.onRoundModeChange(((ModeItemView)(view.getParent())).getInfo().getValue());
                                            }
                                        }
                                    }
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
                addView(modeItemView);
        }
    }

    public void setSelectedMode(int type) {
        if(datas==null)
            return;
        String name = null;
        for (ModeItemInfo itemInfo:datas){
            if(type==itemInfo.getValue()){
                name = itemInfo.getName();
                break;
            }
        }
        if(name!=null) {
            refreshItemView(name);
        }
    }

    private void refreshItemView(String name) {
        if(!TextUtils.isEmpty(name)) {
            clearSelected();
            GLRectView view = getView(name);
            if(null != view) {
                if(view instanceof ModeItemView) {
                    ((ModeItemView) view).setItemClick();
                }
                ((ModeItemView) view).setSelected(true);
            }
        }

    }

    private void clearSelected() {
        for(GLRectView childView : getChildView()) {
            if(null != childView) {
                if(childView instanceof ModeItemView) {
                    ((ModeItemView) childView).setSelected(false);
                    ((ModeItemView) childView).setItemFocuse(false);
                }
            }
        }
    }

    private IPlayerSettingCallBack mCallBack;

    public void setIPlayerSettingCallBack(IPlayerSettingCallBack callBack) {

        this.mCallBack = callBack;
    }
}
