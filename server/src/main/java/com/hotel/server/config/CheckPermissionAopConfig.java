package com.hotel.server.config;

import cn.dev33.satoken.exception.NotPermissionException;
import cn.dev33.satoken.stp.StpUtil;
import com.hotel.common.constants.RedisKeys;
import com.hotel.common.entity.Staff;
import com.hotel.common.service.server.CacheService;
import com.hotel.common.service.server.StaffService;
import com.hotel.server.annotation.CheckPermission;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.reflect.Method;

/**
 * 接口限流aop实现类
 *
 * @author: 郭晨旭
 * @create: 2024-01-26 00:44
 * @version: 1.0
 */

@Slf4j
@Component
@Aspect
@Order(2)
public class CheckPermissionAopConfig {

    @DubboReference
    private CacheService cacheService;

    @DubboReference
    private StaffService staffService;

    @Before("@annotation(com.hotel.server.annotation.CheckPermission)")
    public void checkBefore(JoinPoint jp) throws Throwable {
        Class<?> targetCls = jp.getTarget().getClass();
        MethodSignature ms = (MethodSignature) jp.getSignature();
        Method method = targetCls.getDeclaredMethod(ms.getName(), ms.getParameterTypes());
        CheckPermission checkPermission = method.getAnnotation(CheckPermission.class);
        String[] needPermissions = checkPermission.value();
        if (needPermissions == null || needPermissions.length == 0) {
            return;
        }

        long id = StpUtil.getLoginIdAsLong();
        Staff staff = (Staff) cacheService.get(String.format(RedisKeys.STAFF_ID_INFO, id), Staff.class);
        if (staff == null) {
            staff = staffService.getById(id);
        }
        log.info("staff: {}", staff);
        String userPermission = staff.getPermission();
        String[] hasPermissions = userPermission.split(",");

        for (String need : needPermissions) {
            for (String has : hasPermissions) {
                if (need.equals(has)) {
                    log.info("user: {}, 权限 {} 校验通过", id, need);
                    return;
                }
            }
        }

        log.error("user: {}, 权限校验失败: {}", id, needPermissions);
        throw new NotPermissionException("权限校验失败, 用户没有权限访问");
    }
}
