package com.hotel.common.dto.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 列出权限返回类
 *
 * @author: 郭晨旭
 * @create: 2024-03-19 22:06
 * @version: 1.0
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@ApiModel("列出权限返回类")
public class ListPermissionResp implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("权限名称")
    private String name;

    @ApiModelProperty("权限标识符")
    private String code;
}
