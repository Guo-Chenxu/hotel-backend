package com.hotel.server.annotation;

import cn.dev33.satoken.annotation.SaCheckLogin;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.List;

/**
 * 接口权限校验
 *
 * @author: 郭晨旭
 * @create: 2024-01-26 00:40
 * @version: 1.0
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@SaCheckLogin
public @interface CheckPermission {
    /**
     * 访问接口需要的权限
     */
    String[] value();
}
