package com.mojing.vrplayer.simpleview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mojing.vrplayer.R;


/**
 * Created by wanghongfang on 2017/6/6.
 * 触屏全屏播放时异常提示框
 */
public class PlayerDialogView extends RelativeLayout{
    private TextView reloadTv;
    private Button reloadBtn;
    private boolean isContinuBtn = false;
    public PlayerDialogView(Context context) {
        super(context);
        initView();
    }

    public PlayerDialogView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView(){
        View view = LayoutInflater.from(getContext()).inflate(R.layout.player_simple_dialog_layout,null);
        reloadTv = (TextView) view.findViewById(R.id.player_reload_tv);
        reloadBtn = (Button) view.findViewById(R.id.player_relaod_btn);
        this.addView(view,ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    }

    public void setTipText(String text){
        if(reloadTv!=null){
            reloadTv.setText(text);
        }
    }

    public void setTipBtnText(String text){
        if(reloadBtn!=null){
            reloadBtn.setText(text);
        }
    }
    public void setBtnOnClickListener(OnClickListener listener){
        if(reloadBtn!=null) {
            reloadBtn.setOnClickListener(listener);
        }
    }
    public boolean getIsContinueBtn(){
        return isContinuBtn;
    }

    public void showExceptionDialog(String msg,String BtnName,boolean isContinuBtn){
        this.isContinuBtn = isContinuBtn;
        setTipBtnText(BtnName);
        setTipText(msg);
    }

    public interface DismissListener {
        void dismiss();
    }
}
