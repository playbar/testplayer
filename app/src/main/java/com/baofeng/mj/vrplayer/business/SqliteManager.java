package com.baofeng.mj.vrplayer.business;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.baofeng.mj.util.publicutil.VideoTypeUtil;

import java.util.HashMap;

/**
 * 数据库管理类
 */
public class SqliteManager {
    private static SqliteManager instance;//单例
    private static SqliteHelper helper;//数据库帮助类
    private static SQLiteDatabase sqLiteDatabase;//数据库实例


    private SqliteManager() {
    }

    public static synchronized SqliteManager getInstance(Context mContext) {
        if (instance == null) {
            instance = new SqliteManager();
        }
        if (helper == null) {
            helper = new SqliteHelper(mContext.getApplicationContext());
        }
        if (sqLiteDatabase == null || !sqLiteDatabase.isOpen()) {
            sqLiteDatabase = helper.getWritableDatabase();
        }
        return instance;
    }

    /**
     * 关闭数据库
     */
    public void closeSQLiteDatabase() {
        if (sqLiteDatabase != null && sqLiteDatabase.isOpen())
            sqLiteDatabase.close();
    }

    /**
     * 查询是否存在
     *
     * @param sql 查询语句
     * @return true存在，false不存在
     */
    public boolean queryIfExist(String sql) {
        boolean exist = false;
        Cursor c = null;
        try {
            c = sqLiteDatabase.rawQuery(sql, null);
            if (c != null && c.moveToFirst()) {
                exist = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (c != null) {
                c.close();
            }
        }
        return exist;
    }

    /**
     * 在local_video_type表里是否存在某条记录
     *
     * @param videoPath
     * @return
     */
    public boolean queryIfExistFromLocalVideoType(String videoPath) {
        String sql = "select _id from local_video_type where path='" + String.valueOf(videoPath.hashCode()) + "'";
        return queryIfExist(sql);
    }

    /**
     * 添加记录到local_video_type表
     *
     * @param videoPath
     * @param videoType
     * @return
     */
    public boolean addToLocalVideoType(String videoPath, int videoType) {
        if (!queryIfExistFromLocalVideoType(videoPath)) {
            sqLiteDatabase.execSQL("insert into local_video_type values(null,?,?)", new Object[]{String.valueOf(videoPath.hashCode()), videoType});
            return true;
        }
        return false;
    }

    /**
     * 更新记录到local_video_type表
     */
    public boolean updateToLocalVideoType(String videoPath, int videoType) {
        if (queryIfExistFromLocalVideoType(videoPath)) {//已存在
            ContentValues cv = new ContentValues();
            cv.put("videoType", videoType);
            return sqLiteDatabase.update("local_video_type", cv, " path=? ", new String[]{String.valueOf(videoPath.hashCode())}) > 0;
        }else{//不存在
            sqLiteDatabase.execSQL("insert into local_video_type values(null,?,?)", new Object[]{String.valueOf(videoPath.hashCode()), videoType});
            return true;
        }
    }

    /**
     * 从local_video_type表里获取videoType
     *
     * @param videoPath
     * @return
     */
    public int getFromLocalVideoType(String videoPath) {
        int videoType = VideoTypeUtil.MJVideoPictureTypeUnCreate;
        String sql = "select videoType from local_video_type where path='" + String.valueOf(videoPath.hashCode()) + "'";
        Cursor c = null;
        try {
            c = sqLiteDatabase.rawQuery(sql, null);
            if (c != null && c.moveToFirst()) {
                videoType = c.getInt(c.getColumnIndex("videoType"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (c != null) {
                c.close();
            }
        }
        return videoType;
    }

    /**
     * 从local_video_type表里获取所有videoType
     */
    public HashMap<String, Integer> getAllFromLocalVideoType() {
        HashMap<String, Integer> videoTypeMap = new HashMap<String, Integer>();
        String sql = "select * from local_video_type";
        Cursor c = null;
        try {
            c = sqLiteDatabase.rawQuery(sql, null);
            if (c != null) {
                while (c.moveToNext()) {
                    String videoPath = c.getString(c.getColumnIndex("path"));
                    int videoType = c.getInt(c.getColumnIndex("videoType"));
                    videoTypeMap.put(videoPath, videoType);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (c != null) {
                c.close();
            }
        }
        return videoTypeMap;
    }


    public boolean queryIfExistFromLocalVideoDuration(String videoPath) {
        String sql = "select _id from local_video_duration where path='" + String.valueOf(videoPath.hashCode()) + "'";
        return queryIfExist(sql);
    }

    /**
     * 添加记录到local_video_type表
     *
     * @param videoPath
     * @param videoDuration
     * @return
     */
    public boolean addToLocalVideoDuration(String videoPath, int videoDuration) {
        if (!queryIfExistFromLocalVideoDuration(videoPath)) {
            sqLiteDatabase.execSQL("insert into local_video_duration values(null,?,?)", new Object[]{String.valueOf(videoPath.hashCode()), videoDuration});
            return true;
        }
        return false;
    }


    /**
     * 从local_video_type表里获取所有videoType
     */
    public HashMap<String, Integer> getAllFromLocalVideoDuration() {
        HashMap<String, Integer> videoTypeMap = new HashMap<String, Integer>();
        String sql = "select * from local_video_duration";
        Cursor c = null;
        try {
            c = sqLiteDatabase.rawQuery(sql, null);
            if (c != null) {
                while (c.moveToNext()) {
                    String videoPath = c.getString(c.getColumnIndex("path"));
                    int videoType = c.getInt(c.getColumnIndex("videoDuration"));
                    videoTypeMap.put(videoPath, videoType);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (c != null) {
                c.close();
            }
        }
        return videoTypeMap;
    }

    /**
     * 从local_video_type表里删除某条记录
     *
     * @param videoPath
     * @return
     */
    public boolean deleteFromLocalVideoType(String videoPath) {
        if (queryIfExistFromLocalVideoType(videoPath)) {
            return sqLiteDatabase.delete("local_video_type", "path = ?", new String[]{String.valueOf(videoPath.hashCode())}) > 0;
        }
        return false;
    }

    /**
     * 从local_video_type表里删除所有记录
     * @return
     */
    public boolean deleteAllFromLocalVideoType() {
        return sqLiteDatabase.delete("local_video_type", null, null) > 0;
    }
}
