package org.ljz.gift;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages = "org.ljz.gift.mapper")
public class APPStart {
    public static void main(String[] args) {
        SpringApplication.run(APPStart.class,args);
    }
}
