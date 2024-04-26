package com.hotel.customer;

import com.hotel.common.entity.Customer;
import com.hotel.common.service.customer.CustomerService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

/**
 * 顾客服务测试类
 *
 * @author: 郭晨旭
 * @create: 2024-04-26 21:07
 * @version: 1.0
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
public class CustomerServiceTest {

    @DubboReference
    private CustomerService customerService;

    @Test
    public void testSelectInTime() {
        List<Customer> customers = customerService.listCustomerInRoom(Arrays.asList(2306L, 2307L, 2308L));
        customers.forEach(System.out::println);
    }
}
