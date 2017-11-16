package com.mojing.vrplayer.utils.publicutil;

import java.util.Comparator;

/**
 * Created by liuchuanchi on 2016/5/23.\
 * Double类型的比较器
 */
public class ComparatorDouble implements Comparator<Double> {
    @Override
    public int compare(Double lhs, Double rhs) {
        if(rhs > lhs){
            return 1;
        }else if(lhs == rhs){
            return 0;
        }else{
            return -1;
        }
    }
}
