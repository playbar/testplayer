package com.mojing.vrplayer.view;

import android.content.Context;
import android.text.TextUtils;

import com.bfmj.viewcore.render.GLColor;
import com.bfmj.viewcore.view.GLImageView;
import com.bfmj.viewcore.view.GLRelativeView;
import com.bfmj.viewcore.view.GLTextView;
import com.mojing.vrplayer.R;

/**
 * Created by yushaochen on 2017/4/10.
 */

public class GLProcessToast extends GLRelativeView{

    private Context mContext;

    private int width = 360;
    private int height = 300;

    private int mImageWidth = 150;
    private int mImageHeight = 150;

    private GLImageView processImageView;
    private GLTextView speedText;
    private GLTextView textView1;
    private GLTextView textView2;

    private boolean mRotate = false;
    private int mSpeed = 100;
    private float mAngle = 45f;

    public GLProcessToast(Context context) {
        super(context);

        mContext = context;
        setLayoutParams(width,mImageHeight);
//        setBackground(new GLColor(0xff0000));
        //创建顶部旋转进度显示
        createProcessView();

        setVisible(false);
        setHandleFocus(false);
    }

    private void createProcessView() {
        //创建圆形进度
        processImageView = new GLImageView(mContext);
        processImageView.setLayoutParams(mImageWidth,mImageHeight);
        processImageView.setMargin((width-mImageWidth)/2,0f,0f,0f);
        processImageView.setBackground("play_icon_loading");
        addView(processImageView);
        //创建进度文案显示
        speedText = new GLTextView(mContext);
        speedText.setLayoutParams(mImageWidth,mImageHeight);
        speedText.setMargin((width-mImageWidth)/2,0f,0f,0f);
        speedText.setPadding(0f,55f,0f,0f);
        speedText.setAlignment(GLTextView.ALIGN_CENTER);
        speedText.setTextSize(28);
        speedText.setTextColor(new GLColor(0xaaaaaa));
//        speedText.setText("1020KB/S");
        addView(speedText);
//        //创建进度条下方文字显示
//        textView1 = new GLTextView(mContext);
//        textView1.setLayoutParams(width,40f);
//        textView1.setMargin(0f,150+10f,0f,0f);
//        textView1.setTextSize(28);
//        textView1.setTextColor(new GLColor(0x888888));
//        textView1.setAlignment(GLTextView.ALIGN_CENTER);
////        textView1.setText("从01:02:03开始继续播放");
//        addView(textView1);
//        //创建进度条下方文字显示
//        textView2 = new GLTextView(mContext);
//        textView2.setLayoutParams(width,60f);
//        textView2.setMargin(0f,150+10f+40f+20f,0f,0f);
//        textView2.setTextSize(32);
//        textView2.setTextColor(new GLColor(0xaaaaaa));
//        textView2.setAlignment(GLTextView.ALIGN_CENTER);
////        textView2.setText("即将播放:爱乐之城");
//        addView(textView2);
    }

    private void rotate() {
        (new Thread(new Runnable() {
            public void run() {
                for(; GLProcessToast.this.mRotate; GLProcessToast.this.processImageView.rotate(-GLProcessToast.this.mAngle, 0.0F, 0.0F, 1.0F)) {
                    try {
                        Thread.sleep((long)GLProcessToast.this.mSpeed);
                    } catch (InterruptedException var2) {
                        var2.printStackTrace();
                    }
                }

            }
        })).start();
    }

    public void setSpeed(int speed) {
        this.mSpeed = speed;
    }

    public void removeRotate() {
        this.mRotate = false;
    }

    public void setProcessImage(String imageId) {
        processImageView.setBackground(imageId);
    }

    public void setSpeedText(String text) {
        if(!TextUtils.isEmpty(text)) {
            speedText.setText(text);
        } else {
            speedText.setText("");
        }
    }

//    public void setText1(String text1) {
//        if(!TextUtils.isEmpty(text1)) {
//            textView1.setText(text1);
//        } else {
//            textView1.setText("");
//        }
//    }
//
//    public void setText2(String text2) {
//        if(!TextUtils.isEmpty(text2)) {
//            textView2.setText(text2);
//        } else {
//            textView2.setText("");
//        }
//    }

    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);
        if(visible) {
            //开启转动
            if(!this.mRotate) {
                this.mRotate = true;
                this.rotate();
            }
        } else {
            //关闭转动
            this.mRotate = false;
        }
    }

}
