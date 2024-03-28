package com.hotel.server.serviceImpl;

import com.alibaba.fastjson2.JSON;
import com.hotel.common.constants.ACStatus;
import com.hotel.common.constants.HttpCode;
import com.hotel.common.constants.RedisKeys;
import com.hotel.common.dto.R;
import com.hotel.common.dto.request.CustomerACReq;
import com.hotel.common.service.customer.CustomerService;
import com.hotel.common.service.server.CacheService;
import com.hotel.common.service.server.CoolService;
import com.hotel.common.service.server.RoomService;
import com.hotel.common.service.timer.TimerService;
import com.hotel.server.config.IndoorTemperatureConfig;
import com.hotel.server.entity.ACProperties;
import com.hotel.server.thread.ACThread;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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

    @DubboReference
    private CacheService cacheService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public synchronized void addRoom(String userId, HttpServletResponse response) {
        Long roomId = customerService.getById(userId).getRoom();
        Double temperature = roomService.getById(roomId).getTemperature();
        // todo 少参数
        ACThread thread = null;
        try {
            thread = ACThread.builder().userId(userId).status(ACStatus.OFF).temperature(temperature)
                    .indoorTemperatureConfig(indoorTemperatureConfig)
                    .isRunning(true).recover(true).writer(response.getWriter())
                    .timerService(timerService).build();
        } catch (IOException e) {
            throw new RuntimeException("获取响应流失败, 无法监控房间温度");
        }
        thread.start();
        threadMap.put(userId, thread);
    }

    /**
     * 校验请求数据是否合法
     */
    private void checkRequest(CustomerACReq customerACReq) {
        ACProperties acProperties = getACProperties();
        if (acProperties == null) {
            throw new IllegalArgumentException("参数异常, 未设置空调参数, 请联系酒店方开启空调");
        }

        if (customerACReq.getTargetTemperature() == null) {
            customerACReq.setTargetTemperature(acProperties.getDefaultTargetTemp());
        }
        if (customerACReq.getStatus() == null || customerACReq.getStatus() == 0) {
            customerACReq.setStatus(acProperties.getDefaultStatus());
        }

        Double target = customerACReq.getTargetTemperature();
        if (target.compareTo(acProperties.getUpperBoundTemperature()) > 0
                || target.compareTo(acProperties.getLowerBoundTemperature()) < 0) {
            throw new IllegalArgumentException("参数异常");
        }
    }

    private ACProperties getACProperties() {
        String json = cacheService.get(RedisKeys.AC_PROPERTIES);
        if (StringUtils.isBlank(json)) {
            return null;
        }
        return JSON.parseObject(json, ACProperties.class);
    }
}
