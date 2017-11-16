package com.baofeng.mj.smb.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.baofeng.mj.smb.bean.SMBFolderBean;
import com.baofeng.mj.smb.util.SMBViewTypeUtil;
import com.baofeng.mj.util.fileutil.FileSizeUtil;
import com.baofeng.mj.vrplayer.R;

import java.util.List;


/**
 * Created by panxin on 2017/4/18.
 */

public class SMBFolderAdapter extends BaseAdapter {
    private Context context;
    private List<SMBFolderBean> list;
    public SMBFolderAdapter(Context context) {
        this.context = context;
    }

    public void setData(List<SMBFolderBean> list){
        this.list = list;
        notifyDataSetChanged();
    }

    public List<SMBFolderBean> getData(){
        return list;
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public SMBFolderBean getItem(int position) {
        return list.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_smb_file, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final SMBFolderBean bean = list.get(position);
        if(bean.type == SMBViewTypeUtil.TYPE_FILE_FILE){
            viewHolder.imageview_icon.setImageResource(R.mipmap.local_file_icon);
            viewHolder.textview_size.setText(FileSizeUtil.formatFileSize(bean.size));
        }else if(bean.type == SMBViewTypeUtil.TYPE_FILE_DIR){
            viewHolder.imageview_icon.setImageResource(R.mipmap.local_folder_icon);
            viewHolder.textview_size.setText(bean.size+context.getResources().getString(R.string.mj_share_smb_file_unit));
        }else if(bean.type == SMBViewTypeUtil.TYPE_FILE_VIDEO){
            viewHolder.imageview_icon.setImageResource(R.mipmap.local_video_icon);
            viewHolder.textview_size.setText(FileSizeUtil.formatFileSize(bean.size));
        }else if(bean.type == SMBViewTypeUtil.TYPE_FILE_SRT){
            viewHolder.imageview_icon.setImageResource(R.mipmap.local_text_icon);
            viewHolder.textview_size.setText(FileSizeUtil.formatFileSize(bean.size));
        }
        viewHolder.textview_name.setText(bean.filename);

        return convertView;
    }

    public class ViewHolder {
        private ImageView imageview_icon;
        private TextView textview_name;
        private TextView textview_size;

        public ViewHolder(View view) {
            imageview_icon = (ImageView) view.findViewById(R.id.imageview_icon);
            textview_name = (TextView) view.findViewById(R.id.textview_name);
            textview_size = (TextView) view.findViewById(R.id.textview_size);
        }
    }
}