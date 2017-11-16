package com.mojing.vrplayer.view;

import android.content.Context;
import com.bfmj.viewcore.interfaces.GLOnKeyListener;
import com.bfmj.viewcore.interfaces.GLViewFocusListener;
import com.bfmj.viewcore.render.GLColor;
import com.bfmj.viewcore.render.GLConstant;
import com.bfmj.viewcore.view.GLImageView;
import com.bfmj.viewcore.view.GLLinearView;
import com.bfmj.viewcore.view.GLTextView;
import com.mojing.vrplayer.bean.ModeItemInfo;
import com.mojing.vrplayer.utils.HeadControlUtil;

/**
 * Created by yushaochen on 2017/5/16.
 */

public class ModeItemView extends GLLinearView{

    private Context mContext;

    private int mWidth = 130;
    private int mHeigth = 80+15+35;

    private GLImageView topImageView;

    private GLTextView textView;

    private ModeItemInfo info;

    public ModeItemView(Context context) {
        super(context);
        mContext = context;
        setOrientation(GLConstant.GLOrientation.VERTICAL);
        //创建顶部图片
        createTopImage();
        //创建底部文本显示
        createTextView();
    }

    private void createTextView() {
        textView = new GLTextView(mContext);
        textView.setLayoutParams(130,35);
        textView.setTextSize(28);
        textView.setTextColor(new GLColor(0x888888));
        textView.setMargin(0,15,0,0);
        textView.setAlignment(GLTextView.ALIGN_CENTER);

        addView(textView);
    }

    private void createTopImage() {
        topImageView = new GLImageView(mContext);
        topImageView.setLayoutParams(130,80);

        addView(topImageView);
        HeadControlUtil.bindView(topImageView);
    }

    public void setData(ModeItemInfo info) {
        this.info = info;
        if(null != info) {
            topImageView.setBackground(info.getImage_no_focus_Str());
            textView.setText(info.getName());
        }
    }

    public void setImageFocusListener(GLViewFocusListener listener){
        topImageView.setFocusListener(listener);
    }

    public void setImageKeyListener(GLOnKeyListener listener) {
        topImageView.setOnKeyListener(listener);
    }

    public void setItemClick() {
        topImageView.setBackground(info.getImage_click_Str());
        textView.setBackground(new GLColor(0,0,0,0));
        textView.setTextColor(new GLColor(0x008cb3));
    }

    public void setItemFocuse(boolean flag) {
        if(flag) {
            topImageView.setBackground(info.getImage_focused_Str());
            textView.setBackground(new GLColor(0,0,0,0));
            textView.setTextColor(new GLColor(0xbbbbbb));
        } else {
            topImageView.setBackground(info.getImage_no_focus_Str());
            textView.setBackground(new GLColor(0,0,0,0));
            textView.setTextColor(new GLColor(0x888888));
        }
    }
    public ModeItemInfo getInfo(){
        return info;
    }
}
