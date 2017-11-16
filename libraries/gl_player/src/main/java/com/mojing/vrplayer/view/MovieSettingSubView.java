package com.mojing.vrplayer.view;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.text.TextUtils;

import com.bfmj.viewcore.interfaces.GLOnKeyListener;
import com.bfmj.viewcore.interfaces.GLViewFocusListener;
import com.bfmj.viewcore.render.GLConstant;
import com.bfmj.viewcore.view.GLImageView;
import com.bfmj.viewcore.view.GLLinearView;
import com.bfmj.viewcore.view.GLRectView;
import com.bfmj.viewcore.view.GLRelativeView;
import com.bfmj.viewcore.view.GLTextView;
import com.mojing.vrplayer.R;
import com.mojing.vrplayer.bean.AudioSteamInfo;
import com.mojing.vrplayer.bean.VideoModeType;
import com.mojing.vrplayer.interfaces.IPlayerSettingCallBack;
import com.mojing.vrplayer.simpleview.PlaySettingsView;
import com.mojing.vrplayer.utils.BitmapUtil;
import com.mojing.vrplayer.utils.GLConst;
import com.mojing.vrplayer.utils.HeadControlUtil;
import com.storm.smart.common.utils.LogHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yushaochen on 2017/4/7.
 */

public class MovieSettingSubView extends GLLinearView{
    static final String TAG="MovieSettingView";
    private Context mContext;
    ScreenLightSeekBar moveLightSeekBar;
    ScreenLightSeekBar moveSizeSeekBar;
    private IPlayerSettingCallBack mCallBack;

    AudioTrackView audioTrackView;
    SubTitleView subtitlesView;

    private int mType=0;
    HorizontalTextItem mDecodeView;
    HorizontalTextItem mScreenSizeView;

    private GLLinearView glLinearView1;
    private SettingBottomRightView settingBottomRightView;

    public MovieSettingSubView(Context context,int type) {
        super(context);
        mContext = context;
        mType=type;
        int height=0;

        ScreenLightView screenLightView=createScreenLightView("屏幕亮度");//light
        moveLightSeekBar=screenLightView.soundSeekBar;
        addView(screenLightView);
        this.setFocusListener(showSettingfocusListener);

        if(type== GLConst.MOVIE){
//            mScreenSizeView=createScrrenSizeView();
//            addView(mScreenSizeView);//screen size
            ScreenLightView tempScreenLightView=createScreenLightView("屏幕大小");//light
            moveSizeSeekBar=tempScreenLightView.soundSeekBar;
            moveSizeSeekBar.setSeekCallback(mScreenSizeSeekCallback);
            addView(tempScreenLightView);
            createScene();
            height=330;
        }else if(type==GLConst.LOCAL_MOVIE){
            mDecodeView=createDecodeView();
//            mScreenSizeView=createScrrenSizeView();
//            addView(mScreenSizeView);//screen size
            ScreenLightView tempScreenLightView=createScreenLightView("屏幕大小");//light
            moveSizeSeekBar=tempScreenLightView.soundSeekBar;
            moveSizeSeekBar.setSeekCallback(mScreenSizeSeekCallback);
            addView(tempScreenLightView);
            addView(mDecodeView);//decode
            subtitlesView=createSubtitlesView();
            addView(subtitlesView);//字幕
            audioTrackView=createAudioTrackView();//audio
            addView(audioTrackView);
            createScene();
            height=600-115;
        }else if(type==GLConst.PANO){
            height=130;
        }else if(type==GLConst.LOCAL_PANO){
            mDecodeView=createDecodeView();
            addView(mDecodeView);//decode
            subtitlesView=createSubtitlesView();
            addView(subtitlesView);//字幕
            audioTrackView=createAudioTrackView();//audio
            addView(audioTrackView);
            height=400;
        }
        setOrientation(GLConstant.GLOrientation.VERTICAL);
        setLayoutParams(1000,height);
        setMargin(0,0,0,0);
        Bitmap bitmap3 = BitmapUtil.getBitmap(1000, height, 20f,"#272729");
        setBackground(bitmap3);
    }
    SubViewCallback mSubViewCallback=null;
    interface SubViewCallback{
        public void showScene();
    }
    public void setSubViewCallback(SubViewCallback subViewCallback){
        mSubViewCallback=subViewCallback;
    }

    public void createScene(){
        glLinearView1 = new GLLinearView(mContext);
        glLinearView1.setLayoutParams(1000,150);
//        Bitmap bitmap = BitmapUtil.getBitmap(1000, 150, 20f, "#272729");
//        glLinearView1.setBackground(bitmap);
//        glLinearView1.setPadding(55f,35f,55f,35f);
        glLinearView1.setOrientation(GLConstant.GLOrientation.VERTICAL);
        settingBottomRightView = new SettingBottomRightView(mContext);
        settingBottomRightView.setMargin(55,30,0,0);
        settingBottomRightView.setOnKeyListener(new GLOnKeyListener() {
            @Override
            public boolean onKeyDown(GLRectView view, int keycode) {
                if(mSubViewCallback!=null){
                    mSubViewCallback.showScene();
                }
                return false;
            }

            @Override
            public boolean onKeyUp(GLRectView view, int keycode) {
                return false;
            }

            @Override
            public boolean onKeyLongPress(GLRectView view, int keycode) {
                return false;
            }
        });
        settingBottomRightView.setType(SettingBottomRightView.TYPE_SETTING);
        HeadControlUtil.bindView(settingBottomRightView);
        glLinearView1.addView(settingBottomRightView);
      //  addView(glLinearView1);
    }


    public ScreenLightView createScreenLightView(String name){
        return new ScreenLightView(mContext,name);
    }

    ScreenLightSeekBar.SeekCallback mScreenSizeSeekCallback=new ScreenLightSeekBar.SeekCallback(){
        @Override
        public void onProgressChanged(int size, ScreenLightSeekBar screenLightSeekBar) {
            int id=getScreenSizeByProgress(size);
            if(mCallBack!=null){
                mCallBack.onScreenSizeChange(id);
            }
            screenLightSeekBar.setProcess(getProgressbyScreenSize(id));
        }
    };

    private int getScreenSizeByProgress(int progress){
        int id;
        if(12>=progress){
           // progress=0;
            id=VideoModeType.Small_Screen;
        }else if(37<progress&&progress<=62){
          //  progress=50;
            id=VideoModeType.Large_Screen;
        }else if(62<progress&&progress<=87){
           // progress=75;
            id=VideoModeType.Huge_Screen;
        }else if(87<progress){
           // progress=100;
            id=VideoModeType.IMAX_Screen;
        }else{
          //  progress=25;
            id=VideoModeType.Normal_Screen;
        }
        return id;
    }
    private int getProgressbyScreenSize(int type){
        int progress=0;
        if(type==VideoModeType.Small_Screen){
            progress=0;
        }else if(type==VideoModeType.Normal_Screen){
            progress=25;
        }else if(type==VideoModeType.Large_Screen){
            progress=50;
        }else if(type==VideoModeType.Huge_Screen){
            progress=75;
        }else if(type==VideoModeType.IMAX_Screen){
            progress=100;
        }
        return  progress;
    }

    public HorizontalTextItem createScrrenSizeView(){
        ArrayList<ItemData> mItem=new ArrayList<ItemData>();
        ItemData itemData=new ItemData();
        itemData.type= ItemData.TYPE_SCREEN_SIZE;
        itemData.name="小";
        itemData.id= VideoModeType.Small_Screen;
        mItem.add(itemData);

        itemData=new ItemData();
        itemData.type= ItemData.TYPE_SCREEN_SIZE;
        itemData.name="标准";
        itemData.id= VideoModeType.Normal_Screen;
        mItem.add(itemData);

        itemData=new ItemData();
        itemData.type= ItemData.TYPE_SCREEN_SIZE;
        itemData.name="大";
        itemData.id= VideoModeType.Large_Screen;
        mItem.add(itemData);

        itemData=new ItemData();
        itemData.type= ItemData.TYPE_SCREEN_SIZE;
        itemData.name="巨屏";
        itemData.id= VideoModeType.Huge_Screen;
        mItem.add(itemData);

        itemData=new ItemData();
        itemData.type= ItemData.TYPE_SCREEN_SIZE;
        itemData.name="IMAX";
        itemData.id= VideoModeType.IMAX_Screen;
        mItem.add(itemData);

        HorizontalTextItem horizontalTextItem=new HorizontalTextItem(mContext,"屏幕大小",mItem);
        return horizontalTextItem;
    }

    public SubTitleView createSubtitlesView(){
        return new SubTitleView(mContext,"字幕选择", AudioTrackView.TYPE_SUBTITLES);
    }

    public AudioTrackView createAudioTrackView(){
        return new AudioTrackView(mContext,"音轨切换", AudioTrackView.TYPE_AUDIOTRACK);
    }

    class ScreenLightView extends GLLinearView{
        String mName;
        Context mContext;
        public ScreenLightSeekBar soundSeekBar;
        public ScreenLightView(Context context,String name) {
            super(context);
            setOrientation(GLConstant.GLOrientation.HORIZONTAL);
            mName=name;
            this.setLayoutParams(1000,60);
            this.setMargin(55,35,0,0);
            mContext=context;
            initView();
        }
        private void initView(){
            GLTextView glTextView=new GLTextView(mContext);
            glTextView.setText(mName+":");
            glTextView.setTextSize(28);
            glTextView.setMargin(0,0,10,0);
            glTextView.setLayoutParams(140,60);
            glTextView.setAlignment(GLTextView.ALIGN_CENTER);
            glTextView.setPadding(0,14,0,0);
            glTextView.setTextColor(0xff888888);
            addView(glTextView);

            GLRelativeView glRelativeView = new GLRelativeView(mContext);
            glRelativeView.setLayoutParams(740,60);
            glRelativeView.setBackground("play_bg_control_normal_740_60");

            soundSeekBar = new ScreenLightSeekBar(mContext);
            soundSeekBar.setBackground("play_volume_slider_bg");
            soundSeekBar.setBarImage("play_volume_cursor_normal");
            soundSeekBar.setDuration(100);
            soundSeekBar.setMargin(30f,20f,30f,0);
            soundSeekBar.setProcess(0);
            HeadControlUtil.bindView(soundSeekBar);

            glRelativeView.addView(soundSeekBar);
            addView(glRelativeView);
        }
    }

    public HorizontalTextItem createDecodeView(){
        ArrayList<ItemData> mItem=new ArrayList<ItemData>();
        ItemData itemData=new ItemData();
        itemData.type= ItemData.TYPE_DECODE;
        itemData.name="自动";
        itemData.id= VideoModeType.PLAYER_AUTO;
        mItem.add(itemData);

        itemData=new ItemData();
        itemData.type= ItemData.TYPE_DECODE;
        itemData.name="硬解";
        itemData.id= VideoModeType.PLAYER_SYS;
        mItem.add(itemData);

        itemData=new ItemData();
        itemData.type= ItemData.TYPE_DECODE;
        itemData.name="硬解+";
        itemData.id= VideoModeType.PLAYER_SYSPLUS;
        mItem.add(itemData);

        itemData=new ItemData();
        itemData.type= ItemData.TYPE_DECODE;
        itemData.name="软解";
        itemData.id= VideoModeType.PLAYER_SOFT;
        mItem.add(itemData);

        HorizontalTextItem horizontalTextItem=new HorizontalTextItem(mContext,"解码方式",mItem);
        return horizontalTextItem;
    }

    public static final String settingCallback="settingCallback";

    public void setIPlayerSettingCallBack(IPlayerSettingCallBack callBack) {
        this.mCallBack = callBack;
        if(moveLightSeekBar!=null){
            moveLightSeekBar.setIPlayerSettingCallBack(mCallBack);
        }
        LogHelper.d(settingCallback,"setIPlayerSettingCallBack");
        setSubtitlesTextSize();
    }

    public void setSubtitlesTextSize(){
        if(subtitlesView!=null){
            int size=getSubtitleSize(mContext);
            LogHelper.d(settingCallback,"setIPlayerSettingCallBack size:"+size);
            subtitlesView.setSelected(subtitlesView.getIndexByID(size));
            if(mCallBack!=null){
                mCallBack.onScreenSubtitleFontSize(size);
            }
        }
    }

    public void setSelectedSubtitleFontSize(int size){
        LogHelper.d(MovieSettingSubView.settingCallback,"lay subview : "+size);
        if(subtitlesView!=null) {
            LogHelper.d(MovieSettingSubView.settingCallback,"lay subview   ,subtitlesView!=null: "+size);
            subtitlesView.setSelected(subtitlesView.getIndexByID(size));
        }
        saveSubtitleSize(mContext,size);
    }

    private GLViewFocusListener showSettingfocusListener = new GLViewFocusListener() {
        @Override
        public void onFocusChange(GLRectView view, boolean focused) {
                if(focused) {
//                    ((GLBaseActivity)getContext()).showCursorView();
                    if(null != mCallBack) {
                        mCallBack.onHideControlAndSettingView(false);
                    }
                } else {
//                    ((GLBaseActivity)getContext()).hideCursorView2();
                    if(null != mCallBack) {
                        mCallBack.onHideControlAndSettingView(true);
                    }
                }
        }
    };

    private GLViewFocusListener focusListener = new GLViewFocusListener() {
        @Override
        public void onFocusChange(GLRectView view, boolean focused) {
            if(view instanceof MULTIView){
                MULTIView imageTextView=(MULTIView)view;
                if(imageTextView.mType== MULTIView.TYPE_TEXT){
                    if(imageTextView.mState!= MULTIView.STATE_SELECTED){
                        if(focused){
                            imageTextView.setState(MULTIView.STATE_HOVER);
                        }else{
                            imageTextView.setState(MULTIView.STATE_NORMAL);
                        }
                    }
                }else{
                    if(focused){
                        imageTextView.setState(MULTIView.STATE_HOVER);
                    }else{
                        imageTextView.setState(MULTIView.STATE_NORMAL);
                    }
                }
            }

        }
    };

    GLOnKeyListener mGLOnKeyListener= new GLOnKeyListener() {
        @Override
        public boolean onKeyDown(GLRectView view, int keycode) {
            if(view instanceof  MULTIView){
                MULTIView imageTextView=(MULTIView)view;
                ItemData itemData=imageTextView.mData;
                if(itemData.type== ItemData.TYPE_SCREEN_SIZE){
//                    mScreenSizeView.setSelected(mScreenSizeView.getIndexByID(itemData.id));
//                    if(mCallBack!=null){
//                        mCallBack.onScreenSizeChange(itemData.id);
//                    }
                }else if(itemData.type== ItemData.TYPE_DECODE){
                    mDecodeView.setSelected(mDecodeView.getIndexByID(itemData.id));
                    if(mCallBack!=null){
                        mCallBack.onChangeDecodeType(itemData.id);
                    }
                }else if(itemData.type== ItemData.TYPE_AUDIO_TRACK){
                    if(mCallBack!=null){
                        LogHelper.d(TAG,"TYPE_AUDIO_TRACK current id:"+imageTextView.mData.id);
                        audioTrackView.setNextIndex();
                        LogHelper.d(TAG,"TYPE_AUDIO_TRACK next id:"+imageTextView.mData.id);
                        mCallBack.onAudioTrackChange(imageTextView.mData.id);
                    }
                }else if(itemData.type== ItemData.TYPE_SCREEN_SUBTITLES){
                    if(mCallBack!=null){
                        LogHelper.d(TAG,"TYPE_SCREEN_SUBTITLES current id:"+imageTextView.mData.id);
                        subtitlesView.setNextIndex();
                        LogHelper.d(TAG,"TYPE_SCREEN_SUBTITLES next id:"+imageTextView.mData.id);
                        mCallBack.onSelectedSubtitle(imageTextView.mData.id,imageTextView.mData.name2);
                    }
                }else if(itemData.type== ItemData.TYPE_SCREEN_CAPTION_SIZE){
                    saveSubtitleSize(mContext,itemData.id);
                    subtitlesView.setSelected(subtitlesView.getIndexByID(itemData.id));
                    if(mCallBack!=null){
                        mCallBack.onScreenSubtitleFontSize(imageTextView.mData.id);
                    }
                }
            }
            return false;
        }
        @Override
        public boolean onKeyUp(GLRectView view, int keycode) {
            return false;
        }
        @Override
        public boolean onKeyLongPress(GLRectView view, int keycode) {
            return false;
        }
    };

    public static final int subTitleSize_s=32;
    public static final int subTitleSize_m=40;
    public static final int subTitleSize_l=46;
    class SubTitleView extends AudioTrackView{
        ArrayList<MULTIView> mMULTIViews=new ArrayList<MULTIView>();
        int currentSelected=0;

        public SubTitleView(Context context,String name,int type) {
            super(context, name, type);
            addCaptionSize();
        }

        //字幕用
        public void addCaptionTextView(ArrayList<ItemData> item){
            for(ItemData itemData:item){
                addItem(itemData);
            }
        }

        public void newDataChange(boolean enable){
            for(int i=0;i<mMULTIViews.size();i++){
                mMULTIViews.get(i).enable=enable;
                mMULTIViews.get(i).notifyState();
            }
        }

        public void addItem(ItemData itemData){
            MULTIView imageTextView=new MULTIView(mContext,itemData, MULTIView.TYPE_TEXT);
            imageTextView.setFocusListener(focusListener);
            imageTextView.setOnKeyListener(mGLOnKeyListener);
            HeadControlUtil.bindView(imageTextView);
            imageTextView.setLayoutParams(80,60);
            imageTextView.glTextView.setLayoutParams(80,60);
            if(itemData.id==subTitleSize_s){
                imageTextView.glTextView.setPadding(3,14,0,0);
                imageTextView.glTextView.setTextSize(24);
            }else if(itemData.id==subTitleSize_m){
                imageTextView.glTextView.setPadding(0,9,0,0);
                imageTextView.glTextView.setTextSize(32);
            }else if(itemData.id==subTitleSize_l){
                imageTextView.glTextView.setPadding(0,4,0,0);
                imageTextView.glTextView.setTextSize(40);
            }
            mMULTIViews.add(imageTextView);
            addView(imageTextView);
        }

        public void addCaptionSize(){
            ArrayList<ItemData> mItem=new ArrayList<ItemData>();
            ItemData itemData=new ItemData();
            itemData.type= ItemData.TYPE_SCREEN_CAPTION_SIZE;
            itemData.name="Aa";
            itemData.id= subTitleSize_s;
            mItem.add(itemData);

            itemData=new ItemData();
            itemData.type= ItemData.TYPE_SCREEN_CAPTION_SIZE;
            itemData.name="Aa";
            itemData.id= subTitleSize_m;
            mItem.add(itemData);

            itemData=new ItemData();
            itemData.type= ItemData.TYPE_SCREEN_CAPTION_SIZE;
            itemData.name="Aa";
            itemData.id= subTitleSize_l;
            mItem.add(itemData);
            addCaptionTextView(mItem);
        }

        public void setSelected(int index){
            currentSelected=index;
            for(MULTIView mULTIView:mMULTIViews){
                mULTIView.setState(MULTIView.STATE_NORMAL);
            }
            mMULTIViews.get(index).setState(MULTIView.STATE_SELECTED);
        }

        public int getFontSizeByIndex(int index){
            return mMULTIViews.get(index).mData.id;
        }

        public int getIndexByID(int id){
            for(int i=0;i<mMULTIViews.size();i++){
                if(mMULTIViews.get(i).mData.id==id){
                    return  i;
                }
            }
            return 0;
        }

    }

    // 字幕和音轨共用
    class AudioTrackView extends GLLinearView{
        public final static int TYPE_AUDIOTRACK=1;
        public final static int TYPE_SUBTITLES=2;
        String mName;
        ArrayList<ItemData> mItem;
        Context mContext;
        MULTIView mMULTIView;
        int mType=0;
        public AudioTrackView(Context context,String name,int type) {
            super(context);
            mType=type;
            setOrientation(GLConstant.GLOrientation.HORIZONTAL);
            mName=name;
            mContext=context;
            setLayoutParams(1000,60);
            setMargin(55,30,0,0);
            initView();
        }
        public  void initView(){
            GLTextView glTextView=new GLTextView(mContext);
            glTextView.setText(mName+":");
            glTextView.setTextSize(28);
            glTextView.setLayoutParams(140,60);
            glTextView.setAlignment(GLTextView.ALIGN_CENTER);
            glTextView.setPadding(0,14,0,0);
            glTextView.setTextColor(0xff888888);
            addView(glTextView);
            addDataView();
        }
        public ItemData getEmptyItem(){
            ItemData itemData=new ItemData();
            if(mType==TYPE_AUDIOTRACK){
                itemData.type= ItemData.TYPE_AUDIO_TRACK;
                itemData.name="无可选音轨";
            }else{
                itemData.type= ItemData.TYPE_SCREEN_CAPTION_SIZE;
                itemData.name="无可选字幕";
            }
            itemData.id=-1000;
            return itemData;
        }


        public  void setData(ArrayList<ItemData> item){
            LogHelper.d(TAG,"AudioTrackView setData");
            mItem=item;
            if(mItem.size()==1){
                mMULTIView.setImageDisable();
            }
            setSelectIndex(0);
            if(isEmpty()){
                ItemData itemData=getEmptyItem();
                mMULTIView.setData(itemData);
                newDataChange(false);
            }else{
                newDataChange(true);
            }
        }
        public void newDataChange(boolean enable){

        }

        public boolean isEmpty(){
            if(mItem==null||mItem.size()==0){
                return true;
            }
            return false;
        }

        public void setNextIndex(){
            if(mItem==null||mItem.size()<2){
                return;
            }
            currentIndex++;
            if(currentIndex>=mItem.size()){
                currentIndex=0;
            }
            setSelectIndex(currentIndex);
        }
        int currentIndex;
        public void setSelectIndex(int index){
            if(mItem!=null&&mItem.size()>0&&mItem.size()>index&&index>=0){
                mMULTIView.setFocusListener(focusListener);
                mMULTIView.setOnKeyListener(mGLOnKeyListener);
                HeadControlUtil.bindView(mMULTIView);
                currentIndex=index;
                LogHelper.d(TAG,"AudioTrackView  id:"+mItem.get(index).id);
                mMULTIView.setData(mItem.get(index));
                if(mItem.size()==1){
                    mMULTIView.setImageDisable();
                }
            }else{
                LogHelper.d(TAG,"AudioTrackView mItem is null");
            }
        }

        public int getIndexByName(String name){
            if(mItem==null||TextUtils.isEmpty(name)){
                return 0;
            }else{
                for(ItemData itemData: mItem){
                    if(!TextUtils.isEmpty(itemData.name)){
                        if(itemData.name.equals(name)){
                            return itemData.id;
                        }
                    }
                }
            }
            return 0;
        }

        public void addDataView(){
            ItemData ItemData=getEmptyItem();
            mMULTIView= new MULTIView(mContext,ItemData, MULTIView.TYPE_MULTI_TEXT_IMAGE);
            addView(mMULTIView);
        }

    }

    class HorizontalTextItem extends GLLinearView{
        String mName;
        ArrayList<MULTIView> mMULTIViews;
        ArrayList<ItemData> mItem;
        Context mContext;
        public HorizontalTextItem(Context context,String name,ArrayList<ItemData> item) {
            super(context);
            setOrientation(GLConstant.GLOrientation.HORIZONTAL);
            mName=name;
            mItem=item;
            this.setLayoutParams(1000,60);
            this.setMargin(55,30,0,0);
            mContext=context;
            initView();
        }
        public int size(){
            if(mItem==null){
                return 0;
            }else{
                return mItem.size();
            }
        }

        private void initView(){
            GLTextView glTextView=new GLTextView(mContext);
            glTextView.setText(mName+":");
            glTextView.setTextSize(28);
            glTextView.setLayoutParams(140,60);
            glTextView.setAlignment(GLTextView.ALIGN_CENTER);
            glTextView.setPadding(0,14,0,0);
            glTextView.setTextColor(0xff888888);
            addView(glTextView);
            mMULTIViews=new ArrayList<MULTIView>();
            for(ItemData itemData:mItem){
                addItem(itemData);
            }
        }
        public void addItem(ItemData itemData){
            MULTIView imageTextView=new MULTIView(mContext,itemData, MULTIView.TYPE_TEXT);
            imageTextView.setFocusListener(focusListener);
            imageTextView.setOnKeyListener(mGLOnKeyListener);
            HeadControlUtil.bindView(imageTextView);
            addView(imageTextView);
            mMULTIViews.add(imageTextView);
        }
        int currentSelected=0;
        public void setSelected(int index){
            currentSelected=index;
            for(MULTIView mULTIView:mMULTIViews){
                mULTIView.setState(MULTIView.STATE_NORMAL);
            }
            mMULTIViews.get(index).setState(MULTIView.STATE_SELECTED);
        }

        public int getIndexByID(int id){
            for(int i=0;i<mItem.size();i++){
                if(mItem.get(i).id==id){
                    return  i;
                }
            }
            return 0;
        }
    }

    public static class ItemData{
        public final static int TYPE_SCREEN_LIGHT=1;
        public final static int TYPE_SCREEN_SUBTITLES=2;//字幕
        public final static int TYPE_DECODE=3;
        public final static int TYPE_SCREEN_SIZE=4;
        public final static int TYPE_AUDIO_TRACK=5;
        public final static int TYPE_SCREEN_CAPTION_SIZE=6;//字幕大小
        public String name;
        public int id;
        public int type;
        public String name2;
    }

    class MULTIView extends GLLinearView{
        public final  static int TYPE_TEXT=0;
        public final  static int TYPE_MULTI_TEXT_IMAGE=1;

        public final  static boolean STATE_ENABLED=true;
        public final  static boolean STATE_DISABLED=false;

        public final  static int STATE_SELECTED=2;
        public final  static int STATE_HOVER=1;
        public final  static int STATE_NORMAL=0;

        private int mState=STATE_NORMAL;
        private boolean enable=true;

        private String drawable_disable;
        private String drawable_normal;
        private String drawable_hover;
        private String drawable_selected;
        private String mName="";
        public ItemData mData;
        public int mType=0;

        public GLTextView glTextView;
        GLTextView glTextView2;
        GLImageView glImageView2;

        public MULTIView(Context context,ItemData itemData,int type){
            super(context);
            mData=itemData;
            setOrientation(GLConstant.GLOrientation.HORIZONTAL);
            mType=type;
            if(mType==TYPE_TEXT){
                addTextView();
            }else if(mType==TYPE_MULTI_TEXT_IMAGE){
                addImageView();
            }
            setState(mState);
            setData(itemData);
        }

        public void setData(ItemData itemData){
            mData=itemData;
            if(mType==TYPE_TEXT){
                glTextView.setText(mData.name);
            }else if(mType==TYPE_MULTI_TEXT_IMAGE){
                String showText=mData.name;
                if(!TextUtils.isEmpty(showText)){
                    if(showText.length()>10){
                        showText=showText.substring(0,10)+"...";
                    }
                    glTextView2.setText(showText);
                }
                //glTextView2.setText();
                if(mData.id==-1000){
                    glTextView2.setTextColor(0xff333333);
                    glImageView2.setBackground("play_icon_setting_switch_disable");
                }else{
                    glTextView2.setTextColor(0xff888888);
                    glImageView2.setBackground("play_icon_setting_switch_hover");
                }
            }
        }

        public void addImageView(){
            drawable_disable="play_bg_control_normal_440_60";
            drawable_normal="play_bg_control_normal_440_60";
            drawable_hover="play_bg_control_hover_440_60";
            drawable_selected="play_bg_control_normal_440_60";
            setLayoutParams(440,60);
            setBackground(drawable_normal);
            setMargin(10f,0,0f,0f);
            glTextView2=new GLTextView(mContext);
            glTextView2.setPadding(12,12,0,0);
            glTextView2.setTextSize(28);
            glTextView2.setAlignment(GLTextView.ALIGN_CENTER);
            glTextView2.setTextColor(0xff888888);
            glTextView2.setLayoutParams(340,60);
            glTextView2.setText(mData.name);
            this.addView(glTextView2);

            glImageView2=new GLImageView(mContext);
            glImageView2.setLayoutParams(40,40);
            glImageView2.setBackground("play_icon_setting_switch_hover");
            glImageView2.setMargin(20f,10,0f,0f);
            this.addView(glImageView2);

        }

        public void setImageDisable(){
            if(glImageView2!=null){
                glImageView2.setBackground("play_icon_setting_switch_disable");
            }
            if(glTextView2!=null){
               // glTextView2.setTextColor(0xff333333);
            }
        }

        public void addTextView(){
            drawable_disable="play_bg_control_normal_140_60";
            drawable_normal="play_bg_control_normal_140_60";
            drawable_hover="play_bg_control_hover_140_60";
            drawable_selected="play_bg_control_normal_140_60";
            setBackground(drawable_normal);

            glTextView=new GLTextView(mContext);
            glTextView.setAlignment(GLTextView.ALIGN_CENTER);
            setMargin(10f,0,0f,0f);
            if(mData.type== ItemData.TYPE_SCREEN_CAPTION_SIZE){
            }else{
                glTextView.setPadding(0,12,0,0);
                glTextView.setTextSize(28);
                setLayoutParams(140,60);
                glTextView.setLayoutParams(140,60);
            }
            glTextView.setTextColor(0xff888888);

            glTextView.setText(mData.name);
            this.addView(glTextView);
        }

        public void setName(String name){
            mName=name;
            if(mType==TYPE_TEXT){
                glTextView.setText(mName);
            }else if(mType==TYPE_MULTI_TEXT_IMAGE){
                glTextView2.setText(mName);
            }
        }

        public void setState(int  state){
            mState=state;
            notifyState();
        }
        public void notifyState(){
            if(!enable){
                this.setBackground(drawable_disable);
                glTextView.setTextColor(0xff333333);
            }else{
                if(mState==STATE_NORMAL){
                    this.setBackground(drawable_normal);
                    if(mType==TYPE_TEXT){
                        glTextView.setTextColor(0xff888888);
                    }
                }else if(mState==STATE_HOVER){
                    this.setBackground(drawable_hover);
                }else if(mState==STATE_SELECTED){
                    this.setBackground(drawable_selected);
                    if(mType==TYPE_TEXT){
                        glTextView.setTextColor(0xff008cb3);
                    }
                }
            }
        }
    }

    public void setAudioStreamList(List<AudioSteamInfo.AudioStremItem> audioStreamList) {
        ArrayList<ItemData> mItem=new ArrayList<ItemData>();
        if(audioStreamList==null||audioStreamList.size()==0){
            LogHelper.d(TAG,"audioStreamList is null");
            audioTrackView.setData(mItem);
            audioTrackView.setSelectIndex(0);
            return;
        }

        for(int i=0;audioStreamList.size()>i;i++){
            AudioSteamInfo.AudioStremItem audioItem=audioStreamList.get(i);
            ItemData itemData=new ItemData();
            itemData.type= ItemData.TYPE_AUDIO_TRACK;
            itemData.name= PlaySettingsView.changeAudioStream(audioItem.getHandler_name(),i);
            itemData.name2= audioItem.getHandler_name();
            itemData.id= audioItem.getIndex();
            mItem.add(itemData);
        }
        LogHelper.d(TAG,"audioTrackView.setData(mItem)");
        audioTrackView.setData(mItem);
        LogHelper.d(TAG,"audioTrackView setSelectIndex");
        if(preLastAudioStreamIndex==-1){
            audioTrackView.setSelectIndex(0);
        }else{
            audioTrackView.setSelectIndex(preLastAudioStreamIndex);
        }

    }

    public void setSubtitleList(List<String> subtitleList){
        ArrayList<ItemData> mItem=new ArrayList<ItemData>();
        if(subtitleList==null||subtitleList.size()==0){
            LogHelper.d(TAG,"setSubtitleList is  null");
            subtitlesView.setData(mItem);
            subtitlesView.setSelectIndex(0);
            return;
        }
        for(int i=0;i<subtitleList.size();i++){
            String str=subtitleList.get(i);
            LogHelper.d(TAG,"setSubtitleList is not null,subtitles:"+str+" id :"+i);
            ItemData itemData=new ItemData();
            itemData.type= ItemData.TYPE_SCREEN_SUBTITLES;
            itemData.name=str;
            itemData.id= i;
            mItem.add(itemData);
        }
        subtitlesView.setData(mItem);
        if(TextUtils.isEmpty(preLastSubtitleName)){
            subtitlesView.setSelectIndex(0);
            return ;
        }else{
            subtitlesView.setSelectIndex(subtitlesView.getIndexByName(preLastSubtitleName));
        }
    }
    public void setSelectedAudioStream(int index) {
        preLastAudioStreamIndex=index;
        if(preLastAudioStreamIndex!=-1&&audioTrackView!=null){
            audioTrackView.setSelectIndex(preLastAudioStreamIndex);
        }
    }
    public void setSelectedDecodeType(int type) {
        LogHelper.d(TAG,"setSelectedDecodeType:"+type);
        mDecodeView.setSelected(mDecodeView.getIndexByID(type));
    }
    public void setSelectedScreenSize(int type) {
        LogHelper.d(TAG,"setSelectedScreenSize:"+type);
      //  mScreenSizeView.setSelected(mScreenSizeView.getIndexByID(type));
        if(moveSizeSeekBar!=null){
            moveSizeSeekBar.setProcess(getProgressbyScreenSize(type));
        }
    }

    public void setSelectedScreenLight(int light) {
        moveLightSeekBar.setProcess(light);
    }
    String preLastSubtitleName="";
    int preLastAudioStreamIndex=-1;
    public void setSelectedSubtitle(String name) {
        preLastSubtitleName=name;
        if(subtitlesView!=null&&!TextUtils.isEmpty(preLastSubtitleName)){
            subtitlesView.setSelectIndex(subtitlesView.getIndexByName(preLastSubtitleName));
        }
    }

    public void setType(int type){
        mType=type;
    }
    public static final String movie_subtitle_size_name="movie_subtitle";
    public static final String movie_subtitle_size_key ="movie_subtitle_size_key";
    public static int getSubtitleSize(Context context){
        int size;
        SharedPreferences sp = context.getSharedPreferences(movie_subtitle_size_name, Context.MODE_PRIVATE);
        size=sp.getInt(movie_subtitle_size_key,subTitleSize_m);
        return size;
    }
    public void saveSubtitleSize(Context context,int size){
        SharedPreferences sp = context.getSharedPreferences(movie_subtitle_size_name, Context.MODE_PRIVATE);
        sp.edit().putInt(movie_subtitle_size_key,size ).commit();
    }
}
