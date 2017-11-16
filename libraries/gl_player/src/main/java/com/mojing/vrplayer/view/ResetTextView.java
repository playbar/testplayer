package com.mojing.vrplayer.view;

import android.content.Context;

import com.bfmj.viewcore.view.GLTextView;
import com.mojing.vrplayer.interfaces.IResetView;
import com.mojing.vrplayer.utils.ViewUtil;

/**
 * Created by lixianke on 2017/4/6.
 */

public class ResetTextView extends GLTextView implements IResetView {
    private boolean isFixedToBottom = false;
    private float currentYAngle = 0;
    private boolean isResetFixed = false;

    public ResetTextView(Context context) {
        super(context);
        setCostomHeadView(true);
    }

    @Override
    public void onBeforeDraw(boolean isLeft) {
        if(isResetFixed){
            super.onBeforeDraw(isLeft);
            return;
        }
        float[] mtx;
        float[] headview = getMatrixState().getVMatrix();

        if (isFixedToBottom){
            mtx = ViewUtil.computerVMatrix(headview, currentYAngle);
        } else {
            mtx = ViewUtil.computerVMatrix(getMatrixState().getVMatrix());
        }
        getMatrixState().setVMatrix(mtx);
        super.onBeforeDraw(isLeft);
    }

    @Override
    public void setFixToBottom(boolean flag) {
        isFixedToBottom = flag;
    }

    @Override
    public void setCurrentYAngle(float currentYAngle) {
        this.currentYAngle = currentYAngle;
    }

    @Override
    public void setResetFixed(boolean isfixed) {
        isResetFixed = isfixed;
    }
}
