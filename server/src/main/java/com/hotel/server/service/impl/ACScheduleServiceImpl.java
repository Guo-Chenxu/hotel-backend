package com.hotel.server.service.impl;

import com.hotel.common.constants.ACStatus;
import com.hotel.common.constants.RedisKeys;
import com.hotel.common.entity.ACProperties;
import com.hotel.common.entity.ACRequest;
import com.hotel.common.service.server.CacheService;
import com.hotel.common.service.server.CoolService;
import com.hotel.server.service.ACScheduleService;
import com.hotel.server.thread.ACThread;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

/**
 * 空调调度线程
 *
 * @author: 郭晨旭
 * @create: 2024-03-28 11:46
 * @version: 1.0
 */
@Slf4j
@Lazy
@Service
public class ACScheduleServiceImpl implements ACScheduleService {
    // 请求调度队列
    private static final PriorityQueue<ACRequest> requestQueue = new PriorityQueue<>((o1, o2) -> {
        if (!Objects.equals(o1.getPrice(), o2.getPrice())) {
            return ~(new BigDecimal(o1.getPrice()).compareTo(new BigDecimal(o2.getPrice())));
        }
        return o1.getStartTime().compareTo(o2.getStartTime());
    });

    // 已有调度的map
    private static final Map<String, ACThread> runningMap = new HashMap<>();

    // todo 缺少时间片轮转调度, 目前两个想法
    // 1. 像定时更新一样, 每隔5分钟发送一条消息, 接受到消息后开始清理超过5分钟的工作的空调
    // 2. 第二个想法还没想好 (其实是太粪了, 很不靠谱)

    @DubboReference
    private CacheService cacheService;

    @DubboReference(check = false)
    private CoolService coolService;

    /**
     * 从已经有调度的map中删除
     */
    @Override
    public void removeOne(String userId) {
        runningMap.remove(userId);
        this.schedule();
    }

    @Override
    public void addOne(ACRequest acRequest) {
        requestQueue.add(acRequest);
        this.schedule();
    }

    /**
     * 调度
     */
    public synchronized void schedule() {
        ACProperties acProperties = (ACProperties) cacheService.get(RedisKeys.AC_PROPERTIES, ACProperties.class);
        // 先满足让运行空调达到最大值
        while (runningMap.size() < acProperties.getCount() && !requestQueue.isEmpty()) {
            ACRequest acRequest = requestQueue.poll();
            ACThread acThread = (ACThread) coolService.getACThread(acRequest.getUserId());
            acThread.turnOn(acRequest.getTargetTemperature(), acRequest.getChangeTemperature(),
                    acRequest.getStatus(), acRequest.getPrice());
            runningMap.put(acRequest.getUserId(), acThread);
        }
        if (requestQueue.isEmpty()) {
            return;
        }

        // 然后满足运行空调档位最高
        boolean flag = true;
        while (runningMap.size() <= acProperties.getCount() && !requestQueue.isEmpty() && flag) {
            flag = false;
            ACRequest acRequest = requestQueue.peek();
            Set<String> keys = runningMap.keySet();
            for (String userId : keys) {
                ACThread acThread = runningMap.get(userId);
                if (new BigDecimal(acThread.getPrice())
                        .compareTo(new BigDecimal(acThread.getPrice())) < 0) {
                    flag = true;
                    requestQueue.poll();

                    // 剥夺
                    runningMap.remove(userId);
                    ACRequest oldRequest = acThread.turnOff();
                    acThread.setStatus(ACStatus.WAITING);
                    requestQueue.add(oldRequest);

                    // 占用
                    ACThread ready = (ACThread) coolService.getACThread(acRequest.getUserId());
                    ready.turnOn(acRequest.getTargetTemperature(), acRequest.getChangeTemperature(),
                            acRequest.getStatus(), acRequest.getPrice());
                    runningMap.put(acRequest.getUserId(), ready);
                    break;
                }
            }
        }
    }
}
