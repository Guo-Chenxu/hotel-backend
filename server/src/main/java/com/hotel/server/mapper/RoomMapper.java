package com.hotel.server.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import com.hotel.common.entity.Room;

/**
 * (Room)表数据库访问层
 *
 * @author: guochenxu
 * @create: 2024-03-20 10:32:18
 * @version: 1.0
 */
@Mapper
public interface RoomMapper extends BaseMapper<Room> {
}

