package com.hotel.common.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 报表请求值
 *
 * @author: 郭晨旭
 * @create: 2024-04-26 09:15
 * @version: 1.0
 */
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("报表请求值")
public class ReportReq implements Serializable {
    private static final long serialVersionUID = 1342L;

    @ApiModelProperty("起始时间, 默认一周前")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    @ApiModelProperty("终止时间, 默认前一天")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    @ApiModelProperty("报表数据类型, 1/2/3只能选一个")
    private String type;
}
