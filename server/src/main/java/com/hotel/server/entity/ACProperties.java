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
 * 空调参数
 *
 * @author: 郭晨旭
 * @create: 2024-03-28 11:49
 * @version: 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ApiModel("空调参数")
public class ACProperties implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("上限温度")
    @NotNull
    private Double upperBoundTemperature;

    @ApiModelProperty("下限温度")
    @NotNull
    private Double lowerBoundTemperature;

    @ApiModelProperty("默认空调目标温度")
    @NotNull
    private Double defaultTargetTemp;

    @ApiModelProperty("高档价格参数")
    @NotNull
    private ACPrice high;

    @ApiModelProperty("中档价格参数")
    @NotNull
    private ACPrice middle;

    @ApiModelProperty("低档价格参数")
    @NotNull
    private ACPrice low;
}