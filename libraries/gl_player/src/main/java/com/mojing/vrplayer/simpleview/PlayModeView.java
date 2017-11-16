package com.mojing.vrplayer.simpleview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.RelativeLayout;


import com.mojing.vrplayer.R;
import com.mojing.vrplayer.bean.ModeItemInfo;
import com.mojing.vrplayer.bean.VideoModeType;
import com.mojing.vrplayer.interfaces.IPlayerSettingCallBack;
import com.mojing.vrplayer.simpleadapter.PlayModeGirdAdapter;

import java.util.ArrayList;

/**
 * Created by yushaochen on 2017/6/7.
 */

public class PlayModeView extends RelativeLayout{

    private Context mContext;

    private ArrayList<ModeItemInfo> leftRightDatas = new ArrayList<>();

    private ArrayList<ModeItemInfo> roundDatas = new ArrayList<>();

    private RelativeLayout mRootView;

    private GridView left_right_grid;

    private GridView round_grid;

    private PlayModeGirdAdapter leftRightGirdAdapter;

    private PlayModeGirdAdapter roundGirdAdapter;

    private int currentLeftRightMode = -1;

    private int currentRoundMode = -1;

    private Animation right_in_animation;
    private Animation right_out_animation;

    public PlayModeView(Context context) {
        super(context);
        mContext = context;
        right_in_animation = AnimationUtils.loadAnimation(mContext, R.anim.slide_right_in);
        right_out_animation = AnimationUtils.loadAnimation(mContext, R.anim.slide_right_out);
        initView();
    }

    public PlayModeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        right_in_animation = AnimationUtils.loadAnimation(mContext, R.anim.slide_right_in);
        right_out_animation = AnimationUtils.loadAnimation(mContext, R.anim.slide_right_out);
        initView();
    }

    private void initView() {
        //初始化数据
        initRoundData();
        initLeftRightData();

        mRootView = (RelativeLayout) LayoutInflater.from(mContext).inflate(R.layout.player_select_mode, null);
        left_right_grid = (GridView) mRootView.findViewById(R.id.left_right_grid);
        round_grid = (GridView) mRootView.findViewById(R.id.round_grid);

        leftRightGirdAdapter = new PlayModeGirdAdapter(mContext);
        left_right_grid.setAdapter(leftRightGirdAdapter);
        leftRightGirdAdapter.setData(leftRightDatas);

        roundGirdAdapter = new PlayModeGirdAdapter(mContext);
        round_grid.setAdapter(roundGirdAdapter);
        roundGirdAdapter.setData(roundDatas);

        setListener();

        addView(mRootView);
    }

    private void setListener() {
        left_right_grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                setSelectedLeftRightMode(leftRightDatas.get(position).getValue());
                if(null != mCallBack) {
                    mCallBack.onLeftRightModeChange(currentLeftRightMode);
                }
            }
        });
        round_grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                setSelectedRoundMode(roundDatas.get(position).getValue());
                if(null != mCallBack) {
                    mCallBack.onRoundModeChange(currentRoundMode);
                }
            }
        });
//        reset_mode_btn.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                currentLeftRightMode = -1;
//                leftRightGirdAdapter.setCurrentSelect(currentLeftRightMode);
//                currentRoundMode = -1;
//                roundGirdAdapter.setCurrentSelect(currentRoundMode);
//                if(null != mCallBack) {
//                    mCallBack.onResetMode();
//                }
//            }
//        });
//        closeBtn.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                hideView();
//                if(null != mCallBack) {
//                    mCallBack.onSettingShowChange(PlayControlView.VIEW_ID_playMode,false);
//                }
//            }
//        });
    }

    /**
     * 本地设置场景2D，3D左右，3D上下
     * @param mode
     */
    public void setSelectedLeftRightMode(int mode) {
        if(mode==currentLeftRightMode)
            return;
        currentLeftRightMode = mode;
        leftRightGirdAdapter.setCurrentSelect(currentLeftRightMode);

    }
    /**
     * 本地设置场景平面，半球，球面
     * @param mode
     */
    public void setSelectedRoundMode(int mode) {
        if(currentRoundMode==mode)
            return;
        currentRoundMode = mode;
        roundGirdAdapter.setCurrentSelect(currentRoundMode);
    }

    private void initRoundData() {
        ModeItemInfo modeItemInfo = new ModeItemInfo();
        modeItemInfo.setName("平面");
        modeItemInfo.setImage_no_focus(R.drawable.play_touch_model_icon_plane_normal);
        modeItemInfo.setImage_click(R.drawable.play_touch_model_icon_plane_click);
        modeItemInfo.setImage_no_focus_Str("play_touch_model_icon_plane_normal");
        modeItemInfo.setImage_click_Str("play_touch_model_icon_plane_click");
        modeItemInfo.setValue(VideoModeType.Mode_Rect);
        roundDatas.add(modeItemInfo);
        modeItemInfo = new ModeItemInfo();
        modeItemInfo.setName("半球");
        modeItemInfo.setImage_no_focus(R.drawable.play_touch_model_icon_180_normal);
        modeItemInfo.setImage_click(R.drawable.play_touch_model_icon_180_click);
        modeItemInfo.setImage_no_focus_Str("play_touch_model_icon_180_normal");
        modeItemInfo.setImage_click_Str("play_touch_model_icon_180_click");
        modeItemInfo.setValue(VideoModeType.Mode_Sphere180);
        roundDatas.add(modeItemInfo);
        modeItemInfo = new ModeItemInfo();
        modeItemInfo.setName("球面");
        modeItemInfo.setImage_no_focus(R.drawable.play_touch_model_icon_360_normal);
        modeItemInfo.setImage_click(R.drawable.play_touch_model_icon_360_click);
        modeItemInfo.setImage_no_focus_Str("play_touch_model_icon_360_normal");
        modeItemInfo.setImage_click_Str("play_touch_model_icon_360_click");
        modeItemInfo.setValue(VideoModeType.Mode_Sphere360);
        roundDatas.add(modeItemInfo);
    }

    private void initLeftRightData() {
        ModeItemInfo modeItemInfo = new ModeItemInfo();
        modeItemInfo.setName("2D");
        modeItemInfo.setImage_no_focus(R.drawable.play_touch_model_icon_2d_normal);
        modeItemInfo.setImage_click(R.drawable.play_touch_model_icon_2d_click);
        modeItemInfo.setImage_no_focus_Str("play_touch_model_icon_2d_normal");
        modeItemInfo.setImage_click_Str("play_touch_model_icon_2d_click");
        modeItemInfo.setValue(VideoModeType.Video2D);
        leftRightDatas.add(modeItemInfo);
        modeItemInfo = new ModeItemInfo();
        modeItemInfo.setName("3D左右");
        modeItemInfo.setImage_no_focus(R.drawable.play_touch_model_icon_3dside_normal);
        modeItemInfo.setImage_click(R.drawable.play_touch_model_icon_3dside_click);
        modeItemInfo.setImage_no_focus_Str("play_touch_model_icon_3dside_normal");
        modeItemInfo.setImage_click_Str("play_touch_model_icon_3dside_click");
        modeItemInfo.setValue(VideoModeType.VideoLR3D);
        leftRightDatas.add(modeItemInfo);
        modeItemInfo = new ModeItemInfo();
        modeItemInfo.setName("3D上下");
        modeItemInfo.setImage_no_focus(R.drawable.play_touch_model_icon_3dtop_normal);
        modeItemInfo.setImage_click(R.drawable.play_touch_model_icon_3dtop_click);
        modeItemInfo.setImage_no_focus_Str("play_touch_model_icon_3dtop_normal");
        modeItemInfo.setImage_click_Str("play_touch_model_icon_3dtop_click");
        modeItemInfo.setValue(VideoModeType.VideoUD3D);
        leftRightDatas.add(modeItemInfo);
    }

    private IPlayerSettingCallBack mCallBack;

    public void setIPlayerSettingCallBack(IPlayerSettingCallBack callBack) {

        this.mCallBack = callBack;
    }

    public void showView() {
        if(getVisibility() != VISIBLE) {
//            startAnimation(right_in_animation);
            setVisibility(VISIBLE);
        }
    }

    public void hideView() {
        if(getVisibility() == VISIBLE) {
//            startAnimation(right_out_animation);
            setVisibility(INVISIBLE);
        }
    }

}
