package com.hotel.customer.controller;

import com.hotel.common.dto.R;
import com.hotel.common.service.timer.TimerService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: 郭晨旭
 * @create: 2024-03-16 17:11
 * @version: 1.0
 */

@RestController
@RequestMapping("user")
@Slf4j
@Api(tags = "用户相关接口")
public class TestController {

    @DubboReference
    private TimerService timerService;

    @GetMapping("/time/{id}")
    public R test(@PathVariable("id") String id) {
        return R.success(timerService.getTime() + "  " + id);
    }
}
