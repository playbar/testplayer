package com.baofeng.mj.vrplayer.util;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by liuchuanchi on 2016/8/1.
 * asset工具类
 */
public class AssetUtil {
    /**
     * 加载asset文件夹里的文件
     * @param mContext 上下文
     * @param fileName 文件名
     * @return 字符串
     */
    public static String loadAssetFileAsString(Context mContext, String fileName) {
        StringBuilder buf = new StringBuilder();

        InputStream inputStream = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader bufferedReader = null;
        try {
            inputStream = mContext.getAssets().open(fileName);
            inputStreamReader = new InputStreamReader(inputStream);
            bufferedReader = new BufferedReader(inputStreamReader);

            String str;
            boolean isFirst = true;
            while ( (str = bufferedReader.readLine()) != null ) {
                if (isFirst){
                    isFirst = false;
                }else{
                    buf.append('\n');
                }
                buf.append(str);
            }
        } catch (IOException e) {
            Log.e("loadAssetTextAsString", "Error opening asset " + fileName);
        } finally {
            try {
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
                if (inputStreamReader != null) {
                    inputStreamReader.close();
                }
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                Log.e("loadAssetTextAsString", "Error closing asset " + fileName);
            }
        }

        return buf.toString();
    }
}
