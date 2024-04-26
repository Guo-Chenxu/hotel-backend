package com.hotel.server;

import com.hotel.common.entity.Room;
import com.hotel.common.service.server.RoomService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

/**
 * 房间服务测试类
 *
 * @author: 郭晨旭
 * @create: 2024-03-20 10:37
 * @version: 1.0
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
public class RoomServiceTest {

    @DubboReference
    private RoomService roomService;

    @Test
    public void testSelectList() {
        List<String> strings = roomService.selectAllRoomPrice(Arrays.asList(1L, 3L));
        strings.forEach(log::info);
    }
}
