package org.ljz.gift;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
@MapperScan("org.ljz.gift.mapper")
public class AuthStart {
    public static void main(String[] args) {
        SpringApplication.run(AuthStart.class,args);
    }
}