package com.hotel.common.service.server;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hotel.common.dto.request.SaveFoodReq;
import com.hotel.common.dto.response.PageFoodResp;
import com.hotel.common.entity.Food;
import com.sun.org.apache.xpath.internal.operations.Bool;

import java.util.List;

/**
 * 食物表(Food)表服务接口
 *
 * @author: guochenxu
 * @create: 2024-03-24 14:01:11
 * @version: 1.0
 */
public interface FoodService extends IService<Food> {

    /**
     * 保存食物
     */
    Boolean saveFood(SaveFoodReq saveFoodReq);

    /**
     * 删除食物
     */
    Boolean deleteFood(List<String> id);

    /**
     * 分页查询
     */
    Page<PageFoodResp> pageFood(int page, int pageSize);
}

