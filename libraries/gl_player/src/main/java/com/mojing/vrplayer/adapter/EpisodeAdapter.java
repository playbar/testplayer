package com.mojing.vrplayer.adapter;

import android.content.Context;

import com.bfmj.viewcore.adapter.GLBaseAdapter;
import com.bfmj.viewcore.render.GLColor;
import com.bfmj.viewcore.view.GLGroupView;
import com.bfmj.viewcore.view.GLRectView;
import com.bfmj.viewcore.view.GLTextView;
import com.mojing.vrplayer.R;
import com.mojing.vrplayer.utils.GLConst;
import com.mojing.vrplayer.utils.HeadControlUtil;
import com.mojing.vrplayer.view.EpisodeItemView;

import java.util.ArrayList;

/**
 * Created by yushaochen on 2017/4/14.
 */

public class EpisodeAdapter extends GLBaseAdapter {

    private Context mContext;

    private ArrayList<Integer> datas = new ArrayList<>();

    public EpisodeAdapter(Context context) {
        mContext = context;
    }

    public void setData(ArrayList<Integer> datas) {
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
        EpisodeItemView itemView = (EpisodeItemView) convertView;
        if (itemView == null) {
            itemView = new EpisodeItemView(mContext);
            itemView.scale(GLConst.Player_Settings_Scale);
            if (parent != null) {
                itemView.setDepth(parent.getDepth());
            }
        }

        GLTextView textView = (GLTextView) itemView.getView("textView");


        textView.setText(datas.get(position)+"");

        if(num == datas.get(position)) {
            textView.setBackground("play_bg_control_normal_150_60");
            textView.setTextColor(new GLColor(0x008cb3));
        } else {
            textView.setBackground("play_bg_control_normal_150_60");
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
