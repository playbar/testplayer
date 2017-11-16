package com.baofeng.mj.smb.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;


import com.baofeng.mj.ui.view.TitleBar;
import com.baofeng.mj.vrplayer.R;
import com.baofeng.mj.vrplayer.activity.BaseActivity;

/**
 * Created by panxin on 2017/8/15.
 */

public class SMBHelpListActivity extends BaseActivity {

    private TitleBar mTitleBar;
    private RelativeLayout layout_help_share;
    private RelativeLayout layout_help_question;

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_smb_help_list);
////        initView();
////        initListener();
//    }

    public void initView(){
        setContentView(R.layout.activity_smb_help_list);
        mTitleBar = (TitleBar) findViewById(R.id.rl_title_bar);
        mTitleBar.setTitleBarTitle(getResources().getString(R.string.mj_share_smb));

        layout_help_share = (RelativeLayout)findViewById(R.id.layout_help_share);
        layout_help_question = (RelativeLayout)findViewById(R.id.layout_help_question);
    }

    @Override
    public void initData() {

    }

    public void initListener(){

        mTitleBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        layout_help_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SMBHelpListActivity.this,SMBHelpShareActivity.class);
                startActivity(intent);
            }
        });

        layout_help_question.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SMBHelpListActivity.this,HelpFeedBackActivity.class);
                startActivity(intent);
            }
        });
    }
}
