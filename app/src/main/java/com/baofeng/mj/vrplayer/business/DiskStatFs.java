package com.baofeng.mj.vrplayer.business;

import android.os.StatFs;
import android.text.TextUtils;

import java.io.File;

/**
 * Created by liuchuanchi on 2016/5/5.
 */
public class DiskStatFs {
    /**
     * 获取文件总大小，可用大小，已使用大小
     */
    public static long[] getStatFsAll(String filePath) {
        if(TextUtils.isEmpty(filePath)){
            return new long[]{0, 0, 0};
        }
        if(!new File(filePath).exists()){
            return new long[]{0, 0, 0};
        }
        StatFs sf = new StatFs(filePath);
        long blockSize = sf.getBlockSize();
        long totalSize = sf.getBlockCount() * blockSize;
        long availableSize = sf.getAvailableBlocks() * blockSize;
        long usedSize = totalSize - availableSize;
        return new long[]{totalSize, availableSize, usedSize};
    }

    /**
     * 获取文件总大小
     */
    public static long getStatFsTotal(String filePath) {
        if(TextUtils.isEmpty(filePath)){
            return 0;
        }
        if(!new File(filePath).exists()){
            return 0;
        }
        StatFs sf = new StatFs(filePath);
        long blockSize = sf.getBlockSize();
        long totalSize = sf.getBlockCount() * blockSize;
        return totalSize;
    }

    /**
     * 获取文件可用大小
     */
    public static long getStatFsAvailable(String filePath) {
        if(TextUtils.isEmpty(filePath)){
            return 0;
        }
        if(!new File(filePath).exists()){
            return 0;
        }
        StatFs sf = new StatFs(filePath);
        long blockSize = sf.getBlockSize();
        long availableSize = sf.getAvailableBlocks() * blockSize;
        return availableSize;
    }

    /**
     * 获取文件已使用大小
     */
    public static long getStatFsUsed(String filePath) {
        if(TextUtils.isEmpty(filePath)){
            return 0;
        }
        if(!new File(filePath).exists()){
            return 0;
        }
        StatFs sf = new StatFs(filePath);
        long blockSize = sf.getBlockSize();
        long totalSize = sf.getBlockCount() * blockSize;
        long availableSize = sf.getAvailableBlocks() * blockSize;
        long usedSize = totalSize - availableSize;
        return usedSize;
    }
}
