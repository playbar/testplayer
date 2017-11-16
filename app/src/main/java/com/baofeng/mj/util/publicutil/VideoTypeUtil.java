package com.baofeng.mj.util.publicutil;

import android.content.Context;
import android.media.MediaMetadataRetriever;

import com.baofeng.mj.util.threadutil.SqliteProxy;
import com.baofeng.mj.vrplayer.business.SqliteManager;
import com.baofeng.mj.vrplayer.util.ImageUtil;
import com.baofeng.mj.vrplayer.util.ThreadPoolUtil;

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
    public static final int MJVideoPictureTypeSingle = 1; //2d 平面
    public static final int MJVideoPictureTypeSingle180 = 2; //not implemented yet
    public static final int MJVideoPictureTypeSideBySide = 3; //3d左右 平面
    public static final int MJVideoPictureTypeSideBySide180 = 4; //not implemented yet
    public static final int MJVideoPictureTypeStacked = 5; //3d上下 平面
    public static final int MJVideoPictureTypeStacked360 = 6; //not implemented yet
    public static final int MJVideoPictureTypeStacked180 = 7; //not implemented yet
    public static final int MJVideoPictureTypePanorama360 = 8; //2d 球面(360度全景)
    public static final int MJVideoPictureTypeFacebook360 = 9; //not implemented yet
    public static final int MJVideoPictureTypePanorama3603DStacked = 10; //3d上下 球面(3d上下 360度全景)
    public static final int MJVideoPictureTypePanorama3603DSide = 11; //3d左右 球面(3d左右 360度全景)
    public static final int MJVideoPictureTypePanorama360Cube = 12; //360度立方体全景
    public static final int MJVideoPictureTypePanorama1803DSide = 13; //3d左右 半球(3d左右 180度全景)

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
    public static boolean videoIs3D(String videoDimension){
        int video_dimension = Integer.valueOf(videoDimension);
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
     * @param mVideoType 全景类型
     * @param mVideo3Dtype 视频模式
     */
    public static int getVideoTypeFromLoaclHistroy(int mVideoType,int mVideo3Dtype){
        return getVideoType(mVideoType, mVideo3Dtype);
    }

    /**
     * 获取视频类型
     * @param videoPanorama 全景类型
     * @param videoDimension 视频模式
     */
    public static int getVideoType(int videoPanorama, int videoDimension){
        int videoType = MJVideoPictureTypeSingle;//默认值
        switch (videoPanorama){
            case panorama_normal://全景普通
                if(dimension_2d == videoDimension){
                    videoType = MJVideoPictureTypeSingle;
                }else if(dimension_3d_top_bottom == videoDimension){
                    videoType = MJVideoPictureTypeStacked;
                }else if(dimension_3d_left_right == videoDimension){
                    videoType = MJVideoPictureTypeSideBySide;
                }
                break;
            case panorama_360://全景360度
                if(dimension_2d == videoDimension){
                    videoType = MJVideoPictureTypePanorama360;
                }else if(dimension_3d_top_bottom == videoDimension){
                    videoType = MJVideoPictureTypePanorama3603DStacked;
                }else if(dimension_3d_left_right == videoDimension){
                    videoType = MJVideoPictureTypePanorama3603DSide;
                }
                break;
            case panorama_180://全景180度
                if(dimension_2d == videoDimension){
                    videoType = MJVideoPictureTypePanorama360;
                }else if(dimension_3d_top_bottom == videoDimension){
                    videoType = MJVideoPictureTypePanorama360;
                }else if(dimension_3d_left_right == videoDimension){
                    videoType = MJVideoPictureTypePanorama1803DSide;
                }
                break;
            case panorama_cube://全景立方体
                if(dimension_2d == videoDimension){
                    videoType = MJVideoPictureTypePanorama360Cube;
                }else if(dimension_3d_top_bottom == videoDimension){
                    videoType = MJVideoPictureTypePanorama360Cube;
                }else if(dimension_3d_left_right == videoDimension){
                    videoType = MJVideoPictureTypePanorama360Cube;
                }
                break;
            default:
                break;
        }
        return videoType;
    }

    /**
     * 获取全景类型和视频模式
     * @param videoType 视频类型
     */
    public static int[] getPanoramaAndDimension(int videoType){
        switch (videoType){
            case MJVideoPictureTypeSingle:
                return new int[]{panorama_normal, dimension_2d};
            case MJVideoPictureTypeSideBySide:
                return new int[]{panorama_normal, dimension_3d_left_right};
            case MJVideoPictureTypeStacked:
                return new int[]{panorama_normal, dimension_3d_top_bottom};
            case MJVideoPictureTypePanorama360:
                return new int[]{panorama_360, dimension_2d};
            case MJVideoPictureTypePanorama3603DStacked:
                return new int[]{panorama_360, dimension_3d_top_bottom};
            case MJVideoPictureTypePanorama3603DSide:
                return new int[]{panorama_360, dimension_3d_left_right};
            case MJVideoPictureTypePanorama1803DSide:
                return new int[]{panorama_180, dimension_3d_left_right};
            default:
                return new int[]{panorama_normal, dimension_2d};
        }
    }

    /**
     * 获取视频类型
     * @param videoPath 视频地址
     * @param videoTypeCallback 回调
     */
    public static void getVideoType(final Context mContext, final String videoPath, final VideoTypeCallback videoTypeCallback){
        //从数据库获取视频类型
        int videoType = VideoTypeUtil.MJVideoPictureTypeUnCreate;
        Integer videoTypeI = SqliteProxy.getInstance().addProxyExecute(new SqliteProxy.ProxyExecute<Integer>() {
            @Override
            public Integer execute() {
                int tempVideoType = SqliteManager.getInstance(mContext).getFromLocalVideoType(videoPath);
                SqliteManager.getInstance(mContext).closeSQLiteDatabase();
                return tempVideoType;
            }
        });
        if(videoTypeI != null){
            videoType = videoTypeI;
        }
        if(MJVideoPictureTypeUnCreate == videoType){//当前视频没有创建过视频类型
            ThreadPoolUtil.runThread(new Runnable() {
                @Override
                public void run() {
                    MediaMetadataRetriever retriever = new MediaMetadataRetriever();
                    final int videoType = ImageUtil.createVideoType(retriever, videoPath);
                    SqliteProxy.getInstance().addProxyExecute(new SqliteProxy.ProxyExecute<Integer>() {
                        @Override
                        public Integer execute() {
                            SqliteManager.getInstance(mContext).addToLocalVideoType(videoPath, videoType);
                            SqliteManager.getInstance(mContext).closeSQLiteDatabase();
                            return null;
                        }
                    });
                    try {
                        retriever.release();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    if (videoTypeCallback != null) {
                        videoTypeCallback.result(videoType);
                    }
                }
            });
        }else{
            if(videoTypeCallback != null){
                videoTypeCallback.result(videoType);
            }
        }
    }

    /**
     * 保存视频类型
     * @param videoPath 视频地址
     * @param videoType 视频类型
     */
    public static void saveVideoType(final Context mContext, final String videoPath, final int videoType){
        SqliteProxy.getInstance().addProxyExecute(new SqliteProxy.ProxyExecute<Integer>() {
            @Override
            public Integer execute() {
                SqliteManager.getInstance(mContext).updateToLocalVideoType(videoPath, videoType);
                SqliteManager.getInstance(mContext).closeSQLiteDatabase();
                return null;
            }
        });
    }

    public interface VideoTypeCallback{
        void result(int videoType);
    }
}
