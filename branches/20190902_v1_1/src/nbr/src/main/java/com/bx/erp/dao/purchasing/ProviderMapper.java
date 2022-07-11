package com.bx.erp.dao.purchasing;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.bx.erp.dao.BaseMapper;
import com.bx.erp.model.BaseModel;

@Component("providerMapper")
public interface ProviderMapper extends BaseMapper {
	public List<BaseModel> retrieveNByFields(Map<String, Object> params);
}
