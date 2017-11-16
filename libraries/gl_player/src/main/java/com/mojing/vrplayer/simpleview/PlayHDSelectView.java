package com.mojing.vrplayer.simpleview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;


import com.mojing.vrplayer.R;
import com.mojing.vrplayer.interfaces.IPlayerSettingCallBack;
import com.mojing.vrplayer.simpleadapter.PlayHDSelectAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yushaochen on 2017/6/5.
 */

public class PlayHDSelectView extends RelativeLayout{

    private Context mContext;

    private RelativeLayout mRootView;

    private ListView hdListView;

    private PlayHDSelectAdapter hdAdapter;

    private List<String> mHDtypes = new ArrayList<String>();

    private RelativeLayout closeBtn;

    private Animation right_in_animation;
    private Animation right_out_animation;

    public PlayHDSelectView(Context context) {
        super(context);
        mContext = context;
        right_in_animation = AnimationUtils.loadAnimation(mContext, R.anim.slide_right_in);
        right_out_animation = AnimationUtils.loadAnimation(mContext, R.anim.slide_right_out);
        initView();
    }

    public PlayHDSelectView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        right_in_animation = AnimationUtils.loadAnimation(mContext, R.anim.slide_right_in);
        right_out_animation = AnimationUtils.loadAnimation(mContext, R.anim.slide_right_out);
        initView();
    }

    private void initView() {
        mRootView = (RelativeLayout) LayoutInflater.from(mContext).inflate(R.layout.player_select_hd, null);
        hdListView = (ListView) mRootView.findViewById(R.id.hd_select_list);
        hdAdapter = new PlayHDSelectAdapter(mContext);
        hdListView.setAdapter(hdAdapter);

        hdListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(null != mCallBack) {
                    mCallBack.onHDChange(mHDtypes.get(position));
                    setSelectedHD(mHDtypes.get(position));
                    //选择后关闭选择view
                    hideView();

                }
            }
        });

        closeBtn = (RelativeLayout) mRootView.findViewById(R.id.close_btn);
        closeBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                hideView();
                if(null != mCallBack) {
                    mCallBack.onSettingShowChange(PlayControlView.VIEW_ID_HDSelect,false);
                }
            }
        });

        addView(mRootView);
    }

    public void setHDdata(String[] data, String defaultHD) {
        if(null == data || data.length == 0) {
            return;
        }
        mHDtypes.clear();
        for(String hd : data) {
            mHDtypes.add(hd);
        }
        hdAdapter.setData(mHDtypes);
        setSelectedHD(defaultHD);
    }

    public void setSelectedHD(String currentHD) {
        hdAdapter.setCurrentSelect(currentHD);
    }

    private IPlayerSettingCallBack mCallBack;

    public void setIPlayerSettingCallBack(IPlayerSettingCallBack callBack) {

        this.mCallBack = callBack;
    }

    public boolean isHaveHd() {
        return mHDtypes.isEmpty();
    }

    public void showView() {
        if(getVisibility() != VISIBLE) {
            startAnimation(right_in_animation);
            setVisibility(VISIBLE);
        }
    }

    public void hideView() {
        if(getVisibility() == VISIBLE) {
            startAnimation(right_out_animation);
            setVisibility(INVISIBLE);
        }
    }
}
