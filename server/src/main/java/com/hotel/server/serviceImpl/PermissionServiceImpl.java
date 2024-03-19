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
    public List<ListPermissionResp> list() {
        // 这里直接硬编码
        return Arrays.asList(
                ListPermissionResp.builder().name("管理员").code(Permission.ADMIN).build(),
                ListPermissionResp.builder().name("前台").code(Permission.RECEPTION).build(),
                ListPermissionResp.builder().name("餐饮").code(Permission.FOOD).build(),
                ListPermissionResp.builder().name("保洁").code(Permission.CLEAN).build(),
                ListPermissionResp.builder().name("纳凉").code(Permission.COOL).build(),
                ListPermissionResp.builder().name("财务").code(Permission.FINICAL).build()
        );
    }
}
