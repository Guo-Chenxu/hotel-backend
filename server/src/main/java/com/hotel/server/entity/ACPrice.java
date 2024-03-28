package com.hotel.server.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 空调价格参数
 *
 * @author: 郭晨旭
 * @create: 2024-03-28 11:55
 * @version: 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ApiModel("空调价格参数")
public class ACPrice implements Serializable {
    private static final long serialVersionUID = 8019324L;

    @ApiModelProperty("每分钟变化温度")
    @NotNull
    private Double changeTemperature;

    @ApiModelProperty("价格 元/分钟")
    @NotNull
    private String price;
}
