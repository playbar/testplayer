package com.mojing.vrplayer.view;

import android.content.Context;

import com.bfmj.viewcore.interfaces.GLViewFocusListener;
import com.bfmj.viewcore.render.GLColor;
import com.bfmj.viewcore.render.GLConstant;
import com.bfmj.viewcore.view.GLImageView;
import com.bfmj.viewcore.view.GLLinearView;
import com.bfmj.viewcore.view.GLRectView;
import com.bfmj.viewcore.view.GLTextView;
import com.mojing.vrplayer.R;

/**
 * Created by yushaochen on 2017/4/7.
 */

public class SettingBottomRightView extends GLLinearView{

    private Context mContext;

    private GLImageView icon;
    private GLTextView textView;
    private GLImageView rightIcon;
    private GLImageView leftIcon;

    public SettingBottomRightView(Context context) {
        super(context);
        mContext = context;
        setOrientation(GLConstant.GLOrientation.HORIZONTAL);
        setLayoutParams(890,80);
        setMargin(0,20,0,0);
        setBackground("play_bg_control_normal_890_80");
        //创建view
        createView();
    }
    public static final int TYPE_SETTING=1;
    public static final int TYPE_SKYBOX=0;
    int mType=TYPE_SETTING;
    public void setType(int type){
        mType=type;
        changeUIByType(false);
    }

    public void changeUIByType(boolean focused){
        if(focused) {
            if(mType==TYPE_SETTING){
                icon.setBackground("play_icon_setting_scene_hover");
                rightIcon.setBackground("play_icon_setting_go_hover");
                rightIcon.setVisible(true);
                leftIcon.setVisible(false);
            }else{
                icon.setBackground("play_icon_setting_high_hover");
                leftIcon.setBackground("play_icon_setting_return_hover");
                leftIcon.setVisible(true);
                rightIcon.setVisible(false);
            }
            setBackground("play_bg_control_hover_890_80");
            textView.setTextColor(new GLColor(0xbbbbbb));
        } else {
            if(mType==TYPE_SETTING){
                icon.setBackground("play_icon_setting_scene_normal");
                rightIcon.setBackground("play_icon_setting_go_normal");
                rightIcon.setVisible(true);
                leftIcon.setVisible(false);
            }else{
                icon.setBackground("play_icon_setting_high_normal");
                leftIcon.setBackground("play_icon_setting_return_normal");
                leftIcon.setVisible(true);
                rightIcon.setVisible(false);
            }
            setBackground("play_bg_control_normal_890_80");
            textView.setTextColor(new GLColor(0x888888));
        }
    }

    private void createView() {

        leftIcon = new GLImageView(mContext);
        leftIcon.setLayoutParams(40,40);
        leftIcon.setBackground("play_icon_setting_return_normal");
        leftIcon.setMargin(40f,20f,0f,0f);
        addView(leftIcon);

        icon = new GLImageView(mContext);
        icon.setLayoutParams(40,40);
        icon.setBackground("play_icon_setting_scene_normal");
        icon.setMargin(268f,20f,0f,0f);
        addView(icon);

        textView = new GLTextView(mContext);
        textView.setLayoutParams(160f,40f);
        textView.setTextSize(32);
        textView.setTextColor(new GLColor(0x888888));
        textView.setAlignment(GLTextView.ALIGN_CENTER);
        textView.setText("场景选择");
        textView.setMargin(30f,20f,0f,0f);
        addView(textView);

        rightIcon = new GLImageView(mContext);
        rightIcon.setLayoutParams(40,40);
        rightIcon.setBackground("play_icon_setting_go_normal");
        rightIcon.setMargin(222f,20f,0f,0f);
        addView(rightIcon);



        setFocusListener(new GLViewFocusListener() {
            @Override
            public void onFocusChange(GLRectView view, boolean focused) {
                changeUIByType(focused);
//                if(focused) {
//                    setBackground(R.drawable.play_bg_control_hover_890_80);
//                    icon.setBackground(R.drawable.play_icon_setting_scene_hover);
//                    textView.setTextColor(new GLColor(0x888888));
//                    rightIcon.setBackground(R.drawable.play_icon_setting_go_hover);
//                } else {
//                    setBackground(R.drawable.play_bg_control_normal_890_80);
//                    icon.setBackground(R.drawable.play_icon_setting_scene_normal);
//                    textView.setTextColor(new GLColor(0xbbbbbb));
//                    rightIcon.setBackground(R.drawable.play_icon_setting_go_normal);
//                }
            }
        });




    }
    public void setBackName(String name){
        textView.setText(name);
    }

//    public void setKeyListener(GLOnKeyListener listener) {
//        setOnKeyListener(listener);
//    }
}
