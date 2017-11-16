
package com.baofeng.mj.vrplayer.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.Toast;

import com.baofeng.mj.vrplayer.MyAppliaction;
import com.baofeng.mj.vrplayer.R;
import com.baofeng.mj.vrplayer.publicc.ActivityUtil;
import com.baofeng.mj.vrplayer.publicc.AppContant;
import com.baofeng.mj.vrplayer.util.BottomTabUtil;
import com.baofeng.mj.vrplayer.util.PageTypeUtil;
import com.baofeng.mj.vrplayer.util.ReportUtil;
import com.baofeng.mj.vrplayer.util.ThreadPoolUtil;
import com.baofeng.mj.vrplayer.widget.MyFragmentTabHost;
import com.baofeng.mojing.MojingSDK;
import com.mojing.vrplayer.publicc.ReportBusiness;

import java.util.HashMap;

/**
 * 主activity
 */
public class MainActivity extends BaseActivity implements TabHost.OnTabChangeListener {
    private MyFragmentTabHost myFragmentTabHost;
    private int mCurrentTab = 0;
    private LinearLayout layout_loading;
    public static  ImageView tabNewVideoTAG=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);
        super.onCreate(savedInstanceState);
        myFragmentTabHost = (MyFragmentTabHost) findViewById(android.R.id.tabhost);
        myFragmentTabHost.setup(this, getSupportFragmentManager(), R.id.tabcontent);
        myFragmentTabHost.setOnTabChangedListener(this);
        BottomTabUtil.initBottomTab(this, myFragmentTabHost);//初始化底部tab
      //  myFragmentTabHost.getta
        AppContant.comeInTime = System.currentTimeMillis();
        processIntent();//处理意图
        if(savedInstanceState != null){
            mCurrentTab = savedInstanceState.getInt("currentTab");
        }
        switchCurrentTab();
    }

    @Override
    public void initView() {
        layout_loading = (LinearLayout)findViewById(R.id.layout_loading);
    }

    @Override
    public void initData() {
        try {
            if (!MojingSDK.GetInitSDK()) {
                MojingSDK.Init(this.getApplicationContext());
                MojingSDK.onDisableVrService(true);
             //   MojingSDK.SetEngineVersion("Android");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initListener() {
        layout_loading.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    public void showLoadingView(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                layout_loading.setVisibility(View.VISIBLE);
            }
        });
    }
    public void hideLoadingView(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                layout_loading.setVisibility(View.GONE);
            }
        });

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        processIntent();//处理意图
    }

    /**
     * 处理意图
     */
    private void processIntent() {
        Intent intent = getIntent();
        if (intent != null) {
        }
    }

    /**
     * 切换当前页面
     */
    private void switchCurrentTab() {
        myFragmentTabHost.setCurrentTab(mCurrentTab);
    }

    @Override
    public void onTabChanged(String tabId) {
        onTabChanged(myFragmentTabHost.getCurrentTab());
    }

    public void onTabChanged(int position) {
        mCurrentTab = position;
        final int size = myFragmentTabHost.getTabWidget().getTabCount();
        for (int i = 0; i < size; i++) {
            View view = myFragmentTabHost.getTabWidget().getChildAt(i);
            if (i == position) {
                view.setSelected(true);
            } else {
                view.setSelected(false);
            }
        }
        reportPV(mCurrentTab);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("currentTab", mCurrentTab);
    }

    public Fragment getFragment(int position) {
        return getSupportFragmentManager().findFragmentByTag(
                BottomTabUtil.getFragmentTabTag(this, position));
    }

    public Fragment getCurrentFragment() {
        return getSupportFragmentManager().findFragmentByTag(
                myFragmentTabHost.getCurrentTabTag());
    }

    public int getCurrentTab(){
        return mCurrentTab;
    }

    private long exitTime = 0;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (System.currentTimeMillis() - exitTime > 2000) {
                Toast.makeText(this, getString(R.string.mj_vrplayer_main_exit_msg), Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                ReportUtil.reportTimer("1");
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ActivityUtil.getInstance().exitAllActivity();
                    }
                },200);

            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onResume() {
        super.onResume();
        tabShowNewVideoTAG();
    }

    public static void tabShowNewVideoTAG(){
        if(MyAppliaction.getInstance().getmFileUploadBusiness()!=null&&MyAppliaction.getInstance().getmFileUploadBusiness().isShowNewOnTab()){
            MainActivity.tabNewVideoTAG.setVisibility(View.VISIBLE);
        }else{
            MainActivity.tabNewVideoTAG.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onDestroy() {
        ThreadPoolUtil.clear();//清理线程池
        super.onDestroy();
    }

    private void reportPV(int mCurrentTab){
        HashMap<String,String> params = new HashMap<>();
        params.put("etype","pv");
        if(mCurrentTab == 0){
            params.put("pagetype", PageTypeUtil.PageTypeLocal);
        }else if(mCurrentTab == 1){
            params.put("pagetype", PageTypeUtil.PageTypeFile);
        }else if(mCurrentTab == 2){
            params.put("pagetype", PageTypeUtil.PageTypeSetting);
        }
        ReportBusiness.getInstance().reportVV(params);
    }
}
