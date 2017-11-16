package com.baofeng.mj.vrplayer.util;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import com.baofeng.mj.vrplayer.MyAppliaction;
import com.baofeng.mj.vrplayer.R;
import com.baofeng.mj.vrplayer.activity.MainActivity;
import com.baofeng.mj.vrplayer.fragment.FileUploadFragment;
import com.baofeng.mj.vrplayer.fragment.SettingFragment;
import com.baofeng.mj.vrplayer.fragment.VideoFragment;
import com.baofeng.mj.vrplayer.widget.MyFragmentTabHost;

/**
 * 底部tab工具类
 */
public class BottomTabUtil {
    public static int VIDEO;//视频
    public static int FILE_UPLOAD;//文件上传
    public static int SETTING;//设置

    /**
     * 初始化底部tab
     * @param mContext 上下文
     * @param myFragmentTabHost FragmentTabHost
     */
    public static void initBottomTab(Context mContext, MyFragmentTabHost myFragmentTabHost) {
        VIDEO = 0;
        FILE_UPLOAD = 1;
        SETTING = 2;

        Bundle bundleVideo = new Bundle();
        bundleVideo.putInt("currentTab", VIDEO);
        Bundle bundleFileUpload = new Bundle();
        bundleFileUpload.putInt("currentTab", FILE_UPLOAD);
        Bundle bundleSetting = new Bundle();
        bundleSetting.putInt("currentTab", SETTING);

        TabHost.TabSpec tabSpecVideo = newTabSpec(mContext, myFragmentTabHost, "视频", R.drawable.mj_vrplayer_selector_bottom_tab_video, VIDEO);
        TabHost.TabSpec tabSpecFileUpload = newTabSpec(mContext, myFragmentTabHost, "传文件", R.drawable.mj_vrplayer_selector_bottom_tab_upload, FILE_UPLOAD);
        TabHost.TabSpec tabSpecSetting = newTabSpec(mContext, myFragmentTabHost, "设置", R.drawable.mj_vrplayer_selector_bottom_tab_setting, SETTING);
        myFragmentTabHost.addTab(tabSpecVideo, VideoFragment.class, bundleVideo);
        myFragmentTabHost.addTab(tabSpecFileUpload, FileUploadFragment.class, bundleFileUpload);
        myFragmentTabHost.addTab(tabSpecSetting, SettingFragment.class, bundleSetting);
    }

    /**
     * 创建TabHost.TabSpec
     * @param mContext 上下文
     * @param myFragmentTabHost FragmentTabHost
     * @param resName 名称
     * @param resIcon 图标
     */
    private static TabHost.TabSpec newTabSpec(Context mContext, MyFragmentTabHost myFragmentTabHost, String resName, int resIcon, int position){
        View indicator = View.inflate(mContext, R.layout.layout_bottom_tab, null);
        TextView tv_name = (TextView) indicator.findViewById(R.id.tv_name);
        ImageView iv_icon = (ImageView) indicator.findViewById(R.id.iv_icon);
        if("视频".equals(resName)){
            MainActivity.tabNewVideoTAG= (ImageView) indicator.findViewById(R.id.iv_tag);
            if(MyAppliaction.getInstance().getmFileUploadBusiness()!=null&&MyAppliaction.getInstance().getmFileUploadBusiness().isShowNewOnTab()){
                MainActivity.tabNewVideoTAG.setVisibility(View.VISIBLE);
            }else{
                MainActivity.tabNewVideoTAG.setVisibility(View.GONE);
            }
        }
        tv_name.setText(resName);
        iv_icon.setImageResource(resIcon);

        TabHost.TabSpec tabSpec = myFragmentTabHost.newTabSpec(getFragmentTabTag(mContext, position));
        tabSpec.setIndicator(indicator);

        return tabSpec;
    }

    public static String getFragmentTabTag(Context mContext, int position){
        return mContext.toString() + position;
    }
}
