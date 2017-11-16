package com.mojing.vrplayer.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
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
public class GLLoadToast extends GLLinearView {

    private Context mContext;

    private GLTextView textView;
    private GLImageView imageView;

    private boolean mRotate = false;
    private int mSpeed = 100;
    private float mAngle = 45f;


    public GLLoadToast(Context context){
        super(context);
        mContext = context;
        setLayoutParams(735,80);
        setOrientation(GLConstant.GLOrientation.HORIZONTAL);
        Bitmap bitmap = BitmapUtil.getBitmap(735, 80, 20f, "#000000");
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
        textView.setLayoutParams(735-170,80);
//        textView.setBackground(new GLColor(0xff0000));
        textView.setMargin(20,0,0,0);
        textView.setTextSize(28);
        textView.setTextColor(new GLColor(0x888888));
        textView.setPadding(0,20,0,0);
        textView.setAlignment(GLTextView.ALIGN_CENTER);
        textView.setText("");
        addView(textView);
    }

    /**
     * 1 样式为：即将播放:这是一个测试的...
     * 2 样式为：从00:00:00继续播放:这是一个测试的...
     * @param name
     */
    public void setText(String time,String name){
        if(!TextUtils.isEmpty(name)) {
            int length = 10;
            if(name.length() > length) {
                name = name.substring(0, 5) +"..."+name.substring(name.length()-5);
            }
            if(TextUtils.isEmpty(time)) {
                textView.setText("即将播放:"+name);
            } else {
                textView.setText("从"+time+"继续播放:"+name);
            }
        } else {
            textView.setText("");
        }
    }

    private void rotate() {
        (new Thread(new Runnable() {
            public void run() {
                for(; GLLoadToast.this.mRotate; GLLoadToast.this.imageView.rotate(-GLLoadToast.this.mAngle, 0.0F, 0.0F, 1.0F)) {
                    try {
                        Thread.sleep((long)GLLoadToast.this.mSpeed);
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
