package com.hotel.server.controller;


import cn.dev33.satoken.annotation.SaCheckLogin;
import com.hotel.common.constants.Permission;
import com.hotel.common.dto.R;
import com.hotel.common.dto.response.ListPermissionResp;
import com.hotel.common.service.server.PermissionService;
import com.hotel.common.service.server.StaffService;
import com.hotel.server.annotation.CheckPermission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/**
 * 权限控制层
 *
 * @author: guochenxu
 * @create: 2024-03-19 22:09:21
 * @version: 1.0
 */
@RestController
@RequestMapping("permission")
@Slf4j
@Api(tags = "权限控制接口")
public class PermissionController {
    /**
     * 服务对象
     */
    @DubboReference
    private PermissionService permissionService;

    @GetMapping("list")
    @ApiOperation("列出所有权限")
    @SaCheckLogin
    @CheckPermission({Permission.ADMIN})
    public R<List<String>> list() {
        return R.success(permissionService.list());
    }
}

