package com.hotel.common.dto.response;

import com.hotel.common.entity.CustomerAC;
import com.hotel.common.entity.CustomerFood;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 详单
 *
 * @author: 郭晨旭
 * @create: 2024-03-24 21:45
 * @version: 1.0
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("获取详单返回值")
public class BillStatementResp implements Serializable {
    private static final long serialVersionUID = 4655L;

    private String id;

    @ApiModelProperty("顾客id")
    private String customerId;

    @ApiModelProperty("房间id")
    private String roomId;

    @ApiModelProperty("入住时间")
    private Date checkInTime;

    @ApiModelProperty("退房时间")
    private Date checkOutTime;

    @ApiModelProperty("房间每晚价格")
    private String roomPrice;

    @ApiModelProperty("押金")
    private String deposit;

    @ApiModelProperty("餐饮详单")
    private List<CustomerFood> foodBillList;

    @ApiModelProperty("空调详单")
    private List<CustomerAC> acBillList;

    @ApiModelProperty("扣除押金后总价")
    private String totalPrice;
}