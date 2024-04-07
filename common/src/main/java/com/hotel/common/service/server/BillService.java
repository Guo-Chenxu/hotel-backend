package com.hotel.common.service.server;

import cn.hutool.cache.CacheUtil;
import com.hotel.common.dto.response.BillResp;
import com.hotel.common.dto.response.BillStatementResp;
import com.hotel.common.entity.CustomerAC;

/**
 * 财务服务
 *
 * @author: 郭晨旭
 * @create: 2024-04-01 17:25
 * @version: 1.0
 */

public interface BillService {

    /**
     * 获取详单
     */
    BillStatementResp getBillStatement(String customerId);

    /**
     * 获取账单
     */
    BillResp getBill(String customerId);

    /**
     * 将客户的详单保存到数据库中
     * 退房时调用, 调用前必须保证放除房间外的所有资源
     */
    Boolean saveBillStatement(String customerId);

    /**
     * 保存空调账单
     */
    void saveACBill(CustomerAC customerAC);
}
