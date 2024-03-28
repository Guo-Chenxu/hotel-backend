package com.hotel.server.serviceImpl;

import com.hotel.common.service.customer.CustomerService;
import com.hotel.common.service.server.CoolService;
import com.hotel.common.service.server.RoomService;
import com.hotel.common.service.timer.TimerService;
import com.hotel.server.config.IndoorTemperatureConfig;
import com.hotel.server.thread.ACThread;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.annotation.Tainted;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 空调服务实现类
 *
 * @author: 郭晨旭
 * @create: 2024-03-28 11:40
 * @version: 1.0
 */

@DubboService
@Slf4j
public class CoolServiceImpl implements CoolService {
    // todo init 线程map
    private static final ConcurrentHashMap<String, ACThread> threadMap = new ConcurrentHashMap<>();

    @DubboReference(check = false)
    private TimerService timerService;

    @Resource
    private IndoorTemperatureConfig indoorTemperatureConfig;

    @DubboReference(check = false)
    private CustomerService customerService;

    @DubboReference(check = false)
    private RoomService roomService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public synchronized void addRoom(String userId, HttpServletResponse response) {
        Long roomId = customerService.getById(userId).getRoom();
        Double temperature = roomService.getById(roomId).getTemperature();
        // todo 少参数
        ACThread thread = null;
        try {
            thread = ACThread.builder().userId(userId).status(0).temperature(temperature)
                    .indoorTemperatureConfig(indoorTemperatureConfig)
                    .isRunning(true).recover(true).writer(response.getWriter())
                    .timerService(timerService).build();
        } catch (IOException e) {
            throw new RuntimeException("获取响应流失败, 无法监控房间温度");
        }
        thread.start();
        threadMap.put(userId, thread);
    }
}
