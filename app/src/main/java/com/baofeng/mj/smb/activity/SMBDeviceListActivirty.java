package com.baofeng.mj.smb.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baofeng.mj.business.sqlitebusiness.SqliteManager;
import com.baofeng.mj.smb.adapter.SMBDeviceAdapter;
import com.baofeng.mj.smb.bean.SMBDeviceBean;
import com.baofeng.mj.smb.interfaces.IDeviceDeleteListener;
import com.baofeng.mj.smb.interfaces.ILongClickListener;
import com.baofeng.mj.smb.interfaces.ISMBLoginListener;
import com.baofeng.mj.smb.util.SMBViewTypeUtil;
import com.baofeng.mj.smb.view.SMBDeleteDialog;
import com.baofeng.mj.smb.view.SMBLoginDialog;
import com.baofeng.mj.ui.view.TitleBar;
import com.baofeng.mj.unity.launcher.SharedPreferencesUtil;
import com.baofeng.mj.util.publicutil.NetworkUtil;
import com.baofeng.mj.util.threadutil.SqliteProxy;
import com.baofeng.mj.vrplayer.MyAppliaction;
import com.baofeng.mj.vrplayer.R;
import com.baofeng.mj.vrplayer.activity.BaseActivity;
import com.handmark.pulltorefresh.library.PullToRefreshRecyle;
import com.mj.smb.smblib.bean.DeviceBean;
import com.mj.smb.smblib.interfaces.IScanFinishListener;
import com.mj.smb.smblib.util.ScanUtil;

import java.util.List;
import java.util.Vector;

/**
 * Created by panxin on 2017/8/7.
 */

public class SMBDeviceListActivirty extends BaseActivity {

    private TitleBar mTitleBar;
    private RecyclerView mRecyclerView;
    private PullToRefreshRecyle mPullToRefreshRecyle;
    private SMBDeviceAdapter adapter;
    private List<SMBDeviceBean> notConnectList;
    private List<SMBDeviceBean> connectedList;
    private List<SMBDeviceBean> allList;
    private boolean isDestroy;
    private Handler handler = new Handler();
    private SMBLoginDialog mSMBLoginDialog;
    private SMBDeleteDialog mSMBDeleteDialog;
    private LinearLayout layout_no_list;
    private TextView textview_reset_scan;
    private LinearLayout layout_first;
    private TextView textview_tell_me;
    private TextView textview_setting_ok;


//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        initView();
//        initData();
//        initListener();
//    }


    public void initView() {
        setContentView(R.layout.activity_smb_device_list);
        mTitleBar = (TitleBar) findViewById(R.id.rl_title_bar);
        mTitleBar.setTitleBarTitle(getResources().getString(R.string.mj_share_smb));
        mTitleBar.getRightBtn().setImageResource(R.mipmap.nav_icon_add);

        mPullToRefreshRecyle = (PullToRefreshRecyle) findViewById(R.id.mPullToRefreshRecyle);
        layout_no_list = (LinearLayout) findViewById(R.id.layout_no_list);
        textview_reset_scan = (TextView) findViewById(R.id.textview_reset_scan);
        layout_first = (LinearLayout) findViewById(R.id.layout_first);
        textview_setting_ok = (TextView) findViewById(R.id.textview_setting_ok);
        textview_tell_me = (TextView) findViewById(R.id.textview_tell_me);

        mRecyclerView = mPullToRefreshRecyle.getRefreshableView();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new SMBDeviceAdapter(this);
        mRecyclerView.setAdapter(adapter);
        mSMBLoginDialog = new SMBLoginDialog(this);
        mSMBDeleteDialog = new SMBDeleteDialog(this);

    }

    public void initData() {
        //UmengReport.reportEvent("smb_play","name","enter");
        allList = new Vector<>();
        notConnectList = new Vector<>();
        connectedList = new Vector<>();
        isDestroy = false;
        if(SharedPreferencesUtil.getInstance().getBoolean(SMBViewTypeUtil.KEY_SMB_FIRST_COMEIN,true)){
            layout_first.setVisibility(View.VISIBLE);
            mTitleBar.getRightBtn().setVisibility(View.GONE);
        }else{
            layout_first.setVisibility(View.GONE);
            mTitleBar.getRightBtn().setVisibility(View.VISIBLE);
            if (!ScanUtil.instance().isScaning()) {
                ScanUtil.instance().startScan();
            }
        }

    }

    public void initListener() {
        mTitleBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mTitleBar.getRightBtn().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  UmengReport.reportEvent("smb_play","name","add_server");
                Intent intent = new Intent(SMBDeviceListActivirty.this, SMBAddDeviceActivity.class);
                startActivityForResult(intent, 1);
            }
        });


        ScanUtil.instance().setIScanFinishListener(new IScanFinishListener() {

            @Override
            public void scanStart() {
                refreshAdapter();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        layout_no_list.setVisibility(View.GONE);
                    }
                });
            }

            @Override
            public void scanFinish(final List<DeviceBean> list) {
                if (isDestroy) {
                    return;
                }
                SqliteProxy.getInstance().addProxyRunnable(new SqliteProxy.ProxyRunnable() {
                    @Override
                    public void run() {
                        computeAllList(list);
                        refreshAdapter();
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                if (allList.size() <= 1) {
                                    layout_no_list.setVisibility(View.VISIBLE);
                                } else {
                                    layout_no_list.setVisibility(View.GONE);
                                }
                            }
                        });
                    }
                });
            }

            @Override
            public void scanError() {  //检查网络
                refreshAdapter();
                if(!NetworkUtil.isWifiAvailable(MyAppliaction.getInstance())&&allList.size()<=1){
                    layout_no_list.setVisibility(View.VISIBLE);
                }
                Toast.makeText(MyAppliaction.getInstance(), getString(R.string.mj_share_smb_not_WIFI),
                        Toast.LENGTH_SHORT).show();
            }
        });


        //长按item弹出删除对话框
        adapter.setILongClickListener(new ILongClickListener() {
            @Override
            public void longClick(final String ip) {
                if (mSMBDeleteDialog != null && !mSMBDeleteDialog.isShowing()) {
                    mSMBDeleteDialog.setIp(ip);
                    mSMBDeleteDialog.show();
                }
            }
        });

        mSMBDeleteDialog.setIDeviceDeleteListener(new IDeviceDeleteListener() {
            @Override
            public void delete(final String ip) {
                SqliteProxy.getInstance().addProxyRunnable(new SqliteProxy.ProxyRunnable() {
                    @Override
                    public void run() {
                        SqliteManager.getInstance().deleteSMBDevice(ip);
                        refreshConnectList();
                        refreshAdapter();
                    }
                });
            }
        });

        mRecyclerView.setOnItemClickListener(new RecyclerView.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int pos) {
                if(!NetworkUtil.isWifiAvailable(MyAppliaction.getInstance())){
                    Toast.makeText(MyAppliaction.getInstance(), getString(R.string.mj_share_smb_not_WIFI),
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                SMBDeviceBean bean = allList.get(pos);
                if (pos <= connetSize && bean.type == SMBViewTypeUtil.TYPE_SMB_ITEM) {//点击连接过的
                    clickConnectItem(bean);
                } else if (pos > connetSize && bean.type == SMBViewTypeUtil.TYPE_SMB_ITEM) {//点击未连接过的
                    if (!mSMBLoginDialog.isShowing()) {
                        mSMBLoginDialog.setIpName(bean.ip, bean.name);
                        mSMBLoginDialog.show();
                    }
                }
            }
        });

        mSMBLoginDialog.setISMBLoginListener(new ISMBLoginListener() {
            @Override
            public void loginStart() {
                showProgressDialog();
            }

            @Override
            public void loginSuccess(final String ip, final String deviceName,final String username,final String password) {
                dismissProgressDialog();
                if (mSMBLoginDialog.isShowing()) {
                    mSMBLoginDialog.dismiss();
                }
                SqliteProxy.getInstance().addProxyRunnable(new SqliteProxy.ProxyRunnable() {
                    @Override
                    public void run() {
                        SMBDeviceBean b = new SMBDeviceBean();
                        b.ip = ip;
                        b.name = deviceName;
                        b.connect_time = System.currentTimeMillis();
                        b.username = username;
                        b.password = password;
                        SqliteManager.getInstance().addSMBDevice(b);
                    }
                });
                intentFileListActivity(ip, deviceName,username,password);
            }

            @Override
            public void loginError(final String ip,final String deviceName) {
                dismissProgressDialog();
                SqliteProxy.getInstance().addProxyRunnable(new SqliteProxy.ProxyRunnable() {
                    @Override
                    public void run() {
                        SMBDeviceBean b = SqliteManager.getInstance().getSMBDeviceByIp(ip);
                        if(b!=null){
                            b.username = "";
                            b.password = "";
                            SqliteManager.getInstance().addSMBDevice(b);
                        }

                    }
                });
                if (!mSMBLoginDialog.isShowing()) {
                    mSMBLoginDialog.setIpName(ip,deviceName);
                    mSMBLoginDialog.show();
                }
            }
        });


        textview_reset_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!ScanUtil.instance().isScaning()) {
                    ScanUtil.instance().startScan();
                }
            }
        });




        layout_first.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //屏蔽其他按钮，不做任何处理
            }
        });


        textview_setting_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferencesUtil.getInstance().setBoolean(SMBViewTypeUtil.KEY_SMB_FIRST_COMEIN,false);
                layout_first.setVisibility(View.GONE);
                mTitleBar.getRightBtn().setVisibility(View.VISIBLE);
                if (!ScanUtil.instance().isScaning()) {
                    ScanUtil.instance().startScan();
                }
                SqliteProxy.getInstance().addProxyRunnable(new SqliteProxy.ProxyRunnable() {
                    @Override
                    public void run() {
                        refreshConnectList();
                        refreshAdapter();
                    }
                });
            }
        });

        textview_tell_me.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SMBDeviceListActivirty.this,SMBHelpShareActivity.class);
                startActivity(intent);
            }
        });
    }


    /**
     * 点击保存过的列表
     *
     * @param bean
     */
    private void clickConnectItem(SMBDeviceBean bean) {
        mSMBLoginDialog.setIpName(bean.ip, bean.name);
        mSMBLoginDialog.requestTask(bean.username, bean.password);
    }

    /**
     * 重新计算全部列表
     *
     * @param list
     */
    private void computeAllList(final List<DeviceBean> list) {
        notConnectList.clear();
        refreshConnectList();
        for (int i = 0; i < list.size(); i++) {
            DeviceBean bean = list.get(i);
            SMBDeviceBean smbBean = new SMBDeviceBean();
            smbBean.ip = bean.ip;
            smbBean.name = bean.name;
            smbBean.type = SMBViewTypeUtil.TYPE_SMB_ITEM;
            notConnectList.add(smbBean);
        }
        computeNotConnectRepeatList();
//        allList.addAll(notConnectList);

    }

    int connetSize;

    /**
     * 刷新连接过的列表
     */
    private void refreshConnectList() {
        connectedList.clear();
        allList.clear();
        List<SMBDeviceBean> tempList = SqliteManager.getInstance().getSMBDeviceList();
        connetSize = tempList.size();
        adapter.setConnectCount(connetSize);
        if (tempList != null && connetSize != 0) {
            SMBDeviceBean smbBean = new SMBDeviceBean();
            smbBean.type = SMBViewTypeUtil.TYPE_SMB_CONNECT;
            tempList.add(0, smbBean);
            connectedList.addAll(tempList);
        }
        {
            SMBDeviceBean smbBean = new SMBDeviceBean();
            smbBean.type = SMBViewTypeUtil.TYPE_SMB_NOCONNECT;
            connectedList.add(smbBean);
        }
        allList.addAll(connectedList);
        computeNotConnectRepeatList();
    }


    /**
     * 去除重复的列表数据
     * 当已连接列表有跟扫描出的ip相同的ip地址时，
     * 总列表不添加此ip，
     * 未完成列表数据不变（ui层显示的是用的总列表），
     * 方便控制删除已完成列表的Ip时UI层的未连接如果有此ip再次刷新出来
     */
    private void computeNotConnectRepeatList(){
        for(int i = 0;i<notConnectList.size();i++){
            boolean isContains = false;
            for(int j =0;j < connectedList.size();j++){
                if(notConnectList.get(i).ip.equals(connectedList.get(j).ip)){
                    isContains = true;
                    break;
                }
            }
            if(!isContains){
                allList.add(notConnectList.get(i));
            }
        }
    }

    private void refreshAdapter() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                adapter.setData(allList);
            }
        });
    }


    private void intentFileListActivity(String ip, String deviceName,String username,String password) {
        Intent intent = new Intent(SMBDeviceListActivirty.this, SMBFileListActivity.class);
        intent.putExtra(SMBViewTypeUtil.KEY_SMB_DEVICE_IP, ip);
        intent.putExtra(SMBViewTypeUtil.KEY_SMB_DEVICE_NAME, deviceName);
        intent.putExtra(SMBViewTypeUtil.KEY_SMB_DEVICE_USERNAME, username);
        intent.putExtra(SMBViewTypeUtil.KEY_SMB_DEVICE_PASSWORD, password);
        startActivity(intent);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == 1 && data != null) {
            String ip = data.getStringExtra(SMBViewTypeUtil.KEY_SMB_DEVICE_IP);
            String deviceName = data.getStringExtra(SMBViewTypeUtil.KEY_SMB_DEVICE_NAME);
            String username = data.getStringExtra(SMBViewTypeUtil.KEY_SMB_DEVICE_USERNAME);
            String password = data.getStringExtra(SMBViewTypeUtil.KEY_SMB_DEVICE_PASSWORD);
            mSMBLoginDialog.setIpName(ip, deviceName);
            mSMBLoginDialog.requestTask(username, password);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!SharedPreferencesUtil.getInstance().getBoolean(SMBViewTypeUtil.KEY_SMB_FIRST_COMEIN,true)){
            SqliteProxy.getInstance().addProxyRunnable(new SqliteProxy.ProxyRunnable() {
                @Override
                public void run() {
                    refreshConnectList();
                    refreshAdapter();
                }
            });
        }

    }

    @Override
    protected void onDestroy() {

        isDestroy = true;
        ScanUtil.instance().cancle();
        if (allList != null) {
            allList.clear();
        }
        if (notConnectList != null) {
            notConnectList.clear();
        }
        if (connectedList != null) {
            connectedList.clear();
        }
        SqliteManager.getInstance().closeSQLiteDatabase();

        super.onDestroy();
    }
}
