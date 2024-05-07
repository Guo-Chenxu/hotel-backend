package com.hotel.customer.controller;


import cn.dev33.satoken.annotation.SaIgnore;
import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hotel.common.dto.R;
import com.hotel.common.dto.response.CustomerLoginResp;
import com.hotel.common.dto.response.StaffLoginResp;
import com.hotel.common.service.customer.CustomerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.List;


/**
 * 顾客表(Customer)表控制层
 *
 * @author: guochenxu
 * @create: 2024-03-20 22:37:48
 * @version: 1.0
 */
@RestController
@RequestMapping("customer")
@Api(tags = "顾客接口")
@Slf4j
// @CrossOrigin
public class CustomerController {
    /**
     * 服务对象
     */
    @DubboReference
    private CustomerService customerService;


    @PostMapping("/login")
    @ApiOperation("登录")
    @SaIgnore
    public R<CustomerLoginResp> login(@RequestParam("name") String name, @RequestParam("room") String room) {
        return R.success(customerService.login(name, Long.parseLong(room)));
    }

    @GetMapping("/getToken")
    @ApiOperation("后端测试用")
    @SaIgnore
    public R getToken() {
        StpUtil.login(1781186036103704578L);
        String token = StpUtil.getTokenValue();
        return R.success(token);
    }

}

