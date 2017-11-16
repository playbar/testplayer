package com.baofeng.mj.smb.view;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.baofeng.mj.smb.interfaces.IDeviceDeleteListener;
import com.baofeng.mj.util.publicutil.PixelsUtil;
import com.baofeng.mj.vrplayer.R;

/**
 * Created by panxin on 2017/8/9.
 */

public class SMBDeleteDialog extends Dialog{

    public String ip;

    public SMBDeleteDialog(Context mContext) {
        super(mContext, R.style.SMB_Loging_Dialog);
        init(mContext);
    }


    private void init(Context context) {

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.layout_smb_dialog_delete, null);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.width = PixelsUtil.getWidthPixels() - PixelsUtil.dip2px(40);
        addContentView(layout, params);

        setCancelable(true);//true可以点击返回键取消对话框
        final TextView textview_cancle = (TextView) layout.findViewById(R.id.textview_cancle);
        final TextView textview_delete = (TextView) layout.findViewById(R.id.textview_delete);
        textview_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        textview_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mIDeviceDeleteListener != null) {
                    mIDeviceDeleteListener.delete(ip);
                }
                dismiss();
            }
        });
    }

    public void setIp(String ip){
        this.ip = ip;
    }

    private IDeviceDeleteListener mIDeviceDeleteListener;
    public void setIDeviceDeleteListener(IDeviceDeleteListener listener){
        this.mIDeviceDeleteListener = listener;
    }
}
