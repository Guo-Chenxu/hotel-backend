package com.hotel.timer.controller;

import cn.dev33.satoken.annotation.SaIgnore;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.date.DateUtil;
import com.hotel.common.dto.R;
import com.hotel.common.service.timer.TimerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.*;

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

    @ApiOperation("获取时间, 建立连接后不断, 使用sse每1s向前端推一次当前时间")
    @GetMapping("/now")
    @SaIgnore
    public void getTime(HttpServletResponse response) throws IOException, InterruptedException {
        response.setContentType("text/event-stream;charset=UTF-8");
        PrintWriter writer = response.getWriter();
        while (true) {
            writer.write("data: "
                    + DateUtil.format(timerService.getTime(), "yyyy-MM-dd HH:mm:ss")
                    + "\n\n");
            writer.flush();
            Thread.sleep(1000);
        }
    }

    @ApiOperation("获取当前时间")
    @GetMapping("/time")
    @SaIgnore
    public R getTime() {
        return R.success(timerService.getTime());
    }
}
