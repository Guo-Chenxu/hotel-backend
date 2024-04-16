package com.hotel.customer.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.stp.StpUtil;
import com.hotel.common.constants.BillType;
import com.hotel.common.constants.Permission;
import com.hotel.common.dto.R;
import com.hotel.common.dto.response.BillResp;
import com.hotel.common.dto.response.BillStatementResp;
import com.hotel.common.service.server.BillService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/billStatement")
    @SaCheckLogin
    @ApiOperation("获取用户详单")
    public R<BillStatementResp> billStatement(@RequestParam("type") String type) {
        Set<String> types = Arrays.stream(type.split(",")).collect(Collectors.toSet());
        return R.success(billService.getBillStatement(StpUtil.getLoginIdAsString(), types));
    }

    @GetMapping("/bill")
    @SaCheckLogin
    @ApiOperation("获取用户账单")
    public R<BillResp> bill() {
        Set<String> types = new HashSet<>();
        types.add(BillType.ROOM);
        types.add(BillType.AC);
        types.add(BillType.FOOD);
        return R.success(billService.getBill(StpUtil.getLoginIdAsString(), types));
    }
}
