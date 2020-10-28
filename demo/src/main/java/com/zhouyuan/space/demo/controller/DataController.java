package com.zhouyuan.space.demo.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.casic.htzy.log.annotation.NetworkLog;
import com.casic.htzy.log.constant.LogActionEnum;
import com.zhouyuan.space.demo.entity.Data;
import com.zhouyuan.space.demo.entity.LogCenter;
import com.zhouyuan.space.demo.service.DataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLConnection;
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
    @NetworkLog(description = "http请求测试接口",action = LogActionEnum.READ)
    public String test(@RequestParam("name") String name, @RequestParam("id") int id) {

        //List<Data> data = dataService.listAll();

        //改
        Map<String, Object> map = new HashMap<>(2);
        map.put("id", 3);
        map.put("name", "zhouyuan");
        dataService.updateNameById(map);

        //增
        Data data1 = new Data("hull", "desc", "jjj", 23, 9);
        Data data2 = new Data("log", "log-desc", "log-json-test", 18, 18);

        LogCenter logCenter = new LogCenter(data2);
        logCenter.setaBoolean(true);
        logCenter.setCheckHealthUrl("Health_url");
        logCenter.setPort("8080");
        logCenter.setServiceName("service");
        logCenter.setSqlType(0);
        logCenter.setSystemName("system");
        logCenter.setUrl("url");
        logCenter.setVersion("0.01");
        dataService.save(data1,logCenter);

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
    public String test(@RequestParam("name") String name, @RequestParam("id") int id, @RequestBody Data data) {
        dataService.save(data, new LogCenter(data));
        return "ok";
    }

    @Autowired
    LogCenter logCenterProperties;

    @GetMapping(value = "/test1")
    @Transactional
    //@HttpLog(description = "http请求测试接口")
    public String test1() {
        Boolean aBoolean = logCenterProperties.getaBoolean();
        System.out.println(aBoolean == null);
        return logCenterProperties.getSqlType().toString();
    }

    @PostMapping(value = "/upload")
    @NetworkLog(action = LogActionEnum.CREATE,description = "upload test")
    public String upload(@RequestParam("name") String name, @RequestParam("id") int id, @RequestParam("file")MultipartFile file) {
        String fileName = file.getName();
        String originalFilename = file.getOriginalFilename();
        String contentType = file.getContentType();
        long size = file.getSize();
        JSONObject object = new JSONObject();
        object.put("fileName",fileName);
        object.put("originalFilename",originalFilename);
        object.put("contenType",contentType);
        object.put("size",size);

        return object.toJSONString();
    }

    @PostMapping(value = "/uploadMulti")
    @NetworkLog(action = LogActionEnum.CREATE,description = "upload test")
    public String uploadMulti(@RequestParam("name") String name, @RequestParam("id") int id, @RequestParam("files")MultipartFile[] files) {
        JSONArray objects = new JSONArray();
        for (MultipartFile file : files) {

            String fileName = file.getName();
            String originalFilename = file.getOriginalFilename();
            String contentType = file.getContentType();
            long size = file.getSize();
            JSONObject object = new JSONObject();
            object.put("fileName",fileName);
            object.put("originalFilename",originalFilename);
            object.put("contenType",contentType);
            object.put("size",size);
            objects.add(object);
        }

        return objects.toJSONString();
    }

    @GetMapping(value = "/download")
    @NetworkLog(action = LogActionEnum.READ,description = "download test")
    public byte[] download(@RequestParam("path") String path) throws IOException {
        File file = new File(path);
        byte[] bytes = FileCopyUtils.copyToByteArray(file);
        return bytes;
    }

    @GetMapping(value = "/downAttachment")
    @NetworkLog(action = LogActionEnum.READ,description = "downAttachment test")
    public void downAttachment(@RequestParam("path") String path, @RequestParam("name") String name, HttpServletResponse response) {
        try {
            File file = new File(path);
            InputStream fis = new BufferedInputStream(new FileInputStream(file));
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            response.setHeader("Content-Disposition", "attachment;filename=" + new String(name.getBytes("utf-8"), "ISO8859-1"));
            response.setHeader("Access-Control-Allow-Origin", "*");
            OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
            String contentType = null;
            try {
                // contentType= Files.probeContentType(path);
                contentType = URLConnection.guessContentTypeFromStream(fis);
            } catch (Exception e) {
                e.printStackTrace();
            }
            response.setContentType(contentType);
            if (contentType == null) {
                response.setContentType("application/x-msdownload");
            }
            try {
                toClient.write(buffer);
                toClient.flush();
                toClient.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        response.setContentType("text/html;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
    }

}
