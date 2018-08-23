package com.lxzh123.albumdemo.model;

/**
 * description $desc$
 * author      Created by lxzh
 * date        2018/8/22}
 */
public class AlbumGroupBean {
    private int id;
    private String path;
    private int bucketId;
    private String bucketName;
    private int count;

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

    public void setBucketId(int bucketId) {
        this.bucketId = bucketId;
    }

    public String getBucketName() {
        return bucketName;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public AlbumGroupBean() {
    }

    @Override
    public String toString() {
        return String.format("id=%d,path=%s,bucketId=%d,bucketName=%s,count=%d",this.id,
                this.path,this.bucketId,this.bucketName,this.count);
    }
}
