package com.lxzh123.albumdemo.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.lxzh123.albumdemo.common.Constant;
import com.lxzh123.albumdemo.view.AlbumGridView.IGridImageLoader;

/**
 * description $desc$
 * author      Created by lxzh
 * date        2018/8/23
 */
public class ImageLoader implements IGridImageLoader{
    @Override
    public void displayGridImage(Context context, String url, ImageView imageView) {
        Bitmap bitmap=AlbumUtil.loadThumbnail(url, Constant.IMG_THUMBNAIL_SIZE);
        imageView.setImageBitmap(bitmap);
    }

    @Override
    public void displayGridImage(Context context, String url, ImageView imageView, float scale) {
        int w=imageView.getWidth();
        int h=imageView.getHeight();
        Bitmap bitmap=AlbumUtil.loadThumbnail(url, (int)(scale*(w>h?w:h)));
        imageView.setImageBitmap(bitmap);
    }
}
