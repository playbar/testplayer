package com.baofeng.mj.vrplayer.business.flyscreen.interfaces;

import android.content.Context;
import android.os.Message;
import android.text.TextUtils;
import android.widget.Toast;

import com.baofeng.mj.vrplayer.bean.DeviceInfo;
import com.baofeng.mj.vrplayer.bean.Login;
import com.baofeng.mj.vrplayer.bean.Resource;
import com.baofeng.mj.vrplayer.business.flyscreen.logic.FlyScreenLoginModel;
import com.baofeng.mj.vrplayer.business.flyscreen.logic.FlyScreenResListModel;
import com.baofeng.mj.vrplayer.business.flyscreen.logic.FlyScreenTcpSocket;
import com.baofeng.mj.vrplayer.business.flyscreen.logic.FlyScreenUdpSocket;
import com.baofeng.mj.vrplayer.business.flyscreen.logic.TcpConnCallback;
import com.baofeng.mj.vrplayer.business.flyscreen.util.FlyScreenUtil;
import com.baofeng.mj.unity.UnityLocalBusiness;
import com.baofeng.mj.util.publicutil.NetworkUtil;
import com.google.protobuf.InvalidProtocolBufferException;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;


/**
 * 飞屏u3d接口方法类
 * ClassName: FlyScreenActivity <br/>
 * @author linzanxian    
 * @date: 2015-8-6 上午11:06:59 <br/>
 * description:
 */
public class FlyScreenInterface implements TcpConnCallback {
	private FlyScreenTcpSocket tcpClient;
	private FlyScreenUdpSocket udpSocket;
	private Context mContext;

	private int recursionCount = 0;
	private List<DeviceInfo> mDeviceInfos;
	private FlyScreenLoginModel loginModel;
	private FlyScreenResListModel resListModel;
	
	private static FlyScreenInterface mInstance;
	
	private int port;
	
	private static final int MSG_UPDATE_RES_DIR_PAGE = 0x1003;
	private static final int MSG_UPDATE_RES_NOTIFY_CHANGE = 0x1004;
	private static final int MSG_TCP_DISCONNECTED = 0x1005;
	private static final int MSG_RES_LOGIN_METHOD = 2;
	private static final int MSG_RES_LOGIN = 3;
	private static final int MSG_UPDATE_DIR_COUNT = 0x1006;
	
	private static FlyScreenUtil.FlyResType resType = FlyScreenUtil.FlyResType.video;
	private Stack<String> dirStack = new Stack<String>();
	private DeviceInfo currDevInfo;
	private boolean mResourceFresh = false;
	
	public FlyScreenInterface(Context context) {
		mContext = context;
	}
	private boolean isGetDevice = false;
	
	/****************** u3d调用接口   start **************************/
	
	public static FlyScreenInterface getInstance(Context context) {
		if (mInstance == null) {
			mInstance = new FlyScreenInterface(context);
		}
		
		return mInstance;
	}
	
	/**
	 * 初始化加载
	 * @author linzanxian  @Date 2015-8-18 下午5:09:38 
	 * @return void
	 */
	public void init() {
		isGetDevice = false;
		// 向服务器组播请求设备列表
		tcpClient = FlyScreenTcpSocket.getSingleInstance();
		tcpClient.setDevListHandler(mHandlerCallBack);

		udpSocket = FlyScreenUdpSocket.getSingleInstance(mContext);
		udpSocket.udpDeviceList();
		
		recursionFindDevice();
	}
	
	/**
	 * 对外接口刷新列表
	 * @author linzanxian  @Date 2015-8-6 上午11:42:04
	 * @return void
	 */
	public void freshDevlistClick() {
		isGetDevice = false;
		//reportClick();
		udpSocket.udpDeviceList();
		
		recursionFindDevice();
	}
	
	/**
	 * 获取设备资源列表
	 * @author linzanxian  @Date 2015-8-6 下午2:24:20
	 * @param index 索引  
	 * @return void
	 */
	public void getDeviceResourceList(int index, boolean fresh) {
		boolean connect = NetworkUtil.getNetStatus(mContext);
		if (!connect) {
			//UnityPlayer.UnitySendMessage("AndroidInterface", "flyScreenError", "3");
			return;
		}

		//Log.d("test2", "getDeviceResourceList"+ mDeviceInfos.size() +"----"+ fresh);
		if (!fresh) {
			dirStack.clear();
		}
		mResourceFresh = fresh;
		if (index < 0 || index >= mDeviceInfos.size()) {
			return;
		}
		
		currDevInfo = mDeviceInfos.get(index);
		//Log.d("test2", currDevInfo.toString());
		loginModel = FlyScreenLoginModel.getSingleInstance(mContext.getApplicationContext());
		
		requestLoginData(currDevInfo);
		
	}
	
	/**
	 * 前进
	 * @author linzanxian  @Date 2015-8-11 下午4:21:08
	 * @param {引入参数名} {引入参数说明}  
	 * @return {返回值说明}
	 */
	public synchronized void forwardDir(String dirUri) {
		//Log.d("test2", "forwardDir:"+dirUri);
		if (!mResourceFresh) {
			if ("/".equals(getCurDirUri())) {
				dirUri = dirUri.replace(getCurDirUri(), "") + "/";
			} else {
				dirUri = dirUri.replace(getCurDirUri(), "");
			}
			
			dirStack.push(dirUri);
		} else {
			if (getCurDirUri().equals("")) {
				dirStack.push("/");
			}
		}
//		mGlVideoListRelativeView.setData(null, tcpClient.getmCurrDevInfo().getIp(), port);
		getDirCount(getCurDirUri());
		mResourceFresh = false;
	}
	
	/**
	 * 后 退目录
	 */
	public synchronized void backDir() {
		if (dirStack.isEmpty()) {
			//UnityPlayer.UnitySendMessage("AndroidInterface", "rootDirectory", "");
			finish();
			return;
		}
		dirStack.pop();
		if (dirStack.isEmpty()) {
			//UnityPlayer.UnitySendMessage("AndroidInterface", "rootDirectory", "");
			finish();
			return;
		}
		if (dirStack.size() == 1) {
			//UnityPlayer.UnitySendMessage("AndroidInterface", "rootDirectory", "");
		}
//		mGlVideoListRelativeView.setData(null, tcpClient.getmCurrDevInfo()
//				.getIp(), port);
		getDirCount(getCurDirUri());
	}
	
	
	/*************** u3d调用接口  end ********************************/
	

	private FlyScreenTcpSocket.HandlerCallBack mHandlerCallBack = new FlyScreenTcpSocket.HandlerCallBack() {

		@Override
		public void handlerMessage(Message msg) {
			switch (msg.what) {
			case 2:
				// dealLoginMethodResult(msg);
				break;
			case 3:// 返回登录结果
				// dealLoginResult(msg);
				break;
			case 4:// 设备发现通知
				dealDeviceFindResult();
				break;
			default:
				break;
			}
		}

	};
	
	/**
	 * 启动设备查找
	 * @author linzanxian  @Date 2015-8-6 上午11:25:52
	 * @return void
	 */
	private void recursionFindDevice() {
		recursionCount = 0;
		new Thread(new Runnable() {

			@Override
			public void run() {
				while (recursionCount <= 12) {
					recursionCount++;
					if (udpSocket.getCheckedDevList().size() > 0) {
						break;
					}
					try {
						Thread.sleep(250);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				
				dealDeviceFindResult();
				recursionCount = 0;
			}
		}).start();
	}
	
	/**
	 * 处理查找结果
	 * @author linzanxian  @Date 2015-8-6 上午11:26:48
	 * @return void
	 */
	private void dealDeviceFindResult() {
		if (isGetDevice) {
			return;
		}

		if (udpSocket.getCheckedDevList().size() == 0) {
			//通知u3d,未查找到设备
			//UnityPlayer.UnitySendMessage("AndroidInterface", "flyDevice", "0");
		} else {
			//通知u3d, 查找到设备
			isGetDevice = true;
			//UnityPlayer.UnitySendMessage("AndroidInterface", "flyDevice", "1");

			//传递设备列表
			freshDevListData();
		}
	}
	
	/**
	 * 传递设备列表
	 * @author linzanxian  @Date 2015-8-6 上午11:32:52
	 * @return void
	 */
	private void freshDevListData() {
		setDevListData();
//		new Thread(new Runnable() {
//			
//			@Override
//			public void run() {
//				try {
//					Thread.sleep(100);
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				}
//				
//				setDevListData();
//			}
//		});
	}
	
	/**
	 * 传递设备列表给u3d
	 * @author linzanxian  @Date 2015-8-6 上午11:38:49
	 * @return void
	 */
	private void setDevListData() {
		if (mContext == null)
			return;

		if (udpSocket == null) {
			udpSocket = FlyScreenUdpSocket.getSingleInstance(mContext
					.getApplicationContext());
		}

		List<DeviceInfo> tcpDevList = udpSocket.getCheckedDevList();
		mDeviceInfos = joinTcpDevAndSqliteDev(tcpDevList);
		//Log.d("test2", ObjectToJsonString.deviceToString(mDeviceInfos));
		//传数据给u3d
		//UnityPlayer.UnitySendMessage("AndroidInterface", "refreshRet", ObjectToJsonString.deviceToString(mDeviceInfos));
		//UnityLocalBusiness.sendFlyScreenDeviceList(ObjectToJsonString.deviceToString(mDeviceInfos));
	}
	
	/**
	 * @author zhangxin @Date 2015-1-16 上午11:19:36
	 *         description:tcp查找到的设备列表与本地数据库里缓存的设备列表做去重合并
	 * @param tcpDevList:tcp查找的设备列表
	 * @return 总设备列表
	 */
	private List<DeviceInfo> joinTcpDevAndSqliteDev(List<DeviceInfo> tcpDevList) {
		if (mContext == null)
			return null;

		List<DeviceInfo> retDevList = new ArrayList<DeviceInfo>();
		retDevList.addAll(tcpDevList);
//		List<DeviceInfo> joinDevList =  SqlProxy.getQuery(new SqlProxy.sqlQuery<List<DeviceInfo>>() {
//			@Override
//			public List<DeviceInfo> doSqlQuery(SqliteManager sqlite) {
//				return sqlite.getFlyDeviceList();
//			}
//		});

//		for (int i = 0; i < joinDevList.size(); i++) {
//			DeviceInfo d1 = joinDevList.get(i);
//
//			boolean isexist = false;
//			DeviceInfo d2 = null;
//			for (int j = 0; j < tcpDevList.size(); j++) {
//				d2 = tcpDevList.get(j);
//				if (d1.getId().equalsIgnoreCase(d2.getId())) {
//					isexist = true;
//					break;
//				}
//			}
//
//			if (!isexist && d2 != null) {
//				retDevList.add(d1);
//			}
//		}

		return retDevList;
	}
	
	/**
	 * 验证登录
	 * @author linzanxian  @Date 2015-8-11 下午3:33:31
	 * @param currDevInfo DeviceInfo设备
	 * @return void
	 */
	private void requestLoginData(DeviceInfo currDevInfo) {
		tcpClient = FlyScreenTcpSocket.getSingleInstance();
		tcpClient.setmCurrDevInfo(currDevInfo);
		tcpClient.tcpConnectAndCallback(mContext, currDevInfo.getIp(), currDevInfo.getPort(), this);
		tcpClient.setDevListHandler(this.mTcpHandlerCallBack);
	}
	
	private FlyScreenTcpSocket.HandlerCallBack mTcpHandlerCallBack = new  FlyScreenTcpSocket.HandlerCallBack() {

		@Override
		public void handlerMessage(Message msg) {
			switch (msg.what) {
			case 2:
				// dealLoginMethodResult(msg);
				break;
			case 3:// 返回登录结果
				dealLoginResult(msg);
				break;
			default:
				break;
			}
		}

	};
	
	/**
	 * 更新资源列表
	 * 
	 * @param baseResourceMsg
	 */
	public static void updateResList(FlyScreenUtil.FlyResType resType,
			Resource.BasicResourceMessage baseResourceMsg) {
		// if(!getTopActivity(App.getInstance().getApplicationContext()).contains("com.baofeng.mj.flyscreen.activity.VideoList")){
		// return ;
		// }
		if (baseResourceMsg == null)
			return;
		if (mVideoHandlerCallBack == null)
			return;
		Message msg = null;
		switch (baseResourceMsg.getMt()) {
		case ResourceMessageType_ResponseDirPageData:
			if (FlyScreenInterface.resType != resType)
				return;
			msg = new Message();
			msg.what = MSG_UPDATE_RES_DIR_PAGE;
			msg.obj = baseResourceMsg;
			break;
		case ResourceMessageType_NotifyResourceChange:
			msg = new Message();
			msg.what = MSG_UPDATE_RES_NOTIFY_CHANGE;
			msg.obj = baseResourceMsg;
			break;
		case ResourceMessageType_ResponseDirCount:
			msg = new Message();
			msg.what = MSG_UPDATE_DIR_COUNT;
			msg.obj = baseResourceMsg;
			break;
		default:
			break;
		}
		if (msg != null) {
			mVideoHandlerCallBack.handlerMessage(msg);
		}

	}
	
	/**
	 * 处理登录结果
	 * @author linzanxian  @Date 2015-8-11 下午3:38:28
	 * @param msg Message
	 * @return void
	 */
	protected void dealLoginResult(Message msg) {
		Login.LoginResultErrorcode r = loginModel.getLoginErrorCode();
		//Log.d("test2", "dealLoginResult:"+ r);
		switch (r) {
		case LoginResultErrorcode_OK:

			if (FlyScreenLoginModel.isbContentEmpty()) {
				initData();
//				HashMap<String, String> hs = new HashMap<String, String>();
//				hs.put("pagetype", "i_flyvideo");
//				hs.put("fly_form", "4");
//				Report.getSingleReport(mContext).reportPv(hs);
			} else {
				initData();
//				HashMap<String, String> hs = new HashMap<String, String>();
//				hs.put("pagetype", "i_flyvideo");
//				hs.put("fly_form", "3");
//				Report.getSingleReport(mContext).reportPv(hs);
			}
			break;
		case LoginResultErrorcode_FAIL:
			break;
		case LoginResultErrorcode_BadUserNameOrPassWord:
			//UnityPlayer.UnitySendMessage("AndroidInterface", "flyScreenError", "2");
			break;
		case LoginResultErrorcode_ExceedMaxUser:
			//超过最大用户数，通知unity
			//UnityPlayer.UnitySendMessage("AndroidInterface", "flyScreenError", "1");
			break;
		case LoginResultErrorcode_MethodNotSupported:
			break;
		default:
			break;
		}
	}
	
	/**
	 * 初始化数据
	 * @author linzanxian  @Date 2015-8-11 下午3:40:58 
	 * @return void
	 */
	private void initData() {

		loginModel = FlyScreenLoginModel.getSingleInstance(mContext.getApplicationContext());
		port = FlyScreenLoginModel.getServerPort();
		if (port == 0) {
			// toastLong("未请求到服务器端口地址，请重试");
			finish();
			return;
		}
		//UnityPlayer.UnitySendMessage("AndroidInterface", "webPort", port+"");
		UnityLocalBusiness.sendFlyScreenServerPort(port);

		resListModel = new FlyScreenResListModel();
		mVideoHandlerCallBack = new VideoHandlerCallBack() {

			@Override
			public void handlerMessage(Message msg) {
				Resource.BasicResourceMessage baseResourceMsg = (Resource.BasicResourceMessage) msg.obj;
				switch (msg.what) {
					case MSG_TCP_DISCONNECTED:
						// CommonToast.makeText(activity.getApplicationContext(),
						// "您的电脑已经关闭飞屏功能，页面即将调转 ?",
						// CommonToast.LENGTH_SHORT).show();
						Toast.makeText(mContext,"您的电脑已经关闭飞屏功能，页面即将调转 ?", Toast.LENGTH_SHORT);
						break;
						
					case MSG_UPDATE_RES_DIR_PAGE:
						Resource.ResponsePageData responsePageData = null;
						try {
							responsePageData = Resource.ResponsePageData
									.parseFrom(baseResourceMsg.getDetailMsgBytes());
						} catch (InvalidProtocolBufferException e) {
							e.printStackTrace();
						}
						//Log.d("test2", "getCurDirUri():"+getCurDirUri());
						switch (responsePageData.getErrorcode()) {
							case ResourceErrorCode_OK:
								// 判断返回的数据是否是当前进入的目录
								if (!TextUtils.isEmpty(responsePageData.getUri())
										&& !getCurDirUri().equals(
												responsePageData.getUri())) {
									return;
								}
								List<Resource.PageItem> pageItemList = responsePageData
										.getItemsList();
								if (pageItemList == null || pageItemList.size() == 0) {
									//通知unity列表没有信息
									//UnityPlayer.UnitySendMessage("AndroidInterface", "flyScreenContent", "");
									//UnityLocalBusiness.sendFlyScreenDeviceResourceList("");
									return;
								}

//								int size = pageItemList.size();
//								for (int i = 0; i < size; i++) {
//									String urlString = getResUriSmall(pageItemList.get(i).getUri()) +"&md5="+ pageItemList.get(i).getMd5Sum();
//									Log.d("test2", urlString);
//
//									DisplayImageOptions options = new DisplayImageOptions.Builder()
//									.showImageForEmptyUri(R.drawable.about_icon)
//									.showImageOnFail(R.drawable.about_icon).cacheInMemory(false)
//									.cacheOnDisc(true).bitmapConfig(Bitmap.Config.RGB_565) // 设置图片的解码类型
//									.build();
//									Bitmap bitmap = ImageLoader.getInstance()
//											.loadImageSync(urlString, options);
//									
//									if (bitmap != null) {
//										Log.d("test2", "bitmap:"+ bitmap.toString());
//									}
//								}
								
								//传递list给unity
								//UnityPlayer.UnitySendMessage("AndroidInterface", "flyScreenContent", ObjectToJsonString.pageItemToString(pageItemList));
								//UnityLocalBusiness.sendFlyScreenDeviceResourceList(ObjectToJsonString.pageItemToString(pageItemList));
								break;
							case ResourceErrorCode_FAIL:
								if (!dirStack.isEmpty()) {
									dirStack.pop();
								}
								break;
							case ResourceErrorCode_FAIL_NotLogin:
								break;
						}
						break;
						
					case MSG_UPDATE_DIR_COUNT:
						Resource.ResponseDirCount responseDirCount = null;
						try {
							responseDirCount = Resource.ResponseDirCount
									.parseFrom(baseResourceMsg.getDetailMsgBytes());
						} catch (InvalidProtocolBufferException e) {
						}
						switch (responseDirCount.getErrorcode()) {
							case ResourceErrorCode_OK:
								tcpClient.send(mContext, resListModel.createtDirPageRequest(
									resType, getCurDirUri(), 0));
								break;
							case ResourceErrorCode_FAIL:
								//UnityPlayer.UnitySendMessage("AndroidInterface", "flyScreenContent", "");
								break;
							case ResourceErrorCode_FAIL_NotLogin:
								break;
							default:
								break;
						}
						break;
					case MSG_UPDATE_RES_NOTIFY_CHANGE:
						Resource.NotifyResourceChange notifyResourceChange = null;
						try {
							notifyResourceChange = Resource.NotifyResourceChange
									.parseFrom(baseResourceMsg.getDetailMsgBytes());
						} catch (InvalidProtocolBufferException e) {
						}
						
						//Logger.i("数据改变");
						if (notifyResourceChange.getUri().equals("") && notifyResourceChange.getReason().getNumber() == 1) {
							dirStack.clear();
							requestLoginData(currDevInfo);
						}
						break;
				}

			}

		};

		forwardDir("/");
	}
	
	/**
	 * 结束
	 * @author linzanxian  @Date 2015-8-11 下午3:41:48
	 * @param {引入参数名} {引入参数说明}  
	 * @return {返回值说明}
	 */
	public void finish() {
		loginModel.requestLogout(mContext);
		tcpClient.close();
	}
	
	public interface VideoHandlerCallBack {
		public void handlerMessage(Message msg);

	}

	private static VideoHandlerCallBack mVideoHandlerCallBack;

	public void setHandlerCallBack(VideoHandlerCallBack mVideoHandlerCallBack) {
		FlyScreenInterface.mVideoHandlerCallBack = mVideoHandlerCallBack;
	}
	
	/**
	 * 获取当前目录uri
	 * @author linzanxian  @Date 2015-8-11 下午3:56:25
	 * @return String
	 */
	public String getCurDirUri() {
		String dirUri = "";
		for (int i = 0; i < dirStack.size(); i++) {
			dirUri += dirStack.get(i);
		}
		//Log.d("test2", "getCurDirUri:"+ dirUri);
		return dirUri;
	}

	/**
	 * 获取资源uri
	 * @author linzanxian  @Date 2015-8-11 下午3:56:52
	 * @param uri 资源地址
	 * @return String
	 */
	public String getResUri(String uri) {
		return "http://" + tcpClient.getmCurrDevInfo().getIp() + ":" + port
				+ uri;
	}

	/**
	 * 获取资源uri，小图
	 * @author linzanxian  @Date 2015-8-11 下午3:57:27
	 * @param uri 资源地址 
	 * @return String
	 */
	public String getResUriSmall(String uri) {
		return "http://" + tcpClient.getmCurrDevInfo().getIp() + ":" + port
				+ uri + "?snap={\"mode\":\"0\"}";
	}

	private void getDirCount(String dir) {
		if(tcpClient == null||currDevInfo == null||!tcpClient.isTcpConnect(currDevInfo.getIp())||resListModel == null){
			
			return;
		}
		
		tcpClient.send(mContext, resListModel.createtDirCountRequest(resType, dir));
	}

	@Override
	public void onTcpConnSuccess(DeviceInfo deviceInfo) {
		tcpClient.heartBeat(mContext);
		loginModel = FlyScreenLoginModel.getSingleInstance(mContext
				.getApplicationContext());
		loginModel.requestLogout(mContext);
		loginModel.requestLogin(mContext, Login.LoginMethod.LoginMethod_None, "");

//		SqlProxy.addRun(new SqlProxy.sqlWork() {
//			@Override
//			public void doSql(SqliteManager sqlite) {
//				sqlite.addFlyDevice(currDevInfo);
//			}
//		});
	}

	@Override
	public void onTcpConnError() {
		//UnityPlayer.UnitySendMessage("AndroidInterface", "flyDisContent", "");
	}
}
