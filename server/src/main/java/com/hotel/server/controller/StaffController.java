package com.hotel.server.controller;


import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaIgnore;
import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hotel.common.constants.Permission;
import com.hotel.common.dto.R;
import com.hotel.common.dto.request.SaveStaffReq;
import com.hotel.common.dto.response.PageStaffResp;
import com.hotel.common.dto.response.StaffLoginResp;
import com.hotel.common.entity.Staff;
import com.hotel.common.service.server.StaffService;
import com.hotel.common.service.timer.TimerService;
import com.hotel.server.annotation.CheckPermission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.*;

import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
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
// @CrossOrigin
public class StaffController {
    /**
     * 服务对象
     */
    @DubboReference
    private StaffService staffService;

    @DubboReference
    private TimerService timerService;

    @GetMapping("/token")
    @SaIgnore
    @ApiOperation("获取token, 后端测试用")
    public R getToken() {
        Staff staff = staffService.getOne(new LambdaQueryWrapper<Staff>().eq(Staff::getUsername, "admin"));
        StpUtil.login(staff.getId());
        return R.success(StpUtil.getTokenValue());
    }

    @PostMapping("/login")
    @ApiOperation("登录")
    @SaIgnore
    public R<StaffLoginResp> login(@RequestParam("username") String username, @RequestParam("password") String password) {
        return R.success(staffService.login(username, password));
    }

    @PostMapping("/saveStaff")
    @ApiOperation("新增或修改员工")
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
    public R<Page<PageStaffResp>> pageStaff(@RequestParam(value = "page", defaultValue = "1") @Min(1) Integer page,
                                            @RequestParam(value = "pageSize", defaultValue = "20") @Min(1) @Max(100) Integer pageSize) {
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


    @PostMapping("/time/speed")
    @ApiOperation("设置时间流速, 系统1秒等于实际speed秒")
    @SaCheckLogin
    @CheckPermission({Permission.ADMIN})
    public R setSpeed(@RequestParam("speed") String speed) {
        Double s = Double.parseDouble(speed);
        if (s.compareTo(0.001) < 0) {
            return R.error("速度不合法, 必须大于0.001");
        }
        timerService.setSpeed(s);
        return R.success();
    }

    @GetMapping("/time/speed")
    @ApiOperation("查看时间流速")
    @SaCheckLogin
    @CheckPermission({Permission.ADMIN})
    public R getSpeed() {
        return R.success("执行成功", timerService.getSpeed());
    }
}

