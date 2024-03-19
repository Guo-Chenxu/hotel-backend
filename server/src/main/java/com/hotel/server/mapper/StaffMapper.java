package com.hotel.server.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import com.hotel.common.entity.Staff;

/**
 * 酒店服务人员表(Staff)表数据库访问层
 *
 * @author: guochenxu
 * @create: 2024-03-19 22:09:21
 * @version: 1.0
 */
@Mapper
public interface StaffMapper extends BaseMapper<Staff> {

}

