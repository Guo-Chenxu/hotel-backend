package com.hotel.server.service.impl;

import cn.hutool.core.util.IdUtil;
import com.hotel.common.constants.ACStatus;
import com.hotel.common.constants.RedisKeys;
import com.hotel.common.entity.ACProperties;
import com.hotel.common.entity.ACRequest;
import com.hotel.common.service.server.CacheService;
import com.hotel.common.service.server.CoolService;
import com.hotel.server.service.ACScheduleService;
import com.hotel.server.thread.ACThread;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

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
//@Async
public class ACScheduleServiceImpl implements ACScheduleService {
    // 请求调度队列
    private static final PriorityQueue<ACRequest> requestQueue = new PriorityQueue<>((o1, o2) -> {
        if (!Objects.equals(o1.getPrice(), o2.getPrice())) {
            return new BigDecimal(o2.getPrice()).compareTo(new BigDecimal(o1.getPrice()));
        }
        return o1.getStartTime().compareTo(o2.getStartTime());
    });

    // 服务队列
    private static final PriorityQueue<ACRequest> runningQueue = new PriorityQueue<>((o1, o2) -> {
        if (!Objects.equals(o1.getPrice(), o2.getPrice())) {
            return new BigDecimal(o1.getPrice()).compareTo(new BigDecimal(o2.getPrice()));
        }
        return o1.getStartTime().compareTo(o2.getStartTime());
    });

    @DubboReference
    private CacheService cacheService;

    @DubboReference(check = false)
    private CoolService coolService;

    /**
     * 只有关闭需要删除，关闭的话从调度和运行队列中均需要删除
     */
    @Override
    @Async
    public void removeOne(String userId) {
        boolean isRunning = runningQueue.removeIf((e) -> userId.equals(e.getUserId()));
        requestQueue.removeIf((e) -> userId.equals(e.getUserId()));
        if (isRunning && !requestQueue.isEmpty()) {
            // 只有正在运行的被删除后需要调度
            this.newSchedule();
        }
    }

    @Override
    @Async
    public void addOne(ACRequest acRequest) {
        addUniqueQueue(acRequest, requestQueue);
        this.newSchedule();
    }

    @Override
    public boolean isRequestEmpty() {
        return requestQueue.isEmpty();
    }

    /**
     * 双优先队列调度
     */
    public synchronized void newSchedule() {
        String traceId = IdUtil.getSnowflakeNextIdStr();
        log.info("=======================调度前 {}========================", traceId);
        log.info("运行队列: {}, 运行的空调个数: {}", runningQueue, runningQueue.size());
        log.info("调度队列: {}, 等待的空调个数: {}", requestQueue, requestQueue.size());
        ACProperties acProperties = (ACProperties) cacheService.get(RedisKeys.AC_PROPERTIES, ACProperties.class);

        // 判断先满足让运行空调达到最大值
        // 然后满足运行空调档位最高
        // 令人深思的调度方式, 使我的大脑旋转 😵
        boolean flag = true;
        while (runningQueue.size() <= acProperties.getCount() && !requestQueue.isEmpty() && flag) {
            if (runningQueue.size() < acProperties.getCount()) {
                ACRequest acRequest = requestQueue.poll();
                log.info("空调请求: {}, 进入服务队列", acRequest);
                ACThread acThread = (ACThread) coolService.getACThread(acRequest.getUserId());
                acThread.turnOn(acRequest.getTargetTemperature(), acRequest.getChangeTemperature(),
                        acRequest.getStatus(), acRequest.getPrice());
                addUniqueQueue(acRequest, runningQueue);
            } else {
                flag = false;
                ACRequest running = runningQueue.peek();
                ACRequest request = requestQueue.peek();

                if (request != null && running != null
                        && new BigDecimal(request.getPrice()).compareTo(new BigDecimal(running.getPrice())) > 0) {
                    // 可以调度
                    flag = true;
                    requestQueue.poll();
                    runningQueue.poll();
                    log.info("发生了调度, 正在运行的: {}, 等待被调度进来的: {}", running, request);

                    // 剥夺
                    runningQueue.removeIf((e) -> e.getUserId().equals(running.getUserId()));
                    ACThread acThread = (ACThread) coolService.getACThread(running.getUserId());
                    ACRequest oldRequest = acThread.turnOffInSchedule();
                    if (oldRequest != null) {
                        addUniqueQueue(oldRequest, requestQueue);
                        acThread.setReq(oldRequest);
                        acThread.setStatus(ACStatus.WAITING);
                    }

                    // 占用
                    ACThread ready = (ACThread) coolService.getACThread(request.getUserId());
                    ready.turnOn(request.getTargetTemperature(), request.getChangeTemperature(),
                            request.getStatus(), request.getPrice());
                    addUniqueQueue(request, runningQueue);
                }
            }
        }
        log.info("=======================调度后 {}========================", traceId);
        log.info("运行队列: {}, 运行的空调个数: {}", runningQueue, runningQueue.size());
        log.info("调度队列: {}, 等待的空调个数: {}", requestQueue, requestQueue.size());
    }

    private static synchronized void addUniqueQueue(ACRequest acRequest, PriorityQueue<ACRequest> queue) {
        if (acRequest == null) {
            return;
        }
        queue.removeIf((e) -> Objects.equals(e.getUserId(), acRequest.getUserId()));
        queue.add(acRequest);
    }
}
