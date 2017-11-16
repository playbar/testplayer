package com.baofeng.mj.vrplayer.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Handler;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

import com.baofeng.mj.vrplayer.R;

import java.lang.ref.WeakReference;

/**
 * Created by zhaominglei on 2016/5/24.
 * 飞屏搜索动画
 */
public class FlyScreenSearchProgressBar extends AppCompatImageView {

    private Bitmap mBgBitmap, mFgBitmap, mMask;

    private WeakReference<Bitmap> mWeakFgBitmap, mWeakBgBitmap, mWeakMask;
    private Paint mMaskPaint, mImagePaint;
    private float mAngle = 5;
    private RectF mBounds;
    private float mCircleRatio = 0.89337f;


    public FlyScreenSearchProgressBar(Context context) {
        super(context);
        init();
    }

    public FlyScreenSearchProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mFgBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.flyscreen_search_foreground);
        mBgBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.flyscreen_search_background);
        mMask = BitmapFactory.decodeResource(getResources(), R.mipmap.flyscreen_search_mask);

        mWeakBgBitmap = new WeakReference<Bitmap>(mFgBitmap);
        mWeakFgBitmap = new WeakReference<Bitmap>(mBgBitmap);
        mWeakMask = new WeakReference<Bitmap>(mMask);

        mMaskPaint = new Paint();
        mMaskPaint.setAntiAlias(true);
        mMaskPaint.setFilterBitmap(true);
        mMaskPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OUT));
        mImagePaint = new Paint();
        mImagePaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
    }

    private Rect getSourceBoundRect(Bitmap source) {
        int left = 0, top = 0, right = 0, bottom = 0;
        ScaleType scaleType = getScaleType();
        double sourceRatio = source.getWidth() / source.getHeight();
        double targetRatio = 1.0;

        switch (scaleType) {
            case CENTER_CROP:
                if (sourceRatio >= targetRatio) {
                    bottom = source.getHeight();
                    int rectWidth = (int) (bottom * targetRatio);
                    left = (source.getWidth() - rectWidth) / 2;
                    right = left + rectWidth;
                } else {
                    right = source.getWidth();
                    int rectHeight = (int) (right / targetRatio);
                    top = (source.getHeight() - rectHeight) / 2;
                    bottom = top + rectHeight;
                }
                break;
            default:
                right = source.getWidth();
                bottom = source.getHeight();
                break;
        }

        return new Rect(left, top, right, bottom);
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mWeakFgBitmap.get() == null) {
            mWeakFgBitmap = new WeakReference<Bitmap>(BitmapFactory.decodeResource(getResources(), R.mipmap.flyscreen_search_foreground));
        }
        if (mWeakFgBitmap.get() == null) {
            return;
        }
        if (mWeakBgBitmap.get() == null) {
            mWeakBgBitmap = new WeakReference<Bitmap>(BitmapFactory.decodeResource(getResources(), R.mipmap.flyscreen_search_background));
        }
        if (mWeakBgBitmap.get() == null) {
            return;
        }
        if (mWeakMask.get() == null) {
            mWeakMask = new WeakReference<Bitmap>(BitmapFactory.decodeResource(getResources(), R.mipmap.flyscreen_search_mask));
        }
        if (mWeakMask.get() == null) {
            return;
        }
        Rect rect = getSourceBoundRect(mWeakBgBitmap.get());
        canvas.drawBitmap(mWeakBgBitmap.get(), rect, mBounds, null);

        int saveFlags = Canvas.MATRIX_SAVE_FLAG
                | Canvas.CLIP_SAVE_FLAG
                | Canvas.HAS_ALPHA_LAYER_SAVE_FLAG
                | Canvas.FULL_COLOR_LAYER_SAVE_FLAG
                | Canvas.CLIP_TO_LAYER_SAVE_FLAG;
        int src = canvas.saveLayer(0, 0, canvas.getWidth(), canvas.getHeight(), null, saveFlags);
        //遮罩半径
        int circleRadius = (int) (mBounds.width() * mCircleRatio * 0.5);
        //遮罩圆周运动的半径
        float smallRadius = (mBounds.width() - circleRadius) * 0.5f;

        float x = (float) (Math.cos(mAngle * Math.PI / 180F) * smallRadius);
        float y = (float) (Math.sin(mAngle * Math.PI / 180F) * smallRadius);

        canvas.drawBitmap(mWeakMask.get(), getSourceBoundRect(mWeakMask.get()), new Rect(
                        (int) (mBounds.centerX() - circleRadius - x),
                        (int) (mBounds.centerY() - circleRadius + y),
                        (int) (mBounds.centerX() + circleRadius - x),
                        (int) (mBounds.centerY() + circleRadius + y)),
                mMaskPaint);
        canvas.drawBitmap(mWeakFgBitmap.get(), rect, mBounds, mImagePaint);
        canvas.restoreToCount(src);
        circularMotion();
    }

    /***
     * 圆周运动
     */
    private void circularMotion() {
        mAngle = ((mAngle -= 10) % 360);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                invalidate();
            }
        }, 100);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(measureWidth(widthMeasureSpec), measureHeight(heightMeasureSpec));
    }

    private int measureHeight(int measureSpec) {
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        int result = 88;
        if (specMode == MeasureSpec.AT_MOST) {
            result = specSize;
        } else if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        }
        return result;
    }


    private int measureWidth(int measureSpec) {
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        int result = 50;
        if (specMode == MeasureSpec.AT_MOST) {
            result = specSize;
        } else if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        }
        return result;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mBounds = new RectF(getLeft(), getTop(), getRight(), getBottom());
    }
}
