package com.bx.erp.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.bx.erp.model.BaseModel;

@Component("permissionMapper")
public interface PermissionMapper extends BaseMapper {
	public List<BaseModel> retrieveAlsoRoleStaff(Map<String, Object> params);
}
