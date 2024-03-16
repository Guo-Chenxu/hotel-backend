package com.hotel.common.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

/**
 * 日期工具
 *
 * @author: 郭晨旭
 * @create: 2023-11-17 13:33
 * @version: 1.0
 */
public class DateUtil {


    // 格式化日期, 精确到秒
    private static final DateTimeFormatter DATE_FORMAT_SECOND = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    // 格式化日期, 精确到月
    private static final DateTimeFormatter DATE_FORMAT_MONTH = DateTimeFormatter.ofPattern("yyyy-MM");

    private static final Calendar calendar = Calendar.getInstance();

    public static String getNowTime() {
        return LocalDateTime.now().format(DATE_FORMAT_SECOND);
    }

    public static String getNowMonth() {
        return LocalDateTime.now().format(DATE_FORMAT_MONTH);
    }

    /**
     * 获取上月时间
     */
    public static String getLastMonth() {
        return LocalDateTime.now().minusMonths(1).format(DATE_FORMAT_MONTH);
    }

    /**
     * 获取当前天是该月的第几天
     */
    public static int getDayOfMonth() {
        return LocalDateTime.now().getDayOfMonth();
    }
}
