package com.lxzh123.albumdemo.util;

import com.lxzh123.albumdemo.common.Constant;

import java.util.Date;

/**
 * description 日期工具
 * author      Created by lxzh
 * date        2018/8/24
 */
public class DateUtils {

    public static String converToString(Date date) {
        return Constant.SIMPLE_DATE_FORMAT.format(date);
    }

    public static Date convertToDate(String strDate) throws Exception {
        return Constant.SIMPLE_DATE_FORMAT.parse(strDate);
    }

    public static String converToString(long timeMillion) {
        return Constant.SIMPLE_DATE_FORMAT.format(timeMillion);
    }

    public static String converSecondToString(long timeSecond) {
        return Constant.SIMPLE_DATE_FORMAT.format(timeSecond*1000);
    }
    /**
     * 时长转换为字符串
     * @param duration 时长 单位:ms
     * @return
     */
    public static String covertDurationToString(int durationMS){
        durationMS/=1000;
        int h=durationMS/3600;durationMS%=3600;
        int m=durationMS/60;durationMS%=60;
        int s=durationMS;
        if(h>0){
            return String.format("%02d:%02d:%02d",h,m,s);
        }else if(m>0){
            return String.format("%02d:%02d",m,s);
        }else{
            return String.format("00:%02d",s);
        }
    }
}
