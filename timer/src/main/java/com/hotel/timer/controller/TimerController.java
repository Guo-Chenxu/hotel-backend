package com.hotel.timer.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.hotel.common.service.timer.TimerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * 时间控制器
 *
 * @author: 郭晨旭
 * @create: 2024-03-16 22:10
 * @version: 1.0
 */

@RestController
@RequestMapping("timer")
@Slf4j
@Api(tags = "时间控制器")
public class TimerController {

    @DubboReference
    private TimerService timerService;

    @ApiOperation("获取时间, 建立连接后不断, 使用sse每100ms向前端推一次当前时间")
    @GetMapping("/now")
    public void getTime(HttpServletResponse response) throws IOException, InterruptedException {
        PrintWriter writer = response.getWriter();
        while (true) {
            writer.write("data: " + timerService.getTime() + "\n\n");
            writer.flush();
            Thread.sleep(50);
        }
    }
}
