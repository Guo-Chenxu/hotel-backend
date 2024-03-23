package com.hotel.server.controller;


import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaIgnore;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hotel.common.constants.Permission;
import com.hotel.common.dto.R;
import com.hotel.common.dto.request.SaveStaffReq;
import com.hotel.common.dto.response.PageStaffResp;
import com.hotel.common.dto.response.StaffLoginResp;
import com.hotel.common.entity.Staff;
import com.hotel.common.service.server.StaffService;
import com.hotel.server.annotation.CheckPermission;
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

    @PostMapping("/saveStaff")
    @ApiOperation("新增员工")
    @SaCheckLogin
    @CheckPermission({Permission.ADMIN})
    public R saveStaff(@RequestBody SaveStaffReq saveStaffReq) {
        return staffService.save(saveStaffReq)
                ? R.success()
                : R.error();
    }

    @GetMapping("/page")
    @ApiOperation("分页查询")
    @SaCheckLogin
    @CheckPermission({Permission.ADMIN})
    public R<Page<PageStaffResp>> pageStaff(@RequestParam("page") Integer page, @RequestParam("pageSize") Integer pageSize) {
        return R.success(staffService.pageStaff(page, pageSize));
    }

    @PostMapping("/deleteStaff")
    @ApiOperation("删除员工")
    @SaCheckLogin
    @CheckPermission({Permission.ADMIN})
    public R deleteStaff(@RequestBody List<String> ids) {
        return staffService.delete(ids)
                ? R.success()
                : R.error();
    }
}

