package com.mojing.vrplayer.publicc.bean;

import java.io.Serializable;

/**
 * 本地视频实体类
 */
public class LocalVideoBean implements Serializable{
	private static final long serialVersionUID = 928338994927344504L;
	public String name;//文件名
	public String size;//文件大小，例如：50.5MB
	public long length;//文件大小，例如：1000
	public String path;//文件地址
	public long lastModify;//文件上次修改时间
	public String thumbPath;//缩略图地址
	public long videoDuration;//视频长度
}
