package com.hotel.server.script;

import cn.hutool.crypto.digest.DigestUtil;
import com.hotel.common.constants.Permission;
import com.hotel.common.entity.Room;
import com.hotel.common.entity.Staff;
import com.hotel.common.service.server.RoomService;
import com.hotel.common.service.server.StaffService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * 数据库初始数据脚本
 *
 * @author: 郭晨旭
 * @create: 2024-03-20 11:34
 * @version: 1.0
 */
@SpringBootTest
@Slf4j
public class DatabaseScriptTest {

    @DubboReference
    private RoomService roomService;

    @DubboReference
    private StaffService staffService;

    @Test
    public void insertAdmin() {
        Staff staff = Staff.builder().username("admin").password(DigestUtil.md5Hex("admin"))
                .permission(Permission.ADMIN).build();
        staffService.save(staff);
    }
}
