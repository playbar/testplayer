package com.baofeng.mj.vrplayer.util;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.ViewConfiguration;
import android.view.Window;
import android.view.WindowManager;

import java.lang.reflect.Method;

/**
 * 像素工具类
 */
public class PixelsUtil {
	private static DisplayMetrics displayMetrics;

	public static DisplayMetrics getDisplayMetrics(Context context){
		if(displayMetrics == null){
			displayMetrics = context.getApplicationContext().getResources().getDisplayMetrics();
		}
		return displayMetrics;
	}

	/**
	 * 获取屏幕宽度
	 */
	public static int getWidthPixels(Context context){
		int widthPixels = getDisplayMetrics(context).widthPixels;
		int heightPixels = getDisplayMetrics(context).heightPixels;
		int pixels = widthPixels < heightPixels ? widthPixels : heightPixels;
		return pixels;
	}

	/**
	 * 获取屏幕高度
	 */
	public static int getHeightPixels(Context context){
		int widthPixels = getDisplayMetrics(context).widthPixels;
		int heightPixels = getDisplayMetrics(context).heightPixels;
		int pixels = widthPixels < heightPixels ? heightPixels : widthPixels;
		return pixels;
	}

	/**
	 * 将dip值转换为px值
	 */
	public static int dip2px(Context context, float dpValue) {
		return (int) (dpValue * getDisplayMetrics(context).density + 0.5f);
	}
	
	/**
	 * 将px值转换为dip值
	 */
	public static int px2dip(Context context, float pxValue) {
		return (int) (pxValue / getDisplayMetrics(context).density + 0.5f);
	}
	
	/** 
     * 将sp值转换为px值
     */  
    public static int sp2px(Context context, float spValue) {
        return (int) (spValue * getDisplayMetrics(context).scaledDensity + 0.5f);
    }


	/**
	 * 检查是否存在虚拟按键栏
	 *
	 * @param context
	 * @return
	 */
	public static boolean hasNavBar(Context context) {
		Resources res = context.getResources();
		int resourceId = res.getIdentifier("config_showNavigationBar", "bool", "android");
		if (resourceId != 0) {
			boolean hasNav = res.getBoolean(resourceId);
			// check override flag
			String sNavBarOverride = getNavBarOverride();
			if ("1".equals(sNavBarOverride)) {
				hasNav = false;
			} else if ("0".equals(sNavBarOverride)) {
				hasNav = true;
			}
			return hasNav;
		} else { // fallback
			return !ViewConfiguration.get(context).hasPermanentMenuKey();
		}
	}

	/**
	 * 判断虚拟按键栏是否重写
	 *
	 * @return
	 */
	private static String getNavBarOverride() {
		String sNavBarOverride = null;
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			try {
				Class c = Class.forName("android.os.SystemProperties");
				Method m = c.getDeclaredMethod("get", String.class);
				m.setAccessible(true);
				sNavBarOverride = (String) m.invoke(null, "qemu.hw.mainkeys");
			} catch (Throwable e) {
			}
		}
		return sNavBarOverride;
	}
	//获取NavigationBar的高度
	public static int getNavigationBarHeight(Activity activity) {
		Resources resources = activity.getResources();
		int resourceId = resources.getIdentifier("navigation_bar_height","dimen", "android");

		int height = resources.getDimensionPixelSize(resourceId);
		return height;
	}


	//获取屏幕原始尺寸高度，包括虚拟功能键高度
	public static int getDpi(Context context){
		int dpi = 0;
		WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		Display display = windowManager.getDefaultDisplay();
		DisplayMetrics displayMetrics = new DisplayMetrics();
		@SuppressWarnings("rawtypes")
        Class c;
		try {
			c = Class.forName("android.view.Display");
			@SuppressWarnings("unchecked")
            Method method = c.getMethod("getRealMetrics",DisplayMetrics.class);
			method.invoke(display, displayMetrics);
			dpi=displayMetrics.widthPixels;
		}catch(Exception e){
			e.printStackTrace();
		}
		return dpi;
	}

	public static int getMaxDpi(Context context){
		int dpi = 0;
		WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		Display display = windowManager.getDefaultDisplay();
		DisplayMetrics displayMetrics = new DisplayMetrics();
		@SuppressWarnings("rawtypes")
        Class c;
		try {
			c = Class.forName("android.view.Display");
			@SuppressWarnings("unchecked")
            Method method = c.getMethod("getRealMetrics",DisplayMetrics.class);
			method.invoke(display, displayMetrics);
			dpi=displayMetrics.widthPixels>displayMetrics.heightPixels?displayMetrics.widthPixels:displayMetrics.heightPixels;
		}catch(Exception e){
			e.printStackTrace();
		}
		return dpi;
	}

	/**
	 * 获取 虚拟按键的高度
	 * @param context
	 * @return
	 */
	public static int getBottomStatusHeight(Activity context){
		int totalHeight = getDpi(context);

		int contentHeight = getScreenWidth(context);
		return totalHeight  - contentHeight;
	}

	/**
	 * 标题栏高度
	 * @return
	 */
	public static int getTitleHeight(Activity activity){
		return  activity.getWindow().findViewById(Window.ID_ANDROID_CONTENT).getTop();
	}

	/**
	 * 获得状态栏的高度
	 *
	 * @param context
	 * @return
	 */
	public static int getStatusHeight(Context context)
	{

		int statusHeight = -1;
		try
		{
			Class<?> clazz = Class.forName("com.android.internal.R$dimen");
			Object object = clazz.newInstance();
			int height = Integer.parseInt(clazz.getField("status_bar_height")
					.get(object).toString());
			statusHeight = context.getResources().getDimensionPixelSize(height);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return statusHeight;
	}


	/**
	 * 获得屏幕高度
	 *
	 * @param
	 * @return
	 */
	public static int getScreenWidth(Activity activity)
	{
		int height = activity.getWindowManager().getDefaultDisplay().getWidth();
		return height;
	}



}