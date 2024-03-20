package com.hotel.common.constants;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

/**
 * 权限列表
 *
 * @author: 郭晨旭
 * @create: 2024-03-16 17:48
 * @version: 1.0
 */
public interface Permission {
    String ADMIN = "管理员";
    String RECEPTIONIST = "前台";
    String CLEANER = "保洁";
    String FOOD = "餐饮";
    String COOL ="纳凉";
    String FINANCIAL = "财务";
}
