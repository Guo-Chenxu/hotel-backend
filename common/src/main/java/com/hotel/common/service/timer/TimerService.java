package com.hotel.common.service.timer;

import java.util.Date;

/**
 * 计时器服务类
 *
 * @author: 郭晨旭
 * @create: 2024-03-16 21:30
 * @version: 1.0
 */
public interface TimerService {

    /**
     * 获取该用户当前模拟到的时间
     */
    Date getTime();

    /**
     * 设置时间流速
     */
    void setSpeed(long speed);

    /**
     * 查看时间流速
     */
    String getSpeed();
}
