package com.hotel.server.serviceImpl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hotel.common.dto.request.OrderFoodReq;
import com.hotel.common.dto.request.SaveFoodReq;
import com.hotel.common.dto.response.PageFoodResp;
import com.hotel.common.dto.response.PageStaffResp;
import com.hotel.common.entity.CustomerFood;
import com.hotel.common.entity.Staff;
import com.hotel.common.service.server.StaffService;
import com.hotel.server.dao.CustomerFoodDao;
import com.hotel.server.mapper.FoodMapper;
import com.hotel.common.entity.Food;
import com.hotel.common.service.server.FoodService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.BeanUtils;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
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

    @Resource
    private CustomerFoodDao customerFoodDao;

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

    @Override
    public List<CustomerFood> history(String userId) {
        return customerFoodDao.selectAll(userId);
    }

    @Override
    public Boolean saveOrder(String userId, OrderFoodReq orderFoodReq) {
        CustomerFood customerFood = CustomerFood.builder().customerId(userId)
                .remarks(orderFoodReq.getRemarks()).build();
        List<String> foodIds = new ArrayList<>(orderFoodReq.getOrder().keySet());
        if (CollectionUtils.isEmpty(foodIds)) {
            throw new RuntimeException("订单为空");
        }

        // 计算总价
        List<Food> foods = foodMapper.selectBatchIds(foodIds);
        Map<Food, Integer> map = new HashMap<>(foods.size());
        BigDecimal totalPrice = BigDecimal.ZERO;
        for (Food f : foods) {
            int count = orderFoodReq.getOrder().get(String.valueOf(f.getId()));
            totalPrice = totalPrice.add(new BigDecimal(f.getPrice()).multiply(new BigDecimal(count)));
            map.put(f, count);
        }

        customerFood.setTotalPrice(String.valueOf(totalPrice));
        customerFood.setFoods(map);
        customerFood = customerFoodDao.save(customerFood);
        // todo 写入账单
        return customerFood != null && customerFood.getId() != null;
    }
}

