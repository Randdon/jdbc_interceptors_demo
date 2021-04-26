package com.zhouyuan.space.demo.service;

import com.casic.htzy.log.config.CustomThreadPoolTaskExecutor;
import com.zhouyuan.space.demo.util.PropertiesUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DataServiceTest {

    @Test
    public void concurrencyTest(){
        int coreSize = Runtime.getRuntime().availableProcessors();
        LinkedBlockingQueue<Runnable> queue = new LinkedBlockingQueue<>(10);
        ThreadPoolExecutor executor = new ThreadPoolExecutor(1,1,60,
                TimeUnit.SECONDS,queue,new ThreadPoolExecutor.DiscardOldestPolicy());
        for (int i = 0; i < 20; i++) {
            executor.execute(() -> System.out.println(Thread.currentThread().getName() + " " +
                    executor.getPoolSize() + " " + queue.size()));
        }
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static final Logger myLogger = LoggerFactory.getLogger("yuandong");

    @Test
    public void logBackTest(){
        myLogger.info("This is my Logger Info!!!!");
    }

    @Test
    public void propertiesReadAndWriteConcurrencyTest(){
        String filePath = "H:\\temp\\test.properties";
        for (int i = 0; i < 1000; i++) {
            new Thread(() -> {
                PropertiesUtils.versionPlus(filePath);
                myLogger.info("ThreadName:{},getVersion:{}",Thread.currentThread().getName(),
                        PropertiesUtils.getVersion(filePath));
            }).start();
        }
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(PropertiesUtils.getVersion(filePath));
    }
}