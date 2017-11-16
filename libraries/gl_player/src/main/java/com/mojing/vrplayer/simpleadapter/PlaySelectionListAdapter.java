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
 * 播放选集adapter
 */

public class PlaySelectionListAdapter extends BaseAdapter {

    private Context mContext;
    private List<String> mDatas = new ArrayList<String>();
    public int mCurrentPos = 0;
    private boolean isTvLeft = false;
    public PlaySelectionListAdapter(Context context) {
        mContext = context;
    }
    public void setTextLeft(boolean isLeft){
        isTvLeft = isLeft;
    }

    @Override
    public int getCount() {
        if(mDatas!=null){
            return mDatas.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if(mDatas!=null){
            return mDatas.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        if(mDatas!=null)
            return position;
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;
        ViewHolder viewHolder;
        if (convertView == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(mContext);
            if(isTvLeft) {//网格索引
                view = layoutInflater.inflate(R.layout.player_select_grid_index_item, null);
            } else {//综艺选集
                view = layoutInflater.inflate(R.layout.player_select_list_item, null);
            }
            viewHolder = new ViewHolder();
            viewHolder.tv_video_type = (TextView) view.findViewById(R.id.item_tv);

            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }

        fillData(viewHolder, ((String) getItem(position)));
        if(position==mCurrentPos){
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

        viewHolder.tv_video_type.setText(type);

    }


    class ViewHolder {
        private TextView tv_video_type;
    }
    public void setData(List<String> data){
        this.mDatas = data;
        notifyDataSetChanged();
    }
    public void setCurrentSelect(int pos){
        this.mCurrentPos = pos;
        notifyDataSetChanged();
    }

}
