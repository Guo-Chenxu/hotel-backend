package com.hotel.customer.controller;


import cn.dev33.satoken.annotation.SaCheckLogin;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hotel.common.constants.Permission;
import com.hotel.common.dto.R;
import com.hotel.common.dto.request.BookRoomReq;
import com.hotel.common.dto.request.PageRoomReq;
import com.hotel.common.dto.response.RoomInfoResp;
import com.hotel.common.entity.Room;
import com.hotel.common.service.server.RoomService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.*;


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

    @DubboReference(check = false)
    private RoomService roomService;


    @GetMapping("/info/{roomId}")
    @SaCheckLogin
    @ApiOperation("房间详情")
    public R<RoomInfoResp> info(@PathVariable("roomId") String roomId) {
        RoomInfoResp info = roomService.info(Long.parseLong(roomId));
        return info != null
                ? R.success(info)
                : R.error("没有查询到该房间信息");
    }
}

