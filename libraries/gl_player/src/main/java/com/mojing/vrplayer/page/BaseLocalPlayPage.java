package com.mojing.vrplayer.page;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.bfmj.sdk.util.TimeFormat;
import com.bfmj.viewcore.interfaces.IGLPlayer;
import com.bfmj.viewcore.util.GLExtraData;
import com.bfmj.viewcore.view.GLPanoView;
import com.google.gson.Gson;
import com.mojing.vrplayer.R;
import com.mojing.vrplayer.activity.MjVrPlayerActivity;
import com.mojing.vrplayer.bean.VideoModeType;
import com.mojing.vrplayer.publicc.CreateHistoryUtil;
import com.mojing.vrplayer.publicc.HistoryBusiness;
import com.mojing.vrplayer.publicc.MD5Util;
import com.mojing.vrplayer.publicc.ReportBusiness;
import com.mojing.vrplayer.publicc.SettingSpBusiness;
import com.mojing.vrplayer.publicc.StickUtil;
import com.mojing.vrplayer.publicc.ThreadProxy;
import com.mojing.vrplayer.publicc.VideoTypeUtil;
import com.mojing.vrplayer.publicc.bean.HistoryInfo;
import com.mojing.vrplayer.publicc.bean.LocalVideoBean;
import com.mojing.vrplayer.utils.GLConst;
import com.mojing.vrplayer.utils.GLSubTitleUtils;
import com.mojing.vrplayer.utils.MJGLUtils;
import com.mojing.vrplayer.utils.MediaHelp;
import com.mojing.vrplayer.view.BasePlayerView;
import com.mojing.vrplayer.view.GLTextToast;
import com.storm.smart.core.PlayCorehUtil;
import com.storm.smart.play.baseplayer.BaseSurfacePlayer;
import com.storm.smart.play.call.IBfPlayerConstant;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by wanghongfang on 2017/5/12.
 * 本地播放基类
 */
public abstract class BaseLocalPlayPage extends BasePlayerPage  {
        protected HistoryInfo historyInfo;//存储的播放的历史数据
        protected String mVideoPath;
        protected String titleName;
        protected String suffix = "";
        protected String fileMd5="";
        private int video3DType = VideoModeType.Video2D;
        private int videoModelType = VideoModeType.Mode_Rect;
        private String subtitleName =""; //字幕
        private int mAudioTrack = -1;//音轨
        protected int file_duration;//总时长
        protected String mRatioType = "原始";//画面比例
        protected int mRotateAngle = 0; //旋转角度
        protected int rePortBeginVrType; //记录进入播放时选择的模式，退出播放时报数使用
        public ArrayList<String> subtitlesList = new ArrayList<String>();//字幕列表
        protected int decodeType = VideoModeType.PLAYER_AUTO;
        private PlayCorehUtil playerCoreh;
        /* 内嵌字幕总数  当前选择的字幕id   subType：当前选择的是内嵌或外挂类型 ：1是内嵌  2是外挂*/
        private int innerSubCount = 0, chooseSub = -1, subType = 0;
        public int index;
        private String local_menu_id;
        public BaseLocalPlayPage(Context context,int type) {
            super(context, type);
        }

        protected void initData(GLExtraData data) {
            if(data==null)
                return;
            mVideoPath = data.getExtraString("videoPath");
            titleName = data.getExtraString("videoName");
            videoModelType = data.getExtraInt("videoType");//视频类型
            video3DType = data.getExtraInt("video3DType"); //播放3d類型
            index = data.getExtraInt("index");
            local_menu_id = data.getExtraString("local_menu_id");
            rePortBeginVrType = getReportVrType();
            setData();
            playUINotifyBusiness.setLocalMovieSelectVideoDatas(MjVrPlayerActivity.videoList,index);
            playUINotifyBusiness.setName(titleName);
            playUINotifyBusiness.setSelectedScreenSize(SettingSpBusiness.getInstance(mActivity).getPlayerScreenType());
        }

        /**
         * 设置播放资源数据
         */
        public void setData(){

            startReport = System.currentTimeMillis();
            reSetRoundId();
            initPlayFromHistory();
            getSuffix();
            playerView.isloacl = true;

            if(playerView.mPrepareSeekTo>1000) {
                String time = TimeFormat.formatCH(playerView.mPrepareSeekTo/1000,false);
                if(playUINotifyBusiness!=null){
                    playUINotifyBusiness.setLoadText(time,titleName);
                }
            }else {
                if(playUINotifyBusiness!=null){
                    playUINotifyBusiness.setLoadText("",titleName);
                }
            }
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if(mActivity==null||mActivity.isFinishing())
                    {
                        return;
                    }
                    initPlay();
                    mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(playerView!=null) {
                                playerView.startPlayer();
                            }
                        }
                    });
                }
            }).start();
            reportVV("1", "0", "0"  );
            initControlUI();
        }

        @Override
        public void onResume() {
            super.onResume();
            if(playerView==null|| MediaHelp.mPlayer==null){
                return;
            }

            //没有异常弹窗并且是可以播放的状态下，根据上次的播放状态设置继续播放或暂停状态。否则只显示页面不进行播放操作
            if(moviePlayerControlView!=null&&!moviePlayerControlView.isDialogShowing()) {
                if (moviePlayerControlView.isPlayFlag()) {
                    pausePlay();
                } else {
                    if (playerView.mCurrentState != BasePlayerView.PlayerState.PREPARED) {
                        rePlay();
                    } else {
                        startPlay();
                    }
                }
            }
        }

        @Override
        public void onPause() {
            super.onPause();
            if(playerView!=null) {
                pausePlay();
                saveHistory();
            }
        }

        @Override
        public void onFinish() {
            super.onFinish();
            if (playerCoreh != null)
                playerCoreh.SubContextDone();
            if(playerView!=null) {
                if(!(playerView.mCurrentState== BasePlayerView.PlayerState.COMPLETE||playerView.mCurrentState== BasePlayerView.PlayerState.ERROR)) {
                    reportPlaySuccess();
                }
                pausePlay();
                playerView.destroyView();
            }

            playerView = null;
        }

    /**
     * 进入后台，保存播放记录
     */
    private void saveHistory(){
        if(playerView==null)
            return;
        if(historyInfo==null){
            historyInfo = new HistoryInfo();
            historyInfo.setAudioTrack(-1);
            historyInfo.setType(0);
            historyInfo.setResType(0);
            historyInfo.setLastSetIndex(0);
            historyInfo.setPlayDuration(lastPlaytime);
            if(playerView!=null) {
                historyInfo.setPlayFinished(playerView.isPlayCompletion ? 1 : 0);
            }
            historyInfo.setPlayTimestamp(System.currentTimeMillis());
            historyInfo.setTotalDuration(file_duration);
            historyInfo.setVideoPlayUrl(mVideoPath);
            historyInfo.setVideoTitle(titleName);

            historyInfo.setVideoId("");
            historyInfo.setVideoImg("");
            historyInfo.setVideoClarity("");
            historyInfo.setDetailUrl("");
            historyInfo.setVideoSet(1);
        }else {
            historyInfo.setTotalDuration(file_duration);
            historyInfo.setPlayDuration(lastPlaytime);
            if(playerView!=null) {
                historyInfo.setPlayFinished(playerView.isPlayCompletion ? 1 : 0);
            }
            historyInfo.setPlayTimestamp(System.currentTimeMillis());

            historyInfo.setVideoPlayUrl(mVideoPath);
            historyInfo.setLastSetIndex(0);
        }
        historyInfo.setPlayType( decodeType);
        historyInfo.setVideo3dType(video3DType);
         historyInfo.setVideoType(videoModelType);
        historyInfo.setViewRatio(mRatioType);
        historyInfo.setViewRotate(mRotateAngle);
        historyInfo.setAudioTrack(mAudioTrack);
        historyInfo.setSubtitle(subtitleName);
        String json=new Gson().toJson(historyInfo);
        HistoryBusiness.writeToHistory(json,mVideoPath,0);
    }

    /**
     * 读取播放记录数据
     */
    private void initPlayFromHistory(){
        String history = HistoryBusiness.readFromHistory(mVideoPath,0);
        try {
            if(history!=null) {
                JSONObject myJsonObject = new JSONObject(history);
                historyInfo = CreateHistoryUtil.localJsonToHistoryInfo(myJsonObject);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        if (historyInfo != null) {
            if (!(historyInfo.getPlayFinished() == 1) &&  historyInfo.getPlayDuration()!=historyInfo.getTotalDuration()) {
                playerView.mPrepareSeekTo = historyInfo.getPlayDuration();
            }
            int m3dType = historyInfo.getVideo3dType();
            int videotype = historyInfo.getVideoType();
            if(m3dType>0&&videotype>0) {
                video3DType = m3dType;
                videoModelType = videotype;
            }
//            int type = HistoryBusiness.JudgePlayerTypeHistroyToCore( historyInfo.getPlayType());
//            if(type>0) {
//                if(playerView!=null){
//                    playerView.setDecodeType(type);
//                }
//            }
            mAudioTrack = historyInfo.getAudioTrack();
            mRatioType = historyInfo.getViewRatio();
            mRotateAngle = historyInfo.getViewRotate();
            subtitleName = historyInfo.getSubtitle();
            decodeType = historyInfo.getPlayType();
        }else {
            if(playUINotifyBusiness!=null) {
                int count = SettingSpBusiness.getInstance(mActivity).getLocalVideoTypeCount();
                if(count<3) {
                    playUINotifyBusiness.showImgToast(getTipStr(), R.drawable.play_toast_icon_model);
                    SettingSpBusiness.getInstance(mActivity).setLocalVideoTypeTipCount(count++);
                }

            }
        }
    }



    private void getSuffix(){
        if (!TextUtils.isEmpty(mVideoPath) && mVideoPath.contains(".")) {
            int index = mVideoPath.lastIndexOf(".");
            if (index > 0 && index < mVideoPath.length()) {
                suffix = mVideoPath.substring(index);
            }
        }
        ThreadProxy.getInstance().addRun(new ThreadProxy.IHandleThreadWork() {
            @Override
            public void doWork() {
                //取视频文件的前50M的md5值上传报数
                fileMd5 = MD5Util.md5FileSum(mVideoPath,30);
                reportMd5(fileMd5);
            }
        });

    }

    private String getTipStr(){
        String str="";
        switch (videoModelType){
            case VideoModeType.Mode_Rect:
                str+="平面";
                break;
            case VideoModeType.Mode_Sphere180:
                str+="半球";
                break;
            case VideoModeType.Mode_Sphere360:
                str+="球面";
                break;
        }
        switch (video3DType){
            case VideoModeType.Video2D:
                str+="-2D";
                break;
            case VideoModeType.VideoLR3D:
                str+="-3D左右";
                break;
            case VideoModeType.VideoUD3D:
                str+="-3D上下";
                break;
        }
        return str;
    }


    /**
         * 首次初始完数据开始播放
         */
        private void initPlay(){
            if(playerView==null)
                return;
            playerView.setPlayUrl(mVideoPath);
            if(decodeType!=VideoModeType.PLAYER_AUTO){
                playerView.setDecodeType(decodeType);
            }
            playerView.createPlayer();
            setSenceMode(videoModelType);
            set3DMode(video3DType);

        }


    /**
     * 设置播放3d模式
     * @param mode
     */
    public void set3DMode(int mode){
        switch (mode){
            case VideoModeType.Video2D:
                playerView.setPlayMode(GLPanoView.PLAY_TYPE_2D);
                break;
            case VideoModeType.VideoUD3D:
                playerView.setPlayMode(GLPanoView.PLAY_TYPE_3D_TB);
                break;
            case VideoModeType.VideoLR3D:
                playerView.setPlayMode(GLPanoView.PLAY_TYPE_3D_LR);
                break;
        }

    }

    /**
     * 设置播放模式
     * @param sence
     */
    public void setSenceMode(int sence){
        //case数值是随接口定义匹配的
        if(playerView==null)
            return;
        switch (sence){
            case VideoModeType.Mode_Sphere360://360
                playerView.setSceneType(GLPanoView.SCENE_TYPE_SPHERE);
                break;
            case VideoModeType.Mode_Sphere180://180
                playerView.setSceneType(GLPanoView.SCENE_TYPE_HALF_SPHERE);
                break;
            case VideoModeType.Mode_Box://立方体
                playerView.setSceneType(GLPanoView.SCENE_TYPE_SKYBOX);
                break;
            case VideoModeType.Mode_Rect:  //平面 (切换非全景播放)
                break;

        }
    }



    /**
         * 重播
         */
        public void rePlay(){
            if(playerView!=null){
                playerView.rePlay();
            }
        }


        @Override
        public void startPlay() {
            if(playerView!=null){
                playerView.startPlay();
            }
        }

        @Override
        public void pausePlay(){
            if(playerView!=null) {
                playerView.pausePlay();
            }
        }
    @Override
    public void hideLoading(){
        if(playUINotifyBusiness!=null){
            playUINotifyBusiness.hideLoading();
        }
    }

    @Override
    public  void showLoading(){
        if(playUINotifyBusiness!=null){
            playUINotifyBusiness.showLoading();
        }
    }
        @Override
        public void updateProgress() {
            if (playerView == null ) {
                return;
            }
            if(MediaHelp.mPlayer==null)
                return;
            /**java.lang.NullPointerException  添加 try-catch 20151019 whf*/
            try{

                int duration = playerView.getDuration();
			/*添加该判断是因为有戏视频流获取不到duration*/
                if(duration<=0 ){
                    duration =file_duration;
                }
                final int current = playerView.mPrepareSeekTo > -1 ? playerView.mPrepareSeekTo : playerView
                        .getCurrentPosition();
                if (current < 0) {
                    return;
                }
                if(lastPlaytime>0&&lastPlaytime==current){
                    return;
                }
                if(playUINotifyBusiness!=null){
                    playUINotifyBusiness.updateProgress(current,duration);
                }
                lastPlaytime = current;

            }catch(Exception e){
                e.printStackTrace();
            }
            //更新字幕
            updateUIForSubTitle();
        }



        /**
         * 播放完成后回调了该方法（在playerView中），播放完后自动播放下集或者重新播放
         */
        @Override
        public void onPlayComplete(){
            super.onPlayComplete();

                if(playerView!=null) {
                    playerView.setPlayCompletion(true);
                    playerView.mCurrentState = BasePlayerView.PlayerState.COMPLETE;
                }
//                rePlay();
//            reportVV("1", "0", "0"  );
            index++;
            if(MjVrPlayerActivity.videoList!=null&&MjVrPlayerActivity.videoList.size()>index) {
                LocalVideoBean videoBean = MjVrPlayerActivity.videoList.get(index);
                onSelectedLocalMovie(videoBean,index);
            }else {
                if(playUINotifyBusiness!=null){
                    playUINotifyBusiness.showReplayDialog(null,0);
                }
            }

        }

    @Override
    public void reportVV(String type, String utime, String ltime) {
        super.reportVV(type,utime,ltime);
        HashMap<String, String> hs = new HashMap<String, String>();
        hs.put("etype", "vv");
        hs.put("tpos", "0");
        if(type!=null) {
            hs.put("vvtype", type);
        }
        hs.put("pagetype", pageType);
        hs.put("local_menu_id", local_menu_id);
        hs.put("roundid",ReportRoundID );
        if(utime!=null) {
            hs.put("utime3", utime);
        }
        if(ltime!=null) {
            hs.put("ltime", ltime + "");
        }
        hs.put("title", titleName );
        hs.put("local_format", suffix);
        hs.put("filetime",file_duration+"");
        hs.put("filepath",mVideoPath);
        if(!TextUtils.isEmpty(fileMd5)) {
            hs.put("md5", fileMd5);
        }
        int vrType = getReportVrType();
        hs.put("viewtype",vrType+"");
        hs.put("joystick", StickUtil.isConnected()?"1":"0");
        hs.put("vvplaysource",report_vvplaysorce+"");
        hs.put("isfirst",report_isFirst+"");
        if("7".equals(type)){ //退出播放时 上报视频播放配置
            hs.put("vrsetting","{begin:"+rePortBeginVrType+",end:"+vrType+"}");
            hs.put("is_loading",report_isloading+"");
            hs.put("conntime",report_conntime+"");
            hs.put("360time",report_360time+"");
        }

        ReportBusiness.getInstance().reportVV(hs);
    }

    private void reportMd5(String md5){
        HashMap<String, String> hs = new HashMap<String, String>();
        hs.put("etype", "vv");
        hs.put("tpos", "0");
        hs.put("pagetype", pageType);
        hs.put("local_menu_id", local_menu_id);
        hs.put("roundid",ReportRoundID );

        hs.put("title", titleName );
        hs.put("local_format", suffix);
        hs.put("filetime",file_duration+"");
        hs.put("filepath",mVideoPath);
        if(md5!=null) {
            hs.put("md5", md5);
        }
        hs.put("viewtype",getReportVrType()+"");
        hs.put("joystick", StickUtil.isConnected()?"1":"0");
        ReportBusiness.getInstance().reportVV(hs);
    }

    boolean isInitSubtitle = false;
    @Override
    public void onPrepared(IGLPlayer player) {
        super.onPrepared(player);
        isInitSubtitle = false;
        file_duration = player.getDuration();
        if(playUINotifyBusiness!=null){
            playUINotifyBusiness.setDisplayDuration(file_duration);
        }
        initAudioStrack();
    }

    @Override
    public boolean onInfo(IGLPlayer player, int what, Object extra) {
        switch (what) {
            case IBfPlayerConstant.IOnInfoType.INFO_SUBTITLE: //加载字幕
                if (playerView!=null&&playerView.mPrepareSeekTo >= 0) {
                    player.seekTo(playerView.mPrepareSeekTo);
                    playerView.mPrepareSeekTo = -1;
                }
                initSubTitlePlug(extra);
                break;
            default:
                break;
        }
        return super.onInfo(player, what, extra);
    }

    //初始化音轨
    private void initAudioStrack(){
        if(playerView==null)
            return;

        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(playUINotifyBusiness!=null){
                    playUINotifyBusiness.setAudioStreamList(playerView.getAudioStreamInfos());
                }
                playerView.setAudioStream(mAudioTrack);
            }
        });
    }
    /**
     * 初始化外挂字幕， 加载字幕列表
     * @param extra 播放器附加信息
     * @return 无
     */
    private void initSubTitlePlug(final Object extra) {
        if(isInitSubtitle==true)
            return;
        isInitSubtitle = true;
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                subtitlesList.clear();
//                GLSubTitleUtils.addSubTitleNo(mActivity, subtitlesList);
                if (!extra.equals(BaseSurfacePlayer.noSubInfo)) {
                    GLSubTitleUtils.parseSubtitle(mActivity, extra, subtitlesList);
                }

                innerSubCount = subtitlesList.size();
                playerCoreh = new PlayCorehUtil(mActivity);
                GLSubTitleUtils.addSubtitlePlug(mActivity, subtitlesList, mVideoPath);
                if (playUINotifyBusiness != null && subtitlesList != null && subtitlesList.size() > 0) {
                    final ArrayList<String> list = new ArrayList<String>();
                    for (String subtitle : subtitlesList) {
                        list.add(subtitle.substring(subtitle.lastIndexOf("/") + 1,
                                subtitle.length()));
                    }
                    playUINotifyBusiness.setSubtitleList(list);
                } else {
                    subtitlesList.clear();
                    if (playUINotifyBusiness != null) {
                        playUINotifyBusiness.setSubtitleList(null);
                    }
                }
                if( subtitlesList!=null&&subtitlesList.contains(subtitleName)){
                    int index = subtitlesList.indexOf(subtitleName);
                    setSubtitleIndex(index);
                }else {
                    //设置字幕 默认第一个
                    setSubtitleIndex(0);
                }


            }

        });
    }

    /**
     *选择字幕
     * @param index 字幕索引
     * @return 无
     */
    public void setSubtitleIndex(final int index) {
        if(playerView==null||subtitlesList==null||subtitlesList.size()<=0){
            return;
        }
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                {
                    playerView.disableSub();
                    if (index < innerSubCount) {
                        playerView.setSubTitleIndex(index);
                        subType = 1;
                    } else {
                        playerCoreh.OpenPlugSub(subtitlesList.get(index));
                        subType = 2;
                    }
                    subtitleName = subtitlesList.get(index);
                    chooseSub = index;
                }
            }
        });

        showSubTitle("");
    }
    /**
     * 显示字幕 timer控制每1秒刷新一次
     * @param text 字幕
     * @return 无
     */
    public void showSubTitle(final String text) {
        MJGLUtils.exeGLQueueEvent( mActivity, new Runnable() {
            @Override
            public void run() {
                if(playUINotifyBusiness!=null){
                    playUINotifyBusiness.showSubTitle(text);
                }
            }
        });
    }

    /**
     * @description:{更新字幕
     */
    public void updateUIForSubTitle() {
        if (playerView == null ) {
            return;
        }
        if(subtitlesList==null||subtitlesList.size()<=0)
            return;
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (subType == 2) {
                    showText();
                } else {
                    showInnerText();
                }
            }
        });



    }
    /**获取外挂字幕内容*/
    private void showText() {
        if(playerView==null||playerCoreh==null) {
            return;
        }

        showSubTitle(playerCoreh.readSub(playerView.getCurrentPosition()));
    }
    /**获取内嵌字幕内容*/
    private void showInnerText() {
        if(playerView==null)
            return;
        String string = playerView.readSubInfo();
        showSubTitle(string);
    }

    @Override
    public void onSelectedSubtitle(final int index, final String subtitleName) { //字幕切换回调
        super.onSelectedSubtitle(index,subtitleName);
        new Thread(new Runnable() {
            @Override
            public void run() {
                setSubtitleIndex(index);
            }
        }).start();
    }

    @Override
    public void onHideSubtitleView(boolean isHide) {
    }

    /**
     * 本地3d模式回调
     * @param modeltype
     */
    @Override
    public void onLeftRightModeChange(int modeltype) {
        super.onLeftRightModeChange(modeltype);
        changeLeftRightMode(modeltype);
    }
    private void changeLeftRightMode(int modelType){
        if(video3DType==modelType)
            return;
        video3DType = modelType;
        set3DMode(modelType);
    }

    /**
     * 本地播放模式回调
     * @param modeltype
     */
    @Override
    public void onRoundModeChange(int modeltype) {
        super.onRoundModeChange(modeltype);
        changeRoundMode(modeltype);
    }
    private void changeRoundMode(int modeltype){
        if(videoModelType==modeltype)
            return;
        videoModelType = modeltype;
        boolean result ;
        if(modeltype==VideoModeType.Mode_Rect){
            result = changePlayPage(GLConst.LOCAL_MOVIE);
        }else {
            result = changePlayPage(GLConst.LOCAL_PANO);
        }
        if(result) {
            return;
        }
        setSenceMode(modeltype);
    }

    @Override
    public void onChangeToSoft() {
        super.onChangeToSoft();
        if(playUINotifyBusiness!=null&&decodeType!=VideoModeType.PLAYER_AUTO){
            String decodeName = decodeType==VideoModeType.PLAYER_SYS?"硬解":decodeType==VideoModeType.PLAYER_SYSPLUS?"硬解+":"软解";
            playUINotifyBusiness.showToast(decodeName+"解码失败，已为您切换到自动模式", GLTextToast.LONG);
            decodeType =VideoModeType.PLAYER_AUTO;
            playUINotifyBusiness.setSelectedDecodeType(decodeType);
        }
    }

    /**
     * 软硬解切换
     * @param type
     */
    @Override
    public void onChangeDecodeType(int type){
        super.onChangeDecodeType(type);
        if(playerView==null||decodeType==type)
            return;
        playerView.mPrepareSeekTo = playerView.getCurrentPosition();
        if (playerCoreh != null)
            playerCoreh.SubContextDone();
        if(type==VideoModeType.PLAYER_AUTO){
            playerView.setDecodeType(IBfPlayerConstant.IBasePlayerType.TYPE_SYSPLUS);
        }else {
            playerView.setDecodeType(type);
        }
        decodeType = type;
        playerView.reSetPlay();

    }


    public void initControlUI(){
        if(playUINotifyBusiness!=null) {
            playUINotifyBusiness.setSelectedRatio(mRatioType);
            playUINotifyBusiness.setSelectedSubtitle(subtitleName);
            playUINotifyBusiness.setSelectedAudioStream(mAudioTrack);
            playUINotifyBusiness.setSelectedLeftRightMode(video3DType);
            playUINotifyBusiness.setSelectedRoundMode(videoModelType);
            playUINotifyBusiness.setSelectedDecodeType(decodeType);
        }
    }

    /**
     * 全景非全景切换
     */
    public boolean changePlayPage(int pageType) {
        if(mPageType==pageType)
            return false;
       final GLExtraData playExtraData = new GLExtraData();
        if(pageType==GLConst.LOCAL_PANO){
            // TODO: 2017/5/16  切换全景
            playExtraData.putExtraString("videoPath",mVideoPath);
            playExtraData.putExtraString("videoName",titleName);
            playExtraData.putExtraInt("videoType",videoModelType);
            playExtraData.putExtraInt("video3DType",video3DType);
            playExtraData.putExtraInt("play_mode",mCurPlayMode);
            playExtraData.putExtraString("pageType",this.pageType);
            playExtraData.putExtraInt("vvplaysource",this.report_vvplaysorce);
            playExtraData.putExtraInt("index",index);
            playExtraData.putExtraString("local_menu_id",this.local_menu_id);
            (mActivity).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                   finish();
                    mActivity.getRootView().queueEvent(new Runnable() {
                        @Override
                        public void run() {
                            startPage(new LocalPanoPlayPage(mActivity), playExtraData);
                        }
                    });


                }
            });

        }else if(pageType==GLConst.LOCAL_MOVIE) {
            // TODO: 2017/5/16 切换影院
            playExtraData.putExtraString("videoPath",mVideoPath);
            playExtraData.putExtraString("videoName",titleName);
            playExtraData.putExtraInt("videoType",videoModelType);
            playExtraData.putExtraInt("video3DType",video3DType);
            playExtraData.putExtraInt("play_mode",mCurPlayMode);
            playExtraData.putExtraString("pageType",this.pageType);
            playExtraData.putExtraInt("vvplaysource",this.report_vvplaysorce);
            playExtraData.putExtraInt("index",index);
            playExtraData.putExtraString("local_menu_id",this.local_menu_id);
            (mActivity).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    finish();
                    mActivity.getRootView().queueEvent(new Runnable() {
                        @Override
                        public void run() {
                            startPage(new LocalMoviePlayPage(mActivity), playExtraData);
                        }
                    });

                }
            });
        }
        return true;
    }

    /**
     * 设置音轨选项
     * @param index 索引
     */
    @Override
    public void onAudioTrackChange(int index) {
        super.onAudioTrackChange(index);
        mAudioTrack = index;
        if(playerView!=null){
            playerView.setAudioStream(index);
        }
    }

    /**
     * 切换选片
     * @param currentNum
     */
    @Override
    public void onSelectedLocalMovie(final LocalVideoBean videoBean, final int currentNum) {
        super.onSelectedLocalMovie(videoBean, currentNum);
        if(videoBean==null)
            return;
        VideoTypeUtil.getVideoType(videoBean.path, new VideoTypeUtil.VideoTypeCallback() {
            @Override
            public void result(final int videoType) {
                (mActivity).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                        mActivity.getRootView().queueEvent(new Runnable() {
                            @Override
                            public void run() {
                                ((MjVrPlayerActivity)mActivity).changeLocalPage(String.valueOf(videoType),videoBean.path,videoBean.name,currentNum,mCurPlayMode,pageType);
                            }
                        });


                    }
                });

            }
        });
    }

    @Override
    public void onReplayBtnClick(boolean isRePlay) {
        super.onReplayBtnClick(isRePlay);
        if(isRePlay){
            rePlay();
            reportVV("1", "0", "0"  );

        }else {
            mActivity.finish();
        }
    }

    @Override
    public void reSetMode() {
        super.reSetMode();
        VideoTypeUtil.getVideoType(mVideoPath, new VideoTypeUtil.VideoTypeCallback() {
            @Override
            public void result(final int videoType) {
                MJGLUtils.exeGLQueueEvent(mActivity, new Runnable() {
                    @Override
                    public void run() {
                        HistoryBusiness.VideoViewparam videoViewparam =  HistoryBusiness.JudgeVideoType(videoType);
                        changeLeftRightMode(videoViewparam.mVideo3DType);
                        changeRoundMode(videoViewparam._videoModelType);
                        if(playUINotifyBusiness!=null){
                            playUINotifyBusiness.setSelectedLeftRightMode(videoViewparam.mVideo3DType);
                            playUINotifyBusiness.setSelectedRoundMode(videoViewparam._videoModelType);
                        }
                    }
                });

            }
        });
    }

    public void reportClickBuy(){
        HashMap<String,String> params = new HashMap<>();
        params.put("etype","click");
        params.put("clicktype","switch");
        params.put("clickitem","buyvr");
        ReportBusiness.getInstance().reportClick(params);
    }

    public int getReportVrType(){
        switch (videoModelType){
            case VideoModeType.Mode_Rect:
                switch (video3DType){
                    case VideoModeType.Video2D:
                        return 1;
                    case VideoModeType.VideoLR3D:
                        return 2;
                    case VideoModeType.VideoUD3D:
                        return 3;
                }
                break;
            case VideoModeType.Mode_Sphere180:
                switch (video3DType){
                    case VideoModeType.Video2D:
                        return 9;
                    case VideoModeType.VideoLR3D:
                        return 7;
                    case VideoModeType.VideoUD3D:
                        return 10;
                }
                break;
            case VideoModeType.Mode_Sphere360:
                switch (video3DType){
                    case VideoModeType.Video2D:
                        return 4;
                    case VideoModeType.VideoLR3D:
                        return 5;
                    case VideoModeType.VideoUD3D:
                        return 6;
                }
                break;
            case VideoModeType.Mode_Box:
                switch (video3DType){
                    case VideoModeType.Video2D:
                        return 8;
                    case VideoModeType.VideoLR3D:
                        return 11;
                    case VideoModeType.VideoUD3D:
                        return 12;
                }
                break;

        }

        return 1;

    }
}
