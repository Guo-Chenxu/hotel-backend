package com.hotel.common.dto.request;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
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
@ApiModel("保存服务员请求参数")
public class SaveStaffReq implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("员工主键id, 修改时传入")
    private String id;

    @ApiModelProperty("员工姓名")
    @NotNull
    private String username;

    @ApiModelProperty("员工密码")
    @NotNull
    private String password;

    @ApiModelProperty("员工权限")
    @NotNull
    private List<String> permission;
}
