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

/**
 * 查看房间详情返回值
 *
 * @author: 郭晨旭
 * @create: 2024-03-21 10:06
 * @version: 1.0
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@ApiModel("房间详情返回值")
public class RoomInfoResp implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("顾客姓名")
    private String customerName;

    @ApiModelProperty("顾客id")
    private String customerId;

    @ApiModelProperty("入住时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    @ApiModelProperty("退房时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date leaveTime;

    @ApiModelProperty("房间号")
    private String roomId;

    @ApiModelProperty("房间价格")
    private String price;

    @ApiModelProperty("房间温度")
    private String temperature;
}
