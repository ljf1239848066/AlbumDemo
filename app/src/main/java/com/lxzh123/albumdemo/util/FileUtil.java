package com.lxzh123.albumdemo.util;

import android.media.ExifInterface;
import android.text.TextUtils;

import java.io.File;
import java.io.IOException;
import java.util.Date;

/**
 * description $desc$
 * author      Created by lxzh
 * date        2018/8/24
 */
public class FileUtil {
    public static Date parseDate(File file) {
        ExifInterface exif = null;
        Date date = null;
        try {
            exif = new ExifInterface(file.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        String dateStr = exif.getAttribute(ExifInterface.TAG_DATETIME);
        try {
            if (!TextUtils.isEmpty(dateStr)) {
                date = DateUtils.convertToDate(dateStr);
            } else {
                date = DateUtils.convertToDate("1970/01/01 00:00:00");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date;
    }
}
