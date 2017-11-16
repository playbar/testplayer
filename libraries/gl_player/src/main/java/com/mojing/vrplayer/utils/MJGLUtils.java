package com.mojing.vrplayer.utils;

import android.content.Context;

import com.bfmj.viewcore.render.GLScreenParams;
import com.mojing.vrplayer.activity.GLBaseActivity;


public class MJGLUtils {
/**
 * @author wanghongfang  @Date 2015-7-30 下午5:51:58
 * description: 统一调用GL线程更新页面方法
 * @param {引入参数名} {引入参数说明}  
 * @return {返回值说明}
 */
	public static void exeGLQueueEvent(GLBaseActivity activity, Runnable runnable){
		if(activity==null)
			return;
		if(activity.isFinishing())
			return;
		if(activity.getRootView()==null)
			return;
		activity.getRootView().queueEvent(runnable);
	}

    /**
     *
     * @param context 自动转成 BaseViewActivity
     * @param runnable
     */
    public static void exeGLQueueEvent(Context context, Runnable runnable){
        try{
            exeGLQueueEvent((GLBaseActivity)context,runnable);
        }
        catch(Exception e){
        e.printStackTrace();
        }
    }

    public static float GLUnitToPx(float depth){
        float xDpi = GLScreenParams.getXDpi();
        return  depth *xDpi/GLScreenParams.getDefualtDepth();
    }
}
