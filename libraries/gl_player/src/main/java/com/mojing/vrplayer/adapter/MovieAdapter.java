package com.mojing.vrplayer.adapter;

import android.content.Context;
import android.text.TextUtils;

import com.bfmj.viewcore.adapter.GLBaseAdapter;
import com.bfmj.viewcore.view.GLGroupView;
import com.bfmj.viewcore.view.GLImageView;
import com.bfmj.viewcore.view.GLRectView;
import com.bfmj.viewcore.view.GLTextView;
import com.mojing.vrplayer.R;
import com.mojing.vrplayer.publicc.GlideUtil;
import com.mojing.vrplayer.publicc.bean.ContentInfo;
import com.mojing.vrplayer.utils.GLConst;
import com.mojing.vrplayer.utils.HeadControlUtil;
import com.mojing.vrplayer.view.MovieItemView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yushaochen on 2017/5/19.
 */

public class MovieAdapter extends GLBaseAdapter {

    private Context mContext;

    private List<ContentInfo> datas = new ArrayList<>();

    public MovieAdapter(Context context) {
        mContext = context;
    }

    public void setData(List<ContentInfo> datas) {
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
        MovieItemView itemView = (MovieItemView) convertView;
        if (itemView == null) {
            itemView = new MovieItemView(mContext);
            itemView.scale(GLConst.Player_Settings_Scale);
            if (parent != null) {
                itemView.setDepth(parent.getDepth());
            }
        }
        GLImageView imageView = (GLImageView) itemView.getView("imageView");
        GLTextView titleView = (GLTextView) itemView.getView("titleView");

        String title = datas.get(position).getTitle();

        if(!TextUtils.isEmpty(title)) {
            int length = 7;
            if(title.length() > length) {
                title = title.substring(0,6)+"...";
            }
            titleView.setText(title);
        }

        GlideUtil.loadBitmap2(mContext, imageView, datas.get(position).getPic_url());

        if(num == position) {
            imageView.setBackground("play_video_online_continue_bg");
        } else {
            imageView.setBackground("play_video_online_bg_empty");
        }
        HeadControlUtil.bindView(itemView);
        return itemView;
    }

    private int num = -1;

    public void setNum(int num) {
        this.num = num;
    }
}
