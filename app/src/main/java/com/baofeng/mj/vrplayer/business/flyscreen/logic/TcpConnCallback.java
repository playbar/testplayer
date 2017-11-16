package com.baofeng.mj.vrplayer.business.flyscreen.logic;


import com.baofeng.mj.vrplayer.bean.DeviceInfo;

/**
 * Name: TcpConnCallback
 * @author zhangxin    
 * @date: 2015-1-19 下午4:41:33 
 * description:tcp连接成功后回调接口
 */
public interface TcpConnCallback {
	public void onTcpConnSuccess(DeviceInfo deviceInfo);
	
	public void onTcpConnError();
}
