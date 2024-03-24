package com.hotel.server.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import com.hotel.common.entity.Food;

/**
 * 食物表(Food)表数据库访问层
 *
 * @author: guochenxu
 * @create: 2024-03-24 14:01:11
 * @version: 1.0
 */
@Mapper
public interface FoodMapper extends BaseMapper<Food> {
}

