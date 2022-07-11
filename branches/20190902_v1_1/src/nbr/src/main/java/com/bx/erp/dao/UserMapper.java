package com.bx.erp.dao;

import java.util.Map;
import org.springframework.stereotype.Component;
import com.bx.erp.model.BaseModel;

@Component("userMapper")
public interface UserMapper extends BaseMapper {
	BaseModel login(Map<String, Object> params);
	BaseModel setPassword(Map<String, Object> params);
	BaseModel getPassword(Map<String, Object> params);
}
