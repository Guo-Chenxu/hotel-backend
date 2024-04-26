package com.hotel.server.serviceImpl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hotel.common.dto.request.BookRoomReq;
import com.hotel.common.dto.request.PageRoomReq;
import com.hotel.common.dto.response.PageRoomResp;
import com.hotel.common.dto.response.RoomInfoResp;
import com.hotel.common.entity.Customer;
import com.hotel.common.service.customer.CustomerService;
import com.hotel.common.service.server.BillService;
import com.hotel.common.service.server.CoolService;
import com.hotel.common.service.timer.TimerService;
import com.hotel.common.utils.DateUtil;
import com.hotel.server.config.IndoorTemperatureConfig;
import com.hotel.server.mapper.RoomMapper;
import com.hotel.common.entity.Room;
import com.hotel.common.service.server.RoomService;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.IntConsumer;
import java.util.stream.Collectors;

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

    @Resource
    private IndoorTemperatureConfig indoorTemperatureConfig;

    @Override
    @Transactional(rollbackFor = Exception.class)
    // todo 注意测试
    public Page<PageRoomResp> conditionPage(PageRoomReq pageRoomReq) {
        Page<Room> roomPage = roomMapper.selectPage(new Page<>(pageRoomReq.getPage(), pageRoomReq.getPageSize()),
                new LambdaQueryWrapper<Room>()
                        .eq(pageRoomReq.getRoomNo() != null, Room::getId, pageRoomReq.getRoomNo()));
        Page<PageRoomResp> resp = new Page<>();
        BeanUtils.copyProperties(roomPage, resp, "records");

        Map<Long, String> room2Name = customerService
                .listCustomerInRoom(roomPage.getRecords().stream().map(Room::getId).collect(Collectors.toList()))
                .stream().collect(Collectors.toMap(Customer::getRoom, Customer::getName));

        List<PageRoomResp> pageRoomResps = roomPage.getRecords().stream().map((r) ->
                PageRoomResp.builder().roomNo(String.valueOf(r.getId()))
                        .price(r.getPrice()).temperature(r.getTemperature()).deposit(r.getDeposit())
                        .username(room2Name.get(r.getId())).build()).collect(Collectors.toList());

        resp.setRecords(pageRoomResps);
        return resp;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean bookRoom(BookRoomReq bookRoomReq) {
        Date now = timerService.getTime();
        // 房间相关时间均只判定到小时
        if (DateUtil.compareHour(now, bookRoomReq.getStartTime()) > 0
                || DateUtil.compareHour(bookRoomReq.getStartTime(), bookRoomReq.getLeaveTime()) >= 0) {
            throw new RuntimeException("时间不合法, 入住时间应当大于等于当前时间, 离店时间应当大于入住时间");
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
        return customerService.saveCustomer(customer);
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

        List<Customer> customers = customerService.listCustomers(roomId);
        Date now = timerService.getTime();
        for (Customer customer : customers) {
            if (DateUtil.compareHour(now, customer.getStartTime()) >= 0
                    && DateUtil.compareHour(now, customer.getLeaveTime()) <= 0) {
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
        Customer customer = customerService.getById(customerId);
        customer.setLeaveTime(timerService.getTime());
        customerService.saveCustomer(customer);
        if (!this.removeById(roomId) || !customerService.removeById(customerId)) {
            throw new RuntimeException("退房失败, 请稍后重试");
        }
        return true;
    }

    @Override
    public List<String> selectAllRoomPrice(List<Long> roomIds) {
        if (CollectionUtils.isEmpty(roomIds)) {
            return new ArrayList<>();
        }
        return roomMapper.selectAllRoomsPrices(roomIds);
    }
}

