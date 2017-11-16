package com.mojing.vrplayer.publicc;

/**
 * Created by zhaominglei on 2016/8/22.
 */
public class PlayerBusiness {

    //遥控器ok
    public int KEY_BLUETOOTH_OK = 66;
    //游戏手柄
    public int KEY_JOYSTICK_OK = 96;
    //遥控器长按
    public int KEY_BLUETOOTH_LONG_PRESS = 23;
    //遥控器长按左键
    public int KEY_BLUETOOTH_LEFT = 21;
    //遥控器长按右键
    public int KEY_BLUETOOTH_RIGHT = 22;
    //遥控器上键
    public int KEY_BLUETOOTH_UP = 19;
    //遥控器下键
    public int KEY_BLUETOOTH_DOWN = 20;
    //遥控器返回键
    public int KEY_BLUETOOTH_BACK = 4;
    //手柄返回键
    public int KEY_JOYSTICK_BACK = 97;

    private int STEP = 5;

    private static PlayerBusiness instance;


    private PlayerBusiness() {

    }

    public static PlayerBusiness getInstance() {
        if (instance == null) {
            instance = new PlayerBusiness();
        }
        return instance;
    }


}
