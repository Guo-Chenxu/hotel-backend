package com.hotel.common.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 分页查询房间
 *
 * @author: 郭晨旭
 * @create: 2024-03-20 11:02
 * @version: 1.0
 */

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@ApiModel("分页查询房间请求参数")
public class PageRoomReq implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("页数")
    private Integer page;

    @ApiModelProperty("每页大小")
    private Integer pageSize;

    @ApiModelProperty("房间号")
    private Integer roomNo;
}
