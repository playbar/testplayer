package com.mojing.vrplayer.view;

import android.content.Context;

import com.bfmj.viewcore.render.GLConstant;
import com.bfmj.viewcore.view.GLImageView;
import com.bfmj.viewcore.view.GLLinearView;
import com.bfmj.viewcore.view.GLRelativeView;
import com.bfmj.viewcore.view.GLTextView;
import com.mojing.vrplayer.R;
import com.mojing.vrplayer.utils.GLColorUitl;

/**
 * Created by yushaochen on 2017/5/22.
 */

public class LocalSourceItemView extends GLRelativeView{

    private Context mContext;

    private int mWidth = 300;
    private int mHeight = 170+40+60;

    private GLImageView imageView;

    private GLImageView imageIcon;

    private GLTextView titleView;
    private GLTextView durationTitleView;

    public LocalSourceItemView(Context context) {
        super(context);

        mContext = context;
        setLayoutParams(mWidth,mHeight);
        //创建左侧缩略图显示
        createLeftImageView();
        //创建文本标题显示
        createTitleView();
        createDurationView();
    }

    private void createTitleView() {
        titleView = new GLTextView(mContext);
        titleView.setId("titleView");
        titleView.setLayoutParams(256,35);
        titleView.setText("");
        titleView.setTextSize(32);
        titleView.setTextColor(GLColorUitl.HoverTextColor);
        titleView.setMargin(0,20+170,0,15);
        titleView.setAlignment(GLTextView.ALIGN_LEFT);
        titleView.setAlign(GLConstant.GLAlign.CENTER_VERTICAL);
        titleView.setLineHeight(2);

        addView(titleView);
    }

    private void createDurationView() {
        durationTitleView = new GLTextView(mContext);
        durationTitleView.setId("durationView");
        durationTitleView.setLayoutParams(256,30);
        durationTitleView.setText("00:00");
        durationTitleView.setTextSize(32);
        durationTitleView.setTextColor(GLColorUitl.DefaultTextColor);
        durationTitleView.setMargin(0,15+170+40,0,15);
        durationTitleView.setAlignment(GLTextView.ALIGN_LEFT);
        durationTitleView.setAlign(GLConstant.GLAlign.CENTER_VERTICAL);
        durationTitleView.setLineHeight(2);

        addView(durationTitleView);
    }

    private void createLeftImageView() {
        imageView = new GLImageView(mContext);
        imageView.setId("imageView");
        imageView.setLayoutParams(300,170);
        imageView.setPadding(4,4,4,4);
        imageView.setImage("play_video_local_ph_bg");
        imageView.setBackground("play_video_local_bg_empty");

        imageIcon = new GLImageView(mContext);
        imageIcon.setId("imageIcon");
        imageIcon.setLayoutParams(50,50);
        imageIcon.setMargin((300/2-25),(170/2-25),0,0);
        imageIcon.setBackground("play_video_local_icon_play");
        imageIcon.setVisible(false);

        addView(imageView);
        addView(imageIcon);
    }
}
