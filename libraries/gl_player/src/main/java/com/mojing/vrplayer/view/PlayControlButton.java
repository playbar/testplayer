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

public class PlayControlButton extends GLRelativeView{
    private Context mContext;
    private float mImageWidth = 60f;
    private float mImageHeight = 60f;
    private float mTextWidth = 60f;
    private float mTextHeight = 40f;

    public PlayControlButton(Context context) {
        super(context);
        mContext = context;
        this.setLayoutParams(mTextWidth,mImageHeight +mTextHeight);
        //创建顶部图片
        createTopImage();
        //创建底部文字提示
        createBottom();
    }
    public  GLImageView mImage;

    private void createTopImage() {
        mImage = new GLImageView(mContext);
        mImage.setLayoutParams(mImageWidth,mImageHeight);
        mImage.setMargin(0f,0f,0f,5f);
      //  HeadControlUtil.bindView(mImage);
        this.addView(mImage);
    }

    private GLTextView mText;

    private void createBottom() {
        mText = new GLTextView(mContext);
        mText.setLayoutParams(mTextWidth,mTextHeight);
        mText.setMargin(0f,60f,0f,0f);
        mText.setTextSize(28);
        mText.setTextColor(new GLColor(0x888888));
        mText.setPadding(0f,0f,0f,0f);
        mText.setAlignment(GLTextView.ALIGN_CENTER);
        mText.setVisible(false);
        this.addView(mText);
    }

//    public void setImageBg(int id){
//        mImage.setBackground(id);
//    }

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

    public void setImageHeadControl(boolean flag) {
        if(flag) {
            HeadControlUtil.bindView(mImage);
        } else {
            HeadControlUtil.unbindView(mImage);
        }
    }
    public void setImageLayout(float width,float height){
        mImage.setLayoutParams(width,height);
    }
    public GLTextView getText(){
       return mText;
    }
}
