package com.hotel.server;

import cn.hutool.crypto.digest.DigestUtil;
import com.hotel.common.constants.Permission;
import com.hotel.common.dto.request.SaveStaffReq;
import com.hotel.common.entity.Staff;
import com.hotel.common.service.server.StaffService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Collections;

/**
 * 服务人员服务测试类
 *
 * @author: 郭晨旭
 * @create: 2024-03-20 10:37
 * @version: 1.0
 */
@SpringBootTest
@Slf4j
public class StaffServiceTest {

    @Resource
    private StaffService staffService;

    @Test
    public void testSave() {
        SaveStaffReq build = SaveStaffReq.builder().id("1771569627727994882")
                .username("test123").password("test").permission(Collections.singletonList(Permission.CLEANER)).build();
        staffService.save(build);
    }
}
