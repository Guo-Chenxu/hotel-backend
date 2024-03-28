package com.hotel.server;

import com.hotel.server.config.IndoorTemperatureConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
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
@EnableConfigurationProperties(value = {IndoorTemperatureConfig.class})
public class ServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServerApplication.class, args);
    }

    @PostConstruct
    public void init() {
        // 从配置文件读取室温配置并写入redis
    }

}
