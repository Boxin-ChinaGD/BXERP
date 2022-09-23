package com.bx.erp.dao;


import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.bx.erp.model.BaseModel;

@Component("shopMapper")
public interface ShopMapper extends BaseMapper{
	public void checkStatus(Map<String, Object> params);
	
	public List<BaseModel> retrieveNByFields(Map<String, Object> params);
}
