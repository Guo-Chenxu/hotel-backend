package com.hotel.server.listener;

import com.hotel.common.service.server.CoolService;
import com.hotel.common.service.server.RoomService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * redis订阅消息
 *
 * @author: 郭晨旭
 * @create: 2024-03-31 15:35
 * @version: 1.0
 */
@Service
@Slf4j
public class RedisReceiver {

    @DubboReference
    private CoolService coolService;

    public void receiveMessage(String message) {
        log.info("接受到redis消息: {}, 开始同步房间温度", message);
        coolService.syncRoomTemp();
    }
}
