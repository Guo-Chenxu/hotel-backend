package com.hotel.server;

import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.annotation.PostConstruct;

@SpringBootApplication
@EnableDiscoveryClient
@EnableDubbo(scanBasePackages = {"com.hotel.server.serviceImpl"})
@EnableMongoAuditing
@EnableAsync
@EnableScheduling
@Slf4j
@EnableSwagger2
public class ServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServerApplication.class, args);
    }

    @PostConstruct
    public void init() {
        log.info("数据初始化中...");
        // todo 初始化后将所有房间数据都从数据库拿到redis中, 房间状态的修改查询均操作redis, 定期从redis同步数据到数据库中
        log.info("所有房间数据已初始化到redis中");
    }
}
