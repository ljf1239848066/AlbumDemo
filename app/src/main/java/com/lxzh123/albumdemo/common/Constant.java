package com.lxzh123.albumdemo.common;

import java.text.SimpleDateFormat;

/**
 * description 静态常量数据
 * author      Created by lxzh
 * date        2018/8/22
 */
public class Constant {
    public static final int TAG_PERMISSION_READ_EXTERNAL_STORAGE = 1;
    /**
     * 相册图片显示列数
     */
    public final static int ALBUM_COLUMN_COUNT=3;

    public final static int LOAD_ALBUM_GROUP_MSGWHAT=0;

    public final static int LOAD_ALBUM_MSGWHAT=1;

    /**
     * 图片缩放系数
     */
    public final static float IMG_THUMBNAIL_SCALRE = 0.2f;
    /**
     * 分组列表缩略图显示尺寸
     */
    public final static int GROUP_THUMBNAIL_SIZE =20;

    /**
     * 缩略图显示尺寸
     */
    public final static int IMG_THUMBNAIL_SIZE=100;


    public final static int STATUS_SUCCESS=1;

    public final static int STATUS_FAIL=0;

    public final static String START_UPLOAD_DATA_TAG="START_UPLOAD_DATA_TAG";

    /**
     * 时间格式化器
     */
    public final static SimpleDateFormat SIMPLE_DATE_FORMAT=new SimpleDateFormat("yyyy-MM-dd");
}
