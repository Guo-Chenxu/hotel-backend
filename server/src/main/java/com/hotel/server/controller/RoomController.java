package com.hotel.server.controller;


import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaIgnore;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hotel.common.constants.BillType;
import com.hotel.common.constants.Permission;
import com.hotel.common.dto.R;
import com.hotel.common.dto.request.BookRoomReq;
import com.hotel.common.dto.request.PageRoomReq;
import com.hotel.common.dto.response.BillResp;
import com.hotel.common.dto.response.BillStatementResp;
import com.hotel.common.dto.response.RoomInfoResp;
import com.hotel.common.entity.Room;
import com.hotel.common.service.server.BillService;
import com.hotel.common.service.server.CoolService;
import com.hotel.common.service.server.RoomService;
import com.hotel.server.annotation.CheckPermission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.*;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Path;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * (Room)表控制层
 *
 * @author: guochenxu
 * @create: 2024-03-20 10:32:17
 * @version: 1.0
 */
@RestController
@RequestMapping("room")
@Slf4j
@Api(tags = "房间接口")
@CrossOrigin
public class RoomController {

    @DubboReference
    private RoomService roomService;

    @DubboReference
    private BillService billService;

    @DubboReference
    private CoolService coolService;

    @PostMapping("/page")
    @SaCheckLogin
    @CheckPermission({Permission.RECEPTIONIST})
    @ApiOperation("条件分页查询")
    public R<Page<Room>> page(@RequestBody PageRoomReq pageRoomReq) {
        if (pageRoomReq.getPage() == null || pageRoomReq.getPage() < 1) {
            pageRoomReq.setPage(1);
        }
        if (pageRoomReq.getPageSize() == null || pageRoomReq.getPageSize() < 1 || pageRoomReq.getPageSize() > 100) {
            pageRoomReq.setPageSize(20);
        }
        Page<Room> roomPage = roomService.conditionPage(pageRoomReq);
        return R.success(roomPage);
    }

    @PostMapping("/book")
    @SaCheckLogin
    @CheckPermission({Permission.RECEPTIONIST})
    @ApiOperation("订房")
    public R book(@RequestBody BookRoomReq bookRoomReq) {
        return roomService.bookRoom(bookRoomReq)
                ? R.success()
                : R.error();
    }

    @GetMapping("/info/{roomId}")
    @SaCheckLogin
    @CheckPermission({Permission.RECEPTIONIST})
    @ApiOperation("房间详情")
    public R<RoomInfoResp> info(@PathVariable("roomId") String roomId) {
        RoomInfoResp info = roomService.info(Long.parseLong(roomId));
        return info != null
                ? R.success(info)
                : R.error("没有查询到该房间信息");
    }

    @PostMapping("/leave")
    @SaCheckLogin
    @CheckPermission({Permission.RECEPTIONIST})
    @ApiOperation("确认离店")
    public R leave(@RequestParam("roomId") String roomId, @RequestParam("customerId") String customerId) {
        // 先释放用户占用的所有资源 餐饮(这个逻辑改了应该不需要释放了) 纳凉
        coolService.turnOff(String.valueOf(customerId));

        // 然后将用户账单写入数据库
        if (!billService.saveBillStatement(String.valueOf(customerId))) {
            throw new RuntimeException("写入账单失败, 请稍后重试");
        }

        // 最后删除房间和用户
        Boolean leave = roomService.leave(Long.parseLong(roomId), Long.parseLong(customerId));
        return leave
                ? R.success()
                : R.error();
    }
}

