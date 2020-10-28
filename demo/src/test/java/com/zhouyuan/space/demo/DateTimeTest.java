package com.zhouyuan.space.demo;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.apache.commons.text.StringEscapeUtils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

public class DateTimeTest {

    public static void main(String[] args) {
        Map<String, String> map = new HashMap<>(2);
        String vone = "vone";
        System.out.println(vone);
        map.put("kone", vone);
        map.put("ktwo", "vtwo");
        String jsonString = JSON.toJSONString(map, SerializerFeature.WriteMapNullValue, SerializerFeature.WriteSlashAsSpecial);
        System.out.println(jsonString);
        String unescapeJava = StringEscapeUtils.unescapeJava(jsonString);
        System.out.println(unescapeJava);
        String unescapeJson = StringEscapeUtils.unescapeJson(jsonString);
        System.out.println(unescapeJson);
    }
}
