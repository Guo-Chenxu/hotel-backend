package com.hotel.server;

import com.hotel.common.utils.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

/**
 * 时间功能测试
 *
 * @author: 郭晨旭
 * @create: 2024-04-26 09:32
 * @version: 1.0
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
public class DateUtilTest {

    @Test
    public void testDateFormat() {
        Date now = new Date();
        log.info("now: {}", now);
        log.info("7 days ago: {}", DateUtil.get7DaysAgo(now));
        log.info("1 day age: {}", DateUtil.getDayAgo(now));
        log.info("start time: {}", DateUtil.getDayZeroTime(now));
        log.info("end time: {}", DateUtil.getDayEndTime(now));
    }
}
