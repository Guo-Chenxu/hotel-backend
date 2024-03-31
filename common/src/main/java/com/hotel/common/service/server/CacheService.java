package com.hotel.common.service.server;

import java.util.concurrent.TimeUnit;

/**
 * 缓存服务
 *
 * @author: 郭晨旭
 * @create: 2023-11-09 13:46
 * @version: 1.0
 */
public interface CacheService {

    /**
     * 添加缓存
     */
    void add(String key, String val);

    /**
     * 设置带有过期时间的缓存
     * 如果存在则是刷新过期时间
     */
    void addWithExpireTime(String key, String val, long expireTime, TimeUnit unit);

    /**
     * 获取缓存
     */
    String get(String key);

    /**
     * 读取缓存
     */
    Object get(String key, Class<?> clazz);

    /**
     * 查看缓存值是否存在
     */
    boolean exist(String key);

    /**
     * 删除缓存
     */
    void delete(String key);

    /**
     * 缓存续期
     */
    void renew(String key, long expireTime, TimeUnit unit);
}
