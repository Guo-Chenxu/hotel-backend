package com.hotel.server.serviceImpl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hotel.common.dto.request.BookRoomReq;
import com.hotel.common.dto.request.PageRoomReq;
import com.hotel.common.dto.response.RoomInfoResp;
import com.hotel.common.entity.Customer;
import com.hotel.common.service.customer.CustomerService;
import com.hotel.common.service.server.BillService;
import com.hotel.common.service.server.CoolService;
import com.hotel.common.service.timer.TimerService;
import com.hotel.server.config.IndoorTemperatureConfig;
import com.hotel.server.mapper.RoomMapper;
import com.hotel.common.entity.Room;
import com.hotel.common.service.server.RoomService;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.function.IntConsumer;

/**
 * (Room)表服务实现类
 *
 * @author: guochenxu
 * @create: 2024-03-20 10:32:19
 * @version: 1.0
 */
@DubboService
@Slf4j
public class RoomServiceImpl extends ServiceImpl<RoomMapper, Room> implements RoomService {

    @Resource
    private RoomMapper roomMapper;

    @DubboReference(check = false)
    private CustomerService customerService;

    @DubboReference(check = false)
    private TimerService timerService;

    @DubboReference(check = false)
    private CoolService coolService;

    @DubboReference(check = false)
    private BillService billService;

    @Resource
    private IndoorTemperatureConfig indoorTemperatureConfig;

    @Override
    public Page<Room> conditionPage(PageRoomReq pageRoomReq) {
        return roomMapper.selectPage(new Page<Room>(pageRoomReq.getPage(), pageRoomReq.getPageSize()),
                new LambdaQueryWrapper<Room>()
                        .eq(pageRoomReq.getRoomNo() != null, Room::getId, pageRoomReq.getRoomNo()));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean bookRoom(BookRoomReq bookRoomReq) {
        Date now = timerService.getTime();
        if (now.compareTo(bookRoomReq.getStartTime()) > 0 || now.compareTo(bookRoomReq.getLeaveTime()) >= 0
                || bookRoomReq.getStartTime().compareTo(bookRoomReq.getLeaveTime()) >= 0) {
            throw new RuntimeException("时间不合法");
        }
        Room room = Room.builder().price(bookRoomReq.getPrice())
                .temperature(indoorTemperatureConfig.getIndoorTemperature())
                .deposit(bookRoomReq.getDeposit()).build();
        int insert = roomMapper.insert(room);
        if (insert < 1) {
            return false;
        }

        Customer customer = Customer.builder().room(room.getId()).name(bookRoomReq.getCustomerName())
                .startTime(bookRoomReq.getStartTime()).leaveTime(bookRoomReq.getLeaveTime()).build();
        return customerService.save(customer);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RoomInfoResp info(Long roomId) {
        Room room = roomMapper.selectById(roomId);
        if (room == null) {
            return null;
        }
        RoomInfoResp resp = RoomInfoResp.builder().roomId(String.valueOf(roomId))
                .price(room.getPrice()).temperature(String.valueOf(room.getTemperature())).build();

        List<Customer> customers = customerService.list(new LambdaQueryWrapper<Customer>()
                .eq(Customer::getRoom, roomId));
        Date now = timerService.getTime();
        for (Customer customer : customers) {
            if (now.compareTo(customer.getStartTime()) >= 0
                    && now.compareTo(customer.getLeaveTime()) <= 0) {
                resp.setCustomerName(customer.getName());
                resp.setStartTime(customer.getStartTime());
                resp.setLeaveTime(customer.getLeaveTime());
                return resp;
            }
        }
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean leave(Long roomId, Long customerId) {
        // 先释放用户占用的所有资源 餐饮(这个逻辑改了应该不需要释放了) 纳凉
        // 然后将用户账单写入数据库
        // 最后删除房间和用户
        coolService.turnOff(String.valueOf(customerId));
        if (!billService.saveBillStatement(String.valueOf(customerId))) {
            throw new RuntimeException("写入账单失败, 请稍后重试");
        }
        if (!this.removeById(roomId) || !customerService.removeById(customerId)) {
            throw new RuntimeException("退房失败, 请稍后重试");
        }
        return true;
    }
}

