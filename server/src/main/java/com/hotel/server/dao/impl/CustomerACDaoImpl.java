package com.hotel.server.dao.impl;


import com.hotel.common.entity.CustomerAC;
import com.hotel.common.entity.CustomerAC;
import com.hotel.common.service.timer.TimerService;
import com.hotel.server.dao.CustomerACDao;
import com.hotel.server.dao.CustomerACDao;
import org.apache.dubbo.config.annotation.DubboReference;
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
public class CustomerACDaoImpl implements CustomerACDao {

    @Resource
    private MongoTemplate mongoTemplate;

    @DubboReference(check = false)
    private TimerService timerService;

    @Override
    public CustomerAC save(CustomerAC customerAC) {
        customerAC.setCreateAt(timerService.getTime());
        return mongoTemplate.save(customerAC);
    }

    @Override
    public List<CustomerAC> selectAll(String userId) {
        Query q = Query.query(Criteria.where("customerId").is(userId));
        return mongoTemplate.find(q, CustomerAC.class);
    }
}
