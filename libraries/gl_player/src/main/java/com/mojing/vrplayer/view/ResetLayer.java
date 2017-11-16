package com.mojing.vrplayer.view;

import android.content.Context;

import com.bfmj.viewcore.interfaces.GLViewFocusListener;
import com.bfmj.viewcore.render.GLScreenParams;
import com.bfmj.viewcore.view.GLRectView;
import com.mojing.vrplayer.activity.GLBaseActivity;
import com.mojing.vrplayer.interfaces.IResetLayerCallBack;
import com.mojing.vrplayer.publicc.ReportBusiness;
import com.mojing.vrplayer.utils.GLConst;
import com.mojing.vrplayer.utils.ViewUtil;

import java.util.HashMap;

/**
 * Created by lixianke on 2017/4/6.
 */

public class ResetLayer extends ResetRelativeLayout {
//    private float mDepth = 3.8f;
    private float mWidth = 2400f;
    private float mHeight = 600f;
    private ResetView mResetView;
    private boolean isNeedGetYAngle = false;
    private float currentAngle = 0;

    public ResetLayer(Context context) {
        super(context);

//        setDepth(mDepth);
        setLayoutParams( mWidth, mHeight);
        setX((GLScreenParams.getXDpi() - mWidth) / 2);
        setY((GLScreenParams.getYDpi() - mHeight) / 2 + 140f);
//        setBackground(new GLColor(1, 0, 0));

        float width = 550f;
        float height = 140f;
        mResetView = new ResetView(context);
        mResetView.setLayoutParams( width, height);
        mResetView.setMargin((GLScreenParams.getXDpi() - width) / 2, 50, 0, 0);
        addView(mResetView);
        mResetView.setVisible(false);

        setFocusListener(new GLViewFocusListener() {
            @Override
            public void onFocusChange(GLRectView view, boolean focused) {
                //先执行外部调用，不然会导致X号按钮未恢复旋转，而隐藏了该view，下次会显示错误
                if(null != mCallBack) {
                    mCallBack.onFocusChange(focused);
                }
                if(focused) {
                    mResetView.setVisible(true);
                    //报数
                    reportEvent();
                } else {
                    mResetView.setVisible(false);
                }

                isNeedGetYAngle = focused;
                mResetView.setFixToBottom(focused);
            }
        });
    }

    public void showChildView() {
        mResetView.setVisible(true);
    }

    public boolean isShowing(){
        if(mResetView!=null){
            return mResetView.isVisible();
        }
        return false;
    }

    public void hideChildView() {
        mResetView.setVisible(false);
    }

    @Override
    public void onBeforeDraw(boolean isLeft) {
        if(isResetFixed()){
            super.onBeforeDraw(isLeft);
            return;
        }
        if (isFocused()){
            float yAngle = ViewUtil.getRotateYAngle(getMatrixState().getVMatrix());
            if (isNeedGetYAngle){
                isNeedGetYAngle = false;
                currentAngle = yAngle;
            }

            float angle = currentAngle;

            if (yAngle - currentAngle > 340){
                currentAngle += 360;
            } else if (yAngle - currentAngle < -340){
                currentAngle -= 360;
            }

            if (yAngle > currentAngle + 16){
                angle = yAngle - 16;
                if (yAngle > currentAngle + 18){
                    currentAngle = yAngle - 18;
                }
            }

            if (yAngle < currentAngle - 16){
                angle = yAngle + 16;
                if (yAngle < currentAngle - 18){
                    currentAngle = yAngle + 18;
                }
            }

            mResetView.setCurrentYAngle(angle);
        } else {
            currentAngle = 0;
        }

        super.onBeforeDraw(isLeft);
    }

    private IResetLayerCallBack mCallBack;

    public void setIResetLayerCallBack(IResetLayerCallBack callBack) {
        mCallBack = callBack;
        if (null != mResetView) {
            mResetView.setIResetLayerCallBack(mCallBack);
        }
    }

    public void showOpen(boolean isEnter) {
        showChildView();
        mResetView.showOpen(isEnter);
    }

    public void showClose(boolean isEnter) {
        mResetView.showClose(isEnter);
        hideChildView();
    }

    boolean ischange = false;
    @Override
    public void setResetFixed(boolean resetFixed) {
        super.setResetFixed(resetFixed);
        setFixed(resetFixed);
        if(resetFixed) {
            setY(getY() + ViewUtil.getDip(480, GLConst.Bottom_Menu_Scale));
            ischange = true;
        }else if(ischange) {
            setY(getY()- ViewUtil.getDip(480, GLConst.Bottom_Menu_Scale));
            ischange = false;
        }
    }

    public void reportEvent() {
        HashMap<String,String> params = new HashMap<>();
        params.put("etype","show");
        int type = ((GLBaseActivity)getContext()).getCurPageType();
        if(type == GLConst.MOVIE || type == GLConst.LOCAL_MOVIE) {
            params.put("sensemode","commmovie");
        } else if(type == GLConst.PANO || type == GLConst.LOCAL_PANO) {
            params.put("sensemode","360video");
        }
        params.put("showtype","1");
        ReportBusiness.getInstance().reportClick(params);
    }
}
