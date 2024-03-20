package com.hotel.server.controller;


import cn.dev33.satoken.annotation.SaCheckLogin;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hotel.common.constants.Permission;
import com.hotel.common.dto.R;
import com.hotel.common.dto.request.PageRoomReq;
import com.hotel.common.entity.Room;
import com.hotel.common.service.server.RoomService;
import com.hotel.server.annotation.CheckPermission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.*;

import lombok.extern.slf4j.Slf4j;


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

}

