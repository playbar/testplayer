package com.baofeng.mj.smb.viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.baofeng.mj.vrplayer.R;


/**
 * Created by panxin on 2017/8/8.
 */

public class SMBNotConnectViewHolder extends RecyclerView.ViewHolder {
    public ImageView imageview_refresh;
    public ProgressBar progressbar_loading;
    public TextView textview_help;
    public ImageView imageview_help;

    public SMBNotConnectViewHolder(View itemView) {
        super(itemView);
        imageview_refresh = (ImageView) itemView.findViewById(R.id.imageview_refresh);
        progressbar_loading = (ProgressBar) itemView.findViewById(R.id.progressbar_loading);
        textview_help = (TextView) itemView.findViewById(R.id.textview_help);
        imageview_help = (ImageView) itemView.findViewById(R.id.imageview_help);
    }
}