package com.mojing.vrplayer.view;

import android.content.Context;
import com.bfmj.viewcore.interfaces.GLOnKeyListener;
import com.bfmj.viewcore.interfaces.GLViewFocusListener;
import com.bfmj.viewcore.render.GLColor;
import com.bfmj.viewcore.render.GLConstant;
import com.bfmj.viewcore.view.GLImageView;
import com.bfmj.viewcore.view.GLLinearView;
import com.bfmj.viewcore.view.GLRectView;
import com.bfmj.viewcore.view.GLTextView;
import com.mojing.vrplayer.R;
import com.mojing.vrplayer.utils.HeadControlUtil;

/**
 * Created by yushaochen on 2017/5/15.
 */

public class StickIntroductionView extends GLLinearView{

    private Context mContext;

    private int mWidth = 960;
    private int mHeight = 700+70+70;

    private int mImageWidth = 960;
    private int mImageHeight = 700;

    private GLImageView imageView;

    private GLTextView okBtn;

    public static final int WII_STICK = 1;//体感手柄模式
    public static final int NO_WII_STICK = 2;//非体感手柄模式

    public StickIntroductionView(Context context) {
        super(context);
        mContext = context;
        setLayoutParams(mWidth,mHeight);
        setOrientation(GLConstant.GLOrientation.VERTICAL);
        //创建顶部说明图展示
        createTopImageView();
        //创建底部确定按钮
        createOkBtn();
    }

    private void createOkBtn() {
        okBtn = new GLTextView(mContext);
        okBtn.setLayoutParams(240f,70f);
        okBtn.setBackground("play_button_bg_ok_normal");
        okBtn.setMargin((mWidth-240f)/2,70f,0f,0f);
        okBtn.setTextSize(32);
        okBtn.setTextColor(new GLColor(0x888888));
        okBtn.setAlignment(GLTextView.ALIGN_CENTER);
        okBtn.setText("我学会了");
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

    private void createTopImageView() {
        imageView = new GLImageView(mContext);
        imageView.setLayoutParams(mImageWidth,mImageHeight);
//        imageView.setBackground(R.drawable.play_welcome_image_handle);
        addView(imageView);
    }

    public void setStickMode(int mode) {
        if(mode == WII_STICK) {
            imageView.setBackground("play_welcome_image_handle");
        } else if(mode == NO_WII_STICK) {
            imageView.setBackground("play_welcome_image_otherhandle");
        }
    }

    public void setOkKeyListener(GLOnKeyListener keyListener) {
        if(null != keyListener) {
            okBtn.setOnKeyListener(keyListener);
        }
    }
}
