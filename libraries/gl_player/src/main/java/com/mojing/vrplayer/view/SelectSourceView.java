package com.mojing.vrplayer.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;

import com.bfmj.viewcore.interfaces.GLViewFocusListener;
import com.bfmj.viewcore.view.GLRectView;
import com.bfmj.viewcore.view.GLRelativeView;
import com.mojing.vrplayer.bean.SelectInfo;
import com.mojing.vrplayer.interfaces.IPlayerSettingCallBack;
import com.mojing.vrplayer.publicc.bean.VideoDetailBean;
import com.mojing.vrplayer.utils.BitmapUtil;
import com.mojing.vrplayer.utils.GLConst;
import com.mojing.vrplayer.utils.ViewUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yushaochen on 2017/4/13.
 */

public class SelectSourceView extends GLRelativeView{

    private Context mContext;

    private SelectEpisodeView selectEpisodeView;
    private SelectVarietyView selectVarietyView;

    private static final int UNIT = 5;//将现有的数据分成5份

    private VideoDetailBean mVieoDetailBean;

    public SelectSourceView(Context context) {
        super(context);

        mContext = context;

        setLayoutParams(1000,440);
//        setBackground(new GLColor(0xff0000));
        //创建选集view
        createEpisodeView();

        //创建综艺类选集
        createVarietyView();

    }

    private void createVarietyView() {
        selectVarietyView = new SelectVarietyView(mContext);
        selectVarietyView.setVisible(false);
        addView(selectVarietyView);
        selectVarietyView.setFocusListener(focusListener);
    }

    private void createEpisodeView() {
        selectEpisodeView = new SelectEpisodeView(mContext);
        selectEpisodeView.setVisible(false);
        addView(selectEpisodeView);
        selectEpisodeView.setFocusListener(focusListener);
    }

    private void test() {

        mVieoDetailBean = new VideoDetailBean();
        mVieoDetailBean.setCategory_type(2);
        mVieoDetailBean.setTotal("19");
    }

    public void prePage() {
        if(categoryType==1||categoryType==4) {//综艺和电影 直接显示标题
            if(selectVarietyView.isVisible()) {
                selectVarietyView.prePage();
            }
        } else {
            if(selectEpisodeView.isVisible()) {
                selectEpisodeView.prePage();
            }
        }
    }

    public void nextPage() {
        if(categoryType==1||categoryType==4) {//综艺和电影 直接显示标题
            if(selectVarietyView.isVisible()) {
                selectVarietyView.nextPage();
            }
        } else {
            if(selectEpisodeView.isVisible()) {
                selectEpisodeView.nextPage();
            }
        }
    }
    private int  categoryType;
    public void setMovieVideoDatas(VideoDetailBean videosBean,int index) {
//        test();
        if(null != videosBean) {
            mVieoDetailBean = videosBean;
            categoryType = mVieoDetailBean.getCategory_type();
            if(categoryType==1||categoryType==4) {//综艺和电影 直接显示标题
                handleVarietyData();
                if(datas.size() > 1) {
                    int width = ViewUtil.getDip(1000, GLConst.Player_Settings_Scale);
                    int height = ViewUtil.getDip(440, GLConst.Player_Settings_Scale);
                    setLayoutParams(width,height);
                    Bitmap bitmap = BitmapUtil.getBitmap(width, height, ViewUtil.getDip(20f,GLConst.Player_Settings_Scale), "#272729");
                    selectVarietyView.setLayoutParams(width,height);
                    selectVarietyView.setBackground(bitmap);
//                    selectVarietyView.scale(GLConst.Player_Settings_Scale);
                } else {
                    int width = ViewUtil.getDip(1000, GLConst.Player_Settings_Scale);
                    int height = ViewUtil.getDip(370,GLConst.Player_Settings_Scale);
                    setLayoutParams(width,height);
                    Bitmap bitmap = BitmapUtil.getBitmap(width, height,ViewUtil.getDip(20f,GLConst.Player_Settings_Scale), "#272729");
                    selectVarietyView.setLayoutParams(width,height);
                    selectVarietyView.setBackground(bitmap);
                    setY(getY()+ViewUtil.getDip(70,GLConst.Player_Settings_Scale));
//                    ViewUtil.reSetViewPos(this,1000,370,GLConst.Player_Settings_Scale);
                }
                selectVarietyView.setVisible(true);
                selectVarietyView.setData(datas);
                selectVarietyView.setCurrentNum(index);
            } else {//创建电视剧选集
                handleEpisodeData();
                if(datas.size() > 1) {
                    int width = ViewUtil.getDip(1000, GLConst.Player_Settings_Scale);
                    int height = ViewUtil.getDip(440,GLConst.Player_Settings_Scale);
                    setLayoutParams(width,height);
                    Bitmap bitmap = BitmapUtil.getBitmap(width, height, ViewUtil.getDip(20f,GLConst.Player_Settings_Scale), "#272729");
                    selectEpisodeView.setLayoutParams(width,height);
                    selectEpisodeView.setBackground(bitmap);
                } else {
                    int width = ViewUtil.getDip(1000, GLConst.Player_Settings_Scale);
                    int height = ViewUtil.getDip(370,GLConst.Player_Settings_Scale);
                    setLayoutParams(width,height);
                    Bitmap bitmap = BitmapUtil.getBitmap(width, height, ViewUtil.getDip(20f,GLConst.Player_Settings_Scale), "#272729");
                    selectEpisodeView.setLayoutParams(width,height);
                    selectEpisodeView.setBackground(bitmap);
                    setY(getY()+ViewUtil.getDip(70,GLConst.Player_Settings_Scale));
                }
                selectEpisodeView.setVisible(true);
                selectEpisodeView.setData(datas);
                selectEpisodeView.setCurrentNum(index);
            }
//            setDepth(GLConst.Player_Settings_Depth);
        }
    }

    public void setCurrentNum(int index) {
        if(null != mVieoDetailBean) {
            int  categoryType = mVieoDetailBean.getCategory_type();
            if(categoryType==1||categoryType==4) {//综艺和电影 直接显示标题
                selectVarietyView.setCurrentNum(index);
            } else {
                selectEpisodeView.setCurrentNum(index);
            }
        }
    }

    private ArrayList<SelectInfo<VideoDetailBean.AlbumsBean.VideosBean>> datas = new ArrayList<>();

    public void handleEpisodeData() {
        if(TextUtils.isEmpty(mVieoDetailBean.getTotal())) return;
        int total = Integer.parseInt(mVieoDetailBean.getTotal());
            if(total > 50) {
                int offset = (total % UNIT == 0) ? total / UNIT
                        : (total / UNIT + 1);
                int start = 0;
                int end = 0;
                for (int i = 0; i < UNIT; i++) {
                    start = (i * offset) + 1;
                    if (i == (UNIT - 1)) {
                        end = total;
                    } else {
                        end = (i + 1) * offset;
                    }
                    SelectInfo selectInfo = new SelectInfo();
                    selectInfo.setNums(start + "-" + end);
                    selectInfo.setTotal(total);
                    datas.add(selectInfo);
                }
            } else {
                SelectInfo selectInfo = new SelectInfo();
                selectInfo.setNums(1 + "-" + total);
                selectInfo.setTotal(total);
                datas.add(selectInfo);
            }

    }

    public void handleVarietyData() {
        VideoDetailBean.AlbumsBean albumsBean = mVieoDetailBean.getAlbums().get(0);
        if(null != albumsBean) {
            List<VideoDetailBean.AlbumsBean.VideosBean> videos = albumsBean.getVideos();
            if(videos.size() > 50) {
                int offset = (videos.size() % UNIT == 0) ? videos.size() / UNIT
                        : (videos.size() / UNIT + 1);
                int start = 0;
                int end = 0;
                for (int i = 0; i < UNIT; i++) {
                    start = (i * offset) + 1;
                    if (i == (UNIT - 1)) {
                        end = videos.size();
                    } else {
                        end = (i + 1) * offset;
                    }
                    SelectInfo selectInfo = new SelectInfo();
                    selectInfo.setNums(start + "-" + end);
                    selectInfo.setTotal(videos.size());
                    fillData(start,end, selectInfo,videos);
                    datas.add(selectInfo);
                }
            } else {
                SelectInfo selectInfo = new SelectInfo();
                selectInfo.setNums(1 + "-" + videos.size());
                selectInfo.setTotal(videos.size());
                fillData(1,videos.size(), selectInfo, videos);
                datas.add(selectInfo);
            }
        }

    }

    public void fillData(int start, int end, SelectInfo ti, List<VideoDetailBean.AlbumsBean.VideosBean> videos) {
        for (int x = start - 1; x < end; x++) {
            ti.getInfos().add(videos.get(x));
        }
    }

    private IPlayerSettingCallBack mCallBack;

    public void setIPlayerSettingCallBack(IPlayerSettingCallBack callBack) {

        this.mCallBack = callBack;
        selectVarietyView.setIPlayerSettingCallBack(mCallBack);
        selectEpisodeView.setIPlayerSettingCallBack(mCallBack);
    }

    private GLViewFocusListener focusListener = new GLViewFocusListener() {
        @Override
        public void onFocusChange(GLRectView view, boolean focused) {
            if(focused) {
//                ((GLBaseActivity)getContext()).showCursorView();
                if(null != mCallBack) {
                    mCallBack.onHideControlAndSettingView(false);
                }
            } else {
//                ((GLBaseActivity)getContext()).hideCursorView2();
                if(null != mCallBack) {
                    mCallBack.onHideControlAndSettingView(true);
                }
            }
        }
    };
}
