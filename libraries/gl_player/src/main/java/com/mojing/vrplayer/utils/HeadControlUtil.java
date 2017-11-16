package com.mojing.vrplayer.utils;

import android.content.Context;
import android.graphics.Color;

import com.bfmj.viewcore.interfaces.ICursorView;
import com.bfmj.viewcore.render.GLScreenParams;
import com.bfmj.viewcore.util.GLFocusUtils;
import com.bfmj.viewcore.util.GLFocusUtils.OnCursorDepthChangeListener;
import com.bfmj.viewcore.view.GLCursorView;
import com.bfmj.viewcore.view.GLRectView;
import com.bfmj.viewcore.view.GLRelativeView;
import com.mojing.vrplayer.R;
import com.mojing.vrplayer.view.GLProcessCircleCanvasView;

import java.util.Timer;
import java.util.TimerTask;


public class HeadControlUtil extends GLRelativeView implements ICursorView{
    private static HeadControlUtil instance;
    private GLCursorView mCursorView;
    private GLProcessCircleCanvasView mProgressView;
    private float defaultDepth = 4.0f;
    private int defaultWidth = 80;
    private int defaultHeight = 80;
    private Timer mUpdateProgressTimer;
    private boolean isSysView = false;
    private Context mContext;

    public void setIsSysView(boolean isSysView){
        this.isSysView = isSysView;
    }

    private static OnHeadClickListener mHeadClickListener = new OnHeadClickListener() {

        @Override
        public boolean onHeadClickStart(GLRectView view) {
            headClickStart(view);
            return false;
        }

        @Override
        public boolean onHeadClickEnd(GLRectView view) {
            headClickEnd(view);
            return false;
        }

        @Override
        public boolean onHeadClickCancel(GLRectView view) {
            headClickEnd(view);
            return false;
        }
    };

    public HeadControlUtil(Context context) {
        super(context);
        this.mContext = context;
        instance = this;
        init();
        setFixed(true);
    }


    private void init() {
        setLayoutParams(defaultWidth, defaultWidth);
//        setBackground(new GLColor(0xff0000));
        mCursorView = new GLCursorView(getContext());
        mCursorView.setId("headcursor");
        mCursorView.setImage("play_cursor");
        mCursorView.setLayoutParams(60, 60);
        mCursorView.setMargin(10,10,0,0);
        addView(mCursorView);

        mProgressView = new HeadcontrolProgress(getContext());
        mProgressView.setLayoutParams(60, 60);
        mProgressView.setMargin(10,10,0,0);
//		mProgressView
//				.setBackground(R.drawable.mj_gl_cursor_normal);
//		mProgressView.setDirection(GLDirection.CLOCKWISE);
		mProgressView
				.setAttribute(Color.rgb(0, 140, 179), Color.rgb(187, 187, 187), Color.argb(30,00,00,00),6);
		mProgressView.setShowText(false);
//		mProgressView.createView(0);
        mProgressView.setVisible(false);
        addView(mProgressView);

        setZIndex(-1);

        GLFocusUtils
                .setOnCursorDepthChangeListener(new OnCursorDepthChangeListener() {

                    @Override
                    public void onCursorDepthChange(float depth) {
                        setCursorDepth(depth);
                    }
                });
    }

//    public void setCursorViewBackground(int resId) {
//        mCursorView.setImage(resId);
//    }

    private void setCursorDepth(float depth) {
        depth -= 0.01f;

        float ratio = depth / defaultDepth;
        float width = defaultWidth * ratio;
        float height = defaultHeight * ratio;
        float x = (GLScreenParams.getXDpi() - defaultHeight) / 2;
        float y = (GLScreenParams.getYDpi() - defaultHeight) / 2;
        setDepth(depth);
//		setLayoutParams(x, y, width, height);

        mCursorView.setDepth(depth);
//		mCursorView.setLayoutParams(x, y, defaultWidth, defaultHeight);

        mProgressView.setDepth(depth);
        mProgressView.setLayoutParams(defaultWidth, defaultHeight);
    }

    private void clickStart(GLRectView view) {
        if (view == null) {
            return;
        }

        mCursorView.setVisible(false);
        mProgressView.setVisible(true);
        try {
            startUpdateProgress(view.getHeadClickTime());
        } catch (Exception e) {

        }
    }

    public void clickEnd(final GLRectView view) {
        mProgressView.setVisible(false);
        mCursorView.setVisible(true);
        if (getRootView() != null) {
            getRootView().queueEvent(new Runnable() {

                @Override
                public void run() {
                    mProgressView.setProcess(0);
                }
            });
        }
        stopUpdateProgress();
    }

    /**
     * 开启进度更新定时器
     *
     * @param
     * @return
     * @author lixianke @Date 2015-9-2 上午10:08:52
     */
    private void startUpdateProgress(final int time) {
        stopUpdateProgress();

        final int timeSpan = time / 101;

        mUpdateProgressTimer = new Timer();
        mUpdateProgressTimer.schedule(new TimerTask() {
            private int timeTotal = 0;

            @Override
            public void run() {
                timeTotal += timeSpan;

                if (timeTotal > time) {
                    stopUpdateProgress();
                    return;
                }

                if (getRootView() != null) {
                    getRootView().queueEvent(new Runnable() {

                        @Override
                        public void run() {
                            mProgressView.setProcess(timeTotal / timeSpan);
                        }
                    });
                }
            }
        }, timeSpan, timeSpan);
    }

    /**
     * 关闭进度更新定时器
     *
     * @param
     * @return
     * @author lixianke @Date 2015-9-1 上午11:09:47
     */
    private void stopUpdateProgress() {
        if (mUpdateProgressTimer != null) {
            mUpdateProgressTimer.cancel();
            mUpdateProgressTimer = null;
            getRootView().clearTextureQueue();//解决延迟问题
        }
    }

//    @Override
//    public void draw() {
//        if (mCursorView != null) {
//            float[] mtx = new float[16];
//            Matrix.setRotateM(mtx, 0, 0, 1, 1, 1);
//            getMatrixState().setVMatrix(mtx);
//            super.draw();
//
//        }
//    }

//	private GLRootView getRootView() {
//		BaseViewActivity activity = (BaseViewActivity) getContext();
//		if (activity != null) {
//			return activity.getRootView();
//		}
//
//		return null;
//	}

    private static void headClickStart(GLRectView view) {
        if (instance != null) {
            instance.clickStart(view);
        }

    }

    private static void headClickEnd(GLRectView view) {
        if (instance != null) {
            instance.clickEnd(view);
        }
    }

    public static void bindView(GLRectView view) {
//        view.setOnHeadClickListener(mHeadClickListener);
        bindView(view,250,1000);
    }

    public static void bindView(GLRectView view, int headStayTime,
                                int headClickTime) {
        view.setOnHeadClickListener(headStayTime, headClickTime,
                mHeadClickListener);
    }

    public static void unbindView(GLRectView view) {
        view.setOnHeadClickListener(null);
    }

    public class HeadcontrolProgress extends GLProcessCircleCanvasView {

        public HeadcontrolProgress(Context context) {
            super(context);

        }

//        @Override
//        public void draw() {
//            float[] mtx = new float[16];
//            Matrix.setRotateM(mtx, 0, 0, 1, 1, 1);
////            Matrix.rotateM(mtx, 0, 180, 0, 0, 1);
//            float[] angles = {0, 0, 0};
//            MojingSDK.getLastHeadEulerAngles(angles);
//            Matrix.rotateM(mtx, 0, -(float) Math.toDegrees(angles[2]), 0, 0, 1);
//            getMatrixState().setVMatrix(mtx);
//            super.draw();
//
//        }
    }

    public void setVisible(boolean isVisible){
        super.setVisible(isVisible);
//        if(isVisible){
//            GLFocusUtils.openHeadControl();
//        }else{
//            GLFocusUtils.closeHeadControl();
//        }
    }
}
