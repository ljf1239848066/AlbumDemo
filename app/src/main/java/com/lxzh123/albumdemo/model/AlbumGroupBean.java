package com.lxzh123.albumdemo.model;

import android.support.annotation.NonNull;

/**
 * description $desc$
 * author      Created by lxzh
 * date        2018/8/22}
 */
public class AlbumGroupBean implements Comparable{
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

