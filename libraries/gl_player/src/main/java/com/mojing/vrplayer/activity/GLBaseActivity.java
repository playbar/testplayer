package com.mojing.vrplayer.activity;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.WindowManager;

import com.baofeng.mojing.MojingSDK;
import com.baofeng.mojing.MojingSDKReport;
import com.baofeng.mojing.MojingSDKServiceManager;
import com.baofeng.mojing.input.base.MojingInputCallback;
import com.baofeng.mojing.input.base.MojingKeyCode;
import com.baofeng.mojing.sdk.glhelper.AstcLoader;
import com.baofeng.mojing.sdk.glhelper.Texture;
import com.baofeng.mojing.sdk.glhelper.TextureLoader;
import com.baofeng.mojing.sensor.MojingSDKSensorManager;
import com.bfmj.viewcore.render.GLColor;
import com.bfmj.viewcore.util.GLBlendTexture;
import com.bfmj.viewcore.util.GLFocusUtils;
import com.bfmj.viewcore.view.BaseViewActivity;
import com.bfmj.viewcore.view.GLCursorView;
import com.bfmj.viewcore.view.GLPanoView;
import com.bfmj.viewcore.view.GLTextView;
import com.bfmj.viewcore.view.GLViewPage;
import com.google.vr.sdk.base.AndroidCompat;
import com.google.vr.sdk.controller.Controller;
import com.google.vr.sdk.controller.ControllerManager;
import com.google.vr.sdk.controller.Orientation;
import com.mojing.sdk.pay.widget.mudoles.Glass;
import com.mojing.sdk.pay.widget.mudoles.Manufacturer;
import com.mojing.sdk.pay.widget.mudoles.ManufacturerList;
import com.mojing.sdk.pay.widget.mudoles.Product;
import com.mojing.vrplayer.R;
import com.mojing.vrplayer.bean.VideoModeType;
import com.mojing.vrplayer.interfaces.IResetLayerCallBack;
import com.mojing.vrplayer.page.BasePlayerPage;
import com.mojing.vrplayer.publicc.MotionInputCallback;
import com.mojing.vrplayer.publicc.SettingSpBusiness;
import com.mojing.vrplayer.publicc.StickUtil;
import com.mojing.vrplayer.utils.GLConst;
import com.mojing.vrplayer.utils.HeadControlUtil;
import com.mojing.vrplayer.utils.JoystickUitlManager;
import com.mojing.vrplayer.utils.MJGLUtils;
import com.mojing.vrplayer.view.ResetLayer;
import com.storm.smart.common.utils.LogHelper;

import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;


public class GLBaseActivity extends BaseViewActivity implements MojingInputCallback, MotionInputCallback {
	public static final String MOTION_NAME = "体感手柄";
	public static final int SCENE_TYPE_CINEMA = 0;
	public static final int SCENE_TYPE_HOME = 1;
	public static final int SCENE_TYPE_OUTCINEMA = 2;

	private int mSceneType = -1;

	private HeadControlUtil mHeadControlCursor;
	private GLPanoView mSkyboxView;
	private ResetLayer mResetView;
	private Timer timer ;
	private BlueToothBroadCast blueToothBroadCast;
	private ControllerManager mControllerManager;
	private Controller mController;
	private GLCursorView mMotionCursorView;
	public boolean isLockScreen = false;
	public boolean isActivityPause = false;
	private MotionEventListener listener;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		super.onCreate(savedInstanceState);
		//
//		getRootView().setMultiThread(true);
//		getRootView().setTimeWarp(true);

		isLockScreen = SettingSpBusiness.getInstance(this.getApplicationContext()).getPlayerLockScreen();
		//解决体感手柄断开，焦点事件不生效的问题
		GLFocusUtils.setControlMode(GLFocusUtils.MODE_HEAD_CONTROL);
		GLFocusUtils.setMotionHeadView(null);
		mSkyboxView = new GLPanoView(GLBaseActivity.this);
		getRootView().addView(mSkyboxView);
		//显示头控
		mHeadControlCursor = new HeadControlUtil(this);
		mHeadControlCursor.setX( 1160);
		mHeadControlCursor.setY( 1160);
//			mHeadControlCursor.setLayoutParams(80, 80);
//			mHeadControlCursor.setImage(R.drawable.cursor_normal);
		mHeadControlCursor.setDepth(GLConst.Cursor_Depth,GLConst.Cursor_Scale);
		getRootView().addView(mHeadControlCursor);
//		showCursorView();
		hideCursorView();

		listener = new MotionEventListener();
		mControllerManager = new ControllerManager(this, listener);
		mController = mControllerManager.getController();
		mController.setEventListener(listener);
		AndroidCompat.setVrModeEnabled(this, true);

		//显示复位菜单
		mResetView = new ResetLayer(this);
		mResetView.setDepth(GLConst.Bottom_Menu_Depth,GLConst.Bottom_Menu_Scale);
//		mResetLayer.addView(mResetView);
		getRootView().addView(mResetView);
		hideResetLayerView();

		StickUtil.getInstance(this);
		StickUtil.setCallback(this);
		blueToothBroadCast = new BlueToothBroadCast();

		GLBlendTexture.getInstance().load(R.raw.bigmojingtexturebaker_json, R.raw.bigmojingtexturebaker_astc, R.raw.bigmojingtexturebaker_ktx);
		GLBlendTexture.getInstance().load(R.raw.mojingtexturebaker_json, R.raw.mojingtexturebaker_astc, R.raw.mojingtexturebaker_ktx);
	}

	/**
	 * 显示复位图标
	 */
	public void showResetLayerView() {
        if(((MjVrPlayerActivity)this).mCurPlayMode== VideoModeType.PLAY_MODE_VR) {
			if (StickUtil.getConnectStatus() == 1) return;
		MJGLUtils.exeGLQueueEvent(this, new Runnable() {
			@Override
			public void run() {
				if (mResetView != null){
					mResetView.setVisible(true);
				}
			}
		});
		}
	}

	public void showResetView() {
		if(StickUtil.getConnectStatus() == 1) return;
		if (mResetView != null){
			mResetView.showChildView();
		}
	}

	public boolean isRestViewVisiable(){
		if(mResetView!=null){
			return mResetView.isShowing();
		}
		return false;
	}

	/**
	 * 隐藏复位图标
	 */
	public void hideResetLayerView() {
//		if(((MjVrPlayerActivity)this).mCurPlayMode== VideoModeType.PLAY_MODE_VR) {
//			if (StickUtil.getConnectStatus() == 1) return;
//		}
		MJGLUtils.exeGLQueueEvent(this, new Runnable() {
			@Override
			public void run() {
				if (mResetView != null ) {
					mResetView.setVisible(false);
				}
			}
		});

	}

	public void hideResetView() {
		if(StickUtil.getConnectStatus() == 1) return;
		MJGLUtils.exeGLQueueEvent(this, new Runnable() {
			@Override
			public void run() {
				if (mResetView != null){
					mResetView.hideChildView();
				}
			}
		});

	}

	public void fixedResetView(boolean isFixed){
		if (mResetView!=null){
//			mResetView.setFixed(isFixed);
			mResetView.setResetFixed(isFixed);

		}
	}

	/**
	 * 重新初始化视角
	 */
	public void initHeadView() {
		MJGLUtils.exeGLQueueEvent(this, new Runnable() {
			@Override
			public void run() {
				getRootView().initHeadView();
			}
		});

	}

	/**
	 * 显示天空盒场景
	 * @param
     */
	public synchronized void showSkyBox(final int type){
		getRootView().queueEvent(new Runnable() {
			@Override
			public void run() {
				if(null != mSkyboxView) {
					mSkyboxView.reset();
				}

				mSkyboxView.setSceneType(GLPanoView.SCENE_TYPE_SPHERE);//3D场景

				TextureLoader.TextureType texType = TextureLoader.TextureType.TEXTURE_TYPE_ASTC;
				int format = AstcLoader.GL_COMPRESSED_RGBA_ASTC_6x6_KHR;
				boolean isSurportAstc = Texture.isCompressedTextureFormatSupported(format); //是否支持

				String leftId, rightId;

				// 默认18m
				if (isSurportAstc){
					leftId = "play_cinema_left";
					rightId = "play_cinema_right";
				} else {
					leftId = "play_cinema_left"; //"play_cinema_left_k";
					rightId = "play_cinema_right"; // R.raw.play_cinema_right_k;
					texType = TextureLoader.TextureType.TEXTURE_TYPE_KTX;
					format = 0;
				}


				if(type == SCENE_TYPE_HOME) { //6m
					if (isSurportAstc){
						leftId = "play_home_left";
						rightId = "play_home_right";
					} else {
						leftId = "play_home_left";////
						rightId = "play_home_right";////
					}
				} else if(type == SCENE_TYPE_OUTCINEMA) { //6m
					if (isSurportAstc){
						leftId = "play_outcinema_left";
						rightId = "play_outcinema_right";
					} else {
						leftId = "play_outcinema_left";//
						rightId = "play_outcinema_left";//
					}
				}

				//设置右边场景
				mSkyboxView.setLeftImage(leftId);
				//设置左边场景
				mSkyboxView.setImage(rightId);

				SettingSpBusiness.getInstance(GLBaseActivity.this.getApplicationContext()).setSkyboxIndex(type);
				if(null != mSkyboxView) {
					mSkyboxView.setVisible(true);
				}
//				setSkyboxFixed(isLockScreen);
			}
		});


	}

	public void setSkyboxFixed(final boolean fixed){
		getRootView().queueEvent(new Runnable() {
			@Override
			public void run() {
				if(mSkyboxView!=null) {
					mSkyboxView.setFixed(fixed);
				}
			}
		});

	}

	/**
	 * 隐藏天空盒场景
	 */
	public void hideSkyBox(){

		getRootView().queueEvent(new Runnable() {
			@Override
			public void run() {
				if(null != mSkyboxView) {
					mSkyboxView.setVisible(false);
				}
			}
		});
	}

	/**
	 * 隐藏天空盒场景
	 */
	public void showSkyBox(){
		if(((MjVrPlayerActivity)this).isLockScreen){
			return;
		}
		getRootView().queueEvent(new Runnable() {
			@Override
			public void run() {
				if(null != mSkyboxView) {
					mSkyboxView.setVisible(true);
				}
			}
		});
	}


	@Override
	public void showCursorView() {
//		LogHelper.d("cursor","--------showCursorView");
		if(((MjVrPlayerActivity)this).mCurPlayMode== VideoModeType.PLAY_MODE_VR) {
			if (StickUtil.getConnectStatus() == 1) return;
			MJGLUtils.exeGLQueueEvent(this, new Runnable() {
				@Override
				public void run() {
					if (mHeadControlCursor != null) {
						cancelHideCursorViewTimer();
						if(null == mMotionCursorView || !mMotionCursorView.isVisible()) {
							mHeadControlCursor.setVisible(true);
						}
					}
				}
			});
		}

	}

	public void setCursorFixed(boolean fixed){
		if (mHeadControlCursor != null ) {
			mHeadControlCursor.setFixed(fixed);
		}
	}

    /**
     * 立刻隐藏头控焦点图标
     */
	@Override
	public void hideCursorView() {
//		LogHelper.d("cursor","--------hideCursorView");
//		if(((MjVrPlayerActivity)this).mCurPlayMode== VideoModeType.PLAY_MODE_VR) {
//			if (StickUtil.getConnectStatus() == 1) return;
//		}
		MJGLUtils.exeGLQueueEvent(this, new Runnable() {
			@Override
			public void run() {
				if (mHeadControlCursor != null ) {
					mHeadControlCursor.setVisible(false);
				}
			}
		});

	}

	/**
	 * 隐藏头控焦点图标(2S后消失)
	 */
	public void hideCursorView2() {
//		if(StickUtil.getConnectStatus() == 1) return;
		if (mHeadControlCursor != null ) {
			setDelayVisiable(2*1000);
		}
	}

	public void showMotionCursorView(){
//		LogHelper.d("cursor","--------showMotionCursorView");
		if (mMotionCursorView == null){
			mMotionCursorView = new GLCursorView(GLBaseActivity.this);
			mMotionCursorView.setImage(com.bfmj.viewcore.R.drawable.cursor_normal);
			mMotionCursorView.setLayoutParams(80, 80);
			mMotionCursorView.setDepth(3.0f);
			mMotionCursorView.setX(1160);
			mMotionCursorView.setY(1160);
			mMotionCursorView.setController(mController);
			MJGLUtils.exeGLQueueEvent(this, new Runnable() {
				@Override
				public void run() {
					getRootView().addView(mMotionCursorView);
				}
			});
		}
		if(((MjVrPlayerActivity)this).mCurPlayMode== VideoModeType.PLAY_MODE_VR) { //只有沉浸模式下才显示体感手柄瞄点
			mMotionCursorView.setVisible(true);
//			LogHelper.d("cursor","--------hideCursorView---showMotionCursorView");
			hideCursorView();
		} else {
			mMotionCursorView.setVisible(false);
		}
	}

	public void hideMotionCursorView(){
//		LogHelper.d("cursor","--------hideMotionCursorView");
		if (mMotionCursorView != null){
			mMotionCursorView.setVisible(false);
		}
	}

	public void setDelayVisiable(int duration){
		if(timer!=null){
			timer.cancel();
			timer = null;
		}
		timer = new Timer();
		TimerTask task = new TimerTask() {
			@Override
			public void run() {
//				MJGLUtils.exeGLQueueEvent(this, new Runnable() {
//					@Override
//					public void run() {
//				mHeadControlCursor.setVisible(false);
//					}
//				});
			}
		};
		timer.schedule(task,duration);
	}

	public void cancelHideCursorViewTimer(){
		if(timer!=null){
			timer.cancel();
			timer = null;
		}
	}



	@Override
	protected void onDestroy() {
		GLPanoView.releaseInstance();
		super.onDestroy();
		if(mSkyboxView!=null){
			mSkyboxView.release();
		}
	}

	@Override
	protected void onPause() {
		isActivityPause = true;
		StickUtil.setCallback(null);
		StickUtil.disconnect();
		mControllerManager.stop();
		MojingSDKSensorManager.UnRegisterSensor(this);
		MojingSDK.StopTracker();
		MojingSDKServiceManager.onPause(this);
		if (blueToothBroadCast != null) {
			unregisterReceiver(blueToothBroadCast);// 取消蓝牙广播接收者
		}
		super.onPause();
//		MobclickAgent.onPause(getApplicationContext());
		MojingSDKReport.onPause(this);
	}

	@Override
	protected void onResume() {
		isActivityPause = false;
		super.onResume();
		if(null != listener) {
			listener.controllerState = MotionInputCallback.STATE_DISCONNECTED;
		}
		MojingSDKSensorManager.RegisterSensor(this);
		//MojingSDKServiceManager.onResume(this);
		MojingSDK.StartTracker(100);
		StickUtil.getInstance(this);
		StickUtil.setCallback(this);
		if(blueToothBroadCast!=null) {
			registerReceiver(blueToothBroadCast, new IntentFilter(
					BluetoothAdapter.ACTION_STATE_CHANGED));
		}

		mControllerManager.start();

//		MobclickAgent.onResume(getApplicationContext());
		MojingSDKReport.onResume(this);

		if (getRootView() != null){
			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

					getRootView().queueEvent(new Runnable() {
						@Override
						public void run() {
							getRootView().initHeadView();
						}
					});
				}
			}).start();
		}
	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		if(event.getKeyCode() == KeyEvent.KEYCODE_VOLUME_DOWN){// 音量减小
			return super.dispatchKeyEvent(event);
		}else if(event.getKeyCode() == KeyEvent.KEYCODE_VOLUME_UP){// 音量增大
			return super.dispatchKeyEvent(event);
		}
		if (StickUtil.dispatchKeyEvent(event) && event.getKeyCode() != 4)
			return true;
		else
			return super.dispatchKeyEvent(event);
	}

	@Override
	public boolean dispatchGenericMotionEvent(MotionEvent event) {
		if (StickUtil.dispatchGenericMotionEvent(event))
			return true;
		else
			return super.dispatchGenericMotionEvent(event);
	}

	@Override
	public void onBluetoothAdapterStateChanged(int arg0) {
		StickUtil.onBluetoothAdapterStateChanged(arg0);
	}

	private void changeControlMode(){
		Set<String> deviceNames = StickUtil.getJoystickNames();
		if (deviceNames.contains(MOTION_NAME)){
			GLFocusUtils.setControlMode(GLFocusUtils.MODE_MOTION_JOYSTICK_CONTROL);
		} else if (deviceNames.size() > 0){
			GLFocusUtils.setControlMode(GLFocusUtils.MODE_HEAD_AND_JOYSTICK_CONTROL);
		} else {
			GLFocusUtils.setControlMode(GLFocusUtils.MODE_HEAD_CONTROL);
		}
	}

	@Override
	public void onMojingDeviceAttached(String deviceName) {
		LogHelper.d("aaaa", "onMojingDeviceAttached: " + deviceName);

		if (deviceName == null){
			deviceName = "";
		}

		if (!StickUtil.blutoothEnble()) {// 蓝牙关闭
			return;
		}
		if(!StickUtil.filterDeviceName(deviceName)){
			return;
		}

		StickUtil.setJoystickName(deviceName);
		changeControlMode();

		onConnStartCheck();
		StickUtil.onMojingDeviceAttached(deviceName);
	}

	@Override
	public void onMojingDeviceDetached(String deviceName) {
		LogHelper.d("aaaa", "onMojingDeviceDetached: " + deviceName);

		if (deviceName == null){
			deviceName = "";
		}

		if(!StickUtil.filterDeviceName(deviceName)){
			return;
		}

		StickUtil.removeJoystickName(deviceName);
		changeControlMode();
		onConnStartCheck();
		StickUtil.onMojingDeviceDetached(deviceName);
	}

	@Override
	public void onTouchPadStatusChange(String s, boolean b) {

	}

	@Override
	public void onTouchPadPos(String s, float v, float v1) {

	}

	@Override
	public boolean onMojingKeyDown(String deviceName, final int keyCode) {
		LogHelper.d("aaaa", "onMojingKeyDown: " + keyCode);
		StickUtil.onMojingKeyDown(deviceName, keyCode);
		onZKeyDown(keyCode);
		return false;
	}

	@Override
	public boolean onMojingKeyLongPress(String deviceName, final int keyCode) {
		onZKeyLongPress(keyCode);
		return false;
	}

	@Override
	public boolean onMojingKeyUp(String deviceName, final int keyCode) {
		StickUtil.onMojingKeyUp(deviceName, keyCode);
		onZKeyUp(keyCode);
		return false;
	}

	@Override
	public boolean onMojingMove(String deviceName, int axis, float x, float y, float z) {
		StickUtil.onMojingMove(deviceName, axis, x, y, z);
		return false;
	}

	@Override
	public boolean onMojingMove(String deviceName, int axis, float value) {
		StickUtil.onMojingMove(deviceName, axis, value);
		return false;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(StickUtil.isConnected()){
			return true;
		}
		 if(keyCode==KeyEvent.KEYCODE_VOLUME_MUTE||keyCode==KeyEvent.KEYCODE_VOLUME_DOWN|| keyCode==KeyEvent.KEYCODE_VOLUME_UP){
			 return super.onKeyDown(keyCode, event);
		 }
		if (keyCode != KeyEvent.KEYCODE_BACK || getPageManager().hasMorePage()){
			if (keyCode == 23){
				keyCode = MojingKeyCode.KEYCODE_ENTER;
			}
			onZKeyDown(keyCode);
			return  true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if(StickUtil.isConnected()){
			return true;
		}
		if(keyCode==KeyEvent.KEYCODE_VOLUME_MUTE||keyCode==KeyEvent.KEYCODE_VOLUME_DOWN|| keyCode==KeyEvent.KEYCODE_VOLUME_UP){
			return super.onKeyUp(keyCode, event);
		}
		if (keyCode != KeyEvent.KEYCODE_BACK || getPageManager().hasMorePage()){
			if (keyCode == 23){
				keyCode = MojingKeyCode.KEYCODE_ENTER;
			}
			onZKeyUp(keyCode);
			return  true;
		}
		return super.onKeyUp(keyCode, event);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN){
			onZKeyDown(MojingKeyCode.KEYCODE_ENTER);
		} else if (event.getAction() == MotionEvent.ACTION_UP){
			onZKeyUp(MojingKeyCode.KEYCODE_ENTER);
		}

		return super.onTouchEvent(event);
	}

	@Override
	public boolean onZKeyDown(int keyCode) {
		keyCode = transferKey(keyCode);
		boolean b = JoystickUitlManager.getInstance().onZKeyDown(keyCode);
		if(b) return true;//底层不处理直接返回true
		return super.onZKeyDown(keyCode);
	}

	@Override
	public boolean onZKeyUp(int keyCode) {
		keyCode = transferKey(keyCode);
		JoystickUitlManager.getInstance().onZKeyUp(keyCode);
		return super.onZKeyUp(keyCode);
	}

	@Override
	public boolean onZKeyLongPress(int keyCode) {
		keyCode = transferKey(keyCode);
		JoystickUitlManager.getInstance().onZKeyLongPress(keyCode);
		return super.onZKeyLongPress(keyCode);
	}

	private int transferKey(int keyCode){
		int code = keyCode;
		switch (keyCode){
			case MojingKeyCode.KEYCODE_PICTSYMBOLS:
				code = MojingKeyCode.KEYCODE_VOLUME_UP;
				break;
			case MojingKeyCode.KEYCODE_SWITCH_CHARSET:
				code = MojingKeyCode.KEYCODE_VOLUME_DOWN;
				break;
			case MojingKeyCode.KEYCODE_BUTTON_A:
				code = MojingKeyCode.KEYCODE_ENTER;
				break;
			case MojingKeyCode.KEYCODE_BUTTON_B:
				code = MojingKeyCode.KEYCODE_BACK;
				break;
			case MojingKeyCode.KEYCODE_BUTTON_X:
				code = MojingKeyCode.KEYCODE_MENU;
				break;
		}
		return code;
	}

	public void onConnStartCheck(){
		JoystickUitlManager.getInstance().onConnStartCheck();
	}

	private String getGlassKey(){
		if (!MojingSDK.GetInitSDK()) {
			MojingSDK.Init(this.getApplicationContext());
			MojingSDK.onDisableVrService(true);
		}

		ManufacturerList m_ManufacturerList = ManufacturerList.getInstance("zh");

		List<Manufacturer> manufacturers = m_ManufacturerList.mManufaturerList;
		List<Product> products = manufacturers.get(0).mProductList;
		List<Glass> glasses = products.get(0).mGlassList;
		String key = glasses.get(0).mKey;
		return key;
	}

	private IResetLayerCallBack mCallBack;

	public void setIResetLayerCallBack(IResetLayerCallBack callBack){
		mCallBack = callBack;
		if (null != mResetView) {
			mResetView.setIResetLayerCallBack(mCallBack);
		}
	}

	public void showOpen(boolean isEnter) {
		if(StickUtil.getConnectStatus() == 1) return;
		mResetView.showOpen(isEnter);
	}

	public void showClose(boolean isEnter) {
		mResetView.showClose(isEnter);
	}

	private void initLog(){
		final GLTextView fps = new GLTextView(this);
		fps.setX(900);
		fps.setY(2200);
		fps.setLayoutParams(600, 100);
		fps.setFixed(true);
		fps.setBackground(new GLColor(0x000000, 0.5f));
		fps.setTextColor(new GLColor(0xffffff));
		fps.setTextSize(80);

		getRootView().addView(fps);

		new Thread(new Runnable() {
			long times = 0;
			int max = 0;
			int min = 60;

			@Override
			public void run() {
				getRootView().getFPS();
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				while (true){
					final int f = getRootView().getFPS();
					if (f > 0 && f < 70){
						times++;
						if (times > 2) {
							max = Math.max(f, max);
							min = Math.min(f, min);
							getRootView().queueEvent(new Runnable() {
								@Override
								public void run() {
									String msg = "FPS : " + f;
									if (max > 0){
										msg +=  " [" + min + "~" + max + "]";
									}
									fps.setText(msg);
								}
							});
						}
					}
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}

			}
		}).start();
	}

	@Override
	public void onMotionStateChanged(String motionName, int state) {}

	@Override
	public void onMotionKeyDown(int keycode) {}

	@Override
	public void onMotionKeyUp(int keycode) {}

	@Override
	public void onMotionLongPress(int keycode) {}

	@Override
	public void onMotionTouch(Event event) {
		JoystickUitlManager.getInstance().onMotionTouch(event);
	}

	@Override
	public void onMotionOrientation(Orientation orientation) {}

	private class MotionEventListener extends Controller.EventListener
			implements ControllerManager.EventListener {

		// The status of the overall controller API. This is primarily used for error handling since
		// it rarely changes.
		private String apiStatus;

		// The state of a specific Controller connection.
		public int controllerState = MotionInputCallback.STATE_DISCONNECTED;
		private boolean lastIsTouching = false;
		private float lastX = -1;
		private float lastY = -1;

		@Override
		public void onApiStatusChanged(int state) {
			apiStatus = ControllerManager.ApiStatus.toString(state);
			LogHelper.d("aaaa", "onApiStatusChanged => " + apiStatus+"  "+state);
		}

		@Override
		public void onConnectionStateChanged(int state) {
			LogHelper.d("aaaa", "onConnectionStateChanged => " + state);
//			LogHelper.d("cursor","--------onConnectionStateChanged:"+state);
			if(state == controllerState) return;
			controllerState = state;
			if(controllerState == MotionInputCallback.STATE_CONNECTED){//连上遥控器
				StickUtil.setJoystickName("体感手柄");
				changeControlMode();
//				LogHelper.d("cursor","--------hideCursorView---onConnectionStateChanged");
				hideCursorView();
				showMotionCursorView();
				hideResetLayerView();
				if (mMotionCursorView != null){
					mMotionCursorView.setMotionConnected(true);
				}
				onConnStartCheck();
			}else if (controllerState == MotionInputCallback.STATE_DISCONNECTED){//遥控器链接失败
				StickUtil.removeJoystickName("体感手柄");
				changeControlMode();
				hideMotionCursorView();
//				LogHelper.d("cursor","--------showCursorView---onConnectionStateChanged");
				showCursorView();
				showResetLayerView();
				if (mMotionCursorView != null){
					mMotionCursorView.setMotionConnected(false);
				}
				onConnStartCheck();
			}
		}

		@Override
		public void onRecentered() {
			LogHelper.d("aaaa", "onRecentered => ");
			onZKeyLongPress(MojingKeyCode.KEYCODE_HOME);
		}

		private int lastButton = 0;
		@Override
		public void onUpdate() {
			mController.update();
			onMotionOrientation(mController.orientation);
			//触摸事件处理
			if (mController.isTouching) {
				lastX = mController.touch.x;
				lastY = mController.touch.y;
				if (!lastIsTouching){
					lastIsTouching = true;
					onMotionTouch(new Event(Event.ACTION_DOWN, lastX, lastY));
				} else {
					onMotionTouch(new Event(Event.ACTION_MOVE, lastX, lastY));
				}
			} else {
				if (lastIsTouching){
					lastIsTouching = false;
					onMotionTouch(new Event(Event.ACTION_UP, lastX, lastY));
				}
			}

			int button = 0;
			if (mController.appButtonState){
				button = MojingKeyCode.KEYCODE_BACK;
			} else if (mController.homeButtonState){
				button = MojingKeyCode.KEYCODE_HOME;
			} else if (mController.clickButtonState){
				button = MojingKeyCode.KEYCODE_ENTER;
			} else if (mController.volumeUpButtonState){
				button = MojingKeyCode.KEYCODE_VOLUME_UP;
			} else if (mController.volumeDownButtonState){
				button = MojingKeyCode.KEYCODE_VOLUME_DOWN;
			}

			if (lastButton == 0 && button != 0){
				onZKeyDown(button);
				lastButton = button;
			} else if (lastButton != 0 && button == 0){
				onZKeyUp(lastButton);
				lastButton = 0;
			}
		}
	}


	private class BlueToothBroadCast extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			switch (intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, -1)) {
				case BluetoothAdapter.STATE_TURNING_ON:
					break;
				case BluetoothAdapter.STATE_TURNING_OFF:
					break;
				case BluetoothAdapter.STATE_ON:
				case BluetoothAdapter.STATE_OFF:
					onConnStartCheck();// 开始校验
					break;
				default:
					break;
			}
		}
	}

	/**
	 * 获取当前page类型
	 * @return
     */
  public int getCurPageType(){
	  GLViewPage page =  getPageManager().getIndexView();
	  if(page!=null&&(page instanceof BasePlayerPage)){
		  return ((BasePlayerPage)page).getmPageType();
	  }
	  return 0;
  }

}
