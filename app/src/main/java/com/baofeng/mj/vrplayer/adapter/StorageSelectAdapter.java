package com.baofeng.mj.vrplayer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.baofeng.mj.vrplayer.R;
import com.baofeng.mj.vrplayer.bean.NewFile;

import java.util.List;

/**
 * 存储选择适配器
 */
public class StorageSelectAdapter extends BaseAdapter {
    private LayoutInflater layoutInflater;
    private List<NewFile> fileList;

    public StorageSelectAdapter(Context context, List<NewFile> fileList) {
        this.fileList = fileList;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return fileList == null ? 0 : fileList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object getItem(int position) {
        return fileList.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MViewHolder viewHolder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.dialog_storage_select_item, null);
            viewHolder = new MViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (MViewHolder) convertView.getTag();
        }

        final NewFile newFile = fileList.get(position);
        viewHolder.tv_name.setText(newFile.getName());

        return convertView;
    }

    public class MViewHolder {
        private TextView tv_name;

        public MViewHolder(View view) {
            tv_name = (TextView) view.findViewById(R.id.tv_name);
        }
    }
}