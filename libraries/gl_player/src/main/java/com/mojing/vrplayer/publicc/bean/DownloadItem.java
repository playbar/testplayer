
package com.mojing.vrplayer.publicc.bean;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;


import com.mojing.vrplayer.publicc.SdcardUtil;

import java.io.Serializable;

/**
 * 任务对象
 * 
 * @author xuxiangxun
 * @modified chengzhenyu , 2013.12.28
 */
public class DownloadItem implements Serializable, Parcelable {

    private static final String TAG = "DownloadItem";

    private static final long serialVersionUID = 1L;

    public static final String BIND_APK_FLAG = "DownLoadBindApk";

    private long id;

    /**
     * 下载文件DownloadItem的类别， 1,2,3分别为APK文件下载，暴风视频下载和普通文件下载
     * 具体类型参见DownloadConstant文件中DownloadItemType接口
     */
    private int itemType = 2;

    /**
     * 下载类型 1 强制下载：3G预约状态下也可正常下载 2.预约下载 ：3G状态下任务暂停，等待网络恢复 3.正常下载
     */
    private int downloadType = 3;

    private long createTime;

    private String fileDir;

    private int predownloadedSize;

    private int threadCount = 1;

    private int preDownloadRate;

    /**
     * 下载错误代码， 1:文件下载地址破解失败 2:下载过程中因网络问题导致失败 3:破解成功后获取文件信息时失败
     * 具体类型参见DownloadConstant文件中ErrorCode接口
     */
    private long downloadErrorCode = -1;

    /**
     * 下载链接地址 DownloadItem type 为文件下载和普通文件下载时必选项
     */
    private String httpUrl;

    /**
     * site,seq,aid,channelType,childTasks,duration,playTime,
     * definition参数针对于暴风视频下载
     */
    private String site;

    private String seq;

    private String aid;

    private String channelType;


    private int duration;

    private int playTime;

    private int definition;

    private int resumeFlag = 0; // 存储暂停前的下载状态，供恢复用 设置的值应为DownloadState中的一种

    private int vid;

    private int supportBreak = 0; // 0表示支持，1表示不支持

    /** 以下属性值针对于apk下载 */

    /**
     * apk 包名
     */

    private int appId;

    private int isSelected;

    private String has;

    private boolean isCalculateRateAble;

    private String topicId;

    private int isUlike;

    private String fromTag;

    private String pageUrl;

    /**
     * 该任务是否可以在其他任务暂停或完成或失败时，按照约定排序自动开始下载 如果置true，表明可以执行下载；否则即使轮到，也不能开始下载
     */
    private boolean isAutoDownload = true;

    private int isCreateShortCut = 1;// 1:true 0;false 默认创建快捷方式;

    private int threeDVideoFlag = 0; // 0代表非3d视频，1代表3d视频

    private int pauseReason = 1;

    // 世界杯以及今后扩展apk下载时所需成员变量
    /**
     * 主下载已有变量，apk下载中代表app名。
     */
    private String title;

    /**
     * 主下载已有变量，apk下载中代表app包名。
     */
    private String packageName;

    /**
     * 主下载已有变量，apk下载中代表app icon地址。
     */
    private String imageUrl;

    /**
     * 主下载已有变量，apk下载中代表app类型，比如预下载，捆绑等等。
     */
    private int apkDownloadType;

    /**
     * 主下载已有变量，apk下载重试次数。
     */
    private int retryTime;

    /**
     * 新增变量，apk安装类型，如完成后直接安装，不安装等
     */
    private int apkInstallType;

    /**
     * 新增变量，apk描述
     */
    private String apkDescription;

    /**
     * 新增变量，apk版本号
     */
    private String apkVersionCode = "0";

    /**
     * 新增变量，apk有升级版本的版本号
     */
    private String apkUpdateVersionCode = "0";

    /**
     * 新增变量，apk安装包大小
     */
    private int apkPackageSize;

    /**
     * 新增变量，apk安装大小
     */
    private int apkInstallSize;

    /**
     * 暴风游戏中心新增变量，apk下载人数描述
     */
    private String apkDownloadNum;

    /**
     * 暴风游戏中心新增变量，apk版本名
     */
    private String apkVersionName;

    /**
     * 暴风游戏中心新增变量，下载完成时间
     */
    private long completeTime;

    private int isBd;

    private int reason;//暂停原因

    private int downloadStatus;       //下载状态
    private long totalLen;              //总字节
    private long offset;                //已经下载字节
    private int progress;               //下载进度
    private String filePathName;        //下载文件路径
    /**
     * 使用的缓存类型：1三级缓存（内存、本地、网络），2二级缓存（本地、网络），3一级缓存（网络）
     */
    private int cacheType = 2;              //下载类型

    private int is4k;//下载4k视频为1，其他为0
    private int is_panorama;//全景控制
    private int video_dimension;//全景视频播放模式
    private int pov_heading;//初始角度
    private int operation_type;//播放类型
    private String source;//资源来源
    private String play_mode;//游戏的控制方式
    private String url;//资源详情的访问接口
    private String downloadRate;
    private int downloadErrorCount;
    private long finishTime;
    private int abortCount;
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    /** 以上属性值针对于apk下载 */

    public DownloadItem(Parcel in) {
        readFromParcel(in);
    }

    public DownloadItem(int itemType) {
        setItemType(itemType);
//         createTime = System.nanoTime();
    }



    public String getPageUrl() {
        return pageUrl;
    }

    public void setPageUrl(String pageUrl) {
        this.pageUrl = pageUrl;
    }

    /**
     * 获取下载文件DownloadItem的类别， 1,2,3分别为APK文件下载，暴风视频下载和普通文件下载 * 4.p2p下载
     * 具体类型参见DownloadConstant文件中DownloadItemType接口
     */
//    public int getItemType() {
//        if (site != null && site.startsWith("bf-")) {
//            return DownloadItemType.ITEM_TYPE_P2P;
//        }
//
//        return itemType;
//    }

    /**
     * 设置下载文件DownloadItem的类别， 1,2,3分别为APK文件下载，暴风视频下载和普通文件下载 4.p2p下载
     * 具体类型参见DownloadConstant文件中DownloadItemType接口
     */
    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    /**
     * 获取文件的下载地址
     * 
     * @return
     */
    public String getHttpUrl() {
        return (httpUrl == null) ? "" : httpUrl;
    }

    /**
     * 设置文件下载地址
     * 
     * @param httpUrl
     */
    public void setHttpUrl(String httpUrl) {
        this.httpUrl = httpUrl;
    }

    /**
     * 获取视频站点，仅当DownloadItem为2，即视频类时有效，可能为null
     * 
     * @return
     */
    public String getSite() {
        return site == null ? "" : site;
    }

    /**
     * 设置视频站点，仅当DownloadItem为2，即视频类时有效，可能为null
     * 
     * @param site
     */
    public void setSite(String site) {
        this.site = site;
    }

    /**
     * 获取视频集数信息，仅当DownloadItem为2，即视频类时有效，可能为null
     * 
     * @return 视频第几集
     */
    public String getSeq() {
        return seq == null ? "" : seq;
    }

    /**
     * 设置视频集数信息，仅当DownloadItem为2，即视频类时有效，可能为null
     * 
     * @param seq
     */
    public void setSeq(String seq) {
        this.seq = seq;
    }

    /**
     * 获取视频id信息，仅当DownloadItem为2，即视频类时有效，可能为null
     * 
     * @return 视频id
     */
    public String getAid() {
        return aid == null ? "" : aid;
    }

    /**
     * 设置视频id信息，仅当DownloadItem为2，即视频类时有效，可能为null
     * 
     * @param aid
     */
    public void setAid(String aid) {
        this.aid = aid;
    }

    /**
     * 获取下载文件名，非空。 未设置时,如果httpUrl非空，则自动划分该地址最后一个“/”后字符串作为文件名
     * 
     * @return
     */
    public String getTitle() {
        if (title == null) {
            if (httpUrl != null) {
                String[] splits = httpUrl.split("/");
                title = splits[splits.length - 1];
            } else {
                title = "";
            }
        }
        return title;
    }

    /**
     * 设置下载文件名
     * 
     * @param title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * 获取文件下载路径
     * 默认为Environment.getExternalStorageDirectory().getAbsolutePath()+/baofeng
     * /download
     * 
     * @return
     */
    public String getFileDir() {
        return fileDir == null ? SdcardUtil.getDefaultDownLoadPath() : fileDir;
    }

    /**
     * 设置文件下载路径
     * 
     * @param fileDir
     */
    public void setFileDir(String fileDir) {
        this.fileDir = fileDir;
    }

    /**
     * 获取下载的创建时间,默认为创建下载任务创建时的毫秒数
     * 
     * @return
     */
    public long getCreateTime() {
        return createTime;
    }

    /**
     * 设置下载的创建时间
     * 
     * @param createTime
     */
    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }


    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getPlayTime() {
        return playTime;
    }

    public void setPlayTime(int playTime) {
        this.playTime = playTime;
    }

    /**
     * 获取下载分类， 1:订阅 2:预约下载 3:强制下载 具体类型参见DownloadConstant文件中DownloadType接口
     */
    public int getDownloadType() {
        return downloadType;
    }

    /**
     * 设置下载类别， 1:订阅 2:预约下载 3:强制下载 具体类型参见DownloadConstant文件中DownloadType接口
     */
    public void setDownloadType(int downloadType) {
        this.downloadType = downloadType;
    }

    public int getDefinition() {
        return definition;
    }

    public void setDefinition(int definition) {
        this.definition = definition;
    }

    public String getChannelType() {
        return channelType == null ? "4" : channelType;
    }

    public void setChannelType(String channelType) {
        this.channelType = channelType;
    }

    /**
     * 获取文件下载线程数，默认为1
     * 
     * @return
     */
    public int getThreadCount() {
        return threadCount;
    }

    /**
     * 设置文件下载线程数
     * 
     * @param threadCount
     */
    public void setThreadCount(int threadCount) {
        this.threadCount = threadCount;
    }

    /**
     * 获取下载失败代码，默认为-1，表示没有错误发生 1:文件下载地址破解失败 2:下载过程中因网络问题导致失败 3:破解成功后获取文件信息时失败
     * 具体类型参见DownloadConstant文件中ErrorCode接口
     * 
     * @return
     */
    public long getDownloadErrorCode() {
        return downloadErrorCode;
    }

    /**
     * 设置下载失败代码，默认为-1，表示没有错误发生 1:文件下载地址破解失败 2:下载过程中因网络问题导致失败 3:破解成功后获取文件信息时失败
     * 具体类型参见DownloadConstant文件中ErrorCode接口
     * 
     * @param downloadErrorCode
     */
    public void setDownloadErrorCode(long downloadErrorCode) {
        this.downloadErrorCode = downloadErrorCode;
    }

    /**
     * 获取已重试的次数
     * 
     * @return
     */
    public int getRetryTime() {
        return retryTime;
    }

    /**
     * 设置已重试次数
     * 
     * @param retryTime
     */
    public void setRetryTime(int retryTime) {
        this.retryTime = retryTime;
    }

    /**
     * 存储暂停前的下载状态，供恢复用
     * 
     * @return
     */
    public int getResumeFlag() {
        return resumeFlag;
    }

    /**
     * 存储暂停前的下载状态，供恢复用 设置的值应为DownloadState中的一种
     * 
     * @param resumeFlag
     */
    public void setResumeFlag(int resumeFlag) {
        this.resumeFlag = resumeFlag;
    }

    public int getVid() {
        return vid;
    }

    public void setVid(int vid) {
        this.vid = vid;
    }

    /**
     * 0表示支持，1表示不支持
     * 
     * @return
     */
    public int getSupportBreak() {
        return supportBreak;
    }

    public void setSupportBreak(int supportBreak) {
        this.supportBreak = supportBreak;
    }

    public String getPackageName() {

        return packageName == null ? "" : packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public int getAppId() {
        return appId;
    }

    public void setAppId(int appId) {
        this.appId = appId;
    }

    /**
     * ApkFromType
     * 
     * @return
     */
    public String getAppFromType() {
        return fromTag == null ? "" : fromTag;
    }

    public void setAppFromType(String fromTag) {
        this.fromTag = fromTag;
    }

    public boolean isSelected() {
        return isSelected > 0;
    }

    public void setSelected(boolean isSelected) {
        this.isSelected = 1;
    }

    public void setForceDown(boolean b) {

    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String getHas() {
        return has;
    }

    public void setHas(String has) {
        this.has = has;
    }

    /**
     * 该任务是否可以在其他任务暂停或完成或失败时，按照约定排序自动开始下载 如果置true，表明可以执行下载；否则即使轮到，也不能开始下载
     * 该变量的设计目的在于防止批量暂停任务时，因时序原因干扰自动下载逻辑 详情可询问 chengzhenyu
     */
    public boolean isAutoDownload() {
        return isAutoDownload;
    }

    /**
     * 设置该任务是否可以在其他任务暂停或完成或失败时，按照约定排序自动开始下载 如果置true，表明可以执行下载；否则即使轮到，也不能开始下载
     * 该变量的设计目的在于防止批量暂停任务时，因时序原因干扰自动下载逻辑 详情可询问 chengzhenyu
     */
    public void setAutoDownload(boolean isAutoDownload) {
        this.isAutoDownload = isAutoDownload;
    }

    public int getReason() {
        return reason;
    }

    public void setReason(int reason) {
        this.reason = reason;
    }

    public int getDownloadState() {
        return downloadStatus;
    }

    public void setDownloadStatus(int downloadStatus) {
        this.downloadStatus = downloadStatus;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public long getOffset() {
        return offset;
    }

    public void setOffset(long offset) {
        this.offset = offset;
    }

    public long getTotalLen() {
        return totalLen;
    }

    public void setTotalLen(long totalLen) {
        this.totalLen = totalLen;
    }

    public String getFilePathName() {
        return filePathName;
    }

    public void setFilePathName(String filePathName) {
        this.filePathName = filePathName;
    }

    public int getCacheType() {
        return cacheType;
    }

    public void setCacheType(int cacheType) {
        this.cacheType = cacheType;
    }

    public boolean isVideoType() {
        return this.itemType == 2 || this.itemType == 4;
    }
    public int getIs4k() {
        return is4k;
    }

    public void setIs4k(int is4k) {
        this.is4k = is4k;
    }

    public int getIs_panorama() {
        return is_panorama;
    }

    public void setIs_panorama(int is_panorama) {
        this.is_panorama = is_panorama;
    }

    public int getVideo_dimension() {
        return video_dimension;
    }

    public void setVideo_dimension(int video_dimension) {
        this.video_dimension = video_dimension;
    }

    public int getPov_heading() {
        return pov_heading;
    }

    public void setPov_heading(int pov_heading) {
        this.pov_heading = pov_heading;
    }

    public int getOperation_type() {
        return operation_type;
    }

    public void setOperation_type(int operation_type) {
        this.operation_type = operation_type;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getPlay_mode() {
        return play_mode;
    }

    public void setPlay_mode(String play_mode) {
        this.play_mode = play_mode;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public long getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(long finishTime) {
        this.finishTime = finishTime;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(site);
        dest.writeString(seq);
        dest.writeString(aid);
        dest.writeString(title);
        dest.writeString(channelType);
        dest.writeString(fileDir);
        dest.writeString(httpUrl);
        dest.writeString(packageName);
        dest.writeInt(apkDownloadType);
        dest.writeInt(isCreateShortCut);
        dest.writeString(has);
        dest.writeString(pageUrl);
        dest.writeLong(id);
        dest.writeInt(duration);
        dest.writeInt(playTime);
        dest.writeInt(downloadType);
        dest.writeInt(definition);
        dest.writeInt(threadCount);
        dest.writeLong(createTime);
        dest.writeLong(downloadErrorCode);
        dest.writeInt(preDownloadRate);
        dest.writeInt(predownloadedSize);
        dest.writeInt(itemType);
        dest.writeInt(retryTime);
        dest.writeInt(resumeFlag);
        dest.writeInt(supportBreak);
        dest.writeInt(appId);
        dest.writeInt(isSelected);
        dest.writeString(topicId);
        dest.writeInt(isUlike);
        dest.writeInt(threeDVideoFlag);
        dest.writeInt(pauseReason);
        dest.writeString(fromTag);
        dest.writeString(imageUrl);
        dest.writeString(apkDescription);
        dest.writeString(apkVersionCode);
        dest.writeString(apkUpdateVersionCode);
        dest.writeString(apkVersionName);
        dest.writeString(apkDownloadNum);
        dest.writeInt(apkPackageSize);
        dest.writeInt(apkInstallSize);
        dest.writeInt(apkInstallType);
        dest.writeLong(completeTime);
        dest.writeInt(vid);
        dest.writeInt(isBd);
        dest.writeInt(reason);
        dest.writeInt(downloadStatus);
        dest.writeLong(totalLen);
        dest.writeLong(offset);
        dest.writeInt(progress);
        dest.writeString(filePathName);
        dest.writeInt(cacheType);
        dest.writeInt(is4k);
        dest.writeInt(is_panorama);
        dest.writeInt(video_dimension);
        dest.writeInt(pov_heading);
        dest.writeInt(operation_type);
        dest.writeString(source);
        dest.writeString(play_mode);
        dest.writeString(url);
        dest.writeString(downloadRate);
        dest.writeInt(downloadErrorCount);
        dest.writeLong(finishTime);
        dest.writeInt(abortCount);
    }

    private void readFromParcel(Parcel in) {
        site = in.readString();
        seq = in.readString();
        aid = in.readString();
        title = in.readString();
        channelType = in.readString();
        fileDir = in.readString();
        httpUrl = in.readString();
        packageName = in.readString();
        apkDownloadType = in.readInt();
        isCreateShortCut = in.readInt();
        has = in.readString();
        pageUrl = in.readString();
        id = in.readLong();
        duration = in.readInt();
        playTime = in.readInt();
        downloadType = in.readInt();
        definition = in.readInt();
        threadCount = in.readInt();
        createTime = in.readLong();
        downloadErrorCode = in.readLong();
        preDownloadRate = in.readInt();
        predownloadedSize = in.readInt();
        itemType = in.readInt();
        retryTime = in.readInt();
        resumeFlag = in.readInt();
        supportBreak = in.readInt();
        appId = in.readInt();
        isSelected = in.readInt();
        topicId = in.readString();
        isUlike = in.readInt();
        threeDVideoFlag = in.readInt();
        pauseReason = in.readInt();
        fromTag = in.readString();
        imageUrl = in.readString();
        apkDescription = in.readString();
        apkVersionCode = in.readString();
        apkUpdateVersionCode = in.readString();
        apkVersionName = in.readString();
        apkDownloadNum = in.readString();
        apkPackageSize = in.readInt();
        apkInstallSize = in.readInt();
        apkInstallType = in.readInt();
        completeTime = in.readLong();
        vid = in.readInt();
        isBd = in.readInt();
        reason = in.readInt();
        downloadStatus = in.readInt();
        totalLen = in.readLong();
        offset = in.readLong();
        progress = in.readInt();
        filePathName = in.readString();
        cacheType = in.readInt();
        is4k = in.readInt();
        is_panorama = in.readInt();
        video_dimension = in.readInt();
        pov_heading = in.readInt();
        operation_type = in.readInt();
        source = in.readString();
        play_mode = in.readString();
        url = in.readString();
        downloadRate = in.readString();
        downloadErrorCount = in.readInt();
        finishTime = in.readLong();
        abortCount = in.readInt();
    }

    public static final Creator<DownloadItem> CREATOR = new Creator<DownloadItem>() {
        public DownloadItem createFromParcel(Parcel in) {
            return new DownloadItem(in);
        }

        public DownloadItem[] newArray(int size) {
            return new DownloadItem[size];
        }
    };

    public void calculateRatePerSecond() {
        long downloadedSize = getOffset();
        int size = (int)(downloadedSize - predownloadedSize);
        int speed = size / 1024;
        predownloadedSize = (int)downloadedSize;
        if (speed < 0) {
            speed = 0;
        }
        if (isCalculateRateAble) {
            if (speed > 10 * preDownloadRate && speed > 1024) {
                setDownloadRate(preDownloadRate + "KB/s");
            } else {
                setDownloadRate(speed + "KB/s");
            }
        }

        preDownloadRate = speed;
    }
    /**
     * 获取文件下载速率
     *
     * @return
     */
    public String getDownloadRate() {
        if (downloadRate == null) {
            return "";
        }
        return downloadRate;
    }

    public void setDownloadRate(String downloadRate) {
        this.downloadRate = downloadRate;
    }
    public boolean isCalculateRateAble() {
        return isCalculateRateAble;
    }

    public void setCalculateRateAble(boolean isCalculateRateAble) {
        this.isCalculateRateAble = isCalculateRateAble;
    }
    public String getTopicId() {
        return this.topicId;
    }

    public void setTopicId(String topicId) {
        this.topicId = topicId;
    }

    public boolean isUlike() {
        return this.isUlike == 1;
    }

    public void setUlike(boolean isUlike) {
        this.isUlike = isUlike ? 1 : 0;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getApkDownloadType() {
        return apkDownloadType;
    }

    public void setApkDownloadType(int apkDownloadType) {
        this.apkDownloadType = apkDownloadType;
    }

    public boolean isCreateShortCut() {
        return isCreateShortCut == 1;
    }

    public void setCreateShortCut(boolean isCreateShortCut) {
        this.isCreateShortCut = isCreateShortCut ? 1 : 0;
    }

    /**
     * 获取视频3D标志位
     * 
     * @return 0 代表非3d ，1代表3d
     */
    public int getThreeDVideoFlag() {
        return threeDVideoFlag;
    }

    /**
     * 设置视频3D标志位
     * 
     * @param threeDVideoFlag 0 代表非3d ，1代表3d
     */
    public void setThreeDVideoFlag(int threeDVideoFlag) {
        this.threeDVideoFlag = threeDVideoFlag;
    }


    public int getPauseReason() {
        return pauseReason;
    }

    public void setPauseReason(int pauseReason) {
        this.pauseReason = pauseReason;
    }

    public String getApkDescription() {
        return apkDescription;
    }

    public void setApkDescription(String apkDescription) {
        this.apkDescription = apkDescription;
    }

    public String getApkVersionCode() {
        return apkVersionCode;
    }

    public void setApkVersionCode(String apkVersionCode) {
        this.apkVersionCode = apkVersionCode;
    }

    public String getApkUpdateVersionCode() {
        return apkUpdateVersionCode;
    }

    public void setApkUpdateVersionCode(String apkUpdateVersionCode) {
        this.apkUpdateVersionCode = apkUpdateVersionCode;
    }

    public int getApkPackageSize() {
        return apkPackageSize;
    }

    public void setApkPackageSize(int apkPackageSize) {
        this.apkPackageSize = apkPackageSize;
    }

    public int getApkInstallSize() {
        return apkInstallSize;
    }

    public void setApkInstallSize(int apkInstallSize) {
        this.apkInstallSize = apkInstallSize;
    }

    public int getApkInstallType() {
        return apkInstallType;
    }

    public void setApkInstallType(int apkInstallType) {
        this.apkInstallType = apkInstallType;
    }

    public String getApkDownloadNum() {
        return apkDownloadNum;
    }

    public void setApkDownloadNum(String apkDownloadNum) {
        this.apkDownloadNum = apkDownloadNum;
    }

    public int getIsBd() {
        return isBd;
    }

    public void setIsBd(int isBd) {
        this.isBd = isBd;
    }

    public String getApkVersionName() {
        return apkVersionName;
    }

    public void setApkVersionName(String apkVersionName) {
        this.apkVersionName = apkVersionName;
    }

    public long getCompleteTime() {
        return completeTime;
    }

    public void setCompleteTime(long completeTime) {
        this.completeTime = completeTime;
    }

    public int getDownloadErrorCount() {
        return downloadErrorCount;
    }

    public void setDownloadErrorCount(int downloadErrorCount) {
        this.downloadErrorCount = downloadErrorCount;
    }


    public int getAbortCount() {
        return abortCount;
    }

    public void setAbortCount(int abortCount) {
        this.abortCount = abortCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DownloadItem that = (DownloadItem) o;

        return title.equals(that.title);

    }

    @Override
    public int hashCode() {
        if(TextUtils.isEmpty(title)){
            return  0;
        }

        return title.hashCode();
    }

}
