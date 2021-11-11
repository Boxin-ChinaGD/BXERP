package com.bx.erp.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.bx.erp.model.BaseModel;

@Component("retailTradeMapper")
public interface RetailTradeMapper extends BaseMapper {

	public void checkAmount(Map<String, Object> params);
	
	public void checkPaymentType(Map<String, Object> params);
	
	public void checkVipID(Map<String, Object> params);
	
	public void checkStatus(Map<String, Object> params);
	
	public void checkSmallSheetID(Map<String, Object> params);
	
	public void checkRetailTradeCommodity(Map<String, Object> params);
	
	public List<BaseModel> retrieveNByCommodityNameInTimeRange(Map<String, Object> params);
}
