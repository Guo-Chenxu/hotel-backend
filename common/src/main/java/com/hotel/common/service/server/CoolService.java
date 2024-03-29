package com.hotel.common.service.server;

import javax.servlet.http.HttpServletResponse;

/**
 * 温度服务
 *
 * @author: 郭晨旭
 * @create: 2024-03-27 11:10
 * @version: 1.0
 */

public interface CoolService {
    /**
     * 增加一个房间的空调线程
     */
    void watchAC(String userId);
}
