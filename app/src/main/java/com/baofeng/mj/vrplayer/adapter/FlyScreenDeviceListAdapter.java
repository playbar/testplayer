package com.baofeng.mj.vrplayer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.baofeng.mj.vrplayer.R;
import com.baofeng.mj.vrplayer.bean.DeviceInfo;

import java.util.List;

/**
 * 飞屏设备列表
 */
public class FlyScreenDeviceListAdapter extends BaseAdapter {
    private Context mContext;
    private List<DeviceInfo> dataList;
    private LayoutInflater inflater;

    public FlyScreenDeviceListAdapter(Context context, List<DeviceInfo> dataList) {
        mContext = context;
        this.dataList = dataList;
        inflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public Object getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.flyscreen_device_item, null);
            holder = new ViewHolder();
            holder.tv_device_name = (TextView) convertView.findViewById(R.id.tv_device_name);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        DeviceInfo deviceInfo = dataList.get(position);
        holder.tv_device_name.setText(deviceInfo.getName());

        return convertView;
    }

    class ViewHolder {
        TextView tv_device_name;
    }
}
