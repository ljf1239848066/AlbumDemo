package com.lxzh123.albumdemo.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.lxzh123.albumdemo.util.DateUtils;

/**
 * description 媒体文件数据结构
 * author      Created by lxzh
 * date        2018/8/23
 */
public class MediaBean<T extends Parcelable> implements Comparable,Parcelable{
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

    public MediaBean(){

    }

    public MediaBean(Parcel in) {
        this.id=in.readInt();
        this.thumbPath=in.readString();
        this.path=in.readString();
        this.time=in.readLong();
        this.timeStr=in.readString();
        this.mediaType=MediaType.values()[in.readInt()];
        this.duration=in.readInt();
        this.isChecked=in.readInt()>0;
    }

    public MediaBean(int id, String path) {
        this.id = id;
        this.path = path;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(id);
        out.writeString(thumbPath);
        out.writeString(path);
        out.writeLong(time);
        out.writeString(timeStr);
        out.writeInt(mediaType.ordinal());
        out.writeInt(duration);
        out.writeByte((byte)(isChecked?1:0));
    }

    public static final Parcelable.Creator<MediaBean> CREATOR = new Parcelable.Creator<MediaBean>()
    {
        @Override
        public MediaBean createFromParcel(Parcel source)
        {
            return new MediaBean(source);
        }

        @Override
        public MediaBean[] newArray(int size)
        {
            return new MediaBean[size];
        }
    };

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
