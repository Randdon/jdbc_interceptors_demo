package com.zhouyuan.space.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.zhouyuan.space.demo")
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
        System.out.println("!!!!!!!!!!!!!!!!!!!系统启动成功！！！！！！！！！！！！！！！！！！！");
    }
}
