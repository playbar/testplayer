package com.mojing.vrplayer.simpleadapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mojing.vrplayer.R;
import com.mojing.vrplayer.bean.ModeItemInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * 模式adapter
 */

public class PlayModeGirdAdapter extends BaseAdapter {

    private Context mContext;
    private List<ModeItemInfo> mDatas = new ArrayList<>();
    public int mCurrentMode = -1;

    public PlayModeGirdAdapter(Context context) {
        mContext = context;
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
            view = layoutInflater.inflate(R.layout.player_select_mode_item, null);
            viewHolder = new ViewHolder();
            viewHolder.mode_image_icon = (ImageView) view.findViewById(R.id.mode_image_icon);
            viewHolder.tv_mode_type = (TextView) view.findViewById(R.id.mode_item_tv);

            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }

        if(!TextUtils.isEmpty(mDatas.get(position).getName())) {
            viewHolder.tv_mode_type.setText(mDatas.get(position).getName());
        }

        if(mCurrentMode == mDatas.get(position).getValue()) {
            viewHolder.mode_image_icon.setImageResource(mDatas.get(position).getImage_click());
//            viewHolder.mode_image_icon.setBackgroundResource(R.drawable.play_select_grid_item_click);
            viewHolder.tv_mode_type.setTextColor(mContext.getResources().getColor(R.color.play_select_grid_item_text_click));
        } else {
            viewHolder.mode_image_icon.setImageResource(mDatas.get(position).getImage_no_focus());
//            viewHolder.mode_image_icon.setBackgroundResource(R.drawable.play_select_grid_item_normal);
            viewHolder.tv_mode_type.setTextColor(mContext.getResources().getColor(R.color.play_select_grid_item_text_normal));
        }
        return view;
    }

    class ViewHolder {
        private ImageView mode_image_icon;
        private TextView tv_mode_type;
    }
    public void setData(List<ModeItemInfo> data){
        this.mDatas = data;
        notifyDataSetChanged();
    }
    public void setCurrentSelect(int mode){
        this.mCurrentMode = mode;
        notifyDataSetChanged();
    }

}
