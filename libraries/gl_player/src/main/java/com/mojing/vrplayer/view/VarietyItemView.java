package com.mojing.vrplayer.view;

import android.content.Context;
import android.text.TextUtils;

import com.bfmj.viewcore.render.GLColor;
import com.bfmj.viewcore.view.GLRelativeView;
import com.bfmj.viewcore.view.GLTextView;
import com.mojing.vrplayer.R;

/**
 * Created by yushaochen on 2017/4/17.
 */

public class VarietyItemView extends GLRelativeView{

    private Context mContext;

    private GLTextView textView;

    private int mWidth = 400;
    private int mHeight = 60;

    public VarietyItemView(Context context) {
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
        textView.setBackground("play_bg_control_hover_400_60");
        textView.setTextColor(new GLColor(0x888888));
        textView.setTextSize(28);
        textView.setAlignment(GLTextView.ALIGN_CENTER);
        textView.setPadding(0f,10f,0f,0f);
        addView(textView);
    }

    public void setText(String text) {
        if(!TextUtils.isEmpty(text)) {
            int length = text.length();
            if(length>15) {
                String start = text.substring(0, 7);
                String end = text.substring(text.length()-6);
                textView.setText(start+"..."+end);
            } else {
                textView.setText(text);
            }
        } else {
            textView.setText("");
        }
//        textView.setText("01  这是一个好看的电视的啊");
    }
}
