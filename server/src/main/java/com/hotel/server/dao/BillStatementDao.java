package com.hotel.server.dao;



import com.hotel.common.entity.BillStatement;
import com.hotel.common.entity.CustomerAC;

import java.util.List;

/**
 * 聊天会话数据库接口
 *
 * @author: 郭晨旭
 * @create: 2023-11-16 12:58
 * @version: 1.0
 */
public interface BillStatementDao {
    /**
     * 保存
     */
    BillStatement save(BillStatement billStatement);

    /**
     * 根据用户id查询
     */
    List<BillStatement> selectAll(String userId);
}
