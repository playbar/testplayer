package com.baofeng.mj.vrplayer.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baofeng.mj.smb.activity.SMBDeviceListActivirty;
import com.baofeng.mj.vrplayer.R;
import com.baofeng.mj.vrplayer.activity.FlyScreenActivity;
import com.baofeng.mj.vrplayer.business.OpenActivity;
import com.baofeng.mj.vrplayer.ftp.activity.FtpActivity;
import com.baofeng.mj.vrplayer.http.activity.HttpActivity;
import com.bumptech.glide.Glide;

/**
 * 文件上传Fragment
 */
public class FileUploadFragment extends BaseFragment {
    private TextView tv_nav_title;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Glide.with(this).onStart();//开启图片加载
        if(rootView == null){
            rootView = inflater.inflate(R.layout.layout_file_upload, container, false);
            initTopBar();//初始化顶部导航栏
            initView(rootView);
        }else{
            removeRootView();
        }
        return rootView;
    }

    private void initView(View view){
        LinearLayout layout_http = (LinearLayout) view.findViewById(R.id.layout_http);
        layout_http.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().startActivity(new Intent(getActivity(), HttpActivity.class));
            }
        });
        LinearLayout layout_ftp = (LinearLayout) view.findViewById(R.id.layout_ftp);
        layout_ftp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().startActivity(new Intent(getActivity(), FtpActivity.class));
            }
        });

        LinearLayout layout_smb = (LinearLayout) view.findViewById(R.id.layout_smb);
        layout_smb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(getActivity(), SMBDeviceListActivirty.class);
                startActivity(in);
            }
        });

        LinearLayout layout_flyscreen = (LinearLayout) view.findViewById(R.id.layout_flyscreen);
        layout_flyscreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().startActivity(new Intent(getActivity(), FlyScreenActivity.class));
            }
        });
        ImageView imageview_vr = (ImageView)view.findViewById(R.id.imageview_vr);
        imageview_vr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenActivity.openUnity(getActivity());
            }
        });
    }

    @Override
    public void onDestroyView() {
        Glide.with(this).onStop();//停止图片加载
        super.onDestroyView();
    }

    /**
     * 初始化顶部导航栏
     */
    private void initTopBar(){
        tv_nav_title = (TextView) rootView.findViewById(R.id.tv_nav_title);
        tv_nav_title.setVisibility(View.VISIBLE);
        tv_nav_title.setText("传文件");
    }
}
