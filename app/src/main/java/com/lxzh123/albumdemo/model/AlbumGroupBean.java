package com.lxzh123.albumdemo.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

/**
 * description $desc$
 * author      Created by lxzh
 * date        2018/8/22}
 */
public class AlbumGroupBean<T extends Parcelable> implements Comparable,Parcelable {
    private int id;
    private String path;
    private int bucketId;
    private String bucketName;
    private MediaType thumbType;
    private long time;
    private int count;
    /**
     * 合并分组标记使用
     */
    public int tag;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getBucketId() {
        return bucketId;
    }

    public void setBucketId(int bucketId) {
        this.bucketId = bucketId;
    }

    public String getBucketName() {
        return bucketName;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public MediaType getThumbType() {
        return thumbType;
    }

    public void setThumbType(MediaType thumbType) {
        this.thumbType = thumbType;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public AlbumGroupBean() {
        this.tag = 0;
    }

    public AlbumGroupBean(Parcel in){
        this.id=in.readInt();
        this.path=in.readString();
        this.bucketId=in.readInt();
        this.bucketName=in.readString();
        this.thumbType=MediaType.values()[in.readInt()];
        this.time=in.readLong();
        this.count=in.readInt();
        this.tag=in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(id);
        out.writeString(path);
        out.writeInt(bucketId);
        out.writeString(bucketName);
        out.writeInt(thumbType.ordinal());
        out.writeLong(time);
        out.writeInt(count);
        out.writeInt(tag);
    }

    public static final Parcelable.Creator<AlbumGroupBean> CREATOR = new Parcelable.Creator<AlbumGroupBean>()
    {
        @Override
        public AlbumGroupBean createFromParcel(Parcel source)
        {
            return new AlbumGroupBean(source);
        }

        @Override
        public AlbumGroupBean[] newArray(int size)
        {
            return new AlbumGroupBean[size];
        }
    };

    @Override
    public String toString() {
        return String.format("id=%d,path=%s,bucketId=%d,bucketName=%s,time=%d,count=%d,tag=%d", this.id,
                this.path, this.bucketId, this.bucketName,this.time, this.count, this.tag);
    }

    public String toDebugString() {
        return String.format("id=%d,bucketId=%d,time=%d,count=%d,tag=%d", this.id,
                 this.bucketId, this.time, this.count, this.tag);
    }

    @Override
    public int compareTo(@NonNull Object o) {
        long oCnt=((AlbumGroupBean)o).count;
        if(oCnt<count){
            return -1;
        }else if(oCnt==count){
            return 0;
        }else{
            return 1;
        }
    }
}

