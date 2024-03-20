package com.hotel.common.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

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
    private String customerName;
}
