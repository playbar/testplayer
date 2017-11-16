package com.mojing.vrplayer.view;

import android.content.Context;
import android.text.TextUtils;

import com.baofeng.mojing.input.base.MojingKeyCode;
import com.bfmj.viewcore.interfaces.GLOnKeyListener;
import com.bfmj.viewcore.interfaces.GLViewFocusListener;
import com.bfmj.viewcore.render.GLColor;
import com.bfmj.viewcore.view.GLRectView;
import com.bfmj.viewcore.view.GLRelativeView;
import com.bfmj.viewcore.view.GLTextView;
import com.mojing.vrplayer.R;
import com.mojing.vrplayer.interfaces.IPlayerSettingCallBack;
import com.mojing.vrplayer.utils.GLConst;
import com.mojing.vrplayer.utils.HeadControlUtil;

import java.util.ArrayList;

/**
 * Created by yushaochen on 2017/5/16.
 */

public class RatioView extends GLRelativeView{

    private Context mContext;

    private int mItemWidth = 130;
    private int mItemHeigth = 60;

    private String[] strs = new String[]{"原始","16:9","4:3","16:10","3:2","2.35:1"};

    private ArrayList<ArrayList<String>> datas = new ArrayList<>();

    private int COLUMN = 3;//列数定死

    private int ROW = 0;//行数，根据数据和列数计算得到

    private int mType = 1;

    public RatioView(Context context) {
        super(context);
        mContext = context;
        //处理数据
        handleData();

        setLayoutParams(mItemWidth*COLUMN+(COLUMN-1)*10,mItemHeigth*ROW+(ROW-1)*20);

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

    private void createItemView() {
        for(int x  = 0; x < datas.size(); x++) {//取行数
            ArrayList<String> list = datas.get(x);
            for (int y = 0; y < list.size(); y++) {//取列数
                String name = list.get(y);
                GLTextView textView = new GLTextView(mContext);
                textView.setId(name);
                textView.setSelected(false);
                textView.setLayoutParams(mItemWidth,mItemHeigth);
                textView.setBackground("play_bg_control_normal_130_60");
                textView.setAlignment(GLTextView.ALIGN_CENTER);
                textView.setTextSize(28);
                textView.setTextColor(new GLColor(0x888888));
                textView.setText(name);
                textView.setPadding(0f,12f,0f,0f);
                textView.setMargin(mItemWidth*y+y*10,mItemHeigth*x+x*20,0,0);
                textView.setFocusListener(new GLViewFocusListener() {
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
                textView.setOnKeyListener(new GLOnKeyListener() {
                    @Override
                    public boolean onKeyDown(GLRectView view, int keycode) {
                        switch (keycode) {
                            case MojingKeyCode.KEYCODE_ENTER:
                                if(mType == GLConst.LOCAL_PANO) return false;
                                setSelectedRatio(view.getId());
                                if(null != mCallBack) {
                                    mCallBack.onRatioChange(view.getId());
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
                addView(textView);
                HeadControlUtil.bindView(textView);
            }
        }
    }

    public void setSelectedRatio(String name) {
        if(mType == GLConst.LOCAL_PANO) return;
        refreshItemView(name);
    }

    private void refreshItemView(String name) {
        if(!TextUtils.isEmpty(name)) {
            clearSelected();
            GLRectView view = getView(name);
            if(view instanceof GLTextView) {
                ((GLTextView) view).setBackground("play_bg_control_normal_130_60");
                ((GLTextView) view).setTextColor(new GLColor(0x008cb3));
            }
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

    private void handleData() {
        ArrayList<String> list = null;
        for(int x = 0; x < strs.length; x++) {
            if(x % COLUMN == 0) {
                list = new ArrayList<>();
            }
            list.add(strs[x]);
            if(x == strs.length - 1) {
                datas.add(list);
            } else {
                if((x + 1)%COLUMN == 0) {
                    datas.add(list);
                }
            }
        }
        if(strs.length > COLUMN) {
            ROW = strs.length % COLUMN == 0 ? strs.length / COLUMN : strs.length / COLUMN + 1;
        } else {
            ROW = 1;
        }
    }

    private IPlayerSettingCallBack mCallBack;

    public void setIPlayerSettingCallBack(IPlayerSettingCallBack callBack) {

        this.mCallBack = callBack;
    }
}
