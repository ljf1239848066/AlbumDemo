package com.lxzh123.albumdemo.model;

/**
 * description $desc$
 * author      Created by lxzh
 * date        2018/8/23$
 */
public class MediaBean {
    private int id;
    private String path;

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

    public MediaBean(int id, String path) {
        this.id = id;
        this.path = path;
    }

    @Override
    public String toString() {
        return String.format("MeadiaBean:id=%d,path=%s",id,path);
    }
}
