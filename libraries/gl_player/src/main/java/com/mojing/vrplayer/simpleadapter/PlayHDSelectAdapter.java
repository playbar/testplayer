package com.mojing.vrplayer.simpleadapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.mojing.vrplayer.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 清晰度选择adapter
 */

public class PlayHDSelectAdapter extends BaseAdapter {

    private Context mContext;
    private List<String> mHDtypes = new ArrayList<String>();
    private String mCurrentHD = "";
    public PlayHDSelectAdapter(Context context) {
        mContext = context;
    }

    @Override
    public int getCount() {
        if(mHDtypes!=null){
            return mHDtypes.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if(mHDtypes!=null){
            return mHDtypes.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        if(mHDtypes!=null)
            return position;
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;
        final ViewHolder viewHolder;
        if (convertView == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(mContext);
            view = layoutInflater.inflate(R.layout.player_select_hd_item, null);
            viewHolder = new ViewHolder();
            viewHolder.tv_video_type = (TextView) view.findViewById(R.id.hd_item_tv);
            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }
        fillData(viewHolder, mHDtypes.get(position));
        if(mCurrentHD.equals(mHDtypes.get(position))){
            viewHolder.tv_video_type.setTextColor(mContext.getResources().getColor(R.color.play_select_grid_item_text_click));
        }else {
            viewHolder.tv_video_type.setTextColor(mContext.getResources().getColor(R.color.play_select_grid_item_text_normal));
        }
        return view;
    }

    private void fillData(ViewHolder viewHolder, String type) {

        if(TextUtils.isEmpty(type)){
            return;
        }

        if(type.endsWith("k")){
            viewHolder.tv_video_type.setText(type.substring(0,type.length()-1)+"K");
        }else if(type.endsWith("p")){
            viewHolder.tv_video_type.setText(type.substring(0,type.length()-1)+"P");
        }else if(!(type.endsWith("K")||type.endsWith("P"))){
            viewHolder.tv_video_type.setText(type+"P");
        }


    }


    class ViewHolder {
        private TextView tv_video_type;
    }

    public void setData(List<String> data){
        mHDtypes.clear();
        mHDtypes.addAll(data);
        notifyDataSetChanged();
    }

    public void setCurrentSelect(String currentHD){
        if(TextUtils.isEmpty(currentHD)) return;
        this.mCurrentHD = currentHD;
        notifyDataSetChanged();
    }

}
