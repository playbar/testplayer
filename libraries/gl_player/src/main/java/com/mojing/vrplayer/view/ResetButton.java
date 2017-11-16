package com.mojing.vrplayer.view;

import android.content.Context;
import android.graphics.Bitmap;

import com.bfmj.viewcore.animation.GLRotateAnimation;
import com.bfmj.viewcore.interfaces.GLOnKeyListener;
import com.bfmj.viewcore.interfaces.GLViewFocusListener;
import com.bfmj.viewcore.render.GLColor;
import com.bfmj.viewcore.view.GLTextView;
import com.mojing.vrplayer.utils.BitmapUtil;
import com.mojing.vrplayer.utils.HeadControlUtil;

/**
 * Created by yushaochen on 2017/4/3.
 */

public class ResetButton extends ResetRelativeLayout {
    private Context mContext;
    private float mImageWidth = 80f;
    private float mImageHeight = 80f;
    private float mTextWidth = 150f;
    private float mTextHeight = 50f;

    public ResetButton(Context context) {
        super(context);
        mContext = context;
        this.setLayoutParams(mTextWidth,mImageHeight + 10f +mTextHeight);
        //创建顶部图片
        createTopImage();
        //创建底部文字提示
        createBottom();
    }
    private ResetImageView mImage;

    private void createTopImage() {
        mImage = new ResetImageView(mContext);
        mImage.setLayoutParams(mImageWidth,mImageHeight);
        mImage.setMargin(35f,0f,35f,10f);
        HeadControlUtil.bindView(mImage);
        this.addView(mImage);
    }

    private ResetTextView mText;

    private void createBottom() {
        mText = new ResetTextView(mContext);
        mText.setLayoutParams(mTextWidth,mTextHeight);
        mText.setMargin(0f,90f,0f,0f);
        mText.setTextSize(28);
        mText.setTextColor(new GLColor(0x888888));
        mText.setPadding(0f,7f,0f,0f);
        mText.setAlignment(GLTextView.ALIGN_CENTER);
        Bitmap bitmap = BitmapUtil.getBitmap((int) mTextWidth, (int) mTextHeight, 10f, "#19191a");
        mText.setBackground(bitmap);
        mText.setVisible(false);
        this.addView(mText);
    }


    public void setImageBg(String id){
        mImage.setBackground(id);
    }

    public ResetImageView getImageView(){
        return mImage;
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

    public void startTranslate(boolean rote) {
        GLRotateAnimation glRotateAnimation = null;
        if(rote) {//逆时针旋转45度
            glRotateAnimation = new GLRotateAnimation(45,0f,0f,1f);
        } else {//顺时针旋转45度
            glRotateAnimation = new GLRotateAnimation(-45,0f,0f,1f);
        }
        glRotateAnimation.setAnimView(mImage);
        glRotateAnimation.setDuration(0);
        mImage.startAnimation(glRotateAnimation);
    }
}
