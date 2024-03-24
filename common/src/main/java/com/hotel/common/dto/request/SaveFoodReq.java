package com.hotel.common.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 保存服务员请求
 *
 * @author: 郭晨旭
 * @create: 2024-03-23 23:45
 * @version: 1.0
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ApiModel("保存食物请求参数")
public class SaveFoodReq implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键id, 修改时传入")
    private String id;

    @ApiModelProperty("食物名")
    private String name;

    @ApiModelProperty("价格")
    private String price;

    @ApiModelProperty("图片")
    private String img;
}
