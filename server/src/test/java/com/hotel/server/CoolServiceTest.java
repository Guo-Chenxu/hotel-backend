package com.hotel.server;

import com.hotel.common.entity.ACRequest;
import com.hotel.server.service.ACScheduleService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.ResourceLock;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.PriorityQueue;
import java.util.logging.Handler;

/**
 * 空调服务测试
 *
 * @author: 郭晨旭
 * @create: 2024-04-25 17:36
 * @version: 1.0
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
public class CoolServiceTest {
    @Resource
    private ACScheduleService acScheduleService;

    @Test
    public void testGet() {
        log.info("ac schedule = {}", acScheduleService);
    }
}
