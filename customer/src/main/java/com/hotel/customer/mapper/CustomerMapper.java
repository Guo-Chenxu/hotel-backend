package com.hotel.customer.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hotel.common.entity.Customer;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 顾客表(Customer)表数据库访问层
 *
 * @author: guochenxu
 * @create: 2024-03-20 22:37:48
 * @version: 1.0
 */
@Mapper
public interface CustomerMapper extends BaseMapper<Customer> {
}

