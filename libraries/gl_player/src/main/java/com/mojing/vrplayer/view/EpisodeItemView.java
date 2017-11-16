package com.mojing.vrplayer.view;

import android.content.Context;

import com.bfmj.viewcore.render.GLColor;
import com.bfmj.viewcore.view.GLRelativeView;
import com.bfmj.viewcore.view.GLTextView;
import com.mojing.vrplayer.R;

/**
 * Created by yushaochen on 2017/4/14.
 */

public class EpisodeItemView extends GLRelativeView{

    private Context mContext;

    private GLTextView textView;

    private int mWidth = 150;
    private int mHeight = 60;

    public EpisodeItemView(Context context) {
        super(context);
        mContext = context;
        setLayoutParams(mWidth,mHeight);
        //创建显示的view
        createView();
    }

    private void createView() {
        textView = new GLTextView(mContext);
        textView.setId("textView");
        textView.setLayoutParams(mWidth,mHeight);
        textView.setBackground("play_bg_control_normal_150_60");
        textView.setTextColor(new GLColor(0x888888));
        textView.setTextSize(28);
        textView.setAlignment(GLTextView.ALIGN_CENTER);
        textView.setPadding(0f,10f,0f,0f);
        addView(textView);
    }
}
