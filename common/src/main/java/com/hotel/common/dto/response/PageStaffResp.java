package com.hotel.common.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

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
@ApiModel("分页查询员工信息返回值")
public class PageStaffResp implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("员工主键id, 修改时传入")
    private String id;

    @ApiModelProperty("员工姓名")
    private String username;

    @ApiModelProperty("员工权限")
    private String permission;

    @ApiModelProperty("创建日期")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createAt;
}
