package com.lxzh123.albumdemo.model;

import android.support.annotation.NonNull;

import com.lxzh123.albumdemo.common.Constant;

/**
 * description $desc$
 * author      Created by lxzh
 * date        2018/8/23
 */
public class MediaBean implements Comparable{
    private int id;
    private String thumbPath;
    private String path;
    private long time;
    private String timeStr;
    private MediaType mediaType;
    private boolean isChecked;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getThumbPath() {
        return thumbPath;
    }

    public void setThumbPath(String thumbPath) {
        this.thumbPath = thumbPath;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
//        Date date=new Date();
//        date.setTime(this.time);
        this.timeStr= Constant.SIMPLE_DATE_FORMAT.format(this.time);
    }

    public String getTimeStr() {
        return timeStr;
    }

    public MediaType getMediaType() {
        return mediaType;
    }

    public void setMediaType(MediaType mediaType) {
        this.mediaType = mediaType;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public MediaBean() {
    }

    public MediaBean(int id, String path) {
        this.id = id;
        this.path = path;
    }

    @Override
    public String toString() {
        return String.format("MeadiaBean:id=%d,path=%s,date=%s,isChecked=%d",id,path,timeStr,isChecked?1:0);
    }

    @Override
    public int compareTo(@NonNull Object o) {
        long oTime=((MediaBean)o).time;
        if(oTime<time){
            return -1;
        }else if(oTime==time){
            return 0;
        }else{
            return 1;
        }
    }
}
