package com.baofeng.mj.smb.viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.baofeng.mj.vrplayer.R;


/**
 * Created by panxin on 2017/8/8.
 */

public class SMBConnectedViewHolder extends RecyclerView.ViewHolder {
    public TextView textview_connect;

    public SMBConnectedViewHolder(View itemView) {
        super(itemView);
        textview_connect = (TextView) itemView.findViewById(R.id.textview_connect);
    }
}