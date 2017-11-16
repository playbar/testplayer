package com.baofeng.mj.vrplayer.business;

import java.text.Collator;
import java.util.Comparator;

/**
 * Created by liuchuanchi on 2016/5/23.
 * String类型的比较器(降序)
 */
public class ComparatorStringDescending implements Comparator<String> {
    private Collator collator = Collator.getInstance();

    @Override
    public int compare(String lhs, String rhs) {
        if (lhs == null || rhs == null){
            return 0; //如果有空值，直接返回0
        }
        return collator.getCollationKey(rhs).compareTo(collator.getCollationKey(lhs));
    }
}
