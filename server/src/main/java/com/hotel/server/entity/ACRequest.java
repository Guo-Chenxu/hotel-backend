package com.hotel.server.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 空调请求类
 *
 * @author: 郭晨旭
 * @create: 2024-03-28 11:41
 * @version: 1.0
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ACRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    private String userId;
    private Date startTime; // 申请空调启动时间
    private Double targetTemperature; // 目标温度
    private Double changeTemperature; // 每分钟变化温度

    @Override
    public int hashCode() {
        return userId.hashCode();
    }
}