package com.hotel.server.dao;


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
public interface CustomerFoodDao {
    /**
     * 保存
     */
    CustomerFood save(CustomerFood customerFood);

    /**
     * 根据用户id查询
     */
    List<CustomerFood> selectAll(String userId);

    /**
     * 选取所有时间范围内的订单
     */
    List<CustomerFood> selectInTime(Date startTime, Date endTime);
}
