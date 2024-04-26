package com.hotel.server.dao;


import com.hotel.common.entity.CustomerAC;
import com.hotel.common.entity.CustomerFood;

import java.util.Date;
import java.util.List;

/**
 * 聊天会话数据库接口
 *
 * @author: 郭晨旭
 * @create: 2023-11-16 12:58
 * @version: 1.0
 */
public interface CustomerACDao {
    /**
     * 保存
     */
    CustomerAC save(CustomerAC customerAC);

    /**
     * 根据用户id查询
     */
    List<CustomerAC> selectAll(String userId);

    /**
     * 查询所有在范围时间内的数据
     */
    List<CustomerAC> selectInTime(Date startTime, Date endTime);
}
