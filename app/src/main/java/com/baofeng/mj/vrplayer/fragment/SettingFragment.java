package com.baofeng.mj.vrplayer.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baofeng.mj.sdk.gvr.vrcore.entity.GlassesNetBean;
import com.baofeng.mj.sdk.gvr.vrcore.utils.GlassesManager;
import com.baofeng.mj.vrplayer.R;
import com.baofeng.mj.vrplayer.business.LocalVideoPathBusiness;
import com.baofeng.mj.vrplayer.business.OpenActivity;
import com.baofeng.mj.vrplayer.business.SpPublicBusiness;
import com.baofeng.mj.vrplayer.widget.StorageDeleteDialog;
import com.bumptech.glide.Glide;

/**
 * 设置Fragment
 */
public class SettingFragment extends BaseFragment implements View.OnClickListener {
    private TextView tv_nav_title;
    private LinearLayout ll_glasses_select;
    private TextView tv_glasses_name;
    private LinearLayout ll_storage_container;
    private ImageView imageview_vr;
    private StorageDeleteDialog storageDeleteDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Glide.with(this).onStart();//开启图片加载
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.layout_setting, container, false);
            initView();//初始化控件
            initTopBar();//初始化顶部导航栏
        } else {
            removeRootView();
        }
        return rootView;
    }

    @Override
    public void onDestroyView() {
        Glide.with(this).onStop();//停止图片加载
        super.onDestroyView();
    }

    @Override
    public void onResume() {
        super.onResume();
        showGlassesText();//显示眼镜文案
        ll_storage_container.removeAllViews();//移除所有条目
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        createDefaultStorageItem(inflater);//创建默认存储条目
        for (String customVideoDir : LocalVideoPathBusiness.getAllCustomDir(getActivity(), true)) {
            createCustomStorageItem(inflater, customVideoDir);//创建自定义存储条目
        }
    }

    /**
     * 初始化控件
     */
    private void initView() {
        ll_glasses_select = (LinearLayout) rootView.findViewById(R.id.ll_glasses_select);
        tv_glasses_name = (TextView) rootView.findViewById(R.id.tv_glasses_name);
        ll_storage_container = (LinearLayout) rootView.findViewById(R.id.ll_storage_container);
        imageview_vr = (ImageView) rootView.findViewById(R.id.imageview_vr);
        ll_glasses_select.setOnClickListener(this);
        imageview_vr.setOnClickListener(this);
    }

    /**
     * 初始化顶部导航栏
     */
    private void initTopBar() {
        tv_nav_title = (TextView) rootView.findViewById(R.id.tv_nav_title);
        tv_nav_title.setVisibility(View.VISIBLE);
        tv_nav_title.setText("设置");
    }

    /**
     * 创建默认存储条目
     */
    private void createDefaultStorageItem(LayoutInflater inflater) {
        final View view = inflater.inflate(R.layout.layout_setting_storage_item, null);
        final TextView tv_storage_name = (TextView) view.findViewById(R.id.tv_storage_name);
        final TextView tv_storage_des = (TextView) view.findViewById(R.id.tv_storage_des);
        final LinearLayout ll_storage_select = (LinearLayout) view.findViewById(R.id.ll_storage_select);
        final ImageView iv_storage_select = (ImageView) view.findViewById(R.id.iv_storage_select);
        tv_storage_name.setText("手机内部存储");
        tv_storage_des.setText("手机自带存储卡，勾选后扫描卡内视频");
        if (SpPublicBusiness.getInstance().getDefaultStorageSearch(getActivity())) {
            iv_storage_select.setImageResource(R.mipmap.mj_vrplayer_check_select_icon);
        } else {
            iv_storage_select.setImageResource(R.mipmap.mj_vrplayer_check_normal_icon);
        }
        ll_storage_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SpPublicBusiness.getInstance().getDefaultStorageSearch(getActivity())) {
                    SpPublicBusiness.getInstance().setDefaultStorageSearch(getActivity(), false);
                    iv_storage_select.setImageResource(R.mipmap.mj_vrplayer_check_normal_icon);
                } else {
                    SpPublicBusiness.getInstance().setDefaultStorageSearch(getActivity(), true);
                    iv_storage_select.setImageResource(R.mipmap.mj_vrplayer_check_select_icon);
                }
            }
        });
        ll_storage_container.addView(view);//加入父容器
    }

    /**
     * 创建自定义存储条目
     */
    private void createCustomStorageItem(LayoutInflater inflater, final String customVideoDir) {
        final View view = inflater.inflate(R.layout.layout_setting_storage_item, null);
        final TextView tv_storage_name = (TextView) view.findViewById(R.id.tv_storage_name);
        final TextView tv_storage_des = (TextView) view.findViewById(R.id.tv_storage_des);
        final LinearLayout ll_storage_select = (LinearLayout) view.findViewById(R.id.ll_storage_select);
        final ImageView iv_storage_select = (ImageView) view.findViewById(R.id.iv_storage_select);
        tv_storage_name.setText(customVideoDir);
        tv_storage_des.setText("长按可删除该条添加记录");
        if (SpPublicBusiness.getInstance().getCustomStorageSearch(getActivity(), customVideoDir)) {
            iv_storage_select.setImageResource(R.mipmap.mj_vrplayer_check_select_icon);
        } else {
            iv_storage_select.setImageResource(R.mipmap.mj_vrplayer_check_normal_icon);
        }
        ll_storage_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SpPublicBusiness.getInstance().getCustomStorageSearch(getActivity(), customVideoDir)) {
                    SpPublicBusiness.getInstance().setCustomStorageSearch(getActivity(), customVideoDir, false);
                    iv_storage_select.setImageResource(R.mipmap.mj_vrplayer_check_normal_icon);
                } else {
                    SpPublicBusiness.getInstance().setCustomStorageSearch(getActivity(), customVideoDir, true);
                    iv_storage_select.setImageResource(R.mipmap.mj_vrplayer_check_select_icon);
                }
            }
        });
        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {//长按删除
                if (storageDeleteDialog == null) {
                    storageDeleteDialog = new StorageDeleteDialog(getActivity());
                }
                storageDeleteDialog.showDialog(new StorageDeleteDialog.MyDialogInterface() {
                    @Override
                    public void dialogCallBack() {
                        SpPublicBusiness.getInstance().removeCustomStorageSearch(getActivity(), customVideoDir);
                        LocalVideoPathBusiness.removeCustomDir(customVideoDir);
                        ll_storage_container.removeView(view);
                    }
                });
                return true;
            }
        });
        ll_storage_container.addView(view);//加入父容器
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_glasses_select://VR眼镜选择
                OpenActivity.openGlassesSelect(getActivity());
                break;
            case R.id.imageview_vr://进入unity
                OpenActivity.openUnity(getActivity());
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {//VR眼镜选择返回
            showGlassesText();//显示眼镜文案
        }
    }

    /**
     * 显示眼镜文案
     */
    private void showGlassesText() {
        GlassesNetBean glassesNetBean = GlassesManager.getGlassesNetBean();
        if (glassesNetBean != null && glassesNetBean.isSelected()) {
            tv_glasses_name.setText(glassesNetBean.getGlass_name());
        } else {
            tv_glasses_name.setText("未设置");
        }
    }
}
