package com.hotel.server.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaIgnore;
import com.hotel.common.constants.BillType;
import com.hotel.common.constants.Permission;
import com.hotel.common.dto.R;
import com.hotel.common.dto.request.ReportReq;
import com.hotel.common.dto.response.BillResp;
import com.hotel.common.dto.response.BillStatementResp;
import com.hotel.common.dto.response.ReportResp;
import com.hotel.common.entity.BillStatement;
import com.hotel.common.entity.CustomerAC;
import com.hotel.common.entity.CustomerFood;
import com.hotel.common.entity.Food;
import com.hotel.common.service.customer.CustomerService;
import com.hotel.common.service.server.BillService;
import com.hotel.common.service.timer.TimerService;
import com.hotel.common.utils.DateUtil;
import com.hotel.server.annotation.CheckPermission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;
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
@Api(tags = "财务接口")
// @CrossOrigin
public class BillController {
    @DubboReference
    private BillService billService;

    @DubboReference(check = false)
    private CustomerService customerService;

    @DubboReference(check = false)
    private TimerService timerService;

    @GetMapping("/ping/{id}")
    public R ping(@PathVariable("id") String id) {
        return R.success(customerService.ping(id));
    }

    @GetMapping("/billStatement/{customerId}")
    @SaCheckLogin
    @CheckPermission({Permission.FINANCIAL, Permission.RECEPTIONIST})
    @ApiOperation("获取用户详单")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "type", value = "以逗号分隔的详单类型, 如\"1,2\", 1=房费, 2=空调, 3=餐饮")
    })
    public R<BillStatementResp> billStatement(@PathVariable("customerId") String customerId, @RequestParam("type") String type) {
        Set<String> types = Arrays.stream(type.split(",")).collect(Collectors.toSet());
        return R.success(billService.getBillStatement(customerId, types));
    }

    @GetMapping("/bill/{customerId}")
    @SaCheckLogin
    @CheckPermission({Permission.FINANCIAL, Permission.RECEPTIONIST})
    @ApiOperation("获取用户账单")
    public R<BillResp> bill(@PathVariable("customerId") String customerId) {
        return R.success(billService.getBill(customerId, new HashSet<>(Arrays.asList(BillType.AC, BillType.ROOM, BillType.FOOD))));
    }

    @GetMapping("/downloadBillStatement/{customerId}")
    @SaCheckLogin
    @CheckPermission({Permission.FINANCIAL, Permission.RECEPTIONIST})
    @ApiOperation("下载用户详单pdf")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "type", value = "以逗号分隔的详单类型, 如\"1,2\", 1=房费, 2=空调, 3=餐饮")
    })
    public void downloadBillStatement(@PathVariable("customerId") String customerId, @RequestParam("type") String type,
                                      HttpServletResponse response) throws IOException {
        Set<String> types = Arrays.stream(type.split(",")).collect(Collectors.toSet());
        BillStatementResp resp = billService.getBillStatement(customerId, types);

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "inline; filename=\"" + customerId + ".pdf\"");

        ServletOutputStream out = response.getOutputStream();
        byte[] bytes = billService.generateBillStatementPDF(resp, types);
        out.write(bytes);
        out.flush();
        out.close();
    }

    @GetMapping("/downloadBill/{customerId}")
    @SaCheckLogin
    @CheckPermission({Permission.FINANCIAL, Permission.RECEPTIONIST})
    @ApiOperation("下载用户账单pdf")
    public void downloadBill(@PathVariable("customerId") String customerId, HttpServletResponse response) throws IOException {
        BillResp bill = billService.getBill(customerId, new HashSet<>(Arrays.asList(BillType.AC, BillType.ROOM, BillType.FOOD)));
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "inline; filename=\"" + customerId + ".pdf\"");

        ServletOutputStream out = response.getOutputStream();
        byte[] bytes = billService.generateBillPDF(bill);
        out.write(bytes);
        out.flush();
        out.close();
    }

    @PostMapping("/report")
    @SaCheckLogin
    @CheckPermission({Permission.FINANCIAL})
    @ApiOperation("报表")
    public R<ReportResp> report(@RequestBody ReportReq req) {
        if (req.getStartTime() == null) {
            req.setStartTime(DateUtil.get7DaysAgo(timerService.getTime()));
        }
        if (req.getEndTime() == null) {
            req.setEndTime(DateUtil.getDayAgo(timerService.getTime()));
        }
        return R.success(billService.report(DateUtil.getDayZeroTime(req.getStartTime()),
                DateUtil.getDayEndTime(req.getEndTime()), req.getType()));
    }
}
