package com.zhouyuan.space.demo.service.impl;

import com.zhouyuan.space.demo.service.SysComService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class SysComServiceImpl implements SysComService {
    private Logger logger = LoggerFactory.getLogger(SysComServiceImpl.class);


    @Override
    @Scheduled(cron = "*/5 * * * * ?")
    @Async
    public void initOrgTreeToRedis(){
        logger.info("narutohhhh " + Thread.currentThread().getName() + new Date());
        try {
            // 休眠10s
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

}
