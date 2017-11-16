package com.baofeng.mj.unity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

/**
 * Created by dupengwei on 2016/12/22.
 */

public class HomeListener {
    private Context mContext;
    private IntentFilter mHomeBtnIntentFilter;
    private OnHomeBtnPressListener mOnHomeBtnPressListener;
    private HomeBtnReceiver mHomeBtnReceiver;


    public HomeListener(Context context) {
        mContext = context;
        mHomeBtnReceiver = new HomeBtnReceiver( );
        mHomeBtnIntentFilter = new IntentFilter();
        mHomeBtnIntentFilter.addAction(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        mHomeBtnIntentFilter.addAction("android.intent.action.LONG_PRESS_HOME");
    }

    public void setOnHomeBtnPressListener( OnHomeBtnPressListener onHomeBtnPressListener ){
        mOnHomeBtnPressListener = onHomeBtnPressListener;
    }

    public void start(){
        mContext.registerReceiver( mHomeBtnReceiver, mHomeBtnIntentFilter );
    }

    public void stop(){
        mContext.unregisterReceiver( mHomeBtnReceiver );
    }

    class HomeBtnReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            receive( context, intent );
        }
    }

    private void receive(Context context, Intent intent){
        String action = intent.getAction();
        if (action.equals(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)) {
            String reason = intent.getStringExtra("reason");
            if (reason != null) {
                if( null != mOnHomeBtnPressListener ){
                    if(reason.equals("homekey")){
                        // 按Home按键
                        mOnHomeBtnPressListener.onHomeBtnPress();
                    }else if(reason.equals("recentapps")){
                        // 长按Home按键
//                        mOnHomeBtnPressListener.onHomeBtnLongPress();
                    }
                }
            }
        }else if(intent.getAction().equals("android.intent.action.LONG_PRESS_HOME")){
            // 长按Home按键
            if(null != mOnHomeBtnPressListener){
                mOnHomeBtnPressListener.onHomeBtnLongPress();
            }
        }
    }

    public interface OnHomeBtnPressListener{
         void onHomeBtnPress();
         void onHomeBtnLongPress();
    }


}
