package com.mojing.vrplayer.adapter;

import android.content.Context;
import android.media.MediaMetadataRetriever;
import android.text.TextUtils;
import android.util.Log;

import com.bfmj.sdk.util.StringUtils;
import com.bfmj.viewcore.adapter.GLBaseAdapter;
import com.bfmj.viewcore.view.GLGroupView;
import com.bfmj.viewcore.view.GLImageView;
import com.bfmj.viewcore.view.GLRectView;
import com.bfmj.viewcore.view.GLTextView;
import com.mojing.vrplayer.R;
import com.mojing.vrplayer.publicc.GlideUtil;
import com.mojing.vrplayer.publicc.VideoTypeUtil;
import com.mojing.vrplayer.publicc.bean.LocalVideoBean;
import com.mojing.vrplayer.utils.GLColorUitl;
import com.mojing.vrplayer.utils.GLConst;
import com.mojing.vrplayer.utils.HeadControlUtil;
import com.mojing.vrplayer.utils.ImageUtil;
import com.mojing.vrplayer.utils.ThreadPoolUtil;
import com.mojing.vrplayer.view.LocalSourceItemView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yushaochen on 2017/5/19.
 */

public class LocalSourceAdapter extends GLBaseAdapter {

    private Context mContext;

    private List<LocalVideoBean> datas = new ArrayList<>();

    public LocalSourceAdapter(Context context) {
        mContext = context;
    }

    public void setData(List<LocalVideoBean> datas) {
        this.datas.clear();
        if(null == datas) {
            return;
        }

        this.datas.addAll(datas);
        initDuration();
        notifyDataSetChanged();
    }

    public void initDuration(){
        ThreadPoolUtil.runThread(new Runnable() {
            @Override
            public void run() {
                MediaMetadataRetriever retriever = new MediaMetadataRetriever();
                for(LocalVideoBean localVideoBean:datas){
                    if(!TextUtils.isEmpty(localVideoBean.path)){
                        localVideoBean.videoDuration = ImageUtil.getVideoDuration(retriever, localVideoBean.path);
                    }
                }
                try {
                    retriever.release();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
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
        LocalSourceItemView itemView = (LocalSourceItemView) convertView;
        if (itemView == null) {
            itemView = new LocalSourceItemView(mContext);
            itemView.scale(GLConst.Player_Settings_Scale);
            if (parent != null) {
                itemView.setDepth(parent.getDepth());
            }
        }
        GLImageView imageView = (GLImageView) itemView.getView("imageView");
        GLImageView imageIcon = (GLImageView) itemView.getView("imageIcon");
        GLTextView titleView = (GLTextView) itemView.getView("titleView");
        GLTextView durationView = (GLTextView) itemView.getView("durationView");
        String title = datas.get(position).name;


        if(!TextUtils.isEmpty(title)) {
            int length = 10;
            if(title.length() > length) {
                int i = title.lastIndexOf(".");
              //  if(i>=0){
                    title = title.substring(0,7)+"...";//+title.substring(i-5);
             //   }
            }
            titleView.setText(title);
        }
        durationView.setText(StringUtils.getStringTime(datas.get(position).videoDuration*1000));
       // imageView.setImage(R.drawable.imageload_defaut_img);
        GlideUtil.loadBitmap3(mContext, imageView, datas.get(position).thumbPath);

        if(num == position) {
            imageView.setBackground("play_video_local_bg_click");
            imageIcon.setVisible(true);
            titleView.setTextColor(GLColorUitl.ClickTextColor);
        } else {
            imageView.setBackground("play_video_online_bg_empty");
            imageIcon.setVisible(false);
            titleView.setTextColor(GLColorUitl.DefaultTextColor);
        }

        HeadControlUtil.bindView(itemView);
        return itemView;
    }

    private int num = -1;

    public void setNum(int num) {
        this.num = num;
    }
}
