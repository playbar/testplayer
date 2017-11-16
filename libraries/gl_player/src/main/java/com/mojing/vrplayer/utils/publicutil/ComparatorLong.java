package com.mojing.vrplayer.utils.publicutil;

import java.util.Comparator;

/**
 * Created by liuchuanchi on 2016/5/23.\
 * Long类型的比较器
 */
public class ComparatorLong implements Comparator<Long> {
    @Override
    public int compare(Long lhs, Long rhs) {
        if(rhs > lhs){
            return 1;
        }else if(lhs == rhs){
            return 0;
        }else{
            return -1;
        }
    }
}
