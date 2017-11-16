package com.baofeng.mj.vrplayer.widget;

import android.content.Context;
import android.graphics.Color;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.baofeng.mj.vrplayer.R;
import com.baofeng.mj.vrplayer.util.PixelsUtil;
import com.baofeng.mj.vrplayer.util.ReportUtil;
import com.baofeng.mj.util.publicutil.VideoTypeUtil;

/**
 * Created by liuchuanchi on 2016/5/17.
 * 视频类型选择popWindow
 */
public class VideoTypeSelectPop extends PopupWindow implements View.OnClickListener{
    private Context context;
    private View mContentView;
    private LinearLayout ll_2d;
    private ImageView iv_2d;
    private TextView tv_2d;
    private LinearLayout ll_3d_left_right;
    private ImageView iv_3d_left_right;
    private TextView tv_3d_left_right;
    private LinearLayout ll_3d_top_bottom;
    private ImageView iv_3d_top_bottom;
    private TextView tv_3d_top_bottom;
    private LinearLayout ll_plane;
    private ImageView iv_plane;
    private TextView tv_plane;
    private LinearLayout ll_peskoe;
    private ImageView iv_peskoe;
    private TextView tv_peskoe;
    private LinearLayout ll_sphere;
    private ImageView iv_sphere;
    private TextView tv_sphere;
    private String videoPath;//视频地址
    private int videoPanorama;//全景类型
    private int videoDimension;//视频模式
    private VideoTypeCallback videoTypeCallback;
    private boolean hasReport;//true已经上报过，false相反

    public VideoTypeSelectPop(Context context) {
        super(context);
        initView(context);//初始化控件
        this.setContentView(mContentView);
        this.setWidth(PixelsUtil.dip2px(context, 180));
        this.setHeight(PixelsUtil.dip2px(context, 110));
        this.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.shape_transparent_bg));//设置背景
        this.setAnimationStyle(R.style.popAnimation);//设置动画
        this.setFocusable(true); //设置弹出窗体可点击
        this.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
                if(!hasReport){
                    ReportUtil.reportVideoModeChange(null);
                }
            }
        });
        mContentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissPop();
            }
        });
    }

    /**
     * 初始化控件
     */
    private void initView(Context context){
        this.context = context;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mContentView = inflater.inflate(R.layout.pop_video_type_select, null);
        ll_2d = (LinearLayout) mContentView.findViewById(R.id.ll_2d);
        iv_2d = (ImageView) mContentView.findViewById(R.id.iv_2d);
        tv_2d = (TextView) mContentView.findViewById(R.id.tv_2d);
        ll_3d_left_right = (LinearLayout) mContentView.findViewById(R.id.ll_3d_left_right);
        iv_3d_left_right = (ImageView) mContentView.findViewById(R.id.iv_3d_left_right);
        tv_3d_left_right = (TextView) mContentView.findViewById(R.id.tv_3d_left_right);
        ll_3d_top_bottom = (LinearLayout) mContentView.findViewById(R.id.ll_3d_top_bottom);
        iv_3d_top_bottom = (ImageView) mContentView.findViewById(R.id.iv_3d_top_bottom);
        tv_3d_top_bottom = (TextView) mContentView.findViewById(R.id.tv_3d_top_bottom);
        ll_plane = (LinearLayout) mContentView.findViewById(R.id.ll_plane);
        iv_plane = (ImageView) mContentView.findViewById(R.id.iv_plane);
        tv_plane = (TextView) mContentView.findViewById(R.id.tv_plane);
        ll_peskoe = (LinearLayout) mContentView.findViewById(R.id.ll_peskoe);
        iv_peskoe = (ImageView) mContentView.findViewById(R.id.iv_peskoe);
        tv_peskoe = (TextView) mContentView.findViewById(R.id.tv_peskoe);
        ll_sphere = (LinearLayout) mContentView.findViewById(R.id.ll_sphere);
        iv_sphere = (ImageView) mContentView.findViewById(R.id.iv_sphere);
        tv_sphere = (TextView) mContentView.findViewById(R.id.tv_sphere);
        ll_2d.setOnClickListener(this);
        ll_3d_left_right.setOnClickListener(this);
        ll_3d_top_bottom.setOnClickListener(this);
        ll_plane.setOnClickListener(this);
        ll_peskoe.setOnClickListener(this);
        ll_sphere.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id == R.id.ll_2d){//2D
            if(videoTypeIsSupport(videoPanorama, VideoTypeUtil.dimension_2d)){
                videoDimension = VideoTypeUtil.dimension_2d;
                responseClick();
            }
        }else if(id == R.id.ll_3d_left_right){//3D左右
            videoDimension = VideoTypeUtil.dimension_3d_left_right;
            responseClick();
        }else if(id == R.id.ll_3d_top_bottom){//3D上下
            if(videoTypeIsSupport(videoPanorama, VideoTypeUtil.dimension_3d_top_bottom)){
                videoDimension = VideoTypeUtil.dimension_3d_top_bottom;
                responseClick();
            }
        }else if(id == R.id.ll_plane){//平面
            videoPanorama = VideoTypeUtil.panorama_normal;
            responseClick();
        }else if(id == R.id.ll_peskoe){//半球
            if(videoTypeIsSupport(VideoTypeUtil.panorama_180, videoDimension)){
                videoPanorama = VideoTypeUtil.panorama_180;
                responseClick();
            }
        }else if(id == R.id.ll_sphere){//球面
            videoPanorama = VideoTypeUtil.panorama_360;
            responseClick();
        }
    }

    /**
     * 视频类型：true支持，false不支持
     */
    private boolean videoTypeIsSupport(int videoPanorama, int videoDimension){
        if(videoPanorama == VideoTypeUtil.panorama_180){
            if(videoDimension == VideoTypeUtil.dimension_2d){
                Toast.makeText(context, "暂不支持2D 半球", Toast.LENGTH_SHORT).show();
                return false;
            }else if(videoDimension == VideoTypeUtil.dimension_3d_top_bottom){
                Toast.makeText(context, "暂不支持3D上下 半球", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        return true;
    }

    /**
     * 响应点击事件
     */
    private void responseClick(){
        updateUI();//更新UI
        int videoType = VideoTypeUtil.getVideoType(videoPanorama, videoDimension);//获取视频类型
        VideoTypeUtil.saveVideoType(context, videoPath, videoType);//保存视频类型
        if(videoTypeCallback != null){
            videoTypeCallback.select(videoType);
        }
        ReportUtil.reportVideoModeChange(String.valueOf(videoType));
        hasReport = true;
        dismissPop();
    }

    /**
     * 更新UI
     */
    private void updateUI(){
        int colorNormal = Color.WHITE;
        int colorClick = context.getResources().getColor(R.color.colorDarkBlueText);
        if(videoDimension == VideoTypeUtil.dimension_2d){
            iv_2d.setImageResource(R.mipmap.mj_vrplayer_video_model_icon_2d_click);
            tv_2d.setTextColor(colorClick);
        }else{
            iv_2d.setImageResource(R.mipmap.mj_vrplayer_video_model_icon_2d_normal);
            tv_2d.setTextColor(colorNormal);
        }
        if(videoDimension == VideoTypeUtil.dimension_3d_left_right){
            iv_3d_left_right.setImageResource(R.mipmap.mj_vrplayer_video_model_icon_3dside_click);
            tv_3d_left_right.setTextColor(colorClick);
        }else{
            iv_3d_left_right.setImageResource(R.mipmap.mj_vrplayer_video_model_icon_3dside_normal);
            tv_3d_left_right.setTextColor(colorNormal);
        }
        if(videoDimension == VideoTypeUtil.dimension_3d_top_bottom){
            iv_3d_top_bottom.setImageResource(R.mipmap.mj_vrplayer_video_model_icon_3dtop_click);
            tv_3d_top_bottom.setTextColor(colorClick);
        }else{
            iv_3d_top_bottom.setImageResource(R.mipmap.mj_vrplayer_video_model_icon_3dtop_normal);
            tv_3d_top_bottom.setTextColor(colorNormal);
        }
        if(videoPanorama == VideoTypeUtil.panorama_normal){
            iv_plane.setImageResource(R.mipmap.mj_vrplayer_video_model_icon_plane_click);
            tv_plane.setTextColor(colorClick);
        }else{
            iv_plane.setImageResource(R.mipmap.mj_vrplayer_video_model_icon_plane_normal);
            tv_plane.setTextColor(colorNormal);
        }
        if(videoPanorama == VideoTypeUtil.panorama_180){
            iv_peskoe.setImageResource(R.mipmap.mj_vrplayer_video_model_icon_180_click);
            tv_peskoe.setTextColor(colorClick);
        }else{
            iv_peskoe.setImageResource(R.mipmap.mj_vrplayer_video_model_icon_180_normal);
            tv_peskoe.setTextColor(colorNormal);
        }
        if(videoPanorama == VideoTypeUtil.panorama_360){
            iv_sphere.setImageResource(R.mipmap.mj_vrplayer_video_model_icon_360_click);
            tv_sphere.setTextColor(colorClick);
        }else{
            iv_sphere.setImageResource(R.mipmap.mj_vrplayer_video_model_icon_360_normal);
            tv_sphere.setTextColor(colorNormal);
        }
    }

    public void showPop(View parent, String videoPath, int videoType, VideoTypeCallback videoTypeCallback){
        if(!isShowing()){
            this.videoPath = videoPath;
            int[] result = VideoTypeUtil.getPanoramaAndDimension(videoType);
            this.videoPanorama = result[0];
            this.videoDimension = result[1];
            this.videoTypeCallback = videoTypeCallback;
            updateUI();//更新UI
            showAsDropDown(parent);
            hasReport = false;
        }
    }

    public void dismissPop(){
        if(isShowing()){
            dismiss();
        }
    }

    public interface VideoTypeCallback{
        void select(int videoType);
    }
}
