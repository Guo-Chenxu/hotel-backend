package com.hotel.common.service.customer;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hotel.common.dto.response.CustomerLoginResp;
import com.hotel.common.entity.Customer;

import java.util.List;

/**
 * 顾客表(Customer)表服务接口
 *
 * @author: guochenxu
 * @create: 2024-03-20 22:37:48
 * @version: 1.0
 */
public interface CustomerService extends IService<Customer> {

    String ping(String id);

    /**
     * 顾客登录
     */
    CustomerLoginResp login(String name, long room);

    /**
     * 保存用户
     */
    boolean saveCustomer(Customer customer);

    /**
     * 根据房间号列出所有用户
     */
    List<Customer> listCustomers(Long roomId);
}

