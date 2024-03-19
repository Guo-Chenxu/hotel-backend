package com.hotel.common.service.server;

import com.hotel.common.dto.response.ListPermissionResp;

import java.util.List;

/**
 * 权限服务类
 *
 * @author: 郭晨旭
 * @create: 2024-03-19 22:08
 * @version: 1.0
 */
public interface PermissionService {
    List<ListPermissionResp> list();
}
