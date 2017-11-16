package com.mojing.vrplayer.simpleview;


import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mojing.vrplayer.R;
import com.mojing.vrplayer.bean.AudioSteamInfo;
import com.mojing.vrplayer.bean.VideoModeType;
import com.mojing.vrplayer.interfaces.IPlayerSettingCallBack;
import com.mojing.vrplayer.view.MovieSettingSubView;
import com.storm.smart.common.utils.LogHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yushaochen on 2017/4/7.
 */

public class PlaySettingsView extends LinearLayout implements  View.OnClickListener{
    static final String TAG="PlaySettingsView";
    private Context mContext;
    private Animation right_in_animation;
    private Animation right_out_animation;
    private ImageView closesetting;

    public PlaySettingsView(Context context) {
        super(context);
    }
    public PlaySettingsView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext=context;
        right_in_animation = AnimationUtils.loadAnimation(mContext, R.anim.slide_right_in);
        right_out_animation = AnimationUtils.loadAnimation(mContext, R.anim.slide_right_out);
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.activity_simple_player_settings, null);
        this.addView(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        initView(view);
    }
    TextView decode_aoto;
    TextView decode_sys;
    TextView decode_sysplus;
    TextView decode_soft;
    View subtitle_root;
    TextView subtitle_title;
    TextView subtitle_text;
    ImageView subtitle_image;
    TextView subtitle_larger;
    TextView subtitle_middle;
    TextView subtitle_small;
    TextView track_title;
    View track_root;
    TextView track_text;
    ImageView track_image;

    public void initView(View view){
        decode_aoto=(TextView)view.findViewById(R.id.decode_aoto);
        decode_sys=(TextView)view.findViewById(R.id.decode_sys);
        decode_sysplus=(TextView)view.findViewById(R.id.decode_sysplus);
        decode_soft=(TextView)view.findViewById(R.id.decode_soft);
        decode_aoto.setOnClickListener(this);
        decode_sys.setOnClickListener(this);
        decode_sysplus.setOnClickListener(this);
        decode_soft.setOnClickListener(this);
        decode_aoto.setTag(VideoModeType.PLAYER_AUTO);
        decode_sys.setTag(VideoModeType.PLAYER_SYS);
        decode_sysplus.setTag(VideoModeType.PLAYER_SYSPLUS);
        decode_soft.setTag(VideoModeType.PLAYER_SOFT);

        subtitle_title=(TextView)view.findViewById(R.id.subtitle_title);
        subtitle_root=view.findViewById(R.id.subtitle_root);
        subtitle_image=(ImageView)view.findViewById(R.id.subtitle_image);
        subtitle_text=(TextView)view.findViewById(R.id.subtitle_text);

        subtitle_larger=(TextView)view.findViewById(R.id.subtitle_larger);
        subtitle_middle=(TextView)view.findViewById(R.id.subtitle_middle);
        subtitle_small=(TextView)view.findViewById(R.id.subtitle_small);
        subtitle_root.setOnClickListener(this);
        subtitle_larger.setOnClickListener(this);
        subtitle_middle.setOnClickListener(this);
        subtitle_small.setOnClickListener(this);
        subtitle_larger.setTag(FONT_SIZE_L);
        subtitle_middle.setTag(FONT_SIZE_M);
        subtitle_small.setTag(FONT_SIZE_S);

        track_title=(TextView)view.findViewById(R.id.track_title);
        track_root=view.findViewById(R.id.track_root);
        track_text=(TextView)view.findViewById(R.id.track_text);
        track_image=(ImageView)view.findViewById(R.id.track_image);
        track_root.setOnClickListener(this);
        closesetting=(ImageView)view.findViewById(R.id.closesetting);
        closesetting.setOnClickListener(this);
    }

    public static int FONT_SIZE_S=9;
    public static int FONT_SIZE_M=12;
    public static int FONT_SIZE_L=15;

    @Override
    public void onClick(View view) {
        if(closesetting==view){
            mCallBack.onSettingShowChange(PlayControlView.VIEW_ID_SETTINGS,false);
        }else if(doDecode(view)){
        }else if(doSubtitleSize(view)){
        }else if(doSubtitleIndex(view)){
        }else if(doTrackIndex(view)){
        }
    }

    public boolean doDecode(View view){
        if(view==decode_aoto||view==decode_sys
                ||view==decode_sysplus||view==decode_soft){
            clearDecodeView();
            setViewBg(view,true);
            mCallBack.onChangeDecodeType((int)view.getTag());
            return true;
        }
        return false;
    }

    public boolean doSubtitleIndex(View view){
        if(view==subtitle_root){
            setNextAudioStreamIndex(mSubtitleRichData);
            ItemData itemData=mSubtitleRichData.getCurrentItem();
            mCallBack.onSelectedSubtitle(itemData.id,itemData.name2);
            return true;
        }
        return false;
    }

    public boolean doTrackIndex(View view){
        if(view==track_root){
            setNextAudioStreamIndex(mAudioStreamRichData);
            ItemData itemData=mAudioStreamRichData.getCurrentItem();
            mCallBack.onAudioTrackChange(itemData.id);
            return true;
        }
        return false;
    }

    public boolean doSubtitleSize(View view){
        if(view==subtitle_larger||view==subtitle_middle
                ||view==subtitle_small){
            clearSubtitleView();
            setViewBg(view,true);
            mCallBack.onScreenSubtitleFontSize((int)view.getTag());
            return true;
        }
        return false;
    }

    public void setViewBg(View view,boolean isFocus){
        if(!isFocus){
            view.setBackgroundResource(R.drawable.play_select_grid_item_normal);
        }else{
            view.setBackgroundResource(R.drawable.play_select_grid_item_click);
        }
    }
    public void clearDecodeView(){
        setViewBg(decode_aoto,false);
        setViewBg(decode_sys,false);
        setViewBg(decode_sysplus,false);
        setViewBg(decode_soft,false);
    }
    public void clearSubtitleView(){
        setViewBg(subtitle_larger,false);
        setViewBg(subtitle_middle,false);
        setViewBg(subtitle_small,false);
    }

    public static class ItemData{
        public final static int TYPE_SCREEN_SUBTITLES=2;//字幕
        public final static int TYPE_AUDIO_TRACK=5;
        public String name;
        public int id;
        public int type;
        public String name2;
    }

    public static class RichData{
        public ArrayList<ItemData> mAudioStreamList;
        public int index;
        public ItemData getCurrentItem(){
            if(mAudioStreamList==null||mAudioStreamList.size()<=index){
                return null;
            }
            return mAudioStreamList.get(index);
        }
    }

    RichData mAudioStreamRichData;
    RichData mSubtitleRichData;

    public void setNextAudioStreamIndex(RichData data){
        if(data==null||data.mAudioStreamList==null||data.mAudioStreamList.size()==0){
            return;
        }else{
            data.index++;
            if(data.index>=data.mAudioStreamList.size()){
                data.index=0;
            }
        }
        setAudioStreamIndex(data);
    }

    public void  setAudioStreamIndex(RichData data){
        if(data==null||data.mAudioStreamList.size()==0
                ||data.index>=data.mAudioStreamList.size()||data.index<0){
            return ;
        }
        ItemData itemData=data.mAudioStreamList.get(data.index);
        if(itemData.type== ItemData.TYPE_AUDIO_TRACK){
            if(itemData.id==-1000){
                track_title.setTextColor(0xff666666);
                track_text.setTextColor(0xff666666);
                track_text.setText("无可选音轨");
                track_image.setImageResource(R.drawable.play_touch_setting_icon_change_disable);
            }else{
                track_title.setTextColor(0xffffffff);
                track_text.setTextColor(0xffffffff);
                track_text.setText(itemData.name);
                if(data.mAudioStreamList.size()==1){
                    track_image.setImageResource(R.drawable.play_touch_setting_icon_change_disable);
                }else{
                    track_image.setImageResource(R.drawable.play_touch_setting_icon_change_normal);
                }
            }
        }else{
            if(itemData.id==-1000){
                subtitle_title.setTextColor(0xff666666);
                subtitle_text.setTextColor(0xff666666);
                subtitle_text.setText("无可选字幕");
                subtitle_image.setImageResource(R.drawable.play_touch_setting_icon_change_disable);
                subtitle_larger.setTextColor(0xff666666);
                subtitle_middle.setTextColor(0xff666666);
                subtitle_small.setTextColor(0xff666666);
                subtitle_larger.setClickable(false);
                subtitle_middle.setClickable(false);
                subtitle_small.setClickable(false);
                setViewBg(subtitle_small,false);
                setViewBg(subtitle_middle,false);
                setViewBg(subtitle_larger,false);
            }else{
                subtitle_title.setTextColor(0xffffffff);
                subtitle_text.setTextColor(0xffffffff);
                subtitle_larger.setTextColor(0xffffffff);
                subtitle_middle.setTextColor(0xffffffff);
                subtitle_small.setTextColor(0xffffffff);
                subtitle_larger.setClickable(true);
                subtitle_middle.setClickable(true);
                subtitle_small.setClickable(true);
                subtitle_text.setText(itemData.name);
                if(data.mAudioStreamList.size()==1){
                    subtitle_image.setImageResource(R.drawable.play_touch_setting_icon_change_disable);
                }else{
                    subtitle_image.setImageResource(R.drawable.play_touch_setting_icon_change_normal);
                }
            }
        }
    }

    public void setAudioStreamList(List<AudioSteamInfo.AudioStremItem> audioStreamList) {
        mAudioStreamRichData=new RichData();
        ArrayList<ItemData> mItem=new ArrayList<ItemData>();
        if(audioStreamList==null||audioStreamList.size()==0){
            LogHelper.d(TAG,"audioStreamList is null");
            ItemData itemData=new ItemData();
            itemData.id= -1000;
            itemData.type= ItemData.TYPE_AUDIO_TRACK;
            itemData.name="";
            mItem.add(itemData);
            mAudioStreamRichData.mAudioStreamList=mItem;
            mAudioStreamRichData.index=0;
           setNextAudioStreamIndex(mAudioStreamRichData);
            return;
        }
        for(int i=0;audioStreamList.size()>i;i++){
            AudioSteamInfo.AudioStremItem audioItem=audioStreamList.get(i);
            LogHelper.d(TAG,"audioStreamList is not null,audioItem:"+audioItem.handler_name+" id :"+audioItem.getIndex());
            ItemData itemData=new ItemData();
            itemData.type= ItemData.TYPE_AUDIO_TRACK;
            itemData.name= PlaySettingsView.changeAudioStream(audioItem.getHandler_name(),i);
            itemData.name2= audioItem.getHandler_name();
            itemData.id= audioItem.getIndex();
            mItem.add(itemData);
        }
        LogHelper.d(TAG,"audioTrackView.setData(mItem)");
        mAudioStreamRichData.mAudioStreamList=mItem;
        LogHelper.d(TAG,"audioTrackView setSelectIndex");
        if(preLastAudioStreamIndex==-1){
            mAudioStreamRichData.index=0;
            setAudioStreamIndex(mAudioStreamRichData);
        }else{
            mAudioStreamRichData.index=preLastAudioStreamIndex;
            setAudioStreamIndex(mAudioStreamRichData);
        }

    }

    public void setSubtitleList(List<String> subtitleList){
        mSubtitleRichData=new RichData();
        ArrayList<ItemData> mItem=new ArrayList<ItemData>();
        if(subtitleList==null||subtitleList.size()==0){
            LogHelper.d(TAG,"setSubtitleList is  null");
            ItemData itemData=new ItemData();
            itemData.id= -1000;
            itemData.type= ItemData.TYPE_SCREEN_SUBTITLES;
            itemData.name="";
            mItem.add(itemData);
            mSubtitleRichData.mAudioStreamList=mItem;
            mSubtitleRichData.index=0;
            setNextAudioStreamIndex(mSubtitleRichData);
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
        mSubtitleRichData.mAudioStreamList=mItem;
        if(TextUtils.isEmpty(preLastSubtitleName)){
            mSubtitleRichData.index=0;
            setAudioStreamIndex(mSubtitleRichData);
            return ;
        }else{
            mSubtitleRichData.index=getIndexByName(mSubtitleRichData,preLastSubtitleName);
            setAudioStreamIndex(mSubtitleRichData);
        }
    }

    String preLastSubtitleName="";
    public void setSelectedSubtitle(String name) {
        preLastSubtitleName=name;
        if(!TextUtils.isEmpty(preLastSubtitleName)&&mSubtitleRichData!=null){
            mSubtitleRichData.index=getIndexByName(mSubtitleRichData,preLastSubtitleName);
            setAudioStreamIndex(mSubtitleRichData);
        }
    }

    int preLastAudioStreamIndex;
    public void setSelectedAudioStream(int index) {
        preLastAudioStreamIndex=index;
        if(preLastAudioStreamIndex!=-1&&mAudioStreamRichData!=null){
            mAudioStreamRichData.index=preLastAudioStreamIndex;
            setAudioStreamIndex(mAudioStreamRichData);
        }
    }

    public int getIndexByName(RichData richData,String name){
            if(richData.mAudioStreamList==null|| TextUtils.isEmpty(name)){
                return 0;
            }else{
                for(ItemData itemData: richData.mAudioStreamList){
                    if(!TextUtils.isEmpty(itemData.name)){
                        if(itemData.name.equals(name)){
                            return itemData.id;
                        }
                    }
                }
            }
            return 0;
    }

    public static String changeAudioStream(String name,int index){
        if(TextUtils.isEmpty(name)){
            return "";
        }
        if(name.contains("default")){
            return "未知音轨"+(index+1);
        }
        return name;
    }


    private IPlayerSettingCallBack mCallBack;

    public void setIPlayerSettingCallBack(IPlayerSettingCallBack callBack) {
        this.mCallBack = callBack;
        int fontSize=getSubtitleSize(mContext);
        fontSize=changeGL2Layout(fontSize);
        mCallBack.onScreenSubtitleFontSize(fontSize);
        clearSubtitleView();
        if(fontSize==(int)subtitle_small.getTag()){
            setViewBg(subtitle_small,true);
        }else if(fontSize==(int)subtitle_middle.getTag()){
            setViewBg(subtitle_middle,true);
        }else if(fontSize==(int)subtitle_larger.getTag()){
            setViewBg(subtitle_larger,true);
        }
    }

    public static int getSubtitleSize(Context context){
        int size;
        SharedPreferences sp = context.getSharedPreferences(MovieSettingSubView.movie_subtitle_size_name, Context.MODE_PRIVATE);
        size=sp.getInt(MovieSettingSubView.movie_subtitle_size_key,MovieSettingSubView.subTitleSize_m);
        return size;
    }

    public void setSelectedDecodeType(int type) {
        LogHelper.d(TAG,"setSelectedDecodeType:"+type);
        View view=null;
        if(type==(int)decode_aoto.getTag()){
            view=decode_aoto;
        }else if(type==(int)decode_sys.getTag()){
            view=decode_sys;
        }else if(type==(int)decode_sysplus.getTag()){
            view=decode_sysplus;
        }else if(type==(int)decode_soft.getTag()){
            view=decode_soft;
        }
        if(view!=null){
            LogHelper.d(TAG,"setSelectedDecodeType is not null:"+type);
            clearDecodeView();
            setViewBg(view,true);
        }
    }

    public void setSelectedSubtiteFondSize(int fontSize){
            clearSubtitleView();
            LogHelper.d(MovieSettingSubView.settingCallback,"play lay clearSubtitleView : "+fontSize);
            if(FONT_SIZE_L==fontSize){
                setViewBg(subtitle_larger,true);
            }else if(FONT_SIZE_M==fontSize){
                setViewBg(subtitle_middle,true);
            }else if(FONT_SIZE_S==fontSize){
                setViewBg(subtitle_small,true);
            }
    }

    public int changeGL2Layout(int size){
        int fontSize=FONT_SIZE_M;
        if(size==MovieSettingSubView.subTitleSize_s){
            fontSize=FONT_SIZE_S;
        }else if(size==MovieSettingSubView.subTitleSize_m){
            fontSize=FONT_SIZE_M;
        }else if(size==MovieSettingSubView.subTitleSize_l){
            fontSize=FONT_SIZE_L;
        }
        return fontSize;
    }

    public int sendLayout2GL(int size){
        int fontSize=MovieSettingSubView.subTitleSize_m;
        if(size==FONT_SIZE_S){
            fontSize= MovieSettingSubView.subTitleSize_s;
        }else if(size==FONT_SIZE_M ){
            fontSize=MovieSettingSubView.subTitleSize_m;
        }else if(size==FONT_SIZE_L ){
            fontSize=MovieSettingSubView.subTitleSize_l;
        }
        return fontSize;
    }

    public void showView() {
        //if(getVisibility() != VISIBLE) {
            startAnimation(right_in_animation);
            setVisibility(VISIBLE);
      //  }
    }

    public void hideView() {
        if(getVisibility() == VISIBLE) {
            startAnimation(right_out_animation);
            setVisibility(INVISIBLE);
        }
    }

}