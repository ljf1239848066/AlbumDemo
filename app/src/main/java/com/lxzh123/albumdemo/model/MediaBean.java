package com.lxzh123.albumdemo.model;

import android.support.annotation.NonNull;

import com.lxzh123.albumdemo.util.DateUtils;

/**
 * description 媒体文件数据结构
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
    /**
     * 音视频资源时长
     */
    private int duration;
    private String durationStr;
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
        this.timeStr=DateUtils.converToString(time);
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

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
        this.durationStr= DateUtils.covertDurationToString(duration);
    }

    public String getDurationStr() {
        return durationStr;
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
