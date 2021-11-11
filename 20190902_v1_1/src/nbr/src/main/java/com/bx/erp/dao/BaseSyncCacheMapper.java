package com.bx.erp.dao;

import java.util.Map;

import com.bx.erp.model.BaseModel;

public interface BaseSyncCacheMapper extends BaseMapper {
	public BaseModel deleteAll(Map<String, Object> params);
}
