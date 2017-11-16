package com.mojing.vrplayer.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.bfmj.viewcore.util.GLExtraData;
import com.bfmj.viewcore.view.GLViewPage;
import com.mojing.vrplayer.bean.VideoModeType;
import com.mojing.vrplayer.page.BasePlayerPage;
import com.mojing.vrplayer.page.LocalMoviePlayPage;
import com.mojing.vrplayer.page.LocalPanoPlayPage;
import com.mojing.vrplayer.page.SplashPage;
import com.mojing.vrplayer.publicc.CreateHistoryUtil;
import com.mojing.vrplayer.publicc.HistoryBusiness;
import com.mojing.vrplayer.publicc.ReportBusiness;
import com.mojing.vrplayer.publicc.SettingSpBusiness;
import com.mojing.vrplayer.publicc.bean.DownloadItem;
import com.mojing.vrplayer.publicc.bean.HistoryInfo;
import com.mojing.vrplayer.publicc.bean.LocalVideoBean;
import com.mojing.vrplayer.publicc.bean.PanoramaVideoBean;
import com.mojing.vrplayer.publicc.bean.VideoDetailBean;
import com.mojing.vrplayer.utils.BitmapUtil;
import com.mojing.vrplayer.utils.MJGLUtils;
import com.mojing.vrplayer.utils.SharedPreferencesTools;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * Created by wanghongfang on 2017/4/1.
 * 魔镜VR模式播放activity
 */
public class MjVrPlayerActivity extends GLBaseActivity {

    public static PanoramaVideoBean panoramaVideoBean;
    public static VideoDetailBean VideoBean;
    public static List<LocalVideoBean> videoList; //本地列表数据
    public static  List<DownloadItem> downloadedList; //已下载
    private int mType = 0;
    String vidotype;
    String videoPath;
    String videoName;
    int mIndex;
    int playmode =  VideoModeType.PLAY_MODE_SIMPLE;
    String detailUrl;
    private String pageType; //报数用的
    private int vvplaysource = 2; //报数用的
    public int mCurPlayMode = VideoModeType.PLAY_MODE_VR;//播放模式： 极简模式或沉浸模式
    public int isFirst = 0;
    private String local_menu_id;//本地报数使用  //1:手机存储 2：已下载 3：飞屏 4：外部打开
    public boolean isComeFromUnity;//是否从unity进入播放

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        super.onCreate(savedInstanceState);

        setImmersiveSticky();
        this.getWindow()
                .getDecorView()
                .setOnSystemUiVisibilityChangeListener(
                        new View.OnSystemUiVisibilityChangeListener() {
                            @Override
                            public void onSystemUiVisibilityChange(int visibility) {
                                if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
                                    setImmersiveSticky();
                                }
                            }
                        });
        mType = getIntent().getExtras().getInt("type");
        isFirst = SettingSpBusiness.getInstance(this.getApplicationContext()).getPlayDetailIsFirst();
        if(isFirst==1){
            SettingSpBusiness.getInstance(this.getApplicationContext()).setPlayDetailIsFirst(0);
        }
//        getPageManager().push(new TestPage(MjVrPlayerActivity.this), null);
        init();
    }

    private void init() {
        detailUrl = getIntent().getExtras().getString("detail_url");
        vidotype = getIntent().getStringExtra("videoType");
        videoPath = getIntent().getStringExtra("videoPath");
        videoName = getIntent().getStringExtra("videoName");
        mIndex = getIntent().getIntExtra("index",-1);
        if(getIntent().getExtras()!=null&&getIntent().getExtras().containsKey("play_mode")) {
            playmode = getIntent().getIntExtra("play_mode", VideoModeType.PLAY_MODE_VR);
        }

        pageType = getIntent().getStringExtra("pageType");
        vvplaysource = getIntent().getIntExtra("vvplaysource",2);
        local_menu_id = getIntent().getStringExtra("local_menu_id");
        isComeFromUnity = getIntent().getBooleanExtra("isComeFromUnity",false);
        //首页跳转和历史跳转需要先请求详情数据
//        if(ReportBusiness.PAGE_TYPE_RECOMMEND.equals(pageType)||ReportBusiness.PAGE_TYPE_ACCOUNT.equals(pageType)){
//
//
//        }

        if(mType==VideoModeType.PANO_TYPE){
            if(panoramaVideoBean==null) {
                loadPanoramaDetail(detailUrl);
            }
        }else if(mType==VideoModeType.MOVIE_TYPE){
            if(VideoBean==null) {
                loadMovieDetailData(detailUrl);
            }
        }
        startPlayPage();
    }

    public void changeMoviePage(VideoDetailBean videoBean,String detailUrl,int playmode,String pageType,int index){
        this.detailUrl = detailUrl;
        this.playmode = playmode;
        VideoBean = videoBean;
        this.pageType = pageType;
        this.mIndex = index;
        this.mType = VideoModeType.MOVIE_TYPE;
        startPlayPage();
    }
    public void changePanoPage(PanoramaVideoBean videoBean,String detailUrl,int playmode,String pageType){
        this.detailUrl = detailUrl;
        this.playmode = playmode;
        panoramaVideoBean = videoBean;
        this.pageType = pageType;
        this.mType = VideoModeType.PANO_TYPE;
        startPlayPage();
    }

    public void changeLocalPage(String videoType,String videoPath,String videoName,int mIndex,int playmode,String pageType){
        this.vidotype = videoType;
        this.videoPath = videoPath;
        this.videoName = videoName;
        this.mIndex = mIndex;
        this.playmode = playmode;
        this.pageType = pageType;
        startLocalPage(videoType,videoPath,videoName,mIndex);
    }

    public void changeDownloadPage(int mIndex,int playmode,String pageType){
        this.mIndex = mIndex;
        this.playmode = playmode;
        this.pageType = pageType;
        startDownloadPlayPage(mIndex);
    }

    public void startPlayPage(){
//        MJGLUtils.exeGLQueueEvent(this, new Runnable() {
//            @Override
//            public void run() {
                //切片后底部操作区域要恢复成加号
//                Log.d("cursor","--------hideCursorView---startPlayPage");
//                hideCursorView();
                showClose(false);
                if(mType == VideoModeType.MOVIE_TYPE){ //影院
                    startMoviePage();
                }else if(mType == VideoModeType.PANO_TYPE){//全景
                    startPanoPage();
                }else if(mType==VideoModeType.LOCAL_TYPE){ //本地
                    startLocalPage(vidotype,videoPath,videoName,mIndex);
                }else if(mType==VideoModeType.DOWNLOAD_TYPE){ //已下载
                    startDownloadPlayPage(mIndex);
                }
//            }
//        });


    }

    public void startDownloadPlayPage(int mIndex){
        if(downloadedList==null||downloadedList.size()<=0||mIndex>downloadedList.size()){
           return;
        }
        DownloadItem downloadItem = downloadedList.get(mIndex);
        if(downloadItem==null) {
            return;
        }

         final int vidoModelType = downloadItem.getIs_panorama();
        final GLExtraData playExtraData = new GLExtraData();
        playExtraData.putExtraInt("index",mIndex);
        playExtraData.putExtraInt("play_mode",playmode);
        playExtraData.putExtraString("pageType",pageType);
        playExtraData.putExtraInt("vvplaysource",vvplaysource);
//        MJGLUtils.exeGLQueueEvent(this, new Runnable() {
//            @Override
//            public void run() {
//                if (vidoModelType == VideoModeType.Mode_Rect) {//本地2d视频
//                    if (!SettingSpBusiness.getInstance().getVrGuide()) {
//                        getPageManager().push(new SplashPage(MjVrPlayerActivity.this), null);
//                        return;
//                    }
//                    getPageManager().push(new DownLoadedMoviePlayPage(MjVrPlayerActivity.this), playExtraData);
//                } else {
//                    getPageManager().push(new DownLoadedPanoPlayPage(MjVrPlayerActivity.this), playExtraData);
//                }
//
//            }
//        });


    }



    private void startPanoPage(){
        final GLExtraData playExtraData = new GLExtraData();
        if(panoramaVideoBean==null) {
            return;
        }
        if(getIntent().getExtras()!=null){
            playExtraData.putExtraString("detail_url",detailUrl);
            playExtraData.putExtraInt("play_mode",playmode);
        }
        playExtraData.putExtraObject("videobean",panoramaVideoBean);
        playExtraData.putExtraString("pageType",pageType);
        playExtraData.putExtraInt("vvplaysource",vvplaysource);

//        MJGLUtils.exeGLQueueEvent(this, new Runnable() {
//            @Override
//            public void run() {
//                getPageManager().push(new PanoNetPlayPage(MjVrPlayerActivity.this), playExtraData);
//            }
//        });

    }

    private void startMoviePage(){
        final GLExtraData playExtraData = new GLExtraData();
        //playmode != VideoModeType.PLAY_MODE_SIMPLE_FULL &&
        if(!SettingSpBusiness.getInstance(this.getApplicationContext()).getVrGuide()) {
            MJGLUtils.exeGLQueueEvent(this, new Runnable() {
                @Override
                public void run() {
                    getPageManager().push(new SplashPage(MjVrPlayerActivity.this), null);
                }
            });

            return;
        }
        if(VideoBean==null) {
            return;
        }
        if(getIntent().getExtras()!=null){
            playExtraData.putExtraObject("index",mIndex);
            playExtraData.putExtraString("detail_url",detailUrl);
            playExtraData.putExtraInt("play_mode",playmode);
        }
        playExtraData.putExtraObject("videobean",VideoBean);
        playExtraData.putExtraString("pageType",pageType);
        playExtraData.putExtraInt("vvplaysource",vvplaysource);

//        MJGLUtils.exeGLQueueEvent(this, new Runnable() {
//            @Override
//            public void run() {
//                getPageManager().push(new MoviePlayPage(MjVrPlayerActivity.this), playExtraData);
//            }
//        });
    }

    private void startLocalPage(String type,String path,String name,int mIndex){
        final GLExtraData playExtraData = new GLExtraData();
        if(TextUtils.isEmpty(type)){
            vidotype = "0";
            type = "0";
        }
        final HistoryBusiness.VideoViewparam videoViewparam =  HistoryBusiness.JudgeVideoType(Integer.parseInt(type));
        String history = HistoryBusiness.readFromHistory(path,0);
        HistoryInfo historyInfo = null;
        try {
            if(history!=null) {
                JSONObject myJsonObject = new JSONObject(history);
                historyInfo = CreateHistoryUtil.localJsonToHistoryInfo(myJsonObject);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        if (historyInfo != null&&historyInfo.getVideo3dType()>0) {
            videoViewparam.mVideo3DType = historyInfo.getVideo3dType();
            videoViewparam._videoModelType = historyInfo.getVideoType();
        }

        playExtraData.putExtraString("videoPath",path);
        playExtraData.putExtraString("videoName",name);
        playExtraData.putExtraInt("videoType",videoViewparam._videoModelType);
        playExtraData.putExtraInt("video3DType",videoViewparam.mVideo3DType);
        playExtraData.putExtraInt("index",mIndex);
        playExtraData.putExtraInt("play_mode",playmode);
        playExtraData.putExtraString("pageType",pageType);
        playExtraData.putExtraInt("vvplaysource",vvplaysource);
        playExtraData.putExtraString("local_menu_id",local_menu_id);

        MJGLUtils.exeGLQueueEvent(this, new Runnable() {
            @Override
            public void run() {
                if (videoViewparam._videoModelType == VideoModeType.Mode_Rect) {//本地2d视频
                    if (!SettingSpBusiness.getInstance(MjVrPlayerActivity.this.getApplicationContext()).getVrGuide()) {
                        getPageManager().push(new SplashPage(MjVrPlayerActivity.this), null);
                        return;
                    }
                    getPageManager().push(new LocalMoviePlayPage(MjVrPlayerActivity.this), playExtraData);
                } else {
                    getPageManager().push(new LocalPanoPlayPage(MjVrPlayerActivity.this), playExtraData);
                }
            }
        });



    }

    @Override
    protected void onDestroy() {
        if(ReportBusiness.PAGE_TYPE_DETAIL.equals(pageType)){
            if( getPageManager().getIndexView()!=null){
                this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        getPageManager().getIndexView().finish();
                    }
                });

            }
        }
        SharedPreferencesTools.getInstance(this).saveLastVideoName(videoName);
        super.onDestroy();
        VideoBean = null;
        panoramaVideoBean = null;
        videoList = null;
        SettingSpBusiness.getInstance(this.getApplicationContext()).setPlayDetailIsFirst(1);
        MJGLUtils.exeGLQueueEvent(this, new Runnable() {
            @Override
            public void run() {
                HashMap<String, Bitmap> map = BitmapUtil.map;
                Iterator<String> it = map.keySet().iterator();
                while(it.hasNext()) {
                    String key = it.next();
                    Bitmap bitmap = map.get(key);
                    if(null != bitmap) {
                        if(!bitmap.isRecycled()) {
                            bitmap.recycle();
                        }
                    }
                    it.remove();
                }

            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){
            finish();
        }else if(keyCode==KeyEvent.KEYCODE_VOLUME_DOWN ||keyCode==KeyEvent.KEYCODE_VOLUME_UP||keyCode==KeyEvent.KEYCODE_VOLUME_MUTE){ /**系统音量按键事件检测*/
            GLViewPage page = getPageManager().getIndexView();
            if(page!=null){
               if(page instanceof BasePlayerPage){
                   ((BasePlayerPage)page).updateVolumChange(keyCode);
               }
            }

        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void finish() {
        if(ReportBusiness.PAGE_TYPE_DETAIL.equals(pageType)){
            if( getPageManager().getIndexView()!=null){
                this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        getPageManager().getIndexView().finish();
                    }
                });

            }
        }
        super.finish();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            setImmersiveSticky();
        }
    }

    private void setImmersiveSticky() {
        this.getWindow()
                .getDecorView()
                .setSystemUiVisibility(
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                                | View.SYSTEM_UI_FLAG_FULLSCREEN
                                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }

    private void loadMovieDetailData(String detailUrl) {
//        new VideoApi().getVideoDetailInfo(this, detailUrl, new ApiCallBack<ResponseBaseBean<VideoDetailBean>>() {
//
//            @Override
//            public void onStart() {
//                super.onStart();
//
//            }
//
//            @Override
//            public void onFinish() {
//                super.onFinish();
//
//            }
//
//            @Override
//            public void onSuccess(ResponseBaseBean<VideoDetailBean> result) {
//                super.onSuccess(result);
//                if (result != null) {
//                    if (result.getStatus() == 0) {
//                        if (result.getData() != null) {
//                            VideoBean = result.getData();
//                            startPlayPage();
//                        }
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(Throwable error, String content) {
//                super.onFailure(error, content);
//
//            }
//        });
    }

    private void loadPanoramaDetail(final String url) {

//                new ChoicenessApi().getPanoramaDetailInfo(MjVrPlayerActivity.this, url, new ApiCallBack<ResponseBaseBean<PanoramaVideoBean>>() {
//                    @Override
//                    public void onStart() {
//                        super.onStart();
//                    }
//                    @Override
//                    public void onFinish() {
//                        super.onFinish();
//                    }
//                    @Override
//                    public void onSuccess(final ResponseBaseBean<PanoramaVideoBean> result) {
//                        super.onSuccess(result);
//                        if (result != null) {
//                            if (result.getStatus() == 0) {
//                                if (result.getData() != null) {
//                                    panoramaVideoBean = result.getData();
//                                    startPlayPage();
//                                }
//                            }
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Throwable error, String content) {
//                        super.onFailure(error, content);
//
//                    }
//                });

    }

    public void onLockChanged(boolean isLockScreen){
      this.setCursorFixed(!isLockScreen);
      this.setSkyboxFixed(isLockScreen);
      this.fixedResetView(isLockScreen);
       if(isLockScreen) {
           hideSkyBox();
       }else {
           showSkyBox();
       }
  }
}
