package com.hotel.server.controller;


import cn.dev33.satoken.annotation.SaCheckLogin;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hotel.common.constants.HttpCode;
import com.hotel.common.constants.Permission;
import com.hotel.common.constants.RedisKeys;
import com.hotel.common.dto.R;
import com.hotel.common.dto.response.PageRoomACResp;
import com.hotel.common.service.server.CacheService;
import com.hotel.common.service.server.CoolService;
import com.hotel.server.annotation.CheckPermission;
import com.hotel.common.entity.ACProperties;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;


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
@CrossOrigin
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
        return R.success(coolService.getACProperties());
    }

    @GetMapping("/pageRoomCool")
    @ApiOperation("分页查询各房间空调状态")
    @SaCheckLogin
    @CheckPermission({Permission.COOL})
    public R<Page<PageRoomACResp>> pageRoomCool(@RequestParam(value = "page", defaultValue = "1") @Min(1) Integer page,
                                           @RequestParam(value = "pageSize", defaultValue = "20")  @Min(1) @Max(100) Integer pageSize) {
        return R.success(coolService.pageRoomAC(page, pageSize));
    }

    @PostMapping("/turnOff")
    @ApiOperation("关闭指定用户空调")
    @SaCheckLogin
    @CheckPermission({Permission.COOL})
    public R turnOff(@RequestParam("customerId") String customerId) {
        coolService.turnOff(customerId);
        return R.success();
    }

}

