package com.hotel.customer.serviceImpl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hotel.common.dto.response.CustomerLoginResp;
import com.hotel.common.entity.Customer;
import com.hotel.common.entity.Room;
import com.hotel.common.service.server.RoomService;
import com.hotel.common.service.timer.TimerService;
import com.hotel.customer.mapper.CustomerMapper;
import com.hotel.common.service.customer.CustomerService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;
import java.util.Date;

/**
 * 顾客表(Customer)表服务实现类
 *
 * @author: guochenxu
 * @create: 2024-03-20 22:37:48
 * @version: 1.0
 */
@DubboService
@Slf4j
public class CustomerServiceImpl extends ServiceImpl<CustomerMapper, Customer> implements CustomerService {

    @Resource
    private CustomerMapper customerMapper;

    @DubboReference
    private TimerService timerService;

    @DubboReference
    private RoomService roomService;

    @Override
    public CustomerLoginResp login(String name, long roomId) {
        Customer customer = customerMapper.selectOne(new LambdaQueryWrapper<Customer>()
                .eq(Customer::getName, name)
                .eq(Customer::getRoom, roomId));
        Room room = roomService.getById(roomId);
        Date now = timerService.getTime();
        if (customer == null || room == null || now.compareTo(customer.getStartTime()) < 0 && now.compareTo(customer.getLeaveTime()) > 0) {
            throw new RuntimeException("该顾客在该时间段内没有预定房间");
        }

        StpUtil.login(customer.getId());
        String token = StpUtil.getTokenValue();
        return CustomerLoginResp.builder().customerId(String.valueOf(customer.getId()))
                .name(customer.getName()).leaveTime(customer.getLeaveTime())
                .token(token).room(String.valueOf(roomId)).temperature(room.getTemperature()).build();
    }
}
