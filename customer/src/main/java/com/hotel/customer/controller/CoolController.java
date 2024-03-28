package com.hotel.customer.controller;


import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaIgnore;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.http.HttpResponse;
import com.hotel.common.dto.R;
import com.hotel.common.dto.response.CustomerLoginResp;
import com.hotel.common.service.customer.CustomerService;
import com.hotel.common.service.server.CoolService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;


/**
 * 纳凉服务控制层
 *
 * @author: guochenxu
 * @create: 2024-03-20 22:37:48
 * @version: 1.0
 */
@RestController
@RequestMapping("cool")
@Slf4j
@Api(tags = "纳凉接口")
public class CoolController {
    @DubboReference
    private CoolService coolService;


    @PostMapping("/watchAC")
    @ApiOperation("登陆后调用, 开始监测房间空调")
    @SaCheckLogin
    public void watchAC(HttpServletResponse response) {
        response.setContentType("text/event-stream;charset=UTF-8");
        coolService.addRoom(StpUtil.getLoginIdAsString(), response);
    }

}

