package com.lxzh123.albumdemo.util;

import com.lxzh123.albumdemo.model.MediaBean;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Observable;

/**
 * description 图片选择观察器
 * author      Created by lxzh
 * date        2018/8/23
 */
public class ImageSelectObservable extends Observable {

    public static Object imgSelectObj = new Object();

    private static ImageSelectObservable sObserver;

    /**某一文件夹下所有图片*/
    private List<MediaBean> mFolderAllImages;
    /**已选图片*/
    private List<MediaBean> mSelectImages;

    private ImageSelectObservable() {
        mFolderAllImages = new ArrayList<>();
        mSelectImages = new ArrayList<>();
    }

    public static ImageSelectObservable getInstance() {

        if (sObserver == null) {
            synchronized (ImageSelectObservable.class) {
                if (sObserver == null) {
                    sObserver = new ImageSelectObservable();
                }
            }
        }
        return sObserver;
    }

    public void addFolderImagesAndClearBefore(Collection<? extends MediaBean> list) {
        mFolderAllImages.clear();
        if (list != null) {
            mFolderAllImages.addAll(list);
        }
    }

    public void addSelectImagesAndClearBefore(Collection<? extends MediaBean> list) {
        mSelectImages.clear();
        if (list != null) {
            mSelectImages.addAll(list);
        }
    }

    public List<MediaBean> getFolderAllImages() {
        return mFolderAllImages;
    }

    public List<MediaBean> getSelectImages() {
        return mSelectImages;
    }

    /**
     * 通知图片选择已改变
     */
    public void updateImageSelectChanged () {
        setChanged();
        notifyObservers(imgSelectObj);

    }

    public void clearFolderImages () {
        mFolderAllImages.clear();
    }

    public void clearSelectImgs () {
        mSelectImages.clear();
    }
}
