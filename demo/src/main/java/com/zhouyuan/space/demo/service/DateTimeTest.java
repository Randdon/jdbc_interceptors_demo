package com.zhouyuan.space.demo.service;

import com.alibaba.fastjson.JSON;
import com.zhouyuan.space.demo.entity.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.TimeZone;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

public class DateTimeTest {

    private static final Logger logger = LoggerFactory.getLogger(DateTimeTest.class);
    public static void main(String[] args) {
        long l = System.currentTimeMillis();
        LocalDateTime dateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(l), TimeZone.getDefault().toZoneId());
        String format = dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"));
        System.out.println(format);
        System.out.println(logger.getName());
        Data data = new Data();
        String str = "cun";
        biConsume(data,str,Data::setDescription);
        Data data1 = new Data("hull", "desc", "jjj", 23);
        System.out.println(JSON.toJSONString(data1));
    }

    private static void biConsume(Data data,String str,BiConsumer<Data,String> consumer){
        consumer.accept(data,str);
        System.out.println(data);
    }


}
