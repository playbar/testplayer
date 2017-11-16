package com.baofeng.mj.smb.util;


import com.baofeng.mj.smb.bean.SMBFolderBean;

import java.util.Comparator;

/**
 * Created by panxin on 2017/8/11.
 */

public class ComparatorSizeDes implements Comparator<SMBFolderBean> {
    @Override
    public int compare(SMBFolderBean lhs, SMBFolderBean rhs) {
        if(lhs.size < rhs.size){
            return 1;
        }else if(lhs.size == rhs.size){
            return 0;
        }else{
            return -1;
        }

    }
}