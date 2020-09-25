package com.zhouyuan.space.demo.mapper;

import com.zhouyuan.space.demo.entity.Data;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface DataMapper {

    List<Data> listAll();

    List<Data> listAllExp();

    List<Data> listAllById(@Param("id") Integer id);

    void updateNameById(Map<String,Object> map);

    void save(Data data);

    void deleteById(@Param("id") Integer id);
}
