package com.baofeng.mj.ui.view;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.baofeng.mj.vrplayer.R;


/** 自定义Loading动画
 * Created by muyu on 2016/8/16.
 */
public class CustomProgressView extends FrameLayout {
    private Context mContext;
    private View rootView;
    private ImageView imageView;

    private AnimationDrawable animationDrawable;

    public CustomProgressView(Context context) {
        super(context);
        this.mContext = context;
        initView();
    }

    public CustomProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        initView();
    }

    private void initView(){
        rootView = LayoutInflater.from(mContext).inflate(R.layout.progress_dialog_custom, this);
        imageView = (ImageView) rootView.findViewById(R.id.loadingImageView);
        animationDrawable = (AnimationDrawable) imageView.getBackground();
        animationDrawable.start();
    }

    public void setMessage(String strMessage){
        TextView tvMsg = (TextView)findViewById(R.id.id_tv_loadingmsg);

        if (tvMsg != null){
            tvMsg.setText(strMessage);
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        animationDrawable = (AnimationDrawable) imageView.getBackground();
        animationDrawable.start();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        animationDrawable.stop();
        animationDrawable = null;
    }
}
