package com.hotel.server.serviceImpl;

import com.hotel.common.constants.Permission;
import com.hotel.common.dto.response.ListPermissionResp;
import com.hotel.common.service.server.PermissionService;
import org.apache.dubbo.config.annotation.DubboService;

import java.util.Arrays;
import java.util.List;

/**
 * 权限服务实现类
 *
 * @author: 郭晨旭
 * @create: 2024-03-19 22:18
 * @version: 1.0
 */
@DubboService
public class PermissionServiceImpl implements PermissionService {
    @Override
    public List<String> list() {
        return Arrays.asList(
                Permission.ADMIN,
                Permission.RECEPTIONIST,
                Permission.FOOD,
                Permission.CLEANER,
                Permission.COOL,
                Permission.FINANCIAL
        );
    }
}
