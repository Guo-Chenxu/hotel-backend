package com.hotel.common.constants;

/**
 * 订单返回结果
 *
 * @author: 郭晨旭
 * @create: 2024-03-24 21:33
 * @version: 1.0
 */
public interface OrderResult {
    String TIMEOUT = "订单超时，请重新下单";
    String SUCCESS = "订单已被接收";
    String FAILED = "订单被拒绝";
    String ERROR = "下单失败，请重新尝试";
}
