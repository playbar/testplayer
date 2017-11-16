package com.baofeng.mj.vrplayer.bean;

import java.io.File;
import java.io.Serializable;

/**
 * Created by liuchuanchi on 2016/5/18.
 * 根目录file
 */
public class DirFile implements Serializable {
    private static final long serialVersionUID = -8805330146064859590L;
    private String dirName;//根目录名
    private File dirFile;//根目录文件
    private long totalSize;//文件总大小
    private long usedSize;//文件已使用大小
    private long avilableSize;//文件可用大小



    public String getDirName() {
        return dirName;
    }

    public void setDirName(String dirName) {
        this.dirName = dirName;
    }

    public File getDirFile() {
        return dirFile;
    }

    public void setDirFile(File dirFile) {
        this.dirFile = dirFile;
    }

    public long getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(long totalSize) {
        this.totalSize = totalSize;
    }

    public long getUsedSize() {
        return usedSize;
    }

    public void setUsedSize(long usedSize) {
        this.usedSize = usedSize;
    }

    public long getAvilableSize() {
        return avilableSize;
    }

    public void setAvilableSize(long avilableSize) {
        this.avilableSize = avilableSize;
    }
}
