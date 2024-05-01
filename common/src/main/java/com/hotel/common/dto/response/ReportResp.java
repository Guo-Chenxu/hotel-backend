package com.hotel.common.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 报表返回值
 *
 * @author: 郭晨旭
 * @create: 2024-04-26 09:15
 * @version: 1.0
 */
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("报表返回值")
public class ReportResp implements Serializable {
    private static final long serialVersionUID = 2341L;

    @ApiModelProperty("起始时间, 默认一周前")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    @ApiModelProperty("终止时间, 默认前一天")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    @ApiModelProperty("报表数据类型, 1/2/3只能选一个")
    private String type;

    @ApiModelProperty("销量, 计量单位为u, 键值对为k-v, 表示价格[k, k+u)区间的销量为v, 房间/餐饮/空调的u分别为100/10/1")
    private Map<Integer, Integer> count;
}
