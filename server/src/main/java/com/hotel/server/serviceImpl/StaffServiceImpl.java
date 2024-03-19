package com.hotel.server.serviceImpl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hotel.common.dto.response.StaffLoginResp;
import com.hotel.common.service.server.StaffService;
import com.hotel.server.mapper.StaffMapper;
import com.hotel.common.entity.Staff;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;

/**
 * 酒店服务人员表(Staff)表服务实现类
 *
 * @author: guochenxu
 * @create: 2024-03-19 22:09:21
 * @version: 1.0
 */
@DubboService
@Slf4j
public class StaffServiceImpl extends ServiceImpl<StaffMapper, Staff> implements StaffService {

    @Resource
    private StaffMapper staffMapper;

    @Override
    public StaffLoginResp login(String username, String password) {
        Staff staff = staffMapper.selectOne(new LambdaQueryWrapper<Staff>().eq(Staff::getUsername, username));
        StpUtil.login(staff.getId());
        String token = StpUtil.getTokenValue();
        return StaffLoginResp.builder().staffId(String.valueOf(staff.getId()))
                .token(token).permission(staff.getPermission()).build();
    }
}

