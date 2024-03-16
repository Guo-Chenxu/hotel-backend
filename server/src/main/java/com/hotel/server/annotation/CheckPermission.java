package com.hotel.server.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 接口限流
 *
 * @author: 郭晨旭
 * @create: 2024-01-26 00:40
 * @version: 1.0
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface CheckPermission {
    /**
     * 指定方法一秒内只能访问限定次数, 默认3次
     */
    double value() default 3;
}
