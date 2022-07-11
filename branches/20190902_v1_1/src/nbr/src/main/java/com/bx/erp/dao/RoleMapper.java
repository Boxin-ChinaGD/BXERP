package com.bx.erp.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.bx.erp.model.BaseModel;

@Component("roleMapper")
public interface RoleMapper extends BaseMapper{
	public List<BaseModel> retrieveAlsoStaff(Map<String, Object> params);
}
