package com.hotel.common.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 顾客食物表
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
public class CustomerFood implements Serializable {
    private static final long serialVersionUID = 465465L;

    @Id
    private String id;

    /**
     * 顾客id
     */
    private String customerId;

    /**
     * 点餐列表
     */
    private Map<String, Integer> foods;

    /**
     * 总价
     */
    private String totalPrice;

    /**
     * 备注
     */
    private String remarks;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createAt;
}
