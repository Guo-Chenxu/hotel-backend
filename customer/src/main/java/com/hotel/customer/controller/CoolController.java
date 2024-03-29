package com.hotel.customer.controller;


import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaIgnore;
import cn.dev33.satoken.stp.StpUtil;
import com.hotel.common.dto.R;
import com.hotel.common.dto.request.CustomerACReq;
import com.hotel.common.service.server.CoolService;
import com.hotel.customer.ws.ACWebsockt;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;


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

    @Value("${hotel.acURL}")
    private String acUrl;

    @GetMapping("/watchAC")
    @ApiOperation("登陆后调用, 开始监测房间空调")
    @SaCheckLogin
    public R watchAC() {
        coolService.watchAC(StpUtil.getLoginIdAsString());
        return R.success();
    }

    @PostMapping("/turnOn")
    @ApiOperation("开启空调")
    @SaCheckLogin
    public R turnOn(@RequestBody CustomerACReq customerACReq) {
        return R.error();
    }

    @PostMapping("/turnOff")
    @ApiOperation("关闭空调")
    @SaCheckLogin
    public R turnOff() {
        return R.error();
    }

    @PostMapping("/change")
    @ApiOperation("调节目标温度或者风速档位")
    @SaCheckLogin
    public R change(@RequestBody CustomerACReq customerACReq) {
        return R.error();
    }
}

