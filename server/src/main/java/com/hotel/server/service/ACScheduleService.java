package com.hotel.server.service;

import com.hotel.common.entity.ACRequest;

/**
 * 空调调度线程
 *
 * @author: 郭晨旭
 * @create: 2024-03-28 11:46
 * @version: 1.0
 */
public interface ACScheduleService {
    /**
     * 从已经有调度的map中删除
     */
    void removeOne(String userId);

    /**
     * 向调度队列里面放入一个空调请求
     */
    void addOne(ACRequest acRequest);

    /**
     * 查看调度队列里面是否有请求
     */
    boolean isRequestEmpty();
}
