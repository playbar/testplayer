package com.baofeng.mj.vrplayer.activity;

import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baofeng.mj.vrplayer.R;
import com.baofeng.mj.vrplayer.adapter.FlyScreenDeviceListAdapter;
import com.baofeng.mj.vrplayer.adapter.FlyScreenVideoListAdapter;
import com.baofeng.mj.vrplayer.bean.DeviceInfo;
import com.baofeng.mj.vrplayer.bean.Resource;
import com.baofeng.mj.vrplayer.business.flyscreen.FlyScreenBusiness;
import com.baofeng.mj.vrplayer.business.flyscreen.interfaces.FlyScreenListener;
import com.baofeng.mj.vrplayer.business.flyscreen.util.FlyScreenConstant;
import com.baofeng.mj.vrplayer.business.flyscreen.util.FlyScreenUtil;
import com.baofeng.mj.vrplayer.widget.FlyScreenGuideView;
import com.baofeng.mj.vrplayer.widget.SlideLayout;
import com.baofeng.mojing.MojingSDKReport;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by liuchuanchi on 2017/7/7.
 * 飞屏
 */
public class FlyScreenActivity extends BaseActivity implements View.OnClickListener, FlyScreenListener{
    private ImageView imageview_back;
    private TextView tv_title;
    private ListView listview_device, listview_video;
    private FlyScreenDeviceListAdapter flyDeviceListAdapter;
    private FlyScreenVideoListAdapter flyVideoListAdapter;
    private List<DeviceInfo> deviceInfoList = new ArrayList<DeviceInfo>();
    private List<Resource.PageItem> pageItemList = new ArrayList<Resource.PageItem>();
    private RelativeLayout rl_content;
    private TextView fly_screen_name;
    private TextView tv_rescan;
    private TextView tv_common_problem;
    private ImageView iv_common_problem;
    private TextView tv_fly_screen_parent_dir;
    private RelativeLayout rl_fly_screen_loading;
    private RelativeLayout rl_fly_screen_not_found;
    private FlyScreenGuideView rl_fly_screen_guide;
    private FlyScreenBusiness flyScreenBusiness;
    private SlideLayout layout_SlideLayout;
    private RelativeLayout layout_parent_touch;

    @Override
    public void initView() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        setContentView(R.layout.activity_flyscreen);
        layout_SlideLayout = (SlideLayout)findViewById(R.id.layout_SlideLayout);
        layout_parent_touch = (RelativeLayout) findViewById(R.id.layout_parent_touch);
        layout_SlideLayout.setTouchView(layout_parent_touch);
        imageview_back = (ImageView) findViewById(R.id.imageview_back);
        tv_title = (TextView) findViewById(R.id.tv_title);
        rl_content = (RelativeLayout) findViewById(R.id.rl_content);
        fly_screen_name = (TextView) findViewById(R.id.tv_fly_screen_name);
        listview_device = (ListView) findViewById(R.id.listview_device);
        listview_video = (ListView) findViewById(R.id.listview_folder);
        rl_fly_screen_guide = (FlyScreenGuideView) findViewById(R.id.rl_fly_screen_guide);
        rl_fly_screen_loading = (RelativeLayout) findViewById(R.id.rl_fly_screen_loading);
        rl_fly_screen_not_found = (RelativeLayout) findViewById(R.id.rl_fly_screen_not_found);
        tv_rescan = (TextView) findViewById(R.id.tv_rescan);
        tv_common_problem = (TextView) findViewById(R.id.tv_common_problem);
        iv_common_problem = (ImageView) findViewById(R.id.iv_common_problem);
        tv_fly_screen_parent_dir = (TextView) findViewById(R.id.tv_fly_screen_parent_dir);
    }

    @Override
    public void initListener() {
        imageview_back.setOnClickListener(this);
        tv_rescan.setOnClickListener(this);
        tv_common_problem.setOnClickListener(this);
        iv_common_problem.setOnClickListener(this);
        tv_fly_screen_parent_dir.setOnClickListener(this);
        listview_device.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (deviceInfoList.size() > position)
                    flyScreenBusiness.requestLoginData(deviceInfoList.get(position));
            }
        });
        listview_video.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (pageItemList.size() > position)
                    flyScreenBusiness.handlePageItem(pageItemList.get(position));
            }
        });
        flyDeviceListAdapter = new FlyScreenDeviceListAdapter(FlyScreenActivity.this, deviceInfoList);
        flyVideoListAdapter = new FlyScreenVideoListAdapter(FlyScreenActivity.this, pageItemList);
        listview_device.setAdapter(flyDeviceListAdapter);
        listview_video.setAdapter(flyVideoListAdapter);
        flyScreenBusiness = new FlyScreenBusiness();
        flyScreenBusiness.init(FlyScreenActivity.this);
        flyScreenBusiness.setTcpReceiver(true);
        flyScreenBusiness.setFlyScreenListener(this);
        rl_fly_screen_guide.setFlyScreenBusiness(flyScreenBusiness);

        layout_SlideLayout.setOnSildingFinishListener(new SlideLayout.OnSildingFinishListener() {

            @Override
            public void onSildingFinish() {
                finish();
            }
        });
    }

    @Override
    public void initData() {
        tv_title.setText("飞屏");
    }

    @Override
    public void onClick(View v) {
        int resid = v.getId();
        if (resid == R.id.tv_rescan) {
            setDeviceNotFoundVisibility(View.GONE);
            flyScreenBusiness.startScan();
        } else if (resid == R.id.tv_common_problem || resid == R.id.iv_common_problem) {
            reportClick("airwiki");
            flyScreenBusiness.startFlyScreenHelpActivity();
        } else if (resid == R.id.tv_fly_screen_parent_dir) {
            flyScreenBusiness.backToParentDir();
        } else if(resid == R.id.imageview_back){
            finish();
        }
    }

    private void setDeviceNotFoundVisibility(int visibility) {
        if (rl_fly_screen_not_found.getVisibility() == View.GONE && visibility == View.VISIBLE) {
            rl_fly_screen_not_found.setVisibility(View.VISIBLE);
            rl_content.setVisibility(View.GONE);
        } else if (rl_fly_screen_not_found.getVisibility() == View.VISIBLE && visibility == View.GONE) {
            rl_fly_screen_not_found.setVisibility(View.GONE);
        }
    }

    private void dealDeviceFindResult() {
        List<DeviceInfo> udpdevices = flyScreenBusiness.getmDeviceInfos();
        deviceInfoList.clear();
        deviceInfoList.addAll(udpdevices);
        if (deviceInfoList.size() > 0) {
            rl_fly_screen_loading.setVisibility(View.GONE);
            rl_content.setVisibility(View.VISIBLE);
            setDeviceNotFoundVisibility(View.GONE);
            tv_fly_screen_parent_dir.setVisibility(View.GONE);
            listview_video.setVisibility(View.GONE);
            listview_device.setVisibility(View.VISIBLE);
            fly_screen_name.setText(getResources().getString(R.string.flyscreen_device_list));
            flyDeviceListAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        guideViewShow();
    }

    public void guideViewShow() {
        rl_content.setVisibility(View.GONE);
        listview_video.setVisibility(View.GONE);
        tv_fly_screen_parent_dir.setVisibility(View.GONE);
        setDeviceNotFoundVisibility(View.GONE);
        if (flyScreenBusiness.isSkipGuide()) {
            rl_fly_screen_guide.setStepGuidEnd();
            flyScreenBusiness.startScan();
        } else {
            rl_fly_screen_guide.checkBeginStepGuid();
            rl_fly_screen_guide.showGuide();
        }
    }

    @Override
    public void onDestroy() {
        flyScreenBusiness.onDestroy();
        deviceInfoList.clear();
        super.onDestroy();
    }

    public boolean onKeyDown(View v, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            try {
                flyScreenBusiness.backToParentDir();
                return true;
            } catch (Exception e) {
            }
        }
        return false;
    }

    @Override
    public void onMessageReceived(final int type) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                handleMessage(type);//处理消息
            }
        });
    }

    /**
     * 处理消息
     */
    private void handleMessage(int messageType){
        switch (messageType) {//消息类型
            case FlyScreenConstant.SHOW_PROGRESS_BAR:
                rl_fly_screen_loading.setVisibility(View.VISIBLE);
                break;
            case FlyScreenConstant.FLY_SCREEN_LOGIN_PWD_ERROR:
                Toast.makeText(this, getResources().getString(R.string.flyscreen_password_error), Toast.LENGTH_SHORT).show();
                break;
            case FlyScreenConstant.FLY_SCREEN_RECONNECT:
                Toast.makeText(this, getResources().getString(R.string.flyscreen_reconnect), Toast.LENGTH_SHORT).show();
                break;
            case FlyScreenConstant.FIND_FLY_SCREEN:
                dealDeviceFindResult();
                break;
            case FlyScreenConstant.FLY_SCREEN_NOT_FOUND:
                rl_fly_screen_loading.setVisibility(View.GONE);
                setDeviceNotFoundVisibility(View.VISIBLE);
                break;
            case FlyScreenConstant.BACK_TO_DEVICE_LIST:
                //设备列表显示
                listview_device.setVisibility(View.VISIBLE);
                //设备详细列表隐藏
                listview_video.setVisibility(View.GONE);
                //返回上级隐藏
                tv_fly_screen_parent_dir.setVisibility(View.GONE);
                fly_screen_name.setText(getResources().getString(R.string.flyscreen_device_list));
                break;
            case FlyScreenConstant.FLY_SCREEN_SERVERCLOSED:
                Toast.makeText(this, getResources().getString(R.string.flyscreen_serverclosed), Toast.LENGTH_SHORT).show();
                guideViewShow();
                break;
            case FlyScreenConstant.FLY_SCREEN_RESOURCE_FAIL:
                Toast.makeText(this, getResources().getString(R.string.flyscreen_resource_fail), Toast.LENGTH_SHORT).show();
                break;
            case FlyScreenConstant.FLY_SCREEN_RESOURCE_FAIL_NOTLOGIN:
                Toast.makeText(this, getResources().getString(R.string.flyscreen_resource_fail_notlogin), Toast.LENGTH_SHORT).show();
                break;
            case FlyScreenConstant.FLY_SCREEN_NETWORK_ERROR:
                Toast.makeText(this, getResources().getString(R.string.flyscreen_network_error), Toast.LENGTH_SHORT).show();
                rl_fly_screen_loading.setVisibility(View.GONE);
                setDeviceNotFoundVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public void onDataReceived(int type, final Object obj) {
        if (type == FlyScreenConstant.GET_FILES_FROM_FLY_SCREEN) {
            if (obj instanceof Collection) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        listview_video.setVisibility(View.VISIBLE);
                        tv_fly_screen_parent_dir.setVisibility(View.VISIBLE);
                        listview_device.setVisibility(View.GONE);
                        fly_screen_name.setText(flyScreenBusiness.getCurrentDevice().getName());
                        pageItemList.clear();
                        pageItemList.addAll((List<Resource.PageItem>) obj);
                        flyVideoListAdapter.notifyDataSetInvalidated();
                        FlyScreenUtil.saveSubtitleFile(flyScreenBusiness, pageItemList);
                    }
                });
            }
        }
    }

    private void reportClick(String airvideohelp){
        try {
            JSONObject reportClick = new JSONObject();
            reportClick.put("etype", "click");
            reportClick.put("clicktype", "chooseitem");
            reportClick.put("tpos", "1");
            reportClick.put("pagetype", "airvideo");
            reportClick.put("local_menu_id", "3");
            reportClick.put("airevideohelp", airvideohelp);
            MojingSDKReport.onEvent(reportClick.toString(), "UNKNOWN", "UNKNOWN", 0, "UNKNOWN", 0);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
