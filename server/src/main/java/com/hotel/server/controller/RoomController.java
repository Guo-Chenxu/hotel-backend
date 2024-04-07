package com.hotel.server.controller;


import cn.dev33.satoken.annotation.SaCheckLogin;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hotel.common.constants.Permission;
import com.hotel.common.dto.R;
import com.hotel.common.dto.request.BookRoomReq;
import com.hotel.common.dto.request.PageRoomReq;
import com.hotel.common.dto.response.BillResp;
import com.hotel.common.dto.response.BillStatementResp;
import com.hotel.common.dto.response.RoomInfoResp;
import com.hotel.common.entity.Room;
import com.hotel.common.service.server.BillService;
import com.hotel.common.service.server.RoomService;
import com.hotel.server.annotation.CheckPermission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.*;

import lombok.extern.slf4j.Slf4j;

import javax.ws.rs.Path;


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
public class RoomController {

    @DubboReference
    private RoomService roomService;

    @DubboReference
    private BillService billService;

    @PostMapping("/page")
    @SaCheckLogin
    @CheckPermission({Permission.RECEPTIONIST})
    @ApiOperation("条件分页查询")
    public R<Page<Room>> page(@RequestBody PageRoomReq pageRoomReq) {
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
        Boolean leave = roomService.leave(Long.parseLong(roomId), Long.parseLong(customerId));
        return leave
                ? R.success()
                : R.error();
    }

    @GetMapping("/bill/{customerId}")
    @SaCheckLogin
    @CheckPermission({Permission.RECEPTIONIST})
    @ApiOperation("查看用户账单")
    public R<BillResp> bill(@PathVariable("customerId") String customerId) {
        return R.success(billService.getBill(customerId));
    }

    @GetMapping("/billStatement/{customerId}")
    @SaCheckLogin
    @CheckPermission({Permission.RECEPTIONIST})
    @ApiOperation("查看用户详单")
    public R<BillStatementResp> billStatement(@PathVariable("customerId") String customerId) {
        return R.success(billService.getBillStatement(customerId));
    }
}

