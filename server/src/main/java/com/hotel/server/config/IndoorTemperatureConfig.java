package com.hotel.server.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 室内温度相关参数配置
 *
 * @author: 郭晨旭
 * @create: 2024-03-28 11:22
 * @version: 1.0
 */
@Data
@ConfigurationProperties(prefix = "hotel.indoor")
public class IndoorTemperatureConfig {
    /**
     * 回归室温的变化温度 (度/分)
     */
    private Double recoverChangeTemperature;
    /**
     * 室内温度
     */
    private Double indoorTemperature;
    /**
     * 时间片, 分钟
     */
    private Long timeSlice;
}
