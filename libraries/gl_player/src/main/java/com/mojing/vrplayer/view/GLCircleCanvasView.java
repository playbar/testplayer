package com.mojing.vrplayer.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

import com.bfmj.viewcore.render.GLConstant;
import com.bfmj.viewcore.view.GLImageView;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by dupengwei on 2016/7/22.
 * 圆形下载进度
 */
public class GLCircleCanvasView extends GLImageView {
    private Context mContext;
    private float mWidth = 60.0F;
    private float mHeight = 60.0F;
    private int mProcessColor;
    private int mBgResId ;
    private float mStrokeWidth = 6.0F;
    private int mTextSize = 0;
    private int mTextColor = Color.rgb(255, 255, 255);
    private int mTextStrokeWidth = 6;
    private RectF mRectF;
    private Paint mPaint;
    private float mSweepAngle = -360.0F;
    private boolean isShowText = true;
    private Bitmap mBgBitmap;
    public GLCircleCanvasView(Context context) {
        super(context);
        this.mContext = context;
        this.mRectF = new RectF();
        this.mPaint = new Paint();
        this.setLayoutParams(this.mWidth, this.mHeight);
    }

    public void setLayoutParams(float width, float height) {
        this.mWidth = width;
        this.mHeight = height;
        super.setLayoutParams( width, height);
    }

    public void setDirection(GLConstant.GLDirection direction) {
        if(direction == GLConstant.GLDirection.ANTICLOCKWISE) {
            this.mSweepAngle = -360.0F;
        } else {
            this.mSweepAngle = 360.0F;
        }

    }

    public void setAttribute(int processColor, int mBgResId, float strokeWidth) {
        if(processColor != 0) {
            this.mProcessColor = processColor;
        }

        if(mBgResId > -1) {
            this.mBgResId = mBgResId;
            mBgBitmap = getBitmap(mBgResId);
        }

        if(strokeWidth != 0.0F) {
            this.mStrokeWidth = strokeWidth;
        }

    }

    private Bitmap getBitmap(int bgResId){
        Bitmap bitmap = null;
        InputStream is = mContext.getResources().openRawResource(bgResId);
        try {
            bitmap = BitmapFactory.decodeStream(is);
        } finally {
            try {
                is.close();
            } catch (IOException var13) {
                var13.printStackTrace();
            }

        }

        return bitmap;
    }

    public void setTextAttribute(int textSize, int color, int textStrokeWidth) {
        if(textSize != 0) {
            this.mTextSize = textSize;
        }

        if(color != 0) {
            this.mTextColor = color;
        }

        if(textStrokeWidth != 0) {
            this.mTextStrokeWidth = textStrokeWidth;
        }

    }

    public void createView(float process) {
        this.setProcess(process);
    }

    public void setProcess(int process) {
        this.setProcess((float)process);
    }

    private void setCanvasSize(){
        if(this.mWidth != this.mHeight) {
            float bitmap = Math.min(this.mWidth, this.mHeight);
            this.mWidth = bitmap;
            this.mHeight = bitmap;
        }
    }
    public void setProcess(float process) {
       setCanvasSize();
        Bitmap bitmap;

        if(null != mBgBitmap){
            bitmap = Bitmap.createScaledBitmap(mBgBitmap,(int)this.mWidth, (int)this.mHeight,true);
//            bitmap = Bitmap.createBitmap(mBgBitmap,0,0,(int)this.mWidth, (int)this.mHeight);
//            bitmap = BitmapFactory.decodeResource(mContext.getResources(),
//                    mBgResId).copy(Bitmap.Config.ARGB_8888, true);
        }else {
            bitmap = Bitmap.createBitmap((int)this.mWidth, (int)this.mHeight, Bitmap.Config.ARGB_8888);
        }

//        bitmap.setWidth((int)this.mWidth);
//        bitmap.setHeight((int)this.mHeight);
//        Log.i("info","bitmap1=="+bitmap.getWidth()+"==height=="+bitmap.getHeight());
        Canvas canvas = new Canvas(bitmap);
        this.mPaint.setAntiAlias(true);
        this.mPaint.setColor(0);
        canvas.drawColor(0);
        this.mPaint.setStrokeWidth(this.mStrokeWidth);
        this.mPaint.setStyle(Paint.Style.STROKE);
        this.mRectF.left = this.mStrokeWidth / 2.0F + 2.0F;
        this.mRectF.top = this.mStrokeWidth / 2.0F + 2.0F;
        this.mRectF.right = this.mWidth - this.mStrokeWidth / 2.0F - 2.0F;
        this.mRectF.bottom = this.mHeight - this.mStrokeWidth / 2.0F - 2.0F;
//        Log.i("info","left="+mRectF.left+"==top=="+mRectF.top+"==right=="+mRectF.right+"==bottom=="+mRectF.bottom);
        canvas.drawArc(this.mRectF, -90.0F, this.mSweepAngle, false, this.mPaint);
        this.mPaint.setColor(this.mProcessColor);
        canvas.drawArc(this.mRectF, -90.0F, process / 100.0F * this.mSweepAngle, false, this.mPaint);
        if(this.isShowText) {
            this.mPaint.setColor(this.mTextColor);
            this.mPaint.setStrokeWidth((float)this.mTextStrokeWidth);
            String text = (int)process + "%";
            float textHeight = this.mTextSize == 0?this.mHeight / 4.0F:(float)this.mTextSize;
            this.mPaint.setTextSize(textHeight);
            float textWidth = (float)((int)this.mPaint.measureText(text, 0, text.length()));
            this.mPaint.setStyle(Paint.Style.FILL);
            canvas.drawText(text, this.mWidth / 2.0F - textWidth / 2.0F, this.mHeight / 2.0F + textHeight / 2.0F - 5.0F, this.mPaint);
        }

        this.setImage(bitmap);
//        bitmap = null;
    }
    public void setProcessImageView(float process , int mResId){
        setCanvasSize();
        Bitmap resIdBitmap = getBitmap(mResId);
        Bitmap bitmap;
        if(null != mBgBitmap){
            bitmap = Bitmap.createScaledBitmap(mBgBitmap,(int)this.mWidth, (int)this.mHeight,true);
//            bitmap1 = BitmapFactory.decodeResource(mContext.getResources(),
//                    mBgResId).copy(Bitmap.Config.ARGB_8888, true);
        }else {
            bitmap = Bitmap.createBitmap((int)this.mWidth, (int)this.mHeight, Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(bitmap);
        this.mPaint.setAntiAlias(true);
        this.mPaint.setColor(0);
        canvas.drawColor(0);
        this.mPaint.setStrokeWidth(this.mStrokeWidth);
        this.mPaint.setStyle(Paint.Style.STROKE);
        this.mRectF.left = this.mStrokeWidth / 2.0F + 2.0F;
        this.mRectF.top = this.mStrokeWidth / 2.0F + 2.0F;
        this.mRectF.right = this.mWidth - this.mStrokeWidth / 2.0F - 2.0F;
        this.mRectF.bottom = this.mHeight - this.mStrokeWidth / 2.0F - 2.0F;
        canvas.drawArc(this.mRectF, -90.0F, this.mSweepAngle, false, this.mPaint);
        this.mPaint.setColor(this.mProcessColor);
        canvas.drawArc(this.mRectF, -90.0F, process / 100.0F * this.mSweepAngle, false, this.mPaint);

        if(null != resIdBitmap){
            canvas.drawBitmap(resIdBitmap,null,mRectF,this.mPaint);
        }

        this.setImage(bitmap);
    }
    public boolean isShowText() {
        return this.isShowText;
    }

    public void setShowText(boolean isShowText) {
        this.isShowText = isShowText;
    }
}
