package com.hotel.server.serviceImpl;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hotel.common.constants.ACStatus;
import com.hotel.common.dto.response.ACStatusResp;
import com.hotel.common.dto.response.PageRoomACResp;
import com.hotel.common.entity.ACRequest;
import com.hotel.common.entity.Customer;
import com.hotel.common.entity.Room;
import com.hotel.common.service.server.BillService;
import com.hotel.server.service.ACScheduleService;
import com.hotel.server.service.impl.ACScheduleServiceImpl;
import com.hotel.server.ws.WebSocketServer;
import com.hotel.common.constants.RedisKeys;
import com.hotel.common.dto.request.CustomerACReq;
import com.hotel.common.service.customer.CustomerService;
import com.hotel.common.service.server.CacheService;
import com.hotel.common.service.server.CoolService;
import com.hotel.common.service.server.RoomService;
import com.hotel.common.service.timer.TimerService;
import com.hotel.server.config.IndoorTemperatureConfig;
import com.hotel.common.entity.ACProperties;
import com.hotel.server.thread.ACThread;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * 空调服务实现类
 *
 * @author: 郭晨旭
 * @create: 2024-03-28 11:40
 * @version: 1.0
 */

@DubboService(interfaceClass = CoolService.class)
@Slf4j
public class CoolServiceImpl implements CoolService {
    // 用户id - 用户空调
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

    @DubboReference(check = false)
    private BillService billService;

    @Resource
    private WebSocketServer webSocketServer;

    //    private static final ACScheduleService acScheduleService = new ACScheduleServiceImpl();
    @Resource
    @Lazy
    private ACScheduleService acScheduleService;

    @Override
    @Async
    @Transactional(rollbackFor = Exception.class)
    public void syncRoomTemp() {
        List<Room> rooms = roomService.list();
        Map<Long, Long> room2customer = customerService.listCustomers(null)
                .stream().collect(Collectors.toMap(Customer::getRoom, Customer::getId));

        rooms.forEach((e) -> {
            Long customerId = room2customer.get(e.getId());
            ACThread acThread = threadMap.get(String.valueOf(customerId));
            if (acThread != null) {
                e.setTemperature(acThread.getTemperature());
            }
        });

        log.info("同步房间温度, room: {}", rooms);
        roomService.saveOrUpdateBatch(rooms);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public synchronized void watchAC(String userId) {
        //  测试代码, 测试后删除
//        ACThread thread;
//        thread = ACThread.builder().userId(userId).status(ACStatus.OFF).temperature(27.0)
//                .indoorTemperatureConfig(indoorTemperatureConfig)
//                .isRunning(true).recover(true).webSocketServer(webSocketServer)
//                .timerService(timerService).build();
//        thread.start();
//        threadMap.put(userId, thread);

        if (threadMap.containsKey(userId)) {
            return;
        }
        Long roomId = customerService.getById(userId).getRoom();
        Double temperature = roomService.getById(roomId).getTemperature();
        ACThread thread = ACThread.builder().userId(userId).status(ACStatus.OFF).temperature(temperature)
                .indoorTemperatureConfig(indoorTemperatureConfig)
                .isRunning(true).recover(true).webSocketServer(webSocketServer)
                .timerService(timerService).billService(billService).acScheduleService(acScheduleService)
                .build();
        thread.start();
        threadMap.put(userId, thread);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Page<PageRoomACResp> pageRoomAC(Integer page, Integer pageSize) {
        Page<Customer> customerPage = customerService.page(new Page<>(page, pageSize));
        Page<PageRoomACResp> resp = new Page<>();
        BeanUtils.copyProperties(customerPage, resp, "records");
        resp.setRecords(customerPage.getRecords().stream().map((e) -> {
                    ACStatusResp acStatus = this.getACStatus(String.valueOf(e.getId()));
                    if (acStatus.getTemperature() == null) {
                        Room room = roomService.getById(e.getRoom());
                        acStatus.setTemperature(room.getTemperature());
                    }
                    return PageRoomACResp.builder().roomId(String.valueOf(e.getRoom()))
                            .acStatus(acStatus)
                            .build();
                })
                .collect(Collectors.toList()));
        return resp;
    }

    @Override
    public ACStatusResp getACStatus(String userId) {
        ACThread acThread = threadMap.get(userId);
        if (acThread == null) {
            return ACStatusResp.builder().price("0").status(ACStatus.OFF).changeTemp(0.0).build();
        }

        ACStatusResp resp = ACStatusResp.builder().temperature(acThread.getTemperature())
                .status(acThread.getStatus()).build();
        if (!ACStatus.OFF.equals(resp.getStatus())) {
            resp.setPrice(acThread.getPrice());
            resp.setChangeTemp(acThread.getChangeTemperature());
            resp.setTargetTemp(acThread.getTargetTemperature());
        }
        return resp;
    }

    @Override
    public void turnOn(String userId, CustomerACReq customerACReq) {
        ACRequest acRequest = checkRequest(userId, customerACReq);
        acScheduleService.addOne(acRequest);
    }

    @Override
    public void change(String userId, CustomerACReq customerACReq) {
        ACRequest acRequest = checkRequest(userId, customerACReq);
        ACThread acThread = threadMap.get(userId);
        acThread.change(acRequest.getTargetTemperature(), acRequest.getChangeTemperature(),
                acRequest.getStatus(), acRequest.getPrice());
    }


    @Override
    public void turnOff(String userId) {
        ACThread acThread = threadMap.get(userId);
        if (acThread != null) {
            acThread.turnOff();
        }
    }

    @Override
    public Object getACThread(String customerId) {
        return threadMap.get(customerId);
    }


    /**
     * 校验请求数据是否合法
     */
    private ACRequest checkRequest(String userId, CustomerACReq customerACReq) {
        ACProperties acProperties = getACProperties();
        if (acProperties == null) {
            throw new IllegalArgumentException("参数异常, 未设置空调参数, 请联系酒店方开启空调");
        }

        if (customerACReq.getTargetTemperature() == null) {
            customerACReq.setTargetTemperature(acProperties.getDefaultTargetTemp());
        }
        if (customerACReq.getStatus() == null || ACStatus.OFF.equals(customerACReq.getStatus())) {
            customerACReq.setStatus(acProperties.getDefaultStatus());
        }

        Double target = customerACReq.getTargetTemperature();
        if (target.compareTo(acProperties.getUpperBoundTemperature()) > 0
                || target.compareTo(acProperties.getLowerBoundTemperature()) < 0) {
            throw new IllegalArgumentException("参数异常");
        }

        ACRequest acRequest = ACRequest.builder().userId(userId).startTime(timerService.getTime())
                .targetTemperature(customerACReq.getTargetTemperature())
                .status(customerACReq.getStatus()).build();
        if (ACStatus.LOW.equals(acRequest.getStatus())) {
            acRequest.setPrice(acProperties.getLow().getPrice());
            acRequest.setChangeTemperature(acProperties.getLow().getChangeTemperature());
        } else if (ACStatus.MIDDLE.equals(acRequest.getStatus())) {
            acRequest.setPrice(acProperties.getMiddle().getPrice());
            acRequest.setChangeTemperature(acProperties.getMiddle().getChangeTemperature());
        } else if (ACStatus.HIGH.equals(acRequest.getStatus())) {
            acRequest.setPrice(acProperties.getHigh().getPrice());
            acRequest.setChangeTemperature(acProperties.getHigh().getChangeTemperature());
        } else {
            log.error("acRequest: {}", acRequest);
            throw new IllegalArgumentException("参数异常, 空调档位不合法");
        }

        return acRequest;
    }

    @Override
    public ACProperties getACProperties() {
        String json = cacheService.get(RedisKeys.AC_PROPERTIES);
        if (StringUtils.isBlank(json)) {
            throw new RuntimeException("未找到空调参数, 请先完成参数设置");
        }
        return JSON.parseObject(json, ACProperties.class);
    }
}
