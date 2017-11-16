package com.baofeng.mj.vrplayer.business;

/**
 * Created by panxin on 2017/7/13.
 */

import java.util.Comparator;

/**
 * Created by panxin on 2017/7/12.
 */

public class ComparatorKeyDes implements Comparator<String> {
    @Override
    public int compare(String lhs, String rhs) {
        if(Long.parseLong(lhs) < Long.parseLong(rhs)){
            return 1;
        }else if(lhs.equals(rhs) ){
            return 0;
        }else{
            return -1;
        }
    }
}