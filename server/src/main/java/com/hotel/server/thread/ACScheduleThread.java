package com.hotel.server.thread;

import com.hotel.server.entity.ACRequest;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

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
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
public class ACScheduleThread extends Thread {
    // 请求调度队列
    private static final PriorityQueue<ACRequest> pq = new PriorityQueue<>((o1, o2) -> {
        if (!Objects.equals(o1.getChangeTemperature(), o2.getChangeTemperature())) {
            return o1.getChangeTemperature().compareTo(o2.getChangeTemperature());
        }
        return o1.getStartTime().compareTo(o2.getStartTime());
    });
}
