package com.baofeng.mj.smb.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.baofeng.mj.ui.view.CustomProgressView;
import com.baofeng.mj.ui.view.EmptyView;
import com.baofeng.mj.util.publicutil.ConfigUrl;
import com.baofeng.mj.vrplayer.R;
import com.baofeng.mj.vrplayer.activity.BaseActivity;
import  com.baofeng.mj.util.publicutil.NetworkUtil;

/** 201704XX版本新增 帮助与反馈
 * Created by muyu on 2017/4/17.
 */
public class HelpFeedBackActivity extends BaseActivity implements View.OnClickListener{

    private WebView mWebView;
    private String loadUrl;
    private EmptyView emptyView;
    private CustomProgressView progressView;
    private ImageButton backImgBtn;
    private TextView feedbackTV;
    private TextView app_title_name;

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
////        initData();
////        initView();
//    }

    public void initData(){
        loadUrl = ConfigUrl.HELP_FEEDBACK_URL;
    }

    @Override
    public void initListener() {

    }

    public void initView(){
        setContentView(R.layout.activity_help_feedback);
        progressView = (CustomProgressView) findViewById(R.id.h5_loading);
        emptyView = (EmptyView) findViewById(R.id.h5_empty_view);
        emptyView.getRefreshView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (NetworkUtil.networkEnable(HelpFeedBackActivity.this)) {
                    emptyView.setVisibility(View.GONE);
                    progressView.setVisibility(View.VISIBLE);
                    mWebView.loadUrl(loadUrl);
                } else {
                    Toast.makeText(HelpFeedBackActivity.this, "当前网络不可用，请稍候再试", Toast.LENGTH_SHORT).show();
                }
            }
        });
        mWebView = (WebView) findViewById(R.id.h5_webview);
        app_title_name = (TextView) findViewById(R.id.app_title_name);
        backImgBtn = (ImageButton) findViewById(R.id.app_title_back_imagebtn);
        backImgBtn.setOnClickListener(this);
        feedbackTV = (TextView) findViewById(R.id.app_title_right);
        feedbackTV.setOnClickListener(this);
        WebSettings webSettings = mWebView.getSettings();
        mWebView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        mWebView.getSettings().setJavaScriptEnabled(true);
        webSettings.setPluginState(WebSettings.PluginState.ON);
        // 设置 缓存模式
        mWebView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        // 开启 Application Caches 功能
        mWebView.getSettings().setAppCacheEnabled(true);
        // 开启 DOM storage API 功能
        mWebView.getSettings().setDomStorageEnabled(true);
        // 开启 database storage API 功能
        mWebView.getSettings().setDatabaseEnabled(true);
        // / 设置可以支持缩放
        mWebView.getSettings().setSupportZoom(true);
        mWebView.getSettings().setDisplayZoomControls(false);
        mWebView.getSettings().setBuiltInZoomControls(true);
        //清除缓存
        mWebView.clearCache(true);
        mWebView.getSettings().setUseWideViewPort(true);
        mWebView.getSettings().setLoadWithOverviewMode(true);
        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                if(!TextUtils.isEmpty(title)) {
                    app_title_name.setText(title);
                } else {
                    app_title_name.setText("帮助与反馈");
                }
            }
        });

        mWebView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                if (url != null && !(url.startsWith("http://") || url.startsWith("https://"))) {
                    return true;
                } else {
                    return false;
                }
            }

            @Override
            public void onReceivedError(WebView view, int errorCode,
                                        String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                emptyView.setVisibility(View.VISIBLE);
                progressView.setVisibility(View.GONE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                progressView.setVisibility(View.GONE);
            }
        });

//        mWebView.loadUrl("file:///android_asset/index.html");
        mWebView.loadUrl(loadUrl);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mWebView.clearCache(true);
        mWebView.clearSslPreferences();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if(mWebView.canGoBack()){
                mWebView.goBack();// 返回前一个页面
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id == R.id.app_title_back_imagebtn){
            if(mWebView.canGoBack()){
                mWebView.goBack();// 返回前一个页面
            }else {
                finish();
            }
        } else if(id == R.id.app_title_right){
            //TODO startActivityForResult(new Intent(this, FeedbackActivity.class),200);
          //  startActivityForResult(new Intent(this, FeedbackActivity.class),200);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 200 && resultCode == 200) {
            finish();
        }
    }
}
