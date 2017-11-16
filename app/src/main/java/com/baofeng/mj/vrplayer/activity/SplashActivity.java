package com.baofeng.mj.vrplayer.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.widget.ImageView;
import android.widget.Toast;

import com.baofeng.mj.vrplayer.MyAppliaction;
import com.baofeng.mj.vrplayer.R;
import com.baofeng.mj.vrplayer.util.PermissionUtil;
import com.storm.smart.play.utils.LibraryUtils;
import com.storm.smart.play.utils.PlayCheckUtil;

/**
 * app启动页
 */
public class SplashActivity extends BaseActivity {
    private ImageView iv_splash;
    private boolean toMainActivity = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        iv_splash = (ImageView) findViewById(R.id.iv_splash);
        iv_splash.setImageResource(R.mipmap.splash);
        if(PermissionUtil.checkStoragePermission(this)){
            init();
        }
        MyAppliaction.getInstance().initFileUploadBusiness(MyAppliaction.getInstance());
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {

    }

    @Override
    public void initListener() {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if(requestCode == 200){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {//权限授予
                init();
            } else {//权限未授予
                Toast.makeText(this, "权限未授予！", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            toMainActivity = false;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void init(){
        libSOInit();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                gotoMainActivity();
            }
        }, 2000);
    }

    /**
     * 进入主界面
     */
    private void gotoMainActivity(){
        if(toMainActivity){
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
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
