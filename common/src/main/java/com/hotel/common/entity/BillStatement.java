package com.hotel.common.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 详单
 *
 * @author: 郭晨旭
 * @create: 2024-03-24 21:45
 * @version: 1.0
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document
public class BillStatement implements Serializable {
    private static final long serialVersionUID = 465465L;

    @Id
    private String id;

    /**
     * 顾客id
     */
    private String customerId;

    /**
     * 入住时间
     */
    private Date checkInTime;

    /**
     * 退房时间
     */
    private Date checkoutTime;

    /**
     * 房费
     */
    private String roomPrice;

    /**
     * 餐饮详单
     */
    private List<CustomerFood> foodBillList;

    /**
     * 空调详单
     */
    private List<CustomerAC> acBillList;
}