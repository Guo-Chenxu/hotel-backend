package com.hotel.common.dto.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 顾客登录返回类
 *
 * @author: 郭晨旭
 * @create: 2024-03-19 22:25
 * @version: 1.0
 */

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@ApiModel("顾客登录返回类")
public class CustomerLoginResp implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("顾客id")
    private String customerId;

    @ApiModelProperty("顾客姓名")
    private String name;

    @ApiModelProperty("顾客离店时间")
    private Date leaveTime;

    @ApiModelProperty("token, 后续放在请求头里鉴权")
    private String token;

    @ApiModelProperty("房间号")
    private String room;

    @ApiModelProperty("房间温度")
    private Double temperature;
}
