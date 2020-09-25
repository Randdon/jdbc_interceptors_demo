package com.zhouyuan.space.demo.resultset;

import java.util.List;

import org.json.JSONArray;

public class ListModel extends AbstractUnmarshaller{
	
	public ListModel(JSONArray jsonArray){
		super.setJsonArray(jsonArray);
		super.setJSONString(jsonArray.toString());
	}
	
	public ListModel(){
		
	}
	
	@Override
	public List<Object> populateListModel(Class beanClass) {
		return super.populateListModel(beanClass);
	}




	


	
	
	
	
}
