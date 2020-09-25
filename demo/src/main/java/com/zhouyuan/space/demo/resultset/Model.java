package com.zhouyuan.space.demo.resultset;
import org.json.JSONArray;




/**
 * 
 * @author masterOpti
 */
public class Model extends  AbstractUnmarshaller {

	
	public Model(JSONArray jsonArray){
		super.setJsonArray(jsonArray);
		super.setJSONString(jsonArray.toString());
	}
	
	public Model(){
		
	}
	
	@Override
	public Object populateModel(Class beanClass) {
		return super.populateModel(beanClass);
	}


	@Override
	public String getJSONString(){
		return super.getJSONString();
	}
}
