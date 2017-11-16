package com.mojing.vrplayer.view;

import android.content.Context;

import com.bfmj.viewcore.view.GLRectView;
import com.bfmj.viewcore.view.GLRelativeView;
import com.mojing.vrplayer.interfaces.IResetView;
import com.mojing.vrplayer.utils.ViewUtil;

/**
 * Created by lixianke on 2017/4/6.
 */

public class ResetRelativeLayout extends GLRelativeView implements IResetView {
    private boolean isFixedToBottom = false;
    private boolean isResetFixed = false;
    private float currentYAngle = 0;


    public ResetRelativeLayout(Context context) {
        super(context);
        setCostomHeadView(true);
    }

    @Override
    public void onBeforeDraw(boolean isLeft) {

        if (!isResetFixed){
            float[] mtx;
            float[] headview = getMatrixState().getVMatrix();


            if (isFixedToBottom){
                mtx = ViewUtil.computerVMatrix(headview, currentYAngle);
            } else {
                mtx = ViewUtil.computerVMatrix(headview);
            }
            getMatrixState().setVMatrix(mtx);
        }
        super.onBeforeDraw(isLeft);
    }

    @Override
    public void setFixToBottom(boolean flag) {
        isFixedToBottom = flag;

        for (int i = 0; i < mChildView.size(); i++){
            GLRectView view = mChildView.get(i);
            if (view != null && view instanceof IResetView){
                ((IResetView)view).setFixToBottom(flag);
            }
        }
    }

    @Override
    public void setCurrentYAngle(float currentYAngle) {
        this.currentYAngle = currentYAngle;
        for (int i = 0; i < mChildView.size(); i++){
            GLRectView view = mChildView.get(i);
            if (view != null && view instanceof IResetView){
                ((IResetView)view).setCurrentYAngle(currentYAngle);
            }
        }
    }

    public boolean isResetFixed() {
        return isResetFixed;
    }

    public void setResetFixed(boolean resetFixed) {
        isResetFixed = resetFixed;
        setCostomHeadView(!isResetFixed);
        for (int i = 0; i < mChildView.size(); i++){
            GLRectView view = mChildView.get(i);
            if (view != null && view instanceof ResetRelativeLayout){
                ((ResetRelativeLayout)view).setResetFixed(resetFixed);
            }
            if (view != null && view instanceof IResetView){
                ((IResetView)view).setResetFixed(resetFixed);
            }
            (view).setCostomHeadView(!isResetFixed);
        }



    }
}
