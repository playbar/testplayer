package com.mojing.vrplayer.publicc;

import android.content.Context;
import android.media.MediaMetadataRetriever;

import com.mojing.vrplayer.utils.ImageUtil;
import com.mojing.vrplayer.utils.ThreadPoolUtil;

/**
 * Created by liuchuanchi on 2016/6/16.
 * 视频类型工具类
 */
public class VideoTypeUtil {
    /**
     * 视频类型（算法组定义的）
     */
    public static final int MJVideoPictureTypeUnCreate = -1; //未创建过
    public static final int MJVideoPictureTypeUnknown = 0; //未设置
    public static final int MJVideoPictureTypeSingle = 1; //2D
    public static final int MJVideoPictureTypeSingle180 = 2; //not implemented yet
    public static final int MJVideoPictureTypeSideBySide = 3; //3d左右
    public static final int MJVideoPictureTypeSideBySide180 = 4; //not implemented yet
    public static final int MJVideoPictureTypeStacked = 5; //3d上下
    public static final int MJVideoPictureTypeStacked360 = 6; //not implemented yet
    public static final int MJVideoPictureTypeStacked180 = 7; //not implemented yet
    public static final int MJVideoPictureTypePanorama360 = 8; //360度全景
    public static final int MJVideoPictureTypeFacebook360 = 9; //not implemented yet
    public static final int MJVideoPictureTypePanorama3603DStacked = 10; //360度全景3d上下
    public static final int MJVideoPictureTypePanorama3603DSide = 11; //360度全景3d左右
    public static final int MJVideoPictureTypePanorama360Cube = 12; //360度立方体全景
    public static final int MJVideoPictureTypePanorama1803DSide = 13; //180度全景3d左右

    /**
     * 视频3d类型（横屏播放定义的）
     */
    public static final int Video2D = 0;
    public static final int VideoLR3D = 1;
    public static final int VideoUD3D = 2;
    public static final int VideoLR3DNoZoom = 3;
    public static final int VideoLR180 = 4;//全景180左右3D
    public static final int VideoCube2D = 5;//全景立方体2D

    /**
     * 全景类型（接口返回的）
     */
    public static final int panorama_normal = 1;//全景普通平面
    public static final int panorama_360 = 2;//全景360度球
    public static final int panorama_180 = 3;//全景180度球
    public static final int panorama_cube = 4;//全景立方体

    /**
     * 视频模式（接口返回的）
     */
    public static final int dimension_2d = 1;//2D
    public static final int dimension_3d_top_bottom = 2;//3D上下
    public static final int dimension_3d_left_right = 3;//3D左右

    /**
     * 本地u3d存储的video3dtype
     */
    public static final int video3Dtype_2d = 1;//2D
    public static final int video3Dtype_3d_top_bottom = 2;//3D上下
    public static final int video3Dtype_3d_left_right = 3;//3D左右

    /**
     * true视频是3d，false不是
     */
    public static boolean videoIs3D(int video3dType){
        if(VideoLR3D == video3dType ||
                VideoUD3D == video3dType ||
                VideoLR3DNoZoom == video3dType ||
                VideoLR180 == video3dType){
            return true;
        }
        return false;
    }

    /**
     * true视频是3d，false不是
     */
    public static boolean videoIs3D(String dimension){
        int video_dimension = Integer.valueOf(dimension);
        if(dimension_3d_top_bottom == video_dimension ||
                dimension_3d_left_right == video_dimension){
            return true;
        }
        return false;
    }

    /**
     * 是不是全景视频
     * @param videoType 视频类型
     * @return true是，false不是
     */
    public static boolean isPanoramaVideo(int videoType){
        if(MJVideoPictureTypePanorama360 == videoType ||
                MJVideoPictureTypePanorama3603DStacked == videoType ||
                MJVideoPictureTypePanorama3603DSide == videoType ||
                MJVideoPictureTypePanorama360Cube == videoType ||
                MJVideoPictureTypePanorama1803DSide == videoType){
            return true;
        }
        return false;
    }

    /**
     * 获取视频类型 根据历史文件中的videoType 和video3Dtype
     * @param mVideoType
     * @param mVideo3Dtype
     * @return
     */
    public static int getVideoTypeFromLoaclHistroy(int mVideoType,int mVideo3Dtype){
        int videoType = MJVideoPictureTypeSingle;//默认值
        switch (mVideoType){
            case panorama_normal://全景普通
                if(video3Dtype_2d == mVideo3Dtype){
                    videoType = MJVideoPictureTypeSingle;
                }else if(video3Dtype_3d_top_bottom == mVideo3Dtype){
                    videoType = MJVideoPictureTypeStacked;
                }else if(video3Dtype_3d_left_right == mVideo3Dtype){
                    videoType = MJVideoPictureTypeSideBySide;
                }
                break;
            case panorama_360://全景360度
                if(video3Dtype_2d == mVideo3Dtype){
                    videoType = MJVideoPictureTypePanorama360;
                }else if(video3Dtype_3d_top_bottom == mVideo3Dtype){
                    videoType = MJVideoPictureTypePanorama3603DStacked;
                }else if(video3Dtype_3d_left_right == mVideo3Dtype){
                    videoType = MJVideoPictureTypePanorama3603DSide;
                }
                break;
            case panorama_180://全景180度
                if(video3Dtype_2d == mVideo3Dtype){
                    videoType = MJVideoPictureTypePanorama360;
                }else if(video3Dtype_3d_top_bottom == mVideo3Dtype){
                    videoType = MJVideoPictureTypePanorama360;
                }else if(video3Dtype_3d_left_right == mVideo3Dtype){
                    videoType = MJVideoPictureTypePanorama1803DSide;
                }
                break;
            case panorama_cube://全景立方体
                if(video3Dtype_2d == mVideo3Dtype){
                    videoType = MJVideoPictureTypePanorama360Cube;
                }else if(video3Dtype_3d_top_bottom == mVideo3Dtype){
                    videoType = MJVideoPictureTypePanorama360Cube;
                }else if(video3Dtype_3d_left_right == mVideo3Dtype){
                    videoType = MJVideoPictureTypePanorama360Cube;
                }
                break;
            default:
                break;
        }
        return videoType;
    }

    /**
     * 获取视频类型
     * @param is_panorama 全景类型
     * @param video_dimension 视频模式
     * @return 视频类型
     */
    public static int getVideoType(int is_panorama, int video_dimension){
        int videoType = MJVideoPictureTypeSingle;//默认值
        switch (is_panorama){
            case panorama_normal://全景普通
                if(dimension_2d == video_dimension){
                    videoType = MJVideoPictureTypeSingle;
                }else if(dimension_3d_top_bottom == video_dimension){
                    videoType = MJVideoPictureTypeStacked;
                }else if(dimension_3d_left_right == video_dimension){
                    videoType = MJVideoPictureTypeSideBySide;
                }
                break;
            case panorama_360://全景360度
                if(dimension_2d == video_dimension){
                    videoType = MJVideoPictureTypePanorama360;
                }else if(dimension_3d_top_bottom == video_dimension){
                    videoType = MJVideoPictureTypePanorama3603DStacked;
                }else if(dimension_3d_left_right == video_dimension){
                    videoType = MJVideoPictureTypePanorama3603DSide;
                }
                break;
            case panorama_180://全景180度
                if(dimension_2d == video_dimension){
                    videoType = MJVideoPictureTypePanorama360;
                }else if(dimension_3d_top_bottom == video_dimension){
                    videoType = MJVideoPictureTypePanorama360;
                }else if(dimension_3d_left_right == video_dimension){
                    videoType = MJVideoPictureTypePanorama1803DSide;
                }
                break;
            case panorama_cube://全景立方体
                if(dimension_2d == video_dimension){
                    videoType = MJVideoPictureTypePanorama360Cube;
                }else if(dimension_3d_top_bottom == video_dimension){
                    videoType = MJVideoPictureTypePanorama360Cube;
                }else if(dimension_3d_left_right == video_dimension){
                    videoType = MJVideoPictureTypePanorama360Cube;
                }
                break;
            default:
                break;
        }
        return videoType;
    }

    public static void getVideoDuration(final Context mContext, final String videoPath, final VideoDurationCallback videoDurationCallback){
            ThreadPoolUtil.runThread(new Runnable() {
                @Override
                public void run() {
                    MediaMetadataRetriever retriever = new MediaMetadataRetriever();
                    final long videoDuration = ImageUtil.getVideoDuration(retriever, videoPath);
                    try {
                        retriever.release();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    if (videoDurationCallback != null) {
                        videoDurationCallback.result(videoDuration);
                    }
                }
            });
    }


    /**
     * 获取视频类型
     * @param filePath 文件路径
     * @param videoTypeCallback 回调
     */
    public static void getVideoType(final String filePath, final VideoTypeCallback videoTypeCallback){
        //从数据库获取视频类型
//        int videoType = SqliteProxy.getInstance().addProxyExecute(new SqliteProxy.ProxyExecute<Integer>() {
//            @Override
//            public Integer execute() {
//                int tempVideoType = SqliteManager.getInstance().getFromLocalVideoType(filePath);
//                SqliteManager.getInstance().closeSQLiteDatabase();
//                return tempVideoType;
//            }
//        });
//        if(MJVideoPictureTypeUnCreate == videoType){//当前视频没有创建过视频类型
//            ThreadPoolUtil.runThread(new Runnable() {
//                @Override
//                public void run() {
//                    MediaMetadataRetriever retriever = new MediaMetadataRetriever();
//                    int videoType = ImageUtil.createVideoType(retriever, filePath);
//                    SqliteProxy.getInstance().addProxyRunnable(new SqliteProxy.ProxyRunnable() {
//                        @Override
//                        public void run() {
//                            SqliteManager.getInstance().closeSQLiteDatabase();
//                        }
//                    });
//                    try {
//                        retriever.release();
//                    } catch (Exception ex) {
//                        ex.printStackTrace();
//                    }
//                    if (videoTypeCallback != null) {
//                        videoTypeCallback.result(videoType);
//                    }
//                }
//            });
//        }else{
//            if(videoTypeCallback != null){
//                videoTypeCallback.result(videoType);
//            }
//        }



        if (videoTypeCallback != null) {
                        videoTypeCallback.result(0);
                    }
    }

    public interface VideoTypeCallback{
        void result(int videoType);
    }
    public interface VideoDurationCallback{
        void result(long duration);
    }
}
