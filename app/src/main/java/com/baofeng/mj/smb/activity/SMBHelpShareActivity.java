package com.baofeng.mj.smb.activity;

import android.os.Bundle;
import android.view.View;


import com.baofeng.mj.ui.view.TitleBar;
import com.baofeng.mj.vrplayer.R;
import com.baofeng.mj.vrplayer.activity.BaseActivity;

/**
 * Created by panxin on 2017/8/15.
 */

public class SMBHelpShareActivity extends BaseActivity {

    private TitleBar mTitleBar;

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        initView();
//    }


    public void initView(){
        setContentView(R.layout.activity_smb_help_share);
        mTitleBar = (TitleBar) findViewById(R.id.rl_title_bar);
        mTitleBar.setTitleBarTitle(getResources().getString(R.string.mj_share_smb_help_share));

        mTitleBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void initData() {

    }

    @Override
    public void initListener() {

    }


}
