package com.hotel.common.dto.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 房间空调状态返回值
 *
 * @author: 郭晨旭
 * @create: 2024-03-30 15:27
 * @version: 1.0
 */

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("房间空调状态返回值")
public class ACStatusResp implements Serializable {
    private static final long serialVersionUID = 1482L;

    @ApiModelProperty("房间温度")
    private Double temperature;

    @ApiModelProperty("空调状态")
    private Integer status;

    @ApiModelProperty("空调价格")
    private String price;

    @ApiModelProperty("空调目标温度")
    private Double targetTemp;

    @ApiModelProperty("空调每分钟变化温度")
    private Double changeTemp;

    @ApiModelProperty("当前费用")
    private String currentPrice;

    @ApiModelProperty("累计费用")
    private String totalPrice;
}
