package com.baofeng.mj.smb.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.baofeng.mj.smb.activity.SMBHelpListActivity;
import com.baofeng.mj.smb.bean.SMBDeviceBean;
import com.baofeng.mj.smb.interfaces.ILongClickListener;
import com.baofeng.mj.smb.util.SMBViewTypeUtil;
import com.baofeng.mj.smb.viewholders.SMBConnectedViewHolder;
import com.baofeng.mj.smb.viewholders.SMBItemViewHolder;
import com.baofeng.mj.smb.viewholders.SMBNotConnectViewHolder;
import com.baofeng.mj.vrplayer.R;
import com.mj.smb.smblib.util.ScanUtil;

import java.util.List;

/**
 * Created by panxin on 2017/8/7.
 */

public class SMBDeviceAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private List<SMBDeviceBean> list;
    private Context mContext;
    private LayoutInflater layoutInflater;
    private int connectCount = 0;

    public SMBDeviceAdapter(Context context) {
        this.mContext = context;
        layoutInflater = LayoutInflater.from(mContext);
    }

    public void setConnectCount(int count){
        this.connectCount = count;
    }

    public int getConnectCount(){
        return connectCount;
    }

    public void setData(List<SMBDeviceBean> list){
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        int type = list.get(position).type;
        return type;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int type, int i1) {
        View view;
        switch (type){
            case SMBViewTypeUtil.TYPE_SMB_CONNECT:
                view = layoutInflater.inflate(R.layout.item_smb_device_connected, null);
                return new SMBConnectedViewHolder(view);
            case SMBViewTypeUtil.TYPE_SMB_NOCONNECT:
                view = layoutInflater.inflate(R.layout.item_smb_device_not_connect, null);
                return new SMBNotConnectViewHolder(view);
            case SMBViewTypeUtil.TYPE_SMB_ITEM:
                view = layoutInflater.inflate(R.layout.item_smb_device, null);
                return new SMBItemViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        switch (getItemViewType(position)) {
            case SMBViewTypeUtil.TYPE_SMB_CONNECT:
                updateConnectedView((SMBConnectedViewHolder)viewHolder);
                break;
            case SMBViewTypeUtil.TYPE_SMB_NOCONNECT:
                updateNotConnectView((SMBNotConnectViewHolder)viewHolder);
                break;
            case SMBViewTypeUtil.TYPE_SMB_ITEM:
                updateItemView((SMBItemViewHolder)viewHolder,list.get(position),position);
                break;
        }
    }

    @Override
    public int getItemCount() {
        if(list == null){
            return 0;
        }
        return list.size();
    }

    private void updateConnectedView(SMBConnectedViewHolder viewHolder){
        viewHolder.textview_connect.setText(mContext.getResources().getString(R.string.mj_share_smb_device_connected)+" ("+connectCount+")");
    }

    private void updateNotConnectView(final SMBNotConnectViewHolder viewHolder){
        if(ScanUtil.instance().isScaning()){
            viewHolder.progressbar_loading.setVisibility(View.VISIBLE);
            viewHolder.imageview_refresh.setVisibility(View.GONE);
        }else{
            viewHolder.imageview_refresh.setVisibility(View.VISIBLE);
            viewHolder.progressbar_loading.setVisibility(View.GONE);
        }
        viewHolder.imageview_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!ScanUtil.instance().isScaning()){
                    ScanUtil.instance().startScan();
//                    viewHolder.imageview_refresh.setVisibility(View.GONE);
//                    viewHolder.progressbar_loading.setVisibility(View.VISIBLE);
                }

            }
        });

        viewHolder.imageview_help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  UmengReport.reportEvent("smb_play","name","help");
                Intent intent = new Intent(mContext, SMBHelpListActivity.class);
                mContext.startActivity(intent);
            }
        });
    }

    private void updateItemView(final SMBItemViewHolder viewHolder,final SMBDeviceBean bean,final int position){
        viewHolder.textview_name.setText(bean.name);
        viewHolder.textview_ip.setText(bean.ip);
        viewHolder.view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(position<=connectCount){
                    if(mILongClickListener!=null){
                        mILongClickListener.longClick(bean.ip);
                    }
                }
                return false;
            }
        });
    }

    public ILongClickListener mILongClickListener;
    public void setILongClickListener(ILongClickListener listener){
        this.mILongClickListener = listener;
    }
}
