package com.ewan.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

public class CommonUtils {
    public static String getRandomStr() {
        // 创建SimpleDateFormat对象，指定日期时间格式
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        // 获取当前日期时间
        Date now = new Date();
        // 格式化日期时间为字符串
        String formattedDateTime = sdf.format(now);
        return formattedDateTime + System.currentTimeMillis();
    }
}
