package com.bx.erp.dao;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.bx.erp.model.BaseModel;

@Component("posMapper")
public interface PosMapper extends BaseMapper {
	public BaseModel retrieve1BySN(Map<String, Object> params);

	public BaseModel reset(Map<String, Object> params);
	
	public BaseModel recycleApp(Map<String, Object> params);
	
	public void checkStatus(Map<String, Object> params);
}
