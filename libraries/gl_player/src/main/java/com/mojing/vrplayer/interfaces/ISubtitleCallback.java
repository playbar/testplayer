package com.mojing.vrplayer.interfaces;

import java.util.ArrayList;

/**
 * 
 * ClassName: ISubtitleCallback <br/>
 * @author lixianke    
 * @date: 2015-1-19 上午10:07:20 <br/>  
 * description: 本地字幕接口
 */
public interface ISubtitleCallback {
	/**
	 * 
	 * @author lixianke  @Date 2015-1-19 上午10:07:43
	 * description: 当字幕列表更新时的回调
	 * @param lists 字幕列表  
	 * @return 无
	 */
	void onSubtitlesListUpdate(ArrayList<String> lists);
	/**
	 * 
	 * @author lixianke  @Date 2015-1-19 上午10:08:36
	 * description: 当字幕更新时的回调
	 * @param text 字幕
	 * @return 无
	 */
	void onSubtitleUpdate(String text);
}
