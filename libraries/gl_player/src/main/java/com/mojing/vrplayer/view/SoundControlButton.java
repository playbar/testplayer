package com.mojing.vrplayer.view;

import android.content.Context;

import com.bfmj.viewcore.interfaces.GLOnKeyListener;
import com.bfmj.viewcore.interfaces.GLViewFocusListener;
import com.bfmj.viewcore.render.GLColor;
import com.bfmj.viewcore.view.GLImageView;
import com.bfmj.viewcore.view.GLRelativeView;
import com.bfmj.viewcore.view.GLTextView;
import com.mojing.vrplayer.utils.HeadControlUtil;

/**
 * Created by yushaochen on 2017/4/3.
 */

public class SoundControlButton extends GLRelativeView{
    private Context mContext;
    private float mImageWidth = 44f;
    private float mImageHeight = 44f;
    private float mTextWidth = 88f;
    private float mTextHeight = 20f;

    public SoundControlButton(Context context) {
        super(context);
        mContext = context;
        this.setLayoutParams(mTextWidth,mImageHeight +mTextHeight);
        //创建顶部图片
        createTopImage();
        //创建底部文字提示
        createBottom();
    }
    private GLImageView mImage;

    private void createTopImage() {
        mImage = new GLImageView(mContext);
        mImage.setLayoutParams(mImageWidth,mImageHeight);
        mImage.setMargin(22f,0f,0f,5f);
        HeadControlUtil.bindView(mImage);

        this.addView(mImage);
    }

    private GLTextView mText;

    private void createBottom() {
        mText = new GLTextView(mContext);
        mText.setLayoutParams(mTextWidth,mTextHeight);
        mText.setMargin(0f,44f,0f,0f);
        mText.setTextSize(20);
        mText.setTextColor(new GLColor(0x888888));
        mText.setAlignment(GLTextView.ALIGN_CENTER);
        mText.setVisible(false);
        this.addView(mText);
    }

    public void setImageBg(String id){
        mImage.setBackground(id);
    }

    public void setImageFocusListener(GLViewFocusListener listener){
        mImage.setFocusListener(listener);
    }

    public void setImageKeyListener(GLOnKeyListener listener) {
        mImage.setOnKeyListener(listener);
    }

    public void setText(String text) {
        mText.setText(text);
    }

    public void setTextVisible(boolean visible) {
        mText.setVisible(visible);
    }

    public GLImageView getTopImage() {
        return mImage;
    }

}
