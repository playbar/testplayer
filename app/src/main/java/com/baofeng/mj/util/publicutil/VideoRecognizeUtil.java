package com.baofeng.mj.util.publicutil;

import android.graphics.Bitmap;

/**
 * Created by liuchuanchi on 2016/6/27.
 * 视频识别工具类
 */
public class VideoRecognizeUtil {
    static {
        System.loadLibrary("VideoClassifier");
    }

    /**
     * 获取视频类型
     */

    public static native int getVideoType(Bitmap bitmap);
}
