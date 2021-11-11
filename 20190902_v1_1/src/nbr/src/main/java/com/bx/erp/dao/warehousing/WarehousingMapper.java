package com.bx.erp.dao.warehousing;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.bx.erp.dao.BaseMapper;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.warehousing.Warehousing;

@Component("warehousingMapper")
public interface WarehousingMapper extends BaseMapper{
	public List<Warehousing> retrieve1OrderID(Map<String, Object> params);
	public List<BaseModel> retrieveNByFields(Map<String, Object> params);
	public List<List<BaseModel>> approveEx(Map<String, Object> params);
	public void checkStaffID(Map<String, Object> params);
	public void checkStatus(Map<String, Object> params);
	public void checkProviderID(Map<String, Object> params);
	public void checkWarehouseID(Map<String, Object> params);
	public void checkBarcodesID(Map<String, Object> params);
	public void checkCommodity(Map<String, Object> params);
	public void checkPackageUnitID(Map<String, Object> params);
	public void checkSalableNO(Map<String, Object> params);
	public void checkWarehousingID(Map<String, Object> params);
	public void checkWarehousingCommodity(Map<String, Object> params);
}
