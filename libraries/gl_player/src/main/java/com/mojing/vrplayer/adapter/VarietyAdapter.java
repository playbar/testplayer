package com.mojing.vrplayer.adapter;

import android.content.Context;
import com.bfmj.viewcore.adapter.GLBaseAdapter;
import com.bfmj.viewcore.render.GLColor;
import com.bfmj.viewcore.view.GLGroupView;
import com.bfmj.viewcore.view.GLRectView;
import com.bfmj.viewcore.view.GLTextView;
import com.mojing.vrplayer.R;
import com.mojing.vrplayer.publicc.bean.VideoDetailBean;
import com.mojing.vrplayer.utils.GLConst;
import com.mojing.vrplayer.utils.HeadControlUtil;
import com.mojing.vrplayer.view.VarietyItemView;

import java.util.ArrayList;

/**
 * Created by yushaochen on 2017/4/17.
 */

public class VarietyAdapter extends GLBaseAdapter {

    private Context mContext;

    private ArrayList<VideoDetailBean.AlbumsBean.VideosBean> datas = new ArrayList<>();

    public VarietyAdapter(Context context) {
        mContext = context;
    }

    public void setData(ArrayList<VideoDetailBean.AlbumsBean.VideosBean> datas) {
        this.datas.clear();
        if(null == datas) {
            return;
        }

        this.datas.addAll(datas);
        notifyDataSetChanged();
    }

    @Override
    public void addIndex(int i, GLRectView glRectView) {

    }

    @Override
    public void removeIndex(int i) {

    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Object getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public GLRectView getGLView(int position, GLRectView convertView, GLGroupView parent) {
        if (position >= datas.size()) {
            return null;
        }
        VarietyItemView itemView = (VarietyItemView) convertView;
        if (itemView == null) {
            itemView = new VarietyItemView(mContext);
            itemView.scale(GLConst.Player_Settings_Scale);
            if (parent != null) {
                itemView.setDepth(parent.getDepth());
            }
        }

        GLTextView textView = (GLTextView) itemView.getView("textView");
//
//        textView.setText(datas.get(position).getSeq()+"  "+datas.get(position).getTitle());

        itemView.setText(datas.get(position).getTitle());

        if(num == datas.get(position).getSeq()) {
            textView.setBackground("play_bg_control_hover_400_60");
            textView.setTextColor(new GLColor(0x008cb3));
        } else {
            textView.setBackground("play_bg_control_hover_400_60");
            textView.setTextColor(new GLColor(0x888888));
        }
        HeadControlUtil.bindView(itemView);
        return itemView;
    }

    private int num;

    public void setNum(int num) {
        this.num = num;
    }
}
