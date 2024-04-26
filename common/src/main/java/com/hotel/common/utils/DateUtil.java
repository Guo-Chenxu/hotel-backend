package com.hotel.common.utils;

import lombok.extern.slf4j.Slf4j;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期工具
 *
 * @author: 郭晨旭
 * @create: 2023-11-17 13:33
 * @version: 1.0
 */
@Slf4j
public class DateUtil {


    // 格式化日期, 精确到秒
    private static final DateTimeFormatter DATE_FORMAT_SECOND = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    // 格式化日期, 精确到月
    private static final DateTimeFormatter DATE_FORMAT_MONTH = DateTimeFormatter.ofPattern("yyyy-MM");

    // 格式化日期, 精确到小时
    private static final SimpleDateFormat DATE_FORMAT_HOUR = new SimpleDateFormat("yyyy-MM-dd HH");
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

    public static int compareHour(Date date1, Date date2) {
        // 比较到小时的精度
        String formattedDate1 = DATE_FORMAT_HOUR.format(date1);
        String formattedDate2 = DATE_FORMAT_HOUR.format(date2);

        try {
            Date d1 = DATE_FORMAT_HOUR.parse(formattedDate1);
            Date d2 = DATE_FORMAT_HOUR.parse(formattedDate2);
            return d1.compareTo(d2);
        } catch (ParseException e) {
            log.error("日期格式不正确: d1={}, d2={}", date1, date2);
            throw new RuntimeException("日期转换不合法");
        }
    }

    /**
     * 获取七天前的时间
     */
    public static Date get7DaysAgo(Date time) {
        calendar.setTime(time);
        calendar.add(Calendar.DAY_OF_YEAR, -7);
        return calendar.getTime();
    }

    /**
     * 获取1天前的时间
     */
    public static Date getDayAgo(Date time) {
        calendar.setTime(time);
        calendar.add(Calendar.DAY_OF_YEAR, -1);
        return calendar.getTime();
    }

    /**
     * 获取一天的零时时间
     */
    public static Date getDayZeroTime(Date time) {
        calendar.setTime(time);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    /**
     * 获取一天的最终时间
     */
    public static Date getDayEndTime(Date time) {
        calendar.setTime(time);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTime();
    }
}
