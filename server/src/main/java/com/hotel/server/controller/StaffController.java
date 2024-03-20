package com.hotel.server.controller;


import cn.dev33.satoken.annotation.SaIgnore;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hotel.common.dto.R;
import com.hotel.common.dto.response.StaffLoginResp;
import com.hotel.common.entity.Staff;
import com.hotel.common.service.server.StaffService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.*;

import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.List;


/**
 * 酒店服务人员表(Staff)表控制层
 *
 * @author: guochenxu
 * @create: 2024-03-19 22:09:21
 * @version: 1.0
 */
@RestController
@RequestMapping("staff")
@Slf4j
@Api(tags = "酒店服务人员接口")
public class StaffController {
    /**
     * 服务对象
     */
    @DubboReference
    private StaffService staffService;

    @PostMapping("/login")
    @ApiOperation("登录")
    @SaIgnore
    public R<StaffLoginResp> login(@RequestParam("username") String username, @RequestParam("password") String password) {
        return R.success(staffService.login(username, password));
    }

// todo @焦耳 增删改查
//    增改应该可以用同一个接口
//  查包括查所有和根据权限查, 并且都是分页查询
}

