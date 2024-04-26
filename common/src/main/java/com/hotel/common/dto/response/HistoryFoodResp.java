package com.hotel.common.dto.response;

import com.hotel.common.entity.Food;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.Date;
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
public class HistoryFoodResp implements Serializable {
    private static final long serialVersionUID = 465465L;

    private String id;

    /**
     * 顾客id
     */
    private String customerId;

    /**
     * 点餐列表
     */
    private Map<Food, Integer> foods;

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
    private Date createAt;
}
