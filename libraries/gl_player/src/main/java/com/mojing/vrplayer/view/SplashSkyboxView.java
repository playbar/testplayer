package com.mojing.vrplayer.view;

import android.content.Context;
import android.graphics.Bitmap;

import com.bfmj.viewcore.interfaces.GLOnKeyListener;
import com.bfmj.viewcore.interfaces.GLViewFocusListener;
import com.bfmj.viewcore.render.GLColor;
import com.bfmj.viewcore.view.GLRectView;
import com.bfmj.viewcore.view.GLRelativeView;
import com.bfmj.viewcore.view.GLTextView;
import com.mojing.vrplayer.R;
import com.mojing.vrplayer.publicc.SettingSpBusiness;
import com.mojing.vrplayer.utils.BitmapUtil;
import com.mojing.vrplayer.utils.HeadControlUtil;

/**
 * Created by yushaochen on 2017/4/10.
 */

public class SplashSkyboxView extends GLRelativeView{

    private Context mContext;

    private int width = 1000;

    private int height = 60 + 70 + 284 + 70 + 70;

    private GLTextView topText;

    private GLTextView okBtn;

    private SkyboxView skyboxView;

    public SplashSkyboxView(Context context) {
        super(context);

        mContext = context;

        setLayoutParams(width,height);

        //创建顶部文字提示
        createTopText();
        //创建中间场景选择
        createSkyboxView();
        //创建底部确定按钮
        createOkBtn();
    }

    private void createOkBtn() {
        okBtn = new GLTextView(mContext);
        okBtn.setLayoutParams(240f,70f);
        okBtn.setBackground("play_button_bg_ok_normal");
        okBtn.setMargin((width-240f)/2,60f+70f+284f+70f,0f,0f);
        okBtn.setTextSize(32);
        okBtn.setTextColor(new GLColor(0x888888));
        okBtn.setAlignment(GLTextView.ALIGN_CENTER);
        okBtn.setText("确定");
        okBtn.setPadding(0f,12f,0f,0f);
        okBtn.setFocusListener(new GLViewFocusListener() {
            @Override
            public void onFocusChange(GLRectView view, boolean focused) {
                if(focused) {
                    okBtn.setBackground("play_button_bg_ok_hover");
                    okBtn.setTextColor(new GLColor(0x19191a));
                } else {
                    okBtn.setBackground("play_button_bg_ok_normal");
                    okBtn.setTextColor(new GLColor(0x888888));
                }
            }
        });
        HeadControlUtil.bindView(okBtn);
        addView(okBtn);
    }

    private void createSkyboxView() {

        skyboxView = new SkyboxView(mContext);
        skyboxView.setLayoutParams(1000,285);
        Bitmap bitmap = BitmapUtil.getBitmap(1000, 285, 20f,"#272729");
        skyboxView.setBackground(bitmap);
        skyboxView.setPadding(105f,55f,105f,55f);
        skyboxView.setMargin(0f,60f+70f,0f,0f);
        //设置默认选项
        skyboxView.setSelected(SettingSpBusiness.getInstance(mContext).getSkyboxIndex()+"");
        addView(skyboxView);
    }

    private void createTopText() {
        topText = new GLTextView(mContext);
        topText.setLayoutParams(200,60);
        topText.setTextSize(40);
        topText.setTextColor(new GLColor(0x888888));
        topText.setMargin((1000f-200f)/2,0f,0f,0f);
        topText.setText("场景选择");
        topText.setAlignment(GLTextView.ALIGN_CENTER);

        addView(topText);
    }

    public void setOkKeyListener(GLOnKeyListener keyListener) {
        if(null != keyListener) {
            okBtn.setOnKeyListener(keyListener);
        }
    }

}
