package com.hotel.customer.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.hotel.common.service.timer.TimerService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class MPDateConfig implements MetaObjectHandler {

    @DubboReference(check = false)
    private TimerService timerService;

    /**
     * 使用mp做添加操作时候，这个方法执行
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        //设置属性值
        this.setFieldValByName("createdAt", timerService.getTime(), metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
//        this.setFieldValByName("updateTime",new Date(),metaObject);
    }
}