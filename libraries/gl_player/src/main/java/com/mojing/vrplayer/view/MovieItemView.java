package com.mojing.vrplayer.view;

import android.content.Context;

import com.bfmj.viewcore.render.GLColor;
import com.bfmj.viewcore.view.GLImageView;
import com.bfmj.viewcore.view.GLRelativeView;
import com.bfmj.viewcore.view.GLTextView;
import com.mojing.vrplayer.R;

/**
 * Created by yushaochen on 2017/5/19.
 */

public class MovieItemView extends GLRelativeView{

    private Context mContext;

    private int mWidth = 268;
    private int mHeigth = 156;

    private GLImageView imageView;

    private GLImageView imageView2;

    private GLTextView titleView;

    public MovieItemView(Context context) {
        super(context);
        mContext = context;
        setLayoutParams(mWidth,mHeigth);
//        setBackground( new GLColor(0x00ff00));

        //资源图片
        imageView = new GLImageView(mContext);
        imageView.setId("imageView");
        imageView.setLayoutParams(268,156);
        imageView.setPadding(6,6,6,6);
        imageView.setImage("play_video_online_ph_bg");
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
    }
}
