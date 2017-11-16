package com.mojing.vrplayer.page;

import android.content.Context;

import com.baofeng.mojing.input.base.MojingKeyCode;
import com.bfmj.viewcore.interfaces.GLOnKeyListener;
import com.bfmj.viewcore.util.GLExtraData;
import com.bfmj.viewcore.view.GLRectView;
import com.bfmj.viewcore.view.GLRelativeView;
import com.bfmj.viewcore.view.GLViewPage;
import com.mojing.vrplayer.activity.GLBaseActivity;
import com.mojing.vrplayer.activity.MjVrPlayerActivity;
import com.mojing.vrplayer.publicc.SettingSpBusiness;
import com.mojing.vrplayer.publicc.StickUtil;
import com.mojing.vrplayer.view.SplashSkyboxView;
import com.mojing.vrplayer.view.StickIntroductionView;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by yushaochen on 2017/4/5.
 */

public class SplashPage extends GLViewPage {

    private Context mContext;
    private GLRelativeView indexRootView;

    private SplashSkyboxView skyboxView;

    private StickIntroductionView stickIntroductionView;

    public SplashPage(Context context) {
        super(context);
        mContext = context;
    }

    @Override
    protected GLRectView createView(GLExtraData data) {
        int skyBoxtype = SettingSpBusiness.getInstance(mContext).getSkyboxIndex();
        ((GLBaseActivity)mContext).showSkyBox(skyBoxtype);
        indexRootView = new GLRelativeView(mContext);
        indexRootView.setLayoutParams(GLRectView.MATCH_PARENT,
                GLRectView.MATCH_PARENT);
//        indexRootView.setBackground(new GLColor(0x414141));
//        Log.d("cursor","--------showCursorView---SplashPage");
//        Log.d("splash", "!!!!!!!!!!!----------createView");
        //显示光标
        ((GLBaseActivity)getContext()).showCursorView();
        //隐藏底部复位
        ((GLBaseActivity)getContext()).hideResetLayerView();

        //创建手柄说明view
        createStickView();
        //创建中间场景选择
        createSkyboxView();
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                //获取当前连接的手柄类型
                checkStickMode();
            }
        },2000);

        return indexRootView;
    }

    @Override
    public void onResume() {
        super.onResume();
//        Log.d("splash", "!!!!!!!!!!!----------onResume:"+SettingSpBusiness.getInstance().getVrGuide());
        if(SettingSpBusiness.getInstance(mContext).getVrGuide()) {
            if(null != skyboxView) {
                skyboxView.setVisible(false);
            }
        } else {
            if(null != skyboxView) {
                skyboxView.setVisible(false);
            }
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    //获取当前连接的手柄类型
                    checkStickMode();
                }
            },2000);
        }
    }

    private void checkStickMode() {
//        Log.d("splash", "!!!!!!!!!!!----------checkStickMode:"+StickUtil.getConnectStatus());
        if(StickUtil.getConnectStatus() == 0) {
            setNoConnected();
        } else if(StickUtil.getConnectStatus() == 1) {
            setWiiStick();
        } else if(StickUtil.getConnectStatus() == 2) {
            setNoWiiStick();
        }

//        if (BaseApplication.INSTANCE.isBFMJ5Connection() && BaseApplication.INSTANCE.getJoystickConnect()) { //魔镜5代usb连接，并且遥控器连接上
//            if(!TextUtils.isEmpty(BaseApplication.INSTANCE.getJoystickName())) {
//                if(BaseApplication.INSTANCE.getJoystickName().equals("体感手柄")) {
//                    setWiiStick();
//                } else {
//                    setNoWiiStick();
//                }
//            }
//        } else if (!StickUtil.blutoothEnble()) {// 蓝牙关闭
//            setNoConnected();
//        } else if (!StickUtil.isBondBluetooth()) {// 蓝牙与魔镜设备未配对
//            setNoConnected();
//        } else if (!StickUtil.isConnected) {// 设备未开启或者设备休眠
//            setNoConnected();
//        } else {// 已连接
//            if(!TextUtils.isEmpty(BaseApplication.INSTANCE.getJoystickName())) {
//                if(BaseApplication.INSTANCE.getJoystickName().equals("体感手柄")) {
//                    setWiiStick();
//                } else {
//                    setNoWiiStick();
//                }
//            }
//        }

//        if(StickUtil.isConnected) {// 手柄连接
//            if(!TextUtils.isEmpty(BaseApplication.INSTANCE.getJoystickName())) {
//                if(BaseApplication.INSTANCE.getJoystickName().equals("体感手柄")) {//体感手柄
//                    stickIntroductionView.setVisible(true);
//                    stickIntroductionView.setStickMode(StickIntroductionView.WII_STICK);
//                    ((GLBaseActivity)getContext()).hideSkyBox();
//                    skyboxView.setVisible(false);
//                } else {//普通手柄
//                    stickIntroductionView.setVisible(true);
//                    stickIntroductionView.setStickMode(StickIntroductionView.NO_WII_STICK);
//                    ((GLBaseActivity)getContext()).hideSkyBox();
//                    skyboxView.setVisible(false);
//                }
//            }
//        } else {// 手柄未连接
//            stickIntroductionView.setVisible(false);
//            ((GLBaseActivity)getContext()).showSkyBox(SettingSpBusiness.getInstance().getSkyboxIndex());
//            skyboxView.setVisible(true);
//        }

    }

    /**
     * 普通手柄连接
     */
    public void setNoWiiStick() {
        stickIntroductionView.setVisible(true);
        stickIntroductionView.setStickMode(StickIntroductionView.NO_WII_STICK);
        ((GLBaseActivity)getContext()).hideSkyBox();
        skyboxView.setVisible(false);
    }
    /**
     * 体感手柄连接
     */
    public void setWiiStick() {
        stickIntroductionView.setVisible(true);
        stickIntroductionView.setStickMode(StickIntroductionView.WII_STICK);
        ((GLBaseActivity)getContext()).hideSkyBox();
        skyboxView.setVisible(false);
    }
    /**
     * 无手柄连接
     */
    public void setNoConnected() {
        stickIntroductionView.setVisible(false);
        ((GLBaseActivity)getContext()).showSkyBox(SettingSpBusiness.getInstance(getContext()).getSkyboxIndex());
        skyboxView.setVisible(true);
    }

    private void createStickView() {
        stickIntroductionView = new StickIntroductionView(mContext);
        stickIntroductionView.setMargin(1200f-480f,(1200f-(700f+70f+70f)/2),0f,0f);
        stickIntroductionView.setVisible(false);
        stickIntroductionView.setOkKeyListener(new GLOnKeyListener() {
            @Override
            public boolean onKeyDown(GLRectView view, int keycode) {
                switch (keycode) {
                    case MojingKeyCode.KEYCODE_ENTER:
                        stickIntroductionView.setVisible(false);
//                        Log.d("splash", "!!!!!!!!!!!----------createStickView");
                        skyboxView.setVisible(true);
                        ((GLBaseActivity)getContext()).showSkyBox(SettingSpBusiness.getInstance(mContext).getSkyboxIndex());
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
//        stickIntroductionView.setFocusListener(focusListener);
        indexRootView.addView(stickIntroductionView);
    }

    private void createSkyboxView() {
        skyboxView = new SplashSkyboxView(mContext);
        skyboxView.setMargin(1200f-500f,(1200f-(60 + 70 + 284 + 70 + 70)/2),0f,0f);
        skyboxView.setVisible(false);
//        skyboxView.setBackground(new GLColor(0xff0000));
        skyboxView.setOkKeyListener(new GLOnKeyListener() {
            @Override
            public boolean onKeyDown(GLRectView view, int keycode) {
                switch (keycode) {
                    case MojingKeyCode.KEYCODE_ENTER:
//                        ((GLBaseActivity)getContext()).runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                Log.d("splash", "!!!!!!!!!!!----------createSkyboxView");
                                //引导结束，隐藏选择场景
                                skyboxView.setVisible(false);

                                //关闭当前页面
                                finish();

                                SettingSpBusiness.getInstance(mContext).setVrGuide(true);

                                //开启播放页
                                ((MjVrPlayerActivity) getContext()).startPlayPage();
//                            }
//                        });
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
        indexRootView.addView(skyboxView);
    }
}
