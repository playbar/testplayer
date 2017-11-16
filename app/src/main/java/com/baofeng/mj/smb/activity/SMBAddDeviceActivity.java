package com.baofeng.mj.smb.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


import com.baofeng.mj.smb.util.SMBViewTypeUtil;
import com.baofeng.mj.smb.util.SmbIpUtil;
import com.baofeng.mj.smb.view.SMBErrorIpDialog;
import com.baofeng.mj.vrplayer.R;
import com.baofeng.mj.vrplayer.activity.BaseActivity;

import jcifs.netbios.NbtAddress;

/**
 * Created by panxin on 2017/8/15.
 */

public class SMBAddDeviceActivity extends BaseActivity {

    private ImageView back;
    private TextView tv_right;
    private EditText edittext_ip;
    private EditText edittext_name;
    private EditText edittext_password;
    private SMBErrorIpDialog mSMBErrorIpDialog;
    private GetDeviceNameTask task;

//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_smb_add_device);
//        initView();
//        initListener();
//    }

    public void initView() {
        setContentView(R.layout.activity_smb_add_device);
        back = (ImageView) findViewById(R.id.back);
        tv_right = (TextView) findViewById(R.id.tv_right);

        edittext_ip = (EditText) findViewById(R.id.edittext_ip);
        edittext_name = (EditText) findViewById(R.id.edittext_name);
        edittext_password = (EditText) findViewById(R.id.edittext_password);

        mSMBErrorIpDialog = new SMBErrorIpDialog(this);
    }

    @Override
    public void initData() {

    }

    public void initListener() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tv_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SmbIpUtil.isValidIpAddress(edittext_ip.getText().toString())) {
                    connect();
                } else {
                    if (!mSMBErrorIpDialog.isShowing()) {
                        mSMBErrorIpDialog.show();
                    }
                }
            }
        });
    }

    private void connect() {
        String ip = edittext_ip.getText().toString();
        String username = edittext_name.getText().toString();
        String password = edittext_password.getText().toString();
        getDeviceNameByIp(ip,username,password);
    }

    private void getDeviceNameByIp(final String currnetIp,String username,String password) {
        if(task == null){
            task = new GetDeviceNameTask();
            task.execute(currnetIp,username,password);
        }
    }


    class GetDeviceNameTask extends AsyncTask<String, Void, String> {

        private String deviceName;
        private String ip;
        private String username;
        private String password;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialog();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                ip = params[0];
                username = params[1];
                password = params[2];
                NbtAddress nbtAddress = NbtAddress.getByName(ip);
                if (nbtAddress.isActive()) {
                    if (!TextUtils.isEmpty(ip)) {
                        deviceName = nbtAddress.firstCalledName();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return deviceName;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            dismissProgressDialog();
            if(TextUtils.isEmpty(deviceName)){
                deviceName = ip;
            }
            Intent mIntent = new Intent();
            mIntent.putExtra(SMBViewTypeUtil.KEY_SMB_DEVICE_IP, ip);
            mIntent.putExtra(SMBViewTypeUtil.KEY_SMB_DEVICE_NAME, deviceName);
            mIntent.putExtra(SMBViewTypeUtil.KEY_SMB_DEVICE_USERNAME, username);
            mIntent.putExtra(SMBViewTypeUtil.KEY_SMB_DEVICE_PASSWORD, password);
            setResult(1, mIntent);
            finish();

        }
    }
}
