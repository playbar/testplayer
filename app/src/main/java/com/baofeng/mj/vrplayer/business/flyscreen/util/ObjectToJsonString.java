package com.baofeng.mj.vrplayer.business.flyscreen.util;

import com.baofeng.mj.vrplayer.bean.DeviceInfo;
import com.baofeng.mj.vrplayer.bean.Resource;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

//import com.baofeng.mj.pubblico.dao.MovieInfo;

/**
 * 对象转json字符串
 * ClassName: ObjectToJsonString <br/>
 * @author linzanxian    
 * @date: 2015-8-11 下午4:06:39 <br/>  
 * description:
 */
public class ObjectToJsonString {
	
	/**
	 * 把DeviceInfo对象转成字符串
	 * @author linzanxian  @Date 2015-8-11 下午2:10:58
	 * @param deviceInfos 设备列表  
	 * @return String
	 */
	public static String deviceToString(List<DeviceInfo> deviceInfos) {
		JSONArray jsonArray = new JSONArray();

		int size = deviceInfos.size();
		if (size > 0) {
			for (int i = 0; i < size; i++) {
				JSONObject jsonObject = deviceToJsonObject(deviceInfos.get(i));
				
				if (jsonObject != null && jsonObject.has("name")) {
					jsonArray.put(jsonObject);
				}
			}
		}
		
		return jsonArray.toString();
	}
	
	/**
	 * DeviceInfo对象转json对象
	 * @author linzanxian  @Date 2015-8-11 下午3:06:22
	 * @param deviceInfo DeviceInfo
	 * @return JSONObject
	 */
	public static JSONObject deviceToJsonObject(DeviceInfo deviceInfo) {
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("id", deviceInfo.getId());
			jsonObject.put("name", deviceInfo.getName());
			jsonObject.put("ip", deviceInfo.getIp());
			jsonObject.put("port", deviceInfo.getPort());
			jsonObject.put("lastLoginDate", deviceInfo.getLastLoginDate());
			jsonObject.put("icheck", deviceInfo.getIcheck());
			jsonObject.put("isLive", deviceInfo.isLive());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return jsonObject;
	}
	
	/**
	 * 把PageItem对象转成字符串
	 * @author linzanxian  @Date 2015-8-11 下午2:10:58
	 * @param deviceInfos 设备列表  
	 * @return String
	 */
	public static String pageItemToString(List<Resource.PageItem> pageItems) {
		JSONArray jsonArray = new JSONArray();

		int size = pageItems.size();
		if (size > 0) {
			for (int i = 0; i < size; i++) {
				JSONObject jsonObject = pageItemToJsonObject(pageItems.get(i));
				
				if (jsonObject != null && jsonObject.has("name")) {
					jsonArray.put(jsonObject);
				}
			}
		}
		
		return jsonArray.toString();
	}
	
	/**
	 * PageItem对象转json对象
	 * @author linzanxian  @Date 2015-8-11 下午3:06:22
	 * @param deviceInfo DeviceInfo
	 * @return JSONObject
	 */
	public static JSONObject pageItemToJsonObject(Resource.PageItem pageItem) {
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("name", pageItem.getName());
			jsonObject.put("bDir", pageItem.getBDir());
			jsonObject.put("uri", pageItem.getUri());
			jsonObject.put("md5Sum", pageItem.getMd5Sum());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return jsonObject;
	}
	
	/**
	 * 把MovieInfo对象转成字符串
	 * @author linzanxian  @Date 2015-8-11 下午2:10:58
	 * @param movieInfos 资源列表
	 * @return String
	 */
//	public static String movieInfoToString(List<MovieInfo> movieInfos) {
//		JSONArray jsonArray = movieInfoToArray(movieInfos);
//
//		return jsonArray.length() == 0 ? "" : jsonArray.toString();
//	}
	
	/**
	 * movieinfo转JSONArray
	 * @param movieInfos 资源列表
	 * @return JSONArray
	 */
//	public static JSONArray movieInfoToArray(List<MovieInfo> movieInfos) {
//		JSONArray jsonArray = new JSONArray();
//
//		int size = movieInfos.size();
//		if (size > 0) {
//			for (int i = 0; i < size; i++) {
//				JSONObject jsonObject = movieInfoToJsonObject(movieInfos.get(i));
//
//				if (jsonObject != null && jsonObject.has("movie_title")) {
//					jsonArray.put(jsonObject);
//				}
//			}
//		}
//
//		return jsonArray;
//	}
	
	/**
	 * MovieInfo对象转json对象
	 * @author linzanxian  @Date 2015-8-11 下午3:06:22
	 * @param deviceInfo DeviceInfo
	 * @return JSONObject
	 */
//	public static JSONObject movieInfoToJsonObject(MovieInfo movieInfo) {
//		JSONObject jsonObject = new JSONObject();
//		try {
//			jsonObject.put("movie_title", movieInfo.getMovie_title());
//			jsonObject.put("movie_img", movieInfo.getMovie_img());
//			jsonObject.put("play_url", movieInfo.getPlay_url());
//			jsonObject.put("hd_type", movieInfo.getHd_type());
//			jsonObject.put("directors", movieInfo.getDirectors());
//			jsonObject.put("writers", movieInfo.getWriters());
//			jsonObject.put("actors", movieInfo.getActors());
//			jsonObject.put("cate", movieInfo.getCate());
//			jsonObject.put("play_time", movieInfo.getPlay_time());
//			jsonObject.put("update_brief", movieInfo.getUpdate_brief());
//			jsonObject.put("description", movieInfo.getDescription());
//			jsonObject.put("seq_str", movieInfo.getSeq_str());
//			jsonObject.put("typeid", movieInfo.getTypeid());
//			jsonObject.put("movie_id", movieInfo.getMovie_id());
//			jsonObject.put("coop_movie_id", movieInfo.coop_movie_id);
//			jsonObject.put("duration", movieInfo.getDuration());
//			jsonObject.put("video_duration", movieInfo.getVideoDuration());
//			jsonObject.put("seqNO", movieInfo.getSeqNO());
//			jsonObject.put("lastPlayDate", movieInfo.getLastPlayDate());
//			jsonObject.put("lastPlaytime", movieInfo.getLastPlaytime());
//			jsonObject.put("lastIndex", movieInfo.getLastIndex());
//			jsonObject.put("total", movieInfo.getTotal());
//			jsonObject.put("is_yu", movieInfo.getIs_yu());
//			jsonObject.put("score", movieInfo.getScore());
//			jsonObject.put("finish", movieInfo.getFinish());
//			jsonObject.put("max_hdtype", movieInfo.getMax_hdtype());
//			jsonObject.put("is_3d", movieInfo.getIs_3d());
//			jsonObject.put("video_title", movieInfo.getVideo_title());
//			jsonObject.put("area", movieInfo.area);
//			jsonObject.put("from_id", movieInfo.getFrom_id());
//		} catch (JSONException e) {
//			e.printStackTrace();
//		}
//
//		return jsonObject;
//	}
}
