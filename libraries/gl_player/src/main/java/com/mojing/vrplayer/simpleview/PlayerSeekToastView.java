package com.mojing.vrplayer.simpleview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bfmj.sdk.util.TimeFormat;
import com.mojing.vrplayer.R;

/**
 * Created by wanghongfang on 2017/6/7.
 */
public class PlayerSeekToastView extends RelativeLayout {
    ImageView seekImg;
    TextView curTv;
    TextView durationTv;
    public PlayerSeekToastView(Context context) {
        super(context);
        initView();
    }

    public PlayerSeekToastView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView(){
        View view = LayoutInflater.from(getContext()).inflate(R.layout.player_seek_toast_view,null);
        seekImg = (ImageView) view.findViewById(R.id.seek_img);
        curTv = (TextView) view.findViewById(R.id.cur_tv);
        durationTv = (TextView) view.findViewById(R.id.duration_tv);
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        this.addView(view,params);
    }

    public void updateSeekingProcess(final int curpos, final int duration,final boolean rewind){
        String cur = TimeFormat.format(curpos/1000);
        String dur = TimeFormat.format(duration/1000);
        curTv.setText(cur);
        durationTv.setText("/"+dur);
        seekImg.setImageResource(rewind?R.drawable.play_toast_icon_rewind:R.drawable.play_toast_icon_fastforward);
    }
}
