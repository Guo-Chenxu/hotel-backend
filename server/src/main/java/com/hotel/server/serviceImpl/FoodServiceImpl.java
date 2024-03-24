package com.hotel.server.serviceImpl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hotel.common.dto.request.SaveFoodReq;
import com.hotel.common.dto.response.PageFoodResp;
import com.hotel.common.dto.response.PageStaffResp;
import com.hotel.common.entity.Staff;
import com.hotel.common.service.server.StaffService;
import com.hotel.server.mapper.FoodMapper;
import com.hotel.common.entity.Food;
import com.hotel.common.service.server.FoodService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 食物表(Food)表服务实现类
 *
 * @author: guochenxu
 * @create: 2024-03-24 14:01:12
 * @version: 1.0
 */
@DubboService
@Slf4j
public class FoodServiceImpl extends ServiceImpl<FoodMapper, Food> implements FoodService {

    @Resource
    private FoodMapper foodMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean saveFood(SaveFoodReq saveFoodReq) {
        Food food = Food.builder().name(saveFoodReq.getName())
                .price(saveFoodReq.getPrice()).img(saveFoodReq.getImg()).build();
        if (saveFoodReq.getId() != null) {
            food.setId(Long.parseLong(saveFoodReq.getId()));
        }
        return this.saveOrUpdate(food);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteFood(List<String> id) {
        return this.removeByIds(id.stream().map(Long::parseLong).collect(Collectors.toList()));
    }

    @Override
    public Page<PageFoodResp> pageFood(int page, int pageSize) {
        Page<Food> foodPage = foodMapper.selectPage(new Page<>(page, pageSize), new LambdaQueryWrapper<>());
        Page<PageFoodResp> foodRespPage = new Page<>(page, pageSize);
        BeanUtils.copyProperties(foodPage, foodRespPage, "records");
        foodRespPage.setRecords(foodPage.getRecords().stream().map((e) ->
                PageFoodResp.builder().id(String.valueOf(e.getId()))
                        .name(e.getName()).price(e.getPrice()).img(e.getImg()).build()
        ).collect(Collectors.toList()));
        return foodRespPage;
    }
}

