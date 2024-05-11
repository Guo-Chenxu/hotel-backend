package com.hotel.server;

import cn.hutool.core.lang.hash.Hash;
import com.hotel.common.service.timer.TimerService;
import com.hotel.server.thread.ACThread;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Map;

/**
 * 空调线程测试
 *
 * @author: 郭晨旭
 * @create: 2024-05-04 00:26
 * @version: 1.0
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
public class ACThreadTest {

    @DubboReference
    private TimerService timerService;

    @Test
    public void testMap() {
        Map<String, ACThread> map = new HashMap<>();
        ACThread t1 = ACThread.builder().userId("1").targetTemperature(11.11).build();
        ACThread t2 = ACThread.builder().userId("1").targetTemperature(22.22).build();
        map.put(t1.getUserId(), t1);
        System.out.println(map);
        map.put(t2.getUserId(), t2);
        System.out.println(map);
    }

    @Test
    public void testTime() throws InterruptedException {
        log.info("{}", System.currentTimeMillis());
        log.info("{}", timerService.getTime().getTime());
        Thread.sleep(1000);
        log.info("{}", System.currentTimeMillis());
        log.info("{}", timerService.getTime().getTime());

        long lastTime = timerService.getTime().getTime();
        log.info("lastTime: {}", lastTime);
        long now = timerService.getTime().getTime();
        long dur = (now - lastTime) / 1000;
        while (dur < 1) {
            now = timerService.getTime().getTime();
            dur = (now - lastTime) / 1000;
        }
        log.info("now: {}", now);
        log.info("dur: {}", dur);
    }
}
