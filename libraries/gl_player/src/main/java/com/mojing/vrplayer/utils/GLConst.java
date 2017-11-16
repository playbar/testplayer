package com.mojing.vrplayer.utils;

/**
 * 应用的 公共常量
 * @author wanghongfang
 * @date 2017-4-17 下午7:02:22
 */
public class GLConst {
	//沉浸模式是否调整Unity
    public static boolean GoUnity = false;

	/**
	 * 深度值
	 */
	public static float Movie_Player_Depth = 18; //影片层深度
	public static float Player_Controler_Depth = 1.6f;//基础控制层
	public static float Player_Settings_Depth = 1.6f; //高级设置层
	public static float Bottom_Menu_Depth = 2f; //底部菜单层
	public static float LockScreen_Depth = 2f;//锁屏
	public static float Dialog_Depth = 1.4f; // 弹窗层深度
	public static float Cursor_Depth = 1.2f;  //瞄点深度
	public static float Subtitle_Depth = 4f;//基础控制层

	/**
	 * 放大倍数
	 */
	public static float Movie_Player_Scale = 4.5f;
	public static float Player_Controler_Scale =0.4f;

	public static float Player_Settings_Scale = 0.4f-0.02f; //高级设置层
	public static float Bottom_Menu_Scale =0.6f; //底部菜单层
	public static float LockScreen_Scale = 0.5f;//锁屏
	public static float Dialog_Scale = 0.35f; // 弹窗层
	public static float Cursor_Scale = 0.3f;  //瞄点
	public static float Subtitle_Scale = 1f;//基础控制层

	/**
	 *播放页面类型
	 */
	public static final int MOVIE = 0;//在线影院
	public static final int LOCAL_MOVIE = 1;//本地影院
	public static final int PANO = 2;//在线全景
	public static final int LOCAL_PANO = 3;//本地全景

	/**
	 *选片选集类型
	 */
	public static final int EPISODE_SOURCE = 1;//选集
	public static final int MOVIE_SOURCE = 2;//选片
	public static final int NO_SOURCE = 0;//无选集，无选片
}
