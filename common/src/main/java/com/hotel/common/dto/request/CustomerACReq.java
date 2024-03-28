package com.hotel.common.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 客户端打开空调
 *
 * @author: 郭晨旭
 * @create: 2024-03-28 17:48
 * @version: 1.0
 */
@ApiModel("客户端打开空调请求参数")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomerACReq implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("目标温度")
    private Double targetTemperature;

    @ApiModelProperty("空调档位, 1:低, 2:中, 3:高")
    private Integer status;
}
