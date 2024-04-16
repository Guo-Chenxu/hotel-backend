package com.hotel.common.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 订房请求参数
 *
 * @author: 郭晨旭
 * @create: 2024-03-20 11:42
 * @version: 1.0
 */

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@ApiModel("订房请求参数")
public class BookRoomReq implements Serializable {
    private static final long serialVersionUID = 25340831130L;

    @ApiModelProperty("顾客姓名")
    @NotNull
    private String customerName;

    @ApiModelProperty("入住时间")
    @NotNull
    private Date startTime;

    @ApiModelProperty("退房时间")
    @NotNull
    private Date leaveTime;

    @ApiModelProperty("房间价格")
    @NotNull
    private String price;

    @ApiModelProperty("押金")
    private String deposit;
}
