package com.baofeng.mj.business.sqlitebusiness;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

//import com.baofeng.mj.business.historybusiness.HistoryBusiness;
import com.baofeng.mj.smb.dao.SMBDaoBean;

/**
 * 数据库帮助类
 */
public class SqliteHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "mojing.db";//数据库名称
    public static final int DATABASE_VERSION = 5;//数据库版本号
    public static final String TABLE_LOCAL_VIDEO_TYPE = "local_video_type";//本地视频类型表

    public SqliteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
//        String payTable =  "create table IF NOT EXISTS pay(_id integer primary key autoincrement,mobile varchar(100),res_type integer,res_id varchar(100))";
//        db.execSQL(payTable);//创建资源已支付表
        String localVideoTypeTable =  "create table IF NOT EXISTS "+ TABLE_LOCAL_VIDEO_TYPE +"(_id integer primary key autoincrement,path varchar(300),videoType integer)";
        db.execSQL(localVideoTypeTable);//创建本地视频类型表

        String otaTable = "create table IF NOT EXISTS " +
                "ota(_id integer primary key autoincrement," +
                "version varchar(300),download_url varchar(300), " +
                "md5 varchar(300), tag varchar(300),version_desc varchar(300), version_requirement varchar(300))";
        db.execSQL(otaTable);//创建OTA表


        String PluginTable = "create table IF NOT EXISTS " +
                "plugin(_id integer primary key autoincrement," +
                "plugin_id varchar(100),status integer,plugin_version_name varchar(100),download_url varchar(300), " +
                "version_name varchar(100),apk_name varchar(100), plugin_name varchar(100),plugin_upgrade integer)";
        db.execSQL(PluginTable);//创建插件表

        creatSMBTable(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if( oldVersion==1&&newVersion==2){
            db.execSQL("DELETE FROM "+TABLE_LOCAL_VIDEO_TYPE);
           // HistoryBusiness.editLocalHistoryFile();
        }
        if(oldVersion<=2){
            String PluginTable = "create table IF NOT EXISTS " +
                    "plugin(_id integer primary key autoincrement," +
                    "plugin_id varchar(100),status integer,plugin_version_name varchar(100),download_url varchar(300), " +
                    "version_name varchar(100),apk_name varchar(100), plugin_name varchar(100))";
            db.execSQL(PluginTable);//创建插件表
        }

        if(oldVersion<=3){
            db.execSQL("Alter table plugin add column plugin_upgrade INTEGER");
        }

        if(oldVersion<=4){
            creatSMBTable(db);
        }
    }

    /**
     * 创建smb表
     * @param db
     */
    private void creatSMBTable(SQLiteDatabase db){
        String smbTable = "create table IF NOT EXISTS "
                + SMBDaoBean.TABLE_NAME + "(_id integer primary key autoincrement,"
                + SMBDaoBean.SMB_IP + " varchar(100),"
                + SMBDaoBean.SMB_NAME + " varchar(200),"
                + SMBDaoBean.SMB_USERNAME + " varchar(200),"
                + SMBDaoBean.SMB_PASSWORD + " varchar(200),"
                + SMBDaoBean.SMB_CONNECT_TIME + " varchar(50))";

        db.execSQL(smbTable);//创建smb表
    }
}
