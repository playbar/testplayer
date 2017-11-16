package com.baofeng.mj.vrplayer.ftp.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baofeng.mj.vrplayer.MyAppliaction;
import com.baofeng.mj.vrplayer.R;
import com.baofeng.mj.vrplayer.activity.BaseActivity;
import com.baofeng.mj.vrplayer.business.OpenActivity;
import com.baofeng.mj.vrplayer.ftp.ftp.FtpServerService;
import com.baofeng.mj.vrplayer.http.activity.HttpActivity;
import  com.baofeng.mj.util.publicutil.NetworkUtil;
import com.baofeng.mj.vrplayer.util.PageTypeUtil;
import com.baofeng.mj.vrplayer.util.ReportUtil;
import com.baofeng.mj.vrplayer.widget.SlideLayout;

import java.net.InetAddress;

/**
 * Created by panxin on 2017/6/30.
 */

public class FtpActivity extends BaseActivity{

    private ImageView imageview_back;
    private TextView tv_title;
    private TextView textview_ip;
    private ImageView imageview_vr;
    private boolean wifiIsAvailable;
    private SlideLayout layout_SlideLayout;
    private LinearLayout layout_parent_touch;
    TextView httpName;
    TextView httpSendSize;
    static CallBackReceiver callBackReceiver;
    @Override
    public void initView() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        setContentView(R.layout.activity_ftp);
        layout_SlideLayout = (SlideLayout)findViewById(R.id.layout_SlideLayout);
        layout_parent_touch = (LinearLayout)findViewById(R.id.layout_parent_touch);
        layout_SlideLayout.setTouchView(layout_parent_touch);
        textview_ip = (TextView)findViewById(R.id.textview_ip);
        imageview_back = (ImageView)findViewById(R.id.imageview_back);
        tv_title = (TextView) findViewById(R.id.tv_title);
        imageview_vr = (ImageView)findViewById(R.id.imageview_vr);
        httpName= (TextView)findViewById(R.id.ftpName);
        httpSendSize= (TextView)findViewById(R.id.ftpSendSize);
    }
    Handler mProgress=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(!TextUtils.isEmpty(name)){
                Log.v("logname","finish name set:"+name);
                httpName.setText(name);
            }
            if(curentSize!=0){
                if(state==ACTION_VRPLAYER_FTP_STATE_FINISH){
                   // httpSeekbar.setProgress(100);
                    Log.v("logname","finish name:"+httpName.getText().toString());
                    httpSendSize.setText(getFormatSize(curentSize));
                    if(!TextUtils.isEmpty(httpName.getText().toString())){
                        Log.v("logname","finish name save:"+name);
                        MyAppliaction.getInstance().getmFileUploadBusiness().saveUploadVideo(name);
                    }
                  //  httpSendRate.setText("100%");
                }else if(state==ACTION_VRPLAYER_FTP_STATE_ING){
                    //int cureent=(int)(curentSize*100/totalSize);
                    //httpSeekbar.setProgress(cureent);
                    httpSendSize.setText(getFormatSize(curentSize));
                   // httpSendRate.setText(cureent+"%");
                }
            }
        }
    };

    public String getFormatSize(long size){
        if(size==0){
            return "0M";
        }
        int M_Size=(int)(size/(1024*1024));
        int K_Size=(int)(size%(1024*1024))/1024/10;
        if(M_Size==0){
            return M_Size+"."+K_Size+"M";
        }else{
            return M_Size+"M";
        }
    }

    public static final String ACTION_VRPLAYER_FTP="ACTION_VRPLAYER_FTP";
    public static final String ACTION_VRPLAYER_FTP_KEY_NAME="ACTION_VRPLAYER_FTP_KEY_NAME";
    public static final String ACTION_VRPLAYER_FTP_KEY_TOTAL_SIZE="ACTION_VRPLAYER_FTP_KEY_TOTAL_SIZE";
    public static final String ACTION_VRPLAYER_FTP_KEY_CUREENT_SIZE="ACTION_VRPLAYER_FTP_KEY_CUREENT_SIZE";
    public static final String ACTION_VRPLAYER_FTP_KEY_STATE="ACTION_VRPLAYER_FTP_KEY_STATE";

    public static final int ACTION_VRPLAYER_FTP_STATE_ING=0;
    public static final int ACTION_VRPLAYER_FTP_STATE_FINISH=1;
    public static final int ACTION_VRPLAYER_FTP_STATE_ERR=2;
    String name="";
    long curentSize=0;
   // long totalSize=0;
    int state=ACTION_VRPLAYER_FTP_STATE_ING;

    public class CallBackReceiver {
        public void onReceive(int upgradeState,String upgradeName,long upgradTotalSize,long upgradeCurentSize) {
                state=upgradeState;
                if(!TextUtils.isEmpty(upgradeName)){
                    name=upgradeName;
                    if(!TextUtils.isEmpty(name)&&name.startsWith("/")){
                        name=name.substring(1,name.length());
                    }
                    mProgress.sendEmptyMessage(0);
                }
                if(upgradeCurentSize!=0){
                    curentSize =upgradeCurentSize;
                    mProgress.sendEmptyMessage(0);
                }
            }
    }

    public static CallBackReceiver getCallBackReceiverInstance(){
        return callBackReceiver;
    }

    @Override
    protected void onStart() {
        super.onStart();
        callBackReceiver=new CallBackReceiver();
    }

    @Override
    protected void onStop() {
        super.onStop();
        callBackReceiver=null;
    }


    @Override
    public void initData() {
        wifiIsAvailable = NetworkUtil.isWIFIConnected(getApplicationContext());
        if(wifiIsAvailable){
            InetAddress address = NetworkUtil.getLocalInetAddress(getApplicationContext());
            if (address != null) {
                String port = ":" + FtpServerService.getPort();
                textview_ip.setText("ftp://" + address.getHostAddress() + port);
            }
            startServer();
        }else{
            textview_ip.setText(getString(R.string.mj_vrplayer_wifi_no));
        }
        tv_title.setText(getResources().getString(R.string.mj_vrplayer_ftp));
        ReportUtil.reportPV(PageTypeUtil.PageTypeFtpFile);
    }

    @Override
    public void initListener() {
        imageview_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });
        imageview_vr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenActivity.openUnity(FtpActivity.this);
            }
        });


        layout_SlideLayout.setOnSildingFinishListener(new SlideLayout.OnSildingFinishListener() {

            @Override
            public void onSildingFinish() {
                finish();
            }
        });
    }


    private void startServer(){
        Intent intent = new Intent(this, FtpServerService.class);
        if (!FtpServerService.isRunning()) {
            startService(intent);
        }
    }

    private void stopServer(){
        Intent intent = new Intent(this, FtpServerService.class);
        stopService(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopServer();
    }


}
