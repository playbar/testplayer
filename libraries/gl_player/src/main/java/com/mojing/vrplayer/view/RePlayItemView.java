package com.mojing.vrplayer.view;

import android.content.Context;

import com.bfmj.viewcore.interfaces.GLViewFocusListener;
import com.bfmj.viewcore.render.GLColor;
import com.bfmj.viewcore.view.GLImageView;
import com.bfmj.viewcore.view.GLRectView;
import com.bfmj.viewcore.view.GLRelativeView;
import com.bfmj.viewcore.view.GLTextView;
import com.mojing.vrplayer.R;
import com.mojing.vrplayer.utils.HeadControlUtil;

/**
 * Created by yushaochen on 2017/5/17.
 */

public class RePlayItemView extends GLRelativeView{

    private Context mContext;

    private int mWidth = 268;
    private int mHeigth = 156;

    private GLImageView imageView;

    private GLImageView imageView2;

    private GLTextView titleView;

    private GLTextView textView;

    private GLImageView playIcon;

    public RePlayItemView(Context context) {
        super(context);
        mContext = context;
        setLayoutParams(mWidth,mHeigth);
//        setBackground( new GLColor(0x00ff00));

        //资源图片
        imageView = new GLImageView(mContext);
        imageView.setId("imageView");
        imageView.setLayoutParams(268,156);
        imageView.setPadding(6,6,6,6);
        imageView.setImage("play_video_online_continue_ph_bg");
        imageView.setBackground("play_video_online_bg_empty");

        addView(imageView);

        imageView2 = new GLImageView(mContext);
        imageView2.setId("imageView2");
        imageView2.setLayoutParams(256+3,80+2);
        imageView2.setMargin(6,144-80+6,6,0);
        imageView2.setBackground("play_video_online_bg_mask");

        addView(imageView2);

        //资源标题
        titleView = new GLTextView(mContext);
        titleView.setId("titleView");
        titleView.setLayoutParams(256,40);
        titleView.setText("");
        titleView.setTextSize(32);
        titleView.setTextColor(new GLColor(0xcccccc));
        titleView.setMargin(0,144-10-40+10,0,0);
        titleView.setPadding(15,0,0,0);
        titleView.setAlignment(GLTextView.ALIGN_LEFT);

        addView(titleView);

        //播放按钮
        playIcon = new GLImageView(mContext);
        playIcon.setId("playIcon");
        playIcon.setLayoutParams(80,80);
        playIcon.setMargin((mWidth-80)/2,21,0,0);
        playIcon.setBackground("play_video_button_play_normal");
        playIcon.setVisible(false);
        playIcon.setFocusListener(new GLViewFocusListener() {
            @Override
            public void onFocusChange(GLRectView view, boolean focused) {
                if(focused) {
                    playIcon.setBackground("play_video_button_play_hover");
                } else {
                    playIcon.setBackground("play_video_button_play_normal");
                }
            }
        });
        HeadControlUtil.bindView(playIcon);
        addView(playIcon);

        //即将播放提示
        textView = new GLTextView(mContext);
        textView.setId("textView");
        textView.setLayoutParams(268,35);
        textView.setText("即将播放");
        textView.setTextSize(28);
        textView.setTextColor(new GLColor(0x008cb3));
        textView.setMargin(0,156+10,0,0);
        textView.setAlignment(GLTextView.ALIGN_CENTER);
        textView.setVisible(false);

        addView(textView);
    }
}
