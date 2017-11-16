package com.baofeng.mj.vrplayer.business.flyscreen.util;

/**
 * Created by zhaominglei on 2016/5/12.
 */
public class FlyScreenConstant {
    //发现飞屏设备
    public static final int FIND_FLY_SCREEN = 0x101;
    //获取飞屏设备文件
    public static final int GET_FILES_FROM_FLY_SCREEN = 0x102;
    //显示圆形进度条
    public static final int SHOW_PROGRESS_BAR = 0x104;
    //设备没有找到
    public static final int FLY_SCREEN_NOT_FOUND = 0x105;
    //返回设备目录列表
    public static final int BACK_TO_DEVICE_LIST = 0x106;
    //登陆密码错误
    public static final int FLY_SCREEN_LOGIN_PWD_ERROR = 0x107;
    //断开设备重新连接
    public static final int FLY_SCREEN_RECONNECT = 0x108;

    // 本地点击事件，防止快速点击
//    public static final int GOTO_DEVICE = 0x201;
//    public static final int GOTO_FOLDER = 0x202;
    //服务器断开消息
    public static final int FLY_SCREEN_SERVERCLOSED = 0x203;

    //----------------------------飞屏异常----------------------
    public static final int FLY_SCREEN_LOGIN_FAIL = 1;//登录失败
    public static final int FLY_SCREEN_LOGIN_PASSWORD_ERROR = 2;//用户名或者密码错误
    public static final int FLY_SCREEN_LOGIN_DEVICE_CONNECTED = 3;//设备已被连接
    public static final int FLY_SCREEN_LOGIN_METHOD_NOTSUPPORTED = 4;//登录方法不支持
    public static final int FLY_SCREEN_LOGIN_VERSION_NOTMATCHED = 5;//飞屏登录协议过新或过旧
    public static final int FLY_SCREEN_DEVICE_DISCONNECT = 6;//设备断开连接
    public static final int FLY_SCREEN_RESOURCE_FAIL = 7;//设备资源获取失败
    public static final int FLY_SCREEN_RESOURCE_FAIL_NOTLOGIN = 8;//因未登录获取资源失败
    public static final int FLY_SCREEN_NETWORK_ERROR = 9;//网络错误
    //----------------------------结束--------------------------
}
