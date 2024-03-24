package com.hotel.server.dao.impl;


import com.hotel.server.dao.CustomerFoodDao;
import com.hotel.common.entity.CustomerFood;
import com.hotel.common.service.timer.TimerService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * 聊天会话数据库接口实现类
 *
 * @author: 郭晨旭
 * @create: 2023-11-16 12:58
 * @version: 1.0
 */
@Component
public class CustomerFoodDaoImpl implements CustomerFoodDao {

    @Resource
    private MongoTemplate mongoTemplate;

    @DubboReference(check = false)
    private TimerService timerService;

    @Override
    public CustomerFood save(CustomerFood customerFood) {
        customerFood.setCreateAt(timerService.getTime());
        return mongoTemplate.save(customerFood);
    }

    @Override
    public List<CustomerFood> selectAll(String userId) {
        Query q = Query.query(Criteria.where("userId").is(userId));
        return mongoTemplate.find(q, CustomerFood.class);
    }
}
