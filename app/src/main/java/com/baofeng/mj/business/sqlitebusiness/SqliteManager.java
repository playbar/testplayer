package com.baofeng.mj.business.sqlitebusiness;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

//import com.baofeng.mj.bean.PayInfo;
//import com.baofeng.mj.bean.PluginDownloadInfo;
//import com.baofeng.mj.business.downloadbusiness.DownloadResBusiness;
//import com.baofeng.mj.business.publicbusiness.BaseApplication;
import com.baofeng.mj.smb.bean.SMBDeviceBean;
import com.baofeng.mj.smb.dao.SMBDaoBean;
import com.baofeng.mj.util.publicutil.VideoTypeUtil;
//import com.baofeng.mj.util.updateutil.APKDownloadUtils;
import com.baofeng.mj.vrplayer.MyAppliaction;
//import com.baofeng.mojing.sdk.download.utils.MjDownloadErrorCode;
//import com.baofeng.mojing.sdk.download.utils.MjDownloadStatus;

import java.util.ArrayList;
import java.util.List;

/**
 * 数据库管理类
 */
public class SqliteManager {
    private static SqliteManager instance;//单例
    private static SqliteHelper helper;//数据库帮助类
    private static SQLiteDatabase sqLiteDatabase;//数据库实例

    private SqliteManager() {
    }

    public static synchronized SqliteManager getInstance() {
        if (instance == null) {
            instance = new SqliteManager();
        }
        if (helper == null) {
            helper = new SqliteHelper(MyAppliaction.getInstance());
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

//    /**
//     * 资源已支付记录加入pay表
//     *
//     * @param mobile  手机号
//     * @param payInfo
//     * @return
//     */
//    public boolean addToPay(String mobile, PayInfo payInfo) {
//        int res_type = payInfo.getRes_type();
//        String res_id = payInfo.getRes_id();
//        return addToPay(mobile, res_type, res_id);
//    }

    /**
     * 资源已支付记录加入pay表
     *
     * @param mobile   手机号
     * @param res_type 资源类型
     * @param res_id   资源id
     * @return
     */
    public boolean addToPay(String mobile, int res_type, String res_id) {
        String sql = "select _id from pay where mobile='" + mobile + "' and res_type='" + res_type + "' and res_id='" + res_id + "'";
        if (!queryIfExist(sql)) {
            sqLiteDatabase.execSQL("insert into pay values(null,?,?,?)", new Object[]{mobile, res_type, res_id});
            return true;
        }
        return false;
    }

//    /**
//     * 从pay表获取所有资源已支付记录
//     *
//     * @param mobile
//     * @return
//     */
//    public List<PayInfo> getAllFromPay(String mobile) {
//        List<PayInfo> payList = new ArrayList<PayInfo>();
//        Cursor c = null;
//        try {
//            String sql = "select * from pay where mobile='" + mobile + "' order by _id desc";
//            c = sqLiteDatabase.rawQuery(sql, null);
//            while (c != null && c.moveToNext()) {
//                PayInfo payInfo = new PayInfo();
//                payInfo.setRes_type(c.getInt(c.getColumnIndex("res_type")));
//                payInfo.setRes_id(c.getString(c.getColumnIndex("res_id")));
//                payList.add(payInfo);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            if (c != null) {
//                c.close();
//            }
//        }
//        return payList;
//    }

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

    /**
     * /**
     * 查询feedBack是否有记录
     *
     * @param id
     * @return
     */
    public boolean checkIfExistFromFeedBack(String id) {
        return queryIfExist("select count(0) from feed_back where id='" + id
                + "'");
    }

    /**
     * 往feedback表里添加数据
     *
     * @param cv
     * @return
     */
    public boolean insertFeedBack(ContentValues cv) {
        return sqLiteDatabase.insert("feed_back", "mid", cv) > 0;
    }

    /**
     * 更新feed-back记录
     *
     * @param id
     * @param cv
     * @return
     */
    public boolean updateFeedBack(String id, ContentValues cv) {
        return sqLiteDatabase.update("feed_back", cv, " id=? ", new String[]{id}) > 0;
    }


    //-----------插件列表数据库操作------

    /**
     * 从plugin表中查找是否存在Pluginid的插件
     *
     * @param pluginId
     * @return
     */
    public boolean queryIfExistFromPluginList(String pluginId) {
        String sql = "select * from plugin where plugin_id='" +pluginId + "'";
        return queryIfExist(sql);
    }

//    /**
//     * 添加记录到plugin表
//     * @return
//     */
//    public void addPlugin(PluginDownloadInfo info) {
//        if(info==null)
//            return;
////        "plugin(_id integer primary key autoincrement," +
////                "plugin_id varchar(100),status integer,plugin_version_name varchar(100),download_url varchar(300), " +
////                "version_name varchar(100),apk_name varchar(100), plugin_name varchar(100))";
//        if (!queryIfExistFromPluginList(info.getPlugin_id())) {
//            sqLiteDatabase.execSQL("insert into plugin values(null,?,?,?,?,?,?,?,?)", new Object[]{info.getPlugin_id(), info.getStatus(), info.getPlugin_version_name(), info.getDownload_url(), info.getVersion_name(), info.getApk_name(), info.getPlugin_name(),info.getPlugin_upgrade()});
//            return;
//        }else {
//            updatePlugin(info);
//        }
//
//        return;
//    }

//    /**
//     * 更新记录到plugin表
//     */
//    private void updatePlugin(PluginDownloadInfo info) {
//        if(info==null)
//           return ;
//        ContentValues cv = new ContentValues();
//        cv.put("plugin_id", info.getPlugin_id());
//        cv.put("status", info.getStatus());
//        cv.put("plugin_version_name", info.getPlugin_version_name());
//        cv.put("download_url", info.getDownload_url());
//        cv.put("version_name", info.getVersion_name());
//        cv.put("apk_name", info.getApk_name());
//        cv.put("plugin_name", info.getPlugin_name());
//        cv.put("plugin_upgrade",info.getPlugin_upgrade());
//        sqLiteDatabase.update("plugin", cv, " plugin_id=? ", new String[]{info.getPlugin_id()});
//    }

//    /**
//     * 从plugin表里获取 PluginDownloadInfo
//     * @return
//     */
//    public PluginDownloadInfo getFromLocalPluginInfo(String pluginId) {
//        String sql = "select * from plugin where plugin_id = ?";
//        PluginDownloadInfo info = null ;
//        Cursor c = null;
//        try {
//            c = sqLiteDatabase.rawQuery(sql, new String[]{pluginId});
////            info = convertCursor(c);
//            if(c!=null&&c.moveToFirst()){
//                info = convertObject(c);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            if (c != null) {
//                c.close();
//            }
//        }
//
//        return info;
//    }
//
//    private PluginDownloadInfo convertObject(Cursor c){
//        PluginDownloadInfo info = new PluginDownloadInfo();
//        info.setPlugin_id(c.getString(c.getColumnIndex("plugin_id")));
//        info.setStatus(c.getInt(c.getColumnIndex("status")));
//        info.setPlugin_version_name(c.getString(c.getColumnIndex("plugin_version_name")));
//        info.setDownload_url(c.getString(c.getColumnIndex("download_url")));
//        info.setVersion_name(c.getString(c.getColumnIndex("version_name")));
//        info.setApk_name(c.getString(c.getColumnIndex("apk_name")));
//        info.setPlugin_name(c.getString(c.getColumnIndex("plugin_name")));
//        info.setPlugin_upgrade(c.getInt(c.getColumnIndex("plugin_upgrade")));
//        return info;
//    }
//
//    private List<PluginDownloadInfo> convertCursor(Cursor c){
//        List<PluginDownloadInfo> listInfo = new ArrayList<>();
//        while (c != null && c.moveToNext()) {
//            listInfo.add(convertObject(c));
//        }
//
//        return listInfo;
//
//    }
//
//    /**
//     * 获取插件列表 （下载中  已下载  已安装）
//     * @return
//     */
//    public List<PluginDownloadInfo> queryPluginDownloadInfos(){
//        String sql = "select * from plugin";
//        Cursor c = null;
//        List<PluginDownloadInfo> infos = null;
//        try {
//            c = sqLiteDatabase.rawQuery(sql,null);
//            infos = convertCursor(c);
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            if (c != null) {
//                c.close();
//            }
//        }
//        return infos;
//    }
//
//    /**
//     * 获取下载完成的插件列表
//     * @return
//     */
//    public List<PluginDownloadInfo> queryPluginInstalled(){
//        String sql = "select * from plugin where status = ? ";
//        Cursor c = null;
//        List<PluginDownloadInfo> infos = null;
//        try {
//            c = sqLiteDatabase.rawQuery(sql,new String[]{MjDownloadStatus.COMPLETE+""});
//            infos = convertCursor(c);
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            if (c != null) {
//                c.close();
//            }
//        }
//        return infos;
//    }
//
//    /**
//     * 获取下载中和正在安装的插件列表
//     * @return
//     */
//    public List<PluginDownloadInfo> queryPluginDownloaded(){
//        String sql = "select * from plugin where status = ? or status = ? ";
//        Cursor c = null;
//        List<PluginDownloadInfo> infos = null;
//        try {
//            c = sqLiteDatabase.rawQuery(sql,new String[]{MjDownloadStatus.COMPLETE+"",PluginDownloadInfo.Installed+""});
//            infos = convertCursor(c);
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            if (c != null) {
//                c.close();
//            }
//        }
//        return infos;
//    }
    //-----------插件列表数据库操作end---


    public boolean deleteFromPlugin(String pluginid) {
        if ( queryIfExistFromPluginList(pluginid)) {
             int res  =  sqLiteDatabase.delete("plugin", " plugin_id = ?", new String[]{pluginid});
        }
        return false;
    }



    //---------------------------smb start---------------------




    public void addSMBDevice(SMBDeviceBean bean) {
        if (bean == null) {
            return;
        }
        ContentValues cv = new ContentValues();
        cv.put(SMBDaoBean.SMB_IP, bean.ip);
        cv.put(SMBDaoBean.SMB_NAME, bean.name);
        cv.put(SMBDaoBean.SMB_CONNECT_TIME, bean.connect_time);
        cv.put(SMBDaoBean.SMB_USERNAME, bean.username);
        cv.put(SMBDaoBean.SMB_PASSWORD, bean.password);
        addSMBDeviceValues(cv);

    }

    private void addSMBDeviceValues(ContentValues values) {
        try {
            if (addSMBDeviceContentValues(values)) {// 插入成功
            }else{
                updateSMBDeviceContentValues(values);// 插入不成功，更新数据库
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean addSMBDeviceContentValues(ContentValues cv) {
        try {
            int count = checkQueryCount("select count(0) from " + SMBDaoBean.TABLE_NAME + " where " + SMBDaoBean.SMB_IP + "='"
                    + cv.getAsString(SMBDaoBean.SMB_IP) + "'");
            if (count == 0) {
                long r = sqLiteDatabase.insert(SMBDaoBean.TABLE_NAME, SMBDaoBean.SMB_IP, cv);
                if (r > 0) {
                    return true;
                }
            }
        } catch (Exception e) {
        }
        return false;
    }



    private void updateSMBDeviceContentValues(ContentValues cv) {
        try {
            sqLiteDatabase.update(SMBDaoBean.TABLE_NAME, cv, SMBDaoBean.SMB_IP + " = ?",
                    new String[]{cv.getAsString(SMBDaoBean.SMB_IP)});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 删除指定的设备
     *
     * @param ip
     */
    public void deleteSMBDevice(String ip) {
        try {
            sqLiteDatabase.delete(SMBDaoBean.TABLE_NAME, SMBDaoBean.SMB_IP + " = ?",
                    new String[]{ip});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 查询SMB已连接过的设备列表
     *
     * @return
     */
    public ArrayList<SMBDeviceBean> getSMBDeviceList() {
        ArrayList<SMBDeviceBean> items = new ArrayList<>();
        Cursor c = null;
        try {
            c = sqLiteDatabase.rawQuery("select * from " + SMBDaoBean.TABLE_NAME+" order by "+SMBDaoBean.SMB_CONNECT_TIME+" desc",
                    null);
            while (c.moveToNext()) {
                items.add(creatSMBDeviceBean(c));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (c != null) {
                c.close();
            }
        }

        return items;
    }

    /**
     * 查询SMB已连接过的设备列表
     *
     * @return
     */
    public SMBDeviceBean getSMBDeviceByIp(String ip) {
        SMBDeviceBean bean = null;
        Cursor c = null;
        try {
            c = sqLiteDatabase.rawQuery("select * from " + SMBDaoBean.TABLE_NAME+" where "+SMBDaoBean.SMB_IP+"='"+ip+"'",
                    null);
            if(c!=null&&c.moveToNext()){
                bean = creatSMBDeviceBean(c);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (c != null) {
                c.close();
            }
        }

        return bean;
    }

    private SMBDeviceBean creatSMBDeviceBean(Cursor c) {
        SMBDeviceBean bean = new SMBDeviceBean();
        if (c.isNull(c.getColumnIndex(SMBDaoBean.SMB_IP)) == false) {
            bean.ip = c.getString(c.getColumnIndex(SMBDaoBean.SMB_IP));
        }
        if (c.isNull(c.getColumnIndex(SMBDaoBean.SMB_NAME)) == false) {
            bean.name = c.getString(c.getColumnIndex(SMBDaoBean.SMB_NAME));
        }
        if (c.isNull(c.getColumnIndex(SMBDaoBean.SMB_CONNECT_TIME)) == false) {
            bean.connect_time = c.getLong(c.getColumnIndex(SMBDaoBean.SMB_CONNECT_TIME));
        }
        if (c.isNull(c.getColumnIndex(SMBDaoBean.SMB_USERNAME)) == false) {
            bean.username = c.getString(c.getColumnIndex(SMBDaoBean.SMB_USERNAME));
        }
        if (c.isNull(c.getColumnIndex(SMBDaoBean.SMB_PASSWORD)) == false) {
            bean.password = c.getString(c.getColumnIndex(SMBDaoBean.SMB_PASSWORD));
        }
        return bean;
    }


    /**
     * 检查是否存在查询记录
     *
     * @return int 数量
     * @author linzanxian @Date 2015年1月22日 下午2:31:00
     * description:检查是否存在查询记录
     * @param sql sql语名 ：select count(*) from table
     */
    public int checkQueryCount(String sql) {
        int count = 0;
        Cursor c = null;
        try {
            c = sqLiteDatabase.rawQuery(sql, null);
            c.moveToNext();
            count = c.getInt(0);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (c != null) {
                c.close();
            }
        }

        return count;
    }
}
