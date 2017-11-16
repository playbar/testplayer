package com.baofeng.mj.vrplayer.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.baofeng.mj.vrplayer.MyAppliaction;
import com.baofeng.mj.vrplayer.R;
import com.baofeng.mj.vrplayer.activity.MainActivity;
import com.baofeng.mj.vrplayer.adapter.VideoAdapter;
import com.baofeng.mj.vrplayer.bean.LocalVideoBean;
import com.baofeng.mj.vrplayer.business.LocalVideoBusiness;
import com.baofeng.mj.vrplayer.business.LocalVideoPathBusiness;
import com.baofeng.mj.vrplayer.business.OpenActivity;
import com.baofeng.mj.vrplayer.business.PlayBusiness;
import com.baofeng.mj.vrplayer.business.SpPublicBusiness;
import com.baofeng.mj.vrplayer.util.FileCommonUtil;
import com.baofeng.mj.vrplayer.util.LocalVideoProxy;
import com.baofeng.mj.vrplayer.util.SingleThreadProxy;
import com.baofeng.mj.util.publicutil.VideoTypeUtil;
import com.baofeng.mj.vrplayer.widget.FileDeleteDialog;
import com.baofeng.mj.vrplayer.widget.StorageSelectDialog;
import com.bumptech.glide.Glide;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.mojing.vrplayer.interfaces.IMojingAppCallback;
import com.mojing.vrplayer.publicc.MojingAppCallbackUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;

/**
 * 视频Fragment
 */
public class VideoFragment extends BaseFragment {
    private TextView tv_nav_title;
    private LinearLayout ll_nav_one;
    private ImageView iv_nav_one;
    private LinearLayout ll_nav_two;
    private ImageView iv_nav_two;
    private LinearLayout ll_nav_three;
    private ImageView iv_nav_three;
    private LinearLayout ll_nav_four;
    private ImageView iv_nav_four;
    private ImageView imageview_vr;
//    private View headerView;//置顶的HeaderView
    private ListView listView;
    PullToRefreshListView mPullToRefreshListView;
    private VideoAdapter videoAdapter;
    private List<LocalVideoBean> localVideoBeanList;
    private boolean isNameAscending = true;//true名称升序，false相反
    private boolean isTimeAscending = true;//true时间升序，false相反
    private boolean isLoadingData;//true正在加载数据，false相反
    private StorageSelectDialog storageSelectDialog;
    private IMojingAppCallback iMojingAppCallback;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        localVideoBeanList = new ArrayList<LocalVideoBean>();
        videoAdapter = new VideoAdapter(getActivity(), this, localVideoBeanList);
        iMojingAppCallback = new IMojingAppCallback() {
            @Override
            public void changeVideoType(String videoPath, int videoDimension, int videoPanorama) {
                int videoType = VideoTypeUtil.getVideoType(videoPanorama, videoDimension);
                VideoTypeUtil.saveVideoType(getActivity(), videoPath, videoType);
            }
        };
        MojingAppCallbackUtil.setIMojingAppCallback(iMojingAppCallback);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Glide.with(this).onStart();//开启图片加载
        if(rootView == null){
            rootView = inflater.inflate(R.layout.layout_video, container, false);
            initView(inflater);//初始化控件
            initTopBar();//初始化顶部导航栏
        }else{
            removeRootView();
        }
        return rootView;
    }

    @Override
    public void onDestroyView() {
        Glide.with(this).onStop();//停止图片加载
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        MojingAppCallbackUtil.setIMojingAppCallback(null);
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        getData(true);//获取数据
    }

    /**
     * 获取数据
     */
    private void getData(final boolean onResume){
        if(isLoadingData){
            Toast.makeText(getActivity(), "正在加载数据...", Toast.LENGTH_SHORT).show();
            mPullToRefreshListView.onRefreshComplete();
            return;
        }
        isLoadingData = true;
        final int sortRule = SpPublicBusiness.getInstance().getLocalVideoSort(getActivity());
        LocalVideoProxy.getInstance().addProxyRunnable(new SingleThreadProxy.ProxyRunnable() {
            @Override
            public void run() {
                if(!onResume){
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                if(getActivity() == null){
                    isLoadingData = false;
                    mPullToRefreshListView.onRefreshComplete();
                    return;
                }
                LocalVideoBusiness.getInstance().searchVideoByNative(getActivity(), new LocalVideoBusiness.LocalVideoDataCallback() {
                    @Override
                    public void callback(List<LocalVideoBean> resultList) {
                        if(sortRule == FileCommonUtil.ruleFileLastModify){
                            refreshUI(LocalVideoBusiness.getInstance().sortVideo(getActivity(), resultList, sortRule, isTimeAscending));
                        }else if(sortRule == FileCommonUtil.ruleFileName){
                            refreshUI(LocalVideoBusiness.getInstance().sortVideo(getActivity(), resultList, sortRule, isNameAscending));
                        }
                        isLoadingData = false;

                    }
                });
            }
        });
    }

    /**
     * 刷新UI
     */
    private void refreshUI(final HashMap<String, TreeMap<String, LocalVideoBean>> hashMap){
        if(getActivity() == null){
            return;
        }
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                localVideoBeanList.clear();
                List<String> localVideoDirList = LocalVideoPathBusiness.getAllDir(getActivity(), false);
                for(int i = 0;i<localVideoDirList.size();i++){//遍历所有文件夹(所有分组)
                    String groupName = localVideoDirList.get(i);
                    TreeMap treeMap = (TreeMap) hashMap.get(groupName);
                    Iterator iterator = treeMap.values().iterator();
                    LocalVideoBean localVideoBeanTitle=new LocalVideoBean();
                    localVideoBeanTitle.itemType=LocalVideoBean.ITEM_TYPE_TITLE;
                    localVideoBeanTitle.group=groupName;
                    localVideoBeanTitle.name="";
                    localVideoBeanTitle.size="";
                    localVideoBeanTitle.path="";
                    localVideoBeanTitle.thumbPath="";
                    localVideoBeanTitle.visible=LocalVideoBean.VISIBLE_TYPE_VISIBLE;
                    localVideoBeanList.add(localVideoBeanTitle);
                    boolean isAdd=false;
                    while (iterator.hasNext()) {
                        LocalVideoBean localVideoBean = (LocalVideoBean) iterator.next();
                        localVideoBean.visible=LocalVideoBean.VISIBLE_TYPE_VISIBLE;
                        localVideoBeanList.add(localVideoBean);
                        isAdd=true;
                    }
                    if(!isAdd){
                        localVideoBeanList.remove(localVideoBeanTitle);
                    }
                }
                if(MyAppliaction.getInstance().getmFileUploadBusiness()!=null){
                    MyAppliaction.getInstance().getmFileUploadBusiness().removeNotExist(localVideoBeanList);
                }
                videoAdapter.notifyDataSetChanged();
                mPullToRefreshListView.onRefreshComplete();
            }
        });
    }

    /**
     * 初始化控件
     */
    private void initView(LayoutInflater inflater){
        imageview_vr = (ImageView)rootView.findViewById(R.id.imageview_vr);
        mPullToRefreshListView = (PullToRefreshListView) rootView.findViewById(R.id.mPullToRefreshListView);
        listView = (ListView) mPullToRefreshListView.getRefreshableView();
//        headerView = inflater.inflate(R.layout.layout_video_item_header, listView, false);
//        listView.setPinnedHeader(headerView);
        listView.setAdapter(videoAdapter);
        listView.setOnScrollListener(videoAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                ((MainActivity)getActivity()).showLoadingView();//显示加载匡
                final int currentPosition=localVideoBeanList.indexOf((LocalVideoBean)videoAdapter.getItem(position-1)) ;
                final LocalVideoBean localVideoBean = localVideoBeanList.get(currentPosition);
                VideoTypeUtil.getVideoType(getActivity(), localVideoBean.path, new VideoTypeUtil.VideoTypeCallback() {
                    @Override
                    public void result(int videoType) {
                        ((MainActivity)getActivity()).hideLoadingView();//隐藏加载匡
                        localVideoBean.videoType = videoType;
                        MyAppliaction.getInstance().getmFileUploadBusiness().removeVideoTag(localVideoBean.name);

                        List<LocalVideoBean> playListlocalVideoBeanList=new ArrayList<LocalVideoBean>();
                        LocalVideoBean playListLocalVideoBean=localVideoBeanList.get(currentPosition);
                        for(LocalVideoBean localVideoBean:localVideoBeanList){
                            if(localVideoBean.itemType==LocalVideoBean.ITEM_TYPE_ITEM){
                                playListlocalVideoBeanList.add(localVideoBean);
                            }
                        }
                        int playIndex=playListlocalVideoBeanList.indexOf(playListLocalVideoBean);
                        PlayBusiness.playLocalVideo(getActivity(), playListlocalVideoBeanList, playIndex, videoType,false);
                       // PlayBusiness.playLocalVideo(getActivity(), localVideoBeanList, currentPosition, videoType,false);
                    }
                });
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int i, long l) {
                if (fileDeleteDialog == null) {
                    fileDeleteDialog = new FileDeleteDialog(getActivity());
                }
                fileDeleteDialog.showDialog(new FileDeleteDialog.MyDialogInterface() {
                    @Override
                    public void dialogCallBack() {
                        final int currentPosition=localVideoBeanList.indexOf((LocalVideoBean)videoAdapter.getItem(i-1)) ;
                        if(currentPosition>=0){
                            LocalVideoBean localVideoBean=localVideoBeanList.get(currentPosition);
                            localVideoBeanList.remove(currentPosition);
                            int sizeInFolder=0;
                            LocalVideoBean tempFolderLocalVideoBean=null;
                            for(LocalVideoBean tempLocalVideoBean:localVideoBeanList){//过滤删除没有文件的文件夹
                                if(tempLocalVideoBean.group.equals(localVideoBean.group)){
                                    tempFolderLocalVideoBean=tempLocalVideoBean;
                                    sizeInFolder++;
                                }
                            }
                            if(sizeInFolder==1&&tempFolderLocalVideoBean.itemType==LocalVideoBean.ITEM_TYPE_TITLE){
                                localVideoBeanList.remove(tempFolderLocalVideoBean);
                            }

                            videoAdapter.notifyDataSetChanged();
                            com.mojing.vrplayer.publicc.FileCommonUtil.deleteFile(localVideoBean.path);
                        }
                    }
                });

                return true;
            }
        });

        imageview_vr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenActivity.openUnity(getActivity());
            }
        });
        mPullToRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                getData(false);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {

            }
        });
    }
    FileDeleteDialog fileDeleteDialog;

    /**
     * 初始化顶部导航栏
     */
    private void initTopBar(){
        tv_nav_title = (TextView) rootView.findViewById(R.id.tv_nav_title);
        ll_nav_one = (LinearLayout) rootView.findViewById(R.id.ll_nav_one);
        iv_nav_one = (ImageView) rootView.findViewById(R.id.iv_nav_one);
        ll_nav_two = (LinearLayout) rootView.findViewById(R.id.ll_nav_two);
        iv_nav_two = (ImageView) rootView.findViewById(R.id.iv_nav_two);
        ll_nav_three = (LinearLayout) rootView.findViewById(R.id.ll_nav_three);
        iv_nav_three = (ImageView) rootView.findViewById(R.id.iv_nav_three);
        ll_nav_four = (LinearLayout) rootView.findViewById(R.id.ll_nav_four);
        iv_nav_four = (ImageView) rootView.findViewById(R.id.iv_nav_four);

        tv_nav_title.setVisibility(View.VISIBLE);
        tv_nav_title.setText("视频");
        iv_nav_one.setImageResource(R.mipmap.video_icon_refresh);
        ll_nav_one.setVisibility(View.VISIBLE);
        ll_nav_one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//刷新
               // getData(false);//获取数据
                mPullToRefreshListView.setRefreshing();
            }
        });
        iv_nav_two.setImageResource(R.mipmap.mj_vrplayer_icon_folder_add);
        ll_nav_two.setVisibility(View.VISIBLE);
        ll_nav_two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//添加文件夹
                if(storageSelectDialog == null){
                    storageSelectDialog = new StorageSelectDialog(getActivity());
                }
                storageSelectDialog.showDialog(new StorageSelectDialog.MyDialogInterface() {
                    @Override
                    public void dialogCallBack(String selectPath) {//选择文件夹回调
                        LocalVideoPathBusiness.addCustomDir(selectPath);
                        getData(false);//获取数据
                    }
                });
            }
        });
        iv_nav_three.setImageResource(R.mipmap.video_icon_name_sort_ascending);
        ll_nav_three.setVisibility(View.VISIBLE);
        ll_nav_three.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//名称排序
                if(isNameAscending){//升序
                    isNameAscending = false;
                    iv_nav_three.setImageResource(R.mipmap.video_icon_name_sort_descending);
                }else{//降序
                    isNameAscending = true;
                    iv_nav_three.setImageResource(R.mipmap.video_icon_name_sort_ascending);
                }
                int sortRule = FileCommonUtil.ruleFileName;
                SpPublicBusiness.getInstance().setLocalVideoSort(getActivity(), sortRule);
                refreshUI(LocalVideoBusiness.getInstance().sortVideo(getActivity(), localVideoBeanList, sortRule, isNameAscending));
            }
        });
        iv_nav_four.setImageResource(R.mipmap.video_icon_time_sort_ascending);
        ll_nav_four.setVisibility(View.VISIBLE);
        ll_nav_four.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//上次修改时间排序
                if(isTimeAscending){//升序
                    isTimeAscending = false;
                    iv_nav_four.setImageResource(R.mipmap.video_icon_time_sort_descending);
                }else{//降序
                    isTimeAscending = true;
                    iv_nav_four.setImageResource(R.mipmap.video_icon_time_sort_ascending);
                }
                int sortRule = FileCommonUtil.ruleFileLastModify;
                SpPublicBusiness.getInstance().setLocalVideoSort(getActivity(), sortRule);
                refreshUI(LocalVideoBusiness.getInstance().sortVideo(getActivity(), localVideoBeanList, sortRule, isTimeAscending));
            }
        });
    }
}
