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
import com.mojing.vrplayer.interfaces.IPlayerSettingCallBack;

import java.util.ArrayList;

/**
 * Created by yushaochen on 2017/5/16.
 */

public class ModeView2 extends GLRelativeView{

    private Context mContext;

    private int mItemWidth = 130;
    private int mItemHeigth = 80+15+35;

    private ArrayList<ModeItemInfo> originalDatas = new ArrayList<>();

    private ArrayList<ArrayList<ModeItemInfo>> datas = new ArrayList<>();

    private int COLUMN = 3;//列数定死

    private int ROW = 0;//行数，根据数据和列数计算得到

    public ModeView2(Context context) {
        super(context);
        mContext = context;
        //初始化数据
        initData();
        //处理数据
        handleData();

        setLayoutParams(mItemWidth*COLUMN+(COLUMN-1)*10,mItemHeigth*ROW+(ROW-1)*20);

        //创建所有选项
        createItemView();
    }

    private void initData() {
        ModeItemInfo modeItemInfo = new ModeItemInfo();
        modeItemInfo.setName("2D");
        modeItemInfo.setImage_no_focus_Str("play_icon_model_2d_normal");
        modeItemInfo.setImage_focused_Str("play_icon_model_2d_hover");
        modeItemInfo.setImage_click_Str("play_icon_model_2d_click");
        modeItemInfo.setImage_no_focus(R.drawable.play_icon_model_2d_normal);
        modeItemInfo.setImage_focused(R.drawable.play_icon_model_2d_hover);
        modeItemInfo.setImage_click(R.drawable.play_icon_model_2d_click);
        originalDatas.add(modeItemInfo);
        modeItemInfo = new ModeItemInfo();
        modeItemInfo.setName("3D左右");
        modeItemInfo.setImage_no_focus_Str("play_icon_model_3dside_normal");
        modeItemInfo.setImage_focused_Str("play_icon_model_3dside_hover");
        modeItemInfo.setImage_click_Str("play_icon_model_3dside_click");
        modeItemInfo.setImage_no_focus(R.drawable.play_icon_model_3dside_normal);
        modeItemInfo.setImage_focused(R.drawable.play_icon_model_3dside_hover);
        modeItemInfo.setImage_click(R.drawable.play_icon_model_3dside_click);
        originalDatas.add(modeItemInfo);
        modeItemInfo = new ModeItemInfo();
        modeItemInfo.setName("3D上下");
        modeItemInfo.setImage_no_focus_Str("play_icon_model_3dtop_normal");
        modeItemInfo.setImage_focused_Str("play_icon_model_3dtop_hover");
        modeItemInfo.setImage_click_Str("play_icon_model_3dtop_click");
        modeItemInfo.setImage_no_focus(R.drawable.play_icon_model_3dtop_normal);
        modeItemInfo.setImage_focused(R.drawable.play_icon_model_3dtop_hover);
        modeItemInfo.setImage_click(R.drawable.play_icon_model_3dtop_click);
        originalDatas.add(modeItemInfo);
        modeItemInfo = new ModeItemInfo();
        modeItemInfo.setName("平面");
        modeItemInfo.setImage_no_focus_Str("play_icon_model_plane_normal");
        modeItemInfo.setImage_focused_Str("play_icon_model_plane_hover");
        modeItemInfo.setImage_click_Str("play_icon_model_plane_click");
        modeItemInfo.setImage_no_focus(R.drawable.play_icon_model_plane_normal);
        modeItemInfo.setImage_focused(R.drawable.play_icon_model_plane_hover);
        modeItemInfo.setImage_click(R.drawable.play_icon_model_plane_click);
        originalDatas.add(modeItemInfo);
        modeItemInfo = new ModeItemInfo();
        modeItemInfo.setName("半球");
        modeItemInfo.setImage_no_focus_Str("play_icon_model_180_normal");
        modeItemInfo.setImage_focused_Str("play_icon_model_180_hover");
        modeItemInfo.setImage_click_Str("play_icon_model_180_click");
        modeItemInfo.setImage_no_focus(R.drawable.play_icon_model_180_normal);
        modeItemInfo.setImage_focused(R.drawable.play_icon_model_180_hover);
        modeItemInfo.setImage_click(R.drawable.play_icon_model_180_click);
        originalDatas.add(modeItemInfo);
        modeItemInfo = new ModeItemInfo();
        modeItemInfo.setName("球面");
        modeItemInfo.setImage_no_focus_Str("play_icon_model_360_normal");
        modeItemInfo.setImage_focused_Str("play_icon_model_360_hover");
        modeItemInfo.setImage_click_Str("play_icon_model_360_click");
        modeItemInfo.setImage_no_focus(R.drawable.play_icon_model_360_normal);
        modeItemInfo.setImage_focused(R.drawable.play_icon_model_360_hover);
        modeItemInfo.setImage_click(R.drawable.play_icon_model_360_click);
        originalDatas.add(modeItemInfo);
    }

    private void createItemView() {
        for(int x  = 0; x < datas.size(); x++) {//取行数
            ArrayList<ModeItemInfo> list = datas.get(x);
            for (int y = 0; y < list.size(); y++) {//取列数
                String name = list.get(y).getName();
                ModeItemView modeItemView = new ModeItemView(mContext);
                modeItemView.setId(name);
                modeItemView.setSelected(false);
                modeItemView.setData(list.get(y));
                modeItemView.setMargin(mItemWidth*y+y*10,mItemHeigth*x+x*43,0,0);
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
                                        setSelectedMode(view.getParent().getId());
                                        if(null != mCallBack) {

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
    }

    public void setSelectedMode(String name) {
        refreshItemView(name);
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

    private void handleData() {
        ArrayList<ModeItemInfo> list = null;
        for(int x = 0; x < originalDatas.size(); x++) {
            if(x % COLUMN == 0) {
                list = new ArrayList<>();
            }
            list.add(originalDatas.get(x));
            if(x == originalDatas.size() - 1) {
                datas.add(list);
            } else {
                if((x + 1)%COLUMN == 0) {
                    datas.add(list);
                }
            }
        }
        if(originalDatas.size() > COLUMN) {
            ROW = originalDatas.size() % COLUMN == 0 ? originalDatas.size() / COLUMN : originalDatas.size() / COLUMN + 1;
        } else {
            ROW = 1;
        }
    }

    private IPlayerSettingCallBack mCallBack;

    public void setIPlayerSettingCallBack(IPlayerSettingCallBack callBack) {

        this.mCallBack = callBack;
    }
}
