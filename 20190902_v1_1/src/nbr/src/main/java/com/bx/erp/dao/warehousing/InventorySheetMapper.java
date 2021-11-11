package com.bx.erp.dao.warehousing;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.bx.erp.dao.BaseMapper;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.warehousing.InventorySheet;

@Component("inventorySheetMapper")
public interface InventorySheetMapper extends BaseMapper {
	public List<List<BaseModel>> approveEx(Map<String, Object> params);

	public List<BaseModel> retrieveNByFields(Map<String, Object> params);

	public InventorySheet submit(Map<String, Object> params);
	
	public void checkStaffID(Map<String, Object> params);
	
	public void checkStatus(Map<String, Object> params);
	
	public void checkWarehouseID(Map<String, Object> params);
	
	public void checkInventoryCommodtiy(Map<String, Object> params);
}
