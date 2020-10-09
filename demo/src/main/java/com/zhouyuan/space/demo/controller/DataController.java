package com.zhouyuan.space.demo.controller;

import com.zhouyuan.space.demo.entity.Data;
import com.zhouyuan.space.demo.entity.LogCenter;
import com.zhouyuan.space.demo.service.DataService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("/data")
@RestController
public class DataController {

    @Autowired
    DataService dataService;

    @GetMapping(value = "/test")
    @Transactional
    //@HttpLog(description = "http请求测试接口")
    public String test(@Param("name") String name, @Param("id") int id) {

        //List<Data> data = dataService.listAll();

        //改
        Map<String, Object> map = new HashMap<>(2);
        map.put("id", 3);
        map.put("name", "zhouyuan");
        dataService.updateNameById(map);

        //增
        dataService.save(new Data("hull", "desc", "jjj", 23, 10));

        //删
        dataService.deleteById(5);

        //查
        List<Data> data = dataService.listAll();
        System.out.println("controller ThreadName: " + Thread.currentThread().getName());

        //异常查
        dataService.listAllExp(name, id);
        System.out.println(data.get(0));
        return "ok";
    }

    @PostMapping(value = "/post")
    @Transactional
    public String test(@Param("name") String name, @Param("id") int id, @RequestBody Data data) {
        dataService.save(data);
        return "ok";
    }

    @Autowired
    LogCenter logCenterProperties;

    @GetMapping(value = "/test1")
    @Transactional
    //@HttpLog(description = "http请求测试接口")
    public String test1() {
        Boolean aBoolean = logCenterProperties.getABoolean();
        System.out.println(aBoolean == null);
        return logCenterProperties.getSqlType().toString();
    }
}
