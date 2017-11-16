package com.baofeng.mj.smb.view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.TextView;

import com.baofeng.mj.vrplayer.R;


/** 自定义Loading动画
 * Created by muyu on 2016/6/13.
 */
public class CustomProgressDialog extends Dialog {
    private Context context;

    public CustomProgressDialog(Context context){
        super(context, R.style.CustomProgressDialog);
        this.context = context;
        initView();
    }

    private void initView(){
        this.setContentView(R.layout.progress_dialog_custom);
        this.getWindow().getAttributes().gravity = Gravity.CENTER;
        this.setCancelable(false);
        ImageView imageView = (ImageView)findViewById(R.id.loadingImageView);
        AnimationDrawable animationDrawable = (AnimationDrawable) imageView.getBackground();
        animationDrawable.start();
    }

    public void setMessage(String strMessage){
        TextView tvMsg = (TextView)findViewById(R.id.id_tv_loadingmsg);

        if (tvMsg != null){
            tvMsg.setText(strMessage);
        }
    }
}
