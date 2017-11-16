package com.baofeng.mj.vrplayer.http.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.baofeng.mj.vrplayer.MyAppliaction;
import com.baofeng.mj.vrplayer.R;
import com.baofeng.mj.vrplayer.activity.BaseActivity;
import com.baofeng.mj.vrplayer.business.OpenActivity;
import com.baofeng.mj.vrplayer.ftp.activity.FtpActivity;
import com.baofeng.mj.vrplayer.http.constvalue.ConstValue;
import com.baofeng.mj.vrplayer.http.server.ServerRunner;
import  com.baofeng.mj.util.publicutil.NetworkUtil;
import com.baofeng.mj.vrplayer.util.PageTypeUtil;
import com.baofeng.mj.vrplayer.util.ReportUtil;
import com.baofeng.mj.vrplayer.util.StorageUtil;
import com.baofeng.mj.vrplayer.widget.SlideLayout;

import org.w3c.dom.Text;

/**
 * Created by panxin on 2017/6/29.
 */

public class HttpActivity extends BaseActivity{
    private ImageView imageview_back;
    private TextView tv_title;
    private TextView textview_ip;
    private ImageView imageview_vr;
    private String IPAddr;
    private boolean wifiIsAvailable = false;
    private SlideLayout layout_SlideLayout;
    private LinearLayout layout_parent_touch;
    TextView httpName;
    TextView httpSendSize;
    TextView httpSendRate;
    SeekBar httpSeekbar;

    @Override
    public void initView() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        setContentView(R.layout.activity_http);
        layout_SlideLayout = (SlideLayout)findViewById(R.id.layout_SlideLayout);
        layout_parent_touch = (LinearLayout)findViewById(R.id.layout_parent_touch);
        layout_SlideLayout.setTouchView(layout_parent_touch);
        textview_ip = (TextView)findViewById(R.id.textview_ip);
        imageview_back = (ImageView)findViewById(R.id.imageview_back);
        tv_title = (TextView) findViewById(R.id.tv_title);
        imageview_vr = (ImageView)findViewById(R.id.imageview_vr);
        httpName= (TextView)findViewById(R.id.httpName);
        httpSendSize= (TextView)findViewById(R.id.httpSendSize);
        httpSendRate= (TextView)findViewById(R.id.httpSendRate);
        httpSeekbar= (SeekBar)findViewById(R.id.httpSeekbar);
        httpSeekbar.setEnabled(false);
    }

    Handler mProgress=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(!TextUtils.isEmpty(name)){
                httpName.setText(name);
            }
            if(curentSize!=0&&totalSize!=0){
                if(state==ACTION_VRPLAYER_HTTP_STATE_FINISH){
                    httpSeekbar.setProgress(100);
                    httpSendSize.setText(getFormatSize(totalSize)+"/"+getFormatSize(totalSize));
                    httpSendRate.setText("100%");
                    if(!TextUtils.isEmpty(httpName.getText().toString())){
                        MyAppliaction.getInstance().getmFileUploadBusiness().saveUploadVideo(httpName.getText().toString());
                    }
                }else if(state==ACTION_VRPLAYER_HTTP_STATE_ING){
                    int cureent=(int)(curentSize*100/totalSize);
                    httpSeekbar.setProgress(cureent);
                    httpSendSize.setText(getFormatSize(curentSize)+"/"+getFormatSize(totalSize));
                    httpSendRate.setText(cureent+"%");
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

    public static final String ACTION_VRPLAYER_HTTP="ACTION_VRPLAYER_HTTP";
    public static final String ACTION_VRPLAYER_HTTP_KEY_NAME="ACTION_VRPLAYER_HTTP_KEY_NAME";
    public static final String ACTION_VRPLAYER_HTTP_KEY_TOTAL_SIZE="ACTION_VRPLAYER_HTTP_KEY_TOTAL_SIZE";
    public static final String ACTION_VRPLAYER_HTTP_KEY_CUREENT_SIZE="ACTION_VRPLAYER_HTTP_KEY_CUREENT_SIZE";
    public static final String ACTION_VRPLAYER_HTTP_KEY_STATE="ACTION_VRPLAYER_HTTP_KEY_STATE";

    public static final int ACTION_VRPLAYER_HTTP_STATE_ING=0;
    public static final int ACTION_VRPLAYER_HTTP_STATE_FINISH=1;
    public static final int ACTION_VRPLAYER_HTTP_STATE_ERR=2;
    String name="";
    long curentSize=0;
    long totalSize=0;
    int state=ACTION_VRPLAYER_HTTP_STATE_ING;
    static CallBackReceiver callBackReceiver;
    public class CallBackReceiver {
        public void onReceive(int upgradeState,String upgradeName,long upgradTotalSize,long upgradeCurentSize) {
                state=upgradeState;
                if(!TextUtils.isEmpty(upgradeName)){
                    name=upgradeName;
                    mProgress.sendEmptyMessage(0);
                }
                if(upgradTotalSize!=0){
                    if(state==ACTION_VRPLAYER_HTTP_STATE_ING){
                        totalSize =upgradTotalSize;
                    }
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
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_VRPLAYER_HTTP);
        callBackReceiver=new CallBackReceiver();
    }

    @Override
    protected void onStop() {
        super.onStop();
        callBackReceiver=null;
    }

    @Override
    public void initData() {
        // create the dir to store the received files
        ConstValue.BASE_DIR = StorageUtil.getDownloadDir();
        wifiIsAvailable = NetworkUtil.isWIFIConnected(getApplicationContext());
        if (wifiIsAvailable) {
            IPAddr = NetworkUtil.getWifiIpAddress(getApplicationContext());
            textview_ip.setText(getBrowserAddr());
            // start the server
            ServerRunner.startServer(ConstValue.PORT);
        }else{
            textview_ip.setText(getString(R.string.mj_vrplayer_wifi_no));
        }
        tv_title.setText(getResources().getString(R.string.mj_vrplayer_http));
        ReportUtil.reportPV(PageTypeUtil.PageTypeHttpFile);
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
                OpenActivity.openUnity(HttpActivity.this);
            }
        });
        layout_SlideLayout.setOnSildingFinishListener(new SlideLayout.OnSildingFinishListener() {

            @Override
            public void onSildingFinish() {
                finish();
            }
        });
    }


    public String getBrowserAddr() {
        if (wifiIsAvailable) {
            return "http://" + IPAddr + ":" + ConstValue.PORT;
        } else {
            return "";
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        ServerRunner.stopServer();
    }
}
