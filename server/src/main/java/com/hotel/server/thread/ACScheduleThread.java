package com.hotel.server.thread;

import com.hotel.server.entity.ACRequest;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.PriorityQueue;

/**
 * 空调调度线程
 *
 * @author: 郭晨旭
 * @create: 2024-03-28 11:46
 * @version: 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@Slf4j
public class ACScheduleThread extends Thread {
    // 请求调度队列
    private static final PriorityQueue<ACRequest> requestQueue = new PriorityQueue<>((o1, o2) -> {
        if (!Objects.equals(o1.getPrice(), o2.getPrice())) {
            return ~(new BigDecimal(o1.getPrice()).compareTo(new BigDecimal(o2.getPrice())));
        }
        return o1.getStartTime().compareTo(o2.getStartTime());
    });

    // 已有调度的map
    private static final Map<String, ACThread> runningMap = new HashMap<>();

    /**
     * 从已经有调度的map中删除
     */
    public static void removeOne(String userId) {
        runningMap.remove(userId);
    }

    // todo 调度时从调度队列弹出, 放入调度map
    // todo 循环判断当前队列内请求和已经满足的请求, 满足优先级
}
