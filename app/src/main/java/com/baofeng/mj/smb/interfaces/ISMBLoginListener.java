package com.baofeng.mj.smb.interfaces;

/**
 * Created by panxin on 2017/4/19.
 */

public interface ISMBLoginListener {

    void loginStart();
    void loginSuccess(String ip,String name,String username,String password);
    void loginError(String ip,String deviceName);
}
