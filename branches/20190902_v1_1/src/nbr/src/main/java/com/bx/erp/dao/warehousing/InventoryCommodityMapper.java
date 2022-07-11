package com.bx.erp.dao.warehousing;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.bx.erp.dao.BaseMapper;
import com.bx.erp.model.BaseModel;

@Component("inventoryCommodityMapper")
public interface InventoryCommodityMapper extends BaseMapper {
	public BaseModel updateNoReal(Map<String, Object> params);
	
	public void checkInventorySheetID(Map<String, Object> params);
	
	public void checkCommodity(Map<String, Object> params);

	public void checkNOReal(Map<String, Object> params);
}
