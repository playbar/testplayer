package com.baofeng.mj.vrplayer.business.flyscreen.logic;

import com.baofeng.mj.vrplayer.bean.BaseMessage;
import com.google.protobuf.ByteString;

import java.io.UnsupportedEncodingException;

/**
 * ClassName: FlyScreenBaseModel
 * 
 * @author zhangxin
 * @date: 2015-1-19 下午4:42:15 description: 协议基础包
 */
public class FlyScreenBaseModel {
	/**
	 * @author zhangxin @Date 2015-1-19 下午4:42:46 
	 * description:生成总包字节数组
	 * @param msgType：总包类型；detailMsg：总包数据
	 * @return 总包
	 */
	public static byte[] createBasicMassage(BaseMessage.MessageType msgType,
			byte[] detailMsg) {
		BaseMessage.BasicMessage.Builder builder = BaseMessage.BasicMessage
				.newBuilder();
		builder.setMt(msgType);
		if (detailMsg != null) {
			String s = "";
			try {
				s = new String(detailMsg, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			builder.setDetailMsg(s);
		}
		BaseMessage.BasicMessage message = builder.build();
		byte[] bmsg = message.toByteArray();
		return bmsg;
	}

	/**
	 * @author zhangxin @Date 2015-1-19 下午4:44:34 
	 * description:生成总包字节数组
	 * @param msgType：总包类型；detailMsg：总包数据
	 */
	public static ByteString createBasicMassage(
			BaseMessage.MessageType msgType, ByteString detailMsg) {
		BaseMessage.BasicMessage.Builder builder = BaseMessage.BasicMessage
				.newBuilder();
		builder.setMt(msgType);
		if (detailMsg != null) {
			builder.setDetailMsgBytes(detailMsg);
		}
		BaseMessage.BasicMessage message = builder.build();
		return message.toByteString();
	}

	/**
	 * @author zhangxin @Date 2015-1-19 下午4:44:34 
	 * description:生成总包字节数组
	 * @param msgType：总包类型；detailMsg：总包数据
	 */
	public static byte[] createBasicMassage2(BaseMessage.MessageType msgType,
			ByteString detailMsg) {
		BaseMessage.BasicMessage.Builder builder = BaseMessage.BasicMessage
				.newBuilder();
		builder.setMt(msgType);
		if (detailMsg != null) {
			builder.setDetailMsgBytes(detailMsg);
		}
		BaseMessage.BasicMessage message = builder.build();
		byte[] bmsg = message.toByteArray();
		return bmsg;
	}
}
