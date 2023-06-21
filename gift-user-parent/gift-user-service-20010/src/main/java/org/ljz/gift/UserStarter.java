package org.ljz.gift;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
@MapperScan(basePackages = "org.ljz.gift.mapper")
public class UserStarter {
    public static void main(String[] args) {
        SpringApplication.run(UserStarter.class,args);
    }
}
