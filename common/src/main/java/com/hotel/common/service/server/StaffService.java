package com.hotel.common.service.server;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hotel.common.dto.response.StaffLoginResp;
import com.hotel.common.entity.Staff;

/**
 * 酒店服务人员表(Staff)表服务接口
 *
 * @author: guochenxu
 * @create: 2024-03-19 22:09:21
 * @version: 1.0
 */
public interface StaffService extends IService<Staff> {

    /**
     * 登录
     */
    StaffLoginResp login(String username, String password);

}

