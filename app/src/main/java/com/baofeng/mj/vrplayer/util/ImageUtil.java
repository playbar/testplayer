package com.baofeng.mj.vrplayer.util;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.media.MediaMetadataRetriever;
import android.text.TextUtils;
import android.util.Log;

import com.baofeng.mj.util.publicutil.VideoRecognizeUtil;
import com.baofeng.mj.util.publicutil.VideoTypeUtil;
import com.baofeng.mj.vrplayer.business.LocalVideoPathBusiness;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 图片操作类
 */
public class ImageUtil {
	/**
	 * 保存bitmap
	 * @param bitmap
	 * @param file
	 */
	public static void saveBitmap(Bitmap bitmap, File file){
		saveBitmap(bitmap, file, 60);
	}

	/**
	 * 保存bitmap
	 * @param bitmap
	 * @param file
	 * @param scaling
	 */
	public static void saveBitmap(Bitmap bitmap, File file, int scaling){
		if (bitmap == null || file == null || file.exists()){
			return;
		}
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(file);
			bitmap.compress(Bitmap.CompressFormat.JPEG, scaling, fos);
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			if (fos != null) {
				fos.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 将资源文件drawable转为bitmap
	 * @param mContext 上下文
	 * @param res
	 * @return
	 */
	public static Bitmap getBitmap(Context mContext, int res) {
		Resources resources = mContext.getResources();
		return BitmapFactory.decodeResource(resources, res, getBitmapFactoryOptions());
	}

	/**
	 * 获取图片的缩略图
	 * @param mContext 上下文
	 * @param res 资源文件drawable
	 * @param thumbWidth 缩略图的宽
	 * @param extraScaling 额外可以加的缩放比例
	 * @return bitmap 指定宽高的缩略图
	 */
	public static Bitmap getThumbBitmap(Context mContext, int res, int thumbWidth, int extraScaling) {
		try {
			Resources resources = mContext.getResources();
			BitmapFactory.Options outOptions = getBitmapFactoryOptions(mContext, res, thumbWidth, extraScaling);
			return BitmapFactory.decodeResource(resources, res, outOptions);
		} catch (Throwable t) {
			t.printStackTrace();
		}
		return null;
	}

	/**
	 * 获取图片的缩略图
	 * @param localImagePath 图片的路径
	 * @param thumbWidth 缩略图的宽
	 * @param extraScaling 额外可以加的缩放比例
	 * @return bitmap 指定宽高的缩略图
	 */
	public static Bitmap getThumbBitmap(String localImagePath, int thumbWidth, int extraScaling) {
		if (TextUtils.isEmpty(localImagePath)) {
			return null;
		}
		BitmapFactory.Options outOptions = getBitmapFactoryOptions(localImagePath, thumbWidth, extraScaling);
		try {
			return BitmapFactory.decodeFile(localImagePath, outOptions);
		} catch (Throwable t) {
			t.printStackTrace();
		}
		return null;
	}

	public static BitmapFactory.Options getBitmapFactoryOptions(){
		BitmapFactory.Options outOptions = new BitmapFactory.Options();
		outOptions.inSampleSize = 1;
		outOptions.inPurgeable = true;
		outOptions.inInputShareable = true;
		outOptions.inPreferredConfig = Bitmap.Config.RGB_565;
		return outOptions;
	}

	public static BitmapFactory.Options getBitmapFactoryOptions(Context mContext, int res, int thumbWidth, int extraScaling){
		BitmapFactory.Options outOptions = new BitmapFactory.Options();
		outOptions.inJustDecodeBounds = true;// 设置该属性为true，不加载图片到内存，只返回图片的宽高到options中
		try {
			BitmapFactory.decodeResource(mContext.getResources(), res, outOptions);
		} catch (Throwable t) {
			t.printStackTrace();
		}
		if (outOptions.outWidth > thumbWidth) {
			outOptions.inSampleSize = outOptions.outWidth / thumbWidth + 1 + extraScaling;
			outOptions.outWidth = thumbWidth;
			outOptions.outHeight = outOptions.outHeight / outOptions.inSampleSize;
		}
		outOptions.inJustDecodeBounds = false;// 重新设置该属性为false，加载图片返回
		outOptions.inPurgeable = true;
		outOptions.inInputShareable = true;
		outOptions.inPreferredConfig = Bitmap.Config.RGB_565;
		return outOptions;
	}

	public static BitmapFactory.Options getBitmapFactoryOptions(String imagePath, int thumbWidth, int extraScaling){
		BitmapFactory.Options outOptions = new BitmapFactory.Options();
		outOptions.inJustDecodeBounds = true;// 设置该属性为true，不加载图片到内存，只返回图片的宽高到options中
		try {
			BitmapFactory.decodeFile(imagePath, outOptions);
		} catch (Throwable t) {
			t.printStackTrace();
		}
		if (outOptions.outWidth > thumbWidth) {
			outOptions.inSampleSize = outOptions.outWidth / thumbWidth + 1 + extraScaling;
			outOptions.outWidth = thumbWidth;
			outOptions.outHeight = outOptions.outHeight / outOptions.inSampleSize;
		}
		outOptions.inJustDecodeBounds = false;// 重新设置该属性为false，加载图片返回
		outOptions.inPurgeable = true;
		outOptions.inInputShareable = true;
		outOptions.inPreferredConfig = Bitmap.Config.RGB_565;
		return outOptions;
	}

	/**
	 * 图片缩放
	 * @param bitmap
	 * @param newWidth
	 * @param newHeight
	 * @return
	 */
	public static Bitmap zoomBitmap(Bitmap bitmap, float newWidth, float newHeight) {
		float width = bitmap.getWidth();
		float height = bitmap.getHeight();
		float scaleWidth = ((float) newWidth) / width;
		//float scaleHeight = ((float) newHeight) / height;
		float scaleHeight = ((float) newWidth) / width;
		Matrix matrix = new Matrix();
		matrix.postScale(scaleWidth, scaleHeight);// width
		return Bitmap.createBitmap(bitmap, 0, 0, (int) width, (int) height, matrix, true);
	}


//	public static Bitmap ImageCrop(Bitmap bitmap, boolean isRecycled)
	public static Bitmap ImageCrop(Bitmap bitmap,boolean isRecycled) {
		if (bitmap == null)
		{
			return null;
		}
		int w = bitmap.getWidth(); // 得到图片的宽，高
		int h = bitmap.getHeight();
		int wh = w > h ? h : w;// 裁切后所取的正方形区域边长
		int retX = w > h ? (w - h) / 2 : 0;// 基于原图，取正方形左上角x坐标
		int retY = w > h ? 0 : (h - w) / 2;
		Bitmap bmp = Bitmap.createBitmap(bitmap, retX, retY, wh, wh, null,
				false);
		Bitmap bmp2 = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight()*2/3, null,
				false);
		if (isRecycled && bitmap != null && !bitmap.equals(bmp)
				&& !bitmap.isRecycled()) {
			bitmap.recycle();
			bitmap = null;
		}
		if (isRecycled && bmp != null && !bmp.equals(bmp2)
				&& !bmp.isRecycled()) {
			bmp.recycle();
			bmp = null;
		}
		// 下面这句是关键
		return bmp2;// Bitmap.createBitmap(bitmap, retX, retY, wh, wh, null,
		// false);
	}


	/**
	 * 压缩图片大小decode
	 * @param options 选项
	 * @param minSideLength 最小长度
	 * @param maxNumOfPixels 大小
	 * @return int
	 */
	public static int computeSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels) {
		int initialSize = computeInitialSampleSize(options, minSideLength, maxNumOfPixels);
		int roundedSize;
		if (initialSize <= 8) {
			roundedSize = 1;
			while (roundedSize < initialSize) {
				roundedSize <<= 1;
			}
		} else {
			roundedSize = (initialSize + 7) / 8 * 8;
		}
		return roundedSize;
	}

	/**
	 * 计算
	 * @author linzanxian @Date 2015年1月16日 下午6:05:38
	 *         description:压缩图片大小
	 * @param options 选项
	 * @param minSideLength 最小长度
	 * @param maxNumOfPixels 大小
	 * @return int
	 */
	private static int computeInitialSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels) {
		double w = options.outWidth;
		double h = options.outHeight;

		int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math
				.sqrt(w * h / maxNumOfPixels));
		int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(
				Math.floor(w / minSideLength), Math.floor(h / minSideLength));

		if (upperBound < lowerBound) {
			return lowerBound;
		}

		if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
			return 1;
		} else if (minSideLength == -1) {
			return lowerBound;
		} else {
			return upperBound;
		}
	}

	/**
	 * 合并Bitmap
	 * @author linzanxian @Date 2015年1月28日 下午4:16:12
	 *         description:合并Bitmap
	 * @param bg背景层
	 * @param icon图标层
	 * @return Bitmap
	 */
	public static Bitmap combineBitmap(Bitmap bg, Bitmap icon) {
		if (bg != null && icon != null) {
			Paint paint = new Paint();
			Canvas canvas = new Canvas(bg);
			int b1w = bg.getWidth();
			int b1h = bg.getHeight();
			int b2w = icon.getWidth();
			int b2h = icon.getHeight();
			int bx = (b1w - b2w) / 2;
			int by = (b1h - b2h) / 2;
			canvas.drawBitmap(icon, bx, by, paint);
			// 叠加新图icon 并且居中
			canvas.save(Canvas.ALL_SAVE_FLAG);
			canvas.restore();
		}
		return bg;
	}
	
	/*
	 * 旋转图片
	 * @param angle
	 * @param bitmap
	 * @return Bitmap
	 */
	public static Bitmap rotaingImageView(int angle, Bitmap bitmap) {
		Matrix matrix = new Matrix();
		matrix.postRotate(angle);
		return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
	}

	/**
	 * 创建视频缩略图
	 */
	public static boolean createVideoThumbnail(Context mContext, MediaMetadataRetriever retriever, final String videoPath, final int width, final int height) {
		File file = new File(LocalVideoPathBusiness.getLocalVideoImg(mContext, videoPath));//缩略图
		if(!file.exists()){
			try {
				if (retriever != null) {
					retriever.setDataSource(videoPath);
//				String mime = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_MIMETYPE);
//				String bitrate = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_BITRATE);
//				String timeString = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
//				long time = Long.parseLong(timeString) * 1000;
					Bitmap bitmap = retriever.getFrameAtTime(-1); // 按视频长度比例选择帧
//					String  intWidth =retriever.extractMetadata(android.media.MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH);
//					String  intHeight =retriever.extractMetadata(android.media.MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT);
//					Log.v("widhtmy",intWidth);
//					Log.v("widhtmy",HEIGHT);
					if (bitmap != null) {
						bitmap=ImageCrop(bitmap,false);
						bitmap = zoomBitmap(bitmap, 480, 480);//压缩bitmap
						saveBitmap(bitmap, file);//保存bitmap到本地
						return true;
					}
				}
			} catch (IllegalArgumentException ex) {
				ex.printStackTrace();
			} catch (RuntimeException ex) {
				ex.printStackTrace();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return false;
	}

	/**
	 * 创建视频类型
	 */
	public static int createVideoType(final MediaMetadataRetriever retriever, final String videoPath) {
		try {
			if(retriever != null){
				retriever.setDataSource(videoPath);
				String durationString = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
				long totalDuration = Long.valueOf(durationString) * 1000;//视频总时长（微秒）
				int videoType1 = getVideoType(retriever, totalDuration / 4);//视频类型1
				int videoType2 = getVideoType(retriever, totalDuration / 2);//视频类型2
				int videoType3 = getVideoType(retriever, totalDuration / 4 * 3);//视频类型3
				if(videoType1 == videoType2){
					return videoType1;
				}
				if(videoType1 == videoType3){
					return videoType1;
				}
				if(videoType2 == videoType3){
					return videoType2;
				}
				return videoType2;
			}
		} catch (IllegalArgumentException ex) {
		} catch (RuntimeException ex) {
		} catch (Exception ex) {
		}
		return VideoTypeUtil.MJVideoPictureTypeSingle;//默认值
	}

	/**
	 * 获取视频类型
	 */
	private static int getVideoType(MediaMetadataRetriever retriever, long duration){
		try {
			if(retriever != null){
				Bitmap bitmap = retriever.getFrameAtTime(duration, MediaMetadataRetriever.OPTION_CLOSEST_SYNC);
				if(bitmap != null){
					return VideoRecognizeUtil.getVideoType(bitmap);//获取视频类型
				}
			}
		} catch (Exception e) {
		}
		return VideoTypeUtil.MJVideoPictureTypeSingle;//默认值
	}

	/**
	 * 获取圆角位图的方法
	 * @param bitmap 需要转化成圆角的位图
	 * @param pixels 圆角的度数，数值越大，圆角越大
	 * @return 处理后的圆角位图
	 */
	public static Bitmap toRoundCorner(Context mContext, Bitmap bitmap, int pixels, int defaultRes) {
		if(bitmap == null){
			bitmap = getBitmap(mContext, defaultRes);
		}
		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
				bitmap.getHeight(), Bitmap.Config.RGB_565);
		Canvas canvas = new Canvas(output);
		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);
		final float roundPx = pixels;
		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
		paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);
		return output;
	}

	public static int readPictureDegree(String path) {
		int degree = 0;
		try {
			ExifInterface exifInterface = new ExifInterface(path);
			int orientation = exifInterface.getAttributeInt(
					ExifInterface.TAG_ORIENTATION,
					ExifInterface.ORIENTATION_NORMAL);
			switch (orientation) {
				case ExifInterface.ORIENTATION_ROTATE_90:
					degree = 90;
					break;
				case ExifInterface.ORIENTATION_ROTATE_180:
					degree = 180;
					break;
				case ExifInterface.ORIENTATION_ROTATE_270:
					degree = 270;
					break;
				default:
					break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return degree;
	}

	/**
	 * 将图片绘制成四周圆角
	 * @param bitmap
	 * @param width
	 * @param height
	 * @param rid
	 * @return
	 */
	public static Bitmap getRoundImage(Bitmap bitmap,int width,int height,float rid) {
		Drawable imageDrawable = new BitmapDrawable(bitmap);

		Bitmap output = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_4444);
		Canvas canvas = new Canvas(output);

		// 新建一个矩形
		RectF outerRect = new RectF(0, 0, width, height);

		// 产生一个红色的圆角矩形
		Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		//paint.setColor(Color.RED);
//        canvas.drawRoundRect(outerRect, rid, rid, paint);

		Path path = new Path();
		//向路径中添加圆角矩形。radii数组定义圆角矩形的四个圆角的x,y半径。radii长度必须为8
		//圆角的半径，依次为左上角xy半径，右上角，右下角，左下角
		float[] rids = {rid,rid,rid,rid,rid,rid,rid,rid};//上面圆角，底部直角
		path.addRoundRect(outerRect,rids,Path.Direction.CW);//Path.Direction.CW,顺时针绘制
		canvas.drawPath(path, paint);

		// 将源图片绘制到这个圆角矩形上
		paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
		imageDrawable.setBounds(0, 0, width, height);
		canvas.saveLayer(outerRect, paint, Canvas.ALL_SAVE_FLAG);
		imageDrawable.draw(canvas);
		canvas.restore();

		return output;
	}
}