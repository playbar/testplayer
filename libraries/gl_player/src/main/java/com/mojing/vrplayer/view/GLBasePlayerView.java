package com.mojing.vrplayer.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Surface;

import com.bfmj.viewcore.view.GLPlayerView;
import com.mojing.vrplayer.utils.MJGLUtils;
import com.storm.smart.play.baseplayer.BaseSoftPlayer;
import com.storm.smart.play.baseplayer.BaseSurfacePlayer;
import com.storm.smart.play.call.BaofengPlayerListener;
import com.storm.smart.play.call.IBfPlayerConstant;
import com.storm.smart.play.call.PlayerWithoutSurfaceFactory;

/**
 *
 * ClassName: GLSystemPlayer <br/>
 * @author lixianke
 * @date: 2015-3-17 上午10:21:34 <br/>
 * description:
 */
public class GLBasePlayerView extends GLPlayerView {
	private static final String TAG = "GLSoftPlayer";
	private Activity mContext;

	private double mPhysicalSize;
	//private InternalHandler handler;
	private boolean isPlayerPrepared;
	protected int mAspect;
	private boolean isAttatched;
	private boolean hasReportPrepare;
	private int surfaceWidth;
	private int surfaceHeight;
	private int windowWidth;
	private int windowHeight;

	// 软解有时Seek后不来seekComplete消息
    private boolean isSeekCompleteMsgMissing;
    private boolean coreContextInited;

	private int rightEarMode;// 右耳模式
	private String subInfo;
	BaseSurfacePlayer mediaPlayer;


	// Notify Codec Type Value
    public static final int STP_JNI_NTFY_V_DEC_SOFT = 0;
	private int decodeType = IBfPlayerConstant.IBasePlayerType.TYPE_SYS;
	private BaofengPlayerListener listener;
	public GLBasePlayerView(Context context, BaofengPlayerListener listener, int decodeType) {
		super(context);
		mContext = (Activity)context;
		this.listener = listener;
		this.decodeType = decodeType;
		init();
	}

	private void init(){
		((Activity)getContext()).runOnUiThread(new Runnable() {
			@Override
			public void run() {
				setScreenMode(IBfPlayerConstant.IScreenMode.MODE_ORIGINAL);
				getPhysicalResolution();
				createPlayer();
			}
		});


	}

	public void setDecodeType(int decodeType) {
		this.decodeType = decodeType;
	}

	public BaseSurfacePlayer createPlayer() {
		mediaPlayer = PlayerWithoutSurfaceFactory.createPlayer((Activity) mContext, decodeType,true);
		mediaPlayer.setListener(listener);
		return mediaPlayer;
	}


	private void getPhysicalResolution() {

		DisplayMetrics dm = new DisplayMetrics();
		Display display = mContext.getWindowManager().getDefaultDisplay();

		// 默认方式获取
		display.getMetrics(dm);
		int mDensityDpi = dm.densityDpi;
		int width = dm.widthPixels;
		int height = dm.heightPixels;

		if (mDensityDpi <= 0) {
			mPhysicalSize = 4.5;
		} else {
			mPhysicalSize = Math.sqrt(width * width + height * height)
					/ mDensityDpi;
		}
	}

	@Override
	protected boolean openVideo(){
		if (getPath() == null || getSurfaceTexture() == null){
			 return false;
		}
		bindSurface();
		try {
			if (!mediaPlayer.setVideoPath(getPath())) {
				errorToChangeSoft();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	 return true;


	}
	public void bindSurface() {
		mediaPlayer.setSurfaceTexture(getSurfaceTexture());
		mediaPlayer.setSurface(new Surface(this.getSurfaceTexture()));
	}
	/**
	 * 播放失败切换到软解
	 */
	public void errorToChangeSoft() {
		if (decodeType != IBfPlayerConstant
				.IBasePlayerType.TYPE_SOFT) {
			switchViewType(IBfPlayerConstant.IBasePlayerType.TYPE_SOFT);
		}
	}

	/**
	 * 切换 解码方式
	 * @param decodeType
	 */
	public void switchViewType(final int decodeType) {
		if (this.decodeType != decodeType) {
			setDecodeType(decodeType);
			((Activity)getContext()).runOnUiThread(new Runnable() {
				@Override
				public void run() {
					if (mediaPlayer != null) {
//						mSurface.release();

						mediaPlayer.release();
						createPlayer();
						openVideo();
					}
				}
			});


		}
	}


	@Override
	public void reset(){
		//TODO

		((Activity)getContext()).runOnUiThread(new Runnable() {
			@Override
			public void run() {
				release();

				if (mediaPlayer != null) {
					mediaPlayer.release();
				}
				createPlayer();
			}
		});


	}

	@Override
	public void release() {
		super.release();
	}

	@Override
	public void start() {
		super.start();
	}

	public void startplay(){
		((Activity) getContext()).runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if (mediaPlayer != null)
					mediaPlayer.start();

			}
		});

		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Thread.sleep(50);
					MJGLUtils.exeGLQueueEvent(getContext(), new Runnable() {
						@Override
						public void run() {
							start();
						}
					});
				}catch (Exception e){

				}
			}
		}).start();

	}

	@Override
	public void pause(){
		((Activity)getContext()).runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if(mediaPlayer!=null)
					mediaPlayer.pause();
			}
		});

        super.pause();
	}

	@Override
	public void stop(){
		pause();
		((Activity)getContext()).runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if (mediaPlayer != null){
					mediaPlayer.stop();
				}
			}
		});

	}

	@Override
	public void releasePlay(){
		((Activity)getContext()).runOnUiThread(new Runnable() {
			@Override
			public void run() {
				try {
					if(mediaPlayer!=null){
						mediaPlayer.release();
					}
//					if(mSurface!=null){
//						mSurface.release();
//					}
				}catch (Exception e){
					e.printStackTrace();
				}

			}
		});

		super.releasePlay();
	}

	@Override
	public void seekTo(final int pos){
		((Activity)getContext()).runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if(mediaPlayer!=null) {
					mediaPlayer.seekTo(pos);
				}

			}
		});

	}

	@Override
	public int getCurrentPosition(){

		if (mediaPlayer!=null) {
            return mediaPlayer.getCurrentPosition();
        }
        return 0;
	}

	@Override
	public int getDuration() {
		if (mediaPlayer==null) {
            return 0;
        }
        return mediaPlayer.getDuration();
	}

	@Override
	public boolean isPlaying() {
        if (mediaPlayer!=null) {
            return mediaPlayer.isPlaying();
        }
        return false;
	}

	@SuppressLint("NewApi")
	@Override
	public void onSurfaceCreated() {
		super.onSurfaceCreated();
		if(mediaPlayer==null) {
			createPlayer();
			openVideo();
		}else {
		bindSurface();
		}

		int t= (int) (getWidth() - getPaddingLeft() - getPaddingRight());
		int h= (int) (getHeight() - getPaddingTop() - getPaddingBottom());
		setVideoSize(t,h);
	}

	@SuppressLint("NewApi")
	@Override
	public void onSurfaceChanged(int width, int height) {
//		super.onSurfaceChanged(width, height);
//		if (width == surfaceWidth && height == surfaceHeight) {
//			return;
//		}
//		LogHelper.d(TAG, "onSurfaceChanged2 width = " + width + ", height = "
//				+ height);
//		surfaceWidth = width;
//		surfaceHeight = height;
//		setDefaultBufferSize(surfaceWidth, surfaceHeight);
//		setVideoSize(surfaceWidth,surfaceHeight);
	}
	public void onPrepared() {
		playPrepared();
		setVideoSize(mediaPlayer.getVideoWidth(),mediaPlayer.getVideoHeight());

	}

	private void setDefaultBufferSize(int width, int height){
		getSurfaceTexture().setDefaultBufferSize(width, height);
	}


	/**
	 * 设置屏幕模式:全屏,适应屏幕,剪切等.
	 *
	 * @param screenMode
	 */
	public final boolean setScreenMode(int screenMode) {
		 if(mediaPlayer!=null)
			 mediaPlayer.setScreenMode(screenMode);
		return true;
	}


	public boolean isSystemPlayer(){
		if(mediaPlayer!=null){
			return !(mediaPlayer instanceof BaseSoftPlayer);
		}
		return true;
	}
	public int getPlayerType(){
		if(mediaPlayer!=null){
			return mediaPlayer.getDecoderType();
		}
		return 0;
	}
 public String getPlayPath(){
		 return getPath();
 }

	public String readSubInfo(){
		if(mediaPlayer!=null){
			return mediaPlayer.readSubInfo();
		}
		return "";
	}

	public void disableSub(){
		((Activity)getContext()).runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if(mediaPlayer!=null){
					mediaPlayer.disableSub();
				}
			}
		});

	}

	public void setSubTitleIndex(final int index){
		((Activity)getContext()).runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if(mediaPlayer!=null){
					mediaPlayer.setSubTitleIndex(index);
				}
			}
		});

	}

	public int getAvgSpeed(){
		if(mediaPlayer!=null){
			return mediaPlayer.getAverageSpeed();
		}
		return 0;
	}


}
