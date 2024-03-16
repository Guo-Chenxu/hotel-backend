package com.hotel.server.config;

import cn.hutool.crypto.SecureUtil;
import cn.hutool.json.JSONUtil;
import com.hotel.common.exception.VisitLimitException;
import com.hotel.server.annotation.CheckPermission;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.CodeSignature;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

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

    @Value("${sa-token.token-name}")
    private String tokenName;

    @Before("@annotation(com.hotel.server.annotation.CheckPermission)")
    public void checkBefore(JoinPoint jp) throws Throwable {
        Class<?> targetCls = jp.getTarget().getClass();
        MethodSignature ms = (MethodSignature) jp.getSignature();
        Method method = targetCls.getDeclaredMethod(ms.getName(), ms.getParameterTypes());
        CheckPermission checkPermission = method.getAnnotation(CheckPermission.class);
        long expireSeconds = (long) (1000.0 / checkPermission.value());

        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        String token = request.getHeader(tokenName);

        String methodName = ms.getName();
        Map<String, Object> paramMap = getNameAndValue(jp);
        String paramMd5Sign = SecureUtil.md5(JSONUtil.toJsonStr(paramMap));

        String limitKey = String.format("visit_limit_%s_%s_%s", token, methodName, paramMd5Sign);
        boolean getLock = redisTemplate.opsForValue().setIfAbsent(limitKey, "1", expireSeconds, TimeUnit.MILLISECONDS);
        if (!getLock) {
            log.error("visit limit: token: {}, methodName:{}", token, methodName);
            throw new VisitLimitException("访问过于频繁，请稍后再试");
        }
    }

    Map<String, Object> getNameAndValue(JoinPoint joinPoint) {
        Map<String, Object> param = new HashMap<>();
        Object[] paramValues = joinPoint.getArgs();
        String[] paramNames = ((CodeSignature) joinPoint.getSignature()).getParameterNames();
        for (int i = 0; i < paramNames.length; ++i) {
            param.put(paramNames[i], paramValues[i]);
        }
        return param;
    }
}
