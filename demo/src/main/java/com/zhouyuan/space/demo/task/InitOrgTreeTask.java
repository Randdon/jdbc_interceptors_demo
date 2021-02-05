package com.zhouyuan.space.demo.task;

import com.casic.htzy.log.constant.CommonConstant;
import com.zhouyuan.space.demo.service.SysComService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * @Description: 从消息中心获取Kafka分配的账户信息——程序启动时触发
 * @Author panglinhao
 * @Date 2020/9/23
 **/
@Component
public class InitOrgTreeTask implements ApplicationRunner {

    @Autowired
    private SysComService sysComService;

    private Logger logger = LoggerFactory.getLogger(InitOrgTreeTask.class);

    @Override
    public void run(ApplicationArguments args) {
        logger.info(CommonConstant.LOG_LOCAL_PREFIX + "----初始化组织树----");
        //异步获取Kafka认证账户信息
        sysComService.initOrgTreeToRedis();
    }

}
