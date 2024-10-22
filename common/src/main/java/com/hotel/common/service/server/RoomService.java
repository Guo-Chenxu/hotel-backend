package com.hotel.common.service.server;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hotel.common.dto.request.BookRoomReq;
import com.hotel.common.dto.request.PageRoomReq;
import com.hotel.common.dto.response.PageRoomResp;
import com.hotel.common.dto.response.RoomInfoResp;
import com.hotel.common.entity.Room;

import java.util.List;

/**
 * (Room)表服务接口
 *
 * @author: guochenxu
 * @create: 2024-03-20 10:32:19
 * @version: 1.0
 */
public interface RoomService extends IService<Room> {

    /**
     * 分页查询
     */
    Page<PageRoomResp> conditionPage(PageRoomReq pageRoomReq);

    /**
     * 订房
     */
    Boolean bookRoom(BookRoomReq bookRoomReq);

    /**
     * 查询房间详情
     */
    RoomInfoResp info(Long roomId);

    /**
     * 离店
     */
    Boolean leave(Long roomId, Long customerId);

    /**
     * 查询所有房间
     */
    List<String> selectAllRoomPrice(List<Long> roomIds);
}

