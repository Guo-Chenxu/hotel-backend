package com.hotel.common.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hotel.common.entity.CustomerAC;
import com.hotel.common.entity.CustomerFood;
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

/**
 * 账单返回值
 *
 * @author: 郭晨旭
 * @create: 2024-04-01 17:16
 * @version: 1.0
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("获取账单返回值")
public class BillResp implements Serializable {

    private static final long serialVersionUID = 8013249L;

    @ApiModelProperty("顾客id")
    private String customerId;

    @ApiModelProperty("房间id")
    private String roomId;

    @ApiModelProperty("入住时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date checkInTime;

    @ApiModelProperty("退房时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date checkOutTime;

    @ApiModelProperty("截至目前的总房费")
    private String roomTotPrice;

    @ApiModelProperty("押金")
    private String deposit;

    @ApiModelProperty("餐饮消费")
    private String foodBill;

    @ApiModelProperty("空调消费")
    private String acBill;

    @ApiModelProperty("扣除押金后总价")
    private String totalPrice;
}
