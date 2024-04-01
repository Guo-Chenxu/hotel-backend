package com.hotel.common.dto.response;

import com.hotel.common.entity.CustomerAC;
import com.hotel.common.entity.CustomerFood;
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
    private Date checkInTime;

    @ApiModelProperty("截至目前的房费")
    private String roomPrice;

    @ApiModelProperty("押金")
    private String deposit;

    @ApiModelProperty("餐饮消费")
    private String foodBill;

    @ApiModelProperty("空调消费")
    private String acBill;

    @ApiModelProperty("扣除押金后总价")
    private String totalPrice;
}
