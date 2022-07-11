package com.bx.erp.dao.purchasing;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.bx.erp.dao.BaseMapper;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.purchasing.PurchasingOrder;

@Component("purchasingOrderMapper")
public interface PurchasingOrderMapper extends BaseMapper {
	// public PurchasingOrder create(Map<String, Object> params);
	//
	// public PurchasingOrder update(Map<String, Object> params);

	public PurchasingOrder approve(Map<String, Object> params);

	// public PurchasingOrder retrieveOne(Map<String, Object> params);

	// public List<PurchasingOrder> retrieveN(Map<String, Object> params);

	// public PurchasingOrder delete(Map<String, Object> params);
	public PurchasingOrder updateStatus(Map<String, Object> params);

	public List<BaseModel> retrieveNByFields(Map<String, Object> params);
	
	public List<BaseModel> checkStaffID(Map<String, Object> params);

	public List<BaseModel> checkStatus(Map<String, Object> params);

	public List<BaseModel> checkBarcodesID(Map<String, Object> params);

	public List<BaseModel> checkCommodity(Map<String, Object> params);

	public List<BaseModel> checkPackageUnitID(Map<String, Object> params);
	
	public List<BaseModel> checkPurchasingOrderCommodity(Map<String, Object> params);
}
