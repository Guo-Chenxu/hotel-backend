package com.hotel.server.exception.handler;

import cn.dev33.satoken.exception.*;
import com.hotel.common.constants.HttpCode;
import com.hotel.common.dto.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * sa-token异常处理
 */
@RestControllerAdvice
@Slf4j
public class SaTokenException {

    // 拦截：未登录异常
    @ExceptionHandler(NotLoginException.class)
    public R handlerException(NotLoginException e) {
        log.error("未登录异常: {}", e.getMessage());
        return R.error(HttpCode.FORBIDDEN, e.getMessage());
    }

    // 拦截：缺少权限异常
    @ExceptionHandler(NotPermissionException.class)
    public R handlerException(NotPermissionException e) {
        log.error("缺少权限异常: {}", e.getMessage());
        return R.error(HttpCode.FORBIDDEN, "缺少权限：" + e.getPermission());
    }

    // 拦截：缺少角色异常
    @ExceptionHandler(NotRoleException.class)
    public R handlerException(NotRoleException e) {
        log.error("缺少角色异常: {}", e.getMessage());
        return R.error(HttpCode.FORBIDDEN, "缺少角色：" + e.getRole());
    }

    // 拦截：二级认证校验失败异常
    @ExceptionHandler(NotSafeException.class)
    public R handlerException(NotSafeException e) {
        log.error("二级认证校验失败异常: {}", e.getMessage());
        return R.error(HttpCode.FORBIDDEN, "二级认证校验失败：" + e.getService());
    }

    // 拦截：服务封禁异常
    @ExceptionHandler(DisableServiceException.class)
    public R handlerException(DisableServiceException e) {
        log.error("服务封禁异常: {}", e.getMessage());
        return R.error(HttpCode.FORBIDDEN, "当前账号 " + e.getService() + " 服务已被封禁 (level=" + e.getLevel() + ")：" + e.getDisableTime() + "秒后解封");
    }

    // 拦截：Http Basic 校验失败异常
    @ExceptionHandler(NotBasicAuthException.class)
    public R handlerException(NotBasicAuthException e) {
        log.error("http basic 校验失败异常: {}", e.getMessage());
        return R.error(HttpCode.FORBIDDEN, e.getMessage());
    }
}