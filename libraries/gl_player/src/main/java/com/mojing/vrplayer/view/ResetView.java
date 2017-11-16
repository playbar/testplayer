package com.mojing.vrplayer.view;

import android.content.Context;

import com.baofeng.mojing.input.base.MojingKeyCode;
import com.bfmj.viewcore.animation.GLAlphaAnimation;
import com.bfmj.viewcore.animation.GLAnimation;
import com.bfmj.viewcore.animation.GLTranslateAnimation;
import com.bfmj.viewcore.interfaces.GLOnKeyListener;
import com.bfmj.viewcore.interfaces.GLViewFocusListener;
import com.bfmj.viewcore.view.GLRectView;
import com.mojing.vrplayer.R;
import com.mojing.vrplayer.activity.GLBaseActivity;
import com.mojing.vrplayer.activity.MjVrPlayerActivity;
import com.mojing.vrplayer.interfaces.IResetLayerCallBack;
import com.mojing.vrplayer.publicc.ReportBusiness;
import com.mojing.vrplayer.utils.GLConst;
import com.mojing.vrplayer.utils.HeadControlUtil;
import com.mojing.vrplayer.utils.ViewUtil;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by yushaochen on 2017/4/3.
 */

public class ResetView extends ResetRelativeLayout {
    private Context mContext;
//    private float mDepth = 3.8f;
    private float mWidth = 550f;
    private float mHeight = 140f;

    public ResetView(Context context) {
        super(context);
        mContext = context;
        setCostomHeadView(true);
//        setDepth(mDepth);
//        setBackground(new GLColor(0xffffff));
        //创建返回按钮
        createBackBtn();
        //创建复位按钮
        createResetBtn();
        //创建加号按钮
        createAddBtn();

    }

    private ResetButton resetBtn;

    private void createResetBtn() {
        resetBtn = new ResetButton(mContext);
//        resetBtn.setDepth(mDepth);
//        resetBtn.setMargin((mWidth-150f)/2+200f,0f,0f,0f);
        resetBtn.setMargin((mWidth-150f)/2,0f,0f,0f);
        resetBtn.setImageBg("play_menu_button_view_normal");
//        addBtn.setBackground(new GLColor(0xffffff));
        resetBtn.setText("视角复位");
        resetBtn.setVisible(false);
        resetBtn.setImageFocusListener(new GLViewFocusListener() {
            @Override
            public void onFocusChange(GLRectView glRectView, boolean b) {
                if(b) {
                    resetBtn.setImageBg("play_menu_button_view_hover");
                    resetBtn.setTextVisible(true);
                } else {
                    resetBtn.setImageBg("play_menu_button_view_normal");
                    resetBtn.setTextVisible(false);
                }
            }
        });
        resetBtn.setImageKeyListener(new GLOnKeyListener() {
            @Override
            public boolean onKeyDown(GLRectView view, int keycode) {
                switch (keycode) {
                    case MojingKeyCode.KEYCODE_ENTER:
                        if(ResetView.this.isFixed()){
                            return false;
                        }
                        ((GLBaseActivity)mContext).initHeadView();
                        if(null != mCallBack) {
                            mCallBack.onResetView();
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
      //  addView(resetBtn);
    }

    private ResetButton addBtn;

    private void createAddBtn() {
        addBtn = new ResetButton(mContext);
        HeadControlUtil.bindView(addBtn.getImageView(),0,0);
//        addBtn.setDepth(mDepth);
        addBtn.setMargin((mWidth-150f)/2,0f,0f,0f);
        addBtn.setImageBg("play_menu_button_launch_normal");
//        addBtn.setBackground(new GLColor(0xffffff));
        addBtn.setText("展开菜单");
        addBtn.setSelected(false);
        addBtn.setImageFocusListener(new GLViewFocusListener() {
            @Override
            public void onFocusChange(GLRectView glRectView, boolean b) {
                if(b) {
                    addBtn.setImageBg("play_menu_button_launch_hover");
                    addBtn.setTextVisible(true);
                } else {
                    addBtn.setImageBg("play_menu_button_launch_normal");
                    addBtn.setTextVisible(false);
                }
            }
        });
        addBtn.setImageKeyListener(new GLOnKeyListener() {
            @Override
            public boolean onKeyDown(GLRectView view, int keycode) {
                switch (keycode) {
                    case MojingKeyCode.KEYCODE_ENTER:
                        if(!addBtn.isSelected()) {
                            addBtn.setText("关闭菜单");
                            addBtn.startTranslate(true);
                          //  resetBtn.setVisible(true);
                            backBtn.setVisible(true);
//                            setTranslateAnimation(resetBtn, ViewUtil.getDip(200, GLConst.Bottom_Menu_Scale),0,0);
//                            startGLAlphaAnimation(resetBtn,0,1);
                            setTranslateAnimation(backBtn,-ViewUtil.getDip(200, GLConst.Bottom_Menu_Scale),0,0);
                            startGLAlphaAnimation(backBtn,0,1);
                            addBtn.setSelected(true);
                            //报数
                            reportEvent();
                        } else {
                            addBtn.setText("展开菜单");
                            addBtn.startTranslate(false);
//                            setTranslateAnimation(resetBtn,-ViewUtil.getDip(200, GLConst.Bottom_Menu_Scale),0,0);
//                            startGLAlphaAnimation(resetBtn,1,0);
                            //        resetBtn.setVisible(false);
                            setTranslateAnimation(backBtn,ViewUtil.getDip(200, GLConst.Bottom_Menu_Scale),0,0);
                            startGLAlphaAnimation(backBtn,1,0);
                            //        backBtn.setVisible(false);
                            addBtn.setSelected(false);
                            //保证关闭动画完成，再隐藏view，否则view不能到指定位置
                            new Timer().schedule(new TimerTask() {
                                @Override
                                public void run() {
//                                    resetBtn.setVisible(false);
                                    backBtn.setVisible(false);
                                }
                            },300);
                        }
                        if(null != mCallBack) {
                            mCallBack.isOpen(addBtn.isSelected());
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
       // addView(addBtn);
    }

    public void showOpen(boolean isEnter) {
        if(isEnter && addBtn.isFocused()) return;
        if(!addBtn.isSelected()) {
            addBtn.setText("关闭菜单");
            addBtn.startTranslate(true);
//            resetBtn.setVisible(true);
//            setTranslateAnimation(resetBtn, ViewUtil.getDip(200, GLConst.Bottom_Menu_Scale),0,0);
//            startGLAlphaAnimation(resetBtn,0,1);
            backBtn.setVisible(true);
            setTranslateAnimation(backBtn,-ViewUtil.getDip(200, GLConst.Bottom_Menu_Scale),0,0);
            startGLAlphaAnimation(backBtn,0,1);
            addBtn.setSelected(true);
        }
    }

    public void showClose(boolean isEnter) {
        if(isEnter && addBtn.isFocused()) return;
        if(addBtn.isSelected()) {
            addBtn.setText("展开菜单");
            addBtn.startTranslate(false);
//            resetBtn.setX(addBtn.getX());
//            resetBtn.setVisible(false);
            backBtn.setX(addBtn.getX());
            backBtn.setVisible(false);
            addBtn.setSelected(false);
        }
    }

    private ResetButton backBtn;

    private void createBackBtn() {
        backBtn = new ResetButton(mContext);
//        backBtn.setDepth(mDepth);
//        backBtn.setMargin((mWidth-150f)/2-200f,0f,0f,0f);
        backBtn.setMargin((mWidth-150f)/2,0f,0f,0f);
        backBtn.setImageBg("play_menu_button_quit_normal");
//        addBtn.setBackground(new GLColor(0xffffff));
        backBtn.setText("退出播放");
        backBtn.setVisible(false);
        backBtn.setImageFocusListener(new GLViewFocusListener() {
            @Override
            public void onFocusChange(GLRectView glRectView, boolean b) {
                if(b) {
                    backBtn.setImageBg("play_menu_button_quit_hover");
                    backBtn.setTextVisible(true);
                } else {
                    backBtn.setImageBg("play_menu_button_quit_normal");
                    backBtn.setTextVisible(false);
                }
            }
        });
        backBtn.setImageKeyListener(new GLOnKeyListener() {
            @Override
            public boolean onKeyDown(GLRectView view, int keycode) {
                switch (keycode) {
                    case MojingKeyCode.KEYCODE_ENTER:
                        ((MjVrPlayerActivity)mContext).finish();
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
        //addView(backBtn);
    }

    private IResetLayerCallBack mCallBack;

    public void setIResetLayerCallBack(IResetLayerCallBack callBack) {
        mCallBack = callBack;
    }

    public void setTranslateAnimation(GLRectView view, final float x, float y,float z){
        if(view == null)
            return;
        GLAnimation animation = new GLTranslateAnimation(x, y, z);
        animation.setAnimView(view);
        animation.setDuration(83);
        view.startAnimation(animation);
    }

    public void startGLAlphaAnimation(GLRectView view, float startAlpha, float endAlpha) {
        if (view == null)
            return;
        GLAnimation animation = new GLAlphaAnimation(startAlpha,endAlpha);
        animation.setAnimView(view);
        animation.setDuration(83);
        view.startAnimation(animation);
    }

    @Override
    public void setResetFixed(boolean resetFixed) {
        super.setResetFixed(resetFixed);
    }

    public void reportEvent() {
        HashMap<String,String> params = new HashMap<>();
        params.put("etype","show");
        int type = ((GLBaseActivity)getContext()).getCurPageType();
        if(type == GLConst.MOVIE || type == GLConst.LOCAL_MOVIE) {
            params.put("sensemode","commmovie");
        } else if(type == GLConst.PANO || type == GLConst.LOCAL_PANO) {
            params.put("sensemode","360video");
        }
        params.put("showtype","1");
        params.put("popuptype","1");
        ReportBusiness.getInstance().reportClick(params);
    }
}
