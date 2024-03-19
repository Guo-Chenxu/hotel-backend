package com.hotel.timer.serviceImpl;


import com.hotel.common.service.timer.TimerService;
import com.hotel.timer.thread.TimeThread;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.PostConstruct;
import java.util.*;

@DubboService
@Slf4j
public class TimerServiceImpl implements TimerService {
    private static final TimeThread timeThread = TimeThread.getInstance();

    @Value("${timer.speed}")
    private long speed;

    @PostConstruct
    private void init() {
        log.info("模拟时间开始初始化");
        this.startSimulateThread(new Date());
    }

    private void startSimulateThread(Date startTime) {
        if (startTime == null) {
            startTime = new Date();
        }

        timeThread.setNow(startTime);
        timeThread.setSpeed(speed);
        timeThread.start();
        log.info("开始模拟时间，初始模拟时间：{}, 时间流速: {}", startTime, speed);
    }

    @Override
    public Date getTime() {
//        log.info("当前模拟时间，为：{}", timeThread.getNow());
        return timeThread.getNow();
    }
}
