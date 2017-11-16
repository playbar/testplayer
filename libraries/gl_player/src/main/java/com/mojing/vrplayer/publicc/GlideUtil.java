package com.mojing.vrplayer.publicc;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bfmj.viewcore.view.GLImageView;
import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.mojing.vrplayer.activity.GLBaseActivity;
import com.mojing.vrplayer.utils.ImageUtil;

import java.lang.ref.WeakReference;

/**
 * Created by liuchuanchi on 2016/10/24.
 * 网址：http://blog.csdn.net/shangmingchao/article/details/51125554
 * Glide基本可以load任何可以拿到的媒体资源，如：
 * load SD卡资源：load("file://"+ Environment.getExternalStorageDirectory().getPath()+"/test.jpg")
 * load assets资源：load("file:///android_asset/f003.gif")
 * load raw资源：load("Android.resource://com.frank.glide/raw/raw_1")或load("android.resource://com.frank.glide/raw/"+R.raw.raw_1)
 * load drawable资源：load("android.resource://com.frank.glide/drawable/news")或load("android.resource://com.frank.glide/drawable/"+R.drawable.news)
 * load ContentProvider资源：load("content://media/external/images/media/139469")
 * load http资源：load("http://img.my.csdn.net/uploads/201508/05/1438760757_3588.jpg")
 * load https资源：load("https://img.alicdn.com/tps/TB1uyhoMpXXXXcLXVXXXXXXXXXX-476-538.jpg_240x5000q50.jpg_.webp")
 * 当然，load不限于String类型，还可以：
 * load(Uri uri)，load(File file)，load(Integer resourceId)，load(URL url)，load(byte[] model)，load(T model)，loadFromMediaStore(Uri uri)。
 * load的资源也可以是本地视频，如果想要load网络视频或更高级的操作可以使用VideoView等其它控件完成。
 */
public class GlideUtil {

    /**
     * ImageView是固定大小时，推荐用此方法（Glide框架会根据ImageView大小，自动裁剪再加载）
     */
    public static void displayImage(Context context, WeakReference<ImageView> weakIv, int drawable, int defaultDrawable){
        displayImage(context, null, weakIv, null, drawable, defaultDrawable, 0, 0);
    }

    /**
     * ImageView是固定大小时，推荐用此方法（Glide框架会根据ImageView大小，自动裁剪再加载）
     */
    public static void displayImage(Fragment fragment, WeakReference<ImageView> weakIv, int drawable, int defaultDrawable){
        displayImage(null, fragment, weakIv, null, drawable, defaultDrawable, 0, 0);
    }

    /**
     * ImageView不是固定大小时，推荐用此方法（裁剪后再加载）
     */
    public static void displayImage(Context context, WeakReference<ImageView> weakIv, int drawable, int defaultDrawable, int width, int height){
        displayImage(context, null, weakIv, null, drawable, defaultDrawable, width, height);
    }

    /**
     * ImageView不是固定大小时，推荐用此方法（裁剪后再加载）
     */
    public static void displayImage(Fragment fragment, WeakReference<ImageView> weakIv, int drawable, int defaultDrawable, int width, int height){
        displayImage(null, fragment, weakIv, null, drawable, defaultDrawable, width, height);
    }

    /**
     * ImageView是固定大小时，推荐用此方法（Glide框架会根据ImageView大小，自动裁剪再加载）
     */
    public static void displayImage(Context context, WeakReference<ImageView> weakIv, String url, int defaultDrawable){
        displayImage(context, null, weakIv, url, 0, defaultDrawable, 0, 0);
    }

    /**
     * 开屏图用
     */
    public static void displayImageSplash(Context context, WeakReference<ImageView> weakIv, String url, int defaultDrawable){
        displayImageSplash(context, weakIv, url, defaultDrawable, 0, 0);
    }

    /**
     * ImageView是固定大小时，推荐用此方法（Glide框架会根据ImageView大小，自动裁剪再加载）
     */
    public static void displayImage(Fragment fragment, WeakReference<ImageView> weakIv, String url, int defaultDrawable){
        displayImage(null, fragment, weakIv, url, 0, defaultDrawable, 0, 0);
    }

    /**
     * ImageView不是固定大小时，推荐用此方法（裁剪后再加载）
     */
    public static void displayImage(Context context, WeakReference<ImageView> weakIv, String url, int defaultDrawable, int width, int height){
        displayImage(context, null, weakIv, url, 0, defaultDrawable, width, height);
    }

    /**
     * ImageView不是固定大小时，推荐用此方法（裁剪后再加载）
     */
    public static void displayImage(Fragment fragment, WeakReference<ImageView> weakIv, String url, int defaultDrawable, int width, int height){
        displayImage(null, fragment, weakIv, url, 0, defaultDrawable, width, height);
    }

    /**
     * 加载图片
     * @param context Context
     * @param fragment Fragment
     * @param weakIv ImageView
     * @param url 图片path
     * @param drawable 图片drawable
     * @param defaultDrawable 默认drawable
     * @param width 图片宽（ImageView的宽）
     * @param height 图片高（ImageView的高）
     */
    private static void displayImage(Context context, Fragment fragment, WeakReference<ImageView> weakIv, String url, int drawable, int defaultDrawable, int width, int height){
        if(weakIv == null || weakIv.get() == null){
            return;
        }
        RequestManager requestManager;
        if(context == null){
            requestManager = Glide.with(fragment);
        }else{
            requestManager = Glide.with(context);
        }
        DrawableRequestBuilder builder;

        if(drawable > 0){//drawable图片
            builder = requestManager.load(drawable).centerCrop().placeholder(defaultDrawable).skipMemoryCache(true).crossFade().diskCacheStrategy(DiskCacheStrategy.RESULT);
        }else if(!TextUtils.isEmpty(url) && url.endsWith(".gif")){//gif动态图（磁盘缓存策略用DiskCacheStrategy.SOURCE，不然加载会很慢）
            builder = requestManager.load(url).centerCrop().placeholder(defaultDrawable).skipMemoryCache(true).crossFade().diskCacheStrategy(DiskCacheStrategy.SOURCE);
        }else{//普通图
            builder = requestManager.load(url).centerCrop().placeholder(defaultDrawable).skipMemoryCache(true).crossFade().diskCacheStrategy(DiskCacheStrategy.ALL);
        }

        if(width == 0 || height == 0){
            builder.into(weakIv.get());
        }else{
            builder.override(width, height).into(weakIv.get());
        }
    }

    /**
     * Splash显示不能用 centerCrop()
     * @param context Context
     * @param weakIv ImageView
     * @param url 图片path
     * @param defaultDrawable 默认drawable
     * @param width 图片宽（ImageView的宽）
     * @param height 图片高（ImageView的高）
     */
    private static void displayImageSplash(Context context, WeakReference<ImageView> weakIv, String url, int defaultDrawable, int width, int height){
        if(weakIv == null || weakIv.get() == null){
            return;
        }
        RequestManager requestManager;
            requestManager = Glide.with(context);
        DrawableRequestBuilder builder;

        if(!TextUtils.isEmpty(url) && url.endsWith(".gif")){//gif动态图（磁盘缓存策略用DiskCacheStrategy.SOURCE，不然加载会很慢）
            builder = requestManager.load(url).centerCrop().placeholder(defaultDrawable).skipMemoryCache(true).crossFade().diskCacheStrategy(DiskCacheStrategy.SOURCE);
        }else{//普通图
            builder = requestManager.load(url).fitCenter().placeholder(defaultDrawable).skipMemoryCache(true).crossFade().diskCacheStrategy(DiskCacheStrategy.ALL);
        }

        if(width == 0 || height == 0){
            builder.into(weakIv.get());
        }else{
            builder.override(width, height).into(weakIv.get());
        }
    }

    //仅在线加载的图片 需要检测加载结果的使用该方法
    public static void displayImage(Context context, WeakReference<ImageView> weakIv, String url, int defaultDrawable, int width, int height,RequestListener listener){
        if(weakIv == null || weakIv.get() == null){
            return;
        }
        if(context==null)
            return;
        if(context instanceof Activity && ((Activity)context).isFinishing())
            return;

        RequestManager requestManager;
        requestManager = Glide.with(context);
        DrawableRequestBuilder builder;
         if(!TextUtils.isEmpty(url) && url.endsWith(".gif")){//gif动态图（磁盘缓存策略用DiskCacheStrategy.SOURCE，不然加载会很慢）
            builder = requestManager.load(url).centerCrop().placeholder(defaultDrawable).skipMemoryCache(true).crossFade().diskCacheStrategy(DiskCacheStrategy.SOURCE);
        }else{//普通图
            builder = requestManager.load(url).centerCrop().placeholder(defaultDrawable).skipMemoryCache(true).crossFade().diskCacheStrategy(DiskCacheStrategy.ALL);
        }
        if(listener!=null) {
            builder.listener(listener);
        }

        if(width == 0 || height == 0){
            builder.into(weakIv.get());
        }else{
            builder.override(width, height).into(weakIv.get());
        }
    }


        /**
         * 加载Bitmap
         */
    public static void loadBitmap(Context context, String url, SimpleTarget<Bitmap> simpleTarget){
        Glide.with(context).load(url).asBitmap().skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.ALL).into(simpleTarget);
    }

    /**
     * 加载bitmap图片，GLImageView专用
     */
    public static void loadBitmap2(final Context context, final GLImageView imageView, final String url){
        ((GLBaseActivity)context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Glide.with(context).load(url).asBitmap().skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.ALL).into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap bitmap, GlideAnimation<? super Bitmap> glideAnimation) {
                        if(null != bitmap) {
                            imageView.setImage(bitmap);
                        }
                    }
                });
            }
        });
//        Glide.with(context).load(url).asBitmap().skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.ALL).into(new SimpleTarget<Bitmap>() {
//            @Override
//            public void onResourceReady(Bitmap bitmap, GlideAnimation<? super Bitmap> glideAnimation) {
//                imageView.setImage(bitmap);
//            }
//        });
    }
    /**
     * 加载bitmap图片，GLImageView专用
     */
    public static void loadBitmap3(final Context context, final GLImageView imageView, final String url){
        ((GLBaseActivity)context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Glide.with(context).load(url).asBitmap().skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.ALL).into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap bitmap, GlideAnimation<? super Bitmap> glideAnimation) {
                        if(null != bitmap) {
                            bitmap=ImageUtil.getRoundImage(bitmap,bitmap.getWidth(),bitmap.getHeight(),40f);
                            imageView.setImage(bitmap);
                        }
                    }
                });
            }
        });
//        Glide.with(context).load(url).asBitmap().skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.ALL).into(new SimpleTarget<Bitmap>() {
//            @Override
//            public void onResourceReady(Bitmap bitmap, GlideAnimation<? super Bitmap> glideAnimation) {
//                imageView.setImage(bitmap);
//            }
//        });
    }
}
