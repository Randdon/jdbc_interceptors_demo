package com.zhouyuan.space.demo.service;

import com.casic.log.annotation.ServiceLog;
import com.zhouyuan.space.demo.entity.Data;
import com.zhouyuan.space.demo.mapper.DataMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class DataService {

    @Autowired
    DataMapper dataMapper;

    public List<Data> listAll(){
        System.out.println("service ThreadName: " + Thread.currentThread().getName());

        return dataMapper.listAll();
    }

    @ServiceLog(description = "查询所有记录",message = "查询不存在的列")
    public List<Data> listAllExp(String name, int id){
        return dataMapper.listAllExp();
    }
    public List<Data> listAllById(){
        return dataMapper.listAllById(3);
    }

    public void updateNameById(Map<String,Object> map){
        dataMapper.updateNameById(map);
    }

    @ServiceLog(description = "保存用户信息",message = "新增用户操作")
    public void save(Data data){
        dataMapper.save(data);
    }

    public void deleteById(int id){
        dataMapper.deleteById(id);
    }
}
