package com.hotel.server.controller;


import cn.dev33.satoken.annotation.SaCheckLogin;
import com.alibaba.fastjson2.JSON;
import com.hotel.common.constants.HttpCode;
import com.hotel.common.constants.Permission;
import com.hotel.common.constants.RedisKeys;
import com.hotel.common.dto.R;
import com.hotel.common.service.server.CacheService;
import com.hotel.common.service.server.CoolService;
import com.hotel.common.service.server.PermissionService;
import com.hotel.server.annotation.CheckPermission;
import com.hotel.server.entity.ACProperties;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * 空调控制层
 *
 * @author: guochenxu
 * @create: 2024-03-19 22:09:21
 * @version: 1.0
 */
@RestController
@RequestMapping("cool")
@Slf4j
@Api(tags = "空调控制接口")
public class CoolController {
    @DubboReference
    private CoolService coolService;

    @DubboReference
    private CacheService cacheService;

    @PostMapping("/properties")
    @SaCheckLogin
    @CheckPermission({Permission.COOL})
    @ApiOperation("设置空调参数")
    public R setProperties(@RequestBody ACProperties acProperties) {
        cacheService.add(RedisKeys.AC_PROPERTIES, JSON.toJSONString(acProperties));
        return R.success();
    }

    @GetMapping("/properties")
    @ApiOperation("查看空调参数")
    @SaCheckLogin
    @CheckPermission({Permission.COOL})
    public R<ACProperties> getProperties() {
        String json = cacheService.get(RedisKeys.AC_PROPERTIES);
        if (StringUtils.isBlank(json)) {
            return R.error(HttpCode.PARAM_WRONG, "未找到空调参数, 请先完成参数设置");
        }
        return R.success(JSON.parseObject(json, ACProperties.class));
    }
}

