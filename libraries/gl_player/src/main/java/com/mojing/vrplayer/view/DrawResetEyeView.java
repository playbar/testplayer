package com.mojing.vrplayer.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextPaint;
import android.util.Log;

import com.bfmj.viewcore.render.GLColor;
import com.bfmj.viewcore.render.GLRenderParams;
import com.bfmj.viewcore.util.GLFontUtils;
import com.bfmj.viewcore.util.GLTextureUtils;
import com.bfmj.viewcore.view.GLRectView;
import com.mojing.vrplayer.R;

import java.io.IOException;
import java.io.InputStream;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by yushaochen on 2016/11/1.
 */
public class DrawResetEyeView extends GLRectView {
    private static final int[][] WIFI_STATES = {
            {0, 0, 0, 100},
    };
    private static final int[][] BACKGROUND_iMAGE = {
            {0, 0, 100, 100},
    };
    private int sprite;
    private GLRenderParams mRenderParams;
    private int mX = 100;
    private int mY = 100;

    public DrawResetEyeView(Context context) {
        super(context);
        this.setLayoutParams(mX,mY);
        sprite = R.drawable.play_view_charge;
       // setDelayVisiable1(resetTime);
      //  setBackground(new GLColor(0x00ff00));
    }

    int offX=0;
    @Override
    public void initDraw() {
        super.initDraw();
    }

    @Override
    public void onBeforeDraw(boolean isLeft) {
        super.onBeforeDraw(isLeft);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        cancelDelay1();
    }


    public void cancelDelay1(){
        if(timerq!=null){
            timerq.cancel();
            timerq = null;
        }
    }
    int resetTime=3000/100;
    int offXa=1;
    Timer timerq ;
    public void setDelayVisiable1(int duraion){
        if(timerq!=null){
            timerq.cancel();
            timerq = null;
        }
        timerq = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                showCursorView();
                offX+=offXa;
                if(offX<=101){
                    setDelayVisiable1(resetTime);
                }
                Log.v("44444","do setDelayVisiable1 offX:"+offX);
            }
        };
        timerq.schedule(task, duraion);
    }

    public void startDrawme(){
        offX=0;
        setDelayVisiable1(resetTime);
    }


    public void showCursorView() {
        com.mojing.vrplayer.utils.MJGLUtils.exeGLQueueEvent(getContext(), new Runnable() {
            @Override
            public void run() {
                Log.v("44444","do updateUI start");
                updateUI();
                Log.v("44444","do updateUI end");
            }
        });

    }

    private void updateUI(){
        if (!isSurfaceCreated() ){
            return;
        }

        if (mRenderParams != null){
            removeRender(mRenderParams);
            mRenderParams = null;
        }

        int textureId = -1;

        Bitmap bitmap = createBitmap();
        if (bitmap != null){
            textureId = GLTextureUtils.initImageTexture(bitmap, true, false);
        }

        if (textureId > -1){
            mRenderParams = new GLRenderParams(GLRenderParams.RENDER_TYPE_IMAGE);
            mRenderParams.setTextureId(textureId);
            updateRenderSize(mRenderParams, getWidth(), getHeight());
        }

        if (mRenderParams != null){
            addRender(mRenderParams);
        }
        Log.v("44444","do updateUI in ok");

    }



    private Bitmap createBitmap(){
        int width = (int)getWidth();
        int height = (int)getHeight();
        Bitmap spriteBp = getSpriteBitmap(sprite);
        Bitmap spriteBpBg = getSpriteBitmap(R.drawable.play_view_normal);
        Bitmap bitmapNew = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmapNew);

        drawBitmap(canvas, spriteBp,spriteBpBg ,WIFI_STATES[0]);
        return bitmapNew;
    }

    private Bitmap getSpriteBitmap(int resID){
        Bitmap bitmap = null;
        InputStream is = getContext().getResources().openRawResource(resID);

        try {
            bitmap = BitmapFactory.decodeStream(is);
        } finally {
            try {
                is.close();
            } catch(IOException e) {
                e.printStackTrace();
            }
        }

        return bitmap;
    }

    private void drawBitmap(Canvas canvas, Bitmap bitmap,Bitmap bitmapBG, int[] stateRect){
        Rect oRect =null;
        Rect dRect =null;
        Rect BGRect1=null;
        Rect BGRect2=null;
        if(offX<=100){
             oRect = new Rect(stateRect[0], stateRect[1], stateRect[0] + stateRect[2]+offX, stateRect[1] + stateRect[3]);
             dRect = new Rect(stateRect[0], stateRect[1], stateRect[0] + stateRect[2]+offX, stateRect[1] + stateRect[3]);
             BGRect1 = new Rect(0, 0, 100, 100);
             BGRect2 = new Rect(0, 0, 100, 100);
        }else{
            oRect = new Rect(stateRect[0], stateRect[1], stateRect[0] + stateRect[2], stateRect[1] + stateRect[3]);
            dRect = new Rect(stateRect[0], stateRect[1], stateRect[0] + stateRect[2], stateRect[1] + stateRect[3]);
             BGRect1 = new Rect(0, 0, 0, 0);
             BGRect2 = new Rect(0, 0, 0, 0);
        }

        canvas.drawBitmap(bitmapBG, BGRect1, BGRect2, new Paint(Paint.ANTI_ALIAS_FLAG));
        canvas.drawBitmap(bitmap, oRect, dRect, new Paint(Paint.ANTI_ALIAS_FLAG));
    }


}

