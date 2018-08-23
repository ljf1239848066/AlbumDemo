package com.lxzh123.albumdemo.model;

import android.support.annotation.NonNull;

import java.util.List;

/**
 * description $desc$
 * author      Created by lxzh
 * date        2018/8/23
 */
public class MediaDateGroupBean implements Comparable{
    private String date;
    private List<MediaBean> mediaBeans;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<MediaBean> getMediaBeans() {
        return mediaBeans;
    }

    public void setMediaBeans(List<MediaBean> mediaBeans) {
        this.mediaBeans = mediaBeans;
    }

    public MediaDateGroupBean(String date) {
        this.date = date;
    }

    @Override
    public int compareTo(@NonNull Object o) {
        return ((MediaDateGroupBean)o).getDate().compareTo(date);
    }
}
