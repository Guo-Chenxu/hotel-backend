package com.hotel.common.constants;

/**
 * redis中存储的键
 *
 * @author: 郭晨旭
 * @create: 2023-10-30 00:13
 * @version: 1.0
 */
public interface RedisKeys {
    String AC_PROPERTIES = "HOTEL:AC_PROPERTIES"; // 空调参数
    String STAFF_ID_INFO = "HOTEL:STAFF:ID:%s"; // 员工信息
    String AC_REQUEST_USERID = "HOTEL:AC_REQUEST:USERID:%s"; // 空调请求记录
    String AC_SCHEDULE_LOCK = "HOTEL:AC_SCHEDULE_LOCK"; // 空调调度锁
}
