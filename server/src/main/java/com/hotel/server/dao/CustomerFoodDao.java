package com.hotel.server.dao;



import com.hotel.common.entity.CustomerFood;

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
     * 保存一次会话
     */
    CustomerFood save(CustomerFood customerFood);

    /**
     * 根据用户id查询会话
     */
    List<CustomerFood> selectAll(String userId);
}
