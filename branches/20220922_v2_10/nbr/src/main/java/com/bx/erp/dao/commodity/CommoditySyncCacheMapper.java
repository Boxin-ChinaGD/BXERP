package com.bx.erp.dao.commodity;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.bx.erp.dao.BaseMapper;
import com.bx.erp.model.BaseModel;

@Component("commoditySyncCacheMapper")
public interface CommoditySyncCacheMapper extends BaseMapper{
	public BaseModel deleteSameCache(Map<String, Object> params);
	public BaseModel deleteAll(Map<String, Object> params);
}
