package com.baofeng.mj.vrplayer.bean;

import java.io.Serializable;

/**
 * 本地视频实体类
 */
public class LocalVideoBean implements Serializable {
	private static final long serialVersionUID = 928338994927344504L;
	public static final int VISIBLE_TYPE_VISIBLE=0;
	public static final int VISIBLE_TYPE_GONE=1;
	public static final int ITEM_TYPE_TITLE=0;
	public static final int ITEM_TYPE_ITEM=1;
	public String name;//文件名
	public String size;//文件大小，例如：50.5MB
	public long length;//文件大小，例如：1000
	public String path;//文件地址
	public long lastModify;//文件上次修改时间
	public String thumbPath;//缩略图地址
	public String group;//分组
	public int videoType;//视频类型
	public int visible=VISIBLE_TYPE_VISIBLE;// VISIBLE_TYPE_VISIBLE =1 VISIBLE_TYPE_GONE=0
	public int itemType=ITEM_TYPE_ITEM;
	public int videoDuration=0;
}
