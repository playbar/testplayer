package com.baofeng.mj.smb.util;

/**
 * Created by panxin on 2017/8/8.
 */

public class SMBViewTypeUtil {
    public static final int TYPE_SMB_ITEM = 0;//item内容
    public static final int TYPE_SMB_CONNECT = 1;//已连接标签
    public static final int TYPE_SMB_NOCONNECT = 2;//未连接标签


    public static final int TYPE_FILE_DIR = 0;//文件夹
    public static final int TYPE_FILE_VIDEO = 1;//视频
    public static final int TYPE_FILE_SRT = 2;//字幕
    public static final int TYPE_FILE_FILE = 3;//其他文件


    public static final String KEY_SMB_DEVICE_IP = "key_smb_device_ip";//存文件的key,ip地址
    public static final String KEY_SMB_DEVICE_NAME = "key_smb_device_name";//存文件的key,设备名字
    public static final String KEY_SMB_DEVICE_USERNAME = "key_smb_device_username";//存文件的key,登录账号
    public static final String KEY_SMB_DEVICE_PASSWORD = "key_smb_device_password";//存文件的key,登录密码

    public static final String KEY_SMB_SORT_TYPE = "key_smb_sort_type";//存文件的key,排序类型

    public static final String KEY_SMB_FIRST_COMEIN = "key_smb_first_comein";//存文件的key,第一次进入smb

    public static final int TYPE_SORT_TIME = 0;//时间排序
    public static final int TYPE_SORT_NAME = 1;//名字排序
    public static final int TYPE_SORT_SIZE = 2;//大小排序

}
