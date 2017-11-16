package com.baofeng.mj.vrplayer.business;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 数据库帮助类
 */
public class SqliteHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "mojing_vr_player.db";//数据库名称
    public static final int DATABASE_VERSION = 2;//数据库版本号
    public static final String TABLE_LOCAL_VIDEO_TYPE = "local_video_type";//本地视频类型的数据表
    public static final String TABLE_LOCAL_VIDEO_Duration = "local_video_duration";//本地视频类型的数据表

    public SqliteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String tableLocalVideoType =  "create table IF NOT EXISTS "+ TABLE_LOCAL_VIDEO_TYPE +"(_id integer primary key autoincrement,path varchar(300),videoType integer)";
        db.execSQL(tableLocalVideoType);//创建本地视频类型的数据表

        String tableLocalVideoDuration =  "create table IF NOT EXISTS "+ TABLE_LOCAL_VIDEO_Duration +"(_id integer primary key autoincrement,path varchar(300),videoDuration INTEGER)";
        db.execSQL(tableLocalVideoDuration);//创建本地视频类型的数据表
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
