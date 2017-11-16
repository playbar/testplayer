package com.baofeng.mj.smb.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.baofeng.mj.business.sqlitebusiness.SqliteManager;
import com.baofeng.mj.smb.adapter.SMBFolderAdapter;
import com.baofeng.mj.smb.bean.SMBDeviceBean;
import com.baofeng.mj.smb.bean.SMBFolderBean;
import com.baofeng.mj.smb.interfaces.ISMBLoginListener;
import com.baofeng.mj.smb.interfaces.ISMBSortChangeListener;
import com.baofeng.mj.smb.util.ComparatorNameAsc;
import com.baofeng.mj.smb.util.ComparatorSizeDes;
import com.baofeng.mj.smb.util.ComparatorTimeDes;
import com.baofeng.mj.smb.util.SMBViewTypeUtil;
import com.baofeng.mj.smb.util.SmbIpUtil;
import com.baofeng.mj.smb.view.SMBLoginDialog;
import com.baofeng.mj.smb.view.SMBSortMenuView;
import com.baofeng.mj.unity.launcher.SharedPreferencesUtil;
import com.baofeng.mj.util.publicutil.NetworkUtil;
import com.baofeng.mj.util.publicutil.VideoExtensionUtil;
import com.baofeng.mj.util.publicutil.VideoTypeUtil;
import com.baofeng.mj.util.threadutil.SqliteProxy;
import com.baofeng.mj.util.viewutil.StartActivityHelper;
import com.baofeng.mj.utils.StringUtils;
import com.baofeng.mj.vrplayer.MyAppliaction;
import com.baofeng.mj.vrplayer.R;
import com.baofeng.mj.vrplayer.activity.BaseActivity;

import com.mj.smb.smblib.service.PlayFileService;
import com.mj.smb.smblib.util.FileUtil;
import com.mojing.vrplayer.publicc.bean.LocalVideoBean;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.SmbAuthException;
import jcifs.smb.SmbException;
import jcifs.smb.SmbFile;

/**
 * Created by panxin on 2017/8/9.
 */

public class SMBFileListActivity extends BaseActivity {

    private ImageView back;
    private ImageView imageview_closeall;
    private TextView title;
    private TextView textview_file_size;
    private ImageView imageview_menu;
    private ListView listview_smb;
    private SMBSortMenuView layout_sort_menu;

    private SMBFolderAdapter adapter;
    private SearchTask task = null;
    private String rootPath;
    private String parrentPath;
    private String username = "";
    private String password = "";
    private SMBLoginDialog mSMBLoginDialog;
    private String ip;
    private String deviceName;
    private String currentPath;
    private boolean isShowMenu = false;
    private List<SMBFolderBean> dirList = new ArrayList<>();//所有文件列表，不包含文件夹，排序对此列表排序
    private List<SMBFolderBean> fileList = new ArrayList<>();//所有文件列表，不包含文件夹，排序对此列表排序
    private List<LocalVideoBean> videoList = new ArrayList<>();//播放器需要传递的视频列表
    private boolean isDestroy = false;

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_smb_file);
//        initView();
//        initData();
//        initListener();
//    }


    public void initView() {
        setContentView(R.layout.activity_smb_file);
        back = (ImageView) findViewById(R.id.back);
        imageview_closeall = (ImageView) findViewById(R.id.imageview_closeall);
        title = (TextView) findViewById(R.id.title);
        textview_file_size = (TextView) findViewById(R.id.textview_file_size);
        imageview_menu = (ImageView) findViewById(R.id.imageview_menu);
        listview_smb = (ListView) findViewById(R.id.listview_smb);
        layout_sort_menu = (SMBSortMenuView) findViewById(R.id.layout_sort_menu);

        adapter = new SMBFolderAdapter(this);
        listview_smb.setAdapter(adapter);
        mSMBLoginDialog = new SMBLoginDialog(this);
    }


    public void initData() {
        isDestroy = false;
        Intent in = getIntent();
        ip = in.getStringExtra(SMBViewTypeUtil.KEY_SMB_DEVICE_IP);
        deviceName = in.getStringExtra(SMBViewTypeUtil.KEY_SMB_DEVICE_NAME);
        username = in.getStringExtra(SMBViewTypeUtil.KEY_SMB_DEVICE_USERNAME);
        password = in.getStringExtra(SMBViewTypeUtil.KEY_SMB_DEVICE_PASSWORD);
        title.setText(deviceName);
        rootPath = SmbIpUtil.getSmbIp(ip);
        parrentPath = "smb://";
        startService(username,password);
        searchFile(rootPath);
        textview_file_size.setText("0" + getString(R.string.mj_share_smb_file_unit));

    }

    private long clickTime = 0;

    public void initListener() {
        listview_smb.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SMBFolderBean bean = adapter.getData().get(position);
                if (bean.type == SMBViewTypeUtil.TYPE_FILE_VIDEO) {
                  //  UmengReport.reportEvent("smb_play","name","play");
                    String url = getHttpUrl(bean);
                    StartActivityHelper.playVideoWithSMB(SMBFileListActivity.this, bean.filename, VideoTypeUtil.MJVideoPictureTypeSingle, url, getVideoList(adapter.getData()), getCurVideoIndex(url));
                } else if (bean.type == SMBViewTypeUtil.TYPE_FILE_DIR) {
                    searchFile(bean.filepath);
                } else {
                    if(System.currentTimeMillis() - clickTime<1000){
                        return;
                    }
                    clickTime = System.currentTimeMillis();
                    Toast.makeText(MyAppliaction.getInstance(), getString(R.string.mj_share_smb_file_only_video), Toast.LENGTH_SHORT).show();
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
                if(isDestroy){
                    return;
                }
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
                startService(username,password);
                searchFile(currentPath);
            }

            @Override
            public void loginError(final String ip,final String deviceName) {
                dismissProgressDialog();
                //登录失败清除保存的用户名和密码
                SqliteProxy.getInstance().addProxyRunnable(new SqliteProxy.ProxyRunnable() {
                    @Override
                    public void run() {
                        SMBDeviceBean b = SqliteManager.getInstance().getSMBDeviceByIp(ip);
                        if(b != null){
                            b.username = "";
                            b.password = "";
                            SqliteManager.getInstance().addSMBDevice(b);
                        }

                    }
                });
            }

        });


        imageview_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isShowMenu) {
                    layout_sort_menu.setVisibility(View.GONE);
                } else {
                    layout_sort_menu.setVisibility(View.VISIBLE);
                }
                isShowMenu = !isShowMenu;
            }
        });

        layout_sort_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageview_menu.performClick();
            }
        });

        layout_sort_menu.setISMBSortChangeListener(new ISMBSortChangeListener() {
            @Override
            public void changeSort() {//切换排序方式时执行
                adapter.setData(getSortList());
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickFinish();
            }
        });

        imageview_closeall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    /**
     * 取只有视频类型的列表，播放器叙播需要
     * @param list
     * @return
     */
    public List<LocalVideoBean> getVideoList(List<SMBFolderBean> list) {
        if (list == null || list.size() <= 0) {
            return null;
        }
        videoList.clear();
        for (int i = 0; i < list.size(); i++) {
            SMBFolderBean folderBean = list.get(i);
            if (folderBean.type == SMBViewTypeUtil.TYPE_FILE_VIDEO) {
                LocalVideoBean bean = new LocalVideoBean();
                bean.name = folderBean.filename;
                bean.path = getHttpUrl(folderBean);
                videoList.add(bean);
            }
        }
        return videoList;
    }

    /**
     * 根据点击的视频从视频列表里筛选第几位，播放器叙播需要从该位置播下一个
     * @param path
     * @return 在视频列表中的第几位
     */
    public int getCurVideoIndex(String path) {
        if (videoList == null || videoList.size() <= 0)
            return 0;
        for (int i = 0; i < videoList.size(); i++) {
            LocalVideoBean bean = videoList.get(i);
            if (bean == null) {
                continue;
            }
            if (bean.path.equals(path)) {
                return i;
            }
        }

        return 0;
    }

    /**
     * 将smburl转成http协议的url
     * @param bean
     * @return
     */
    private String getHttpUrl(SMBFolderBean bean) {
        String ipVal = FileUtil.ip;
        int portVal = FileUtil.port;
        String path = bean.filepath;
        String httpReq = "http://" + ipVal + ":" + portVal + "/smb=";
        path = path.substring(6);
        try {
            path = URLEncoder.encode(path, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String url = httpReq + path;
        return url;
    }


    private void startService(String username,String password) {
        this.username = username;
        this.password = password;
        Intent intent = new Intent(this, PlayFileService.class);
        intent.putExtra(PlayFileService.KEY_SMB_USENAME, username);
        intent.putExtra(PlayFileService.KEY_SMB_PASSWORD, password);
        startService(intent);

    }


    private void cancleTask() {
        if (task != null) {
            task.cancel(true);
            task = null;
        }
    }

    private void searchFile(String path) {
        cancleTask();
        currentPath = path;
        task = new SearchTask();
        task.execute(path);

    }

    class SearchTask extends AsyncTask<String, Void, List<SMBFolderBean>> {
        String tempPath = null;
        int error = 0;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            showProgressDialog();
        }

        @Override
        protected List<SMBFolderBean> doInBackground(String... params) {

            try {

                error = 0;
                NtlmPasswordAuthentication auth = new NtlmPasswordAuthentication("", username, password);
                SmbFile smbFile;
                if (username == null || "".equals(username) || password == null || "".equals(password)) {
                    smbFile = new SmbFile(params[0]);
                } else {
                    smbFile = new SmbFile(params[0], auth);
                }
                SmbFile mSmbFileList[] = smbFile.listFiles();
                dirList.clear();
                fileList.clear();
                tempPath = smbFile.getParent();

                for (int i = 0; i < mSmbFileList.length; i++) {
                    SmbFile f = smbFile.listFiles()[i];
                    SMBFolderBean bean = new SMBFolderBean();
                    bean.filepath = f.getPath();
                    bean.modified = f.createTime();
                    bean.size = f.getContentLength();
                    bean.filename = f.getName();
                    if (f.isFile()) {
                        if (VideoExtensionUtil.fileIsVideo(bean.filename)) {
                            bean.type = SMBViewTypeUtil.TYPE_FILE_VIDEO;
                        } else if (VideoExtensionUtil.fileIsSRT(bean.filename)) {
                            bean.type = SMBViewTypeUtil.TYPE_FILE_SRT;
                        } else {
                            bean.type = SMBViewTypeUtil.TYPE_FILE_FILE;
                        }
                        fileList.add(bean);
                    } else {
                        bean.type = SMBViewTypeUtil.TYPE_FILE_DIR;
                        String name = f.getName();
                        if(name.endsWith("$/")){//过滤系统文件，系统文件一般都是以此符号结尾
                            continue;
                        }
                        try {
                            if (name.endsWith("/")) {//获取的文件夹名一般最后一位都带“/”，这里去除“/”
                                bean.filename = name.substring(0, name.length() - 1);
                            } else {
                                bean.filename = name;
                            }
                        } catch (Exception e) {
                            bean.filename = name;
                            e.printStackTrace();
                        }
                        try{
                            bean.size = f.list().length;
                        }catch (Exception e){
                            bean.size = 0;
                            e.printStackTrace();
                        }
                        dirList.add(bean);
                    }
                }
            } catch (SmbAuthException e) {
                e.printStackTrace();
                error = -2;//登录失败
            } catch (MalformedURLException e) {
                e.printStackTrace();
                error = -1;
            } catch (SmbException e) {
                e.printStackTrace();
                error = -1;
            } catch (Exception e) {
                e.printStackTrace();
                error = -1;
            }

            return getSortList();
        }

        @Override
        protected void onPostExecute(List<SMBFolderBean> result) {
            super.onPostExecute(result);
            if(isDestroy){
                return;
            }
            dismissProgressDialog();
            if (error == 0) {
                parrentPath = tempPath;
                adapter.setData(result);
                textview_file_size.setText(result.size() + getString(R.string.mj_share_smb_file_unit));
                String dirname = SmbIpUtil.getCurrentDirName(currentPath);
                if (dirname.equals("smb://")) {
                    title.setText(deviceName);
                    imageview_closeall.setVisibility(View.GONE);
                } else {
                    title.setText(dirname);
                    imageview_closeall.setVisibility(View.VISIBLE);
                }
            } else if(error == -2){
                if (!mSMBLoginDialog.isShowing()) {
                    mSMBLoginDialog.setIpName(ip,deviceName);
                    mSMBLoginDialog.setNoPasswordTitle();
                    mSMBLoginDialog.show();
                }
            } else {
                Toast.makeText(MyAppliaction.getInstance(), getString(R.string.mj_share_smb_file_load_error),
                        Toast.LENGTH_SHORT).show();

            }

        }

    }


    /**
     * 对文件类型的list排序并返回包括文件夹的全部list
     *
     * @return
     */
    private List<SMBFolderBean> getSortList() {
        List<SMBFolderBean> list = new ArrayList<>();
        try {
            int type = SharedPreferencesUtil.getInstance().getInt(SMBViewTypeUtil.KEY_SMB_SORT_TYPE, 0);
            switch (type) {
                case SMBViewTypeUtil.TYPE_SORT_TIME:
                    Collections.sort(fileList, new ComparatorTimeDes());
                    break;
                case SMBViewTypeUtil.TYPE_SORT_NAME:
                    Collections.sort(fileList, new ComparatorNameAsc());
                    break;
                case SMBViewTypeUtil.TYPE_SORT_SIZE:
                    Collections.sort(fileList, new ComparatorSizeDes());
                    break;
            }
            list.addAll(dirList);
            list.addAll(fileList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }


    private void clickFinish() {
        if (isShowMenu) {
            layout_sort_menu.setVisibility(View.GONE);
            isShowMenu = !isShowMenu;
            return;
        }
        if (parrentPath.equals("smb://")) {
            cancleTask();
            finish();
        } else {
            if(!NetworkUtil.isWIFIConnected(this)){
                Toast.makeText(MyAppliaction.getInstance(),getString(R.string.mj_share_smb_not_WIFI),Toast.LENGTH_SHORT).show();
                finish();
                return;
            }
            searchFile(parrentPath);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isDestroy = true;
        cancleTask();
        Intent intent = new Intent(this, PlayFileService.class);
        stopService(intent);
        dirList.clear();
        fileList.clear();
        videoList.clear();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            clickFinish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
