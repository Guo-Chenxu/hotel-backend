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
 * 分页查询员工返回值
 *
 * @author: 郭晨旭
 * @create: 2024-03-23 23:52
 * @version: 1.0
 */

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@ApiModel("分页查询食物信息返回值")
public class PageFoodResp implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("食物主键id, 修改时传入")
    private String id;

    @ApiModelProperty("食物名")
    private String name;

    @ApiModelProperty("食物价格")
    private String price;

    @ApiModelProperty("食物图片")
    private String img;
}
