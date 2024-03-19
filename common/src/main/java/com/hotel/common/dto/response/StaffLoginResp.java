package com.hotel.common.dto.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 员工登录返回类
 *
 * @author: 郭晨旭
 * @create: 2024-03-19 22:25
 * @version: 1.0
 */

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@ApiModel("员工登录返回类")
public class StaffLoginResp implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("员工id")
    private String staffId;

    @ApiModelProperty("token, 后续放在请求头里鉴权")
    private String token;

    @ApiModelProperty("权限, 以逗号分割")
    private String permission;
}
