package com.lxzh123.albumdemo.model;

import com.lxzh123.albumdemo.common.Constant;

import java.util.Date;

/**
 * description $desc$
 * author      Created by lxzh
 * date        2018/8/23
 */
public class MediaBean {
    private int id;
    private String thumbPath;
    private String path;
    private long time;
    private String timeStr;
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
        this.timeStr= Constant.SIMPLE_DATE_FORMAT.format(new Date(this.time*1000l));
    }

    public String getTimeStr() {
        return timeStr;
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
}
