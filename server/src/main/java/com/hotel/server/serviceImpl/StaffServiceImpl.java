package com.hotel.server.serviceImpl;

import cn.dev33.satoken.stp.StpUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hotel.common.constants.RedisKeys;
import com.hotel.common.dto.request.SaveStaffReq;
import com.hotel.common.dto.response.PageStaffResp;
import com.hotel.common.dto.response.StaffLoginResp;
import com.hotel.common.service.server.CacheService;
import com.hotel.common.service.server.StaffService;
import com.hotel.server.mapper.StaffMapper;
import com.hotel.common.entity.Staff;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

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

    @DubboReference
    private CacheService cacheService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public StaffLoginResp login(String username, String password) {
        Staff staff = staffMapper.selectOne(new LambdaQueryWrapper<Staff>().eq(Staff::getUsername, username));
        if (!staff.checkPassword(password)) {
            throw new RuntimeException("密码错误");
        }

        cacheService.addWithExpireTime(String.format(RedisKeys.STAFF_ID_INFO, staff.getId()),
                JSON.toJSONString(staff), 7, TimeUnit.DAYS);
        StpUtil.login(staff.getId());
        String token = StpUtil.getTokenValue();
        return StaffLoginResp.builder().staffId(String.valueOf(staff.getId()))
                .token(token).permission(staff.getPermission()).build();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean save(SaveStaffReq saveStaffReq) {
        Staff staff = Staff.builder().username(saveStaffReq.getUsername()).password(saveStaffReq.getPassword()).build();
        staff.encryptPassword();
        if (saveStaffReq.getId() != null) {
            staff.setId(Long.parseLong(saveStaffReq.getId()));
        }
        StringBuilder sb = new StringBuilder();
        saveStaffReq.getPermission().forEach(sb::append);
        staff.setPermission(sb.toString());

        return this.saveOrUpdate(staff);
    }

    @Override
    public Page<PageStaffResp> pageStaff(Integer page, Integer pageSize) {
        Page<Staff> staffPage = staffMapper.selectPage(new Page<>(page, pageSize), new LambdaQueryWrapper<>());
        Page<PageStaffResp> pageStaffRespPage = new Page<>(page, pageSize);
        BeanUtils.copyProperties(staffPage, pageStaffRespPage, "records");
        pageStaffRespPage.setRecords(staffPage.getRecords().stream().map((e) ->
                        PageStaffResp.builder().id(String.valueOf(e.getId()))
                                .username(e.getUsername())
                                .permission(e.getPermission())
                                .createAt(e.getCreateAt())
                                .build())
                .collect(Collectors.toList()));
        return pageStaffRespPage;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean delete(List<String> id) {
        return this.removeByIds(id.stream().map(Long::parseLong).collect(Collectors.toList()));
    }
}

