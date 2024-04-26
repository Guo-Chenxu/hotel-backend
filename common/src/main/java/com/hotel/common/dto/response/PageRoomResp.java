package com.hotel.common.dto.response;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 房间分页查询返回值
 *
 * @author: 郭晨旭
 * @create: 2024-04-26 20:31
 * @version: 1.0
 */

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@ApiModel("房间分页查询返回值")
public class PageRoomResp implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("房间号")
    private String roomNo;

    @ApiModelProperty("房间顾客名")
    private String username;

    @ApiModelProperty("价格 元/晚")
    private String price;

    @ApiModelProperty("房间当前温度")
    private Double temperature;

    @ApiModelProperty("押金")
    private String deposit;
}
