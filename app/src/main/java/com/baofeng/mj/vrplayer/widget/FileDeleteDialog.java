package com.baofeng.mj.vrplayer.widget;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baofeng.mj.vrplayer.R;
import com.baofeng.mj.vrplayer.util.PixelsUtil;

/**
 * Created by liuchuanchi on 2016/6/14.
 * 存储删除对话框
 */
public class FileDeleteDialog {
    private Dialog dialog;//对话框
    private MyDialogInterface myDialogInterface;//回调接口

    public FileDeleteDialog(Activity context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.dialog_file_delete,null);//生成布局文件
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.width = PixelsUtil.getWidthPixels(context) - PixelsUtil.dip2px(context, 40);
        dialog = new Dialog(context, R.style.translucence_dialog);//创建半透明对话框
        dialog.setContentView(view, params);//设置布局文件
        dialog.setCancelable(true);//true可以点击返回键取消对话框
        TextView tv_cancel = (TextView) view.findViewById(R.id.tv_cancel);
        TextView tv_delete = (TextView) view.findViewById(R.id.tv_delete);
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        tv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (myDialogInterface != null) {
                    myDialogInterface.dialogCallBack();
                }
                dialog.dismiss();
            }
        });
    }

    /**
     * 显示对话框
     */
    public void showDialog(MyDialogInterface myDialogInterface){
        if(dialog != null && !dialog.isShowing()){
            this.myDialogInterface = myDialogInterface;
            dialog.show();
        }
    }

    public interface MyDialogInterface {
        void dialogCallBack();
    }
}
