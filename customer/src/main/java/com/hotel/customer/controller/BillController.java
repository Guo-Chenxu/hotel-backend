package com.hotel.customer.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.hotel.common.constants.Permission;
import com.hotel.common.dto.R;
import com.hotel.common.dto.response.BillResp;
import com.hotel.common.dto.response.BillStatementResp;
import com.hotel.common.service.server.BillService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @PostMapping("/billStatement")
    @SaCheckLogin
    @ApiOperation("获取用户详单")
    public R<BillStatementResp> billStatement() {
        return null;
    }

    @PostMapping("/bill")
    @SaCheckLogin
    @ApiOperation("获取用户账单")
    public R<BillResp> bill() {
        return null;
    }
}
