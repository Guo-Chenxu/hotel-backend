package com.hotel.server.serviceImpl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hotel.common.dto.request.PageRoomReq;
import com.hotel.server.mapper.RoomMapper;
import com.hotel.common.entity.Room;
import com.hotel.common.service.server.RoomService;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;

/**
 * (Room)表服务实现类
 *
 * @author: guochenxu
 * @create: 2024-03-20 10:32:19
 * @version: 1.0
 */
@DubboService
@Slf4j
public class RoomServiceImpl extends ServiceImpl<RoomMapper, Room> implements RoomService {

    @Resource
    private RoomMapper roomMapper;

    @Override
    public Page<Room> conditionPage(PageRoomReq pageRoomReq) {
        return roomMapper.selectPage(new Page<Room>(pageRoomReq.getPage(), pageRoomReq.getPageSize()),
                new LambdaQueryWrapper<Room>()
                        .eq(pageRoomReq.getRoomNo() != null, Room::getId, pageRoomReq.getRoomNo()));
    }
}

