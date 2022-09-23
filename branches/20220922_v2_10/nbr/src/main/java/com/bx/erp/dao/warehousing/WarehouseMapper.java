package com.bx.erp.dao.warehousing;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.bx.erp.dao.BaseMapper;
import com.bx.erp.model.BaseModel;

@Component("warehouseMapper")
public interface WarehouseMapper extends BaseMapper {
//	public List<BaseModel> retrieveNByFields(Map<String, Object> params);

	public BaseModel retrieveInventory(Map<String, Object> params);
	
	public void checkStatus(Map<String, Object> params);
}
