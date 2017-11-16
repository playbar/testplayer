package com.mojing.vrplayer.adapter;

import android.content.Context;
import android.text.TextUtils;

import com.baofeng.mojing.input.base.MojingKeyCode;
import com.bfmj.viewcore.adapter.GLBaseAdapter;
import com.bfmj.viewcore.interfaces.GLOnKeyListener;
import com.bfmj.viewcore.view.GLGroupView;
import com.bfmj.viewcore.view.GLImageView;
import com.bfmj.viewcore.view.GLRectView;
import com.bfmj.viewcore.view.GLTextView;
import com.mojing.vrplayer.R;
import com.mojing.vrplayer.interfaces.IRePlayDialogCallBack;
import com.mojing.vrplayer.publicc.GlideUtil;
import com.mojing.vrplayer.publicc.bean.ContentInfo;
import com.mojing.vrplayer.utils.GLConst;
import com.mojing.vrplayer.view.RePlayItemView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yushaochen on 2017/4/14.
 */

public class RePlayAdapter extends GLBaseAdapter {

    private Context mContext;

    private List<ContentInfo> datas = new ArrayList<>();

    private GLImageView playIcon;

    public RePlayAdapter(Context context) {
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
    public GLRectView getGLView(final int position, GLRectView convertView, GLGroupView parent) {
        if (position >= datas.size()) {
            return null;
        }
        RePlayItemView itemView = (RePlayItemView) convertView;
        if (itemView == null) {
            itemView = new RePlayItemView(mContext);
            itemView.scale(GLConst.Dialog_Scale);
            if (parent != null) {
                itemView.setDepth(parent.getDepth());
            }
        }
        GLImageView imageView = (GLImageView) itemView.getView("imageView");
        playIcon = (GLImageView) itemView.getView("playIcon");
        playIcon.setOnKeyListener(new GLOnKeyListener() {
            @Override
            public boolean onKeyDown(GLRectView view, int keycode) {
                switch (keycode) {
                    case MojingKeyCode.KEYCODE_ENTER:
                        if(null != mCallBack) {
                            mCallBack.onSelected(datas.get(position),position);
                        }
                        break;
                }
                return false;
            }

            @Override
            public boolean onKeyUp(GLRectView view, int keycode) {
                return false;
            }

            @Override
            public boolean onKeyLongPress(GLRectView view, int keycode) {
                return false;
            }
        });
        GLTextView textView = (GLTextView) itemView.getView("textView");
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
            textView.setVisible(true);
            imageView.setBackground("play_video_online_continue_bg");
        } else {
            textView.setVisible(false);
            if(itemView.isFocused()) {
                imageView.setBackground("play_video_online_bg_hover");
                playIcon.setVisible(true);
            } else {
                imageView.setBackground("play_video_online_bg_empty");
            }
        }

        return itemView;
    }

    private int num = -1;

    public void setNum(int num) {
        this.num = num;
    }

    private IRePlayDialogCallBack mCallBack;

    public void setIRePlayDialogCallBack(IRePlayDialogCallBack callBack) {
        mCallBack = callBack;
    }
}
