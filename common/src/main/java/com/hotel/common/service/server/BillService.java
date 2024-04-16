package com.hotel.common.service.server;

import cn.hutool.cache.CacheUtil;
import com.hotel.common.dto.response.BillResp;
import com.hotel.common.dto.response.BillStatementResp;
import com.hotel.common.entity.CustomerAC;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Set;

/**
 * 财务服务
 *
 * @author: 郭晨旭
 * @create: 2024-04-01 17:25
 * @version: 1.0
 */

public interface BillService {

    String ping(String id);

    /**
     * 获取详单
     */
    BillStatementResp getBillStatement(String customerId, Set<String> types);

    /**
     * 获取账单
     */
    BillResp getBill(String customerId, Set<String> types);

    /**
     * 将客户的详单保存到数据库中
     * 退房时调用, 调用前必须保证放除房间外的所有资源
     */
    Boolean saveBillStatement(String customerId);

    /**
     * 保存空调账单
     */
    void saveACBill(CustomerAC customerAC);

    /**
     * 输出详单pdf
     */
    void outputBillStatementPDF(BillStatementResp billStatement, Set<String> types, OutputStream output) throws IOException;

    /**
     * 输出账单pdf
     */
    void outputBillPDF(BillResp bill, OutputStream output) throws IOException;
}
