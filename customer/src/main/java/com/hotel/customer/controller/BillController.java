package com.hotel.customer.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaIgnore;
import cn.dev33.satoken.stp.StpUtil;
import com.hotel.common.constants.BillType;
import com.hotel.common.constants.Permission;
import com.hotel.common.dto.R;
import com.hotel.common.dto.response.BillResp;
import com.hotel.common.dto.response.BillStatementResp;
import com.hotel.common.service.server.BillService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
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
// @CrossOrigin
public class BillController {
    @DubboReference(check = false)
    private BillService billService;

    @GetMapping("/ping/{id}")
    public R ping(@PathVariable("id") String id) {
        return R.success(billService.ping(id));
    }

    @GetMapping("/billStatement")
    @SaCheckLogin
    @ApiOperation("获取用户详单")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "type", value = "以逗号分隔的详单类型, 如\"1,2\", 1=房费, 2=空调, 3=餐饮")
    })
    public R<BillStatementResp> billStatement(@RequestParam("type") String type) {
        Set<String> types = Arrays.stream(type.split(",")).collect(Collectors.toSet());
        return R.success(billService.getBillStatement(StpUtil.getLoginIdAsString(), types));
    }

    @GetMapping("/bill")
    @SaCheckLogin
    @ApiOperation("获取用户账单")
    public R<BillResp> bill() {
        return R.success(billService.getBill(StpUtil.getLoginIdAsString(), new HashSet<>(Arrays.asList(BillType.AC, BillType.ROOM, BillType.FOOD))));
    }

    @GetMapping("/downloadBillStatement")
    @SaCheckLogin
    @ApiOperation("下载用户详单pdf")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "type", value = "以逗号分隔的详单类型, 如\"1,2\", 1=房费, 2=空调, 3=餐饮")
    })
    public void downloadBillStatement(@RequestParam("type") String type,
                                      HttpServletResponse response) throws IOException {
        Set<String> types = Arrays.stream(type.split(",")).collect(Collectors.toSet());
        BillStatementResp resp = billService.getBillStatement(StpUtil.getLoginIdAsString(), types);

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "inline; filename=\"" + StpUtil.getLoginIdAsString() + ".pdf\"");

        ServletOutputStream out = response.getOutputStream();
        byte[] bytes = billService.generateBillStatementPDF(resp, types);
        out.write(bytes);
        out.flush();
        out.close();
    }

    @GetMapping("/downloadBill")
    @SaCheckLogin
    @ApiOperation("下载用户账单pdf")
    @SaIgnore
    public void downloadBill(HttpServletResponse response) throws IOException {
        BillResp bill = billService.getBill(StpUtil.getLoginIdAsString(),
                new HashSet<>(Arrays.asList(BillType.AC, BillType.ROOM, BillType.FOOD)));
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "inline; filename=\"" + StpUtil.getLoginIdAsString() + ".pdf\"");

        ServletOutputStream out = response.getOutputStream();
        byte[] bytes = billService.generateBillPDF(bill);
        out.write(bytes);
        out.flush();
        out.close();
    }
}
