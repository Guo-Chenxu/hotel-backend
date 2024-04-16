package com.hotel.server.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.hotel.common.constants.BillType;
import com.hotel.common.constants.Permission;
import com.hotel.common.dto.R;
import com.hotel.common.dto.response.BillResp;
import com.hotel.common.dto.response.BillStatementResp;
import com.hotel.common.entity.BillStatement;
import com.hotel.common.service.server.BillService;
import com.hotel.server.annotation.CheckPermission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 财务接口
 *
 * @author: 郭晨旭
 * @create: 2024-03-31 14:58
 * @version: 1.0
 */

@RestController
@RequestMapping("bill")
@Slf4j
@Api(tags = "财务控制接口")
public class BillController {
    @DubboReference
    private BillService billService;

    @GetMapping("/billStatement/{customerId}")
    @SaCheckLogin
    @CheckPermission({Permission.FINANCIAL})
    @ApiOperation("获取用户详单")
    public R<BillStatementResp> billStatement(@PathVariable("customerId") String customerId, @RequestParam("type") String type) {
        Set<String> types = Arrays.stream(type.split(",")).collect(Collectors.toSet());
        return R.success(billService.getBillStatement(customerId, types));
    }

    @GetMapping("/bill/{customerId}")
    @SaCheckLogin
    @CheckPermission({Permission.FINANCIAL})
    @ApiOperation("获取用户账单")
    public R<BillResp> bill(@PathVariable("customerId") String customerId) {
        Set<String> types = new HashSet<>();
        types.add(BillType.ROOM);
        types.add(BillType.AC);
        types.add(BillType.FOOD);
        return R.success(billService.getBill(customerId, types));
    }

    // todo 各种报表
}
