package com.mojing.vrplayer.publicc;

import com.google.vr.sdk.controller.Orientation;

/**
 * Created by lixianke on 2017/5/23.
 */

public interface MotionInputCallback {
    int KEYCODE_APP = 0;
    int KEYCODE_HOME = 1;
    int KEYCODE_CLICK = 2;
    int KEYCODE_VOLUME_UP = 3;
    int KEYCODE_VOLUME_DOWN = 4;
    int STATE_DISCONNECTED = 0;
    int STATE_SCANNING = 1;
    int STATE_CONNECTING = 2;
    int STATE_CONNECTED = 3;

    class Event {
        public static final int ACTION_DOWN = 0;
        public static final int ACTION_UP = 1;
        public static final int ACTION_MOVE = 2;
        private int action = ACTION_DOWN;
        private float x = -1;
        private float y = -1;
        public int getAction(){
            return action;

        }
        public float getX(){
            return x;
        }
        public float getY(){
            return y;
        }
        public Event(int action, float x, float y){
            this.action = action;
            this.x = x;
            this.y = y;
        }
    }

    void onMotionStateChanged(String motionName, int state); //state 0 DISCONNECTED  1 SCANNING  2 CONNECTING  3 CONNECTED
    void onMotionKeyDown(int keycode);
    void onMotionKeyUp(int keycode);
    void onMotionLongPress(int keycode);
    void onMotionTouch(Event event);
    void onMotionOrientation(Orientation orientation);
}
