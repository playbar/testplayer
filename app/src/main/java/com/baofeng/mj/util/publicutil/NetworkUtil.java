package com.baofeng.mj.util.publicutil;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.baofeng.mj.vrplayer.ftp.ftp.Util;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.List;

/**
 * 网络信息工具类
 */
public class NetworkUtil {
	public static final int NETWORK_MOBILE_BUT_CONNECTED = -2;//mobile网络（已连接，区分不了2G，3G，4G...）
	public static final int NETWORK_UNKNOWN_BUT_CONNECTED = -1;//未识别网络（已连接）
	public static final int NETWORK_UNKNOWN_NOT_CONNECTED = 0;//未识别网络（未连接）
	public static final int NETWORK_WIFI = 1;//WiFi连接
	public static final int NETWORK_2_G = 2;//2G连接
	public static final int NETWORK_3_G = 3;//3G连接
	public static final int NETWORK_4_G = 4;//4G连接

	public static final int WIFI_LEVEL_0 = 0;//WiFi最好
	public static final int WIFI_LEVEL_1 = 1;//WiFi偏好
	public static final int WIFI_LEVEL_2 = 2;//WiFi偏差
	public static final int WIFI_LEVEL_3 = 3;//WiFi最差

	/**
	 * 获取网络
	 */
	public static int getNetwork(Context mContext) {
		return getNetwork(mContext, getActiveNetworkInfo(mContext));
	}

	/**
	 * 获取网络
	 */
	public static int getNetwork(Context mContext, NetworkInfo networkInfo) {
		try {
			if (networkInfo != null && networkInfo.isConnected() && networkInfo.getState() == NetworkInfo.State.CONNECTED) {
				int type = networkInfo.getType();
				if (type == ConnectivityManager.TYPE_WIFI) {
					return NETWORK_WIFI;
				} else if (type == ConnectivityManager.TYPE_MOBILE) {
					TelephonyManager telephonyManager = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
					int networkType = telephonyManager.getNetworkType();
					if(TelephonyManager.NETWORK_TYPE_GPRS == networkType ||
					   TelephonyManager.NETWORK_TYPE_EDGE == networkType ||
					   TelephonyManager.NETWORK_TYPE_CDMA == networkType ||
					   TelephonyManager.NETWORK_TYPE_1xRTT == networkType ||
					   TelephonyManager.NETWORK_TYPE_IDEN == networkType){
						return NETWORK_2_G;
					}else if(TelephonyManager.NETWORK_TYPE_UMTS == networkType ||
							 TelephonyManager.NETWORK_TYPE_EVDO_0 == networkType ||
							 TelephonyManager.NETWORK_TYPE_EVDO_A == networkType ||
							 TelephonyManager.NETWORK_TYPE_HSDPA == networkType ||
							 TelephonyManager.NETWORK_TYPE_HSUPA == networkType ||
							 TelephonyManager.NETWORK_TYPE_HSPA == networkType ||
							 TelephonyManager.NETWORK_TYPE_EVDO_B == networkType ||
							 TelephonyManager.NETWORK_TYPE_EHRPD == networkType ||
							 TelephonyManager.NETWORK_TYPE_HSPAP == networkType){
						return NETWORK_3_G;
					}else if(TelephonyManager.NETWORK_TYPE_LTE == networkType){
						return NETWORK_4_G;
					}else{
						return NETWORK_MOBILE_BUT_CONNECTED;
					}
				} else {
					return NETWORK_UNKNOWN_BUT_CONNECTED;
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return NETWORK_UNKNOWN_NOT_CONNECTED;
	}

	/**
	 * 获取活动的网络信息
	 * @return
	 */
	public static NetworkInfo getActiveNetworkInfo(Context mContext){
		NetworkInfo networkInfo = null;
		try {
			ConnectivityManager connectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
			networkInfo = connectivityManager.getActiveNetworkInfo();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return networkInfo;
	}

	/**
	 * true当前网络可用，false不可用
	 */
	public static boolean networkEnable(Context mContext) {
		NetworkInfo networkInfo = getActiveNetworkInfo(mContext);
		if (networkInfo != null && networkInfo.isConnected() && networkInfo.getState() == NetworkInfo.State.CONNECTED) {
			return true;
		}
		return false;
	}

	/**
	 * wifi信号强度，值是一个0到-100的区间值
	 * 0到-25表示信号最好，-25到-50表示信号偏好，-50到-75表示信号偏差，小于-75表示最差，有可能连接不上或者掉线
	 */
/*	public static int getWifiLevel() {
		WifiManager wifiManager = (WifiManager) BaseApplication.INSTANCE.getSystemService(Context.WIFI_SERVICE);
		if(wifiManager != null){
			WifiInfo wifiInfo = wifiManager.getConnectionInfo();
			if(wifiInfo != null && wifiInfo.getBSSID() != null){
				int wifiLevle = wifiInfo.getRssi();// 获得信号强度值
				if(wifiLevle > -25){
					return WIFI_LEVEL_0;
				}else if(wifiLevle <= -25 && wifiLevle > -50){
					return WIFI_LEVEL_1;
				}else if(wifiLevle <= -50 && wifiLevle > -75){
					return WIFI_LEVEL_2;
				}
			}
		}
		return WIFI_LEVEL_3;
	}*/

	/**
	 *
	 * @return 0-4
     */
	public static int getWifiLevel(Context mContext) {
		WifiManager wifiManager = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
		int wifiLevle = 0;
		if (wifiManager != null) {
			WifiInfo wifiInfo = wifiManager.getConnectionInfo();
			if (wifiInfo != null && wifiInfo.getBSSID() != null) {
				wifiLevle = wifiInfo.getRssi();// 获得信号强度值
			}

		}

		/*if (wifiLevle == Integer.MAX_VALUE) {
			return -1;
		}*/
		return calculateSignalLevel(wifiLevle, 5);
	}
	private static int calculateSignalLevel(int rssi, int numLevels) {
		if (rssi <= MIN_RSSI) {
			return 0;
		} else if (rssi >= MAX_RSSI) {
			return numLevels - 1;
		} else {
			float inputRange = (MAX_RSSI - MIN_RSSI);
			float outputRange = (numLevels - 1);
			return (int)((float)(rssi - MIN_RSSI) * outputRange / inputRange);
		}
	}
	/** Anything worse than or equal to this will show 0 bars. */
	private static final int MIN_RSSI = -100;

	/** Anything better than or equal to this will show the max bars. */
	private static final int MAX_RSSI = -55;


	/**
	 * 获取wifi强度等级
	 */
	public static int getCalculateWifiLevel(Context mContext) {
		try{
			return WifiManager.calculateSignalLevel(getWifiLevel(mContext), 5);
		}catch(Exception e){
		}
		return 0;
	}

	public static int getWifiCount(Context mContext) {
		WifiManager wifiManager = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
		List<ScanResult> scanResults = wifiManager.getScanResults();
		return scanResults == null ? 0 : scanResults.size();
	}

	/***
	 * 获取本地IP地址
	 * @return
     */
	public static String getLocalIpAddress() {
		try {
			Enumeration<NetworkInterface> en = NetworkInterface
					.getNetworkInterfaces();
			// 遍历所用的网络接口
			while (en.hasMoreElements()) {
				NetworkInterface nif = en.nextElement();// 得到每一个网络接口绑定的所有ip
				Enumeration<InetAddress> inet = nif.getInetAddresses();
				// 遍历每一个接口绑定的所有ip
				while (inet.hasMoreElements()) {
					InetAddress ip = inet.nextElement();
					if (!ip.isLoopbackAddress() && ip instanceof Inet4Address) {
						return ip.getHostAddress();
					}
				}
			}
		} catch (SocketException ex) {
			Log.e("WifiPreference", ex.toString());
			return "";
		}
		return "";
	}

	/***
	 * 获取wifi IP地址
	 * @param context
	 * @return
     */
	public static String getWifiIpAddress(Context context) {
		// 获取wifi服务
		WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);

		// 判断wifi是否开启
		if (!wifiManager.isWifiEnabled()) {
			return null;
			//wifiManager.setWifiEnabled(true);
		}
		WifiInfo wifiInfo = wifiManager.getConnectionInfo();
		int ipAddress = wifiInfo.getIpAddress();

		if(ipAddress==0)
			return null;

		return ((ipAddress & 0xff)+"."+(ipAddress>>8 & 0xff)+"."
				+(ipAddress>>16 & 0xff)+"."+(ipAddress>>24 & 0xff));
	}

	/***
	 * 判断手机是否联网
	 * @param context
	 * @return
     */
	public static boolean isNetworkConnected(Context context){
		try {
			ConnectivityManager connectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			if (connectivityManager != null) {
				NetworkInfo info = connectivityManager.getActiveNetworkInfo();
				if (info != null && info.isConnected()) {
					// 判断当前网络是否已经连接
					if (info.getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 转换网络
	 */
	public static int convertNetwork(int currentNetwork){
		if(currentNetwork == NetworkUtil.NETWORK_UNKNOWN_BUT_CONNECTED){//未识别网络（已连接）
			currentNetwork = NetworkUtil.NETWORK_WIFI;//转成WiFi连接
		} else if(currentNetwork == NetworkUtil.NETWORK_MOBILE_BUT_CONNECTED){//mobile网络（已连接，区分不了2G，3G，4G...）
			currentNetwork = NetworkUtil.NETWORK_4_G;//转成4G连接
		}
		return currentNetwork;
	}

	public static boolean getNetStatus(Context context) {
		NetworkInfo network = ((ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE))
				.getActiveNetworkInfo();
		return network != null && network.isConnected();
	}

	/**
	 * 判断是否连接上WIFI
	 * @param context
	 * @return
	 */
	public static boolean isWIFIConnected(Context context) {
		WifiManager wifiManager = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);
		boolean flag = false;
		if (wifiManager.isWifiEnabled()) {
			WifiInfo info = wifiManager.getConnectionInfo();
			if (info != null) {
				flag = info.getNetworkId() != -1;
			}
		}
		return flag;
	}


	/**
	 * Gets the local ip address
	 *
	 * @return local ip adress or null if not found
	 */
	public static InetAddress getLocalInetAddress(Context mContext) {

		// @TODO: next if block could probably be removed
		if (isWIFIConnected(mContext)) {
			WifiManager wm = (WifiManager) mContext
					.getSystemService(Context.WIFI_SERVICE);
			int ipAddress = wm.getConnectionInfo().getIpAddress();
			if (ipAddress == 0)
				return null;
			return Util.intToInet(ipAddress);
		}
		// This next part should be able to get the local ip address, but in
		// some case
		// I'm receiving the routable address
		try {
			Enumeration<NetworkInterface> netinterfaces = NetworkInterface
					.getNetworkInterfaces();
			while (netinterfaces.hasMoreElements()) {
				NetworkInterface netinterface = netinterfaces.nextElement();
				Enumeration<InetAddress> adresses = netinterface
						.getInetAddresses();
				while (adresses.hasMoreElements()) {
					InetAddress address = adresses.nextElement();
					// this is the condition that sometimes gives problems
					if (!address.isLoopbackAddress()
							&& !address.isLinkLocalAddress())
						return address;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * @author liuchuanchi @Date 2015-10-10 上午11:50:20
	 * @description:{WiFi是否可用
	 * @param context
	 * @return
	 */
	public static boolean isWifiAvailable(final Context context) {
		NetworkInfo networkInfo = ((ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE))
				.getActiveNetworkInfo();
		return (networkInfo != null && networkInfo.isConnected() && networkInfo
				.getType() == ConnectivityManager.TYPE_WIFI);
	}
}
