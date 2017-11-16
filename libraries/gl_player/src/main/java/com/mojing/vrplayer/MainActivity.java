package com.mojing.vrplayer;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.baofeng.mojing.MojingSDK;
import com.mojing.vrplayer.activity.MjVrPlayerActivity;
import com.mojing.vrplayer.bean.VideoModeType;
import com.mojing.vrplayer.publicc.CheckPermission;
import com.mojing.vrplayer.publicc.PermissionListener;
import com.mojing.vrplayer.publicc.PermissionUtil;
import com.mojing.vrplayer.publicc.ReportBusiness;
import com.mojing.vrplayer.publicc.bean.LocalVideoBean;
import com.storm.smart.play.utils.LibraryUtils;
import com.storm.smart.play.utils.PlayCheckUtil;

import java.util.ArrayList;

public class MainActivity extends Activity {
    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText = (EditText) findViewById(R.id.edit_text);
        Button play_btn = (Button) findViewById(R.id.play_btn);
        play_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doPlay();
            }
        });
        String externalStorageDir = Environment.getExternalStorageDirectory().getAbsolutePath();
        String path = externalStorageDir + "/2.mp4";
        editText.setText(path);
        int permission = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission == PackageManager.PERMISSION_GRANTED) {
            init();
        } else {
            if (!(Build.VERSION.SDK_INT >= 23)) {
                init();
            }
        }
        checkPermission();
        ArrayList<LocalVideoBean> list = new ArrayList<>();
        for(int i = 0;i<21;i++){
            LocalVideoBean bean = new LocalVideoBean();
            bean.name = "一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十"+i;
            bean.path = editText.getText().toString().trim();
            list.add(bean);
        }
        MjVrPlayerActivity.videoList = list;
    }

    private void doPlay() {
        int index = 2;//默认选第3个影片进入
        Intent intent = new Intent(this, MjVrPlayerActivity.class);
        intent.putExtra("type", VideoModeType.LOCAL_TYPE);
        intent.putExtra("videoPath", MjVrPlayerActivity.videoList.get(index).path);
        intent.putExtra("videoName", MjVrPlayerActivity.videoList.get(index).name);
        intent.putExtra("videoType", "0");//扫描后可得类型
        intent.putExtra("play_mode", VideoModeType.PLAY_MODE_SIMPLE_FULL);
        intent.putExtra("pageType", ReportBusiness.PAGE_TYPE_LOCAL);
        intent.putExtra("index",index);//该影片是列表中的第几个，需要计算
        this.startActivity(intent);
    }

    private void checkPermission() {
        CheckPermission.from(this)
                .setPermissions(PermissionUtil.ALL_PERMISSIONS)
                .setPermissionListener(new PermissionListener() {
                    @Override
                    public void permissionGranted() {
                        if (PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(MainActivity.this,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                            init();
                        }
                    }

                    @Override
                    public void permissionDenied() {
                        if (PackageManager.PERMISSION_DENIED == ContextCompat.checkSelfPermission(MainActivity.this,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                        }
                    }
                }).check();
    }


    public void init() {
        try {
            if (!MojingSDK.GetInitSDK()) {
                MojingSDK.Init(this.getApplicationContext());
             //   MojingSDK.SetEngineVersion("Android");
                MojingSDK.onDisableVrService(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        libSOInit();//初始化播放相关
//        initSDKListeners();//初始化魔镜5代

    }

    public void libSOInit() {
        PlayCheckUtil.checkLibs(this, new LibraryUtils.OnLibraryInitListener() {
            @Override
            public void onLibraryInitResult(boolean result) {
                // 解压SO库的结果,如果解压失败,软解无法播放.解压失败的原因一般是内置存储空间不足;
//                LogHelper.d(TAG, "onLibraryInitResult result = " + result );
            }
        });
    }
}
