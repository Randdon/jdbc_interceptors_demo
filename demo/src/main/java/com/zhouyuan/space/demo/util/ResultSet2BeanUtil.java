package com.zhouyuan.space.demo.util;

import com.zhouyuan.space.demo.resultset.Model;
import com.zhouyuan.space.demo.resultset.ModelType;
import com.zhouyuan.space.demo.resultset.UnmarshallerFactory;

import java.sql.ResultSet;

public class ResultSet2BeanUtil {

    public static String dealWithResultSet(ResultSet resultSet){
        UnmarshallerFactory factory = new UnmarshallerFactory();
        ModelType mType=factory.getModelType(resultSet);
        Model model=mType.getModel();
        String jsonString=model.getJSONString(); //getting json data from model object
        return jsonString;
    }

}
