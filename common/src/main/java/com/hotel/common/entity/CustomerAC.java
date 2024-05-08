package com.hotel.common.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.Date;

/**
 * 顾客空调关系类
 *
 * @author: 郭晨旭
 * @create: 2024-03-30 16:08
 * @version: 1.0
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document
public class CustomerAC implements Serializable {
    private static final long serialVersionUID = 78134902L;

    @Id
    private String id;

    /**
     * 顾客id
     */
    private String customerId;

    /**
     * 单价
     */
    private String price;

    /**
     * 档位
     */
    private Integer status;

    /**
     * 改变温度
     */
    private Double changeTemperature;

    /**
     * 此次服务时长 分钟
     */
    private Integer duration;

    /**
     * 此次服务总价
     */
    private String totalPrice;

    /**
     * 请求时间
     */
    private Date requestTime;

    /**
     * 服务开始时间
     */
    private Date createAt;

    /**
     * 服务结束时间
     */
    private Date endTime;
}