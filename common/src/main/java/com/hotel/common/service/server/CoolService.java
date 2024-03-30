package com.hotel.common.service.server;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hotel.common.dto.response.ACStatusResp;
import com.hotel.common.dto.response.PageRoomACResp;
import com.hotel.common.entity.ACProperties;

import javax.servlet.http.HttpServletResponse;

/**
 * 温度服务
 *
 * @author: 郭晨旭
 * @create: 2024-03-27 11:10
 * @version: 1.0
 */

public interface CoolService {
    /**
     * 增加一个房间的空调线程
     */
    void watchAC(String userId);

    /**
     * 分页查询房间空调状态
     */
    Page<PageRoomACResp> pageRoomAC(Integer page, Integer pageSize);

    /**
     * 查看顾客房间空调状态
     */
    ACStatusResp getACStatus(String userId);

    /**
     * 获取空调参数
     */
    ACProperties getACProperties();
}
