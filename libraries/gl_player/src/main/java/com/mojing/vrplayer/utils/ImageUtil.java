package com.mojing.vrplayer.utils;

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


import com.mojing.vrplayer.publicc.VideoTypeUtil;
import com.mojing.vrplayer.utils.publicutil.VideoRecognizeUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 图片操作类
 */
public class ImageUtil {

	/**
	 * 创建视频类型
	 */
	public static long getVideoDuration(final MediaMetadataRetriever retriever, final String videoPath) {
		try {
			if(retriever != null){
				retriever.setDataSource(videoPath);
				String durationString = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
				long totalDuration = Long.valueOf(durationString) / 1000;//视频总时长为秒
				return totalDuration;
			}
		} catch (IllegalArgumentException ex) {
		} catch (RuntimeException ex) {
		} catch (Exception ex) {
		}
		return 0;//默认值
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