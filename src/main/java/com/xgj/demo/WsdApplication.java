package com.xgj.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * @author gjXia
 * @date 2021/2/22 14:19
 */
@SpringBootApplication(exclude={DataSourceAutoConfiguration.class}, scanBasePackages = {"com.xgj"})
public class WsdApplication {

    public static void main(String[] args) {
        SpringApplication.run(WsdApplication.class, args);
    }
}
