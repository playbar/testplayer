package com.mojing.vrplayer.view;

import android.content.Context;
import android.graphics.Bitmap;

import com.bfmj.viewcore.render.GLColor;
import com.bfmj.viewcore.render.GLConstant;
import com.bfmj.viewcore.view.GLImageView;
import com.bfmj.viewcore.view.GLLinearView;
import com.bfmj.viewcore.view.GLTextView;
import com.mojing.vrplayer.R;
import com.mojing.vrplayer.utils.BitmapUtil;

/**
 * Created by yushaochen on 2017/4/27.
 */
public class GLLoadingToast extends GLLinearView {

    private Context mContext;

    private GLTextView textView;
    private GLImageView imageView;

    private boolean mRotate = false;
    private int mSpeed = 100;
    private float mAngle = 45f;


    public GLLoadingToast(Context context){
        super(context);
        mContext = context;
        setLayoutParams(270,80);
        setOrientation(GLConstant.GLOrientation.HORIZONTAL);
        Bitmap bitmap = BitmapUtil.getBitmap(270, 80, 20f, "#000000");
        setBackground(bitmap);
        //创建旋转icon
        createImageView();
        //创建文本显示
        createTextView();

        setVisible(false);
    }

    private void createImageView() {
        imageView = new GLImageView(mContext);
        imageView.setLayoutParams(50,50);
        imageView.setBackground("play_icon_loading");
        imageView.setMargin(50,15,0,0);
        addView(imageView);
    }

    private void createTextView(){
        textView = new GLTextView(mContext);
        textView.setLayoutParams(100,80);
//        textView.setBackground(new GLColor(0xff0000));
        textView.setMargin(20,0,0,0);
        textView.setTextSize(28);
        textView.setTextColor(new GLColor(0x888888));
        textView.setPadding(0,20,0,0);
        textView.setAlignment(GLTextView.ALIGN_CENTER);
        textView.setText("加载中");
        addView(textView);
    }

    private void rotate() {
        (new Thread(new Runnable() {
            public void run() {
                for(; GLLoadingToast.this.mRotate; GLLoadingToast.this.imageView.rotate(-GLLoadingToast.this.mAngle, 0.0F, 0.0F, 1.0F)) {
                    try {
                        Thread.sleep((long)GLLoadingToast.this.mSpeed);
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
