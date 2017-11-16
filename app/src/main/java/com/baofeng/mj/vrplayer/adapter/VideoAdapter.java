package com.baofeng.mj.vrplayer.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.Fragment;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baofeng.mj.vrplayer.MyAppliaction;
import com.baofeng.mj.vrplayer.R;
import com.baofeng.mj.vrplayer.bean.LocalVideoBean;
import com.baofeng.mj.vrplayer.business.LocalVideoPathBusiness;
import com.baofeng.mj.vrplayer.util.GlideUtil;
import com.baofeng.mj.vrplayer.util.StringUtils;
import com.baofeng.mj.util.publicutil.VideoTypeUtil;
import com.baofeng.mj.vrplayer.widget.PinnedHeaderListView;
import com.baofeng.mj.vrplayer.widget.VideoTypeSelectPop;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.mojing.vrplayer.utils.SharedPreferencesTools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.List;

/**
 * 视频适配器
 */
public class VideoAdapter extends BaseAdapter implements OnScrollListener , PinnedHeaderListView.PinnedHeaderAdapter {
	private Context mContext;
	private Fragment fragment;
	private List<LocalVideoBean> localVideoBeanList;
	private LayoutInflater mLayoutInflater;
	private VideoTypeSelectPop videoTypeSelectPop;

	public VideoAdapter(Context mContext, Fragment fragment, List<LocalVideoBean> localVideoBeanList) {
		this.mContext = mContext;
		this.fragment = fragment;
		this.localVideoBeanList = localVideoBeanList;
		mLayoutInflater = LayoutInflater.from(mContext);
		videoTypeSelectPop = new VideoTypeSelectPop(mContext);
	}

	@Override
	public int getCount() {
		int size=0;
		if(localVideoBeanList!=null){
			for(LocalVideoBean localVideoBean:localVideoBeanList){
				if(localVideoBean.itemType==LocalVideoBean.ITEM_TYPE_ITEM&&localVideoBean.visible==LocalVideoBean.VISIBLE_TYPE_GONE){

				}else{
					size++;
				}
			}
		}
		return size;
	}




	@Override
	public Object getItem(int position) {
		int size=0;
		if(localVideoBeanList!=null){
			for(LocalVideoBean localVideoBean:localVideoBeanList){
				if(localVideoBean.itemType==LocalVideoBean.ITEM_TYPE_ITEM&&localVideoBean.visible==LocalVideoBean.VISIBLE_TYPE_GONE){

				}else{
					if(position==size){
						return localVideoBean;
					}
					size++;
				}
			}
		}
		return null;
		//return localVideoBeanList == null ? null : localVideoBeanList.get(position);
	}

	@Override
	public long getItemId(int position) {
		Log.v("mm","getItemId :"+position);
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if (convertView == null) {
			convertView = mLayoutInflater.inflate(R.layout.layout_video_item, null);
			viewHolder = new ViewHolder();
			viewHolder.tv_group_container=  convertView.findViewById(R.id.tv_group_container);
			viewHolder.tv_group_text = (TextView) convertView.findViewById(R.id.tv_group_text);
			viewHolder.tv_group_image = (ImageView) convertView.findViewById(R.id.tv_group_image);
			viewHolder.iv_video_img = new WeakReference<ImageView>((ImageView) convertView.findViewById(R.id.iv_video_img));
			viewHolder.tv_video_title = (TextView) convertView.findViewById(R.id.tv_video_title);
			viewHolder.tv_video_duration = (TextView) convertView.findViewById(R.id.tv_video_duration);
			viewHolder.ll_video_type = (LinearLayout) convertView.findViewById(R.id.ll_video_type);
			viewHolder.iv_video_type = (ImageView) convertView.findViewById(R.id.iv_video_type);
			viewHolder.newItemTag = (ImageView) convertView.findViewById(R.id.newItemTag);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		final LocalVideoBean localVideoBean =(LocalVideoBean) getItem(position);


		if(localVideoBean.videoDuration>0){
			viewHolder.tv_video_duration.setText( StringUtils.getStringTime((long)localVideoBean.videoDuration*1000) );
		}
	//	GlideUtil.displayImage(fragment, viewHolder.iv_video_img, localVideoBean.thumbPath, R.mipmap.video_ph_220_165);
		final WeakReference<ImageView> tempIv_video_img=viewHolder.iv_video_img;
	//	tempIv_video_img.get().setScaleType(ImageView.ScaleType.CENTER);
		if(TextUtils.isEmpty(localVideoBean.thumbPath)){
			tempIv_video_img.get().setImageResource(R.drawable.imageload_defaut_img);
		}else{
			//file:///storage/emulated/0/mojingVrPlayer/localVideoFolder/1303727399.png
			File file=new File(localVideoBean.thumbPath.substring(5));
			if(!file.exists()){
				tempIv_video_img.get().setImageResource(R.drawable.imageload_defaut_img);
			}
		}
		if(!TextUtils.isEmpty(localVideoBean.thumbPath)){
			com.bumptech.glide.Glide.with(mContext).load(localVideoBean.thumbPath).asBitmap().skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.ALL).into(new SimpleTarget<Bitmap>() {
				@Override
				public void onResourceReady(Bitmap bitmap, GlideAnimation<? super Bitmap> glideAnimation) {
					if(null != bitmap) {
						bitmap=com.baofeng.mj.vrplayer.util.ImageUtil.getRoundImage(bitmap,bitmap.getWidth(),bitmap.getHeight(),15f);
						tempIv_video_img.get().setImageBitmap(bitmap);
					}
				}
			});
		}

		if (needShowTitle(position) ) {
			int lastIndexOf = localVideoBean.group.lastIndexOf("/") + 1;
			String name = localVideoBean.group.substring(lastIndexOf);
			viewHolder.tv_group_text.setText(LocalVideoPathBusiness.convertName(name));
			viewHolder.tv_group_container.setVisibility(View.VISIBLE);
		} else {
			viewHolder.tv_group_container.setVisibility(View.GONE);
		}

		if(localVideoBean.itemType==LocalVideoBean.ITEM_TYPE_TITLE){
			convertView.findViewById(R.id.tv_item).setVisibility(View.GONE);
			convertView.findViewById(R.id.tv_group_container).setVisibility(View.VISIBLE);
			int lastIndexOf = localVideoBean.group.lastIndexOf("/") + 1;
			String name = localVideoBean.group.substring(lastIndexOf);
			viewHolder.tv_group_text.setText(LocalVideoPathBusiness.convertName(name));
	//		viewHolder.newItemTag.setVisibility(View.GONE);
			if(localVideoBean.visible==LocalVideoBean.VISIBLE_TYPE_VISIBLE){
				viewHolder.tv_group_image.setImageResource(R.mipmap.video_list_fold_normal);
			}else{
				viewHolder.tv_group_image.setImageResource(R.mipmap.video_list_unfold_normal);
			}
		}else{
			if(localVideoBean.visible==LocalVideoBean.VISIBLE_TYPE_VISIBLE){
				convertView.findViewById(R.id.tv_item).setVisibility(View.VISIBLE);
			}else{
				convertView.findViewById(R.id.tv_item).setVisibility(View.GONE);
			}
			convertView.findViewById(R.id.tv_group_container).setVisibility(View.GONE);
			if(MyAppliaction.getInstance().getmFileUploadBusiness().isShowNewOnItem(localVideoBean.name)){
				//viewHolder.newItemTag.setVisibility(View.VISIBLE);
				viewHolder.tv_video_title.setText(getSmallName(localVideoBean.name));
				Bitmap bitmap= BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.video_label_new);
				ImageSpan imgSpan = new ImageSpan(mContext,bitmap);
				SpannableString spanString = new SpannableString("icon");
				spanString.setSpan(imgSpan, 0, 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				viewHolder.tv_video_title.append(spanString);
			}else{
				viewHolder.tv_video_title.setText(getSmallName(localVideoBean.name));
//				viewHolder.newItemTag.setVisibility(View.GONE);
			}
			String lastPlayName=SharedPreferencesTools.getInstance(mContext).getLastVideoName();
			viewHolder.tv_video_title.setTextColor(mContext.getResources().getColor(R.color.colorGrayOne));
			if(!TextUtils.isEmpty(lastPlayName)&&!TextUtils.isEmpty(localVideoBean.name)&&localVideoBean.name.equals(lastPlayName)){
				viewHolder.tv_video_title.setTextColor(0xff508cfe);
			}
		}
		viewHolder.tv_group_container.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
					if(localVideoBeanList!=null){
						for(LocalVideoBean templocalVideoBean:localVideoBeanList){
							if(getSingleName(localVideoBean.group).equals(getSingleName(templocalVideoBean.group))){
								if(templocalVideoBean.visible==LocalVideoBean.VISIBLE_TYPE_VISIBLE) {
									templocalVideoBean.visible =LocalVideoBean.VISIBLE_TYPE_GONE;
								}else{
									templocalVideoBean.visible =LocalVideoBean.VISIBLE_TYPE_VISIBLE;
								}
							}
						}
						VideoAdapter.this.notifyDataSetChanged();
					}
				}
		});

		updateVideoType(viewHolder.iv_video_type, localVideoBean);//更新视频类型

		final ImageView iv_video_type = viewHolder.iv_video_type;
		viewHolder.ll_video_type.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				videoTypeSelectPop.showPop(v, localVideoBean.path, localVideoBean.videoType, new VideoTypeSelectPop.VideoTypeCallback() {
					@Override
					public void select(int videoType) {
						localVideoBean.videoType = videoType;
						updateVideoType(iv_video_type, localVideoBean);//更新视频类型
					}
				});
			}
		});
		return convertView;
	}

	public String getSmallName(String name){
		if(TextUtils.isEmpty(name)){
			return "";
		}
		if(name.length()>35){
			String nameStart=name.substring(0,15);
			String nameEnd=name.substring(name.length()-15,name.length());
			name=nameStart+"... "+nameEnd;//增加中文空格
			return name;
		}else{
			return name;
		}

	}

	public String getSingleName(String fullName){
		String name;
		int lastIndexOf = fullName.lastIndexOf("/") + 1;
		name = fullName.substring(lastIndexOf);
		return LocalVideoPathBusiness.convertName(name);
	}

	/**
	 * 更新视频类型
	 */
	private void updateVideoType(ImageView iv_video_type, LocalVideoBean localVideoBean){
		switch (localVideoBean.videoType){//视频类型
			case VideoTypeUtil.MJVideoPictureTypeSingle:
				iv_video_type.setImageResource(R.drawable.mj_vrplayer_selector_video_icon_2d);
				break;
			case VideoTypeUtil.MJVideoPictureTypeSideBySide:
			case VideoTypeUtil.MJVideoPictureTypeStacked:
				iv_video_type.setImageResource(R.drawable.mj_vrplayer_selector_video_icon_3d);
				break;
			case VideoTypeUtil.MJVideoPictureTypePanorama360:
			case VideoTypeUtil.MJVideoPictureTypePanorama3603DStacked:
			case VideoTypeUtil.MJVideoPictureTypePanorama3603DSide:
			case VideoTypeUtil.MJVideoPictureTypePanorama360Cube:
			case VideoTypeUtil.MJVideoPictureTypePanorama1803DSide:
				iv_video_type.setImageResource(R.drawable.mj_vrplayer_selector_video_icon_vr);
				break;
			default:
				iv_video_type.setImageResource(R.drawable.mj_vrplayer_selector_video_icon_film);
				break;
		}
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		if(view instanceof PinnedHeaderListView) {
			((PinnedHeaderListView) view).controlPinnedHeader(firstVisibleItem);
		}
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
	}


	@Override
	public int getPinnedHeaderState(int position) {
		if (getCount() == 0 || position < 0) {
			return PinnedHeaderListView.PinnedHeaderAdapter.PINNED_HEADER_GONE;
		}
		if (isMove(position)) {
			return PinnedHeaderListView.PinnedHeaderAdapter.PINNED_HEADER_PUSHED_UP;
		}
		return PinnedHeaderListView.PinnedHeaderAdapter.PINNED_HEADER_VISIBLE;
	}

	@Override
	public void configurePinnedHeader(View headerView, int position, int alpaha) {
		LocalVideoBean localVideoBean = localVideoBeanList.get(position);
		int lastIndexOf = localVideoBean.group.lastIndexOf("/") + 1;
		TextView tv_header = (TextView) headerView.findViewById(R.id.header);
		String name = localVideoBean.group.substring(lastIndexOf);
		tv_header.setText(LocalVideoPathBusiness.convertName(name));
	}

	private boolean needShowTitle(int position) {
		if (position == 0) {
			return true;
		}
		LocalVideoBean curLocalVideoBean = localVideoBeanList.get(position);
		LocalVideoBean preLocalVideoBean = localVideoBeanList.get(position - 1);
		if (curLocalVideoBean == null || preLocalVideoBean == null) {
			return false;
		}

		String curTitle = curLocalVideoBean.group;
		String preTitle = preLocalVideoBean.group;
		if (curTitle == null || preTitle == null) {
			return false;
		}
		if (curTitle.equals(preTitle)) {
			return false;
		}
		return true;
	}

	private boolean isMove(int position) {
		if(position + 1 >= localVideoBeanList.size()){
			return false;
		}
		LocalVideoBean curLocalVideoBean = localVideoBeanList.get(position);
		LocalVideoBean nextLocalVideoBean = localVideoBeanList.get(position + 1);
		if (curLocalVideoBean == null || nextLocalVideoBean == null) {
			return false;
		}
		String curTitle = curLocalVideoBean.group;
		String nextTitle = nextLocalVideoBean.group;
		if (curTitle == null || nextTitle == null) {
			return false;
		}
		if (curTitle.equals(nextTitle)) {
			return false;
		}
		return true;
	}

	private class ViewHolder {
		private View tv_group_container;
		private TextView tv_group_text;
		private ImageView tv_group_image;
		private WeakReference<ImageView> iv_video_img;
		private TextView tv_video_title;
		private TextView tv_video_duration;
		private LinearLayout ll_video_type;
		private ImageView iv_video_type;
		private ImageView newItemTag;
	}
}