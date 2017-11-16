package com.mojing.vrplayer.utils;

import android.opengl.Matrix;

import com.bfmj.viewcore.util.GLFocusUtils;
import com.bfmj.viewcore.view.GLRectView;

/**
 * Created by lixianke on 2017/4/6.
 */

public class ViewUtil {
    public static float[] computerVMatrix(float[] sourceMatrix){
        float[] mtx = new float[16];
        Matrix.setIdentityM(mtx, 0);
        float[] angles = {0, 0, 0};
        GLFocusUtils.getEulerAngles(sourceMatrix, angles, 0);
        Matrix.rotateM(mtx, 0, -(float) Math.toDegrees(angles[2]), 0, 0, 1);
        Matrix.rotateM(mtx, 0, -(float) Math.toDegrees(angles[1]) - 30, 1, 0, 0);

        return mtx;
    }

    public static float[] computerVMatrix(float[] sourceMatrix, float yAngle){
        float[] mtx = new float[16];
        Matrix.setIdentityM(mtx, 0);
        float[] angles = {0, 0, 0};
        GLFocusUtils.getEulerAngles(sourceMatrix, angles, 0);

        Matrix.rotateM(mtx, 0, -(float) Math.toDegrees(angles[2]), 0, 0, 1);
        Matrix.rotateM(mtx, 0, -(float) Math.toDegrees(angles[1]) - 30, 1, 0, 0);
        Matrix.rotateM(mtx, 0, -(float) Math.toDegrees(angles[0]) + yAngle, 0, 1, 0);
        return mtx;
    }

    public static float getRotateYAngle(float[] sourceMatrix){
        float[] mtx = new float[16];
        Matrix.setIdentityM(mtx, 0);
        float[] angles = {0, 0, 0};
        GLFocusUtils.getEulerAngles(sourceMatrix, angles, 0);
        return  (float) Math.toDegrees(angles[0]);
    }

    public static int getDip(float sorce, float scale){
        return (int)(sorce * scale);
    }

    public static void reSetViewPos(GLRectView view,int width,int height,float scale){
        float[] cPos = {view.getLeft() + view.getX() + width / 2, view.getTop() + view.getY()+ height / 2};
        float x = (1 - scale) * cPos[0] + scale * view.getX();
        float y = (1 - scale) * cPos[1] + scale * view.getY();
        view.setX(x);
        view.setY(y);
    }
}
