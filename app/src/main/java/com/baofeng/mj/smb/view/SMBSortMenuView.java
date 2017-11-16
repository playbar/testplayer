package com.baofeng.mj.smb.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.baofeng.mj.smb.interfaces.ISMBSortChangeListener;
import com.baofeng.mj.smb.util.SMBViewTypeUtil;
import com.baofeng.mj.unity.launcher.SharedPreferencesUtil;
import com.baofeng.mj.vrplayer.R;

/**
 * Created by panxin on 2017/8/11.
 */

public class SMBSortMenuView extends LinearLayout {

    private Context mContext;
    private RelativeLayout layout_sort_time;
    private RelativeLayout layout_sort_name;
    private RelativeLayout layout_sort_size;
    private TextView textview_sort_time;
    private TextView textview_sort_name;
    private TextView textview_sort_size;
    private ImageView imageview_sort_time_icon;
    private ImageView imageview_sort_name_icon;
    private ImageView imageview_sort_size_icon;



    public SMBSortMenuView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        initView();
        initListener();
    }


    private void initView() {
        LinearLayout layout_menu = (LinearLayout) LayoutInflater.from(mContext).inflate(R.layout.layout_smb_sort_menu, null);
        layout_sort_time = (RelativeLayout)layout_menu.findViewById(R.id.layout_sort_time);
        layout_sort_name = (RelativeLayout)layout_menu.findViewById(R.id.layout_sort_name);
        layout_sort_size = (RelativeLayout)layout_menu.findViewById(R.id.layout_sort_size);
        textview_sort_time = (TextView) layout_menu.findViewById(R.id.textview_sort_time);
        textview_sort_name = (TextView)layout_menu.findViewById(R.id.textview_sort_name);
        textview_sort_size = (TextView)layout_menu.findViewById(R.id.textview_sort_size);
        imageview_sort_time_icon = (ImageView) layout_menu.findViewById(R.id.imageview_sort_time_icon);
        imageview_sort_name_icon = (ImageView) layout_menu.findViewById(R.id.imageview_sort_name_icon);
        imageview_sort_size_icon = (ImageView) layout_menu.findViewById(R.id.imageview_sort_size_icon);
        addView(layout_menu);
        chooseSortType(SharedPreferencesUtil.getInstance().getInt(SMBViewTypeUtil.KEY_SMB_SORT_TYPE,0));
        setVisibility(View.GONE);
    }

    private void initListener(){
        layout_sort_time.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
             //   UmengReport.reportEvent("smb_play","name","order");
                chooseSortType(SMBViewTypeUtil.TYPE_SORT_TIME);
            }
        });

        layout_sort_name.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
              //  UmengReport.reportEvent("smb_play","name","order");
                chooseSortType(SMBViewTypeUtil.TYPE_SORT_NAME);
            }
        });

        layout_sort_size.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
              //  UmengReport.reportEvent("smb_play","name","order");
                chooseSortType(SMBViewTypeUtil.TYPE_SORT_SIZE);
            }
        });
    }

    private void chooseSortType(int type){
        SharedPreferencesUtil.getInstance().setInt(SMBViewTypeUtil.KEY_SMB_SORT_TYPE,type);
        switch (type){
            case SMBViewTypeUtil.TYPE_SORT_TIME:
                textview_sort_time.setTextColor(mContext.getResources().getColor(R.color.smb_login_button_purple));
                textview_sort_name.setTextColor(mContext.getResources().getColor(R.color.black));
                textview_sort_size.setTextColor(mContext.getResources().getColor(R.color.black));
                imageview_sort_time_icon.setVisibility(View.VISIBLE);
                imageview_sort_name_icon.setVisibility(View.GONE);
                imageview_sort_size_icon.setVisibility(View.GONE);
                break;
            case SMBViewTypeUtil.TYPE_SORT_NAME:
                textview_sort_time.setTextColor(mContext.getResources().getColor(R.color.black));
                textview_sort_name.setTextColor(mContext.getResources().getColor(R.color.smb_login_button_purple));
                textview_sort_size.setTextColor(mContext.getResources().getColor(R.color.black));
                imageview_sort_time_icon.setVisibility(View.GONE);
                imageview_sort_name_icon.setVisibility(View.VISIBLE);
                imageview_sort_size_icon.setVisibility(View.GONE);
                break;
            case SMBViewTypeUtil.TYPE_SORT_SIZE:
                textview_sort_time.setTextColor(mContext.getResources().getColor(R.color.black));
                textview_sort_name.setTextColor(mContext.getResources().getColor(R.color.black));
                textview_sort_size.setTextColor(mContext.getResources().getColor(R.color.smb_login_button_purple));
                imageview_sort_time_icon.setVisibility(View.GONE);
                imageview_sort_name_icon.setVisibility(View.GONE);
                imageview_sort_size_icon.setVisibility(View.VISIBLE);
                break;
        }

        if(mISMBSortChangeListener!=null){
            mISMBSortChangeListener.changeSort();
        }
    }


    private ISMBSortChangeListener mISMBSortChangeListener;
    public void setISMBSortChangeListener(ISMBSortChangeListener listener){
        this.mISMBSortChangeListener = listener;
    }
}
