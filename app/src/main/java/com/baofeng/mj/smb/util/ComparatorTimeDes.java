package com.baofeng.mj.smb.util;

/**
 * Created by panxin on 2017/7/13.
 */

import com.baofeng.mj.smb.bean.SMBFolderBean;

import java.util.Comparator;

/**
 * Created by panxin on 2017/7/12.
 */

public class ComparatorTimeDes implements Comparator<SMBFolderBean> {
    @Override
    public int compare(SMBFolderBean lhs, SMBFolderBean rhs) {
        if(lhs.modified < rhs.modified){
            return 1;
        }else if(lhs.modified == rhs.modified){
            return 0;
        }else{
            return -1;
        }
    }
}