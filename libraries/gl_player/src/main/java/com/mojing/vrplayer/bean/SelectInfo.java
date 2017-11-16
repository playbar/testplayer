package com.mojing.vrplayer.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yushaochen on 2017/4/14.
 */

public class SelectInfo<T> {
    private String nums;//底部需要展示的文案
    private List<T> infos = new ArrayList<>();//综艺需要展示的数据集合
    private int total;//总集数

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String getNums() {
        return nums;
    }

    public void setNums(String nums) {
        this.nums = nums;
    }

    public List<T> getInfos() {
        return infos;
    }

    public void setInfos(List<T> infos) {
        this.infos = infos;
    }
}
