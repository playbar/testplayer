package com.baofeng.mj.smb.util;

import com.baofeng.mj.smb.bean.SMBFolderBean;

import java.text.Collator;
import java.util.Comparator;

/**
 * Created by liuchuanchi on 2016/5/23.
 * String类型的比较器(升序)
 */
public class ComparatorNameAsc implements Comparator<SMBFolderBean> {
    private Collator collator = Collator.getInstance();

    @Override
    public int compare(SMBFolderBean lhs, SMBFolderBean rhs) {
        if (lhs == null || rhs == null){
            return 0; //如果有空值，直接返回0
        }
        return collator.getCollationKey(lhs.filename).compareTo(collator.getCollationKey(rhs.filename));
    }
}
