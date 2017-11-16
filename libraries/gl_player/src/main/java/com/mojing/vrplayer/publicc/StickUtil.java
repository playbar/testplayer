package com.mojing.vrplayer.publicc;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;

import com.baofeng.mojing.MojingSDK;
import com.baofeng.mojing.input.MojingInputManager;
import com.baofeng.mojing.input.base.MojingInputCallback;
import com.mojing.vrplayer.activity.GLBaseActivity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * ClassName: StickUtil <br/>
 * @author qiguolong
 * @date: 2015-8-26 下午3:28:05 <br/>
 * @description:
 */
public class StickUtil {
	public static String TAG = "StickUtil";
	private static StickUtil instance;
	private static Activity initContext;
	private MojingInputManager joystick;
	private static boolean isActiveClose = false; //是否主动断开
	private static List<MojingInputCallback> CallBackList = new ArrayList<MojingInputCallback>();
	private static boolean isAddProtocal = false;

	private static Set<String> joystickNames = new HashSet<>();
	private static boolean isBFMJ5Connection = false;//魔镜5代接入
	private static boolean isJoystickConnect = false;
	public static boolean getJoystickConnect() {
		return isJoystickConnect;
	}

	public static void clearJoystickNames(){
		joystickNames.clear();
	}

	public static void setJoystickName(String name) {
		if (name != null && !name.isEmpty()){
			joystickNames.add(name);
		}
	}

	public static void removeJoystickName(String name){
		if (name != null && !name.isEmpty()) {
			joystickNames.remove(name);
		}
	}

	public static Set<String> getJoystickNames() {
		return  joystickNames;
	}

	public static String getJoystickName() {
		String name = "";
		for (String n : joystickNames){
			name = n;
		}
		return  name;
	}

	public static boolean isBFMJ5Connection() {
		return isBFMJ5Connection;
	}
	public static StickUtil getInstance(Activity context){
		Log.d(TAG, "getInstance");
		if (instance == null) {
			initContext = context;
			instance = new StickUtil();
		}
		return instance;
	}
	
	private StickUtil(){
		isActiveClose = false;
		Log.d(TAG, "StickUtil");

		if(!MojingSDK.GetInitSDK()){
			MojingSDK.Init(initContext);
			MojingSDK.onDisableVrService(true);
		}
		joystick = MojingInputManager.getMojingInputManager();
		if(!isAddProtocal) {
			joystick.AddProtocal(MojingInputManager.Protocol_Bluetooth);
			isAddProtocal = true;
		}
		connect();

		//重联，连接状态大概6s才返回
		new Handler().postDelayed(new Runnable(){
			public void run() {
//				Log.d(TAG, "postDelayed"+isActiveClose+"----"+isConnected);
				if (isActiveClose) {
					return;
				}
				if (!isConnected()) {
					if (instance != null && instance.joystick != null) {
						try {
							instance.joystick.Disconnect();
						} catch (Exception e) {
							com.baofeng.mojing.MojingSDK.LogTrace("StickUtil disconnect Exception = " + e);
						}
						connect();
					}
				}
			}
		}, 7000);
	}

	private void connect(){
//		Log.d(TAG, "connect");
//		String filepath = MojingInputManager.GetDefaultInputmapFile(initContext);
//		if (filepath == null || filepath.isEmpty())
//		{
//			com.baofeng.mojing.MojingSDK.LogTrace("Can not get the deault inputmap!");
//		}
//		else
//		{
			com.baofeng.mojing.MojingSDK.LogTrace("StickUtil connect start ");
		if(joystick!=null&&initContext!=null) {
			joystick.Connect(initContext, null);
		}
			com.baofeng.mojing.MojingSDK.LogTrace("StickUtil connect end ");
//		}
	}

	public static void setCallback(Activity callback){
		 clearJoystickNames();

//		Log.d(TAG, "setCallback");
		if (instance != null && instance.joystick != null) {
//			Log.d(TAG, "setCallback true" + callback);
			com.baofeng.mojing.MojingSDK.LogTrace("StickUtil setCallback start ");
			instance.joystick.setCallback(callback);
			com.baofeng.mojing.MojingSDK.LogTrace("StickUtil setCallback end ");
		}
//		if(callback==null){
//			disconnect();
//		}
	}

	public static void disconnect(){
//		Log.d(TAG, "disconnect");
//		setCallback(initContext);
		isActiveClose = true;
		if (instance != null && instance.joystick != null) {
			try {
				com.baofeng.mojing.MojingSDK.LogTrace("StickUtil disconnect start ");
				instance.joystick.Disconnect();
				com.baofeng.mojing.MojingSDK.LogTrace("StickUtil disconnect end ");
			} catch (Exception e) {
				com.baofeng.mojing.MojingSDK.LogTrace("StickUtil disconnect Exception = "+e);
			}
//			instance.joystick.RevmoveProtocal(MojingInputManager.Protocol_Bluetooth);
//			instance.joystick.cleanManager();
//			Log.d(TAG, "RevmoveProtocal");
			instance.joystick = null;
			instance = null;
		}
		 clearJoystickNames();
		initContext = null;

	}
	
	public static boolean dispatchKeyEvent(KeyEvent event){
//		Log.d(TAG, "dispatchKeyEvent");
		MojingSDK.LogTrace("StickUtil dispatchKeyEvent ");
		if (instance != null && instance.joystick != null) {
			boolean b = instance.joystick.dispatchKeyEvent(event);
			Log.d(TAG,"instance.joystick.dispatchKeyEvent = "+b);
			return  b;
		}
		return false;
	}
	
	public static boolean dispatchGenericMotionEvent(MotionEvent event){
//		Log.d(TAG, "dispatchGenericMotionEvent " );
		MojingSDK.LogTrace("StickUtil dispatchGenericMotionEvent ");
//		Log.d(TAG," dispatchGenericMotionEvent instance = "+instance+"---instance.joystick = "+instance);
		if (instance != null && instance.joystick != null) {
			boolean b =  instance.joystick.dispatchGenericMotionEvent(event);

			Log.d(TAG,"instance.joystick.dispatchGenericMotionEvent = "+b);
			return  b;
		}
		return false;
	}

	public static boolean blutoothEnble() {
		BluetoothAdapter mAdapter = BluetoothAdapter.getDefaultAdapter();
		return mAdapter != null && mAdapter.isEnabled();
	}

	/**
	 * @author qiguolong @Date 2015-9-14 下午2:41:24
	 * @description:{遥控器是否配对
	 * @return
	 */
	public static boolean isBondBluetooth() {
		BluetoothAdapter mBluetoothAdapter = BluetoothAdapter
				.getDefaultAdapter();
		Set<BluetoothDevice> devices = mBluetoothAdapter.getBondedDevices();

		String dname = "";
		for (BluetoothDevice device : devices) {
			if (device != null) {
				dname = device.getName();
				if (!TextUtils.isEmpty(dname)) {
					if (dname.toLowerCase().startsWith("mojing")) {
						return true;
					}
				}
			}
		}
		return false;
	}

	public static void onBind(MojingInputCallback callBack){
		if(CallBackList==null){
			CallBackList = new ArrayList<MojingInputCallback>();
		}
		CallBackList.add(callBack);
	}
	public static void unBind(MojingInputCallback callback){
       if(CallBackList!=null && CallBackList.size()>0&& CallBackList.contains(callback)){
		   CallBackList.remove(callback);
	   }
	}


	public static void onMojingKeyDown(String var1, int var2){
		if(CallBackList==null || CallBackList.size()<=0)
			return;
		for(MojingInputCallback callBack:CallBackList){
			callBack.onMojingKeyDown(var1, var2);
		}
	}

	public static  void onMojingKeyUp(String var1, int var2){
		if(CallBackList==null || CallBackList.size()<=0)
			return;
		for(MojingInputCallback callBack:CallBackList){
			callBack.onMojingKeyUp(var1, var2);
		}
	}

	public static  void onMojingKeyLongPress(String var1, int var2){
		if(CallBackList==null || CallBackList.size()<=0)
			return;
		for(MojingInputCallback callBack:CallBackList){
			callBack.onMojingKeyLongPress(var1, var2);
		}
	}

	public static void onMojingMove(String var1, int var2, float var3, float var4, float var5){
		if(CallBackList==null || CallBackList.size()<=0)
			return;
		for(MojingInputCallback callBack:CallBackList){
			callBack.onMojingMove(var1, var2, var3, var4, var5);
		}
	}

	public static void onMojingMove(String var1, int var2, float var3){
		if(CallBackList==null || CallBackList.size()<=0)
			return;
		for(MojingInputCallback callBack:CallBackList){
			callBack.onMojingMove(var1,var2,var3);
		}
	}

	public static void onMojingDeviceAttached(String var1){
		if(CallBackList==null || CallBackList.size()<=0)
			return;
		for(MojingInputCallback callBack:CallBackList){
			callBack.onMojingDeviceAttached(var1);
		}
	}

	public static void onMojingDeviceDetached(String var1){
		if(CallBackList==null || CallBackList.size()<=0)
			return;
		for(MojingInputCallback callBack:CallBackList){
			callBack.onMojingDeviceDetached(var1);
		}
	}

	public static void onBluetoothAdapterStateChanged(int var1){
		if(CallBackList==null || CallBackList.size()<=0)
			return;
		for(MojingInputCallback callBack:CallBackList){
			callBack.onBluetoothAdapterStateChanged(var1);
		}
	}

	/**
	 *
	 * @param deviceName 设备名称
	 * @return 是否可连
	 * */
	public static boolean filterDeviceName(String deviceName){
		String model = Build.MODEL;
		if (deviceName.contains("msm8974-taiko-mtp-snd-card Button Jack") && "vivo Xplay3S".equals(model)) {
			return false;
		}
		if(deviceName.contains("mojing5_BL")){ //魔镜5代排除
			return false;
		}
		if(deviceName.contains("msm8939-snd-card-skuk Button Jack") && "M463C".equals(model)){
			return false;
		}
		if(deviceName.contains("msm8974-ak4961-snd-card Button Jack") && "NX506J".equals(model)){//努比亚手机
			return false;
		}
		if(deviceName.contains("msm8974-taiko-mtp-snd-card Button Jack") && "A0001".equals(model)){//一加0001型号手机，目前版本4.3，我们的app安装不上，手机系统有可能升级，屏蔽
			return false;
		}
		if(deviceName.contains("msm8974-taiko-mtp-snd-card Button Jack") && "Lenovo K920".equals(model)){ //K920
			return false;
		}
		if(deviceName.contains("mtk-kpd_4") && "PRO 6".equals(model)){ //MEIZU PRO 6
			return false;
		}
		if(deviceName.contains("mtk-kpd_12") && "OPPO R9m".equals(model)){ //OPPO R9m
			return false;
		}
		if(deviceName.contains("mtk-kpd_4") && "MX5".equals(model)){ //MX5
			return false;
		}
		if(deviceName.contains("MTK BT HID")&& "MX5".equals(model)){ //MTK BT HID
			return false;
		}

		if(deviceName.contains("mtk-kpd_3")&& "vivo X5Pro".equals(model)){ //vivo X5Pro
			return false;
		}

		if(deviceName.contains("hzl VR")){ //海之南屏蔽
			return false;
		}
		// --- Matrix820 ---
		if(deviceName.contains("qbt1000_key_input"))
			return  false;
		if(deviceName.contains("gpio-keys"))
			return  false;
//		if(deviceName.contains("mojing-motion"))
//			return  false;
		if(deviceName.contains("MojingOvernet"))
			return  false;
		if(deviceName.contains("Mojing controller"))
			return false;
		System.out.println("zl->filterDeviceName:" + deviceName);
		return true;
	}

	public void getConnectDevices(Context context){
		BluetoothAdapter bluetoothAdapter = BluetoothAdapter
				.getDefaultAdapter();
		int a2dp = bluetoothAdapter.getProfileConnectionState(BluetoothProfile.A2DP);
		int headset = bluetoothAdapter.getProfileConnectionState(BluetoothProfile.HEADSET);
		int health = bluetoothAdapter.getProfileConnectionState(BluetoothProfile.HEALTH);
		int flag = -1;
		if (a2dp == BluetoothProfile.STATE_CONNECTED) {
			flag = a2dp;
		} else if (headset == BluetoothProfile.STATE_CONNECTED) {
			flag = headset;
		} else if (health == BluetoothProfile.STATE_CONNECTED) {
			flag = health;
		}

		if (flag != -1) {
			bluetoothAdapter.getProfileProxy(context, new BluetoothProfile.ServiceListener() {

				@Override
				public void onServiceDisconnected(int profile) {
					// TODO Auto-generated method stub

				}

				@Override
				public void onServiceConnected(int profile, BluetoothProfile proxy) {
					// TODO Auto-generated method stub
					List<BluetoothDevice> mDevices = proxy.getConnectedDevices();
					if (mDevices != null && mDevices.size() > 0) {
						for (BluetoothDevice device : mDevices) {
							Log.i("W", "device name: " + device.getName());
						}
					} else {
						Log.i("W", "mDevices is null");
					}
				}
			}, flag);
		}
	}

	public static boolean isConnected() {
		return !getJoystickName().isEmpty();
	}

	/**
	 * 是不是体感手柄
	 * @return true是，false不是
	 */
	public static boolean isMotionHandle(String deviceName){
		if(TextUtils.isEmpty(deviceName)){
			return false;
		}
		deviceName = deviceName.toLowerCase();
		if(deviceName.contains("mojing-motion")){
			return true;
		}
		return false;
	}

	/**
	 * 是不是普通手柄
	 * @return true是，false不是
	 */
	public static boolean isCommonHandle(String deviceName){
		if(TextUtils.isEmpty(deviceName)){
			return false;
		}
		deviceName = deviceName.toLowerCase();
		if(deviceName.contains("mojing-motion")){
			return false;
		}
		if(deviceName.contains("mojing")){
			return true;
		}
		return false;
	}

	public static int getConnectStatus() {
		Set<String> deviceNames =  getJoystickNames();
		if ( isBFMJ5Connection() &&  getJoystickConnect()) { //魔镜5代usb连接，并且遥控器连接上

			if(deviceNames.contains(GLBaseActivity.MOTION_NAME)) {
				return 1;//体感
			} else if(deviceNames.size() > 0){
				return 2;//普通
			} else {
				return 0;//无连接
			}
		} else if (!StickUtil.blutoothEnble()) {// 蓝牙关闭
			return 0;//无连接
		} else if (!StickUtil.isBondBluetooth()) {// 蓝牙与魔镜设备未配对
			return 0;//无连接
		} else if (!StickUtil.isConnected()) {// 设备未开启或者设备休眠
			return 0;//无连接
		} else {// 已连接
			if(deviceNames.contains(GLBaseActivity.MOTION_NAME)) {
				return 1;//体感
			} else if(deviceNames.size() > 0){
				return 2;//普通
			} else {
				return 0;//无连接
			}
		}
	}
}
