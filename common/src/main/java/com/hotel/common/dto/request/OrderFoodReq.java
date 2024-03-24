package com.hotel.common.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Map;

/**
 * 订餐请求
 *
 * @author: 郭晨旭
 * @create: 2024-03-24 22:14
 * @version: 1.0
 */

@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("订餐请求")
@Data
public class OrderFoodReq implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("点餐明细，key为菜品id，value为数量")
    private Map<String, Integer> order;

    @ApiModelProperty("备注")
    private String remarks;
}
