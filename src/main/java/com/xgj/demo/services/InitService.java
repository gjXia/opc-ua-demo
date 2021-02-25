package com.xgj.demo.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 * @author gjXia
 * @date 2021/2/23 9:49
 */
@Slf4j
@Component
public class InitService implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private OpcService opcService;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        log.info("spring容器初始化完成，业务数据初始化开始");

        opcService.init();

        log.info("spring容器初始化完成，业务数据初始化结束");
    }
}
