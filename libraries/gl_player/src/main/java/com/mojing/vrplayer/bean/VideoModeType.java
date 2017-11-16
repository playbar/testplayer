package com.mojing.vrplayer.bean;

import com.storm.smart.play.call.IBfPlayerConstant;

/**
 * Created by wanghongfang on 2017/5/18.
 * 定义播放模式类型值
 *     同步Unity存储的数据类型，保证存储到文件后，u3d播放取到的数据可用的
 */
public class VideoModeType {
    /**
     * 播放模式
     */
   public static final int Video2D = 1;
    public static final int  VideoUD3D = 2;
    public static final int VideoLR3D = 3;
    public static final int   VideoLR3DNOZoom = 4;

    public static final int Mode_Rect = 1;
    public static final int Mode_Sphere360 = 2;
    public static final int Mode_Sphere180 = 3;
    public static final int Mode_Box = 4;

    /**
     * 画面旋转
     */
    public static final int Mode_CCW_Rotate = 0;//逆时针旋转
    public static final int Mode_CW_Rotate = 1;//顺时针旋转
    /**
     * 解码方式
     */
    public static int PLAYER_AUTO = IBfPlayerConstant.IBasePlayerType.TYPE_NONE; //自动
    public static int PLAYER_SYS = IBfPlayerConstant.IBasePlayerType.TYPE_SYS; //硬解
    public static int PLAYER_SOFT = IBfPlayerConstant.IBasePlayerType.TYPE_SOFT; //软解
    public static int PLAYER_SYSPLUS = IBfPlayerConstant.IBasePlayerType.TYPE_SYSPLUS; //硬解+

 /**
  * 播放屏幕大小模式
  */
    public static final  int Small_Screen = 1;
    public static final  int Normal_Screen = 2;
    public static final  int Large_Screen = 3;
    public static final  int Huge_Screen = 4;
    public static final  int IMAX_Screen = 5;

    public static final int PLAY_MODE_VR = 0; //沉浸模式
    public static final int PLAY_MODE_SIMPLE = 1; //触屏模式
    public static final int PLAY_MODE_SIMPLE_FULL = 2; //触屏全屏模式


    /**
     * type
     */
    public static final int MOVIE_TYPE = 1;
    public static final int PANO_TYPE = 2;
    public static final int LOCAL_TYPE = 3;
    public static final int DOWNLOAD_TYPE = 4;


}
