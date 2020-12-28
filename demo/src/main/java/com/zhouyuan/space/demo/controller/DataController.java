package com.zhouyuan.space.demo.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.casic.htzy.log.annotation.NetworkLog;
import com.casic.htzy.log.common.constant.stater.LogActionEnum;
import com.zhouyuan.space.demo.entity.Data;
import com.zhouyuan.space.demo.entity.LogCenter;
import com.zhouyuan.space.demo.service.DataService;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.text.StringEscapeUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
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
    @NetworkLog(description = "http请求测试接口",action = LogActionEnum.CREATE)
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
        Map<String,String> map = new HashMap<>(2);
        String vone = JSON.toJSONString("vone",SerializerFeature.WriteSlashAsSpecial,SerializerFeature.QuoteFieldNames);
        System.out.println(vone);
        map.put("kone",vone);
        map.put("ktwo","vtwo");
        String jsonString = JSON.toJSONString(map, SerializerFeature.WriteMapNullValue, SerializerFeature.WriteSlashAsSpecial);
        System.out.println(jsonString);
        String unescapeJava = StringEscapeUtils.unescapeJava(jsonString);
        System.out.println(unescapeJava);
        String unescapeJson = StringEscapeUtils.unescapeJson(jsonString);
        System.out.println(unescapeJson);
        return unescapeJava;
/*
        Boolean aBoolean = logCenterProperties.getABoolean();
        System.out.println(aBoolean == null);
        return logCenterProperties.getSqlType().toString();
*/
    }

    @GetMapping(value = "/test2")
    public void test() throws IOException {
        // 创建Httpclient对象
        CloseableHttpClient httpclient = HttpClients.createDefault();
        // 创建http POST请求
        HttpPost httpPost = new HttpPost("http://localhost:8048/account/signin/action/");
        // 设置2个post参数，一个是scope、一个是q
        List<NameValuePair> parameters = new ArrayList<NameValuePair>(0);
        parameters.add(new BasicNameValuePair("username", "admin"));
        parameters.add(new BasicNameValuePair("password", "Htkg123."));
        parameters.add(new BasicNameValuePair("ref_url", "/"));

        // 构造一个form表单式的实体
        UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(parameters);
        // 将请求实体设置到httpPost对象中
        httpPost.setEntity(formEntity);
        //伪装浏览器
        httpPost.setHeader("User-Agent",
                "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36");
        CloseableHttpResponse response = null;
        try {
            // 执行请求
            response = httpclient.execute(httpPost);
            // 判断返回状态是否为200
            if (response.getStatusLine().getStatusCode() == 302) {
                //内容写入文件
                InputStream content1 = response.getEntity().getContent();
                FileCopyUtils.copy(content1,new FileOutputStream(new File("D:\\software\\kafka-eagle-web-2.0.3\\logs\\out1.html")));
                Header[] allHeaders = response.getAllHeaders();
                Header kafkaEagleShiroCookie = response.getFirstHeader("Set-Cookie");
                System.out.println(kafkaEagleShiroCookie.getName() + ": " + kafkaEagleShiroCookie.getValue());
                for (Header allHeader : allHeaders) {
                    System.out.println(allHeader.toString());
                }


                HttpGet httpGet = new HttpGet("http://localhost:8048/");
                httpGet.setHeader(kafkaEagleShiroCookie.getName(),kafkaEagleShiroCookie.getValue());
                CloseableHttpResponse httpResponse = httpclient.execute(httpGet);
                StatusLine statusLine = httpResponse.getStatusLine();
                InputStream content = httpResponse.getEntity().getContent();
                FileCopyUtils.copy(content,new FileOutputStream(new File("D:\\software\\kafka-eagle-web-2.0.3\\logs\\out.html")));
                System.out.println(statusLine);
            }

        } catch (IOException exception){
            System.out.println(exception);
        }
        finally {
            if (response != null) {
                response.close();
            }
            httpclient.close();
        }

    }

    @GetMapping(value = "/test3")
    public void test3() throws IOException {
        // 创建Httpclient对象
        CloseableHttpClient httpclient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        try {
            HttpGet httpGet = new HttpGet("http://192.168.12.249/open/query/users_trail?systemId=fad13f3167a80c3895cf2de34-15548b34637e6a-4c4a44d&trailTime=2020-10-24&userId=123456");
            httpGet.setHeader("apikey","EB27E58D-2C26-4425-996A-6D4F4583B4A5");
            CloseableHttpResponse httpResponse = httpclient.execute(httpGet);
            StatusLine statusLine = httpResponse.getStatusLine();
            System.out.println(statusLine);
            HttpEntity entity = httpResponse.getEntity();
            InputStream content = entity.getContent();
            String s = IOUtils.toString(content, Charset.defaultCharset());
            System.out.println(s);
            FileUtils.copyInputStreamToFile(content,new File("E:\\code\\log_center\\docs\\result.txt"));
        } catch (IOException exception){
            System.out.println(exception);
        }
        finally {
            if (response != null) {
                response.close();
            }
            httpclient.close();
        }

    }

}
