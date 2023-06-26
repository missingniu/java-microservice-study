package org.ljz.gift;

import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableDiscoveryClient
@MapperScan(basePackages = "org.ljz.gift.mapper")
@EnableFeignClients
public class SystemManageStart {
    public static void main(String[] args) {

        SpringApplication.run(SystemManageStart.class,args);
    }
}
