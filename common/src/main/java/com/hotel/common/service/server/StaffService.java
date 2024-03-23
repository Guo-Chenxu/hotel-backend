package com.hotel.common.service.server;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hotel.common.dto.request.SaveStaffReq;
import com.hotel.common.dto.response.PageStaffResp;
import com.hotel.common.dto.response.StaffLoginResp;
import com.hotel.common.entity.Staff;
import com.sun.org.apache.xpath.internal.operations.Bool;

import java.util.List;

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

    /**
     * 保存员工
     */
    Boolean save(SaveStaffReq saveStaffReq);

    /**
     * 分页查询
     */
    Page<PageStaffResp> pageStaff(Integer page, Integer pageSize);

    /**
     * 删除员工
     */
    Boolean delete(List<String> id);
}

