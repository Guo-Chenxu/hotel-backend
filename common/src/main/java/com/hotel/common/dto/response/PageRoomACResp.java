package com.hotel.common.dto.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 分页查看各房间空调状态
 *
 * @author: 郭晨旭
 * @create: 2024-03-30 15:24
 * @version: 1.0
 */

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@ApiModel("分页查询各房间空调状态")
public class PageRoomACResp implements Serializable {
    private static final long serialVersionUID = 1028497L;

    @ApiModelProperty("房间id")
    private String roomId;

    @ApiModelProperty("顾客id")
    private String customerId;

    @ApiModelProperty("空调状态")
    private ACStatusResp acStatus;
}
