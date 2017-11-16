package com.baofeng.mj.smb.viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.baofeng.mj.vrplayer.R;


/**
 * Created by panxin on 2017/8/8.
 */

public class SMBItemViewHolder extends RecyclerView.ViewHolder {
    public TextView textview_name;
    public TextView textview_ip;
    public View view;

    public SMBItemViewHolder(View itemView) {
        super(itemView);
        textview_name = (TextView) itemView.findViewById(R.id.textview_name);
        textview_ip = (TextView) itemView.findViewById(R.id.textview_ip);
        this.view = itemView;
    }
}