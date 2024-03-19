package com.hotel.server.config;

import cn.dev33.satoken.exception.NotPermissionException;
import cn.dev33.satoken.stp.StpUtil;
import com.hotel.server.annotation.CheckPermission;
import lombok.extern.slf4j.Slf4j;
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

    @Resource
    private RedisTemplate redisTemplate;

//    @Resource
//   private UserService userService;

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
        String userPermission = "1,2,3,4,5,6"; // todo 先查缓存再查数据库
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
